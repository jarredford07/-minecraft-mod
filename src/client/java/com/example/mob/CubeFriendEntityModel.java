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

public class CubeFriendEntityModel extends SinglePartEntityModel<CubeFriendEntity> {

	public static final EntityModelLayer LAYER =
		new EntityModelLayer(new Identifier("custommob", "cube_friend"), "main");

	private final ModelPart root;

	public CubeFriendEntityModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartData body = root.addChild("body", ModelPartBuilder.create()
			.uv(0, 0)
			.cuboid(-4.0f, -12.0f, -4.0f, 8.0f, 8.0f, 8.0f),
			ModelTransform.pivot(0.0f, 24.0f, 0.0f));

		body.addChild("leg_fl", ModelPartBuilder.create()
			.uv(0, 16)
			.cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 4.0f, 2.0f),
			ModelTransform.pivot(-2.5f, -4.0f, -2.5f));

		body.addChild("leg_fr", ModelPartBuilder.create()
			.uv(0, 16)
			.cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 4.0f, 2.0f),
			ModelTransform.pivot(2.5f, -4.0f, -2.5f));

		body.addChild("leg_bl", ModelPartBuilder.create()
			.uv(0, 16)
			.cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 4.0f, 2.0f),
			ModelTransform.pivot(-2.5f, -4.0f, 2.5f));

		body.addChild("leg_br", ModelPartBuilder.create()
			.uv(0, 16)
			.cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 4.0f, 2.0f),
			ModelTransform.pivot(2.5f, -4.0f, 2.5f));

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void setAngles(CubeFriendEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		// Intentionally left blank - the body must stay rigid while bouncing, no squash/stretch.
	}
}
