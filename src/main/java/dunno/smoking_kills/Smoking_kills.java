package dunno.smoking_kills;

import dunno.smoking_kills.blocks.ModBlocks;
import dunno.smoking_kills.items.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Smoking_kills implements ModInitializer {
	public static final String MOD_ID = "smoking_kills";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		LOGGER.info("Items initialized");
		ModBlocks.initialize();
		LOGGER.info("Blocks initialized");
	}
}