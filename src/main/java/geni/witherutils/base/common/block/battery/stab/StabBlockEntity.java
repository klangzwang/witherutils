package geni.witherutils.base.common.block.battery.stab;

import java.util.Objects;

import javax.annotation.Nullable;

import geni.witherutils.base.common.base.IInteractBlockEntity;
import geni.witherutils.base.common.base.IMultiBlockPart;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.battery.core.CoreBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.particle.IntParticleType;
import geni.witherutils.core.common.util.McTimerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StabBlockEntity extends WitherMachineBlockEntity implements IMultiBlockPart, IInteractBlockEntity {

    public CoreBlockEntity core = null;
    public Direction coreDirection = Direction.DOWN;
    public BlockPos coreOffset;
    
    public StabBlockEntity(BlockPos pos, BlockState state)
    {
		super(WUTEntities.STAB.get(), pos, state);
    }

    @Override
    public void serverTick()
    {
    	super.serverTick();
    	
    	if(findCore() == null)
    		return;
    	
        if(core == null)
        {
        	core = findCore();
        }
    }
    
    @Override
    public void clientTick()
    {
    	super.clientTick();

    	if(findCore() == null)
    		return;
    	
        if(core == null)
        {
        	core = findCore();
        }

        setLitProperty(core.active);
        
    	if(coreOffset != null && core.active)
    	{
    		updateVisual();
    	}
    }
    
    public CoreBlockEntity findCore()
    {
        if (getCore() != null)
            return getCore();

        for (Direction facing : Direction.values())
        {
            for (int i = 0; i < 16; i++)
            {
                BlockEntity tile = level.getBlockEntity(worldPosition.offset(facing.getStepX() * i, facing.getStepY() * i, facing.getStepZ() * i));
                if (tile instanceof CoreBlockEntity)
                {
                	CoreBlockEntity core = (CoreBlockEntity) tile;
                	setCore(core);
                    return core;
                }
            }
        }
        return null;
    }
    public CoreBlockEntity getCore()
    {
        BlockPos corePos = getCorePos();
        if (corePos != null)
        {
            BlockEntity tile = level.getBlockEntity(corePos);
            if (tile instanceof CoreBlockEntity)
            {
                return (CoreBlockEntity) tile;
            }
            else
            {
                coreOffset = null;
            }
        }
        return null;
    }
    @Nullable
    private BlockPos getCorePos()
    {
        return coreOffset == null ? null : worldPosition.subtract(Objects.requireNonNull(coreOffset));
    }
    
    @Override
    public InteractionResult handleRemoteClick(Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        CoreBlockEntity core = getCore();
        if (core == null)
        {
            core = findCore();
        }
        if (core != null)
        {
            core.handleRemoteClick(player, hand, hit);
        }
        else
        {
            player.sendSystemMessage(Component.translatable("CORE NOT FOUND").withStyle(ChatFormatting.DARK_RED));
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public InteractionResult onBlockUse(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return handleRemoteClick(player, hand, hit);
    }
    
    public void setCore(@Nullable CoreBlockEntity core)
    {
        if (core == null)
        {
            coreOffset = null;
            return;
        }
        BlockPos offset = worldPosition.subtract(core.getBlockPos());
        coreOffset = offset;
        coreDirection = Direction.getNearest(offset.getX(), offset.getY(), offset.getZ()).getOpposite();
    }
    
    @OnlyIn(Dist.CLIENT)
    private void updateVisual()
    {
        Vec3D spawn = new Vec3D(worldPosition);
        spawn.add(0.0, 0.5, 0.0);
        
        double rand = level.random.nextInt(100) / 12D;
        double randOffset = rand * (Math.PI * 2D);
        
        double offsetX = Math.sin((McTimerUtil.clientTimer / 180D * Math.PI) + randOffset);
        double offsetY = Math.cos((McTimerUtil.clientTimer / 180D * Math.PI) + randOffset);

        if (level.random.nextBoolean())
        {
            if (coreDirection.getAxis() == Direction.Axis.Z)
            {
                spawn.add(offsetX * 1.1, offsetY * 1.1, (level.random.nextBoolean() ? -0.38 : 0.38) * 1);
            }
            else if (coreDirection.getAxis() == Direction.Axis.Y)
            {
                spawn.add(offsetX * 1.1, (level.random.nextBoolean() ? -0.38 : 0.38) * 1, offsetY * 1.1);
            }
            else if (coreDirection.getAxis() == Direction.Axis.X)
            {
                spawn.add((level.random.nextBoolean() ? -0.38 : 0.38) * 1, offsetY * 1.1, offsetX * 1.1);
            }
            Vector3 target = Vector3.fromBlockPosCenter(worldPosition).subtract(coreOffset);
            level.addParticle(new IntParticleType.IntParticleData(WUTParticles.ENERGY_CORE.get(), 1, (int) (randOffset * 100D), core != null ? 1 : 0), spawn.x, spawn.y, spawn.z, target.x, target.y, target.z);
        }
        else
        {
            if (coreDirection.getAxis() == Direction.Axis.Z)
            {
                spawn.add(offsetX * 1.2, offsetY * 1.2, level.random.nextBoolean() ? -0.38 : 0.38);
            }
            else if (coreDirection.getAxis() == Direction.Axis.Y)
            {
                spawn.add(offsetX * 1.2, level.random.nextBoolean() ? -0.38 : 0.38, offsetY * 1.2);
            }
            else if (coreDirection.getAxis() == Direction.Axis.X)
            {
                spawn.add(level.random.nextBoolean() ? -0.38 : 0.38, offsetY * 1.2, offsetX * 1.2);
            }
            Vector3 target = Vector3.fromBlockPosCenter(worldPosition);
            level.addParticle(new IntParticleType.IntParticleData(WUTParticles.ENERGY_CORE.get(), 0), spawn.x, spawn.y, spawn.z, target.x, target.y, target.z);
        }
    }
	
	@Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return StabBlockEntity.INFINITE_EXTENT_AABB;
    }
}

