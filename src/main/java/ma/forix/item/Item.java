package ma.forix.item;

import ma.forix.container.Stockable;
import ma.forix.tile.Tile;

public class Item extends Stockable {
    public static Item[] items = new Item[16];
    public static byte idCounter;

    public static Item stick = new Item("stick").setSolid();

    private byte id;
    private String texture;
    private boolean solid;
    private boolean isTile = false;

    public Item(String texture){
        this.id = idCounter;
        idCounter++;
        this.texture = texture;

        if (items[id] != null){
            throw new IllegalStateException("Items at ["+id+"] has already being used !");
        }

        items[id] = this;
    }
    
    public Item(String texture, boolean isTile){
        this(texture);
        this.isTile = isTile;
    }

    public Tile tileOf(){
        return Tile.tiles[id-1];
    }

    public static byte generateItemFromTile(Tile tile){
        Item item = new Item(tile.getTexture(), true);
        return (byte) (idCounter-1);
    }

    public boolean isTile() {
        return isTile;
    }

    public Item setSolid() {
        this.solid = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        if (isTile){
            return texture;
        } else
            return "items/"+texture;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", texture='" + texture + '\'' +
                '}';
    }
}
