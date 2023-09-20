package geni.witherutils.base.client.sound;

import com.google.common.collect.Maps;

import geni.witherutils.base.common.init.WUTSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.Level;

import java.util.Map;


public final class MachineSoundController {

    private static final Map<GlobalPos, MachineSound> sounds = Maps.newHashMap();

    public static void stopSound(Level worldObj, BlockPos pos)
    {
        GlobalPos g = GlobalPos.of(worldObj.dimension(), pos);
        if (sounds.containsKey(g))
        {
            AbstractTickableSoundInstance movingSound = sounds.get(g);
            Minecraft.getInstance().getSoundManager().stop(movingSound);
            sounds.remove(g);
        }
    }

    private static void playSound(Level worldObj, BlockPos pos, SoundEvent soundType)
    {
        MachineSound sound = new MachineSound(soundType, worldObj, pos);
        stopSound(worldObj, pos);
        Minecraft.getInstance().getSoundManager().play(sound);
        GlobalPos g = GlobalPos.of(worldObj.dimension(), pos);
        sounds.put(g, sound);
    }

    public static void playStartup(Level worldObj, BlockPos pos) {
        playSound(worldObj, pos, WUTSounds.ENGINESTART.get());
    }

    public static void playLoop(Level worldObj, BlockPos pos) {
        playSound(worldObj, pos, WUTSounds.ENGINELOOP.get());
    }

    public static void playShutdown(Level worldObj, BlockPos pos) {
        playSound(worldObj, pos, WUTSounds.ENGINESTOP.get());
    }

    public static boolean isStartupPlaying(Level worldObj, BlockPos pos) {
        return isSoundTypePlayingAt(WUTSounds.ENGINESTART.get(), worldObj, pos);
    }

    public static boolean isLoopPlaying(Level worldObj, BlockPos pos) {
        return isSoundTypePlayingAt(WUTSounds.ENGINELOOP.get(), worldObj, pos);
    }

    public static boolean isShutdownPlaying(Level worldObj, BlockPos pos) {
        return isSoundTypePlayingAt(WUTSounds.ENGINESTOP.get(), worldObj, pos);
    }

    private static boolean isSoundTypePlayingAt(SoundEvent event, Level world, BlockPos pos)
    {
        MachineSound s = getSoundAt(world, pos);
        return s != null && s.isSoundType(event);
    }
    private static MachineSound getSoundAt(Level world, BlockPos pos)
    {
        return sounds.get(GlobalPos.of(world.dimension(), pos));
    }
}
