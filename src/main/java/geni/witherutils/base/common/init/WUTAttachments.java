package geni.witherutils.base.common.init;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import geni.witherutils.api.lib.Names;
import geni.witherutils.core.common.helper.SoulsHelper;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class WUTAttachments {
	
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Names.MODID);

    public static final Supplier<AttachmentType<Integer>> SOULS_CONTROL = ATTACHMENT_TYPES.register("souls_control", () -> AttachmentType.builder(()-> SoulsHelper.getSouls()).serialize(Codec.INT).build());
}
