package geni.witherutils.base.common.block.collector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTTags;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.MultiSlotAccess;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.item.card.CardItem;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class CollectorBlockEntity extends WitherMachineBlockEntity implements MenuProvider {

    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    public static final MultiSlotAccess OUTPUTS = new MultiSlotAccess();
    
    private static final double COLLISION_DISTANCE_SQ = 1 * 1;
    private static final double SPEED = 0.025;
    private static final double SPEED_4 = SPEED * 4;
    
    private float maxSpeed = 30F;
    private float acceleration = 0.5F;
    
    @OnlyIn(Dist.CLIENT)
    public float prevFanRotation;
    @OnlyIn(Dist.CLIENT)
    public float fanRotation;
    @OnlyIn(Dist.CLIENT)
    private float currentSpeed;

	public boolean overflow = false;
	public boolean render = false;
	
    private List<WeakReference<ItemEntity>> entities = new ArrayList<>();
    private Class<ItemEntity> targetClass;
    
	public CollectorBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.COLLECTOR.get(), pos, state);
        this.targetClass = ItemEntity.class;
        add2WayDataSlot(new BooleanDataSlot(this::getOverflow, p -> overflow = p, SyncMode.GUI));
        add2WayDataSlot(new BooleanDataSlot(this::getRender, p -> render = p, SyncMode.GUI));
	}
	
    @Override
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(getIOConfig(), layout)
        {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
            {
                if(slot == 0)
                {
                	if(!(stack.getItem() instanceof CardItem))
                	{
                        return stack;
                	}
                	else
                	{
        	      		SoundUtil.playSlotSound(level, worldPosition, SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 2.0f);
        	      		return super.insertItem(slot, stack, simulate);
                	}
                }
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
        if (this.getRedstoneControl().isActive(level.hasNeighborSignal(worldPosition)))
        {
            this.attractEntities(this.getLevel(), this.getBlockPos(), this.getRange());
        }

    	super.serverTick();
	}

    @Override
	public void clientTick()
	{
        if (this.getRedstoneControl().isActive(level.hasNeighborSignal(worldPosition)))
        {
            this.attractEntities(this.getLevel(), this.getBlockPos(), this.getRange());
        }
        
    	super.clientTick();

        prevFanRotation = fanRotation;
        if(entities.size() > 0)
        {
    	    setLitProperty(true);
        	
            currentSpeed += acceleration;
            if(currentSpeed > maxSpeed)
            {
                currentSpeed = maxSpeed;
            }
        }
        else
        {
	    	setLitProperty(false);
	    	
            currentSpeed *= 0.95F;
        }
        fanRotation += currentSpeed;
        
        if(entities.size() > 0 && level.random.nextFloat() < 0.5F)
        {
            double d0 = (double)0.03125D;
            level.addParticle(ParticleTypes.REVERSE_PORTAL,
                    (double) worldPosition.getX() + (double) 0.13125F + (double) 0.7375F * (double) level.random.nextFloat(),
                    (double) worldPosition.getY() + d0 + (double) level.random.nextFloat() * (1.0D - d0),
                    (double) worldPosition.getZ() + (double) 0.13125F + (double) 0.7375F * (double) level.random.nextFloat(),
                    0, 0, 0);
        }
	}
    
    private void attractEntities(Level level, BlockPos pos, int range)
    {
        if ((level.getGameTime() + pos.asLong()) % 5 == 0)
        {
            getEntities(level, pos, range);
        }
        
        Iterator<WeakReference<ItemEntity>> iterator = entities.iterator();
        
        while (iterator.hasNext())
        {
            WeakReference<ItemEntity> ref = iterator.next();
            
            if (ref.get() == null)
            {
                iterator.remove();
                continue;
            }
            
            ItemEntity entity = ref.get();
            
            if (entity.isRemoved())
            {
                iterator.remove();
                continue;
            }
            
            if (moveToPos(entity, pos, SPEED, SPEED_4, COLLISION_DISTANCE_SQ))
            {
                handleEntity(entity);
            }
        }
    }

    private void getEntities(Level level, BlockPos pos, int range)
    {
        this.entities.clear();
        AABB area = new AABB(pos).inflate(range);
        for (ItemEntity ie : level.getEntitiesOfClass(targetClass, area))
        {
        	if(!isNotBlacklisted(ie.getItem()))
        		return;
        	
        	if(INPUT.getItemStack(inventory).isEmpty())
        		this.entities.add(new WeakReference<>(ie));
        	else
        	{
    			if(INPUT.getItemStack(inventory).getItem() instanceof CardItem filter)
    			{
    				IItemHandler myFilter = getInventory().getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
    				if(myFilter != null)
    				{
    					for(int i = 0; i < myFilter.getSlots(); i++)
    					{
    						ItemStack filterPtr = myFilter.getStackInSlot(i);
							if(ItemStackUtil.matches(ie.getItem(), filterPtr))
								this.entities.add(new WeakReference<>(ie));
    					}
    				}
    			}
        	}
        }
    }
    
    public static boolean moveToPos(Entity entity, BlockPos pos, double speed, double speed4, double collisionDistanceSq)
    {
        return moveToPos(entity, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, speed, speed4, collisionDistanceSq);
    }

    public static boolean moveToPos(Entity entity, double x, double y, double z, double speed, double speed4, double collisionDistanceSq)
    {
        if (entity.isRemoved())
        {
            return false;
        }
        
        x -= entity.getX();
        y -= entity.getY();
        z -= entity.getZ();

        double distanceSq = x * x + y * y + z * z;

        if (distanceSq < collisionDistanceSq)
        {
            return true;
        }
        else
        {
            double adjustedSpeed = speed4 / distanceSq;
            Vec3 mov = entity.getDeltaMovement();
            double deltaX = mov.x + x * adjustedSpeed;
            double deltaZ = mov.z + z * adjustedSpeed;
            double deltaY;
            if (y > 0)
            {
                deltaY = 0.12;
            }
            else
            {
                deltaY = mov.y + y * speed;
            }
            entity.setDeltaMovement(deltaX, deltaY, deltaZ);
            return false;
        }
    }
    
    public void handleEntity(ItemEntity entity)
    {
    	for (int i = 0; i < 7; i++)
    	{
    		if(!OUTPUTS.get(i).getItemStack(inventory).isEmpty())
    		{
        		if(overflow)
        		{
        			if(level.random.nextFloat() < 0.1F)
        				entity.hurt(level.damageSources().inFire(), 1);
        			entity.hurt(level.damageSources().inFire(), -1);
        			entity.setSecondsOnFire(10);
        			if(entity.isRemoved())
        				level.playSound(null, getBlockPos(), SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.4F + 0.8F);
        			return;
        		}
    		}
    		else
    		{
    	    	if(level.random.nextFloat() < 0.75F)
    	    		SoundUtil.playSound(level, worldPosition, SoundEvents.ENDERMAN_TELEPORT, 1);
    	    	
    	    	ItemStack stacker = ItemStackUtil.insertItemMultiSlot(getInventory(), entity.getItem().copy(), OUTPUTS);
//                ItemStack reminder = OUTPUTS.get(i).insertItem(inventory, entity.getItem().copy(), false);
                if (stacker.isEmpty())
                {
                    entity.discard();
                    return;
                }
                else
                {
                    entity.getItem().setCount(stacker.getCount());
                }
    		}
    	}
    }

	/**
	 * 
	 * GETTER/SETTER
	 * 
	 */
	public boolean getOverflow()
    {
    	return overflow;
    }
    public void setOverflow(boolean overflow)
    {
    	this.overflow = overflow;
    }
    
	public boolean getRender()
	{
		return render;
	}
    public void setRender(boolean render)
    {
    	this.render = render;
    }
    
    public int getRange()
    {
        return BlocksConfig.COLLECTORRANGE.get();
    }
//    private int getMaxItems()
//    {
//        return BlocksConfig.COLLECTORMAXITEMS.get();
//    }
    
	@Override
	public void load(CompoundTag tag)
	{
		this.overflow = tag.getBoolean("overflow");
		this.render = tag.getBoolean("render");
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putBoolean("render", this.render);
		super.saveAdditional(tag);
	}

	public boolean shouldRenderFace(Direction direction)
	{
		return direction.getAxis() == Direction.Axis.Y;
	}
   
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new CollectorContainer(this, playerInventory, i);
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
            .inputSlot().slotAccess(INPUT)
            .outputSlot(8).slotAccess(OUTPUTS)
            .build();
    }
    
	private static boolean isNotBlacklisted(ItemStack stack)
	{
		return !stack.is(WUTTags.Items.COLLECTOR_BLACKLIST);
	}
	
	@Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return CollectorBlockEntity.INFINITE_EXTENT_AABB;
    }
}
