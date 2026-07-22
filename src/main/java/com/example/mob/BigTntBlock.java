package com.example.mob;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

public class BigTntBlock extends Block {

	public BigTntBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(TntBlock.UNSTABLE, false));
	}

	private static void primeBigTnt(World world, BlockPos pos) {
		primeBigTnt(world, pos, null);
	}

	private static void primeBigTnt(World world, BlockPos pos, LivingEntity causingEntity) {
		if (world.isClient()) {
			return;
		}

		BigTntEntity tnt = new BigTntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, causingEntity);
		world.spawnEntity(tnt);
		world.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
		world.emitGameEvent(causingEntity, GameEvent.PRIME_FUSE, pos);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (oldState.isOf(state.getBlock())) {
			return;
		}
		if (world.isReceivingRedstonePower(pos)) {
			primeBigTnt(world, pos);
			world.removeBlock(pos, false);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (world.isReceivingRedstonePower(pos)) {
			primeBigTnt(world, pos);
			world.removeBlock(pos, false);
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient() && !player.isCreative() && state.get(TntBlock.UNSTABLE)) {
			primeBigTnt(world, pos);
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		if (world.isClient()) {
			return;
		}
		BigTntEntity tnt = new BigTntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosion.getCausingEntity());
		int fuse = tnt.getFuse();
		tnt.setFuse((short) (world.random.nextInt(fuse / 4) + fuse / 8));
		world.spawnEntity(tnt);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isOf(Items.FLINT_AND_STEEL) || stack.isOf(Items.FIRE_CHARGE)) {
			primeBigTnt(world, pos, player);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

			if (!player.isCreative()) {
				if (stack.isOf(Items.FLINT_AND_STEEL)) {
					stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
				} else {
					stack.decrement(1);
				}
			}

			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			return ActionResult.success(world.isClient());
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (world.isClient()) {
			return;
		}
		BlockPos pos = hit.getBlockPos();
		if (projectile.isOnFire() && projectile.canModifyAt(world, pos)) {
			LivingEntity owner = projectile.getOwner() instanceof LivingEntity livingEntity ? livingEntity : null;
			primeBigTnt(world, pos, owner);
			world.removeBlock(pos, false);
		}
	}

	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}

	@Override
	protected void appendProperties(net.minecraft.state.StateManager.Builder<Block, BlockState> builder) {
		builder.add(TntBlock.UNSTABLE);
	}
}
