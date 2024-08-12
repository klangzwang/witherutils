package geni.witherutils.base.common.block.totem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.base.common.block.totem.handler.AttractionHandlers;
import geni.witherutils.base.common.block.totem.handler.IMobAttractionHandler;
import geni.witherutils.base.common.block.totem.handler.IMobAttractionHandler.State;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.fakeplayer.WUTFakePlayer;
import geni.witherutils.core.common.network.NetworkDataSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;

public class TotemBlockEntity extends WitherMachineFakeBlockEntity implements MenuProvider {
	
	private static final SingleSlotAccess TOTEMTYPE = new SingleSlotAccess();
	private Map<LivingEntity, IMobAttractionHandler> tracking = new HashMap<LivingEntity, IMobAttractionHandler>();
	public static final LinkedList<Entity> spawnerClients = new LinkedList<>();
	
	private int timer;
	
	private boolean preview;
	private int scaleX = 4;
	private int scaleY = 4;
	private int scaleZ = 4;
    
    public TotemBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(WUTBlockEntityTypes.TOTEM.get(), worldPosition, blockState);
        NeoForge.EVENT_BUS.register(new EventHandler());
        addDataSlot(NetworkDataSlot.INT.create(this::getTimer, p -> timer = p));
        addDataSlot(NetworkDataSlot.BOOL.create(this::getPreview, p -> preview = p));
        addDataSlot(NetworkDataSlot.INT.create(this::getScaleX, p -> scaleX = p));
        addDataSlot(NetworkDataSlot.INT.create(this::getScaleY, p -> scaleY = p));
        addDataSlot(NetworkDataSlot.INT.create(this::getScaleZ, p -> scaleZ = p));
    }

    @Override
    public void serverTick()
    {
        super.serverTick();

    	if(fakePlayer == null)
    		this.fakePlayer = initFakePlayer((ServerLevel) level,
    				BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).getPath(), this);
        
		if(getInventory().getStackInSlot(0).isEmpty())
		{
			timer = 0;
			untrackAll();
			return;
		}

		timer++;

		if(timer < 15)
		{
			if(timer < 5)
				cleanTrackedEntities();
			else
			{
				if(timer < 13)
					tickTrackedEntities();
				else
				{
					if (tracking.size() < 6)
						collectEntities();
				}
			}
		}
		else
			timer = 0;
    }
    @Override
    public void clientTick()
    {
        super.clientTick();
        
        setLitProperty(!getInventory().getStackInSlot(0).isEmpty());

		updateVisual();
    }

    /*
     * 
     * VISUAL
     * 
     */
    @OnlyIn(Dist.CLIENT)
    private void updateVisual()
    {
//        Vec3D spawn = new Vec3D(worldPosition);
//        spawn.add(0.5, 0.5, 0.5);
//        
//        double rand = level.random.nextInt(100) / 12D;
//        double randOffset = rand * (Math.PI * 2D);
//        
//        double offsetX = Math.sin((McTimerUtil.clientTimer / 180D * Math.PI) + randOffset);
//        double offsetY = Math.cos((McTimerUtil.clientTimer / 180D * Math.PI) + randOffset);
//
//    	spawn.add(offsetX * 1.2, level.random.nextBoolean() ? -0.38 : 0.38, offsetY * 1.2);
//        Vector3 target = Vector3.fromBlockPosCenter(worldPosition);
//        level.addParticle(new IntParticleType.IntParticleData(WUTParticles.ENERGY_CORE.get(), 0),
//        		spawn.x, spawn.y, spawn.z, target.x, target.y, target.z);
    }
    
    /*
     * 
     * TRACKING - untrack
     * 
     */
	private void untrackAll()
	{
		for (Entry<LivingEntity, IMobAttractionHandler> tracked : tracking.entrySet())
		{
			tracked.getValue().release(this, tracked.getKey());
		}
		tracking.clear();
	}
	
    /*
     * 
     * TRACKING - cleantrack
     * 
     */
	private void cleanTrackedEntities()
	{
		Iterator<Entry<LivingEntity, IMobAttractionHandler>> iterator = tracking.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<LivingEntity, IMobAttractionHandler> next = iterator.next();
			if (!next.getKey().isAlive() || !canAttract(next.getKey()))
			{
				next.getValue().release(this, next.getKey());
				iterator.remove();
			}
		}
	}

    /*
     * 
     * TRACKING - ticktrack
     * 
     */
	private void tickTrackedEntities()
	{
		for (Entry<LivingEntity, IMobAttractionHandler> tracked : tracking.entrySet())
		{
			if (tracked.getKey().isAlive())
				tracked.getValue().tick(this, tracked.getKey());	
		}
	}

	/*
	 * 
	 * COLLECTING
	 * 
	 */
	private void collectEntities()
	{
		for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, getBounds()))
		{
			if (entity.isAlive() && !tracking.containsKey(entity) && canAttract(entity))
			{
				collectEntity(entity);
				
				if (tracking.size() >= 6)
					return;
			}
		}
	}
	private void collectEntity(@Nonnull LivingEntity entity)
	{
		for (Entry<IMobAttractionHandler, Item> handler : AttractionHandlers.instance.getRegistry().entrySet())
		{
			if(handler.getValue() == TOTEMTYPE.getItemStack(getInventory()).getItem())
			{
				IMobAttractionHandler attrHandler = handler.getKey();
				
				State state = attrHandler.canAttract(this, entity);
				if (state == State.CAN_ATTRACT)
				{
					attrHandler.startAttracting(this, entity);
					tracking.put(entity, attrHandler);
					return;
				}
				else if (state == State.ALREADY_ATTRACTING)
				{
					if(getInventory().getStackInSlot(0).isEmpty())
						tracking.clear();
					return;
				}
			}
		}
	}
	
	public void setData(Entity entity, IMobAttractionHandler attrHandler, boolean state)
	{
		entity.getPersistentData().putBoolean(attrHandler.getHandlerName(), state);
	}
	
	/*
	 * 
	 * GETTER/SETTER
	 * 
	 */
	public boolean canAttract(LivingEntity mob)
	{
		return getBounds().intersects(mob.getBoundingBox());
	}

	public @Nonnull AABB getBounds()
	{
		return new AABB(getBlockPos().getCenter().subtract(-scaleX, 0, -scaleZ), getBlockPos().getCenter().add(scaleX +1, scaleY, scaleZ +1));
	}
	
    public int getTimer()
    {
    	return timer;
    }
    
    public boolean getPreview()
    {
    	return preview;
    }
    public void setPreview(boolean preview)
    {
    	this.preview = preview;
    }
    
    public int getScaleX()
    {
    	return scaleX;
    }
    public void setScaleX(int scaleX)
    {
    	this.scaleX = scaleX;
    }
    
    public int getScaleY()
    {
    	return scaleY;
    }
    public void setScaleY(int scaleY)
    {
    	this.scaleY = scaleY;
    }
    
    public int getScaleZ()
    {
    	return scaleZ;
    }
    public void setScaleZ(int scaleZ)
    {
    	this.scaleZ = scaleZ;
    }
    
	public int getOffsetAxis(Axis axis)
	{
		int scaling = 0;
		switch(axis)
		{
			case X :
				scaling = scaleX;
				break;
			case Y :
				scaling = scaleY;
				break;
			case Z :
				scaling = scaleZ;
				break;
			default :
				break;
		}
		return scaling;
	}
    public void setOffsetAxis(Axis axis, int scaling)
    {
    	switch(axis)
    	{
			case X :
		    	this.scaleX = scaling;
				break;
			case Y :
		    	this.scaleY = scaling;
				break;
			case Z :
		    	this.scaleZ = scaling;
				break;
			default :
				break;
    	}
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new TotemContainer(this, playerInventory, i);
    }
    
	/*
	 * 
	 * LAYOUT
	 * 
	 */
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
        		.setStackLimit(1)
        		.inputSlot()
        		.slotAccess(TOTEMTYPE)
        		.build();
    }
    
	/*
	 * 
	 * EVENTHANDLER
	 * 
	 */
    public static class EventHandler
    {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
        public void spawnParticle(final ClientTickEvent.Post event)
        {
            if (Minecraft.getInstance().isPaused())
                return;
            
            RandomSource random = RandomSource.create();
            
            final Iterator<Entity> iterator = TotemBlockEntity.spawnerClients.iterator();
            while (iterator.hasNext())
            {
                final Entity entity = iterator.next();
                
                if(entity.isRemoved())
                {
                    iterator.remove();
                }
                else 
                {
                    if (entity.level() instanceof ServerLevel serverLevel && !entity.getPersistentData().contains("switchmob"))
                    {
                        serverLevel.sendParticles(ParticleTypes.SMOKE,
                		entity.getX() + (random.nextDouble() - 0.5) * entity.getBbWidth(),
                		entity.getY() + random.nextDouble() * entity.getBbHeight(),
                		entity.getZ() + (random.nextDouble() - 0.5) * entity.getBbWidth(),
                		0, 0.0, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
        
        @SuppressWarnings("rawtypes")
		@SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public void render(final RenderLivingEvent.Pre event)
        {
            if (spawnerClients.contains(event.getEntity()))
            {
                final float v = 0.3f;
                RenderSystem.setShaderColor(v, v, v, 1.0f);
            }
        }
        
        @SuppressWarnings("rawtypes")
		@SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public void render(final RenderLivingEvent.Post event)
        {
        	if (spawnerClients.contains(event.getEntity()))
        	{
            	RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    /*
     * 
     * FAKEPLAYER
     * 
     */
	public WUTFakePlayer getFakePlayer()
	{
		return fakePlayer;
	}
	
	/*
	 * 
	 * TAG
	 * 
	 */
	@Override
	public void saveAdditional(CompoundTag tag, Provider lookupProvider)
	{
		super.saveAdditional(tag, lookupProvider);
        tag.putBoolean("preview", preview);
		tag.putInt("scaleX", scaleX);
		tag.putInt("scaleY", scaleY);
		tag.putInt("scaleZ", scaleZ);
	}
	@Override
	public void loadAdditional(CompoundTag tag, Provider lookupProvider)
	{
		super.loadAdditional(tag, lookupProvider);
        preview = tag.getBoolean("preview");
        scaleX = tag.getInt("scaleX");
        scaleY = tag.getInt("scaleY");
        scaleZ = tag.getInt("scaleZ");
	}
}
