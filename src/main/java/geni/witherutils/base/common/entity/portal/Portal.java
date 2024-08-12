package geni.witherutils.base.common.entity.portal;

import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class Portal extends Entity implements IEntityWithComplexSpawn {

	public int lifetime;
	public int maxLifetime = 50;
	
	public Portal(Level level)
	{
		super(WUTEntities.PORTAL.get(), level);
	}
	public Portal(EntityType<?> type, Level level)
	{
		super(type, level);
	}
	
	@Override
	public void tick()
	{
		if (this.isAlive())
		{
			if(lifetime < maxLifetime)
				this.lifetime++;
			else
				deconstruct();
		}
		super.tick();
	}
	
	@SuppressWarnings("resource")
	private void deconstruct()
	{
		if (!this.level().isClientSide)
		{
			this.kill();
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {}
	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {}
	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {}
	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) {}
	@Override
	protected void defineSynchedData(Builder pBuilder) {}
}
