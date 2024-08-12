package geni.witherutils.base.common.entity;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

public class EntityEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onMonkSpawning(FinalizeSpawnEvent event)
	{
//		if (event.getSpawnType() == MobSpawnType.SPAWNER)
//			return;
//	
//		LivingEntity entity = event.getEntity();
//		LevelAccessor level = event.getLevel();
//
//		if(entity instanceof WitherMonk monk)
//		{
//			if(!level.getBlockState(monk.blockPosition().below()).getBlock().toString().contains("black"))
//			{
//				System.out.println("EntityEventHandler: " + monk + " is CANCELED!!!");
//				event.setCanceled(true);
//			}
//		}
//		if(entity instanceof Piglin piglin && piglin.getType() != EntityType.ZOMBIFIED_PIGLIN)
//		{
//			if(level.getBlockState(piglin.blockPosition().below()).getBlock().toString().contains("black"))
//			{
//				WitherMonk hammerPig = piglin.convertTo(WUTEntities.WITHERMONK.get(), true);
//				System.out.println("EntityEventHandler: " + hammerPig.blockPosition());
//			}
//		}
	}
}
