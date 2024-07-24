package geni.witherutils.base.common.base;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import geni.witherutils.base.client.ClientTooltipHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WitherItem extends Item {

	public WitherItem(Properties properties)
	{
		super(properties.rarity(Rarity.RARE));
	}

	@Override
    public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> list, TooltipFlag flag)
    {
        ClientTooltipHandler.Tooltip.addInformation(stack, pContext, list, flag, true);
    }
    
	public void fillCreativeTab(Output out)
	{
		out.accept(this);
	}
	
	protected void shootMe(Level world, Player shooter, Projectile ball, float pitch, float velocityFactor)
	{
		if (world.isClientSide)
		{
			return;
		}
		
		Vec3 vec31 = shooter.getUpVector(1.0F);
		Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(pitch * ((float) Math.PI / 180F), vec31.x, vec31.y, vec31.z);
		Vec3 vec3 = shooter.getViewVector(1.0F);
		Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
		ball.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocityFactor * 1.5f, 1.0F);
		world.addFreshEntity(ball);
	}
}
