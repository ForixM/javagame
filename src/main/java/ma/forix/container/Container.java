package ma.forix.container;

import ma.forix.item.Item;
import ma.forix.item.ItemStack;

public class Container {

    private ItemStack[] itemStacks;
    private int size;

    public Container(int size){
        this.size = size;
        itemStacks = new ItemStack[size];
    }

    public int getSize() {
        return size;
    }
    
    public ItemStack getObject(int slot){
        return itemStacks[slot];
    }

    public Container addObject(ItemStack object){
        int stackId = -1;
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] != null){
                if (itemStacks[i].getItem().getId() == object.getItem().getId()){
                    stackId = i;
                    continue;
                }
            }
        }

        if (stackId != -1){
            int rest = itemStacks[stackId].addStack(object.getStack());
            if (rest == 0)
                return this;
        }
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] != null) {
                if (itemStacks[i].getItem().getId() == object.getItem().getId()) {
                    if (itemStacks[i].addStack(object.getStack()) != 0) {
                        continue;
                    } else
                        return this;
                }
            }
            if (itemStacks[i] == null) {
                itemStacks[i] = object;
                return this;
            }
        }

        return null;
    }

    public boolean addObject(int slot, ItemStack object){
        if (itemStacks[slot] == null) {
            itemStacks[slot] = object;
            return true;
        }
        return false;
    }

    public boolean isFull(){
        for (ItemStack object : itemStacks) {
            if (object == null)
                return false;
        }
        return true;
    }

    public boolean removeObject(int slot){
        itemStacks[slot] = null;
        return true;
    }

    public ItemStack[] getContent(){
        return itemStacks;
    }

    public void removeFirstItem(ItemStack item){
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] != null) {
                if (( itemStacks[i]).compare(item)) {
                    itemStacks[i] = null;
                    return;
                }
            }
        }
    }

    public boolean haveObject(ItemStack item){
        for (ItemStack stack : itemStacks) {
            if (stack != null) {
                if ((stack).compare(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void cloneContainer(Container container){
        itemStacks = container.getContent();
    }

}
