package ma.forix.tile.screens;

import ma.forix.entity.Inventory;
import ma.forix.game.Factory;
import ma.forix.gui.CustomScreen;
import ma.forix.gui.widgets.CraftingButton;
import ma.forix.renderer.Shader;
import ma.forix.renderer.Window;
import ma.forix.util.CraftingRecipe;
import org.joml.Vector2f;

public class WorkbenchScreen extends CustomScreen {

    private CraftingButton craftingPlank;
    private Inventory inventory;

    public WorkbenchScreen(Window window) {
        super(window, "DefaultScreen");
        //addWidget(new Button(new Vector2f(0, 0), new Vector2f(100, 100)));
        craftingPlank = new CraftingButton(new Vector2f(0, 150), new Vector2f(100, 50), CraftingRecipe.plank);
        addWidget(craftingPlank);
        inventory = new Inventory(Factory.player.getContainer().getSize(), new Vector2f(0, 0), Factory.player.getContainer());
        addWidget(inventory);
    }

    @Override
    public void draw() {
        super.draw();
        //Factory.player.showInventory(true);
    }
}
