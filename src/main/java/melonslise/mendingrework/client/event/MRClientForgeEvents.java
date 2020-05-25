package melonslise.mendingrework.client.event;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.utility.MRUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MRCore.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class MRClientForgeEvents
{
	@SubscribeEvent
	public static void onConstructTooltip(ItemTooltipEvent event)
	{
		if(event.isCanceled()) return;
		ItemStack stack = event.getItemStack();
		if(!MRUtil.hasRepairedDamage(stack)) return;
		event.getToolTip().add(new TranslationTextComponent("tooltip.mendingrework.repair_bonus", ItemStack.DECIMALFORMAT.format(MRUtil.getRepairBonus(stack) * 100d)).applyTextStyle(TextFormatting.DARK_AQUA));
	}
}