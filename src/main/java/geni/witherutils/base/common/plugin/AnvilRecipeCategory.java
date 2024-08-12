package geni.witherutils.base.common.plugin;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.block.anvil.AnvilRecipe;
import geni.witherutils.base.common.init.WUTBlocks;
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

public class AnvilRecipeCategory implements IRecipeCategory<AnvilRecipe> {

	private static final int FONT = 4210752;
	private static final ResourceLocation ID = WitherUtilsRegistry.loc("anvil");
	static final RecipeType<AnvilRecipe> TYPE = new RecipeType<>(ID, AnvilRecipe.class);
	private IDrawable gui;
	private IDrawable icon;

	public AnvilRecipeCategory(IGuiHelper helper)
	{
		gui = helper.drawableBuilder(WitherUtilsRegistry.loc("textures/jei/anvil_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(WUTBlocks.ANVIL.get()));
	}
	
	@Override
	public Component getTitle()
	{
		return Component.translatable(WUTBlocks.ANVIL.get().getDescriptionId());
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
	public RecipeType<AnvilRecipe> getRecipeType()
	{
		return TYPE;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void draw(AnvilRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gg, double mouseX, double mouseY)
	{
	    var font = Minecraft.getInstance().font;
	    gg.drawString(font, recipe.hitcounter() + " Hits needed", 90, 30, FONT, false);
//	    animatedFlame.draw(gg, 3, 29);
//	    animatedFlame.draw(gg, 51, 29);
	}

	@SuppressWarnings("resource")
    @Override
	public void setRecipe(IRecipeLayoutBuilder builder, AnvilRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(INPUT, 90, 5).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(OUTPUT, 90, 43).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}
}