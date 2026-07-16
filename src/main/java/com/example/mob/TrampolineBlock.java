package com.example.mob;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TrampolineBlock extends Block {

	public TrampolineBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (entity.isSneaking()) {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
			return;
		}

		Vec3d velocity = entity.getVelocity();
		double launch = Math.max(1.2, -velocity.y * 1.6);
		entity.setVelocity(velocity.x, launch, velocity.z);
		entity.velocityModified = true;
		entity.fallDistance = 0.0f;
	}
}
