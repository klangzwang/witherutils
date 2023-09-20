package geni.witherutils.base.common.plugin;

import java.util.List;
import java.util.Objects;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

@JeiPlugin
public class WitherPluginJEI implements IModPlugin {

	private static final ResourceLocation ID = new ResourceLocation(WitherUtils.MODID, "jei");

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
	  
	@Override
	public ResourceLocation getPluginUid()
	{
		return ID;
	}
	
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
    	IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

    	registry.addRecipeCategories(new AlloyRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new FurnaceRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new AnvilRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new CauldronRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new CutterRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new WormRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new SlicedConcreteRecipeCategory(guiHelper));
    }
    
	@Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
    	registration.addRecipeCatalyst(new ItemStack(WUTBlocks.ALLOY_FURNACE.get()), AlloyRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTBlocks.ELECTRO_FURNACE.get()), FurnaceRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTBlocks.ANVIL.get()), AnvilRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTBlocks.CAULDRON.get()), CauldronRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTItems.CUTTER.get()), CutterRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTItems.WORM.get()), WormRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTBlocks.SLICEDCONCRETEGRAY.get()), WormRecipeCategory.TYPE);
    }
	
	@SuppressWarnings("resource")
	@Override
    public void registerRecipes(IRecipeRegistration registry)
    {
		ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
    	RecipeManager rm = world.getRecipeManager();
    	
    	registry.addRecipes(AlloyRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(WUTRecipes.ALLOY.get())));
    	registry.addRecipes(AnvilRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(WUTRecipes.ANVIL.get())));
		registry.addRecipes(FurnaceRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(RecipeType.SMELTING)));
    	registry.addRecipes(CauldronRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(WUTRecipes.CAULDRON.get())));
    	registry.addRecipes(CutterRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(WUTRecipes.CUTTER.get())));
    	registry.addRecipes(WormRecipeCategory.TYPE, WormRecipeCategory.getRecipes());
    	registry.addRecipes(SlicedConcreteRecipeCategory.TYPE, SlicedConcreteRecipeCategory.getRecipes());
    	
		SwordRecipeCategory.addSwordBasicEnergyRecipe(registry, WUTItems.SWORD.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
		WandRecipeCategory.addWandBasicEnergyRecipe(registry, WUTItems.WAND.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
		WandRecipeCategory.addWandPearlPortRecipe(registry, WUTItems.WAND.get().getDefaultInstance(), Items.ENDER_PEARL.getDefaultInstance());
		ShieldRecipeCategory.addShieldBasicEnergyRecipe(registry, WUTItems.SHIELDENERGY.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
		
    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_HELMET.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_CHEST.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_LEGGINGS.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_BOOTS.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
    	
    	registry.addIngredientInfo(new ItemStack(WUTBlocks.ANVIL.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.anvil"));
    	registry.addIngredientInfo(new ItemStack(WUTBlocks.CAULDRON.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.cauldron"));
    	registry.addIngredientInfo(new ItemStack(WUTItems.SOULORB.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.soulorb"));
    	registry.addIngredientInfo(new ItemStack(WUTItems.WRENCH.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.wrench"));

    	registry.addIngredientInfo(new ItemStack(WUTItems.EXPERIENCE_BUCKET.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.experience"));
    	registry.addIngredientInfo(new ItemStack(WUTItems.FERTILIZER_BUCKET.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.fertilizer"));
    }
}
