package xinquan.cn.diandian.no4activitys;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.TeamBean;
import xinquan.cn.diandian.share.ShareSDKHelper;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的团队Activity
 */
public class MyTeam extends Activity implements OnClickListener {
	private EditText content; // 要搜索的姓名
	private ImageView search; // 搜索
	private ListView lv;
	private Myad ad; // 团队列表适配器
	private ArrayList<TeamBean> al; // 团队列表数据源
	private int page = 1; // 向服务器请求的页数
	private int totalpage = 1; // 服务器返回的总页数
	private int lastitem;
	private Boolean jiazai = true;
	private Button intercept;
	private View footView;
	private String nam = "";
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myteam);
		initview();
		initlvdata(nam);
		initlistener();
	}

	/**
	 * 初始化监听器
	 */
	private void initlistener() {
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 5 + 1 && jiazai) {
					jiazai = false;
					intercept.setVisibility(View.GONE);
					footView.setVisibility(View.VISIBLE);
					initlvdata(nam);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});
		mTitleBar.setLeftOnClickListener(this).setRightOnClickListener(this);
		search.setOnClickListener(this);
		content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				nam = content.getText().toString();
			}
		});
	}

	/**
	 * 加载我的团队列表数据，分頁請求
	 */
	private void initlvdata(String nam) { // 刷新楼盘信息
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "get_group");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", "-1"));
			ha.put("page", "" + page);
			ha.put("rowCount", "5");
			ha.put("name", nam);
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
									Log.e("erhrfjfjfj", response.toString());
									page = page + 1;
									totalpage = Integer.parseInt(response
											.getString("totlepage"));
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										TeamBean tb = new TeamBean();
										tb.setLast_time(jb
												.getString("last_time"));
										tb.setName(jb.getString("name"));
										tb.setNumber(jb.getString("number"));
										tb.setTelephone(jb
												.getString("telephone"));
										tb.setUserId(jb.getString("userId"));
										al.add(tb);
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
	 * 初始化页面View的引用
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.Myteam);
		mTitleBar.setRightTextResource(R.string.invitation, false);
		content = (EditText) findViewById(R.id.content);
		search = (ImageView) findViewById(R.id.search);
		intercept = (Button) findViewById(R.id.intercept);
		footView = MyApplication.lf.inflate(R.layout.footview, null);
		al = new ArrayList<TeamBean>();
		ad = new Myad();
		lv = (ListView) findViewById(R.id.lv);
		lv.addFooterView(footView);
		lv.setAdapter(ad);
	}

	/**
	 * 我的团队适配器
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return al.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TeamBean tb = al.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf
						.inflate(R.layout.team_item, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView phone = (TextView) convertView
					.findViewById(R.id.telephone);
			TextView number = (TextView) convertView.findViewById(R.id.number);
			TextView last_time = (TextView) convertView
					.findViewById(R.id.last_time);
			name.setText(tb.getName());
			phone.setText(tb.getTelephone());
			number.setText(tb.getNumber());
			last_time.setText(tb.getLast_time());
			return convertView;
		}

	}
	
	PlatformActionListener paListener = new PlatformActionListener() {

		public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
			
			Log.d(getClass().getSimpleName(), res.toString());
			handler.sendEmptyMessage(1);
		}

		public void onError(Platform plat, int action, Throwable t) {
			t.printStackTrace();
			handler.sendEmptyMessage(2);
		}

		public void onCancel(Platform plat, int action) {
			// 在这里添加取消分享的处理代码
		}

	};
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				Toast.makeText(MyTeam.this, "分享成功", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2){
				Toast.makeText(MyTeam.this, "分享失败", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			MyTeam.this.finish();
			break;
		/**
		 * 邀请通讯录
		 */
		case R.id.title_icon_right_layout:
//			startActivityForResult(new Intent(Intent.ACTION_PICK,
//					ContactsContract.Contacts.CONTENT_URI), 0);
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 1);
//			ShareSDKHelper.shareShortMessage("点点卖房", getString(R.string.Short_Msg_Content), "http://www.diandianmaifang.com/mobile/regist.html", paListener);
			break;
		/**
		 * 搜索搞定的人
		 */
		case R.id.search:
			nam = content.getText().toString();
			al.clear();
			page = 1;
			totalpage = 1;
			initlvdata(nam);
			break;
		default:
			break;
		}

	}

	/**
	 * 从通讯录返回的数据会走到此回调方法,如果调用通讯录成功并且网络正常，会像服务器请求官方的邀请用户并且发送费客户，同时通知服务器我邀请谁了。
	 */
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
			Cursor phone = reContentResolverol.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phone.moveToNext()) {
				String usernumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				usernum = usernumber;
			}
			if (!usernum.equals("")) {
				final String s = usernum;
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "user");
				ha.put("a", "get_user_invite");
				ha.put("user_key", MyApplication.seskey);
				ha.put("userId", MyApplication.sp.getString("userid", "-1"));
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(final JSONObject respon) {
								try {
									if (respon.getInt("code") == 1) {
										HashMap<String, String> ha = new HashMap<String, String>();
										ha.put("m", "user");
										ha.put("c", "user");
										ha.put("a", "save_invite");
										ha.put("user_key", MyApplication.seskey);
										ha.put("userId", MyApplication.sp
												.getString("userid", "-1"));
										ha.put("phone", s);
										MyApplication.client.postWithURL(
												UrlPath.baseURL, ha,
												new Listener<JSONObject>() {

													@Override
													public void onResponse(
															JSONObject response) {
														try {
															if (response
																	.getInt("code") == 1) {
																Log.i("test", "sendMsg-->"+respon.getString("invite"));
																Uri ur = Uri
																		.parse("smsto:");
																Intent sendIntent = new Intent(
																		Intent.ACTION_VIEW,
																		ur);
																sendIntent
																		.putExtra(
																				"address",
																				s); // 电话号码，这行去掉的话，默认就没有电话
																sendIntent
																		.putExtra(
																				"sms_body",
																				respon.getString("invite"));
																sendIntent
																		.setType("vnd.android-dir/mms-sms");
																startActivity(sendIntent);
															} else {
																Toast.makeText(
																		MyTeam.this,
																		response.getString("msg"),
																		2000)
																		.show();
															}
														} catch (JSONException e) {
															Toast.makeText(
																	MyTeam.this,
																	"数据异常2",
																	2000)
																	.show();
															e.printStackTrace();
														}

													}
												}, new ErrorListener() {

													@Override
													public void onErrorResponse(
															VolleyError error) {
														Toast.makeText(
																MyTeam.this,
																"网络异常", 2000)
																.show();
													}
												});

									} else {
										Toast.makeText(MyTeam.this,
												"获取短信邀请语失败", 2000).show();
									}
								} catch (JSONException e) {
									Toast.makeText(MyTeam.this, "数据异常1", 2000)
											.show();
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(MyTeam.this, "网络异常", 2000)
										.show();
							}
						});

			}

		}
	}

}
