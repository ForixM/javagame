package ma.forix.tile;

import ma.forix.tile.tilentities.ChestTile;
import ma.forix.tile.tilentities.TileEntity;

public class Chest extends Tile {
    public Chest(String texture) {
        super(texture);
    }

    @Override
    public TileEntity createTileEntity() {
        return new ChestTile();
    }
}
