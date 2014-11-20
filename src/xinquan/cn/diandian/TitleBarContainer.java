package xinquan.cn.diandian;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleBarContainer implements OnClickListener{
	private static final String tag = "TitleBarContainer";
	private TextView title;
	private ImageView left_menu, right_menu;
	private View leftLayout, rightLayout;
	private TextView left_text, right_text;
	
	public TitleBarContainer(View mainView, String titleStr) {
		leftLayout = mainView.findViewById(R.id.title_icon_left_layout);
		rightLayout = mainView.findViewById(R.id.title_icon_right_layout);
		title = (TextView) mainView.findViewById(R.id.title_text); 
		left_menu = (ImageView) mainView.findViewById(R.id.title_icon_left);
		right_menu = (ImageView) mainView.findViewById(R.id.title_icon_right);
		left_text = (TextView) mainView.findViewById(R.id.title_text_left);
		right_text = (TextView) mainView.findViewById(R.id.title_text_right);
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
		setTitle(titleStr);
	}
	
	public TitleBarContainer(View mainView, int titleId) {
		leftLayout = mainView.findViewById(R.id.title_icon_left_layout);
		rightLayout = mainView.findViewById(R.id.title_icon_right_layout);
		title = (TextView) mainView.findViewById(R.id.title_text); 
		left_menu = (ImageView) mainView.findViewById(R.id.title_icon_left);
		right_menu = (ImageView) mainView.findViewById(R.id.title_icon_right);
		left_text = (TextView) mainView.findViewById(R.id.title_text_left);
		right_text = (TextView) mainView.findViewById(R.id.title_text_right);
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
		setTitle(titleId);
	}

	
	public TitleBarContainer setLeftOnClickListener(OnClickListener l) {
		leftLayout.setOnClickListener(l);
		return this;
	}
	
	public TitleBarContainer setRightOnClickListener(OnClickListener l) {
		rightLayout.setOnClickListener(l);
		return this;
	}
	
	public TitleBarContainer setTitle(int resId) {
		title.setText(resId);
		return this;
	}
	
	public TitleBarContainer setTitle(String text) {
		title.setText(text);
		return this;
	}
	
	public TitleBarContainer setLeftMenuResource(int resId) {
		if (left_menu != null) {
			left_menu.setImageResource( resId);
		}
		return this;
	}
	
	public TitleBarContainer setRightMenuResource(int resId) {
		if (right_menu != null) {
			right_menu.setImageResource( resId);
		}
		return this;
	}
	
	public TitleBarContainer setLeftTextResource(int resId, boolean showIcon) {
		if (left_text != null) {
			left_text.setText(resId);
		}
		left_menu.setVisibility(showIcon ? View.VISIBLE :View.GONE);
		return this;
	}
	
	public TitleBarContainer setLeftTextResource(String lt, boolean showIcon) {
		if (left_text != null) {
			left_text.setText(lt);
		}
		left_menu.setVisibility(showIcon ? View.VISIBLE :View.GONE);
		return this;
	}
	
	public TitleBarContainer setRightTextResource(int resId, boolean showIcon) {
		if (right_text != null) {
			right_text.setText( resId);
		}
		right_menu.setVisibility(showIcon ? View.VISIBLE :View.GONE);
		return this;
	}
	
	public View getBadgeView(boolean isLeft) {
		return isLeft ? leftLayout : rightLayout;
	}
	
	public TitleBarContainer setLeftMenuVisible(boolean show) {
		int visible = show ? View.VISIBLE : View.INVISIBLE;
		if (leftLayout != null && leftLayout.getVisibility() != visible) {
			leftLayout.setVisibility(visible);
		}
		return this;
	}
	
	public TitleBarContainer setRightMenuVisible(boolean show) {
		int visible = show ? View.VISIBLE : View.INVISIBLE;
		if (rightLayout != null && rightLayout.getVisibility() != visible) {
			rightLayout.setVisibility(visible);
		}
		return this;
	}
	
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.title_icon_left_layout:
				
				break;
			case R.id.title_icon_right_layout:
				
				break;

			default:
				break;
			}
		} catch (Exception e) {
			Log.e(tag, e.getMessage(), e);
		}
	}
	
}
