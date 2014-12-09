package xinquan.cn.diandian.no4activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.MyCollectionBean;
import xinquan.cn.diandian.no1activitys.HouseDetails;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 我的收藏列表Activity
 */
public class MyFavorite extends Activity implements OnClickListener {
	private ListView lv;
	private Myad ad; // 我的收藏适配器
	private ArrayList<MyCollectionBean> al; // 我的收藏数据源
	private int page = 1; // 当前页数
	private int totalpage = 1; // 总页数
	private int lastitem; // 最后的item
	private Boolean jiazai = true; // 是否加载完毕
	private Button intercept; // 拦截
	private View footView;
	private SlidingDrawer slid; // slidingDrawer
	private Button cancel; // 取消
	private Button confirm; // 确认
	private TextView tv;
	private String nowid; // 当前选中的ID
	private int nowpostion; // 当前选中的positi
	private AtomicBoolean isEdit ;
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mycollection);
		isEdit = new AtomicBoolean(false);
		initview();
		initlvdata();
		initlistener();
	}

	/**
	 * 初始化监听器
	 */

	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this).setRightOnClickListener(this);
		lv.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 5 + 1 && jiazai) {
					jiazai = false;
					intercept.setVisibility(View.GONE);
					footView.setVisibility(View.VISIBLE);
					initlvdata();
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String tag = (String) arg1.findViewById(R.id.picture).getTag();
				Intent in = new Intent(MyFavorite.this, HouseDetails.class);
				in.putExtra("id", tag);
				startActivity(in);
			}
		});
		cancel.setOnClickListener(MyFavorite.this);
		confirm.setOnClickListener(MyFavorite.this);
	}

	/**
	 * 加载listView数据
	 */
	private void initlvdata() {
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "get_collect");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", ""));
			ha.put("page", "" + page);
			ha.put("rowcount", "5");
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							Log.e("shoucang", response.toString());
							try {
								if (response.getInt("code") == 1) {
									page = page + 1;
									totalpage = Integer.parseInt(response
											.getString("totlepage"));
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										MyCollectionBean mb = new MyCollectionBean();
										mb.setAddTime(jb.getString("addTime"));
										mb.setArea_name(jb
												.getString("area_name"));
										mb.setCollectId(jb
												.getString("collectId"));
										mb.setDiscount_note(jb
												.getString("discount_note"));
										mb.setScale_price(StringUtils.convertIntString(jb
												.getString("scale_price")));
										mb.setDiscount_time(jb
												.getJSONObject("discount_time"));
										mb.setHousesId(jb.getString("housesId"));
										mb.setHousesName(jb
												.getString("housesName"));
										mb.setPicture(jb.getString("picture"));
										mb.setPrice(StringUtils.convertIntString(jb.getString("price")));
										mb.setUserId(jb.getString("userId"));
										al.add(mb);
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

	/**
	 * 初始化View的引用
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.Mycollection);
		mTitleBar.setRightTextResource(R.string.delete, false);
		
		slid = (SlidingDrawer) findViewById(R.id.slid);
		al = new ArrayList<MyCollectionBean>();
		intercept = (Button) findViewById(R.id.intercept);
		footView = MyApplication.lf.inflate(R.layout.footview, null);
		lv = (ListView) findViewById(R.id.lv);
		ad = new Myad();
		lv.addFooterView(footView);
		lv.setAdapter(ad);
		tv = (TextView) slid.findViewById(R.id.tv);
		confirm = (Button) slid.findViewById(R.id.confirm);
		cancel = (Button) slid.findViewById(R.id.cancel);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			MyFavorite.this.finish();
			break;
		/**
		 * 删除触发所有数据删除图标显示出来，让用户选择性删除
		 */
		case R.id.delete:
			MyCollectionBean mb = (MyCollectionBean) v.getTag();
			intercept.setVisibility(View.GONE);
			slid.animateToggle();
			tv.setText(mb.getHousesName());
			nowid = mb.getCollectId();
			nowpostion = mb.getPosition();
			break;
		/**
		 * 确认删除
		 */
		case R.id.confirm:
			intercept.setVisibility(View.GONE);
			slid.animateToggle();
			delet(nowid, nowpostion);
			break;
		/**
		 * 取消删除
		 */
		case R.id.cancel:
			intercept.setVisibility(View.GONE);
			slid.animateToggle();
			break;
		/**
		 * 删除
		 */
		case R.id.title_icon_right_layout:
			if(isEdit.get()) {
				cancelDelete();
				mTitleBar.setRightTextResource(R.string.delete, false);
				isEdit.set(false);
			} else {
				setDelete();
				mTitleBar.setRightTextResource(R.string.cancel, false);
				isEdit.set(true);
			}

			break;

		default:
			break;
		}

	}

	/**
	 * 向服务器发送删除某条收藏请求，删除成功的时候更新列表数据
	 */
	private void delet(String s, final int m) {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "user");
		ha.put("c", "user");
		ha.put("a", "del_collect");
		ha.put("user_key", MyApplication.seskey);
		ha.put("userId", MyApplication.sp.getString("userid", "-1"));
		ha.put("collectId", s);
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							switch (response.getInt("code")) {
							case 1:
								Toast.makeText(MyFavorite.this, "删除成功", 2000)
										.show();
								al.remove(m);
								ad.notifyDataSetChanged();
								break;
							case 2:
								Toast.makeText(MyFavorite.this, "删除失败", 2000)
										.show();
								break;
							case 3:
								Toast.makeText(MyFavorite.this,
										"登录认证有误，请重新登录", 2000).show();
								break;

							default:
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 收藏列表适配器
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			return al.size();
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
			MyCollectionBean mb = al.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(R.layout.collect_item,
						null);
			}
			mb.setPosition(position);
			NetworkImageView picture = (NetworkImageView) convertView
					.findViewById(R.id.picture);
			picture.setTag(mb.getHousesId());
			ImageView delete = (ImageView) convertView
					.findViewById(R.id.delete);
			TextView area_name = (TextView) convertView
					.findViewById(R.id.area_name);
			TextView housesName = (TextView) convertView
					.findViewById(R.id.housesName);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			TextView discount_note = (TextView) convertView
					.findViewById(R.id.discount_note);
			TextView day = (TextView) convertView.findViewById(R.id.day);
			TextView hour = (TextView) convertView.findViewById(R.id.hour);
			TextView addTime = (TextView) convertView
					.findViewById(R.id.addTime);
			TextView scale_price = (TextView) convertView
					.findViewById(R.id.scale_price);
			MyApplication.client.getImageForNetImageView(mb.getPicture(),
					picture, R.drawable.ic_launcher);
			area_name.setText("[" + mb.getArea_name() + "]");
			housesName.setText(mb.getHousesName());
			price.setText(mb.getPrice()+"元/㎡");
			discount_note.setText(mb.getDiscount_note());
			JSONObject jj = mb.getDiscount_time();
			try {
				day.setText(jj.getString("day"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				hour.setText(jj.getString("hour"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addTime.setText(mb.getAddTime());
			scale_price.setText( mb.getScale_price());
			delete.setTag(mb);
			delete.setOnClickListener(MyFavorite.this);
			if (mb.getYincang()) {
				delete.setVisibility(View.GONE);
			} else {
				delete.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

	}

	/**
	 * 修改数据源属性 用来改变列表ITEM删除按钮状态
	 */
	private void setDelete() {
		for (int i = 0; i < al.size(); i++) {
			al.get(i).setYincang(false);
		}
		ad.notifyDataSetChanged();
	}

	/**
	 * 修改数据源属性 用来改变列表ITEM删除按钮状态
	 */
	private void cancelDelete() {
		for (int i = 0; i < al.size(); i++) {
			al.get(i).setYincang(true);
		}
		ad.notifyDataSetChanged();
	}

}
