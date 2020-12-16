package com.cqs.weblib.view;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\16 0016 15:01
 * 邮箱:chen510470614@163.com
 */
public class HWebViewListener {

    void onPageStarted(HWebView hWebView, String url){

    }

    void onPageFinished(HWebView hWebView, String url){

    }

    void onReceivedError(HWebView hWebView, int errorCode, String description, String failingUrl){

    }

    boolean handleReceivedSslError(HWebView hWebView, final SslErrorHandler handler, SslError error){
        return false;
    }
}
