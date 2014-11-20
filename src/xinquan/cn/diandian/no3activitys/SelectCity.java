package xinquan.cn.diandian.no3activitys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.MainActivity;
import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.address.CharacterParser;
import xinquan.cn.diandian.address.ClearEditText;
import xinquan.cn.diandian.address.PinyinComparator;
import xinquan.cn.diandian.address.SideBar;
import xinquan.cn.diandian.address.SideBar.OnTouchingLetterChangedListener;
import xinquan.cn.diandian.address.SortAdapter;
import xinquan.cn.diandian.address.SortModel;
import xinquan.cn.diandian.main5fragment.Fragment3;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class SelectCity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectaddress);
		initViews();
		initData();
		initlistener();
	}

	private void initlistener() {
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent in = new Intent(SelectCity.this, SelectArea.class);
				in.putExtra("cityid",
						((SortModel) adapter.getItem(position)).getId());
				in.putExtra("cityname",
						((SortModel) adapter.getItem(position)).getName());
				startActivity(in);
			}
		});
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initData() {
		// 根据a-z进行排序源数据
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "city");
		ha.put("c", "city");
		ha.put("a", "get_town");
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								SourceDateList = filledData(ja);
								Collections.sort(SourceDateList,
										pinyinComparator);
								adapter.updateListView(SourceDateList);
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

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		if (SourceDateList == null) {
			SourceDateList = new ArrayList<SortModel>();
		}
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */

	private List<SortModel> filledData(JSONArray ja) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < ja.length(); i++) {

			try {
				JSONObject jb = ja.getJSONObject(i);
				SortModel sortModel = new SortModel();
				sortModel.setName(jb.getString("name"));
				sortModel.setId(jb.getString("cityId"));
				// 汉字转换成拼音
				String pinyin = characterParser
						.getSelling(jb.getString("name"));
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}

				mSortList.add(sortModel);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.xuanhao) {
			MyApplication.xuanhao = false;
			SelectCity.this.finish();
		}
	}

}
