package Reika.EnderForest.ItemBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.EnderForest.EnderForest;
import Reika.EnderForest.Registry.EnderOptions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLiquidEnder extends BlockFluidClassic {

	private Icon[] theIcon = new Icon[2];

	public BlockLiquidEnder(int par1, Material par2Material) {
		//super(par1, EnderForest.ender, par2Material);
		super(par1, EnderForest.ender, Material.water);

		this.setHardness(100F);
		this.setLightOpacity(0);
		this.setResistance(500);
		renderPass = 0;
	}

	@Override
	public int getRenderType() {
		return 4;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ico) {
		theIcon = new Icon[]{ico.registerIcon("enderforest:ender"), ico.registerIcon("enderforest:flowingender")};
	}

	@Override
	public Fluid getFluid() {
		return FluidRegistry.getFluid("ender");
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		world.setBlock(x, y, z, 0);
		return new FluidStack(FluidRegistry.getFluid("ender"), 1000);
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity e) {
		if (!EnderOptions.ENDER.getState())
			return;
		double v = 1;
		e.motionX = ReikaRandomHelper.getRandomPlusMinus(0, v);
		e.motionZ = ReikaRandomHelper.getRandomPlusMinus(0, v);
		e.motionY += 0.2;
		//e.motionZ = -e.motionZ;
		if (e instanceof EntityLivingBase)
			e.playSound("mob.endermen.portal", 0.5F, 1.0F);
	}

	@Override
	public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec)
	{
		if (densityDir > 0) return;
		Vec3 vec_flow = this.getFlowVector(world, x, y, z);
		vec.xCoord += vec_flow.xCoord * (quantaPerBlock * 4);
		vec.yCoord += vec_flow.yCoord * (quantaPerBlock * 4);
		vec.zCoord += vec_flow.zCoord * (quantaPerBlock * 4);
	}

	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public Icon getIcon(int s, int meta) {
		return meta == 0 ? theIcon[0] : theIcon[1];
	}
}
