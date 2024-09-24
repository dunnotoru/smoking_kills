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
		LOGGER.info(Registry.RECIPE_SERIALIZER.getIds().toString());
		for (int i = 0; i < Registry.RECIPE_SERIALIZER.size(); i++) {
			LOGGER.info(Registry.RECIPE_SERIALIZER.get(i).toString() + Registry.RECIPE_SERIALIZER.getId(Registry.RECIPE_SERIALIZER.get(i)));
		}
		String string = "minecraft:crafting_stuff_with_tobacco";
		var a = (RecipeSerializer)Registry.RECIPE_SERIALIZER.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
			return new JsonSyntaxException("Invalid or unsupported recipe type '" + string + "'");
		});
	}
}