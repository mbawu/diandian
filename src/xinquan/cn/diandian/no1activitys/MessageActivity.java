package xinquan.cn.diandian.no1activitys;

import java.util.ArrayList;
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
import xinquan.cn.diandian.bean.MyMessage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 消息列表Activty
 */
public class MessageActivity extends Activity implements OnClickListener {
	private ListView lv;
	private Button intercept;
	private ArrayList<MyMessage> al;
	private Myad ad;
	private int page = 1;
	private int totalpage = 1;
	private int lastitem;
	private View footview;
	private Boolean jiazai = true;
	private TitleBarContainer mTitleBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.messageactivity);
		initView();
		initData();
		initlistener();
	}

	/*
	 * 初始化数据
	 */
	private void initData() {
		initlv();
	}

	/*
	 * 初始化ListView数据
	 */
	private void initlv() {
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "get_msg");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", "-1"));
			ha.put("page", "" + page);
			ha.put("rowcount", "6");
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
									page = page + 1;
									totalpage = response.getInt("totlepage");
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										MyMessage ms = new MyMessage();
										JSONObject jb = ja.getJSONObject(i);
										ms.setAddTime(jb.getString("addTime"));
										ms.setContent(jb.getString("content"));
										ms.setMsg_title(jb
												.getString("msg_title"));
										ms.setMsgId(jb.getString("msgId"));
										ms.setPicture(jb.getString("picture"));
										ms.setState(jb.getString("state"));
										ms.setUrl(jb.getString("url"));
										al.add(ms);
									}
									ad.notifyDataSetChanged();
									footview.setVisibility(View.GONE);
									intercept.setVisibility(View.GONE);
								} else {
									ad.notifyDataSetChanged();
									footview.setVisibility(View.GONE);
									intercept.setVisibility(View.GONE);
								}
							} catch (JSONException e) {
								footview.setVisibility(View.GONE);
								intercept.setVisibility(View.GONE);
								e.printStackTrace();
							}
							jiazai = true;
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							footview.setVisibility(View.GONE);
							intercept.setVisibility(View.GONE);
							jiazai = true;
						}
					});
		}
	}

	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		mTitleBar.setLeftOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				final ViewHolder holder = (ViewHolder) arg1.getTag();
				HashMap<String, String> ha = new HashMap<String, String>();
				ha.put("m", "user");
				ha.put("c", "user");
				ha.put("a", "update_msg");
				ha.put("user_key", MyApplication.seskey);
				ha.put("userId", MyApplication.sp.getString("userid", "-1"));
				ha.put("msgId", holder.t_msgId);
				MyApplication.client.postWithURL(UrlPath.baseURL, ha,
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject arg0) {
								try {
									if (arg0.getInt("code") == 1) {
										ad.notifyDataSetChanged();
										MyApplication.messageneedflash = true;
										holder.status.setVisibility(View.GONE);
										Intent in = new Intent(
												MessageActivity.this,
												Message2Activity.class);
										in.putExtra("url", holder.t_url);
										in.putExtra("content", holder.t_content);
										startActivity(in);
									} else {
										Toast.makeText(MessageActivity.this,
												"此数据详细信息有误", 2000).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError arg0) {

							}
						});

			}
		});
		/*
		 * ListView滑动监听器
		 */
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastitem == (page - 1) * 6 + 1 && jiazai) {
					jiazai = false;
					intercept.setVisibility(View.GONE);
					footview.setVisibility(View.VISIBLE);
					initlv();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastitem = totalItemCount;
			}
		});

	}

	/*
	 * 初始化View
	 */
	private void initView() {
		mTitleBar = new TitleBarContainer(findViewById(R.id.title_layout), R.string.Mymessage);
		mTitleBar.setRightMenuVisible(false);
		footview = MyApplication.lf.inflate(R.layout.footview, null);
		lv = (ListView) findViewById(R.id.lv);
		intercept = (Button) findViewById(R.id.intercept);
		al = new ArrayList<MyMessage>();
		ad = new Myad();
		lv.addFooterView(footview);
		lv.setAdapter(ad);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_icon_left_layout:
			MessageActivity.this.finish();
			break;

		default:
			break;
		}

	}

	/*
	 * ListView适配器
	 */
	private class Myad extends BaseAdapter {

		@Override
		public int getCount() {
			if (al == null) {
				return 0;
			} else {
				return al.size();
			}

		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyMessage ms = al.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(R.layout.message_item, null);
				holder = new ViewHolder();
				
//				holder.image = (NetworkImageView) convertView
//						.findViewById(R.id.im);
				holder.status = (ImageView)convertView
						.findViewById(R.id.status);
//				holder.content = (TextView) convertView
//						.findViewById(R.id.content);
				holder.time = (TextView) convertView
						.findViewById(R.id.addTime);
//				holder.place = (TextView) convertView
//						.findViewById(R.id.place);
				holder.title = (TextView) convertView
						.findViewById(R.id.msg_title);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.t_content = ms.getContent();
			holder.t_title = ms.getMsg_title();
			holder.t_url = ms.getUrl();
			holder.t_time = ms.getAddTime();
			holder.t_msgId = ms.getMsgId();
			convertView.setTag(holder);
			
			holder.title.setText(holder.t_title);
			holder.time.setText(holder.t_time);
			
			if("1".equals(ms.getState())){
				holder.status.setVisibility(View.GONE);
			}else{
				holder.status.setVisibility(View.VISIBLE);
			}
//			holder.content.setText(holder.t_content);
//			holder.place.setText("北京市 海淀区");
//			MyApplication.client.getImageForNetImageView(ms.getPicture(), holder.image, R.drawable.ic_launcher);
			return convertView;
		}
	}

	private class ViewHolder{
//		NetworkImageView image;
		ImageView status;
		TextView title, time;
		String t_title, t_url, t_time, t_msgId, t_content;
	}
}
