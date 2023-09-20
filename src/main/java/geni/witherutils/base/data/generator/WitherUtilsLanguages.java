package geni.witherutils.base.data.generator;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

public class WitherUtilsLanguages extends LanguageProvider {
    
    public WitherUtilsLanguages(PackOutput gen, String locale)
    {
        super(gen, WitherUtils.MODID, locale);
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
    
    @Override
    protected void addTranslations()
    {
        add("itemGroup.witherutils.items", "§9WitherUtils - §fItems");
        add("itemGroup.witherutils.blocks", "§9WitherUtils - §fBlocks");
        add("itemGroup.witherutils.decos", "§9WitherUtils - §fDecos");
        
    	addItemName(WUTItems.HAMMER.get(), "§9Hammer§r");
    	addItemDesc(WUTItems.HAMMER.get(), "The hammer is a versatile Tool. It offers three Functions that can prove useful in various situations.", "NULL", "In Combination with the WitherAnvil, you can process materials and create additional items. Also serve as a weapon against enemies. It can demolish blocks while recovering the recipe's materials. So, if you accidentally break misplaced blocks, you can reclaim the resources for reuse.");

        add("jei.witherutils.anvil", "\n §6How to become:§r \n §0Place a Vanilla Anvil on Ground and right Click the Anvil with a SoulOrb. If something getting Hot or burns, you are finished and now, call the Firefighters.");
        add("jei.witherutils.soulorb", "\n §6How to become:§r \n §0Any Mob in the Nether will drop one Soul/Orb.");
        add("jei.witherutils.wrench", "\n §6How to Use:§r \n §0Can rotate Blocks and, look through Camo Slots/Blocks.");
        add("jei.witherutils.cauldron", "\n §6How to become:§r \n\n §0Place a Vanilla Cauldron on Ground and right Click the Anvil with a SoulOrb. If something getting Hot or burns, you are finished and call the Firefighters.");
        add("jei.witherutils.fertilizer", "\n §6How to become:§r \n\n §0Insert a Bone Block into the WitherCauldron. Wait a Minute, or two and you can OutBucket 1000mb/Bucket from it. \n\n §6How to use:§r \n\n §0Can be filled in the Farmer for extra Bonemeal Effects.");
        add("jei.witherutils.experience", "\n §6How to become:§r \n\n §0Step on Xp Handler Blocks, or any other Mods with can convert Experience to Fluid.");
        
        add("witherutils.armorpiercingattackdamage", "Piercing Damage");
        add("witherutils.godslayingattackdamage", "Slay Damage");
        add("witherutils.souldamage", "Soul Damage");
        
        add("enchantment.witherutils.energy", "Energy Upgrade");
        add("enchantment.witherutils.pearl", "Pearl Upgrade");
        add("enchantment.witherutils.feather_fall", "Feather Upgrade");
        add("enchantment.witherutils.solar_power", "Solar Upgrade");
        add("enchantment.witherutils.night_vision", "Vision Upgrade");
        add("enchantment.witherutils.sprinting", "Sprinting Upgrade");
        add("enchantment.witherutils.jumping", "Jumping Upgrade");
        add("enchantment.witherutils.squid_jump", "SquidJump Upgrade");

        add("block.witherutils.case_big", "Case (Big)");
        add("block.witherutils.case_small", "Case (Small)");
        
        add("block.witherutils.alloy_furnace", "Alloy Furnace");
        add("block.witherutils.alloy_furnace.desc", "\n §9How to use:§r \n Burn and Smelt your needed Items.");
        add("block.witherutils.electro_furnace", "Electro Furnace");
        add("block.witherutils.electro_furnace.desc", "\n §9How to use:§r \n Vanilla Energy Furnace.");
        add("block.witherutils.anvil", "Anvil");
        add("block.witherutils.anvil.desc", "\n §9How to Become:§r \n Place a Vanilla Anvil and right Click it with a SoulOrb. \n\n §9How to use:§r \n Place Ingredients with Right Click. Use the Hammer, to smash it.");
        add("block.witherutils.lava_generator", "Lava Energy Generator");
        add("block.witherutils.lava_generator.desc", "\n §9How to Use:§r \n Lava, Magma etc. around, will produce a small Amount of Energy.");
        add("block.witherutils.water_generator", "Water Energy Generator");
        add("block.witherutils.water_generator.desc", "\n §9How to Use:§r \n Every Water Stream coming from Sides produces a small Amount of Energy.");
        add("block.witherutils.wind_generator", "Wind Energy Generator");
        add("block.witherutils.wind_generator.desc", "\n §9How to Use:§r \n Generates Energy from Wind Streams.");
        add("block.witherutils.creative_generator", "Creative Energy Generator");
        add("block.witherutils.creative_generator.desc", "\n §9How to Use:§r \n Booowlie mooowwwlliieee Goomlie.");
        add("block.witherutils.tankreservoir", "Tank Reservoir");
        add("block.witherutils.tankreservoir.desc", "\n §9How to use:§r\n Same like Vanilla Infinite Four Buckets Water \nResource.");
        add("block.witherutils.creative_trash", "Creative Trash");
        add("block.witherutils.creative_trash.desc", "\n §9How to Use:§r \n Booowlie mooowwwlliieee Goomlie.");
        add("block.witherutils.angel", "Angel Block");
        add("block.witherutils.angel.desc", "\n§9How to use:§r\n Can be placed in Air. Look up and Click!");
        add("block.witherutils.online", "Online");
        add("block.witherutils.online.desc", "\n§9How to use:§r\n Enter a Username in GUI and if this \nUser is online -> Redstone Signal.");
        add("block.witherutils.miner_basic", "Miner (Basic)");
        add("block.witherutils.miner_basic.desc", "\n §9How to Use:§r\n Place a mineable Block in front. \nNeeds a Pickaxe and maybe Books in GUI.");
        add("block.witherutils.miner_adv", "Miner (Advanced)");
        add("block.witherutils.miner_adv.desc", "\n §9How to Use:§r\n Place a mineable Block in front. \nNeeds a Pickaxe and maybe Books in GUI.");
        add("block.witherutils.lilly", "Ender Lilly Seed");
        add("block.witherutils.lilly.desc", "\n §9How to Become:§r\n Rare Drop from Enderman. \n\n §9How to use:§r\n Can be placed, on a Hard Surface, ike Stones or Cobble. \nWill be burn under Sun or Light \n and grows faster, when in \nDark Areas.");
        add("block.witherutils.floorsensor", "Floor Sensor");
        add("block.witherutils.floorsensor.desc", "\n §9How to use:§r\n Can detect Players, Mobs etc. and \nsend a Signal to above. \nexample: can open Doors or activate RS Stuff.");
        add("block.witherutils.wallsensor", "Wall Sensor");
        add("block.witherutils.wallsensor.desc", "\n§9How to use:§r\n It detects you, or other Players, by \ncoming closer. Perfect mounting over Doors. \nSends Redstone to Block behind.");
        add("block.witherutils.tankdrum", "Tank Drum");
        add("block.witherutils.tankdrum.desc", "\n§9How to use:§r\n Click on the Tank with Buckets, or use the Gui, by \nleft for Full Buckets. Right Slots to become FluidBuckets. If LiquidXP is inside, you can RightClick with GlassBottles.");
        add("block.witherutils.smarttv", "SmartTV");
        add("block.witherutils.smarttv.desc", "\n§9How to use:§r\n Use the Buttons in the Gui and enter URL.");
        add("block.witherutils.xpwireless", "XP Wireless");
        add("block.witherutils.xpwireless.desc", "\n§9How to use:§r\n Place it on a Tank, and right Click with a PlayerId Card. \nRight Click will switch Input/Output. \nConverts Experience to Fluid and back.");
        add("block.witherutils.xpplate", "XP Plate");
        add("block.witherutils.xpplate.desc", "\n§9How to use:§r\n Place it on TOP of a Tank, and right Click with a PlayerId Card. \nRight Click will switch Input/Output. \nConverts Experience to Fluid and back.");
        add("block.witherutils.floodgate", "Flood Gate");
        add("block.witherutils.floodgate.desc", "\n §9How to use:§r\n Place, and open UI, you can toggle the Range View.");
        add("block.witherutils.totem", "Totem");
        add("block.witherutils.totem.desc", "\n §9How to use:§r\n Place Totem Items on the Totem for Actions.");
        
        add("block.witherutils.placer", "Placer");
		add("block.witherutils.placer.desc", "\n §9How to Use:§r\n Block placing in front of it. \nCan place multiple Blocks.");
        add("block.witherutils.clicker", "Clicker");
        add("block.witherutils.clicker.desc", "\n §9How to Place:§r\n Crouch and Click will place the Block to the Front. Good for scanning Entities. \n §9How to use:§r\nAuto LR Clicker with Speed Modifier and Crouching. \nSlot for Items/Tools.");
        add("block.witherutils.collector", "Collector");
        add("block.witherutils.collector.desc", "\n§9How to use:§r\nCollecting within a Range. Like Magnets do \nCan be configured with a Filter Card. \nCan burn and delete, if overflowing Slots.");
        add("block.witherutils.scanner", "Scanner");
        add("block.witherutils.scanner.desc", "\n §9How to Use:§r\n If something is configured in GUI \nthe Scanner will emit Redstone Signals.");
        add("block.witherutils.activator", "Activator");
        add("block.witherutils.activator.desc", "\n §9How to Use:§r\n Simply activates something in front of it. \nCan be configured.");
		add("block.witherutils.greenhouse", "Greenhouse");
		add("block.witherutils.greenhouse.desc", "\n§9How to use:§r\n Multiblock - Place above Crops will speed up \nany Aging Progress of Crops. Like Bonemeal");
		add("block.witherutils.spawner", "Spawner");
		add("block.witherutils.spawner.desc", "\n§9How to use:§r\n Put a MobBank Item in Slot. \nNeeds Power to spawn a Mob");
		add("block.witherutils.fisher", "Fisher");
		add("block.witherutils.fisher.desc", "\n §9How to Place:§r\n Needs Water around. \n §9How to use:§r\n Needs a Fishing Rod in a Slot. \nNeeds Food in a Slot. \nIf you enable the AutoFeed Button \nthe Fisher looks for Food in \nthe Output Slots.");
		add("block.witherutils.farmer", "Farmer");
		add("block.witherutils.farmer.desc", "\n §9How to use:§r\n Needs a Hoe and Seeds. Water is optional. \nFertilizer Fluid have BoneMeal Effects.");
		add("block.witherutils.cauldron", "Cauldron");
		add("block.witherutils.cauldron.desc", "\n §9How to use:§r\n Right Click with Crafting Items/Blocks and wait for Smelting.");
		add("block.witherutils.slicedconcrete_black", "Sliced Concrete (Black)");
		add("block.witherutils.slicedconcrete_black.desc", "\n §9How to use:§r\n Can be placed around all Faces.");
		add("block.witherutils.slicedconcrete_gray", "Sliced Concrete (Gray)");
		add("block.witherutils.slicedconcrete_gray.desc", "\n §9How to use:§r\n Can be placed around all Faces.");
		add("block.witherutils.slicedconcrete_white", "Sliced Concrete (White)");
		add("block.witherutils.slicedconcrete_white.desc", "\n §9How to use:§r\n Can be placed around all Faces.");

		add("block.witherutils.rack_case", "Rack Case");
        add("block.witherutils.rack_case.desc", "\n§9How to:§r\n This block is used to create a Data Rack. Create a hollow 3x3x3 structure with at least one Rack Controller in the Middle, and one or more Rack Terminals in a face (not edge or corner) of the structure.");
		add("block.witherutils.rack_terminal", "Rack Terminal");
        add("block.witherutils.rack_terminal.desc", "\n§9How to:§r\n This block is the Input and Output of the Data Rack. For more Info, read the Rack Case Tooltip.");
		add("block.witherutils.rackitem_controller", "Rack Item Controller");
        add("block.witherutils.rackitem_controller.desc", "\n§9How to:§r\n This block is the Heart of the Data Rack. For more Info, read the Rack Case Tooltip.");
		add("block.witherutils.rackfluid_controller", "Rack Fluid Controller");
        add("block.witherutils.rackfluid_controller.desc", "\n§9How to:§r\n This block is the Heart of the Data Rack. For more Info, read the Rack Case Tooltip.");

        add("block.witherutils.catwalk", "Catwalk");
        add("block.witherutils.catwalk.desc", "\n§9How to use:§r\nLike all Decorations work on Planet Earth.");
        add("block.witherutils.steel_pole", "Steel Pole");
        add("block.witherutils.steel_pole.desc", "\n§9How to use:§r\nLike all Decorations work on Planet Earth.");
        add("block.witherutils.steel_pole_head", "Steel Pole Head");
        add("block.witherutils.steel_pole_head.desc", "\n§9How to use:§r\nLike all Decorations work on Planet Earth.");
        add("block.witherutils.steel_railing", "Steel Railing");
        add("block.witherutils.steel_railing.desc", "\n§9How to use:§r\nLike all Decorations work on Planet Earth.");
               
        add("block.witherutils.bricks_dark", "Bricks Dark");
        add("block.witherutils.bricks_dark.desc", "\n§9How to use:§r\n Nothing Special.");
        add("block.witherutils.bricks_lava", "Bricks Lava");
        add("block.witherutils.bricks_lava.desc", "\n§9How to use:§r\n Nothing Special. \nwill stop Burning when Rain is gone.");
        
        add("block.witherutils.cased_door", "Door (Cased)");
        add("block.witherutils.cased_door.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.creep_door", "Door (Creep)");
        add("block.witherutils.creep_door.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.liron_door", "Door (LIron)");
        add("block.witherutils.liron_door.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.steel_door", "Door (Steel)");
        add("block.witherutils.steel_door.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.striped_door", "Door (Striped)");
        add("block.witherutils.striped_door.desc", "\n§9How to use:§r\n Vanilla Door.");
        add("block.witherutils.metaldoor", "Door (Metal)");
        add("block.witherutils.metaldoor.desc", "\n§9How to use:§r\n Vanilla Door.");
        
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

        add("block.witherutils.core", "Battery Core");
        add("block.witherutils.core.desc", "\n§9How to Construct:§r\n Place down and Building Guide will \nshow you the needed Blocks around.");
        add("block.witherutils.stab", "Battery Stab");
        add("block.witherutils.stab.desc", "\n§9How to Construct:§r\n Place a Stab on four Sides of \nthe Battery Core.");
        add("block.witherutils.pylon", "Battery Pylon");
        add("block.witherutils.pylon.desc", "\n§9How to Use:§r\n Place in near of the Battery \nConnect Energy Cables to Input/Output.");
        
        add("block.witherutils.solar_case", "Solar Panel (Empty)");
        add("block.witherutils.solar_case.desc", "\n §9How to use:§r\n Part of Solar Panel Crafting Recipes.");
        add("block.witherutils.solar_basic", "Solar Panel (Basic)");
        add("block.witherutils.solar_basic.desc", "\n §9How to Use:§r\n Generates Energy from Sunlight. \nBottom is Power Output.");
        add("block.witherutils.solar_adv", "Solar Panel (Advanced)");
        add("block.witherutils.solar_adv.desc", "\n §9How to Use:§r\n Generates Energy from Sunlight. \nBottom is Power Output.");
        add("block.witherutils.solar_ultra", "Solar Panel (Ultra)");
        add("block.witherutils.solar_ultra.desc", "\n §9How to Use:§r\n Generates Energy from Sunlight. \nBottom is Power Output.");
        
        add("block.witherutils.withersteel_block", "WitherSteel Block");
        add("item.witherutils.withersteel_ingot", "WitherSteel Ingot");
        add("item.witherutils.withersteel_nugget", "WitherSteel Nugget");
        add("item.witherutils.withersteel_plate", "WitherSteel Plate");
        add("item.witherutils.withersteel_gear", "WitherSteel Gear");
        
        add("block.witherutils.soulished_block", "Soulished Block");
        add("item.witherutils.soulished_ingot", "Soulished Ingot");
        add("item.witherutils.soulished_nugget", "Soulished Nugget");
        
        add("item.witherutils.iron_plate", "Iron Plate");
        add("item.witherutils.iron_gear", "Iron Gear");
        add("item.witherutils.iron_rod", "Iron Rod");
        
        add("item.witherutils.enderpearl_shard", "Enderpearl Shard");
        
        add("item.witherutils.blink_plate", "Blink Plate");
        
        add("item.witherutils.shovel", "Shovel");
        add("item.witherutils.fan", "Fan");
        add("item.witherutils.spiral", "Spiral");
        add("item.witherutils.anchor", "Anchor");
        
        add("item.witherutils.sword", "Sword");
        add("item.witherutils.sword.desc", "\n §9SwordAttack:§r \n §7Left-Click§r on Mobs for standard SwordHandling. \n\n §9SwordDash:§r \n §7Right-Click§r holding and tab Keys to Walking Direction for Dashing.");

        add("item.witherutils.wand", "Wand");
        add("item.witherutils.wand.desc", "\n §9WandAttack:§r \n §7Left-Click§r on Mobs WIP. \n\n §9WandPort:§r \n §7Right-Click§r Porting in direction you look at.");

        add("item.witherutils.shield_basic", "WitherSteel Shield");
        add("item.witherutils.shield_basic.desc", "\n §9How to Use:§r \nRight Click to Block.");

        add("item.witherutils.shield_adv", "WitherSteel Shield Advanced");
        add("item.witherutils.shield_adv.desc", "\n §9How to Use:§r \nRight Click to Block.");
        
        add("item.witherutils.shield_rotten", "WitherSteel Rotten Shield");
        add("item.witherutils.shield_rotten.desc", "\n §9How to Use:§r \nRight Click to Block.");
        
        add("item.witherutils.shield_energy", "WitherSteel Energy Shield");
        add("item.witherutils.shield_energy.desc", "\n §9How to Use:§r \nRight Click to Block.");
        
        add("item.witherutils.wrench", "Wrench");
        add("item.witherutils.wrench.desc", "\n §9How to Use:§r \nThis is for rotating Blocks and give Xray for Camo Blocks.");

        add("item.witherutils.cutter", "Cutter");
        add("item.witherutils.cutter.desc", "\n §9How to Use:§r \nAnother Version of Chisel like Tool. \nRight Click to open Gui, and using \nthe left Slot for Input Ingredients.");
    	
        add("item.witherutils.soulorb", "SoulOrb");
        add("item.witherutils.soulorb.desc", "\n §9How to become:§r \nAny Mob in the Nether will drop one Soul/Orb.");

        add("item.witherutils.worm", "Cursed Worm");
        add("item.witherutils.worm.desc", "\n §9How to use:§r \nRight Click on Grass, Dirt or Farmland.");
        
        add("item.witherutils.soulbank_case", "Soul Energy Bank (Empty)");
        add("item.witherutils.soulbank_basic", "Soul Energy Bank (Basic)");
        add("item.witherutils.soulbank_adv", "Soul Energy Bank (Advanced)");
        add("item.witherutils.soulbank_ultra", "Soul Energy Bank (Ultra)");
        
        add("item.witherutils.card", "Card");
        add("item.witherutils.card.desc", "\n §9How to use:§r \nCrouch and Right Click for ItemFilter. \nRight Click to save PlayerName and Id. \nLeft Click on Mobs to save the MobName and Id.");
    
        add("item.witherutils.remote", "Remote");
        add("item.witherutils.remote.desc", "\n §9How to use:§r \nAllows you to wirelessly control a TV. To change the channel, simply right click in the direction of the TV. You can turn on/off the TV by sneaking instead.");

        add("item.witherutils.steelarmor_boots", "WitherSteel Boots Armor");
		add("item.witherutils.steelarmor_boots.desc", "Boots Armor");
		add("item.witherutils.steelarmor_chest", "WitherSteel Chest Armor");
		add("item.witherutils.steelarmor_chest.desc", "Chest Armor");
		add("item.witherutils.steelarmor_helmet", "WitherSteel Helmet Armor");
		add("item.witherutils.steelarmor_helmet.desc", "Helmet Armor");
		add("item.witherutils.steelarmor_leggings", "WitherSteel Leggings Armor");
		add("item.witherutils.steelarmor_leggings.desc", "Leggings Armor");
        
		add("item.witherutils.upcase", "Blank Upgrade");
		add("item.witherutils.upcase.desc", "\n §9How to Use:§r \nBasic Blank Case for all Withersteel \nthis Upgrades and Items.");
		add("item.witherutils.upgrade_vision", "NightVision Upgrade");
		add("item.witherutils.upgrade_vision.desc", "\n §9How to Anvil:§r \nInsert your Energized Helmet and \nthis Upgrade into a Vanilla Anvil.");
		add("item.witherutils.upgrade_speed", "WalkingSpeed Upgrade");
		add("item.witherutils.upgrade_speed.desc", "\n §9How to Anvil:§r \nInsert your Energized Leg and this \nUpgrade into a Vanilla Anvil.");
		add("item.witherutils.upgrade_jump", "DoubleJump Upgrade");
		add("item.witherutils.upgrade_jump.desc", "\n §9How to Anvil:§r \nInsert your Energized Feet and this \nUpgrade into a Vanilla Anvil.");
		add("item.witherutils.upgrade_feather", "FeatherFalling Upgrade");
		add("item.witherutils.upgrade_feather.desc", "\n §9How to Anvil:§r \nInsert your Energized Chest and this \nUpgrade into a Vanilla Anvil.");
		add("item.witherutils.upgrade_squid", "SquidJumping Upgrade");
		add("item.witherutils.upgrade_squid.desc", "\n §9How to Anvil:§r \nInsert your Energized Feet and this \nUpgrade into a Vanilla Anvil.");

		add("item.witherutils.feather", "Rotten Feather");
		add("item.witherutils.feather.desc", "\n §9How to Become:§r \nUse Vanilla Shears on a Chicken and look, \nwhat you did.");
		
		add("item.witherutils.egg_cursedzombie", "Cursed Zombie");
		add("item.witherutils.egg_cursedcreeper", "Cursed Creeper");
		add("item.witherutils.egg_cursedskeleton", "Cursed Skeleton");
		add("item.witherutils.egg_cursedspider", "Cursed Spider");
		
        add("item.witherutils.experience_bucket", "Experience Bucket");
        add("item.witherutils.fertilizer_bucket", "Fertilizer Bucket");
        add("fluid_type.witherutils.experience", "Experience Fluid");
        add("fluid_type.witherutils.fertilizer", "Fertilizer Fluid");
        
        add("gui.witherutils.namesquares", "□■□ ");
        add("gui.witherutils.blockheads", "╘ Blockheads Inc.");

        add("gui.witherutils.progress", "Progress %s%%");
        add("gui.witherutils.redstone_mode", "Redstone Mode");
        add("gui.witherutils.redstone_always_active", "Always active");
        add("gui.witherutils.redstone_active_with_signal", "Active with signal");

        add("tooltip.witherutils.temperature.deg_c", "%s °C");
        add("tooltip.witherutils.durability", "Durability");
        add("tooltip.witherutils.ispowered", "Powered");
        add("tooltip.witherutils.isnotpowered", "NotPowered");
        add("tooltip.witherutils.powerstatus", "Status");
        add("tooltip.witherutils.stored", "Stored");
        add("tooltip.witherutils.energy_amount", "%s FE");
        add("tooltip.witherutils.hint.extended", ">>> §9SHIFT§r Key for Tooltips.");
        add("tooltip.witherutils.soulbank.needed", "Insert SoulBank");
        add("tooltip.witherutils.soulbank.base", "Bank Type: %s");
        add("tooltip.witherutils.soulbank.energy_capacity", "Energy Capacity: %s");
        add("tooltip.witherutils.soulbank.energy_transfer", "Energy Transfer: %s");
        add("tooltip.witherutils.soulbank.energy_use", "Energy Use: %s");
        
        add("info.witherutils.owner", "Owner: %s");
        add("info.witherutils.clicktobind", "Right click to bind.");
        add("info.witherutils.nobinding", "This Card is already bound to %s!");
        
        add("info.witherutils.farm.outputfull", "Output Full");
        add("info.witherutils.farm.noseeds", "No Seeds");
        add("info.witherutils.farm.noaxe", "No Axe");
        add("info.witherutils.farm.nohoe", "No Hoe");
        add("info.witherutils.farm.noshears", "No Shears");
        add("info.witherutils.farm.notreetap", "No Treetap");
        add("info.witherutils.farm.nopower", "No Power");
        add("info.witherutils.farm.nosoulbank", "No SoulBank");
        add("info.witherutils.farm.nowater", "No Water");
    }
}
