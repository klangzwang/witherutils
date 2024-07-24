//package geni.witherutils.base.common.item.soulbank;
//
//import javax.annotation.Nullable;
//
//import geni.witherutils.api.capability.IMultiCapabilityItem;
//import geni.witherutils.api.capability.MultiCapabilityProvider;
//import geni.witherutils.api.soulbank.ISoulBankData;
//import geni.witherutils.base.common.base.WitherItem;
//import geni.witherutils.base.common.init.WUTCapabilities;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.common.util.LazyOptional;
//
//public class SoulBankItem extends WitherItem implements IMultiCapabilityItem {
//
//    private final ISoulBankData data;
//    
//	public SoulBankItem(ISoulBankData data, Properties pProperties)
//	{
//		super(pProperties.stacksTo(1));
//        this.data = data;
//	}
//	
//    @Nullable
//    @Override
//    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider)
//    {
//        provider.add(WUTCapabilities.SOULBANK, LazyOptional.of(() -> data));
//        return provider;
//    }
//}
