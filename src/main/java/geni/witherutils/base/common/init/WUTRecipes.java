package geni.witherutils.base.common.init;

import java.util.function.Supplier;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.anvil.AnvilRecipe;
import geni.witherutils.base.common.item.cutter.CutterRecipe;
import geni.witherutils.core.common.recipe.RecipeTypeSerializerPair;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTRecipes {

	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Names.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Names.MODID);

    public static final RecipeTypeSerializerPair<AnvilRecipe, AnvilRecipe.Serializer> ANVIL = register("anvil", AnvilRecipe.Serializer::new);
    
    public static final RecipeTypeSerializerPair<CutterRecipe, CutterRecipe.Serializer> CUTTER = register("cutter", CutterRecipe.Serializer::new);
    
    @SuppressWarnings("unused")
	private static <I extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<I>> registerType(String name)
    {
        return RECIPE_TYPES.register(name, () -> RecipeType.simple(WitherUtilsRegistry.loc(name)));
    }

    private static <R extends Recipe<?>, S extends RecipeSerializer<? extends R>> RecipeTypeSerializerPair<R, S> register(String name, Supplier<S> serializerFactory)
    {
        var type = RECIPE_TYPES.<RecipeType<R>>register(name, () -> RecipeType.simple(WitherUtilsRegistry.loc(name)));
        var serializer = RECIPE_SERIALIZERS.register(name, serializerFactory);
        return new RecipeTypeSerializerPair<>(type, serializer);
    }
}
