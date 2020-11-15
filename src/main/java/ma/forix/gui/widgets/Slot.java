package ma.forix.gui.widgets;

import ma.forix.assets.Assets;
import ma.forix.collision.AABB;
import ma.forix.collision.Collision;
import ma.forix.gui.Widget;
import ma.forix.item.Item;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.util.Input;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Slot implements Widget {
    public static final int STATE_IDLE = 0;
    public static final int STATE_SELECTED = 1;

    private int selectedState;

    private Item item;

    private AABB boundingBox;

    public Slot(Vector2f position, Vector2f scale){
        this(position, scale, null);
    }

    public Slot(Vector2f position, Vector2f scale, Item item){
        this.boundingBox = new AABB(position, scale);
        this.item = item;
    }

    private Matrix4f transform;
    private Vector2f position, scale;
    @Override
    public void render(Camera camera, Shader shader) {
        transform = new Matrix4f();
        position = boundingBox.getCenter();
        scale = boundingBox.getHalfExtent();
        transform.identity().translate(position.x, position.y, 0).scale(scale.x, scale.y, 1);
        shader.bind();
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState){
            case 0:
                shader.setUniform("color", new Vector4f(0, 0, 0, 0f));
                break;
            case 1:
                shader.setUniform("color", new Vector4f(0.2f, 0.2f, 0.2f, 0.4f));
                break;
        }

        Assets.getModel().render();
    }

    private Collision data;
    @Override
    public void update(Input input) {
        data = boundingBox.getCollision(input.getMousePosition());
        if (data.isIntersecting) {
            selectedState = STATE_SELECTED;
        }
        else selectedState = STATE_IDLE;
    }

    @Override
    public void onMouseClicked(int mouseButton) {

    }

    public void setItem(Item item){
        this.item = item;
    }

    public void deleteItem(){
        this.item = null;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public AABB getBoundingBox() {
        return boundingBox;
    }

    public void setPosition(Vector2f position) {
        this.boundingBox.setCenter(position);
    }
}
