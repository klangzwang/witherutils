package geni.witherutils.base.common.plugin;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class WitherPluginJEI implements IModPlugin {

	private static final ResourceLocation ID = WitherUtilsRegistry.loc("jei");

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
	}
	  
	@Override
	public ResourceLocation getPluginUid()
	{
		return ID;
	}
	
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
    	IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

    	registry.addRecipeCategories(new AnvilRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new CutterRecipeCategory(guiHelper));
    	registry.addRecipeCategories(new WormRecipeCategory(guiHelper));
    }
    
	@Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
    	registration.addRecipeCatalyst(new ItemStack(WUTBlocks.ANVIL.get()), AnvilRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTItems.CUTTER.get()), CutterRecipeCategory.TYPE);
    	registration.addRecipeCatalyst(new ItemStack(WUTItems.WORM.get()), WormRecipeCategory.TYPE);
    }
	
	@Override
    public void registerRecipes(IRecipeRegistration registry)
    {
//		ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
//    	RecipeManager rm = world.getRecipeManager();
    	
//    	registry.addRecipes(AnvilRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(WUTRecipes.ANVIL.type())));
//    	registry.addRecipes(CutterRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(WUTRecipes.CUTTER.type())));
    	registry.addRecipes(WormRecipeCategory.TYPE, WormRecipeCategory.getRecipes());
    	
//		SwordRecipeCategory.addSwordBasicEnergyRecipe(registry, WUTItems.SWORD.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
//		WandRecipeCategory.addWandBasicEnergyRecipe(registry, WUTItems.WAND.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
//		WandRecipeCategory.addWandPearlPortRecipe(registry, WUTItems.WAND.get().getDefaultInstance(), Items.ENDER_PEARL.getDefaultInstance());
//		ShieldRecipeCategory.addShieldBasicEnergyRecipe(registry, WUTItems.SHIELDENERGY.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
//		
//    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_HELMET.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
//    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_CHEST.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
//    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_LEGGINGS.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
//    	RecipeRegistryHelper.addArmorBasicEnergyRecipe(registry, WUTItems.STEELARMOR_BOOTS.get().getDefaultInstance(), WUTItems.SOULBANK_BASIC.get().getDefaultInstance());
    	
    	registry.addIngredientInfo(new ItemStack(WUTBlocks.ANVIL.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.anvil"));
    	registry.addIngredientInfo(new ItemStack(WUTItems.SOULORB.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.soulorb"));
    	registry.addIngredientInfo(new ItemStack(WUTItems.WRENCH.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.wrench"));

    	registry.addIngredientInfo(new ItemStack(WUTItems.EXPERIENCE_BUCKET.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.experience"));
    	registry.addIngredientInfo(new ItemStack(WUTItems.FERTILIZER_BUCKET.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.witherutils.fertilizer"));
    }
}
