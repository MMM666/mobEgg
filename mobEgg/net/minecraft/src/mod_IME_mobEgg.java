package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class mod_IME_mobEgg extends BaseMod {

	@MLProp(info="MobEgg's ItemID(on Editors number)")		// shiftedIndex値
	public static int ItemID = 22201;			
	@MLProp(info="Spown mob List.(null is All random)")		// 設定されているMOBのリスト
	public static String mobList = "Spider,Zombie,Creeper,Cow,Enderman,PigZombie,Squid,Wolf,Ghast,Pig,Slime,Sheep,Skeleton,Silverfish,Giant,Chicken";
	@MLProp(info="RandomEgg Selection.")					// ランダム対象のリスト
	public static String randomList = "";
	@MLProp(info="GUI Enable(dont't use GUI is false)")		// GUIの有効無効
	public static boolean guiEnable = true;
	@MLProp(info="Exclusion mob List.")						// ランダム対象のリスト
	public static String exclusionList = "";

	// コンフィグ項目を作るときは@MLPropを有効にする
//	@MLProp(info="Crafting ItemID")
	public static int CraftID = 344;	// 卵
	
	
	public static Item mobegg;
	public static Map<String, Entity> entityMap;
	public static Map<String, Boolean> randomMap = new TreeMap<String, Boolean>();
	public static String mobNames[] = new String[16];
	public static List<String> exclusions = new ArrayList<String>();
	public static Class classMobEgg;


	@Override
	public String getName() {
		return "mobEgg";
	}

	@Override
	public String getPriorities() {
		return "required-after:mod_MMM_MMMLib";
	}

	@Override
	public String getVersion() {
		return "1.6.2-2";
	}

	@Override
	public void load() {
		// MMMLibのRevisionチェック
		MMM_Helper.checkRevision("3");
		
		entityMap = new TreeMap<String, Entity>();
		
		// cfgからの値を解析
		if (mobList != "") {
			// 卵の設定値
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
			// 除外対象MOB
			String s[] = exclusionList.split(",");
			for (String t : s) {
				exclusions.add(t.trim());
			}
		}
		if (randomList != "") {
			// randomEgg対象MOB
			String s[] = randomList.split(",");
			for (String t : s) {
				t = t.trim();
				if (!exclusions.contains(t)) {
					randomMap.put(t, Boolean.TRUE);
				}
			}
		}
		
		// アイテムの作成
		mobegg = new IME_ItemMobEgg(ItemID - 256).setUnlocalizedName("MobEgg").func_111206_d("spawn_egg").setCreativeTab(CreativeTabs.tabMisc);
		ModLoader.addName(mobegg, "MobEgg");
		for (int i = 0; i < 16; i++) {
			// レシピの追加
			if (Item.itemsList[CraftID] instanceof ItemBlock)
				ModLoader.addShapelessRecipe(new ItemStack(mobegg, 1, i), new Object[]{Block.blocksList[CraftID], new ItemStack(Item.dyePowder, 1, i)});
			else
				ModLoader.addShapelessRecipe(new ItemStack(mobegg, 1, i), new Object[]{Item.itemsList[CraftID], new ItemStack(Item.dyePowder, 1, i)});
		}
		// Entityの登録
		classMobEgg = MMM_Helper.getForgeClass(this, "IME_EntityMobEgg");
		if (classMobEgg == null) {
			return;
		}
		MMM_Helper.registerEntity(classMobEgg, "mobEgg", 0, this, 64, 10, true);
		
		// GUI を開くキーの登録と名称変換テーブルの登録
		if (guiEnable && MMM_Helper.isClient) {
			String s = "key.mobEgg";
			ModLoader.registerKey(this, new KeyBinding(s, 22), false);
			ModLoader.addLocalization(
					(new StringBuilder()).append(s).toString(),
					(new StringBuilder()).append("MobEggGui").toString()
					);
		}
		
		// ディスペンサー
		ModLoader.addDispenserBehavior(mobegg, new IME_BehaviorMobEggDispense());
	}

	@Override
	public void keyboardEvent(KeyBinding keybinding) {
		// GUIを開く
		IME_GuiMobEgg.keyboardEvent(keybinding);
	}

	@Override
	public void addRenderer(Map map) {
		// 投げたときのRenderを追加
		map.put(IME_EntityMobEgg.class, new IME_RenderMobEgg(mobegg));
	}

	public static String getInnerEntityName(int index) {
		// インデックスに応じた設定MOBの名称を返す
		if (mobNames.length <= index) return "";
		return mobNames[index];
	}

	// Modloader
	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		// 専用卵を発生させる
		EntityLivingBase lentity = ((IME_EntityMobEgg)var1).thrower;
		return new IME_PacketMobEgg(var1, 0, lentity == null ? 0 : lentity.entityId);
	}

}
