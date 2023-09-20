package geni.witherutils.base.common.block.cauldron;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.base.common.base.IInteractBlockEntity;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.io.fluid.MachineFluidHandler;
import geni.witherutils.base.common.io.fluid.MachineFluidTank;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.recipes.CauldronRecipe;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.FluidStackDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.ItemStackDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class CauldronBlockEntity extends WitherMachineBlockEntity implements IInteractBlockEntity {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();

    private CauldronRecipe recipe = null;
    private final CauldronRecipe.Container container;
    
    private int timer;
    
    private boolean containRecipe;
    private int experience;
    
	public static final int CAPACITY = 1 * FluidType.BUCKET_VOLUME;
	public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
	
    private final MachineFluidTank fluidTank;
    private final MachineFluidHandler fluidHandler;

	public CauldronBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.CAULDRON.get(), pos, state);
        this.fluidTank = createFluidTank(CAPACITY);
        this.fluidHandler = new MachineFluidHandler(getIOConfig(), fluidTank);
        addCapabilityProvider(fluidHandler);
        addDataSlot(new FluidStackDataSlot(getFluidTank()::getFluid, getFluidTank()::setFluid, SyncMode.WORLD));
        add2WayDataSlot(new FluidStackDataSlot(fluidTank::getFluid, fluidTank::setFluid, SyncMode.GUI));
        addDataSlot(new IntegerDataSlot(this::getTimer, p -> timer = p, SyncMode.WORLD));
        addDataSlot(new BooleanDataSlot(this::getContainRecipe, p -> containRecipe = p, SyncMode.WORLD));
        addDataSlot(new ItemStackDataSlot(() -> INPUT.getItemStack(inventory), p -> INPUT.setStackInSlot(inventory, p), SyncMode.WORLD));
        
        container = new CauldronRecipe.Container(getInventory(), getFluidTank());
	}

    @Override
    public void serverTick()
	{
        super.serverTick();

        this.findMatchingRecipe();

        if(recipe == null || !fluidTank.isEmpty())
        {
        	this.recipe = null;
        	this.containRecipe = false;
            this.timer = 0;
            return;
        }

        if (recipe == null || !recipe.matches(container, level))
        {
            this.findMatchingRecipe();
            
            if (recipe == null)
            {
            	this.recipe = null;
            	this.containRecipe = false;
                this.timer = 0;
                return;
            }
        }

        setLitProperty(timer > 0);
        
        if (timer > 0)
        {
            timer--;
            return;
        }
        else
        {
            this.tryProcessRecipe();
        }
	}
    
	@Override
    public void clientTick()
    {
        super.clientTick();
        setLitProperty(timer > 0);
    }

	@Override
	public InteractionResult onBlockUse(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(level.isClientSide)
			return InteractionResult.SUCCESS;
		
		ItemStack heldStack = player.getItemInHand(hand);
		if (heldStack.getItem() instanceof BucketItem || heldStack.getItem() == Items.BUCKET)
		{
			if(experience >= 0)
			{
	            if(level instanceof ServerLevel serverlevel)
	            {
	            	ExperienceOrb.award(serverlevel, worldPosition.getCenter(), experience);
	            	setExperience(0);
	            }
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.SUCCESS;
	}
    
    public FluidTank getFluidTank()
    {
        return fluidTank;
    }
	public FluidStack getTankFluid()
	{
		return fluidTank.getFluid();
	}
	public void setFluid(FluidStack fluid)
	{
		fluidTank.setFluid(fluid);
	}

	@Override
	public void load(CompoundTag tag)
	{
		this.experience = tag.getInt("experience");
		this.containRecipe = tag.getBoolean("containrecipe");
		this.fluidTank.readFromNBT(tag.getCompound("fluid"));
		this.timer = tag.getInt("timer");
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
        tag.putInt("experience", this.experience);
        tag.putBoolean("containrecipe", this.containRecipe);
        tag.put("fluid", this.fluidTank.writeToNBT(new CompoundTag()));
		tag.putInt("timer", this.timer);
		super.saveAdditional(tag);
	}

    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder().inputSlot().slotAccess(INPUT).setStackLimit(1).build();
    }
    
    /*
     * 
     * RECIPE
     * 
     */
	public List<CauldronRecipe> getAllRecipesCauldron()
	{
		return level.getRecipeManager().getAllRecipesFor(WUTRecipes.CAULDRON.get());
	}
    public CauldronRecipe getRecipe()
    {
        return recipe;
    }
    public int getTimer()
    {
        return timer;
    }
	public boolean getContainRecipe()
	{
		return containRecipe;
	}
	public int getExperience()
	{
		return experience;
	}
	public void setExperience(int experience)
	{
		this.experience = experience;
	}
	
	@Override
	protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
	{
		return new MachineInventory(getIOConfig(), layout)
		{
			@Override
			public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
			{
				if(!fluidTank.isEmpty())
					return stack;
				return super.insertItem(slot, stack, simulate);
			}
			@Override
			public int getSlotLimit(int slot)
			{
				return 1;
			}
	        @Override
	        protected void onContentsChanged(int slot)
	        {
	        	onInventoryContentsChanged(slot);
	            super.onContentsChanged(slot);
	        }
		};
	}
    @Override
    protected void onInventoryContentsChanged(int slot)
    {
        super.onInventoryContentsChanged(slot);
    }
    private MachineFluidTank createFluidTank(int capacity)
    {
        return new MachineFluidTank(capacity, this)
        {
            @Override
            protected void onContentsChanged()
            {
            	onTankContentsChanged();
                super.onContentsChanged();
            }
        };
    }
    private void onTankContentsChanged()
    {
    }

    private void findMatchingRecipe()
    {
        if(recipe != null && recipe.matches(container, level))
            return;
        
        recipe = null;
        
        List<CauldronRecipe> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.CAULDRON.get());
        for(CauldronRecipe rec : recipes)
        {
            if(rec.matches(container, level))
            {
                recipe = rec;
                timer = rec.getTimer();
                experience = rec.getExperience();
            	SoundUtil.playServerSound(level, worldPosition, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, 1.0f, 1.0f);
                break;
            }
        }
    }

	private boolean tryProcessRecipe()
	{
		int test = fluidTank.fill(this.recipe.getResultFluid(level.registryAccess()), FluidAction.SIMULATE);
		if (test == this.recipe.getResultFluid(level.registryAccess()).getAmount() && recipe.matches(container, level))
		{
			inventory.getStackInSlot(0).shrink(1);
			fluidTank.fill(this.recipe.getResultFluid(level.registryAccess()), FluidAction.EXECUTE);
			SoundUtil.playServerSound(level, worldPosition, SoundEvents.WITHER_AMBIENT, 0.75f, 1.0f);
			return true;
		}
		return false;
	}
}
