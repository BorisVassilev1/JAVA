package kontrolni.example.wtf.boqn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Car {
    private String brand;
    private int years;
    private float weight;
    private String number;

    public Car(String brand, int years,float weight,String number) {
        setBrand(brand);
        setYears(years);
        setWeight(weight);
        setNumber(number);
    }

    public Car() {
        this("BMW", 2005, 150.3f, "CA 1234 BG");
    }

    public Car(Car value) {
        this(value.brand, value.years, value.weight, value.number);
    }

    public void setNumber(String value){
        Pattern pattern = Pattern.compile("^[A-Z]{2}\s[0-9]{4}\s[A-Z]{2}$");
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()){
            number = value;
            return;
        } 
        throw new RuntimeException("Invalid Number");
        
    }
    
    public String getNumber(){
        return number;
    }

    public void setBrand(String value) {
        brand = value;
    }

    public String getBrand() {
        return brand;
    }

    public void setYears(int value) {
        years = value;
    }

    public int getYears() {
        return years;
    }

    public void setWeight(float value) {
        weight = value;
    }

    public float getWeight() {
        return weight;
    }

    public void WeightChange(float change){
        weight += change;
    }
    
    public boolean isWeightAbove(){
        return weight > 3.5;
    }
    
    public static String toString(Car[] arr){
        String string = "";
        
        for(Car car : arr){
            string += String.format("%s %d %.1f %s \n" ,
                         car.getBrand(), car.getYears(), car.getWeight(), car.getNumber());
        }
        
        return string;
    }
    
    public static void toFile(String fileName,Car[] arr) throws IOException {
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        
        writer.write(Car.toString(arr));
        writer.close();
        
    }
}
