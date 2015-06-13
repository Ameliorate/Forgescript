package org.ame.jsforge.blockapi;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Amelorate
 */
public class MaterialWrapper {
	public boolean isLiquid = false;
	public boolean isSolid = true;
	public boolean canKillGrassUnder = true;
	public boolean blocksMovement = true;
	public boolean canBurn = false;
	public boolean isReplaceable = false;
	public boolean isTranslucent = true;
	public boolean requiresNoTool = true;
	/*
	public boolean isNoPushMobility = false;
	public boolean isImmovableMobility = false;		// I'm not sure what these do.
	*/

	/**
	 * If this block is breakable with your hands in adventure mode.
	 */
	public boolean isAdventureModeExempt = false;

	public Material toMaterial() {
		MaterialMethodOverride asMaterial = new MaterialMethodOverride();
		if (isAdventureModeExempt) {
			invokePrivate("setAdventureModeExempt", asMaterial);
		}
		if (isLiquid) {
			asMaterial.isLiquid = true;
		}
		if (isTranslucent) {
			invokePrivate("setTranslucent", asMaterial);
		}
		if (isReplaceable) {
			invokePrivate("setReplaceable", asMaterial);
		}
		if (isSolid) {
			asMaterial.isSolid = true;
		}
		if (canBurn) {
			invokePrivate("setBurning", asMaterial);
		}
		if (canKillGrassUnder) {
			asMaterial.canBlockGrass = true;
		}
		if (blocksMovement) {
			asMaterial.blocksMovement = true;
		}
		if (!requiresNoTool) {
			invokePrivate("setRequiresTool", asMaterial);
		}
		return asMaterial;
	}

	private void invokePrivate(String invoking, Object invokingOn) {
		try {
			Method method = invokingOn.getClass().getDeclaredMethod(invoking);
			method.setAccessible(true);
			method.invoke(invokingOn);
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new AssertionError(e);
		}
	}

	private class MaterialMethodOverride extends Material {	// Since some methods in material return true/false instead the value of a field, I use this.
		public MaterialMethodOverride() {
			super(MapColor.stoneColor);
		}

		public boolean isLiquid;
		public boolean isSolid;
		public boolean canBlockGrass;
		public boolean blocksMovement;

		@Override
		public boolean isLiquid() {
			return isLiquid;
		}

		@Override
		public boolean isSolid() {
			return isSolid;
		}

		@Override
		public boolean getCanBlockGrass() {
			return canBlockGrass;
		}

		@Override
		public boolean blocksMovement() {
			return blocksMovement;
		}
	}
}
