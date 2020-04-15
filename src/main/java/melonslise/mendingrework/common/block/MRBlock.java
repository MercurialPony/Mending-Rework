package melonslise.mendingrework.common.block;

import melonslise.mendingrework.MRCore;
import net.minecraft.block.Block;

public class MRBlock extends Block
{
	public MRBlock(String name, Block.Properties properties)
	{
		super(properties);
		this.setRegistryName(MRCore.ID, name);
	}
}