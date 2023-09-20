package geni.witherutils.api.block;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BStateProperties {
	
    public static final EnumProperty<MultiBlockState> MBSTATE = EnumProperty.create("mbstate", MultiBlockState.class);
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
}
