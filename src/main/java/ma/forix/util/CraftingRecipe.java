package ma.forix.util;

import ma.forix.container.Container;
import ma.forix.container.Stockable;
import ma.forix.item.Item;
import ma.forix.item.ItemStack;
import ma.forix.tile.Tile;


public class CraftingRecipe {

    public static CraftingRecipe plank = new CraftingRecipe(new ItemStack(Tile.plank.getItem(), 1)).addIngredient(new ItemStack(Tile.log.getItem(), 1));

    private ItemStack[] ingredients = new ItemStack[3];
    private ItemStack result;

    public CraftingRecipe(ItemStack result){
        this.result = result;
    }

    public CraftingRecipe addIngredient(int id, ItemStack itemStack){
        ingredients[id] = itemStack;
        return this;
    }

    public CraftingRecipe addIngredient(ItemStack item){
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] == null){
                ingredients[i] = item;
                return this;
            }
        }
        return this;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack[] getIngredients() {
        return ingredients;
    }

    public boolean haveAllIngredients(Container container){
        int equalCounter = 0;
        int itemNumber = 0;
        for (ItemStack ingredient : ingredients) {
            if (ingredient != null)
                itemNumber++;
        }
        for (ItemStack stockable : container.getContent()) {
            ItemStack item = stockable;
            if (item != null) {
                for (ItemStack ingredient : ingredients) {
                    if (ingredient != null) {
                        if (item.getItem().getId() == ingredient.getItem().getId())
                            equalCounter++;
                    }
                }
            }
        }
        return equalCounter == itemNumber;
    }
}
