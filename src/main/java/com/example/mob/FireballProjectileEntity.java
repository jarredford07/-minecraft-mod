package com.example.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireballProjectileEntity extends ThrownItemEntity {

	public FireballProjectileEntity(EntityType<? extends FireballProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public FireballProjectileEntity(World world, LivingEntity owner) {
		super(
			CustomMobMod.FIREBALL_PROJECTILE,
			owner.getX() + owner.getRotationVector().x,
			owner.getEyeY() - 0.1 + owner.getRotationVector().y * 0.5,
			owner.getZ() + owner.getRotationVector().z,
			world
		);
		this.setOwner(owner);
	}

	@Override
	protected Item getDefaultItem() {
		return CustomMobMod.FIREBALL;
	}

	@Override
	protected void onEntityHit(EntityHitResult hitResult) {
		super.onEntityHit(hitResult);
		Entity hitEntity = hitResult.getEntity();
		hitEntity.setOnFireFor(8);
		if (hitEntity instanceof LivingEntity livingEntity && !getWorld().isClient()) {
			livingEntity.damage(getWorld().getDamageSources().thrown(this, getOwner()), 2.0f);
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult hitResult) {
		super.onBlockHit(hitResult);
		if (!getWorld().isClient()) {
			BlockPos placePos = hitResult.getBlockPos().offset(hitResult.getSide());
			if (getWorld().isAir(placePos)) {
				getWorld().setBlockState(placePos, net.minecraft.block.Blocks.FIRE.getDefaultState());
			}
		}
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (!getWorld().isClient()) {
			getWorld().sendEntityStatus(this, (byte) 3);
			this.discard();
		}
	}
}
