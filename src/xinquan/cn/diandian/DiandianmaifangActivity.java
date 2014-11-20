package xinquan.cn.diandian;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.push.MyServer;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/*入口Activity 
 整个程序的主入口*/
public class DiandianmaifangActivity extends Activity { // LOGO界面，公司标示
	private Timer ti; // 定时器
	private Handler handler; // handler

	/**
     * 生命周期内初始化View  数据  监听器 
     */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.diandian);
		/* 程序初始化的时候获得屏幕宽高 */
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		MyApplication.with = metric.widthPixels;
		MyApplication.height = metric.heightPixels;
		MyApplication.getInstance().addActivity(this);
		/*
		 * 判断本地SharedPreferences是否保存有登录有的记录 如果有 证明用户注册过 直接取出用户名密码自动登录
		 */
		if (MyApplication.sp.getString("phone", null)!=null && MyApplication.sp.getString("password", null)!=null){
			/*
			 * 向服务器发送自动登录请求
			 */
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "login");
			ha.put("a", "do_login");
			ha.put("phone", MyApplication.sp.getString("phone", ""));
			ha.put("passwords", MyApplication.sp.getString("password", ""));
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
									MyApplication.seskey = response
											.getString("user_key");

									MyApplication.ed.putString("username",
											response.getString("userName"));

									MyApplication.ed.putString("name",
											response.getString("name"));
									MyApplication.ed.putString("address",
											response.getString("address"));
									MyApplication.ed.putString("phone",
											response.getString("phone"));
									MyApplication.ed
											.putString(
													"telephone",
													response.getString("telephone"));
									MyApplication.ed.putString("card",
											response.getString("card"));
									MyApplication.ed.putString("type",
											response.getString("type"));
									MyApplication.ed.putString("bankname",
											response.getString("bankName"));
									MyApplication.ed.putString(
											"banknumber",
											response.getString("bankNumber"));
									MyApplication.ed.putString(
											"bankusername",
											response.getString("bankUserName"));
									MyApplication.ed.putString("money",
											response.getString("money"));
									MyApplication.ed.putString(
											"authentication",
											response.getString("authentication"));
									MyApplication.ed.putString("addtime",
											response.getString("addTime"));
									MyApplication.ed.putString(
											"parent_userid",
											response.getString("Parent_userId"));
									MyApplication.ed.putString("iP",
											response.getString("IP"));
									MyApplication.ed.putString(
											"ip_address",
											response.getString("IP_address"));
									MyApplication.ed.putString(
											"head_portrait",
											response.getString("head_portrait"));
									MyApplication.ed.commit();
									MyApplication.login = true;
									Toast.makeText(
											DiandianmaifangActivity.this,
											"登录成功", 2000).show();
								} else {
//									Toast.makeText(
//											DiandianmaifangActivity.this,
//											"登录失败", 2000).show();
									MyApplication.login = false;
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
		/* 2秒钟以后启动Handler */
		handler = new Handler() {
			public void handleMessage(Message msg) {
				start();
			}
		};
		handler.sendEmptyMessageDelayed(1, 2000);
		/*
		 * 启动后台服务接收推送消息
		 */
		DiandianmaifangActivity.this.startService(new Intent(
				DiandianmaifangActivity.this, MyServer.class));
	}

	/*
	 * 2秒后启动的方法代码,如果第一次使用本软件，跳到用户指南，如果不是第一次使用，跳到主界面
	 */
	private void start() {
		ti = new Timer();
		if (MyApplication.sp.getBoolean("one", true) == true) {
			startActivity(new Intent(DiandianmaifangActivity.this,
					UserguideActivity.class));
		} else {
			TimerTask tast = new TimerTask() {
				public void run() {
					startActivity(new Intent(DiandianmaifangActivity.this,
							MainActivity.class));
				}
			};
			ti.schedule(tast, 100);
		}
		DiandianmaifangActivity.this.finish();
	}

}