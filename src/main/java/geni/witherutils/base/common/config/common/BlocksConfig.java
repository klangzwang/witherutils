package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class BlocksConfig {

    public static ForgeConfigSpec.ConfigValue<Integer> ALLOYMAXENERGY;
    public static ForgeConfigSpec.ConfigValue<Integer> ALLOYSENDPERTICK;
    public static ForgeConfigSpec.ConfigValue<Integer> ALLOYUSEENERGY;
    
    public static ForgeConfigSpec.ConfigValue<Integer> FURNACEMAXENERGY;
    public static ForgeConfigSpec.ConfigValue<Integer> FURNACESENDPERTICK;
    public static ForgeConfigSpec.ConfigValue<Integer> FURNACEUSEENERGY;
    
    public static ForgeConfigSpec.ConfigValue<Integer> MINERMAXENERGY;
    public static ForgeConfigSpec.ConfigValue<Integer> MINERSENDPERTICK;
    public static ForgeConfigSpec.ConfigValue<Integer> MINERUSEENERGY;
    
    public static ForgeConfigSpec.ConfigValue<Integer> TANKDRUMCAPACITY;
    
    public static ForgeConfigSpec.ConfigValue<Integer> GREENHOUSECHANCE;
    public static ForgeConfigSpec.ConfigValue<Boolean> GREENHOUSEMOISTURE;
    
    public static ForgeConfigSpec.ConfigValue<Integer> COLLECTORRANGE;
    public static ForgeConfigSpec.ConfigValue<Integer> COLLECTORMAXITEMS;
    
	public static ForgeConfigSpec.ConfigValue<Integer> FISHINGTIMER;
	public static ForgeConfigSpec.ConfigValue<Integer> FISHINGCOOLDOWN;
	public static ForgeConfigSpec.ConfigValue<Integer> FISHINGMAXFOOD;
	
    public static ForgeConfigSpec.ConfigValue<Integer> ITEMCONTROLLERCAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> FLUIDCONTROLLERCAPACITY;
    
    public static ForgeConfigSpec.ConfigValue<Double> MACHINESOUND_VOLUME;
    
    public BlocksConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("blocks");
    	
	    	builder.push("alloy");
	    	
				builder.push("alloyMaxRF");
				ALLOYMAXENERGY = builder
		                .comment("Maximum RF storage that Alloy Furnace can hold")
		                .define("alloyMaxRF", 100000);
		        builder.pop();
		        
		        builder.push("alloyRFPerTick");
		        ALLOYSENDPERTICK = builder
		                .comment("RF per tick that Alloy Furnace can send")
		                .define("alloyRFPerTick", 2000);
		        builder.pop();
		        
		        builder.push("alloyUseEnergy");
		        ALLOYUSEENERGY = builder
		                .comment("RF per Task the Alloy Furnace needs")
		                .define("alloyUseEnergy", 30);
		        builder.pop();
		        
		    builder.pop();
	
	    	builder.push("furnace");
	    	
				builder.push("furnaceMaxRF");
				FURNACEMAXENERGY = builder
		                .comment("Maximum RF storage that Electro Furnace can hold")
		                .define("furnaceMaxRF", 100000);
		        builder.pop();
		        
		        builder.push("furnaceRFPerTick");
		        FURNACESENDPERTICK = builder
		                .comment("RF per tick that Electro Furnace can send")
		                .define("furnaceRFPerTick", 2000);
		        builder.pop();
		        
		        builder.push("furnaceUseEnergy");
		        FURNACEUSEENERGY = builder
		                .comment("RF per Task the Electro Furnace needs")
		                .define("furnaceUseEnergy", 100);
		        builder.pop();
	
		    builder.pop();
		    
	    	builder.push("miner");
	    	
				builder.push("minerMaxRF");
				MINERMAXENERGY = builder
		                .comment("Maximum RF storage that Miner can hold")
		                .define("minerMaxRF", 100000);
		        builder.pop();
		        
		        builder.push("minerRFPerTick");
		        MINERSENDPERTICK = builder
		                .comment("RF per tick that Miner can transfer")
		                .define("minerRFPerTick", 2000);
		        builder.pop();
		        
		        builder.push("minerUseEnergy");
		        MINERUSEENERGY = builder
		                .comment("RF per Operation the Miner needs")
		                .define("minerUseEnergy", 3000);
		        builder.pop();
		        
		    builder.pop();

	    	builder.push("tankdrum");
	    	
				builder.push("drumCapacity");
				TANKDRUMCAPACITY = builder
		                .comment("Maximum Value the Tank can hold. 28 x 1000 - 28000 for example.")
		                .define("drumCapacity", 28);
		        builder.pop();
		        
		    builder.pop();
		    
	    	builder.push("controller");
	    	
				builder.push("itemControllerCapacity");
				ITEMCONTROLLERCAPACITY = builder
		                .comment("Maximum Value the ItemController can hold.")
		                .define("itemControllerCapacity", 64 * 32 * 32 * 32 * 8);
		        builder.pop();
		        
				builder.push("fluidControllerCapacity");
				FLUIDCONTROLLERCAPACITY = builder
		                .comment("Maximum Value the FluidController can hold.")
		                .define("fluidControllerCapacity", Integer.MAX_VALUE);
		        builder.pop();
		        
		    builder.pop();
		    
	    	builder.push("greenhouse");
	    	
				builder.push("growChance");
				GREENHOUSECHANCE = builder
		                .comment("Lowering this, will increase the Chance, the Crop will bonemealed.")
		                .define("growChance", 1200);
		        builder.pop();

				builder.push("moistureNeed");
				GREENHOUSEMOISTURE = builder
		                .comment("Is a moistured Farmland Block needed?.")
		                .define("moistureNeed", true);
		        builder.pop();
		        
		    builder.pop();
		    
	    	builder.push("collector");
	    	
				builder.push("collectRange");
				COLLECTORRANGE = builder
						.comment("The Maximum Distance, the Collector can find Items to suck on.")
						.define("range", 5);

		        builder.pop();

				builder.push("collectMaxItems");
				COLLECTORMAXITEMS = builder
						.comment("How many Items the Collector can suck at the same time.")
						.define("maxItems", 20);
		        builder.pop();
		        
		    builder.pop();
		    
	    	builder.push("fisher");
	    	
				builder.push("timerForFishing");
				FISHINGTIMER = builder
						.comment("How many Ticks needed for one Fishing Fishes Fish.")
						.define("timerForFishing", 100);

		        builder.pop();

				builder.push("cooldownFishing");
				FISHINGCOOLDOWN = builder
						.comment("How many Ticks needed for one Fishing Fishes Fish Cooldown.")
						.define("cooldownFishing", 25);
		        builder.pop();

				builder.push("maxfoodFishing");
				FISHINGMAXFOOD = builder
						.comment("Maximum Amount of Food the Fisher can hold.")
						.define("maxfoodFishing", 25);
		        builder.pop();
		        
		    builder.pop();
		    
	    	builder.push("sound");
	    	
				builder.push("machinesound");
				MACHINESOUND_VOLUME = builder
		                .comment("The Volume for Machine Sounds. 0.0 will mute them all.")
		                .defineInRange("machinesound", 1.0, 0, 1);
		        builder.pop();
		        
		    builder.pop();
		    
        builder.pop();
    }
}
