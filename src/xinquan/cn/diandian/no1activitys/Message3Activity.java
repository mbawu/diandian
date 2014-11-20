package xinquan.cn.diandian.no1activitys;

import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.widget.EulaWebView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;

/*
 * 消息详情Activity
 */
public class Message3Activity extends Activity implements OnClickListener {
	private EulaWebView wb;
	private String url;
	private TitleBarContainer mTitleBar;
	private boolean allowHorizontalScroll = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message3activity);
		url = getIntent().getExtras().getString("url");
		initview();
		initlistener();
		initdata();
	}

	/*
	 * 初始化数据
	 */
	private void initdata() {
		wb.loadUrl(url);
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
	}

	/*
	 * 初始化View
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.message_detail);
		mTitleBar.setRightMenuVisible(false);
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
			Message3Activity.this.finish();
			this.overridePendingTransition(R.anim.view_in_from_left,
					R.anim.view_out_to_right);
			break;

		default:
			break;
		}

	}

}
