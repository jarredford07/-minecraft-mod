package com.example.mob;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;

public class CarEntityModel extends SinglePartEntityModel<CarEntity> {

	public static final EntityModelLayer LAYER =
		new EntityModelLayer(new Identifier("custommob", "car"), "main");

	private final ModelPart root;

	public CarEntityModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartData body = root.addChild("body", ModelPartBuilder.create(),
			ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		body.addChild("chassis", ModelPartBuilder.create()
			.uv(0, 0)
			.cuboid(-8.0f, 0.0f, -16.0f, 16.0f, 8.0f, 32.0f),
			ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		body.addChild("cabin", ModelPartBuilder.create()
			.uv(0, 40)
			.cuboid(-7.0f, 8.0f, -6.0f, 14.0f, 12.0f, 16.0f),
			ModelTransform.pivot(0.0f, 0.0f, -2.0f));

		body.addChild("wheel_fl", ModelPartBuilder.create()
			.uv(0, 68)
			.cuboid(-2.0f, -4.0f, -4.0f, 4.0f, 8.0f, 8.0f),
			ModelTransform.pivot(-8.0f, 0.0f, -11.0f));

		body.addChild("wheel_fr", ModelPartBuilder.create()
			.uv(0, 68)
			.cuboid(-2.0f, -4.0f, -4.0f, 4.0f, 8.0f, 8.0f),
			ModelTransform.pivot(8.0f, 0.0f, -11.0f));

		body.addChild("wheel_bl", ModelPartBuilder.create()
			.uv(0, 68)
			.cuboid(-2.0f, -4.0f, -4.0f, 4.0f, 8.0f, 8.0f),
			ModelTransform.pivot(-8.0f, 0.0f, 11.0f));

		body.addChild("wheel_br", ModelPartBuilder.create()
			.uv(0, 68)
			.cuboid(-2.0f, -4.0f, -4.0f, 4.0f, 8.0f, 8.0f),
			ModelTransform.pivot(8.0f, 0.0f, 11.0f));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void setAngles(CarEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		// Intentionally left blank - rigid body, no deformation.
	}
}
