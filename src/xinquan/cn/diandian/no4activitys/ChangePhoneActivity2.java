package xinquan.cn.diandian.no4activitys;

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
 * 更换手机号码 验证第二步Activity
 */
public class ChangePhoneActivity2 extends Activity implements OnClickListener {
	private EditText phone; // 手机号
	private EditText verification; // 确认
	private TextView getverification; // 得到验证码
	private Button commit; // 提交
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changephone2);
		initview();
		initlistener();

	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		getverification.setOnClickListener(this);
		commit.setOnClickListener(this);
	}

	/*
	 * 初始化View
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_change_phone_2);
		mTitleBar.setRightMenuVisible(false);
		phone = (EditText) findViewById(R.id.phone);
		verification = (EditText) findViewById(R.id.verification);
		getverification = (TextView) findViewById(R.id.getverification);
		commit = (Button) findViewById(R.id.commit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			ChangePhoneActivity2.this.finish();
			break;
		/*
		 * 获取验证码
		 */
		case R.id.getverification:
			if (phone.getText().toString().length() < 10) {
				Toast.makeText(ChangePhoneActivity2.this, "请输入正确的手机号码", 2000)
						.show();
			} else {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "login");
				ha.put("a", "send_telephone_msg");
				ha.put("telephone", phone.getText().toString());
				ha.put("user_key", MyApplication.seskey);
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									verification.setText(response
											.getString("verification"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {

							}
						});
			}

			break;
		/*
		 * 提交
		 */
		case R.id.commit:
			if (phone.getText().toString().length() > 10
					&& !verification.getText().toString().equals("")) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "user");
				ha.put("a", "update_phone");
				ha.put("user_key", MyApplication.seskey);
				ha.put("userId", MyApplication.sp.getString("userid", "-1"));
				ha.put("telephone", phone.getText().toString());
				ha.put("verification", verification.getText().toString());
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									switch (response.getInt("code")) {
									case 1:
										MyApplication.ed.putString("phone",
												phone.getText().toString());
										MyApplication.ed.commit();
										MyApplication.fragment4needflash = true;
										Intent in = new Intent(
												ChangePhoneActivity2.this,
												ChangePhoneActivity3.class);
										in.putExtra("phone", phone.getText()
												.toString());
										startActivity(in);
										ChangePhoneActivity2.this.finish();
										break;
									case 2:
										Toast.makeText(
												ChangePhoneActivity2.this,
												"修改失败", 2000).show();
										break;

									case 3:
										Toast.makeText(
												ChangePhoneActivity2.this,
												"您尚未登录", 2000).show();
										break;
									case 4:
										Toast.makeText(
												ChangePhoneActivity2.this,
												"验证码有误", 2000).show();
										break;

									default:
										break;
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {

							}
						});

			} else {

				Toast.makeText(ChangePhoneActivity2.this, "请输入正确的手机号码", 2000)
						.show();
			}

			break;

		default:
			break;
		}

	}

}
