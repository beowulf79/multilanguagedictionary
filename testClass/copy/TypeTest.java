package testClass.copy;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Field;

public class TypeTest {

	  public static void main (String args[]) {


	    try  {
	    Class<?> c = Class.forName("net.verza.jdict.Word");
	    System.out.println("Class type " + c.getName());
	    
	    Field field = c.getDeclaredField("linkID");
	    System.out.println("field type "+field.getGenericType());
	    
	    
	    Class<?> i = Class.forName("java.lang.Integer");
	    System.out.println("class "+i.toString());
	    
	    
	    
	    Integer array = new Integer(10);
	    Class<?> type = array.getClass();
	    if (type.isArray()) {
	      Class<?> elementType = type.getComponentType();
	      System.out.println("Array of: " + elementType);
	      System.out.println("Array size: " + Array.getLength(array));
	    }
	    
	    
	    } catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException "+e.getMessage());
		} catch (NoSuchFieldException e) {
			System.out.println("ClassNotFoundException "+e.getMessage());
		}

	  }

}
