package dunno.smoking_kills;

import com.google.gson.JsonSyntaxException;
import dunno.smoking_kills.blocks.ModBlocks;
import dunno.smoking_kills.items.ModItems;
import dunno.smoking_kills.recipes.ModRecipes;
import net.fabricmc.api.ModInitializer;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
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
	}
}