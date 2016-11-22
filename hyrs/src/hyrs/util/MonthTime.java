package hyrs.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthTime {
	
	SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
	SimpleDateFormat MM = new SimpleDateFormat("MM");
	SimpleDateFormat dd = new SimpleDateFormat("dd");
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String year = yyyy.format(new Date()); 
	private String month = MM.format(new Date());
	private String date = dd.format(new Date());

	public String GetMonth() {
		if (Integer.parseInt(date) > 5) {
			int DealMonth = Integer.parseInt(month)+1;
			if (Integer.parseInt(month) > 8 && Integer.parseInt(month) < 11 ) {
				String Dmonth = year + DealMonth;
				return Dmonth;
			} else if(Integer.parseInt(month) > 11){
				String Dmonth =String.valueOf(Integer.parseInt(yyyy.format(new Date()))+1)+"01";
				return Dmonth;
			} else {
				String Dmonth = year + DealMonth;
				return Dmonth;
			}
		} else {
			int DealMonth = Integer.parseInt(month);
			if (Integer.parseInt(month) > 9) {
				String Dmonth = year + DealMonth;
				return Dmonth;
			} else {
				String Dmonth = year + "0" + DealMonth;
				return Dmonth;
			}
		}
	}
	
	public String GetTime() {
		return format.format(new Date());
	}
	public static void main(String[] args){
		System.out.println(new MonthTime().GetMonth());
	}
}