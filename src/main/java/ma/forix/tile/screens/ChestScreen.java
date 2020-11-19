package ma.forix.tile.screens;

import ma.forix.container.Container;
import ma.forix.entity.Inventory;
import ma.forix.game.Factory;
import ma.forix.gui.CustomScreen;
import ma.forix.item.Item;
import ma.forix.renderer.Window;
import org.joml.Vector2f;

public class ChestScreen extends CustomScreen {

    private Inventory inventory;

    public ChestScreen(Window window, Container container) {
        super(window, "Default screen");
        inventory = new Inventory(container.getSize(), new Vector2f(0, 200), container);
        addWidget(inventory);
    }

    @Override
    public void draw() {
        super.draw();
        //Factory.player.showInventory(true);
    }
}
