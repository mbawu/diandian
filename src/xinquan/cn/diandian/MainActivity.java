package xinquan.cn.diandian;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import xinquan.cn.diandian.login.LoginActivity;
import xinquan.cn.diandian.main5fragment.Fragment1;
import xinquan.cn.diandian.main5fragment.Fragment2;
import xinquan.cn.diandian.main5fragment.Fragment3;
import xinquan.cn.diandian.main5fragment.Fragment4;
import xinquan.cn.diandian.main5fragment.Fragment5;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 主界面Activity，重点：包含了5个一级的Fragment（Fragment1,Fragment2,Fragment3,Fragment4,Fragment5）切换的时候，切换Fragment即可
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	
	private static final String tag = "MainActivity";
	private LinearLayout ly1; // 楼盘View
	private LinearLayout ly2; // 客户View
	private LinearLayout ly3; // 推荐View
	private LinearLayout ly4; // 我的View
	private LinearLayout ly5; // 更多View
	private Fragment1 fm1; // 楼盘Fragment
	private Fragment2 fm2; // 客户Fragment
	private Fragment3 fm3; // 推荐Fragment
	private Fragment4 fm4; // 我的Fragment
	private Fragment5 fm5; // 更多Fragment
	private FragmentManager fm; // Fragment管理器
	private LinearLayout li;
	private LinearLayout ly;
	
	public int tabId = 1;
	
	private int gotoTabId = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * 设置无标题显示Activity
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainactivity);
		/*
		 * 初始化工作，包括初始化View引用，初始化数据，初始化监听器（以后不再多做解释通用）
		 */
		initView();
		initData();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		ly1.setOnClickListener(this);
		ly2.setOnClickListener(this);
		ly3.setOnClickListener(this);
		ly4.setOnClickListener(this);
		ly5.setOnClickListener(this);
	}

	/*
	 * 初始化界面数据
	 */
	private void initData() {
		changer(1);
	}

	/*
	 * 初始化View引用
	 */
	private void initView() {
		li = (LinearLayout) findViewById(R.id.li);
		ly = (LinearLayout) findViewById(R.id.ly);
		fm = getSupportFragmentManager();
		ly1 = (LinearLayout) findViewById(R.id.ly1);
		ly2 = (LinearLayout) findViewById(R.id.ly2);
		ly3 = (LinearLayout) findViewById(R.id.ly3);
		ly4 = (LinearLayout) findViewById(R.id.ly4);
		ly5 = (LinearLayout) findViewById(R.id.ly5);
	}

	/*
	 * 点击事件触发
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly1:
			changer(1);
			break;
		case R.id.ly2:
			if (MyApplication.login) {
				changer(2);
			} else {
				gotoTabId = 2;
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
			}

			break;
		case R.id.ly3:
			if (MyApplication.login) {
				changer(3);
			} else {
				gotoTabId = 3;
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
			}
			break;
		case R.id.ly4:
			if (MyApplication.login) {
				changer(4);
			} else {
				gotoTabId = 4;
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
			}
			break;
		case R.id.ly5:
			changer(5);
			break;

		default:
			break;
		}

	}

	/*
	 * 点击下导航不同模块按钮的时候，切换不同的Fragment
	 */
	private void changer(int i) {
		tabId = i;
		switch (i) {
		/*
		 * 启动第一个Fragment
		 */
		case 1:
			if (fm1 == null) {
				fm1 = new Fragment1();
			}
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.li, fm1);
			ft.commit();
			changeclor(0);
			break;
		/*
		 * 启动第二个Fragment
		 */
		case 2:
			if (fm2 == null) {
				fm2 = new Fragment2();
			}
			FragmentTransaction ft2 = fm.beginTransaction();
			ft2.replace(R.id.li, fm2);
			ft2.commit();
			changeclor(1);
			break;
		/*
		 * 启动第三个Fragment
		 */
		case 3:
			if (fm3 == null) {
				fm3 = new Fragment3();
			}
			FragmentTransaction ft3 = fm.beginTransaction();
			ft3.replace(R.id.li, fm3);
			ft3.commit();
			changeclor(2);
			break;
		/*
		 * 启动第四个Fragment
		 */
		case 4:
			if (fm4 == null) {
				fm4 = new Fragment4();
			}
			FragmentTransaction ft4 = fm.beginTransaction();
			ft4.replace(R.id.li, fm4);
			ft4.commit();
			changeclor(3);
			break;
		/*
		 * 启动第五个Fragment
		 */
		case 5:
			if (fm5 == null) {
				fm5 = new Fragment5();
			}
			FragmentTransaction ft5 = fm.beginTransaction();
			ft5.replace(R.id.li, fm5);
			ft5.commit();
			changeclor(4);
			break;

		default:
			break;
		}
	}

	/*
	 * 点击的时候下导航图标颜色的转变
	 */
	private void changeclor(int m) {
		for (int i = 0; i < 5; i++) {
			LinearLayout li = (LinearLayout) ly.getChildAt(i);
			li.setBackgroundResource(R.drawable.buttom_bar);
			ImageView im = (ImageView) li.findViewById(R.id.im);
			TextView tv = (TextView) li.findViewById(R.id.tv);
			switch (i) {
			case 0:
				im.setBackgroundResource(R.drawable.loupan);
				break;
			case 1:
				im.setBackgroundResource(R.drawable.kehu);
				break;
			case 2:
				im.setBackgroundResource(R.drawable.tuijian);
				break;
			case 3:
				im.setBackgroundResource(R.drawable.wode);
				break;
			case 4:
				im.setBackgroundResource(R.drawable.gengduo);
				break;

			default:
				break;
			}

			tv.setTextColor(Color.parseColor("#8c8c8c"));
		}
		LinearLayout li = (LinearLayout) ly.getChildAt(m);
		li.setBackgroundResource(R.drawable.buttom_an);
		ImageView im = (ImageView) li.findViewById(R.id.im);
		TextView tv = (TextView) li.findViewById(R.id.tv);
		switch (m) {
		case 0:
			im.setBackgroundResource(R.drawable.loupan_ming);
			break;
		case 1:
			im.setBackgroundResource(R.drawable.kehu_ming);
			break;
		case 2:
			im.setBackgroundResource(R.drawable.tuijian_ming);
			break;
		case 3:
			im.setBackgroundResource(R.drawable.wode_ming);
			break;
		case 4:
			im.setBackgroundResource(R.drawable.gengduo_ming);
			break;
		default:
			break;
		}
		tv.setTextColor(Color.parseColor("#ffffff"));

	}
	public static final int chooseAddress = 10000;
	/*
	 * 拨打电话跳转通讯录获取联系人信息的回调方法 ，由于Fragment里面无法复写此方法，只能在此复写
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==chooseAddress){
			if(tabId==1 && fm1!= null){
				fm1.refreshList(false);
			}
		}
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
			Log.e("asdfsfgsgsg", "" + requestCode + "--" + username);
			if (MyApplication.usernumberneedflush) {
				MyApplication.usernumber = usernum;
				MyApplication.userName = username;
				return;
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
																		MainActivity.this,
																		response.getString("msg"),
																		2000)
																		.show();
															}
														} catch (JSONException e) {
															Toast.makeText(
																	MainActivity.this,
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
																MainActivity.this,
																"网络异常", 2000)
																.show();
													}
												});

									} else {
										Toast.makeText(MainActivity.this,
												"获取短信邀请语失败", 2000).show();
									}
								} catch (JSONException e) {
									Toast.makeText(MainActivity.this, "数据异常1",
											2000).show();
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(MainActivity.this, "网络异常", 2000)
										.show();
							}
						});

			}

		}
	}

	/*
	 * 键盘返回键监听事件
	 */
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) {
			logout();
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	/*
	 * 最后根Activity按返回键退出的时候弹出对话框是否确定退出应用程序
	 */
	public void logout() {
		new AlertDialog.Builder(MainActivity.this)
				.setMessage("确定退出?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_OK);//
						MyApplication.getInstance().exit();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);//
					}
				}).show();
	}
	
	public void onResume(){
		super.onResume();
		Log.i(tag, "MainActivity.onResume current tab="+tabId);
		if(tabId==4 && !MyApplication.login){
			changer(1);
		}else if(MyApplication.login && this.gotoTabId != 0){
			changer(gotoTabId);
			gotoTabId = 0;
		}
	}

}
