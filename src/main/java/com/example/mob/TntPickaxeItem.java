package com.example.mob;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;

public class TntPickaxeItem extends PickaxeItem {

	public TntPickaxeItem(Item.Settings settings) {
		super(ToolMaterials.DIAMOND, 1, -2.8f, settings);
	}
}
