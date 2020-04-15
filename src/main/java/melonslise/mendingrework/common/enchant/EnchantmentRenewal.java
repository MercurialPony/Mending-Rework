package melonslise.mendingrework.common.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantmentRenewal extends MREnchantment
{
	public EnchantmentRenewal()
	{
		super("renewal", Enchantment.Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values());
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 25;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return this.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean isTreasureEnchantment()
	{
		return true;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}
}