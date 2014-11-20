package xinquan.cn.diandian.no3activitys;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * 推荐看房成功
 */
public class ShowActivity extends Activity implements OnClickListener {
	private Button commit; // 确认
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showactivity);
		initView();
		initlistener();
	}

	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		commit.setOnClickListener(this);
	}

	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_suggest);
		mTitleBar.setRightMenuVisible(false);
		commit = (Button) findViewById(R.id.commit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_icon_left_layout:
			
			ShowActivity.this.finish();
			break;
		case R.id.commit:
			MyApplication.usernumber="";
			MyApplication.userName="";
			MyApplication.clearSex=true;
			MyApplication.usernumberneedflush=true;
			ShowActivity.this.finish();
			break;
		default:
			break;
		}

	}

}
