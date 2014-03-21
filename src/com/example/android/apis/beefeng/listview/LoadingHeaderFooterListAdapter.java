package com.example.android.apis.beefeng.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.apis.R;

/**
 * 带加载状态的adapter
 * @author liuqingfeng
 *
 * @param <E>
 */
public class LoadingHeaderFooterListAdapter<E extends BaseAdapter> extends HeaderFooterListAdapter<BaseAdapter>{
	
	public static final int STATUS_HAS_MORE = 0;
	
	public static final int STATUS_LOADING = 1;
	
	public static final int STATUS_NO_MORE = 2;
	
	public static final int STATUS_LOAD_FAIL = 3;
	
	private View mLoadingHeader;
	
	private View mLoadingFooter;

	private ProgressBar mLoadingMoreProgress;
	
	private TextView mLoadingMoreText;
	
	public LoadingHeaderFooterListAdapter(ListView view, BaseAdapter adapter) {
		super(view, adapter);
		mLoadingHeader = LayoutInflater.from(view.getContext()).inflate(R.layout.list_loading_header, null);
		mLoadingFooter = LayoutInflater.from(view.getContext()).inflate(R.layout.list_loading_footer, null);
		mLoadingMoreProgress = (ProgressBar)mLoadingFooter.findViewById(R.id.loading_progress);
		mLoadingMoreText = (TextView)mLoadingFooter.findViewById(R.id.loading_message);
	}

	public void setLoadingHeaderVisibility(boolean visible){
		if(visible){
			if(!isViewAdded(mLoadingHeader)){
				addHeader(mLoadingHeader);
			}
		}else{
			removeHeader(mLoadingHeader);
		}
	}
	
	public void setLoadingFooterStatus(int status){
		if(!isViewAdded(mLoadingFooter)){
			addFooter(mLoadingFooter);
			getWrappedAdapter().notifyDataSetChanged();
		}
		
		switch (status) {
		case STATUS_HAS_MORE:
			mLoadingMoreProgress.setVisibility(View.GONE);
			mLoadingMoreText.setText("加载更多");
			break;
		case STATUS_LOADING:
			mLoadingMoreProgress.setVisibility(View.VISIBLE);
			mLoadingMoreText.setText("正在加载。。。");
			break;
		case STATUS_NO_MORE:
			mLoadingMoreProgress.setVisibility(View.GONE);
			mLoadingMoreText.setText("没有更多了");
			break;
		case STATUS_LOAD_FAIL:
			break;
		default:
			break;
		}
		
	}
	
	public boolean isViewAdded(View view){
		return view.getParent() != null;
	}
}
