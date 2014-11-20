package xinquan.cn.diandian.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class GroupBean implements Serializable {
	private String housesId = ""; // 樓盤ID
	private String housesName = ""; // 楼盘名称
	private String discount_note; // 优惠信息
	private String picture; // 图片路径
	private String price; // 价格
	private String day; // 天
	private String hour; // 小时
	private String scale_price; //
	private String cityName = ""; // 城市名称
	private String cityId = ""; // 城市ID
	private String areaId; // 地区ID
	private String areaName; // 地区名称

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

	public String getDiscount_note() {
		return discount_note;
	}

	public void setDiscount_note(String discount_note) {
		this.discount_note = discount_note;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getScale_price() {
		return scale_price;
	}

	public void setScale_price(String scale_price) {
		this.scale_price = scale_price;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
