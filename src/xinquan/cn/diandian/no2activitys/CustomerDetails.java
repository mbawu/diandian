package xinquan.cn.diandian.no2activitys;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.RecommendActivity;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.CustomerBean;
import xinquan.cn.diandian.bean.CustomerHouseBean;
import xinquan.cn.diandian.bean.RecommendBean;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/*
 * 客户详情Activity
 */
public class CustomerDetails extends Activity implements OnClickListener {
	private String id; //
	private TextView name; // 客户名称
	private TextView phone; // 客户联系电话
	private TextView clien_time; // 添加时间
	private Myad ad; // 适配器
	private ListView lv; // listView
	private ArrayList<CustomerHouseBean> al; // 客户详情数据源
	private Button add;
	private CustomerBean customerbean = new CustomerBean();
	private TitleBarContainer mTitleBar;
	private int firstLog=1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.customerdetails);
		initview();
		initdata();
		initlistener();
	}

	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
	}

	/*
	 * 初始化客户详情数据
	 */
	private void initdata() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "user");
		ha.put("c", "user");
		ha.put("a", "get_clientele_info");
		ha.put("user_key", MyApplication.seskey);
		ha.put("userId", MyApplication.sp.getString("userid", "-1"));
		Log.e("fffffff", id);
		ha.put("phone", id);
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								name.setText(response.getString("name"));
								phone.setText(response.getString("phone"));
								customerbean.userName = response.getString("name");
								customerbean.phone = response.getString("phone");
								clien_time.setText(response.getString("clien_time"));
								
								JSONArray ja = response.getJSONArray("list");

								for (int i = 0; i < ja.length(); i++) {
									CustomerHouseBean cb = new CustomerHouseBean();
									JSONObject jb = ja.getJSONObject(i);
									cb.setAddTime(jb.getString("addTime"));
									cb.setBargain_state(jb.getString("bargain_state"));
									cb.setBargain_time(jb.getString("bargain_time"));
									cb.setCity_name(jb.getString("city_name"));
									cb.setDevelopers_state(jb.getString("developers_state"));
									cb.setDevelopers_time(jb.getString("developers_time"));
									cb.setHousesName(jb.getString("housesName"));
									cb.setMake_state(jb.getString("make_state"));
									cb.setMake_time(jb.getString("make_time"));
									cb.setMoney_state(jb.getString("money_state"));
									cb.setMoney_time(jb.getString("money_time"));
									cb.setReserve_state(jb.getString("reserve_state"));
									cb.setReserve_time(jb.getString("reserve_time"));
									cb.setVisit_state(jb.getString("visit_state"));
									cb.setVisit_time(jb.getString("visit_time"));
									cb.setLast_state(jb.getString("last_state"));
									cb.setLast_state_value(jb.getString("last_state_value"));
									
									RecommendBean rb = new RecommendBean();
									/*
									 楼盘名称      housesName
									楼盘区域名称      area_name
									楼盘区域编号      areaId
									楼盘城市名称      city_name
									楼盘城市编号      cityId
									*/
									
									rb.setArea_name(jb.getString("area_name"));
									rb.setAreaId(jb.getString("areaId"));
									rb.setCity_name(jb.getString("city_name"));
									rb.setCityId(jb.getString("cityId"));
									rb.setHousesId(jb.getString("housesId"));
									rb.setHousesName(cb.getHousesName());
									rb.setSex(jb.getInt("sex"));
									customerbean.setSex(jb.getInt("sex"));
									customerbean.beans.add(rb);
									al.add(cb);
								}
								if(al.size()>=3) add.setVisibility(View.INVISIBLE);
								ad.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("fffffff", "00");
					}
				});

	}

	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_customer_detail);
		mTitleBar.setRightMenuVisible(false);
		
		al = new ArrayList<CustomerHouseBean>();
		lv = (ListView) findViewById(R.id.lv);
		View view = MyApplication.lf.inflate(R.layout.customerfooter, null);
		lv.addFooterView(view);
		ad = new Myad();
		lv.setAdapter(ad);
		/*
		 * 从上个Activity返回的数据信息
		 */
		id = getIntent().getExtras().getString("phone");
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		clien_time = (TextView) findViewById(R.id.clien_time);
		
		add = (Button)findViewById(R.id.add);
		add.setOnClickListener(this);
	}

	/*
	 * 客户详情里诶包ListView数据适配器
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			if (al == null) {
				return 0;
			} else {
				return al.size();
			}

		}

		@Override
		public Object getItem(int position) {

			return al.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CustomerHouseBean cb = al.get(position);
			View view = MyApplication.lf.inflate(R.layout.customerdetails_item,
					null);
			TextView housesName = (TextView) view.findViewById(R.id.housesName);
			TextView state = (TextView) view.findViewById(R.id.state);
			TextView addTime = (TextView) view.findViewById(R.id.addTime);
			View diandianshenhe = view.findViewById(R.id.diandianshenhe);
			ImageView diandiangou = (ImageView) view
					.findViewById(R.id.diandiangou);
			ImageView diandianprocess = (ImageView) view
					.findViewById(R.id.diandianprocess);
			TextView diandiantime = (TextView) view
					.findViewById(R.id.diandiantime);

			View kaifashangshenhe = view.findViewById(R.id.kaifashangshenhe);
			ImageView kaifashanggou = (ImageView) view
					.findViewById(R.id.kaifashanggou);
			ImageView kaifashangprocess = (ImageView) view
					.findViewById(R.id.kaifashangshenheprocess);
			TextView kaifashangtime = (TextView) view
					.findViewById(R.id.kaifashangtime);
			View yidaofangshenhe = view.findViewById(R.id.yidaofangshenhe);
			ImageView yidaofanggou = (ImageView) view
					.findViewById(R.id.yidaofanggou);
			ImageView yidaofangprocess = (ImageView) view
					.findViewById(R.id.yidaofangprocess);
			TextView yidaofangtime = (TextView) view
					.findViewById(R.id.yidaofangtime);

			View yirengoushenhe = view.findViewById(R.id.yirengoushenhe);
			ImageView yirengougou = (ImageView) view
					.findViewById(R.id.yirengougou);
			ImageView yirengouprocess = (ImageView) view
					.findViewById(R.id.yirengougprocess);
			TextView yirengoutime = (TextView) view
					.findViewById(R.id.yirengoutime);

			View yichengjiaoshenhe = view.findViewById(R.id.yichengjiaoshenhe);
			ImageView yichengjiaogou = (ImageView) view
					.findViewById(R.id.yichengjiaogou);
			ImageView yichengjiaoprocess = (ImageView) view
					.findViewById(R.id.yichengjiaoprocess);
			TextView yichengjiaotime = (TextView) view
					.findViewById(R.id.yichengjiaotime);
			View yijieyongshenhe = view.findViewById(R.id.yijieyongshenhe);
			ImageView yijieyonggou = (ImageView) view
					.findViewById(R.id.yijieyonggou);
			ImageView yijieyongprocess = (ImageView) view
					.findViewById(R.id.yijieyongprocess);
			TextView yijieyongtime = (TextView) view
					.findViewById(R.id.yijieyongtime);
			housesName.setText(cb.getHousesName());
			addTime.setText(cb.getAddTime());
			/*
			 * 判断数据审核到了哪一个步以展示响应的数据
			 */
			
			int stateValue = Integer.parseInt(cb.getLast_state());
			switch (stateValue) {
			/*
			 * 点点审核
			 */
			case 1:
				diandianshenhe.setVisibility(View.VISIBLE);
				diandiantime.setText(cb.getBargain_time());
				diandianprocess.setBackgroundResource(R.drawable.customer_process_only);
				if (cb.getLast_state_value().equals("1")) {
					diandiangou.setBackgroundResource(R.drawable.clent_yes);
					state.setText("点点审核");
				} else {
					diandiangou.setBackgroundResource(R.drawable.clent_no);
					state.setText("审核未通过");
				}
				break;
			/*
			 * 开发商审核
			 */
			case 2:
				diandianshenhe.setVisibility(View.VISIBLE);
				kaifashangshenhe.setVisibility(View.VISIBLE);
				diandiangou.setBackgroundResource(R.drawable.clent_yes);
				kaifashangprocess.setBackgroundResource(R.drawable.customer_process_1);
				diandiantime.setText(cb.getBargain_time());
				kaifashangtime.setText(cb.getDevelopers_time());
				if (cb.getLast_state_value().equals("1")) {
					state.setText("开发商审核");
					kaifashanggou.setBackgroundResource(R.drawable.clent_yes);
				} else {
					state.setText("开发商审核");
					kaifashanggou.setBackgroundResource(R.drawable.clent_no);
				}
				break;
			/*
			 * 已到访
			 */
			case 3:
				diandianshenhe.setVisibility(View.VISIBLE);
				kaifashangshenhe.setVisibility(View.VISIBLE);
				yidaofangshenhe.setVisibility(View.VISIBLE);
				yidaofangprocess.setBackgroundResource(R.drawable.customer_process_1);
				diandiangou.setBackgroundResource(R.drawable.clent_yes);
				kaifashanggou.setBackgroundResource(R.drawable.clent_yes);
				diandiantime.setText(cb.getBargain_time());
				kaifashangtime.setText(cb.getDevelopers_time());
				yidaofangtime.setText(cb.getVisit_time());
				if (cb.getLast_state_value().equals("1")) {
					state.setText("已到访");
					yidaofanggou.setBackgroundResource(R.drawable.clent_yes);
				} else {
					state.setText("已到访");
					yidaofanggou.setBackgroundResource(R.drawable.clent_no);
				}
				break;
			/*
			 * 已认购
			 */
			case 4:
				diandianshenhe.setVisibility(View.VISIBLE);
				kaifashangshenhe.setVisibility(View.VISIBLE);
				yidaofangshenhe.setVisibility(View.VISIBLE);
				yirengoushenhe.setVisibility(View.VISIBLE);
				diandiangou.setBackgroundResource(R.drawable.clent_yes);
				kaifashanggou.setBackgroundResource(R.drawable.clent_yes);
				yidaofanggou.setBackgroundResource(R.drawable.clent_yes);
				yirengouprocess.setBackgroundResource(R.drawable.customer_process_1);
				diandiantime.setText(cb.getBargain_time());
				kaifashangtime.setText(cb.getDevelopers_time());
				yidaofangtime.setText(cb.getVisit_time());
				yirengoutime.setText(cb.getReserve_time());
				if (cb.getLast_state_value().equals("1")) {
					state.setText("已认购");
					yirengougou.setBackgroundResource(R.drawable.clent_yes);
				} else {
					state.setText("已认购");
					yirengougou.setBackgroundResource(R.drawable.clent_no);
				}
				break;
			/*
			 * 已成交
			 */
			case 5:
				diandianshenhe.setVisibility(View.VISIBLE);
				kaifashangshenhe.setVisibility(View.VISIBLE);
				yidaofangshenhe.setVisibility(View.VISIBLE);
				yirengoushenhe.setVisibility(View.VISIBLE);
				yichengjiaoshenhe.setVisibility(View.VISIBLE);
				diandiangou.setBackgroundResource(R.drawable.clent_yes);
				kaifashanggou.setBackgroundResource(R.drawable.clent_yes);
				yidaofanggou.setBackgroundResource(R.drawable.clent_yes);
				yirengougou.setBackgroundResource(R.drawable.clent_yes);
				yichengjiaoprocess.setBackgroundResource(R.drawable.customer_process_1);
				diandiantime.setText(cb.getBargain_time());
				kaifashangtime.setText(cb.getDevelopers_time());
				yidaofangtime.setText(cb.getVisit_time());
				yirengoutime.setText(cb.getReserve_time());
				yichengjiaotime.setText(cb.getMake_time());
				if (cb.getLast_state_value().equals("1")) {
					state.setText("已成交");
					yichengjiaogou.setBackgroundResource(R.drawable.clent_yes);
				} else {
					state.setText("已成交");
					yichengjiaogou.setBackgroundResource(R.drawable.clent_no);
				}
				break;
			/*
			 * 已结佣
			 */
			case 6:
				diandianshenhe.setVisibility(View.VISIBLE);
				kaifashangshenhe.setVisibility(View.VISIBLE);
				yidaofangshenhe.setVisibility(View.VISIBLE);
				yirengoushenhe.setVisibility(View.VISIBLE);
				yichengjiaoshenhe.setVisibility(View.VISIBLE);
				yijieyongshenhe.setVisibility(View.VISIBLE);
				diandiangou.setBackgroundResource(R.drawable.clent_yes);
				kaifashanggou.setBackgroundResource(R.drawable.clent_yes);
				yidaofanggou.setBackgroundResource(R.drawable.clent_yes);
				yirengougou.setBackgroundResource(R.drawable.clent_yes);
				yichengjiaogou.setBackgroundResource(R.drawable.clent_yes);
				yijieyonggou.setBackgroundResource(R.drawable.clent_yes);
				diandiantime.setText(cb.getBargain_time());
				kaifashangtime.setText(cb.getDevelopers_time());
				yidaofangtime.setText(cb.getVisit_time());
				yirengoutime.setText(cb.getReserve_time());
				yichengjiaotime.setText(cb.getMake_time());
				yijieyongtime.setText(cb.getMoney_time());
				if (cb.getLast_state_value().equals("1")) {
					state.setText("已结佣");
					yichengjiaogou.setBackgroundResource(R.drawable.clent_yes);
				} else {
					state.setText("已结佣");
					yichengjiaogou.setBackgroundResource(R.drawable.clent_no);
				}
				break;

			default:
				break;
			}
			return view;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MyApplication.costomerDetailneedflush&&firstLog!=1)
		{
			al.clear();
		initdata();
		MyApplication.costomerDetailneedflush=false;
		}
		firstLog++;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_icon_left_layout:
			CustomerDetails.this.finish();
			break;
		case R.id.add:
			Intent in = new Intent(this, RecommendActivity.class);

			in.putExtra("customerbean", customerbean);
			
			startActivity(in);
			
			break;

		default:
			break;
		}

	}

}
