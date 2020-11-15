package ma.forix.tile.tilentities;

import ma.forix.container.Container;
import ma.forix.game.Factory;
import ma.forix.item.Item;

public class WorkbenchTile extends TileEntity {

    public WorkbenchTile() {
        System.out.println("created tileEntity");
    }

    @Override
    public void update() {

    }

    @Override
    public Container<Item> createContainer() {
        return super.createContainer();
    }

    @Override
    public void interact() {
        new WorkbenchScreen(Factory.getInstance().getWindow());
    }

    @Override
    public void tick() {
        //System.out.println("oui");
    }
}