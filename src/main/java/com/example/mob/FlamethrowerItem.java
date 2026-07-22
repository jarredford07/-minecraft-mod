package com.example.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FlamethrowerItem extends Item {

	private static final double RANGE = 8.0;
	private static final double CONE_ANGLE_COS = 0.85;

	public FlamethrowerItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.setCurrentHand(hand);
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (world.isClient() || !(user instanceof PlayerEntity player)) {
			return;
		}

		ServerWorld serverWorld = (ServerWorld) world;
		Vec3d eyePos = user.getEyePos();
		Vec3d look = user.getRotationVec(1.0f);

		for (int i = 1; i <= (int) RANGE; i++) {
			Vec3d point = eyePos.add(look.multiply(i));
			serverWorld.spawnParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 2, 0.1, 0.1, 0.1, 0.01);
		}

		List<Entity> hit = world.getOtherEntities(user, user.getBoundingBox().expand(RANGE), entity -> {
			if (!(entity instanceof LivingEntity) || entity == user) {
				return false;
			}
			Vec3d toEntity = entity.getPos().subtract(eyePos);
			double distance = toEntity.length();
			if (distance > RANGE || distance < 0.001) {
				return false;
			}
			return toEntity.normalize().dotProduct(look) > CONE_ANGLE_COS;
		});

		for (Entity entity : hit) {
			entity.setOnFireFor(4);
			if (entity instanceof LivingEntity livingEntity) {
				livingEntity.damage(world.getDamageSources().playerAttack(player), 1.0f);
			}
		}

		if (remainingUseTicks % 10 == 0) {
			stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
		}
	}
}
