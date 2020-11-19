package ma.forix.tile.tilentities;

import ma.forix.container.Container;
import ma.forix.game.Factory;
import ma.forix.item.Item;
import ma.forix.tile.containers.ChestContainer;
import ma.forix.tile.screens.ChestScreen;

public class ChestTile extends TileEntity {

    public Container container = createContainer();

    @Override
    public Container createContainer() {
        return new ChestContainer(10);
    }

    @Override
    public void update() {

    }

    @Override
    public void interact() {
        new ChestScreen(Factory.getInstance().getWindow(), container);
    }

    @Override
    public void tick() {

    }
}
