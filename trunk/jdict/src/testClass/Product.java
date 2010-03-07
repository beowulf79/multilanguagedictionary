package testClass;

public class Product extends Item {

    private String id;

    public Product() {
	super("0x123");
	System.out.println("called class " + this.getClass().getName());
	this.id = "10";
    }

    public String getid() {
	return this.id;
    }

}
