package geni.witherutils.base.common.plugin;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.recipes.CauldronRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("resource")
public class CauldronRecipeCategory implements IRecipeCategory<CauldronRecipe> {

	private static final int FONT = 4210752;
	private static final ResourceLocation ID = new ResourceLocation(WitherUtils.MODID, "cauldron");
	static final RecipeType<CauldronRecipe> TYPE = new RecipeType<>(ID, CauldronRecipe.class);
	private IDrawable gui;
	private IDrawable icon;

	public CauldronRecipeCategory(IGuiHelper helper)
	{
		gui = helper.drawableBuilder(new ResourceLocation(WitherUtils.MODID, "textures/jei/cauldron_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(WUTBlocks.CAULDRON.get()));
	}
	
	@Override
	public Component getTitle()
	{
		return Component.translatable(WUTBlocks.CAULDRON.get().getDescriptionId());
	}
	
	@Override
	public IDrawable getBackground()
	{
		return gui;
	}
	
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}
	
	@Override
	public RecipeType<CauldronRecipe> getRecipeType()
	{
		return TYPE;
	}
	
	@Override
	public void draw(CauldronRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gg, double mouseX, double mouseY)
	{
	    var font = Minecraft.getInstance().font;
		gg.drawString(font, "Timer: " + recipe.getTimer(), 110, 2, FONT);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CauldronRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(INPUT, 76, 11).addIngredients(recipe.getInput());
		builder.addSlot(OUTPUT, 144, 43).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}
}
