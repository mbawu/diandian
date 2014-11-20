package xinquan.cn.diandian.no4activitys;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.tools.FileUt;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

/*
 * 修改用户头像等信息Activity
 */
public class ChangePhotoActivity extends Activity implements OnClickListener {
	private ImageView im; // 用户头像
	private Button changephoto; // 更换头像按钮
	private EditText username; // 用户昵称
	private Button commit; // 确认提交
	private SlidingDrawer sl;
	private Button bt1; // 照相
	private Button bt2; // 相册
	private Button bt3; // 取消
	private Bitmap bm; // 从相册返回的图片bitmap
	private Handler handler;
	private String path; // 记住上传到服务器返回的图片路径
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changephoto);
		initview();
		initdata();
		initlistener();
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		changephoto.setOnClickListener(this);
		commit.setOnClickListener(this);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
	}

	/*
	 * 初始化数据
	 */
	private void initdata() {
//		MyApplication.client.getImageForNetImageView(path, im,	R.drawable.ic_launcher);
		MyApplication.client.downloadImage(im, MyApplication.sp.getString("head_portrait", ""));
		username.setText(MyApplication.sp.getString("username", ""));
	}

	/*
	 * 初始化View的引用
	 */
	private void initview() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.obj.equals("")) {
				} else {
					path = (String) msg.obj;
					im.setBackgroundDrawable(new BitmapDrawable(bm));
				}
			}
		};
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), "修改头像和姓名");
		mTitleBar.setRightMenuVisible(false);
		path = MyApplication.sp.getString("head_portrait", "");
		changephoto = (Button) findViewById(R.id.changephoto);
		username = (EditText) findViewById(R.id.username);
		commit = (Button) findViewById(R.id.commit);
		im = (ImageView) findViewById(R.id.im);
		im.setScaleType(ScaleType.FIT_XY);
		sl = (SlidingDrawer) findViewById(R.id.sl);
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 返回上级界面
		 */
		case R.id.title_icon_left_layout:
			ChangePhotoActivity.this.finish();
			break;
		/*
		 * 更换形象照片
		 */
		case R.id.changephoto:
			sl.animateToggle();
			break;
		/*
		 * 确认修改个人信息
		 */
		case R.id.commit:
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "update_user_name");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", "-1"));
			ha.put("username", username.getText().toString());
			ha.put("head_portrait", path);
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								switch (response.getInt("code")) {
								case 1:
									MyApplication.ed.putString("head_portrait",
											path);
									MyApplication.ed.putString("username",
											username.getText().toString());
									MyApplication.ed.commit();
									MyApplication.fragment4needflash = true;
									MyApplication.setactivityneedflush = true;
									ChangePhotoActivity.this.finish();
									Toast.makeText(ChangePhotoActivity.this,
											"修改个人资料成功", 2000).show();
									break;
								case 2:
									Toast.makeText(ChangePhotoActivity.this,
											"修改个人资料失败", 2000).show();
									break;
								case 3:
									Toast.makeText(ChangePhotoActivity.this,
											"您尚未登录", 2000).show();
									break;
								case 4:
									Toast.makeText(ChangePhotoActivity.this,
											"提交数据位空", 2000).show();
									break;

								default:
									break;
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

			break;
		/*
		 * 照相启动
		 */
		case R.id.bt1:
			sl.animateToggle();
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(in, 1);
			} else {
				Toast.makeText(ChangePhotoActivity.this, "请检查Sd卡是否插上", 2000)
						.show();
			}

			break;
		/*
		 * 调用相册
		 */
		case R.id.bt2:
			sl.animateToggle();
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 3);
				intent.putExtra("aspectY", 4);
				intent.putExtra("outputX", 180);
				intent.putExtra("outputY", 240);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, 2);
			} else {
				Toast.makeText(ChangePhotoActivity.this, "请检查Sd卡是否插上", 2000)
						.show();
			}
			break;
		/*
		 * 取消
		 */
		case R.id.bt3:
			sl.animateToggle();

			break;
		default:
			break;
		}

	}

	/*
	 * 从相册返回的图片,上传到服务器 返回服务器返回的路径保存起来
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				if (data != null) {
					if (data.getExtras() != null) {
						bm = (Bitmap) data.getExtras().get("data");
						try {
							HashMap<String, String> ha = new HashMap<String, String>();
							ha.put("uploadedfile", android.util.Base64
									.encodeToString(FileUt.bitmap2Bytes(bm),
											Base64.DEFAULT));
							Log.e("shangchuan",
									""
											+ android.util.Base64
													.encodeToString(FileUt
															.bitmap2Bytes(bm),
															Base64.DEFAULT));
							MyApplication.client
									.postWithURL(
											UrlPath.baseURL+"?m=otherData&c=other&a=update_portrait",
											ha, new Listener<JSONObject>() {
												@Override
												public void onResponse(
														JSONObject response) {
													try {
														switch (response
																.getInt("code")) {
														case 1:
															Toast.makeText(
																	ChangePhotoActivity.this,
																	"上传成功",
																	2000)
																	.show();
															String photo = response
																	.getString("image");
															Message ms = new Message();
															ms.obj = photo;
															handler.sendMessage(ms);
															break;
														case 2:
															Toast.makeText(
																	ChangePhotoActivity.this,
																	"上传失败",
																	2000)
																	.show();
															break;
														case 3:
															Toast.makeText(
																	ChangePhotoActivity.this,
																	"您尚未登录",
																	2000)
																	.show();
															break;
														case 4:
															Toast.makeText(
																	ChangePhotoActivity.this,
																	"您提交的数据为空",
																	2000)
																	.show();
															break;

														default:
															break;
														}
													} catch (JSONException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}

												}
											}, new ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {
													Toast.makeText(
															ChangePhotoActivity.this,
															"上传失败", 2000)
															.show();
												}
											});

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				Toast.makeText(ChangePhotoActivity.this, "请检查Sd卡是否插上", 2000)
						.show();
			}
		} else {
			if (data != null) {
				if (data.getExtras() != null) {
					bm = (Bitmap) data.getExtras().get("data");
					try {
						HashMap<String, String> ha = new HashMap<String, String>();
						ha.put("uploadedfile", android.util.Base64
								.encodeToString(FileUt.bitmap2Bytes(bm),
										Base64.DEFAULT));
						MyApplication.client
								.postWithURL(
										UrlPath.baseURL+"?m=otherData&c=other&a=update_portrait",
										ha, new Listener<JSONObject>() {
											@Override
											public void onResponse(
													JSONObject response) {
												try {
													switch (response
															.getInt("code")) {
													case 1:
														Toast.makeText(
																ChangePhotoActivity.this,
																"上传成功", 2000)
																.show();
														String photo = response
																.getString("image");
														Message ms = new Message();
														ms.obj = photo;
														handler.sendMessage(ms);
														break;
													case 2:
														Toast.makeText(
																ChangePhotoActivity.this,
																"上传失败", 2000)
																.show();
														break;
													case 3:
														Toast.makeText(
																ChangePhotoActivity.this,
																"您尚未登录", 2000)
																.show();
														break;
													case 4:
														Toast.makeText(
																ChangePhotoActivity.this,
																"您提交的数据为空",
																2000).show();
														break;

													default:
														break;
													}
												} catch (JSONException e) {
													// TODO Auto-generated
													// catch block
													e.printStackTrace();
												}

											}
										}, new ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {
												Toast.makeText(
														ChangePhotoActivity.this,
														"上传失败", 2000).show();
											}
										});

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
