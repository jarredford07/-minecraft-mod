package com.example.mob;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class MinecartBlockHandler {

	public static ActionResult onUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
		if (world.isClient() || hand != Hand.MAIN_HAND || !(entity instanceof AbstractMinecartEntity minecart)) {
			return ActionResult.PASS;
		}

		DisplayEntity.BlockDisplayEntity existingDisplay = null;
		for (Entity passenger : minecart.getPassengerList()) {
			if (passenger instanceof DisplayEntity.BlockDisplayEntity blockDisplay) {
				existingDisplay = blockDisplay;
				break;
			}
		}

		ItemStack heldStack = player.getStackInHand(hand);

		if (heldStack.getItem() instanceof BlockItem blockItem) {
			if (existingDisplay != null) {
				return ActionResult.PASS;
			}

			BlockState state = blockItem.getBlock().getDefaultState();
			DisplayEntity.BlockDisplayEntity display = EntityType.BLOCK_DISPLAY.create(world);
			if (display == null) {
				return ActionResult.PASS;
			}

			NbtCompound nbt = new NbtCompound();
			nbt.put("block_state", NbtHelper.fromBlockState(state));
			display.readNbt(nbt);
			display.refreshPositionAndAngles(minecart.getX(), minecart.getY(), minecart.getZ(), 0.0f, 0.0f);

			world.spawnEntity(display);
			display.startRiding(minecart);

			if (!player.getAbilities().creativeMode) {
				heldStack.decrement(1);
			}

			return ActionResult.SUCCESS;
		}

		if (heldStack.isEmpty() && existingDisplay != null) {
			Item item = getItemFor(existingDisplay);
			existingDisplay.discard();

			if (item != null && !player.getAbilities().creativeMode) {
				player.giveItemStack(new ItemStack(item));
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	private static Item getItemFor(DisplayEntity.BlockDisplayEntity display) {
		NbtCompound nbt = display.writeNbt(new NbtCompound());
		if (!nbt.contains("block_state")) {
			return null;
		}
		NbtCompound blockStateNbt = nbt.getCompound("block_state");
		if (!blockStateNbt.contains("Name")) {
			return null;
		}
		Identifier id = Identifier.tryParse(blockStateNbt.getString("Name"));
		if (id == null) {
			return null;
		}
		Block block = Registries.BLOCK.get(id);
		return block.asItem();
	}
}
