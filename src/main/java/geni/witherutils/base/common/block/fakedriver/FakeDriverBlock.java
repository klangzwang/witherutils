package geni.witherutils.base.common.block.fakedriver;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.config.common.RestrictionConfig;
import geni.witherutils.base.common.data.PlayerData;
import geni.witherutils.base.common.data.WorldData;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.item.fakejob.IFakeJobItem;
import geni.witherutils.core.common.block.WitherEntityBlock;
import geni.witherutils.core.common.fakeplayer.WUTFakePlayer;
import geni.witherutils.core.common.lib.LogicSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent;

public class FakeDriverBlock extends WitherAbstractBlock implements WitherEntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	private final BiFunction<BlockPos,BlockState,? extends FakeDriverBlockEntity> beFactory;
	
	public FakeDriverBlock(BiFunction<BlockPos,BlockState,? extends FakeDriverBlockEntity> beFactory)
	{
		super(WUTBlocks.standardProps().noOcclusion());
        this.beFactory = beFactory;

		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));

		NeoForge.EVENT_BUS.register(new EventHandler());
	}
	
    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side)
    {
        BlockEntity te = world.getBlockEntity(pos);
        if (state.getBlock() instanceof WitherAbstractBlock && te instanceof WitherMachineBlockEntity)
        {
            Direction direction = LogicSupport.getFacing(state);
            return switch (direction)
            {
                case NORTH, SOUTH -> side == Direction.NORTH || side == Direction.SOUTH;
                case WEST, EAST -> side == Direction.WEST || side == Direction.EAST;
                case DOWN, UP -> side == Direction.DOWN || side == Direction.UP;
            };
        }
        return false;
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        checkRedstone(world, pos);
    }
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(placer != null)
			world.setBlock(pos, state.setValue(FACING, getFacingFromEntity(pos, placer)), 2);
        checkRedstone(world, pos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Player placer = context.getPlayer();
		Direction face = placer.getDirection().getOpposite();
		if (placer.getXRot() > 50) face = Direction.UP;
		else if (placer.getXRot() < -50) face = Direction.DOWN;
		return this.defaultBlockState().setValue(FACING, face);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack pstack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult hitResult)
	{
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if(tileEntity instanceof FakeDriverBlockEntity fakedriver)
		{
			if(!fakedriver.getInventory().getStackInSlot(0).isEmpty())
			{
	        	ItemStack stack = fakedriver.getInventory().extractItem(0, 64, false);
	            ItemEntity entityItem = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, stack);
	            entityItem.setPickUpDelay(20);
	            level.addFreshEntity(entityItem);
	            
	            if(level instanceof ServerLevel serverLevel)
	            {
	            	fakedriver.disableFakePlayer();
					WUTFakePlayer.releaseInstance(serverLevel);
	            }
			}
			if(fakedriver.getInventory().getStackInSlot(0).isEmpty())
			{
                if(!player.getItemInHand(player.getUsedItemHand()).isEmpty() && player.getItemInHand(player.getUsedItemHand()).getItem() instanceof IFakeJobItem)
                {
    				ItemStack excess = fakedriver.getInventory().insertItem(0, player.getItemInHand(player.getUsedItemHand()), false);
    				player.setItemInHand(player.getUsedItemHand(), excess);
    				
    	            if(level instanceof ServerLevel serverLevel)
    	            {
    	            	fakedriver.initFakePlayer(serverLevel, BuiltInRegistries.BLOCK.getKey(this).getPath(), fakedriver);
    	            	fakedriver.setFakePlayerReady(true);
    	            }
                }
			}
		}
        return ItemInteractionResult.SUCCESS;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if(!RestrictionConfig.FAKEPLAYERALLOWED.get())
			return;
		
        @SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;
        
        if(level instanceof ServerLevel serverLevel)
        {
            WorldData data = WorldData.get(serverLevel);
            data.setDirty();

            PlayerData.PlayerSave save = PlayerData.getDataFromPlayer(player);
            
            if (save != null)
            {
            	if(save.NumberOfPlaced == RestrictionConfig.MAXBLOCKSALLOWED.get())
            	{
            		return;
            	}
            	else
            	{
                    save.setNumberOfPlaced(false);
            	}
            }
        }
		super.onPlace(state, level, pos, oldState, isMoving);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
        @SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;
		
        if(worldIn instanceof ServerLevel serverLevel)
        {
            WorldData data = WorldData.get(serverLevel);
            data.setDirty();
            
            PlayerData.PlayerSave save = PlayerData.getDataFromPlayer(player);
            
            if (save != null)
            {
                save.setNumberOfPlaced(true);

            }
        }
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos)
	{
        if(!pLevel.isClientSide() && pLevel instanceof ServerLevel serverLevel)
        {
            WorldData data = WorldData.get(serverLevel);
            data.setDirty();
            
            @SuppressWarnings("resource")
			Player player = Minecraft.getInstance().player;
            PlayerData.PlayerSave save = PlayerData.getDataFromPlayer(player);
            
            if (save != null)
            {
            	if(save.NumberOfPlaced == RestrictionConfig.MAXBLOCKSALLOWED.get())
            	{
            		return false;
            	}
            }
        }
        return RestrictionConfig.FAKEPLAYERALLOWED.get() && super.canSurvive(pState, pLevel, pPos);
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
	{
        Block block = state.getBlock();
        if(block != this)
        {
            return block.getLightEmission(state, level, pos);
        }
        return state.getValue(LIT) ? 6 : 0;
	}
	
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return beFactory.apply(pPos, pState);
    }
    
    /*
     * 
     * REDSTONE
     * 
     */
    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
    }
    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction facing)
    {
    	return state.getSignal(level, pos, facing);
    }
    @Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction facing)
    {
        if(!state.getValue(LIT))
            return 0;
//        	return getRedstoneOutput(blockState, blockAccess, pos, side);
        else
        {
        	if(state.getValue(FACING) != facing.getOpposite())
        		return 0;
        	return 15;
        }
	}

    protected int getInputStrength(Level world, BlockPos pos, Direction side)
    {
        int power = world.getSignal(pos.relative(side), side);
        if (power < 15)
        {
            BlockState blockState = world.getBlockState(pos.relative(side));
            Block b = blockState.getBlock();
            if (b == Blocks.REDSTONE_WIRE)
            {
                power = Math.max(power, blockState.getValue(RedStoneWireBlock.POWER));
            }
        }
        return power;
    }

    @Override
    protected void checkRedstone(Level world, BlockPos pos)
    {
        super.checkRedstone(world, pos);
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof WitherMachineBlockEntity generic)
        {
            Direction inputSide = LogicSupport.getFacing(world.getBlockState(pos));
            int power = getInputStrength(world, pos, inputSide);
            generic.setPowerInput(power);
        }
    }

	/*
	 * 
	 * PLACINGDATA
	 * 
	 */
	public static class EventHandler
	{
		@SubscribeEvent
		public void onPlaceEvent(final EntityPlaceEvent event)
		{
			if(event.getEntity() instanceof ServerPlayer serverPlayer)
			{
				if(event.getPlacedBlock().getBlock() == WUTBlocks.FAKE_DRIVER.get())
				{
		    		WorldData.get(serverPlayer.getCommandSenderWorld()).setDirty();
				}
			}
		}
		@SubscribeEvent
		public void onBreakEvent(final BreakEvent event)
		{
			if(event.getPlayer() instanceof ServerPlayer serverPlayer)
			{
				if(event.getState().getBlock() == WUTBlocks.FAKE_DRIVER.get())
				{
		    		WorldData.get(serverPlayer.getCommandSenderWorld()).setDirty();
				}
			}
		}
	}
}