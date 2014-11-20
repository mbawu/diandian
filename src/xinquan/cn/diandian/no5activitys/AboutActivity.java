package xinquan.cn.diandian.no5activitys;

import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * 推荐流程Activity
 * 
 * 
 */
public class AboutActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_activity);
		(new TitleBarContainer(findViewById(R.id.title_layout), R.string.about_us)).setRightMenuVisible(false).setLeftOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});

	}

}
