package com.example.mob;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BananaFriendEntityRenderer extends MobEntityRenderer<BananaFriendEntity, BananaFriendEntityModel> {

	private static final Identifier TEXTURE = new Identifier("custommob", "textures/entity/banana_friend.png");

	public BananaFriendEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BananaFriendEntityModel(context.getPart(BananaFriendEntityModel.LAYER)), 0.3f);
	}

	@Override
	public Identifier getTexture(BananaFriendEntity entity) {
		return TEXTURE;
	}
}
