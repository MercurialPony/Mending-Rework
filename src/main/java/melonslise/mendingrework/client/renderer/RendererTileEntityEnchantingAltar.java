package melonslise.mendingrework.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import melonslise.mendingrework.common.tileentity.TileEntityEnchantingAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class RendererTileEntityEnchantingAltar extends TileEntityRenderer<TileEntityEnchantingAltar>
{
	public RendererTileEntityEnchantingAltar(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(TileEntityEnchantingAltar altar, float partialTick, MatrixStack matrixStack,IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		long time = altar.getWorld().getGameTime();
		IItemHandler inventory = altar.getInventory();
		ItemStack stack = inventory.getStackInSlot(0);
		if(!stack.isEmpty())
		{
			float f = ((float) time + partialTick) / 10f;
			matrixStack.push();
			matrixStack.translate(0.5d, 1.25d + (double) MathHelper.sin(f) / 15d, 0.5d);
			matrixStack.rotate(Vector3f.YP.rotation(f / 1.3f));
			matrixStack.scale(0.375f, 0.375f, 0.375f);
			Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
			matrixStack.pop();
		}
		ItemStack starStack = inventory.getStackInSlot(1);
		if(!starStack.isEmpty())
		{
			matrixStack.push();
			matrixStack.translate(0.509f, 1.01f, 0.509f);
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90f));
			matrixStack.scale(0.375f, 0.375f, 0.375f);
			Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(Items.NETHER_STAR), TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
			matrixStack.pop();
		}
		if(altar.beamHeight > 0)
		{
			matrixStack.push();
			matrixStack.translate(0d, 1d, 0d);
			BeaconTileEntityRenderer.renderBeamSegment(matrixStack, buffer, BeaconTileEntityRenderer.TEXTURE_BEACON_BEAM, partialTick, 1f, time, 0, altar.beamHeight, new float[] { 1f, 1f, 1f }, 0.2f, 0.25f);
			matrixStack.pop();
		}
	}

	@Override
	public boolean isGlobalRenderer(TileEntityEnchantingAltar altar)
	{
		return true;
	}
}