package testClass.copy;



public class Test {

	public static void main(String args[]) {
		
		String[] tmp = { "1"};
		if (tmp != null) {
			String[] src_linkID = tmp[0].split(",");
			System.out.println("LinkID lenght " + src_linkID.length);
			for (int i = 0; i < src_linkID.length; i++) {
				System.out.println("searching ID " + src_linkID[i]);
				
			}
		}
	}
	
	
	
}
