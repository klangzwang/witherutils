//package geni.witherutils.base.common.block.farmer;
//
//import java.lang.ref.WeakReference;
//import java.util.UUID;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//import com.mojang.authlib.GameProfile;
//
//import geni.witherutils.api.farm.FarmNotification;
//import geni.witherutils.api.farm.IFarmer;
//import geni.witherutils.api.farm.IFarmingTool;
//import geni.witherutils.core.common.util.ItemUtil;
//import geni.witherutils.core.common.util.NNList.ShortCallback;
//import geni.witherutils.core.common.util.UserIdent;
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.enchantment.EnchantmentHelper;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.common.util.FakePlayer;
//import net.minecraftforge.common.util.FakePlayerFactory;
//import net.minecraftforge.registries.ForgeRegistries;
//
//public class FarmerLogic implements IFarmer {
//
//	private class InsertCallback implements ShortCallback<FarmerSlots>
//	{
//		private final @Nonnull ItemStack stack;
//		private FarmerSlots emptySlot = null;
//
//		public InsertCallback(@Nonnull ItemStack stack)
//		{
//			this.stack = stack;
//		}
//
//		@Override
//		public boolean apply(@Nonnull FarmerSlots slot)
//		{
//			if (slot.isValid(owner, stack))
//			{
//				ItemStack slotStack = slot.get(owner);
//				if (slotStack.isEmpty())
//				{
//					if (emptySlot == null)
//					{
//						emptySlot = slot;
//					}
//				}
//				else if (ItemUtil.areStackMergable(stack, slotStack) && !ItemUtil.isStackFull(slotStack))
//				{
//					int addable = Math.max(0, Math.min(slotStack.getMaxStackSize(), slot.getInventoryStackLimit(owner)) - slotStack.getCount());
//					if (addable >= stack.getCount())
//					{
//						slotStack.grow(stack.getCount());
//						stack.setCount(0);
//						owner.setChanged();
//					}
//					else
//					{
//						slotStack.grow(addable);
//						stack.shrink(addable);
//						owner.setChanged();
//					}
//				}
//			}
//			return stack.isEmpty();
//		}
//
//		@Override
//		public boolean finish()
//		{
//			if (emptySlot != null)
//			{
//				emptySlot.set(owner, stack.copy());
//				stack.setCount(0);
//				owner.setChanged();
//			}
//			return stack.isEmpty();
//		}
//	}
//
//	private final @Nonnull FarmerBlockEntity owner;
//	private final @Nonnull WeakReference<FakePlayer> farmerJoe;
//	private @Nonnull UserIdent fpOwner = UserIdent.NOBODY;
//	
//	public FarmerLogic(@Nonnull FarmerBlockEntity owner)
//	{
//	    this.owner = owner;
//	    farmerJoe = new WeakReference<FakePlayer>(FakePlayerFactory.get((ServerLevel) owner.getLevel(), new GameProfile(UUID.randomUUID(), "fp")));
//	    setOwner(fpOwner);
//	}
//
//	public UUID getOwner()
//	{
//		return fpOwner == UserIdent.NOBODY ? null : fpOwner.getUUID();
//	}
//	public @Nonnull WeakReference<FakePlayer> setOwner(@Nullable UserIdent fpOwner)
//	{
//		this.fpOwner = owner == null ? UserIdent.NOBODY : fpOwner;
//		return farmerJoe;
//	}
//
//	@Override
//	@Nonnull
//	public WeakReference<FakePlayer> getFakePlayer()
//	{
//		return farmerJoe;
//	}
//
//	@Override
//	@Nonnull
//	public Level getWorld()
//	{
//		return owner.getLevel();
//	}
//
//	@Override
//	@Nonnull
//	public BlockPos getLocation()
//	{
//		return owner.getBlockPos();
//	}
//
//	@Override
//	@Nonnull
//	public BlockState getBlockState(@Nonnull BlockPos pos)
//	{
//		if (getWorld().isLoaded(pos))
//		{
//			return getWorld().getBlockState(pos);
//		}
//		else
//		{
//			return Blocks.AIR.defaultBlockState();
//		}
//	}
//
//	@Override
//	public int getFarmSize()
//	{
//		return owner.getFarmSize();
//	}
//
//	@Override
//	public void setNotification(@Nonnull FarmNotification notification)
//	{
//		owner.setNotification(notification);
//	}
//
//	@Override
//	@Nonnull
//	public ItemStack getSeedTypeInSuppliesFor(@Nonnull BlockPos pos)
//	{
//		return mapBlockPosToSeedSlot(pos).get(owner);
//	}
//
//	private @Nonnull FarmerSlots mapBlockPosToSeedSlot(@Nonnull BlockPos pos)
//	{
//		BlockPos offset = pos.subtract(getLocation());
//		if (offset.getX() <= 0 && offset.getZ() > 0)
//		{
//			return FarmerSlots.SEED1;
//		}
//		else if (offset.getX() > 0 && offset.getZ() >= 0)
//		{
//			return FarmerSlots.SEED2;
//		}
//		else if (offset.getX() < 0 && offset.getZ() <= 0)
//		{
//			return FarmerSlots.SEED3;
//		}
//		return FarmerSlots.SEED4;
//	}
//
//	@Override
//	@Nonnull
//	public ItemStack takeSeedFromSupplies(@Nonnull ItemStack seeds, @Nonnull BlockPos pos, boolean simulate)
//	{
//		FarmerSlots slot = mapBlockPosToSeedSlot(pos);
//		ItemStack inv = slot.get(owner);
//		if (!inv.isEmpty() && (seeds.isEmpty() || ItemUtil.areStacksEqual(seeds, inv)))
//		{
//			if (inv.getCount() > 1 || !owner.isSlotLocked(slot))
//			{
//				if (simulate)
//				{
//					return inv.copy().split(1);
//				}
//				else
//				{
//					owner.setChanged();
//					return inv.split(1);
//				}
//			}
//		}
//		return ItemStack.EMPTY;
//	}
//
//	@Override
//	public boolean hasSeed(@Nonnull ItemStack seeds, @Nonnull BlockPos pos)
//	{
//		return ItemUtil.areStacksEqual(seeds, mapBlockPosToSeedSlot(pos).get(owner));
//	}
//
//	@Override
//	public int isLowOnSaplings(@Nonnull BlockPos pos)
//	{
//		return 90 * (FarmConfig.farmSaplingReserveAmount.get() - getSeedTypeInSuppliesFor(pos).getCount()) / FarmConfig.farmSaplingReserveAmount.get();
//	}
//
//	@Override
//	public boolean isSlotLocked(@Nonnull BlockPos pos)
//	{
//		return owner.isSlotLocked(mapBlockPosToSeedSlot(pos));
//	}
//
//	@Override
//	public boolean hasTool(@Nonnull IFarmingTool tool)
//	{
//		return !getTool(tool).isEmpty();
//	}
//
//	@Override
//	public int getLootingValue(@Nonnull IFarmingTool tool)
//	{
//		ItemStack stack = getTool(tool);
//		return Math.max(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, stack), EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack));
//	}
//
//	@Override
//	@Nonnull
//	public ItemStack getTool(@Nonnull IFarmingTool tool)
//	{
//		FarmerSlots slot = owner.getSlotForTool(tool);
//		if (slot != null)
//		{
//			ItemStack stack = slot.get(owner);
//			if (TicProxy.isBroken(stack) || FarmingTool.isDryRfTool(stack))
//			{
//				if (!joeInUse)
//				{
//					handleExtraItem(stack, null);
//				}
//			}
//			else if (tool.itemMatches(stack))
//			{
//				switch (tool(tool))
//				{
//					case AXE:
//						owner.removeNotification(FarmNotification.NO_AXE);
//						break;
//					case HOE:
//						owner.removeNotification(FarmNotification.NO_HOE);
//						break;
//					case TREETAP:
//						owner.removeNotification(FarmNotification.NO_TREETAP);
//						break;
//					default:
//						break;
//				}
//				return stack;
//			}
//		}
//		return ItemStack.EMPTY;
//	}
//
//	@Override
//	public void handleExtraItem(@Nonnull ItemStack stack, @Nullable BlockPos drop)
//	{
//		if (Prep.isValid(stack)) {
//			if (drop == null) {
//				drop = getLocation();
//			}
//			InsertCallback insertCallback = new InsertCallback(stack);
//			insertCallback.apply(mapBlockPosToSeedSlot(drop));
//			if (Prep.isValid(stack)) {
//				FarmSlots.SEEDS.apply(insertCallback);
//				if (Prep.isValid(stack)) {
//					FarmSlots.OUTPUTS.apply(insertCallback);
//					if (Prep.isValid(stack)) {
//						if (FarmConfig.useOutputQueue.get()) {
//							owner.enQueueOverflow(stack.copy());
//						} else {
//							Block.spawnAsEntity(getWorld(), drop, stack.copy());
//						}
//						stack.setCount(0);
//					}
//				}
//			}
//		}
//	}
//
//	// Item use
//
//	private boolean joeInUse = false;
//	private FarmSlots joeHasTool = null;
//
//	@Override
//	public @Nonnull FakePlayerEIO startUsingItem(@Nonnull ItemStack stack) {
//		cleanJoe();
//		joeInUse = true;
//		farmerJoe.setHeldItem(EnumHand.MAIN_HAND, stack);
//		return farmerJoe;
//	}
//
//	private void cleanJoe() {
//		if (joeInUse) {
//			removeJoesTool();
//			endUsingItem(false).apply(new Callback<ItemStack>() {
//				@Override
//				public void apply(@Nonnull ItemStack istack) {
//					handleExtraItem(istack, getLocation());
//				}
//			});
//		}
//	}
//
//	private void removeJoesTool() {
//		if (joeHasTool != null) {
//			joeHasTool.set(owner, farmerJoe.getHeldItem(EnumHand.MAIN_HAND));
//			farmerJoe.setHeldItem(EnumHand.MAIN_HAND, Prep.getEmpty());
//			joeHasTool = null;
//		}
//	}
//
//	@Override
//	public @Nonnull FakePlayerEIO startUsingItem(@Nonnull IFarmingTool tool) {
//		cleanJoe();
//		joeInUse = true;
//		ItemStack toolStack = getTool(tool);
//		for (FarmSlots slot : FarmSlots.TOOLS) {
//			if (slot.get(owner) == toolStack) { // sic! identity check
//				joeHasTool = slot;
//			}
//		}
//		farmerJoe.setHeldItem(EnumHand.MAIN_HAND, toolStack);
//		return farmerJoe;
//	}
//
//	@Override
//	@Nonnull
//	public NNList<ItemStack> endUsingItem(boolean trashHandItem) {
//		NNList<ItemStack> result = new NNList<>();
//		for (int i = 0; i < farmerJoe.inventory.getSizeInventory(); i++) {
//			ItemStack stack = farmerJoe.inventory.removeStackFromSlot(i);
//			if (Prep.isValid(stack)) {
//				result.add(stack);
//			}
//		}
//		joeInUse = false;
//		return result;
//	}
//
//	@Override
//	@Nonnull
//	public NNList<ItemStack> endUsingItem(@Nonnull IFarmingTool tool) {
//		removeJoesTool();
//		final NNList<ItemStack> items = endUsingItem(false);
//		ItemStack toolStack = getTool(tool);
//		if (TicProxy.isBroken(toolStack) || FarmingTool.isDryRfTool(toolStack)) {
//			handleExtraItem(toolStack, null);
//		}
//		return items;
//	}
//
//	// Result Handling
//
//	@Override
//	public void handleExtraItems(@Nonnull NonNullList<ItemStack> items, @Nullable BlockPos pos) {
//		NNList.wrap(items).apply(new Callback<ItemStack>() {
//			@Override
//			public void apply(@Nonnull ItemStack stack) {
//				handleExtraItem(stack, NullHelper.first(pos, getLocation()));
//			}
//		});
//	}
//
//	// Actions
//
//	@Override
//	public boolean checkAction(@Nonnull FarmingAction action, @Nonnull IFarmingTool tool) {
//		if (tool != FarmingTool.HAND && !hasTool(tool)) {
//			switch (tool(tool)) {
//			case AXE:
//				setNotification(FarmNotification.NO_AXE);
//				break;
//			case HOE:
//				setNotification(FarmNotification.NO_HOE);
//				break;
//			case SHEARS:
//				setNotification(FarmNotification.NO_SHEARS);
//				break;
//			case TREETAP:
//				setNotification(FarmNotification.NO_TREETAP);
//				break;
//			default:
//				break;
//			}
//			return false;
//		}
//		return owner.getEnergyStored() >= getEnergyUse(action, tool);
//	}
//
//	@Override
//	public void registerAction(@Nonnull FarmingAction action, @Nonnull IFarmingTool tool) {
//		owner.usePower(getEnergyUse(action, tool));
//	}
//
//	private int getEnergyUse(@Nonnull FarmingAction action, @Nonnull IFarmingTool tool) {
//		switch (action) {
//		case FERTILIZE:
//			return FarmConfig.farmBonemealEnergyUseSuccess.get();
//		case HARVEST:
//			return tool == FarmingTool.AXE ? FarmConfig.farmHarvestAxeEnergyUse.get()
//					: FarmConfig.farmHarvestEnergyUse.get();
//		case PLANT:
//			return FarmConfig.farmPlantEnergyUse.get();
//		case TILL:
//			return FarmConfig.farmTillEnergyUse.get();
//		default:
//			return 0;
//		}
//	}
//
//	@Override
//	public void registerAction(@Nonnull FarmingAction action, @Nonnull IFarmingTool tool, @Nonnull IBlockState state,
//			@Nonnull BlockPos pos) {
//		registerAction(action, tool);
//		ItemStack toolStack = getTool(tool);
//		if (Prep.isValid(toolStack) && getWorld().rand.nextFloat() <= FarmConfig.farmToolDamageChance.get()) {
//			FarmSlots toolSlot = null;
//			for (FarmSlots slot : FarmSlots.TOOLS) {
//				if (slot.get(owner) == toolStack) { // sic! identity check
//					toolSlot = slot;
//				}
//			}
//			boolean damageHandItem = false;
//			ItemStack restoreJoe = Prep.getEmpty();
//			if (joeInUse) {
//				if (toolStack == farmerJoe.getHeldItem(EnumHand.MAIN_HAND)) {
//					// good case
//					damageHandItem = true;
//				} else {
//					// bad case
//					restoreJoe = farmerJoe.getHeldItem(EnumHand.MAIN_HAND);
//					farmerJoe.setHeldItem(EnumHand.MAIN_HAND, Prep.getEmpty());
//				}
//			} else if (Prep.isValid(farmerJoe.getHeldItem(EnumHand.MAIN_HAND))) {
//				handleExtraItem(farmerJoe.getHeldItem(EnumHand.MAIN_HAND), null);
//				farmerJoe.setHeldItem(EnumHand.MAIN_HAND, Prep.getEmpty());
//			}
//			if (!damageHandItem) {
//				farmerJoe.setHeldItem(EnumHand.MAIN_HAND, toolStack);
//			}
//
//			boolean canDamage = FarmingTool.canDamage(toolStack);
//			switch (tool(tool)) {
//			case AXE:
//				toolStack.getItem().onBlockDestroyed(toolStack, getWorld(), state, pos, farmerJoe);
//				break;
//			case HAND:
//				break;
//			case HOE:
//				int origDamage = toolStack.getItemDamage();
//				toolStack.getItem().onItemUse(farmerJoe, getWorld(), pos, EnumHand.MAIN_HAND, EnumFacing.UP, 0.5f, 0.5f,
//						0.5f);
//				ItemStack newToolStack = farmerJoe.getHeldItem(EnumHand.MAIN_HAND);
//				if (origDamage == newToolStack.getItemDamage() && canDamage) {
//					newToolStack.damageItem(1, farmerJoe);
//				}
//				if (newToolStack != toolStack) {
//					if (damageHandItem && toolSlot != null) {
//						toolSlot.set(owner, newToolStack);
//						toolStack = getTool(tool);
//						farmerJoe.setHeldItem(EnumHand.MAIN_HAND, toolStack);
//					} else {
//						toolStack = newToolStack;
//					}
//				}
//				break;
//			case NONE:
//				break;
//			case SHEARS:
//			case TREETAP:
//			default:
//				if (canDamage) {
//					toolStack.damageItem(1, farmerJoe);
//				}
//				break;
//			}
//
//			if (damageHandItem) {
//				owner.markDirty();
//				return;
//			}
//			farmerJoe.setHeldItem(EnumHand.MAIN_HAND, restoreJoe);
//			if (toolSlot != null) {
//				toolSlot.set(owner, toolStack);
//				owner.markDirty();
//			}
//		}
//	}
//
//	// Tool methods
//
//	@Override
//	public boolean tillBlock(@Nonnull BlockPos pos) {
//		BlockPos dirtLoc = pos.down();
//		IBlockState dirtBlockState = getBlockState(pos);
//		Block dirtBlock = dirtBlockState.getBlock();
//		if (dirtBlock == Blocks.FARMLAND) {
//			return true;
//		} else {
//			if (!checkAction(FarmingAction.TILL, FarmingTool.HOE)) {
//				return false;
//			}
//			ItemStack toolStack = getTool(FarmingTool.HOE);
//
//			if (toolStack.getItem() instanceof ItemDarkSteelHand) {
//				toolStack = new ItemStack(Items.DIAMOND_HOE);
//			} else {
//				toolStack = toolStack.copy();
//			}
//
//			ItemStack heldItem = farmerJoe.getHeldItem(EnumHand.MAIN_HAND);
//			farmerJoe.setHeldItem(EnumHand.MAIN_HAND, toolStack);
//			EnumActionResult itemUse = toolStack.getItem().onItemUse(farmerJoe, farmerJoe.world, dirtLoc,
//					EnumHand.MAIN_HAND, EnumFacing.UP, 0.5f, 0.5f, 0.5f);
//			farmerJoe.setHeldItem(EnumHand.MAIN_HAND, Prep.getEmpty());
//
//			boolean temp = joeInUse;
//			endUsingItem(false).apply(new Callback<ItemStack>() {
//				@Override
//				public void apply(@Nonnull ItemStack istack) {
//					handleExtraItem(istack, getLocation());
//				}
//			});
//			joeInUse = temp;
//
//			farmerJoe.setHeldItem(EnumHand.MAIN_HAND, heldItem);
//			if (itemUse != EnumActionResult.SUCCESS) {
//				return false;
//			}
//
//			getWorld().playSound(dirtLoc.getX() + 0.5F, dirtLoc.getY() + 0.5F, dirtLoc.getZ() + 0.5F,
//					SoundEvents.BLOCK_GRASS_STEP, SoundCategory.BLOCKS,
//					(Blocks.FARMLAND.getSoundType().getVolume() + 1.0F) / 2.0F,
//					Blocks.FARMLAND.getSoundType().getPitch() * 0.8F, false);
//
//			PacketHandler.sendToAllAround(new PacketFarmAction(pos), owner);
//
//			registerAction(FarmingAction.TILL, FarmingTool.HOE, dirtBlockState, dirtLoc);
//			return true;
//		}
//	}
//
//	private @Nonnull FarmingTool tool(@Nonnull IFarmingTool tool)
//	{
//		return (tool instanceof FarmingTool) ? (FarmingTool) tool : FarmingTool.NONE;
//	}
//}
