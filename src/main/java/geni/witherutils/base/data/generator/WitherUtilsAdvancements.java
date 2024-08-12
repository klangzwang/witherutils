package geni.witherutils.base.data.generator;

import static net.minecraft.advancements.AdvancementRewards.Builder.experience;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.adv.StandardTrigger;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemDamagePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

@SuppressWarnings("unused")
public class WitherUtilsAdvancements extends AdvancementProvider {

    private static final ResourceLocation BACKGROUND_TEXTURE = WitherUtilsRegistry.loc("textures/gui/adv_bg.png");

    public WitherUtilsAdvancements(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(generatorIn.getPackOutput(), lookupProvider, existingFileHelper, List.of(new WUTAdvancements()));
    }
	
    private static class WUTAdvancements implements AdvancementGenerator {
    	
		@Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper)
        {
            AdvancementHolder root = itemAdvancement("root", AdvancementType.TASK, WUTItems.HAMMER.get())
            		.rewards(experience(10))
            		.save(saver, id("root"));
            
            AdvancementHolder anvil = itemInteractionAdvancement("anvil", root, AdvancementType.TASK, Blocks.ANVIL, WUTItems.HAMMER.get())
            		.save(saver, id("anvil"));
            
            AdvancementHolder cauldron = itemInteractionAdvancement("cauldron", root, AdvancementType.TASK, Blocks.CAULDRON, WUTItems.HAMMER.get())
            		.save(saver, id("cauldron"));
        }
        
        private static String id(String s)
        {
            return Names.MODID + ":" + s;
        }

        private Advancement.Builder customAdvancement(Supplier<StandardTrigger> triggerSupplier, AdvancementType type, ItemLike itemDisp)
        {
        	StandardTrigger trigger = triggerSupplier.get();
            String namespace = trigger.getInstance().id().getNamespace();
            String path = trigger.getInstance().id().getPath();
            return Advancement.Builder.advancement()
                    .display(itemDisp,
                    		Component.translatable(namespace + ".advancement." + path),
                    		Component.translatable(namespace + ".advancement." + path + ".desc"),
                            BACKGROUND_TEXTURE, type, true, true, false)
                    .addCriterion("0", new Criterion<>(trigger, trigger.getInstance()));
        }

        private Advancement.Builder itemAdvancement(String name, AdvancementType type, ItemLike... items)
        {
            Validate.isTrue(items.length > 0);
            return Advancement.Builder.advancement()
                    .display(items[0],
                    		Component.translatable("witherutils.advancement." + name),
                    		Component.translatable("witherutils.advancement." + name + ".desc"),
                            BACKGROUND_TEXTURE, type, true, true, false)
                    .addCriterion("0", InventoryChangeTrigger.TriggerInstance.hasItems(items));
        }

        private Advancement.Builder itemAdvancement(String name, AdvancementType type, ItemLike item, ItemPredicate[] predicates)
        {
            return Advancement.Builder.advancement()
                    .display(item,
                    		Component.translatable("witherutils.advancement." + name),
                    		Component.translatable("witherutils.advancement." + name + ".desc"),
                            BACKGROUND_TEXTURE, type, true, true, false)
                    .addCriterion("0", InventoryChangeTrigger.TriggerInstance.hasItems(predicates));
        }

        private ItemPredicate itemPredicate(ItemLike item, int minCount)
        {
            return ItemPredicate.Builder.item()
                    .of(item.asItem())
                    .withCount(MinMaxBounds.Ints.atLeast(minCount))
                    .withSubPredicate(ItemSubPredicates.DAMAGE, ItemDamagePredicate.durability(MinMaxBounds.Ints.ANY))
                    .build();
        }

        private ItemPredicate itemPredicateNoNBT(ItemLike item, int minCount)
        {
            return ItemPredicate.Builder.item()
                    .of(item.asItem())
                    .withCount(MinMaxBounds.Ints.atLeast(minCount))
                    .withSubPredicate(ItemSubPredicates.DAMAGE, ItemDamagePredicate.durability(MinMaxBounds.Ints.ANY))
                    .build();
        }

        private Advancement.Builder itemInteractionAdvancement(String name, AdvancementHolder pParent, AdvancementType type, Block interactedBlock, ItemLike... items)
        {
            Validate.isTrue(items.length > 0);
            return Advancement.Builder.advancement()
            		.parent(pParent)
                    .display(items[0],
                    Component.translatable("witherutils.advancement." + name),
                    Component.translatable("witherutils.advancement." + name + ".desc"),
                    BACKGROUND_TEXTURE, type, true, true, false)
                    .addCriterion(name, ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                            LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(interactedBlock)),
                            ItemPredicate.Builder.item().of(items[0])));
        }
    }
}
