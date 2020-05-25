package melonslise.mendingrework.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MRConfig
{
	public static final ForgeConfigSpec SPECIFICATION;

	public static final ForgeConfigSpec.DoubleValue MAX_REPAIR_BOOST;
	public static final ForgeConfigSpec.BooleanValue ENABLE_RENEWAL;
	public static final ForgeConfigSpec.BooleanValue RENEWAL_WITH_MENDING;
	public static final ForgeConfigSpec.BooleanValue RENEWAL_WITH_INFINITY;
	public static final ForgeConfigSpec.BooleanValue MENDING_WITH_INFINITY;

	static
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		builder.push("General");
		ENABLE_RENEWAL = builder.comment("If renewal can be obtained in survival without commands").define("Enable renewal", true);
		MAX_REPAIR_BOOST = builder.comment("The maximum percentage of a tool's stats gained from the repair bonus").defineInRange("Max repair boost", 0.1d, 0d, 1000d);
		RENEWAL_WITH_MENDING = builder.comment("If renewal is compatible with mending").define("Renewal with mending", false);
		RENEWAL_WITH_INFINITY = builder.comment("If renewal is compatible with infinity").define("Renewal with infinity", false);
		MENDING_WITH_INFINITY = builder.comment("If mending is compatible with infinity").define("Mending with infinity", true);
		builder.pop();

		SPECIFICATION = builder.build();
	}
}