package com.example.mob;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CubeFriendEntityRenderer extends MobEntityRenderer<CubeFriendEntity, CubeFriendEntityModel> {

	private static final Identifier TEXTURE = new Identifier("custommob", "textures/entity/cube_friend.png");

	public CubeFriendEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new CubeFriendEntityModel(context.getPart(CubeFriendEntityModel.LAYER)), 0.3f);
	}

	@Override
	public Identifier getTexture(CubeFriendEntity entity) {
		return TEXTURE;
	}
}
