package melonslise.mendingrework.common.tileentity;

import java.util.function.Predicate;

import melonslise.mendingrework.common.config.MRConfig;
import melonslise.mendingrework.common.init.MREnchantments;
import melonslise.mendingrework.common.init.MRTileEntityTypes;
import melonslise.mendingrework.common.particle.GlyphParticleData;
import melonslise.mendingrework.utility.MRUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;


public class EnchantingAltarTileEntity extends TileEntity implements ITickableTileEntity
{
	public static final Predicate<BlockState>[][][] STRUCTURE = new Predicate[7][6][7];
	static
	{
		// FIXME Require air?
		Predicate<BlockState> quartz = state -> state.getBlock() == Blocks.SMOOTH_QUARTZ || state.getBlock() == Blocks.QUARTZ_BLOCK;

		STRUCTURE[3][0][2] = quartz;
		STRUCTURE[3][0][4] = quartz;
		STRUCTURE[2][0][3] = quartz;
		STRUCTURE[4][0][3] = quartz;
		//
		STRUCTURE[3][1][1] = quartz;
		STRUCTURE[1][1][3] = quartz;
		STRUCTURE[5][1][3] = quartz;
		STRUCTURE[3][1][5] = quartz;
		//
		STRUCTURE[3][2][3] = state -> state.getBlock() == Blocks.END_ROD && state.get(DirectionalBlock.FACING).getAxis().isVertical();
		STRUCTURE[3][2][0] = quartz;
		STRUCTURE[0][2][3] = quartz;
		STRUCTURE[6][2][3] = quartz;
		STRUCTURE[3][2][6] = quartz;
		//
		STRUCTURE[3][3][3] = state -> state.getBlock() == Blocks.DRAGON_EGG;
		STRUCTURE[3][3][0] = quartz;
		STRUCTURE[0][3][3] = quartz;
		STRUCTURE[6][3][3] = quartz;
		STRUCTURE[3][3][6] = quartz;

		STRUCTURE[3][3][1] = state -> state.getBlock() == Blocks.DRAGON_WALL_HEAD && state.get(WallSkullBlock.FACING) == Direction.SOUTH;
		STRUCTURE[1][3][3] = state -> state.getBlock() == Blocks.DRAGON_WALL_HEAD && state.get(WallSkullBlock.FACING) == Direction.EAST;
		STRUCTURE[5][3][3] = state -> state.getBlock() == Blocks.DRAGON_WALL_HEAD && state.get(WallSkullBlock.FACING) == Direction.WEST;
		STRUCTURE[3][3][5] = state -> state.getBlock() == Blocks.DRAGON_WALL_HEAD && state.get(WallSkullBlock.FACING) == Direction.NORTH;
		//
		STRUCTURE[3][4][1] = quartz;
		STRUCTURE[1][4][3] = quartz;
		STRUCTURE[5][4][3] = quartz;
		STRUCTURE[3][4][5] = quartz;
		//
		STRUCTURE[3][5][2] = quartz;
		STRUCTURE[3][5][4] = quartz;
		STRUCTURE[2][5][3] = quartz;
		STRUCTURE[4][5][3] = quartz;
	}

	private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(2)
	{
		@Override
		public boolean isItemValid(int slot, ItemStack stack)
		{
			return slot == 1 ? stack.getItem() == Items.NETHER_STAR : true;
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return 1;
		}

		@Override
		protected void onContentsChanged(int slot)
		{
			super.onContentsChanged(slot);
			EnchantingAltarTileEntity.this.sync();
		}
	});
	public int ritualTicks = 0, beamHeight = 0;
	public final int maxRitualTicks = 160, spiralDelay = 10;
	public BlockPos headNorth, headEast, headSouth, headWest;

	public EnchantingAltarTileEntity()
	{
		super(MRTileEntityTypes.ENCHANTING_ALTAR);
	}

	@Override
	public void setWorldAndPos(World world, BlockPos position)
	{
		super.setWorldAndPos(world, position);
		this.headNorth = this.pos.add(0, 2, -2);
		this.headEast = this.pos.add(2, 2, 0);
		this.headSouth = this.pos.add(0, 2, 2);
		this.headWest = this.pos.add(-2, 2, 0);
	}

	@Override
	public void remove()
	{
		super.remove();
		this.interruptRitual();
		if(!this.world.isRemote)
		{
			IItemHandler inventory = this.getInventory();
			for(int a = 0; a < inventory.getSlots(); ++a) InventoryHelper.spawnItemStack(this.world, (double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ(), inventory.getStackInSlot(a));
		}
	}

	public void sync()
	{
		this.markDirty();
		BlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}

	public int getSignalStrength()
	{
		ItemStack stack = this.getInventory().getStackInSlot(0);
		return stack.isEmpty() ? 0 : stack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantments(stack).containsKey(MREnchantments.RENEWAL) ? 2 : 1;
	}

	public ItemStackHandler getInventory()
	{
		return this.inventory.orElse(null);
	}

	public boolean checkStructure()
	{
		for(int x = 0; x < this.STRUCTURE.length; ++x) for(int y = 0; y < this.STRUCTURE[0].length; ++y) for(int z = 0; z < this.STRUCTURE[0][0].length; ++z)
		{
			BlockPos position = new BlockPos(x - 3 + this.pos.getX(), y - 1 + this.pos.getY(), z - 3 + this.pos.getZ());
			Predicate<BlockState> check = this.STRUCTURE[x][y][z];
			if(check != null && !check.test(world.getBlockState(position))) return false;
		}
		return true;
	}

	public boolean use(PlayerEntity player, Hand hand)
	{
		ItemStack heldStack = player.getHeldItem(hand);
		IItemHandler inventory = this.getInventory();
		if(inventory.getStackInSlot(0).isEmpty())
		{
			if(!inventory.getStackInSlot(1).isEmpty()) player.addItemStackToInventory(inventory.extractItem(1, 1, false));
			else if(!heldStack.isEmpty())
			{
				// FIXME!!!
				inventory.insertItem(0, heldStack.copy(), false);
				if(!player.isCreative()) heldStack.shrink(1);
			}
		}
		else if(inventory.getStackInSlot(1).isEmpty() && heldStack.getItem() == Items.NETHER_STAR)
		{
			// DITTO
			inventory.insertItem(1, heldStack.copy(), false);
			if(!player.isCreative()) heldStack.shrink(1);
		}
		else player.addItemStackToInventory(inventory.extractItem(0, 1, false));
		return false;
	}

	public void startRitual()
	{
		if(this.world.isRemote)
		{
			this.animateSkulls(true);
			this.updateBeam();
		}
		this.ritualTicks = this.maxRitualTicks;
	}

	public void endRitual()
	{
		if(this.world.isRemote)
		{
			this.animateSkulls(false);
			this.beamHeight = 0;
		}
		IItemHandler inventory = this.getInventory();
		inventory.extractItem(0, 1, false);
		inventory.extractItem(1, 1, false);
		inventory.insertItem(0, EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(MREnchantments.RENEWAL, 1)), false);
	}

	public void interruptRitual()
	{
		if(this.world.isRemote)
		{
			this.animateSkulls(false);
			this.beamHeight = 0;
		}
		this.ritualTicks = 0;
	}

	// FIXME Figure out the ticks and shit
	@Override
	public void tick()
	{
		IItemHandler inventory = this.getInventory();
		if(inventory.getStackInSlot(0).getItem() == Items.BOOK && inventory.getStackInSlot(1).getItem() == Items.NETHER_STAR && this.checkStructure())
		{
			if(this.ritualTicks > 0)
			{
				if(this.world.isRemote)
				{
					this.animateSkulls(true);
					this.particleTick();
					this.updateBeam();
				}
				--this.ritualTicks;
				if(this.ritualTicks == 0) this.endRitual();
			}
			else if(MRConfig.ENABLE_RENEWAL.get()) this.startRitual();
		}
		else if(this.ritualTicks > 0) this.interruptRitual();
	}

	public void particleTick()
	{
		if(this.ritualTicks % 10 == 0)
		{
			if(this.ritualTicks < this.maxRitualTicks - this.spiralDelay) for(int a = 1; a <= 4 && a <= (this.maxRitualTicks - this.ritualTicks - this.spiralDelay) / 20; ++a)
			{
				world.addParticle(new GlyphParticleData(Math.PI * (double) a, 0.3d, Math.PI / 45d, 60), this.pos.getX() + 0.5d, this.pos.getY() + 2d -  (double) (a - 1) / 5d, this.pos.getZ() + 0.5d, 0d, -0.003d, 0d);
				//world.addParticle(new ParticleDataGlyph(Math.PI * (double) a, 0.2d, -Math.PI / 45d, 60), this.pos.getX() + 0.5d, this.pos.getY() + 2d -  (double) (a - 1) / 5d, this.pos.getZ() + 0.5d, 0d, -0.003d, 0d);
			}
		}
		if(this.ritualTicks % 5 == 0)
		{
			for(int a = 1; a <= 4 && a <= (this.maxRitualTicks - this.ritualTicks) / 10; ++a)
			{
				double m = 0.00001d;
				world.addParticle(new GlyphParticleData(Math.PI * (double) a / 2d, 4d, 0.015d, 100), this.pos.getX() + 0.5d, this.pos.getY() + 1.5d, this.pos.getZ() + 0.5d, 0d, m, m / 2d);
				world.addParticle(new GlyphParticleData(Math.PI * (double) a / 2d, 4d, 0.015d, 100), this.pos.getX() + 0.5d, this.pos.getY() + 1.5d, this.pos.getZ() + 0.5d, 0d, m, -m / 2d);
				world.addParticle(new GlyphParticleData(Math.PI * (double) a / 2d, 4d, 0.015d, 100), this.pos.getX() + 0.5d, this.pos.getY() + 1.5d, this.pos.getZ() + 0.5d, m / 2d, m, 0);
				world.addParticle(new GlyphParticleData(Math.PI * (double) a / 2d, 4d, 0.015d, 100), this.pos.getX() + 0.5d, this.pos.getY() + 1.5d, this.pos.getZ() + 0.5d, -m / 2d, m, 0);
			}
			world.addParticle(new GlyphParticleData(0d, 0d, 0d, 40), this.headNorth.getX() + 0.5d, this.headNorth.getY() + 0.4d, this.headNorth.getZ() + 1d, 0d, 0d, 0.025d);
			world.addParticle(new GlyphParticleData(0d, 0d, 0d, 40), this.headEast.getX(), this.headEast.getY() + 0.4d, this.headEast.getZ() + 0.5d, -0.025d, 0d, 0d);
			world.addParticle(new GlyphParticleData(0d, 0d, 0d, 40), this.headSouth.getX() + 0.5d, this.headSouth.getY() + 0.4d, this.headSouth.getZ(), 0d, 0d, -0.025d);
			world.addParticle(new GlyphParticleData(0d, 0d, 0d, 40), this.headWest.getX() + 1d, this.headWest.getY() + 0.4d, this.headWest.getZ() + 0.5d, 0.025d, 0d, 0d);
		}
	}

	public void updateBeam()
	{
		this.beamHeight = 0;
		BlockPos position = this.pos.up();
		BlockState state = this.world.getBlockState(position);
		int highest = this.world.getHeight(Heightmap.Type.WORLD_SURFACE, this.pos.getX(), this.pos.getZ()) - 1;
		while(state.getOpacity(this.world, position) < 15 && position.getY() < highest)
		{
			++this.beamHeight;
			position = position.up();
			state = this.world.getBlockState(position);
		}
		if(position.getY() == highest && state.getOpacity(this.world, position) < 15) this.beamHeight = 1000;
	}

	public void animateSkulls(boolean animate)
	{
		MRUtil.animateSkull(this.world, this.headNorth, animate);
		MRUtil.animateSkull(this.world, this.headEast, animate);
		MRUtil.animateSkull(this.world, this.headSouth, animate);
		MRUtil.animateSkull(this.world, this.headWest, animate);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.inventory.cast() : super.getCapability(capability, side);
	}

	public static final String KEY_INVENTORY = "Inventory", KEY_RITUAL_TICKS = "RitualTicks";

	public CompoundNBT writeData(CompoundNBT nbt)
	{
		nbt.put(KEY_INVENTORY, this.getInventory().serializeNBT());
		nbt.putByte(KEY_RITUAL_TICKS, (byte) this.ritualTicks);
		return nbt;
	}

	public void readData(CompoundNBT nbt)
	{
		this.getInventory().deserializeNBT(nbt.getCompound(KEY_INVENTORY));
		this.ritualTicks = (int) nbt.getByte(KEY_RITUAL_TICKS);
	}

	/*
	 * if the only thing in your TE is one itemstack, you can use the same NBT reading and writing methods for all three things and call markDirty and notifyBlockUpdate whenever you change it
	 * for more complicated things, you'd want to set up a scheme where getUpdateTag sends the client everything that a client needs to know about the TE, and getUpdatePacket sends whatever things have changed since the last time you called getUpdatePacket
	 * -- Commoble
	 */

	// getUpdatePacket gets called when world.notifyBlockUpdate gets called on that block
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 13, this.writeData(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet)
	{
		this.readData(packet.getNbtCompound());
	}

	// getUpdateTag gets called when the client loads the chunk with the TE in it
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT nbt)
	{
		this.read(nbt);
	}

	@Override
	public void read(CompoundNBT nbt)
	{
		super.read(nbt);
		this.readData(nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt)
	{
		super.write(nbt);
		this.writeData(nbt);
		return nbt;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536d;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
}