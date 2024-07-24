package geni.witherutils.base.client.model.special;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = WitherUtilsRegistry.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public enum SpecialModels
{
	ANVIL_HOT("wither/anvil/hot"),
	
	COLLECTOR("wither/collector/collector"),
	COLLECTOR_ROTATE("wither/collector/collector_rotate"),
	COLLECTOR_EMM("wither/collector/collector_emm"),
	
	EMFURNACE("wither/emissive/em_furnace"),
	EMFLOODGATE("wither/emissive/em_floodgate"),
	EMWALLSENSOR("wither/emissive/em_wallsensor"),
	EMDRUM("wither/emissive/em_drum"),
	EMPLACER("wither/emissive/em_placer"),
	EMFISHER("wither/emissive/em_fisher"),
	EMFARMER("wither/emissive/em_farmer"),
	EMSPAWNER("wither/emissive/em_spawner"),
	EMTOTEM("wither/emissive/em_totem"),
	EMMULTI("wither/emissive/em_multi"),
//	EMMINER("wither/emissive/em_miner"),
//	EMPROCESSOR("wither/emissive/em_processor"),

	DRYHEAD_HEAD("wither/entity/dryhead/head"),
	DRYHEAD_HAIR("wither/entity/dryhead/hair"),
	DRYHEAD_EYES("wither/entity/dryhead/eyes"),
	DRYHEAD_JAW("wither/entity/dryhead/jaw"),

//	SCANSPHERE("wither/scan/sphere"),
	SCANFOOT("wither/scan/foot"),
	
//	ARMOR_HELMET00("wither/armor/helmet/helmet00"),
//	ARMOR_CHEST00("wither/armor/chest/chest00"),
//	ARMOR_ARM00("wither/armor/arm/arm00"),
//	ARMOR_ARM01("wither/armor/arm/arm01"),
//	ARMOR_LEG00("wither/armor/leg/leg00"),
//	ARMOR_LEG01("wither/armor/leg/leg01"),
//	ARMOR_EYES("wither/eyes/player"),

	SKULLUP("wither/skull/witherskull_up"),
	SKULLLW("wither/skull/witherskull_lw"),
	SKULLEM("wither/skull/witherskull_em"),
	
	MULTICENTER("wither/multi/center"),
	
	SOUL_FIRE("wither/fire/soul"),
	
	FISHER_HOOK("wither/fisher/fisher_hook"),
	FISHER_PLATFORM("wither/fisher/fisher_platform"),
	
	FARMERTOP("wither/farmer/farmer_top"),
	
	GLASSDRUM("wither/glass/glass_drum"),
	GLASSMINER("wither/glass/glass_miner"),
	
//	SKULL_UPPER("wither/skull/skull_upper"),
//	SKULL_LOWER("wither/skull/skull_lower"),
	
//	ADAPTER("wither/processor/processor_adapter"),
	
	MOTOR("wither/motor/motor"),
//	WHEEL("wither/wheel/wheel"),
	PYLONIO("wither/iomode/pylonio"),
//	SPHEREINNERSCREEN("wither/sphere/sphereinner_screen"),
//	SPHEREOUTERSCREEN("wither/sphere/sphereouter_screen"),
//	SPHEREINNER("wither/sphere/sphereinner"),
//	SPHEREOUTER("wither/sphere/sphereouter"),
//	SPHERESTAB("wither/sphere/spherestab"),
	
//	SWORDBASE("wither/sword/sword_0"),
//	SWORDENERGY("wither/sword/sword_1"),
//	SWORDHANDLE("wither/sword/sword_2"),
//	SWORDPEARL("wither/sword/sword_3"),
	
	WANDHELPER("item/wand_helper"),
	WANDPOWER("item/wand_powered"),
	WANDSWING("item/wand_swinging");
//	FAN("item/fan_helper"),
//	SHOVEL("item/shovel_helper"),
//	BLITZ("wither/misc/blitz"),
//	LAVA("wither/misc/lava"),
//	CYLINDER("wither/misc/cylinder"),
//	GLOW("wither/glow/glow"),
//	STATUS_LIGHT("wither/status/status_light"),
//	METALDOOR("wither/door/metal_model"),
//	METALDOORTEETH("wither/door/metal_teeth_model"),
//	FAUCET("wither/floodgate/faucet"),
//	FAUCET_OPEN("wither/floodgate/faucet_open"),
//	PLUG("wither/plug/plug"),
//	PLUG_LIGHT("wither/plug/plug_light");
	
    private final ModelResourceLocation modelLocation;
    private BakedModel cachedModel;

    SpecialModels(String model)
    {
        this.modelLocation = ModelResourceLocation.standalone(WitherUtilsRegistry.loc(model));
    }

    public BakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            this.cachedModel = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
        }
        return this.cachedModel;
    }

    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event)
    {
        for(SpecialModels model : values())
        {
            event.register(model.modelLocation);
        }
    }

    @SubscribeEvent
    public static void onBake(ModelEvent.BakingCompleted event)
    {
        for(SpecialModels model : values())
        {
            model.cachedModel = null;
        }
    }
}
