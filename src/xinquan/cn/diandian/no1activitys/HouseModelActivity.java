package xinquan.cn.diandian.no1activitys;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/*
 * 楼盘户型相册信息Activity
 */
public class HouseModelActivity extends Activity implements OnClickListener,
		OnGestureListener {
	private LinearLayout ly;
	private TextView tv1; // 户型图
	private TextView tv2; // 效果图
	private TextView tv3; // 交通图
	private TextView tv4; // 实景图
	private TextView tv5; // 样板间
	private ViewFlipper vf; // 相冊ViewFlipper
	private TextView tv; //
	private String id;
	private String housesid; // 楼盘id
	private int photopage; // 图片页数
	private int photototalpage; // 图片总页数
	private GestureDetector gd; // 手势加工类
	private String tag = "HouseModelActivity";
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.housemodel);
		initView();
		initdata();
		initListener();
	}

	/*
	 * 初始化监听器
	 */
	private void initListener() {
		mTitleBar.setLeftOnClickListener(this);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
		tv5.setOnClickListener(this);
		vf.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.e(tag, "1");
				return gd.onTouchEvent(event);
			}
		});
	}

	/*
	 * 初始化数据
	 */
	private void initdata() {
		initvf("family_pictures");
	}

	/*
	 * 加載册数据
	 */
	private void initvf(String s) {
		vf.removeAllViews();
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "houses");
		ha.put("c", "houses");
		ha.put("a", "get_houses_pictures");
		ha.put("housesId", housesid);
		ha.put("pictures_type", s);
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								photopage = 1;
								photototalpage = ja.length();
								tv.setText("" + photopage + "/"
										+ photototalpage);
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									RelativeLayout rl = (RelativeLayout) MyApplication.lf
											.inflate(R.layout.vf_item, null);
									NetworkImageView nw = (NetworkImageView) rl
											.findViewById(R.id.im);
									TextView tv = (TextView) rl
											.findViewById(R.id.tv);
									MyApplication.client
											.getImageForNetImageView(
													jb.getString("url"), nw,
													R.drawable.ic_launcher);
									tv.setText(jb.getString("alt"));
									vf.addView(rl);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
	}

	/*
	 * 初始化界面View的引用
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_hosue_detail);
		mTitleBar.setRightMenuVisible(false);
		String houseName = getIntent().getExtras().getString("housesname");
		if (!TextUtils.isEmpty(houseName)) {
			mTitleBar.setTitle(houseName);	
		}
		
		gd = new GestureDetector(this);
		housesid = getIntent().getExtras().getString("housesid");
		id = (String) getIntent().getExtras().get("id");
		ly = (LinearLayout) findViewById(R.id.ly);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		vf = (ViewFlipper) findViewById(R.id.vf);
		tv = (TextView) findViewById(R.id.tv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 户型图
		 */
		case R.id.tv1:
			chagerColor(v);
			initvf("family_pictures");
			break;
		/*
		 * 效果图
		 */
		case R.id.tv2:
			chagerColor(v);
			initvf("album_pictures");
			break;
		/*
		 * 交通图
		 */
		case R.id.tv3:
			chagerColor(v);
			initvf("traffic_pictures");
			break;
		/*
		 * 实景图
		 */
		case R.id.tv4:
			chagerColor(v);
			initvf("rim_pictures");
			break;
		/*
		 * 样板间
		 */
		case R.id.tv5:
			chagerColor(v);
			initvf("assort_pictures");
			break;
		case R.id.title_icon_left_layout:
			HouseModelActivity.this.finish();
			break;

		default:
			break;
		}
	}

	/*
	 * 点击时候切换效果
	 */
	private void chagerColor(View v) {
		for (int i = 0; i < ly.getChildCount(); i++) {
			TextView tv = (TextView) ly.getChildAt(i);
			tv.setTextColor(Color.parseColor("#808080"));
			tv.setBackgroundResource(R.drawable.mode_an);
		}
		TextView tt = (TextView) v;
		tt.setTextColor(Color.parseColor("#3cb478"));
		tt.setBackgroundResource(R.drawable.mode_ming);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	/*
	 * 手势滑动触发
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() > e2.getX()) {
			if (photopage < photototalpage) {
				vf.setInAnimation(HouseModelActivity.this,
						R.anim.view_in_from_right);
				vf.setOutAnimation(HouseModelActivity.this,
						R.anim.view_out_to_left);
				vf.showNext();
				photopage = photopage + 1;
				tv.setText("" + photopage + "/" + photototalpage);
			}
		} else {
			if (photopage == 1) {

			} else {
				vf.setInAnimation(HouseModelActivity.this,
						R.anim.view_in_from_left);
				vf.setOutAnimation(HouseModelActivity.this,
						R.anim.view_out_to_right);
				vf.showPrevious();
				photopage = photopage - 1;
				tv.setText("" + photopage + "/" + photototalpage);
			}
		}
		Log.e(tag, "3");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.e(tag, "5");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
