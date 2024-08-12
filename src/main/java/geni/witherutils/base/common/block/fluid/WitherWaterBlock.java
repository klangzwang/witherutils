package geni.witherutils.base.common.block.fluid;

import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class WitherWaterBlock extends LiquidBlock {

	private static boolean animalconvert;

	
    public WitherWaterBlock(FlowingFluid supplier, Properties props)
    {
        super(supplier, props);
	}
    
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
	{
		return WUTFluids.WITHERWATER_FLUID_TYPE.get().getLightLevel();
	}
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}

	@Override
	public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource random)
	{
		BlockPos blockpos = pos.above();
		BlockState blockstate = worldIn.getBlockState(blockpos);

		if(!blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP))
		{
			double d1 = (double) pos.getX() + random.nextDouble();
			double d2 = (double) pos.getY() + 0.1F + state.getFluidState().getHeight(worldIn, blockpos);
			double d3 = (double) pos.getZ() + random.nextDouble();
			
			if(random.nextFloat() < 0.1F)
			{
				worldIn.addParticle(WUTParticles.RISINGSOUL.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
				worldIn.addParticle(ParticleTypes.REVERSE_PORTAL, d1, d2, d3, 0.0D, 0.1D, 0.0D);
			}
		}
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity)
	{
		if(!worldIn.isClientSide && entity instanceof LivingEntity && entity.isAlive())
		{
			LivingEntity living = (LivingEntity) entity;

			if(living.isOnFire() == false && living.fireImmune() == false)
			{
				living.setRemainingFireTicks((int) Math.floor(worldIn.random.nextDouble() * 10));

				if (entity.isAlive() && entity instanceof Villager)
				{
					Villager villager = (Villager) entity;
					villager.convertTo(EntityType.ZOMBIE, false);
				}

				if (entity.isAlive() && entity instanceof Skeleton && !(entity instanceof WitherSkeleton))
				{
					Skeleton skeleton = (Skeleton) entity;
					skeleton.blockPosition();
					skeleton.remove(RemovalReason.KILLED);
					WitherSkeleton wither = EntityType.WITHER_SKELETON.create(worldIn);
					wither.moveTo(skeleton.getX(), skeleton.getY(), skeleton.getZ(), skeleton.xRotO, skeleton.yRotO);
					wither.setHealth(skeleton.getHealth());
					worldIn.addFreshEntity((Entity) wither);
				}

				if (entity.isAlive() && entity instanceof Creeper && !((Creeper) entity).isPowered())
				{
					Creeper creeper = (Creeper) entity;
					creeper.blockPosition();
					LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(worldIn);
					lightningboltentity.moveTo(creeper.getX(), creeper.getY(), creeper.getZ(), creeper.xRotO, creeper.yRotO);
					worldIn.addFreshEntity(lightningboltentity);
					creeper.setHealth(creeper.getMaxHealth());
					worldIn.explode(creeper, creeper.getX(), creeper.getY() -15, creeper.getZ(), 2, Level.ExplosionInteraction.NONE);
				}

				if (entity.isAlive() && entity instanceof Spider && !(entity instanceof CaveSpider))
				{
					Spider spider = (Spider) entity;
					spider.blockPosition();
					spider.remove(RemovalReason.KILLED);
					CaveSpider caveSpider = EntityType.CAVE_SPIDER.create(worldIn);
					caveSpider.moveTo(spider.getX(), spider.getY(), spider.getZ(), spider.xRotO, spider.yRotO);
					caveSpider.setHealth(spider.getMaxHealth());
					worldIn.addFreshEntity((Entity) caveSpider);
				}

				if (entity.isAlive() && entity instanceof Chicken)
				{
					Chicken chicken = (Chicken) entity;
					chicken.blockPosition();
					chicken.hurt(entity.level().damageSources().inFire(), -100);
					chicken.moveTo(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.xRotO, chicken.yRotO);
					chicken.setHealth(chicken.getMaxHealth());
					worldIn.addFreshEntity(chicken);
				}

				if(entity.level().canSeeSky(pos) && entity.isAlive() && isAnimalConverting() && entity instanceof Animal)
				{
					Animal animal = (Animal) entity;
					animal.blockPosition();
					LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(worldIn);
					lightningboltentity.moveTo(animal.getX(), animal.getY(), animal.getZ(), animal.xRotO, animal.yRotO);
					worldIn.addFreshEntity(lightningboltentity);
				}

				living.setRemainingFireTicks(1);
				living.hurt(entity.level().damageSources().lava(), 5.0F);
				if(living.getHealth() > 10) { living.setSpeed(1); living.setSprinting(false); }
				if(living.getHealth() < 10) { living.setSpeed(30); living.setSprinting(true); }
				living.canAttack(living);
				living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 22, 5));
				living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 15));
				living.addEffect(new MobEffectInstance(MobEffects.WITHER, 15, 10));
				living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 5));
				living.removeEffect(MobEffects.REGENERATION);
				living.removeEffect(MobEffects.DAMAGE_RESISTANCE);
				living.removeEffect(MobEffects.HEALTH_BOOST);
				living.removeEffect(MobEffects.HEAL);
			}
		}
	}

	public static boolean isAnimalConverting()
	{
		return animalconvert;
	}
	public static void setAnimalConverting(boolean convert)
	{
		animalconvert = convert;
	}
}