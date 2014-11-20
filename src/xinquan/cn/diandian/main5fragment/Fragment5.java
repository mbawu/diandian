package xinquan.cn.diandian.main5fragment;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import xinquan.cn.diandian.MainActivity;
import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.login.LoginActivity;
import xinquan.cn.diandian.no1activitys.HouseDetails;
import xinquan.cn.diandian.no5activitys.AboutActivity;
import xinquan.cn.diandian.no5activitys.FlowActivity;
import xinquan.cn.diandian.no5activitys.SuggestActivity;
import xinquan.cn.diandian.share.ShareSDKHelper;
import xinquan.cn.diandian.tools.Banbenshengji;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 更多  Fragment  根试图Fragment   第五个
 */
public class Fragment5 extends Fragment implements OnClickListener {
	private View v;
	private RelativeLayout flow; // 流程
	private RelativeLayout invite; // 邀请
	private RelativeLayout push; // 推送消息
	private RelativeLayout sugest; // 意见反馈
	private RelativeLayout update; // 版本更新
	private RelativeLayout cache; // 清空缓存
	private RelativeLayout about; // 关于我们
	private RelativeLayout tel; // 客服电话
	private ImageView push_im; // 推送消息按钮
	private TextView phone; // 联系电话
	private Handler ha;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment5, container, false);
			initView();
			initlistener();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	/*
	 * 初始化所有按钮图标监听器
	 */
	private void initlistener() {
		flow.setOnClickListener(this);
		invite.setOnClickListener(this);
		push.setOnClickListener(this);
		sugest.setOnClickListener(this);
		update.setOnClickListener(this);
		cache.setOnClickListener(this);
		about.setOnClickListener(this);
		tel.setOnClickListener(this);
		push_im.setOnClickListener(this);
	}

	/*
	 * 初始化View的引用
	 */
	private void initView() {
		ha = new Handler() {
			public void handleMessage(Message arg0) {
				super.handleMessage(arg0);
				Toast.makeText(getActivity(), "清除完毕", 2000).show();
			}
		};
		flow = (RelativeLayout) v.findViewById(R.id.flow);
		invite = (RelativeLayout) v.findViewById(R.id.invite);
		push = (RelativeLayout) v.findViewById(R.id.push);
		sugest = (RelativeLayout) v.findViewById(R.id.sugest);
		update = (RelativeLayout) v.findViewById(R.id.update);
		cache = (RelativeLayout) v.findViewById(R.id.cache);
		about = (RelativeLayout) v.findViewById(R.id.about);
		tel = (RelativeLayout) v.findViewById(R.id.tel);
		push_im = (ImageView) v.findViewById(R.id.push_im);
		phone = (TextView) v.findViewById(R.id.phone);
		/*
		 * 得到服务器的客服电话
		 */
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "otherData");
		ha.put("a", "other");
		ha.put("c", "service_phone");
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								phone.setText(response.getString("phone"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
		/*
		 * 判斷是否推送消息，推送消息状态在本地有保存
		 */
		if (MyApplication.sp.getBoolean("push", true)) {
			push_im.setBackgroundResource(R.drawable.push_yes);
		} else {
			push_im.setBackgroundResource(R.drawable.push_no);
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
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2){
				Toast.makeText(getActivity(), "分享失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 推荐流程
		 */
		case R.id.flow:
			startActivity(new Intent(getActivity(), FlowActivity.class));
			break;
		/*
		 * 邀请推荐
		 */
		case R.id.invite:
			/*if (MyApplication.login) {
				startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}*/
			ShareSDKHelper.shareShortMessage("点点卖房", getString(R.string.Short_Msg_Content), "http://www.diandianmaifang.com/mobile/regist.html", paListener);
//			new ShareSDKHelper(getActivity()).showShare("点点卖房", "欢迎访问点点卖房", null, "www.diandianmaifang.com",  paListener);
			break;
		/*
		 * 是否接收消息推送
		 */
		case R.id.push_im:
			Toast.makeText(getActivity(), "push", 2000).show();
			if (MyApplication.sp.getBoolean("push", false)) {
				MyApplication.ed.putBoolean("push", false);
				MyApplication.ed.commit();
				v.setBackgroundResource(R.drawable.push_no);
			} else {
				MyApplication.ed.putBoolean("push", true);
				MyApplication.ed.commit();
				v.setBackgroundResource(R.drawable.push_yes);
			}
			break;
		/*
		 * 意见反馈
		 */
		case R.id.sugest:
			if(MyApplication.login) {
				startActivity(new Intent(getActivity(), SuggestActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			
			break;
		/*
		 * 版本更新
		 */
		case R.id.update:
			Toast.makeText(getActivity(), "正在检查，请稍后", 2000).show();
			new Banbenshengji("", getActivity()).jiancha();
			break;
		/*
		 * 清除缓存
		 */
		case R.id.cache:
			ha.sendEmptyMessageAtTime(1, 2000);
			break;
		/*
		 * 关于我们
		 */
		case R.id.about:
//			startActivity(new Intent(getActivity(), AbouatActivity.class));//
			startActivity(new Intent(getActivity(), AboutActivity.class));
			break;
		/*
		 * 联系我们
		 */
		case R.id.tel:
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + phone.getText().toString()));
			startActivity(phoneIntent);
			break;
		default:
			break;
		}
	}

}
