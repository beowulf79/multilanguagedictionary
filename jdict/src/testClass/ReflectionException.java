package testClass;

import static java.lang.System.err;

import java.lang.reflect.InvocationTargetException;

public class ReflectionException {
    public ReflectionException() throws IllegalAccessException {
	throw new IllegalAccessException("exception in constructor");
    }

    public static void main(String... args) {
	try {
	    Class<?> c = Class
		    .forName("testClass.copy.reflection.ReflectionException");
	    // Method propagetes any exception thrown by the constructor
	    // (including
	    // checked exceptions).
	    if (args.length > 0 && args[0].equals("class")) {
		Object o = c.newInstance();
	    } else {
		Object o = c.getConstructor().newInstance();
	    }

	    // production code should handle these exceptions more gracefully
	} catch (ClassNotFoundException x) {
	    x.printStackTrace();
	} catch (InstantiationException x) {
	    x.printStackTrace();
	} catch (IllegalAccessException x) {
	    x.printStackTrace();
	} catch (NoSuchMethodException x) {
	    x.printStackTrace();
	} catch (InvocationTargetException x) {
	    x.printStackTrace();
	    err.format("%n%nCaught exception: %s%n", x.getCause());
	}

    }

}
