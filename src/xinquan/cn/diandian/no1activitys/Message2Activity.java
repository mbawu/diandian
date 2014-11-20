package xinquan.cn.diandian.no1activitys;

import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.widget.EulaWebView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;

/*
 * 消息信息Activity
 */
public class Message2Activity extends Activity implements OnClickListener {
	private EulaWebView wb; // webView
	private String url; // webView加载路径
	private String content; // 从上个Activity返回的数据url链接
	private TitleBarContainer mTitleBar;
	private boolean allowHorizontalScroll = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message2activity);
		url = getIntent().getExtras().getString("url");
		content = getIntent().getExtras().getString("content");
		initview();
		initlistener();
		initdata();
	}

	/*
	 * 初始化数据
	 */
	private void initdata() {
		wb.loadUrl(content);
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this).setRightOnClickListener(this);
	}

	/*
	 * 初始化View
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.Mymessage);
		mTitleBar.setRightTextResource(R.string.detail, false);
		wb = (EulaWebView) findViewById(R.id.web);
		wb.getSettings().setSupportZoom(false);
		wb.getSettings().setLoadWithOverviewMode(true);
		wb.setHorizontalScrollBarEnabled(false);
		wb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		
		if (allowHorizontalScroll) {
			wb.setHorizontalScrollBarEnabled(true);
			wb.getSettings().setUseWideViewPort(true);//TODO
			wb.setInitialScale(150);
			wb.setAllowHorizontalScroll(true);
			wb.getSettings().setDefaultZoom(ZoomDensity.FAR);
			wb.getSettings().setSupportZoom(true);
			wb.getSettings().setBuiltInZoomControls(true);
			wb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
		}
		
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
		case R.id.title_icon_left_layout:
			Message2Activity.this.finish();
			/*this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);*/
			break;
		case R.id.title_icon_right_layout:
			Intent in = new Intent(Message2Activity.this,
					Message3Activity.class);
			in.putExtra("url", url);
			startActivity(in);
			break;

		default:
			break;
		}

	}

}
