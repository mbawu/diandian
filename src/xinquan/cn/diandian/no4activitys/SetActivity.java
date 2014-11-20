package xinquan.cn.diandian.no4activitys;
import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设置Activity
 */
public class SetActivity extends Activity implements OnClickListener {
	private ImageView im;
	private View changephoto; // 更换头像
	private View changephone; // 更换联系方式
	private View auth; // 实名认证
	private View changepassword; // 更换密码
	private Button exit; // 退出登录
	private TextView name; // 用户姓名
	private TextView phone; // 用户联系电话
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setactivity);
		initview();
		initdata();
		initlistener();
	}

	/**
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		changephoto.setOnClickListener(this);
		changephone.setOnClickListener(this);
		auth.setOnClickListener(this);
		changepassword.setOnClickListener(this);
		exit.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initdata() {
//		MyApplication.client.getImageForNetImageView(
//				MyApplication.sp.getString("head_portrait", ""), im,
//				R.drawable.app_icon);
		MyApplication.client.downloadImage(im, MyApplication.sp.getString("head_portrait", ""));
		name.setText(MyApplication.sp.getString("username", ""));
		phone.setText(MyApplication.sp.getString("phone", ""));
	}

	/**
	 * 初始化View
	 * 
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.setting);
		mTitleBar.setRightMenuVisible(false);
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		im = (ImageView) findViewById(R.id.im);
		im.setScaleType(ScaleType.FIT_XY);
		changephoto = findViewById(R.id.changephoto);
		changephone = findViewById(R.id.changephone);
		auth = findViewById(R.id.auth);
		changepassword = findViewById(R.id.changepassword);
		exit = (Button) findViewById(R.id.exit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		/**
		 * 返回
		 * 
		 */
		case R.id.title_icon_left_layout:
			SetActivity.this.finish();
			break;

		/**
		 * 更换头像
		 * 
		 */
		case R.id.changephoto:
			startActivity(new Intent(SetActivity.this,
					ChangePhotoActivity.class));
			break;

		/**
		 * 更换手机号
		 * 
		 */
		case R.id.changephone:
			startActivity(new Intent(SetActivity.this,
					ChangePhoneActivity.class));
			break;

		/**
		 * 更换密码
		 * 
		 */
		case R.id.changepassword:
			startActivity(new Intent(SetActivity.this,
					ChangepasswordActivity.class));
			break;

		/**
		 * 实名认证
		 * 
		 */
		case R.id.auth:
			startActivity(new Intent(SetActivity.this,
					AuthenticationActivity.class));
			break;

		/**
		 * 退出登录
		 * 
		 */
		case R.id.exit:
			logout();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.setactivityneedflush) {
			MyApplication.setactivityneedflush = false;
			initdata();
		}
	}

	/**
	 * 退出登录确定框
	 * 
	 */
	public void logout() {
		new AlertDialog.Builder(SetActivity.this)
				.setMessage("确定退出登录?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_OK);//

						if (MyApplication.login) {
							MyApplication.login = false;
							MyApplication.ed.remove("userid");
							MyApplication.ed.remove("phone");
							MyApplication.ed.remove("password");
							MyApplication.ed.commit();
							Toast.makeText(SetActivity.this, "成功注销登录", 2000)
									.show();
							MyApplication.fragment4needflash = true;
							SetActivity.this.finish();
						} else {
							Toast.makeText(SetActivity.this, "您已处于未登录状态", 2000)
									.show();
						}

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);//
					}
				}).show();
	}

}
