package xinquan.cn.diandian.no4activitys;

import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*
 *更换手机号码成功提示用户Activity
 */
public class ChangePhoneActivity3 extends Activity implements OnClickListener {
	private TextView phone; // 手机号码
	private Button commit; // 提交
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changephone3);
		initview();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		phone.setOnClickListener(this);
		commit.setOnClickListener(this);
	}

	/*
	 * 初始化View
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_regist);
		mTitleBar.setRightMenuVisible(false);
		phone = (TextView) findViewById(R.id.phone);
		commit = (Button) findViewById(R.id.commit);
		String s = getIntent().getExtras().getString("phone");
		phone.setText(s);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			ChangePhoneActivity3.this.finish();
			break;
		/*
		 * 确认
		 */
		case R.id.commit:
			ChangePhoneActivity3.this.finish();
			break;

		default:
			break;
		}

	}

}
