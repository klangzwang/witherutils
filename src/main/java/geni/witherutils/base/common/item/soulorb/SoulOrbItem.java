package geni.witherutils.base.common.item.soulorb;

import java.util.List;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.config.common.LootConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SoulOrbItem extends WitherItem {
	
    public SoulOrbItem()
	{
        super(new Item.Properties());
	}

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
//		if(!pLevel.isClientSide)
//		{
//	    	if(pPlayer.getCapability(WitherUtilsCapabilities.PLAYERSOUL) != null)
//	    	{
//	    		if(!pPlayer.isCrouching())
//	    		{
////					WitherUtilsCapabilities.getPlayerSoulHandler((ServerPlayer) pPlayer).ifPresent(f -> {
////						f.receiveSouls(1, false);
////						CoreNetwork.sendToPlayer(new PacketSoulsReceive(1), (ServerPlayer) pPlayer);
////					});
//					
//					receiveSouls(1, false);
//					CoreNetwork.sendToPlayer(new PacketSoulsReceive(1), (ServerPlayer) pPlayer);
//	    		}
//	    		else
//	    		{
////					WitherUtilsCapabilities.getPlayerSoulHandler((ServerPlayer) pPlayer).ifPresent(f -> {
////						f.extractSouls(1, false);
////						CoreNetwork.sendToPlayer(new PacketSoulsExtract(1), (ServerPlayer) pPlayer);
////					});
//	    			
//					extractSouls(1, false);
//					CoreNetwork.sendToPlayer(new PacketSoulsExtract(1), (ServerPlayer) pPlayer);
//	    		}
//	    		
//	    		System.out.println("SERVER: " + ((ServerPlayer) pPlayer).getCapability(WitherUtilsCapabilities.PLAYERSOUL).getSoulsStored());
//	    	}
//		}
//		else
//		{
//			
//    		System.out.println("CLIENT: " + pPlayer.getCapability(WitherUtilsCapabilities.PLAYERSOUL).getSoulsStored());
//		}
    	return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> list, TooltipFlag flag)
    {
    	super.appendHoverText(stack, pContext, list, flag);

    	list.add(Component.empty());
    	list.add(Component.translatable(ChatFormatting.GRAY + "■-§9 §nDimensions: "));
    	
    	if(LootConfig.SOULORBDROPLIST.get().size() == 0)
    	{
    		list.add(Component.translatable(ChatFormatting.GREEN + "minecraft:the_nether"));
    	}
    	else
    	{
    		final List<String> dimensionlist = LootConfig.SOULORBDROPLIST.get();
    		for (String key : dimensionlist)
    		{
    			list.add(Component.translatable(ChatFormatting.GREEN + key.toString()));
    		}
    	}
    	
    	
    }

    
    
//	public int recSouls(int toReceive, boolean simulate, ServerPlayer player)
//	{
//		IPlayerSoul playerSoul = WitherUtilsCapabilities.getPlayerSoulHandler(player).orElse(null);
//		return playerSoul.receiveSouls(toReceive, simulate);
//	}
//    
//	@Override
//	public int receiveSouls(int toReceive, boolean simulate)
//	{
//		IPlayerSoul playerSoul = WitherUtilsCapabilities.getPlayerSoulHandler(player).orElse(null);
//		return storage.receiveSouls(toReceive, simulate);
//	}
//	@Override
//	public int extractSouls(int toExtract, boolean simulate)
//	{
//		IPlayerSoul playerSoul = WitherUtilsCapabilities.getPlayerSoulHandler(player).orElse(null);
//		return storage.extractSouls(toExtract, simulate);
//	}
//	@Override
//	public int getSoulsStored()
//	{
//		return storage.getSoulsStored();
//	}
//	@Override
//	public int getMaxSoulsStored()
//	{
//		return storage.getMaxSoulsStored();
//	}
//	@Override
//	public Tag serializeNBT(Provider provider)
//	{
//		return storage.serializeNBT(provider);
//	}
//	@Override
//	public void deserializeNBT(Provider provider, Tag nbt)
//	{
//		storage.deserializeNBT(provider, nbt);
//	}
}
