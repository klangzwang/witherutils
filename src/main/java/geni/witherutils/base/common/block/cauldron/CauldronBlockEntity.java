package geni.witherutils.base.common.block.cauldron;

import java.util.List;

import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.anvil.AnvilRecipe.RecipeIn;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.io.fluid.FluidTankUser;
import geni.witherutils.base.common.io.fluid.MachineFluidHandler;
import geni.witherutils.base.common.io.fluid.MachineFluidTank;
import geni.witherutils.base.common.io.fluid.MachineTankLayout;
import geni.witherutils.base.common.io.fluid.TankAccess;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.network.NetworkDataSlot;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class CauldronBlockEntity extends WitherMachineBlockEntity implements FluidTankUser {
	
    @Nullable
    private RecipeHolder<CauldronRecipe> currentRecipe;
	
    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
	
    private final MachineFluidHandler fluidHandler;
    private static final TankAccess TANK = new TankAccess();
    private static final int CAPACITY = 3 * FluidType.BUCKET_VOLUME;
    private Fluid type = Fluids.EMPTY;
    private int experience;
    private int timer;
    
	public CauldronBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTBlockEntityTypes.CAULDRON.get(), pos, state);
        addDataSlot(NetworkDataSlot.INT.create(() -> getTimer(), p -> timer = p));
		
		fluidHandler = createMachineFluidHandler();
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

        if (currentRecipe == null)
        	return;
        else
        {
            System.out.println("SERVER: Recipe is " + currentRecipe);
            System.out.println("SERVER: Timer is " + timer);
            System.out.println("SERVER: Experience is " + experience);
        	
    		if (timer > 0)
    		{
    			timer--;
    			return;
    		}
    		else
    			fillRecipeOutput(currentRecipe.value().output());
        }
	}
    
	@Override
	public void clientTick()
	{
		super.clientTick();
        setLitProperty(timer > 0);
	}
	
//	@Override
//	public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
//	{
//		if(!level.isClientSide)
//		{
//			ItemStack heldStack = pPlayer.getItemInHand(pHand);
//			if (heldStack.getItem() instanceof BucketItem || heldStack.getItem() == Items.BUCKET)
//			{
//				if(experience >= 0)
//				{
//		            if(level instanceof ServerLevel serverlevel)
//		            {
//		            	ExperienceOrb.award(serverlevel, worldPosition.getCenter(), experience);
//		            	experience = 0;
//		            }
//		            return ItemInteractionResult.CONSUME;
//				}
//			}
//		}
//        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
//	}
//	
//    @Override
//    public ItemInteractionResult onBlockEntityUsed(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
//    {
//        ItemStack stack = player.getItemInHand(hand);
//        if (!stack.isEmpty() && handleFluidItemInteraction(player, hand, stack, this, TANK)) {
//            player.getInventory().setChanged();
//            return ItemInteractionResult.sidedSuccess(level.isClientSide());
//        }
//        return super.onBlockEntityUsed(state, level, pos, player, hand, hit);
//    }

    private void findMatchingRecipe()
    {
    	ItemStack stackIn = INPUT.getItemStack(getInventory());
    	if(stackIn.isEmpty())
    	{
    		currentRecipe = null;
    		experience = 0;
    		timer = 0;
    	}
    	else
    	{
    		List<RecipeHolder<CauldronRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.CAULDRON.type().get());
    		for(RecipeHolder<CauldronRecipe> rec : recipes)
    		{
    			if(rec != null)
    			{
    				ItemStack[] ingredient = rec.value().input().getItems();
        			for (ItemStack stack : ingredient)
        			{
                        if (ItemStack.isSameItem(stack, stackIn))
                        {
                            if(rec.value().matches(new RecipeIn(INPUT.getItemStack(getInventory())), null))
                            {
                        		experience = rec.value().experience();
                        		timer = rec.value().timer();
                            }
                            SoundUtil.playServerSound(level, worldPosition, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, 1.0f, 1.0f);
                            currentRecipe = rec;
                            break;
                        }
    				}
    			}
    		}
    	}
    }

    public void fillRecipeOutput(FluidStack fluidStack)
    {
		int test = TANK.fill(fluidHandler, fluidStack, FluidAction.SIMULATE);
		if (test == fluidStack.getAmount())
		{
			getInventory().getStackInSlot(0).shrink(1);
			TANK.fill(fluidHandler, fluidStack, FluidAction.EXECUTE);
			SoundUtil.playServerSound(level, worldPosition, SoundEvents.WITHER_AMBIENT, 0.75f, 1.0f);
			currentRecipe = null;
			experience = 0;
			timer = 0;
		}
    }

    @Override
    public @Nullable MachineTankLayout getTankLayout()
    {
        return MachineTankLayout.builder().tank(TANK, CAPACITY, f -> type.isSame(f.getFluid())).build();
    }

    public MachineFluidTank getFluidTank()
    {
        return TANK.getTank(this);
    }

    @Override
    public MachineFluidHandler getMachineFluidHandler()
    {
        return fluidHandler;
    }

    @Override
    public MachineFluidHandler createMachineFluidHandler()
    {
        return new MachineFluidHandler(getTankLayout())
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                super.onContentsChanged(slot);
            }
        };
    }
	
    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.saveAdditional(pTag, lookupProvider);
        pTag.putInt("Timer", timer);
        pTag.putInt("Experience", experience);
        saveTank(lookupProvider, pTag);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.loadAdditional(pTag, lookupProvider);
        timer = pTag.getInt("Timer");
        experience = pTag.getInt("Experience");
        loadTank(lookupProvider, pTag);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput components)
    {
        super.applyImplicitComponents(components);

        SimpleFluidContent storedFluid = components.get(WUTComponents.ITEM_FLUID_CONTENT);
        
        if (storedFluid != null)
        {
            var tank = TANK.getTank(this);
            tank.setFluid(storedFluid.copy());
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);

        var tank = TANK.getTank(this);
        if (!tank.isEmpty())
        {
            components.set(WUTComponents.ITEM_FLUID_CONTENT, SimpleFluidContent.copyOf(tank.getFluid()));
        }
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag)
    {
        super.removeComponentsFromTag(tag);
    }
	
    public int getTimer()
    {
        return timer;
    }
}
