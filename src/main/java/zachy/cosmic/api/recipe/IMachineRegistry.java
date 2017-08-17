package zachy.cosmic.api.recipe;

import net.minecraft.inventory.IInventory;

import java.util.List;

/**
 * The recipe registry of a machine
 */
public interface IMachineRegistry {

    /**
     * Adds a recipe to the registry
     *
     * @param recipe the recipe to add
     */
    void addRecipe(IMachineRecipe recipe);

    /**
     * Returns a recipe from the slots
     *
     * @param inventory the machine
     * @param inputs the number of input slots of the machine
     * @return the recipe, or null if no recipe was found
     */
    IMachineRecipe getRecipe(IInventory inventory, int inputs);

    /**
     * @return a list with all of the machine's recipes
     */
    List<IMachineRecipe> getRecipes();
}
