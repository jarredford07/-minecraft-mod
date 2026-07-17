package com.example.mob;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class BounceTowardPlayerGoal extends Goal {

	private final MobEntity mob;
	private final double followRange;
	private final double hopStrength;
	private PlayerEntity target;
	private int cooldown;

	public BounceTowardPlayerGoal(MobEntity mob, double followRange, double hopStrength) {
		this.mob = mob;
		this.followRange = followRange;
		this.hopStrength = hopStrength;
		this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.LOOK));
	}

	@Override
	public boolean canStart() {
		target = mob.getWorld().getClosestPlayer(mob, followRange);
		return target != null && !target.isSpectator() && mob.squaredDistanceTo(target) > 4.0;
	}

	@Override
	public boolean shouldContinue() {
		return target != null && target.isAlive() && !target.isSpectator()
			&& mob.squaredDistanceTo(target) > 4.0 && mob.squaredDistanceTo(target) < followRange * followRange * 4;
	}

	@Override
	public void stop() {
		target = null;
	}

	@Override
	public void tick() {
		if (target == null) {
			return;
		}

		mob.getLookControl().lookAt(target, 30.0f, 30.0f);

		if (cooldown > 0) {
			cooldown--;
			return;
		}

		if (!mob.isOnGround()) {
			return;
		}

		Vec3d toTarget = target.getPos().subtract(mob.getPos()).multiply(1, 0, 1);
		if (toTarget.lengthSquared() < 1.0E-4) {
			return;
		}
		Vec3d direction = toTarget.normalize();

		mob.setVelocity(direction.x * hopStrength, 0.42, direction.z * hopStrength);
		mob.velocityModified = true;
		cooldown = 10;
	}
}
