package geni.witherutils.base.common.block.rack.terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.api.block.BStateProperties;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.rack.MultiStructure;
import geni.witherutils.base.common.block.rack.casing.CaseBlock;
import geni.witherutils.base.common.block.rack.casing.CaseBlockEntity;
import geni.witherutils.base.common.block.rack.controller.fluid.ControllerFluidBlockEntity;
import geni.witherutils.base.common.block.rack.controller.item.ControllerItemBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.BlockEntityUtil;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class TerminalBlockEntity extends WitherMachineBlockEntity {
	
    public int multiBlockX, multiBlockY, multiBlockZ;
    public int multiBlockSize;
    
    public List<TerminalBlockEntity> accessoryTerminals;
    private final List<BlockPos> nbtTerminalList;
    
	public TerminalBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.RACK_TERMINAL.get(), pos, state);
    	
        addDataSlot(new IntegerDataSlot(this::getMultiBlockX, p -> multiBlockX = p, SyncMode.WORLD));
        addDataSlot(new IntegerDataSlot(this::getMultiBlockY, p -> multiBlockY = p, SyncMode.WORLD));
        addDataSlot(new IntegerDataSlot(this::getMultiBlockZ, p -> multiBlockZ = p, SyncMode.WORLD));
        addDataSlot(new IntegerDataSlot(this::getMultiBlockSize, p -> multiBlockSize = p, SyncMode.WORLD));

		this.accessoryTerminals = new ArrayList<>();
		this.nbtTerminalList = new ArrayList<>();
	}

	@Override
	public void serverTick()
	{
		super.serverTick();
		
		isFormed();
	}
	
	@Override
	public void clientTick()
	{
		super.clientTick();
	}

    @Override
    public void onLoad()
    {
        super.onLoad();

        doPostNBTSetup();
    }
    
    private void doPostNBTSetup()
    {
        if (!nbtTerminalList.isEmpty())
        {
            BlockState state = nonNullLevel().getBlockState(getBlockPos());
            if (state.getBlock() instanceof TerminalBlock)
                nonNullLevel().setBlock(getBlockPos(), state.setValue(BStateProperties.FORMED, isPrimaryTerminal()), 2);

            accessoryTerminals.clear();
            for (BlockPos valve : nbtTerminalList)
            {
                BlockEntity te = nonNullLevel().getBlockEntity(valve);
                if (te instanceof TerminalBlockEntity)
                {
                    accessoryTerminals.add((TerminalBlockEntity) te);
                }
            }
            nbtTerminalList.clear();
        }
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        
    	this.multiBlockSize = tag.getInt("multiBlockSize");
    	this.multiBlockX = tag.getInt("multiBlockX");
    	this.multiBlockY = tag.getInt("multiBlockY");
    	this.multiBlockZ = tag.getInt("multiBlockZ");
        
        ListTag accList = tag.getList("Terminals", Tag.TAG_COMPOUND);
        nbtTerminalList.clear();
        for (int i = 0; i < accList.size(); ++i)
        {
            CompoundTag tagCompound = accList.getCompound(i);
            nbtTerminalList.add(NbtUtils.readBlockPos(tagCompound));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        
        tag.putInt("multiBlockX", multiBlockX);
        tag.putInt("multiBlockY", multiBlockY);
        tag.putInt("multiBlockZ", multiBlockZ);
        tag.putInt("multiBlockSize", multiBlockSize);

        ListTag accList = accessoryTerminals.stream()
                .map(terminal -> NbtUtils.writeBlockPos(terminal.getBlockPos()))
                .collect(Collectors.toCollection(ListTag::new));
        tag.put("Terminals", accList);
    }
    
    @Nonnull
    public Level nonNullLevel()
    {
        return Objects.requireNonNull(super.getLevel());
    }
    
    public void onMultiBlockBreak()
    {
        if (isPrimaryTerminal())
        {
            invalidateMultiBlock();
        }
    }
    
    private void invalidateMultiBlock() {
        for (int x = 0; x < multiBlockSize; x++) {
            for (int y = 0; y < multiBlockSize; y++) {
                for (int z = 0; z < multiBlockSize; z++) {
                	
                    BlockEntity te = nonNullLevel().getBlockEntity(new BlockPos(x + multiBlockX, y + multiBlockY, z + multiBlockZ));
                    if (te instanceof CaseBlockEntity teWall)
                    {
                        teWall.setPrimaryTerminal(null);
                    }
                }
            }
        }
        if (accessoryTerminals != null)
        {
            for (TerminalBlockEntity terminal : accessoryTerminals)
            {
            	terminal.multiBlockSize = 0;
            	terminal.multiBlockX = 0;
            	terminal.multiBlockY = 0;
            	terminal.multiBlockZ = 0;

                if (terminal != this)
                {
                	terminal.accessoryTerminals.clear();
                }
                terminal.setChanged();
            }
            accessoryTerminals.clear();
        }
    }

	public static boolean checkForming(Level world, BlockPos pos)
	{
		return calcForming(world, pos);
	}
	private static boolean calcForming(Level world, BlockPos pos)
	{
		return MultiStructure.checkForming(world, pos);
	}

    private boolean isPrimaryTerminal()
    {
        return multiBlockSize > 0;
    }
    
	public int getMultiBlockX()
	{
		return multiBlockX;
	}
	public int getMultiBlockY()
	{
		return multiBlockY;
	}
	public int getMultiBlockZ()
	{
		return multiBlockZ;
	}
	public int getMultiBlockSize()
	{
		return multiBlockSize;
	}
    
    /*
     * 
     * CONTROLLER
     * 
     */
	public boolean isFormed()
	{
		BlockEntity be = level.getBlockEntity(worldPosition.relative(getCurrentFacing()));
		if(be instanceof ControllerItemBlockEntity)
		{
			ControllerItemBlockEntity ctrl = (ControllerItemBlockEntity) be;
			BlockPos ctrlPos = ctrl.getBlockPos();

			for(BlockPos aroundPos : FacingUtil.AROUND_ALL)
			{
				BlockEntity te = level.getBlockEntity(ctrlPos.offset(aroundPos));
				if (te instanceof CaseBlockEntity)
				{
					CaseBlockEntity caseBe = (CaseBlockEntity) te; 
					return CaseBlock.isFormed(caseBe.getBlockState());
				}
			}
		}
		else if(be instanceof ControllerFluidBlockEntity)
		{
			ControllerFluidBlockEntity ctrl = (ControllerFluidBlockEntity) be;
			BlockPos ctrlPos = ctrl.getBlockPos();

			for(BlockPos aroundPos : FacingUtil.AROUND_ALL)
			{
				BlockEntity te = level.getBlockEntity(ctrlPos.offset(aroundPos));
				if (te instanceof CaseBlockEntity)
				{
					CaseBlockEntity caseBe = (CaseBlockEntity) te; 
					return CaseBlock.isFormed(caseBe.getBlockState());
				}
			}
		}
		return false;
	}
	
    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side)
    {
        if (isFormed())
        {
        	if(cap == ForgeCapabilities.ITEM_HANDLER)
        		return getItemHandlerOpposite().cast();
        	else if(cap == ForgeCapabilities.FLUID_HANDLER)
        		return getFluidHandlerOpposite().cast();
        }
        return super.getCapability(cap, side);
    }
	
    /*
     * 
     * ITEM
     * 
     */
    @Nullable
    public LazyOptional<IItemHandler> getItemHandlerOpposite()
    {
    	BlockEntity be = BlockEntityUtil.getBlockEntity(BlockEntity.class, level, worldPosition.relative(getCurrentFacing()));
    	if(be != null)
    	{
    		LazyOptional<IItemHandler> handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER);
        	if(handler != null)
        		return handler;
    	}
    	return null;
    }
    
    @Nullable
    public LazyOptional<IItemHandler> getItemControllerHandler()
    {
    	ControllerItemBlockEntity be = BlockEntityUtil.getBlockEntity(ControllerItemBlockEntity.class, level, worldPosition.relative(getCurrentFacing()));
    	if(be != null)
    	{
    		LazyOptional<IItemHandler> handler = be.getILazyStorage();
        	if(handler != null)
        		return handler;
    	}
    	return null;
    }
    
    /*
     * 
     * FLUID
     * 
     */
    @Nullable
    public LazyOptional<IFluidHandler> getFluidHandlerOpposite()
    {
    	BlockEntity be = BlockEntityUtil.getBlockEntity(BlockEntity.class, level, worldPosition.relative(getCurrentFacing()));
    	if(be != null)
    	{
    		LazyOptional<IFluidHandler> handler = be.getCapability(ForgeCapabilities.FLUID_HANDLER);
        	if(handler != null)
        		return handler;
    	}
    	return null;
    }
    
    @Nullable
    public LazyOptional<IFluidHandler> getFluidControllerHandler()
    {
    	ControllerFluidBlockEntity be = BlockEntityUtil.getBlockEntity(ControllerFluidBlockEntity.class, level, worldPosition.relative(getCurrentFacing()));
    	if(be != null)
    	{
    		LazyOptional<IFluidHandler> handler = be.getFLazyStorage();
        	if(handler != null)
        		return handler;
    	}
    	return null;
    }
}
