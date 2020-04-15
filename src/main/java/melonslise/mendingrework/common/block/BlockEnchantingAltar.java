package melonslise.mendingrework.common.block;

import melonslise.mendingrework.common.init.MRTileEntityTypes;
import melonslise.mendingrework.common.tileentity.TileEntityEnchantingAltar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockEnchantingAltar extends MRBlock
{
	public static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(2d, 0d, 2d, 14d, 4d, 14d), Block.makeCuboidShape(4d, 4d, 4d, 12d, 7d, 12d), Block.makeCuboidShape(5d, 7d, 5d, 11d, 13d, 11d), Block.makeCuboidShape(4d, 13d, 4d, 12d, 15d, 12d), Block.makeCuboidShape(6d, 15d, 6d, 10d, 16d, 10d));

	public BlockEnchantingAltar()
	{
		super("enchanting_altar", Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(4f, 8f));
	}

	// FIXME getRayTraceShape??
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos position, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos position, PathType type)
	{
		return false;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace)
	{
		((TileEntityEnchantingAltar) world.getTileEntity(position)).use(player, hand);
		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos position)
	{
		return ((TileEntityEnchantingAltar) world.getTileEntity(position)).getSignalStrength();
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return MRTileEntityTypes.ENCHANTING_ALTAR.create();
	}
}