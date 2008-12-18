package testClass.copy;

import java.lang.reflect.Method;

public class DynamicLoader {
	public static void main(String[] args) throws Exception {
		Class toRun = Class.forName(args[0]);
		String[] newArgs = scrubArgs(args);
		Method mainMethod = findMain(toRun);
		mainMethod.invoke(null, new Object[] { newArgs });
	}

	private static String[] scrubArgs(String[] args) {
		String[] toReturn = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			toReturn[i - 1] = args[i].toLowerCase();
		}

		return toReturn;
	}

	private static Method findMain(Class clazz) throws Exception {
		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals("main"))
				return methods[i];
		}
		return null;
	}

}