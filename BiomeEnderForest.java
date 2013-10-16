/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.EnderForest;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.biome.SpawnListEntry;
import Reika.DragonAPI.Auxiliary.ModList;

public class BiomeEnderForest extends BiomeGenForest {

	public BiomeEnderForest(int id) {
		super(id);
		spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 10, 1, 4)); //boost Enderman spawn rates

		if (ModList.THERMALEXPANSION.isLoaded()) {
			//resonant ender pools
		}
	}

}
