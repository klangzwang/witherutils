package geni.witherutils.base.common.item.cutter;

import java.util.List;

import com.google.common.collect.Lists;

import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.recipes.CutterRecipe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CutterContainer extends AbstractContainerMenu {

	public static final int PLAYERSIZE = 4 * 9;
	protected Player playerEntity;
	protected Inventory playerInventory;
	protected int startInv = 0;
	protected int endInv = 17;
	
	public static final int INPUT_SLOT = 0;
	public static final int RESULT_SLOT = 1;

	private final ContainerLevelAccess access;
	private final DataSlot selectedRecipeIndex = DataSlot.standalone();
	private final Level level;

	public List<CutterRecipe> recipeList = Lists.newArrayList();
	public CutterRecipe recipe;

	private ItemStack input = ItemStack.EMPTY;

	long lastSoundTime;

	final Slot inputSlot;
	final Slot resultSlot;

	Runnable slotUpdateListener = () -> {};

	public final Container container = new SimpleContainer(1)
	{
		public void setChanged()
		{
			super.setChanged();
			CutterContainer.this.slotsChanged(this);
			CutterContainer.this.slotUpdateListener.run();
		}
	};
	final ResultContainer resultContainer = new ResultContainer();

	public CutterContainer(int id, Inventory playerInventory, Player player)
	{
		this(id, playerInventory, player, ContainerLevelAccess.NULL);
	}
	public CutterContainer(int id, Inventory playerInventory, Player player, final ContainerLevelAccess access)
	{
		super(WUTMenus.CUTTER.get(), id);

		this.playerEntity = player;
		this.playerInventory = playerInventory;

		this.access = access;
		this.level = playerInventory.player.level();

		this.inputSlot = this.addSlot(new Slot(this.container, 0, 17, 113));
		this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 113)
		{
			@Override
			public boolean mayPlace(ItemStack stack)
			{
				return false;
			}
			@Override
			public void onTake(Player player, ItemStack stack)
			{
				stack.onCraftedBy(player.level(), player, stack.getCount());
//				CutterContainer.this.resultContainer.awardUsedRecipes(player, this.container.getItems());
				ItemStack itemstack = CutterContainer.this.inputSlot.remove(1);
				if(!itemstack.isEmpty())
				{
					CutterContainer.this.setupResultSlot();
				}
				access.execute((level, pos) -> {
					long l = level.getGameTime();
					if(CutterContainer.this.lastSoundTime != l)
					{
						level.playSound((Player) null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
						CutterContainer.this.lastSoundTime = l;
					}
				});
				super.onTake(player, stack);
			}
		});

		layoutPlayerInventorySlots(8, 155);
		this.addDataSlot(this.selectedRecipeIndex);
	}

	void setupResultSlot()
	{
		if(!this.recipeList.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get()))
		{
			CutterRecipe cutterrecipe = this.recipeList.get(this.selectedRecipeIndex.get());
			this.resultContainer.setRecipeUsed(cutterrecipe);
			this.resultSlot.set(cutterrecipe.assemble(this.container, this.level.registryAccess()));
		}
		else
		{
			this.resultSlot.set(ItemStack.EMPTY);
		}
		this.broadcastChanges();
	}

	public int getSelectedRecipe()
	{
		return this.selectedRecipeIndex.get();
	}
	public boolean hasInputItem()
	{
		return this.inputSlot.hasItem() && !this.recipeList.isEmpty();
	}
	public int getSelectedRecipeIndex()
	{
		return this.selectedRecipeIndex.get();
	}

	@Override
	public boolean stillValid(Player playerIn)
	{
		return true;
	}
	@Override
	public boolean clickMenuButton(Player p_40309_, int p_40310_)
	{
		if(this.isValidRecipeIndex(p_40310_))
		{
			this.selectedRecipeIndex.set(p_40310_);
			this.setupResultSlot();
		}
		return true;
	}
	private boolean isValidRecipeIndex(int p_40335_)
	{
		return p_40335_ >= 0 && p_40335_ < this.recipeList.size();
	}

	@Override
	public void slotsChanged(Container container)
	{
		ItemStack itemstack = this.inputSlot.getItem();
		if(!itemstack.is(this.input.getItem()))
		{
			this.input = itemstack.copy();
			this.setupRecipeList(container, itemstack);
		};
	}
	private void setupRecipeList(Container p_40304_, ItemStack p_40305_)
	{
		this.recipeList.clear();
		this.selectedRecipeIndex.set(-1);
		this.resultSlot.set(ItemStack.EMPTY);
		if(!p_40305_.isEmpty())
		{
			this.recipeList = this.level.getRecipeManager().getRecipesFor(WUTRecipes.CUTTER.get(), p_40304_, this.level);
		}
	}

	@Override
	public MenuType<?> getType()
	{
		return WUTMenus.CUTTER.get();
	}
	public void registerUpdateListener(Runnable p_40324_)
	{
		this.slotUpdateListener = p_40324_;
	}
	@Override
	public boolean canTakeItemForPickAll(ItemStack p_40321_, Slot p_40322_)
	{
		return p_40322_.container != this.resultContainer && super.canTakeItemForPickAll(p_40321_, p_40322_);
	}
	public void findMatchingCutterRecipe(Container container)
	{
		if(recipe != null && recipe.matches(container, level))
			return;

		recipe = null;

		List<CutterRecipe> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.CUTTER.get());
		for(CutterRecipe rec : recipes)
		{
			if(rec.matches(container, level))
			{
				recipe = rec;
				break;
			}
		}
	}

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
    {
//        if(clickTypeIn != ClickType.QUICK_CRAFT && slotId >= 0)
//        {
//            int clickedSlot = slotId - this.container.getContainerSize();
//            if(clickedSlot == 0 && getSlot(1).hasItem())
//            {
//        		player.playSound(WUTSounds.CUTTER.get(), 1.0f, 1.0f);
//            }
//        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

	@SuppressWarnings("unused")
	@Override
	public void removed(Player player)
	{
		super.removed(player);
		ItemStack inputstack = this.inputSlot.getItem().copy();
		ItemStack outputstack = this.resultSlot.getItem().copy();

		if(!inputstack.isEmpty())
			player.getInventory().add(inputstack);
		this.access.execute((p_40313_, p_40314_) -> {
			this.clearContainer(player, container);
		});
	}
	
	private int addSlotRange(Inventory handler, int index, int x, int y, int amount, int dx) {
		for (int i = 0; i < amount; i++) {
			addSlot(new Slot(handler, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	protected void layoutPlayerInventorySlots(int leftCol, int topRow)
	{
		topRow += 0;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}
	
	public List<CutterRecipe> getRecipes()
	{
		return this.recipeList;
	}
	public int getNumRecipes()
	{
		return this.recipeList.size();
	}
	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_)
	{
		return ItemStack.EMPTY;
	}
}