package melonslise.mendingrework.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import melonslise.mendingrework.common.block.BlockEnchantingAltar;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;

public final class MRBlocks
{
	private static final List<Block> BLOCKS = Lists.newArrayList();

	public static final Block ENCHANTING_ALTAR = add(new BlockEnchantingAltar());

	private MRBlocks() {}

	public static void register(RegistryEvent.Register<Block> event)
	{
		for(Block block : BLOCKS) event.getRegistry().register(block);
	}

	private static Block add(Block block)
	{
		BLOCKS.add(block);
		return block;
	}
}