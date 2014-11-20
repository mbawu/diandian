package xinquan.cn.diandian.push;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.DiandianmaifangActivity;
import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.UrlPath;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * 后台服务接收服务器推送消息
 * 
 */
public class MyServer extends Service {
	private Intent messageIntent = null;
	private PendingIntent messagePendingIntent = null;
	private int messageNotificationID = 1000;
	private Notification messageNotification = null;
	private NotificationManager messageNotificationManager = null;
	private Boolean isrun = true;
	private MyThread mt;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		messageNotification = new Notification();
		messageNotification.tickerText = "新消息"; // 閫氱煡鏍囬
		messageNotification.defaults = Notification.DEFAULT_SOUND;
		messageNotification.icon = R.drawable.app_icon;
		messageNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
		// 鐐瑰嚮鏌ョ湅
		messageIntent = new Intent(this, DiandianmaifangActivity.class);
		messagePendingIntent = PendingIntent.getActivity(this, 0,
				messageIntent, 0);
		mt = new MyThread();
	}

	/**
	 * 
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!mt.isAlive()) {
			mt.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 
	 */
	public class MyThread extends Thread {
		public void run() {
			while (isrun) {
				try {
					Thread.sleep(120000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (MyApplication.sp.getBoolean("push", false)) {
					HashMap<String, String> ha = new HashMap<String, String>();
					ha.put("push_id",
							MyApplication.sp.getString("push_id", "0"));
					ha.put("push_type", "2");
					MyApplication.client.postWithURL(UrlPath.push, ha,
							new Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									try {
										if (response.getInt("code") == 1) {
											JSONArray ja = response
													.getJSONArray("list");
											for (int i = 0; i < ja.length(); i++) {
												JSONObject jb = ja
														.getJSONObject(i);
												messageNotification.setLatestEventInfo(
														MyServer.this,
														"鏃犵嚎鐩掑瓙",
														jb.getString("push_title"),
														messagePendingIntent);
												// 鍙戝竷娑堟伅
												messageNotificationManager
														.notify(messageNotificationID,
																messageNotification);
												// 閬垮厤瑕嗙洊娑堟伅锛岄噰鍙朓D鑷
												messageNotificationID++;
												MyApplication.ed.putString(
														"push_id",
														jb.getString("push_id"));
												MyApplication.ed.commit();
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

			}

		}
	}

}
