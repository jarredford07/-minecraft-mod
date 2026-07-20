package com.example.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CarEntity extends Entity {

	private boolean inputForward;
	private boolean inputBack;
	private boolean inputLeft;
	private boolean inputRight;

	public CarEntity(EntityType<? extends CarEntity> entityType, World world) {
		super(entityType, world);
	}

	public void setInputs(boolean forward, boolean back, boolean left, boolean right) {
		this.inputForward = forward;
		this.inputBack = back;
		this.inputLeft = left;
		this.inputRight = right;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.getWorld().isClient()) {
			if (this.inputLeft) {
				this.setYaw(this.getYaw() - 3.0f);
			}
			if (this.inputRight) {
				this.setYaw(this.getYaw() + 3.0f);
			}

			double throttle = 0.0;
			if (this.inputForward) {
				throttle = 0.4;
			} else if (this.inputBack) {
				throttle = -0.25;
			}

			float yawRad = (float) Math.toRadians(this.getYaw());
			double motionX = -Math.sin(yawRad) * throttle;
			double motionZ = Math.cos(yawRad) * throttle;

			Vec3d velocity = this.getVelocity();
			this.setVelocity(
				velocity.x * 0.9 + motionX * 0.2,
				velocity.y,
				velocity.z * 0.9 + motionZ * 0.2
			);

			Entity passenger = this.getFirstPassenger();
			if (passenger != null && passenger.isSneaking()) {
				passenger.stopRiding();
			}
		}

		Vec3d velocity = this.getVelocity();
		this.move(MovementType.SELF, velocity);

		if (!this.isOnGround()) {
			this.setVelocity(velocity.x, velocity.y - 0.08, velocity.z);
		} else {
			this.setVelocity(velocity.x * 0.9, 0.0, velocity.z * 0.9);
		}
	}

	@Override
	public LivingEntity getControllingPassenger() {
		Entity passenger = this.getFirstPassenger();
		return passenger instanceof LivingEntity livingEntity ? livingEntity : null;
	}

	@Override
	public double getMountedHeightOffset() {
		return 0.55;
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
	}
}
