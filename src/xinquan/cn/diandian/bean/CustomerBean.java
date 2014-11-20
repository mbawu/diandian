package xinquan.cn.diandian.bean;

import java.util.ArrayList;
import java.util.List;

public class CustomerBean implements java.io.Serializable{
	
	public String userName;
	
	public String phone;
	
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int sex;//性别  sex   1男   2女  
	
	public List<RecommendBean> beans = new ArrayList<RecommendBean>();
}
