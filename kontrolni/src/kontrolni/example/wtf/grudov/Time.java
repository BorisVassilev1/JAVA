package kontrolni.example.wtf.grudov;

public class Time {
	private int hours;
	private int minutes;
	
	public Time() {
		hours = 0;
		minutes = 1;
	}
	
	public Time(int hours, int minutes) { 
		this.hours = hours;
		this.minutes = minutes;
	}
	
	public void simplify() {
		if(minutes >= 60) {
			hours += minutes / 60;
			minutes %= 60;
		}
	}
	
	public void reset(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}
	
	public void addHours(int hours) {
		this.hours += hours;
		simplify();
	}
	
	public void addMinutes(int minutes) {
		this.minutes += minutes;
		simplify();
	}
	
	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}
	
	public void print() {
		System.out.println("Time: [ hours: " + hours + ", minutes: " + minutes + "]" );
	}
	
	public Time addTimes(Time t) {
		return new Time(this.hours + t.hours, this.minutes + t.minutes);
	}
	
	public static Time subTimes(Time t1, Time t2) {
		return new Time(t1.hours + t2.hours, t1.minutes + t2.minutes);
	}
	
	public static void main(String[] args) {
		Time t1 = new Time(10, 110);
		t1.simplify();
		Time t2 = new Time(2 , 3);
		t1.print();
		t2.print();
		
		t1.addTimes(t2).print();
		Time.subTimes(t1, t2).print();
		
		t1.addMinutes(t2.getHours());
		t2.addHours(t1.getMinutes());
		t1.print();
		t2.print();
		
	}		
}
