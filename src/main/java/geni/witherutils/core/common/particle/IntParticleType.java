package geni.witherutils.core.common.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IntParticleType extends ParticleType<IntParticleType.IntParticleData> {

    @SuppressWarnings("deprecation")
	private static ParticleOptions.Deserializer<IntParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>()
    {
        @Override
        public IntParticleData fromCommand(ParticleType<IntParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException
        {
            List<Integer> list = new ArrayList<>();
            while (reader.peek() == ' ')
            {
                reader.expect(' ');
                list.add((int) reader.readInt());
            }
            return new IntParticleData(particleTypeIn, toPrimitive(list.toArray(new Integer[0])));
        }

        @Override
        public IntParticleData fromNetwork(ParticleType<IntParticleData> particleTypeIn, FriendlyByteBuf buffer)
        {
            return new IntParticleData(particleTypeIn, buffer.readByte());
        }
    };

    public IntParticleType(boolean alwaysShow)
    {
        super(alwaysShow, DESERIALIZER);
    }

    @Override
    public Codec<IntParticleData> codec()
    {
        return null;
    }

    public static int[] toPrimitive(final Integer[] array)
    {
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++)
        {
            result[i] = array[i];
        }
        return result;
    }
    public static String stringArrayConcat(String[] array, String separator)
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < array.length; i++)
        {
            s.append(array[i]);
            if (i + 1 < array.length)
            {
                s.append(separator);
            }
        }
        return s.toString();
    }
    public static String[] arrayToString(int[] array)
    {
        String[] lowercaseArray = new String[array.length];
        for (int i = 0; i < array.length; i++)
        {
            lowercaseArray[i] = String.valueOf(array[i]);
        }
        return lowercaseArray;
    }
    
    public static class IntParticleData implements ParticleOptions
    {
        private ParticleType<?> type;
        private int[] data;

        public IntParticleData(ParticleType<?> type, int... data)
        {
            this.type = type;
            this.data = data;
        }

        @Override
        public ParticleType<?> getType()
        {
            return type;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer)
        {
            buffer.writeVarIntArray(data);
        }

        public int[] get()
        {
            return data;
        }

        @Override
        public String writeToString()
        {
            return String.format(Locale.ROOT, "%s %sb", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), stringArrayConcat(arrayToString(data), " "));
        }
    }
}
