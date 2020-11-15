package ma.forix.tile.tilentities;

import ma.forix.game.Factory;
import ma.forix.tile.Tile;
import ma.forix.util.TilePos;
import ma.forix.world.World;

public class Workbench extends Tile {
    public Workbench(String texture) {
        super(texture);
    }

    @Override
    public TileEntity createTileEntity() {
        return new WorkbenchTile();
    }

    @Override
    public void destroy(World world, TilePos tilePos) {
        //Factory.removeTickableEntity(world.getTileEntity(tilePos));
    }
}
