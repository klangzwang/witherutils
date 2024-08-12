package geni.witherutils.core.common.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.smarttv.SmartTVBlockEntity;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
@OnlyIn(Dist.CLIENT)
public class DownloadingTexture extends SimpleTexture {
	
    private static final Logger LOGGER = WitherUtils.LOGGER;
    private final File cacheFile;
    private final String imageUrl;
    private final Runnable processTask;
    private CompletableFuture<?> future;
    private int frameIndex = 0;
    private int delayTimer = 0;
    private SmartTVBlockEntity frame;
    private ArrayList<byte[]> frames;
    public boolean error;
    public boolean loaded = false;

    public DownloadingTexture(@Nullable File cacheFileIn, String imageUrlIn, ResourceLocation textureResourceLocation, @Nullable Runnable processTaskIn, SmartTVBlockEntity frame) {
        super(textureResourceLocation);
        this.cacheFile = cacheFileIn;
        this.imageUrl = imageUrlIn;
        this.processTask = processTaskIn;
        this.frame = frame;
    }

    private void setImage(NativeImage nativeImageIn) {
        if (this.processTask != null) {
            this.processTask.run();
        }
        Minecraft.getInstance().execute(() -> {
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(() -> this.upload(nativeImageIn));
            } else {
                this.upload(nativeImageIn);
            }
        });
    }

    private void upload(NativeImage imageIn) {
        TextureUtil.prepareImage((int)this.getId(), (int)imageIn.getWidth(), (int)imageIn.getHeight());
        imageIn.upload(0, 0, 0, true);
    }

    @Override
    public void load(ResourceManager manager) throws IOException {
        if (this.future == null) {
            NativeImage nativeimage;
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                LOGGER.debug("Loading http texture from local cache ({})", this.cacheFile);
                FileInputStream fileinputstream = new FileInputStream(this.cacheFile);
                nativeimage = this.loadTexture(fileinputstream);
            } else {
                nativeimage = null;
            }
            if (nativeimage != null) {
                this.setImage(nativeimage);
            } else {
                this.future = CompletableFuture.runAsync(() -> {
                    HttpURLConnection httpurlconnection = null;
                    LOGGER.debug("Downloading http texture from {} to {}", this.imageUrl, this.cacheFile);
                    try {
                        httpurlconnection = (HttpURLConnection)new URL(this.imageUrl).openConnection(Minecraft.getInstance().getProxy());
                        httpurlconnection.setDoInput(true);
                        httpurlconnection.setDoOutput(false);
                        httpurlconnection.connect();
                        if (httpurlconnection.getResponseCode() / 100 == 2) {
                            InputStream inputstream;
                            if (this.cacheFile != null) {
                                FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), this.cacheFile);
                                inputstream = new FileInputStream(this.cacheFile);
                            } else {
                                inputstream = httpurlconnection.getInputStream();
                            }
                            Minecraft.getInstance().execute(() -> {
                                NativeImage nativeimage1 = this.loadTexture(inputstream);
                                if (nativeimage1 != null) {
                                    this.setImage(nativeimage1);
                                }
                            });
                        }
                    }
                    catch (Exception exception) {
                        LOGGER.error("Couldn't download http texture", (Throwable)exception);
                        this.error = true;
                    }
                    finally {
                        if (httpurlconnection != null) {
                            httpurlconnection.disconnect();
                        }
                    }
                }, Util.backgroundExecutor());
            }
        }
    }

    @Nullable
    private NativeImage loadTexture(InputStream inputStreamIn) {
        NativeImage nativeimage = null;
        try {
            final byte[] data = IOUtils.toByteArray(inputStreamIn);
            String type = DownloadingTexture.readType(data);
            if (type.equalsIgnoreCase("gif")) {
                new Thread(){

					@SuppressWarnings("removal")
					@Override
                    public void run() {
                        GifDecoder gif = new GifDecoder();
                        int status = gif.read(new ByteArrayInputStream(data));
                        if (status == 0) {
                            DownloadingTexture.this.frames = new ArrayList();
                            File pgcfile = new File(SmartTVBlockEntity.cacheDir, Hashing.sha1().hashUnencodedChars(DownloadingTexture.this.frame.url).toString().replaceAll("==", "xx") + ".pgc");
                            if (pgcfile.isFile()) {
                                try {
                                    LOGGER.debug("Loading PGC from local cache ({})", pgcfile.toString());
                                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pgcfile));
                                    DownloadingTexture.this.frames = (ArrayList)ois.readObject();
                                    ois.close();
                                    DownloadingTexture.this.loaded = true;
                                    this.finalize();
                                    return;
                                }
                                catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                            for (int i = 0; i < gif.getFrameCount(); ++i) {
                                try {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ImageIO.write(gif.getFrame(i), "png", baos);
                                    baos.flush();
                                    DownloadingTexture.this.frames.add(baos.toByteArray());
                                    baos.close();
                                    continue;
                                }
                                catch (IOException ioexception) {
                                    LOGGER.warn("Error while loading the texture", ioexception);
                                    DownloadingTexture.this.error = true;
                                }
                            }
                            try {
                                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pgcfile));
                                oos.writeObject(DownloadingTexture.this.frames);
                                oos.close();
                                gif = null;
                                DownloadingTexture.this.loaded = true;
                                this.finalize();
                            }
                            catch (Throwable e) {
                                e.printStackTrace();
                            }
                        } else {
                            LOGGER.error("Failed to read gif: {}", status);
                            DownloadingTexture.this.error = true;
                        }
                    }
                }.start();
            } else {
                this.loaded = true;
            }
            nativeimage = NativeImage.read(new ByteArrayInputStream(data));
        }
        catch (IOException ioexception) {
            LOGGER.warn("Error while loading the texture", ioexception);
            this.error = true;
        }
        return nativeimage;
    }

    private static String readType(byte[] input) throws IOException {
        String string;
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(input);
            string = DownloadingTexture.readType(in);
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(in);
            throw throwable;
        }
        IOUtils.closeQuietly(in);
        return string;
    }

    private static String readType(InputStream input) throws IOException {
        input.mark(0);
        ImageInputStream stream = ImageIO.createImageInputStream(input);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
        if (!iter.hasNext()) {
            return "";
        }
        ImageReader reader = iter.next();
        if (reader.getFormatName().equalsIgnoreCase("gif")) {
            return "gif";
        }
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(stream, true, true);
        try {
            reader.read(0, param);
        }
        catch (IOException e) {
            LOGGER.error("Failed to parse input format", e);
        }
        finally {
            reader.dispose();
            IOUtils.closeQuietly(stream);
        }
        input.reset();
        return reader.getFormatName();
    }

    public boolean isGif() {
        return this.frames != null;
    }

    public void tick() {
        block6: {
            if ((float)this.delayTimer > Math.abs(0.6f * 20.0f - 20.0f)) {
                this.delayTimer = 0;
                try {
                	SmartTVBlockEntity.textureManager.getTexture(this.location).bind();
                    this.setImage(NativeImage.read(new ByteArrayInputStream(this.frames.get(this.frameIndex))));
                    if (this.frameIndex == this.frames.size() - 1) {
                        this.frameIndex = 0;
                        break block6;
                    }
                    ++this.frameIndex;
                }
                catch (IOException e) {
                    LOGGER.error(e.getMessage());
                    this.error = true;
                }
            } else if (this.loaded) {
                ++this.delayTimer;
            }
        }
    }
}

