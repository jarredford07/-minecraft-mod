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
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class CustomMobModClient implements ClientModInitializer {

	public static int blocksBroken = 0;
	private static boolean randomBlockModeEnabled = false;

	public static final Identifier NASHIFIED_PACK_ID = new Identifier("custommob", "nashified");

	private static final KeyBinding FLAMETHROWER_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.custommob.flamethrower",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_G,
		"key.categories.custommob"
	));

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(CustomMobMod.CUSTOM_CREEPER, CreeperEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(BananaFriendEntityModel.LAYER, BananaFriendEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(CustomMobMod.BANANA_FRIEND, BananaFriendEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(CubeFriendEntityModel.LAYER, CubeFriendEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(CustomMobMod.CUBE_FRIEND, CubeFriendEntityRenderer::new);

		EntityRendererRegistry.register(CustomMobMod.BIG_TNT_ENTITY, BigTntEntityRenderer::new);

		EntityRendererRegistry.register(CustomMobMod.FIREBALL_PROJECTILE, FlyingItemEntityRenderer::new);

		FabricLoader.getInstance().getModContainer("custommob").ifPresent(container ->
			ResourceManagerHelper.registerBuiltinResourcePack(
				NASHIFIED_PACK_ID,
				container,
				"Nashified",
				ResourcePackActivationType.NORMAL
			)
		);

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

				ButtonWidget nashifiedButton = ButtonWidget.builder(
					nashifiedButtonText(client),
					button -> {
						boolean currentlyEnabled = client.getResourcePackManager()
							.getEnabledNames().contains(NASHIFIED_PACK_ID.toString());

						if (currentlyEnabled) {
							client.getResourcePackManager().disable(NASHIFIED_PACK_ID.toString());
						} else {
							client.getResourcePackManager().enable(NASHIFIED_PACK_ID.toString());
						}

						client.options.refreshResourcePacks(client.getResourcePackManager());
						client.options.write();
						button.setMessage(nashifiedButtonText(client));

						// Deferred to the next tick rather than run synchronously inside this
						// mouse-click callback - triggering a full resource reload mid-click
						// event processing risked leaving GUI/input state inconsistent.
						client.execute(client::reloadResources);
					}
				).dimensions(scaledWidth - 105, 30, 100, 20).build();

				Screens.getButtons(screen).add(nashifiedButton);
			}

			if (screen instanceof GameMenuScreen) {
				ButtonWidget randomBlockButton = ButtonWidget.builder(
					randomBlockButtonText(),
					button -> {
						randomBlockModeEnabled = !randomBlockModeEnabled;
						button.setMessage(randomBlockButtonText());

						PacketByteBuf buf = PacketByteBufs.create();
						buf.writeBoolean(randomBlockModeEnabled);
						ClientPlayNetworking.send(CustomMobMod.TOGGLE_RANDOM_BLOCK_CHANNEL, buf);
					}
				).dimensions(scaledWidth - 105, 5, 100, 20).build();

				Screens.getButtons(screen).add(randomBlockButton);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null || client.world == null) {
				return;
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

	private static Text randomBlockButtonText() {
		return Text.literal("Random Block: " + (randomBlockModeEnabled ? "ON" : "OFF"));
	}

	private static Text nashifiedButtonText(MinecraftClient client) {
		boolean enabled = client.getResourcePackManager().getEnabledNames().contains(NASHIFIED_PACK_ID.toString());
		return Text.literal("Nashified: " + (enabled ? "ON" : "OFF"));
	}
}
