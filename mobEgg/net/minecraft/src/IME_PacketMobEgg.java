package net.minecraft.src;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

public class IME_PacketMobEgg extends Packet23VehicleSpawn {

	public int damage;


	public IME_PacketMobEgg(Entity par1Entity, int par2, int par3) {
		super(par1Entity, par2, par3);
		
		damage = ((IME_EntityMobEgg)par1Entity).eggItemStack.getItemDamage();
	}

	@Override
	public void readPacketData(DataInput par1DataInput) throws IOException {
		super.readPacketData(par1DataInput);
		damage = par1DataInput.readInt();
	}

	@Override
	public void writePacketData(DataOutput par1DataOutput) throws IOException {
		super.writePacketData(par1DataOutput);
		par1DataOutput.writeInt(damage);
	}

	@Override
	public int getPacketSize() {
		return super.getPacketSize() + 4;
	}

	@Override
	public void processPacket(NetHandler par1NetHandler) {
		if (par1NetHandler instanceof NetClientHandler) {
			Minecraft mc = MMM_Helper.mc;
			WorldClient lworld = mc.theWorld;
			double lx = (double)this.xPosition / 32.0D;
			double ly = (double)this.yPosition / 32.0D;
			double lz = (double)this.zPosition / 32.0D;
			
			IME_EntityMobEgg lentity = null;
			try {
				Constructor<IME_EntityMobEgg> lconstructor = mod_IME_mobEgg.classMobEgg.getConstructor(World.class, double.class, double.class, double.class, ItemStack.class);
				lentity = lconstructor.newInstance(lworld, lx, ly, lz, new ItemStack(mod_IME_mobEgg.mobegg, 1, damage));
			} catch (Exception e) {
			}
			lentity.serverPosX = this.xPosition;
			lentity.serverPosY = this.yPosition;
			lentity.serverPosZ = this.zPosition;
			lentity.rotationYaw = 0.0F;
			lentity.rotationPitch = 0.0F;
			lentity.entityId = this.entityId;
			
			Entity le = (mc.thePlayer.entityId == throwerEntityId) ? mc.thePlayer : lworld.getEntityByID(throwerEntityId);
			if (le instanceof EntityLivingBase) {
//				lentity.thrower = (EntityLiving)le;
			}
			
			lentity.setVelocity((double)Float.intBitsToFloat(this.speedX), (double)Float.intBitsToFloat(this.speedY), (double)Float.intBitsToFloat(this.speedZ));
			lworld.addEntityToWorld(this.entityId, lentity);
		}
	}

}
