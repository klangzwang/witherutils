package geni.witherutils.base.common.entity.xporb;

import java.util.Map.Entry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class XPOrbFalling extends ExperienceOrb {
	
	private int age;
	public int delayBeforeCanPickup;

	public XPOrbFalling(Level level, double x, double y, double z, int expValue)
	{
		super(EntityType.EXPERIENCE_ORB, level);
		setPos(x, y, z);
		setYRot((float) (Math.random() * 360.0D));
		setDeltaMovement(0D ,0D ,0D);
		value = expValue;
	}

	@Override
	public void tick()
	{
		super.tick();

		if (delayBeforeCanPickup > 0)
			--delayBeforeCanPickup;

		xo = getX();
		yo = getY();
		zo = getZ();
		setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));

	      if (!this.level().noCollision(this.getBoundingBox()))
	          this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());

		this.move(MoverType.SELF, this.getDeltaMovement());

	     if (this.onGround())
	         this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));

		++tickCount;
		++age;

		if (age >= 6000)
			removeAfterChangingDimensions();
	}

	@SuppressWarnings("resource")
	@Override
	public void playerTouch(Player player) {
		if (!level().isClientSide) {
			if (delayBeforeCanPickup == 0 && player.takeXpDelay == 0) {
				if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp(player, this)))
					return;
				player.takeXpDelay = 2;
				player.take(this, 1);
				 Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, player, ItemStack::isDamaged);

		            if (entry != null) {
		                ItemStack itemstack = entry.getValue();
		                if (!itemstack.isEmpty() && itemstack.isDamaged()) {
		                   int i = Math.min((int)(this.value * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
		                   this.value -= durabilityToXp(i);
		                   itemstack.setDamageValue(itemstack.getDamageValue() - i);
		                }
		             }

				if (value > 0)
				addPlayerXP(player, value);

				remove(RemovalReason.DISCARDED);
			}
		}
	}
	public static void addPlayerXP(Player player, int amount)
	{
		int experience = getPlayerXP(player) + amount;
		player.totalExperience = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float)(experience - expForLevel) / (float)player.getXpNeededForNextLevel();
	}
	public static int getPlayerXP(Player player)
	{
		return (getExperienceForLevel(player.experienceLevel) + (int)(player.experienceProgress * player.getXpNeededForNextLevel()));
	}

	public static int getLevelForExperience(int experience)
	{
		int i = 0;
		while (getExperienceForLevel(i) <= experience)
		{
			i++;
		}
		return i - 1;
	}

	public static int getExperienceForLevel(int level)
	{
		if (level == 0)
			return 0;
		if (level > 0 && level < 17)
			return (int) (Math.pow(level, 2) + 6 * level);
		else if (level > 16 && level < 32)
			return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
		else
			return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
	}
	
    private int durabilityToXp(int durability)
    {
        return durability / 2;
    }
    @SuppressWarnings("unused")
	private int xpToDurability(int xp)
    {
        return xp * 2;
    }
}