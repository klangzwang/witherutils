package geni.witherutils.base.common.block.cauldron;

import java.util.List;

import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.cauldron.CauldronRecipe.RecipeIn;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.io.fluid.FluidUtils;
import geni.witherutils.base.common.io.fluid.WitherFluidTank;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.network.NetworkDataSlot;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class CauldronBlockEntity extends WitherMachineBlockEntity {
    
    @Nullable
    private RecipeHolder<CauldronRecipe> currentRecipe;
	
    private int timer;
    @SuppressWarnings("unused")
	private int experience;
    
    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
	private final WitherFluidTank TANK = new WitherFluidTank(FluidType.BUCKET_VOLUME);

	public CauldronBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTBlockEntityTypes.CAULDRON.get(), pos, state);
		addDataSlot(NetworkDataSlot.INT.create(() -> getTimer(), p -> timer = p));
        addDataSlot(NetworkDataSlot.ITEM_STACK.create(() -> getInventory().getStackInSlot(0), f -> getInventory().setStackInSlot(0, f)));
        addDataSlot(NetworkDataSlot.FLUID_STACK.create(() -> getFluidHandler(null).getFluidInTank(0), f -> TANK.setFluid(f)));
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
//                if (level == null)
//                    return;
//                if (slot == 0)
//                {
//                	findMatchingRecipe();
//                }
//                setChanged();
            }

	        @Override
	        public int getSlotLimit(int slot)
	        {
	            return 1;
	        }
		};
	}
	
	@Override
	public boolean hasFluidCapability()
	{
		return true;
	}
	
	@Override
	public IFluidHandler getFluidHandler(@Nullable Direction dir)
	{
		return TANK;
	}
	
	@Override
	public void serverTick()
	{
		super.serverTick();

        this.findMatchingRecipe();

        if(currentRecipe == null || !getFluidHandler(null).getFluidInTank(0).isEmpty())
        {
        	this.currentRecipe = null;
            this.timer = 0;
            return;
        }

        if (currentRecipe == null || !currentRecipe.value().matches(new RecipeIn(INPUT.getItemStack(getInventory())), null))
        {
            this.findMatchingRecipe();
            
            if (currentRecipe == null)
            {
            	this.currentRecipe = null;
                this.timer = 0;
                return;
            }
        }
		
        if (timer > 0)
        {
            timer--;
            return;
        }
        else
        {
            this.tryProcessRecipe();
        }
//        System.out.println("SERVER: " + getFluidHandler(null).getFluidInTank(0));
//        System.out.println("SERVER: " + getInventory().getStackInSlot(0));
	}
	
	@Override
	public void clientTick()
	{
		super.clientTick();
        setLitProperty(timer > 0);
        
//        System.out.println("CLIENT: " + getFluidHandler(null).getFluidInTank(0));
//        System.out.println("CLIENT: " + getInventory().getStackInSlot(0));
	}

    @Override
    public ItemInteractionResult onBlockEntityUsed(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
//    	if(player.isCrouching())
//    	{
//    		if(getFluidHandler(null).getFluidInTank(0).isEmpty())
//    			TANK.setFluid(new FluidStack(WUTFluids.BLUELIMBO.get(), 1000));
//    		else
//    			TANK.setFluid(FluidStack.EMPTY);
//    	}
    	
//    	if(player.isCrouching())
//    	{
//    		if(getInventory().getStackInSlot(0).isEmpty())
//    			getInventory().setStackInSlot(0, new ItemStack(Items.OAK_LOG));
//    		else
//    			getInventory().setStackInSlot(0, ItemStack.EMPTY);
//    	}
    	
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty())
        {
            if (player instanceof ServerPlayer)
            {
                if (FluidUtils.tryFluidInsertion(this, null, player, hand))
                {
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                    return ItemInteractionResult.sidedSuccess(false);
                }
                else if (FluidUtils.tryFluidExtraction(this, null, player, hand))
                {
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                    return ItemInteractionResult.sidedSuccess(false);
                }
            }
//            player.getInventory().setChanged();
//            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.onBlockEntityUsed(state, level, pos, player, hand, hit);
    }
    
    public int getTimer()
    {
        return timer;
    }
	
    private void findMatchingRecipe()
    {
        if(currentRecipe != null && currentRecipe.value().matches(new RecipeIn(INPUT.getItemStack(getInventory())), null))
            return;
        
        currentRecipe = null;
        
		List<RecipeHolder<CauldronRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.CAULDRON.type().get());
		for(RecipeHolder<CauldronRecipe> rec : recipes)
		{
            if(rec.value().matches(new RecipeIn(INPUT.getItemStack(getInventory())), null))
            {
            	currentRecipe = rec;
                timer = rec.value().timer();
                experience = rec.value().experience();
            	SoundUtil.playServerSound(level, worldPosition, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, 1.0f, 1.0f);
            	break;
            }
		}
    }
    
	private void tryProcessRecipe()
	{
		getInventory().setStackInSlot(0, ItemStack.EMPTY);
		TANK.setFluid(new FluidStack(currentRecipe.value().output().getFluid(), 1000));
		SoundUtil.playServerSound(level, worldPosition, SoundEvents.WITHER_AMBIENT, 0.75f, 1.0f);
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
/*
    @Nullable
    private RecipeHolder<CauldronRecipe> currentRecipe;
	
    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
	
    private final MachineFluidHandler fluidHandler;
    private static final TankAccess TANK = new TankAccess();
    private static final int CAPACITY = FluidType.BUCKET_VOLUME;
    private Fluid type = Fluids.EMPTY;
    private int experience;
    private int timer;
    
	public CauldronBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTBlockEntityTypes.CAULDRON.get(), pos, state);
        addDataSlot(NetworkDataSlot.INT.create(() -> getTimer(), p -> timer = p));
        addDataSlot(NetworkDataSlot.ITEM_STACK.create(() -> getInventory().getStackInSlot(0), f -> getInventory().setStackInSlot(0, f)));
        addDataSlot(NetworkDataSlot.FLUID_STACK.create(() -> TANK.getFluid(this), f -> TANK.setFluid(this, f)));
		
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

        System.out.println("SERVER: type is " + type);
        
//        if (currentRecipe != null)
//        {
//            getFluidTank().setFluid(new FluidStack(type, 1000));
//            System.out.println("SERVER: Amount is " + getFluidTank().getFluidAmount());
//        }

        
        
        
//        if (currentRecipe == null)
//        	return;
//        else
//        {
//    		if (timer > 0)
//    		{
//    			timer = 0;
////    			timer--;
//    			return;
//    		}
//    		else
//    		{
//    			getFluidTank().setFluid(new FluidStack(Fluids.WATER, 1000));
//    			TANK.setFluid(this, new FluidStack(Fluids.WATER, 1000));
//
//    			System.out.println("SERVER: Amount is " + getFluidTank().getFluidAmount());
//    			System.out.println("SERVER: Amount is " + TANK.getFluid(this).getAmount());
////    			System.out.println("SERVER: RecipeFluid is " + currentRecipe.value().output());
//
////    			if(TANK.getFluid(this).getAmount() == 0)
////    			{
////    		        getFluidTank().setFluid(currentRecipe.value().output());
////    				SoundUtil.playServerSound(level, worldPosition, SoundEvents.WITHER_AMBIENT, 0.75f, 1.0f);
////    				setChanged();
////    			}
//    		}
//        }
	}
    
	@Override
	public void clientTick()
	{
		super.clientTick();
		
//        System.out.println("CLIENT: Item is " + getInventory().getStackInSlot(0));
		
        setLitProperty(timer > 0);
	}
	
    @Override
    public ItemInteractionResult onBlockEntityUsed(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && handleFluidItemInteraction(player, hand, stack, this, TANK))
        {
			if(experience >= 0)
			{
	            if(level instanceof ServerLevel serverlevel)
	            {
	            	ExperienceOrb.award(serverlevel, worldPosition.getCenter(), experience);
	            	experience = 0;
    				getInventory().extractItem(0, 64, false);
    				setChanged();
	            }
			}
            player.getInventory().setChanged();
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.onBlockEntityUsed(state, level, pos, player, hand, hit);
    }
	
    private void findMatchingRecipe()
    {
    	ItemStack stackIn = INPUT.getItemStack(getInventory());
    	if(stackIn.isEmpty())
    	{
    		currentRecipe = null;
    		type = null;
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
                        		type = rec.value().output().getFluid();
                            }
                            currentRecipe = rec;
                            break;
                        }
    				}
    			}
    		}
    	}
        System.out.println("SERVER: Recipe is " + currentRecipe);
        System.out.println("SERVER: Timer is " + timer);
        System.out.println("SERVER: Experience is " + experience);
        System.out.println("SERVER: Fluid is " + type);
    }

    @Override
    public boolean hasFluidCapability()
    {
    	return true;
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
    */
}
