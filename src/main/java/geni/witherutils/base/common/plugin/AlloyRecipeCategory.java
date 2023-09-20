package geni.witherutils.base.common.plugin;

import java.util.List;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.plugin.util.MachineCategory;
import geni.witherutils.base.common.recipes.AlloyFurnaceRecipe;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.core.common.recipes.CountedIngredient;
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

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class AlloyRecipeCategory extends MachineCategory<AlloyFurnaceRecipe> implements IRecipeCategory<AlloyFurnaceRecipe> {

	private static final int FONT = 4210752;
	private static final ResourceLocation ID = new ResourceLocation(WitherUtils.MODID, "alloyfurnace");
	static final RecipeType<AlloyFurnaceRecipe> TYPE = new RecipeType<>(ID, AlloyFurnaceRecipe.class);
	private IDrawable gui;
	private IDrawable icon;
    
	public AlloyRecipeCategory(IGuiHelper helper)
	{
		super(helper, false);
		gui = helper.drawableBuilder(new ResourceLocation(WitherUtils.MODID, "textures/jei/alloy_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(WUTBlocks.ALLOY_FURNACE.get()));
	}
	
	@Override
	public Component getTitle()
	{
		return Component.translatable(WUTBlocks.ALLOY_FURNACE.get().getDescriptionId());
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
	public RecipeType<AlloyFurnaceRecipe> getRecipeType()
	{
		return TYPE;
	}

	@SuppressWarnings("resource")
	@Override
	public void draw(AlloyFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gg, double mouseX, double mouseY)
	{
	    var font = Minecraft.getInstance().font;
	    gg.drawString(font, "Energy Cost: ", 10, 15, FONT, false);
	    gg.drawString(font, "" + recipe.getEnergyCost(), 10, 45, FONT, false);
//	    animatedFlame.draw(gg, 88, 22);
//	    animatedFlame.draw(gg, 113, 22);
	}

	@SuppressWarnings("resource")
    @Override
	public void setRecipe(IRecipeLayoutBuilder builder, AlloyFurnaceRecipe recipe, IFocusGroup focuses)
	{
		List<CountedIngredient> inputs = recipe.getInputs();
		if (inputs.size() > 0)
			builder.addSlot(INPUT, 88, 11).addItemStacks(inputs.get(0).getItems());
		if (inputs.size() > 1)
			builder.addSlot(INPUT, 113, 11).addItemStacks(inputs.get(1).getItems());
		if (inputs.size() > 2)
			builder.addSlot(INPUT, 137, 11).addItemStacks(inputs.get(2).getItems());
		builder.addSlot(OUTPUT, 113, 42).addItemStacks(List.of(recipe.getResultStacks(Minecraft.getInstance().level.registryAccess()).get(0).getItem()));
	}
}