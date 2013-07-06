package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.util.List;

public class IME_ItemMobEgg extends ItemEgg {

	public IME_ItemMobEgg(int i) {
		super(i);
		maxStackSize = 64;
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		// 投げたときの処理
		if (!entityplayer.capabilities.isCreativeMode) {
			itemstack.stackSize--;
		}
		world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if(!world.isRemote) {
			IME_EntityMobEgg lentity = null;
			try {
				Constructor<IME_EntityMobEgg> lconstructor = mod_IME_mobEgg.classMobEgg.getConstructor(World.class, EntityLivingBase.class, ItemStack.class);
				lentity = lconstructor.newInstance(world, entityplayer, new ItemStack(itemstack.getItem(), 1, itemstack.getItemDamage()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			world.spawnEntityInWorld(lentity);
		}
		return itemstack;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		float[] lf = EntitySheep.fleeceColorTable[~(par1ItemStack.getItemDamage()) & 15];
		return (((int)(lf[0] * 255) & 0xff) << 16) | (((int)(lf[1] * 255) & 0xff) << 8) | (((int)(lf[2] * 255) & 0xff));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (mod_IME_mobEgg.mobNames[par1ItemStack.getItemDamage()].isEmpty()) {
			par3List.add(IME_GuiMobEgg.def_randomEgg);
		} else {
			par3List.add(mod_IME_mobEgg.mobNames[par1ItemStack.getItemDamage()]);
		}
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		// クリエイティブのタブに追加するサブアイテム
		for (int i = 0; i < 16; i++) {
			par3List.add(new ItemStack(mod_IME_mobEgg.mobegg, 1, i));
		}
	}

}
