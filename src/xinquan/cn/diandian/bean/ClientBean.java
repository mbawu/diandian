package xinquan.cn.diandian.bean;

import java.util.ArrayList;
/*
 * 客户Bean
 */
public class ClientBean {
	private String day;  //天
	private String name;  //名称
	private String phone;  //电话
	private String addTime_hour;   
	private ArrayList<AvailabilityBean> list;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddTime_hour() {
		return addTime_hour;
	}

	public void setAddTime_hour(String addTime_hour) {
		this.addTime_hour = addTime_hour;
	}

	public ArrayList<AvailabilityBean> getList() {
		return list;
	}

	public void setList(ArrayList<AvailabilityBean> list) {
		this.list = list;
	}

}
