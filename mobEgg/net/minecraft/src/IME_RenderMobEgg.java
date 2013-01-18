package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class IME_RenderMobEgg extends RenderSnowball {

	public IME_RenderMobEgg(int i) {
		super(i);
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		IME_EntityMobEgg entitymobegg = (IME_EntityMobEgg)entity;
		ItemStack itemstack = entitymobegg.eggItemStack;
		
		// 投げた卵に色をつける
		int k = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
		float f15 = (float)(k >> 16 & 0xff) / 255F;
		float f17 = (float)(k >> 8 & 0xff) / 255F;
		float f19 = (float)(k & 0xff) / 255F;
		float f21 = entitymobegg.getBrightness(f1);
		GL11.glColor4f(f15 * f21, f17 * f21, f19 * f21, 1.0F);
		
		super.doRender(entity, d, d1, d2, f, f1);
	}

}
