package ma.forix.tile.tilentities;

import ma.forix.game.Factory;
import ma.forix.tile.screens.WorkbenchScreen;

public class WorkbenchTile extends TileEntity {

    @Override
    public void update() {

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