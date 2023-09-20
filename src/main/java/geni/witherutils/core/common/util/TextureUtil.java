package geni.witherutils.core.common.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class TextureUtil {

    public static int[] loadTextureData(ResourceLocation resource)
    {
        BufferedImage img = loadBufferedImage(resource);
        if(img == null)
        {
            return new int[0];
        }
        int w = img.getWidth();
        int h = img.getHeight();
        int[] data = new int[w * h];
        img.getRGB(0, 0, w, h, data, 0, w);
        return data;
    }
    public static BufferedImage loadBufferedImage(ResourceLocation textureFile)
    {
        try
        {
//            return loadBufferedImage(UtilResource.getResourceAsStream(textureFile));
        }
        catch (Exception e)
        {
            System.err.println("Failed to load texture file: " + textureFile);
            e.printStackTrace();
        }
        return null;
    }
    public static BufferedImage loadBufferedImage(InputStream in) throws IOException
    {
        BufferedImage img = ImageIO.read(in);
        in.close();
        return img;
    }
    public static void prepareTexture(int target, int texture, int min_mag_filter, int wrap)
    {
        GlStateManager._getTexLevelParameter(target, GL11.GL_TEXTURE_MIN_FILTER, min_mag_filter);
        GlStateManager._getTexLevelParameter(target, GL11.GL_TEXTURE_MAG_FILTER, min_mag_filter);

        if(target == GL11.GL_TEXTURE_2D)
        {
            GlStateManager._bindTexture(target);
        }
        else
        {
            GL11.glBindTexture(target, texture);
        }

        switch(target)
        {
            case GL12.GL_TEXTURE_3D:
                GlStateManager._getTexLevelParameter(target, GL12.GL_TEXTURE_WRAP_R, wrap);
            case GL11.GL_TEXTURE_2D:
                GlStateManager._getTexLevelParameter(target, GL11.GL_TEXTURE_WRAP_T, wrap);
            case GL11.GL_TEXTURE_1D:
                GlStateManager._getTexLevelParameter(target, GL11.GL_TEXTURE_WRAP_S, wrap);
        }
    }

    public static TextureManager getTextureManager()
    {
        return Minecraft.getInstance().getTextureManager();
    }
    public static TextureAtlas getTextureMap()
    {
        return Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
    }
    public static TextureAtlasSprite getMissingSprite()
    {
        return getTextureMap().getSprite(MissingTextureAtlasSprite.getLocation());
    }
    public static TextureAtlasSprite getTexture(String location)
    {
        return getTextureMap().getSprite(new ResourceLocation(location));
    }
    public static TextureAtlasSprite getTexture(ResourceLocation location)
    {
        return getTextureMap().getSprite(location);
    }
    public static TextureAtlasSprite getBlockTexture(String string)
    {
        return getBlockTexture(new ResourceLocation(string));
    }
    public static TextureAtlasSprite getBlockTexture(ResourceLocation location)
    {
        return getTexture(new ResourceLocation(location.getNamespace(), "block/" + location.getPath()));
    }
    public static TextureAtlasSprite getItemTexture(String string)
    {
        return getItemTexture(new ResourceLocation(string));
    }
    public static TextureAtlasSprite getItemTexture(ResourceLocation location)
    {
        return getTexture(new ResourceLocation(location.getNamespace(), "items/" + location.getPath()));
    }
    public static void changeTexture(String texture)
    {
        changeTexture(new ResourceLocation(texture));
    }
    public static void changeTexture(ResourceLocation texture)
    {
        getTextureManager().getTexture(texture);
    }
    public static void disableMipmap(String texture)
    {
        disableMipmap(new ResourceLocation(texture));
    }
    public static void disableMipmap(ResourceLocation texture)
    {
        getTextureManager().getTexture(texture).setBlurMipmap(false, false);
    }
    public static void restoreLastMipmap(String texture)
    {
        restoreLastMipmap(new ResourceLocation(texture));
    }
    public static void restoreLastMipmap(ResourceLocation location)
    {
        getTextureManager().getTexture(location).restoreLastBlurMipmap();
    }
    public static void bindBlockTexture()
    {
        changeTexture(TextureAtlas.LOCATION_BLOCKS);
    }
    public static void dissableBlockMipmap()
    {
        disableMipmap(TextureAtlas.LOCATION_BLOCKS);
    }
    public static void restoreBlockMipmap()
    {
        restoreLastMipmap(TextureAtlas.LOCATION_BLOCKS);
    }
    @Deprecated
    public static TextureAtlasSprite[] getSideIconsForBlock(BlockState state)
    {
        TextureAtlasSprite[] sideSprites = new TextureAtlasSprite[6];
        TextureAtlasSprite missingSprite = getMissingSprite();
        for(int i = 0; i < 6; i++)
        {
            TextureAtlasSprite[] sprites = getIconsForBlock(state, i);
            TextureAtlasSprite sideSprite = missingSprite;
            if(sprites.length != 0) 
            {
                sideSprite = sprites[0];
            }
            sideSprites[i] = sideSprite;
        }
        return sideSprites;
    }
    @Deprecated
    public static TextureAtlasSprite[] getIconsForBlock(BlockState state, int side)
    {
        return getIconsForBlock(state, Direction.values()[side]);
    }
    @Deprecated
    public static TextureAtlasSprite[] getIconsForBlock(BlockState state, Direction side)
    {
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        if(model != null)
        {
            List<BakedQuad> quads = model.getQuads(state, side, RandomSource.create(0));
            if(quads != null && quads.size() > 0)
            {
                TextureAtlasSprite[] sprites = new TextureAtlasSprite[quads.size()];
                for(int i = 0; i < quads.size(); i++)
                {
                    sprites[i] = quads.get(i).getSprite();
                }
                return sprites;
            }
        }
        return new TextureAtlasSprite[0];
    }
    @Deprecated
    public static TextureAtlasSprite getParticleIconForBlock(BlockState state)
    {
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        if (model != null)
        {
            return model.getParticleIcon();
        }
        return null;
    }
}