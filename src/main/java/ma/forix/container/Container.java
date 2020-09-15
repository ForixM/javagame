package ma.forix.container;

import ma.forix.item.Item;

public class Container<T extends Stockable> {

    private Stockable[] objects;
    private int size;

    public Container(int size){
        this.size = size;
        objects = new Stockable[size];
    }

    public static Container createContainer(int size){
        if (size > 0) {
            Container container = new Container(size);
            return container;
        }
        return null;
    }

    public int getSize() {
        return size;
    }
    
    public Stockable getObject(int slot){
        return objects[slot];
    }

    public boolean addObject(T object){
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null){
                objects[i] = object;
                return true;
            }
        }
        return false;
    }

    public boolean removeObject(int slot){
        objects[slot] = null;
        return true;
    }

    public boolean addObject(int slot, T object){
        if (objects[slot] != null) {
            objects[slot] = object;
            return true;
        }
        return false;
    }

    public Item[] getContent(){
        return (Item[]) objects;
    }

}
