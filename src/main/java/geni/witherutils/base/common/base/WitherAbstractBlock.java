package geni.witherutils.base.common.base;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.client.ClientTooltipHandler;
import geni.witherutils.base.common.item.soulbank.SoulBankItem;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;

public abstract class WitherAbstractBlock extends Block {
	
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

	private boolean hasScreen = false;
	private boolean hasScreenExtra = false;
	private boolean hasLiquid = false;
	private boolean hasSoulBankSlot = false;
	private boolean hasTooltip = false;

    protected int lightOpacity;
	protected final boolean notNormalBlock;
	
    public WitherAbstractBlock(Properties properties)
    {
        super(properties);
		lightOpacity = -1;
		this.notNormalBlock = !defaultBlockState().canOcclude();
    }
    
    protected WitherAbstractBlock setLightOpacity(int opacity)
	{
		lightOpacity = opacity;
		return this;
	}
	protected WitherAbstractBlock setHasScreen()
	{
		this.hasScreen = true;
		return this;
	}
	protected WitherAbstractBlock setHasScreenExtra()
	{
	    this.hasScreenExtra = true;
	    return this;
	}
	protected WitherAbstractBlock setHasLiquid()
	{
		this.hasLiquid = true;
		return this;
	}
    protected WitherAbstractBlock setHasSoulBankSlot()
    {
        this.hasSoulBankSlot = true;
        return this;
    }
    protected WitherAbstractBlock setHasTooltip()
    {
        this.hasTooltip = true;
        return this;
    }
    
	@Override
	@SuppressWarnings("deprecation")
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		if(this.lightOpacity != -1)
			return this.lightOpacity;
		else if(notNormalBlock)
			return 0;
		else
			return super.getLightBlock(state, worldIn, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos)
	{
		return notNormalBlock ? 1 : super.getShadeBrightness(state, world, pos);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return notNormalBlock || super.propagatesSkylightDown(state, reader, pos);
	}
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        if(context.getLevel().getBlockState(context.getClickedPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        
        else if(context.getLevel().getBlockState(context.getClickedPos()).hasProperty(HorizontalDirectionalBlock.FACING))
        {
            if (context.getClickedFace().getAxis() == Direction.Axis.Y)
                return this.defaultBlockState().setValue(FACING, Direction.NORTH);
            return this.defaultBlockState().setValue(FACING, context.getClickedFace());
        }
        
        else
            return super.defaultBlockState();
    }

    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    @SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> list, TooltipFlag flag)
    {
        if(hasTooltip)
            ClientTooltipHandler.Tooltip.addInformation(stack, world, list, flag, true);
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return state.getBlock() instanceof EntityBlock;
    }

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof WitherMachineBlockEntity wbe)
			{
				if(wbe != null && wbe.getInventory() != null)
				{
					for(int i = 0; i < wbe.getInventory().getSlots(); i++)
					{
						Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), wbe.getInventory().getStackInSlot(i));
					}
				}
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

    @SuppressWarnings("deprecation")
	@Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int type)
    {
        super.triggerEvent(state, level, pos, id, type);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, type);
    }

	@SuppressWarnings("unchecked")
	@Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type1, BlockEntityType<E> type2, BlockEntityTicker<? super E> ticker)
    {
        return type2 == type1 ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type)
    {
        return false;
    }
    
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
	    if(hasSoulBankSlot)
	    {
			if(!world.isClientSide)
			{
	            BlockEntity be = world.getBlockEntity(pos);
	            if (be instanceof WitherMachineEnergyBlockEntity)
	            {
	                WitherMachineEnergyBlockEntity mebe = (WitherMachineEnergyBlockEntity) be;
	                if(mebe.getInventory().getStackInSlot(mebe.getSoulBankSlot()).isEmpty())
	                {
	                    if(!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof SoulBankItem)
	                    {
	                        ItemStack excess = mebe.getInventory().insertItem(mebe.getSoulBankSlot(), player.getItemInHand(hand), false);
	                        player.setItemInHand(hand, excess);
	                        return InteractionResult.SUCCESS;
	                    }
	                }
	            }				
			}
	    }
		if(hasLiquid)
		{
			if(!world.isClientSide)
			{
				BlockEntity tankHere = world.getBlockEntity(pos);
				if(tankHere != null)
				{
					IFluidHandler handler = tankHere.getCapability(ForgeCapabilities.FLUID_HANDLER, hit.getDirection()).orElse(null);
					if(handler != null)
					{
						if(FluidUtil.interactWithFluidHandler(player, hand, handler))
						{
							if(handler.getFluidInTank(0) != null)
							{
								player.displayClientMessage(Component.translatable(getFluidRatioName(handler)), true);
							}
							if(player instanceof ServerPlayer)
							{
								SoundUtil.playSoundFromServer((ServerPlayer) player, SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
							}
						}
						else
						{
							player.displayClientMessage(Component.translatable(getFluidRatioName(handler)), true);
						}
					}
				}
			}
			if(FluidUtil.getFluidHandler(player.getItemInHand(hand)).isPresent())
			{
				return InteractionResult.SUCCESS;
			}
		}
		if(this.hasScreen)
		{
			if(!world.isClientSide)
			{
				BlockEntity tileEntity = world.getBlockEntity(pos);
				if(tileEntity instanceof MenuProvider)
				{
					NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
				}
				else
				{
					throw new IllegalStateException("Our named container provider is missing!");
				}
			}
			return InteractionResult.SUCCESS;
		}
	    if(this.hasScreenExtra)
	    {
	        BlockEntity be = world.getBlockEntity(pos);
	        if (be instanceof WitherBlockEntity)
	        {
	            ((WitherBlockEntity) be).use(state, world, pos, player, hand, hit);
	                return InteractionResult.SUCCESS;
	        }
        }

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IInteractBlockEntity)
        {
            return ((IInteractBlockEntity) tile).onBlockUse(state, player, hand, hit);
        }
        
		return super.use(state, world, pos, player, hand, hit);
	}

	public static String getFluidRatioName(IFluidHandler handler)
	{
		String ratio = handler.getFluidInTank(0).getAmount() + "/" + handler.getTankCapacity(0);
		if (!handler.getFluidInTank(0).isEmpty())
		{
			ratio += " " + handler.getFluidInTank(0).getDisplayName().getString();
		}
		return ratio;
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
	{
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof WitherBlockEntity)
        {
            ((WitherBlockEntity) be).onBlockPlacedBy(level, pos, state, entity, stack);
        }
	}
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof WitherBlockEntity)
        {
            ((WitherBlockEntity) be).onAdded(level, state, oldState, isMoving);
        }
    }
    
    @Override
    public boolean addLandingEffects(@Nonnull BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos bp, @Nonnull BlockState iblockstate, @Nonnull LivingEntity entity, int numberOfParticles)
    {
    	world.sendParticles(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1), bp.getX() + 0.5D, bp.getY() + 1.0D, bp.getZ() + 0.5D, numberOfParticles, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
    	return true;
    }
}