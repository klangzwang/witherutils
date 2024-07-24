package geni.witherutils.core.common.helper;

import java.util.function.BiFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RangeHelper {

    private BlockPos current;
    private AABB box;
    private RangeType type;
    private Direction direction;

    public RangeHelper(BlockPos current, Direction facing, RangeType rangeType)
    {
        this.current = current;
        this.type = rangeType;
        this.direction = facing;
        this.box = rangeType.getOffsetCreation().apply(facing, new AABB(0, 0, 0, 1, 1, 1).move(current));
    }

    public VoxelShape get(int range)
    {
        return Shapes.create(type.getOffsetRange().apply(this, range));
    }
    public BlockPos getCurrent()
    {
        return current;
    }
    public AABB getBox()
    {
        return box;
    }
    public RangeType getType()
    {
        return type;
    }
    public Direction getDirection()
    {
        return direction;
    }

    public enum RangeType
    {
        SW((direction, axisAlignedBB) -> axisAlignedBB.move(direction.getNormal().getX() + 4, direction.getNormal().getY(), direction.getNormal().getZ() + 4),
                (rangeManager, integer) -> rangeManager.getBox().move(rangeManager.getDirection().getNormal().getX() * integer, rangeManager.getDirection().getNormal().getY() * integer, rangeManager.getDirection().getNormal().getZ() * integer).
                inflate(integer, 0, integer)),
        SE((direction, axisAlignedBB) -> axisAlignedBB.move(direction.getOpposite().getNormal().getX() - 4, direction.getOpposite().getNormal().getY(), direction.getOpposite().getNormal().getZ() - 4),
                (rangeManager, integer) -> rangeManager.getBox().move(rangeManager.getDirection().getOpposite().getNormal().getX() * integer, rangeManager.getDirection().getOpposite().getNormal().getY() * integer, rangeManager.getDirection().getOpposite().getNormal().getZ() * integer).
                inflate(integer, 0, integer)),
        NW((direction, axisAlignedBB) -> axisAlignedBB.move(direction.getNormal().getX() - 4, direction.getNormal().getY(), direction.getNormal().getZ() + 4),
                (rangeManager, integer) -> rangeManager.getBox().move(rangeManager.getDirection().getNormal().getX() * integer, rangeManager.getDirection().getNormal().getY() * integer, rangeManager.getDirection().getNormal().getZ() * integer).
                inflate(integer, 0, integer)),
        NE((direction, axisAlignedBB) -> axisAlignedBB.move(direction.getOpposite().getNormal().getX() - 4, direction.getOpposite().getNormal().getY(), direction.getOpposite().getNormal().getZ() + 4),
                (rangeManager, integer) -> rangeManager.getBox().move(rangeManager.getDirection().getOpposite().getNormal().getX() * integer, rangeManager.getDirection().getOpposite().getNormal().getY() * integer, rangeManager.getDirection().getOpposite().getNormal().getZ() * integer).
                inflate(integer, 0, integer)),
        
        ONSELF((direction, axisAlignedBB) -> axisAlignedBB.move(0, 0, 0), (rangeManager, integer) -> rangeManager.getBox().inflate(integer, 0, integer)),
        FRONT((direction, axisAlignedBB) -> axisAlignedBB.move(direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()),
                (rangeManager, integer) -> rangeManager.getBox().move(rangeManager.getDirection().getNormal().getX() * integer, rangeManager.getDirection().getNormal().getY() * integer, rangeManager.getDirection().getNormal().getZ() * integer).
                        inflate(integer, 0, integer)),
        BEHIND((direction, axisAlignedBB) -> axisAlignedBB.move(direction.getOpposite().getNormal().getX(), direction.getOpposite().getNormal().getY(), direction.getOpposite().getNormal().getZ()),
                (rangeManager, integer) -> rangeManager.getBox().move(rangeManager.getDirection().getOpposite().getNormal().getX() * integer, rangeManager.getDirection().getOpposite().getNormal().getY() * integer, rangeManager.getDirection().getOpposite().getNormal().getZ() * integer).
                        inflate(integer, 0, integer)),
        TOP((direction, axisAlignedBB) -> axisAlignedBB.move(0, 1, 0), (rangeManager, integer) -> rangeManager.getBox().inflate(integer, 0, integer)),
        TOP_UP((direction, axisAlignedBB) -> axisAlignedBB.move(0, 2, 0), (rangeManager, integer) -> rangeManager.getBox().inflate(integer, 0, integer)),
        BOTTOM((direction, axisAlignedBB) -> axisAlignedBB.move(0, -1, 0), (rangeManager, integer) -> rangeManager.getBox().inflate(integer, 0, integer));

        private final BiFunction<Direction, AABB, AABB> offsetCreation;
        private final BiFunction<RangeHelper, Integer, AABB> offsetRange;

        RangeType(BiFunction<Direction, AABB, AABB> offsetCreation, BiFunction<RangeHelper, Integer, AABB> offsetRange)
        {
            this.offsetCreation = offsetCreation;
            this.offsetRange = offsetRange;
        }
        public BiFunction<Direction, AABB, AABB> getOffsetCreation()
        {
            return offsetCreation;
        }
        public BiFunction<RangeHelper, Integer, AABB> getOffsetRange()
        {
            return offsetRange;
        }
    }

}