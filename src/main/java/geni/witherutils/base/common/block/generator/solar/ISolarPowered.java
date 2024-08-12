package geni.witherutils.base.common.block.generator.solar;

public interface ISolarPowered {

	SolarType getType();
	
	int getProduction(SolarType type);
}
