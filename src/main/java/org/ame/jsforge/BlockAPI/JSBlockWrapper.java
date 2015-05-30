package org.ame.jsforge.blockapi;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import org.ame.jsforge.EventBus;
import org.ame.jsforge.internal.JSMod;

import java.util.Random;

/**
 * @author Amelorate
 * Wraps all block fuctions in a easy to use way for use in javascript. All backwards compatable methods are affixed with "Safe"
 */
public class JSBlockWrapper extends Block {		// Expect lots of switching over enums for compatability.
	public JSBlockWrapper(Material material, JSMod owner) {
		super(material);
		this.owner = owner;
	}

	/*
	Later interest?

	-- hasTileEntity
	-- createTileEntity

	These all seem awfully application spefific. Create an issue if you need them.
	-- isBed
	-- getBedSpawnPosition
	-- setBedOccupied
	-- getBedDirection
	-- isBedFoot
	-- beginLeavesDecay	// Perhaps I could have these in a different class?
	-- canSustainLeaves
	-- isLeaves
	-- isWood
	-- isFoliage

	 */



	// CURRENTLY AT canEntityDestroy






	/**
	 * The owner mod of this block. Basically who defined it.
	 */
	public JSMod owner;

	/**
	 * What basic model the block should be rendered as.
	 */
	public RenderType renderType = RenderType.BLOCK;

	/**
	 * The explosion resistance of the block. By default based on the hardness.
	 */
	public float explosionResistance = -1F;

	/**
	 * If you can see through this.
	 */
	public boolean isTransparent = false;

	/**
	 * If the player or others can collide with this.
	 */
	public boolean isCollidable = true;

	/**
	 * The color of the block. Probably used in grayscale colonisation or something.
	 */
	public int blockColor = 16777215;

	/**
	 * The color of the block. Probably used in grayscale colonisation or something.
	 */
	public int renderColor = 16777215;

	/**
	 * The color of the block. Probably used in grayscale colonisation or something.
	 */
	public int colorMultiplier = 16777215;

	/**
	 * True if this provides redstone power.
	 */
	public boolean canProvideRedstonePower = false;

	/**
	 * The localised name of this block. By default it uses the localised registry, of if you use that you don't need to set this.
	 */
	public String localisedName = null;

	/**
	 * If this can drop on explosion.
	 */
	public boolean canDropFromExplosion = true;

	/**
	 * Call the javascript function canDropFromExplosion on an explosion if any logic needs done. This overrides canDropFromExplosion.
	 */
	public boolean callCanDropFromExplosionOnExplosion = false;

	/**
	 * If this outputs anything to comparators.
	 */
	public boolean hasComparatorInputOverride = false;

	/**
	 * If it is a ladder to all entities.
	 */
	public boolean isLadder = false;

	/**
	 * To call js space when checking if the block is a ladder.
	 */
	public boolean callIsLadderOnIsLadder = false;

	/**
	 * If this block can be replaced by right clicking it with another block.
	 */
	public boolean canBeReplaced = false;

	/**
	 * If the block is allays burning.
	 */
	public boolean isBurning = false;

	/**
	 * Call to JS space when checking if the block is burning.
	 */
	public boolean callIsBurning = false;

	/**
	 * Use the default implementation of isAir() based on material type?
	 */
	public boolean useDefaultIsAir = true;

	/**
	 * If useDefaultIsAir and callIsAir is false isAir returns this.
	 */
	public boolean isAir = false;

	/**
	 * Call JS space on isAir.
	 */
	public boolean callIsAir = false;

	/**
	 * To base weather or not the block is harvestable based on tool level.
	 */
	public boolean useToolLevelHarvest = true;

	/**
	 * If this is always harvestable with any tool.
	 */
	public boolean isAlwaysHarvestable = false;

	/**
	 * To call JS space if the block is harvestable.
	 */
	public boolean callIsHarvestable = false;

	/**
	 * How flammable the block is.
	 */
	public int flamibility = 0;

	/**
	 * To call JS space when getting flammability.
	 */
	public boolean callGetFlammability = false;

	/**
	 * How fast fire spreads from this block.
	 */
	public int fireSpreadSpeed = 0;

	/**
	 * To call JS space when getting fire spread speed.
	 */
	public boolean callGetFireSpreadSpeed = false;

	/**
	 * If this block is a source of fire.
	 */
	public boolean isFireSource = false;

	/**
	 * Call JS space when determining if this is a source of fire?
	 */
	public boolean callIsFireSource = false;

	/**
	 * The quantity of items or blocks dropped on break.
	 */
	public int quantityDropedOnBreak = 1;

	/**
	 * Call JS when determining how many things to drop on break?
	 */
	public boolean callQuantityDropped = false;

	/**
	 * If the silk touch enchantment can harvest this and keep state.
	 */
	public boolean canSilkTouchHarvest = false;

	/**
	 * If a creature can spawn here.
	 */
	public boolean canCreatureSpawn = true;

	/**
	 * If this block can be replaced by leaves from a growing tree.
	 */
	public boolean canBeReplacedByLeaves = false;

	/**
	 * If you can place a torch on top of the block even if the top of the block isn't solid.
	 */
	public boolean canPlaceTorchOnTopWhenOtherwiseUnable = false;

	/**
	 * If this can support all plants using the IPlantable interface.
	 */
	public boolean canSustainAllPlants = false;

	/**
	 * If this block is always equal to wet farmland instead of dry farmland.
	 */
	public boolean isFertile = false;

	/**
	 * Call JS space when checking if it is wet.
	 */
	public boolean callIsFertile = false;

	/**
	 * If every entity that destroys blocks can destroy this block. If it is false the event canEntityDestroy is called.
	 */
	public boolean canAllEntitiesDestroy = true;

	/**
	 * Determines the amount of enchanting power this block can provide to an enchanting table.
	 */
	public float enchantPowerBonus = 0f;

	/**
	 * The amount of exp the block drops when broken.
	 */
	public int expDrop = 0;


	public void setCreativeTabSafe(CreativeTabsForgeScript tab) {
		setCreativeTab(CreativeTabsForgeScript.translate(tab));
	}

	public void setBlockUnlocalisedNameSafe(String name) {
		setBlockName(name);
	}

	public void setBlockBoundsSafe(float x1, float x2, float y1, float y2, float z1, float z2) {
		setBlockBounds(x1, y1, z1, x2, y2, z2);
	}

	public void setHardnessSafe(float hardness) {
		setHardness(hardness);
	}

	public void setBlockUnbreakableSafe() {
		setBlockUnbreakable();
	}

	public void setTickRandomlySafe(boolean tickRandomly) {
		setTickRandomly(tickRandomly);
	}

	public void setBlockTextureSafe(String textureName) {
		setBlockTextureName(textureName);
	}

	@Override
	public void onBlockHarvested(World world, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_) {
		// TODO: Implement JSEntityPlayerWrapper that backwards compatableises all EntityPlayer methods.
		// Expose this as beforeBlockHarvest
	}

	@Override
	public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_, int p_149664_3_, int p_149664_4_, int p_149664_5_) {
		// TODO: Create World wrapper and do this.
		// Expose this as afterBlockHarvest
	}

	@Override
	public void onBlockPreDestroy(World p_149725_1_, int p_149725_2_, int p_149725_3_, int p_149725_4_, int p_149725_5_) {
		// TODO: Create World wrapper and do this.
	}

	@Override
	public void fillWithRain(World p_149639_1_, int p_149639_2_, int p_149639_3_, int p_149639_4_) {
		// TODO: Create World wrapper and do this.
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		// TODO: Create World wrapper and do this.
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_){
		// TODO: Create World wrapper and do this.
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		// TODO: Create World wrapper and do this.
	}

	@Override
	public void onBlockDestroyedByExplosion(World p_149723_1_, int p_149723_2_, int p_149723_3_, int p_149723_4_, Explosion p_149723_5_) {
		// TODO: World wrapper and Explosion wrapper. Then expose this.
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		// TODO: World wrapper then expose.
		return false;
	}

	@Override
	public void onEntityWalking(World p_149724_1_, int p_149724_2_, int p_149724_3_, int p_149724_4_, Entity p_149724_5_) {
		// TODO: World and entity wrapper then expose this.
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		return p_149660_9_;
		// TODO: World wrapper expose
	}

	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_) {
		// TODO: World entityplayer wrapper expose.
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
		// TODO: World and entity wrapper then expose this to JS.
	}

	@Override
	public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_, int p_149707_5_) {
		return super.canPlaceBlockOnSide(p_149707_1_, p_149707_2_, p_149707_3_, p_149707_4_, p_149707_5_);
		// TODO: World wrapper and then expose this.
	}

	@Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
		return this.quantityDropped(p_149679_2_);
		// TODO: Expose this to JS space.
	}

	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_) {
		return true;
		// TODO: World wrapper and then expose to JS.
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		// TODO: World, EntityLivingBase, ItemStack wrapper then expose.
	}

	@Override
	public void onPostBlockPlaced(World p_149714_1_, int p_149714_2_, int p_149714_3_, int p_149714_4_, int p_149714_5_) {
		// TODO: World wrapper then expose this.
	}

	@Override
	public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_) {
		return false;
		// TODO: World wrapper expose
	}

	@Override
	public void onFallenUpon(World p_149746_1_, int p_149746_2_, int p_149746_3_, int p_149746_4_, Entity p_149746_5_, float p_149746_6_) {
		// TODO: World, Entity
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_) {
		// TODO: World and expose
		return 0;
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		return super.removedByPlayer(world, player, x, y, z, willHarvest); // TODO: World and player wrapper.
	}

	@Override
	public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ) {
		// TODO: World wrapper
	}

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		// TODO: IBlockAccess wrapper.
	}

	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		if (callGetFlammability) {
			return flamibility; // TODO: World and face wrapper, then expose.
		}
		else {
			return flamibility;
		}
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		if (callGetFireSpreadSpeed) {
			return fireSpreadSpeed; // TODO: IBlockAccess and ForgeDirection then expose this.
		}
		else {
			return fireSpreadSpeed;
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return renderType == RenderType.BLOCK;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
		return !this.blockMaterial.blocksMovement();
		// TODO: Expose this to JS space.
	}

	@Override
	public int getRenderType() {
		return renderType.getValue();
	}

	@Override
	public boolean isCollidable() {
		return isCollidable;
	}

	@Override
	public float getExplosionResistance(Entity p_149638_1_) {
		if (explosionResistance != -1f) {
			return explosionResistance;
		}
		else {
			return super.getExplosionResistance(p_149638_1_);
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return !isTransparent;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return blockColor;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int p_149741_1_) {
		return renderColor;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
		return colorMultiplier;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_) {
		return 0;
		// TODO: Wrap IBlockAccess and expose with callGetWeakRedstonePower
	}

	@Override
	public boolean canProvidePower() {
		return canProvideRedstonePower;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_) {
		return 0;
		// TODO: Wrap IBlockAccess and expose with callGetStrongRedstonePower
	}

	@Override
	public String getLocalizedName() {
		if (localisedName != null) {
			return localisedName;
		}
		else {
			return super.getLocalizedName();
		}
	}

	@Override
	public boolean canDropFromExplosion(Explosion p_149659_1_) {
		if (!callCanDropFromExplosionOnExplosion) {
			return canDropFromExplosion;
		}
		else {
			return false;
			// TODO: Replace this with with an invoke when an explosion wrapper is made.
		}
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return hasComparatorInputOverride;
	}

	@Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
		if (!callIsLadderOnIsLadder) {
			return isLadder;
		}
		else {
			return false; // TODO: Replace with event or something.
		}
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return super.isSideSolid(world, x, y, z, side);	// TODO: IBlockAccess, and side wrapper.
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		return canBeReplaced;
	}

	@Override
	public boolean isBurning(IBlockAccess world, int x, int y, int z) {
		if (callIsBurning) {
			return false;	// TODO: IBlockAccess, expose
		}
		else {
			return isBurning;
		}
	}

	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z) {
		if (useDefaultIsAir) {
			return super.isAir(world, x, y, z);
		}
		else if (callIsAir) {
			return false; // TODO: World wrapper then expose.
		}
		else {
			return isAir;
		}
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		if (useToolLevelHarvest) {
			return super.canHarvestBlock(player, meta);
		}
		else if (callIsHarvestable) {
			return true; // TODO: EntityPlayer then expose.
		}
		else {
			return isAlwaysHarvestable;
		}
	}

	@Override
	public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
		if (callIsFireSource) {
			return isFireSource; // TODO: World, ForgeDirection, expose.
		}
		else {
			return isFireSource;
		}
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		if (!callQuantityDropped) {
			return quantityDropedOnBreak;
		}
		else {
			return (int) EventBus.call(owner, "getQuantityDropped", meta, fortune, random);
		}
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		return canSilkTouchHarvest;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return canCreatureSpawn;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
		return canBeReplacedByLeaves;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return this.isSideSolid(world, x, y, z, ForgeDirection.UP) || canPlaceTorchOnTopWhenOtherwiseUnable;
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
		return canSustainAllPlants;
	}

	@Override
	public boolean isFertile(World world, int x, int y, int z) {
		if (!callIsFertile) {
			return isFertile;
		}
		else {
			return isFertile; //TODO: World wrapper
		}
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		if (canAllEntitiesDestroy) {
			return true;
		}
		else {
			return true;
			// TODO: Entity and IBlockAccess wrapper, then event.
		}
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return enchantPowerBonus;
	}

	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		return expDrop;
	}
}
