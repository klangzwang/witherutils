package geni.witherutils.base.common.block.furnace.alloy;

import java.util.List;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.QuadraticScalable;
import geni.witherutils.api.soulbank.SoulBankModifier;
import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.MultiSlotAccess;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.recipes.AlloyFurnaceRecipe;
import geni.witherutils.core.common.recipes.OutputStack;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AlloyFurnaceBlockEntity extends WitherMachineEnergyBlockEntity implements MenuProvider {

    public static final MultiSlotAccess INPUTS = new MultiSlotAccess();
    public static final SingleSlotAccess OUTPUT = new SingleSlotAccess();
    
    public static final QuadraticScalable CAPACITY = new QuadraticScalable(SoulBankModifier.ENERGY_CAPACITY, () -> BlocksConfig.ALLOYMAXENERGY.get());
    public static final QuadraticScalable TRANSFER = new QuadraticScalable(SoulBankModifier.ENERGY_TRANSFER, () -> BlocksConfig.ALLOYSENDPERTICK.get());
    public static final QuadraticScalable USAGE = new QuadraticScalable(SoulBankModifier.ENERGY_USE, () -> BlocksConfig.ALLOYUSEENERGY.get());

    private AlloyFurnaceRecipe recipeAlloy = null;
    private final AlloyFurnaceRecipe.Container container;
    
    private int timer;
    private int burnTimeMax = 0;

    public AlloyFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(EnergyIOMode.Input, CAPACITY, TRANSFER, USAGE, WUTEntities.ALLOY_FURNACE.get(), worldPosition, blockState);
        
        this.container = new AlloyFurnaceRecipe.Container(getInventory());
        
        addDataSlot(new IntegerDataSlot(this::getBurnTimeMax, p -> burnTimeMax = p, SyncMode.GUI));
        addDataSlot(new IntegerDataSlot(this::getTimer, p -> timer = p, SyncMode.GUI));
    }
    
    @Override
    public void serverTick()
    {
        super.serverTick();
        alloytick();
    }
    
    @Override
    public void clientTick()
    {
        super.clientTick();
        setLitProperty(burnTimeMax > 0 && timer > 0);
    }
    
    public int getBurnTimeMax()
    {
        return burnTimeMax;
    }
    public int getTimer()
    {
        return timer;
    }
    
    public void alloytick()
    {
        this.findMatchingAlloyRecipe();
        if(recipeAlloy == null || getInventory().getStackInSlot(3).getCount() >= 64)
        {
            this.timer = 0;
            return;
        }

        final int cost = this.recipeAlloy.getEnergyCost();
        
        if (energyStorage.getEnergyStored() < cost && cost > 0)
        {
            this.timer = 0;
            return;
        }
        if (recipeAlloy == null || !recipeAlloy.matches(container, level))
        {
            this.findMatchingAlloyRecipe();
            if (recipeAlloy == null)
            {
                this.timer = 0;
                return;
            }
        }
        
        if (timer < burnTimeMax)
        {
            timer++;
            return;
        }
        else
        {
            this.tryProcessAlloyRecipe();
            energyStorage.takeEnergy(cost);
            timer = 0;
        }
    }

    private void findMatchingAlloyRecipe()
    {
        if(recipeAlloy != null && recipeAlloy.matches(container, level))
            return;
        
        recipeAlloy = null;
        this.burnTimeMax = 0;
        this.timer = 0;
        
        List<AlloyFurnaceRecipe> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.ALLOY.get());
        for(AlloyFurnaceRecipe rec : recipes)
        {
            if(rec.matches(container, level))
            {
                level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), WUTSounds.REACT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                recipeAlloy = rec;
                this.burnTimeMax = this.recipeAlloy.getEnergyIngredient().getTicks();
                this.timer = 0;
                break;
            }
        }
    }
    
    private boolean tryProcessAlloyRecipe()
    {
        if(recipeAlloy.matches(container, level))
        {
            if(!getInventory().insertItem(3, recipeAlloy.getResultItem(level.registryAccess()), true).isEmpty())
                return false;
            getInventory().getStackInSlot(0).shrink(1);
            getInventory().getStackInSlot(1).shrink(1);
            getInventory().getStackInSlot(2).shrink(1);
            List<OutputStack> output = recipeAlloy.getResultStacks(level.registryAccess());
            getInventory().insertItem(3, output.get(0).getItem(), false);
            return true;
        }
        return false;
    }

    public ItemStack getStackInputSlot(int slot)
    {
        return getInventory().getStackInSlot(slot);
    }
    protected AlloyFurnaceRecipe.Container getContainer()
    {
        return container;
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new AlloyFurnaceContainer(this, playerInventory, i);
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
            .inputSlot(3).slotAccess(INPUTS)
            .outputSlot().slotAccess(OUTPUT)
            .soulbank().build();
    }
    
    public String getStatusText()
    {
        return recipeAlloy == null ? "" : recipeAlloy.getResultItem(level.registryAccess()).toString();
    }

    @Override
    public void saveAdditional(CompoundTag pTag)
    {
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag)
    {
        super.load(pTag);
    }

    @Override
    public void popExperience(ServerPlayer serverplayer, int count)
    {
        int i = Mth.floor((float)count * recipeAlloy.getExperience() / 5);
        float f = Mth.frac((float)count * recipeAlloy.getExperience() / 5);
        if (f != 0.0F && Math.random() < (double) f)
        {
           ++i;
        }
    	ExperienceOrb.award(serverplayer.serverLevel(), serverplayer.position(), i);
    }
}
