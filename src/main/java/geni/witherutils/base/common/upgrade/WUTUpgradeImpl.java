package geni.witherutils.base.common.upgrade;

import com.google.common.collect.ImmutableList;

import geni.witherutils.api.upgrade.WUTUpgrade;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WUTUpgradeImpl implements WUTUpgrade {
	
    private static final AtomicInteger ids = new AtomicInteger();

    private final ResourceLocation id;
    private final int maxTier;
    private final List<String> depModIds;
    private final int cacheId;

    WUTUpgradeImpl(ResourceLocation id, int maxTier, String... depModIds)
    {
        this.id = id;
        this.maxTier = maxTier;
        this.depModIds = ImmutableList.copyOf(depModIds);
        this.cacheId = ids.getAndIncrement();
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public int getCacheId()
    {
        return cacheId;
    }

    @Override
    public final int getMaxTier()
    {
        return maxTier;
    }

    @Override
    public final boolean isDependencyLoaded()
    {
        return depModIds.isEmpty() || depModIds.stream().anyMatch(modid -> ModList.get().isLoaded(modid));
    }

    @Override
    public final ResourceLocation getItemRegistryName(int tier)
    {
        Validate.isTrue(tier > 0 && tier <= maxTier, "tier must be in range 1 .. " + maxTier + "!");
        String path = id.getPath() + "_upgrade" + (maxTier > 1 ? "_" + tier : "");
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path);
    }

    @Override
    public final Item getItem(int tier)
    {
        return BuiltInRegistries.ITEM.get(getItemRegistryName(tier));
    }

    @Override
    public final ItemStack getItemStack(int count)
    {
        Item item = getItem();
        if (item == null) return ItemStack.EMPTY;
        return maxTier == 1 ? new ItemStack(item, count) : new ItemStack(getItem(count));
    }
}
