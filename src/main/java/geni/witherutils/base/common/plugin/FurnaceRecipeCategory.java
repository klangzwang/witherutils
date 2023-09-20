package geni.witherutils.base.common.plugin;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.common.ForgeHooks;

@SuppressWarnings("unused")
public class FurnaceRecipeCategory implements IRecipeCategory<SmeltingRecipe> {

	private static final int FONT = 4210752;
	public static final ResourceLocation ID = new ResourceLocation(WitherUtils.MODID, "electrofurnace");
	static final RecipeType<SmeltingRecipe> TYPE = new RecipeType<>(ID, SmeltingRecipe.class);
	
	public static final ResourceLocation JEI_RECIPE_TEXTURE = new ResourceLocation(ModIds.JEI_ID, "textures/gui/gui_vanilla.png");
	
	private IDrawable gui;
	private IDrawable icon;

	private final Component localizedName;
    private final IDrawableAnimated arrow;
	private final IDrawableStatic staticFlame;
	private final IDrawableAnimated animatedFlame;
	private final IDrawableStatic backgroundFlame;
	
	public FurnaceRecipeCategory(IGuiHelper helper)
	{
		gui = helper.drawableBuilder(new ResourceLocation(WitherUtils.MODID, "textures/jei/alloy_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(WUTBlocks.ELECTRO_FURNACE.get()));

		this.localizedName = Component.translatable(WUTBlocks.ELECTRO_FURNACE.get().getDescriptionId());
		this.arrow = helper.drawableBuilder(JEI_RECIPE_TEXTURE, 82, 128, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
		this.staticFlame = helper.createDrawable(JEI_RECIPE_TEXTURE, 82, 114, 14, 14);
		this.animatedFlame = helper.createAnimatedDrawable(this.staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
		this.backgroundFlame = helper.createDrawable(JEI_RECIPE_TEXTURE, 1, 134, 14, 14);
	}
	
	public Class<? extends SmeltingRecipe> getRecipeClass()
	{
		return SmeltingRecipe.class;
	}
	public static RecipeType<SmeltingRecipe> getType()
	{
		return TYPE;
	}
	public static ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	public Component getTitle()
	{
		return this.localizedName;
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
	public RecipeType<SmeltingRecipe> getRecipeType()
	{
		return TYPE;
	}

	@SuppressWarnings("resource")
	@Override
	public void draw(SmeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gg, double mouseX, double mouseY)
	{
	    var font = Minecraft.getInstance().font;
		gg.drawString(font, "Energy Cost: ", 10, 15, FONT, false);
		gg.drawString(font, "" + ForgeHooks.getBurnTime(new ItemStack(Items.COAL, 1), recipe.getType()) * 10 / 9, 10, 45, FONT, false);

		float experience = recipe.getExperience();
		if (experience > 0)
		{
			MutableComponent experienceString = Component.translatable("", experience);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(experienceString);
			gg.drawString(font, experienceString, this.gui.getWidth() - stringWidth, 0, 0xFF808080);
		}
	}
	
	@SuppressWarnings("resource")
    @Override
	public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 113, 11).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 42).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}
}