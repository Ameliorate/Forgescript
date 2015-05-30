package org.ame.jsforge.blockapi;

/**
 * @author Amelorate
 * Contains all the possible creative tabs in basic minecraft.
 */
public enum CreativeTabsForgeScript {
	MISC,
	ALLSEARCH,
	FOOD,
	TOOLS,
	COMBAT,
	BREWING,
	MATERIALS,
	INVENTORY,
	BLOCK,
	DECORATIONS,
	REDSTONE,
	TRANSPORT;

	/**
	 * Translates between native minecraft creative tabs and enum tabs.
	 */
	public static net.minecraft.creativetab.CreativeTabs translate(CreativeTabsForgeScript tab) {
		switch (tab) {
			case MISC:
				return net.minecraft.creativetab.CreativeTabs.tabMisc;
			case ALLSEARCH:
				return net.minecraft.creativetab.CreativeTabs.tabAllSearch;
			case FOOD:
				return net.minecraft.creativetab.CreativeTabs.tabFood;
			case TOOLS:
				return net.minecraft.creativetab.CreativeTabs.tabTools;
			case COMBAT:
				return net.minecraft.creativetab.CreativeTabs.tabCombat;
			case BREWING:
				return net.minecraft.creativetab.CreativeTabs.tabBrewing;
			case MATERIALS:
				return net.minecraft.creativetab.CreativeTabs.tabMaterials;
			case INVENTORY:
				return net.minecraft.creativetab.CreativeTabs.tabInventory;
			case BLOCK:
				return net.minecraft.creativetab.CreativeTabs.tabBlock;
			case DECORATIONS:
				return net.minecraft.creativetab.CreativeTabs.tabDecorations;
			case REDSTONE:
				return net.minecraft.creativetab.CreativeTabs.tabRedstone;
			case TRANSPORT:
				return net.minecraft.creativetab.CreativeTabs.tabTransport;
			default:
				throw new AssertionError("This code should not be reached!");
		}
	}
}
