package com.example.mob;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomMobMod implements ModInitializer {

	public static EntityType<CustomCreeperEntity> CUSTOM_CREEPER;

	@Override
	public void onInitialize() {
		CUSTOM_CREEPER = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("custommob", "custom_creeper"),
			EntityType.Builder.create(CustomCreeperEntity::new, SpawnGroup.MONSTER)
				.setDimensions(0.6f, 1.7f)
				.build("custom_creeper")
		);

		FabricDefaultAttributeRegistry.register(CUSTOM_CREEPER, CreeperEntity.createCreeperAttributes());

		System.out.println("Custom Mob Mod initialized!");
	}
}
