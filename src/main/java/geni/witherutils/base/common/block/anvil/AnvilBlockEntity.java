package geni.witherutils.base.common.block.anvil;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.api.io.IIOConfig;
import geni.witherutils.api.io.IOMode;
import geni.witherutils.base.common.base.IInteractBlockEntity;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.FixedIOConfig;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.base.common.recipes.AnvilRecipe;
import geni.witherutils.core.common.sync.FloatDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

public class AnvilBlockEntity extends WitherMachineBlockEntity implements IInteractBlockEntity {

    public static final SingleSlotAccess INPUT = new SingleSlotAccess();

    private float hotCounter;
    private int hitCounter;
    private int bonusCount;
    private boolean containRecipe;
    private RecipeWrapper wrapper;
    
    @Nullable
    private AnvilRecipe recipe;
	
	public AnvilBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.ANVIL.get(), pos, state);
        add2WayDataSlot(new FloatDataSlot(this::getHotCounter, p -> hotCounter = p, SyncMode.WORLD));
        addDataSlot(new IntegerDataSlot(this::getHitCounter, p -> hitCounter = p, SyncMode.WORLD));
        wrapper = new RecipeWrapper(getInventory());
	}

	@Override
	protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
	{
		return new MachineInventory(getIOConfig(), layout)
		{
			@Override
			public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
			{
				if(slot == INPUT.getIndex())
					hasIndexRecipes(stack);
				return super.insertItem(slot, stack, simulate);
			}
	        @Override
	        protected void onContentsChanged(int slot)
	        {
	            super.onContentsChanged(slot);

	            if (slot == 0)
	            {
	            	setChanged();
	            }
	        }
	        @Override
	        public int getSlotLimit(int slot)
	        {
	            return 1;
	        }
		};
	}
	
    @Override
    public void serverTick()
	{
        super.serverTick();

        if(hotCounter <= 0)
        	hotCounter = 0;
        else if(hotCounter >= 1)
        	hotCounter = 1;
        
        if(hotCounter > 0.8)
        	this.hotCounter -= 0.02f;
        else
        	this.hotCounter -= 0.01f;
	}

	@Override
    public void clientTick()
    {
        super.clientTick();
        setLitProperty(!INPUT.getItemStack(inventory).isEmpty());
    }

	public void getRecipeResult()
	{
	}
	
	/*
	 * 
	 * USE AND ACTIVE
	 * 
	 */
	@Override
	public InteractionResult onBlockUse(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
	{
        this.findMatchingRecipe();

        for(InteractionHand hands : InteractionHand.values())
        {
			ItemStack heldStack = player.getItemInHand(hands);
			if (heldStack.getItem() instanceof HammerItem || heldStack.getItem() == WUTItems.HAMMER.get())
			{
				if(hotCounter < 0.6)
					this.hotCounter += 0.06f;
				this.hotCounter += 0.04f;
			}
        }

        if(recipe == null || INPUT.getItemStack(inventory).isEmpty())
            return InteractionResult.SUCCESS;

        if (recipe == null || !recipe.matches(wrapper, level))
        {
            this.findMatchingRecipe();
            if (recipe == null)
                return InteractionResult.SUCCESS;
        }

        if(this.hitCounter == recipe.getHitCounter())
        {
            if(tryProcessRecipe())
            {
            	if(player instanceof ServerPlayer)
            	{
            		ServerPlayer splayer = (ServerPlayer) player;
                	popExperience(splayer, recipe.getExperience());
            	}
            }
        }
        else
        {
            for(InteractionHand hands : InteractionHand.values())
            {
				ItemStack heldStack = player.getItemInHand(hands);
				if (heldStack.getItem() instanceof HammerItem || heldStack.getItem() == WUTItems.HAMMER.get())
				{
					hitCounter += 1;

					if (ItemsConfig.ANVILCOOLDOWN.get())
						player.getCooldowns().addCooldown(heldStack.getItem(), 6);

					if (!player.isCreative() && !player.isSpectator())
					{
						ItemStackUtil.damageItem(player, heldStack);
						double foodexhaustion = ItemsConfig.ANVILFOODEXHAUSTION.get();
						player.getFoodData().addExhaustion((float) foodexhaustion);
					}
				}
                return InteractionResult.SUCCESS;
            }
        }
		return IInteractBlockEntity.super.onBlockUse(state, player, hand, hit);
	}
	
    private void findMatchingRecipe()
    {
        if(recipe != null && recipe.matches(wrapper, level))
            return;
        recipe = null;
        List<AnvilRecipe> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.ANVIL.get());
        for(AnvilRecipe rec : recipes)
        {
            if(rec.matches(wrapper, level))
            {
                recipe = rec;
                break;
            }
        }
    }

	private boolean tryProcessRecipe()
    {
		if (recipe.matches(wrapper, level))
		{
			if (recipe.getBonusCount() > 0)
			{
				if(level.random.nextInt(100) < recipe.getBonusCount())
					bonusCount = recipe.getBonusCount();
				else
					bonusCount = 0;
			}

			dropSpecialStack(getRecipeCountVanilla(INPUT) + bonusCount, recipe.getResultItem(level.registryAccess()));

	        getInventory().getStackInSlot(0).shrink(1);
			crashLastHit();
			hitCounter = 0;
			return true;
		}
		return false;
    }
    
	public void crashLastHit()
	{
		level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), WUTSounds.HAMMERHIT.get(), SoundSource.PLAYERS, 1.0F, 0.8F + level.random.nextFloat() - 0.6F, false);
		level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.25F, 0.8F + level.random.nextFloat() - 0.6F, false);
	}
	
	@Override
	public void load(CompoundTag tag)
	{
		this.containRecipe = tag.getBoolean("contain_recipe");
		hitCounter = tag.getInt("hitcounter");
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
        tag.putBoolean("contain_recipe", this.containRecipe);
		tag.putInt("hitcounter", hitCounter);
		super.saveAdditional(tag);
	}

    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder().inputSlot().slotAccess(INPUT).setStackLimit(1).build();
    }

    @Override
    protected IIOConfig createIOConfig()
    {
        return new FixedIOConfig(IOMode.DISABLED);
    }
	
    @Override
    public void popExperience(ServerPlayer serverplayer, int count)
    {
        int i = Mth.floor((float)count * recipe.getExperience() / 5);
        float f = Mth.frac((float)count * recipe.getExperience() / 5);
        if (f != 0.0F && Math.random() < (double) f)
        {
           ++i;
        }
    	ExperienceOrb.award(serverplayer.serverLevel(), serverplayer.position(), i);
    }
	
	/*
	 * 
	 * RECIPE
	 * 
	 */
    @Nullable
    public AnvilRecipe currRecipe()
    {
        return this.recipe;
    }
    public void onSlotChanged(int index)
    {
        if (!isClientSide())
        {
            checkRecipe();
        }
    }
    private void checkRecipe()
    {
        if (this.level != null && !isClientSide())
        {
            Optional<AnvilRecipe> recipe = this.level.getRecipeManager().getRecipeFor(WUTRecipes.ANVIL.get(), wrapper, this.level);
            if (recipe.isPresent())
            {
                this.recipe = recipe.get();
                System.out.println("" + recipe.get() + " recipe found and set to: " + this.recipe);
            }
            else
            {
            	System.out.println("no recipe found!!!");
            }
            setContainRecipe(recipe.isPresent());
        }
    }
    @SuppressWarnings("unused")
	private void checkRecipe(ItemStack stack)
    {
        if (this.level != null && !isClientSide())
        {
            List<AnvilRecipe> recipe = this.level.getRecipeManager().getAllRecipesFor(WUTRecipes.ANVIL.get());
			if (recipe != null && getIngredientStackz() == stack)
                System.out.println("recipe found and set to: " + this.recipe);
            else
            	System.out.println("no recipe found!!!");
        }
    }
    public boolean containRecipe()
    {
        return this.containRecipe;
    }
    public void setContainRecipe(boolean containRecipe)
    {
        this.containRecipe = containRecipe;
    }
    
    public AnvilRecipe getRecipe()
    {
        return recipe;
    }
    public float getHotCounter()
    {
        return hotCounter;
    }
    public void setHotCounter(float hotCounter)
    {
        this.hotCounter = hotCounter;
    }
    public int getHitCounter()
    {
        return hitCounter;
    }
    public int getBonusChance()
    {
        return hitCounter;
    }

	@Nullable
	public ItemStack getIngredientStackz()
	{
		return ItemStack.EMPTY;
	}
	
	public boolean checkForSameStackz(Level level, ItemStack stackIn)
	{
		return getIngredientStackz() == stackIn;
	}
	
	public boolean isPlayerHoldRecipe(Level level, Player player)
	{
    	ItemStack plStack = ItemStack.EMPTY;
    	ItemStack rcStack = ItemStack.EMPTY;
        for(InteractionHand hands : InteractionHand.values())
        {
        	plStack = player.getItemInHand(hands);
        	rcStack = getIngredientStackz();
        }
        return plStack == rcStack;
	}
	
	/*
	 * 
	 * CANINSERTSLOT
	 * 
	 */
	public ItemStack hasIndexRecipes(ItemStack stackIn)
	{
		List<AnvilRecipe> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.ANVIL.get());
		for(AnvilRecipe rec : recipes) {
			if(rec != null) {
				for(Ingredient inging : rec.getIngredients()) {
					ItemStack[] ingredient = inging.getItems();
	    			for (ItemStack stack : ingredient) {
	    				if(stackIn == stack)
	    				{
	    					return stackIn;
	    				}
    				}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	/*
	 * 
	 * CHECKER
	 * 
	 */
	public boolean playerHoldingRecipe(int range)
	{
        AABB areaToCheck = new AABB(worldPosition.offset(-range, 0, -range), worldPosition.offset(range, range, range));
        List<Player> player = level.getEntitiesOfClass(Player.class, areaToCheck);
        
        if(player != null)
        {
        	for(Player p : player)
        	{
    	        for(InteractionHand hands : InteractionHand.values())
    	        {
    	        	String handStackName = ForgeRegistries.ITEMS.getKey(p.getItemInHand(hands).getItem()).getPath();
    	        	if(!handStackName.isEmpty())
    	        	{
        	    		List<AnvilRecipe> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.ANVIL.get());
        	    		for(AnvilRecipe rec : recipes)
        	    		{
        	    			if(rec != null)
        	    			{
        	    				for(Ingredient inging : rec.getIngredients())
        	    				{
        	    					ItemStack[] ingredient = inging.getItems();
        	    	    			for (ItemStack stack : ingredient)
        	        				{
        	    	    				String recipeStackName = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
        	    	    				System.out.println(recipeStackName == handStackName);
//        	    	    				System.out.println(rec.matches(new RecipeWrapper(getInventory()), level));
        	    		    			setLitProperty(recipeStackName == handStackName);
        	        				}
        	    				}
        	    			}
        	    		}
    	        	}
    	        }
        	}
        }
        return false;
	}
	
    public int getRecipeCountVanilla(SingleSlotAccess slotaccess)
    {
    	return isSameVanillaRecipe(slotaccess).getResultItem(level.registryAccess()).getCount();
    }
    
    @Nullable
    public CraftingRecipe isSameVanillaRecipe(SingleSlotAccess slotaccess)
    {
        List<CraftingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        for(CraftingRecipe rec : recipes)
        {
    		for (Ingredient ingredient : rec.getIngredients())
    		{
    			for (ItemStack stack : ingredient.getItems())
    			{
    				if (stack.getItem().toString() == slotaccess.getItemStack(getInventory()).getItem().toString())
    				{
    					return rec;
    				}
    			}
    		}
        }
        return null;
    }
    
    public void dropSpecialStack(int howOften, ItemStack stack)
    {
		for(int i = 0; i < howOften; i++)
		{
//			ItemStack outStack = new ItemStack(output.getItem(), output.getCount());
	        ItemEntity entityItem = new ItemEntity(level, 0, 0, 0, stack);
	        double variance = 0.05F * 4;
	        entityItem.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5);
	        entityItem.setDeltaMovement(Mth.nextDouble(level.random, -variance, variance), 2 / 20F, Mth.nextDouble(level.random, -variance, variance));
	        entityItem.setUnlimitedLifetime();
			entityItem.setPickUpDelay(20);
	        level.addFreshEntity(entityItem);
		}
    }
}
