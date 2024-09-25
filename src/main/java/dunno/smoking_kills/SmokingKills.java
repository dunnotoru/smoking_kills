package dunno.smoking_kills;

import dunno.smoking_kills.block.ModBlocks;
import dunno.smoking_kills.item.ModItems;
import dunno.smoking_kills.recipes.ModRecipes;
import dunno.smoking_kills.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmokingKills implements ModInitializer {
	public static final String MOD_ID = "smoking_kills";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		LOGGER.info("Items initialized");
		ModBlocks.initialize();
		LOGGER.info("Blocks initialized");
		ModRecipes.initialize();
		LOGGER.info("Recipes initialized");
		ModVillagers.initialize();
		LOGGER.info("Goofy Ahh Villagers initialized");
	}
}