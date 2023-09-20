package geni.witherutils.base.common.block.battery.core;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.FixedScalable;
import geni.witherutils.base.common.base.IMultiBlockPart;
import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
import geni.witherutils.base.common.block.battery.stab.StabBlockEntity;
import geni.witherutils.base.common.config.common.BatteryConfig;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.FacingUtil;
import geni.witherutils.core.common.util.ShapeUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public class CoreBlockEntity extends WitherMachineEnergyBlockEntity implements MenuProvider, IMultiBlockPart {

    public static final FixedScalable CAPACITY = new FixedScalable(() -> (float) BatteryConfig.CORECAPACITY.get());
    public static final FixedScalable TRANSFER = new FixedScalable(() -> 5000f);
    public static final FixedScalable USAGE = new FixedScalable(() -> 0f); // not in use
    
	public boolean stabilizersValid;
    public BlockPos[] stabilizerPositions = new BlockPos[4];

	public boolean active;
	private boolean showBuildGuide = true;
    public float rotation = 0;

    private List<BlockPos> controllerPos = new ArrayList<>();
    private List<BlockPos> steelPos = new ArrayList<>();
    private List<BlockPos> coilPos = new ArrayList<>();
	
    public CoreBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(EnergyIOMode.Both, CAPACITY, TRANSFER, USAGE, WUTEntities.CORE.get(), worldPosition, blockState);
        addDataSlot(new BooleanDataSlot(this::isShowingBuildGuide, p -> showBuildGuide = p, SyncMode.GUI));
    }

    @Override
    public void serverTick()
    {
        super.serverTick();
        
        crossTick();
    }
    
    @Override
    public void clientTick()
    {
        super.clientTick();
        
        crossTick();
        
        if(!active)
        	return;
        
        List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(worldPosition, worldPosition.offset(1, 1, 1)).inflate(10, 10, 10));
        for (Player player : players)
        {
            double dist = Vec3D.getCenter(this).distance(new Vec3D(player));
            double distNext = new Vec3D(player).distance(new Vec3D(worldPosition.getX() + player.getDeltaMovement().x + 0.5, worldPosition.getY() + player.getDeltaMovement().y - 0.4, worldPosition.getZ() + player.getDeltaMovement().z + 0.5));
            double threshold = 5;
            double boundary = distNext - threshold;
            double dir = dist - distNext;

            if (boundary <= 0)
            {
                if (dir < 0)
                {
                    player.move(MoverType.PLAYER, new Vec3(-player.getDeltaMovement().x * 1.5, -player.getDeltaMovement().y * 1.5, -player.getDeltaMovement().z * 1.5));
                }
                double multiplier = (threshold - dist) * 0.05;
                double xm = ((worldPosition.getX() + 0.5 - player.getX()) / distNext) * multiplier;
                double ym = ((worldPosition.getY() - 0.4 - player.getY()) / distNext) * multiplier;
                double zm = ((worldPosition.getZ() + 0.5 - player.getZ()) / distNext) * multiplier;
                player.move(MoverType.PLAYER, new Vec3(-xm, -ym, -zm));
            }
        }
    }
    
    public void crossTick()
    {
    	stabilizersValid = isStabValid();

    	if(controllerPos.isEmpty())
    		controllerPos = getControllerPositions(worldPosition);
    	if(coilPos.isEmpty())
    		coilPos = getCoilPositions(worldPosition);
    	if(steelPos.isEmpty())
    		steelPos = getSteelPositions(worldPosition);

    	active = stabilizersValid && isControllerValid() && isCoilValid() && isSteelValid();
        for (Direction facing : Direction.values())
        {
        	BlockPos offset = worldPosition.relative(facing, 2);
        	if(!level.isEmptyBlock(offset))
        		active = false;
        }
        if(active)
        {
        	rotation++;
        }
    }
    
    public boolean isShowingBuildGuide()
    {
    	return showBuildGuide;
    }
    
    public void setShowingBuildGuide(boolean bgshow)
    {
    	this.showBuildGuide = bgshow;
    }
    
    public boolean isActive()
    {
    	return active;
    }
    
    public void setActive(boolean active)
    {
    	this.active = active;
    }
    
    @Override
    public InteractionResult handleRemoteClick(Player player, InteractionHand hand, BlockHitResult hit)
    {
		if(!level.isClientSide)
		{
			BlockEntity tileEntity = level.getBlockEntity(worldPosition);
			if(tileEntity instanceof MenuProvider)
			{
				NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
                SoundUtil.playSoundFromServer((ServerPlayer) player, WUTSounds.ELECTRO.get(), 0.4f, 1.0f);
			}
			else
			{
				throw new IllegalStateException("Our named container provider is missing!");
			}
		}
		return InteractionResult.SUCCESS;
    }

    public boolean isStabValid()
    {
        boolean valid = true;
        if (stabilizersValid)
        {
            for (BlockPos offset : stabilizerPositions)
            {
                if (offset == null)
                {
                    valid = false;
                }
                else
                {
                    BlockPos tilePos = worldPosition.subtract(offset);
                    BlockEntity tile = level.getBlockEntity(tilePos);
                    if (tile instanceof StabBlockEntity stabilizer)
                    {
                    }
                    else
                    {
                        valid = false;
                    }
                }
                if (!valid)
                {
                    break;
                }
            }
            if (!valid)
            {
                stabilizersValid = false;
            }
        }
        else
        {
            for (Direction.Axis axis : Direction.Axis.VALUES)
            {
                Direction[] dirs = FacingUtil.getFacingsAroundAxis(axis);
                List<StabBlockEntity> found = new ArrayList<>();

                for (int fIndex = 0; fIndex < dirs.length; fIndex++)
                {
                    Direction facing = dirs[fIndex];
                    for (int dist = 1; dist < 16; dist++)
                    {
                        BlockPos testPos = worldPosition.offset(facing.getStepX() * dist, facing.getStepY() * dist, facing.getStepZ() * dist);
                        BlockEntity tile = level.getBlockEntity(testPos);
                        if (!(tile instanceof StabBlockEntity stabilizer))
                        {
                            continue;
                        }
                        found.add(stabilizer);
                        break;
                    }
                }
                if (found.size() == 4)
                {
                    for (StabBlockEntity stab : found)
                    {
                        stabilizerPositions[found.indexOf(stab)] = new BlockPos(worldPosition.getX() - stab.getBlockPos().getX(), worldPosition.getY() - stab.getBlockPos().getY(), worldPosition.getZ() - stab.getBlockPos().getZ());
                    }
                    stabilizersValid = true;
                    break;
                }
            }
            if (!stabilizersValid)
            {
                valid = false;
            }
        }
        return valid;
    }

    public boolean isControllerValid()
    {
    	int howMany = 0;
    	for(BlockPos pos : getControllerList())
    	{
    		if(level.getBlockState(pos).getBlock() == Blocks.COAL_BLOCK)
    			howMany = howMany + 1;
    	}
   		return howMany == 20;
    }
    public boolean isCoilValid()
    {
    	int howMany = 0;
    	for(BlockPos pos : getCoilList())
    	{
    		if(level.getBlockState(pos).getBlock() == Blocks.REDSTONE_BLOCK)
    			howMany = howMany + 1;
    	}
   		return howMany == 6;
    }
    public boolean isSteelValid()
    {
    	int howMany = 0;
    	for(BlockPos pos : getSteelList())
    	{
    		if(level.getBlockState(pos).getBlock() == WUTBlocks.WITHERSTEEL_BLOCK.get())
    			howMany = howMany + 1;
    	}
   		return howMany == 48;
    }
    
    public List<BlockPos> getControllerPositions(BlockPos corePos)
    {
		List<BlockPos> controllerBlockPos = new ArrayList<>();
      	for(BlockPos pos : ShapeUtil.cubeSquareBase(corePos.below(), 1, 2))
      	{
      		if(pos != this.worldPosition)
      			controllerBlockPos.add(pos);
      	}
        for (Direction facing : Direction.values())
        {
        	BlockPos coilPos = worldPosition.relative(facing, 1);
        	controllerBlockPos.remove(coilPos);
        }
      	return controllerBlockPos;
    }
    public List<BlockPos> getCoilPositions(BlockPos corePos)
    {
		List<BlockPos> coilBlockPos = new ArrayList<>();
        for (Direction facing : Direction.values())
        {
        	BlockPos coilPos = worldPosition.relative(facing.getOpposite(), 1);
        	coilBlockPos.add(coilPos);
        }
      	return coilBlockPos;
    }
    public List<BlockPos> getSteelPositions(BlockPos corePos)
    {
    	List<BlockPos> witherSteelPos = new ArrayList<>();

        for (Direction facing : Direction.values())
        {
        	if(facing == Direction.UP || facing == Direction.DOWN)
        	{
            	for (BlockPos aroundPos : FacingUtil.getAroundAxis(Axis.Y))
            	{
                	BlockPos offset = aroundPos.relative(facing, 2);
                	BlockPos steelPos = new BlockPos(worldPosition.offset(offset));

                	witherSteelPos.add(steelPos);
            	}
        	}
        	else
        	{
            	if(facing == Direction.EAST || facing == Direction.WEST)
            	{
                	for (BlockPos aroundPos : FacingUtil.getAroundAxis(Axis.X))
                	{
                    	BlockPos offset = aroundPos.relative(facing, 2);
                    	BlockPos steelPos = new BlockPos(worldPosition.offset(offset));

                    	witherSteelPos.add(steelPos);
                	}
            	}
            	else if(facing == Direction.NORTH || facing == Direction.SOUTH)
            	{
                	for (BlockPos aroundPos : FacingUtil.getAroundAxis(Axis.Z))
                	{
                    	BlockPos offset = aroundPos.relative(facing, 2);
                    	BlockPos steelPos = new BlockPos(worldPosition.offset(offset));

                    	witherSteelPos.add(steelPos);
                	}
            	}
        	}
        }
      	return witherSteelPos;
    }

	public List<BlockPos> getControllerList()
	{
		return controllerPos;
	}
	public List<BlockPos> getCoilList()
	{
		return coilPos;
	}
	public List<BlockPos> getSteelList()
	{
		return steelPos;
	}
    
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
	{
        return new CoreContainer(this, playerInventory, i);
	}
	
	@Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return CoreBlockEntity.INFINITE_EXTENT_AABB;
    }
}
