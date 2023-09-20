package geni.witherutils.base.common.entity.worm;

import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.core.common.util.FacingUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.network.NetworkHooks;

public class Worm extends Entity {

    public int timer;
    
	public Worm(EntityType<Worm> type, Level world)
	{
		super(type, world);
		this.setBoundingBox(new AABB(0, 0, 0, 0, 0, 0));
	}
	
	@SuppressWarnings("deprecation")
	public static boolean canWormify(Level world, BlockPos pos, BlockState state)
    {
        Block block = state.getBlock();
        boolean rightBlock = block instanceof FarmBlock || block == Blocks.DIRT || block instanceof GrassBlock || block instanceof SpreadingSnowyDirtBlock;

        if (rightBlock)
        {
            BlockPos posUp = pos.above();
            BlockState stateUp = world.getBlockState(posUp);
            Block blockUp = stateUp.getBlock();
            return blockUp instanceof IPlantable || blockUp instanceof BushBlock || blockUp.canSurvive(state, world, posUp);
        }
        else
        {
            return false;
        }
    }

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		this.timer = tag.getInt("Timer");
	}
	@Override
	public boolean save(CompoundTag tag)
	{
		tag.putInt("Timer", this.timer);
		return super.save(tag);
	}
	
	@SuppressWarnings("resource")
	@Override
    public void tick()
    {
		for(BlockPos pos : FacingUtil.AROUND_Y)
		{
			BlockPos aroundPos = new BlockPos(this.blockPosition().getX() + pos.getX(), this.blockPosition().getY() + pos.getY() + 1, this.blockPosition().getZ() + pos.getZ());
			if(this.level().getGameTime() % 8 == 0)
			{
				this.level().addParticle(WUTParticles.RISINGSOUL.get(),
						aroundPos.getX() + 1.0D - this.level().random.nextDouble(),
						aroundPos.getY(),
						aroundPos.getZ() + 1.0D - this.level().random.nextDouble(),
						0, 0, 0);
			}
		}
		this.baseTick();
    }
	
	@SuppressWarnings("resource")
	@Override
	public void baseTick()
	{
        if (!this.level().isClientSide)
        {
            this.timer++;

            if (this.timer % 50 == 0)
            {
                for (int x = -1; x <= 1; x++)
                {
                    for (int z = -1; z <= 1; z++)
                    {
                    	if(level().getBlockState(this.blockPosition()).isAir())
                    		this.discard();
                    	
                        BlockPos pos = new BlockPos(this.blockPosition().getX() + x, this.blockPosition().getY(), this.blockPosition().getZ() + z);
                        BlockState state = this.level().getBlockState(pos);
                        Block block = state.getBlock();
                        boolean isMiddlePose = x == 0 && z == 0;

                        if (canWormify(this.level(), pos, state))
                        {
                            boolean isFarmland = block instanceof FarmBlock;

                            if (!isFarmland || state.getValue(FarmBlock.MOISTURE) < 7)
                            {
                                if (isMiddlePose || this.level().random.nextFloat() >= 0.45F)
                                {
                                    if (!isFarmland)
                                    {
                                        useHoeAt((ServerLevel) this.level(), pos);
                                    }
                                    state = this.level().getBlockState(pos);
                                    isFarmland = state.getBlock() instanceof FarmBlock;

                                    if (isFarmland)
                                    {
                                        this.level().setBlock(pos, state.setValue(FarmBlock.MOISTURE, 7), 2);
                                    }
                                }
                            }
                            
                            if (isFarmland && this.level().random.nextFloat() >= 0.95F)
                            {
                                BlockPos plant = pos.above();
                                if (!this.level().getBlockState(plant).isAir())
                                {
                                    BlockState plantState = this.level().getBlockState(plant);
                                    Block plantBlock = plantState.getBlock();

                                    if ((plantBlock instanceof IPlantable && !(plantBlock instanceof GrassBlock)))
                                    {
                                        plantBlock.animateTick(plantState, this.level(), plant, this.level().random);
                                        if(plantBlock instanceof CropBlock crop)
                                        {
                                            if (crop.getAge(plantState) != crop.getMaxAge())
                                            {
                                                if (this.level().random.nextFloat() >= 0.9F)
                                                {
                                                	crop.growCrops(this.level(), plant, plantState);	
                                                }
                                                this.level().levelEvent(1505, plant, 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if (isMiddlePose)
                        {
                            this.discard();
                        }
                    }
                }
            }
            int dieTime = 0;
            if (dieTime > 0 && this.timer >= dieTime)
            {
                this.discard();
            }
        }
	}
    
    public void useHoeAt(ServerLevel world, BlockPos farmland)
    {
		if(world.getBlockState(farmland).getBlock() instanceof GrassBlock || world.getBlockState(farmland).getBlock() == Blocks.DIRT)
		{
			world.setBlock(new BlockPos(farmland.getX(), farmland.getY(), farmland.getZ()), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(farmland, Blocks.FARMLAND.defaultBlockState(), 2);
			SoundUtil.playSoundFromServer(world, farmland, SoundEvents.HOE_TILL);
		}
    }

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void defineSynchedData() {}
	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {}
	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {}
}
