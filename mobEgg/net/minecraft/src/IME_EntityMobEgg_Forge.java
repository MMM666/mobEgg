package net.minecraft.src;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.*;


public class IME_EntityMobEgg_Forge extends IME_EntityMobEgg implements IEntityAdditionalSpawnData {

	public IME_EntityMobEgg_Forge(World world) {
		super(world);
	}
	
	public IME_EntityMobEgg_Forge(World world, double d, double d1, double d2, ItemStack itemstack) {
		super(world, d, d1, d2, itemstack);
	}
	
	public IME_EntityMobEgg_Forge(World world, EntityLiving entityliving, ItemStack itemstack) {
		super(world, entityliving, itemstack);
	}

//	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeInt(thrower == null ? entityId : thrower.entityId);
		data.writeInt(eggItemStack.getItemDamage());
	}

//	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		int lthrower = data.readInt();
		if (lthrower != 0) {
			Entity lentity = worldObj.getEntityByID(lthrower);
			if (lentity instanceof EntityLiving) {
				thrower = (EntityLiving)lentity;
			}
		}
		eggItemStack = new ItemStack(mod_IME_mobEgg.mobegg, 1, data.readInt());
	}

}
