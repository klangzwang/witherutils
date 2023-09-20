package geni.witherutils.base.common.block.floodgate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.base.common.base.IInteractBlockEntity;
import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.fluid.MachineFluidHandler;
import geni.witherutils.base.common.io.fluid.WitherFluidTank;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.FluidStackDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.BlockUtil;
import geni.witherutils.core.common.util.FluidsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class FloodgateBlockEntity extends WitherMachineFakeBlockEntity implements MenuProvider, IInteractBlockEntity {
	
	public static final int CAPACITY = 1 * FluidType.BUCKET_VOLUME;
	
    private final WitherFluidTank fluidTank;
    private final MachineFluidHandler fluidHandler;
    
    private static final int[] REBUILD_DELAYS = { 16, 32, 64, 128, 256 };
    
    private int delayIndex = 0;
    private int tick = 0;
    
    private boolean working;
    
    public final Deque<BlockPos> queue = new ArrayDeque<>();
    private final Map<BlockPos, List<BlockPos>> paths = new HashMap<>();
    
    private boolean preview;
    private int scaleX = 1;
    private int scaleY = 1;
    private int scaleZ = 1;
    
    private boolean opened = false;
    private int timer;
    
    public FloodgateBlockEntity(BlockPos pos, BlockState state)
    {
        super(WUTEntities.FLOODGATE.get(), pos, state);

        this.fluidTank = createFluidTank(CAPACITY);
        this.fluidHandler = createFluidHandler(CAPACITY);
        
        addCapabilityProvider(fluidHandler);

        addDataSlot(new FluidStackDataSlot(getFluidTank()::getFluid, getFluidTank()::setFluid, SyncMode.WORLD));
        add2WayDataSlot(new FluidStackDataSlot(fluidTank::getFluid, fluidTank::setFluid, SyncMode.GUI));
        
        addDataSlot(new BooleanDataSlot(this::getWorking, p -> working = p, SyncMode.WORLD));
        
        add2WayDataSlot(new BooleanDataSlot(this::getPreview, p -> preview = p, SyncMode.GUI));
        add2WayDataSlot(new IntegerDataSlot(this::getScaleX, p -> scaleX = p, SyncMode.GUI));
        add2WayDataSlot(new IntegerDataSlot(this::getScaleY, p -> scaleY = p, SyncMode.GUI));
        add2WayDataSlot(new IntegerDataSlot(this::getScaleZ, p -> scaleZ = p, SyncMode.GUI));
    }

    private MachineFluidHandler createFluidHandler(int capacity)
    {
        return new MachineFluidHandler(getIOConfig(), fluidTank)
        {
        	@Override
        	public LazyOptional<IFluidHandler> getCapability(@Nullable Direction side)
        	{
            	if(side != Direction.UP)
            		return LazyOptional.empty();
            	else
            		return super.getCapability(side);
        	}
        };
    }
    
    private WitherFluidTank createFluidTank(int capacity)
    {
        return new WitherFluidTank(this, capacity)
        {
            @Override
            protected void onContentsChanged()
            {
            	onTankContentsChanged();
                super.onContentsChanged();
                setChanged();
            }
        };
    }
    
    @Override
    public void serverTick()
    {
    	super.serverTick();

    	if(fakePlayer == null)
            this.fakePlayer = initFakePlayer((ServerLevel) level,
            ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath(), this);

        if (fluidTank.getFluidAmount() < CAPACITY)
            return;
        
        tick++;
        working = false;
        if (tick % 16 == 0)
        {
            if (!fluidTank.isEmpty() && !queue.isEmpty())
            {
                if (fluidTank.getFluid() != null && fluidTank.getFluidAmount() >= FluidType.BUCKET_VOLUME)
                {
                    BlockPos currentPos = queue.removeLast();
                    List<BlockPos> path = paths.get(currentPos);
                    boolean canFill = true;
                    if (path != null)
                    {
                        for (BlockPos p : path)
                        {
                            if (p.equals(currentPos))
                            {
                                continue;
                            }
                            if (!canFillThrough(currentPos))
                            {
                                canFill = false;
                                break;
                            }
                        }
                    }
                    if (canFill && canFill(currentPos))
                    {
                        if (FluidUtil.tryPlaceFluid(fakePlayer.get(), level, InteractionHand.MAIN_HAND, currentPos, fluidTank, fluidTank.getFluid()))
                        {
                        	working = true;
                            for (Direction side : Direction.values())
                            	level.updateNeighborsAtExceptFromFacing(this.getBlockPos(), this.getBlockState().getBlock(), side);
                            delayIndex = 0;
                            tick = 0;
                        }
                    }
                    else
                    {
                        buildQueue();
                    }
                }
            }
        }

        if (queue.isEmpty() && tick >= getCurrentDelay())
        {
        	delayIndex = Math.min(delayIndex + 1, REBUILD_DELAYS.length - 1);
            tick = 0;
            buildQueue();
        }
    }
    
    @Override
    public void clientTick()
    {
    	super.clientTick();
    	
		opened = true;
    	
    	if(this.timer < 200)
    		this.timer += 5;
    	else if(this.timer >= 200)
    	{
    		this.timer = 200;
    	}

    	if(working && fluidTank.getFluidAmount() > 0)
    	{
    		if(level.getBlockState(worldPosition.below()).isAir())
    		{
        		for(int i = 0; i < 16; i++)
        		{
        			level.addParticle(ParticleTypes.FALLING_WATER,
        					getBlockPos().getX() + 0.5D + (level.random.nextDouble() - 0.5D),
        					getBlockPos().getY(),
        					getBlockPos().getZ() + 0.5D + (level.random.nextDouble() - 0.5D),
            				0.0D, -0.02D, 0.0D);
        		}
    		}
    	}
    }
    
    @Override
    public InteractionResult onBlockUse(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!level.isClientSide)
        {
        	NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) this, getBlockPos());
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new FloodgateContainer(this, playerInventory, i);
    }
    
    private int getCurrentDelay()
    {
        return REBUILD_DELAYS[delayIndex];
    }
    
    private void buildQueue()
    {
        FluidStack fluid = fluidTank.getFluid();
        if (fluid == null || fluid.getAmount() <= 0)
        {
            return;
        }
        
        Set<BlockPos> checked = new HashSet<>();
        checked.add(worldPosition);
        List<BlockPos> nextPosesToCheck = new ArrayList<>();

        for (BlockPos toCheck : cubeSquare())
        {
        	nextPosesToCheck.add(toCheck);
        }
        outer: while (!nextPosesToCheck.isEmpty())
        {
            List<BlockPos> nextPosesToCheckCopy = new ArrayList<>(nextPosesToCheck);
            nextPosesToCheck.clear();

            for (BlockPos toCheck : nextPosesToCheckCopy)
            {
                if (checked.add(toCheck)) {
                    if (canSearch(toCheck)) {
                        if (canFill(toCheck)) {
                            queue.push(toCheck);
                            if (queue.size() >= 4096) {
                                break outer;
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tag.putBoolean("preview", preview);
		tag.putInt("scaleX", scaleX);
		tag.putInt("scaleY", scaleY);
		tag.putInt("scaleZ", scaleZ);
		tag.put("fluid", fluidTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        preview = tag.getBoolean("preview");
        scaleX = tag.getInt("scaleX");
        scaleY = tag.getInt("scaleY");
        scaleZ = tag.getInt("scaleZ");
        fluidTank.readFromNBT(tag.getCompound("fluid"));
    }
    
    public FluidTank getFluidTank()
    {
        return fluidTank;
    }
    
    private void onTankContentsChanged()
    {
        if (level != null)
        {
            if (!level.isClientSide())
            {
            }
        }
    }
    
    public boolean isOpened()
    {
        return opened;
    }

    public int getTimer()
    {
        return timer;
    }
    
    private boolean canFill(BlockPos offsetPos)
    {
        if (level.getBlockState(offsetPos).isAir())
            return true;
        
        Fluid fluid = BlockUtil.getFluidWithFlowing(level, offsetPos);
        return fluid != null && FluidsUtil.areFluidsEqual(fluid, fluidTank.getFluid().getFluid())
            && BlockUtil.getFluidWithoutFlowing(level.getBlockState(offsetPos)) == null;
    }
    private boolean canSearch(BlockPos offsetPos)
    {
        if (canFill(offsetPos))
        {
            return true;
        }
        Fluid fluid = BlockUtil.getFluid(level, offsetPos);
        return FluidsUtil.areFluidsEqual(fluid, fluidTank.getFluid().getFluid());
    }
    private boolean canFillThrough(BlockPos pos)
    {
        if (level.getBlockState(pos).isAir())
        {
            return false;
        }
        Fluid fluid = BlockUtil.getFluidWithFlowing(level, pos);
        return FluidsUtil.areFluidsEqual(fluid, fluidTank.getFluid().getFluid());
    }
    
	public void updateBlock()
	{
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

    /*
     * 
     * AABB
     * 
     */
    public boolean getPreview()
    {
    	return preview;
    }
    public void setPreview(boolean preview)
    {
    	this.preview = preview;
    }
    
    public int getScaleX()
    {
    	return scaleX;
    }
    public void setScaleX(int scaleX)
    {
    	this.scaleX = scaleX;
    }
    
    public int getScaleY()
    {
    	return scaleY;
    }
    public void setScaleY(int scaleY)
    {
    	this.scaleY = scaleY;
    }
    
    public int getScaleZ()
    {
    	return scaleZ;
    }
    public void setScaleZ(int scaleZ)
    {
    	this.scaleZ = scaleZ;
    }
	
    public boolean getWorking()
    {
    	return working;
    }
    
	@OnlyIn(Dist.CLIENT)
	public AABB getAABBForRender()
	{
		AABB thebox = new AABB(-scaleX + 1, -scaleY, -scaleZ + 1, scaleX, 0, scaleZ);
		return thebox;
	}
	public AABB getAABB()
	{
		AABB thebox = new AABB(-scaleX + 1, -scaleY, -scaleZ + 1, scaleX, 0, scaleZ);
		return thebox;
	}
	public List<BlockPos> cubeSquare()
	{
		List<BlockPos> shape = new ArrayList<BlockPos>();

		int xMin = (int) getAABB().minX;
		int yMin = (int) getAABB().minY;
		int zMin = (int) getAABB().minZ;
		int xMax = (int) getAABB().maxX;
		int yMax = (int) getAABB().maxY;
		int zMax = (int) getAABB().maxZ;

		for (int x = xMin; x <= xMax - 1; x++) {
			for (int z = zMin; z <= zMax - 1; z++) {
				for (int y = yMin; y <= yMax - 1; y++) {
					shape.add(new BlockPos(getBlockPos().getX() + x, getBlockPos().getY() + y, getBlockPos().getZ() + z));
				}
			}
		}
		return shape;
	}

	public int getOffsetAxis(Axis axis)
	{
		int scaling = 0;
		switch(axis)
		{
			case X :
				scaling = scaleX;
				break;
			case Y :
				scaling = scaleY;
				break;
			case Z :
				scaling = scaleZ;
				break;
			default :
				break;
		}
		return scaling;
	}
    public void setOffsetAxis(Axis axis, int scaling)
    {
    	switch(axis)
    	{
			case X :
		    	this.scaleX = scaling;
				break;
			case Y :
		    	this.scaleY = scaling;
				break;
			case Z :
		    	this.scaleZ = scaling;
				break;
			default :
				break;
    	}
    }
    
	/*
	 * 
	 * FAKEPLAYER
	 * 
	 */
	@Override
	public void resetFakeStamina()
	{
	}
}
