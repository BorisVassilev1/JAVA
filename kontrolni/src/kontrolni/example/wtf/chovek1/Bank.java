package kontrolni.example.wtf.chovek1;

public abstract class Bank {
	
	private String name;
	private long code;
	
	public Bank(String name, int code) {
		this.setName(name);
		this.setCode(code);
	}
	
	public abstract void addBranch(Branch branch);
	public abstract void removeBranch(Branch branch);
	
	public abstract Branch getBtanch();
	public abstract Branch getAllBranches();
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}


	public class Branch {
		long branch_code;
		String city;
		
		public void addAccount() {}
		public void removeAccount() {}
		public void getAccount() {}
		public void addLoan() {}
		public void removeLoan() {}
		public void getLoan() {}
	}
	
}
