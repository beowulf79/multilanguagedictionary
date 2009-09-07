
package testClass.copy.reflection;

import java.util.TreeMap;
import java.util.Random;
import java.util.Iterator;

public class ComparableObject implements Comparable<Object> {

	private int myInt;

	public ComparableObject(int inInt) {
		myInt = inInt;
	}

	public int compareTo(Object inObject) {
		ComparableObject obj = (ComparableObject)inObject;		
		System.out.println("comparing this.myint "+this.myInt +" against inObject.myint "+obj.myInt );
		int retcode = myInt - ((ComparableObject) inObject).myInt;
		System.out.println("called method compareTo return code "+retcode);
		return retcode;
	}

	public boolean equals(Object inObject) {
		System.out.println("called method equals ");
		if (this.myInt == ((ComparableObject)inObject).myInt) return true;
		
		return false;
	}

	public int hashCode() {
		Random generator = new Random();
		int rnd = generator.nextInt();
		System.out.println("called method hashCode will return "+rnd);
		return rnd;
	}

	public static void main(String[] args) {
		
		ComparableObject a = new ComparableObject(5);
		ComparableObject b = new ComparableObject(6);
		ComparableObject c = new ComparableObject(7);
		ComparableObject d = new ComparableObject(8);
		ComparableObject e = new ComparableObject(9);
		ComparableObject f = new ComparableObject(22);

		TreeMap<ComparableObject, Integer> map = new TreeMap<ComparableObject, Integer>();
		map.put(a, 0);
		System.out.println("-----------");
		map.put(b,1); // the second put calls the method compareTo
		System.out.println("-----------");
		map.put(c,2); // if i try to put the same key it overwrites the previous
		System.out.println("-----------");
		map.put(d,3);
		System.out.println("-----------");
		map.put(e,4);
		System.out.println("-----------");
		map.put(e,5);
		System.out.println("-----------");
		map.put(f,3);
		System.out.println("-----------");
		map.put(f,3);
		System.out.println("-----------");
		map.put(a,8);
		System.out.println("-----------");
		
	//	if(map.containsKey(b))	// if containsKey returns true update the value
		//	map.put(b, 3);
		
		/*
		System.out.println("contains key?? "+map.containsKey(b));		//containsKey calls the method compareTo
		
		
		// trying to get ComparableObject that hasn't been put yet
		System.out.println("ComparableObject c "+map.get(c));
		*/
		
		System.out.println("Print");
		
		Iterator<ComparableObject> itr = map.keySet().iterator();
		while(itr.hasNext())	{
			ComparableObject key = itr.next();
			System.out.println("key "+key.myInt+" value "+map.get(key));	//containsKey calls the method compareTo
			
		}
		

		
		
	}

}
