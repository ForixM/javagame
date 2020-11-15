package ma.forix.entity;

import ma.forix.collision.AABB;
import ma.forix.collision.Collision;
import ma.forix.container.Container;
import ma.forix.entity.player.Player;
import ma.forix.game.Factory;
import ma.forix.gui.*;
import ma.forix.gui.widgets.Slot;
import ma.forix.item.Item;
import ma.forix.renderer.*;
import ma.forix.util.Input;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Inventory extends Container<Item> implements Widget {

    private AABB boundingBox;
    private Texture texture;
    private Entity entity;

    private Slot[] slots;

    public Inventory(int size, Entity entity, Vector2f position){
        this(size, position);
        this.entity = entity;
    }

    public Inventory(int size, Vector2f position){
        super(size);
        this.entity = null;
        this.texture = new Texture("/gui/inventory.png");
        this.boundingBox = new AABB(position, new Vector2f(texture.getWidth(), texture.getHeight()));
        slots = new Slot[size];
        int startX = -168;
        for (int i = 0; i < slots.length; i++) {
            int x = i % 5;
            int y = i / 5;
            slots[i] = new Slot(new Vector2f(startX + (84*x), 42 - (y*88)), new Vector2f(32, 32));
        }
    }

    public Inventory(int size, Vector2f position, Container<Item> container){
        this(size, position);
        cloneContainer(container);
    }

    public Inventory(int size, Entity entity, Vector2f position, Container<Item> container){
        this(size, entity, position);
        cloneContainer(container);
    }

    int mouseX, mouseY;

    @Override
    public void render(Camera camera, Shader shader) {
        Vector2f position = boundingBox.getCenter(), scale = boundingBox.getHalfExtent();
        player = Factory.player;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isShowInventory()) {
                if (player.isRenderBlockStorage())
                    position.y = - 100;
                else position.y = 0;
                Gui.getTextureRenderer().renderTexture(texture, (int)position.x, (int)position.y);
                int startX = -168;
                for (int i = 0; i < slots.length; i++) {
                    if (slots[i] != null) {
                        int x = i%5;
                        int y = i/5;
                        slots[i].setPosition(new Vector2f((position.x+startX)+(84*x), (position.y+42)-(y*88)));
                        slots[i].render(camera, Shader.gui);
                        if (getObject(i) != null) {
                            Gui.getTextureRenderer().renderItem((Item) getObject(i), (int) slots[i].getBoundingBox().getCenter().x, (int) slots[i].getBoundingBox().getCenter().y);
                        }
                    }
                }
                /*if (player.getOnMouse() != null)
                    Gui.getTextureRenderer().renderItem(player.getOnMouse(), mouseX, mouseY);*/
            }
        } else {
            Gui.getTextureRenderer().renderTexture(texture, (int)position.x, (int)position.y);
            int startX = -168;
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] != null) {
                    int x = i%5;
                    int y = i/5;
                    slots[i].setPosition(new Vector2f((position.x+startX)+(84*x), (position.y+42)-(y*88)));
                    if (getObject(i) != null) {
                        Gui.getTextureRenderer().renderItem((Item) getObject(i), (int) slots[i].getBoundingBox().getCenter().x, (int) slots[i].getBoundingBox().getCenter().y);
                    }
                    slots[i].render(camera, Shader.gui);
                }
            }
        }
    }

    private Collision slotData;
    private Item item;
    private Player player;
    @Override
    public void update(Input input) {
        player = Factory.player;
        if (player.isShowInventory()) {
            mouseX = (int) input.getMousePosition().x;
            mouseY = (int) input.getMousePosition().y;
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] != null) {
                    slots[i].update(input);
                    slotData = slots[i].getBoundingBox().getCollision(input.getMousePosition());
                    if (slotData.isIntersecting) {
                        if (input.isMouseButtonPressed(0)) {
                            if (getObject(i) != null) {
                                item = (Item)getObject(i);
                                if (input.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
                                    if (player.isRenderBlockStorage()){
                                        player.getOpened().addObject(item);
                                    } else
                                        player.getHotbar().addObject(item);
                                } else
                                    player.setOnMouse(item);
                                removeObject(i);
                            } else {
                                if (player.getOnMouse() != null) {
                                    addObject(player.getOnMouse());
                                    player.setOnMouse(null);
                                }
                            }
                        }
                    }
                }
            }
        }
        /*mouseX = (int) input.getMousePosition().x;
        mouseY = (int) input.getMousePosition().y;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                slots[i].update(input);
                slotData = slots[i].getBoundingBox().getCollision(input.getMousePosition());
                if (slotData.isIntersecting) {
                    if (input.isMouseButtonPressed(0)) {
                        if (getObject(i) != null) {
                            item = (Item)getObject(i);
                            if (input.isKeyDown(GLFW_KEY_LEFT_SHIFT))
                                player.getInventory().addObject(item);
                            else
                                player.setOnMouse(item);
                            removeObject(i);
                        } else {
                            if (player.getOnMouse() != null) {
                                addObject(player.getOnMouse());
                                player.setOnMouse(null);
                            }
                        }
                    }
                }
            }
        }*/
    }

    @Override
    public void onMouseClicked(int mouseButton) {

    }

    public void closeInventory(){
        if (entity instanceof Player) {
            player = (Player) entity;
            if (player.getOnMouse() != null) {
                addObject(player.getOnMouse());
                player.setOnMouse(null);
            }
        }
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }
}
