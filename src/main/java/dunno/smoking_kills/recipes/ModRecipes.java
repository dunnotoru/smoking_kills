package dunno.smoking_kills.recipes;

import dunno.smoking_kills.SmokingKills;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static final SpecialRecipeSerializer<StuffWithTobaccoRecipe> STUFF_WITH_TOBACCO
            = register("crafting_stuff_with_tobacco", new SpecialRecipeSerializer<StuffWithTobaccoRecipe>(StuffWithTobaccoRecipe::new));

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        SmokingKills.LOGGER.info("REGISTER SERIALIZER");
        return Registry.register(Registry.RECIPE_SERIALIZER, id, serializer);
    }

    public static void initialize() {

    }
}