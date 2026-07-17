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

public class BananaFriendEntityModel extends SinglePartEntityModel<BananaFriendEntity> {

	public static final EntityModelLayer LAYER =
		new EntityModelLayer(new Identifier("custommob", "banana_friend"), "main");

	private final ModelPart root;

	public BananaFriendEntityModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartData body = root.addChild("body", ModelPartBuilder.create(),
			ModelTransform.pivot(0.0f, 20.0f, 0.0f));

		body.addChild("seg1", ModelPartBuilder.create()
			.uv(0, 0)
			.cuboid(-2.0f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f),
			ModelTransform.of(-4.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.4f));

		body.addChild("seg2", ModelPartBuilder.create()
			.uv(0, 0)
			.cuboid(-2.5f, -2.0f, -2.0f, 5.0f, 4.0f, 4.0f),
			ModelTransform.of(0.0f, -1.5f, 0.0f, 0.0f, 0.0f, 0.0f));

		body.addChild("seg3", ModelPartBuilder.create()
			.uv(0, 0)
			.cuboid(-2.0f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f),
			ModelTransform.of(4.0f, 1.0f, 0.0f, 0.0f, 0.0f, -0.4f));

		body.addChild("tip", ModelPartBuilder.create()
			.uv(20, 0)
			.cuboid(-1.0f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f),
			ModelTransform.of(-6.5f, 2.5f, 0.0f, 0.0f, 0.0f, 0.7f));

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void setAngles(BananaFriendEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		// Intentionally left blank - the body must stay rigid while bouncing, no squash/stretch.
	}
}
