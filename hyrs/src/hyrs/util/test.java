package hyrs.util;

public class test {

	public static String s(String IDNumber) {
		if (IDNumber.matches("^[a-zA-Z0-9_]{8,20}$")) {
			return "正确";	
		}else{
			 return "错误";	
		}
	}
	
	public static void main(String[] args) {
		String s=null;
		System.out.println(s.toString());
	}

}
