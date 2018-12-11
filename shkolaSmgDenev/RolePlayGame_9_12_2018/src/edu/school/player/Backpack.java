package edu.school.player;

import java.util.ArrayList;

import edu.school.items.Artifact;
import edu.school.items.Item;
import edu.school.items.ItemStack;

public class Backpack {
	
	private int maxWeight;
	private int slotsCount;
	private ItemStack contents[];
	private int weight;
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Backpack(int carryWeight, int slotsCount) {
		this.maxWeight = carryWeight;
		this.slotsCount = slotsCount;
		contents = new ItemStack[this.slotsCount];
		this.weight = 0;
	}

	public int getCarryWeight() {
		return maxWeight;
	}

	public void setCarryWeight(int carryWeight) {
		this.maxWeight = carryWeight;
	}

	public ItemStack[] getContents() {
		return contents.clone();
	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}

	public int getSlotsCount() {
		return slotsCount;
	}

	public void setSlotsCount(int slotsCount) {
		this.slotsCount = slotsCount;
	}
	
	public int addItems(Item item, int amt)
	{
		if(item instanceof Artifact)
		{
			if(item.getWeight() + weight > maxWeight)
			{
				return 1;
			}
			for(int i = 0; i < this.slotsCount; i++)
			{
				if(contents[i] == null)
				{
					contents[i] = new ItemStack(item,1);
					weight += item.getWeight();
					return 0;
				}
			}
			return 1;
		}
		else
		{
			for(int i = 0; i < this.slotsCount && weight <= maxWeight; i ++)
			{
				if(contents[i] == null)
				{
					if(amt <= item.getStackSize())
					{
						contents[i] = new ItemStack(item, amt);
						weight += item.getWeight() * amt;
						return 0;
					}
					else
					{
						contents[i] = new ItemStack(item, item.getStackSize());
						weight += item.getWeight() * item.getStackSize();
						amt -= item.getStackSize();
						//continue;
					}
				}
				else if(contents[i].getItem().equals(item) && !contents[i].isFull())
				{
					int excess = contents[i].addItems(amt);
					weight += item.getWeight() * (amt - excess); 
					amt = excess;
					if(amt == 0)
					{
						return 0;
					}
					//continue;
				}
			}
			return amt;
		}
	}
	
	public int addItems(ItemStack is)
	{
		return addItems(is.getItem(), is.getCount());
	}
	
	public void removeArtifact(Artifact toRemove)
	{
		for(int i = 0; i < slotsCount; i ++)
		{
			if(contents[i].getItem().equals(toRemove))
			{
				contents[i] = null;
			}
		}
	}
	
	public ItemStack removeItem(int slot)
	{
		ItemStack a = contents[slot];
		contents[slot] = null;
		return a;
	}
	
	public boolean removeItem(ItemStack stack)
	{
		for(int i = 0; i < slotsCount; i ++)
		{
			if(contents[i].equals(stack))
			{
				this.contents[i] = null;
				return true;
			}
		}
		return false;
	}
	
	public ItemStack[] search(String key)
	{
		ArrayList<ItemStack> toReturn = new ArrayList<>();
		for(ItemStack is : contents)
		{
			String name = is.getItem().getName();
			if(name.contains(key))
			{
				toReturn.add(is);
			}
		}
		return (ItemStack[]) toReturn.toArray();
	}
	
	@Override
	public String toString() {
		String toReturn = "{" + "MaxWeight: " + maxWeight + ", Slots: " + slotsCount + ", Contents: [";
		for(int i = 0; i < slotsCount; i++)
		{
			if(contents[i] != null)
			{
				toReturn += "Slot" + i + ": " + contents[i].toString();
				toReturn += ", ";
			}
		}
		toReturn += "]}";
		return toReturn;
	}
	
}
