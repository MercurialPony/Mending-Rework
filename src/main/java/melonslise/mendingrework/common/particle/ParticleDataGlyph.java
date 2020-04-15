package melonslise.mendingrework.common.particle;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import melonslise.mendingrework.common.init.MRParticleTypes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class ParticleDataGlyph implements IParticleData
{
	public static final IParticleData.IDeserializer<ParticleDataGlyph> DESERIALIZER = new IParticleData.IDeserializer<ParticleDataGlyph>()
	{
		@Override
		public ParticleDataGlyph deserialize(ParticleType<ParticleDataGlyph> type, StringReader reader) throws CommandSyntaxException
		{
			reader.expect(' ');
			double rotation = reader.readDouble();
			reader.expect(' ');
			double spiralRadius = reader.readDouble();
			reader.expect(' ');
			double spiralSpeed = reader.readDouble();
			reader.expect(' ');
			int maxAge = reader.readInt();
			return new ParticleDataGlyph(rotation, spiralRadius, spiralSpeed, maxAge);
		}

		@Override
		public ParticleDataGlyph read(ParticleType<ParticleDataGlyph> type, PacketBuffer buffer)
		{
			return new ParticleDataGlyph((double) buffer.readFloat(), (double) buffer.readFloat(), (double) buffer.readFloat(), (int) buffer.readShort());
		}
	};

	public final double rotation, spiralRadius, spiralSpeed;
	public final int maxAge;

	public ParticleDataGlyph(double rotation, double spiralRadius, double spiralSpeed, int maxAge)
	{
		this.rotation = rotation;
		this.spiralRadius = spiralRadius;
		this.spiralSpeed = spiralSpeed;
		this.maxAge = maxAge;
	}

	@Override
	public ParticleType<ParticleDataGlyph> getType()
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