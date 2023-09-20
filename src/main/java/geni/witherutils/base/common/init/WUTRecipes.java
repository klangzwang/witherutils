package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.recipes.AlloyFurnaceRecipe;
import geni.witherutils.base.common.recipes.AnvilRecipe;
import geni.witherutils.base.common.recipes.CauldronRecipe;
import geni.witherutils.base.common.recipes.CutterRecipe;
import geni.witherutils.base.common.recipes.AlloyFurnaceRecipe.SerializeAlloyFurnace;
import geni.witherutils.base.common.recipes.AnvilRecipe.SerializeAnvil;
import geni.witherutils.base.common.recipes.CauldronRecipe.SerializeCauldron;
import geni.witherutils.base.common.recipes.CutterRecipe.SerializeCutter;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTRecipes {
	
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, WitherUtils.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, WitherUtils.MODID);

	public static final RegistryObject<RecipeType<AlloyFurnaceRecipe>> ALLOY = RECIPE_TYPES.register("alloyfurnace", () -> new RecipeType<AlloyFurnaceRecipe>() { });
	public static final RegistryObject<SerializeAlloyFurnace> ALLOY_S = RECIPE_SERIALIZERS.register("alloyfurnace", () -> new SerializeAlloyFurnace());
	
	public static final RegistryObject<RecipeType<AnvilRecipe>> ANVIL = RECIPE_TYPES.register("anvil", () -> new RecipeType<AnvilRecipe>() { });
	public static final RegistryObject<SerializeAnvil> ANVIL_S = RECIPE_SERIALIZERS.register("anvil", () -> new SerializeAnvil());
	
	public static final RegistryObject<RecipeType<CauldronRecipe>> CAULDRON = RECIPE_TYPES.register("cauldron", () -> new RecipeType<CauldronRecipe>() { });
	public static final RegistryObject<SerializeCauldron> CAULDRON_S = RECIPE_SERIALIZERS.register("cauldron", () -> new SerializeCauldron());
	
	public static final RegistryObject<RecipeType<CutterRecipe>> CUTTER = RECIPE_TYPES.register("cutter", () -> new RecipeType<CutterRecipe>() { });
	public static final RegistryObject<SerializeCutter>CUTTER_S = RECIPE_SERIALIZERS.register("cutter", () -> new SerializeCutter());
}
