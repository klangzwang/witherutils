package geni.witherutils.base.common.block.rack.casing;

import static geni.witherutils.api.block.BStateProperties.MBSTATE;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import javax.annotation.Nonnull;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.block.MultiBlockState;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.rack.terminal.TerminalBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class CaseBlockEntity extends WitherMachineBlockEntity {

    private TerminalBlockEntity teTerminal;
    private BlockPos terminalPos;
    
	public CaseBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.RACK_CASE.get(), pos, state);
	}
    protected CaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }
    
    public TerminalBlockEntity getTerminal()
    {
    	return teTerminal;
    }
    
    @Nonnull
    public Level nonNullLevel()
    {
        return Objects.requireNonNull(super.getLevel());
    }
    
    public TerminalBlockEntity getPrimaryTerminal()
    {
        if (teTerminal == null && terminalPos != null)
        {
            setPrimaryTerminal(nonNullLevel().getBlockEntity(terminalPos) instanceof TerminalBlockEntity v ? v : null);
        }
        return teTerminal;
    }

    public void setPrimaryTerminal(TerminalBlockEntity newTerminal)
    {
        boolean terminalChanging = teTerminal != newTerminal || newTerminal == null && terminalPos != null || newTerminal != null && terminalPos == null;
        terminalPos = newTerminal == null ? null : newTerminal.getBlockPos();
        
        if (terminalChanging && !nonNullLevel().isClientSide)
        {
        	teTerminal = newTerminal;
            CubicStateManager.addDeferredUpdate(this, teTerminal);
            setChanged();
        }
    }
    public void onBlockBreak()
    {
    	teTerminal = getPrimaryTerminal();
        if (teTerminal != null)
        {
        	teTerminal.onMultiBlockBreak();
        }
    }

    private void updateBlockState(TerminalBlockEntity terminal)
    {
        if (!this.isRemoved() && (terminal == null || !terminal.isRemoved()))
        {
            if (getBlockState().hasProperty(MBSTATE))
                nonNullLevel().setBlock(getBlockPos(), calcNewCubicState(terminal), Block.UPDATE_CLIENTS);
        }
    }
    
    private BlockState calcNewCubicState(TerminalBlockEntity terminal)
    {
        MultiBlockState cubicState = MultiBlockState.NONE;
        if (terminal != null && !terminal.isRemoved())
        {
            boolean xMin = getBlockPos().getX() == terminal.multiBlockX;
            boolean yMin = getBlockPos().getY() == terminal.multiBlockY;
            boolean zMin = getBlockPos().getZ() == terminal.multiBlockZ;
            boolean xMax = getBlockPos().getX() == terminal.multiBlockX + terminal.multiBlockSize - 1;
            boolean yMax = getBlockPos().getY() == terminal.multiBlockY + terminal.multiBlockSize - 1;
            boolean zMax = getBlockPos().getZ() == terminal.multiBlockZ + terminal.multiBlockSize - 1;

            // Corners
        	if(xMin && zMin && yMin)
        		cubicState = MultiBlockState.XMIN_ZMIN_LO;
        	else if(xMin && zMax && yMin)
        		cubicState = MultiBlockState.XMIN_ZMAX_LO;
        	else if(xMax && zMin && yMin)
        		cubicState = MultiBlockState.XMAX_ZMIN_LO;
        	else if(xMax && zMax && yMin)
        		cubicState = MultiBlockState.XMAX_ZMAX_LO;
        	else if(xMin && zMin && yMax)
        		cubicState = MultiBlockState.XMIN_ZMIN_HI;
        	else if(xMin && zMax && yMax)
        		cubicState = MultiBlockState.XMIN_ZMAX_HI;
        	else if(xMax && zMin && yMax)
        		cubicState = MultiBlockState.XMAX_ZMIN_HI;
        	else if(xMax && zMax && yMax)
        		cubicState = MultiBlockState.XMAX_ZMAX_HI;

            // Edges
        	else if (yMin && xMin || yMax && xMax || yMin && xMax || yMax && xMin)
            {
            	cubicState = MultiBlockState.XEDGE;
            }
            else if (yMin && zMin || yMax && zMax || yMin && zMax || yMax && zMin)
            {
            	cubicState = MultiBlockState.ZEDGE;
            }
            else if (!yMin && !yMax)
            {
            	if (xMin && zMin)
                	cubicState = MultiBlockState.XMIN_ZMIN_YEDGE;
            	if (xMin && zMax)
                	cubicState = MultiBlockState.XMIN_ZMAX_YEDGE;
            	if (xMax && zMin)
                	cubicState = MultiBlockState.XMAX_ZMIN_YEDGE;
            	if (xMax && zMax)
                	cubicState = MultiBlockState.XMAX_ZMAX_YEDGE;

            	if (!xMin && !xMax && !zMin && !zMax)
            	{
//            		cubicState = MultiBlockState.CONTROLLER;
            	}
            	else if (xMin && zMin || xMax && zMax || xMin && zMax || xMax && zMin)
                {
//                	cubicState = MultiBlockState.YEDGE;
                }
                else
                {
                	if (xMin)
                    	cubicState = MultiBlockState.XMIN_CENTER;
                	else if (zMin)
                    	cubicState = MultiBlockState.ZMIN_CENTER;
                	else if (xMax)
                    	cubicState = MultiBlockState.XMAX_CENTER;
                	else if (zMax)
                    	cubicState = MultiBlockState.ZMAX_CENTER;
                }
            }
            else
            {
            	if (yMin)
            		cubicState = MultiBlockState.YMIN_CENTER;
            	else if (yMax)
            		cubicState = MultiBlockState.YMAX_CENTER;
            }
        }
        return getBlockState().setValue(MBSTATE, cubicState);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        if (tag.getBoolean("noTerminal"))
        {
            terminalPos = null;
        }
        else if (tag.contains("terminalPos"))
        {
        	terminalPos = NbtUtils.readBlockPos(tag.getCompound("terminalPos"));
        }
        else
        {
        	terminalPos = new BlockPos(tag.getInt("terminalX"), tag.getInt("terminalY"), tag.getInt("terminalZ"));
        }
        teTerminal = null;
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        if (terminalPos == null)
        {
            tag.putBoolean("noTerminal", true);
        }
        else
        {
            tag.put("terminalPos", NbtUtils.writeBlockPos(terminalPos));
        }
    }

    @Mod.EventBusSubscriber(modid = WitherUtils.MODID)
    public static class CubicStateManager
    {
        private static final Deque<CubicAndTerminal> todo = new ArrayDeque<>();

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event)
        {
            if (event.phase == TickEvent.Phase.END)
            {
                while (!todo.isEmpty())
                {
                	CubicAndTerminal element = todo.poll();
                    element.cubic().updateBlockState(element.terminal());
                }
            }
        }

        private static void addDeferredUpdate(CaseBlockEntity cubic, TerminalBlockEntity terminal)
        {
            todo.offer(new CubicAndTerminal(cubic, terminal));
        }

        private record CubicAndTerminal(CaseBlockEntity cubic, TerminalBlockEntity terminal)
        {
        }
    }
}
