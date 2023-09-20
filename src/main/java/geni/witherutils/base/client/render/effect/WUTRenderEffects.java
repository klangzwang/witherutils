package geni.witherutils.base.client.render.effect;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class WUTRenderEffects {
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onBreakingBlock(BreakEvent event)
    {
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();

        if(block instanceof WitherAbstractBlock)
        {
            for(int i = 0; i < 1; i++)
            {
                level.levelEvent(2001, pos, Block.getId(block.defaultBlockState()));
                if(level instanceof ServerLevel)
                {
                    ServerLevel serverlevel = (ServerLevel) level;
                    datSmokeComes(serverlevel, pos, block.asItem());
                }
            }
        }
    }
    public static void datSmokeComes(ServerLevel level, BlockPos pos, Item thisItem)
    {
        for(int i = 0; i < 20; ++i)
        {
            level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Block.byItem(thisItem))),
                (double)pos.getX() + 0.5D,
                (double)pos.getY() + 0.7D,
                (double)pos.getZ() + 0.5D, 3,
                ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                (double)0.15F);
        }
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlacingBlock(EntityPlaceEvent event)
    {
        ServerLevel serverlevel = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        RandomSource random = RandomSource.create();

        if(block instanceof WitherAbstractBlock)
        {
            for(int i = 0; i < 14; i++)
            {
                serverlevel.sendParticles(ParticleTypes.SMOKE,
                        pos.getX() + 0.5D + (random.nextDouble() - 0.5D),
                        pos.getY() + 0.0,
                        pos.getZ() + 0.5D + (random.nextDouble() - 0.5D),
                        0,
                        0.03D, 0.01D, 0.03D, 0.5D);
                serverlevel.sendParticles(ParticleTypes.SMOKE,
                        pos.getX() + 0.5D + (random.nextDouble() - 0.5D),
                        pos.getY() + 0.0,
                        pos.getZ() + 0.5D + (random.nextDouble() - 0.5D),
                        0,
                        -0.03D, 0.01D, -0.03D, 0.5D);
                serverlevel.sendParticles(ParticleTypes.SMOKE,
                        pos.getX() + 0.5D + (random.nextDouble() - 0.5D),
                        pos.getY() + 0.0,
                        pos.getZ() + 0.5D + (random.nextDouble() - 0.5D),
                        0,
                        0.03D, 0.01D, -0.03D, 0.5D);
                serverlevel.sendParticles(ParticleTypes.SMOKE,
                        pos.getX() + 0.5D + (random.nextDouble() - 0.5D),
                        pos.getY() + 0.0,
                        pos.getZ() + 0.5D + (random.nextDouble() - 0.5D),
                        0,
                        -0.03D, 0.01D, 0.03D, 0.5D);
            }
        }
    }
}
