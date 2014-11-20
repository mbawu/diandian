package xinquan.cn.diandian.no4activitys;

import java.util.HashMap;

import org.json.JSONArray;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/*
 * 申请提款Activity
 */
public class GetCommissionActivity extends Activity implements OnClickListener {
	private Button next; // 下一步
	private EditText password; // 密码
	private String id;
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.getcommission);
		initView();
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
	 * 初始化View引用
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_request_tikuan);
		mTitleBar.setRightMenuVisible(false);
		id = getIntent().getExtras().getString("id");
		next = (Button) findViewById(R.id.next);
		password = (EditText) findViewById(R.id.password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 下一步，发送密码给服务器
		 */
		case R.id.next:
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("passwords", password.getText().toString());
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "verification");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", "-1"));
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								switch (response.getInt("code")) {
								case 1:
									HashMap<String, String> ha = new HashMap<String, String>();
									ha.put("m", "user");
									ha.put("c", "user");
									ha.put("a", "update_usermoney");
									ha.put("user_key", MyApplication.seskey);
									ha.put("userId", MyApplication.sp
											.getString("userid", "-1"));
									ha.put("drawmoneyid", id);
									MyApplication.client.postWithURL(
											UrlPath.baseURL, ha,
											new Listener<JSONObject>() {
												@Override
												public void onResponse(
														JSONObject response) {
													try {
														switch (response
																.getInt("code")) {
														case 1:
															startActivity(new Intent(
																	GetCommissionActivity.this,
																	CommissionOver.class));
															GetCommissionActivity.this
																	.finish();
															break;
														case 2:
															Toast.makeText(
																	GetCommissionActivity.this,
																	"申请失败",
																	2000)
																	.show();

															break;
														case 3:
															Toast.makeText(
																	GetCommissionActivity.this,
																	"您尚未登录",
																	2000)
																	.show();
															break;

														default:
															break;
														}
													} catch (JSONException e) {
														e.printStackTrace();
													}

												}
											}, new ErrorListener() {
												@Override
												public void onErrorResponse(
														VolleyError error) {

												}
											});
									break;
								case 2:
									Toast.makeText(GetCommissionActivity.this,
											"您的密码验证有误", 2000).show();
									break;
								case 3:
									Toast.makeText(GetCommissionActivity.this,
											"您尚未登录", 2000).show();
									break;

								default:
									break;
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

			break;
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			GetCommissionActivity.this.finish();
			break;
		default:
			break;
		}

	}
}
