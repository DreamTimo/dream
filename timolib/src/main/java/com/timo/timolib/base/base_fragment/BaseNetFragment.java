package com.timo.timolib.base.base_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timo.timolib.BaseTools;
import com.timo.timolib.base.base_activity.BaseNetView;
import com.timo.timolib.base.base_activity.DataBasePresenter;
import com.timo.timolib.base.base_activity.ShowDataContract;
import com.timo.timolib.base.base_activity.StateContract;
import com.timo.timolib.http.MyHttpParams;

public abstract class BaseNetFragment<T> extends BaseFragment implements ShowDataContract<T> {
	private StateContract.StateView mView;
	protected DataBasePresenter<T> groupPresenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			initData();
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		mView=new BaseNetView(getActivity()) {
			
			@Override
			public View getChildRootView() {
				return View.inflate(getActivity(),getContentResId(), null);
			}
			@Override
			public void showLoadStatus(int state) {
				 super.showLoadStatus(state);
				 showLoadState(state);
			}
		};
		initView(mView.getRootView());
	    return mView.getRootView();
	}
	protected final StateContract.StateView getStateView() {
        return mView;
    }
	protected void initData() {
		try {
			groupPresenter = new DataBasePresenter<T>(getActivity(), getStateView(), this) {
			};
			groupPresenter.getData(getRequest(), getUrl(), getApi());
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		}
	}
	@Override
	public void setPresenter(DataBasePresenter presenter) {}
	@Override
	public void showError(String message) {}
	protected abstract Class getApi();

	protected abstract MyHttpParams getRequest();

	protected abstract String getUrl();
	protected abstract void initView(View v);
	protected abstract int getContentResId();
	protected void showLoadState(int state){}
	
}
