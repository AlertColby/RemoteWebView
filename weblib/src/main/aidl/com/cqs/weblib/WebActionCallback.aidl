// WebActionCallback.aidl
package com.cqs.weblib;

// responseCode 分为：成功 1， 失败 0. 失败时response返回{"code": 1, "message":"error message"}

interface WebActionCallback {
    void onResult(int responseCode, String actionName, String response);
}
