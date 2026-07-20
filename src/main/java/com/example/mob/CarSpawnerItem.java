package com.example.mob;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CarSpawnerItem extends Item {

	public CarSpawnerItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()) {
			CarEntity car = new CarEntity(CustomMobMod.CAR, world);
			Vec3d pos = user.getPos().add(user.getRotationVector().multiply(2.5));
			car.refreshPositionAndAngles(pos.x, pos.y, pos.z, user.getYaw(), 0.0f);
			world.spawnEntity(car);
		}
		return TypedActionResult.success(user.getStackInHand(hand));
	}
}
