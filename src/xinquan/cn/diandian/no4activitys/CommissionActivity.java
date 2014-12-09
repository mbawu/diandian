package xinquan.cn.diandian.no4activitys;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.Commission;
import xinquan.cn.diandian.tools.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/*
 * 我的佣金Activity
 */
public class CommissionActivity extends Activity implements OnClickListener {
	private TextView tv1; // 已提取佣金
	private TextView tv2; // 未提取佣金
	private ListView lv; // 佣金列表ListView
	private Myad ad; // 佣金列表适配器
	private ArrayList<Commission> al; // 佣金列表数据源
	private String userId; // 用户ID
	private int page; // 请求的页数
	private int totalpage; // 总页数
	private int lastitem; //
	private Boolean jiazai = true; // 是否加载完毕数据
	private Button intercept;
	private View footView;
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.commission);
		initView();
		initdata();
		initListener();
	}

	private void initdata() {
		/*
		 * 请求我的佣金数据
		 */
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "user");
		ha.put("c", "user");
		ha.put("a", "get_usermoney");
		ha.put("user_key", MyApplication.seskey);
		ha.put("userId", MyApplication.sp.getString("userid", "-1"));
		ha.put("page", "" + page);
		ha.put("rowcount", "7");
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								page = page + 1;
								totalpage = response.getInt("totlepage");
								tv1.setText(StringUtils.getPriceCommission(CommissionActivity.this, response.getString("end_money")));
								tv2.setText(StringUtils.getPriceCommission(CommissionActivity.this, response.getString("money")));
								JSONArray ja = response.getJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									Commission cm = new Commission();
									cm.setAddTime(jb.getString("addTime"));
									cm.setCityName(jb.getString("cityName"));
									cm.setDrawmoneyId(jb
											.getString("drawmoneyId"));
									cm.setDrawstate(jb.getString("drawstate"));
									cm.setHousesName(jb.getString("housesName"));
									cm.setMoney(jb.getString("money"));
									cm.setMoney_type(jb.getString("money_type"));
									cm.setName(jb.getString("name"));
									cm.setUserId(jb.getString("userId"));
									al.add(cm);
								}
								ad.notifyDataSetChanged();
								footView.setVisibility(View.GONE);
								intercept.setVisibility(View.GONE);
							} else {
								tv1.setText(StringUtils.getPriceCommission(CommissionActivity.this, response.getString("end_money")));
								tv2.setText(StringUtils.getPriceCommission(CommissionActivity.this, response.getString("money")));
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
						Log.e("tttttttt", "cuowu");
						footView.setVisibility(View.GONE);
						intercept.setVisibility(View.GONE);
						jiazai = true;
					}
				});
	}

	/*
	 * 初始化监听器
	 */
	private void initListener() {
		mTitleBar.setLeftOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
		/*
		 * ListView 滚动监听器
		 */
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 5 + 1 && jiazai) {
					jiazai = false;
					intercept.setVisibility(View.GONE);
					footView.setVisibility(View.VISIBLE);
					initdata();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});

	}

	/*
	 * 初始化我的佣金所有View的引用
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.Mycommission);
		mTitleBar.setRightMenuVisible(false);
		al = new ArrayList<Commission>();
		intercept = (Button) findViewById(R.id.intercept);
		footView = MyApplication.lf.inflate(R.layout.footview, null);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		lv = (ListView) findViewById(R.id.lv);
		ad = new Myad();
		lv.addFooterView(footView);
		lv.setAdapter(ad);
	}

	/*
	 * 佣金列表适配器
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
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Commission cm = al.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(
						R.layout.commission_item, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView money_type = (TextView) convertView
					.findViewById(R.id.money_type);
			TextView money = (TextView) convertView.findViewById(R.id.money);
			TextView housesName = (TextView) convertView
					.findViewById(R.id.housesName);
			TextView cityName = (TextView) convertView
					.findViewById(R.id.cityName);
			TextView addTime = (TextView) convertView
					.findViewById(R.id.addTime);
			TextView drawstate = (TextView) convertView
					.findViewById(R.id.drawstate);
			name.setText(cm.getName());
			if (cm.getMoney_type().equals("1")) {
				money_type.setText("(个人佣金)");
			} else {
				money_type.setText("(团队佣金)");
			}
			money.setText(cm.getMoney());
			housesName.setText(cm.getHousesName());
			cityName.setText("(" + cm.getCityName() + ")");
			addTime.setText(cm.getAddTime());
			/*
			 * 判断佣金状态来显示不同的状态
			 */
			switch (Integer.parseInt(cm.getDrawstate())) {
			case 0:
				drawstate.setText("提取佣金");
				drawstate.setTag(cm.getDrawmoneyId());
				drawstate.setBackgroundResource(R.drawable.tiqu_ming);
				break;
			case 2:
				drawstate.setText("打款中");
				drawstate.setTag(cm.getDrawmoneyId());
				drawstate.setBackgroundResource(R.drawable.dakuan_ming);
				break;
			case 3:
				drawstate.setText("打款中");
				drawstate.setTag(cm.getDrawmoneyId());
				drawstate.setBackgroundResource(R.drawable.dakuan_ming);
				break;
			case 4:
				drawstate.setText("已结佣");
				drawstate.setTag(cm.getDrawmoneyId());
				drawstate.setBackgroundResource(R.drawable.yitiqu);
				break;

			default:
				break;
			}
			drawstate.setOnClickListener(CommissionActivity.this);
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
			CommissionActivity.this.finish();
			break;
		/*
		 * 提取佣金按钮，如果佣金已经提取将无效
		 */
		case R.id.drawstate:
			TextView tv = (TextView) v;
			if (tv.getText().equals("提取佣金")) {
				Intent in = new Intent(CommissionActivity.this,
						GetCommissionActivity.class);
				in.putExtra("id", (String) v.getTag());
				startActivity(in);
			} else {
				Toast.makeText(CommissionActivity.this, "状态不符，请选择可提取的佣金进行申请！",
						2000).show();
			}

			break;
		default:
			break;
		}
	}
}
