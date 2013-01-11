package net.minecraft.src;

import org.lwjgl.input.Mouse;


public class IME_GuiMobEggRandom extends MMM_GuiMobSelect {
    
    public IME_GuiMobEgg parentScreen;

    
    public IME_GuiMobEggRandom(World pWorld, IME_GuiMobEgg guimobegg) {
    	super(pWorld);
        screenTitle = "mobEgg Randam Select";
        parentScreen = guimobegg;
        entityMap = mod_IME_mobEgg.entityMap;
    }
    
    public IME_GuiMobEggRandom(World pWorld) {
    	super(pWorld, mod_IME_mobEgg.entityMap);
    }

    @Override
    protected boolean checkEntity(String pName, Entity pEntity, int pIndex) {
    	if (!mod_IME_mobEgg.exclusions.contains(pName)) {
    		entityMap.put(pName, pEntity);
    	}
    	return false;
    }
    

    @Override
    public void initGui() {
    	super.initGui();
        StringTranslate stringtranslate = StringTranslate.getInstance();

        controlList.add(new GuiButton(200, width / 2 - 100, height - 40, 200, 20, stringtranslate.translateKey("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if(guibutton.id == 200) {
        	// êeGUIÇ÷ñﬂÇÈ
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

	@Override
	public void clickSlot(int pIndex) {
		String s = entityMap.keySet().toArray()[pIndex].toString();
		boolean rselect = mod_IME_mobEgg.randomMap.get(s);
		mod_IME_mobEgg.randomMap.put(s, Boolean.valueOf(!rselect));
		mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight,
			Tessellator pTessellator, String pName, Entity pEntity) {
        boolean rselect = false;
		if (mod_IME_mobEgg.randomMap.containsKey(pName)) {
	        rselect = mod_IME_mobEgg.randomMap.get(pName);
		} else {
			mod_IME_mobEgg.randomMap.put(pName, Boolean.valueOf(rselect));
		}
        int c;
        if (rselect) {
        	c = 0x3fff3f;
        } else {
        	c = 0xff3f3f;
        }
        drawCenteredString(fontRenderer, rselect ? "Select" : "Exclusion", width / 2, pY + 18, c);
        drawCenteredString(fontRenderer, pName, width / 2, pY + 6, 0xffffff);
	}

}
