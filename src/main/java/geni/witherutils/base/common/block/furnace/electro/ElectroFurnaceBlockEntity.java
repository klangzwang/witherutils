package geni.witherutils.base.common.block.furnace.electro;

import java.util.List;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.QuadraticScalable;
import geni.witherutils.api.soulbank.SoulBankModifier;
import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public class ElectroFurnaceBlockEntity extends WitherMachineEnergyBlockEntity implements MenuProvider {
  
    public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    public static final SingleSlotAccess OUTPUT = new SingleSlotAccess();
    
    public static final QuadraticScalable CAPACITY = new QuadraticScalable(SoulBankModifier.ENERGY_CAPACITY, () -> BlocksConfig.FURNACEMAXENERGY.get());
    public static final QuadraticScalable TRANSFER = new QuadraticScalable(SoulBankModifier.ENERGY_TRANSFER, () -> BlocksConfig.FURNACESENDPERTICK.get());
    public static final QuadraticScalable USAGE = new QuadraticScalable(SoulBankModifier.ENERGY_USE, () -> BlocksConfig.FURNACEUSEENERGY.get());

    private SmeltingRecipe recipeFurnace;
    
    private int timer;
    private int burnTimeMax = 0;
    
    public ElectroFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(EnergyIOMode.Input, CAPACITY, TRANSFER, USAGE, WUTEntities.ELECTRO_FURNACE.get(), worldPosition, blockState);
        addDataSlot(new IntegerDataSlot(this::getBurnTimeMax, p -> burnTimeMax = p, SyncMode.GUI));
        addDataSlot(new IntegerDataSlot(this::getTimer, p -> timer = p, SyncMode.GUI));
    }

    @Override
    public void serverTick()
    {
        super.serverTick();
        furnacetick();
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
    
    public void furnacetick()
    {
        this.findMatchingFurnaceRecipe();
        if(recipeFurnace == null || getInventory().getStackInSlot(1).getCount() >= 64)
        {
            this.timer = 0;
            return;
        }

        final int cost = ForgeHooks.getBurnTime(new ItemStack(Items.COAL, 1), RecipeType.SMELTING) * 10 / 9;
        if (energyStorage.getEnergyStored() < cost && cost > 0)
        {
            this.timer = 0;
            return;
        }
        if (recipeFurnace == null || !recipeFurnace.matches(this, level))
        {
            this.findMatchingFurnaceRecipe();
            if (recipeFurnace == null)
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
            this.tryProcessFurnaceRecipe();
            energyStorage.takeEnergy(cost);
            timer = 0;
        }
    }

    private void findMatchingFurnaceRecipe()
    {
        ItemStack stack = getInventory().getStackInSlot(0);

        if(stack.isEmpty())
        {
            recipeFurnace = null;
            this.burnTimeMax = 0;
            this.timer = 0;
            return;
        }
        else if(recipeFurnace != null)
        {
            return;
        }

        Container inv = new SimpleContainer(1);
        inv.setItem(0, stack);

        List<SmeltingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);
        for(SmeltingRecipe rec : recipes)
        {
            if(rec.matches(inv, level))
            {
                level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), WUTSounds.REACT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                recipeFurnace = rec;
                this.burnTimeMax = this.recipeFurnace.getCookingTime();
                this.timer = 0;
                break;
            }
        }
    }

    private boolean tryProcessFurnaceRecipe()
    {
        ItemStack stack = getInventory().getStackInSlot(0);
        if(stack.isEmpty())
        {
            return false;
        }

        Container inv = new SimpleContainer(1);
        inv.setItem(0, stack);

        if(recipeFurnace.matches(inv, level))
        {
            if(!getInventory().insertItem(1, recipeFurnace.getResultItem(level.registryAccess()), true).isEmpty())
            {
                return false;
            }
            getInventory().getStackInSlot(0).shrink(1);
            getInventory().insertItem(1, new ItemStack(recipeFurnace.getResultItem(level.registryAccess()).getItem(), 1), false);
            return true;
        }
        return false;
    }

    public ItemStack getStackInputSlot(int slot)
    {
        return getInventory().getStackInSlot(slot);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new ElectroFurnaceContainer(this, playerInventory, i);
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
            .inputSlot().slotAccess(INPUT)
            .outputSlot().slotAccess(OUTPUT)
            .soulbank().build();
    }
    
    public String getStatusText()
    {
        return recipeFurnace == null ? "" : recipeFurnace.getResultItem(level.registryAccess()).toString();
    }
}
