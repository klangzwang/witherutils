package geni.witherutils.base.common.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.joml.Quaternionf;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.deco.sliced.SlicedConcreteRecipe;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.core.common.util.McTimerUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.data.ModelData;

public class SlicedConcreteRecipeCategory implements IRecipeCategory<SlicedConcreteRecipe> {

    public static final ResourceLocation TEXTURES = WitherUtils.loc("textures/jei/wut_recipe.png");

    public static final RecipeType<SlicedConcreteRecipe> TYPE = RecipeType.create(WitherUtils.MODID, "slicedconcrete", SlicedConcreteRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final ITickTimer timer;

    public SlicedConcreteRecipeCategory(@Nonnull IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(TEXTURES, 0, -20, 128, 80);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(WUTBlocks.SLICEDCONCRETEGRAY.get()));
        this.timer = guiHelper.createTickTimer(60, 320, false);
    }

    @Nonnull
    public static List<SlicedConcreteRecipe> getRecipes()
    {
        List<SlicedConcreteRecipe> recipes = new ArrayList<>();
        recipes.add(new SlicedConcreteRecipe(Blocks.BLACK_CONCRETE, Blocks.OBSIDIAN, new ItemStack(WUTItems.HAMMER.get()), new ItemStack(WUTBlocks.SLICEDCONCRETEBLACK.get())));
        recipes.add(new SlicedConcreteRecipe(Blocks.GRAY_CONCRETE, Blocks.OBSIDIAN, new ItemStack(WUTItems.HAMMER.get()), new ItemStack(WUTBlocks.SLICEDCONCRETEGRAY.get())));
        recipes.add(new SlicedConcreteRecipe(Blocks.WHITE_CONCRETE, Blocks.OBSIDIAN, new ItemStack(WUTItems.HAMMER.get()), new ItemStack(WUTBlocks.SLICEDCONCRETEWHITE.get())));
        return recipes;
    }

    @Nonnull
    public static List<ItemStack> getCatalysts()
    {
        return List.of(new ItemStack(WUTBlocks.SLICEDCONCRETEGRAY.get()));
    }

    @Nonnull
    @Override
    public RecipeType<SlicedConcreteRecipe> getRecipeType()
    {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle()
    {
        return Component.literal("SlicedConcrete");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SlicedConcreteRecipe recipe, @Nonnull IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, 8 + 1, 24 + 1).addItemStack(recipe.input());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102 + 1, 24 + 1).addItemStack(recipe.output());
    }

    @Nonnull
    @Override
    public List<Component> getTooltipStrings(@Nonnull SlicedConcreteRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
    {
        return Collections.emptyList();
    }

    @SuppressWarnings("resource")
	@Override
    public void draw(@Nonnull SlicedConcreteRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        Quaternionf quat = new Quaternionf();
        quat.rotationXYZ(30 * Mth.DEG_TO_RAD, 45 * Mth.DEG_TO_RAD, 0);

        int value = timer.getValue();
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(52, 40, 128 - 32);
        guiGraphics.pose().scale(16, -16, 16);
        guiGraphics.pose().mulPose(quat);
        if(value < 160)
        	dispatcher.renderSingleBlock(recipe.base().defaultBlockState(), guiGraphics.pose(), bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
        else
        	dispatcher.renderSingleBlock(Blocks.GRAY_CONCRETE.defaultBlockState(), guiGraphics.pose(), bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
        guiGraphics.pose().popPose();

        Quaternionf rotquat = new Quaternionf();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(63, 16, 128 - 16);
        float scale = 16f;
        guiGraphics.pose().scale(scale, scale, scale);

        rotquat.rotationXYZ(0, 0.25f, 0.5f);
        guiGraphics.pose().mulPose(rotquat);
        
		float time = Minecraft.getInstance().level.getLevelData().getGameTime() + McTimerUtil.renderPartialTickTime;
		float offset = (float) (Math.sin(time / 3.0D) / 10.0D);

        rotquat.rotationXYZ(0, 0, 12 * offset);
        guiGraphics.pose().mulPose(rotquat);
        
        ItemStack toDisplay = recipe.input();
        itemRenderer.renderStatic(toDisplay, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, guiGraphics.pose(), bufferSource, null, 0);
        guiGraphics.pose().popPose();

        bufferSource.endBatch();

        Font fontRenderer = Minecraft.getInstance().font;
        guiGraphics.drawString(fontRenderer, "", 64 - fontRenderer.width("HAMMER USING") / 2, 68, 0xff404040, false);
    }
}
