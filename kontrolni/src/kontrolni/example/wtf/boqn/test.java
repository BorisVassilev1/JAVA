package kontrolni.example.wtf.boqn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random rand = new Random();
        int n = input.nextInt();
        System.out.println();
        Car[] arr = new Car[n];
        for (int i = 0; i < n; i++) {
            String brand = input.nextLine();
            int years = rand.nextInt(20);
            float weight = input.nextFloat();
            String number = input.nextLine().substring(1);
            System.out.println("\"" + number + "\"");
            try {
            arr[i] = new Car(brand, years, weight, number);
            } catch(Exception e) {
            	e.printStackTrace();
            }
        }

        //input.close();
        try{
            Car.toFile("./a.txt",arr);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("asdas");
        try{
            new Car("BMW",10,200,"ZXCVSAD");
        }
        catch(RuntimeException e){
            e.printStackTrace();
        }
        
        System.out.println(Car.toString(arr));
    }
	
	public static void toFile(String fileName) throws IOException {
		File file = new File(fileName);
		FileWriter writer = new FileWriter(file);
		
		writer.write("jica e na baba ti prostora!");
		writer.close();
		
	}
	
}
