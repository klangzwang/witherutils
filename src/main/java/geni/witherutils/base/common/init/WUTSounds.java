package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTSounds
{
	public static final DeferredRegister<SoundEvent> SOUND_TYPES = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WitherUtils.MODID);

	public static final RegistryObject<SoundEvent> ENGINELOOP = SOUND_TYPES.register("engineloop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "engineloop")));
	public static final RegistryObject<SoundEvent> ENGINESTART = SOUND_TYPES.register("enginestart", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "enginestart")));
	public static final RegistryObject<SoundEvent> ENGINESTOP = SOUND_TYPES.register("enginestop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "enginestop")));
	public static final RegistryObject<SoundEvent> NOISE = SOUND_TYPES.register("noise", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "noise")));
	public static final RegistryObject<SoundEvent> STARTUP = SOUND_TYPES.register("startup", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "startup")));
	public static final RegistryObject<SoundEvent> METALPLACE = SOUND_TYPES.register("metalplace", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "metalplace")));
	public static final RegistryObject<SoundEvent> METALBREAK = SOUND_TYPES.register("metalbreak", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "metalbreak")));
	public static final RegistryObject<SoundEvent> MINIONATTACK1 = SOUND_TYPES.register("minionattack1", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "minionattack1")));
	public static final RegistryObject<SoundEvent> MINIONCAST = SOUND_TYPES.register("minioncast", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "minioncast")));
	public static final RegistryObject<SoundEvent> MINIONHURT = SOUND_TYPES.register("minionhurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "minionhurt")));
	public static final RegistryObject<SoundEvent> CHARGETICK = SOUND_TYPES.register("chargetick", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "chargetick")));
	public static final RegistryObject<SoundEvent> CHARGESOULS = SOUND_TYPES.register("chargesouls", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "chargesouls")));
	public static final RegistryObject<SoundEvent> WOODPOOL = SOUND_TYPES.register("woodpool", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "woodpool")));
	public static final RegistryObject<SoundEvent> WORMBIP = SOUND_TYPES.register("wormbip", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "wormbip")));
	public static final RegistryObject<SoundEvent> SCREAM = SOUND_TYPES.register("scream", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "scream")));
	public static final RegistryObject<SoundEvent> CUTTER = SOUND_TYPES.register("cutter", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "cutter")));
	public static final RegistryObject<SoundEvent> BURNBG = SOUND_TYPES.register("burnbg", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "burnbg")));
	public static final RegistryObject<SoundEvent> GASOLINE = SOUND_TYPES.register("gasoline", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "gasoline")));
	public static final RegistryObject<SoundEvent> GASON = SOUND_TYPES.register("gason", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "gason")));
	public static final RegistryObject<SoundEvent> GASOFF = SOUND_TYPES.register("gasoff", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "gasoff")));
	public static final RegistryObject<SoundEvent> PICKPLICK = SOUND_TYPES.register("pickplick", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "pickplick")));
	public static final RegistryObject<SoundEvent> PICKRIB = SOUND_TYPES.register("pickrib", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "pickrib")));
	public static final RegistryObject<SoundEvent> PICKSHEE = SOUND_TYPES.register("pickshee", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "pickshee")));
	public static final RegistryObject<SoundEvent> FEAR = SOUND_TYPES.register("fear", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "fear")));
	public static final RegistryObject<SoundEvent> SLOT = SOUND_TYPES.register("slot", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "slot")));
	public static final RegistryObject<SoundEvent> REACT = SOUND_TYPES.register("react", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "react")));
    public static final RegistryObject<SoundEvent> GENERATOR = SOUND_TYPES.register("generator", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "generator")));
    public static final RegistryObject<SoundEvent> PLACEBOOMONE = SOUND_TYPES.register("place_boom1", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "place_boom1")));
    public static final RegistryObject<SoundEvent> PLACEBOOMTWO = SOUND_TYPES.register("place_boom2", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "place_boom2")));
    public static final RegistryObject<SoundEvent> PLACESSSHHHH = SOUND_TYPES.register("place_sssh", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "place_sssh")));
    public static final RegistryObject<SoundEvent> HAMMERHIT = SOUND_TYPES.register("hammerhit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "hammerhit")));
    public static final RegistryObject<SoundEvent> SWOOSH = SOUND_TYPES.register("swoosh", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "swoosh")));
    public static final RegistryObject<SoundEvent> WRINKLYAMBIENT = SOUND_TYPES.register("wrinkly_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "wrinkly_ambient")));
    public static final RegistryObject<SoundEvent> WRINKLYDEATH = SOUND_TYPES.register("wrinkly_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "wrinkly_death")));
    public static final RegistryObject<SoundEvent> WRINKLYSPAWN = SOUND_TYPES.register("wrinkly_spawn", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "wrinkly_spawn")));
    public static final RegistryObject<SoundEvent> WRINKLYDISTANT = SOUND_TYPES.register("wrinkly_distant", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "wrinkly_distant")));
    public static final RegistryObject<SoundEvent> ELECTRO = SOUND_TYPES.register("electro", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "electro")));
    public static final RegistryObject<SoundEvent> ELECTRODISTANT = SOUND_TYPES.register("electro_distant", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "electro_distant")));
    public static final RegistryObject<SoundEvent> VISIONON = SOUND_TYPES.register("nightvisionon", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "nightvisionon")));
    public static final RegistryObject<SoundEvent> VISIONOFF = SOUND_TYPES.register("nightvisionoff", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "nightvisionoff")));
    public static final RegistryObject<SoundEvent> JUMP = SOUND_TYPES.register("jumping", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "jumping")));
    public static final RegistryObject<SoundEvent> WINGS_FLAP = SOUND_TYPES.register("wingsflap", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "wingsflap")));
    public static final RegistryObject<SoundEvent> WINGS_TAKEOFF = SOUND_TYPES.register("wingstakeoff", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "wingstakeoff")));
    public static final RegistryObject<SoundEvent> BUZZ = SOUND_TYPES.register("buzz", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "buzz")));
    public static final RegistryObject<SoundEvent> SHIELDHIT = SOUND_TYPES.register("shieldhit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "shieldhit")));
    public static final RegistryObject<SoundEvent> LOSTPOWER = SOUND_TYPES.register("lostpower", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "lostpower")));
    public static final RegistryObject<SoundEvent> WATERHIT = SOUND_TYPES.register("waterhit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "waterhit")));
    public static final RegistryObject<SoundEvent> RODSPOOL = SOUND_TYPES.register("rodspool", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "rodspool")));
    public static final RegistryObject<SoundEvent> FILTERMOB = SOUND_TYPES.register("filtermob", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "filtermob")));
    public static final RegistryObject<SoundEvent> FILTERMOBCLEAR = SOUND_TYPES.register("filtermobclear", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "filtermobclear")));
    public static final RegistryObject<SoundEvent> BUCKET = SOUND_TYPES.register("bucket", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "bucket")));
    public static final RegistryObject<SoundEvent> HANGARDOOROPEN = SOUND_TYPES.register("hangardooropen", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "hangardooropen")));
    public static final RegistryObject<SoundEvent> HANGARDOORCLOSE = SOUND_TYPES.register("hangardoorclose", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "hangardoorclose")));
    public static final RegistryObject<SoundEvent> HANGARDOORFINISHED = SOUND_TYPES.register("hangardoorfinished", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WitherUtils.MODID, "hangardoorfinished")));
}