package melonslise.mendingrework.common.event;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.coremod.MRDelegates;
import melonslise.mendingrework.utility.MRUtilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MRCore.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MRCommonForgeEvents
{
	private MRCommonForgeEvents() {}

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