package melonslise.mendingrework.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import melonslise.mendingrework.MRCore;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public final class MRItems
{
	public static final List<Item> ITEMS = Lists.newArrayList();

	public static final ItemGroup TAB = new ItemGroup(MRCore.ID)
	{
		@OnlyIn(Dist.CLIENT)
		@Override
		public ItemStack createIcon()
		{
			return EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(MREnchantments.RENEWAL, 1));
		}

		// FIXME remove enchantment from other tabs?
		@OnlyIn(Dist.CLIENT)
		public void fill(NonNullList<ItemStack> items)
		{
			super.fill(items);
			items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(MREnchantments.RENEWAL, 1)));
		}
	};

	private MRItems() {}

	public static void register(RegistryEvent.Register<Item> event)
	{
		for(Item item : ITEMS) event.getRegistry().register(item);
	}

	public static Item add(String name, Item item)
	{
		ITEMS.add(item.setRegistryName(MRCore.ID, name));
		return item;
	}
}