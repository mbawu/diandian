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
 * 实名认证Activity
 */
public class AuthenticationActivity extends Activity implements OnClickListener {
	private EditText name; // 姓名
	private EditText card; // 身份证号
	private EditText bankname; // 银行名称
	private EditText bankusername; // 开户银行
	private EditText banknumber; // 银行卡号
	private Button commit; // 提交认证
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.authentication);
		initview();
		initListener();
	}

	/*
	 * 初始化监听器
	 */
	private void initListener() {
		commit.setOnClickListener(this);
		mTitleBar.setLeftOnClickListener(this);
	}

	/*
	 * 初始化View
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_real_Authentication);
		mTitleBar.setRightMenuVisible(false);
		name = (EditText) findViewById(R.id.name);
		card = (EditText) findViewById(R.id.card);
		bankname = (EditText) findViewById(R.id.bankname);
		bankusername = (EditText) findViewById(R.id.bankusername);
		banknumber = (EditText) findViewById(R.id.banknumber);
		commit = (Button) findViewById(R.id.commit);
	}

	/*
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 提交认证
		 */
		case R.id.commit:
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "update_authentication");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", "-1"));
			ha.put("name", name.getText().toString());
			ha.put("card", card.getText().toString());
			ha.put("bankname", bankname.getText().toString());
			ha.put("bankusername", bankusername.getText().toString());
			ha.put("banknumber", banknumber.getText().toString());
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								switch (response.getInt("code")) {
								case 1:
									Toast.makeText(AuthenticationActivity.this,
											"认证成功", 2000).show();
									MyApplication.ed.putString(
											"authentication", "1");
									MyApplication.ed.commit();
									MyApplication.fragment4needflash = true;
									startActivity(new Intent(
											AuthenticationActivity.this,
											AuthenticationOk.class));
									AuthenticationActivity.this.finish();
									break;
								case 2:
									Toast.makeText(AuthenticationActivity.this,
											"认证失败", 2000).show();
									break;
								case 3:
									Toast.makeText(AuthenticationActivity.this,
											"您的登录验证失败", 2000).show();
									break;
								case 4:
									Toast.makeText(AuthenticationActivity.this,
											"姓名格式有误", 2000).show();

									break;
								case 5:
									Toast.makeText(AuthenticationActivity.this,
											"身份证号码格式有误", 2000).show();

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
			AuthenticationActivity.this.finish();
			break;
		default:
			break;
		}

	}

}
