package xinquan.cn.diandian.no1activitys;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.RecommendActivity;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.GroupBean;
import xinquan.cn.diandian.bean.RecommendBean;
import xinquan.cn.diandian.login.LoginActivity;
import xinquan.cn.diandian.tools.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 特惠团购Activity
 * 
 */
public class GroupbuyingActivity extends Activity implements OnClickListener {
	private ListView lv; // 特惠团购列表listView
	private Myad ad; // listView适配器
	private ArrayList<GroupBean> al; // listView 数据源
	private int page = 1; // 请求页数
	private int totalpage = 1; // 总页数
	private int lastitem; // listView滑动时候最后显示的的item
	private Boolean jiazai = true; // 数据是否加载完毕
	private Button intercept; // 屏蔽按钮当数据正在加载的时候 设置其为显示以屏蔽触摸事件 此按钮为透明按钮
	private View footView; // footview
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupbuy);
		initview();
		initdata();
		initlistener();
	}

	/*
	 * 初四花数据
	 */
	private void initdata() {
		initlvdata();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 4 + 1 && jiazai) {
					jiazai = false;
					intercept.setVisibility(View.GONE);
					footView.setVisibility(View.VISIBLE);
					initlvdata();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id = (String) arg1.findViewById(R.id.housesId).getTag();
				Intent in = new Intent(GroupbuyingActivity.this,
						HouseDetails.class);
				in.putExtra("id", id);
				startActivity(in);
			}
		});

	}

	/*
	 * 加载ListVIew数据
	 */
	private void initlvdata() {
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "houses");
			ha.put("c", "houses");
			ha.put("a", "houses");
			ha.put("rowCount", "4");
			ha.put("cityId", MyApplication.sp.getString("cityid", "-1"));
			ha.put("houses_type", "1");
			ha.put("page", "" + page);
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.e("sdgssssddSg", response.toString());
							try {
								if (response.getInt("code") == 1) {
									page = page + 1;
									totalpage = Integer.parseInt(response
											.getString("totlepage"));
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										JSONObject jj = jb
												.getJSONObject("discount_time");
										GroupBean gb = new GroupBean();
										gb.setDiscount_note(jb
												.getString("discount_note"));
										gb.setPicture(jb.getString("picture"));
										gb.setPrice(jb.getString("price"));
										gb.setDay(jj.getString("day"));
										gb.setHour(jj.getString("hour"));
										gb.setScale_price(jb
												.getString("scale_price"));
										gb.setAreaId(jb.getString("areaId"));
										gb.setAreaName(jb
												.getString("area_name"));
										gb.setCityName(jb
												.getString("city_name"));
										gb.setCityId(jb.getString("cityId"));
										gb.setHousesId(jb.getString("housesId"));
										gb.setHousesName(jb
												.getString("housesName"));
										al.add(gb);
									}
									ad.notifyDataSetChanged();
									footView.setVisibility(View.GONE);
									intercept.setVisibility(View.GONE);
								} else {
									ad.notifyDataSetChanged();
									footView.setVisibility(View.GONE);
									intercept.setVisibility(View.GONE);
								}
							} catch (JSONException e) {
								footView.setVisibility(View.GONE);
								intercept.setVisibility(View.GONE);
								e.printStackTrace();
							}
							jiazai = true;
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							footView.setVisibility(View.GONE);
							intercept.setVisibility(View.GONE);
							jiazai = true;
						}
					});
		}
	}

	/*
	 * 初始化VIew指針
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_group_buy);
		mTitleBar.setRightMenuVisible(false);
		al = new ArrayList<GroupBean>();
		lv = (ListView) findViewById(R.id.lv);
		footView = MyApplication.lf.inflate(R.layout.footview, null);
		ad = new Myad();
		lv.addFooterView(footView);
		lv.setAdapter(ad);
		intercept = (Button) findViewById(R.id.intercept);
	}

	/*
	 * ListView 数据适配器
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GroupBean gb = al.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(R.layout.group_item,
						null);
			}
			NetworkImageView picture = (NetworkImageView) convertView
					.findViewById(R.id.picture);
			TextView housesId = (TextView) convertView
					.findViewById(R.id.housesId);
			TextView housesName = (TextView) convertView
					.findViewById(R.id.housesName);
			TextView discount_note = (TextView) convertView
					.findViewById(R.id.discount_note);
			TextView area_name = (TextView) convertView
					.findViewById(R.id.area_name);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			TextView day = (TextView) convertView.findViewById(R.id.day);
			TextView hour = (TextView) convertView.findViewById(R.id.hour);
			TextView recommend = (TextView) convertView
					.findViewById(R.id.recommend);
			TextView commission = (TextView) convertView
					.findViewById(R.id.commission);
			MyApplication.client.getImageForNetImageView(gb.getPicture(),
					picture, R.drawable.ic_launcher);
			housesId.setTag(gb.getHousesId());
			housesName.setText(gb.getHousesName());
			discount_note.setText(gb.getDiscount_note());
			price.setText(StringUtils.getPriceString(GroupbuyingActivity.this, gb.getPrice()));
			commission.setText(StringUtils.getPriceCommission(GroupbuyingActivity.this, gb.getScale_price()));
			area_name.setText("(" + gb.getAreaName() + ")");
			day.setText(gb.getDay());
			hour.setText(gb.getHour());
			recommend.setTag(gb);
			recommend.setOnClickListener(GroupbuyingActivity.this);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			GroupbuyingActivity.this.finish();
			break;
		/*
		 * 马上推荐
		 */
		case R.id.recommend:
			if (MyApplication.login) {
				Intent in = new Intent(GroupbuyingActivity.this,
						RecommendActivity.class);
				GroupBean gb = (GroupBean) v.getTag();
				RecommendBean rb = new RecommendBean();
				rb.setArea_name(gb.getAreaName());
				rb.setAreaId(gb.getAreaId());
				rb.setCity_name(gb.getCityName());
				rb.setCityId(gb.getCityId());
				rb.setHousesId(gb.getHousesId());
				rb.setHousesName(gb.getHousesName());
				in.putExtra("bean", rb);
				startActivity(in);
			} else {
				startActivity(new Intent(GroupbuyingActivity.this, LoginActivity.class));
			}
			break;
		default:
			break;
		}
	}
}
