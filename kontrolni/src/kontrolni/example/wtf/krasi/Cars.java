package kontrolni.example.wtf.krasi;

import java.util.ArrayList;
import java.util.Scanner;

public class Cars {
	ArrayList<Car> cars;
	int MAX;
	
	public Cars() {
		cars = null;
		MAX = 0;
	}
	
	public void add(Car c) {
		if(cars.size() < MAX)
			cars.add(c);
	}
	
	public void input() {
		Scanner sc = new Scanner(System.in);
		int size = sc.nextInt();
		cars = new ArrayList<>();
		for(int i = 0; i < size; i ++) {
			cars.add(Car.input());
		}
	}
	
	public void delete(String model) {
		for(int i = 0; i < cars.size(); i ++) {
			if(cars.get(i).getModel().equals(model)) {
				cars.remove(i);
				i -= 1;
			}
		}
	}
	
	@Override
	public String toString() {
		return "Cars [cars=" + cars.toString() + ", MAX=" + MAX + "]";
	}
}
