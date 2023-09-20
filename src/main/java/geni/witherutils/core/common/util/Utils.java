package geni.witherutils.core.common.util;

import java.nio.file.Path;

import javax.annotation.Nullable;

import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import geni.witherutils.core.common.math.Vec3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class Utils {

    private Utils() {

    }
    
    private static final TagKey<Item> WRENCH_TAG_0 = TagKey.create(ForgeRegistries.Keys.ITEMS, new ResourceLocation("forge:tools/wrench"));
    private static final TagKey<Item> WRENCH_TAG_1 = TagKey.create(ForgeRegistries.Keys.ITEMS, new ResourceLocation("forge:wrenches"));
    
    @SuppressWarnings("deprecation")
	public static boolean isWrench(Item item)
    {
        return item.builtInRegistryHolder().is(WRENCH_TAG_0) || item.builtInRegistryHolder().is(WRENCH_TAG_1);
    }
    
    public static boolean isModLoaded(String modid) {

        return ModList.get().isLoaded(modid);
    }

    public static boolean isClientWorld(Level world) {

        return world.isClientSide;
    }

    public static boolean isServerWorld(Level world) {

        return !world.isClientSide;
    }

    public static boolean isFakePlayer(Entity entity) {

        return entity instanceof FakePlayer;
    }

    public static boolean isCreativePlayer(Entity entity) {

        return entity instanceof Player player && player.isCreative();
    }

    public static EquipmentSlot handToEquipSlot(InteractionHand hand) {

        return hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    public static InteractionHand otherHand(InteractionHand hand) {

        return hand.equals(InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    public static EquipmentSlot otherHand(EquipmentSlot slot) {

        return slot.equals(EquipmentSlot.MAINHAND) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
    }

    public static String createPrettyJSON(String jsonString) {

        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    public static boolean spawnLightningBolt(Level world, BlockPos pos) {

        return spawnLightningBolt(world, pos, null);
    }

    public static boolean spawnLightningBolt(Level world, BlockPos pos, Entity caster) {

        if (Utils.isServerWorld(world)) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(world);
            bolt.moveTo(Vec3.atBottomCenterOf(pos));
            bolt.setCause(caster instanceof ServerPlayer player ? player : null);
            world.addFreshEntity(bolt);
        }
        return true;
    }

    public static void spawnXpOrbs(Level world, int xp, Vec3 pos) {

        if (world == null) {
            return;
        }
        while (xp > 0) {
            int orbAmount = ExperienceOrb.getExperienceValue(xp);
            xp -= orbAmount;
            world.addFreshEntity(new ExperienceOrb(world, pos.x, pos.y, pos.z, orbAmount));
        }
    }

    public static boolean destroyBlock(Level world, BlockPos pos, boolean dropBlock, @Nullable Entity entityIn) {

        BlockState state = world.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(world, pos) < 0 || (entityIn instanceof Player player && state.getDestroyProgress(player, world, pos) < 0)) {
            return false;
        } else {
            FluidState ifluidstate = world.getFluidState(pos);
            if (dropBlock) {
                BlockEntity tileentity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
                Block.dropResources(state, world, pos, tileentity, entityIn, ItemStack.EMPTY);
            }
            return world.setBlock(pos, ifluidstate.createLegacyBlock(), 3);
        }
    }

    @Nullable
    public static String getKeynameFromKeycode(int code) {

        return GLFW.glfwGetKeyName(code, -1);
    }

    @Nullable
    public static String getKeyNameFromScanCode(int code) {

        return GLFW.glfwGetKeyName(-1, code);
    }

    public static void openEntityScreen(ServerPlayer player, MenuProvider containerSupplier, Entity entity) {

        NetworkHooks.openScreen(player, containerSupplier, buf -> buf.writeVarInt(entity.getId()));
    }

    @SuppressWarnings({ "resource", "unchecked" })
    public static <E extends Entity> E getEntityFromBuf(FriendlyByteBuf buf, Class<E> type) {

        if (buf == null) {
            throw new IllegalArgumentException("Null packet buffer.");
        }
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().level == null) {
                throw new IllegalStateException("Client world is null.");
            }
            int entityId = buf.readVarInt();
            Entity e = Minecraft.getInstance().level.getEntity(entityId);
            if (type.isInstance(e)) {
                return (E) e;
            }
            throw new IllegalStateException("Client could not locate entity (id: " + entityId + ")  for entity container or the entity was of an invalid type. This is likely caused by a mod breaking client side entity lookup.");
        });
    }

    public static final int TIME_CONSTANT = 40;
    public static final int TIME_CONSTANT_HALF = TIME_CONSTANT / 2;
    public static final int TIME_CONSTANT_QUARTER = TIME_CONSTANT / 4;

    private static int timeConstant = 0;
    private static int timeConstantHalf = 0;
    private static int timeConstantQuarter = 0;

    public static void tickTimeConstants() {

        if (++timeConstant >= TIME_CONSTANT) {
            timeConstant = 0;
        }
        if (++timeConstantHalf >= TIME_CONSTANT_HALF) {
            timeConstantHalf = 0;
        }
        if (++timeConstantQuarter >= TIME_CONSTANT_QUARTER) {
            timeConstantQuarter = 0;
        }
    }

    public static boolean timeCheck() {

        return timeConstant == 0;
    }

    public static boolean timeCheckHalf() {

        return timeConstantHalf == 0;
    }

    public static boolean timeCheckQuarter() {

        return timeConstantQuarter == 0;
    }
    public static void spawnBlockParticlesClient(Level world, ParticleOptions particle, BlockPos pos, RandomSource rand, int count) {

        for (int i = 0; i < count; ++i) {
            double d0 = (double) pos.getX() + rand.nextDouble();
            double d1 = (double) pos.getY() + rand.nextDouble();
            double d2 = (double) pos.getZ() + rand.nextDouble();
            double d3 = (rand.nextDouble() - 0.5D) * 0.5D;
            double d4 = (rand.nextDouble() - 0.5D) * 0.5D;
            double d5 = (rand.nextDouble() - 0.5D) * 0.5D;
            world.addParticle(particle, d0, d1, d2, d3, d4, d5);
        }
    }

    public static void spawnParticles(Level world, ParticleOptions particle, double posX, double posY, double posZ, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {

        if (isServerWorld(world)) {
            ((ServerLevel) world).sendParticles(particle, posX, posY + 1.0D, posZ, particleCount, xOffset, yOffset, zOffset, speed);
        } else {
            world.addParticle(particle, posX + xOffset, posY + yOffset, posZ + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

    public static void spawnParticles(Level level, ParticleOptions particle, int count, Vec3 pos, float posVar, Vec3 velocity, float velVar) {

        for (int i = 0; i < count; ++i) {
            spawnParticles(level, particle, pos, posVar, velocity, velVar);
        }
    }

    public static void spawnParticles(Level level, ParticleOptions particle, Vec3 pos, float posVar, Vec3 velocity, float velVar) {

        RandomSource rand = level.getRandom();
        spawnParticles(level, particle, pos.add(rand.nextFloat() * posVar * 2 - posVar, rand.nextFloat() * posVar * 2 - posVar, rand.nextFloat() * posVar * 2 - posVar),
                velocity.add(rand.nextFloat() * velVar * 2 - velVar, rand.nextFloat() * velVar * 2 - velVar, rand.nextFloat() * velVar * 2 - velVar));
    }

    public static void spawnParticles(Level level, ParticleOptions particle, Vec3 pos, Vec3 velocity) {

        level.addParticle(particle, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
    }
    // endregion

    // region ENTITY UTILS
    public static boolean addToPlayerInventory(Player player, ItemStack stack) {

        if (stack.isEmpty() || player == null) {
            return false;
        }
        if (stack.getItem() instanceof ArmorItem armorItem) {
            int index = armorItem.getEquipmentSlot().getIndex();
            if (player.getInventory().armor.get(index).isEmpty()) {
                player.getInventory().armor.set(index, stack);
                return true;
            }
        }
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.items.size(); ++i) {
            if (inv.items.get(i).isEmpty()) {
                inv.items.set(i, stack.copy());
                return true;
            }
        }
        return false;
    }

    public static boolean isPotionApplicableNoEvent(LivingEntity entity, MobEffectInstance potioneffectIn) {

        if (entity.getMobType() == MobType.UNDEAD) {
            MobEffect effect = potioneffectIn.getEffect();
            return effect != MobEffects.REGENERATION && effect != MobEffects.POISON;
        }
        return true;
    }

    public static boolean dropItemStackIntoWorld(ItemStack stack, Level world, Vec3 pos) {

        return dropItemStackIntoWorld(stack, world, pos, false);
    }

    public static boolean dropItemStackIntoWorldWithRandomness(ItemStack stack, Level world, BlockPos pos) {

        return dropItemStackIntoWorld(stack, world, Vec3.atCenterOf(pos), true);
    }

    public static boolean dropItemStackIntoWorldWithRandomness(ItemStack stack, Level world, Vec3 pos) {

        return dropItemStackIntoWorld(stack, world, pos, true);
    }

    public static boolean dropItemStackIntoWorld(ItemStack stack, Level world, Vec3 pos, boolean velocity) {

        if (stack.isEmpty()) {
            return false;
        }
        float x2 = 0.5F;
        float y2 = 0.0F;
        float z2 = 0.5F;

        if (velocity) {
            x2 = world.random.nextFloat() * 0.8F + 0.1F;
            y2 = world.random.nextFloat() * 0.8F + 0.1F;
            z2 = world.random.nextFloat() * 0.8F + 0.1F;
        }
        ItemEntity entity = new ItemEntity(world, pos.x + x2, pos.y + y2, pos.z + z2, stack.copy());

        if (velocity) {
            entity.setDeltaMovement(world.random.nextGaussian() * 0.05F, world.random.nextGaussian() * 0.05F + 0.2F, world.random.nextGaussian() * 0.05F);
        } else {
            entity.setDeltaMovement(-0.05, 0, 0);
        }
        world.addFreshEntity(entity);

        return true;
    }
    public static boolean dropDismantleStackIntoWorld(ItemStack stack, Level world, BlockPos pos)
    {
        if (stack.isEmpty() || Utils.isClientWorld(world)) {
            return false;
        }
        float f = 0.3F;
        double x2 = world.random.nextFloat() * f + (1.0F - f) * 0.5D;
        double y2 = world.random.nextFloat() * f + (1.0F - f) * 0.5D;
        double z2 = world.random.nextFloat() * f + (1.0F - f) * 0.5D;
        ItemEntity dropEntity = new ItemEntity(world, pos.getX() + x2, pos.getY() + y2, pos.getZ() + z2, stack);
        dropEntity.setPickUpDelay(10);
        world.addFreshEntity(dropEntity);

        return true;
    }
    public static ResourceLocation getRegistryName(Block block)
    {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
    public static ResourceLocation getRegistryName(Item item)
    {
        return ForgeRegistries.ITEMS.getKey(item);
    }
    public static ResourceLocation getRegistryName(Fluid fluid)
    {
        return ForgeRegistries.FLUIDS.getKey(fluid);
    }
    @SuppressWarnings("rawtypes")
    public static ResourceLocation getRegistryName(EntityType entity)
    {
        return ForgeRegistries.ENTITY_TYPES.getKey(entity);
    }
    public static ResourceLocation getRegistryName(MobEffect effect)
    {
        return ForgeRegistries.MOB_EFFECTS.getKey(effect);
    }
    public static String getModId(Block block)
    {
        ResourceLocation loc = getRegistryName(block);
        return loc == null ? "" : loc.getNamespace();
    }
    public static String getName(Block block)
    {
        ResourceLocation loc = getRegistryName(block);
        return loc == null ? "" : loc.getPath();
    }
    public static String getModId(Item item)
    {
        ResourceLocation loc = getRegistryName(item);
        return loc == null ? "" : loc.getNamespace();
    }
    public static String getModId(ItemStack stack)
    {
        ResourceLocation loc = getRegistryName(stack.getItem());
        return loc == null ? "" : loc.getNamespace();
    }
    public static String getName(Item item)
    {
        ResourceLocation loc = getRegistryName(item);
        return loc == null ? "" : loc.getPath();
    }
    public static String getName(ItemStack stack)
    {
        ResourceLocation loc = getRegistryName(stack.getItem());
        return loc == null ? "" : loc.getPath();
    }
    public static String getModId(Fluid fluid)
    {
        ResourceLocation loc = getRegistryName(fluid);
        return loc == null ? "" : loc.getNamespace();
    }
    public static String getModId(FluidStack stack)
    {
        ResourceLocation loc = getRegistryName(stack.getFluid());
        return loc == null ? "" : loc.getNamespace();
    }
    public static String getName(Fluid fluid)
    {
        ResourceLocation loc = getRegistryName(fluid);
        return loc == null ? "" : loc.getPath();
    }
    public static String getName(FluidStack stack)
    {
        ResourceLocation loc = getRegistryName(stack.getFluid());
        return loc == null ? "" : loc.getPath();
    }
    /**
     * Calculates the exact distance between two points in 3D space
     *
     * @param x1 point A x
     * @param y1 point A y
     * @param z1 point A z
     * @param x2 point B x
     * @param y2 point B y
     * @param z2 point B z
     * @return The distance between point A and point B
     */
    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dy * dy + dz * dz));
    }
    
    public static double getDistance(Vec3D pos1, Vec3D pos2) {
        return getDistance(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }
    
    @Deprecated
    public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return getDistance(x1, y1, z1, x2, y2, z2);
    }

    public static int getCardinalDistance(BlockPos pos1, BlockPos pos2) {
        int x = Math.abs(pos2.getX() - pos1.getX());
        int y = Math.abs(pos2.getY() - pos1.getY());
        int z = Math.abs(pos2.getZ() - pos1.getZ());
        return Math.max(Math.max(x, y), z);
    }

    public static boolean inRangeSphere(BlockPos posA, BlockPos posB, int range) {
        if (Math.abs(posA.getX() - posB.getX()) > range || Math.abs(posA.getY() - posB.getY()) > range || Math.abs(posA.getZ() - posB.getZ()) > range) {
            return false;
        } else
            return getDistanceSq(posA.getX(), posA.getY(), posA.getZ(), posB.getX(), posB.getY(), posB.getZ()) <= range * range;
    }

    @Deprecated
    public static double getDistanceAtoB(double x1, double z1, double x2, double z2)
    {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dz * dz));
    }
    public static double getDistance(double x1, double z1, double x2, double z2)
    {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dz * dz));
    }
    public static double getDistanceSq(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }
    public static double getDistanceSq(double x1, double z1, double x2, double z2)
    {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return dx * dx + dz * dz;
    }
    
	@SuppressWarnings("resource")
	public static Vec3 getEyePosition(Player player)
	{
		Vec3 vec = new Vec3(player.getX(), player.getY(), player.getZ());
		if (player.level().isClientSide)
		{
			vec.add(0, player.getEyeHeight() - player.getEyeY(), 0);
		}
		else
		{
			vec.add(0, player.getEyeHeight(), 0);
			if (player instanceof AbstractClientPlayer && player.isCrouching())
			{
				vec.add(0, 0.08, 0);
			}
		}
		return vec;
	}
	@SuppressWarnings("resource")
	public static Vector3d getEyePositionWU(Player player)
	{
		Vector3d res = new Vector3d(player.getX(), player.getY(), player.getZ());
		if (player.level().isClientSide)
		{
			res.y += player.getEyeHeight() - player.getEyeY();
		}
		else
		{
			res.y += player.getEyeHeight();
			// TODO watch and remember for freak out messages
			if (player instanceof AbstractClientPlayer && player.isCrouching())
			{
				res.y -= 0.08;
			}
		}
		return res;
	}
	public static Vector3d getLookVecWU(Player player)
	{
		Vec3 lv = player.getLookAngle();
		return new Vector3d(lv.x, lv.y, lv.z);
	}
}
