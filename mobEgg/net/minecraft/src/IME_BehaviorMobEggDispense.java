package net.minecraft.src;

import java.lang.reflect.Constructor;

import net.minecraft.server.MinecraftServer;

public class IME_BehaviorMobEggDispense extends BehaviorProjectileDispense {

	protected ItemStack fitemstack;


	@Override
	public ItemStack dispenseStack(IBlockSource par1iBlockSource, ItemStack par2ItemStack) {
		// ’†g‚ğ¯•Ê‚·‚é‚½‚ß‚ÉItemStack‚ğŠm•Û
		fitemstack = par2ItemStack;
		return super.dispenseStack(par1iBlockSource, par2ItemStack);
	}

	@Override
	protected IProjectile getProjectileEntity(World var1, IPosition var2) {
		try {
			Constructor<IME_EntityMobEgg> lconstructor = mod_IME_mobEgg.classMobEgg.getConstructor(World.class, double.class, double.class, double.class, ItemStack.class);
			return lconstructor.newInstance(var1, var2.getX(), var2.getY(), var2.getZ(), fitemstack);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
