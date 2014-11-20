package xinquan.cn.diandian.main5fragment;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.login.LoginActivity;
import xinquan.cn.diandian.no1activitys.MessageActivity;
import xinquan.cn.diandian.no4activitys.AuthenticationActivity;
import xinquan.cn.diandian.no4activitys.CommissionActivity;
import xinquan.cn.diandian.no4activitys.MyFavorite;
import xinquan.cn.diandian.no4activitys.MyTeam;
import xinquan.cn.diandian.no4activitys.SetActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/*
 * 我的Fragment,第四个模块的一级界面Fragment
 */
public class Fragment4 extends Fragment implements OnClickListener {
	private View v; // 作为返回的Fragment的View
	private View commission; // 我的佣金
	private View collect; // 我的收藏
	private View team; // 我的团队
	private View information; // 我的消息
	private ImageView options; // 设置
	private Button authentication; // 实名认证按钮
	private TextView username; // 用户昵称
	private ImageView userstate; // 用户状态
	private TextView phone; // 联系电话
	private ImageView photo; // 形象照片

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment4, container, false);
			initview();
			initdata();
			initlistener();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	/*
	 * 初始化所有的监听器
	 */
	private void initlistener() {
		authentication.setOnClickListener(this);
		options.setOnClickListener(this);
		commission.setOnClickListener(this);
		collect.setOnClickListener(this);
		team.setOnClickListener(this);
		information.setOnClickListener(this);
	}

	private void initdata() {
//		MyApplication.client.getImageForNetImageView(
//				MyApplication.sp.getString("head_portrait", ""), photo,
//				R.drawable.ic_launcher);
		MyApplication.client.downloadImage(photo, MyApplication.sp.getString("head_portrait", ""));
		/*
		 * if (Myapplication.sp.getString("authentication", "").equals("1")) {
		 * authentication.setVisibility(View.GONE); } else {
		 * authentication.setVisibility(View.VISIBLE); }
		 */
		/*
		 * 如果登陸了 就顯示用戶昵称电话号码等等数据，，如果没有登录就显示登录按钮 ，提示用户未登录
		 */
		if (MyApplication.login) {
			username.setText(MyApplication.sp.getString("username", ""));
			phone.setText(MyApplication.sp.getString("phone", ""));
			userstate.setVisibility(View.VISIBLE);
			authentication.setText("实名认证");
			if (MyApplication.sp.getString("authentication", "").equals("")) {
			} else if (MyApplication.sp.getString("authentication", "").equals("0")) {
				userstate.setBackgroundResource(R.drawable.weirenzheng);
			} else {
				userstate.setBackgroundResource(R.drawable.yirenzheng);
				authentication.setVisibility(View.INVISIBLE);
			}
		} else {
			username.setText("未登录");
			phone.setText("请点击右侧登录");
			authentication.setText("登   录");
			userstate.setVisibility(View.GONE);
		}

	}

	/*
	 * 初始化界面View的引用
	 */
	private void initview() {
		photo = (ImageView) v.findViewById(R.id.photo);
		photo.setScaleType(ScaleType.FIT_XY);
		username = (TextView) v.findViewById(R.id.username);
		userstate = (ImageView) v.findViewById(R.id.userstate);
		phone = (TextView) v.findViewById(R.id.phone);
		authentication = (Button) v.findViewById(R.id.authentication);
		options = (ImageView) v.findViewById(R.id.options);
		commission = v.findViewById(R.id.commission);
		collect = v.findViewById(R.id.collect);
		team = v.findViewById(R.id.team);
		information = v.findViewById(R.id.information);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 认证点击事件触发
		 */
		case R.id.authentication:
			Button bt = (Button) v;
			if (!bt.getText().equals("实名认证")) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			} else {
				startActivity(new Intent(getActivity(),
						AuthenticationActivity.class));
			}
			break;
		/*
		 * 我的佣金
		 */
		case R.id.commission:
			startActivity(new Intent(getActivity(), CommissionActivity.class));
			break;
		/*
		 * 我的收藏
		 */
		case R.id.collect:
			startActivity(new Intent(getActivity(), MyFavorite.class));
			break;
		/*
		 * 我的团队
		 */
		case R.id.team:
			startActivity(new Intent(getActivity(), MyTeam.class));
			break;
		/*
		 * 我的消息
		 */
		case R.id.information:
			startActivity(new Intent(getActivity(), MessageActivity.class));
			break;
		/*
		 * 设置图标点击触发
		 */
		case R.id.options:
			startActivity(new Intent(getActivity(), SetActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (MyApplication.fragment4needflash) {
			initdata();
			MyApplication.fragment4needflash = false;
		}
	}

}
