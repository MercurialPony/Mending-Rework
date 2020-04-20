package melonslise.mendingrework.client.event;

import melonslise.mendingrework.MRCore;
import melonslise.mendingrework.client.particle.ParticleGlyph;
import melonslise.mendingrework.client.renderer.RendererTileEntityEnchantingAltar;
import melonslise.mendingrework.common.init.MRBlocks;
import melonslise.mendingrework.common.init.MRParticleTypes;
import melonslise.mendingrework.common.init.MRTileEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MRCore.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MRClientModEvents
{
	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(MRBlocks.ENCHANTING_ALTAR, RenderType.getCutout());
		ClientRegistry.bindTileEntityRenderer(MRTileEntityTypes.ENCHANTING_ALTAR, RendererTileEntityEnchantingAltar::new);
	}

	@SubscribeEvent
	public static void registerParticleFactories(ParticleFactoryRegisterEvent event)
	{
		Minecraft.getInstance().particles.registerFactory(MRParticleTypes.GLYPH, ParticleGlyph.Factory::new);
	}
}