package ma.forix.gui.widgets;

import ma.forix.assets.Assets;
import ma.forix.collision.AABB;
import ma.forix.collision.Collision;
import ma.forix.game.Factory;
import ma.forix.gui.Widget;
import ma.forix.item.Item;
import ma.forix.item.ItemStack;
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

    private ItemStack itemStack;

    private final AABB boundingBox;

    private int id;

    public Slot(Vector2f position, Vector2f scale, int id){
        this(position, scale, null, id);
    }

    public Slot(Vector2f position, Vector2f scale, ItemStack itemStack, int id){
        this.boundingBox = new AABB(position, scale);
        this.itemStack = itemStack;
        this.id = id;
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
    public void onMouseClicked() {
        //Factory.player.setOnMouse(item);
        //deleteItem();
        System.out.println("oui");
    }

    @Override
    public void onKeyPressed() {

    }

    public Slot setItem(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }

    public void deleteItem(){
        this.itemStack = null;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public int getId() {
        return id;
    }

    @Override
    public AABB getBoundingBox() {
        return boundingBox;
    }

    public void setPosition(Vector2f position) {
        this.boundingBox.setCenter(position);
    }

}
