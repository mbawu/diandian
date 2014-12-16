package xinquan.cn.diandian.no1activitys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import xinquan.cn.diandian.CacheManager;
import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.RecommendActivity;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.bean.RecommendBean;
import xinquan.cn.diandian.login.LoginActivity;
import xinquan.cn.diandian.share.ShareSDKHelper;
import xinquan.cn.diandian.tools.StringUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 楼盘介绍Activity
 * 
 * @author Administrator 楼盘介绍Activity
 */
public class HouseDetailsMore extends Activity implements OnClickListener {

	private TitleBarContainer mTitleBar;
	private String data;
	private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9,
			txt10, txt11, txt12, txt13, txt14, txt15;
	private NetworkImageView manageer_pictures; // 经纪人头像
	private TextView manager; // 经纪人
	private TextView manager_phone; // 经纪人联系方式
	private ImageView sharenote; // 短信联系
	private ImageView sharephone; // 电话联系

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.house_detail_more);
		initView();
		try {
			initData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout),
				"楼盘介绍");
		mTitleBar.setRightMenuVisible(false);
		mTitleBar.setLeftOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt5 = (TextView) findViewById(R.id.txt5);
		txt6 = (TextView) findViewById(R.id.txt6);
		txt7 = (TextView) findViewById(R.id.txt7);
		txt8 = (TextView) findViewById(R.id.txt8);
		txt9 = (TextView) findViewById(R.id.txt9);
		txt10 = (TextView) findViewById(R.id.txt10);
		txt11 = (TextView) findViewById(R.id.txt11);
		txt12 = (TextView) findViewById(R.id.txt12);
		txt13 = (TextView) findViewById(R.id.txt13);
		txt14 = (TextView) findViewById(R.id.txt14);
		txt15 = (TextView) findViewById(R.id.txt15);
		manageer_pictures = (NetworkImageView) findViewById(R.id.manageer_pictures);
		manager = (TextView) findViewById(R.id.manager);
		manager_phone = (TextView) findViewById(R.id.manager_phone);
		sharenote = (ImageView) findViewById(R.id.sharenote);
		sharephone = (ImageView) findViewById(R.id.sharephone);
	}

	private String transTheSpace(String str) {
		return str.replaceAll("&nbsp;", " ");
	}

	private void initData() throws JSONException {
		Intent in = getIntent();
		data = in.getStringExtra("data");
		JSONObject response = new JSONObject(data);
		String introduceText = transTheSpace(response.getString("introduce")
				.replace("\\n", "\n"));
		Log.i("test", "介绍：" + introduceText);
		txt1.setText(response.getString("propertyname"));
		txt2.setText(response.getString("housesaddress"));
		txt3.setText(response.getString("proportion"));
		txt4.setText(response.getString("market_time"));
		txt5.setText(response.getString("use_time"));
		txt6.setText(response.getString("propertyfee"));
		txt7.setText(response.getString("market_address"));
		txt8.setText(response.getString("developers"));
		txt9.setText(response.getString("introduce"));
		txt10.setText(response.getString("note_mating"));
		txt11.setText(response.getString("note_traffic"));
		txt12.setText(response.getString("note_fitment"));
		txt13.setText(response.getString("note_storey"));
		txt14.setText(response.getString("note_carport"));
		txt15.setText(response.getString("note_correlation"));
		MyApplication.client.getImageForNetImageView(
				response.getString("manageer_pictures"), manageer_pictures,
				R.drawable.ic_launcher);
		manager.setText(response.getString("manager"));
		manager_phone.setText(response.getString("manager_phone"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sharenote:
			Uri ur = Uri.parse("smsto:");
			Intent sendIntent = new Intent(Intent.ACTION_VIEW, ur);
			sendIntent.putExtra("address", manager_phone.getText().toString()); // 电话号码，这行去掉的话，默认就没有电话
			sendIntent.putExtra("sms_body", "");
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
			break;

		case R.id.sharephone:
			tuichu();
			break;
		default:
			finish();
			break;
		}

	}
	
	/*
	 * 是否確定拨打联系人电话
	 */
	public void tuichu() {
		new AlertDialog.Builder(HouseDetailsMore.this)
				
				.setMessage("确定拨打电话给项目经理?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_OK);//
						Intent phoneIntent = new Intent(
								"android.intent.action.CALL", Uri.parse("tel:"
										+ manager_phone.getText().toString()));
						startActivity(phoneIntent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);//
					}
				}).show();
	}
}
