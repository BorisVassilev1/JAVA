package edu.school.items;

public class ItemStack {
	private Item item;
	private int count;
	
	public ItemStack(Item item, int count) {
		this.item = item;
		this.count = count;
	}
	
	public int addItems(int amt)
	{
		count += amt;
		int excess = count -item.getStackSize();
		if(excess <= 0)
		{
			return 0;
		}
		count -= excess;
		return excess;
	}
	
	public boolean isFull()
	{
		return item.getStackSize() == count;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "{" + item.toString() + ", Count: " + this.count + "}";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ItemStack)
		{
			return this.hashCode() == obj.hashCode();
		}
		return false;
	}
}
