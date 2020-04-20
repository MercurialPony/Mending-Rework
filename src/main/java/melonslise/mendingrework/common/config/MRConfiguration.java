package melonslise.mendingrework.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MRConfiguration
{
	public static final ForgeConfigSpec SPECIFICATION;

	public static final ForgeConfigSpec.DoubleValue MAX_REPAIR_BOOST;
	public static final ForgeConfigSpec.BooleanValue ENABLE_RENEWAL;

	static
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		builder.push("General");
		ENABLE_RENEWAL = builder.comment("If renewal can be obtained in survival without commands").define("Enable renewal", true);
		MAX_REPAIR_BOOST = builder.comment("The maximum percentage of a tool's stats gained from the repair bonus").defineInRange("Max repair boost", 0.1d, 0d, 1000d);
		builder.pop();

		SPECIFICATION = builder.build();
	}
}