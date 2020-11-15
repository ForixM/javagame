package ma.forix.gui;

import ma.forix.collision.AABB;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.util.Input;
import org.joml.Vector2f;

public interface Widget {

    public void render(Camera camera, Shader shader);

    public void update(Input input);

    public void onMouseClicked(int mouseButton);

    default boolean haveBoundingBox(){
        return true;
    }

    AABB getBoundingBox();
}
