package com.example.mob;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BigTntEntityRenderer extends EntityRenderer<BigTntEntity> {

	private final BlockRenderManager blockRenderManager;

	public BigTntEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.shadowRadius = 1.5f;
		this.blockRenderManager = context.getBlockRenderManager();
	}

	@Override
	public Identifier getTexture(BigTntEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}

	@Override
	public void render(BigTntEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.translate(0.0, 0.5, 0.0);

		int fuse = entity.getFuse();
		if ((float) fuse - tickDelta + 1.0f < 10.0f) {
			float wobble = 1.0f - ((float) fuse - tickDelta + 1.0f) / 10.0f;
			wobble = MathHelper.clamp(wobble, 0.0f, 1.0f);
			wobble *= wobble;
			wobble *= wobble;
			float scale = 1.0f + wobble * 0.3f;
			matrices.scale(scale, scale, scale);
		}

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f));
		matrices.translate(-0.5, -0.5, -0.5);

		BlockState state = CustomMobMod.BIG_TNT.getDefaultState();
		this.blockRenderManager.renderBlockAsEntity(state, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);

		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
