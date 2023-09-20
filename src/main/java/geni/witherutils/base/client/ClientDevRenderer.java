//package geni.witherutils.base.client;
//
//import java.awt.Graphics2D;
//import java.awt.geom.AffineTransform;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.nio.ByteBuffer;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//import javax.imageio.ImageIO;
//
//import org.lwjgl.BufferUtils;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//import com.google.common.base.Joiner;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//import com.google.common.io.Files;
//import com.mojang.blaze3d.platform.GlStateManager;
//import com.mojang.blaze3d.systems.RenderSystem;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.resources.language.I18n;
//import net.minecraft.core.NonNullList;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.event.TickEvent.RenderTickEvent;
//import net.minecraftforge.eventbus.api.EventPriority;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.registries.ForgeRegistries;
//
//public class ClientDevRenderer {
//	
//	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
//	
//	public ClientDevRenderer() {
//	}
//
//	/*
//	 * 
//	 * Testing Render Stuff
//	 * 
//	 */
//	@SubscribeEvent(priority = EventPriority.HIGHEST)
//	public void onFrameStart(RenderTickEvent e)
//	{
//		bulkRender("", 0);
//	}
//	
//	private String sanitize(String str)
//	{
//		return str.replaceAll("[^A-Za-z0-9-_ ]", "_");
//	}
//	
//	private void bulkRender(String modidSpec, int size)
//	{
//		Set<String> modids = Sets.newHashSet();
//		for (String str : modidSpec.split(","))
//		{
//			modids.add(str.trim());
//		}
//		List<ItemStack> toRender = Lists.newArrayList();
//		NonNullList<ItemStack> li = NonNullList.create();
//		int rendered = 0;
//		for (ResourceLocation resloc : ForgeRegistries.ITEMS.getKeys())
//		{
//			if (resloc != null && modids.contains(resloc.getNamespace()) || modids.contains("*"))
//			{
//				li.clear();
////				Item i = ForgeRegistries.ITEMS.getObject(resloc);
////				try {
////					i.getSubItems(i.getCreativeTab(), li);
////				} catch (Throwable t) {
////					log.warn("Failed to get renderable items for " + resloc, t);
////				}
//				toRender.addAll(li);
//			}
//		}
//		File folder = new File("renders/" + dateFormat.format(new Date()) + "_" + sanitize(modidSpec) + "/");
//		long lastUpdate = 0;
//		String joined = Joiner.on(", ").join(modids);
////		setUpRenderState(size);
//		for (ItemStack is : toRender) {
////			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
////				break;
////			render(is, folder, false);
//			rendered++;
////			if (Minecraft.getSystemTime() - lastUpdate > 33) {
////				tearDownRenderState();
////				renderLoading(I18n.format("gui.rendering", toRender.size(), joined),
////						I18n.format("gui.progress", rendered, toRender.size(), (toRender.size() - rendered)), is,
////						(float) rendered / toRender.size());
////				lastUpdate = Minecraft.getSystemTime();
////				setUpRenderState(size);
////			}
//		}
////		if (rendered >= toRender.size()) {
////			renderLoading(I18n.get("gui.rendered", toRender.size(), Joiner.on(", ").join(modids)), "", null, 1);
////		} else {
////			renderLoading(I18n.get("gui.renderCancelled"),
////					I18n.get("gui.progress", rendered, toRender.size(), (toRender.size() - rendered)), null,
////					(float) rendered / toRender.size());
////		}
////		tearDownRenderState();
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//		}
//	}
//	
//	private int size;
//	private float oldZLevel;
//	
//	private String render(ItemStack is, File folder, boolean includeDateInFilename)
//	{
//		Minecraft mc = Minecraft.getInstance();
////		String filename = is.getItem().getRegistryName().getResourcePath();
//		
//		
//		String filename = is.getItem().toString();
//		
////		if (is.getHasSubtypes())
////		{
////			String unloc = is.getUnlocalizedName();
////			if (!unloc.contains(filename)) {
////				filename += "" + unloc;
////			} else {
////			}
////			filename = unloc;
////		}
////		if (filename.contains("bucket") || is.getItem().getRegistryName().getResourceDomain().equals("minecraft"))
////		{
////			filename = is.getDisplayName();
////		}
//		filename = sanitize(filename);
//		for (String prefix : new String[] { "^enderio_", "^tile_", "^block_", "^item_", "^enderio_", "^tile_",
//				"^block_", "^item_" }) {
//			filename = filename.replaceFirst(prefix, "");
//		}
//		boolean big = filename.contains("enhanced_");
//		filename = (includeDateInFilename ? dateFormat.format(new Date()) + "_" : "") + filename;
//		RenderSystem.getModelViewStack().pushPose();
//		GlStateManager._clearColor(0, 0, 0, 0);
//		GlStateManager._clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, false);
////        mc.render.getRenderItem().renderItemAndEffectIntoGUI(is, 0, big ? 8 : 0);
//		RenderSystem.getModelViewStack().popPose();
//
//		try
//		{
//			BufferedImage img = createFlipped(readPixels(size, big ? (int) (size * 1.5) : size));
//
//			File f = new File(folder, filename + ".png");
//			int i = 2;
//			while (f.exists()) {
//				f = new File(folder, filename + "_" + i + ".png");
//				i++;
//			}
//			Files.createParentDirs(f);
//			f.createNewFile();
//			ImageIO.write(img, "PNG", f);
//			return I18n.get("msg.render.success", f.getPath());
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//			return I18n.get("msg.render.fail");
//		}
//	}
//	
//	@SuppressWarnings("resource")
//	public BufferedImage readPixels(int width, int height) throws InterruptedException
//	{
//		GL11.glReadBuffer(GL11.GL_BACK);
//		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
//		GL11.glReadPixels(0, Minecraft.getInstance().screen.height - height, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buf);
//		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//		int[] pixels = new int[width * height];
//		buf.asIntBuffer().get(pixels);
//		img.setRGB(0, 0, width, height, pixels, 0, width);
//		return img;
//	}
//	
//	private static BufferedImage createFlipped(BufferedImage image)
//	{
//		AffineTransform at = new AffineTransform();
//		at.concatenate(AffineTransform.getScaleInstance(1, -1));
//		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
//		return createTransformed(image, at);
//	}
//
//	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at)
//	{
//		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g = newImage.createGraphics();
//		g.transform(at);
//		g.drawImage(image, 0, 0, null);
//		g.dispose();
//		return newImage;
//	}
//}
