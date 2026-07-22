package com.example.mob;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.function.IntConsumer;

public class CustomMobMod implements ModInitializer {

	public static final Identifier SET_MULTIPLIER_CHANNEL = new Identifier("custommob", "set_multiplier");
	public static final Identifier ENTER_CAR_CHANNEL = new Identifier("custommob", "enter_car");
	public static final Identifier CAR_INPUT_CHANNEL = new Identifier("custommob", "car_input");

	public static EntityType<CustomCreeperEntity> CUSTOM_CREEPER;
	public static EntityType<BananaFriendEntity> BANANA_FRIEND;
	public static EntityType<CubeFriendEntity> CUBE_FRIEND;
	public static EntityType<CarEntity> CAR;
	public static EntityType<BigTntEntity> BIG_TNT_ENTITY;

	public static SpawnEggItem BANANA_FRIEND_SPAWN_EGG;
	public static SpawnEggItem CUBE_FRIEND_SPAWN_EGG;

	public static final CarSpawnerItem CAR_SPAWNER = new CarSpawnerItem(new Item.Settings().maxCount(1));

	public static final FlamethrowerItem FLAMETHROWER = new FlamethrowerItem(new Item.Settings().maxCount(1).maxDamage(500));

	public static IntConsumer extraBlocksBrokenCallback = amount -> {};

	public static final Item BURGER = new Item(new Item.Settings().food(
		new FoodComponent.Builder()
			.hunger(8)
			.saturationModifier(0.8f)
			.build()
	));

	public static final TntPickaxeItem TNT_PICKAXE = new TntPickaxeItem(new Item.Settings());

	public static final Block TRAMPOLINE = new TrampolineBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK));

	public static final BlockItem TRAMPOLINE_ITEM = new BlockItem(TRAMPOLINE, new Item.Settings());

	public static final Block MULTIPLIER = new MultiplierBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));

	public static final BlockItem MULTIPLIER_ITEM = new BlockItem(MULTIPLIER, new Item.Settings());

	public static BlockEntityType<MultiplierBlockEntity> MULTIPLIER_BLOCK_ENTITY;

	public static final Block BIG_TNT = new BigTntBlock(AbstractBlock.Settings.copy(Blocks.TNT));

	public static final BlockItem BIG_TNT_ITEM = new BlockItem(BIG_TNT, new Item.Settings());

	public static final SwordItem EMERALD_SWORD = new SwordItem(EmeraldToolMaterial.INSTANCE, 3, -2.4f, new Item.Settings());
	public static final EmeraldPickaxeItem EMERALD_PICKAXE = new EmeraldPickaxeItem(new Item.Settings());
	public static final EmeraldAxeItem EMERALD_AXE = new EmeraldAxeItem(new Item.Settings());
	public static final ShovelItem EMERALD_SHOVEL = new ShovelItem(EmeraldToolMaterial.INSTANCE, 1.5f, -3.0f, new Item.Settings());
	public static final EmeraldHoeItem EMERALD_HOE = new EmeraldHoeItem(new Item.Settings());

	public static final ArmorItem EMERALD_HELMET = new ArmorItem(EmeraldArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new Item.Settings());
	public static final ArmorItem EMERALD_CHESTPLATE = new ArmorItem(EmeraldArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new Item.Settings());
	public static final ArmorItem EMERALD_LEGGINGS = new ArmorItem(EmeraldArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new Item.Settings());
	public static final ArmorItem EMERALD_BOOTS = new ArmorItem(EmeraldArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new Item.Settings());

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

		BANANA_FRIEND = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("custommob", "banana_friend"),
			EntityType.Builder.create(BananaFriendEntity::new, SpawnGroup.CREATURE)
				.setDimensions(0.8f, 0.8f)
				.build("banana_friend")
		);
		FabricDefaultAttributeRegistry.register(BANANA_FRIEND, BananaFriendEntity.createBananaFriendAttributes());
		BANANA_FRIEND_SPAWN_EGG = new SpawnEggItem(BANANA_FRIEND, 0xEBCD3C, 0x5A4123, new Item.Settings());
		Registry.register(Registries.ITEM, new Identifier("custommob", "banana_friend_spawn_egg"), BANANA_FRIEND_SPAWN_EGG);

		CUBE_FRIEND = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("custommob", "cube_friend"),
			EntityType.Builder.create(CubeFriendEntity::new, SpawnGroup.CREATURE)
				.setDimensions(0.8f, 0.8f)
				.build("cube_friend")
		);
		FabricDefaultAttributeRegistry.register(CUBE_FRIEND, CubeFriendEntity.createCubeFriendAttributes());
		CUBE_FRIEND_SPAWN_EGG = new SpawnEggItem(CUBE_FRIEND, 0x4682DC, 0x19232D, new Item.Settings());
		Registry.register(Registries.ITEM, new Identifier("custommob", "cube_friend_spawn_egg"), CUBE_FRIEND_SPAWN_EGG);

		CAR = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("custommob", "car"),
			EntityType.Builder.<CarEntity>create(CarEntity::new, SpawnGroup.MISC)
				.setDimensions(1.6f, 1.1f)
				.build("car")
		);
		Registry.register(Registries.ITEM, new Identifier("custommob", "car_spawner"), CAR_SPAWNER);
		Registry.register(Registries.ITEM, new Identifier("custommob", "flamethrower"), FLAMETHROWER);

		BIG_TNT_ENTITY = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("custommob", "big_tnt"),
			EntityType.Builder.<BigTntEntity>create(BigTntEntity::new, SpawnGroup.MISC)
				.setDimensions(2.9f, 2.9f)
				.build("big_tnt")
		);
		Registry.register(Registries.BLOCK, new Identifier("custommob", "big_tnt"), BIG_TNT);
		Registry.register(Registries.ITEM, new Identifier("custommob", "big_tnt"), BIG_TNT_ITEM);

		Registry.register(Registries.ITEM, new Identifier("custommob", "burger"), BURGER);
		Registry.register(Registries.ITEM, new Identifier("custommob", "tnt_pickaxe"), TNT_PICKAXE);
		Registry.register(Registries.BLOCK, new Identifier("custommob", "trampoline"), TRAMPOLINE);
		Registry.register(Registries.ITEM, new Identifier("custommob", "trampoline"), TRAMPOLINE_ITEM);

		Registry.register(Registries.BLOCK, new Identifier("custommob", "multiplier"), MULTIPLIER);
		Registry.register(Registries.ITEM, new Identifier("custommob", "multiplier"), MULTIPLIER_ITEM);
		MULTIPLIER_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			new Identifier("custommob", "multiplier"),
			BlockEntityType.Builder.create(MultiplierBlockEntity::new, MULTIPLIER).build(null)
		);

		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_sword"), EMERALD_SWORD);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_pickaxe"), EMERALD_PICKAXE);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_axe"), EMERALD_AXE);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_shovel"), EMERALD_SHOVEL);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_hoe"), EMERALD_HOE);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_helmet"), EMERALD_HELMET);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_chestplate"), EMERALD_CHESTPLATE);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_leggings"), EMERALD_LEGGINGS);
		Registry.register(Registries.ITEM, new Identifier("custommob", "emerald_boots"), EMERALD_BOOTS);

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
					entries.add(MULTIPLIER_ITEM);
					entries.add(EMERALD_SWORD);
					entries.add(EMERALD_PICKAXE);
					entries.add(EMERALD_AXE);
					entries.add(EMERALD_SHOVEL);
					entries.add(EMERALD_HOE);
					entries.add(EMERALD_HELMET);
					entries.add(EMERALD_CHESTPLATE);
					entries.add(EMERALD_LEGGINGS);
					entries.add(EMERALD_BOOTS);
					entries.add(BANANA_FRIEND_SPAWN_EGG);
					entries.add(CUBE_FRIEND_SPAWN_EGG);
					entries.add(CAR_SPAWNER);
					entries.add(FLAMETHROWER);
					entries.add(BIG_TNT_ITEM);
				})
				.build()
		);

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (!world.isClient() && player.getMainHandStack().getItem() == TNT_PICKAXE) {
				Explosion explosion = world.createExplosion(
					player,
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5,
					4.0f,
					World.ExplosionSourceType.TNT
				);

				extraBlocksBrokenCallback.accept(explosion.getAffectedBlocks().size());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(SET_MULTIPLIER_CHANNEL, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int value = buf.readInt();

			server.execute(() -> {
				World world = player.getWorld();
				if (!world.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4)) {
					return;
				}
				BlockEntity blockEntity = world.getBlockEntity(pos);
				BlockState state = world.getBlockState(pos);
				if (blockEntity instanceof MultiplierBlockEntity multiplierEntity && state.getBlock() == MULTIPLIER) {
					multiplierEntity.setMultiplier(value);
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(ENTER_CAR_CHANNEL, (server, player, handler, buf, responseSender) -> {
			int entityId = buf.readInt();

			server.execute(() -> {
				if (player.getVehicle() != null) {
					return;
				}
				net.minecraft.entity.Entity target = player.getWorld().getEntityById(entityId);
				if (target instanceof CarEntity car && player.squaredDistanceTo(car) < 36.0) {
					player.startRiding(car);
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(CAR_INPUT_CHANNEL, (server, player, handler, buf, responseSender) -> {
			boolean forward = buf.readBoolean();
			boolean back = buf.readBoolean();
			boolean left = buf.readBoolean();
			boolean right = buf.readBoolean();

			server.execute(() -> {
				if (player.getVehicle() instanceof CarEntity car) {
					car.setInputs(forward, back, left, right);
				}
			});
		});

		System.out.println("Custom Mob Mod initialized!");
	}
}
