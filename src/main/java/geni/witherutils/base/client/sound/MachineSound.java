package geni.witherutils.base.client.sound;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@SuppressWarnings({ "resource", "unused" })
public class MachineSound extends AbstractTickableSoundInstance {

    public MachineSound(SoundEvent event, Level world, BlockPos pos)
    {
        super(event, SoundSource.BLOCKS, world.random);
        this.world = world;
        this.pos = pos;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.attenuation = Attenuation.LINEAR;
        this.looping = true;
        this.delay = 0;
        this.loop = event == WUTSounds.GASOLINE.get();
        this.sound = event;
        this.relative = false;
    }

    private final Level world;
    private final BlockPos pos;
	private final boolean loop;
    private final SoundEvent sound;

	@Override
    public void tick()
    {
        Block block = world.getBlockState(pos).getBlock();
        if (block != WUTBlocks.MINERADV.get() && block != WUTBlocks.MINERBASIC.get())
        {
            stop();
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        double distance = Math.sqrt(this.pos.distToCenterSqr(player.getX(), player.getY(), player.getZ()));
        if (distance > 20)
        {
            volume = 0;
        }
        else
        {
            volume = (float) (0.8 * (20-distance)/20.0);
        }
    }

    protected boolean isSoundType(SoundEvent event)
    {
        return sound == event;
    }
}
