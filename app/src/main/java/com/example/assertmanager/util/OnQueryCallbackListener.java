package com.example.assertmanager.util;

import java.util.List;

import com.example.assertmanager.model.ZcBean;

public interface OnQueryCallbackListener {

	void onSuccess(List<ZcBean> results);

	void onError(Exception e);
}
