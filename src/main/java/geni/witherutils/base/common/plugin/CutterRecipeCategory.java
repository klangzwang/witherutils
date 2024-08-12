package geni.witherutils.base.common.plugin;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.item.cutter.CutterRecipe;
import geni.witherutils.base.common.plugin.util.MachineCategory;
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

public class CutterRecipeCategory extends MachineCategory<CutterRecipe> implements IRecipeCategory<CutterRecipe> {

	private static final int FONT = 4210752;
	private static final ResourceLocation ID = WitherUtilsRegistry.loc("cutter");
	static final RecipeType<CutterRecipe> TYPE = new RecipeType<>(ID, CutterRecipe.class);
	private IDrawable gui;
	private IDrawable icon;

	public CutterRecipeCategory(IGuiHelper helper)
	{
		super(helper, false);
		gui = helper.drawableBuilder(WitherUtilsRegistry.loc("textures/jei/cutter_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(WUTItems.CUTTER.get()));
	}
	@Override
	public Component getTitle()
	{
		return Component.translatable(WUTItems.CUTTER.get().getDescriptionId());
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
	public RecipeType<CutterRecipe> getRecipeType()
	{
		return TYPE;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void draw(CutterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gg, double mouseX, double mouseY)
	{
	    var font = Minecraft.getInstance().font;
	    gg.drawString(font, "Cutting from", 54, 12, FONT, false);
	    gg.drawString(font, "" + recipe.getIngredients(), 42, 50, FONT, false);
//	    animatedFlame.draw(gg, 3, 29);
//	    animatedFlame.draw(gg, 51, 29);
	}

	@SuppressWarnings("resource")
    @Override
	public void setRecipe(IRecipeLayoutBuilder builder, CutterRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(INPUT, 13, 26).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(OUTPUT, 139, 26).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}
}