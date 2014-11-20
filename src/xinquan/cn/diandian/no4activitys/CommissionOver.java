package xinquan.cn.diandian.no4activitys;

import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 申请提款成功Activity
 */
public class CommissionOver extends Activity implements OnClickListener {
	private TextView content; //
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.commissionover);
		initView();
		initlistener();

	}

	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_request_tikuan);
		mTitleBar.setRightMenuVisible(false);
		content = (TextView) findViewById(R.id.content);
	}

	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			CommissionOver.this.finish();
			break;

		default:
			break;
		}

	}

}
