/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.EnderForest;

import java.net.URL;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import ttftcuts.atg.api.ATGBiomes;
import ttftcuts.atg.api.ATGBiomes.BiomeType;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.Auxiliary.BiomeCollisionTracker;
import Reika.DragonAPI.Auxiliary.CommandableUpdateChecker;
import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Instantiable.IO.ControlledConfig;
import Reika.DragonAPI.Instantiable.IO.ModLogger;
import Reika.DragonAPI.Libraries.ReikaRegistryHelper;
import Reika.DragonAPI.ModInteract.ThermalHandler;
import Reika.EnderForest.Registry.EnderBlocks;
import Reika.EnderForest.Registry.EnderItems;
import Reika.EnderForest.Registry.EnderOptions;
import Reika.EnderForest.World.BiomeEnderForest;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "EnderForest", name="Ender Forest", version="beta", certificateFingerprint = "@GET_FINGERPRINT@", dependencies="required-after:DragonAPI")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class EnderForest extends DragonAPIMod {

	@Instance("EnderForest")
	public static EnderForest instance = new EnderForest();
	public static ModLogger logger;

	public static final ControlledConfig config = new ControlledConfig(instance, EnderOptions.optionList, EnderBlocks.blockList, EnderItems.itemList, null, 1);

	public static Block[] blocks = new Block[EnderBlocks.blockList.length];

	public static Item[] items = new Item[EnderItems.itemList.length];

	public static final Fluid ender = new Fluid("ender");

	public static final Material enderMat = new Material(MapColor.ironColor);

	public static BiomeEnderForest biome;

	@Override
	@EventHandler
	public void preload(FMLPreInitializationEvent evt) {
		config.loadSubfolderedConfigFile(evt);
		config.initProps(evt);
		logger = new ModLogger(instance, false);

		BiomeCollisionTracker.instance.addBiomeID(instance, EnderOptions.BIOME.getValue(), BiomeEnderForest.class);

		this.basicSetup(evt);
	}

	@Override
	@EventHandler
	public void load(FMLInitializationEvent event) {
		biome = new BiomeEnderForest(EnderOptions.BIOME.getValue());
		GameRegistry.addBiome(biome);

		FluidRegistry.registerFluid(ender);
		ReikaRegistryHelper.instantiateAndRegisterBlocks(instance, EnderBlocks.blockList, blocks);
		ReikaRegistryHelper.instantiateAndRegisterItems(instance, EnderItems.itemList, items);

		//ender.setIcons(EnderBlocks.STILL.getBlockInstance().getIcon(0,0), EnderBlocks.FLOWING.getBlockInstance().getIcon(0,0));
		if (!ModList.THERMALEXPANSION.isLoaded())
			FluidContainerRegistry.registerFluidContainer(new FluidStack(ender, 1000), EnderItems.BUCKET.getStackOf(), new ItemStack(Item.bucketEmpty));

		ATGBiomes.addBiome(BiomeType.LAND, "Forest", biome, 1.0);
	}

	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void textureHook(TextureStitchEvent.Pre event) {
		IconRegister ico = event.map;
		Icon[] icons = new Icon[]{ico.registerIcon("enderforest:ender"), ico.registerIcon("enderforest:flowingender")};
		ender.setIcons(icons[0], icons[1]);
		ender.setBlockID(EnderBlocks.LIQUID.getBlockID());
	}

	public Block getEnderBlockToGenerate() {
		if (ModList.THERMALEXPANSION.isLoaded()) {
			return Block.blocksList[ThermalHandler.getInstance().enderID];
		}
		return EnderBlocks.LIQUID.getBlockInstance();
	}

	@ForgeSubscribe
	public void onBucketUse(FillBucketEvent event) {
		World world = event.world;
		MovingObjectPosition pos = event.target;
		int x = pos.blockX;
		int y = pos.blockY;
		int z = pos.blockZ;
		int id = world.getBlockId(x, y, z);
		if (id == EnderBlocks.LIQUID.getBlockID()) {
			world.setBlock(x, y, z, 0);
			event.setResult(Result.ALLOW);
			event.result = EnderItems.BUCKET.getStackOf();
			//event.entityPlayer.setCurrentItemOrArmor(0, event.result);
		}
	}

	@Override
	@EventHandler
	public void postload(FMLPostInitializationEvent evt) {

	}

	@Override
	public String getDisplayName() {
		return "Ender Forest";
	}

	@Override
	public String getModAuthorName() {
		return "Reika";
	}

	@Override
	public URL getDocumentationSite() {
		return DragonAPICore.getReikaForumPage();
	}

	@Override
	public ModLogger getModLogger() {
		return logger;
	}

	@Override
	public String getWiki() {
		return null;
	}

	@Override
	public String getUpdateCheckURL() {
		return CommandableUpdateChecker.reikaURL;
	}

}
