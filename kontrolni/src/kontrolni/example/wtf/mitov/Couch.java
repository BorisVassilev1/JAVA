package kontrolni.example.wtf.mitov;

public class Couch extends Furniture{
	private String type;
	private String material;
	
	public int numSeats() {
		return 0; //TODO
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
}
