package geni.witherutils.core.common.lib;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import static geni.witherutils.base.common.block.fakedriver.FakeDriverBlock.FACING;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;

public class LogicSupport {

    private int powerOutput = 0;

    public static Direction getFacing(BlockState state)
    {
        return state.getValue(FACING);
    }

    public void setPowerOutput(int powerOutput)
    {
        this.powerOutput = powerOutput;
    }

    public int getPowerOutput()
    {
        return powerOutput;
    }

    public void setRedstoneState(WitherMachineBlockEntity te, int newout)
    {
        if (powerOutput == newout)
        {
            return;
        }
        powerOutput = newout;
        te.setChanged();
        BlockState state = te.getBlockState();
        Direction outputSide = getFacing(state).getOpposite();
        te.getLevel().neighborChanged(te.getBlockPos().relative(outputSide), state.getBlock(), te.getBlockPos());
    }

    public void checkRedstone(WitherMachineBlockEntity te, Level world, BlockPos pos)
    {
        Direction inputSide = getFacing(world.getBlockState(pos));
        int power = getInputStrength(world, pos, inputSide);
        te.setPowerInput(power);
    }

    public int getInputStrength(Level world, BlockPos pos, Direction side)
    {
        int power = world.getSignal(pos.relative(side), side);
        if (power < 15)
        {
            BlockState blockState = world.getBlockState(pos.relative(side));
            Block b = blockState.getBlock();
            if (b == Blocks.REDSTONE_WIRE)
            {
                power = Math.max(power, blockState.getValue(RedStoneWireBlock.POWER));
            }
        }
        return power;
    }

    public int getRedstoneOutput(BlockState state, Direction side)
    {
        if (side == getFacing(state))
        {
            return getPowerOutput();
        }
        else
        {
            return 0;
        }
    }
}
