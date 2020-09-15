package ma.forix.entity.player;

import ma.forix.collision.Collision;
import ma.forix.entity.Entity;
import ma.forix.entity.Inventory;
import ma.forix.gui.Gui;
import ma.forix.gui.Slot;
import ma.forix.item.Item;
import ma.forix.renderer.*;
import ma.forix.util.Transform;
import ma.forix.world.World;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class Player extends Entity {

    private Item onMouse;
    private boolean showInventory = false;
    private boolean renderBlockStorage = false;
    private Inventory inventory;
    private Hotbar hotbar;

    public Player(World world, Transform transform) {
        super(world, new Texture("bg.png"), transform);
        inventory = new Inventory(10, this, new Vector2f(0, 0));
        hotbar = new Hotbar(5, this, new Texture("/gui/hotbar.png"));
    }

    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        processKeyBindings(delta, window, camera, world);

        if (renderBlockStorage){
//            for (int i = 0; i < storageSlots.length; i++) {
//                storageSlots[i].update(window.getInput());
//            }
        }

        super.update(delta, window, camera, world);
    }



    @Override
    public void render(Shader shader, Camera camera, World world) {
        super.render(shader, camera, world);
        if (renderBlockStorage){
            //Gui.getTextureRenderer().renderTexture(new Texture("/gui/inventory.png"), 0, 100);
//            for (int i = 0; i < storageSlots.length; i++) {
//                storageSlots[i].render(camera, Shader.gui);
//            }
        }
    }

    public Item getOnMouse() {
        return onMouse;
    }

    public void setOnMouse(Item onMouse) {
        this.onMouse = onMouse;
    }

    public boolean isShowInventory() {
        return showInventory;
    }

    public boolean isRenderBlockStorage() {
        return renderBlockStorage;
    }

    private void processKeyBindings(float delta, Window window, Camera camera, World world){
        Vector2f movement = new Vector2f();
        if (window.getInput().isKeyDown(GLFW_KEY_A))
            movement.add(-10*delta, 0);
        if (window.getInput().isKeyDown(GLFW_KEY_D))
            movement.add(10*delta, 0);
        if (window.getInput().isKeyDown(GLFW_KEY_W))
            movement.add(0, 10*delta);
        if (window.getInput().isKeyDown(GLFW_KEY_S))
            movement.add(0, -10*delta);
        move(movement);

        if (window.getInput().isKeyPressed(GLFW_KEY_TAB)) {
            showInventory = ! showInventory;
            renderBlockStorage = false;
        }
        if (window.getInput().isMouseButtonPressed(0)){
            Collision hotbarCollision = hotbar.getBoundingBox().getCollision(window.getInput().getMousePosition());
            Collision inventoryCollision = inventory.getBoundingBox().getCollision(window.getInput().getMousePosition());
            if (onMouse != null && onMouse.isTile() && !hotbarCollision.isIntersecting) {
                if (showInventory){
                    if (!inventoryCollision.isIntersecting){
                        Vector2i location = world.getWorldPosition(window, camera);
                        world.setTile(onMouse.tileOf(), location.x, location.y);
                    }
                } else {
                    Vector2i location = world.getWorldPosition(window, camera);
                    world.setTile(onMouse.tileOf(), location.x, location.y);
                }
            }
        }
        if (window.getInput().isMouseButtonPressed(1)){
            Vector2i position = world.getWorldPosition(window, camera);
            inventory.addObject(world.getTile(position.x, position.y).getItem());
            world.removeTile(position.x, position.y);
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_E)){
            Vector2i position = world.getWorldPosition(window, camera);
            if (world.getTile(position.x, position.y).haveStorage()){
//                storageSlots = new Slot[world.getTile(position.x, position.y).getStorageSize()];
//                for (int i = 0; i < storageSlots.length; i++) {
//                    int x = i % 5;
//                    int y = i / 5;
//                    System.out.println("["+i+"]: x:"+ (-168 + (x*84))+" y:"+(142+(88*y)));
//                    storageSlots[i] = new Slot(new Vector2f(-168 + (x*84), 100/*142+(88*y)*/), new Vector2f(32, 32));
//                }
                renderBlockStorage = true;
                showInventory = true;
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
            renderBlockStorage = false;
            showInventory = false;
        }
    }
}
