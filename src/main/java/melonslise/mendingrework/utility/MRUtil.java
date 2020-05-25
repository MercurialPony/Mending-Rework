package melonslise.mendingrework.utility;

import java.lang.reflect.Field;

import melonslise.mendingrework.common.config.MRConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class MRUtil
{
	private MRUtil() {}

	/*
	 * 
	 * Repair Bonus System
	 * 
	 */

	public static final String KEY_REPAIRED_DAMAGE = "RepairedDamage";

	public static boolean hasRepairedDamage(ItemStack stack)
	{
		return stack.isDamageable() && stack.hasTag() && stack.getTag().contains(KEY_REPAIRED_DAMAGE);
	}

	public static int getRepairedDamage(ItemStack stack)
	{
		return stack.isDamageable() ? stack.hasTag() ? stack.getTag().getInt(KEY_REPAIRED_DAMAGE) : 0 : 0;
	}

	public static void setRepairedDamage(ItemStack stack, int damage)
	{
		if(!stack.isDamageable()) return;
		stack.getOrCreateTag().putInt(KEY_REPAIRED_DAMAGE, MathHelper.clamp(damage, 0, stack.getMaxDamage()));
	}

	public static void addRepairedDamage(ItemStack stack, int damage)
	{
		if(!stack.isDamageable()) return;
		setRepairedDamage(stack, getRepairedDamage(stack) + damage);
	}

	public static double getRepairBonus(ItemStack stack)
	{
		return (1d - (double) getRepairedDamage(stack) / (double) stack.getMaxDamage()) * MRConfig.MAX_REPAIR_BOOST.get();
	}

	/*
	 * 
	 * Miscellaneous
	 * 
	 */

	public static Field field_animate_skull;
	static
	{
		try
		{
			field_animate_skull = SkullTileEntity.class.getField("mendingReworkAnimate");
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean animateSkull(World world, BlockPos position, boolean animate)
	{
		TileEntity tileEntity = world.getTileEntity(position);
		if(!(tileEntity instanceof SkullTileEntity)) return false;
		try
		{
			field_animate_skull.setBoolean(tileEntity, animate);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * 
	 * Math
	 * 
	 */

	/*
	 * https://stackoverflow.com/questions/31225062/rotating-a-vector-by-angle-and-axis-in-java
	 */
	public static Vec3d rotateAroundAxis(double x, double y, double z, double axisX, double axisY, double axisZ, double angle)
	{
		double length = (double) MathHelper.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
		axisX /= length;
		axisY /= length;
		axisZ /= length;

		double cos = (double) MathHelper.cos((float) angle);
		double sin= (double) MathHelper.sin((float) angle);

		double rotatedX = axisX * (axisX * x + axisY * y + axisZ * z) * (1d - cos) + x * cos + (-axisZ * y + axisY * z) * sin;
		double rotatedY = axisY * (axisX * x + axisY * y + axisZ * z) * (1d - cos) + y * cos + (axisZ * x - axisX * z)*sin;
		double rotatedZ = axisZ * (axisX * x + axisY * y + axisZ * z) * (1d - cos) + z * cos + (-axisY * x + axisX * y) * sin;

		return new Vec3d(rotatedX, rotatedY, rotatedZ);
	}

	public static Vec3d getOrthogonal(double x, double y, double z, double length)
	{
		if(z == 0d) return new Vec3d(0d, 0d, length);
		double orthX = 1d;
		double orthY = 1d;
		double orthZ = (-x - y) / z;
		double length1 = MathHelper.sqrt(2d + orthZ * orthZ);
		orthX = orthY = orthX / length1 * length;
		orthZ = orthZ / length1 * length;
		return new Vec3d(orthX, orthY, orthZ);
	}
}