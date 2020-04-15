package melonslise.mendingrework.common.item;

import melonslise.mendingrework.common.init.MRItemGroups;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class MRItemBlock extends BlockItem
{
	public MRItemBlock(Block block, Item.Properties properties)
	{
		super(block, properties.group(MRItemGroups.MAIN));
		this.setRegistryName(block.getRegistryName());
	}
}