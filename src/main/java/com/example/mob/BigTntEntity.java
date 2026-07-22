package com.example.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BigTntEntity extends Entity {

	private static final TrackedData<Integer> FUSE =
		DataTracker.registerData(BigTntEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private static final float EXPLOSION_POWER = 60.0f;

	private LivingEntity causingEntity;

	public BigTntEntity(EntityType<? extends BigTntEntity> entityType, World world) {
		super(entityType, world);
		this.intersectionChecked = true;
	}

	public BigTntEntity(World world, double x, double y, double z, LivingEntity causingEntity) {
		this(CustomMobMod.BIG_TNT_ENTITY, world);
		this.setPosition(x, y, z);
		double angle = world.getRandom().nextDouble() * Math.PI * 2.0;
		this.setVelocity(-Math.sin(angle) * 0.02, 0.2, -Math.cos(angle) * 0.02);
		this.setFuse(50);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.causingEntity = causingEntity;
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(FUSE, 50);
	}

	@Override
	protected Entity.MoveEffect getMoveEffect() {
		return Entity.MoveEffect.NONE;
	}

	@Override
	public boolean canHit() {
		return !this.isRemoved();
	}

	@Override
	public void tick() {
		if (!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
		}

		this.move(MovementType.SELF, this.getVelocity());
		this.setVelocity(this.getVelocity().multiply(0.98));

		if (this.isOnGround()) {
			this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
		}

		int fuse = this.getFuse() - 1;
		this.setFuse(fuse);

		if (fuse <= 0) {
			this.discard();
			if (!this.getWorld().isClient()) {
				this.explode();
			}
		} else {
			this.updateWaterState();
			if (this.getWorld().isClient()) {
				this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
			}
		}
	}

	private void explode() {
		this.getWorld().createExplosion(
			this,
			this.getX(),
			this.getBodyY(0.0625),
			this.getZ(),
			EXPLOSION_POWER,
			World.ExplosionSourceType.TNT
		);
	}

	public void setFuse(int fuse) {
		this.dataTracker.set(FUSE, fuse);
	}

	public int getFuse() {
		return this.dataTracker.get(FUSE);
	}

	public LivingEntity getCausingEntity() {
		return this.causingEntity;
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		this.setFuse(nbt.getShort("Fuse"));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putShort("Fuse", (short) this.getFuse());
	}
}
