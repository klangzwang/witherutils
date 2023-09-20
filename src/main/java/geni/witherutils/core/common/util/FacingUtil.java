package geni.witherutils.core.common.util;

import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.EAST;
import static net.minecraft.core.Direction.NORTH;
import static net.minecraft.core.Direction.SOUTH;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.core.Direction.WEST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class FacingUtil {
	
	public static final Direction[] VALUES = Direction.values();
	public static final Direction[] BY_HORIZONTAL_INDEX = Arrays.stream(VALUES)
			.filter((direction) -> direction.getAxis().isHorizontal())
			.sorted(Comparator.comparingInt(Direction::get2DDataValue))
			.toArray(Direction[]::new);
	
    public static final BlockPos[] AROUND_X;
    public static final BlockPos[] AROUND_Y;
    public static final BlockPos[] AROUND_Z;
    
    public static final BlockPos[] AROUND_Y_ALL;
    public static final BlockPos[] AROUND_ALL;

    public static final Direction[] FACES_AROUND_X;
    public static final Direction[] FACES_AROUND_Y;
    public static final Direction[] FACES_AROUND_Z;

    static
    {
        AROUND_X = new BlockPos[]{new BlockPos(0, 1, -1), new BlockPos(0, 1, 0), new BlockPos(0, 1, 1), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, -1, -1), new BlockPos(0, -1, 0), new BlockPos(0, -1, 1)};
        AROUND_Y = new BlockPos[]{new BlockPos(-1, 0, 1), new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, -1), new BlockPos(0, 0, -1), new BlockPos(1, 0, -1)};
        AROUND_Z = new BlockPos[]{new BlockPos(1, 1, 0), new BlockPos(0, 1, 0), new BlockPos(-1, 1, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(1, -1, 0), new BlockPos(0, -1, 0), new BlockPos(-1, -1, 0)};

        AROUND_Y_ALL = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, -1), new BlockPos(0, 0, -1), new BlockPos(1, 0, -1)};

        List<BlockPos> list = new ArrayList<BlockPos>();

        for (BlockPos pos : AROUND_X)
        {
            if (!list.contains(pos))
            {
                list.add(pos);
            }
        }
        for (BlockPos pos : AROUND_Y)
        {
            if (!list.contains(pos))
            {
                list.add(pos);
            }
        }
        for (BlockPos pos : AROUND_Z)
        {
            if (!list.contains(pos))
            {
                list.add(pos);
            }
        }

        for (BlockPos pos : AROUND_Y_ALL)
        {
            if (!list.contains(pos))
            {
                list.add(pos);
            }
        }
        
        AROUND_ALL = list.toArray(new BlockPos[0]);

        List<Direction> x = new ArrayList<Direction>();
        List<Direction> y = new ArrayList<Direction>();
        List<Direction> z = new ArrayList<Direction>();

        for (Direction facing : Direction.values()) {
            if (facing.getAxis() != Direction.Axis.X) {
                x.add(facing);
            }
            if (facing.getAxis() != Direction.Axis.Y) {
                y.add(facing);
            }
            if (facing.getAxis() != Direction.Axis.Z) {
                z.add(facing);
            }
        }

        FACES_AROUND_X = x.toArray(new Direction[1]);
        FACES_AROUND_Y = y.toArray(new Direction[1]);
        FACES_AROUND_Z = z.toArray(new Direction[1]);
    }

    public static BlockPos[] getAroundAxis(Direction.Axis axis)
    {
        if (axis == Direction.Axis.X)
        {
            return AROUND_X;
        }
        else if (axis == Direction.Axis.Y)
        {
            return AROUND_Y;
        }
        else
        {
            return AROUND_Z;
        }
    }

    public static Direction[] getAxisFaces(Direction.Axis axis)
    {
        switch (axis)
        {
            case X: return new Direction[]{Direction.EAST, Direction.WEST};
            case Y: return new Direction[]{Direction.UP, DOWN};
            case Z: return new Direction[]{Direction.NORTH, Direction.SOUTH};
            default: return new Direction[0];
        }
    }

    public static Direction[] getFacingsAroundAxis(Direction.Axis axis)
    {
        if (axis == Direction.Axis.X)
        {
            return FACES_AROUND_X;
        }
        else if (axis == Direction.Axis.Y)
        {
            return FACES_AROUND_Y;
        }
        else
        {
            return FACES_AROUND_Z;
        }
    }

    public static Direction rotateAround(Direction facing, Direction.Axis axis, boolean reverse)
    {
        switch (axis) {
            case X:

                if (facing != Direction.WEST && facing != Direction.EAST) {
                    return rotateX(facing, reverse);
                }

                return facing;
            case Y:

                if (facing != Direction.UP && facing != DOWN) {
                    return rotateY(facing, reverse);
                }

                return facing;
            case Z:

                if (facing != Direction.NORTH && facing != Direction.SOUTH) {
                    return rotateZ(facing, reverse);
                }

                return facing;
            default:
                throw new IllegalStateException("Unable to get CW facing for axis " + axis);
        }
    }

    private static Direction rotateY(Direction facing, boolean reverse)
    {
        switch (facing) {
            case NORTH:
                return reverse ? Direction.WEST : Direction.EAST;
            case EAST:
                return reverse ? Direction.NORTH : Direction.SOUTH;
            case SOUTH:
                return reverse ? Direction.EAST : Direction.WEST;
            case WEST:
                return reverse ? Direction.SOUTH : Direction.NORTH;
            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + facing);
        }
    }

    private static Direction rotateX(Direction facing, boolean reverse)
    {
        switch (facing) {
            case NORTH:
                return reverse ? Direction.UP : DOWN;
            case EAST:
            case WEST:
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of " + facing);
            case SOUTH:
                return reverse ? DOWN : Direction.UP;
            case UP:
                return reverse ? Direction.SOUTH : Direction.NORTH;
            case DOWN:
                return reverse ? Direction.NORTH : Direction.SOUTH;
        }
    }

    private static Direction rotateZ(Direction facing, boolean reverse)
    {
        switch (facing) {
            case EAST:
                return reverse ? Direction.UP : DOWN;
            case SOUTH:
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of " + facing);
            case WEST:
                return reverse ? DOWN : Direction.UP;
            case UP:
                return reverse ? Direction.WEST : Direction.EAST;
            case DOWN:
                return reverse ? Direction.EAST : Direction.WEST;
        }
    }

    public static int distanceInDirection(BlockPos fromPos, BlockPos toPos, Direction direction)
    {
        switch (direction) {
            case DOWN:
                return fromPos.getY() - toPos.getY();
            case UP:
                return toPos.getY() - fromPos.getY();
            case NORTH:
                return fromPos.getZ() - toPos.getZ();
            case SOUTH:
                return toPos.getZ() - fromPos.getZ();
            case WEST:
                return fromPos.getX() - toPos.getX();
            case EAST:
                return toPos.getX() - fromPos.getX();
        }
        return 0;
    }

    public static Direction rotateXYZ(Direction current)
    {
        switch (current) {
            case DOWN:
                return UP;
            case UP:
                return NORTH;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return EAST;
            case EAST:
                return DOWN;
        }
        return NORTH;
    }
}
