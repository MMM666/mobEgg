﻿package net.minecraft.src;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.input.Keyboard;


public class IME_GuiMobEgg extends GuiScreen {
	
    private static RenderItem itemRenderer = new RenderItem();
    public int selectedNum[];
	
    public static final String def_randomEgg = "\247cRandomEgg";	// 表示用の文字列
    protected World fworld;


    public IME_GuiMobEgg(World world) {
		new IME_GuiMobEggRandom(world);
    	fworld = world;
    }

    @Override
    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int j = mod_IME_mobEgg.mobNames.length;
    	selectedNum = new int[j];
        for(int k = 0; k < j; k++) {
        	String ss = mod_IME_mobEgg.mobNames[k];
        	if (ss == null || ss.isEmpty()) {
        		// 空白の場合はランダム
        		ss = def_randomEgg;
        		selectedNum[k] = -1;
        	} else {
                for (int l = 0; l < j; l++) {
                	if (mod_IME_mobEgg.entityMap.keySet().toArray()[l].toString().equalsIgnoreCase(ss)) {
                		selectedNum[k] = l;
                		break;
                	}
                }
        	}
        	controlList.add(new GuiSmallButton(k, (width / 2 - 155) + (k % 2) * 160, height / 6 + 24 * (k >> 1) - 24, ss));
        }

        controlList.add(new GuiSmallButton(200, width / 2 + 20, height / 6 + 168, 120, 20, "Save"));
        controlList.add(new GuiSmallButton(300, width / 2 - 140, height / 6 + 168, 120, 20, "Random Select"));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id < 100)
        {
        	int i = guibutton.id;
        	// shiftで逆回し
        	if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
        		if (--selectedNum[i] < -1) {
        			selectedNum[i] = mod_IME_mobEgg.entityMap.size() - 1;
        		}
        	} else {
        		if (++selectedNum[i] >= mod_IME_mobEgg.entityMap.size()) {
        			selectedNum[i] =  -1;
        		}
        	}
        	if (selectedNum[i] == -1) {
            	guibutton.displayString = def_randomEgg;
            	mod_IME_mobEgg.mobNames[i] = "";
        	} else {
            	String s = mod_IME_mobEgg.entityMap.keySet().toArray()[selectedNum[i]].toString();
            	guibutton.displayString = mod_IME_mobEgg.mobNames[i] = s;
        		s = (new StringBuilder()).append("MobEgg ").append(s).toString();
        	}
        }
        if(guibutton.id == 200)
        {
        	mod_IME_mobEgg.saveParamater();
            mc.displayGuiScreen(null);
        }
        if(guibutton.id == 300)
        {
            mc.displayGuiScreen(new IME_GuiMobEggRandom(fworld, this));
        }
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
        
        // 卵と染料の表示
        ItemStack itemstack = new ItemStack(mod_IME_mobEgg.mobegg, 1, 0);
        ItemStack itemstack2 = new ItemStack(Item.dyePowder, 1, 0);
        
        for (int k = 0; k < selectedNum.length; k++) {
        	itemstack.setItemDamage(k);
        	itemstack2.setItemDamage(k);
        	int x = (width / 2 - 154) + (k % 2) * 160;
        	int y = height / 6 + 24 * (k >> 1) - 22;
            itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
            itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack2, x + 131, y);
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack2, x + 131, y);
        }
    }

}
