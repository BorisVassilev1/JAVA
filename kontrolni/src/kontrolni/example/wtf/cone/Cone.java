package kontrolni.example.wtf.cone;

import java.util.Arrays;

public class Cone {
    private double radius;
    private double height;
    
    public Cone(){
        radius = 1;
        height = 1;
    }
    public Cone(double radius, double height){
        this.radius = radius;
        this.height = height;
    }
    
    public void setRadius(double radius){
        this.radius = radius;
    }
    public double getRadius(){
        return radius;
    }
    public void setHeight(double height){
        this.height = height;
    }
    public double getHeight(){
        return height;
    }
    
    public double V(){
        double V = Math.PI*(radius*radius)*height/3;
        return V;
    }
    public double S(){
        double l = Math.sqrt(Math.pow(height, 2)+Math.pow(radius, 2));
        double S = Math.PI*radius*radius + Math.PI*radius*l;
        return S;
    }
    public boolean isHigher(Cone c){
        if(height>c.getHeight()){
            return true;
        }
        return false;
    }
    
    public static boolean areEqual(Cone c1, Cone c2){
        if(c1.getHeight()==c2.getHeight() && c1.getRadius()==c2.getRadius()){
            return true;
        }
        return false;
    }
    
    @Override
    public String toString(){
        String s = "Cone: [height: " + height + ", radius: " + radius + "]";
		return s;
    }
    
    public static void sortS(Cone [] cones) {
    	Arrays.sort(cones, 
    			(Cone a, Cone b) -> {
    				double s1 = a.S();
    				double s2 = b.S();
    				if(s1 < s2) return -1;
    				else if(s1 > s2) return 1;
    				return 0;
    			});
    }
    
}
