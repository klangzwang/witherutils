package geni.witherutils.base.common.block.scanner;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.helper.NamedEnum;
import geni.witherutils.core.common.sync.EnumDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.ForgeRegistries;

public class ScannerBlockEntity extends WitherMachineFakeBlockEntity implements MenuProvider {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();
	
    private int number = 0;

    private SensorType sensorType = SensorType.SENSOR_BLOCK;
    private AreaType areaType = AreaType.AREA_1;
    private GroupType groupType = GroupType.GROUP_ONE;
    
    private int checkCounter = 0;
    private AABB cachedBox = null;
    
	public ScannerBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.SCANNER.get(), pos, state);
		add2WayDataSlot(new EnumDataSlot<>(this::getSensorType, this::setSensorType, SyncMode.GUI));
		add2WayDataSlot(new EnumDataSlot<>(this::getAreaType, this::setAreaType, SyncMode.GUI));
		add2WayDataSlot(new EnumDataSlot<>(this::getGroupType, this::setGroupType, SyncMode.GUI));
	}

    @Override
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(getIOConfig(), layout)
        {
        	@Override
        	public int getSlotLimit(int slot) 
        	{
        		return 1;
        	}
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
            {
	      		SoundUtil.playSlotSound(level, worldPosition, SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 2.0f);
	      		return super.insertItem(slot, stack, simulate);
            }
            @Override
	      	protected void onContentsChanged(int slot)
	      	{
                onInventoryContentsChanged(slot);
                setChanged();
	      	};
        };
    }

	@Override
	public void serverTick()
	{
		super.serverTick();

    	if(fakePlayer == null)
            this.fakePlayer = initFakePlayer((ServerLevel) level,
            ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath(), this);

        if (this.getRedstoneControl().isActive(level.hasNeighborSignal(worldPosition)))
        {
            checkCounter--;
            if (checkCounter > 0)
                return;
            checkCounter = 10;
            
            checkSensor();
        }
	}

	@Override
	public void clientTick()
	{
		super.clientTick();
	}

    public boolean checkSensor()
    {
        boolean newout;
        Direction facing = getCurrentFacing();
        Direction inputSide = facing.getOpposite();
        BlockPos newpos = getBlockPos().relative(getCurrentFacing());

        newout = switch (sensorType)
        {
            case SENSOR_BLOCK -> checkBlockOrFluid(newpos, facing, inputSide, this::checkBlock);
            case SENSOR_FLUID -> checkBlockOrFluid(newpos, facing, inputSide, this::checkFluid);
            case SENSOR_GROWTHLEVEL -> checkGrowthLevel(newpos, facing, inputSide);
            case SENSOR_ENTITIES -> checkEntities(newpos, facing, inputSide, Entity.class);
            case SENSOR_PLAYERS -> checkEntities(newpos, facing, inputSide, Player.class);
            case SENSOR_HOSTILE -> checkEntitiesHostile(newpos, facing, inputSide);
            case SENSOR_PASSIVE -> checkEntitiesPassive(newpos, facing, inputSide);
            case SENSOR_ITEMS -> checkEntityItems(newpos, facing, inputSide);
        };
        return newout;
    }
    
    private boolean checkBlockOrFluid(BlockPos newpos, Direction facing, Direction dir, Function<BlockPos, Boolean> blockChecker)
    {
        int blockCount = areaType.getBlockCount();
        if (blockCount > 0)
        {
            Boolean x = checkBlockOrFluidRow(newpos, dir, blockChecker, blockCount);
            if (x != null)
            {
                return x;
            }
        }
        else if (blockCount < 0)
        {
        	for(Direction directions : Direction.values())
        	{
                blockCount = -blockCount;
                Boolean x = checkBlockOrFluidRow(newpos, dir, blockChecker, blockCount);
                if (x != null)
                {
                    return x;
                }

                for (int i = 1 ; i <= (blockCount-1)/2 ; i++)
                {
                    BlockPos p = newpos.relative(directions, i);
                    x = checkBlockOrFluidRow(p, dir, blockChecker, blockCount);
                    if (x != null)
                    {
                        return x;
                    }
                    p = newpos.relative(directions, i);
                    x = checkBlockOrFluidRow(p, dir, blockChecker, blockCount);
                    if (x != null)
                    {
                        return x;
                    }
                }
        	}
        }
        return groupType == GroupType.GROUP_ALL;
    }

    private Boolean checkBlockOrFluidRow(BlockPos newpos, Direction dir, Function<BlockPos, Boolean> blockChecker, int count)
    {
        for (int i = 0; i < count; i++)
        {
            boolean result = blockChecker.apply(newpos);
            if (result && groupType == GroupType.GROUP_ONE)
            {
                return true;
            }
            if ((!result) && groupType == GroupType.GROUP_ALL)
            {
                return false;
            }
            newpos = newpos.relative(dir);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
	private boolean checkBlock(BlockPos newpos)
    {
        BlockState state = level.getBlockState(newpos);
        ItemStack matcher = INPUT.getItemStack(inventory);
        if (matcher.isEmpty())
        {
            return state.canOcclude();
        }
        ItemStack stack = state.getBlock().getCloneItemStack(level, newpos, state);
        if (!stack.isEmpty())
        {
            setLitProperty(true);
            return matcher.getItem() == stack.getItem();
        }
        else
        {
        	setLitProperty(false);
            return matcher.getItem() == state.getBlock().asItem();
        }
    }

	private boolean checkFluid(BlockPos newpos)
    {
        BlockState state = level.getBlockState(newpos);
        ItemStack matcher = INPUT.getItemStack(inventory);
        Block block = state.getBlock();
        if (matcher.isEmpty())
        {
            if (block instanceof LiquidBlock || block instanceof IFluidBlock)
            {
                return !level.getBlockState(newpos).isAir();
            }
            return false;
        }
        Item matcherItem = matcher.getItem();
        FluidStack matcherFluidStack = null;
        if (matcherItem instanceof BucketItem)
        {
            matcherFluidStack = new FluidBucketWrapper(matcher).getFluid();
            return checkFluid(block, matcherFluidStack, state, newpos);
        }
        return false;
    }

    private boolean checkFluid(Block block, FluidStack matcherFluidStack, BlockState state, BlockPos newpos)
    {
        if (matcherFluidStack == null)
        {
            return level.getBlockState(newpos).isAir();
        }
        Fluid matcherFluid = matcherFluidStack.getFluid();
        if (matcherFluid == null)
        {
            return false;
        }
        Block matcherFluidBlock = matcherFluid.defaultFluidState().createLegacyBlock().getBlock();
    	setLitProperty(matcherFluidBlock == block);
        return matcherFluidBlock == block;
    }

    private boolean checkGrowthLevel(BlockPos newpos, Direction facing, Direction dir)
    {
        int blockCount = areaType.getBlockCount();
        if (blockCount > 0)
        {
            Boolean x = checkGrowthLevelRow(newpos, dir, blockCount);
            if (x != null)
            {
                return x;
            }
        }
        else if (blockCount < 0)
        {
        	for(Direction directions : Direction.values())
        	{
                blockCount = -blockCount;
                Boolean x = checkGrowthLevelRow(newpos, dir, blockCount);
                if (x != null)
                {
                    return x;
                }
                for (int i = 1 ; i <= (blockCount-1)/2 ; i++)
                {
                    BlockPos p = newpos.relative(directions, i);
                    x = checkGrowthLevelRow(p, dir, blockCount);
                    if (x != null)
                    {
                        return x;
                    }
                    p = newpos.relative(directions, i);
                    x = checkGrowthLevelRow(p, dir, blockCount);
                    if (x != null)
                    {
                        return x;
                    }
                }
        	}
        }
        return groupType == GroupType.GROUP_ALL;
    }

    private Boolean checkGrowthLevelRow(BlockPos newpos, Direction dir, int blockCount)
    {
        for (int i = 0; i < blockCount; i++)
        {
            boolean result = checkGrowthLevel(newpos);
            if (result && groupType == GroupType.GROUP_ONE)
            {
                return true;
            }
            if ((!result) && groupType == GroupType.GROUP_ALL)
            {
                return false;
            }
            newpos = newpos.relative(dir);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	private boolean checkGrowthLevel(BlockPos newpos)
    {
        BlockState state = level.getBlockState(newpos);
        int pct = 0;
        for (Property<?> property : state.getProperties())
        {
            if(!"age".equals(property.getName()))
            {
                continue;
            }
            if(property.getValueClass() == Integer.class)
            {
                Property<Integer> integerProperty = (Property<Integer>)property;
                int age = state.getValue(integerProperty);
                int maxAge = Collections.max(integerProperty.getPossibleValues());
                pct = (age * 100) / maxAge;
            }
            break;
        }
        return pct >= number;
    }

    public void invalidateCache()
    {
        cachedBox = null;
    }

    private AABB getCachedBox(BlockPos pos1, Direction facing, Direction dir)
    {
        if (cachedBox == null)
        {
            int n = areaType.getBlockCount();
            if (n > 0)
            {
                cachedBox = new AABB(pos1);
                if (n > 1)
                {
                    BlockPos pos2 = pos1.relative(dir, n - 1);
                    cachedBox = cachedBox.minmax(new AABB(pos2));
                }
                cachedBox = cachedBox.expandTowards(.1, .1, .1);
            }
            else
            {
                n = -n;
                cachedBox = new AABB(pos1);

            	for(Direction directions : Direction.values())
            	{
                    if (n > 1)
                    {
                        BlockPos pos2 = pos1.relative(dir, n - 1);
                        cachedBox = cachedBox.minmax(new AABB(pos2));
                    }
                    BlockPos pos2 = pos1.relative(directions, (n-1)/2);
                    cachedBox = cachedBox.minmax(new AABB(pos2));
                    pos2 = pos1.relative(directions, (n-1)/2);
                    cachedBox = cachedBox.minmax(new AABB(pos2));
            	}
            }
        }
        return cachedBox;
    }

    private boolean checkEntityItems(BlockPos pos1, Direction facing, Direction dir)
    {
        List<? extends Entity> entities = level.getEntitiesOfClass(ItemEntity.class, getCachedBox(pos1, facing, dir));
        int cnt = 0;
        for (Entity entity : entities)
        {
            if (entity instanceof ItemEntity itemEntity)
            {
                cnt += itemEntity.getItem().getCount();
                if (cnt >= number)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkEntities(BlockPos pos1, Direction facing, Direction dir, Class<? extends Entity> clazz)
    {
        List<? extends Entity> entities = level.getEntitiesOfClass(clazz, getCachedBox(pos1, facing, dir));
        return entities.size() >= number;
    }

    private boolean checkEntitiesHostile(BlockPos pos1, Direction facing, Direction dir)
    {
        List<? extends Entity> entities = level.getEntitiesOfClass(PathfinderMob.class, getCachedBox(pos1, facing, dir));
        int cnt = 0;
        for (Entity entity : entities)
        {
            if (entity instanceof Enemy)
            {
                cnt++;
                if (cnt >= number)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkEntitiesPassive(BlockPos pos1, Direction facing, Direction dir)
    {
        List<? extends Entity> entities = level.getEntitiesOfClass(PathfinderMob.class, getCachedBox(pos1, facing, dir));
        int cnt = 0;
        for (Entity entity : entities)
        {
            if (entity instanceof Mob && !(entity instanceof Enemy))
            {
                cnt++;
                if (cnt >= number)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void load(CompoundTag tagCompound)
    {
        super.load(tagCompound);
        number = tagCompound.getInt("number");
        sensorType = SensorType.values()[tagCompound.getByte("sensor")];
        areaType = AreaType.values()[tagCompound.getByte("area")];
        groupType = GroupType.values()[tagCompound.getByte("group")];
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag tagCompound)
    {
        super.saveAdditional(tagCompound);
        tagCompound.putInt("number", number);
        tagCompound.putByte("sensor", (byte) sensorType.ordinal());
        tagCompound.putByte("area", (byte) areaType.ordinal());
        tagCompound.putByte("group", (byte) groupType.ordinal());
    }

    public void rotateBlock(Rotation axis)
    {
        invalidateCache();
    }
    
    public int getNumber()
    {
        return number;
    }
    public SensorType getSensorType()
    {
        return sensorType;
    }
    public void setSensorType(SensorType sensorType)
    {
        this.sensorType = sensorType;
        cachedBox = null;
        setChanged();
    }
    public AreaType getAreaType()
    {
        return areaType;
    }
    public void setAreaType(AreaType areaType)
    {
        this.areaType = areaType;
        cachedBox = null;
        setChanged();
    }
    public GroupType getGroupType()
    {
        return groupType;
    }
    public void setGroupType(GroupType groupType)
    {
        this.groupType = groupType;
        cachedBox = null;
        setChanged();
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout
            .builder()
            .inputSlot().slotAccess(INPUT)
            .build();
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new ScannerContainer(this, playerInventory, i);
    }
    
	@Override
	public void resetFakeStamina()
	{
	}
	
	public enum SensorType implements NamedEnum<SensorType> {
		
	    SENSOR_BLOCK("Block", false, true, "Detect if a certain type", "of block is present"),
	    SENSOR_FLUID("Fluid", false, true, "Detect if a certain type", "of fluid is present"),
	    SENSOR_GROWTHLEVEL("Growth", true, true, "Detect the growth percentage", "of a crop"),
	    SENSOR_ENTITIES("Entities", true, false, "Count the amount of entities"),
	    SENSOR_PLAYERS("Players", true, false, "Count the amount of players"),
	    SENSOR_HOSTILE("Hostile", true, false, "Count the amount of hostile mobs"),
	    SENSOR_PASSIVE("Passive", true, false, "Count the amount of passive mobs"),
	    SENSOR_ITEMS("Items", true, false, "Count the amount of items");

	    private final String name;
	    private final String[] description;
	    private final boolean supportsNumber;
	    private final boolean supportsGroup;

	    SensorType(String name, boolean supportsNumber, boolean supportsGroup, String... description) {
	        this.name = name;
	        this.supportsNumber = supportsNumber;
	        this.supportsGroup = supportsGroup;
	        this.description = description;
	    }

	    @Override
	    public String getName() {
	        return name;
	    }

	    @Override
	    public String[] getDescription() {
	        return description;
	    }

	    public boolean isSupportsNumber() {
	        return supportsNumber;
	    }

	    public boolean isSupportsGroup() {
	        return supportsGroup;
	    }
	}
	
	public enum GroupType implements NamedEnum<GroupType> {
		
	    GROUP_ONE("One", "At least one detected", "to match the sensor"),
	    GROUP_ALL("All", "All blocks in area", "must match the sensor");

	    private final String name;
	    private final String[] description;

	    GroupType(String name, String... description) {
	        this.name = name;
	        this.description = description;
	    }

	    @Override
	    public String getName() {
	        return name;
	    }

	    @Override
	    public String[] getDescription() {
	        return description;
	    }
	}
	
	public enum AreaType implements NamedEnum<AreaType> {
		
	    AREA_1("Area 1", 1, "1 block in front of sensor"),
	    AREA_3("Area 3", 3, "3 blocks in front of sensor"),
	    AREA_5("Area 5", 5, "5 blocks in front of sensor"),
	    AREA_3X3("Area 3x3", -3, "3x3 blocks in front of sensor"),
	    AREA_5X5("Area 5x5", -5, "5x5 blocks in front of sensor"),
	    AREA_7X7("Area 7x7", -7, "7x7 blocks in front of sensor");

	    private final String name;
	    private final String[] description;
	    private final int blockCount;

	    AreaType(String name, int blockCount, String... description) {
	        this.name = name;
	        this.blockCount = blockCount;
	        this.description = description;
	    }

	    @Override
	    public String getName() {
	        return name;
	    }

	    @Override
	    public String[] getDescription() {
	        return description;
	    }

	    public int getBlockCount() {
	        return blockCount;
	    }
	}
}
