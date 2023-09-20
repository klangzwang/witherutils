package geni.witherutils.base.common.item.withersteel.armor.upgrades.featherfall;

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

public class FeatherFallUpgrade extends SteelUpgradeItem {

	public static void onPlayerTick(ItemStack stack, SteelArmorItem item, Player player)
	{
    	if(item.hasChargeMoreThen(stack, 10) && WitherKeyMappingHandler.isFeatherActive)
    	{
            if (player.fallDistance > 2.5)
                player.fallDistance = 2.5f;
            float terminalVelocity = -0.4f + (1 * 0.08f);
            if (terminalVelocity > -0.05f)
                terminalVelocity = -0.05f;
            if (player.isCrouching() && terminalVelocity > -0.8f)
                terminalVelocity = -0.8F;
            boolean flying = false;
            flying = player.getAbilities().flying;

    		if(!flying && player.getDeltaMovement().y < terminalVelocity)
    		{
    			player.setDeltaMovement(player.getDeltaMovement().x, terminalVelocity, player.getDeltaMovement().z);
    			
        		Minecraft mc = Minecraft.getInstance();
                if(mc.options.particles().get() != ParticleStatus.MINIMAL)
                {
                	Random rand = new Random();
                    Vec3 playerPos = mc.player.position().add(0, 1.5, 0);
                    float random = (rand.nextFloat() - 0.5F) * 0.1F;
                    double[] sneakBonus = player.isCrouching() ? new double[] { -0.30, -0.10 } : new double[] { 0, 0 };
                    Vec3 vLeft = VecHelper.rotate(new Vec3(-0.30, -0.30 + sneakBonus[1], -0.30 + sneakBonus[0]), player.yBodyRot, 0, 0);
                    Vec3 vRight = VecHelper.rotate(new Vec3(0.30, -0.30 + sneakBonus[1], -0.30 + sneakBonus[0]), player.yBodyRot, 0, 0);

                    Vec3 v = playerPos.add(vLeft).add(player.getDeltaMovement().scale(player.getFallFlyingTicks()));
                    player.level().addParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);

                    v = playerPos.add(vRight).add(player.getDeltaMovement().scale(player.getFallFlyingTicks()));
                    player.level().addParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);
                }
            	EnergyUtil.extractEnergy(stack, 10, false);
    		}
    	}
	}
}
