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
 * 楼盘详情Activity
 * 
 * @author Administrator 楼盘详情Activity
 */
public class HouseDetails extends Activity implements OnClickListener {
	private String id; //
	private String housesId; // 楼盘ID
	private NetworkImageView picture; // 图片
	private View collect; // 收藏
	private Button recommend; // 马上推荐
	private TextView purpose_num;
	private TextView bargain_houses; //
	private TextView area_name; // 地区名称
	private TextView housesName; // 楼盘名称
	private TextView scale_price; // 平均价
	private TextView day; // 倒计时天数
	private TextView hour; // 倒计时小时数
	private TextView price; // 价格
	private TextView discount_note; // 优惠信息
	private RelativeLayout map; // 地图
	private TextView Cooperation_rules; // 合作规划
	private TextView introduce; // 介绍
	private NetworkImageView manageer_pictures; // 经纪人头像
	private TextView manager; // 经纪人
	private TextView manager_phone; // 经纪人联系方式
	private ImageView sharenote; // 短信联系
	private ImageView sharephone; // 电话联系
	private String map_sign;
	private LinearLayout ly;
	private TextView bar;
	private LinearLayout lybar;
	private RecommendBean gb;
	private ImageView jiantou;
	private TitleBarContainer mTitleBar;
	
	private String share_url;
	private String share_image;
	private String share_title;
	private StringBuilder share_message;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.housedetails);
		id = getIntent().getExtras().getString("id");
		share_message = new StringBuilder();
		initView();
		initData();
		initListener();
	}

	/*
	 * 初始化监听器
	 */
	private void initListener() {
		lybar.setOnClickListener(this);
		mTitleBar.setLeftOnClickListener(this).setRightOnClickListener(this);
		collect.setOnClickListener(this);
		recommend.setOnClickListener(this);
		sharenote.setOnClickListener(this);
		sharephone.setOnClickListener(this);
		map.setOnClickListener(this);
	}

	/*
	 * 初始化数据
	 */
	private void initData() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "houses");
		ha.put("c", "houses");
		ha.put("a", "get_houses_info");
		ha.put("housesId", id);
		Log.i("test", "url-->"+MyApplication.getUrl(ha));
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
//								Log.i("test", "details-->"+response.toString());
								housesId = (String) response.get("housesId");
								purpose_num.setText(response
										.getString("purpose_num"));
								bargain_houses.setText(response
										.getString("bargain_houses"));
								area_name.setText("("+response
										.getString("area_name")+")");
								share_title = response
										.getString("housesName");
								housesName.setText(share_title);
								share_title = "发现一个不错的楼盘，" + share_title + "项目";
								scale_price.setText(StringUtils.getPriceCommission(HouseDetails.this, StringUtils.convertIntString(response
										.getString("scale_price"))));
								JSONObject jb = response
										.getJSONObject("discount_time");
								day.setText(jb.getString("day"));
								hour.setText(jb.getString("hour"));
								map_sign = response.getString("map_sign");
								price.setText(StringUtils.getPriceString(HouseDetails.this, StringUtils.convertIntString(response.getString("price"))));
								
								String discountNote = response
										.getString("discount_note");
								
								share_message.append(";").append(discountNote).append(";");
								share_message.append("价格："+response.getString("price")+ "元/m²").append(";");
								discount_note.setText("点点特惠："+discountNote);
								Cooperation_rules.setText(transTheSpace(response.getString(
										"Cooperation_rules").replace("\\n", "\n")));
								
								String introduceText = transTheSpace(response.getString(
										"introduce").replace("\\n", "\n"));
								introduce.setText(introduceText);
								
								manager.setText(response.getString("manager"));
								manager_phone.setText(response
										.getString("manager_phone"));
								String imageURL = response.getString("picture");
								MyApplication.client.getImageForNetImageView(
										imageURL, picture,
										R.drawable.ic_launcher);
								readyForShareImage(imageURL);
								MyApplication.client.getImageForNetImageView(
										response.getString("manageer_pictures"),
										manageer_pictures,
										R.drawable.ic_launcher);
								JSONArray ja = response
										.getJSONArray("family_pictures");
								gb = new RecommendBean();
								gb.setCity_name(response.getString("city_name"));
								gb.setCityId(response.getString("cityId"));
								gb.setHousesId(response.getString("housesId"));
								gb.setHousesName(response
										.getString("housesName"));
								gb.setAreaId(response.getString("areaId"));
								gb.setArea_name(response.getString("area_name"));
								
								share_url = response.getString("share_url");
								
								share_message.append(share_url);
//								share_message.append(introduceText);
								/*
								 * 加载楼盘户型信息
								 */
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jj = ja.getJSONObject(i);
									LinearLayout li = (LinearLayout) MyApplication.lf
											.inflate(R.layout.family, null);
									li.setTag("" + i);
									li.setOnClickListener(HouseDetails.this);
									ly.addView(li);
									TextView tv = (TextView) li
											.findViewById(R.id.tv);
									NetworkImageView im = (NetworkImageView) li
											.findViewById(R.id.im);
									tv.setText(jj.getString("alt"));
									MyApplication.client
											.getImageForNetImageView(
													jj.getString("url"), im,
													R.drawable.ic_launcher);
								}
								hide();

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
	 * 初始化View的指針鏈接
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.title_hosue_detail);
		mTitleBar.setRightTextResource(R.string.share, false);
		
		jiantou = (ImageView) findViewById(R.id.jiantou);
		lybar = (LinearLayout) findViewById(R.id.lybar);
		bar = (TextView) findViewById(R.id.bar);
		ly = (LinearLayout) findViewById(R.id.ly);
		picture = (NetworkImageView) findViewById(R.id.picture);
		collect = findViewById(R.id.collect);
		recommend = (Button) findViewById(R.id.recommend);
		purpose_num = (TextView) findViewById(R.id.purpose_num);
		bargain_houses = (TextView) findViewById(R.id.bargain_houses);
		area_name = (TextView) findViewById(R.id.area_name);
		housesName = (TextView) findViewById(R.id.housesName);
		scale_price = (TextView) findViewById(R.id.scale_price);
		day = (TextView) findViewById(R.id.day);
		hour = (TextView) findViewById(R.id.hour);
		price = (TextView) findViewById(R.id.price);
		discount_note = (TextView) findViewById(R.id.discount_note);
		map = (RelativeLayout) findViewById(R.id.map);
		Cooperation_rules = (TextView) findViewById(R.id.Cooperation_rules);
		introduce = (TextView) findViewById(R.id.introduce);
		manageer_pictures = (NetworkImageView) findViewById(R.id.manageer_pictures);
		manager = (TextView) findViewById(R.id.manager);
		manager_phone = (TextView) findViewById(R.id.manager_phone);
		sharenote = (ImageView) findViewById(R.id.sharenote);
		sharephone = (ImageView) findViewById(R.id.sharephone);
	}

	/*
	 * 显示左右的楼盘户型
	 */
	private void show() {
		for (int i = 3; i < ly.getChildCount(); i++) {
			LinearLayout li = (LinearLayout) ly.getChildAt(i);
			li.setVisibility(View.VISIBLE);
		}
		jiantou.setBackgroundResource(R.drawable.xq_shang);
		bar.setText("隐藏楼盘");
	}

	/*
	 * 隐藏所有的楼盘户型
	 */
	private void hide() {
		for (int i = 3; i < ly.getChildCount(); i++) {
			LinearLayout li = (LinearLayout) ly.getChildAt(i);
			li.setVisibility(View.GONE);
		}
		jiantou.setBackgroundResource(R.drawable.xq_xia);
		bar.setText("展开全部");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 是否展开或者以藏楼盘户型列表
		 */
		case R.id.lybar:
			if (bar.getText().toString().equals("展开全部")) {
				show();
			} else {
				hide();
			}
			break;
		/*
		 * 返回
		 */
		case R.id.title_icon_left_layout:
			HouseDetails.this.finish();
			break;
		/*
		 * 短信联系
		 */
		case R.id.sharenote:
			Uri ur = Uri.parse("smsto:");
			Intent sendIntent = new Intent(Intent.ACTION_VIEW, ur);
			sendIntent.putExtra("address", manager_phone.getText().toString()); // 电话号码，这行去掉的话，默认就没有电话
			sendIntent.putExtra("sms_body", "");
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
			break;
		/*
		 * 电话联系
		 */
		case R.id.sharephone:
			tuichu();
			break;
		/*
		 * 右上角分享点击
		 */
		case R.id.title_icon_right_layout:
//			ShareWindow.getInstance(HouseDetails.this).show(share_url);
			new ShareSDKHelper(HouseDetails.this).showShare(share_title, share_message.toString(), share_image, share_url,  paListener);
			break;
		/*
		 * 收藏楼盘
		 */
		case R.id.collect:
			if (MyApplication.login) {
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "user");
				ha.put("a", "add_collect");
				ha.put("user_key", MyApplication.seskey);
				ha.put("userId", MyApplication.sp.getString("userid", "-1"));
				ha.put("housesId", housesId);
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getInt("code") == 1) {
										Toast.makeText(HouseDetails.this,
												"收藏成功", 2000).show();
									} else {
										Toast.makeText(HouseDetails.this,
												"收藏失败", 2000).show();
									}
								} catch (JSONException e) {
									Toast.makeText(HouseDetails.this, "解析异常",
											2000).show();
									e.printStackTrace();
								}
							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(HouseDetails.this, "网络异常", 2000)
										.show();
							}
						});
			} else {
				startActivity(new Intent(HouseDetails.this, LoginActivity.class));
			}
			break;
		/*
		 * 马上推荐楼盘
		 */
		case R.id.recommend:
			if (MyApplication.login) {
				if (gb == null) {
					Toast.makeText(HouseDetails.this, "房屋相信信息未获取完成，请稍等", 2000)
							.show();
				} else {
					Intent in = new Intent(HouseDetails.this,
							RecommendActivity.class);
					in.putExtra("bean", gb);
					startActivity(in);
				}
			} else {
				startActivity(new Intent(HouseDetails.this, LoginActivity.class));
			}

			break;
			
		case R.id.map:
			gotoBaiduMap();
			break;
		default:
			Intent in = new Intent(HouseDetails.this, HouseModelActivity.class);
			in.putExtra("id", (String) v.getTag());
			in.putExtra("housesid", id);
			in.putExtra("housesname", housesName.getText());
			startActivity(in);
			break;
		}

	}
	
	PlatformActionListener paListener = new PlatformActionListener() {

		public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
			
			Log.d(getClass().getSimpleName(), res.toString());
			handler.sendEmptyMessage(1);
		}

		public void onError(Platform plat, int action, Throwable t) {
			t.printStackTrace();
			handler.sendEmptyMessage(2);
		}

		public void onCancel(Platform plat, int action) {
			// 在这里添加取消分享的处理代码
		}

	};
	
	public void readyForShareImage(final String imageURL) {
		CacheManager manager = ((MyApplication)getApplication()).getCacheManager();
		manager.initalizeDir();
		new ShareImageTask(manager.getCacheDirPath(), imageURL).execute();
	}
	
	@Override
	protected void onDestroy() {
		CacheManager manager = ((MyApplication)getApplication()).getCacheManager();
		manager.clearCache();
		super.onDestroy();
	}



	public class ShareImageTask extends AsyncTask<Void, Integer, String> {
		String cachePath, url;

		public ShareImageTask(String cachePath, String url) {
			this.cachePath = cachePath;
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			FileOutputStream output = null;
			HttpURLConnection urlConn = null;
			String path = null;
			try {
				URL mURL = new URL(url);
				urlConn = (HttpURLConnection) (mURL.openConnection());
				InputStream inputStream = urlConn.getInputStream();
				
				int lastIndexOfDot = url.lastIndexOf(".");
				String ex_name = lastIndexOfDot > 0 ? url.substring(lastIndexOfDot) : ".jpg";
				Log.e("Roney", "------ex_name-----"+ex_name);
				File file = new File(cachePath , ("temp"+ex_name));
				path = file.getAbsolutePath();
				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}
				
				output = new FileOutputStream(file);
				byte buffer [] = new byte[1 * 1024];
				int r = 0;
				while((r = inputStream.read(buffer)) != -1){
					output.write(buffer, 0, r);
				}
				output.flush();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (output != null) {
						output.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (urlConn != null) {
					urlConn.disconnect();
				}
			}
			return path;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			share_image = result;
			Log.e("Roney", "------share_image-----"+share_image);
		}
		
	}
	
	private String transTheSpace(String str) {
		return str.replaceAll("&nbsp;", " ");
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				Toast.makeText(HouseDetails.this, "分享成功", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2){
				Toast.makeText(HouseDetails.this, "分享失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	/*
	 * 是否確定拨打联系人电话
	 */
	public void tuichu() {
		new AlertDialog.Builder(HouseDetails.this)
				
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
	
	public static final String latlog = "latlog";
	public void gotoBaiduMap(){
		Intent intent = new Intent(this, BaiduMapActivity.class);
		//map_sign
		intent.putExtra(latlog, map_sign);
		intent.putExtra("housesname", housesName.getText());
		startActivity(intent);
	}

}
