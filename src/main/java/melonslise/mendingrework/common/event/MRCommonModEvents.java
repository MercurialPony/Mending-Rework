package melonslise.mendingrework.common.event;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.common.init.MRBlocks;
import melonslise.mendingrework.common.init.MREnchantments;
import melonslise.mendingrework.common.init.MRItems;
import melonslise.mendingrework.common.init.MRParticleTypes;
import melonslise.mendingrework.common.init.MRTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MRCore.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MRCommonModEvents
{
	private MRCommonModEvents() {}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		MRBlocks.register(event);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		MRItems.register(event);
	}

	@SubscribeEvent
	public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event)
	{
		MRTileEntityTypes.register(event);
	}

	@SubscribeEvent
	public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event)
	{
		MRParticleTypes.register(event);
	}

	@SubscribeEvent
	public static void registerEnchantments(RegistryEvent.Register<Enchantment> event)
	{
		MREnchantments.register(event);
	}
}