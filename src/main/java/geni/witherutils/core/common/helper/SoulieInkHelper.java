package geni.witherutils.core.common.helper;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.config.BaseConfig;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;

public class SoulieInkHelper {

    private SoulieInkHelper() {
    }

    public static boolean isEnabled()
    {
        return BaseConfig.COMMON.TOOLS.SOULIE_INK_ENABLED.get();
    }

//    public static boolean toggleAntiqueInkOnSigns(Level world, Player player, ItemStack stack, boolean newState, BlockPos pos, BlockEntity tile)
//    {
//        var cap = SuppPlatformStuff.getForgeCap(tile, IAntiqueTextProvider.class);
//
//        boolean success = false;
//        if (cap != null) {
//            if (cap.hasAntiqueInk() != newState) {
//                cap.setAntiqueInk(newState);
//                tile.setChanged();
//                if (world instanceof ServerLevel serverLevel) {
//                    NetworkHandler.CHANNEL.sendToAllClientPlayersInRange(serverLevel, pos, 256,
//                            new ClientBoundSyncAntiqueInk(pos, newState));
//                }
//                success = true;
//            }
//        }
//        if (success) {
//            if (newState) {
//                world.playSound(null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
//            } else {
//                world.playSound(null, pos, SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
//            }
//            if (!player.isCreative()) stack.shrink(1);
//            return true;
//        }
//        return false;
//    }

//    public static void setAntiqueInk(BlockEntity tile, boolean ink) {
//        var cap = SuppPlatformStuff.getForgeCap(tile, IAntiqueTextProvider.class);
//        if (cap != null) {
//            cap.setAntiqueInk(ink);
//        }
//    }

    public static void setAntiqueInk(ItemStack stack, boolean ink)
    {
        if (ink)
        {
            stack.getOrCreateTag().putBoolean("SoulieInk", true);
            if ((stack.getItem() instanceof WrittenBookItem || stack.getItem() instanceof WritableBookItem))
            {
                if (stack.hasTag())
                {
                    ListTag listTag = stack.getTag().getList("pages", 8);
                    ListTag newListTag = new ListTag();
                    for (var v : listTag)
                    {
                        MutableComponent comp = Component.Serializer.fromJson(v.getAsString());
                        newListTag.add(StringTag.valueOf(Component.Serializer.toJson(comp.withStyle(comp.getStyle().withFont(WitherUtils.loc("soulie"))))));
                    }
                    stack.addTagElement("pages", newListTag);
                }
                if (stack.getItem() == Items.WRITTEN_BOOK)
                {
                    stack.getOrCreateTag().putInt("generation", 3);
                }
            }
        }
        else if (stack.hasTag())
        {
            stack.getTag().remove("SoulieInk");
            if (stack.hasTag() && (stack.getItem() instanceof WrittenBookItem || stack.getItem() instanceof WritableBookItem))
            {
                ListTag listTag = stack.getTag().getList("pages", 8);
                ListTag newListTag = new ListTag();
                for (var v : listTag)
                {
                    MutableComponent comp = Component.Serializer.fromJson(v.getAsString());
                    newListTag.add(StringTag.valueOf(Component.Serializer.toJson(comp.withStyle(Style.EMPTY))));
                }
                stack.addTagElement("pages", newListTag);
            }
        }
    }

    public static boolean hasAntiqueInk(ItemStack stack)
    {
        var t = stack.getTag();
        if (t != null)
        {
            return t.contains("SoulieInk");
        }
        return false;
    }
}
