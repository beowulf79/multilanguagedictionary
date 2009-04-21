package testClass.copy.reflection;


public class Item {

	private String code;

	public Item(String s)	{
		System.out.println("called class "+this.getClass().getName());
		this.code = s;
	}

	public String getcode(String s )	{
		System.out.println("this.code "+this.code);
		return this.code;
	}


}



