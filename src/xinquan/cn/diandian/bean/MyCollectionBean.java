package xinquan.cn.diandian.bean;

import org.json.JSONObject;

public class MyCollectionBean {
	private String collectId;    //收藏ID
	private String userId;      //用户ID
	private String addTime;    //添加时间
	private String housesId;     //楼盘ID
	private String housesName;   //楼盘名称
	private JSONObject discount_time;   //打折倒计时
	private String discount_note;    //优惠信息
	private String picture;      //图片信息
	private String price;      //价格
	private String scale_price;     
	private Boolean yincang = true;  //是否隱藏
	private int position;     
	private String area_name;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Boolean getYincang() {
		return yincang;
	}

	public void setYincang(Boolean yincang) {
		this.yincang = yincang;
	}

	public String getScale_price() {
		return scale_price;
	}

	public void setScale_price(String scale_price) {
		this.scale_price = scale_price;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
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

	public JSONObject getDiscount_time() {
		return discount_time;
	}

	public void setDiscount_time(JSONObject discount_time) {
		this.discount_time = discount_time;
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

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

}
