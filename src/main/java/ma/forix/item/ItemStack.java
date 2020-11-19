package ma.forix.item;

public class ItemStack {

    private Item item;
    private int stack;

    public ItemStack(Item item, int stack){
        this.item = item;
        this.stack = stack;
    }

    public Item getItem() {
        return item;
    }

    public int getStack() {
        return stack;
    }

    public int addStack(int amount){
        if (stack < 100) {
            if (stack+amount > 100){
                amount -= 100-stack;
                stack = 100;
                return amount;
            }
            stack += amount;
            return 0;
        }
        return amount;
    }

    public int removeStack(int amount){
        stack -= amount;
        return stack;
    }

    public boolean compare(Item item){
        return item.getId() == this.item.getId();
    }

    public boolean compare(ItemStack itemStack){
        return item.getId() == itemStack.getItem().getId();
    }
}
