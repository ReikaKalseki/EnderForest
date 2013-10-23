package Reika.EnderForest.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ForgeDirection;
import Reika.DragonAPI.Instantiable.BlockArray;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.EnderForest.EnderForest;

public class EnderPoolGenerator extends WorldGenerator {

	private static final List<Integer> placeables = new ArrayList();

	static {
		placeables.add(Block.dirt.blockID);
		placeables.add(Block.grass.blockID);
		placeables.add(Block.gravel.blockID);
		placeables.add(Block.stone.blockID);
		placeables.add(Block.sand.blockID);
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		for (int i = 2; i < 7; i++) {
			if (placeables.contains(world.getBlockId(x, y-i, z)))
				if (this.generateRandomShapeAt(world, random, x, y-i, z, 6))
					return true;
		}
		return false;
	}

	private boolean generateRandomShapeAt(World world, Random r, int x, int y, int z, int max) {
		int id = EnderForest.instance.getEnderBlockToGenerate().blockID;

		BlockArray blocks = new BlockArray();
		boolean gen = false;

		if (r.nextDouble() < 0.5)
			gen = this.generateCircularPool(world, x, y, z, r, id, blocks);
		else
			gen = this.generateEllipticalPool(world, x, y, z, r, id, blocks);

		if (gen) {
			//ReikaJavaLibrary.pConsole(blocks);
			for (int i = 0; i < blocks.getSize(); i++) {
				int[] xyz = blocks.getNthBlock(i);
				if (xyz[1] <= y)
					world.setBlock(xyz[0], xyz[1], xyz[2], id);
				else {
					world.setBlock(xyz[0], xyz[1], xyz[2], 0);
					int bid = world.getBlockId(xyz[0], xyz[1]-1, xyz[2]);
					if (bid == Block.dirt.blockID) {
						world.setBlock(xyz[0], xyz[1]-1, xyz[2], Block.grass.blockID);
					}
				}
			}
		}

		return gen;
	}

	private boolean generateCircularPool(World world, int x, int y, int z, Random rand, int id, BlockArray blocks) {
		int d = 2+rand.nextInt(3);
		int[] r = {d,d-1,d-3};
		for (int i = 0; i < 3; i++) {
			for (int j = -r[i]; j <= r[i]; j++) {
				for (int k = -r[i]; k <= r[i]; k++) {
					double dd = ReikaMathLibrary.py3d(j, 0, k);
					if (dd <= r[i]+0.5) {
						//world.setBlock(x+j, y-i, z+k, id);
						if (this.isValidBlock(world, x+j, y-i, z+k))
							blocks.addBlockCoordinate(x+j, y-i, z+k);
						else
							return false;
					}
				}
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = -r[i]-1; j <= r[i]+1; j++) {
				for (int k = -r[i]-1; k <= r[i]+1; k++) {
					double dd = ReikaMathLibrary.py3d(j, 0, k);
					if (dd <= r[i]+1.5) {
						//world.setBlock(x+j, y+i+1, z+k, 0);
						if (Block.blocksList[world.getBlockId(x+j, y+i+1, z+k)] instanceof BlockFluid)
							return false;
						blocks.addBlockCoordinate(x+j, y+i+1, z+k);
					}
				}
			}
		}
		return true;
	}

	private boolean generateEllipticalPool(World world, int x, int y, int z, Random rand, int id, BlockArray blocks) {
		int d = 2+rand.nextInt(3);
		int[] r = {d,d-1,d-3};
		double sc = 0.5+rand.nextDouble()*0.5;
		for (int i = 0; i < 3; i++) {
			for (int j = -r[i]; j <= r[i]; j++) {
				for (int k = (int)Math.floor(-r[i]*sc); k <= r[i]*sc; k++) {
					double dd = ReikaMathLibrary.py3d(j, 0, k);
					if (dd <= r[i]+0.5) {
						//world.setBlock(x+j, y-i, z+k, id);
						if (this.isValidBlock(world, x+j, y-i, z+k))
							blocks.addBlockCoordinate(x+j, y-i, z+k);
						else
							return false;
					}
				}
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = -r[i]-1; j <= r[i]+1; j++) {
				for (int k = (int)Math.floor(-r[i]*sc-1); k <= r[i]*sc+1; k++) {
					double dd = ReikaMathLibrary.py3d(j, 0, k);
					if (dd <= r[i]+1.5) {
						//world.setBlock(x+j, y+i+1, z+k, 0);
						if (Block.blocksList[world.getBlockId(x+j, y+i+1, z+k)] instanceof BlockFluid)
							return false;
						blocks.addBlockCoordinate(x+j, y+i+1, z+k);
					}
				}
			}
		}
		return true;
	}

	private boolean isValidBlock(World world, int x, int y, int z) {
		int id = EnderForest.instance.getEnderBlockToGenerate().blockID;
		int idx = world.getBlockId(x, y, z);
		if (ReikaWorldHelper.softBlocks(world, x, y, z))
			return false;
		ForgeDirection[] dirs = ForgeDirection.values();
		for (int i = 0; i < 6; i++) {
			ForgeDirection dir = dirs[i];
			int dx = x+dir.offsetX;
			int dy = y+dir.offsetY;
			int dz = z+dir.offsetZ;
			if (dir == ForgeDirection.UP) {
				int bid = world.getBlockId(dx, dy, dz);
				if (bid != 0 && Block.blocksList[bid] instanceof BlockFluid || bid == id)
					return false;
			}
			else {
				boolean soft = ReikaWorldHelper.softBlocks(world, dx, dy, dz);
				boolean ender = world.getBlockId(dx, dy, dz) == id;
				if (soft)
					return false;
			}
		}
		return true;
	}

}
