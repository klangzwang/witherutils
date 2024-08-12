package geni.witherutils.base.common.entity.cursed.zombie;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class CursedZombie extends Zombie implements IEntityWithComplexSpawn {

    private boolean transforming;
    private int tcounter;
    
	public CursedZombie(EntityType<? extends CursedZombie> type, Level worldIn)
	{
		super(type, worldIn);
		
        transforming = false;
        tcounter = 0;
        setHuman(true);
	}

    @SuppressWarnings("resource")
	@Override
    public void tick()
    {
    	if(!isHuman())
    	{
    		double pad = 0.2;
    		
    		AABB aabb = getBoundingBox();
    		double x = aabb.minX + Math.random() * (aabb.maxX - aabb.minX + (pad * 2)) - pad;
    		double y = aabb.minY + Math.random() * (aabb.maxY - aabb.minY + (pad * 2)) - pad;
    		double z = aabb.minZ + Math.random() * (aabb.maxZ - aabb.minZ + (pad * 2)) - pad;
    		getCommandSenderWorld().addParticle(ParticleTypes.MYCELIUM, x, y, z, 0, 0, 0);
    	
    		if(Math.random() < 0.1)
    		{
    			y = aabb.minY + 0.1;
    			getCommandSenderWorld().addParticle(ParticleTypes.SOUL, x, y, z, 0, 0, 0);
    		}
    	}

        if (!this.level().isClientSide && this.isAlive() && this.isConverting())
        {
            int i = this.getConversionProgress();
            
            this.villagerConversionTime -= i;
            
            if (this.villagerConversionTime <= 0 && net.neoforged.neoforge.event.EventHooks.canLivingConvert(this, EntityType.VILLAGER, (timer) -> this.villagerConversionTime = timer))
            {
                this.finishConversion((ServerLevel)this.level());
            }
        }

        super.tick();
    }

	public static void init(RegisterSpawnPlacementsEvent event)
    {
		event.register(WUTEntities.CURSEDZOMBIE.get(), Monster::checkMonsterSpawnRules);
    }
	
	public static AttributeSupplier.Builder registerAttributes()
	{
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 10)
				.add(Attributes.FOLLOW_RANGE, 35)
				.add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.ATTACK_DAMAGE, 3)
				.add(Attributes.ARMOR, 0);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		if(isHuman())
			return SoundEvents.VILLAGER_AMBIENT;
		else
			return SoundEvents.ZOMBIE_AMBIENT;
	}
	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource)
	{
		if(isHuman())
			return SoundEvents.VILLAGER_HURT;
		else
			return SoundEvents.ZOMBIE_HURT;
	}
	@Override
	protected SoundEvent getDeathSound()
	{
		if(isHuman())
			return SoundEvents.VILLAGER_DEATH;
		else
			return SoundEvents.ZOMBIE_DEATH;
	}

	/*
	 * 
	 * HUMANFORM/HUNCHED
	 * 
	 */
	private static final EntityDataAccessor<Boolean> DATA_HUMAN = SynchedEntityData.defineId(CursedZombie.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_HUNCHED = SynchedEntityData.defineId(CursedZombie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(CursedZombie.class, EntityDataSerializers.BOOLEAN);
    
    private int villagerConversionTime;
    
    @Nullable
    private UUID conversionStarter;
    
    private void finishConversion(ServerLevel pServerLevel)
    {
//        Villager villager = this.convertTo(EntityType.VILLAGER, false);
//        if (villager != null) {
//            for (EquipmentSlot equipmentslot : this.dropPreservedEquipment(
//                p_351901_ -> !EnchantmentHelper.has(p_351901_, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)
//            )) {
//                SlotAccess slotaccess = villager.getSlot(equipmentslot.getIndex() + 300);
//                slotaccess.set(this.getItemBySlot(equipmentslot));
//            }
//
//            villager.setVillagerData(this.getVillagerData());
//            if (this.gossips != null) {
//                villager.setGossips(this.gossips);
//            }
//
//            if (this.tradeOffers != null) {
//                villager.setOffers(this.tradeOffers.copy());
//            }
//
//            villager.setVillagerXp(this.villagerXp);
//            villager.finalizeSpawn(pServerLevel, pServerLevel.getCurrentDifficultyAt(villager.blockPosition()), MobSpawnType.CONVERSION, null);
//            villager.refreshBrain(pServerLevel);
//            if (this.conversionStarter != null) {
//                Player player = pServerLevel.getPlayerByUUID(this.conversionStarter);
//                if (player instanceof ServerPlayer) {
//                    CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((ServerPlayer)player, this, villager);
//                    pServerLevel.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, player, villager);
//                }
//            }
//
//            villager.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
//            if (!this.isSilent()) {
//                pServerLevel.levelEvent(null, 1027, this.blockPosition(), 0);
//            }
//            net.neoforged.neoforge.event.EventHooks.onLivingConvert(this, villager);
//        }
    }
    
    public boolean isConverting()
    {
        return this.getEntityData().get(DATA_CONVERTING_ID);
    }
    
    private void startConverting(@Nullable UUID pConversionStarter, int pVillagerConversionTime)
    {
        this.conversionStarter = pConversionStarter;
        this.villagerConversionTime = pVillagerConversionTime;
        this.getEntityData().set(DATA_CONVERTING_ID, true);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, pVillagerConversionTime, Math.min(this.level().getDifficulty().getId() - 1, 0)));
        this.level().broadcastEntityEvent(this, (byte)16);
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 16) {
            if (!this.isSilent()) {
                this.level()
                    .playLocalSound(
                        this.getX(),
                        this.getEyeY(),
                        this.getZ(),
                        SoundEvents.ZOMBIE_VILLAGER_CURE,
                        this.getSoundSource(),
                        1.0F + this.random.nextFloat(),
                        this.random.nextFloat() * 0.7F + 0.3F,
                        false
                    );
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    private int getConversionProgress()
    {
        int i = 1;
        if (this.random.nextFloat() < 0.01F)
        {
            int j = 0;
            for (int k = (int)this.getX() - 4; k < (int)this.getX() + 4 && j < 14; k++) {
                for (int l = (int)this.getY() - 4; l < (int)this.getY() + 4 && j < 14; l++) {
                    for (int i1 = (int)this.getZ() - 4; i1 < (int)this.getZ() + 4 && j < 14; i1++) {
                        if (this.random.nextFloat() < 0.3F) {
                            i++;
                        }
                        j++;
                    }
                }
            }
        }
        return i;
    }
	
	/*
	 * 
	 * CREEPY
	 * 
	 */
	private static final ResourceLocation SPEED_MODIFIER_ID = ResourceLocation.withDefaultNamespace("boost");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ID, 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(CursedZombie.class, EntityDataSerializers.BOOLEAN);
	
	public boolean isCreepy()
	{
		return this.entityData.get(DATA_CREEPY);
	}
	

	@SuppressWarnings("resource")
	public void setCreepy(boolean flag)
    {
        if (level().isClientSide)
        	return;
        this.entityData.set(DATA_CREEPY, flag);
    }
    
    public boolean isHuman()
    {
        return this.entityData.get(DATA_HUMAN);
    }

    @SuppressWarnings("resource")
	public void setHuman(boolean flag)
    {
        if (level().isClientSide)
        	return;
        this.entityData.set(DATA_HUMAN, flag);
    }

    public boolean isHunched()
    {
        return this.entityData.get(DATA_HUNCHED);
    }

    @SuppressWarnings("resource")
	public void setHunched(boolean flag)
    {
        if (level().isClientSide)
        	return;
        this.entityData.set(DATA_HUNCHED, flag);
    }
    
	@Override
	public void addAdditionalSaveData(@Nonnull CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		
		compound.putBoolean("HumanForm", isHuman());
		
		compound.putInt("ConversionTime", this.isConverting() ? this.villagerConversionTime : -1);
        if (this.conversionStarter != null)
        {
        	compound.putUUID("ConversionPlayer", this.conversionStarter);
        }
	}
	
	@Override
	public void readAdditionalSaveData(@Nonnull CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		
        setHuman(compound.getBoolean("HumanForm"));
        
        if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1)
        {
            this.startConverting(compound.hasUUID("ConversionPlayer") ? compound.getUUID("ConversionPlayer") : null, compound.getInt("ConversionTime"));
        }
	}
	
	@Override
	protected void defineSynchedData(Builder pBuilder)
	{
		super.defineSynchedData(pBuilder);
		pBuilder.define(DATA_CREEPY, false);
		pBuilder.define(DATA_HUMAN, false);
		pBuilder.define(DATA_HUNCHED, false);
        pBuilder.define(DATA_CONVERTING_ID, false);
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity target)
	{
		AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if(target == null)
		{
			this.entityData.set(DATA_CREEPY, false);
			attributeinstance.removeModifier(SPEED_MODIFIER_ATTACKING);		
		}
		else
		{
			this.entityData.set(DATA_CREEPY, true);
			if (!attributeinstance.hasModifier(SPEED_MODIFIER_ID))
				attributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
			super.setTarget(target);
		}
	}
    
	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer)
	{
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData)
	{
	}

    @Override
    public boolean doHurtTarget(Entity entity)
    {
        if (isHuman())
        {
            setTarget(null);
            return false;
        }
        if (getLastDamageSource() != null && (random.nextInt(15) == 0))
        {
            if (onGround())
            {
                setHunched(true);
                double d = entity.position().x - position().x;
                double d1 = entity.position().z - position().z;
                float f1 = Mth.sqrt(((float)d * (float)d) + ((float)d1 * (float)d1));
                xo = ((d / f1) * 0.5D * 0.80000001192092896D) + (xo * 0.20000000298023221D);
                zo = ((d1 / f1) * 0.5D * 0.80000001192092896D) + (zo * 0.20000000298023221D);
                yo = 0.40000000596046448D;
            }
        }
        else
        {
        	if(entity != null)
        	{
                if (attackStrengthTicker <= 0 && (entity.getBoundingBox().maxY > getBoundingBox().minY) && (entity.getBoundingBox().minY < getBoundingBox().maxY))
                {
                	attackStrengthTicker = 20;
                    hurt(damageSources().mobAttack(this), 2);
                }
        	}
        }
    	return super.doHurtTarget(entity);
    }
    
    @Override
    public boolean hurt(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getEntity();
        if (!isHuman() && (entity != null) && (entity instanceof Player entityplayer))
        {
            ItemStack itemstack = entityplayer.getWeaponItem();
            if (itemstack != null)
            {
                i = itemstack.getItem().getDamage(itemstack);
                if (itemstack.getItem() == Items.WOODEN_AXE)
                {
                    i = 0;
                }
                if (itemstack.getItem() == Items.WOODEN_HOE)
                {
                    i = 0;
                }
                if (itemstack.getItem() == Items.WOODEN_PICKAXE)
                {
                    i = 0;
                }
                if (itemstack.getItem() == Items.WOODEN_SHOVEL)
                {
                    i = 0;
                }
                if (itemstack.getItem() == Items.WOODEN_SWORD)
                {
                    i = 0;
                }
            }
        }
    	return super.hurt(damagesource, i);
    }
    
    @Override
    public LivingEntity getTarget()
    {
        if (isHuman())
        {
        	return null;
        }
        
        Player entityplayer = this.level().getNearestPlayer(this, 16D);
        if ((entityplayer != null) && canBeSeenAsEnemy())
        {
            return entityplayer;
        }
        else
        {
            return null;
        }
    }

    @Override
    protected void moveTowardsClosestSpace(double pX, double pY, double pZ)
    {
        if (!isHuman() && onGround())
        {
            xo *= 1.2D;
            zo *= 1.2D;
        }
    	super.moveTowardsClosestSpace(pX, pY, pZ);
    }

//    @Override
//    public void onRemovedFromLevel()
//    {
//        Entity entity = getLastDamageSource().getEntity();
//        if ((deathScore > 0) && (entity != null))
//        {
//            entity.awardKillScore(entity, deathScore, getLastDamageSource());
//        }
//    	super.onRemovedFromLevel();
//    }

    @SuppressWarnings("resource")
	@Override
    public void baseTick()
    {
        super.baseTick();
        if (!this.level().isClientSide)
        {
            if (((!this.level().isDay() && isHuman()) || (this.level().isDay() && !isHuman())) && (this.level().random.nextInt(250) == 0))
            {
                transforming = true;
            }
            if (isHuman() && (getTarget() != null))
            {
                canAttack(null);
            }
            if ((getTarget() != null) && !isHuman() && ((getTarget().position().x - position().x) > 3D) && ((getTarget().position().z - position().z) > 3D))
            {
                setHunched(true);
            }
            if (isHunched() && (this.level().random.nextInt(50) == 0))
            {
                setHunched(false);
            }
            if (transforming && (this.level().random.nextInt(3) == 0))
            {
                tcounter++;
                if ((tcounter % 2) == 0)
                {
                	position().add(0.29999999999999999D, 0, 0);
                	position().add(0, tcounter / 30, 0);
                	
                	if(getTarget() != null)
                		doHurtTarget(getTarget());
                }
                if ((tcounter % 2) != 0)
                {
                	position().add(-0.29999999999999999D, 0, 0);
                }
                if (tcounter == 10)
                {
                	this.level().playSound(this, blockPosition(), WUTSounds.WORMBIP.get(), getSoundSource(), 1.0F, ((this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) + 1.0F);
                }
                if (tcounter > 30)
                {
                    Transform();
                    tcounter = 0;
                    transforming = false;
                }
            }
            if (this.level().random.nextInt(300) == 0)
            {
//                age -= 100 * worldObj.difficultySetting;
//                if (entityAge < 0)
//                {
//                    entityAge = 0;
//                }
            }
        }
    }

    private void Transform()
    {
        if (deathTime > 0) { return; }
        int i = Mth.floor(position().x);
        int j = Mth.floor(getBoundingBox().minY) + 1;
        int k = Mth.floor(position().z);
        float f = 0.1F;
        for (int l = 0; l < 30; l++)
        {
            double d = i + this.level().getRandom().nextFloat();
            double d1 = j + this.level().getRandom().nextFloat();
            double d2 = k + this.level().getRandom().nextFloat();
            double d3 = d - i;
            double d4 = d1 - j;
            double d5 = d2 - k;
            double d6 = Mth.sqrt(((float)d3 * (float)d3) + ((float)d4 * (float)d4) + ((float)d5 * (float)d5));
            d3 /= d6;
            d4 /= d6;
            d5 /= d6;
            double d7 = 0.5D / ((d6 / f) + 0.10000000000000001D);
            d7 *= (this.level().getRandom().nextFloat() * this.level().getRandom().nextFloat()) + 0.3F;
            d3 *= d7;
            d4 *= d7;
            d5 *= d7;
            this.level().addParticle(ParticleTypes.EXPLOSION, (d + (i * 1.0D)) / 2D, (d1 + (j * 1.0D)) / 2D, (d2 + (k * 1.0D)) / 2D, d3, d4, d5);
        }

        if (isHuman())
        {
            setHuman(false);
            setHealth(40);
            transforming = false;
        }
        else
        {
            setHuman(true);
            setHealth(15);
            transforming = false;
        }
    }
    
    @Override
    protected void updateNoActionTime()
    {
        if (!transforming)
        {
        	super.updateNoActionTime();
        }
    }

    public float getMoveSpeed()
    {
        if (isHunched()) { return 0.9F; }
        return 0.7F;
    }
}
