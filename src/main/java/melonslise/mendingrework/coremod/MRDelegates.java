package melonslise.mendingrework.coremod;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;

import melonslise.mendingrework.common.init.MREnchantments;
import melonslise.mendingrework.utility.MRUtilities;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public final class MRDelegates
{
	private MRDelegates() {}

	public static boolean isValidEnchantment(Enchantment enchantment)
	{
		return enchantment != MREnchantments.RENEWAL;
	}

	public static List<Enchantment> valid_enchantments;

	public static Enchantment getRandomEnchantment(Random random)
	{
		if(valid_enchantments == null) valid_enchantments = Registry.ENCHANTMENT.stream().filter(enchantment -> isValidEnchantment(enchantment)).collect(Collectors.toList());
		return valid_enchantments.get(random.nextInt(valid_enchantments.size()));
	}

	public static void onItemDamage(ItemStack stack, int damage)
	{
		int oldDamage = stack.getDamage();
		if(damage <= oldDamage || !MRUtilities.hasRepairedDamage(stack)) return;
		MRUtilities.addRepairedDamage(stack, damage - oldDamage);
	}

	public static Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> map = stack.getItem().getAttributeModifiers(slot, stack);
		if(slot != EquipmentSlotType.MAINHAND || !MRUtilities.hasRepairedDamage(stack)) return map;
		double multiplier = MRUtilities.getRepairBonus(stack);
		for(AttributeModifier am : map.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) am.amount += am.amount * multiplier;
		return map;
	}

	// FIXME Generalize (work not only for repairing with material)?
	public static ItemStack getRepairOutput(ItemStack input, ItemStack output)
	{
		if(!MRUtilities.hasRepairedDamage(output)) MRUtilities.setRepairedDamage(output, 0);
		else MRUtilities.addRepairedDamage(output, output.getDamage() - input.getDamage());
		return output;
	}

	public static int getNewRepairPenalty(int oldPenalty, ItemStack toRepair, ItemStack material)
	{
		if(EnchantmentHelper.getEnchantments(toRepair).containsKey(Enchantments.MENDING) && toRepair.isDamageable() && toRepair.getItem().getIsRepairable(toRepair, material)) return oldPenalty;
		return RepairContainer.getNewRepairCost(oldPenalty);
	}

	public static int getNewRepairCost(int oldCost, ItemStack toRepair, ItemStack material)
	{
		if(EnchantmentHelper.getEnchantments(toRepair).containsKey(Enchantments.MENDING) && toRepair.isDamageable() && toRepair.getItem().getIsRepairable(toRepair, material)) return 1;
		return oldCost;
	}
}