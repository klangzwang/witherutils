package geni.witherutils.base.common.event;

import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class WitherRenderEvents {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onPlayerRender(RenderPlayerEvent event)
	{
		Player player = event.getEntity();
		PlayerModel<AbstractClientPlayer> playerModel = event.getRenderer().getModel();

		if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == WUTItems.STEELARMOR_HELMET.get())
		{
			playerModel.head.visible = false;
			playerModel.hat.visible = false;
		}
		if(player.getItemBySlot(EquipmentSlot.CHEST).getItem() == WUTItems.STEELARMOR_CHEST.get())
		{
			playerModel.jacket.visible = false;
			playerModel.leftSleeve.visible = false;
			playerModel.rightSleeve.visible = false;
			playerModel.body.visible = false;
			playerModel.leftArm.visible = false;
			playerModel.rightArm.visible = false;
		}
		if(player.getItemBySlot(EquipmentSlot.LEGS).getItem() == WUTItems.STEELARMOR_LEGGINGS.get())
		{
			playerModel.leftPants.visible = false;
			playerModel.rightPants.visible = false;
			playerModel.leftLeg.visible = false;
			playerModel.rightLeg.visible = false;
		}
	}
}
