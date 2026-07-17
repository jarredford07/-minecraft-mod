package com.example.mob;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.CreeperEntityRenderer;

public class CustomMobModClient implements ClientModInitializer {

	public static int blocksBroken = 0;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(CustomMobMod.CUSTOM_CREEPER, CreeperEntityRenderer::new);

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			blocksBroken++;
		});

		CustomMobMod.extraBlocksBrokenCallback = amount -> blocksBroken += amount;

		MultiplierBlock.openScreenCallback = pos -> MinecraftClient.getInstance().setScreen(new MultiplierScreen(pos));

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
