package melonslise.mendingrework;

import melonslise.mendingrework.common.config.MRConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(MRCore.ID)
public final class MRCore
{
	public static final String ID = "mendingrework";

	public MRCore()
	{
		ModLoadingContext.get().registerConfig(Type.SERVER, MRConfig.SPECIFICATION);
	}
}