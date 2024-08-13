package geni.witherutils.base.common.block.fisher;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.api.fisher.FisherNotification;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.init.WUTTags;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.MultiSlotAccess;
import geni.witherutils.core.common.sync.BlockPosDataSlot;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.BlockUtil;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;

public class FisherBlockEntity extends WitherMachineBlockEntity implements MenuProvider {
	
	public static final MultiSlotAccess INPUTS = new MultiSlotAccess();
	public static final MultiSlotAccess OUTPUTS = new MultiSlotAccess();

	public static final int FISHTIMER = BlocksConfig.FISHINGTIMER.get();
	public static final int COOLDOWN = BlocksConfig.FISHINGCOOLDOWN.get();

    private int timer;
	private boolean autofeed;
	
	private FisherBlockEntity master = null;
	private boolean isMaster;
	
	private BlockPos hookPosition = BlockPos.ZERO;
	
	private final @Nonnull Set<FisherNotification> notification = EnumSet.noneOf(FisherNotification.class);
	
	public FisherBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.FISHER.get(), pos, state);
		
        add2WayDataSlot(new BooleanDataSlot(this::getAutofeed, this::setAutofeed, SyncMode.GUI));
        addDataSlot(new BooleanDataSlot(this::isMaster, p -> isMaster = p, SyncMode.WORLD));
        addDataSlot(new BlockPosDataSlot(this::getHookPosition, p -> hookPosition = p, SyncMode.WORLD));
		addDataSlot(new IntegerDataSlot(this::getTimer, p -> timer = p, SyncMode.WORLD));

		addCapabilityProvider(inventory);
	}
	
	@Override
	public boolean canOpenMenu()
	{
		return true;
	}
	
    @Override
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(getIOConfig(), layout)
        {
        	@Override
        	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        	{
            	if(slot == INPUTS.get(0).getIndex() && !stack.getItem().isEdible())
            		return stack;
            	if(slot == INPUTS.get(1).getIndex() && !stack.is(WUTTags.Items.FISHING_RODS))
            		return stack;
				return super.insertItem(slot, stack, simulate);
        	}
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
		if(getMaster() == null)
	    	return super.getCapability(cap, side);

    	if(!isMaster() && cap == ForgeCapabilities.ITEM_HANDLER)
    	{
    		if(getMaster() != null)
    			return getMaster().getCapability(cap, side).cast();
    		else
    			return LazyOptional.empty();
    	}
    	return super.getCapability(cap, side);
    }
    
	@Override
	public void serverTick()
	{
		super.serverTick();

		if(master == null)
			getMasterBe();
		else
			isMaster();

		if(autofeed)
    		getFoodFromOutput();
		
		if(!canAct())
		{
			hookPosition = worldPosition;
    		timer = resetTimer();
			return;
		}

		if(hookPosition == BlockPos.ZERO)
			calcRandomHookPosition();
		
    	if(timer == 0)
    	{
       		this.calcFishingResult(INPUTS.get(1).getItemStack(inventory), worldPosition);
		    calcRandomHookPosition();
		    timer = resetTimer();
    	}

    	if(timer-- > 0)
			return;
    	else
    		timer = 0;
	}
	
	@Override
	public void clientTick()
	{
		super.clientTick();
		
		if(master == null)
			getMasterBe();

		calcNotifications();

		if(hasEnoughWater() > 42)
		{
			if(level.random.nextFloat() < 0.05F)
			{
				FisherBlockType type = FisherBlockType.getType(level.random.nextInt(FisherBlockType.values().length));
				if(type != FisherBlockType.SINGLE && type != FisherBlockType.MASTER)
				{
					BlockPos pos = type.getLocationOfMaster(hookPosition);
					if(pos != null)
					{
						for(int i = 0; i < 10; i++)
						{
						}
						
						level.addParticle(ParticleTypes.SPLASH,
								pos.getX() + 0.5,
								pos.getY(),
								pos.getZ() + 0.5,
								0, 0, 0);
					}
				}
			}
		}
		
		if(!canAct())
			return;

		if(hookPosition != BlockPos.ZERO)
		{
			if(timer == 1)
			{
				SoundUtil.playDistanceSound(Minecraft.getInstance(), level, hookPosition, WUTSounds.WATERHIT.get(), 30);
				SoundUtil.playDistanceSound(Minecraft.getInstance(), level, worldPosition, WUTSounds.RODSPOOL.get(), 30);
				
				Vec3 sourcePos = new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
				Vec3 destPos = new Vec3(hookPosition.getX(), hookPosition.getY(), hookPosition.getZ());

				double x = sourcePos.subtract(destPos).x;
				double z = sourcePos.subtract(destPos).z;
				
				for(int i = 0; i < 12; i++)
				{
					level.addParticle(ParticleTypes.SPLASH,
							getBlockPos().getX() + x + Math.random() + 0.0f,
							getBlockPos().getY() + 0.25f,
							getBlockPos().getZ() + z + Math.random() + 0.0f,
							0, 0, 0);
					level.addParticle(ParticleTypes.BUBBLE,
							getBlockPos().getX() + x + Math.random() + 0.0f,
							getBlockPos().getY() + 0.25f,
							getBlockPos().getZ() + z + Math.random() + 0.0f,
							0, 0, 0);
					level.addParticle(ParticleTypes.FISHING,
							getBlockPos().getX() + x + Math.random() + 0.0f,
							getBlockPos().getY() + 0.25f,
							getBlockPos().getZ() + z + Math.random() + 0.0f,
							0, 0, 0);
				}
			}
		}
	}
	
	public void calcNotifications()
	{
		if (hasEnoughWater() < 42)
		{
			setSingleNotification(FisherNotification.MORE_WATER);
		}
		else
		{
			removeNotification(FisherNotification.MORE_WATER);
			
			if (isOutputFull())
			{
				setSingleNotification(FisherNotification.OUTPUT_FULL);
			}
			else
			{
				removeNotification(FisherNotification.OUTPUT_FULL);
				
				if (INPUTS.get(1).getItemStack(inventory).isEmpty())
				{
					setSingleNotification(FisherNotification.NO_ROD);
				}
				else
				{
					removeNotification(FisherNotification.NO_ROD);
					
					if (INPUTS.get(0).getItemStack(inventory).isEmpty())
					{
						setSingleNotification(FisherNotification.NO_BREAD);
					}
					else
					{
						removeNotification(FisherNotification.NO_BREAD);
						
						if (timer > 0)
						{
							setSingleNotification(FisherNotification.FISHING);
						}
						else
						{
							removeNotification(FisherNotification.FISHING);
						}
					}
				}
			}
		}
	}
	
	private boolean isOutputFull()
	{
		for(int i = 0; i < OUTPUTS.getAccesses().size(); i++)
		{
			if(OUTPUTS.get(i).getItemStack(inventory).isEmpty())
			{
				return false;
			}
		}
		return true;
	}
	
    public void calcRandomHookPosition()
    {
		List<BlockPos> list = new ArrayList<>();
    	
		AABB aabb = new AABB(worldPosition.offset(-2, 0, -2), worldPosition.below().offset(3, 0, 3));
        for(BlockPos pos : BlockUtil.getBlockPosInAABB(aabb))
        {
        	FluidState fluidState = this.level.getFluidState(pos);
        	if (fluidState.getType().equals(Fluids.WATER) && fluidState.isSource())
        	{
        		if(!(level.getBlockState(pos.above()).getBlock() instanceof FisherBlock) &&
        				level.getBlockState(pos).getBlock() == Blocks.WATER)
        		{
        			list.add(pos);
        		}
        	}
        }
        
        if(list.size() > 0)
        	setHookPosition(list.get(level.random.nextInt(list.size())));
        else
        	setHookPosition(worldPosition.below());
    }
	
	public int resetTimer()
	{
		return FISHTIMER + (level.isRaining() ? 450 : 150 + (level.isNight() ? 250 : 0));
	}
	
	@Override
	public boolean canAct()
	{
		if(!INPUTS.get(0).getItemStack(inventory).isEmpty() &&
		   !INPUTS.get(1).getItemStack(inventory).isEmpty() &&
		   !isOutputFull() &&
			hasEnoughWater() > 42)
			return super.canAct();
		return false;
	}
	
    @SuppressWarnings("deprecation")
	public int hasEnoughWater()
    {
    	int amount = 0;
        AABB aabb = new AABB(worldPosition.offset(-4, 0, -4), worldPosition.offset(4, -2, 4));
        for (BlockPos pos : BlockUtil.getBlockPosInAABB(aabb))
        {
            if (!level.hasChunksAt(pos, pos)) continue;
            FluidState fluidState = this.level.getFluidState(pos);
            if (fluidState.getType().equals(Fluids.WATER) && fluidState.isSource())
            {
            	++amount;
            }
        }
        return amount;
    }
	
	private void calcFishingResult(ItemStack fishingRod, BlockPos center)
	{
		if(hookPosition == worldPosition)
			return;
		
		RandomSource rand = level.random;
		
        LootDataManager manager = level.getServer().getLootData();
        if (manager == null)
        	return;
        
        LootTable table = manager.getElement(LootDataType.TABLE, BuiltInLootTables.FISHING);
        if (table == null)
        	return;

        int luck = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FISHING_LUCK, fishingRod) + 1;
        
        LootParams lootContext = new LootParams.Builder((ServerLevel) level).withLuck(luck).withParameter(LootContextParams.ORIGIN,
                new Vec3(center.getX(), center.getY(), center.getZ())).withParameter(LootContextParams.TOOL, fishingRod).create(LootContextParamSets.FISHING);
        
        List<ItemStack> lootDrops = table.getRandomItems(lootContext);
        if (lootDrops != null && lootDrops.size() > 0)
        {
    		for(ItemStack dropMe : lootDrops)
    		{
    			ItemStackUtil.insertItemMultiSlot(inventory, dropMe, OUTPUTS);
    			if(level.random.nextFloat() < 0.25f)
	        	ItemStackUtil.damageItem(INPUTS.get(1).getItemStack(inventory));
	        	INPUTS.get(0).getItemStack(inventory).shrink(level.random.nextInt(2));
    		}

        	if (fishingRod.isDamageableItem())
        	{
        		int mending = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.MENDING, fishingRod);
        		
        		if (mending == 0)
        		{
        			ItemStackUtil.damageItem(fishingRod);
        		}
        		else
        		{
        			if (rand.nextDouble() < 0.25)
        			{
        				ItemStackUtil.damageItem(fishingRod);
        			}
        			else if (rand.nextDouble() < 0.66)
        			{
        				if (fishingRod.getDamageValue() > 0)
        				{
        					fishingRod.setDamageValue(fishingRod.getDamageValue() - rand.nextInt(2, 5));
        				}
        			}
        		}
        	}
        }
	}

	private void getFoodFromOutput()
    {	
		if(INPUTS.get(0).getItemStack(inventory).isEmpty())
		{
			for(int i = 0; i < OUTPUTS.getAccesses().size(); i++)
			{
				if(OUTPUTS.get(i).getItemStack(inventory).getItem().isEdible())
				{
					ItemStack stack = getInventory().extractItem(i, 1, false);
					ItemHandlerHelper.insertItem(getInventory(), stack, false);
				}
			}
		}
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new FisherContainer(this, playerInventory, i);
    }
	
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout
            .builder()
            .inputSlot(2).slotAccess(INPUTS)
            .outputSlot(9).slotAccess(OUTPUTS)
            .build();
    }
	
	public int getTimer()
	{
		return timer;
	}
    
	public BlockPos getHookPosition()
	{
		return hookPosition;
	}
	public void setHookPosition(BlockPos hookPosition)
	{
		this.hookPosition = hookPosition;
	}
	
	public boolean getAutofeed()
	{
		return autofeed;
	}
	public void setAutofeed(boolean autofeed)
	{
		this.autofeed = autofeed;
	}
	
	@Override
	public void load(CompoundTag tag)
	{
		autofeed = tag.getBoolean("autofeed");
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putBoolean("autofeed", autofeed);
		super.saveAdditional(tag);
	}
    
	/*
	 * 
	 * NOTIFICATION
	 * 
	 */
	public @Nonnull Set<FisherNotification> getNotification()
	{
	    return notification;
	};

	public void setSingleNotification(@Nonnull FisherNotification note)
	{
		setNotification(note);
		for (Iterator<FisherNotification> itr = notification.iterator(); itr.hasNext();)
		{
			if (itr.next() != note)
			{
				itr.remove();
			}
		}
	}
	public void setNotification(@Nonnull FisherNotification note)
	{
		if (!notification.contains(note))
		{
			notification.add(note);
		}
	}
	
	public void removeNotification(FisherNotification note)
	{
		getNotification().remove(note);
	}

	public boolean hasNotification()
	{
		return !getNotification().isEmpty();
	}
	
	/*
	 * 
	 * BlockType Spec.
	 * 
	 */
	public boolean isMaster()
	{
		this.isMaster = this.getBlockState().getValue(FisherBlock.BLOCK_TYPE) == FisherBlockType.MASTER;
		return this.isMaster;
	}
	
	public FisherBlockEntity getMasterBe()
	{
		if(master != null)
			return master;

		master = getMaster();
		return master;
	}
	
	public FisherBlockEntity getMaster()
	{
		if(getBlockState().getValue(FisherBlock.BLOCK_TYPE) == FisherBlockType.MASTER)
			return this;

		BlockPos offset = getBlockState().getValue(FisherBlock.BLOCK_TYPE).getOffsetToMaster();
	    if (offset == null)
	    	return null;

		BlockPos masterPos = getBlockPos().offset(offset.getX(), offset.getY(), offset.getZ());
	    if (!level.isLoaded(masterPos))
	    	return null;
		
	    BlockEntity res = level.getBlockEntity(masterPos);
	    if (res instanceof FisherBlockEntity)
	    {
	    	return (FisherBlockEntity) res;
	    }
	    
		return null;
	}
}
