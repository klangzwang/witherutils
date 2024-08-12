package geni.witherutils.base.common.item.hammer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends WitherItem {

	public HammerItem()
	{
		super(new Item.Properties().stacksTo(1).durability(250));
	}

	@Override
	public boolean isRepairable(ItemStack itemstack)
	{
		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		return Math.max(Items.IRON_PICKAXE.getDestroySpeed(stack, state), Items.IRON_SHOVEL.getDestroySpeed(stack, state));
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean isBarVisible(ItemStack pStack)
	{
		return pStack.isDamaged();
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity)
	{
		if(entity.isAlive() && entity instanceof Mob mob)
		{
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 2));
			if(!mob.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))
				mob.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
		}
		player.level().playSound(null, entity.blockPosition(), WUTSounds.HAMMERHIT.get(), SoundSource.PLAYERS, 1.0F, 0.8F + player.level().getRandom().nextFloat() - 0.6F);
		return super.onLeftClickEntity(stack, player, entity);
	}

	public Recipe<?> findMatchingRecipe(Level world, ItemStack dropMe)
	{
		Collection<RecipeHolder<?>> list = world.getServer().getRecipeManager().getRecipes();
		for(RecipeHolder<?> recipe : list)
		{
			if(recipe.value().getType() == RecipeType.CRAFTING)
			{
				if(recipeMatches(world, dropMe, recipe.value()))
				{
					return recipe.value();
				}
			}
		}
		return null;
	}
	
	public boolean recipeMatches(Level level, ItemStack stack, Recipe<?> recipe)
	{
		if(stack.isEmpty() || recipe == null || recipe.getResultItem(level.registryAccess()).isEmpty() || recipe.getResultItem(level.registryAccess()).getCount() > stack.getCount())
		{
			return false;
		}
		return stack.getItem() == recipe.getResultItem(level.registryAccess()).getItem() && ItemStack.matches(stack, recipe.getResultItem(level.registryAccess()));
	}
	
	public boolean uncraftRecipe(Level level, BlockPos pos, Recipe<?> match)
	{
		List<ItemStack> result = match.getIngredients().stream().flatMap(ingredient -> Arrays.stream(ingredient.getItems()).filter(stack -> !stack.hasCraftingRemainingItem()).findAny().map(Stream::of).orElseGet(Stream::empty)).collect(Collectors.toList());
        return !result.isEmpty();
    }
	
	public void hammerSmash(Level level, Player player, BlockPos pos, Recipe<?> match)
	{
		List<ItemStack> result = match.getIngredients().stream().flatMap(ingredient -> Arrays.stream(ingredient.getItems()).filter(stack -> !stack.hasCraftingRemainingItem()).findAny().map(Stream::of).orElseGet(Stream::empty)).collect(Collectors.toList());
		for(ItemStack r : result)
		{
			ItemStack itemstack = r.copy();
	        double d0 = level.random.nextFloat() * 0.5F + 0.25D;
	        double d1 = level.random.nextFloat() * 0.5F + 0.25D;
	        double d2 = level.random.nextFloat() * 0.5F + 0.25D;
	        ItemEntity entityitem = new ItemEntity(level, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, itemstack);
	        entityitem.setDefaultPickUpDelay();
	        entityitem.hurt(level.damageSources().inFire(), -100);
	        entityitem.setRemainingFireTicks(10);
	        level.destroyBlock(pos, false);
	        level.addFreshEntity(entityitem);
	        player.getCooldowns().addCooldown(this, 12);
	        player.getMainHandItem().setDamageValue(player.getMainHandItem().getDamageValue() + 5);
			if(player.getMainHandItem().getDamageValue() >= player.getMainHandItem().getMaxDamage())
			{
				player.getMainHandItem().shrink(1);
			}
		}
	}
}
