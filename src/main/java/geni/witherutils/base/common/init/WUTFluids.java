package geni.witherutils.base.common.init;

import java.util.function.Consumer;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.fluid.ExperienceFluid;
import geni.witherutils.base.common.fluid.FertilizerFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTFluids {

	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, WitherUtils.MODID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, WitherUtils.MODID);

	public static final RegistryObject<ForgeFlowingFluid> FERTILIZER = FLUIDS.register("fertilizer", () -> new FertilizerFluid.Source());
	public static final RegistryObject<ForgeFlowingFluid> FERTILIZER_FLOWING = FLUIDS.register("fertilizer_flowing", () -> new FertilizerFluid.Flowing());
	
	public static final RegistryObject<ForgeFlowingFluid> EXPERIENCE = FLUIDS.register("experience", () -> new ExperienceFluid.Source());
	public static final RegistryObject<ForgeFlowingFluid> EXPERIENCE_FLOWING = FLUIDS.register("experience_flowing", () -> new ExperienceFluid.Flowing());
    
    /*
     * 
     * FLUID PROPS
     * 
     */
	public static FluidType.Properties EXPERIENCE_PROP = FluidType.Properties.create()
    		.lightLevel(15)
    		.viscosity(100)
    		.temperature(50);

	public static FluidType.Properties FERTILIZER_PROP = FluidType.Properties.create();
	
    /*
     * 
     * FLUID TYPES
     * 
     */
	public static final RegistryObject<FluidType> EXPERIENCE_TYPE = FLUID_TYPES.register("experience", () -> new FluidType(EXPERIENCE_PROP)
	{
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions() {
				@Override
				public ResourceLocation getStillTexture() { return WitherUtils.loc("block/fluid/experience_still"); }
				@Override
				public ResourceLocation getFlowingTexture() { return WitherUtils.loc("block/fluid/experience_flow"); }
				@Override
				public ResourceLocation getOverlayTexture() { return null; }
			});
		}
	});
	
	public static final RegistryObject<FluidType> FERTILIZER_TYPE = FLUID_TYPES.register("fertilizer", () -> new FluidType(FERTILIZER_PROP)
	{
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions() {
				@Override
				public ResourceLocation getStillTexture() { return WitherUtils.loc("block/fluid/fertilizer_still"); }
				@Override
				public ResourceLocation getFlowingTexture() { return WitherUtils.loc("block/fluid/fertilizer_flow"); }
				@Override
				public ResourceLocation getOverlayTexture() { return null; }
			});
		}
	});
}
