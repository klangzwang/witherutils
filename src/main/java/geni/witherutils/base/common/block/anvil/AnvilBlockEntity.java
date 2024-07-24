package geni.witherutils.base.common.block.anvil;

import java.util.List;

import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.LogicalBlockEntities;
import geni.witherutils.base.common.block.anvil.AnvilRecipe.RecipeIn;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.core.common.network.NetworkDataSlot;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AnvilBlockEntity extends WitherMachineBlockEntity {

    @Nullable
    private RecipeHolder<AnvilRecipe> currentRecipe;
	
    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    
    private float hotCounter;
    private int hitCounter;

	public AnvilBlockEntity(BlockPos pos, BlockState state)
	{
		super(LogicalBlockEntities.ANVIL.get(), pos, state);
        addDataSlot(NetworkDataSlot.ITEM_STACK.create(() -> INPUT.getItemStack(this), f -> INPUT.setStackInSlot(this, f)));
        addDataSlot(NetworkDataSlot.INT.create(this::getHitCounter, p -> hitCounter = p));
        addDataSlot(NetworkDataSlot.FLOAT.create(this::getHotCounter, p -> hotCounter = p));
	}
	
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder().inputSlot().slotAccess(INPUT).setStackLimit(1).build();
    }
	
	@Override
	protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
	{
		return new MachineInventory(layout)
		{
            protected void onContentsChanged(int slot)
            {
                super.onContentsChanged(slot);
                if (level == null)
                    return;
                if (slot == 0)
                {
                	findMatchingRecipe();
                }
                setChanged();
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
        
//        System.out.println("SERVER: ItemStack is " + INPUT.getItemStack(getInventory()));
//        System.out.println("SERVER: Recipe is " + currentRecipe);
        
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
        setLitProperty(!INPUT.getItemStack(getInventory()).isEmpty());
        
//        System.out.println("CLIENT: ItemStack is " + INPUT.getItemStack(getInventory()));
//        System.out.println("CLIENT: Recipe is " + currentRecipe);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
	{
        for(InteractionHand hands : InteractionHand.values())
        {
			ItemStack heldStack = pPlayer.getItemInHand(hands);
			if (heldStack.getItem() instanceof HammerItem ||
				heldStack.getItem() == WUTItems.HAMMER.get())
			{
				if(hotCounter < 0.6)
					this.hotCounter += 0.06f;
				this.hotCounter += 0.04f;
			}
        }
		
        if(currentRecipe == null || INPUT.getItemStack(getInventory()).isEmpty())
        	return ItemInteractionResult.SUCCESS;
		
        if(this.hitCounter == this.currentRecipe.value().hitcounter())
        {
        	if(pPlayer instanceof ServerPlayer)
        	{
        		ServerPlayer splayer = (ServerPlayer) pPlayer;
            	popRecipeExperience(splayer, this.currentRecipe.value().experience());
        	}
        	tryProcessRecipe();
        }
        else
        {
            for(InteractionHand hands : InteractionHand.values())
            {
				ItemStack heldStack = pPlayer.getItemInHand(hands);
				if (heldStack.getItem() instanceof HammerItem || heldStack.getItem() == WUTItems.HAMMER.get())
				{
					hitCounter += 1;

					if (ItemsConfig.ANVILCOOLDOWN.get())
						pPlayer.getCooldowns().addCooldown(heldStack.getItem(), 2 + level.random.nextInt(4));

					if (!pPlayer.isCreative() && !pPlayer.isSpectator())
					{
						ItemStackUtil.damageItem(pPlayer, heldStack);
						double foodexhaustion = ItemsConfig.ANVILFOODEXHAUSTION.get() + this.currentRecipe.value().satcost();
						pPlayer.getFoodData().addExhaustion((float) foodexhaustion);
					}
				}
				return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
	
    private void findMatchingRecipe()
    {
    	ItemStack stackIn = INPUT.getItemStack(getInventory());
    	if(stackIn.isEmpty())
    	{
    		currentRecipe = null;
    	}
    	else
    	{
    		List<RecipeHolder<AnvilRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.ANVIL.type().get());
    		for(RecipeHolder<AnvilRecipe> rec : recipes)
    		{
    			if(rec != null)
    			{
    				ItemStack[] ingredient = rec.value().input().getItems();
        			for (ItemStack stack : ingredient)
        			{
        				if(ItemStack.isSameItem(stack, stackIn))
        				{
        					currentRecipe = rec;
        				}
    				}
    			}
    		}
    	}
    }
    
	private boolean tryProcessRecipe()
    {
        if(this.currentRecipe.value().matches(new RecipeIn(INPUT.getItemStack(getInventory())), null))
        {
    		SoundUtil.playServerSound(level, worldPosition, WUTSounds.HAMMERHIT.get(), 0.6F, 0.8F + level.random.nextFloat() - 0.6F);
    		SoundUtil.playServerSound(level, worldPosition, SoundEvents.ANVIL_PLACE, 0.6F, 0.8F + level.random.nextFloat() - 0.6F);
    		
    		dropRecipeOutput(currentRecipe.value().getResultItem(level.registryAccess()));
    		INPUT.setStackInSlot(this, ItemStack.EMPTY);
    		hitCounter = 0;
    		
    		return true;
        }
		return false;
    }
	
    public void dropRecipeOutput(ItemStack stack)
    {
        ItemEntity entityItem = new ItemEntity(level, 0, 0, 0, stack);
        double variance = 0.05F * 4;
        entityItem.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5);
        entityItem.setDeltaMovement(Mth.nextDouble(level.random, -variance, variance), 2 / 20F, Mth.nextDouble(level.random, -variance, variance));
        entityItem.setUnlimitedLifetime();
		entityItem.setPickUpDelay(20);
        level.addFreshEntity(entityItem);
    }
	
    @Override
    public void popRecipeExperience(ServerPlayer serverplayer, float count)
    {
        int i = Mth.floor((float)count * 1);
        float f = Mth.frac((float)count * 1);
        if (f != 0.0F && Math.random() < (double) f)
        {
           ++i;
        }
    	ExperienceOrb.award(serverplayer.serverLevel(), serverplayer.position(), i);
    }
    
	@Override
	public void loadAdditional(CompoundTag tag, Provider lookupProvider)
	{
		super.loadAdditional(tag, lookupProvider);
		this.getInventory().deserializeNBT(lookupProvider, tag.getCompound("Items"));
		this.hitCounter = tag.getInt("hitcounter");
	}
	@Override
	public void saveAdditional(CompoundTag tag, Provider lookupProvider)
	{
		super.saveAdditional(tag, lookupProvider);
        tag.put("Items", getInventory().serializeNBT(lookupProvider));
		tag.putInt("hitcounter", hitCounter);
	}

    public int getHitCounter()
    {
        return hitCounter;
    }
    public float getHotCounter()
    {
        return hotCounter;
    }
	public void setHotCounter(float f)
	{
		hotCounter = f;
	}
}
