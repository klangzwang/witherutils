package geni.witherutils.base.data.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import geni.witherutils.base.common.block.cutter.CutterBlock;
import geni.witherutils.base.common.block.cutter.CutterBlock.CutterBlockType;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class WitherUtilsLanguages extends LanguageProvider {
	
    private final Map<Supplier<String>, String> langEntries = new HashMap<>();
    public void add(Map<Supplier<String>, String> entries)
    {
        this.langEntries.putAll(entries);
    }

    public WitherUtilsLanguages(PackOutput gen, String modid, String locale)
    {
        super(gen, modid, locale);
    }

	@Override
    protected void addTranslations()
    {
        for (Map.Entry<Supplier<String>, String> entry : langEntries.entrySet()) {
            if (!entry.getValue().isEmpty())
                this.add(entry.getKey().get(), entry.getValue());
        }
		
        add("tooltip.witherutils.hint.extended", ">>> §9SHIFT§r Key for Tooltips.");
        
    	addItemName(WUTItems.SOULORB.get(), "§9SoulOrb§r");
    	addItemDesc(WUTItems.SOULORB.get(), "This Orb is filled with Souls, that the Wither Boss has trapped.", "Dimension listed below.", "You can either use them for crafting, or Right-Click to enhance your PlayerSoulPower.");

        add("itemGroup.witherutils.items", "§9WitherUtils - §fItems");
        add("itemGroup.witherutils.blocks", "§9WitherUtils - §fBlocks");
        add("itemGroup.witherutils.decos", "§9WitherUtils - §fDecos");
        
    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		final String blockNameId = BuiltInRegistries.BLOCK.getKey(cutterBlock).getPath();
    		addBlockName(cutterBlock, "§9" + blockNameId + "§r");
        	addBlockDesc(cutterBlock,
        			cutterBlock.getType() == CutterBlockType.CONNECTED ? "Connected Texture Block." : "",
        			cutterBlock.getType() == CutterBlockType.CONNECTED ? "Download the Athena Mod to show correctly" : "", "");
        }
        
        add("block.witherutils.anvil", "Anvil");
        add("block.witherutils.anvil.desc", "\n §9How to Become:§r \n Place a Vanilla Anvil and right Click it with a SoulOrb. \n\n §9How to use:§r \n Place Ingredients with Right Click. Use the Hammer, to smash it.");
        add("block.witherutils.angel", "Angel Block");
        add("block.witherutils.angel.desc", "\n§9How to use:§r\n Can be placed in Air. Look up and Click!");
        add("block.witherutils.case", "Case Block");
        add("block.witherutils.case.desc", "\n§9How to use:§r\n Can be used for Crafting!");
        
        add("block.witherutils.door_cased", "Door (Cased)");
        add("block.witherutils.door_cased.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.door_creep", "Door (Creep)");
        add("block.witherutils.door_creep.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.door_liron", "Door (LIron)");
        add("block.witherutils.door_liron.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.door_steel", "Door (Steel)");
        add("block.witherutils.door_steel.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.door_strip", "Door (Striped)");
        add("block.witherutils.door_strip.desc", "\n§9How to use:§r\n Vanilla Door.");
        
        add("block.witherutils.ctm_concrete_a", "Concrete A");
        add("block.witherutils.ctm_concrete_a.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_concrete_b", "Concrete B");
        add("block.witherutils.ctm_concrete_b.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_concrete_c", "Concrete C");
        add("block.witherutils.ctm_concrete_c.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_metal_a", "Metal A");
        add("block.witherutils.ctm_metal_a.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_stone_a", "Stone A");
        add("block.witherutils.ctm_stone_a.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_glass_a", "Glass A");
        add("block.witherutils.ctm_glass_a.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_glass_b", "Glass B");
        add("block.witherutils.ctm_glass_b.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_glass_c", "Glass C");
        add("block.witherutils.ctm_glass_c.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_dirt_a", "Dirt A");
        add("block.witherutils.ctm_dirt_a.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_dirt_b", "Dirt B");
        add("block.witherutils.ctm_dirt_b.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_dirt_c", "Dirt C");
        add("block.witherutils.ctm_dirt_c.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        add("block.witherutils.ctm_dirt_d", "Dirt D");
        add("block.witherutils.ctm_dirt_d.desc", "\n§9How to use:§r\n Needs Athena Mod.");
        
        add("item.witherutils.worm", "Cursed Worm");
        add("item.witherutils.worm.desc", "\n §9How to use:§r \nRight Click on Grass, Dirt or Farmland.");
        
        add("item.witherutils.wand", "Wand");
        add("item.witherutils.wand.desc", "\n §9WandAttack:§r \n §7Left-Click§r on Mobs WIP. \n\n §9WandPort:§r \n §7Right-Click§r Porting in direction you look at.");

        add("item.witherutils.wrench", "Wrench");
        add("item.witherutils.wrench.desc", "\n §9How to Use:§r \nThis is for rotating Blocks and give Xray for Camo Blocks.");
        
        add("item.witherutils.shield_basic", "WitherSteel Shield");
        add("item.witherutils.shield_basic.desc", "\n §9How to Use:§r \nRight Click to Block.");
        
        add("item.witherutils.cutter", "Cutter");
        add("item.witherutils.cutter.desc", "\n §9How to Use:§r \nAnother Version of Chisel like Tool. \nRight Click to open Gui, and using \nthe left Slot for Input Ingredients.");
        
        
        add("gui.witherutils.namesquares", "□■□ ");
        add("gui.witherutils.blockheads", "╘ Blockheads Inc.");
    }

    public void addItemName(Item key, String name)
    {
    	add(key.getDescriptionId(), name);
    }
    public void addItemDesc(Item key, String information, String howtobecome, String howtouse)
    {
    	String completeLines = "\n";

    	if(!information.isEmpty() && information != "NULL")
    		completeLines = completeLines + "■-§9 §nInformation:\n" + "§r§f" + information + "§r\n";
    	if(!howtobecome.isEmpty() && howtobecome != "NULL")
    		completeLines = completeLines + "■-§9 §nHowtoBecome:\n" + "§r§f" + howtobecome + "§r\n";
    	if(!howtouse.isEmpty() && howtouse != "NULL")
    		completeLines = completeLines + "■-§9 §nHowtoUse:\n" + "§r§f" + howtouse + "§r\n";
    	
        add(key.getDescriptionId() + ".desc", completeLines);
    }
    
    public void addBlockName(Block key, String name)
    {
    	add(key.getDescriptionId(), name);
    }
    public void addBlockDesc(Block key, String information, String howtobecome, String howtouse)
    {
    	String completeLines = "\n";

    	if(!information.isEmpty() && information != "NULL")
    		completeLines = completeLines + "■-§9 §nInformation:\n" + "§r§f" + information + "§r\n";
    	if(!howtobecome.isEmpty() && howtobecome != "NULL")
    		completeLines = completeLines + "■-§9 §nHowtoBecome:\n" + "§r§f" + howtobecome + "§r\n";
    	if(!howtouse.isEmpty() && howtouse != "NULL")
    		completeLines = completeLines + "■-§9 §nHowtoUse:\n" + "§r§f" + howtouse + "§r\n";
    	
        add(key.getDescriptionId() + ".desc", completeLines);
    }
}
