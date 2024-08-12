package geni.witherutils.base.common.block.totem.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import net.minecraft.world.entity.LivingEntity;

public interface IMobAttractionHandler {

	enum State
	{
		CAN_ATTRACT,
		CANNOT_ATTRACT,
		ALREADY_ATTRACTING;
	}

	@Nonnull
	State canAttract(TotemBlockEntity attractor, LivingEntity entity);

	void startAttracting(TotemBlockEntity attractor, LivingEntity entity);

	void tick(TotemBlockEntity attractor, LivingEntity entity);

	void release(TotemBlockEntity attractor, LivingEntity entity);
	
	String getHandlerName();
}
