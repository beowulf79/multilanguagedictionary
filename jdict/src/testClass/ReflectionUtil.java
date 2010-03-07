package testClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static void describeInstance(Object object) {
	Class<?> clazz = object.getClass();

	Constructor<?>[] constructors = clazz.getDeclaredConstructors();
	Field[] fields = clazz.getDeclaredFields();

	Method[] methods = clazz.getDeclaredMethods();

	System.out.println("Description for class: " + clazz.getName());
	System.out.println();
	System.out.println("Summary");
	System.out.println("-----------------------------------------");
	System.out.println("Constructors: " + (constructors.length));
	System.out.println("Fields: " + (fields.length));
	System.out.println("Methods: " + (methods.length));

	System.out.println();
	System.out.println();
	System.out.println("Details");
	System.out.println("-----------------------------------------");

	if (constructors.length > 0) {
	    System.out.println();
	    System.out.println("Constructors:");
	    for (Constructor<?> constructor : constructors) {
		System.out.println(constructor);
	    }
	}

	if (fields.length > 0) {
	    System.out.println();
	    System.out.println("Fields:");
	    for (Field field : fields) {
		System.out.println(field);
	    }
	}

	if (methods.length > 0) {
	    System.out.println();
	    System.out.println("Methods:");
	    for (Method method : methods) {
		System.out.println(method);
	    }
	}

	try {
	    Class<?> keyclass = Class
		    .forName("testClass.copy.reflection.Product");
	    Class<?>[] ConstructorTypes = new Class[] {};
	    Constructor<?> keyClassConstructor = keyclass
		    .getConstructor(ConstructorTypes);
	    Product p_ = (Product) keyClassConstructor.newInstance();

	    Class<?>[] methodTypes = new Class[] { java.lang.String.class };
	    Method Method2Call;
	    Method2Call = keyclass.getMethod("getcode", methodTypes);
	    Method2Call.invoke(p_, (Object) "piaggio");

	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	}

    }

    public static void main(String[] args) throws Exception {
	Product product = new Product();

	ReflectionUtil.describeInstance(product);
    }

}