package com.example.mob;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class MultiplierBlockEntity extends BlockEntity {

	public static final int MAX_MULTIPLIER = 1000;
	private static final int MAX_MOB_SPAWNS = 100;
	private static final String PROCESSED_TAG = "custommob_multiplied";

	private int multiplier = 2;

	public MultiplierBlockEntity(BlockPos pos, BlockState state) {
		super(CustomMobMod.MULTIPLIER_BLOCK_ENTITY, pos, state);
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int value) {
		this.multiplier = Math.max(1, Math.min(MAX_MULTIPLIER, value));
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("Multiplier", multiplier);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("Multiplier")) {
			multiplier = nbt.getInt("Multiplier");
		}
	}

	public static void tick(World world, BlockPos pos, BlockState state, MultiplierBlockEntity entity) {
		if (world.isClient() || !(world instanceof ServerWorld serverWorld)) {
			return;
		}

		int multiplier = entity.multiplier;
		if (multiplier <= 1) {
			return;
		}

		Box box = new Box(pos).expand(0.15);

		for (Entity target : world.getOtherEntities(null, box, other ->
			(other instanceof ItemEntity || (other instanceof LivingEntity && !(other instanceof PlayerEntity)))
				&& !other.getCommandTags().contains(PROCESSED_TAG))) {

			if (target instanceof ItemEntity itemEntity) {
				ItemStack stack = itemEntity.getStack();
				long newCount = (long) stack.getCount() * multiplier;
				stack.setCount((int) Math.min(Integer.MAX_VALUE, newCount));
				itemEntity.addCommandTag(PROCESSED_TAG);
				itemEntity.setVelocity(itemEntity.getVelocity().x, 0.4, itemEntity.getVelocity().z);
				itemEntity.velocityModified = true;
			} else if (target instanceof LivingEntity living) {
				int spawnCount = Math.min(multiplier - 1, MAX_MOB_SPAWNS);
				for (int i = 0; i < spawnCount; i++) {
					Entity clone = living.getType().create(world);
					if (clone == null) {
						continue;
					}
					double angle = (2 * Math.PI / spawnCount) * i;
					double ox = Math.cos(angle) * 1.5;
					double oz = Math.sin(angle) * 1.5;
					clone.refreshPositionAndAngles(
						living.getX() + ox,
						living.getY() + 0.2,
						living.getZ() + oz,
						living.getYaw(),
						living.getPitch()
					);
					clone.addCommandTag(PROCESSED_TAG);
					serverWorld.spawnEntity(clone);
				}
				living.addCommandTag(PROCESSED_TAG);
				living.setVelocity(living.getVelocity().x, 0.4, living.getVelocity().z);
				living.velocityModified = true;
			}
		}
	}
}
