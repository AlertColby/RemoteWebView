package com.cqs.weblib.view;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.cqs.weblib.R;
import com.google.gson.Gson;

import java.util.Map;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\16 0016 14:21
 * 邮箱:chen510470614@163.com
 * 描述：封装基本的webView,将基本的配置放在这里实现
 */
public class HWebView extends WebView {

    private static final String TAG = "HWebView";

    private Context context;
    private HWebViewListener hWebViewListener;
    boolean isReady;
    private boolean mTouchByUser;
    private Map<String, String> mHeaders;
    public static final String CONTENT_SCHEME = "file:///android_asset/";

    public HWebView(Context context) {
        super(context);
        init(context);
    }

    public HWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(21)
    public HWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        WebDefaultSettingManager.getInstance().toSetting(this);
        setWebViewClient(new HWebViewClient());
        JsInterface jsInterface = new JsInterface(context);
        //设置js可以访问的对象名为webview
        addJavascriptInterface(jsInterface,"webview");
    }

    /**
     * 设置监听当前HWebView的运行状态
     * @param listener
     */
    public void setListener(HWebViewListener listener){
        this.hWebViewListener = listener;
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    private void resetAllStateInternal(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return;
        }
        resetAllState();
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        Log.e(TAG, "HWebView load url: " + url);
        resetAllStateInternal(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        Log.e(TAG, "HWebView load url: " + url);
        resetAllStateInternal(url);
    }

    /**
     * 回调JS的方法
     * @param cmd
     * @param param
     */
    public void loadJS(String cmd, Object param) {
        String paramJson = "";
        if (param != null){
            paramJson = new Gson().toJson(param);
        }
        String trigger = "javascript:" + cmd + "(" + paramJson + ")";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
        } else {
            loadUrl(trigger);
        }
    }


    // 加载url时重置touch状态
    private void resetAllState() {
        mTouchByUser = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchByUser = true;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isReady() {
        return isReady;
    }

    private boolean isTouchByUser() {
        return mTouchByUser;
    }

    private class HWebViewClient extends WebViewClient {

        public static final String SCHEME_SMS = "sms:";

        /**
         * url重定向会执行此方法以及点击页面某些链接也会执行此方法
         *
         * @return true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载 false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading url: " + url);
            // 当前链接的重定向, 通过是否发生过点击行为来判断
            if (!isTouchByUser()) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            // 如果链接跟当前链接一样，表示刷新
            if (getUrl().equals(url)) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            if (handleLinked(url)) {
                return true;
            }
            // 控制页面中点开新的链接在当前webView中打开
            view.loadUrl(url, mHeaders);
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldOverrideUrlLoading url: "+ request.getUrl());
            // 当前链接的重定向
            if (!isTouchByUser()) {
                return super.shouldOverrideUrlLoading(view, request);
            }
            // 如果链接跟当前链接一样，表示刷新
            if (getUrl().equals(request.getUrl().toString())) {
                return super.shouldOverrideUrlLoading(view, request);
            }
            if (handleLinked(request.getUrl().toString())) {
                return true;
            }
            // 控制页面中点开新的链接在当前webView中打开
            view.loadUrl(request.getUrl().toString(), mHeaders);
            return true;
        }

        /**
         * 支持电话、短信、邮件、地图跳转，跳转的都是手机系统自带的应用
         */
        private boolean handleLinked(String url) {
            if (url.startsWith(WebView.SCHEME_TEL)
                    || url.startsWith(SCHEME_SMS)
                    || url.startsWith(WebView.SCHEME_MAILTO)
                    || url.startsWith(WebView.SCHEME_GEO)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ignored) {
                    ignored.printStackTrace();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e(TAG, "onPageFinished url:" + url);
            if (!TextUtils.isEmpty(url) && url.startsWith(CONTENT_SCHEME)) {
                isReady = true;
            }
            if (hWebViewListener != null) {
                hWebViewListener.onPageFinished(HWebView.this, url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e(TAG, "onPageStarted url: " + url);
            if (hWebViewListener != null) {
                hWebViewListener.onPageStarted(HWebView.this, url);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e(TAG, "webview error" + errorCode + " + " + description);
            if (hWebViewListener != null) {
                hWebViewListener.onReceivedError(HWebView.this, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            if (hWebViewListener != null &&
                    hWebViewListener.handleReceivedSslError(HWebView.this, handler, error)){
                return;
            }
            String channel = "";
            if (!TextUtils.isEmpty(channel) && channel.equals("play.google.com")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String message = context.getString(R.string.ssl_error);
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = context.getString(R.string.ssl_error_not_trust);
                        break;
                    case SslError.SSL_EXPIRED:
                        message = context.getString(R.string.ssl_error_expired);
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = context.getString(R.string.ssl_error_mismatch);
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = context.getString(R.string.ssl_error_not_valid);
                        break;
                }
                message += context.getString(R.string.ssl_error_continue_open);

                builder.setTitle(R.string.ssl_error);
                builder.setMessage(message);
                builder.setPositiveButton(R.string.continue_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                handler.proceed();
            }
        }
    }

}
