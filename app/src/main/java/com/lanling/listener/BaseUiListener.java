package com.lanling.listener;

import android.content.Context;

import com.lanling.activity.LoginActivity;
import com.lanling.util.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class BaseUiListener implements IUiListener {


    @Override
    public void onComplete(Object response) {
        if (null == response) {
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            return;
        }
        doComplete((JSONObject)response);
    }

    protected void doComplete(JSONObject values) {
    }

    @Override
    public void onError(UiError e) {
    }

    @Override
    public void onCancel() {
        Util.log("WodeFragment","取消");
    }

}
