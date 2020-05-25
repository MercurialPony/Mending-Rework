package melonslise.mendingrework.common.particle;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import melonslise.mendingrework.common.init.MRParticleTypes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class GlyphParticleData implements IParticleData
{
	public static final IParticleData.IDeserializer<GlyphParticleData> DESERIALIZER = new IParticleData.IDeserializer<GlyphParticleData>()
	{
		@Override
		public GlyphParticleData deserialize(ParticleType<GlyphParticleData> type, StringReader reader) throws CommandSyntaxException
		{
			reader.expect(' ');
			double rotation = reader.readDouble();
			reader.expect(' ');
			double spiralRadius = reader.readDouble();
			reader.expect(' ');
			double spiralSpeed = reader.readDouble();
			reader.expect(' ');
			int maxAge = reader.readInt();
			return new GlyphParticleData(rotation, spiralRadius, spiralSpeed, maxAge);
		}

		@Override
		public GlyphParticleData read(ParticleType<GlyphParticleData> type, PacketBuffer buffer)
		{
			return new GlyphParticleData((double) buffer.readFloat(), (double) buffer.readFloat(), (double) buffer.readFloat(), (int) buffer.readShort());
		}
	};

	public final double rotation, spiralRadius, spiralSpeed;
	public final int maxAge;

	public GlyphParticleData(double rotation, double spiralRadius, double spiralSpeed, int maxAge)
	{
		this.rotation = rotation;
		this.spiralRadius = spiralRadius;
		this.spiralSpeed = spiralSpeed;
		this.maxAge = maxAge;
	}

	@Override
	public ParticleType<GlyphParticleData> getType()
	{
		return MRParticleTypes.GLYPH;
	}

	@Override
	public void write(PacketBuffer buffer)
	{
		buffer.writeFloat((float) this.rotation);
		buffer.writeFloat((float) this.spiralRadius);
		buffer.writeFloat((float) this.spiralSpeed);
		buffer.writeShort(this.maxAge);
	}

	@Override
	public String getParameters()
	{
		return String.format(Locale.ROOT, "%s %.2f %.2f", this.getType().getRegistryName(), this.spiralRadius, this.spiralSpeed);
	}
}