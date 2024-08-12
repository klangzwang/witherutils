package geni.witherutils.base.common.block.xpdrain;

import java.util.List;

import geni.witherutils.base.common.base.WitherMachineUpgradeBlockEntity;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.core.common.helper.ExperienceHelper;
import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.network.NetworkDataSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class XpDrainBlockEntity extends WitherMachineUpgradeBlockEntity {

    private int particleRate;
    private Player capturedPlayer;
	
    public XpDrainBlockEntity(BlockPos pos, BlockState state)
    {
        super(WUTBlockEntityTypes.XPDRAIN.get(), pos, state, 1);
		addDataSlot(NetworkDataSlot.INT.create(this::getParticleRate, p -> particleRate = p));
    }
    
	@Override
	public void serverTick()
	{
		super.serverTick();
		
//		getPlayers(wireless);
		
		if((particleRate > 1 || (particleRate > 0 && level.random.nextInt(2) == 0)))
			particleRate = particleRate -2;
		
//	    IFluidHandler handler = CapabilityUtil.getFluidHandler(level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(XpWirelessBlock.FACING).getOpposite())), getBlockState().getValue(XpWirelessBlock.FACING).getOpposite());
//	    if(handler != null)
//	    {
//			if(capturedPlayer != null)
//			{
//	        	if(drain)
//	        	{
//					if (handler.getTanks() > 0 && handler.getFluidInTank(0).getAmount() >= 20 && handler.getFluidInTank(0).getFluid().is(WUTTags.Fluids.EXPERIENCE) && level.getGameTime() % 3 == 0)
//					{
//						particleRate = 10;
//						
//						int xpAmount = XPOrbFalling.getExperienceValue(Math.min(20, handler.getFluidInTank(0).getAmount() / 20));
//						if (!handler.drain(xpAmount * 20, FluidAction.EXECUTE).isEmpty())
//						{
//							spawnXP(level, worldPosition, xpAmount, level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(XpWirelessBlock.FACING).getOpposite())));
//						}
//					}
//	        	}
//	        	else
//	        	{
//	    			if (handler.getFluidInTank(0).isEmpty() || handler.getFluidInTank(0).getFluid().is(WUTTags.Fluids.EXPERIENCE))
//	    				captureDroppedXP(handler);
//	        	}
//			}
//	    }
	}
	
	@Override
	public void clientTick()
	{
		super.clientTick();

//		getPlayers(this.wireless);
		
		spawnParticles();
	}
	
	public void spawnXP(Level world, BlockPos pos, int xp, BlockEntity tankTile)
	{
		tankTile.setChanged();
		ExperienceHelper.addPlayerXP(capturedPlayer, xp);
	}
	
	public boolean captureDroppedXP(IFluidHandler handler)
	{
		if(capturedPlayer != null)
		{
			int xpAmount = ExperienceHelper.getPlayerXP(capturedPlayer);
			if (xpAmount <= 0)
				return false;
			
			particleRate = 10;
			
			if(handler.getFluidInTank(0).getAmount() < handler.getTankCapacity(0))
			{
				handler.fill(new FluidStack(WUTFluids.EXPERIENCE.get(), 20), FluidAction.EXECUTE);
				ExperienceHelper.addPlayerXP(capturedPlayer, -1);
				level.playSound(null, capturedPlayer.getX(), capturedPlayer.getY(), capturedPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL , 0.1F, 0.5F * ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.8F));
			}
			return true;
		}
		return false;
	}
	
	public int getParticleRate()
	{
	  	return particleRate;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void spawnParticles()
	{
		RandomSource rand = level.random;
		
		if(capturedPlayer == null)
			return;
		if(particleRate <= 0)
			return;
		
		if (particleRate > 10)
			particleRate = 10;
		
		Vec3D spawn;
		Vec3D dest;
		
		if (particleRate > 5)
		{
			for (int i = 0; i <= particleRate; i++)
			{
				spawn = getParticleSpawn(rand, capturedPlayer);
				dest = getParticleDest(rand, capturedPlayer);
	              
	           	level.addParticle(WUTParticles.EXPERIENCE.get(),
	           			spawn.x,
	           			spawn.y,
	           			spawn.z,
	           			dest.x, dest.y, dest.z);
			}
		}
		else if (rand.nextInt(Math.max(1, 5 - particleRate)) == 0)
		{
			spawn = getParticleSpawn(rand, capturedPlayer);
	      	dest = getParticleDest(rand, capturedPlayer);
	      	
	       	level.addParticle(WUTParticles.EXPERIENCE.get(),
	       			spawn.x,
	       			spawn.y,
	       			spawn.z,
	       			dest.x, dest.y, dest.z);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	private Vec3D getParticleSpawn(RandomSource random, Player player)
	{
//		if(drain)
//		{
//			double range = 1;
//			return new Vec3D(worldPosition).add(
//					(random.nextFloat() - 0.0F) * range,
//					(random.nextFloat() - 0.25F) * range,
//					(random.nextFloat() - 0.0F) * range);
//		}
//		else
//		{
//			return Vec3D.getCenter(player.getOnPos()).add(
//	      			(0.0F),
//	      			(1.0F),
//	      			(0.0F));
//		}
		
		double range = 1;
		return new Vec3D(worldPosition).add(
				(random.nextFloat() - 0.0F) * range,
				(random.nextFloat() - 0.25F) * range,
				(random.nextFloat() - 0.0F) * range);
	}
	@OnlyIn(Dist.CLIENT)
	private Vec3D getParticleDest(RandomSource random, Player player)
	{
//		if(drain)
//		{
//			return Vec3D.getCenter(player.getOnPos()).add(
//					(0.0F),
//					(1.0F),
//					(0.0F));
//		}
//		else
//		{
//			double range = 1;
//			return new Vec3D(worldPosition).add(
//					(random.nextFloat() - 0.0F) * range,
//					(random.nextFloat() - 0.25F) * range,
//					(random.nextFloat() - 0.0F) * range);
//		}
		
		return Vec3D.getCenter(player.getOnPos()).add(
				(0.0F),
				(1.0F),
				(0.0F));
	}

	public void getPlayers(boolean wireless)
	{
		this.capturedPlayer = null;
		
		if(wireless)
		{
			for (Player player : getPlayersAround(level))
			{
				if (player.isSpectator())
					continue;
				this.capturedPlayer = player;
			}
		}
		else
		{
			for (Player player : getPlayersAbove(level, getBlockPos().getX(), getBlockPos().getY() + 0.01D, getBlockPos().getZ()))
			{
				if (player.isSpectator())
					continue;
				this.capturedPlayer = player;
			}
		}
	}
	public List<Player> getPlayersAround(Level world)
	{
		AABB scanArea = new AABB(worldPosition.getCenter().subtract(8, 8, 8), worldPosition.getCenter().add(8, 8, 8));
		return world.<Player>getEntitiesOfClass(Player.class, scanArea, EntitySelector.ENTITY_STILL_ALIVE);
	}
	public List<Player> getPlayersAbove(Level world, double x, double y, double z)
	{
		VoxelShape shape = world.getBlockState(worldPosition).getShape(world, worldPosition);
		return world.<Player>getEntitiesOfClass(Player.class, shape.bounds().move(x, y, z), EntitySelector.ENTITY_STILL_ALIVE);
	}
}
