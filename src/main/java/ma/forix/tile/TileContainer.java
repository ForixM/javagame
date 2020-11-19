package ma.forix.tile;

import ma.forix.container.Container;
import ma.forix.entity.Entity;
import ma.forix.entity.Inventory;
import ma.forix.entity.player.Player;
import ma.forix.item.Item;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.util.Input;
import org.joml.Vector2f;

public class TileContainer extends Container {

    private Inventory inventory;

    public TileContainer(int size){
        super(size);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
