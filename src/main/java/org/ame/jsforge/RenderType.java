package org.ame.jsforge;

/**
 * @author Amelorate
 * Contains all the possible render types.
 */
public enum RenderType {
	SIGN(-1),
	BLOCK(0),
	FLOWER_OR_REED(1),
	TORCH(2),
	FIRE(3),
	FLUIDs(4),
	REDSTONE_WIRE(5),
	CROPS(6),
	DOOR(7),
	LADDER(8),
	MINECART_TRACK(9),
	STAIRS(10),
	FENCE(11),
	LEVER(12),
	CACTUS(13),
	BED(14),
	REDSTONE_REPEATER(15),
	PISTON_BASE(16),
	PISTON_ARM(17),
	GLASS_PANE_OR_IRON_BAR(18),
	MELLON_OR_PUMPKIN_STEM(19),
	VINE(20),
	FENCE_GATE(21),
	CHEST_ENDERCHEST(22),
	LILLY_PAD(23),
	COULDRON(24),
	BREWING_STAND(25),
	END_PORTAL_FRAME(26),
	END_DRAGON_EGG(27),
	COCOA_BEAN(28),
	TRIP_WIRE_HOOK(29),
	TRIP_WIRE(30),
	LOG(31),
	STONE_WALL(32),
	FLOWER_POT(33),
	BEACON(34),
	ANVIL(35);

	private int value;

	RenderType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
