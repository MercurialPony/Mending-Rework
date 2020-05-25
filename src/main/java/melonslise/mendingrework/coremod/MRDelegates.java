package melonslise.mendingrework.coremod;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;

import melonslise.mendingrework.common.config.MRConfig;
import melonslise.mendingrework.common.init.MREnchantments;
import melonslise.mendingrework.utility.MRUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
		if(valid_enchantments == null)
			valid_enchantments = Registry.ENCHANTMENT.stream().filter(enchantment -> isValidEnchantment(enchantment)).collect(Collectors.toList());
		return valid_enchantments.get(random.nextInt(valid_enchantments.size()));
	}

	public static void onItemDamage(ItemStack stack, int damage)
	{
		int oldDamage = stack.getDamage();
		if(damage <= oldDamage || !MRUtil.hasRepairedDamage(stack))
			return;
		MRUtil.addRepairedDamage(stack, damage - oldDamage);
	}

	public static Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> map = stack.getItem().getAttributeModifiers(slot, stack);
		if(slot != EquipmentSlotType.MAINHAND || !MRUtil.hasRepairedDamage(stack))
			return map;
		double multiplier = MRUtil.getRepairBonus(stack);
		for(AttributeModifier am : map.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
			am.amount += am.amount * multiplier;
		return map;
	}

	// FIXME Generalize (work not only for repairing with material)?
	public static ItemStack getRepairOutput(ItemStack input, ItemStack output)
	{
		if(!MRUtil.hasRepairedDamage(output))
			MRUtil.setRepairedDamage(output, 0);
		else
			MRUtil.addRepairedDamage(output, output.getDamage() - input.getDamage());
		return output;
	}

	public static int getNewRepairPenalty(int oldPenalty, ItemStack left, ItemStack right)
	{
		if(EnchantmentHelper.getEnchantments(left).containsKey(Enchantments.MENDING) && left.isDamageable() && left.getItem().getIsRepairable(left, right))
			return oldPenalty;
		return RepairContainer.getNewRepairCost(oldPenalty);
	}

	public static int getNewRepairCost(int oldCost, ItemStack left, ItemStack right)
	{
		Map<Enchantment, Integer> rightEnchs = EnchantmentHelper.getEnchantments(right);
		if((left.getItem() != Items.ENCHANTED_BOOK || right.getItem() != Items.ENCHANTED_BOOK) && (rightEnchs.containsKey(Enchantments.MENDING) || rightEnchs.containsKey(MREnchantments.RENEWAL)))
			return 30;
		if(EnchantmentHelper.getEnchantments(left).containsKey(Enchantments.MENDING) && left.isDamageable() && left.getItem().getIsRepairable(left, right))
			return 1;
		return oldCost;
	}

	public static int getMaxRepairCost(RepairContainer cont)
	{
		Map<Enchantment, Integer> matEnch = EnchantmentHelper.getEnchantments(cont.getSlot(1).getStack());
		if(matEnch.containsKey(Enchantments.MENDING) || matEnch.containsKey(MREnchantments.RENEWAL))
			return 10000;
		return 40;
	}

	public static boolean canApplyWith(Enchantment rightEnch, Enchantment leftEnch, ItemStack right, ItemStack left)
	{
		if(right.getItem() == Items.ENCHANTED_BOOK && left.getItem() == Items.ENCHANTED_BOOK && (rightEnch == Enchantments.MENDING || rightEnch == MREnchantments.RENEWAL || leftEnch == Enchantments.MENDING || leftEnch == MREnchantments.RENEWAL))
			return false;
		return rightEnch.isCompatibleWith(leftEnch);
	}

	public static boolean cannotApplyInfinity(Enchantment with)
	{
		return !MRConfig.MENDING_WITH_INFINITY.get() && with == Enchantments.MENDING;
	}
}