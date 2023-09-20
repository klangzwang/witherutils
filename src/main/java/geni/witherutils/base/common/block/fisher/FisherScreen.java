package geni.witherutils.base.common.block.fisher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.RedstoneControl;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.FoodWidget;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FisherScreen extends WUTScreen<FisherContainer> {
	
	float eftimer;
	
    public FisherScreen(FisherContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        setHotbarOffset(90);
        
		eftimer = 0.0f;
        addRenderableOnly(new FoodWidget(this.font, this.getMenu(), leftPos + 8, topPos + 23, 16, 40));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
    	gg.blit(getRedstoneTexture(), this.leftPos + 155, this.topPos + 30, 0, 0, 12, 12, 12, 12);
    	gg.blit(getAutoFeedTexture(), this.leftPos + 155, this.topPos + 48, 0, 0, 12, 12, 12, 12);

    	renderSlotButtonToolTips(gg, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(GuiGraphics gui, float pPartialTick, int pMouseX, int pMouseY)
    {
    	super.renderBg(gui, pPartialTick, pMouseX, pMouseY);

    	renderItemGhostSlot(gui, Items.BREAD, menu.getSlot(0), WitherUtils.loc("textures/gui/slot/slot_normal.png"));
    	renderItemGhostSlot(gui, Items.FISHING_ROD, menu.getSlot(1), WitherUtils.loc("textures/gui/slot/slot_normal.png"));
    	
    	renderSignalLamp(gui, getMenu().getBlockEntity()::hasEnoughWater, WitherUtils.loc("textures/gui/signals.png"), 160, 75);
    	
    	renderAllSlots(gui);
    }
    
	public ResourceLocation getRedstoneTexture()
	{
		if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
			return WitherUtils.loc("textures/gui/redstone_active.png");
		else
			return WitherUtils.loc("textures/gui/redstone_signal.png");
	}
	public ResourceLocation getAutoFeedTexture()
	{
		if(menu.getBlockEntity().getAutofeed() == true)
			return WitherUtils.loc("textures/gui/autofeed_on.png");
		else
			return WitherUtils.loc("textures/gui/autofeed_off.png");
	}
	
    @Override
    protected String getBarName()
    {
        return "Fisher";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/fisher.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 90);
    }

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(155, 30, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(155, 48, 16, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getAutofeed())
				menu.getBlockEntity().setAutofeed(false);
			else
				menu.getBlockEntity().setAutofeed(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();

        if(isHovering(155, 30, 12, 12, mouseX, mouseY))
        {
            Component appendRedSt = Component.literal("");
            
            if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
            	appendRedSt = Component.literal(ChatFormatting.GREEN + "Always");
            else if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ACTIVE_WITH_SIGNAL)
            	appendRedSt = Component.literal(ChatFormatting.RED + "withSignal");

            list.add(Component.translatable(ChatFormatting.GRAY + "Redstone Signal: ").append(appendRedSt));
        }
        else if(isHovering(155, 48, 12, 12, mouseX, mouseY))
        {
            Component appendAutofeed = Component.literal("");
            
            if(menu.getBlockEntity().getAutofeed() == true)
            	appendAutofeed = Component.literal(ChatFormatting.GREEN + "Active");
            else
            	appendAutofeed = Component.literal(ChatFormatting.RED + "Disabled");

            list.add(Component.translatable(ChatFormatting.GRAY + "Autofeed: ").append(appendAutofeed));
        }
        else if(isHovering(8, 64, 16, 16, mouseX, mouseY))
        {
            Component appendFood = Component.literal("");
        	
            if(menu.getSlot(0).hasItem())
            	return;
            
            appendFood = Component.literal(ChatFormatting.RED + "Empty");
        	list.add(Component.translatable(ChatFormatting.GRAY + "Food: ").append(appendFood));
        }
        else if(isHovering(26, 64, 16, 16, mouseX, mouseY))
        {
            Component appendRod = Component.literal("");
        	
            if(menu.getSlot(1).hasItem())
            	return;
            
            appendRod = Component.literal(ChatFormatting.RED + "NotFound");
        	list.add(Component.translatable(ChatFormatting.GRAY + "FishingRod: ").append(appendRod));
        }
        else if(isHovering(160, 75, 6, 6, mouseX, mouseY))
        {
            Component appendLamp = Component.literal("");
        	
            if(menu.getBlockEntity().hasEnoughWater() > 42)
            	appendLamp = Component.literal(ChatFormatting.GREEN + "EnoughWater");
            else
            	appendLamp = Component.literal(ChatFormatting.RED + "NotEnoughWater");

        	list.add(Component.translatable(ChatFormatting.GRAY + "WaterCheck: ").append(appendLamp));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }

    public void renderAllSlots(GuiGraphics gg)
    {
    	gg.pose().pushPose();
    	
//		if(eftimer >= 1.0f)
//			eftimer = 1.0f;
//		else
//			eftimer += 0.025f;
    	
//		RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1, 1, 1, eftimer);
        
        for(int i = 2; i < 11; i++)
        {
            gg.blit(WitherUtils.loc("textures/gui/slot/slot_normal.png"), menu.getSlot(i).x + leftPos - 1, menu.getSlot(i).y + topPos - 1, 0, 0, 18, 18, 18, 18);
        }
        
        gg.pose().popPose();
    }

    public void renderItemGhostSlot(GuiGraphics gg, Item item, Slot slot, ResourceLocation slotbg)
    {
    	gg.blit(slotbg, slot.x + leftPos - 1, slot.y + topPos - 1, 0, 0, 18, 18, 18, 18);
    	drawItem(gg, new ItemStack(item), getMenu().getBlockEntity().getLevel(), slot.x + leftPos, slot.y + 15 + topPos);
    }
    
    public static void drawItem(GuiGraphics gg, ItemStack stack, @Nullable Level level, int x, int y)
    {
    	PoseStack poseStack = gg.pose();
    	poseStack.pushPose();
        {
        	poseStack.translate(x, y, 100);
        	poseStack.scale(16F, -16F, 16F);
        	poseStack.mulPose(Axis.XP.rotationDegrees(5F));
    		
            RenderSystem.applyModelViewMatrix();
            BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, level, null, 0);
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.tripwire());

            RenderSystem.depthMask(false);
            poseStack.pushPose();
            
            RenderSystem.enableBlend();
            
            RenderSystem.setShaderColor(0.2F, 0.2F, 0.2F, 0.4F);
            
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, poseStack, vertexBuilder);
            
            buffer.endBatch();

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            poseStack.popPose();
            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.depthMask(true);
        }
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    protected void drawFakeItemHover(GuiGraphics gg, ResourceLocation fakeItem, int x, int y)
    {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        gg.blit(fakeItem, x, y, x + 16, y + 16, 0x80FFFFFF, 0x80FFFFFF);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }
    
    public void renderSignalLamp(GuiGraphics gg, Supplier<Integer> getter, ResourceLocation lampi, int x, int y)
    {
    	int hasEnough = getter.get();
    	gg.blit(lampi, leftPos + x, topPos + y, 0, 0, 6, 6, 18, 6);
    	gg.blit(lampi, leftPos + x, topPos + y, hasEnough > 42 ? 6 : 12, 0, 6, 6, 18, 6);
    }
}
