package melonslise.mendingrework.common.enchant;

import melonslise.mendingrework.common.config.MRConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class RenewalEnchantment extends Enchantment
{
	public RenewalEnchantment()
	{
		super(Enchantment.Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values());
	}

	@Override
	public int getMinEnchantability(int lvl)
	{
		return lvl * 25;
	}

	@Override
	public int getMaxEnchantability(int lvl)
	{
		return this.getMinEnchantability(lvl) + 50;
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

	@Override
	public boolean canApplyTogether(Enchantment ench)
	{
		if(!MRConfig.RENEWAL_WITH_INFINITY.get() && ench == Enchantments.INFINITY)
			return false;
		if(!MRConfig.RENEWAL_WITH_MENDING.get() && ench == Enchantments.MENDING)
			return false;
		return super.canApplyTogether(ench);
	}
}