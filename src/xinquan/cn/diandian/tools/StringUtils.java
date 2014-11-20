package xinquan.cn.diandian.tools;

import xinquan.cn.diandian.R;
import android.content.Context;

public class StringUtils {
	
	public static boolean isEmpty(String data){
		
		if(data==null || data.trim().length()==0)
			return true;
		return false;
	}
	
	public static boolean isEqual(String data1, String data2){
		if(data1==null && data2==null){
			return true;
		}else if(data1!=null && data2!=null){
			return data1.equals(data2);
		}
		return false;
	}
	
	public static String convertIntString(String str){
		try{
			return (int)Float.parseFloat(str) +"";
		}catch(Exception e){
			return str;
		}
	}
	
	public static String convertPhoneString(String str){
		if(str == null)
			return null;
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < str.length(); i++){
			if(Character.isDigit(str.charAt(i))){
				sb.append(str.charAt(i));
			}
		}
		
		return sb.toString();
	}
	
	public static String getPriceString(Context context, String price) {
		int lastdot = price.lastIndexOf(".");
		if (lastdot > 0) {
			price = price.substring(0, lastdot);
		}
		return context.getString(R.string.price_unit, price);
	}
	
	public static String getPriceCommission(Context context, String price) {
		int lastdot = price.lastIndexOf(".");
		if (lastdot > 0) {
			price = price.substring(0, lastdot);
		}
		return context.getString(R.string.yuan, price);
	}
}
