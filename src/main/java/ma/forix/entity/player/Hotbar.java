package ma.forix.entity.player;

import ma.forix.collision.AABB;
import ma.forix.collision.Collision;
import ma.forix.container.Container;
import ma.forix.gui.Gui;
import ma.forix.gui.Slot;
import ma.forix.gui.Widget;
import ma.forix.item.Item;
import ma.forix.renderer.Camera;
import ma.forix.renderer.Shader;
import ma.forix.renderer.Texture;
import ma.forix.util.Input;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Hotbar extends Container<Item> implements Widget {

    private Texture texture;
    private Slot[] slots;

    private AABB boundingBox;
    private Player player;

    private int x = 0, y = -300;
    private int mouseX = 0, mouseY = 0;

    public Hotbar(int size, Player player, Texture texture){
        super(size);
        this.texture = texture;
        this.player = player;
        this.boundingBox = new AABB(new Vector2f(x, y), new Vector2f(texture.getWidth(), texture.getHeight()));
        slots = new Slot[size];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot(new Vector2f(-168 + (i*84), y), new Vector2f(32, 32));
        }
    }

    @Override
    public void render(Camera camera, Shader shader) {
        Gui.getTextureRenderer().renderTexture(texture, x, y);
        for (int i = 0; i < slots.length; i++) {
            slots[i].render(camera, Shader.gui);
            Item item = (Item)getObject(i);
            if (item != null){
                Gui.getTextureRenderer().renderItem(item, (int)slots[i].getBoundingBox().getCenter().x, (int)slots[i].getBoundingBox().getCenter().y);
            }
        }
        if (player.getOnMouse() != null)
            Gui.getTextureRenderer().renderItem(player.getOnMouse(), mouseX, mouseY);
    }

    private Collision slotData;
    private Item item;
    @Override
    public void update(Input input) {
        mouseX = (int) input.getMousePosition().x;
        mouseY = (int) input.getMousePosition().y;
        for (int i = 0; i < slots.length; i++) {
            slots[i].update(input);
            slotData = slots[i].getBoundingBox().getCollision(input.getMousePosition());
            if (slotData.isIntersecting){
                if (input.isMouseButtonPressed(0)){
                    item = (Item)getObject(i);
                    if (item != null){
                        if (input.isKeyDown(GLFW_KEY_LEFT_SHIFT))
                            player.getInventory().addObject(item);
                        else
                            player.setOnMouse(item);
                        removeObject(i);
                    } else if (player.getOnMouse() != null){
                        addObject(player.getOnMouse());
                        player.setOnMouse(null);
                    }
                }
            }
        }
    }

    public Item extractItemOnSlot(Vector2f mousePosition){
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null){
                slotData = slots[i].getBoundingBox().getCollision(mousePosition);
                if (slotData.isIntersecting){
                    item = (Item)getObject(i);
                    removeObject(i);
                    return item;
                }
            }
        }
        return null;
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }
}
