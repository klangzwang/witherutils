package geni.witherutils.base.common.item.wrench;

import java.util.Optional;

import com.mojang.datafixers.util.Either;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.io.ISideConfig;
import geni.witherutils.base.common.base.IWrenchable;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketRotateBlock;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class WrenchItem extends WitherItem {
	
	public WrenchItem()
	{
		super(new Item.Properties().stacksTo(1).durability(250));
        if(FMLEnvironment.dist.isClient())
        {
            registerProperty();
        }
	}

    @OnlyIn(Dist.CLIENT)
    public void registerProperty()
    {
        ItemProperties.register(this, WitherUtils.loc("using"), (stack, world, entity, i) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        });
    }

    public static boolean isHoldingWrench(Player player)
    {
        for(ItemStack stack : player.getHandSlots())
        {
            return stack.getItem() == WUTItems.WRENCH.get();
        }
        return false;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        player.isUsingItem();
        return super.use(level, player, hand);
    }
    
    @SuppressWarnings("resource")
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        context.getPlayer().isUsingItem();
        if (context.getLevel().isClientSide)
        {
            CoreNetwork.sendToServer(new PacketRotateBlock(context.getClickedPos(), context.getClickedFace(), context.getHand()));
            context.getPlayer().playSound(SoundEvents.BLAZE_HURT, 0.1F + context.getLevel().random.nextFloat() * 0.1F, 0.6F + context.getLevel().random.nextFloat() * 0.2F);
            context.getPlayer().swing(context.getHand());
        }
        return super.useOn(context);
    }
	
	@Override
	public int getEnchantmentValue()
	{
		return 0;
	}
	@Override
	public boolean isEnchantable(ItemStack p_41456_)
	{
		return false;
	}
	
    public CompoundTag getWrenchNBT(ItemStack stack)
    {
        return stack.getOrCreateTagElement("wrench");
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return oldStack == null || newStack == null || oldStack.getItem() != newStack.getItem();
    }
    
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext pContext)
    {
        Level level = pContext.getLevel();
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        BlockPos pos = pContext.getClickedPos();

        if(level.getBlockEntity(pos) instanceof IWrenchable wrenchable)
        {
            return wrenchable.onWrenched(pContext);
        }
        
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null)
        {
            LazyOptional<ISideConfig> optSideConfig = be.getCapability(WUTCapabilities.SIDECONFIG, pContext.getClickedFace());
            if (optSideConfig.isPresent())
            {
                if (level.isClientSide())
                    return InteractionResult.sidedSuccess(true);
                optSideConfig.ifPresent(ISideConfig::cycleMode);
                return InteractionResult.SUCCESS;
            }
        }

        BlockState state = level.getBlockState(pContext.getClickedPos());
        Optional<Either<DirectionProperty, EnumProperty<Direction.Axis>>> property = getRotationProperty(state);
        if (property.isPresent())
        {
            BlockState newState = getNextState(pContext, state, property.get());
            pContext.getLevel().setBlock(
                pContext.getClickedPos(),
                newState,
                Block.UPDATE_NEIGHBORS + Block.UPDATE_CLIENTS);
            return InteractionResult.SUCCESS;
        }

        return super.onItemUseFirst(stack,pContext);
    }

    @SuppressWarnings("unchecked")
    private static Optional<Either<DirectionProperty, EnumProperty<Direction.Axis>>> getRotationProperty(BlockState state)
    {
        for (Property<?> property : state.getProperties())
        {
            if (property instanceof DirectionProperty directionProperty && directionProperty.getName().equals("facing"))
            {
                return Optional.of(Either.left(directionProperty));
            }
            if (property instanceof EnumProperty enumProperty && enumProperty.getName().equals("axis") && enumProperty.getValueClass().equals(Direction.Axis.class))
            {
                return Optional.of(Either.right(enumProperty));
            }
        }
        return Optional.empty();
    }

    private static BlockState getNextState(UseOnContext pContext, BlockState state, Either<DirectionProperty, EnumProperty<Direction.Axis>> property)
    {
        return handleProperties(pContext, state, property.left(), property.right());
    }

    private static BlockState handleProperties(UseOnContext pContext, BlockState state, Optional<DirectionProperty> directionProperty, Optional<EnumProperty<Direction.Axis>> axisProperty)
    {
        if (directionProperty.isPresent())
            return handleProperty(pContext, state, directionProperty.get());
        if (axisProperty.isPresent())
            return handleProperty(pContext, state, axisProperty.get());
        throw new IllegalArgumentException("At least one Optional should be set");
    }

    @SuppressWarnings("deprecation")
    private static <T extends Comparable<T>> BlockState handleProperty(UseOnContext pContext, BlockState state, Property<T> property)
    {
        int noValidStateIndex = 0;
        do
        {
            state = getNextBlockState(state, property);
            noValidStateIndex++;
        }
        while (noValidStateIndex != property.getPossibleValues().size() && !state.getBlock().canSurvive(state, pContext.getLevel(), pContext.getClickedPos()));
        return state;
    }

    private static <T extends Comparable<T>> BlockState getNextBlockState(BlockState currentState, Property<T> property)
    {
        return currentState.setValue(property, getNextValue(currentState.getValue(property), property));
    }
    
    private static <T extends Comparable<T>> T getNextValue(T value, Property<T> property)
    {
        boolean foundValid = false;
        for (T possibleValue : property.getPossibleValues())
        {
            if (foundValid)
                return possibleValue;
            foundValid = possibleValue == value;
        }
        return property.getPossibleValues().iterator().next();
    }
}
