package melonslise.mendingrework.common.enchant;

import melonslise.mendingrework.MRCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class MREnchantment extends Enchantment
{
	public MREnchantment(String name, Rarity rarity, EnchantmentType type, EquipmentSlotType[] slots)
	{
		super(rarity, type, slots);
		this.setRegistryName(MRCore.ID, name);
	}
}