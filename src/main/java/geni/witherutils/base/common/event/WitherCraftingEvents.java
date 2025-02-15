package geni.witherutils.base.common.event;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.entity.naked.ChickenNaked;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WitherUtils.MODID)
public class WitherCraftingEvents {

	@SubscribeEvent
	public static void clickOnEntity(EntityInteract event)
	{
		if (event.getTarget() instanceof LivingEntity)
		{
			LivingEntity entity = (LivingEntity) event.getTarget();

			if (entity.isAlive() && entity instanceof Chicken && !entity.isBaby() && !(entity instanceof ChickenNaked))
			{
				Level world = entity.getCommandSenderWorld();
				ItemStack eventItem = event.getItemStack();
				
				Chicken chicken = (Chicken) entity;
				chicken.blockPosition();
				// chicken.remove(RemovalReason.KILLED);
				ChickenNaked chickennaked = WUTEntities.CHICKENNAKED.get().create(world);
				chickennaked.moveTo(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.xRotO, chicken.yRotO);
				chickennaked.setHealth(chicken.getHealth());

				RandomSource random = world.random;
				
				if (!eventItem.isEmpty() && eventItem.getItem() instanceof ShearsItem)
				{
					if (!world.isClientSide)
					{
						CompoundTag nbt = entity.getPersistentData();
						if (!nbt.contains("nakedChickey"))
						{
							chicken.remove(RemovalReason.KILLED);
							entity.setInvulnerable(true);
							nbt.putBoolean("nakedChickey", true);
							world.addFreshEntity((Entity) chickennaked);
							
							entity.playSound(WUTSounds.WORMBIP.get(), 1F, 1F);
							entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
							world.levelEvent(1027, entity.blockPosition(), 0);
							
							for (int k = 0; k < 2; ++k)
							{
								ItemStack stack = new ItemStack(WUTItems.FEATHER.get());
								ItemEntity feather = new ItemEntity(world, entity.getX() + (double) (world.random.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), entity.getY() + (double) (world.random.nextFloat() * entity.getBbHeight()), entity.getZ() + (double) (world.random.nextFloat() * entity.getBbWidth() * 2.0F) - (double) entity.getBbWidth(), stack);
								world.addFreshEntity(feather);
							}
							
							if(world instanceof ServerLevel serverlevel)
							{
					        	for(int i = 0; i < 40; i++)
					        	{
					    			double d1 = (double) chickennaked.getX() - 0.5D + random.nextDouble();
					    			double d2 = (double) (chickennaked.getY() + 0.5D) - random.nextDouble() * (double) 0.1F;
					    			double d3 = (double) chickennaked.getZ() - 0.5D + random.nextDouble();
					    			serverlevel.sendParticles(WUTParticles.RISINGSOUL.get(), d1, d2, d3, 3, 0.0D, 0.0D, 0.0D, 0.0f);
					    			serverlevel.sendParticles(ParticleTypes.SMOKE, d1, d2, d3, 3, 0.0D, 0.0D, 0.0D, 0.0f);
					        	}
							}
						}
					}
				}
			}
		}
	}
}