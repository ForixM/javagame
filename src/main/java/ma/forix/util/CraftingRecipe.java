package ma.forix.util;

import ma.forix.container.Container;
import ma.forix.container.Stockable;
import ma.forix.item.Item;
import ma.forix.tile.Tile;


public class CraftingRecipe {

    public static CraftingRecipe plank = new CraftingRecipe(Tile.plank.getItem()).addIngredient(Tile.log.getItem());

    private Item[] ingredients = new Item[3];
    private Item result;

    public CraftingRecipe(Item result){
        this.result = result;
    }

    public CraftingRecipe addIngredient(int id, Item item){
        ingredients[id] = item;
        return this;
    }

    public CraftingRecipe addIngredient(Item item){
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] == null){
                ingredients[i] = item;
                return this;
            }
        }
        return this;
    }

    public Item getResult() {
        return result;
    }

    public Item[] getIngredients() {
        return ingredients;
    }

    public boolean haveAllIngredients(Container<Item> container){
        int equalCounter = 0;
        int itemNumber = 0;
        for (Item ingredient : ingredients) {
            if (ingredient != null)
                itemNumber++;
        }
        for (Stockable stockable : container.getContent()) {
            Item item = (Item)stockable;
            if (item != null) {
                for (Item ingredient : ingredients) {
                    if (ingredient != null) {
                        if (item.getId() == ingredient.getId())
                            equalCounter++;
                    }
                }
            }
        }
        return equalCounter == itemNumber;
    }
}
