package geni.witherutils.base.common.entity.soulorb;

import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoulOrbProjectile extends ThrowableItemProjectile {

	private int age = 255;
	
	public SoulOrbProjectile(EntityType<? extends SoulOrbProjectile> p_37391_, Level p_37392_)
	{
		super(p_37391_, p_37392_);
	}

	public SoulOrbProjectile(Level p_37399_, LivingEntity p_37400_)
	{
		super(WUTEntities.SOULORBPRO.get(), p_37400_, p_37399_);
	}

	public SoulOrbProjectile(Level p_37394_, double p_37395_, double p_37396_, double p_37397_)
	{
		super(WUTEntities.SOULORBPRO.get(), p_37395_, p_37396_, p_37397_, p_37394_);
	}

	@Override
	protected Item getDefaultItem()
	{
		return ItemStack.EMPTY.getItem(); //WUTItems.SOULORB.get();
	}

	@Override
	public void handleEntityEvent(byte p_37402_)
	{
//		for (int i = 0; i < 8; ++i)
//		{
//			this.level().addParticle(ParticleTypes.CLOUD,
//					this.getX(),
//					this.getY(),
//					this.getZ(),
//					0.0D, 0.0D, 0.0D);
//		}
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		double d8 = (double) this.getX() - this.random.nextDouble() + 0.5D;
		double d9 = (double) this.getZ() - this.random.nextDouble() + 0.5D;

		this.level().addParticle(WUTParticles.SOULORB.get(),
				this.getX(),
				this.getY(),
				this.getZ(),
				0.0D, 0.0D, 0.0D);

		for(int i = 0; i < 2; i++)
		{
			this.level().addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR,
					d8, this.getY() - 0.5D, d9,
					0.0D, -0.1D, 0.0D);
		}
		this.level().addParticle(ParticleTypes.CLOUD,
				d8, this.getY() - 0.5D, d9,
				0.0D, -0.1D, 0.0D);
		
		this.age -= 40;
		if(this.age <= 0)
		{
			this.discard();
		}
	}
	
	public int getAge()
	{
		return age;
	}
}
