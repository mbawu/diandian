package xinquan.cn.diandian.main5fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.RecommendBean;
import xinquan.cn.diandian.no3activitys.SelectCity;
import xinquan.cn.diandian.no3activitys.SelectHouses;
import xinquan.cn.diandian.no3activitys.ShowActivity;
import xinquan.cn.diandian.tools.StringUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 推荐Fragment,第三个模块的一级界面Fragment
 */
public class Fragment3 extends Fragment implements OnClickListener {
	private View v;
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
	private String sex = "-1"; // 初始化性别

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*
	 * Fragment每次创建的时候或者返回的时候的回调方法，此方法返回一个View 给所在的Activity
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment3, container, false);
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
	 * 初始化监听器
	 */
	private void initlistener() {
		getphone.setOnClickListener(this);
		add.setOnClickListener(this);
		commit.setOnClickListener(this);
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
	 * 初始化View的指针
	 */
	private void initview() {
		TitleBarContainer titlebar = new TitleBarContainer(v, R.string.title_suggest);
		titlebar.setLeftMenuVisible(false);
		titlebar.setRightMenuVisible(false);
		
		al = new ArrayList<RecommendBean>();
		br = new Mybr();
		ift = new IntentFilter();
		ift.addAction("city");
		getActivity().registerReceiver(br, ift);
		br2 = new Mybr2();
		ift2 = new IntentFilter();
		ift2.addAction("houses");
		getActivity().registerReceiver(br2, ift2);
		getphone = (ImageView) v.findViewById(R.id.getphone);
		name = (EditText) v.findViewById(R.id.name);
		phone = (EditText) v.findViewById(R.id.phone);
		ad = new Myad();
		lv = (ListView) v.findViewById(R.id.lv);
		add = (LinearLayout) v.findViewById(R.id.add);
		lv.setAdapter(ad);
		commit = (Button) v.findViewById(R.id.commit);
		rg = (RadioGroup) v.findViewById(R.id.rg);
		male = (RadioButton) v.findViewById(R.id.male);
		female = (RadioButton) v.findViewById(R.id.female);
	}
	private void initdata() {
		new Utility().setListViewHeightBasedOnChildren(lv);
		ad.notifyDataSetChanged();		
	}
	
	 @Override
	    public void setUserVisibleHint(boolean isVisibleToUser) {
	        super.setUserVisibleHint(isVisibleToUser);
	        if (isVisibleToUser) {
	            //相当于Fragment的onResume
	        	initdata();
	        } else {
	            //相当于Fragment的onPause
	        }
	    }
	/*
	 * 推薦数据的初始化
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
			delet.setOnClickListener(Fragment3.this);
			cityname.setOnClickListener(Fragment3.this);
			housesname.setOnClickListener(Fragment3.this);
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
		 * 調用通訊錄
		 */
		case R.id.getphone:
			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI), 0);
			MyApplication.usernumberneedflush = true;
			break;
		/*
		 * 添加推荐楼盘
		 */
		case R.id.add:
			if (al.size() == 3) {
				Toast.makeText(getActivity(), "已达推荐上限", 2000).show();
			} else {
				al.add(new RecommendBean());				
				ad.notifyDataSetChanged();
			}
			new Utility().setListViewHeightBasedOnChildren(lv);
			break;
		/*
		 * 确定推荐
		 */
		case R.id.commit:
			if (al.size() > 0) {
				if (sex.equals("-1")) {
					Toast.makeText(getActivity(), "请选择性别", 2000).show();
					return;
				}
				for (int i = 0; i < al.size(); i++) {
					if (!al.get(i).getAreaId().equals("")
							&& !al.get(i).getCityId().equals("")) {
					} else {
						Toast.makeText(getActivity(), "请检查您的意向楼盘信息是否输入完整", 2000)
								.show();
						return;
					}
				}
				if (!name.getText().toString().equals("")
						&& !phone.getText().equals("")) {
					HashMap<String, String> ha = new HashMap<String, String>();
					ha.put("m", "user");
					ha.put("c", "user");
					ha.put("a", "add_clientele");
					ha.put("user_key", MyApplication.seskey);
					ha.put("userId", MyApplication.sp.getString("userid", "-1"));
					ha.put("name", name.getText().toString());
					ha.put("phone", StringUtils.convertPhoneString(phone.getText().toString()));
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
											
											MyApplication.clentneedflush = true;
											al.clear();
											new Utility()
													.setListViewHeightBasedOnChildren(lv);
											ad.notifyDataSetChanged();
											startActivity(new Intent(
													getActivity(),
													ShowActivity.class));

											break;
										case 2:
											Toast.makeText(getActivity(),
													"推荐失败", 2000).show();
											break;
										case 3:
											Toast.makeText(getActivity(),
													"请重新登录", 2000).show();
											break;
										case 4:
											Toast.makeText(getActivity(),
													"推荐失败，推荐数超过允许添加数量", 2000)
													.show();
											break;
										case 5:
											Toast.makeText(getActivity(),
													"抱歉该客户已有经纪人", 2000).show();
											break;
										case 6:
											Toast.makeText(getActivity(),
													"客户电话不能为空!", 2000).show();
											break;
										default:
											Toast.makeText(
													getActivity(),
													response.getString("msg"), 2000).show();
											break;
										}
									} catch (JSONException e) {
										Toast.makeText(getActivity(), "解析数据出错",
												2000).show();
										e.printStackTrace();
									}

								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(getActivity(), "网络异常", 2000)
											.show();
								}
							});

				} else {
					Toast.makeText(getActivity(), "请检查您的信息是否输入完整", 2000).show();
				}
			} else {
				Toast.makeText(getActivity(), "请选择楼盘", 2000).show();
			}
			break;
		/*
		 * 删除列表中的某一个推荐楼盘
		 */
		case R.id.delet:
			al.remove(v.getTag());
			new Utility().setListViewHeightBasedOnChildren(lv);
			ad.notifyDataSetChanged();
			break;
		/*
		 * 选择城市区域
		 */
		case R.id.cityname:
			rb = (RecommendBean) v.getTag();
			Intent in = new Intent(getActivity(), SelectCity.class);
			getActivity().startActivity(in);
			break;
		/*
		 * 选择楼盘
		 */
		case R.id.housesname:
			rb = (RecommendBean) v.getTag();
			if (!rb.getCity_name().equals("")) {
				Intent in1 = new Intent(getActivity(), SelectHouses.class);
				in1.putExtra("cityid", rb.getCityId());
				in1.putExtra("areaid", rb.getAreaId());
				getActivity().startActivity(in1);
			} else {
				Toast.makeText(getActivity(), "请选择相应城市", 2000).show();
			}
			break;
		default:
			break;
		}
	}

	/*
	 * 楼盘数据组装发送给服务器
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
	 * 区域数据组装发送费服务器
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
	 * 测算ListView的高度
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

	@Override
	public void onResume() {
		super.onResume();
		if (MyApplication.usernumberneedflush) {
			phone.setText(MyApplication.usernumber);
			name.setText(MyApplication.userName);
			male.setChecked(false);
			female.setChecked(false);
			MyApplication.usernumberneedflush = false;
		}
	}

	/*
	 * 广播接收者 接收选择城市的返回数据
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
	 * 广播接收者，接收返回楼盘的数据
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
}
