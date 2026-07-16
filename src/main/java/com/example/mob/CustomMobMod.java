package com.example.mob;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CustomMobMod implements ModInitializer {

	public static EntityType<CustomCreeperEntity> CUSTOM_CREEPER;

	public static final Item BURGER = new Item(new Item.Settings().food(
		new FoodComponent.Builder()
			.hunger(8)
			.saturationModifier(0.8f)
			.build()
	));

	public static final TntPickaxeItem TNT_PICKAXE = new TntPickaxeItem(new Item.Settings());

	public static final Block TRAMPOLINE = new TrampolineBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK));

	public static final BlockItem TRAMPOLINE_ITEM = new BlockItem(TRAMPOLINE, new Item.Settings());

	public static final RegistryKey<ItemGroup> CUSTOM_GROUP = RegistryKey.of(
		RegistryKeys.ITEM_GROUP,
		new Identifier("custommob", "custom_group")
	);

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

		Registry.register(Registries.ITEM, new Identifier("custommob", "burger"), BURGER);
		Registry.register(Registries.ITEM, new Identifier("custommob", "tnt_pickaxe"), TNT_PICKAXE);
		Registry.register(Registries.BLOCK, new Identifier("custommob", "trampoline"), TRAMPOLINE);
		Registry.register(Registries.ITEM, new Identifier("custommob", "trampoline"), TRAMPOLINE_ITEM);

		Registry.register(
			Registries.ITEM_GROUP,
			CUSTOM_GROUP,
			FabricItemGroup.builder()
				.displayName(Text.translatable("itemGroup.custommob.custom_group"))
				.icon(() -> new ItemStack(BURGER))
				.entries((context, entries) -> {
					entries.add(BURGER);
					entries.add(TNT_PICKAXE);
					entries.add(TRAMPOLINE_ITEM);
				})
				.build()
		);

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (!world.isClient() && player.getMainHandStack().getItem() == TNT_PICKAXE) {
				world.createExplosion(
					player,
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5,
					4.0f,
					World.ExplosionSourceType.TNT
				);
			}
		});

		System.out.println("Custom Mob Mod initialized!");
	}
}
