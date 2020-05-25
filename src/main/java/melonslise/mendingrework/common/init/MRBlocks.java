package melonslise.mendingrework.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.common.block.EnchantingAltarBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public final class MRBlocks
{
	public static final List<Block> BLOCKS = Lists.newArrayList();

	public static final Block ENCHANTING_ALTAR = add("enchanting_altar", new EnchantingAltarBlock(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(4f, 8f)));

	private MRBlocks() {}

	public static void register(RegistryEvent.Register<Block> event)
	{
		for(Block block : BLOCKS) event.getRegistry().register(block);
	}

	public static Block add(String name, Block block)
	{
		BLOCKS.add(block.setRegistryName(MRCore.ID, name));
		MRItems.add(name, new BlockItem(block, new Item.Properties().group(MRItems.TAB)));
		return block;
	}
}