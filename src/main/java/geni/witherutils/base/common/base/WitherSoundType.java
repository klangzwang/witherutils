package geni.witherutils.base.common.base;

import java.util.function.Supplier;

import geni.witherutils.base.common.init.WUTSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public class WitherSoundType extends DeferredSoundType {

	public WitherSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn)
	{
		super(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
	}

	public static final WitherSoundType STANDARD = new WitherSoundType(1.0F, 1.0F,
			() -> SoundEvents.WOOD_BREAK,
			() -> SoundEvents.WOOD_STEP,
			() -> WUTSounds.WOODPOOL.get(),
			() -> WUTSounds.WOODPOOL.get(),
			() -> SoundEvents.WOOD_FALL
			);
	
	public static final WitherSoundType SOULWOOD = new WitherSoundType(1.0F, 1.0F,
			() -> SoundEvents.WOOD_BREAK,
			() -> SoundEvents.WOOD_STEP,
			() -> WUTSounds.WOODPOOL.get(),
			() -> WUTSounds.WOODPOOL.get(),
			() -> SoundEvents.WOOD_FALL
			);
	
	public static final WitherSoundType SOULMETAL = new WitherSoundType(1.0F, 1.0F,
            () -> WUTSounds.METALBREAK.get(),
            () -> SoundEvents.NETHERITE_BLOCK_STEP,
            () -> WUTSounds.METALPLACE.get(),
            () -> WUTSounds.METALPLACE.get(),
            () -> SoundEvents.NETHERITE_BLOCK_FALL
            );
}
