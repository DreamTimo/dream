package com.timo.timolib.base.base_activity;
public interface ShowDataContract<T> extends BaseView<DataBasePresenter> {
	void showData(T data);
	void showError(String message);
}
