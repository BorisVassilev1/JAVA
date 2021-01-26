package kontrolni.example.wtf.krasi;

import java.util.Scanner;

public class Car {
	String model;
	float price;
	int year;
	
	public Car() {
		model = null;
		price = -1;
		year = -1;
	}
	
	public Car(String model, float price, int year) {
		super();
		this.model = model;
		this.price = price;
		this.year = year;
	}
	
	public static Car input() {
		Car c = new Car();
		Scanner sc = new Scanner(System.in);
		c.setModel(sc.next());
		c.setPrice(sc.nextFloat());
		c.setYear(sc.nextInt());
		return c;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public String toString() {
		return "Car [model=" + model + ", price=" + price + ", year=" + year + "]";
	}
}
