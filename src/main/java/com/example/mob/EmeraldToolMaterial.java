package com.example.mob;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public enum EmeraldToolMaterial implements ToolMaterial {
	INSTANCE;

	@Override
	public int getDurability() {
		return 2200;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return 9.5f;
	}

	@Override
	public float getAttackDamage() {
		return 4.0f;
	}

	@Override
	public int getMiningLevel() {
		return 4;
	}

	@Override
	public int getEnchantability() {
		return 15;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.EMERALD);
	}
}
