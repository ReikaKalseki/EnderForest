/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.EnderForest.ItemBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.EnderForest.Registry.EnderBlocks;

public class ItemEnderBucket extends Item {

	public ItemEnderBucket(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setContainerItem(Item.bucketEmpty);
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer ep, World world, int x, int y, int z, int side, float a, float b, float c) {
		if (!ReikaWorldHelper.softBlocks(world, x, y, z) && world.getBlockMaterial(x, y, z) != Material.water && world.getBlockMaterial(x, y, z) != Material.lava) {
			if (side == 0)
				--y;
			if (side == 1)
				++y;
			if (side == 2)
				--z;
			if (side == 3)
				++z;
			if (side == 4)
				--x;
			if (side == 5)
				++x;
			if (!ReikaWorldHelper.softBlocks(world, x, y, z) && world.getBlockMaterial(x, y, z) != Material.water && world.getBlockMaterial(x, y, z) != Material.lava)
				return false;
		}
		world.setBlock(x, y, z, EnderBlocks.LIQUID.getBlockID());
		if (!ep.capabilities.isCreativeMode)
			ep.setCurrentItemOrArmor(0, new ItemStack(Item.bucketEmpty));
		return true;
	}

	@Override
	public void registerIcons(IconRegister ico) {
		itemIcon = ico.registerIcon("EnderForest:enderbucket");
	}

}
