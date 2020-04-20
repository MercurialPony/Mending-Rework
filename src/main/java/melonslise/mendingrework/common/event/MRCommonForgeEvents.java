package melonslise.mendingrework.common.event;

import java.util.Map.Entry;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.common.init.MREnchantments;
import melonslise.mendingrework.coremod.MRDelegates;
import melonslise.mendingrework.utility.MRUtilities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MRCore.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MRCommonForgeEvents
{
	private MRCommonForgeEvents() {}

	@SubscribeEvent
	public static void onPickupXp(PlayerXpEvent.PickupXp event)
	{
		event.setCanceled(true);
		PlayerEntity player = event.getPlayer();
		ExperienceOrbEntity orb = event.getOrb();
		player.xpCooldown = 2;
		player.onItemPickup(orb, 1);
		Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWithEnchantment(MREnchantments.RENEWAL, player);
		if (entry != null)
		{
			ItemStack stack = entry.getValue();
			if (!stack.isEmpty() && stack.isDamaged())
			{
				int i = Math.min((int) (orb.xpValue * stack.getXpRepairRatio()), stack.getDamage());
				orb.xpValue -= orb.durabilityToXp(i);
				stack.setDamage(stack.getDamage() - i);
			}
		}
		if (orb.xpValue > 0) player.giveExperiencePoints(orb.xpValue);
		orb.remove();
	}

	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event)
	{
		if(event.isCanceled()) return;
		MRDelegates.getAttributeModifiers(event.getSlot(), event.getFrom());
		MRDelegates.getAttributeModifiers(event.getSlot(), event.getTo());
	}

	@SubscribeEvent
	public static void breakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(event.isCanceled()) return;
		PlayerEntity player = event.getPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		if(!MRUtilities.hasRepairedDamage(stack)) return;
		event.setNewSpeed(event.getNewSpeed() + player.inventory.getDestroySpeed(event.getState()) * (float) MRUtilities.getRepairBonus(stack));
	}
}