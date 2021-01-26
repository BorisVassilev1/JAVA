package kontrolni.example.wtf.chovek1;

public class Library {
	private String name;
	private String type;
	
	private Magazine magazine;
	
	public void issue_book() { // ne e kazano kakuv tip trqba da vrushta toq method
		/// do something
	}
	
	public Library(String name, String type, Magazine magazine) {
		// TODO Auto-generated constructor stub
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

	public Magazine getMagazine() {
		return magazine;
	}

	public void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}
}
