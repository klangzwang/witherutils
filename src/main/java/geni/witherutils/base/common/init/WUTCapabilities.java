package geni.witherutils.base.common.init;

import static geni.witherutils.api.WitherUtilsRegistry.loc;

import java.util.Optional;

import geni.witherutils.api.soul.PlayerSoul;
import geni.witherutils.base.common.base.IWitherInventoryItem;
import geni.witherutils.base.common.base.IWitherPoweredItem;
import geni.witherutils.base.common.item.pickaxe.PickaxeHeadItem;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class WUTCapabilities {
	
    public static final EntityCapability<PlayerSoul, Void> PLAYERSOUL = EntityCapability.createVoid(loc("playersoul"), PlayerSoul.class);

    public static Optional<PlayerSoul> getPlayerSoulHandler(Entity entity)
    {
        return Optional.ofNullable(entity.getCapability(PLAYERSOUL));
    }
    public static Optional<IEnergyStorage> getItemEnergyHandler(ItemStack stack)
    {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM));
    }
    public static Optional<IEnergyStorage> getBlockEnergyHandler(BlockEntity blockEntity, Direction side)
    {
        return Optional.ofNullable(blockEntity.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), side));
    }
    
    public static void register(RegisterCapabilitiesEvent event)
    {
    	registerEntityCapabilities(event);
    	registerItemCapabilities(event);
    	registerBlockCapabilities(event);
    }
    
    public static void registerEntityCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerEntity(PLAYERSOUL, EntityType.PLAYER, (player, ctx) -> new PlayerSoul());
    }
    
    public static void registerItemCapabilities(RegisterCapabilitiesEvent event)
    {
        WUTItems.ITEM_TYPES.getEntries().forEach(entry -> {
            if (entry.get() instanceof IWitherPoweredItem ipowered)
            {
                event.registerItem(Capabilities.EnergyStorage.ITEM, ipowered.initEnergyCap(), entry.get());
            }
        });
        WUTItems.ITEM_TYPES.getEntries().forEach(entry -> {
            if (entry.get() instanceof IWitherInventoryItem iinventory)
            {
                event.registerItem(Capabilities.ItemHandler.ITEM, iinventory.initItemHandlerCap(), entry.get());
            }
        });
        WUTItems.ITEM_TYPES.getEntries().forEach(entry -> {
            if (entry.get() instanceof PickaxeHeadItem)
            {
                event.registerItem(ItemCapability.createVoid(loc("pickaxe"), Float.class), PickaxeHeadItem.STORED_FLOAT_PROVIDER, entry.get());
            }
        });
    }
    
    public static void registerBlockCapabilities(RegisterCapabilitiesEvent event)
    {
        WUTBlockEntityTypes.streamBlockEntities().forEach(blockEntity -> {
            if (blockEntity instanceof WitherBlockEntity wbe)
            {
                if (wbe.hasItemCapability())
                {
                    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, wbe.getType(),
                            (object, dir) -> object instanceof WitherBlockEntity be ? be.getItemHandler(dir) : null);
                }
                if (wbe.hasFluidCapability())
                {
                    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, wbe.getType(),
                            (object, dir) -> object instanceof WitherBlockEntity be ? be.getFluidHandler(dir) : null);
                }
                if (wbe.hasEnergyCapability())
                {
                    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, wbe.getType(),
                            (object, dir) -> object instanceof WitherBlockEntity be ? be.getEnergyHandler(dir) : null);
                }
            }
        });
    }
}
