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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 登录Activity
 */
public class LoginActivity extends Activity implements OnClickListener {
	private EditText phone; // 手机号
	private EditText password; // 密码
	private TextView getpassword; // 获取密码
	private Button login; // 登录
	private TitleBarContainer mTitleBar;
	/**
	 * 生命周期内初始化View  数据  监听器 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		initview();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this).setRightOnClickListener(this);
		getpassword.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	/*
	 * 初始化View的引用
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.user_login);
		mTitleBar.setRightTextResource(R.string.regist, false);
		phone = (EditText) findViewById(R.id.phone);
		password = (EditText) findViewById(R.id.password);
		getpassword = (TextView) findViewById(R.id.getpassword);
		login = (Button) findViewById(R.id.login);
		
//		phone.setText("13641195771");
//		password.setText("123456");
	}

	/*
	 * 点击事件触发
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回触发
		 */
		case R.id.title_icon_left_layout:
			LoginActivity.this.finish();
			break;
		/*
		 * 注册触发
		 */
		case R.id.title_icon_right_layout:
			startActivity(new Intent(LoginActivity.this, RegistActivity.class));
			LoginActivity.this.finish();
			break;
		/*
		 * 获取密码
		 */
		case R.id.getpassword:
			startActivity(new Intent(LoginActivity.this, GetPasswordActivity.class));

			break;
		/*
		 * 登录触发
		 */
		case R.id.login:
			if (!phone.getText().toString().equals("")
					&& !password.getText().toString().equals("")) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "login");
				ha.put("a", "do_login");
				ha.put("phone", phone.getText().toString());
				ha.put("passwords", password.getText().toString());
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getInt("code") == 1) {
										/*
										 * 登录成功的时候将用户的一些必要信息保存到本地SharedPreferences
										 */
										MyApplication.seskey = response
												.getString("user_key");
										MyApplication.ed.putString("userid",
												response.getString("userId"));
										MyApplication.ed.putString("username",
												response.getString("userName"));
										MyApplication.ed.putString("password",
												password.getText().toString());
										MyApplication.ed.putString("name",
												response.getString("name"));
										MyApplication.ed.putString("address",
												response.getString("address"));
										MyApplication.ed.putString("phone", phone.getText().toString());
												//response.getString("phone"));
										MyApplication.ed
												.putString(
														"telephone",
														response.getString("telephone"));
										MyApplication.ed.putString("card",
												response.getString("card"));
										MyApplication.ed.putString("type",
												response.getString("type"));
										MyApplication.ed.putString("bankname",
												response.getString("bankName"));
										MyApplication.ed.putString(
												"banknumber",
												response.getString("bankNumber"));
										MyApplication.ed.putString(
												"bankusername",
												response.getString("bankUserName"));
										MyApplication.ed.putString("money",
												response.getString("money"));
										MyApplication.ed.putString(
												"authentication",
												response.getString("authentication"));
										MyApplication.ed.putString("addtime",
												response.getString("addTime"));
										MyApplication.ed.putString(
												"parent_userid",
												response.getString("Parent_userId"));
										MyApplication.ed.putString("iP",
												response.getString("IP"));
										MyApplication.ed.putString(
												"ip_address",
												response.getString("IP_address"));
										MyApplication.ed.putString(
												"head_portrait",
												response.getString("head_portrait"));
										MyApplication.ed.commit();
										/*
										 * 将登录状态设置为true
										 */
										MyApplication.login = true;
										/*
										 * 第四个界面的Fragment需要刷新
										 */
										MyApplication.fragment4needflash = true;
										Toast.makeText(LoginActivity.this,
												"登录成功", 2000).show();
										LoginActivity.this.finish();
									} else {
										Toast.makeText(LoginActivity.this,
												"登录失败", 2000).show();
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
				Toast.makeText(LoginActivity.this, "请填写完整资料", 2000).show();
			}
			break;
		default:
			break;
		}

	}

	/*
	 * 如果从注册界面注册成功，返回此页面的时候，需要销毁此界面 ，因为注册成功相当于登录成功。
	 */
	protected void onRestart() {
		super.onRestart();
		if (MyApplication.login) {
			LoginActivity.this.finish();
		}
	}

}
