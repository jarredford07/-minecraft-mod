package com.example.mob;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CustomMobModClient implements ClientModInitializer {

	public static int blocksBroken = 0;

	private static final KeyBinding ENTER_CAR_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.custommob.enter_car",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_Z,
		"key.categories.custommob"
	));

	private static final KeyBinding FLAMETHROWER_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.custommob.flamethrower",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_TAB,
		"key.categories.custommob"
	));

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(CustomMobMod.CUSTOM_CREEPER, CreeperEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(BananaFriendEntityModel.LAYER, BananaFriendEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(CustomMobMod.BANANA_FRIEND, BananaFriendEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(CubeFriendEntityModel.LAYER, CubeFriendEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(CustomMobMod.CUBE_FRIEND, CubeFriendEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(CarEntityModel.LAYER, CarEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(CustomMobMod.CAR, CarEntityRenderer::new);

		EntityRendererRegistry.register(CustomMobMod.BIG_TNT_ENTITY, BigTntEntityRenderer::new);

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			blocksBroken++;
		});

		CustomMobMod.extraBlocksBrokenCallback = amount -> blocksBroken += amount;

		MultiplierBlock.openScreenCallback = pos -> MinecraftClient.getInstance().setScreen(new MultiplierScreen(pos));

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen) {
				ButtonWidget randomizeButton = ButtonWidget.builder(
					Text.literal("Randomize Splash"),
					button -> client.setScreen(new TitleScreen())
				).dimensions(scaledWidth - 105, 5, 100, 20).build();

				Screens.getButtons(screen).add(randomizeButton);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null || client.world == null) {
				return;
			}

			if (ENTER_CAR_KEY.wasPressed()) {
				if (client.player.getVehicle() instanceof CarEntity) {
					client.player.stopRiding();
				} else if (client.player.getVehicle() == null) {
					java.util.List<Entity> nearby = client.world.getOtherEntities(
						client.player,
						client.player.getBoundingBox().expand(4.0),
						entity -> entity instanceof CarEntity
					);

					CarEntity nearestCar = null;
					double nearestDistance = 16.0;
					for (Entity entity : nearby) {
						double distance = client.player.squaredDistanceTo(entity);
						if (distance < nearestDistance) {
							nearestDistance = distance;
							nearestCar = (CarEntity) entity;
						}
					}

					if (nearestCar != null) {
						PacketByteBuf buf = PacketByteBufs.create();
						buf.writeInt(nearestCar.getId());
						ClientPlayNetworking.send(CustomMobMod.ENTER_CAR_CHANNEL, buf);
					}
				}
			}

			if (client.player.getVehicle() instanceof CarEntity) {
				PacketByteBuf buf = PacketByteBufs.create();
				buf.writeBoolean(client.options.forwardKey.isPressed());
				buf.writeBoolean(client.options.backKey.isPressed());
				buf.writeBoolean(client.options.leftKey.isPressed());
				buf.writeBoolean(client.options.rightKey.isPressed());
				ClientPlayNetworking.send(CustomMobMod.CAR_INPUT_CHANNEL, buf);
			}

			if (FLAMETHROWER_KEY.isPressed() && client.player.getMainHandStack().getItem() instanceof FlamethrowerItem) {
				ClientPlayNetworking.send(CustomMobMod.FLAMETHROWER_FIRE_CHANNEL, PacketByteBufs.create());
			}
		});

		HudRenderCallback.EVENT.register((context, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			context.drawText(
				client.textRenderer,
				"Blocks Broken: " + blocksBroken,
				10,
				10,
				0xFFFFFF,
				true
			);
		});
	}
}
