package geni.witherutils.base.common.init;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.block.collector.CollectorBlockEntity;
import geni.witherutils.base.common.block.collector.CollectorContainer;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorContainer;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorContainer;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorContainer;
import geni.witherutils.base.common.block.smarttv.SmartTVBlockEntity;
import geni.witherutils.base.common.block.smarttv.SmartTVContainer;
import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import geni.witherutils.base.common.block.totem.TotemContainer;
import geni.witherutils.base.common.item.card.CardContainer;
import geni.witherutils.base.common.item.cutter.CutterContainer;
import geni.witherutils.base.common.item.scaper.ScaperContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTMenus {
	
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, WitherUtilsRegistry.MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<TotemContainer>> TOTEM = MENU_TYPES.register("totem", () -> IMenuTypeExtension.create((windowId, inv, data) -> 
						new TotemContainer((TotemBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
	public static final DeferredHolder<MenuType<?>, MenuType<CollectorContainer>> COLLECTOR = MENU_TYPES.register("collector", () -> IMenuTypeExtension.create((windowId, inv, data) -> 
						new CollectorContainer((CollectorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
	
    public static final DeferredHolder<MenuType<?>, MenuType<LavaGeneratorContainer>> LAVA_GENERATOR = MENU_TYPES.register("lava_generator", () -> IMenuTypeExtension.create((windowId, inv, data) -> 
    					new LavaGeneratorContainer((LavaGeneratorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final DeferredHolder<MenuType<?>, MenuType<WaterGeneratorContainer>> WATER_GENERATOR = MENU_TYPES.register("water_generator", () -> IMenuTypeExtension.create((windowId, inv, data) -> 
    					new WaterGeneratorContainer((WaterGeneratorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final DeferredHolder<MenuType<?>, MenuType<WindGeneratorContainer>> WIND_GENERATOR = MENU_TYPES.register("wind_generator", () -> IMenuTypeExtension.create((windowId, inv, data) -> 
    					new WindGeneratorContainer((WindGeneratorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));

    public static final DeferredHolder<MenuType<?>, MenuType<SmartTVContainer>> SMARTTV = MENU_TYPES.register("smarttv", () -> IMenuTypeExtension.create((windowId, inv, data) -> 
    					new SmartTVContainer((SmartTVBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));

	public static final DeferredHolder<MenuType<?>, MenuType<CutterContainer>> CUTTER = MENU_TYPES.register("cutter", () -> IMenuTypeExtension.create(CutterContainer::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ScaperContainer>> SCAPER = MENU_TYPES.register("scaper", () -> IMenuTypeExtension.create(ScaperContainer::new));
	public static final DeferredHolder<MenuType<?>, MenuType<CardContainer>> BLOCKCARD = MENU_TYPES.register("blockcard", () -> IMenuTypeExtension.create((windowId, inv, data) -> 
						new CardContainer(windowId, inv, inv.player)));
}
