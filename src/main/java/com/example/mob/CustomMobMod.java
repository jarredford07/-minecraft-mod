package com.example.mob;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomMobMod implements ModInitializer {

	public static final EntityType<CustomCreeperEntity> CUSTOM_CREEPER = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier("custommob", "custom_creeper"),
		EntityType.Builder.create(CustomCreeperEntity::new, SpawnGroup.MONSTER)
			.setDimensions(0.6f, 1.7f)
			.build("custom_creeper")
	);

	@Override
	public void onInitialize() {
		System.out.println("Custom Mob Mod initialized!");
	}
}
