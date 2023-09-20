package geni.witherutils.base.common.entity.portal;

import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class Portal extends Entity implements IEntityAdditionalSpawnData {

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
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
	}
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
	}
	@Override
	protected void defineSynchedData()
	{
	}
	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_)
	{
	}
	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_)
	{
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
