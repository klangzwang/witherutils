package geni.witherutils.core.common.helper;

import java.util.Collection;
import java.util.List;

import net.minecraft.world.level.Level;

public class DescribeHelper
{
    public static void addDescription(final List<String> list, final String name, final Object object)
    {
        addDescription(list, name + " = ");
        addDescription(list, object, 1);
    }
    
    public static void addDescription(final List<String> list, final Object object)
    {
        addDescription(list, object, 0);
    }
    
    @SuppressWarnings("rawtypes")
	public static void addDescription(final List<String> list, final Object object, final int i)
    {
        if (object == null)
        {
            list.add("[null]");
        }
        else if (object instanceof String)
        {
            final StringBuilder builder = new StringBuilder();
            for (int j = 0; j < i; ++j)
            {
                builder.append('\t');
            }
            builder.append(object);
            list.add(builder.toString());
        }
        else if (object instanceof Iterable)
        {
            if (object instanceof Collection)
            {
                addDescription(list, (object.getClass().getSimpleName() + "_(size = " + ((Collection)object).size() + "){"), i);
            }
            else
            {
                addDescription(list, (object.getClass().getSimpleName() + " {"), i);
            }
            for (final Object o : (Iterable)object)
            {
                addDescription(list, o, i + 1);
            }
        }
//        else if(object instanceof Map)
//        {
//        	Map.Entry map = (Map.Entry) object;
//            addDescription(list, (map.getClass().getSimpleName() + "_(size = " + map.size() + "){"), i);
//            for (Map.Entry entry : ((Map) map).entrySet();
//            {
//                addDescription(list, entry.getKey(), i + 1);
//                addDescription(list, (Object)"=", i + 1);
//                addDescription(list, entry.getValue(), i + 1);
//            }
//            addDescription(list, (Object)"}", i);
//        }
//        else if (object instanceof TicketHelper)
//        {
//            final TicketHelper ticket = (TicketHelper)object;
//            addDescription(list, (Object)("Ticket[Player: " + ticket.getEntityTickets() + " World:" + ticket.level().provider.getDimension() + "]"), i);
//            addDescription(list, (Object)ticket.getChunkList().toString(), i);
//        }
        else if (object instanceof Level)
        {
            final Level world = (Level) object;
            addDescription(list, "World[Dim:" + world.dimension() + " isRemote:" + world.isClientSide + "]");
        }
        else
        {
            final String s = object.toString();
            final int hash = object.hashCode();
            final String s2 = object.getClass().getName() + "@" + Integer.toHexString(hash);
            if (s.equals(s2))
            {
                addDescription(list, (Object)(object.getClass().getSimpleName() + ":" + hash), i);
            }
            else
            {
                addDescription(list, (Object)(object.getClass().getSimpleName() + ":" + s), i);
            }
        }
    }
}
