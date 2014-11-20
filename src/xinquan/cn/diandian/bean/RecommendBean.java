package xinquan.cn.diandian.bean;

import java.io.Serializable;

public class RecommendBean implements Serializable {
	private String housesId = "";       //楼盘ID
	private String housesName = "";     //楼盘名称
	private String city_name = "";      //城市名称
	private String area_name = "";      //地区名称
	private String areaId = "";          //地区ID
	private String cityId = "";         //城市ID
	private int sex;//性别  sex   1男   2女  


	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getHousesId() {
		return housesId;
	}

	public void setHousesId(String housesId) {
		this.housesId = housesId;
	}

	public String getHousesName() {
		return housesName;
	}

	public void setHousesName(String housesName) {
		this.housesName = housesName;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}
