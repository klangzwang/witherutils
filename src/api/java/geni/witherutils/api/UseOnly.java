package geni.witherutils.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import net.neoforged.fml.LogicalSide;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface UseOnly {
	
    LogicalSide value();
}
