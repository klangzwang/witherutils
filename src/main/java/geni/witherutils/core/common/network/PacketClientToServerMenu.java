package geni.witherutils.core.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Function;

public abstract class PacketClientToServerMenu<Menu extends AbstractContainerMenu> implements Packet {

    private final int containerID;
    private final Class<Menu> menuClass;

    protected PacketClientToServerMenu(Class<Menu> menuClass, int containerID)
    {
        this.containerID = containerID;
        this.menuClass = menuClass;
    }
    protected PacketClientToServerMenu(Class<Menu> menuClass, FriendlyByteBuf buf)
    {
        this.containerID = buf.readInt();
        this.menuClass = menuClass;
    }

    protected void write(FriendlyByteBuf writeInto)
    {
        writeInto.writeInt(containerID);
    }

    @Override
    public boolean isValid(NetworkEvent.Context context)
    {
        if (context.getSender() != null)
        {
            AbstractContainerMenu menu = context.getSender().containerMenu;
            if (menu != null)
            {
                return menu.containerId == containerID && menuClass.isAssignableFrom(menu.getClass());
            }
        }
        return false;
    }

    protected Menu getMenu(NetworkEvent.Context context)
    {
        return menuClass.cast(context.getSender().containerMenu);
    }

    public static class Handler<MSG extends PacketClientToServerMenu<?>> extends PacketHandler<MSG>
    {
        private final Function<FriendlyByteBuf, MSG> constructor;
        public Handler(Function<FriendlyByteBuf, MSG> constructor)
        {
            this.constructor = constructor;
        }
        @Override
        public MSG fromNetwork(FriendlyByteBuf buf)
        {
            return constructor.apply(buf);
        }
        @Override
        public Optional<NetworkDirection> getDirection()
        {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
        @Override
        public void toNetwork(MSG packet, FriendlyByteBuf buf)
        {
            packet.write(buf);
        }
    }

    protected void handleWrongPlayer(NetworkEvent.Context context) {}
}
