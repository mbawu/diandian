package xinquan.cn.diandian.no5activitys;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 关于我们Activity
 * 
 */
public class AbouatActivity extends Activity implements OnClickListener {
	
	private static final String tag = "AbouatActivity";
	private WebView wb;
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutactivity);
		initView();
		initdata();
		initlistener();
	}

	/**
	 * 初始化监听器
	 * 
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
	}

	/**
	 * 初始化数据
	 * 
	 */
	private void initdata() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "otherData");
		ha.put("c", "other");
		ha.put("a", "connection");
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								String aboutPage = response.getString("url");
								Log.i(tag, "about page-" + aboutPage);
								wb.loadUrl(aboutPage);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(AbouatActivity.this, "网络异常", 2000)
								.show();
					}
				});

	}

	/**
	 * 初始化View
	 * 
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.about_us);
		mTitleBar.setRightMenuVisible(false);
		wb = (WebView) findViewById(R.id.wb);
		wb.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		wb.getSettings().setJavaScriptEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		/**
		 * 返回
		 * 
		 */
		case R.id.title_icon_left_layout:
			AbouatActivity.this.finish();
			break;

		default:
			break;
		}

	}

}
