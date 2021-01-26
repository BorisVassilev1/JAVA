package kontrolni.example.wtf.krasi;

public class Test {
	public static void main(String[] args) {
		Cars kars = new Cars();
		kars.input();
		System.out.println(kars.toString());
		kars.delete("asdf");
		System.out.println(kars.toString());
		
	}
}
