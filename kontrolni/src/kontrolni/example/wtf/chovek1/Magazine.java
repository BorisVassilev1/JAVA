package kontrolni.example.wtf.chovek1;

public class Magazine {
	private String name;
	private String type;
	private int price;
	
	public void gain_knowledge() { // ne e kazano kakvo trqbva da pravi
		//do something
	}
	
	public Magazine(String name, String type, int price) {
		this.name = name;
		this.type = type;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
