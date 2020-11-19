package ma.forix.entity.player;

import ma.forix.container.Container;
import ma.forix.entity.Entity;
import ma.forix.game.Factory;
import ma.forix.gui.CustomScreen;
import ma.forix.gui.Gui;
import ma.forix.gui.Widget;
import ma.forix.gui.widgets.Image;
import ma.forix.gui.widgets.Slot;
import ma.forix.gui.widgets.Text;
import ma.forix.item.Item;
import ma.forix.item.ItemStack;
import ma.forix.renderer.TileSheet;
import ma.forix.renderer.Window;
import ma.forix.util.Input;
import org.joml.Vector2f;

import java.util.Arrays;

public class InventoryScreen extends CustomScreen {

    private final Container container;

    private final Slot[] slots;
    private final Image inventoryGui;
    private final Text text;

    public InventoryScreen(Window window, Container container) {
        super(window, "Inventory");
        this.container = container;
        inventoryGui = new Image(new Vector2f(0, 0), new Vector2f(220, 94), new TileSheet("inventory.png"));
        addWidget(inventoryGui);
        this.text = new Text(new TileSheet("../letters.png"), "test");
        //addWidget(text);

        slots = new Slot[10];
        int startX = -168;
        for (int i = 0; i < slots.length; i++) {
            int x = i % 5;
            int y = i / 5;
            slots[i] = new Slot(new Vector2f(startX + (84*x), 42 - (y*88)), new Vector2f(32, 32), container.getObject(i), i);
        }
        Arrays.stream(slots).forEach(this::addWidget);
    }

    public InventoryScreen(Window window, Entity entity){
        this(window, entity.getContainer());
    }

    public Container getContainer() {
        return container;
    }

    @Override
    public void draw() {
        super.draw();
        int startX = -168;
        for (int i = 0; i < container.getContent().length; i++) {
            if (container.getContent()[i] != null){
                ItemStack item = container.getContent()[i];
                int x = i % 5;
                int y = i / 5;
                Gui.getTextureRenderer().renderItem(item, startX + (84*x), 42 - (y*88));
            }
        }
    }

    @Override
    public void update(Input input) {
        super.update(input);
        for (int i = 0; i < slots.length; i++) {
            slots[i].setItem(container.getObject(i));
            slots[i].update(input);
        }
    }

    @Override
    public void onMouseClicked(Widget widget) {
        super.onMouseClicked(widget);
        if (widget instanceof Slot) {
            Slot slot = (Slot) widget;
            if (slot.getItem() != null) {
                ItemStack tempOnMouse = Factory.player.getOnMouse();
                Factory.player.setOnMouse(slot.getItem());
                container.removeFirstItem(slot.getItem());
                if (tempOnMouse != null){
                    container.addObject(slot.getId(), tempOnMouse);
                } else
                    slot.deleteItem();
            } else {
                slot.setItem(Factory.player.getOnMouse());
                container.addObject(slot.getId(), Factory.player.getOnMouse());
                Factory.player.setOnMouse(null);
            }
        }
    }
}