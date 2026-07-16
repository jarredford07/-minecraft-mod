package com.example.mob;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;

public class EmeraldPickaxeItem extends PickaxeItem {

	public EmeraldPickaxeItem(Item.Settings settings) {
		super(EmeraldToolMaterial.INSTANCE, 1, -2.8f, settings);
	}
}
