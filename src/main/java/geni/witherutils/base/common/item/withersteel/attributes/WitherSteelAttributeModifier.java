package geni.witherutils.base.common.item.withersteel.attributes;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.common.base.Charsets;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

class WitherSteelAttributeModifier extends AttributeModifier {

    private final double config;
    
    public WitherSteelAttributeModifier(@Nonnull String name, @Nonnull double config, @Nonnull Operation op)
    {
        super(UUID.nameUUIDFromBytes(name.getBytes(Charsets.UTF_8)), name, 0, op);
        this.config = config;
    }

    @Override
    public double getAmount()
    {
        return config;
    }
}