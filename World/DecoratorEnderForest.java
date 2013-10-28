/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.EnderForest.World;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.EnderForest.Registry.EnderOptions;

public class DecoratorEnderForest extends BiomeDecorator {

	EnderPoolGenerator gen = new EnderPoolGenerator();

	public DecoratorEnderForest(BiomeGenBase par1BiomeGenBase) {
		super(par1BiomeGenBase);
	}

	@Override
	protected void decorate() {
		super.decorate();

		int i;
		int j;
		int k;
		int i1;
		int l;

		j = chunk_X + randomGenerator.nextInt(16) + 8;
		k = chunk_Z + randomGenerator.nextInt(16) + 8;

		int arg;
		switch(EnderOptions.POOLS.getValue()) {
		case 1:
			arg = 12;
			break;
		case 2:
			arg = 6;
			break;
		case 3:
			arg = 3;
			break;
		default:
			arg = 6;
			break;
		}
		if (randomGenerator.nextInt(arg) == 0)
			gen.generate(currentWorld, randomGenerator, j, currentWorld.getTopSolidOrLiquidBlock(j, k), k);
	}

	@Override
	public void decorate(World par1World, Random par2Random, int par3, int par4)
	{
		if (currentWorld != null)
		{
			ReikaJavaLibrary.pConsole("Already decorating!!");
		}
		else
		{
			currentWorld = par1World;
			randomGenerator = par2Random;
			chunk_X = par3;
			chunk_Z = par4;
			this.decorate();
			currentWorld = null;
			randomGenerator = null;
		}
	}


}
