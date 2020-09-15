package ma.forix.tile;

import ma.forix.container.Container;
import ma.forix.item.Item;

public class Tile {
    public static Tile tiles[] = new Tile[255];

    public static byte idCounter = 0;

    public static final Tile grass = new Tile("grass");
    public static final Tile stone = new Tile("stone");
    public static final Tile water = new Tile("water").setSolid();
    public static final Tile plank = new Tile("plank");
    public static final Tile sand = new Tile("sand");
    public static final Tile bedrock = new Tile("bedrock");
    public static final Tile chest = new Tile("chest", 10);

    private Container<Item> storage = null;

    private byte id;
    private byte itemId;
    private boolean solid;
    private String texture;

    public Tile(String texture){
        this.id = idCounter;
        idCounter++;
        this.texture = "tiles/"+texture;
        this.solid = false;

        if (tiles[id] != null){
            throw new IllegalStateException("Tiles at ["+id+"] has already being used !");
        }
        tiles[id] = this;
        this.itemId = Item.generateItemFromTile(this);
    }

    public Tile(String texture, int storageSize){
        this(texture);
        this.storage = new Container<>(storageSize);
    }

    public Tile setSolid(){
        this.solid = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean haveStorage(){
        return storage != null;
    }

    public void addObject(Item item){
        storage.addObject(item);
    }

    public Item getObject(int slot){
        return (Item)storage.getObject(slot);
    }

    public int getStorageSize(){
        return storage.getSize();
    }

    public void removeObject(int slot){
        storage.removeObject(slot);
    }

    public Item getItem(){
        System.out.println("Item.items[itemId] = " + Item.items[itemId]);
        return Item.items[itemId];
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "id=" + id +
                ", texture='" + texture + '\'' +
                '}';
    }
}
