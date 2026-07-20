package com.example.mob;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class CarEntityRenderer extends EntityRenderer<CarEntity> {

	private static final Identifier TEXTURE = new Identifier("custommob", "textures/entity/car.png");

	private final CarEntityModel model;

	public CarEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.model = new CarEntityModel(context.getPart(CarEntityModel.LAYER));
	}

	@Override
	public Identifier getTexture(CarEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(CarEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.translate(0.0, 0.25, 0.0);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - yaw));

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(TEXTURE));
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);

		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
