package xinquan.cn.diandian;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import xinquan.cn.diandian.bean.RecommendBean;
import xinquan.cn.diandian.no3activitys.SelectCity;
import xinquan.cn.diandian.no3activitys.SelectHouses;
import xinquan.cn.diandian.no3activitys.ShowActivity;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CleanRecommendActivity extends Activity implements OnClickListener {
	private EditText name; // 姓名
	private EditText phone; // 联系电话
	private ListView lv; // 推荐列表listView
	private LinearLayout add; // 新增意向楼盘
	private Button commit; // 提交
	private ImageView getphone; // 获取联系人
	private Myad ad; // 推荐列表适配器
	private ArrayList<RecommendBean> al; // 推荐列表数据源
	private RecommendBean rb; // 推荐的JavaBean
	private IntentFilter ift; // 广播接受者事件过滤器
	private Mybr br; // 广播接受者
	private IntentFilter ift2; // 广播接受者过滤器2
	private Mybr2 br2; // 广播接受者2
	private RadioGroup rg; // 性别选择RadioGroup
	private RadioButton male; // 男性
	private RadioButton female; // 女性
	private String sex = "1"; // 初始化性别
	private TitleBarContainer mTitleBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recom);
		initview();
		initdata();
		initlistener();
	}

	private void initdata() {
		al = new ArrayList<RecommendBean>();//MyApplication.al;
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		getphone.setOnClickListener(this);
		add.setOnClickListener(this);
		commit.setOnClickListener(this);
		mTitleBar.setLeftOnClickListener(this);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.male:
					sex = "1";
					break;
				case R.id.female:
					sex = "2";
					break;
				default:
					break;
				}

			}
		});
	}

	/*
	 * 初始化View
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_suggest);
		mTitleBar.setRightMenuVisible(false);
		br = new Mybr();
		ift = new IntentFilter();
		ift.addAction("city");
		registerReceiver(br, ift);
		br2 = new Mybr2();
		ift2 = new IntentFilter();
		ift2.addAction("houses");
		registerReceiver(br2, ift2);
		getphone = (ImageView) findViewById(R.id.getphone);
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phone);
		ad = new Myad();
		lv = (ListView) findViewById(R.id.lv);
		add = (LinearLayout) findViewById(R.id.add);
		lv.setAdapter(ad);
		commit = (Button) findViewById(R.id.commit);
		rg = (RadioGroup) findViewById(R.id.rg);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
	}

	/*
	 * 推荐房源列表适配器
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
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RecommendBean rb = al.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(R.layout.recommend_item,
						null);
			}
			EditText cityname = (EditText) convertView
					.findViewById(R.id.cityname);
			EditText housesname = (EditText) convertView
					.findViewById(R.id.housesname);
			TextView delet = (TextView) convertView.findViewById(R.id.delet);
			cityname.setText(rb.getCity_name() + rb.getArea_name());
			housesname.setText(rb.getHousesName());
			delet.setTag(rb);
			housesname.setTag(rb);
			cityname.setTag(rb);
			delet.setOnClickListener(CleanRecommendActivity.this);
			cityname.setOnClickListener(CleanRecommendActivity.this);
			housesname.setOnClickListener(CleanRecommendActivity.this);
			if (al.size() == 1) {
				delet.setVisibility(View.GONE);
			} else {
				delet.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 跳转到通讯录
		 */
		case R.id.getphone:
			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI), 0);
			break;
		/*
		 * 新增推荐房源
		 */
		case R.id.add:
			if (al.size() == 3) {
				Toast.makeText(CleanRecommendActivity.this, "已达推荐上限", 2000)
						.show();
			} else {
				al.add(new RecommendBean());
				Log.e("sdgsgsgsgsdg", "" + al.size());
				new Utility().setListViewHeightBasedOnChildren(lv);
				ad.notifyDataSetChanged();
			}
			break;
		/*
		 * 确定提交
		 */
		case R.id.commit:
			if (al.size() > 0) {
				if (sex.equals("-1")) {
					Toast.makeText(CleanRecommendActivity.this, "请选择性别", 2000)
							.show();
					return;
				}
				for (int i = 0; i < al.size(); i++) {
					if (!al.get(i).getAreaId().equals("")
							&& !al.get(i).getCityId().equals("")) {
					} else {
						Toast.makeText(CleanRecommendActivity.this,
								"请检查您的意向楼盘信息是否输入完整", 2000).show();
						return;
					}
				}
				/*
				 * 如果推荐人姓名和电话都不为空的情况下推荐否则提示用户输入用户姓名或者联系电话
				 */
				if (!name.getText().toString().equals("")
						&& !phone.getText().equals("")) {
					HashMap<String, String> ha = new HashMap<String, String>();
					ha.put("m", "user");
					ha.put("c", "user");
					ha.put("a", "add_clientele");
					ha.put("user_key", MyApplication.seskey);
					ha.put("userId", MyApplication.sp.getString("userid", "-1"));
					ha.put("name", name.getText().toString());
					ha.put("phone", phone.getText().toString());
					ha.put("sex", sex);
					ha.put("purpose_cityid", initareaarray());
					ha.put("purpose_housesid", inithousesarray());
					MyApplication.client.postWithURL(UrlPath.baseURL, ha,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									try {
										switch (response.getInt("code")) {
										case 1:
											Toast.makeText(
													CleanRecommendActivity.this,
													"推荐成功", 2000).show();
											MyApplication.clentneedflush = true;
											al.clear();
											new Utility()
													.setListViewHeightBasedOnChildren(lv);
											ad.notifyDataSetChanged();
											startActivity(new Intent(
													CleanRecommendActivity.this,
													ShowActivity.class));
											CleanRecommendActivity.this
													.finish();
											break;
										case 2:
											Toast.makeText(
													CleanRecommendActivity.this,
													"推荐失败", 2000).show();
											break;
										case 3:
											Toast.makeText(
													CleanRecommendActivity.this,
													"请重新登录", 2000).show();
											break;
										case 4:
											Toast.makeText(
													CleanRecommendActivity.this,
													"推荐失败，推荐数超过允许添加数量", 2000)
													.show();
											break;
										case 5:
											Toast.makeText(
													CleanRecommendActivity.this,
													"抱歉该客户已有经纪人", 2000).show();
											break;
										default:
											break;
										}
									} catch (JSONException e) {
										Toast.makeText(
												CleanRecommendActivity.this,
												"解析数据出错", 2000).show();
										e.printStackTrace();
									}

								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(CleanRecommendActivity.this,
											"网络异常", 2000).show();
								}
							});

				} else {
					Toast.makeText(CleanRecommendActivity.this,
							"请检查您的信息是否输入完整", 2000).show();
				}
			} else {
				Toast.makeText(CleanRecommendActivity.this, "请选择楼盘", 2000)
						.show();
			}
			break;
		/*
		 * 删除房源
		 */
		case R.id.delet:
			al.remove(v.getTag());
			new Utility().setListViewHeightBasedOnChildren(lv);
			ad.notifyDataSetChanged();
			break;
		/*
		 * 筛选城市
		 */
		case R.id.cityname:
			rb = (RecommendBean) v.getTag();
			Intent in = new Intent(CleanRecommendActivity.this,
					SelectCity.class);
			CleanRecommendActivity.this.startActivity(in);
			break;
		/*
		 * 筛选楼盘
		 */
		case R.id.housesname:
			rb = (RecommendBean) v.getTag();
			if (!rb.getCity_name().equals("")) {
				Intent in1 = new Intent(CleanRecommendActivity.this,
						SelectHouses.class);
				in1.putExtra("cityid", rb.getCityId());
				in1.putExtra("areaid", rb.getAreaId());
				CleanRecommendActivity.this.startActivity(in1);
			} else {
				Toast.makeText(CleanRecommendActivity.this, "请选择相应城市", 2000)
						.show();
			}
			break;
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			CleanRecommendActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/*
	 * 返回数据源里面的楼盘数据，用来提交给服务器
	 */
	private String inithousesarray() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < al.size(); i++) {
			sb.append(al.get(i).getHousesId());
			sb.append("_");
		}
		String s = sb.substring(0, sb.length() - 1);
		return s;
	}

	/*
	 * 返回数据源里面的城市数据，用来提交给服务器
	 */
	private String initareaarray() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < al.size(); i++) {
			sb.append(al.get(i).getAreaId());
			sb.append("_");
		}
		String s = sb.substring(0, sb.length() - 1);
		return s;
	}

	/*
	 * 根据数据源的多少测算呢listView的总高度
	 */
	public class Utility {
		public void setListViewHeightBasedOnChildren(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}

	/*
	 * 判斷是否需要刷新，如果從通訊錄選擇了联系人，就需要刷新联系人电话号码数据
	 */
	public void onResume() {
		super.onResume();
		if (MyApplication.usernumberneedflush) {
			phone.setText(MyApplication.usernumber);
			MyApplication.usernumberneedflush = false;
		}
	}

	/*
	 * 广播接受者 用来接受选中的城市数据
	 */
	private class Mybr extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			rb.setCity_name(intent.getExtras().getString("cityname"));
			rb.setArea_name(intent.getExtras().getString("areaname"));
			rb.setAreaId(intent.getExtras().getString("areaid"));
			rb.setCityId(intent.getExtras().getString("cityid"));
			rb.setHousesId("");
			rb.setHousesName("");
			new Utility().setListViewHeightBasedOnChildren(lv);
			ad.notifyDataSetChanged();
		}
	}

	/*
	 * 广播接受者，接收选择的楼盘信息数据
	 */
	private class Mybr2 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			rb.setHousesId(intent.getExtras().getString("housesid"));
			rb.setHousesName(intent.getExtras().getString("housesname"));
			new Utility().setListViewHeightBasedOnChildren(lv);
			ad.notifyDataSetChanged();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			ContentResolver reContentResolverol = getContentResolver();
			Uri contactData = data.getData();
			String usernum = "";
			@SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			String username = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phon = reContentResolverol.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phon.moveToNext()) {
				String usernumber = phon
						.getString(phon
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				phone.setText(usernumber);
				name.setText(username);
			}

		}
	}
}
