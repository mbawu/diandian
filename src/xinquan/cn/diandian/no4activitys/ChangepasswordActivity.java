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
 * 修改密码Activity
 */
public class ChangepasswordActivity extends Activity implements OnClickListener {
	private EditText password; // 密码
	private EditText newpassword; // 新密码
	private EditText newpassword2; // 重复新密码
	private Button commit; // 提交
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changepass);
		initview();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		commit.setOnClickListener(this);
	}

	/*
	 * 初始化View引用
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_change_psw);
		mTitleBar.setRightMenuVisible(false);
		password = (EditText) findViewById(R.id.password);
		newpassword = (EditText) findViewById(R.id.newpassword);
		newpassword2 = (EditText) findViewById(R.id.newpassword2);
		commit = (Button) findViewById(R.id.commit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			ChangepasswordActivity.this.finish();
			break;
		/*
		 * 提交修改密码
		 */
		case R.id.commit:
			if (!password.getText().toString().equals("")
					&& !newpassword.getText().toString().equals("")
					&& !newpassword2.getText().toString().equals("")) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "user");
				ha.put("a", "update_paswd");
				ha.put("user_key", MyApplication.seskey);
				ha.put("userId", MyApplication.sp.getString("userid", "-1"));
				ha.put("old_passwords", password.getText().toString());
				ha.put("passwords", newpassword.getText().toString());
				ha.put("passwords_repeat", newpassword2.getText().toString());
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									switch (response.getInt("code")) {
									case 1:
										Toast.makeText(
												ChangepasswordActivity.this,
												"修改密码成功", 2000).show();
										MyApplication.ed.putString("password",
												newpassword.getText()
														.toString());
										MyApplication.ed.commit();
										ChangepasswordActivity.this.finish();
										break;
									case 2:
										Toast.makeText(
												ChangepasswordActivity.this,
												"抱歉修改密码失败", 2000).show();
										break;
									case 3:
										Toast.makeText(
												ChangepasswordActivity.this,
												"您尚未登录", 2000).show();
										break;
									case 4:
										Toast.makeText(
												ChangepasswordActivity.this,
												"您输入的密码不一致", 2000).show();
										break;
									case 5:
										Toast.makeText(
												ChangepasswordActivity.this,
												"您的新密码和原密码重复", 2000).show();
										break;
									case 6:
										Toast.makeText(
												ChangepasswordActivity.this,
												"原始密码输入有误", 2000).show();
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
			} else {
				Toast.makeText(ChangepasswordActivity.this, "请检查您的输入是否完整正确",
						2000).show();
			}
			break;

		default:
			break;
		}

	}

}
