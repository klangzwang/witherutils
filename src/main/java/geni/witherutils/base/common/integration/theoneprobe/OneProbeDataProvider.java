//package geni.witherutils.base.common.integration.theoneprobe;
//
//import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
//import geni.witherutils.base.common.init.WUTItems;
//import mcjty.theoneprobe.api.IProbeHitData;
//import mcjty.theoneprobe.api.IProbeInfo;
//import mcjty.theoneprobe.api.IProbeInfoProvider;
//import mcjty.theoneprobe.api.ProbeMode;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//
//public class OneProbeDataProvider implements IProbeInfoProvider {
//	
//    static final OneProbeDataProvider INSTANCE = new OneProbeDataProvider();
//    private static final ResourceLocation INFO_PROVIDER_ID = new ResourceLocation("witherutils", "default");
//
//    public ResourceLocation getID()
//    {
//        return INFO_PROVIDER_ID;
//    }
//
//    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState state, IProbeHitData hitData)
//    {
////    	System.out.println(hitData.toString().contains("solar"));
//    	
//        BlockEntity te = world.getBlockEntity(hitData.getPos());
//        if (te instanceof WitherMachineEnergyBlockEntity)
//        {
//        	WitherMachineEnergyBlockEntity machineBe = (WitherMachineEnergyBlockEntity) te;
//        	
//        	IProbeInfo i;
//            i = probeInfo.horizontal().item(new ItemStack(WUTItems.TABWU.get())).vertical();
//            if (machineBe.getEnergyStorage().canExtract())
//            {
//            	i = i.text(Component.literal(""));
//                i = i.text(Component.translatable(ChatFormatting.BOLD + (machineBe.getEnergyStorage().canExtract() ? "CAN EXTRACT" : "CAN NOT EXTRACT")));
//                i = i.text(Component.translatable(ChatFormatting.BOLD + (machineBe.getEnergyStorage().canReceive() ? "CAN RECEIVE" : "CAN NOT RECEIVE")));
//            	i = i.text(Component.literal(""));
//            	i = i.text(Component.translatable(ChatFormatting.AQUA + "TransferRate:" + machineBe.getEnergyStorage().getMaxEnergyTransfer()));
//            }
//        }
//    }
//}
//
