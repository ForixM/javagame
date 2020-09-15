package ma.forix.gui;

import ma.forix.assets.Assets;
import ma.forix.collision.AABB;
import ma.forix.renderer.*;
import ma.forix.util.Input;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Image implements Widget {

    private TileSheet tileSheet;

    private AABB boundingBox;
    private Matrix4f transform = new Matrix4f();

    public Image(Vector2f position, Vector2f scale, TileSheet tileSheet){
        this.boundingBox = new AABB(position, scale);
        this.tileSheet = tileSheet;
    }

    @Override
    public void render(Camera camera, Shader shader) {
        Vector2f position = boundingBox.getCenter(), scale = boundingBox.getHalfExtent();

        transform.identity().translate(position.x, position.y, 0).scale(scale.x, scale.y, 1);
        shader.bind();
        tileSheet.bindTile(shader, 0);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        Assets.getModel().render();
    }

    @Override
    public void update(Input input) {

    }
}
