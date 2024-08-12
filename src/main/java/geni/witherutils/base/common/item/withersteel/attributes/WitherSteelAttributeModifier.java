package geni.witherutils.base.common.item.withersteel.attributes;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

class WitherSteelAttributeModifier {

	public final ResourceLocation id;
	public final double config;
	public final Operation op;
	
	public WitherSteelAttributeModifier(ResourceLocation id, double config, Operation op)
	{
		this.id = id;
		this.config = config;
		this.op = op;
	}
	
    @Nullable
    public AttributeModifier load()
    {
        AttributeModifier attmod = new AttributeModifier(id, config, op);
        return attmod;
    }
}