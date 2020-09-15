package ma.forix.entity;

import ma.forix.assets.Assets;
import ma.forix.collision.AABB;
import ma.forix.collision.Collision;
import ma.forix.container.Container;
import ma.forix.item.Item;
import ma.forix.renderer.*;
import ma.forix.util.Transform;
import ma.forix.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class Entity {
    private Texture texture;
    private Transform transform;

    private AABB bounding_box;
    private World world;

    private Container<Item> container;

    public Entity(World world, Texture texture, Transform transform){
        this.texture = texture;
        this.world = world;
        this.transform = transform;
        bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
        container = Container.createContainer(10);
    }

    public void move(Vector2f direction){
        transform.pos.add(new Vector3f(direction, 0));
        bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
    }

    public void update(float delta, Window window, Camera camera, World world){

        AABB[] tile_boxes = new AABB[25];
        AABB[] item_boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tile_boxes[i+j*5] = world.getTileBoundingBox(
                        (int)((transform.pos.x / 2 + 0.5f)-(5/2)) +i,
                        (int)((-transform.pos.y / 2 + 0.5f)-(5/2)) +j
                );
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                item_boxes[i+j*5] = world.getItemBoundingBox(
                        (int)((transform.pos.x / 2 + 0.5f)-(5/2)) +i,
                        (int)((-transform.pos.y / 2 + 0.5f)-(5/2)) +j
                );
            }
        }

        AABB tile_box = null;
        AABB item_box = null;
        // TILES
        for (int i = 0; i < tile_boxes.length; i++) {
            if (tile_boxes[i] != null){
                if (tile_box == null)
                    tile_box = tile_boxes[i];
                Vector2f length1 = tile_box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = tile_boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                if (length1.lengthSquared() > length2.lengthSquared()){
                    tile_box = tile_boxes[i];
                }
            }
        }

        // ITEMS
        for (int i = 0; i < item_boxes.length; i++) {
            if (item_boxes[i] != null){
                if (item_box == null)
                    item_box = item_boxes[i];
                Vector2f length1 = item_box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = item_boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                if (length1.lengthSquared() > length2.lengthSquared()){
                    item_box = item_boxes[i];
                }
            }
        }

        // TILE
        if (tile_box != null) {
            Collision data = bounding_box.getCollision(tile_box);
            if (data.isIntersecting) {
                bounding_box.correctPosition(tile_box, data);
                transform.pos.set(bounding_box.getCenter(), 0);
            }
            for (int i = 0; i < tile_boxes.length; i++) {
                if (tile_boxes[i] != null){
                    if (tile_box == null)
                        tile_box = tile_boxes[i];
                    Vector2f length1 = tile_box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = tile_boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if (length1.lengthSquared() > length2.lengthSquared()){
                        tile_box = tile_boxes[i];
                    }
                }
            }
            data = bounding_box.getCollision(tile_box);
            if (data.isIntersecting) {
                bounding_box.correctPosition(tile_box, data);
                transform.pos.set(bounding_box.getCenter(), 0);
            }
        }

        // ITEM
        if (item_box != null) {
            Collision data = bounding_box.getCollision(item_box);
            if (data.isIntersecting) {
                int x = Math.abs((int)item_box.getCenter().x)/2;
                int y  = Math.abs((int)item_box.getCenter().y)/2;
                container.addObject(world.getItem(x, y));
                world.removeItem(x, y);
                return;
            }

            for (int i = 0; i < item_boxes.length; i++) {
                if (item_boxes[i] != null){
                    if (item_box == null)
                        item_box = item_boxes[i];
                    Vector2f length1 = item_box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = item_boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if (length1.lengthSquared() > length2.lengthSquared()){
                        item_box = item_boxes[i];
                    }
                }
            }
            data = bounding_box.getCollision(item_box);
            if (data.isIntersecting) {
                int x = Math.abs((int)item_box.getCenter().x)/2;
                int y  = Math.abs((int)item_box.getCenter().y)/2;
                container.addObject(world.getItem(x, y));
                world.removeItem(x, y);
                return;
            }
        }

        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
    }

    public void render(Shader shader, Camera camera, World world){
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());

        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(target));
        texture.bind(0);
        Assets.getModel().render();
    }

    /*public void setPlayerScale(float playerScale) {
        this.playerScale *= playerScale;
        transform.scale = new Vector3f(this.playerScale, this.playerScale, 1);
    }

    public int getPlayerScale() {
        return playerScale;
    }*/

    public Transform getTransform() {
        return transform;
    }

    public Container<Item> getContainer() {
        return container;
    }

    //    public Inventory getInventory() {
//        return inventory;
//    }

    public World getWorld() {
        return world;
    }
}
