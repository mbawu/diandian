package xinquan.cn.diandian.no4activitys;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/*
 * 更换手机号Activity
 */
public class ChangePhoneActivity extends Activity implements OnClickListener {
	private EditText password; // 密码
	private Button next; // 下一步
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changephone);
		initview();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		next.setOnClickListener(this);

	}

	/*
	 * 初始化View
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_change_phone);
		mTitleBar.setRightMenuVisible(false);
		password = (EditText) findViewById(R.id.password);
		next = (Button) findViewById(R.id.next);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 下一步
		 */
		case R.id.next:
			if (password.getText().length() > 4) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "user");
				ha.put("a", "verification");
				ha.put("user_key", MyApplication.seskey);
				ha.put("userId", MyApplication.sp.getString("userid", "-1"));
				ha.put("passwords", password.getText().toString());
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getInt("code") == 1) {
										ChangePhoneActivity.this
												.startActivity(new Intent(
														ChangePhoneActivity.this,
														ChangePhoneActivity2.class));
										ChangePhoneActivity.this.finish();

									} else {
										Toast.makeText(
												ChangePhoneActivity.this,
												"抱歉您输入的密码有误", 2000).show();
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
				Toast.makeText(ChangePhoneActivity.this, "您输入的密码过短", 2000)
						.show();
			}
			break;
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			ChangePhoneActivity.this.finish();
			break;

		default:
			break;
		}

	}
}
