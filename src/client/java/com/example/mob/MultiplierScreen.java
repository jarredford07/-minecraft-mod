package com.example.mob;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class MultiplierScreen extends Screen {

	private final BlockPos pos;
	private TextFieldWidget input;

	public MultiplierScreen(BlockPos pos) {
		super(Text.literal("Set Multiplier"));
		this.pos = pos;
	}

	@Override
	protected void init() {
		input = new TextFieldWidget(
			this.textRenderer,
			this.width / 2 - 60,
			this.height / 2 - 10,
			120,
			20,
			Text.literal("Amount")
		);
		input.setMaxLength(4);
		input.setTextPredicate(text -> text.isEmpty() || text.matches("\\d{1,4}"));
		input.setText("2");
		this.addDrawableChild(input);
		this.setFocused(input);
		input.setFocused(true);

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Confirm"), button -> confirm())
			.position(this.width / 2 - 60, this.height / 2 + 20)
			.size(120, 20)
			.build());
	}

	private void confirm() {
		int value;
		try {
			value = Integer.parseInt(input.getText());
		} catch (NumberFormatException e) {
			value = 1;
		}
		value = Math.max(1, Math.min(MultiplierBlockEntity.MAX_MULTIPLIER, value));

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		buf.writeInt(value);
		ClientPlayNetworking.send(CustomMobMod.SET_MULTIPLIER_CHANNEL, buf);

		this.close();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(
			this.textRenderer,
			"Set multiplier amount (1-" + MultiplierBlockEntity.MAX_MULTIPLIER + ")",
			this.width / 2,
			this.height / 2 - 30,
			0xFFFFFF
		);
	}
}
