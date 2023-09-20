package geni.witherutils.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

public abstract class WitherRecipeProvider extends RecipeProvider {
    
    public WitherRecipeProvider(PackOutput generator)
    {
        super(generator);
    }

    protected abstract static class WitherFinishedRecipe implements FinishedRecipe
    {
        private final ResourceLocation id;
        private final List<ICondition> conditions = new ArrayList<>();

        public WitherFinishedRecipe(ResourceLocation id)
        {
            this.id = id;
        }

        protected abstract Set<String> getModDependencies();

        @Override
        public ResourceLocation getId()
        {
            return id;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            Set<String> modDeps = getModDependencies();
            for (String mod : modDeps)
            {
                if (!StringUtils.equalsAny(mod, "minecraft", "forge", "witherutils", getId().getNamespace()))
                    conditions.add(new ModLoadedCondition(mod));
            }

            if (!conditions.isEmpty())
            {
                JsonArray jsonConditions = new JsonArray();
                for (ICondition condition : conditions)
                {
                    jsonConditions.add(CraftingHelper.serialize(condition));
                }
                json.add("conditions", jsonConditions);
            }
        }

        public void addCondition(ICondition condition)
        {
            conditions.add(condition);
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement()
        {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId()
        {
            return null;
        }
    }
}
