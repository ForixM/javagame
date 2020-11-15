package ma.forix.gui.widgets;

import ma.forix.renderer.TileSheet;
import org.joml.Vector2f;

public class Icon extends Image {

    public Icon(Vector2f position, Vector2f scale, TileSheet tileSheet) {
        super(position, scale, tileSheet);
    }

    @Override
    public boolean haveBoundingBox() {
        return false;
    }
}
