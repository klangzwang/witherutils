package geni.witherutils.base.common.event;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;

import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.withersteel.armor.SteelArmorItem;
import geni.witherutils.core.common.util.EnergyUtil;
import geni.witherutils.core.common.util.NNList;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class WitherKeyMappingHandler {
	
	private static final @Nonnull NNList<EquipmentSlot> SLOTS = NNList.of(EquipmentSlot.class);
	
	private static KeyMapping vision;
	private static KeyMapping speed;
	private static KeyMapping jump;
	private static KeyMapping feather;
	private static KeyMapping squid;

	public static boolean isVisionActive = false;
	public static boolean isSpeedActive = false;
	public static boolean isJumpActive = false;
	public static boolean isFeatherActive = false;
	public static boolean isSquidActive = false;
	
	public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
	{
		vision = new KeyMapping("Night-Vision On/Off", GLFW.GLFW_KEY_H, "WitherUtils Hotkeys");
		speed = new KeyMapping("Movement Speed Slow/Fast", GLFW.GLFW_KEY_J, "WitherUtils Hotkeys");
		jump = new KeyMapping("Double Jump On/Off", GLFW.GLFW_KEY_K, "WitherUtils Hotkeys");
		feather = new KeyMapping("Feather Fall On/Off", GLFW.GLFW_KEY_G, "WitherUtils Hotkeys");
		squid = new KeyMapping("Squid Jumping On/Off", GLFW.GLFW_KEY_L, "WitherUtils Hotkeys");
		
		event.register(vision);
		event.register(speed);
		event.register(jump);
		event.register(feather);
		event.register(squid);
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public void onKeyInput(InputEvent.Key event)
	{
		var player = Minecraft.getInstance().player;
		if (player == null)
			return;

		SLOTS.apply(slot -> {
			ItemStack stack = player.getItemBySlot(slot);
			if (stack.getItem() instanceof SteelArmorItem steelArmor)
			{
				if(!steelArmor.isChargeable(stack))
					return;
				if(EnergyUtil.getEnergyStored(stack) <= 0)
					return;
				
				switch(slot)
				{
					case CHEST :
						
						if(feather.consumeClick())
						{
							if(!EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.CHEST)).containsKey(WUTEnchants.FEATHER_FALL.get()))
							{
								player.displayClientMessage(Component.translatable("Feather Upgrade not found.").withStyle(ChatFormatting.WHITE), true);
								return;
							}

							if(isFeatherActive)
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 0.5F);
								isFeatherActive = false;
							}
							else
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 0.5F);
								isFeatherActive = true;
							}
							player.displayClientMessage(Component.translatable("Feather:").withStyle(ChatFormatting.WHITE).append(Component.translatable(isFeatherActive ? " enabled" : " disabled").withStyle(ChatFormatting.GRAY)), true);

						}
						break;
						
					case FEET :
						
						if(jump.consumeClick())
						{
							if(!EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.FEET)).containsKey(WUTEnchants.JUMPING.get()))
							{
								player.displayClientMessage(Component.translatable("Jumping Upgrade not found.").withStyle(ChatFormatting.WHITE), true);
								return;
							}

							if(isJumpActive)
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 0.5F);
								isJumpActive = false;
							}
							else
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 0.5F);
								isJumpActive = true;
							}
							player.displayClientMessage(Component.translatable("Jumping:").withStyle(ChatFormatting.WHITE).append(Component.translatable(isJumpActive ? " enabled" : " disabled").withStyle(ChatFormatting.GRAY)), true);

						}
						if(squid.consumeClick())
						{
							if(!EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.FEET)).containsKey(WUTEnchants.SQUID_JUMP.get()))
							{
								player.displayClientMessage(Component.translatable("SquidJumping Upgrade not found.").withStyle(ChatFormatting.WHITE), true);
								return;
							}

							if(isSquidActive)
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 0.5F);
								isSquidActive = false;
							}
							else
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 0.5F);
								isSquidActive = true;
							}
							player.displayClientMessage(Component.translatable("SquidJump:").withStyle(ChatFormatting.WHITE).append(Component.translatable(isSquidActive ? " enabled" : " disabled").withStyle(ChatFormatting.GRAY)), true);

						}
						break;
						
					case HEAD :
						
						if(vision.consumeClick())
						{
							if(!EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.HEAD)).containsKey(WUTEnchants.NIGHT_VISION.get()))
							{
								player.displayClientMessage(Component.translatable("Night Vision Upgrade not found.").withStyle(ChatFormatting.WHITE), true);
								return;
							}

							if(isVisionActive)
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 0.5F);
								isVisionActive = false;
							}
							else
							{
								SoundUtil.playSound(player, WUTSounds.VISIONON.get(), 0.15F);
								isVisionActive = true;
							}
							player.displayClientMessage(Component.translatable("Vision:").withStyle(ChatFormatting.WHITE).append(Component.translatable(isVisionActive ? " enabled" : " disabled").withStyle(ChatFormatting.GRAY)), true);
						}
						break;
						
					case LEGS :

						if(speed.consumeClick())
						{
							if(!EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.LEGS)).containsKey(WUTEnchants.SPRINTING.get()))
							{
								player.displayClientMessage(Component.translatable("Sprinting Upgrade not found.").withStyle(ChatFormatting.WHITE), true);
								return;
							}

							if(isSpeedActive)
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 1.0F);
								isSpeedActive = false;
							}
							else
							{
								SoundUtil.playSound(player, WUTSounds.VISIONOFF.get(), 1.0F);
								isSpeedActive = true;
							}
							player.displayClientMessage(Component.translatable("Sprinting:").withStyle(ChatFormatting.WHITE).append(Component.translatable(isSpeedActive ? " enabled" : " disabled").withStyle(ChatFormatting.GRAY)), true);
						}
						break;
					default :
						break;
				}
			}
		});
	}
}