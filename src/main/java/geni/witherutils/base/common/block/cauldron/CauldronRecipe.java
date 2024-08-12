package geni.witherutils.base.common.block.cauldron;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.core.common.recipe.OutputStack;
import geni.witherutils.core.common.recipe.WitherRecipe;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class CauldronRecipe implements WitherRecipe<RecipeInput> {

    private final Ingredient input;
    private final FluidStack output;
    private final int experience;
    private final int timer;

	public CauldronRecipe(Ingredient input, FluidStack output, int experience, int timer)
	{
        this.input = input;
        this.output = output;
        this.experience = experience;
        this.timer = timer;
	}
	
	@Override
	public double getBaseSaturationCost() { return 0; }
	@Override
	public int getBaseEnergyCost() { return 0; }
	
	@Override
	public List<OutputStack> craft(RecipeInput container, RegistryAccess registryAccess) { return null; }
	@Override
	public List<OutputStack> getResultStacks(RegistryAccess registryAccess) { return null; }
	
	@Override
	public boolean matches(RecipeInput recipeInput, Level p_345375_)
	{
    	return input.test(recipeInput.getItem(0));
	}
	
	@Override
	public ItemStack assemble(RecipeInput p_345149_, Provider p_346030_)
	{
        return null;
	}
	
	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
        return true;
	}
	
	@Override
	public ItemStack getResultItem(Provider pRegistries)
	{
		return ItemStack.EMPTY;
	}
	
    public Ingredient input()
    {
        return input;
    }
    
    public FluidStack output()
    {
        return output;
    }
    
    public int experience()
    {
        return experience;
    }
    
    public int timer()
    {
        return timer;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return WUTRecipes.CAULDRON.serializer().get();
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return WUTRecipes.CAULDRON.type().get();
    }

    public record RecipeIn(ItemStack input) implements RecipeInput
    {
        @Override
        public ItemStack getItem(int slotIndex)
        {
            return input;
        }

        @Override
        public int size()
        {
            return 1;
        }
    }

    public static class Serializer implements RecipeSerializer<CauldronRecipe>
    {
        public static final MapCodec<CauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
    		Ingredient.CODEC.fieldOf("input").forGetter(CauldronRecipe::input),
    		FluidStack.CODEC.fieldOf("output").forGetter(CauldronRecipe::output),
            Codec.INT.fieldOf("timer").forGetter(CauldronRecipe::experience),
            Codec.INT.fieldOf("experience").forGetter(CauldronRecipe::timer))
    		.apply(inst, CauldronRecipe::new));
        
        public static final StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, CauldronRecipe::input,
            FluidStack.STREAM_CODEC, CauldronRecipe::output,
            ByteBufCodecs.INT, CauldronRecipe::experience,
            ByteBufCodecs.INT, CauldronRecipe::timer,
            CauldronRecipe::new);

        @Override
        public MapCodec<CauldronRecipe> codec()
        {
            return CODEC;
        }
        
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }
    }
}
