package ma.forix.entity.player;

import ma.forix.collision.Collision;
import ma.forix.entity.Entity;
import ma.forix.entity.Inventory;
import ma.forix.game.Factory;
import ma.forix.item.Item;
import ma.forix.renderer.*;
import ma.forix.tile.Tile;
import ma.forix.tile.TileContainer;
import ma.forix.tile.tilentities.TileEntity;
import ma.forix.util.TilePos;
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
    private TileContainer opened = null;
    private Hotbar hotbar;
    private int inventoryWidgetId;
    private int mouseX, mouseY;

    public Player(World world, Transform transform) {
        super(world, new Texture("player.png"), transform);
        inventory = new Inventory(10, this, new Vector2f(0, 0), getContainer());
        hotbar = new Hotbar(5, this, new Texture("/gui/hotbar.png"));
    }

    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        processKeyBindings(delta, window, camera, world);
        mouseX = (int) window.getInput().getMousePosition().x;
        mouseY = (int) window.getInput().getMousePosition().y;
        super.update(delta, window, camera, world);
    }

    @Override
    public void render(Shader shader, Camera camera, World world) {
        super.render(shader, camera, world);
    }

    public Item getOnMouse() {
        return onMouse;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public Hotbar getHotbar() {
        return hotbar;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setOnMouse(Item onMouse) {
        this.onMouse = onMouse;
    }

    public boolean isShowInventory() {
        return showInventory;
    }

    public void showInventory(boolean show){
        this.showInventory = show;
    }

    public boolean isRenderBlockStorage() {
        return renderBlockStorage;
    }

    public TileContainer getOpened(){
        return opened;
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
            if (renderBlockStorage){
                renderBlockStorage = false;
                Factory.gui.removeWidget(inventoryWidgetId);
                inventoryWidgetId = -1;
            }
        }
        if (window.getInput().isMouseButtonPressed(0)){
            Collision hotbarCollision = hotbar.getBoundingBox().getCollision(window.getInput().getMousePosition());
            Collision inventoryCollision = inventory.getBoundingBox().getCollision(window.getInput().getMousePosition());
            if (onMouse != null && onMouse.isTile() && !hotbarCollision.isIntersecting) {
                Vector2i location = world.getWorldPosition(window, camera);
                if (!world.getTile(location.x, location.y).compare(onMouse.tileOf())) {
                    if (showInventory) {
                        if (!inventoryCollision.isIntersecting) {
                            if (renderBlockStorage) {
                                if (!new Inventory(opened.getSize(), new Vector2f(0, 100)).getBoundingBox().getCollision(window.getInput().getMousePosition()).isIntersecting) {
                                    if(!world.getTile(location.x, location.y).compare(Tile.bedrock)){
                                        inventory.addObject(world.getTile(location.x, location.y).getItem());
                                    }
                                    world.setTile(onMouse.tileOf(), location.x, location.y);
                                    if (inventory.haveObject(onMouse)){
                                        inventory.removeFirstItem(onMouse);
                                    } else
                                        onMouse = null;
                                }
                            } else {
                                if(!world.getTile(location.x, location.y).compare(Tile.bedrock)){
                                    inventory.addObject(world.getTile(location.x, location.y).getItem());
                                }
                                world.setTile(onMouse.tileOf(), location.x, location.y);
                                if (inventory.haveObject(onMouse)){
                                    inventory.removeFirstItem(onMouse);
                                } else
                                    onMouse = null;
                            }
                        }
                    } else {
                        if(!world.getTile(location.x, location.y).compare(Tile.bedrock)){
                            inventory.addObject(world.getTile(location.x, location.y).getItem());
                        }
                        world.setTile(onMouse.tileOf(), location.x, location.y);
                        if (inventory.haveObject(onMouse)){
                            inventory.removeFirstItem(onMouse);
                        } else
                            onMouse = null;
                    }
                }
            }
        }
        if (window.getInput().isMouseButtonPressed(1)){
            if (!inventory.isFull()) {
                Vector2i position = world.getWorldPosition(window, camera);
                inventory.addObject(world.getTile(position.x, position.y).getItem());
                world.getTile(position.x, position.y).destroy(world, new TilePos(position.x, position.y));
                world.removeTile(position.x, position.y);
            } else {
                if (!hotbar.isFull()){
                    Vector2i position = world.getWorldPosition(window, camera);
                    hotbar.addObject(world.getTile(position.x, position.y).getItem());
                    world.getTile(position.x, position.y).destroy(world, new TilePos(position.x, position.y));
                    world.removeTile(position.x, position.y);
                }
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_E)){
            if (!renderBlockStorage) {
                Vector2i position = world.getWorldPosition(window, camera);
                if (world.getTile(position.x, position.y).haveStorage()) {
                    renderBlockStorage = true;
                    opened = world.getTileContainer(new TilePos(position.x, position.y));
                    inventoryWidgetId = Factory.gui.insertWidget(new Inventory(opened.getSize(), new Vector2f(0, 100), opened));
                    showInventory = true;
                }
                TileEntity tileEntity = world.getTileEntity(new TilePos(position.x, position.y));
                if (tileEntity != null){
                    tileEntity.interact();
                }
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
            showInventory = false;
            if (renderBlockStorage){
                renderBlockStorage = false;
                Factory.gui.removeWidget(inventoryWidgetId);
                inventoryWidgetId = -1;
            }
            Factory.displayCustomScreen(null);
        }
    }
}
