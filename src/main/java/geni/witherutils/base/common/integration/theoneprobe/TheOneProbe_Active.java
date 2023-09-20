//package geni.witherutils.base.common.integration.theoneprobe;
//
//import com.google.common.base.Function;
//import javax.annotation.Nullable;
//import mcjty.theoneprobe.api.ITheOneProbe;
//import net.minecraftforge.fml.InterModComms;
//
//public class TheOneProbe_Active {
//	
//    public static void sendComms()
//    {
//        InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
//    }
//
//    public static class GetTheOneProbe implements Function<ITheOneProbe, Void>
//    {
//        @Nullable
//        public Void apply(@Nullable ITheOneProbe input)
//        {
//            if (input != null)
//            {
//                input.registerProvider(OneProbeDataProvider.INSTANCE);
//            }
//            return null;
//        }
//    }
//}
//
