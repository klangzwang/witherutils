package geni.witherutils.base.common.block.sensor.floor;

import java.util.List;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class FloorSensorBlockEntity extends WitherMachineBlockEntity implements MenuProvider {

	public static final SingleSlotAccess GHOST = new SingleSlotAccess();
	
    public BlockState cover;
    
    private int scanWidth = 1;
    private int trigger = 1;
    
	public FloorSensorBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.FLOORSENSOR.get(), pos, state);
		add2WayDataSlot(new IntegerDataSlot(this::getScanWidth, p -> scanWidth = p, SyncMode.GUI));
		add2WayDataSlot(new IntegerDataSlot(this::getTrigger, p -> trigger = p, SyncMode.GUI));
	}

	@Override
	public void serverTick()
	{
		super.serverTick();
		
		if(trigger == 1)
		{
			setLitProperty(checkEntities(Player.class));
		}
		if(trigger == 2)
		{
			setLitProperty(checkEntitiesHostile());
		}
		if(trigger == 3)
		{
			setLitProperty(checkEntitiesPassive());
		}
		
        if (getInventory().getStackInSlot(0).isEmpty())
        {
    		removeCover();
    		setCoverProperty(false);
        }
        else
        {
            setCoverProperty(true);
        }
	}
	
	@Override
	public void clientTick()
	{
		super.clientTick();
		
        if (cover == null)
        {
            if (getInventory().getStackInSlot(0).getItem() instanceof BlockItem)
            {
                var block = ((BlockItem) getInventory().getStackInSlot(0).getItem()).getBlock();
                var cover = block.defaultBlockState();
                if (cover != null && !(block instanceof EntityBlock))
                {
                    this.cover = cover;
                    level.playSound(null, worldPosition, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 1, 1);
                    if (!level.isClientSide)
                    	sendBlockEntityToClients(this);
                }
            }
        }
        if (getInventory().getStackInSlot(0).isEmpty())
        {
    		removeCover();
    		setCoverProperty(false);
        }
        else
        {
            setCoverProperty(true);
        }
	}
	
    public void setCoverProperty(boolean covered)
    {
        BlockState st = this.getBlockState();
        boolean previous = st.getValue(FloorSensorBlock.COVERED);
        if(previous != covered)
        {
            this.level.setBlockAndUpdate(worldPosition, st.setValue(FloorSensorBlock.COVERED, covered));
        }
    }
    
    public int getScanWidth()
    {
    	return scanWidth;
    }
    public void setScanWidth(int scanWidth)
    {
    	this.scanWidth = scanWidth;
    }
    
    public int getTrigger()
    {
    	return trigger;
    }
    public void setTrigger(int trigger)
    {
    	this.trigger = trigger;
    }
    
	@SuppressWarnings("deprecation")
	@Override
	public void load(CompoundTag tag)
	{
		scanWidth = tag.getInt("scanWidth");
		trigger = tag.getInt("trigger");
		this.cover = tag.contains("cover") ?
				NbtUtils.readBlockState(this.level != null ? this.level.holderLookup(Registries.BLOCK) :
					BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("cover")) : null;
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putInt("scanWidth", scanWidth);
		tag.putInt("trigger", trigger);
        if (this.cover != null)
            tag.put("cover", NbtUtils.writeBlockState(this.cover));
		super.saveAdditional(tag);
	}
	
    public void removeCover()
    {
        this.cover = null;
        if (!level.isClientSide)
        	sendBlockEntityToClients(this);
    }
    
	@SuppressWarnings("resource")
	public static void sendBlockEntityToClients(BlockEntity tile)
    {
        var world = (ServerLevel) tile.getLevel();
        var entities = world.getChunkSource().chunkMap.getPlayers(new ChunkPos(tile.getBlockPos()), false);
        var packet = ClientboundBlockEntityDataPacket.create(tile, BlockEntity::saveWithoutMetadata);
        for (var e : entities)
            e.connection.send(packet);
    }
	
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
	{
        return new FloorSensorContainer(this, playerInventory, i);
	}
	
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
        		.setStackLimit(1)
        		.ghostSlot()
        		.slotAccess(GHOST)
        		.build();
    }
    
    private AABB forScanBox()
    {
		return new AABB(worldPosition).inflate(scanWidth, 1, scanWidth);
    }
    
    private boolean checkEntities(Class<? extends Entity> clazz)
    {
        List<? extends Entity> entities = level.getEntitiesOfClass(clazz, forScanBox());
        return entities.size() > 0;
    }

    private boolean checkEntitiesHostile()
    {
        List<? extends Entity> entities = level.getEntitiesOfClass(PathfinderMob.class, forScanBox());
        int cnt = 0;
        for (Entity entity : entities)
        {
            if (entity instanceof Enemy)
            {
                cnt++;
                if (cnt > 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkEntitiesPassive()
    {
        List<? extends Entity> entities = level.getEntitiesOfClass(PathfinderMob.class, forScanBox());
        int cnt = 0;
        for (Entity entity : entities)
        {
            if (entity instanceof Mob && !(entity instanceof Enemy))
            {
                cnt++;
                if (cnt > 0)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
