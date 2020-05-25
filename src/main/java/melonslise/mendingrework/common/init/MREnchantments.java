package melonslise.mendingrework.common.init;

import java.util.List;

import com.google.common.collect.Lists;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.common.enchant.RenewalEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;

public final class MREnchantments
{
	private static final List<Enchantment> ENCHANTMENTS = Lists.newArrayList();

	public static final Enchantment RENEWAL = add("renewal", new RenewalEnchantment());

	private MREnchantments() {}

	public static void register(RegistryEvent.Register<Enchantment> event)
	{
		for(Enchantment enchantment : ENCHANTMENTS) event.getRegistry().register(enchantment);
	}

	private static Enchantment add(String name, Enchantment enchantment)
	{
		ENCHANTMENTS.add(enchantment.setRegistryName(MRCore.ID, name));
		return enchantment;
	}
}