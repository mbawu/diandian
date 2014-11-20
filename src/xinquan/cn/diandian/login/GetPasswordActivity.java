package xinquan.cn.diandian.login;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

public class GetPasswordActivity  extends Activity implements OnClickListener{
	/*
	 * 所有注册页面的控件的声明和引用
	 */

	private TextView getcode; // 得到验证码

	private EditText phone; // 联系方式
	private EditText code; // 验证码
	private Button queding; // 确定按钮
	private long nowtime; // 当前时间
	private Boolean chushi = true; // 初始化默认值是true
	
	private EditText password; // 密码
	private EditText password2; // 重复密码
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.getpassword);
		initView();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		getcode.setOnClickListener(this);
		
		queding.setOnClickListener(this);
		

	}

	/*
	 * 初始化View的引用
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_getPassword);
		mTitleBar.setRightMenuVisible(false);
		
		queding = (Button) findViewById(R.id.queding);
		phone = (EditText) findViewById(R.id.phone);
		code = (EditText) findViewById(R.id.code);
		password = (EditText) findViewById(R.id.password);
		password2 = (EditText) findViewById(R.id.password2);
		getcode = (TextView) findViewById(R.id.getcode);
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
			GetPasswordActivity.this.finish();
			break;
		/*
		 * 向服务器发送注册请求
		 */
		case R.id.queding:

				/*
				 * 判断用户注册信息是否输入正确
				 */
				if (!code.getText().toString().equals("")
						&& !phone.getText().toString().equals("")) {
					HashMap<String, String> haa = new HashMap<String, String>();
					haa.put("m", "user");
					haa.put("c", "login");
					haa.put("a", "reset_pswd");
					
					haa.put("verification", code.getText().toString());
					haa.put("pswd", password.getText().toString());
					haa.put("pswd_repeat", password2.getText().toString());
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
											Toast.makeText(GetPasswordActivity.this,
													"密码更改成功", 2000).show();
											GetPasswordActivity.this.finish();

										} else {
											/*
											 * 如果返回其他代表注册失败， 提示用户注册失败
											 */
											Toast.makeText(
													GetPasswordActivity.this,
													"找回密码失败" + response.getString("msg"),
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
					Toast.makeText(GetPasswordActivity.this, "请填写完整信息", 2000).show();
				}
			
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
					Toast.makeText(GetPasswordActivity.this, "请15秒", 2000).show();
				}
			} else {
				Toast.makeText(GetPasswordActivity.this, "请输入手机号", 2000).show();
			}
			break;
		
		default:
			break;
		}

	}
}
