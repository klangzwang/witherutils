package geni.witherutils.base.common.block.collector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTTags;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.MultiSlotAccess;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.item.card.CardItem;
import geni.witherutils.core.common.network.NetworkDataSlot;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CollectorBlockEntity extends WitherMachineBlockEntity {
	
    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    public static final MultiSlotAccess OUTPUTS = new MultiSlotAccess();
    
    private static final double COLLISION_DISTANCE_SQ = 1 * 1;
    private static final double SPEED = 0.025;
    private static final double SPEED_4 = SPEED * 4;
    
    private float distance = 0F;
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
		super(WUTBlockEntityTypes.COLLECTOR.get(), pos, state);
		this.targetClass = ItemEntity.class;
		addDataSlot(NetworkDataSlot.BOOL.create(() -> getOverflow(), p -> overflow = p));
		addDataSlot(NetworkDataSlot.BOOL.create(() -> getRender(), p -> render = p));
	}
	
    @Override
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(layout)
        {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
            {
                if(slot == 0)
                {
                	if(!(stack.getItem() instanceof CardItem))
        	      		return stack;
    	      		SoundUtil.playSlotSound(level, worldPosition, SoundEvents.ARMOR_EQUIP_IRON.value(), 1.0f, 2.0f);
                }
                return super.insertItem(slot, stack, simulate);
            }
            
            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate)
            {
                if(slot == 0)
                {
    	      		SoundUtil.playSlotSound(level, worldPosition, SoundEvents.ARMOR_UNEQUIP_WOLF, 1.0f, 2.0f);
                }
            	return super.extractItem(slot, amount, simulate);
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

        	distance = (float) getBlockPos().distSqr(entity.blockPosition());

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
        	
        	if(INPUT.getItemStack(getInventory()).isEmpty())
        		this.entities.add(new WeakReference<>(ie));
        	else
        	{
    			if(INPUT.getItemStack(getInventory()).getItem() instanceof CardItem)
    			{
//    				if(getItemHandler() != null)
//    				{
//    					for(int i = 0; i < getItemHandler().getSlots(); i++)
//    					{
//    						ItemStack filterPtr = getItemHandler().getStackInSlot(i);
//							if(ItemStackUtil.matches(ie.getItem(), filterPtr))
//								this.entities.add(new WeakReference<>(ie));
//    					}
//    				}
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
    	for (int i = 0; i < OUTPUTS.size(); i++)
    	{
    		if(!OUTPUTS.get(i).getItemStack(getInventory()).isEmpty())
    		{
        		if(overflow && entity.getAge() > 49)
        		{
    				entity.setRemainingFireTicks(10);
        			if(entity.getAge() == 50)
        			{
            			SoundUtil.playSoundDistrib(level, worldPosition, SoundEvents.GENERIC_EXTINGUISH_FIRE, 0.7f, 1.f, false, true);        				
        			}
        			if(entity.getAge() > 100)
        			{
        				entity.remove(RemovalReason.DISCARDED);
        				level.playSound(null, getBlockPos(), SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.4F + 0.8F);
        			}
        			return;
        		}
    		}
    		else
    		{
    	    	if(level.random.nextFloat() < 0.75F)
    	    		SoundUtil.playSound(level, worldPosition, SoundEvents.ENDERMAN_TELEPORT, 1);

    	    	ItemStack reminder = OUTPUTS.get(i).insertItem(getInventory(), entity.getItem().copy(), false);
    	    	
                if (reminder.isEmpty())
                {
                    entity.discard();
                    return;
                }
                else
                {
                    entity.getItem().setCount(reminder.getCount());
                }
    		}
    	}
    }

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
	
    public float getDistance()
    {
        return distance;
    }
    
    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.saveAdditional(pTag, lookupProvider);
        pTag.putBoolean("render", this.render);
        pTag.putBoolean("overflow", this.overflow);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.loadAdditional(pTag, lookupProvider);
		this.overflow = pTag.getBoolean("overflow");
		this.render = pTag.getBoolean("render");
    }
    
	public boolean shouldRenderFace(Direction direction)
	{
		return direction.getAxis() == Direction.Axis.Y;
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
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer)
	{
		return new CollectorContainer(this, pPlayerInventory, pContainerId);
	}
}
