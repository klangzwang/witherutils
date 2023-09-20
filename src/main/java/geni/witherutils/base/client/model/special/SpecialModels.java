package geni.witherutils.base.client.model.special;

import geni.witherutils.WitherUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WitherUtils.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum SpecialModels
{
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
	EMMINER("wither/emissive/em_miner"),
	
	SKULLEM("wither/skull/witherskull_em"),

	VANISHING("wither/vanishing/block"),
	
	MULTICENTER("wither/multi/center"),
	
	ANVIL_HOT("wither/anvil/hot"),
	SOUL_FIRE("wither/fire/soul"),
	
	FISHER_HOOK("wither/fisher/fisher_hook"),
	FISHER_PLATFORM("wither/fisher/fisher_platform"),
	
	FARMERTOP("wither/farmer/farmer_top"),
	
	GLASSDRUM("wither/glass/glass_drum"),
	GLASSMINER("wither/glass/glass_miner"),
	
	COLLECTOR("wither/collector/collector"),
	COLLECTOR_ROTATE("wither/collector/collector_rotate"),
	COLLECTOR_EMM("wither/collector/collector_emm"),
	
	SKULL_UPPER("wither/skull/skull_upper"),
	SKULL_LOWER("wither/skull/skull_lower"),
	
	MOTOR("wither/motor/motor"),
	WHEEL("wither/wheel/wheel"),
	PYLONIO("wither/iomode/pylonio"),
	SPHEREINNERSCREEN("wither/sphere/sphereinner_screen"),
	SPHEREOUTERSCREEN("wither/sphere/sphereouter_screen"),
	SPHEREINNER("wither/sphere/sphereinner"),
	SPHEREOUTER("wither/sphere/sphereouter"),
	SPHERESTAB("wither/sphere/spherestab"),
	SPHEREHEMIS("wither/sphere/spherehemis"),
	
	SWORDBASE("wither/sword/base"),
	SWORDENERGY("wither/sword/energy"),
	SWORDHANDLE("wither/sword/handle"),
	SWORDPEARL("wither/sword/pearl"),
	
	WANDPOWER("item/wand_powered"),
	WANDSWING("item/wand_swinging"),
	FAN("item/fan_helper"),
    SHOVEL("item/shovel_helper"),
    BLITZ("wither/misc/blitz"),
    LAVA("wither/misc/lava"),
    CYLINDER("wither/misc/cylinder"),
    GLOW("wither/glow/glow"),
	STATUS_LIGHT("wither/status/status_light"),
	METALDOOR("wither/door/metal_model"),
	METALDOORTEETH("wither/door/metal_teeth_model"),
	FAUCET("wither/floodgate/faucet"),
	FAUCET_OPEN("wither/floodgate/faucet_open"),
    PLUG("wither/plug/plug"),
    PLUG_LIGHT("wither/plug/plug_light");
	
    private final ResourceLocation modelLocation;

    private BakedModel cachedModel;

    SpecialModels(String model)
    {
        this.modelLocation = new ResourceLocation(WitherUtils.MODID, model);
    }
    
    public BakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            this.cachedModel = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
        }
        return this.cachedModel;
    }
    
    public ResourceLocation getModelFileLocation(SpecialModels model, BlockModelProvider provider)
    {
        for(SpecialModels models : values())
        {
            if(models.modelLocation.getPath() == model.modelLocation.getPath())
            {
                return models.modelLocation;
            }
        }
        return provider.modLoc(null);
    }
    public ModelFile getModelFile(BakedModel model, BlockModelProvider provider)
    {
        for(SpecialModels models : values())
        {
            if(models.cachedModel != null && models.cachedModel == model)
            {
                ResourceLocation location = models.modelLocation;
                ModelFile modelFile = provider.getExistingFile(location);
                return modelFile;
            }
        }
        return provider.getBuilder("no SpecialModel found");
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
