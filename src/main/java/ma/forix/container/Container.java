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

    public Container addObject(T object){
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null){
                objects[i] = object;
                return this;
            }
        }
        return null;
    }

    public boolean isFull(){
        for (Stockable object : objects) {
            if (object == null)
                return false;
        }
        return true;
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

    public Stockable[] getContent(){
        return objects;
    }

    public void removeFirstItem(Item item){
        for (int i = 0; i < objects.length; i++) {
            System.out.println("i = " + i);
            if (objects[i] != null) {
                if (((Item) objects[i]).compare(item)) {
                    objects[i] = null;
                    return;
                }
            }
        }
    }

    public boolean haveObject(Item item){
        for (Stockable object : objects) {
            if (object != null) {
                if (((Item) object).compare(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void cloneContainer(Container<T> container){
        objects = container.getContent();
    }

}
