package geni.witherutils.base.common.item.withersteel.armor.upgrades.squidring;

import java.util.Random;

import geni.witherutils.base.common.event.WitherKeyMappingHandler;
import geni.witherutils.base.common.item.withersteel.armor.SteelArmorItem;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.SteelUpgradeItem;
import geni.witherutils.core.common.helper.VecHelper;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SquidRingUpgrade extends SteelUpgradeItem {

	public static int tickFlight;
	
	@SuppressWarnings("resource")
	public static void onPlayerTick(ItemStack stack, SteelArmorItem item, Player player)
	{
		final boolean isJumping = Minecraft.getInstance().options.keyJump.isDown();
		final boolean onGround = player.onGround();
		
    	if(item.hasChargeMoreThen(stack, 10) && WitherKeyMappingHandler.isSquidActive)
    	{
			if (!onGround && isJumping && tickFlight < getMaxFlightTime())
			{
				++tickFlight;
				player.setDeltaMovement(
						player.getDeltaMovement().x,
	              		player.getDeltaMovement().y * 0.9 + 0.1,
	              		player.getDeltaMovement().z);
				
        		Minecraft mc = Minecraft.getInstance();
                if(mc.options.particles().get() != ParticleStatus.MINIMAL)
                {
                	Random rand = new Random();
                    Vec3 playerPos = mc.player.position().add(0, 0.25, 0);
                    float random = (rand.nextFloat() - 0.5F) * 0.1F;
                    double[] sneakBonus = player.isCrouching() ? new double[] { -0.30, -0.10 } : new double[] { 0, 0 };
                    Vec3 vLeft = VecHelper.rotate(new Vec3(-0.10, -0.30 + sneakBonus[1], -0.0 + sneakBonus[0]), player.yBodyRot, 0, 0);
                    Vec3 vRight = VecHelper.rotate(new Vec3(0.10, -0.30 + sneakBonus[1], -0.0 + sneakBonus[0]), player.yBodyRot, 0, 0);

                    Vec3 v = playerPos.add(vLeft).add(player.getDeltaMovement().scale(player.getFallFlyingTicks()));
                    player.level().addParticle(ParticleTypes.BUBBLE, v.x, v.y, v.z, random, -0.2D, random);
                    player.level().addParticle(ParticleTypes.BUBBLE_POP, v.x, v.y, v.z, random, -0.2D, random);

                    v = playerPos.add(vRight).add(player.getDeltaMovement().scale(player.getFallFlyingTicks()));
                    player.level().addParticle(ParticleTypes.BUBBLE, v.x, v.y, v.z, random, -0.2D, random);
                    player.level().addParticle(ParticleTypes.BUBBLE_POP, v.x, v.y, v.z, random, -0.2D, random);
                }
            	EnergyUtil.extractEnergy(stack, 10, false);
			}
			if (!isJumping && tickFlight > 0)
			{
				--tickFlight;
			}
		}
	}
	
	public static int getMaxFlightTime()
	{
		return 200;
	}
}
