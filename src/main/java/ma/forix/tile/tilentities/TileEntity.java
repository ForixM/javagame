package ma.forix.tile.tilentities;

import ma.forix.container.Container;
import ma.forix.game.Factory;
import ma.forix.item.Item;

public abstract class TileEntity implements ITickableEntity {
    public abstract void update();
    public abstract void interact();
    public Container<Item> createContainer(){
        return null;
    }

    public TileEntity(){
        //Factory.registerTickableEntity(this);
    }

}