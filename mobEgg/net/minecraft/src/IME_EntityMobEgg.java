package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.MaskFormatter;

public class IME_EntityMobEgg extends EntityThrowable {
	
	public ItemStack eggItemStack;
	public EntityLivingBase thrower;

	// Method
	public IME_EntityMobEgg(World world) {
		super(world);
	}

	public IME_EntityMobEgg(World world, double d, double d1, double d2, ItemStack itemstack) {
		super(world, d, d1, d2);

		eggItemStack = itemstack;
	}
	
	public IME_EntityMobEgg(World world, EntityLivingBase entityliving, ItemStack itemstack) {
		super(world, entityliving);
		
		thrower = entityliving;
		eggItemStack = itemstack;
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		// ”òãÄ‘Ì‚ÌÚG”»’è
		if(movingobjectposition.entityHit != null) {
			if(!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0));
		}
		if (eggItemStack == null) {
			return;
		}
		if(!worldObj.isRemote) {
			// ’…’eˆ—
			String s = mod_IME_mobEgg.getInnerEntityName(eggItemStack.getItemDamage());
			if (s.isEmpty()) {
				// İ’è‚ª‚È‚¢ê‡‚Íƒ‰ƒ“ƒ_ƒ€‚Å‘ÎÛ‚ğw’è
				List<String> ll = new ArrayList<String>();
				for (Entry<String, Boolean> t : mod_IME_mobEgg.randomMap.entrySet()) {
					if (t.getValue()) {
						ll.add(t.getKey());
					}
				}
				if (ll.isEmpty()) return;
				s = ll.get(rand.nextInt(ll.size()));
			}
			
			// EntityList‚©‚çµŠ«‘ÎÛ‚ğ’Šo‚·‚é
			Entity entity = EntityList.createEntityByName(s, worldObj);
			if (entity instanceof EntityLiving) {
				// ‰Šúİ’è’l‚ğ—^‚¦‚é
				((EntityLiving)entity).func_110161_a(null);
			}
			if (entity != null) {
				entity.setLocationAndAngles(posX, posY + 1.0F, posZ, rotationYaw, 0.0F);
				worldObj.spawnEntityInWorld(entity);
			}
			setDead();
		}
		for (int i = 0; i < 8; i++) {
			worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		}
		
	}

	public int getItemDamage() {
		return eggItemStack == null ? 0 : eggItemStack.getItemDamage();
	}

}
