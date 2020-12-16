// IWebAction.aidl
package com.cqs.weblib;

// Declare any non-default types here with import statements
import com.cqs.weblib.WebActionCallback;

interface IWebAction {
    void jsCall(String actionName, String jsonParam, in WebActionCallback callback);
}
