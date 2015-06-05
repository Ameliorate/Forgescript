package org.ame.jsforge.blockapi;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author Amelorate
 * Wrapps IBlockAccess for safety.
 */
public class IBlockAccessWrapper {
	public IBlockAccessWrapper(IBlockAccess wrapping) {
		this.wrapping = wrapping;
	}

	private IBlockAccess wrapping;

	/*	// Wrap block
	public BlockWrapper getBlock(int xPos, int var2, int var3) {

	}
	*/

	/*	// Wrap tile entity.
	public TileEntityWrapper getTileEntity(int var1, int var2, int var3) {

	}
	*/

	/**
	 * @param meta Possibly meta, possibly something else.
	 */
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int xPos, int yPos, int zPos, int meta) {
		return wrapping.getLightBrightnessForSkyBlocks(xPos, yPos, zPos, meta);
	}

	public int getBlockMetadata(int xPos, int yPos, int zPos) {
		return wrapping.getBlockMetadata(xPos, yPos, zPos);
	}

	public int isBlockProvidingPowerTo(int xPos, int yPos, int zPos, int meta) {
		return wrapping.isBlockProvidingPowerTo(xPos, yPos, zPos, meta);
	}

	public boolean isAirBlock(int xPos, int yPos, int zPos) {
		return wrapping.isAirBlock(xPos, yPos, zPos);
	}

	/*
	@SideOnly(Side.CLIENT)
	BiomeGenBase getBiomeGenForCoords(int var1, int var2) {

	}
	*/

	/**
	 * @return Possbly the maximum height of the world.
	 */
	@SideOnly(Side.CLIENT)
	int getHeight() {
		return wrapping.getHeight();
	}

	/*	// I have no clue what this does.
	@SideOnly(Side.CLIENT)
	boolean extendedLevelsInChunkCache() {
		return wrapping.extendedLevelsInChunkCache();
	}
	*/

	/*	ForgeDirection wrapper.
	boolean isSideSolid(int var1, int var2, int var3, ForgeDirection var4, boolean var5) {

	}
	*/
}
