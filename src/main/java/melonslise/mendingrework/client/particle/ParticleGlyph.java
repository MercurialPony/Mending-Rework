package melonslise.mendingrework.client.particle;

import melonslise.mendingrework.common.particle.ParticleDataGlyph;
import melonslise.mendingrework.utility.MRUtilities;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// FIXME Transparency
@OnlyIn(Dist.CLIENT)
public class ParticleGlyph extends SpriteTexturedParticle
{
	public double originX, originY, originZ, rotation, oldRotation;
	public final double spiralRadius, spiralSpeed;

	public ParticleGlyph(World world, double x, double y, double z, double speedX, double speedY, double speedZ, double rotation, double spiralRadius, double spiralSpeed, int maxAge)
	{
		super(world, x, y, z);
		Vec3d orth = MRUtilities.getOrthogonal(speedX, speedY, speedZ, spiralRadius);
		this.prevPosX = this.posX = orth.x;
		this.prevPosY = this.posY = orth.y;
		this.prevPosZ = this.posZ = orth.z;
		this.originX = x;
		this.originY = y;
		this.originZ = z;
		this.motionX = speedX;
		this.motionY = speedY;
		this.motionZ = speedZ;
		this.oldRotation = this.rotation = rotation;
		this.spiralRadius = spiralRadius;
		this.spiralSpeed = spiralSpeed;
		this.particleScale = 0.1f * (this.rand.nextFloat() * 0.5f + 0.2f);
		float f = this.rand.nextFloat() * 0.6f + 0.4f;
		this.particleRed = 0.9F * f;
		this.particleGreen = 0.9F * f;
		this.particleBlue = f;
		this.canCollide = false;
		this.maxAge = (int) (Math.random() * 10d) + maxAge;
	}

	public boolean spiral()
	{
		return this.spiralRadius != 0f && this.spiralSpeed != 0f;
	}

	@Override
	public void tick()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.oldRotation = this.rotation;
		if (this.age >= this.maxAge) this.setExpired();
		else
		{
			this.originX += this.motionX;
			this.originY += this.motionY;
			this.originZ += this.motionZ;
			if(this.spiral())
			{
				this.rotation += this.spiralSpeed;
				Vec3d orth = MRUtilities.getOrthogonal(this.motionX, this.motionY, this.motionZ, this.spiralRadius);
				Vec3d position = MRUtilities.rotateAroundAxis(orth.x, orth.y, orth.z, this.motionX, this.motionY, this.motionZ, this.rotation).add(this.originX, this.originY, this.originZ);
				this.posX = position.x;
				this.posY = position.y;
				this.posZ = position.z;
			}
			else
			{
				this.posX = this.originX;
				this.posY = this.originY;
				this.posZ = this.originZ;
			}
		}
		++this.age;
	}

	@Override
	public int getBrightnessForRender(float partialTick)
	{
		int i = super.getBrightnessForRender(partialTick);
		float f = (float) this.age / (float) this.maxAge;
		f = f * f * f * f;
		int j = i & 255;
		int k = i >> 16 & 255;
		k = k + (int) (f * 15f * 16f);
		if (k > 240) k = 240;
		return j | k << 16;
	}

	@Override
	public IParticleRenderType getRenderType()
	{
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<ParticleDataGlyph>
	{
		public final IAnimatedSprite sprites;

		public Factory(IAnimatedSprite sprites)
		{
			this.sprites = sprites;
		}

		@Override
		public Particle makeParticle(ParticleDataGlyph data, World world, double x, double y, double z, double speedX, double speedY, double speedZ)
		{
			ParticleGlyph glyph = new ParticleGlyph(world, x, y, z, speedX, speedY, speedZ, data.rotation, data.spiralRadius, data.spiralSpeed, data.maxAge);
			glyph.selectSpriteRandomly(this.sprites);
			return glyph;
		}
	}
}