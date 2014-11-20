package xinquan.cn.diandian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/*
 *用户指南Activity
 */
public class UserguideActivity extends Activity implements OnGestureListener, OnClickListener { // 鐢ㄦ埛鎸囧崡Activity
	private GestureDetector gd; // 手势监听器
//	private ImageView im5; // 第一个点
//	private ImageView im6; // 第二个点
//	private ImageView im7; // 第三个点
//	private ImageView im8; // 第四个点
	private ViewFlipper vf; // ViewFlipper
	private int i;
	
	private ImageView im3;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yonghuzhinan);
		MyApplication.getInstance().addActivity(this);
		MyApplication.ed.putBoolean("one", false); //
		MyApplication.ed.commit();
		/*
		 * 初始化手势监听器
		 */
		gd = new GestureDetector(this);
		i = 0;
		/*
		 * 初始化ViewFlipper和用户指南图片等等信息
		 */
		vf = (ViewFlipper) findViewById(R.id.vf);

//		im8 = (ImageView) findViewById(R.id.dian4);
//		im5.setBackgroundResource(R.drawable.baidian);
		DisplayMetrics dm = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(dm);
		im3 = (ImageView)findViewById(R.id.im3);
		im3.setOnClickListener(this);
	}

	/*
	 * 初始化ViewFlipper和用户指南图片等等信息
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			startActivity(new Intent(UserguideActivity.this, MainActivity.class));
			UserguideActivity.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/*
	 * 触摸事件交给手势处理器处理
	 */
	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);//
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * 触发了手势滑动事件，判断往左滑还是往右滑 然后执行切换图片效果
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() > e2.getX()) {

			switch (++i % 3) {
			case 1:
//				im6.setBackgroundResource(R.drawable.baidian);
//				im5.setBackgroundResource(R.drawable.dian);
//				im7.setBackgroundResource(R.drawable.dian);
//				im8.setBackgroundResource(R.drawable.dian);
				vf.setInAnimation(UserguideActivity.this,
						R.anim.view_in_from_right);
				// 锟斤拷锟斤拷View锟剿筹拷锟斤拷幕时锟斤拷使锟矫的讹拷锟斤拷
				vf.setOutAnimation(UserguideActivity.this,
						R.anim.view_out_to_left);
				vf.showNext();
				break;
			case 2:
//				im5.setBackgroundResource(R.drawable.dian);
//				im7.setBackgroundResource(R.drawable.baidian);
//				im6.setBackgroundResource(R.drawable.dian);
//				im8.setBackgroundResource(R.drawable.dian);
				vf.setInAnimation(UserguideActivity.this,
						R.anim.view_in_from_right);
				// 锟斤拷锟斤拷View锟剿筹拷锟斤拷幕时锟斤拷使锟矫的讹拷锟斤拷
				vf.setOutAnimation(UserguideActivity.this,
						R.anim.view_out_to_left);
				vf.showNext();
				break;
			case 3:
//				im5.setBackgroundResource(R.drawable.dian);
//				im6.setBackgroundResource(R.drawable.dian);
//				im7.setBackgroundResource(R.drawable.dian);
//				im8.setBackgroundResource(R.drawable.baidian);
				vf.setInAnimation(UserguideActivity.this,
						R.anim.view_in_from_right);
				// 锟斤拷锟斤拷View锟剿筹拷锟斤拷幕时锟斤拷使锟矫的讹拷锟斤拷
				vf.setOutAnimation(UserguideActivity.this,
						R.anim.view_out_to_left);
				vf.showNext();
				break;
			case 0:
//				startActivity(new Intent(UserguideActivity.this,
//						MainActivity.class));
//				UserguideActivity.this.finish();
				break;

			}
		}
		return false;

	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(UserguideActivity.this, MainActivity.class));
		UserguideActivity.this.finish();		
	}

}
