package geni.witherutils.base.common.block.spawner;

import java.util.LinkedList;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.item.card.CardItem;
import geni.witherutils.core.common.network.NetworkDataSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class SpawnerBlockEntity extends WitherMachineEnergyBlockEntity implements MenuProvider {

	public static final LinkedList<Entity> spawnerClients = new LinkedList<>();
	
    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    
    public static final Supplier<Integer> CAPACITY = () -> 16000;
    public static final Supplier<Integer> USAGE = () -> 1200;
    
    public boolean showSpawnerPos = false;
    
    private int timer = 100;
	private float delay;
    
	public SpawnerBlockEntity(BlockPos pos, BlockState state)
	{
        super(EnergyIOMode.INPUT, CAPACITY, USAGE, WUTBlockEntityTypes.SPAWNER.get(), pos, state);
        NeoForge.EVENT_BUS.register(new EventHandler());
        addDataSlot(NetworkDataSlot.BOOL.create(this::getShowSpawnerPos, this::setShowSpawnerPos));
	}

	@Override
	public boolean canOpenMenu()
	{
		return true;
	}
	
	@Override
	protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
	{
		return new MachineInventory(layout)
		{
            @Override
            protected void onContentsChanged(int slot)
            {
//                onInventoryContentsChanged(getSoulBankSlot());
                setChanged();
            }
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
            {
            	if(slot == INPUT.getIndex())
            	{
    				if(stack.getItem() instanceof CardItem)
    					return super.insertItem(slot, stack, simulate);
            	}
	      		return super.insertItem(slot, stack, simulate);
            }
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate)
            {
	      		return super.extractItem(slot, amount, simulate);
            }
		};
	}
	@Override
	protected void onInventoryContentsChanged(int slot)
	{
		getFreshDelay();
		super.onInventoryContentsChanged(slot);
	}
	
	public void getFreshDelay()
	{
		float upDelay = 2;
		this.delay = upDelay * 100;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void serverTick()
	{
		super.serverTick();
		
        if(INPUT.getItemStack(inventory).isEmpty())
        	return;
		if(energyStorage.getEnergyStored() < 1250)
		{
	    	timer = 0;
	    	setLitProperty(false);
	        return;
		}
	    setLitProperty(true);
	    
	    if(delay-- > 0)
	    	return;
    	delay = 0;

	    if(timer-- > 0)
	    	return;
    	timer = 100;
    	getFreshDelay();

		if(INPUT.getItemStack(inventory).getItem() instanceof CardItem filter)
		{
//			IItemHandler cap = getInventory().getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
//			if(cap != null)
//			{
//				ItemStack eggStack = cap.getStackInSlot(0);
//				if(!eggStack.isEmpty())
//				{
//					EntityType<?> type = null;
//					SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
//					type = eggItem.getType(null);
//
//					Mob mob = (Mob) type.create(getLevel());
//					mob.getPersistentData().putInt("PublicEnemy", 60);
//					mob.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(new BlockPos((int) mob.position().x, (int) mob.position().y, (int) mob.position().z)), MobSpawnType.NATURAL, null);
//
//					AABB areaToCheck = new AABB(
//							worldPosition.getX() - 8, 
//							worldPosition.getY(), 
//							worldPosition.getZ() - 8, 
//							worldPosition.getX() + 8, 
//							worldPosition.getY() + 2, 
//							worldPosition.getZ() + 8);
//					
//					int entityCount = level.getEntitiesOfClass(LivingEntity.class, areaToCheck).size();
//
//					if (entityCount >= 2)
//						return;
//					else
//					{
//						trySpawnMob((ServerLevel) level, worldPosition, mob);
//						energyStorage.takeEnergy(1250);
//					}
//				}
//			}
		}
	}
	@Override
	public void clientTick()
	{
		super.clientTick();
		
		if (this.isNearPlayer(level, worldPosition) && level.random.nextFloat() < 0.1f)
		{
			RandomSource randomsource = level.getRandom();
			double d0 = (double) worldPosition.getX() + randomsource.nextDouble();
			double d1 = (double) worldPosition.getY() + randomsource.nextDouble();
			double d2 = (double) worldPosition.getZ() + randomsource.nextDouble();
			level.addParticle(WUTParticles.RISINGSOUL.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
			level.addParticle(WUTParticles.SOULFRAGSOFT.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
			level.addParticle(WUTParticles.SOULFRAGHARD.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	private boolean isNearPlayer(Level level, BlockPos pos)
	{
		return level.hasNearbyAlivePlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, (double) 16);
	}
	
	private static void trySpawnMob(ServerLevel world, BlockPos pos, Mob mob)
	{
		if(mob == null)
			return;

		boolean shouldCenter = world.random.nextBoolean();
		int widex = -9 + world.random.nextInt(18);
		int widez = -9 + world.random.nextInt(18);
		float x = pos.getX() + (shouldCenter ? 0.5F : world.random.nextFloat());
		float y = pos.getY() + 1;
		float z = pos.getZ() + (shouldCenter ? 0.5F : world.random.nextFloat());

		mob.moveTo(x + widex, y, z + widez, world.random.nextFloat() * 360.0F, 0.0F);

		if(mob.isInWall())
		{
			mob.setRemoved(RemovalReason.KILLED);
			return;
		}
		if(spawnMobAsCursed(mob))
		{
			mob.playAmbientSound();
			world.levelEvent(1027, pos, 0);
		}
	}
	public static boolean spawnMobAsCursed(Entity mob)
	{
		if(mob instanceof LivingEntity)
		{
			LivingEntity living = (LivingEntity) mob;
			applyAttribute(living, Attributes.ATTACK_DAMAGE, new AttributeModifier(WitherUtilsRegistry.loc("WitherEarth"), 1.5, AttributeModifier.Operation.ADD_VALUE));
			applyAttribute(living, Attributes.MOVEMENT_SPEED, new AttributeModifier(WitherUtilsRegistry.loc("WitherEarth"), 0.2, AttributeModifier.Operation.ADD_VALUE));

			AttributeInstance attributeInstanceByName = living.getAttributes().getInstance(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
			if(attributeInstanceByName != null)
			{
				attributeInstanceByName.setBaseValue(0);
			}
		}
		if(!mob.level().addFreshEntity(mob))
		{
			return false;
		}
		return true;
	}
	private static void applyAttribute(LivingEntity mob, Holder<Attribute> attackDamage, AttributeModifier modifier)
	{
		AttributeInstance instance = mob.getAttribute(attackDamage);
		if(instance != null)
		{
			instance.addPermanentModifier(modifier);
		}
	}

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new SpawnerContainer(this, playerInventory, i);
    }
    
//    @Override
//    public MachineInventoryLayout getInventoryLayout()
//    {
//        return MachineInventoryLayout.builder()
//            .inputSlot().slotAccess(INPUT)
//            .soulbank().build();
//    }

	@OnlyIn(Dist.CLIENT)
	public Entity getEntityToRender()
	{
		Entity entity = null;
		if(!getInventory().getStackInSlot(0).isEmpty())
		{
//			IItemHandler myFilter = getInventory().getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
//			if(myFilter != null)
//			{
//				ItemStack eggStack = myFilter.getStackInSlot(0);
//				if(!eggStack.isEmpty())
//				{
//					SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
//					entity = eggItem.getType(null).create(getLevel());
//				}
//			}
		}
		return entity;
	}
	
    public boolean isStandardMonster(EntityType<?> entity, ServerLevel world, BlockPos pos)
    {
		WeightedRandomList<MobSpawnSettings.SpawnerData> entry = world.getBiome(pos).value().getMobSettings().getMobs(MobCategory.MONSTER);
		EntityType<?> type = entry.getRandom(world.random).get().type;
		return entity.equals(type);
    }

	public boolean getShowSpawnerPos()
	{
		return showSpawnerPos;
	}
	public void setShowSpawnerPos(boolean showFarmingPos)
	{
		this.showSpawnerPos = showFarmingPos;
	}

    public static class EventHandler
    {
    	@SubscribeEvent
    	public void spawnInWorld(final EntityJoinLevelEvent event)
    	{
    		final Entity entity = event.getEntity();
    		if (entity instanceof LivingEntity)
    		{
    			final CompoundTag nbt = entity.getPersistentData();
    			if (nbt.contains("PublicEnemy", 3))
    			{
    				spawnerClients.add(event.getEntity());

    				final int cursedEarth = nbt.getInt("PublicEnemy");
    				if (cursedEarth <= 0)
    				{
    					entity.kill();
    					event.setCanceled(true);
    				}
    				else
    				{
    					Mob mob = (Mob) entity;
    					mob.goalSelector.addGoal(0, new AICursed(mob, cursedEarth));
    				}
    			}
    		}
    	}
        @SuppressWarnings("rawtypes")
		@SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public void render(final RenderLivingEvent.Pre event)
        {
            if (spawnerClients.contains(event.getEntity()))
            {
                final float v = 0.3f;
                RenderSystem.setShaderColor(v, v, v, 1.0f);
            }
        }
        @SuppressWarnings("rawtypes")
		@SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public void render(final RenderLivingEvent.Post event)
        {
        	if (spawnerClients.contains(event.getEntity()))
        	{
            	RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    @SubscribeEvent
    public void spawnInWorld(final EntityJoinLevelEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof LivingEntity)
        {
            final CompoundTag nbt = entity.getPersistentData();
            if (nbt.contains("PublicEnemy", 3))
            {
                final int cursedEarth = nbt.getInt("PublicEnemy");
                if (cursedEarth <= 0)
                {
                    entity.isRemoved();
                    event.setCanceled(true);
                }
                else
                {
                    final LivingEntity living = (LivingEntity)entity;
                    if(entity instanceof Mob mob)
                    	mob.goalSelector.addGoal(0, (Goal) new AICursed(living, cursedEarth));
                }
            }
        }
    }
    
    public static class AICursed extends Goal
    {
        final LivingEntity living;
        int cursedEarth;
        
        public AICursed(final LivingEntity living, final int cursedEarth)
        {
            this.living = living;
            this.cursedEarth = cursedEarth;
        }
		@Override
		public boolean canUse()
		{
			return true;
		}
        @Override
        public boolean canContinueToUse()
        {
        	return true;
        }
		public void updateTask()
        {
            if (this.living.level().getGameTime() % 20L != 0L)
            {
                return;
            }
            if (this.cursedEarth < 0)
            {
                return;
            }
            if (this.cursedEarth == 0)
            {
                this.living.kill();
            }
            else
            {
                --this.cursedEarth;
                this.living.getPersistentData().putInt("PublicEnemy", this.cursedEarth);
            }
        }
    }
}
