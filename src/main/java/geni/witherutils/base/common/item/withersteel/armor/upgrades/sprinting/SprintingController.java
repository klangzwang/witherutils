package geni.witherutils.base.common.item.withersteel.armor.upgrades.sprinting;

import javax.annotation.Nonnull;

import org.jline.utils.Log;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.core.common.helper.NullHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WitherUtils.MODID, value = Dist.CLIENT)
public class SprintingController {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void handleFovUpdate(@Nonnull ComputeFovModifierEvent evt)
	{
		Player player = NullHelper.notnullF(evt.getPlayer(), "FOVModifierEvent has no player");
		if(!(player instanceof LocalPlayer))
		{
			Log.warn("invalid player type when adjusting FOV " + player);
			return;
		}

		final boolean steellegs = player.getItemBySlot(EquipmentSlot.LEGS).getItem() == WUTItems.STEELARMOR_LEGGINGS.get();
		AttributeInstance moveInst = player.getAttribute(Attributes.MOVEMENT_SPEED);

		if(steellegs && moveInst.getAttribute() != null)
		{
			evt.setNewFovModifier(1.0f);
		}
	}
}
