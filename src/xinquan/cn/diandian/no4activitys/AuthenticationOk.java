package xinquan.cn.diandian.no4activitys;

import xinquan.cn.diandian.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/*
 * 认证成功提示界面
 */
public class AuthenticationOk extends Activity implements OnClickListener {
	private Button commit;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.auok);
		initview();
		initlistener();

	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		commit.setOnClickListener(this);

	}

	/*
	 * 初始化View
	 */
	private void initview() {
		commit = (Button) findViewById(R.id.commit);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 确认
		 */
		case R.id.commit:
			AuthenticationOk.this.finish();
			break;

		default:
			break;
		}

	}

}
