package melonslise.mendingrework.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import melonslise.mendingrework.common.enchant.EnchantmentRenewal;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;

public final class MREnchantments
{
	private static final List<Enchantment> ENCHANTMENTS = Lists.newArrayList();

	public static final Enchantment RENEWAL = add(new EnchantmentRenewal());

	private MREnchantments() {}

	public static void register(RegistryEvent.Register<Enchantment> event)
	{
		for(Enchantment enchantment : ENCHANTMENTS) event.getRegistry().register(enchantment);
	}

	private static Enchantment add(Enchantment enchantment)
	{
		ENCHANTMENTS.add(enchantment);
		return enchantment;
	}
}