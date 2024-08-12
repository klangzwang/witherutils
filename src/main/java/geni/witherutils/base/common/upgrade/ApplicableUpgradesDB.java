package geni.witherutils.base.common.upgrade;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import geni.witherutils.api.upgrade.IUpgradeRegistry;
import geni.witherutils.api.upgrade.WUTUpgrade;
import geni.witherutils.base.common.item.upgrade.UpgradeItem;
import geni.witherutils.core.client.util.ClientUtils;
import geni.witherutils.core.mixin.BlockEntityTypeAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public enum ApplicableUpgradesDB implements IUpgradeRegistry {
	
    INSTANCE;

    private static final int MAX_UPGRADES_IN_TOOLTIP = 12;

    private final Table<BlockEntityType<?>, WUTUpgrade, Integer> TILE_ENTITIES = Tables.synchronizedTable(HashBasedTable.create());
    private final Table<EntityType<?>, WUTUpgrade, Integer> ENTITIES = Tables.synchronizedTable(HashBasedTable.create());
    private final Table<Item, WUTUpgrade, Integer> ITEMS = Tables.synchronizedTable(HashBasedTable.create());

    private final Map<ResourceLocation, WUTUpgrade> knownUpgrades = new ConcurrentHashMap<>();
    private final Map<WUTUpgrade,Set<Item>> acceptedUpgrades = new ConcurrentHashMap<>();

    public static ApplicableUpgradesDB getInstance() {
        return INSTANCE;
    }

    @Override
    public void addApplicableUpgrades(BlockEntityType<?> type, IUpgradeRegistry.Builder builder) {
        addUpgrades(TILE_ENTITIES, type, builder);
    }

    @Override
    public void addApplicableUpgrades(EntityType<?> type, IUpgradeRegistry.Builder builder) {
        addUpgrades(ENTITIES, type, builder);
    }

    @Override
    public void addApplicableUpgrades(Item item, IUpgradeRegistry.Builder builder) {
        addUpgrades(ITEMS, item, builder);
    }

    @Override
    public int getMaxUpgrades(BlockEntity te, WUTUpgrade upgrade) {
        if (te == null || upgrade == null) return 0;
        Integer max = TILE_ENTITIES.get(te.getType(), upgrade);
        return max == null ? 0 : max;
    }

    @Override
    public int getMaxUpgrades(Entity entity, WUTUpgrade upgrade) {
        if (entity == null || upgrade == null) return 0;
        Integer max = ENTITIES.get(entity.getType(), upgrade);
        return max == null ? 0 : max;
    }

    @Override
    public int getMaxUpgrades(Item item, WUTUpgrade upgrade) {
        if (item == null || upgrade == null) return 0;
        Integer max = ITEMS.get(item, upgrade);
        return max == null ? 0 : max;
    }

    @Override
    public void addUpgradeTooltip(WUTUpgrade upgrade, List<Component> tooltip) {
        Collection<Item> acceptors = ApplicableUpgradesDB.getInstance().getItemsWhichAccept(upgrade);
        if (!acceptors.isEmpty()) {
            List<Component> tempList = new ArrayList<>(acceptors.size());
//            for (Item acceptor : acceptors) {
//                tempList.add(Symbols.bullet().append(acceptor.getDescription().copy().withStyle(ChatFormatting.DARK_AQUA)));
//            }
            tempList.sort(Comparator.comparing(Component::getString));
            if (tempList.size() > MAX_UPGRADES_IN_TOOLTIP) {
                int n = (int) ((ClientUtils.getClientLevel().getGameTime() / 8) % acceptors.size());
                List<Component> tempList2 = new ArrayList<>(MAX_UPGRADES_IN_TOOLTIP);
                for (int i = 0; i < MAX_UPGRADES_IN_TOOLTIP; i++) {
                    tempList2.add(tempList.get((n + i) % acceptors.size()));
                }
                tooltip.addAll(tempList2);
            } else {
                tooltip.addAll(tempList);
            }
        }
    }

    @Override
    public WUTUpgrade registerUpgrade(ResourceLocation id, int maxTier, String... depModIds) {
    	WUTUpgrade upgrade = new WUTUpgradeImpl(id, maxTier, depModIds);
        if (knownUpgrades.put(upgrade.getId(), upgrade) != null) {
            throw new IllegalStateException("duplicate upgrade ID: " + id);
        }
        return upgrade;
    }

    @Override
    public WUTUpgrade getUpgradeById(ResourceLocation upgradeId) {
        return knownUpgrades.get(upgradeId);
    }

    @Override
    public Collection<WUTUpgrade> getKnownUpgrades() {
        return Collections.unmodifiableCollection(knownUpgrades.values());
    }

    @Override
    public Item makeUpgradeItem(WUTUpgrade upgrade, int tier, Rarity rarity) {
        return new UpgradeItem(upgrade, tier, rarity);
    }

    @Override
    public Item makeUpgradeItem(WUTUpgrade upgrade, int tier, Item.Properties properties) {
        return new UpgradeItem(upgrade, tier, properties);
    }

    @Override
    public int getUpgradeCount(ItemStack stack, WUTUpgrade upgrade) {
        return UpgradableItemUtils.getUpgradeCount(stack, upgrade);
    }

    @Override
    public Map<WUTUpgrade, Integer> getUpgradesInItem(ItemStack stack) {
        return UpgradableItemUtils.getUpgrades(stack);
    }

    public Collection<Item> getItemsWhichAccept(WUTUpgrade upgrade) {
        return acceptedUpgrades.getOrDefault(upgrade, Collections.emptySet());
    }

    public Map<WUTUpgrade, Integer> getApplicableUpgrades(BlockEntity te) {
        return TILE_ENTITIES.row(te.getType());
    }

    public Map<WUTUpgrade, Integer> getApplicableUpgrades(Entity e) {
        return ENTITIES.row(e.getType());
    }

    public Map<WUTUpgrade, Integer> getApplicableUpgrades(Item item) {
        return ITEMS.row(item);
    }

    private <T> void addUpgrades(Table<T,WUTUpgrade,Integer> table, T entry, IUpgradeRegistry.Builder builder)
    {
        builder.getUpgrades().forEach((upgrade, max) -> {
            table.put(entry, upgrade, max);
            if (entry instanceof Item item)
            {
                addAccepted(upgrade, item);
            }
            else if (entry instanceof BlockEntityType<?> beType)
            {
                ((BlockEntityTypeAccess) beType).getValidBlocks().stream()
                        .map(Block::asItem)
                        .filter(item -> item != Items.AIR)
                        .forEach(item -> addAccepted(upgrade, item));
            }
        });
    }

    private void addAccepted(WUTUpgrade upgrade, Item item)
    {
        acceptedUpgrades.computeIfAbsent(upgrade, k -> ConcurrentHashMap.newKeySet()).add(item);
    }
}
