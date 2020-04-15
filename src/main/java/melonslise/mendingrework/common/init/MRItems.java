package melonslise.mendingrework.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import melonslise.mendingrework.common.item.MRItemBlock;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public final class MRItems
{
	private static final List<Item> ITEMS = Lists.newArrayList();

	public static final Item ENCHANTING_ALTAR = add(new MRItemBlock(MRBlocks.ENCHANTING_ALTAR, new Item.Properties()));

	private MRItems() {}

	public static void register(RegistryEvent.Register<Item> event)
	{
		for(Item item : ITEMS) event.getRegistry().register(item);
	}

	private static Item add(Item item)
	{
		ITEMS.add(item);
		return item;
	}
}