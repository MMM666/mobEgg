package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;

public class mod_IME_mobEgg extends BaseMod {

	@MLProp(info="MobEgg's ItemID(on Editors number)")		// shiftedIndex�l
	public static int ItemID = 22201;			
	@MLProp(info="Spown mob List.(null is All random)")		// �ݒ肳��Ă���MOB�̃��X�g
	public static String mobList = "Spider,Zombie,Creeper,Cow,Enderman,PigZombie,Squid,Wolf,Ghast,Pig,Slime,Sheep,Skeleton,Silverfish,Giant,Chicken";
	@MLProp(info="RandomEgg Selection.")					// �����_���Ώۂ̃��X�g
	public static String randomList = "";
	@MLProp(info="GUI Enable(dont't use GUI is false)")		// GUI�̗L������
	public static boolean guiEnable = true;		
	@MLProp(info="Exclusion mob List.")						// �����_���Ώۂ̃��X�g
	public static String exclusionList = "";

	// �R���t�B�O���ڂ����Ƃ���@MLProp��L���ɂ���
//	@MLProp(info="Crafting ItemID")
	public static int CraftID = 344;	// ��

	
	public static Item mobegg;
	public static Map<String, Entity> entityMap;
	public static Map<String, Boolean> randomMap = new TreeMap<String, Boolean>();
	public static String mobNames[] = new String[16];
	public static List<String> exclusions = new ArrayList<String>();
	public static Class classMobEgg;


	@Override
	public String getVersion() {
		return "1.4.7-1";
	}

	@Override
	public String getName() {
		return "mobEgg";
	}
	
	@Override
	public String getPriorities() {
		return "required-after:mod_MMM_MMMLib";
	}
	
	@Override
	public void load() {
		entityMap = new TreeMap<String, Entity>();
		
		// cfg����̒l�����
		if (mobList != "") {
			// ���̐ݒ�l
			String s[] = mobList.split(",");
			for (int i = 0; i < mobNames.length; i++) {
				if (i < s.length){
					mobNames[i] = s[i].trim();
				} else {
					mobNames[i] = "";
				}
			}
		}
		if (exclusionList != "") {
			// ���O�Ώ�MOB
			String s[] = exclusionList.split(",");
			for (String t : s) {
				exclusions.add(t.trim());
			}
		}
		if (randomList != "") {
			// randomEgg�Ώ�MOB
			String s[] = randomList.split(",");
			for (String t : s) {
				t = t.trim();
				if (!exclusions.contains(t)) {
					randomMap.put(t, Boolean.TRUE);
				}
			}
		}
		
		// �A�C�e���̍쐬
		mobegg = new IME_ItemMobEgg(ItemID - 256).setIconCoord(9, 9).setItemName("MobEgg").setCreativeTab(CreativeTabs.tabMisc);
		ModLoader.addName(mobegg, "MobEgg");
		for (int i = 0; i < 16; i++) {
			// ���V�s�̒ǉ�
			if (Item.itemsList[CraftID] instanceof ItemBlock)
				ModLoader.addShapelessRecipe(new ItemStack(mobegg, 1, i), new Object[]{Block.blocksList[CraftID], new ItemStack(Item.dyePowder, 1, i)});
			else
				ModLoader.addShapelessRecipe(new ItemStack(mobegg, 1, i), new Object[]{Item.itemsList[CraftID], new ItemStack(Item.dyePowder, 1, i)});
		}
		// Entity�̓o�^
		classMobEgg = MMM_Helper.getEntityClass(this, "IME_EntityMobEgg");
		if (classMobEgg == null) {
			return;
		}
		int luid = ModLoader.getUniqueEntityId();
		ModLoader.registerEntityID(classMobEgg, "mobEgg", luid);
		ModLoader.addEntityTracker(this, classMobEgg, luid, 64, 10, true);
		
		
		// GUI ���J���L�[�̓o�^�Ɩ��̕ϊ��e�[�u���̓o�^
		if (guiEnable && MMM_Helper.isClient) {
			String s = "key.mobEgg";
			ModLoader.registerKey(this, new KeyBinding(s, 22), false);
			ModLoader.addLocalization(
					(new StringBuilder()).append(s).toString(),
					(new StringBuilder()).append("MobEggGui").toString()
					);
		}
		
		// �f�B�X�y���T�[
		ModLoader.addDispenserBehavior(mobegg, new IME_BehaviorMobEggDispense(null));
	}

	@Override
	public void keyboardEvent(KeyBinding keybinding) {
		// GUI���J��
		if (MMM_Helper.isForge && ModLoader.isGUIOpen(null)) {
//		if (mc != null && mc.theWorld != null && mc.currentScreen == null) {
			ModLoader.openGUI(MMM_Helper.mc.thePlayer, new IME_GuiMobEgg(MMM_Helper.mc.theWorld));
		}
	}

	@Override
	public void addRenderer(Map map) {
		// �������Ƃ���Render��ǉ�
		map.put(IME_EntityMobEgg.class, new IME_RenderMobEgg(Item.egg.getIconFromDamage(0)));
	}

	public static String getInnerEntityName(int index) {
		// �C���f�b�N�X�ɉ������ݒ�MOB�̖��̂�Ԃ�
		if (mobNames.length <= index) return "";
		return mobNames[index];
	}

	public static void saveParamater() {
		// mobEgg���̑I������cfg�t�@�C���ɕۑ�
		File cfgdir = new File(Minecraft.getMinecraftDir(), "/config/");
		if (cfgdir.exists()) {
			File file = new File(cfgdir, (new StringBuilder(String.valueOf(mod_IME_mobEgg.class.getSimpleName()))).append(".cfg").toString());
			if (file.exists() && file.canRead() && file.canWrite()) {
				// �R���t�B�O�t�@�C�����L��
				try {
					BufferedReader breader = new BufferedReader(new FileReader(file));
					String rl;
					String s;
					List<String> lines = new ArrayList<String>();
					while ((rl = breader.readLine()) != null) {
						s = "mobList";
						if (rl.startsWith(s)) {
							StringBuilder sb = new StringBuilder();
							for (String t : mobNames) {
								if (sb.length() > 0) {
									sb.append(",");
								} else {
									sb.append(s).append("=");
								}
								if (t != null) {
									sb.append(t);
								}
							}
							lines.add(sb.toString());
							continue;
						}
						s = "randomList";
						if (rl.startsWith(s)) {
							StringBuilder sb = new StringBuilder();
							for (Entry<String, Boolean> t : randomMap.entrySet()) {
								if (t.getValue()) {
									if (sb.length() > 0) {
										sb.append(",");
									} else {
										sb.append(s).append("=");
									}
									sb.append(t.getKey());
								}
							}
							lines.add(sb.toString());
							continue;
						}
						s = "exclusionList";
						if (rl.startsWith(s)) {
							StringBuilder sb = new StringBuilder();
							for (String t : exclusions) {
								if (sb.length() > 0) {
									sb.append(",");
								} else {
									sb.append(s).append("=");
								}
								if (t != null) {
									sb.append(t);
								}
							}
							lines.add(sb.toString());
							continue;
						}
						lines.add(rl);
					}
					breader.close();
					
					// �ۑ�
					if(!lines.isEmpty() && (file.exists() || file.createNewFile()) && file.canWrite())
					{
						BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
						for (String t : lines.toArray(new String[0])) {
							bwriter.write(t);
							bwriter.newLine();
						}
						bwriter.close();
					}
				} catch (Exception e) {
					System.out.println("cgf file fail.");
				}
			}
		}
		
	}

	@Override
	public Entity spawnEntity(int entityId, World world, double scaledX, double scaledY, double scaledZ) {
		// Modloader���ł͓Ǝ��ɐ�������̂ŗv��Ȃ��B
		if (!MMM_Helper.isForge) return null;
		
		try {
			Constructor<IME_EntityMobEgg> lconstructor = classMobEgg.getConstructor(World.class);
			IME_EntityMobEgg lentity = lconstructor.newInstance(world);
			lentity.entityId = entityId;
			lentity.setLocationAndAngles(scaledX, scaledY, scaledZ, 0F, 0F);
			
			return lentity;
		} catch (Exception e) {
		}
		return null;
	}

	// Modloader
	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		// ��p���𔭐�������
		EntityLiving lentity = ((IME_EntityMobEgg)var1).thrower;
		return new IME_PacketMobEgg(var1, 0, lentity == null ? 0 : lentity.entityId);
	}

}