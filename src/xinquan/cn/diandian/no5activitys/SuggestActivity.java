package xinquan.cn.diandian.no5activitys;

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

/**
 * 意见反馈Activity
 * 
 */
public class SuggestActivity extends Activity implements OnClickListener {
	private EditText content; // 反馈信息
	private Button commit; // 提交反馈
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.suggest);
		initView();
		initlistener();
	}

	/**
	 * 初始化监听器
	 * 
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		commit.setOnClickListener(this);
	}

	/**
	 * 初始化View
	 * 
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_feedback);
		mTitleBar.setRightMenuVisible(false);
		content = (EditText) findViewById(R.id.content);
		commit = (Button) findViewById(R.id.commit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		/**
		 * 返回
		 * 
		 */
		case R.id.title_icon_left_layout:
			SuggestActivity.this.finish();
			break;
		/**
		 * 确定想服务器提交反馈信息
		 * 
		 */
		case R.id.commit:
			if (MyApplication.login) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "user");
				ha.put("a", "add_feedback");
				ha.put("user_key", MyApplication.seskey);
				ha.put("userId", MyApplication.sp.getString("userid", ""));
				ha.put("content", content.getText().toString());
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getInt("code") == 1) {
										Toast.makeText(SuggestActivity.this,
												"提交成功", 2000).show();
										SuggestActivity.this.finish();
									} else {
										Toast.makeText(SuggestActivity.this,
												"提交失败", 2000).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(SuggestActivity.this, "网络异常",
										2000).show();
							}
						});
			} else {
				Toast.makeText(SuggestActivity.this, "请登录后再提交", 2000).show();
			}
			break;

		default:
			break;
		}

	}

}
