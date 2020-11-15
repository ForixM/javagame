package ma.forix.gui.widgets;

import ma.forix.entity.player.Player;
import ma.forix.game.Factory;
import ma.forix.item.Item;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.TileSheet;
import ma.forix.util.CraftingRecipe;
import ma.forix.util.Input;
import org.joml.Vector2f;

public class CraftingButton extends Button {

    private Icon first, second, result, fleche;
    private CraftingRecipe recipe;

    public CraftingButton(Vector2f position, Vector2f scale, CraftingRecipe recipe) {
        super(position, scale);
        this.recipe = recipe;
        first = new Icon(new Vector2f(position.x-50, position.y), new Vector2f(20, 20), new TileSheet("../tiles/log.png"));
        result = new Icon(new Vector2f(position.x+50, position.y), new Vector2f(20, 20), new TileSheet("../tiles/plank.png"));
        fleche = new Icon(position, new Vector2f(20, 20), new TileSheet("fleche.png"));
    }

    @Override
    public void update(Input input) {
        super.update(input);
        activated = recipe.haveAllIngredients(Factory.player.getInventory());
        if (!activated){
            selectedState = STATE_IDLE;
        }
    }

    @Override
    public void render(Camera camera, Shader shader) {
        super.render(camera, shader);
        first.render(camera, shader);
        result.render(camera, shader);
        fleche.render(camera, shader);
    }

    @Override
    public void onMouseClicked(int mouseButton) {
        super.onMouseClicked(mouseButton);
        Player player = Factory.player;
        for (Item item : recipe.getIngredients()){
            if (item != null)
                player.getInventory().removeFirstItem(item);
        }
        player.getInventory().addObject(recipe.getResult());
    }
}
