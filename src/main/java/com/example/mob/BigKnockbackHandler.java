package com.example.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.Vec3d;

public class BigKnockbackHandler {

	private static final double GRAVITY = 0.08;
	// Idealized (no-drag) 45-degree launch speed for a target range is sqrt(range * gravity).
	// Real Minecraft entities lose speed to air drag every tick, so the actual landing distance
	// will fall short of the target on flat ground - this factor is a rough compensation, not
	// an exact solve, and will need tuning based on how it plays in-game.
	private static final double DRAG_COMPENSATION = 1.3;

	public static boolean onAllowDamage(LivingEntity entity, DamageSource source, float amount) {
		double range = getRangeFor(entity);
		if (range <= 0.0) {
			return true;
		}

		Entity attacker = source.getAttacker();
		if (attacker == null) {
			return true;
		}

		Vec3d direction = entity.getPos().subtract(attacker.getPos()).multiply(1, 0, 1);
		if (direction.lengthSquared() < 1.0E-4) {
			direction = new Vec3d(1, 0, 0);
		} else {
			direction = direction.normalize();
		}

		double launchSpeed = Math.sqrt(range * GRAVITY) * DRAG_COMPENSATION;
		double horizontal = launchSpeed / Math.sqrt(2.0);
		double vertical = launchSpeed / Math.sqrt(2.0);

		entity.setVelocity(direction.x * horizontal, vertical, direction.z * horizontal);
		entity.velocityModified = true;

		return true;
	}

	private static double getRangeFor(LivingEntity entity) {
		if (entity instanceof VillagerEntity) {
			return 30.0;
		}
		if (entity instanceof ChickenEntity) {
			return 35.0;
		}
		if (entity instanceof SlimeEntity) {
			return 50.0;
		}
		return 0.0;
	}
}
