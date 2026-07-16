package com.example.mob;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum EmeraldArmorMaterial implements ArmorMaterial {
	INSTANCE;

	@Override
	public int getDurability(ArmorItem.Type type) {
		switch (type) {
			case HELMET:
				return 400;
			case CHESTPLATE:
				return 580;
			case LEGGINGS:
				return 550;
			case BOOTS:
				return 470;
			default:
				return 400;
		}
	}

	@Override
	public int getProtection(ArmorItem.Type type) {
		switch (type) {
			case HELMET:
				return 3;
			case CHESTPLATE:
				return 8;
			case LEGGINGS:
				return 6;
			case BOOTS:
				return 3;
			default:
				return 0;
		}
	}

	@Override
	public int getEnchantability() {
		return 15;
	}

	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.EMERALD);
	}

	@Override
	public String getName() {
		return "custommob:emerald";
	}

	@Override
	public float getToughness() {
		return 3.0f;
	}

	@Override
	public float getKnockbackResistance() {
		return 0.1f;
	}
}
