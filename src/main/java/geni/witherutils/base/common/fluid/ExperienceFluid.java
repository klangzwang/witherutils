package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class ExperienceFluid extends ForgeFlowingFluid {

	public static final ForgeFlowingFluid.Properties PROPERTIES =
			new ForgeFlowingFluid.Properties(
					() -> WUTFluids.EXPERIENCE_TYPE.get(), () -> WUTFluids.EXPERIENCE.get(), () -> WUTFluids.EXPERIENCE_FLOWING.get()
					).bucket(() -> WUTItems.EXPERIENCE_BUCKET.get()).block(() -> WUTBlocks.EXPERIENCE_MOLTEN.get());

	private ExperienceFluid()
	{
		super(PROPERTIES);
	}

	@Override
	public Vec3 getFlow(BlockGetter world, BlockPos pos, FluidState fluidstate)
	{
		return super.getFlow(world, pos, fluidstate).scale(4);
	}

	public static class Source extends ExperienceFluid
	{
		public Source()
		{
			super();
		}
		public int getAmount(FluidState state)
		{
			return 8;
		}
		public boolean isSource(FluidState state)
		{
			return true;
		}
	}
	public static class Flowing extends ExperienceFluid
	{
		public Flowing()
		{
			super();
		}
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder)
		{
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}
		public int getAmount(FluidState state)
		{
			return state.getValue(LEVEL);
		}
		public boolean isSource(FluidState state)
		{
			return false;
		}
	}
}