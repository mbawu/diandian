package xinquan.cn.diandian.no3activitys;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.address.SortModel;
import xinquan.cn.diandian.bean.Fragment1houseBean;
import xinquan.cn.diandian.bean.HousesBean;
import xinquan.cn.diandian.tools.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

/*
 * 新增意向楼盘Activity
 */
public class SelectHouses extends Activity implements OnClickListener {
	private String areaid; // 地区id
	private String cityid; // 城市id
	private ListView lv; // 选择楼盘ListView
	private Myad ad; // 适配器
	private ArrayList<HousesBean> al; // 楼盘
	private int page = 1;
	private int totalpage = 1;
	private int lastitem;
	private Boolean jiazai = true; // 是否加载完毕
	private Button intercept;
	private View footView;
	private TitleBarContainer mTitleBarContainer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selecthouses);
		initview();
		initdata();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView housesId = (TextView) arg1.findViewById(R.id.housesid);
				TextView housesname = (TextView) arg1
						.findViewById(R.id.housesname);
				Intent in = new Intent();
				in.putExtra("housesname", housesname.getText().toString());
				in.putExtra("housesid", housesId.getText().toString());
				in.setAction("houses");
				sendBroadcast(in);
				MyApplication.xuanhao = true;
				SelectHouses.this.finish();
			}

		});
		/*
		 * ListView 滑动监听器 ，判断滑动到哪个item
		 */
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 8 + 1 && jiazai) {
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
		mTitleBarContainer.setLeftOnClickListener(SelectHouses.this);
	}

	/*
	 * 初始化数据
	 */
	private void initdata() {
		initlvdata();
	}

	/*
	 * 初始化View
	 */
	private void initview() {
		al = new ArrayList<HousesBean>();
		mTitleBarContainer = new TitleBarContainer(findViewById(R.id.title_layout), "新增意向楼盘");
		mTitleBarContainer.setRightMenuVisible(false);
		
		intercept = (Button) findViewById(R.id.intercept);
		areaid = getIntent().getExtras().getString("areaid");
		cityid = getIntent().getExtras().getString("cityid");
		lv = (ListView) findViewById(R.id.lv);
		ad = new Myad();
		footView = MyApplication.lf.inflate(R.layout.footview, null);
		lv.addFooterView(footView);
		lv.setAdapter(ad);
	}

	/*
	 * 初始化LitsView数据
	 */
	private void initlvdata() {
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "houses");
			ha.put("c", "houses");
			ha.put("a", "houses");
			ha.put("rowCount", "8");
			ha.put("cityId", cityid);
			ha.put("page", "" + page);
			ha.put("area_cityId", areaid);
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
									page = page + 1;
									totalpage = Integer.parseInt(response
											.getString("totlepage"));
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										HousesBean hb = new HousesBean();
										hb.setHousesId(jb.getString("housesId"));
										hb.setHousesName(jb
												.getString("housesName"));
										hb.setDiscount_note(jb
												.getString("discount_note"));
										hb.setPicture(jb.getString("picture"));
										hb.setPrice(jb.getString("price"));
										hb.setArea_name(jb
												.getString("area_name"));
										al.add(hb);
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
	 * ListView 适配器
	 */
	private class Myad extends BaseAdapter {
		@Override
		public int getCount() {
			return al.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HousesBean hb = al.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(
						R.layout.recommendhouses_item, null);
			}
			NetworkImageView nv = (NetworkImageView) convertView
					.findViewById(R.id.picture);
			TextView housesId = (TextView) convertView
					.findViewById(R.id.housesid);
			TextView housesName = (TextView) convertView
					.findViewById(R.id.housesname);
			TextView discount_note = (TextView) convertView
					.findViewById(R.id.discount_note);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			TextView area_name = (TextView) convertView
					.findViewById(R.id.area_name);
			MyApplication.client.getImageForNetImageView(hb.getPicture(), nv,
					R.drawable.ic_launcher);
			housesId.setText(hb.getHousesId());
			housesName.setText(hb.getHousesName());
			discount_note.setText(hb.getDiscount_note());
			price.setText("均价 ：" + StringUtils.getPriceString(SelectHouses.this, hb.getPrice()));
			area_name.setText(hb.getArea_name());
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_icon_left_layout:
			SelectHouses.this.finish();
			break;

		default:
			break;
		}

	}

}
