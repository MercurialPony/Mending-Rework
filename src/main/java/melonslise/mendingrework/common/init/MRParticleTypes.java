package melonslise.mendingrework.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.common.particle.ParticleDataGlyph;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;

public final class MRParticleTypes
{
	private static List<ParticleType> PARTICLE_TYPES = Lists.newArrayList();

	public static final ParticleType<ParticleDataGlyph> GLYPH = add("glyph", ParticleDataGlyph.DESERIALIZER);

	private MRParticleTypes() {}

	public static void register(RegistryEvent.Register<ParticleType<?>> event)
	{
		for(ParticleType type : PARTICLE_TYPES) event.getRegistry().register(type);
	}

	private static ParticleType add(String name, IParticleData.IDeserializer deserializer)
	{
		ParticleType type = (ParticleType) new ParticleType(false, deserializer).setRegistryName(MRCore.ID, name);
		PARTICLE_TYPES.add(type);
		return type;
	}
}