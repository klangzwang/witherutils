package geni.witherutils.base.common.block.smarttv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.IInteractBlockEntity;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.remote.RemoteItem;
import geni.witherutils.core.common.helper.DownloadingTexture;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.StringDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.BlockEntityUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkHooks;

public class SmartTVBlockEntity extends WitherMachineBlockEntity implements MenuProvider, IInteractBlockEntity {

    public static final String DEFAULT_URL = "https://i.imgur.com/2tTgIbU.png";
    public String url = "https://www.null.com/null.png";
    
    public int width = 36;
    public int height = 18;
    public String oldUrl = "";
    
    private boolean powered;
    private boolean disabled;

    private List<String> channels = new ArrayList<>();
    private int currentChannel;
    
    @OnlyIn(value=Dist.CLIENT)
    public DownloadingTexture textureWorker;
    @OnlyIn(value=Dist.CLIENT)
    public static File cacheDir;
    @OnlyIn(value=Dist.CLIENT)
    public static TextureManager textureManager;
    @OnlyIn(value=Dist.CLIENT)
    public ResourceLocation textureLocation;
    
    @SuppressWarnings("resource")
	public SmartTVBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(WUTEntities.SMARTTV.get(), worldPosition, blockState);
        add2WayDataSlot(new BooleanDataSlot(this::isPowered, this::setPowered, SyncMode.WORLD));
		add2WayDataSlot(new StringDataSlot(this::getUrl, this::setUrl, SyncMode.GUI));
		
        if (FMLEnvironment.dist.isClient())
        {
        	textureManager = Minecraft.getInstance().getTextureManager();
        	cacheDir = new File(Minecraft.getInstance().gameDirectory, "image_cache");
            cacheDir.mkdir();
            init();
        }
    }
    
    private void init()
    {
        try { FileUtils.writeStringToFile(new File(cacheDir, "!read-me.txt"), "This is a cache for GIFs that are played on the TV in order to speed up load time.\nIt is safe to delete the entire folder in case you are running out of space, however it will mean that all GIFs will have to be downloaded again.", "UTF-8"); }
        catch(IOException e) { e.printStackTrace(); }
    }
    
    @Override
    public InteractionResult onBlockUse(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!level.isClientSide)
        {
	        for(InteractionHand hands : InteractionHand.values())
	        {
	        	if(!(player.getItemInHand(hands).getItem() instanceof RemoteItem))
	        	{
	        		if(isPowered())
	        		{
		                BlockEntity tileEntity = level.getBlockEntity(worldPosition);
		                if(tileEntity instanceof MenuProvider)
		                {
		                    NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
		                    SoundUtil.playSoundFromServer((ServerPlayer) player, WUTSounds.STARTUP.get(), 0.5f, 1.0f);
		                }
		                else
		                    throw new IllegalStateException("Our named container provider is missing!");
		                return InteractionResult.SUCCESS;
	        		}
	        		else
	        			player.sendSystemMessage(Component.translatable("REMOTE TO POWER ON/OFF").withStyle(ChatFormatting.DARK_RED));
	        	}
	        	else
	        	{
	            	if(!isPowered())
	            	{
//	            		setPowered(true);
	            		setLitProperty(true);
	            		setChanged();
	            		
	            		setUrl(DEFAULT_URL);
	            	}
	            	else
	            	{
//	            		setPowered(false);
	            		setLitProperty(false);
	            		setChanged();
	            	}
	        	}
        		return InteractionResult.SUCCESS;
	        }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new SmartTVContainer(this, playerInventory, i);
    }

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
        if(this.url != null)
            tag.putString("ImageVideo", this.url);
        
        ListTag channelList = new ListTag();
        channels.forEach(url -> channelList.add(StringTag.valueOf(url)));
        tag.put("Channels", channelList);
        tag.putInt("CurrentChannel", this.currentChannel);
        tag.putBoolean("Powered", this.powered);
        tag.putBoolean("DisableInteraction", this.disabled);
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
        if(tag.contains("Photo", Tag.TAG_STRING))
            this.url = tag.getString("ImageVideo");

        this.channels.clear();
        if(tag.contains("Channels", Tag.TAG_LIST))
        {
            ListTag channelList = tag.getList("Channels", Tag.TAG_STRING);
            channelList.forEach(nbtBase ->
            {
                if(nbtBase instanceof StringTag)
                {
                	StringTag url = (StringTag) nbtBase;
                    channels.add(url.getAsString());
                }
            });
        }
        else if(tag.contains("URL", Tag.TAG_STRING))
        {
            this.channels.add(tag.getString("URL"));
        }
        if(tag.contains("CurrentChannel", Tag.TAG_INT))
        {
            this.currentChannel = tag.getInt("CurrentChannel");
        }
        if(tag.contains("Powered", Tag.TAG_BYTE))
        {
            this.powered = tag.getBoolean("Powered");
        }
        if(tag.contains("DisableInteraction", Tag.TAG_BYTE))
        {
            this.disabled = tag.getBoolean("DisableInteraction");
        }
	}

    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }

	public BlockPos getContainerPos()
	{
		return this.worldPosition;
	}

    public static void sendToClients(BlockEntity tileEntity)
    {
        Level world = tileEntity.getLevel();
        if(world != null)
        {
            BlockPos pos = tileEntity.getBlockPos();
            world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

	public static enum Fields
	{
		URL;
	}

	public void setUrl(String value)
	{
		url = value;
	}
	public String getUrl()
	{
        String urlLink = "";
        if(!url.isBlank())
        	urlLink = url;
        return urlLink;
	}

	public String updateEntries(String urlLink)
	{
        url = "";
        if(!Strings.isNullOrEmpty(url))
            url = urlLink;
        this.setChanged();
        return null;
	}

    @Override
    public void onDataPacket(final Connection net, final ClientboundBlockEntityDataPacket pkt)
    {
        this.updateTextureIfNeeded();
    }
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

	@SuppressWarnings("unused")
	public void sendToClients()
    {
        final Level f_58857_ = this.getLevel();
        if (f_58857_ instanceof ServerLevel)
        {
            final ServerLevel serverLevel = (ServerLevel) f_58857_;
            final Packet<?> pkt = (Packet<?>) this.getUpdatePacket();
            serverLevel.markAndNotifyBlock(worldPosition, null, getBlockState(), getBlockState(), width, height);
        }
        else
        {
            WitherUtils.LOGGER.warn("Tried to send OPF packet to clients on client side!");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateTextureIfNeeded()
    {
        if (!this.oldUrl.equals(this.url))
        {
            this.loadTexture();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldLoadTexture()
    {
        return !this.isTextureLoaded();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isTextureLoaded()
    {
        return this.textureWorker != null;
    }

    @SuppressWarnings("deprecation")
	@OnlyIn(value=Dist.CLIENT)
    public void loadTexture()
    {
        this.oldUrl = this.url;
        String s = Hashing.sha1().hashUnencodedChars(this.url).toString().replaceAll("==", "");
        this.textureLocation = new ResourceLocation("witherutils", "frames/" + s);
        File f = new File(cacheDir.toString(), s);
        this.textureWorker = new DownloadingTexture(f, this.url, this.textureLocation, null, this);
        textureManager.register(this.textureLocation, (AbstractTexture)this.textureWorker);
        textureManager.getTexture(this.textureLocation, (AbstractTexture)this.textureWorker);
    }

    @OnlyIn(Dist.CLIENT)
    public void tickTexture()
    {
        if (this.textureWorker != null && this.textureWorker.isGif())
        {
            this.textureWorker.tick();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return SmartTVBlockEntity.INFINITE_EXTENT_AABB;
    }
    
    /*
     * 
     * CHANNELED
     * 
     */
    @Nullable
    public String getCurrentChannel()
    {
        if(channels.size() > 0 && currentChannel >= 0 && currentChannel < channels.size())
        {
            return channels.get(currentChannel);
        }
        return null;
    }
    
    @SuppressWarnings("unused")
	public List<String> getEntries()
    {
        List<String> entries = Lists.newArrayList();
        for(int i = 0; i < 3; i++)
        {
            String url = "";
            if(channels.size() > 0 && i < channels.size())
            {
                url = channels.get(i);
            }
        }
        return entries;
    }
    
    public String updateEntries(Map<String, String> entries, Player player)
    {
        channels.clear();
        for(int i = 0; i < 3; i++)
        {
            String url = entries.get("channel_" + i);
            if(!Strings.isNullOrEmpty(url))
            {
                channels.add(url);
            }
        }
        this.powered = Boolean.valueOf(entries.get("powered"));
        this.setChanged();
        return null;
    }

    public boolean nextChannel()
    {
        if(!disabled && powered && channels.size() > 1)
        {
            this.currentChannel++;
            if(this.currentChannel >= channels.size())
            {
                this.currentChannel = 0;
            }
            BlockEntityUtil.sendUpdatePacket(this);
            return true;
        }
        return false;
    }

    /*
     * 
     * POWER 
     * 
     */
	public void setPowered(boolean powered)
	{
		if (!disabled)
		{
			this.powered = powered;
			BlockEntityUtil.sendUpdatePacket(this);
			setChanged();
		}
	}
	public boolean isPowered()
	{
		return powered;
	}
    public boolean isDisabled()
    {
        return disabled;
    }
}
