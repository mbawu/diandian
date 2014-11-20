package xinquan.cn.diandian.main5fragment;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import xinquan.cn.diandian.CleanRecommendActivity;
import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.RecommendActivity;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.AvailabilityBean;
import xinquan.cn.diandian.bean.ClientBean;
import xinquan.cn.diandian.login.LoginActivity;
import xinquan.cn.diandian.no2activitys.CustomerDetails;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 客户Fragment,第二个模块客户模块一级界面
 */
public class Fragment2 extends Fragment implements OnClickListener {
	private View v; // Fragment2的View
	private EditText content; // 客户搜索的文本输入框
	private ImageView search; // 搜索
	private LinearLayout add; // 添加客户
	private ListView lv; // 客户列表ListView
	private Myad ad; // 客户列表适配器
	private ArrayList<ClientBean> al; // 客户数据源
	private int page = 1; // 请求页数初始值
	private int totalpage = 1; // 总页数初始值
	private int lastitem=0; // listView滑动到的最有一个item
	private Boolean jiazai = true; // 数据是否加载完毕
	private Button intercept; // 当数据加载的时候 此按钮显示来屏幕所有触摸事件
	private View footView; // footView
	private PopupWindow mPopupWindow; // 筛选PopupWindow
	private String satae = "0"; // 临时状态
	private String namee = ""; // 临时姓名
	private TextView title; // 标题
	private String[] listStr = {"所有客户","点点审核","开发商审核","已到访","已认购","已成交","已结佣"};
	private int currentIndex = 0;
	private ListView plv;
	private boolean inSearchPage = false;
	private int firstCreate=1;
	private int totalItem=0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*
	 * 初始化View，判断如果已经加载过View利用缓存
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment2, container, false);
			initview();
			initdata();
			initlistener();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	/*
	 * 初始化popwindow
	 */
	private void initpop(View parent) {
//		View popuView = MyApplication.lf.inflate(R.layout.popupwindow, null);
		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		

		View popuView =layoutInflater.inflate(R.layout.popupwindow, null);
		plv = (ListView) popuView.findViewById(R.id.pop_listview);
		plv.setOnItemClickListener(item_click);
		plv.setAdapter(new PopupAdapter());
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
		RelativeLayout layout=(RelativeLayout) v.findViewById(R.id.title_frg2_layout);
		layout.measure(w, h);  
		int height =layout.getMeasuredHeight();  
		mPopupWindow = new PopupWindow(popuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		mPopupWindow.showAsDropDown(parent);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAtLocation(parent, Gravity.TOP,0,height+50); 
		mPopupWindow.update();
//		   mPopupWindow.showAsDropDown(null, -30, 0);  
//		  //pw.showAsDropDown(titleName);  
//		    mPopupWindow.update(); 

	}
	
	private OnItemClickListener item_click = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mPopupWindow.dismiss();
			if (currentIndex == position) {
				return;
			}
			namee = "";
			changer(view);
			al.clear();
			page = 1;
			totalpage = 1;
			intercept.setVisibility(View.GONE);
			footView.setVisibility(View.VISIBLE);
			
			switch (position) {
			case 0:
				satae = "0";
				title.setText("所有客户");
				break;
			case 1:
				satae = "1";
				title.setText("点点审核");
				break;
			case 2:
				satae = "2";
				title.setText("开发商审核");
				break;
			case 3:
				satae = "3";
				title.setText("已到访");
				break;
			case 4:
				satae = "4";
				title.setText("已认购");
				break;
			case 5:
				satae = "5";
				title.setText("已成交");
				break;
			case 6:
				satae = "6";
				title.setText("已结佣");
				break;
			default:
				break;
			}
			currentIndex = position;
			
			loadlvData();
			mPopupWindow.dismiss();
		}
	};

	/*
	 * 初始化所有的监听
	 */
	private void initlistener() {
		title.setOnClickListener(this);
		search.setOnClickListener(this);
		add.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView p = (TextView) arg1.findViewById(R.id.phone);
				String phone = p.getText().toString();
				Intent in = new Intent(getActivity(), CustomerDetails.class);
				in.putExtra("phone", phone);
				Log.e("dsfgsfgsg", phone);
				startActivity(in);
			}
		});
		/*
		 * 设置滑动监听事件
		 */
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				Log.e("test", "frag2-->"+jiazai);
//				Log.e("test", "ifout--page-->"+page);
//				Log.e("lastitem", "lastitem"+lastitem);
//				if (lastitem == (page - 1) * 7 +1 && jiazai) {
//					Log.e("test", "page-->"+page);
//					jiazai = false;
//					intercept.setVisibility(View.GONE);
//					footView.setVisibility(View.VISIBLE);
//					loadlvData();
//				}
			}

			/*
			 * 滑动停止的时候记住最后显示的Item
			 */
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.i("test", "totalItemCount-->"+totalItemCount);
				Log.i("test", "firstVisibleItem-->"+firstVisibleItem);
				Log.i("test", "visibleItemCount-->"+visibleItemCount);
				lastitem = totalItemCount;
				if(visibleItemCount+firstVisibleItem==totalItemCount&&jiazai==true&&page<=totalpage&&page!=1)
				{
					Log.e("test", "page-->"+page);
					jiazai = false;
					intercept.setVisibility(View.GONE);
					footView.setVisibility(View.VISIBLE);
					loadlvData();
				}
			}
		});
	}

	/*
	 * 初始化Fragment2界面的初始化数据
	 */
	private void initdata() {
		intercept.setVisibility(View.GONE);
		footView.setVisibility(View.VISIBLE);
		loadlvData();
	}

	/*
	 * 初始化所有的View的引用
	 */
	private void initview() {
		title = (TextView) v.findViewById(R.id.title);
		al = new ArrayList<ClientBean>();
		footView = MyApplication.lf.inflate(R.layout.footview, null);
		content = (EditText) v.findViewById(R.id.content);
		search = (ImageView) v.findViewById(R.id.search);
		add = (LinearLayout) v.findViewById(R.id.add);
		ad = new Myad();
		lv = (ListView) v.findViewById(R.id.lv);
		lv.addFooterView(footView);
		lv.setAdapter(ad);
		intercept = (Button) v.findViewById(R.id.intercept);
//		initpop(title);
	}

	/*
	 * 客户列表ListView适配器
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
			ClientBean cb = al.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(R.layout.fragment2_item,
						null);
			}
			TextView day = (TextView) convertView.findViewById(R.id.day);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView phone = (TextView) convertView.findViewById(R.id.phone);
			TextView addTime_hour = (TextView) convertView
					.findViewById(R.id.addTime_hour);
			LinearLayout list = (LinearLayout) convertView
					.findViewById(R.id.list);
			list.removeAllViews();
			day.setText(cb.getDay());
			/*
			 * 客户列表标题头逻辑
			 */
			if (position == 0) {
				day.setVisibility(View.VISIBLE);
			} else {
				if (al.get(position).getDay()
						.equals(al.get(position - 1).getDay())) {
					day.setVisibility(View.GONE);
				} else {
					day.setVisibility(View.VISIBLE);
				}
			}
			name.setText(cb.getName());
			phone.setText(cb.getPhone());
			addTime_hour.setText(cb.getAddTime_hour());
			/*
			 * 动态添加每个客户下面的楼盘信息列表
			 */
			for (int i = 0; i < cb.getList().size(); i++) {
				AvailabilityBean ab = cb.getList().get(i);
				RelativeLayout rl = (RelativeLayout) MyApplication.lf.inflate(
						R.layout.clent_item, null);
				TextView housesName = (TextView) rl
						.findViewById(R.id.housesName);
				TextView alter_hour = (TextView) rl
						.findViewById(R.id.alter_hour);
				TextView state = (TextView) rl.findViewById(R.id.state);
				housesName.setText(ab.getHousesName());
				alter_hour.setText(ab.getAlter_hour());
				state.setText(ab.getLast_state());
				list.addView(rl);
			}
			return convertView;
		}
	}

	/*
	 * 点击事件触发
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 添加客户点击事件触发
		 */
		case R.id.add:
			if (MyApplication.login) {
				startActivity(new Intent(getActivity(),
						RecommendActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}

			break;
		/*
		 * 所有客户点击事件弹出popwindow
		 */
		case R.id.title:
			
			initpop(title);
			break;
		case R.id.search:
			if (inSearchPage) {
				content.setText("");
				namee="";
				page = 1;
				totalpage = 1;
				al.clear();
				intercept.setVisibility(View.GONE);
				footView.setVisibility(View.VISIBLE);
				add.setVisibility(View.VISIBLE);
				loadlvData();
				
				inSearchPage = false;
				search.setBackgroundResource(R.drawable.ic_search);
			} else {
				String s = content.getText().toString();
				if (s.equals("")) {
					Toast.makeText(getActivity(), "请输入内容再查询", 2000).show();
				} else {
					namee = s;
					page = 1;
					totalpage = 1;
					al.clear();
					intercept.setVisibility(View.GONE);
					add.setVisibility(View.GONE);
					footView.setVisibility(View.VISIBLE);
					loadlvData();
				}
				inSearchPage = true;
				search.setBackgroundResource(R.drawable.ic_clear_search);
			}
		
			break;

		default:
			break;
		}
	}

	/*
	 * 切換View的状态
	 */
	private void changer(View v) {
		TextView tv = (TextView) plv.getChildAt(currentIndex).findViewById(R.id.txtvwSpinner);
		tv.setTextColor(getResources().getColor(R.color.gray));
		
		TextView tt = (TextView) v.findViewById(R.id.txtvwSpinner);
		tt.setTextColor(getResources().getColor(R.color.green));
	}

	/*
	 * 判断界面是否需要刷新，一般如果有新的客户产生 就需要通知此界面刷新
	 */
	public void onResume() {
		super.onResume();
		Log.i("test", "clentneedflush-->"+MyApplication.clentneedflush);
		if (MyApplication.clentneedflush&&firstCreate!=1) {
			page = 1;
			totalpage = 1;
			al.clear();
			intercept.setVisibility(View.GONE);
			footView.setVisibility(View.VISIBLE);
			loadlvData();
			MyApplication.clentneedflush = false;
		}
		firstCreate++;
		Log.i("test", "firstCreate-->"+firstCreate);
	}

	/*
	 * 加載客戶ListView列表數據
	 */
	public void loadlvData() {
		Log.i("test", "loadlvData");
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "get_clientele");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", "-1"));
			ha.put("page", "" + page);
			ha.put("rowCount", "7");
			ha.put("satae", satae);
			ha.put("name", namee);
			Log.i("test", "page-->"+page);
			Log.i("test", "getlist-->"+MyApplication.getUrl(ha));
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
//									Log.i("test", "page-->"+response.toString());
									page = page + 1;
									totalpage = Integer.parseInt(response
											.getString("totlepage"));
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										ClientBean cb = new ClientBean();
										JSONObject jb = ja.getJSONObject(i);
										cb.setAddTime_hour(jb
												.getString("addTime_hour"));
										cb.setDay(jb.getString("day"));
										cb.setName(jb.getString("name"));
										cb.setPhone(jb.getString("phone"));
										JSONArray jaa = jb.getJSONArray("list");
										ArrayList<AvailabilityBean> all = new ArrayList<AvailabilityBean>();
										
										for (int m = 0; m < jaa.length(); m++) {
											JSONObject jbb = jaa
													.getJSONObject(m);
											AvailabilityBean ab = new AvailabilityBean();
											ab.setAlter_hour(jbb
													.getString("alter_hour"));
											ab.setBargain_state(jbb
													.getString("bargain_state"));
											ab.setCity_name(jbb
													.getString("city_name"));
											ab.setDevelopers_state(jbb
													.getString("developers_state"));
											ab.setHousesName(jbb
													.getString("housesName"));
											ab.setMake_state(jbb
													.getString("make_state"));
											ab.setMoney_state(jbb
													.getString("money_state"));
											ab.setReserve_state(jbb
													.getString("reserve_state"));
											ab.setVisit_state(jbb
													.getString("visit_state"));
											ab.setLast_state(jbb
													.getString("last_state"));
											all.add(ab);
										}
										cb.setList(all);
										al.add(cb);
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
	
	private class PopupAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return listStr.length;
		}

		@Override
		public String getItem(int position) {
			return listStr[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = getActivity().getLayoutInflater().inflate(R.layout.sp_tv_item, null);
			TextView tv = (TextView) v.findViewById(R.id.txtvwSpinner);
			tv.setText(getItem(position));
			setTextSelect(tv, position == currentIndex);
			return v;
		}
	}
	
	public void setTextSelect(TextView tv, boolean select){
		tv.setTextColor(getResources().getColor(select ? R.color.green : R.color.gray));
	}
}
