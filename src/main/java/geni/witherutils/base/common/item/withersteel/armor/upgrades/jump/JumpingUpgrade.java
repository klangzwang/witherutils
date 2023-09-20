package geni.witherutils.base.common.item.withersteel.armor.upgrades.jump;

import geni.witherutils.api.steelupable.ISteelUpable;
import geni.witherutils.base.common.event.WitherKeyMappingHandler;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.SteelUpgradeItem;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JumpingUpgrade extends SteelUpgradeItem {

	protected static boolean jumpedLastTick = false;
	protected static int jumpCount = 0;
	
	public static void onPlayerTick(ItemStack stack, ISteelUpable item, Player player)
	{
		if(EnergyUtil.getEnergyStored(stack) > 990 && WitherKeyMappingHandler.isJumpActive)
        {
    		if(player.onGround() || player.onClimbable())
    		{
    			jumpCount = 2;
    		}
    		else if(!jumpedLastTick && jumpCount > 0 && player.getDeltaMovement().y < 0)
    		{
				if(player.jumping && !player.getAbilities().flying)
				{
					if(canJump(player))
					{
						jumpCount -= 1;
						player.jumpFromGround();
						player.level().playSound(player, player.getOnPos(), WUTSounds.JUMP.get(), SoundSource.PLAYERS, 0.4f, 1);
						for(int i = 0; i < 10; i++)
						{
							EnergyUtil.extractEnergy(stack, 99, false);
						}
						player.level().addParticle(ParticleTypes.CLOUD, player.getEyePosition().x, player.getEyePosition().y - 1.5D, player.getEyePosition().z, 0.0D, 0.0D, 0.0D);
					}
				}
    		}
    		jumpedLastTick = player.jumping;
        }
	}

	private static boolean canJump(Player player)
	{
		return !player.isFallFlying() && !player.isVehicle() && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION);
	}
	
    @SubscribeEvent
    public void jumpHeight(LivingJumpEvent event)
    {
        ItemStack stack = event.getEntity().getItemBySlot(EquipmentSlot.FEET);
        if (stack != null)
        {
        	double deltax = event.getEntity().getDeltaMovement().x;
        	double deltay = event.getEntity().getDeltaMovement().y;
        	double deltaz = event.getEntity().getDeltaMovement().z;
            event.getEntity().setDeltaMovement(deltax, deltay += 0.2, deltaz);
        }
    }
}
