package geni.witherutils.base.common.block.deco.cutter;

import geni.witherutils.base.common.base.ICutableBlock;
import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CutterBlock extends WitherAbstractBlock implements ICutableBlock {

	private final Block inputBlock;
	private Enum<CutterBlockType> type = CutterBlockType.UNCONNECTED;
	private final boolean isGlowing;
	private final boolean isGlass;
	private final int itemDamage;
	
	public CutterBlock(BlockBehaviour.Properties props, Block inputBlock, boolean isGlass, boolean isGlowing, Enum<CutterBlockType> type, int itemDamage)
	{
		super(props);
        this.setHasTooltip();
		this.inputBlock = inputBlock;
	    this.isGlass = isGlass;
	    this.isGlowing = isGlowing;
		this.type = type;
		this.itemDamage = itemDamage;
		WUTBlocks.CUTTERBLOCKS.add(this);
	}

	public Block getInputBlock()
	{
		return inputBlock;
	}
	
	public boolean isGlass()
	{
		return isGlass;
	}
	
	public boolean isGlowing()
	{
		return isGlowing;
	}
	
	public Enum<CutterBlockType> getType()
	{
		return type;
	}
	
    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn)
    {
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles)
    {
        return true;
    }

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return !isGlass();
	}
	
	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		return isGlass() ? 1.0F : 0.0f;
	}
	
	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState)
	{
		return true;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side)
	{
		return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
	}
	
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
    {
        return isGlowing() ? 15 : 0;
    }
    
    @Override
    public int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos)
    {
        return isGlowing() ? pLevel.getMaxLightLevel() : 0;
    }
    
    public enum CutterBlockType implements StringRepresentable
    {
    	CONNECTED,
    	UNCONNECTED;

		@Override
		public String getSerializedName()
		{
			return null;
		}
    }

	@Override
	public int getDamageValue()
	{
		return itemDamage;
	}
}
