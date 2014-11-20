package xinquan.cn.diandian.login;

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
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 注册Activity
 */
public class RegistActivity extends Activity implements OnClickListener {
	/*
	 * 所有注册页面的控件的声明和引用
	 */
	private CheckBox cb; // 同意用户协议CheckBox
	private TextView show; // 显示用户协议
	private Button agree; // 同意用户协议按钮
	private RelativeLayout showview; // 用户协议
	private RadioGroup rg;
	private RadioButton male; // 男性选项
	private RadioButton female; // 女性选项
	private TextView getcode; // 得到验证码
	private int sex = -1; // 女2 男1
	private Boolean check = false; // 初始化被选中状态为false
	private EditText username; // 用户昵称
	private EditText password; // 密码
	private EditText password2; // 重复密码
	private EditText email; // 邮箱
	private EditText phone; // 联系方式
	private EditText code; // 验证码
	private Button queding; // 确定按钮
	private long nowtime; // 当前时间
	private Boolean chushi = true; // 初始化默认值是true
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.regist);
		initView();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		getcode.setOnClickListener(this);
		show.setOnClickListener(this);
		cb.setOnClickListener(this);
		agree.setOnClickListener(this);
		queding.setOnClickListener(this);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.female:
					sex = 2;
					break;
				case R.id.male:
					sex = 1;
					break;

				default:
					break;
				}

			}
		});

	}

	/*
	 * 初始化View的引用
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_regist);
		mTitleBar.setRightMenuVisible(false);
		queding = (Button) findViewById(R.id.queding);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		password2 = (EditText) findViewById(R.id.password2);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		code = (EditText) findViewById(R.id.code);
		rg = (RadioGroup) findViewById(R.id.rg);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		getcode = (TextView) findViewById(R.id.getcode);
		cb = (CheckBox) findViewById(R.id.cb);
		cb.setChecked(false);
		show = (TextView) findViewById(R.id.show);
		agree = (Button) findViewById(R.id.agree);
		showview = (RelativeLayout) findViewById(R.id.showview);
		if (sex == 2) {
			female.setChecked(true);
		} else if(sex==1) {
			male.setChecked(true);
		}
	}

	/*
	 * 点击事件触发
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回事件触发
		 */
		case R.id.title_icon_left_layout:
			RegistActivity.this.finish();
			break;
		/*
		 * 向服务器发送注册请求
		 */
		case R.id.queding:
//			if (check) {
				/*
				 * 判断用户注册信息是否输入正确
				 */
				if (!username.getText().toString().equals("")
						&& !password.getText().toString().equals("")
						&& !password2.getText().toString().equals("")
						&& !code.getText().toString().equals("")
						&& !email.getText().toString().equals("")
						&& !phone.getText().toString().equals("")) {
					HashMap<String, String> haa = new HashMap<String, String>();
					haa.put("m", "user");
					haa.put("c", "login");
					haa.put("a", "add_user");
					haa.put("userName", username.getText().toString());
					haa.put("passwords", password.getText().toString());
					haa.put("passwords_repeat", password2.getText().toString());
					if(sex==-1)
						Toast.makeText(this, "请选择性别！", 2000).show();
					haa.put("sex", "" + sex);
					haa.put("verification", code.getText().toString());
					haa.put("email", email.getText().toString());
					haa.put("telephone", phone.getText().toString());
					haa.put("user_key", MyApplication.seskey);
					Log.e("asdfsfsfgs", MyApplication.seskey);
					/*
					 * 发送注册请求
					 */
					MyApplication.client.postWithURL(UrlPath.baseURL, haa,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {

									try {
										if (response.getInt("code") == 1) {
											/*
											 * 如果返回code1代表注册成功，
											 * 注册成功的时候将用户信息保存到本地SharedPreferences
											 */
											MyApplication.ed.putString("phone",
													phone.getText().toString());
											MyApplication.ed.putString(
													"password", password
															.getText()
															.toString());
											MyApplication.ed.putString(
													"userid",
													response.getString("userId"));
											MyApplication.ed.putString(
													"username", username
															.getText()
															.toString());
											MyApplication.ed.putString("sex",
													"" + sex);
											MyApplication.ed.putString("email",
													email.getText().toString());
											MyApplication.ed.putString(
													"telephone", phone
															.getText()
															.toString());
											MyApplication.ed.putString(
													"authentication", "0");
											MyApplication.ed.putString(
													"head_portrait", "");
											MyApplication.seskey = response
													.getString("user_key");
											MyApplication.login = true;
											MyApplication.ed.commit();
											Toast.makeText(RegistActivity.this,
													"注册成功", 2000).show();
											MyApplication.fragment4needflash = true;
											RegistActivity.this.finish();

										} else {
											/*
											 * 如果返回其他代表注册失败， 提示用户注册失败
											 */
											Toast.makeText(
													RegistActivity.this,
													"注册失败"
															+ response
																	.getString("msg"),
													2000).show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {

								}
							});
				} else {
					Toast.makeText(RegistActivity.this, "请填写完整信息", 2000).show();
				}
//			} else {
//				Toast.makeText(RegistActivity.this, "请仔细阅读用户协议", 2000).show();
//			}
			break;
		/*
		 * 获取手机验证码触发
		 */
		case R.id.getcode:
			/*
			 * 获取手机验证码的时候先判断是否是第一次请求验证码，如果是第一次请求，先把请求时间赋值给一个变量记住此时间
			 */
			if (!phone.getText().toString().equals("")) {
				if (chushi) {
					chushi = false;
					nowtime = System.currentTimeMillis();
					/*
					 * 发送验证请求的时候 ，只需要手机号码即可
					 */
					HashMap<String, String> ha = new HashMap<String, String>();
					ha.put("m", "user");
					ha.put("c", "login");
					ha.put("a", "send_telephone_msg");
					ha.put("telephone", phone.getText().toString());
					ha.put("user_key", MyApplication.seskey);
					Log.e("asdfsfsfgs", MyApplication.seskey);
					MyApplication.client.postWithURL(UrlPath.baseURL, ha,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									try {
										code.setText(response
												.getString("verification"));
										MyApplication.seskey = response
												.getString("user_key");

									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {

								}
							});
					/*
					 * 如果不是第一次请求验证码，判断再次请求的时间是否是15秒以后请求的，如果不是提示用户
					 * 请稍等，不可以在太短的时间内重复请求
					 */
				} else if (System.currentTimeMillis() - nowtime > 15000) {
					nowtime = System.currentTimeMillis();
					HashMap<String, String> ha = new HashMap<String, String>();
					ha.put("m", "user");
					ha.put("c", "login");
					ha.put("a", "send_telephone_msg");
					ha.put("telephone", phone.getText().toString());
					ha.put("user_key", MyApplication.seskey);
					Log.e("asdfsfsfgs", MyApplication.seskey);
					MyApplication.client.postWithURL(UrlPath.baseURL, ha,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									try {
										code.setText(response
												.getString("verification"));
										MyApplication.seskey = response
												.getString("user_key");
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
								}
							});
				} else {
					Toast.makeText(RegistActivity.this, "请15秒", 2000).show();
				}
			} else {
				Toast.makeText(RegistActivity.this, "请输入手机号", 2000).show();
			}
			break;
		/*
		 * 用户阅读协议显示触发
		 */
		case R.id.show:
			if (showview.getVisibility() == View.GONE) {
				showview.setVisibility(View.VISIBLE);
			} else {
				showview.setVisibility(View.GONE);
			}

			break;
		/*
		 * 用户同意协议
		 */
		case R.id.agree:
			showview.setVisibility(View.GONE);
			cb.setChecked(true);
			check = true;
			break;
		/*
		 * 用户是否同意勾选了协议框
		 */
		case R.id.cb:
			if (cb.isChecked()) {
				check = true;
			} else {
				check = false;
			}
			break;
		default:
			break;
		}

	}
}
