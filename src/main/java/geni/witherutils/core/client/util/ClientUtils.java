package geni.witherutils.core.client.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

@SuppressWarnings("resource")
public class ClientUtils {

    public static Level getWorld()
    {
        return Minecraft.getInstance().level;
    }
    public static boolean inWorld()
    {
        return Minecraft.getInstance().getConnection() != null;
    }

    public static String getServerIP()
    {
        try {
            Connection networkManager = Minecraft.getInstance().getConnection().getConnection();
            String s = networkManager.getRemoteAddress().toString();
            s = s.substring(s.indexOf("/") + 1);
            return s;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void emitParticles(Level world, BlockPos pos, ParticleOptions particle, double yOffset) {
        float xOff = world.random.nextFloat() * 0.6F + 0.2F;
        float zOff = world.random.nextFloat() * 0.6F + 0.2F;
        getClientLevel().addParticle(particle,
                pos.getX() + xOff, pos.getY() + yOffset, pos.getZ() + zOff,
                0, 0, 0);
    }

    public static void emitParticles(Level world, BlockPos pos, ParticleOptions particle) {
        emitParticles(world, pos, particle, 1.2);
    }

    @Nonnull
    public static ItemStack getWornArmor(EquipmentSlot slot) {
        return getClientPlayer().getItemBySlot(slot);
    }

    public static boolean isKeyDown(int keyCode) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyCode);
    }

    public static void openContainerGui(MenuType<? extends AbstractContainerMenu> type, Component displayString) {
        MenuScreens.create(type, Minecraft.getInstance(), -1, displayString);
    }

    public static void closeContainerGui(Screen parentScreen) {
        Minecraft.getInstance().setScreen(parentScreen);
        if (parentScreen instanceof AbstractContainerScreen) {
            getClientPlayer().containerMenu = ((AbstractContainerScreen<?>) parentScreen).getMenu();
        }
    }

	@Nonnull
    public static Level getClientLevel() {
        return Objects.requireNonNull(Minecraft.getInstance().level);
    }

    public static Optional<Level> getOptionalClientLevel() {
        return Optional.ofNullable(Minecraft.getInstance().level);
    }

    @Nonnull
    public static Player getClientPlayer() {
        return Objects.requireNonNull(Minecraft.getInstance().player);
    }

    public static Optional<Player> getOptionalClientPlayer() {
        return Optional.ofNullable(Minecraft.getInstance().player);
    }

    public static boolean hasShiftDown() {
        return Screen.hasShiftDown();
    }

    public static BlockEntity getBlockEntity(BlockPos pos) {
        return getClientLevel().getBlockEntity(pos);
    }

    public static boolean intersects(Rect2i rect, double x, double y, double w, double h) {
        if (rect.getWidth() <= 0 || rect.getHeight() <= 0 || w <= 0 || h <= 0) {
            return false;
        }
        double x0 = rect.getX();
        double y0 = rect.getY();
        return (x + w > x0 &&
                y + h > y0 &&
                x < x0 + rect.getWidth() &&
                y < y0 + rect.getHeight());
    }

    public static boolean isScreenHiRes() {
        Window mw = Minecraft.getInstance().getWindow();
        return mw.getGuiScaledWidth() > 700 && mw.getGuiScaledHeight() > 512;
    }

    public static int getLightAt(BlockPos pos) {
        return LevelRenderer.getLightColor(getClientLevel(), pos);
    }

    public static int getStringWidth(String line) {
        return Minecraft.getInstance().font.width(line);
    }

    public static float[] getTextureUV(BlockState state, Direction face) {
        if (state == null) return null;
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        List<BakedQuad> quads = model.getQuads(state, face, getClientLevel().random, ModelData.EMPTY, null);
        if (!quads.isEmpty()) {
            TextureAtlasSprite sprite = quads.get(0).getSprite();
            return new float[] { sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1() };
        } else {
            return null;
        }
    }

    public static void spawnEntityClientside(Entity e) {
        ((ClientLevel) getClientLevel()).addEntity(e);
    }

    public static String translateDirection(Direction d) {
        return I18n.get("pneumaticcraft.gui.tooltip.direction." + d.toString());
    }

    public static Component translateDirectionComponent(Direction d) {
        return Component.translatable("pneumaticcraft.gui.tooltip.direction." + d.toString());
    }

    public static Component translateKeyBind(KeyMapping keyBinding) {
        return keyBinding.getKeyModifier().getCombinedName(keyBinding.getKey(), () -> keyBinding.getKey().getDisplayName())
                .copy().withStyle(ChatFormatting.YELLOW);
    }

    public static int getRenderDistanceThresholdSq() {
        int d = Minecraft.getInstance().options.renderDistance().get() * 16;
        return d * d;
    }

    public static boolean isFirstPersonCamera() {
        return Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    public static float calculateViewScaling(Vec3 targetPos) {
        Player player = ClientUtils.getClientPlayer();
        Vec3 vec1 = targetPos.subtract(player.position());
        double angle = player.getLookAngle().dot(vec1.normalize());
        float size = 1f;
        if (angle >= 0.8) {
            double dist = Math.max(0.0001, Math.sqrt(player.distanceToSqr(targetPos)));
            double s = 0.8 - (1 / dist);
            size = size * (float)((angle - s) * dist);
        }
        return size;
    }

    public static float getStatSizeMultiplier(double dist) {
        if (dist < 4) {
            return Math.max(0.3f, (float) (dist / 4));
        } else if (dist < 10) {
            return 1f;
        } else {
            return (float) (dist / 10);
        }
    }
}
