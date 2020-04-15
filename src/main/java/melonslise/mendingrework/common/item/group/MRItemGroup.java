package melonslise.mendingrework.common.item.group;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.common.init.MREnchantments;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MRItemGroup extends ItemGroup
{
	public MRItemGroup()
	{
		super(MRCore.ID);
	}

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
}