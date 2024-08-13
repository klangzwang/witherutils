package geni.witherutils.base.common.block.fisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import geni.witherutils.core.common.helper.NullHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;

public enum FisherBlockType implements StringRepresentable {

	SINGLE(null),
	MASTER(new BlockPos(0, 0, 0)),
	N(reverseOffsetFor(Direction.NORTH)),
	NE(reverseOffsetFor(Direction.NORTH, Direction.EAST)),
	E(reverseOffsetFor(Direction.EAST)),
	SE(reverseOffsetFor(Direction.SOUTH, Direction.EAST)),
	S(reverseOffsetFor(Direction.SOUTH)),
	SW(reverseOffsetFor(Direction.SOUTH, Direction.WEST)),
	W(reverseOffsetFor(Direction.WEST)),
	NW(reverseOffsetFor(Direction.NORTH, Direction.WEST));

	private final BlockPos offsetToMaster;

	private FisherBlockType(BlockPos offsetToMaster)
	{
		this.offsetToMaster = offsetToMaster;
	}

	@Override
	public @Nonnull String getSerializedName()
	{
		return NullHelper.notnullJ(name().toLowerCase(Locale.US), "toLowerCase returned null!");
	}

	private static BlockPos reverseOffsetFor(Direction... dirs)
	{
		BlockPos res = new BlockPos(0, 0, 0);
		for (Direction dir : dirs)
		{
			res = res.relative(dir.getOpposite());
		}
		return res;
	}

	public BlockPos getOffsetToMaster()
	{
		return offsetToMaster;
	}

	public @Nonnull Vec3i getOffsetFromMaster()
	{
		if (offsetToMaster == null)
		{
			return new Vec3i(0, 0, 0);
		}
		return new BlockPos(-offsetToMaster.getX(), -offsetToMaster.getY(), -offsetToMaster.getZ());
	}

	public BlockPos getLocationOfMaster(@Nonnull BlockPos loc)
	{
		if (offsetToMaster == null)
		{
			return null;
		}
		return loc.offset(offsetToMaster.getX(), offsetToMaster.getY(), offsetToMaster.getZ());
	}

	public List<BlockPos> cornerPositions()
	{
		List<BlockPos> cpos = new ArrayList<>();
		cpos.add(getType(3).getOffsetToMaster());
		cpos.add(getType(5).getOffsetToMaster());
		cpos.add(getType(7).getOffsetToMaster());
		cpos.add(getType(9).getOffsetToMaster());
		return cpos;
	}
	
	public static @Nonnull FisherBlockType getType(int meta)
	{
		if (meta < 0 || meta >= values().length)
		{
			return FisherBlockType.SINGLE;
	    }
	    return NullHelper.notnullJ(values()[meta], "BlockType value is null!");
	}
}
