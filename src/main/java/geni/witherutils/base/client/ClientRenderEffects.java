package geni.witherutils.base.client;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.config.client.EffectsConfig;
import geni.witherutils.base.common.init.WUTParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ClientRenderEffects {
    
    @SubscribeEvent
    public static void onBreakingBlock(BlockEvent.BreakEvent event)
    {
		if (!EffectsConfig.CAN_PARTICLES.get())
			return;
    	
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();

        var name = BuiltInRegistries.BLOCK.getKey(block);
        if (name.getNamespace().equals(Names.MODID))
        {
            ServerLevel serverlevel = (ServerLevel) level;
            datSmokeComes(serverlevel, pos, block.asItem());
        }
    }
    
    public static void datSmokeComes(ServerLevel level, BlockPos pos, Item thisItem)
    {
    	double dirMultiplier = 0.5D;
    	double dirYMultiplier = -0.5D;
		level.sendParticles(WUTParticles.SOULFRAGSOFT.get(), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 20, dirMultiplier, dirYMultiplier, -dirMultiplier, 0.05D);
		level.sendParticles(WUTParticles.SOULFRAGSOFT.get(), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 20, -dirMultiplier, dirYMultiplier, dirMultiplier, 0.05D);
    }
    
    @SubscribeEvent
    public static void onPlacingBlock(BlockEvent.EntityPlaceEvent event)
    {
		if (!EffectsConfig.CAN_PARTICLES.get())
			return;
    	
//		System.out.printf("Created EntityPlaceEvent - [PlacedBlock: %s ][PlacedAgainst: %s ][Entity: %s ]\n", event.getPlacedBlock(), event.getPlacedAgainst(), event.getEntity());
		
        LevelAccessor levelacc = event.getLevel();
        Block block = event.getState().getBlock();

        ItemStack stack = new ItemStack(Block.byItem(event.getPlacedAgainst().getBlock().asItem()));
        
        var name = BuiltInRegistries.BLOCK.getKey(block);
        if (name.getNamespace().equals(Names.MODID) && !stack.isEmpty())
        {
            if(levelacc instanceof ServerLevel serverLevel)
            {
                Player player = (Player) event.getEntity();
                Vec3 vec3 = player.getEyePosition();
                Vec3 vec31 = vec3.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
                BlockHitResult blockhitresult = serverLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            	
                double x = blockhitresult.getBlockPos().getX() + 0.5D;
                double y = blockhitresult.getBlockPos().getY() + 0.5D;
                double z = blockhitresult.getBlockPos().getZ() + 0.5D;
                
                for(int m = 0; m < 50; m++)
                {
                	serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Block.byItem(event.getPlacedAgainst().getBlock().asItem()))),
                	x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.1);
                }
            }
        }
    }
}
