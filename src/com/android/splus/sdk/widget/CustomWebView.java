/**
 * @Title: CustomWebView.java
 * @Package com.sanqi.android.sdk.widget
 * Copyright: Copyright (c) 2013
 * @author xiaoming.yuan
 * @date 2013-11-6 上午10:50:23
 * @version V1.0
 */

package com.android.splus.sdk.widget;

import com.android.splus.sdk.utils.log.LogHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

/**
 * @ClassName: CustomWebView
 * @author xiaoming.yuan
 * @date 2013-11-6 上午10:50:23
 */

@SuppressLint("NewApi")
public class CustomWebView extends WebView {
    private static final String TAG = "CustomWebView";

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context) {
        super(context);
    }

    /**
     * Title: setHorizontalScrollbarOverlay Description:
     *
     * @param overlay
     * @see android.webkit.WebView#setHorizontalScrollbarOverlay(boolean)
     */
    @Override
    public void setHorizontalScrollbarOverlay(boolean overlay) {

        LogHelper.i(TAG, "setHorizontalScrollbarOverlay" + overlay);
        super.setHorizontalScrollbarOverlay(overlay);

    }

    /**
     * Title: setVerticalScrollbarOverlay Description:
     *
     * @param overlay
     * @see android.webkit.WebView#setVerticalScrollbarOverlay(boolean)
     */
    @Override
    public void setVerticalScrollbarOverlay(boolean overlay) {
        LogHelper.i(TAG, "setVerticalScrollbarOverlay" + overlay);
        super.setVerticalScrollbarOverlay(overlay);

    }

    /**
     * Title: overlayHorizontalScrollbar Description:
     *
     * @return
     * @see android.webkit.WebView#overlayHorizontalScrollbar()
     */
    @Override
    public boolean overlayHorizontalScrollbar() {
        LogHelper.i(TAG, "overlayHorizontalScrollbar");
        return super.overlayHorizontalScrollbar();

    }

    /**
     * Title: overlayVerticalScrollbar Description:
     *
     * @return
     * @see android.webkit.WebView#overlayVerticalScrollbar()
     */
    @Override
    public boolean overlayVerticalScrollbar() {
        LogHelper.i(TAG, "overlayVerticalScrollbar");

        return super.overlayVerticalScrollbar();

    }

    /**
     * Title: getCertificate Description:
     *
     * @return
     * @see android.webkit.WebView#getCertificate()
     */
    @Override
    public SslCertificate getCertificate() {
        LogHelper.i(TAG, "getCertificate");
        return super.getCertificate();

    }

    /**
     * Title: setCertificate Description:
     *
     * @param certificate
     * @deprecated
     * @see android.webkit.WebView#setCertificate(android.net.http.SslCertificate)
     */
    @Override
    public void setCertificate(SslCertificate certificate) {
        LogHelper.i(TAG, "setCertificate");
        super.setCertificate(certificate);

    }

    /**
     * Title: savePassword Description:
     *
     * @param host
     * @param username
     * @param password
     * @see android.webkit.WebView#savePassword(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void savePassword(String host, String username, String password) {
        LogHelper.i(TAG, "savePassword");
        super.savePassword(host, username, password);

    }

    /**
     * Title: setHttpAuthUsernamePassword Description:
     *
     * @param host
     * @param realm
     * @param username
     * @param password
     * @see android.webkit.WebView#setHttpAuthUsernamePassword(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setHttpAuthUsernamePassword(String host, String realm, String username,
            String password) {
        LogHelper.i(TAG, "setHttpAuthUsernamePassword");
        super.setHttpAuthUsernamePassword(host, realm, username, password);

    }

    /**
     * Title: getHttpAuthUsernamePassword Description:
     *
     * @param host
     * @param realm
     * @return
     * @see android.webkit.WebView#getHttpAuthUsernamePassword(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String[] getHttpAuthUsernamePassword(String host, String realm) {
        LogHelper.i(TAG, "getHttpAuthUsernamePassword");
        return super.getHttpAuthUsernamePassword(host, realm);

    }

    /**
     * Title: destroy Description:
     *
     * @see android.webkit.WebView#destroy()
     */
    @Override
    public void destroy() {
        LogHelper.i(TAG, "destroy");
        super.destroy();

    }

    /**
     * Title: setNetworkAvailable Description:
     *
     * @param networkUp
     * @see android.webkit.WebView#setNetworkAvailable(boolean)
     */
    @Override
    public void setNetworkAvailable(boolean networkUp) {
        LogHelper.i(TAG, "setNetworkAvailable");
        super.setNetworkAvailable(networkUp);

    }

    /**
     * Title: saveState Description:
     *
     * @param outState
     * @return
     * @see android.webkit.WebView#saveState(android.os.Bundle)
     */
    @Override
    public WebBackForwardList saveState(Bundle outState) {
        LogHelper.i(TAG, "saveState");
        return super.saveState(outState);

    }

    /**
     * Title: restoreState Description:
     *
     * @param inState
     * @return
     * @see android.webkit.WebView#restoreState(android.os.Bundle)
     */
    @Override
    public WebBackForwardList restoreState(Bundle inState) {
        LogHelper.i(TAG, "restoreState");
        return super.restoreState(inState);

    }

    /**
     * Title: loadUrl Description:
     *
     * @param url
     * @param additionalHttpHeaders
     * @see android.webkit.WebView#loadUrl(java.lang.String, java.util.Map)
     */
    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        LogHelper.i(TAG, "loadUrl");
        super.loadUrl(url, additionalHttpHeaders);

    }

    /**
     * Title: loadUrl Description:
     *
     * @param url
     * @see android.webkit.WebView#loadUrl(java.lang.String)
     */
    @Override
    public void loadUrl(String url) {

        LogHelper.i(TAG, "loadUrl");
        super.loadUrl(url);

    }

    /**
     * Title: postUrl Description:
     *
     * @param url
     * @param postData
     * @see android.webkit.WebView#postUrl(java.lang.String, byte[])
     */
    @Override
    public void postUrl(String url, byte[] postData) {
        LogHelper.i(TAG, "postUrl");
        super.postUrl(url, postData);

    }

    /**
     * Title: loadData Description:
     *
     * @param data
     * @param mimeType
     * @param encoding
     * @see android.webkit.WebView#loadData(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void loadData(String data, String mimeType, String encoding) {
        LogHelper.i(TAG, "loadData");
        super.loadData(data, mimeType, encoding);

    }

    /**
     * Title: loadDataWithBaseURL Description:
     *
     * @param baseUrl
     * @param data
     * @param mimeType
     * @param encoding
     * @param historyUrl
     * @see android.webkit.WebView#loadDataWithBaseURL(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding,
            String historyUrl) {
        LogHelper.i(TAG, "loadDataWithBaseURL");
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);

    }

    /**
     * Title: saveWebArchive Description:
     *
     * @param filename
     * @see android.webkit.WebView#saveWebArchive(java.lang.String)
     */
    @Override
    public void saveWebArchive(String filename) {
        LogHelper.i(TAG, "saveWebArchive");
        super.saveWebArchive(filename);

    }

    /**
     * Title: saveWebArchive Description:
     *
     * @param basename
     * @param autoname
     * @param callback
     * @see android.webkit.WebView#saveWebArchive(java.lang.String, boolean,
     *      android.webkit.ValueCallback)
     */
    @Override
    public void saveWebArchive(String basename, boolean autoname, ValueCallback<String> callback) {
        LogHelper.i(TAG, "saveWebArchive");
        super.saveWebArchive(basename, autoname, callback);

    }

    /**
     * Title: stopLoading Description:
     *
     * @see android.webkit.WebView#stopLoading()
     */
    @Override
    public void stopLoading() {
        LogHelper.i(TAG, "stopLoading");
        super.stopLoading();

    }

    /**
     * Title: reload Description:
     *
     * @see android.webkit.WebView#reload()
     */
    @Override
    public void reload() {
        LogHelper.i(TAG, "reload");
        super.reload();

    }

    /**
     * Title: canGoBack Description:
     *
     * @return
     * @see android.webkit.WebView#canGoBack()
     */
    @Override
    public boolean canGoBack() {
        LogHelper.i(TAG, "canGoBack");
        return super.canGoBack();

    }

    /**
     * Title: goBack Description:
     *
     * @see android.webkit.WebView#goBack()
     */
    @Override
    public void goBack() {
        LogHelper.i(TAG, "goBack");
        super.goBack();

    }

    /**
     * Title: canGoForward Description:
     *
     * @return
     * @see android.webkit.WebView#canGoForward()
     */
    @Override
    public boolean canGoForward() {
        LogHelper.i(TAG, "canGoForward");
        return super.canGoForward();

    }

    /**
     * Title: goForward Description:
     *
     * @see android.webkit.WebView#goForward()
     */
    @Override
    public void goForward() {
        LogHelper.i(TAG, "goForward");
        super.goForward();

    }

    /**
     * Title: canGoBackOrForward Description:
     *
     * @param steps
     * @return
     * @see android.webkit.WebView#canGoBackOrForward(int)
     */
    @Override
    public boolean canGoBackOrForward(int steps) {
        LogHelper.i(TAG, "canGoBackOrForward");
        return super.canGoBackOrForward(steps);

    }

    /**
     * Title: goBackOrForward Description:
     *
     * @param steps
     * @see android.webkit.WebView#goBackOrForward(int)
     */
    @Override
    public void goBackOrForward(int steps) {
        LogHelper.i(TAG, "goBackOrForward");
        super.goBackOrForward(steps);

    }

    /**
     * Title: isPrivateBrowsingEnabled Description:
     *
     * @return
     * @see android.webkit.WebView#isPrivateBrowsingEnabled()
     */
    @Override
    public boolean isPrivateBrowsingEnabled() {
        LogHelper.i(TAG, "isPrivateBrowsingEnabled");
        return super.isPrivateBrowsingEnabled();

    }

    /**
     * Title: pageUp Description:
     *
     * @param top
     * @return
     * @see android.webkit.WebView#pageUp(boolean)
     */
    @Override
    public boolean pageUp(boolean top) {
        LogHelper.i(TAG, "pageUp");
        return super.pageUp(top);

    }

    /**
     * Title: pageDown Description:
     *
     * @param bottom
     * @return
     * @see android.webkit.WebView#pageDown(boolean)
     */
    @Override
    public boolean pageDown(boolean bottom) {
        LogHelper.i(TAG, "pageDown");
        return super.pageDown(bottom);

    }

    /**
     * Title: clearView Description:
     *
     * @see android.webkit.WebView#clearView()
     */
    @Override
    public void clearView() {
        LogHelper.i(TAG, "clearView");
        super.clearView();

    }

    /**
     * Title: capturePicture Description:
     *
     * @return
     * @see android.webkit.WebView#capturePicture()
     */
    @Override
    public Picture capturePicture() {
        LogHelper.i(TAG, "capturePicture");
        return super.capturePicture();

    }

    /**
     * Title: getScale Description:
     *
     * @return
     * @deprecated
     * @see android.webkit.WebView#getScale()
     */
    @Override
    public float getScale() {
        LogHelper.i(TAG, "getScale");
        return super.getScale();

    }

    /**
     * Title: setInitialScale Description:
     *
     * @param scaleInPercent
     * @see android.webkit.WebView#setInitialScale(int)
     */
    @Override
    public void setInitialScale(int scaleInPercent) {
        LogHelper.i(TAG, "setInitialScale");
        super.setInitialScale(scaleInPercent);

    }

    /**
     * Title: invokeZoomPicker Description:
     *
     * @see android.webkit.WebView#invokeZoomPicker()
     */
    @Override
    public void invokeZoomPicker() {
        LogHelper.i(TAG, "invokeZoomPicker");
        super.invokeZoomPicker();

    }

    /**
     * Title: getHitTestResult Description:
     *
     * @return
     * @see android.webkit.WebView#getHitTestResult()
     */
    @Override
    public HitTestResult getHitTestResult() {
        LogHelper.i(TAG, "getHitTestResult");
        return super.getHitTestResult();

    }

    /**
     * Title: requestFocusNodeHref Description:
     *
     * @param hrefMsg
     * @see android.webkit.WebView#requestFocusNodeHref(android.os.Message)
     */
    @Override
    public void requestFocusNodeHref(Message hrefMsg) {
        LogHelper.i(TAG, "requestFocusNodeHref");
        super.requestFocusNodeHref(hrefMsg);

    }

    /**
     * Title: requestImageRef Description:
     *
     * @param msg
     * @see android.webkit.WebView#requestImageRef(android.os.Message)
     */
    @Override
    public void requestImageRef(Message msg) {
        LogHelper.i(TAG, "requestImageRef");
        super.requestImageRef(msg);

    }

    /**
     * Title: getUrl Description:
     *
     * @return
     * @see android.webkit.WebView#getUrl()
     */
    @Override
    public String getUrl() {
        LogHelper.i(TAG, "getUrl");
        return super.getUrl();

    }

    /**
     * Title: getOriginalUrl Description:
     *
     * @return
     * @see android.webkit.WebView#getOriginalUrl()
     */
    @Override
    public String getOriginalUrl() {
        LogHelper.i(TAG, "getOriginalUrl");
        return super.getOriginalUrl();

    }

    /**
     * Title: getTitle Description:
     *
     * @return
     * @see android.webkit.WebView#getTitle()
     */
    @Override
    public String getTitle() {
        LogHelper.i(TAG, "getTitle");
        return super.getTitle();

    }

    /**
     * Title: getFavicon Description:
     *
     * @return
     * @see android.webkit.WebView#getFavicon()
     */
    @Override
    public Bitmap getFavicon() {
        LogHelper.i(TAG, "getFavicon");
        return super.getFavicon();

    }

    /**
     * Title: getProgress Description:
     *
     * @return
     * @see android.webkit.WebView#getProgress()
     */
    @Override
    public int getProgress() {
        LogHelper.i(TAG, "getProgress");
        return super.getProgress();

    }

    /**
     * Title: getContentHeight Description:
     *
     * @return
     * @see android.webkit.WebView#getContentHeight()
     */
    @Override
    public int getContentHeight() {
        LogHelper.i(TAG, "getContentHeight");
        return super.getContentHeight();

    }

    /**
     * Title: pauseTimers Description:
     *
     * @see android.webkit.WebView#pauseTimers()
     */
    @Override
    public void pauseTimers() {
        LogHelper.i(TAG, "pauseTimers");
        super.pauseTimers();

    }

    /**
     * Title: resumeTimers Description:
     *
     * @see android.webkit.WebView#resumeTimers()
     */
    @Override
    public void resumeTimers() {
        LogHelper.i(TAG, "resumeTimers");
        super.resumeTimers();

    }

    /**
     * Title: onPause Description:
     *
     * @see android.webkit.WebView#onPause()
     */
    @Override
    public void onPause() {
        LogHelper.i(TAG, "onPause");
        super.onPause();

    }

    /**
     * Title: onResume Description:
     *
     * @see android.webkit.WebView#onResume()
     */
    @Override
    public void onResume() {
        LogHelper.i(TAG, "onResume");
        super.onResume();

    }

    /**
     * Title: freeMemory Description:
     *
     * @see android.webkit.WebView#freeMemory()
     */
    @Override
    public void freeMemory() {
        LogHelper.i(TAG, "freeMemory");
        super.freeMemory();

    }

    /**
     * Title: clearCache Description:
     *
     * @param includeDiskFiles
     * @see android.webkit.WebView#clearCache(boolean)
     */
    @Override
    public void clearCache(boolean includeDiskFiles) {
        LogHelper.i(TAG, "clearCache");
        super.clearCache(includeDiskFiles);

    }

    /**
     * Title: clearFormData Description:
     *
     * @see android.webkit.WebView#clearFormData()
     */
    @Override
    public void clearFormData() {
        LogHelper.i(TAG, "clearFormData");
        super.clearFormData();

    }

    /**
     * Title: clearHistory Description:
     *
     * @see android.webkit.WebView#clearHistory()
     */
    @Override
    public void clearHistory() {
        LogHelper.i(TAG, "clearHistory");
        super.clearHistory();

    }

    /**
     * Title: clearSslPreferences Description:
     *
     * @see android.webkit.WebView#clearSslPreferences()
     */
    @Override
    public void clearSslPreferences() {
        LogHelper.i(TAG, "clearSslPreferences");
        super.clearSslPreferences();

    }

    /**
     * Title: copyBackForwardList Description:
     *
     * @return
     * @see android.webkit.WebView#copyBackForwardList()
     */
    @Override
    public WebBackForwardList copyBackForwardList() {
        LogHelper.i(TAG, "WebBackForwardList");
        return super.copyBackForwardList();

    }

//    /**
//     * Title: setFindListener Description:
//     *
//     * @param listener
//     * @see android.webkit.WebView#setFindListener(android.webkit.WebView.FindListener)
//     */
//    @Override
//    public void setFindListener(FindListener listener) {
//        LogHelper.i(TAG, "setFindListener");
//        super.setFindListener(listener);
//
//    }

    /**
     * Title: findNext Description:
     *
     * @param forward
     * @see android.webkit.WebView#findNext(boolean)
     */
    @Override
    public void findNext(boolean forward) {
        LogHelper.i(TAG, "findNext");
        super.findNext(forward);

    }

    /**
     * Title: findAll Description:
     *
     * @param find
     * @return
     * @deprecated
     * @see android.webkit.WebView#findAll(java.lang.String)
     */
    @Override
    public int findAll(String find) {
        LogHelper.i(TAG, "findAll");
        return super.findAll(find);

    }

//    /**
//     * Title: findAllAsync Description:
//     *
//     * @param find
//     * @see android.webkit.WebView#findAllAsync(java.lang.String)
//     */
//    @Override
//    public void findAllAsync(String find) {
//        LogHelper.i(TAG, "findAllAsync");
//        super.findAllAsync(find);
//
//    }

    /**
     * Title: showFindDialog Description:
     *
     * @param text
     * @param showIme
     * @return
     * @see android.webkit.WebView#showFindDialog(java.lang.String, boolean)
     */
    @Override
    public boolean showFindDialog(String text, boolean showIme) {
        LogHelper.i(TAG, "showFindDialog");
        return super.showFindDialog(text, showIme);

    }

    /**
     * Title: clearMatches Description:
     *
     * @see android.webkit.WebView#clearMatches()
     */
    @Override
    public void clearMatches() {
        LogHelper.i(TAG, "clearMatches");
        super.clearMatches();

    }

    /**
     * Title: documentHasImages Description:
     *
     * @param response
     * @see android.webkit.WebView#documentHasImages(android.os.Message)
     */
    @Override
    public void documentHasImages(Message response) {
        LogHelper.i(TAG, "documentHasImages");
        super.documentHasImages(response);

    }

    /**
     * Title: setWebViewClient Description:
     *
     * @param client
     * @see android.webkit.WebView#setWebViewClient(android.webkit.WebViewClient)
     */
    @Override
    public void setWebViewClient(WebViewClient client) {
        LogHelper.i(TAG, "setWebViewClient");
        super.setWebViewClient(client);

    }

    /**
     * Title: setDownloadListener Description:
     *
     * @param listener
     * @see android.webkit.WebView#setDownloadListener(android.webkit.DownloadListener)
     */
    @Override
    public void setDownloadListener(DownloadListener listener) {
        LogHelper.i(TAG, "setDownloadListener");
        super.setDownloadListener(listener);

    }

    /**
     * Title: setWebChromeClient Description:
     *
     * @param client
     * @see android.webkit.WebView#setWebChromeClient(android.webkit.WebChromeClient)
     */
    @Override
    public void setWebChromeClient(WebChromeClient client) {
        LogHelper.i(TAG, "setWebChromeClient");
        super.setWebChromeClient(client);

    }

    /**
     * Title: setPictureListener Description:
     *
     * @param listener
     * @deprecated
     * @see android.webkit.WebView#setPictureListener(android.webkit.WebView.PictureListener)
     */
    @Override
    public void setPictureListener(PictureListener listener) {
        LogHelper.i(TAG, "setPictureListener");
        super.setPictureListener(listener);

    }

    /**
     * Title: addJavascriptInterface Description:
     *
     * @param object
     * @param name
     * @see android.webkit.WebView#addJavascriptInterface(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public void addJavascriptInterface(Object object, String name) {
        LogHelper.i(TAG, "addJavascriptInterface");
        super.addJavascriptInterface(object, name);

    }

    /**
     * Title: removeJavascriptInterface Description:
     *
     * @param name
     * @see android.webkit.WebView#removeJavascriptInterface(java.lang.String)
     */
    @Override
    public void removeJavascriptInterface(String name) {
        LogHelper.i(TAG, "removeJavascriptInterface");
        super.removeJavascriptInterface(name);

    }

    /**
     * Title: getSettings Description:
     *
     * @return
     * @see android.webkit.WebView#getSettings()
     */
    @Override
    public WebSettings getSettings() {
        LogHelper.i(TAG, "getSettings");
        return super.getSettings();

    }

    /**
     * Title: onChildViewAdded Description:
     *
     * @param parent
     * @param child
     * @deprecated
     * @see android.webkit.WebView#onChildViewAdded(android.view.View,
     *      android.view.View)
     */
    @Override
    public void onChildViewAdded(View parent, View child) {
        LogHelper.i(TAG, "onChildViewAdded");
        super.onChildViewAdded(parent, child);

    }

    /**
     * Title: onChildViewRemoved Description:
     *
     * @param p
     * @param child
     * @deprecated
     * @see android.webkit.WebView#onChildViewRemoved(android.view.View,
     *      android.view.View)
     */
    @Override
    public void onChildViewRemoved(View p, View child) {
        LogHelper.i(TAG, "onChildViewRemoved");
        super.onChildViewRemoved(p, child);

    }

    /**
     * Title: onGlobalFocusChanged Description:
     *
     * @param oldFocus
     * @param newFocus
     * @deprecated
     * @see android.webkit.WebView#onGlobalFocusChanged(android.view.View,
     *      android.view.View)
     */
    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        LogHelper.i(TAG, "onGlobalFocusChanged");
        super.onGlobalFocusChanged(oldFocus, newFocus);

    }

    /**
     * Title: setMapTrackballToArrowKeys Description:
     *
     * @param setMap
     * @deprecated
     * @see android.webkit.WebView#setMapTrackballToArrowKeys(boolean)
     */
    @Override
    public void setMapTrackballToArrowKeys(boolean setMap) {
        LogHelper.i(TAG, "setMapTrackballToArrowKeys");
        super.setMapTrackballToArrowKeys(setMap);

    }

    /**
     * Title: flingScroll Description:
     *
     * @param vx
     * @param vy
     * @see android.webkit.WebView#flingScroll(int, int)
     */
    @Override
    public void flingScroll(int vx, int vy) {
        LogHelper.i(TAG, "flingScroll");
        super.flingScroll(vx, vy);

    }

    /**
     * Title: canZoomIn Description:
     *
     * @return
     * @deprecated
     * @see android.webkit.WebView#canZoomIn()
     */
    @Override
    public boolean canZoomIn() {
        LogHelper.i(TAG, "canZoomIn");
        return super.canZoomIn();

    }

    /**
     * Title: canZoomOut Description:
     *
     * @return
     * @deprecated
     * @see android.webkit.WebView#canZoomOut()
     */
    @Override
    public boolean canZoomOut() {
        LogHelper.i(TAG, "canZoomOut");
        return super.canZoomOut();

    }

    /**
     * Title: zoomIn Description:
     *
     * @return
     * @see android.webkit.WebView#zoomIn()
     */
    @Override
    public boolean zoomIn() {
        LogHelper.i(TAG, "zoomIn");
        return super.zoomIn();

    }

    /**
     * Title: zoomOut Description:
     *
     * @return
     * @see android.webkit.WebView#zoomOut()
     */
    @Override
    public boolean zoomOut() {
        LogHelper.i(TAG, "zoomOut");
        return super.zoomOut();

    }

    /**
     * Title: onAttachedToWindow Description:
     *
     * @see android.webkit.WebView#onAttachedToWindow()
     */
    @Override
    protected void onAttachedToWindow() {
        LogHelper.i(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();

    }

    /**
     * Title: onDetachedFromWindow Description:
     *
     * @see android.webkit.WebView#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        LogHelper.i(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();

    }

    /**
     * Title: setLayoutParams Description:
     *
     * @param params
     * @see android.webkit.WebView#setLayoutParams(android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setLayoutParams(android.view.ViewGroup.LayoutParams params) {
        LogHelper.i(TAG, "setLayoutParams");
        super.setLayoutParams(params);

    }

    /**
     * Title: setOverScrollMode Description:
     *
     * @param mode
     * @see android.webkit.WebView#setOverScrollMode(int)
     */
    @Override
    public void setOverScrollMode(int mode) {
        LogHelper.i(TAG, "setOverScrollMode");
        super.setOverScrollMode(mode);

    }

    /**
     * Title: setScrollBarStyle Description:
     *
     * @param style
     * @see android.webkit.WebView#setScrollBarStyle(int)
     */
    @Override
    public void setScrollBarStyle(int style) {
        LogHelper.i(TAG, "setScrollBarStyle");
        super.setScrollBarStyle(style);

    }

    /**
     * Title: computeHorizontalScrollRange Description:
     *
     * @return
     * @see android.webkit.WebView#computeHorizontalScrollRange()
     */
    @Override
    protected int computeHorizontalScrollRange() {
        LogHelper.i(TAG, "computeHorizontalScrollRange");
        return super.computeHorizontalScrollRange();

    }

    /**
     * Title: computeHorizontalScrollOffset Description:
     *
     * @return
     * @see android.webkit.WebView#computeHorizontalScrollOffset()
     */
    @Override
    protected int computeHorizontalScrollOffset() {
        LogHelper.i(TAG, "computeHorizontalScrollOffset");
        return super.computeHorizontalScrollOffset();

    }

    /**
     * Title: computeVerticalScrollRange Description:
     *
     * @return
     * @see android.webkit.WebView#computeVerticalScrollRange()
     */
    @Override
    protected int computeVerticalScrollRange() {
        LogHelper.i(TAG, "computeVerticalScrollRange");
        return super.computeVerticalScrollRange();

    }

    /**
     * Title: computeVerticalScrollOffset Description:
     *
     * @return
     * @see android.webkit.WebView#computeVerticalScrollOffset()
     */
    @Override
    protected int computeVerticalScrollOffset() {
        LogHelper.i(TAG, "computeVerticalScrollOffset");
        return super.computeVerticalScrollOffset();

    }

    /**
     * Title: computeVerticalScrollExtent Description:
     *
     * @return
     * @see android.webkit.WebView#computeVerticalScrollExtent()
     */
    @Override
    protected int computeVerticalScrollExtent() {
        LogHelper.i(TAG, "computeVerticalScrollExtent");
        return super.computeVerticalScrollExtent();

    }

    /**
     * Title: computeScroll Description:
     *
     * @see android.webkit.WebView#computeScroll()
     */
    @Override
    public void computeScroll() {
        LogHelper.i(TAG, "computeScroll");
        super.computeScroll();

    }

    /**
     * Title: onHoverEvent Description:
     *
     * @param event
     * @return
     * @see android.webkit.WebView#onHoverEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onHoverEvent(MotionEvent event) {
        LogHelper.i(TAG, "onHoverEvent");
        return super.onHoverEvent(event);

    }

    /**
     * Title: onTouchEvent Description:
     *
     * @param event
     * @return
     * @see android.webkit.WebView#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogHelper.i(TAG, "onTouchEvent");
        return super.onTouchEvent(event);

    }

    /**
     * Title: onGenericMotionEvent Description:
     *
     * @param event
     * @return
     * @see android.webkit.WebView#onGenericMotionEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        LogHelper.i(TAG, "onGenericMotionEvent");
        return super.onGenericMotionEvent(event);

    }

    /**
     * Title: onTrackballEvent Description:
     *
     * @param event
     * @return
     * @see android.webkit.WebView#onTrackballEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        LogHelper.i(TAG, "onTrackballEvent");
        return super.onTrackballEvent(event);

    }

    /**
     * Title: onKeyDown Description:
     *
     * @param keyCode
     * @param event
     * @return
     * @see android.webkit.WebView#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogHelper.i(TAG, "onKeyDown");
        return super.onKeyDown(keyCode, event);

    }

    /**
     * Title: onKeyUp Description:
     *
     * @param keyCode
     * @param event
     * @return
     * @see android.webkit.WebView#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogHelper.i(TAG, "onKeyUp");
        return super.onKeyUp(keyCode, event);

    }

    /**
     * Title: onKeyMultiple Description:
     *
     * @param keyCode
     * @param repeatCount
     * @param event
     * @return
     * @see android.webkit.WebView#onKeyMultiple(int, int,
     *      android.view.KeyEvent)
     */
    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        LogHelper.i(TAG, "onKeyMultiple");
        return super.onKeyMultiple(keyCode, repeatCount, event);

    }

    /**
     * Title: shouldDelayChildPressedState Description:
     *
     * @return
     * @deprecated
     * @see android.webkit.WebView#shouldDelayChildPressedState()
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        LogHelper.i(TAG, "shouldDelayChildPressedState");
        return super.shouldDelayChildPressedState();

    }

    /**
     * Title: onInitializeAccessibilityNodeInfo Description:
     *
     * @param info
     * @see android.webkit.WebView#onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo)
     */
    @SuppressLint("NewApi")
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        LogHelper.i(TAG, "onInitializeAccessibilityNodeInfo");
        super.onInitializeAccessibilityNodeInfo(info);

    }

    /**
     * Title: onInitializeAccessibilityEvent Description:
     *
     * @param event
     * @see android.webkit.WebView#onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent)
     */
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        LogHelper.i(TAG, "onInitializeAccessibilityEvent");
        super.onInitializeAccessibilityEvent(event);

    }

//    /**
//     * Title: performAccessibilityAction Description:
//     *
//     * @param action
//     * @param arguments
//     * @return
//     * @see android.webkit.WebView#performAccessibilityAction(int,
//     *      android.os.Bundle)
//     */
//    @Override
//    public boolean performAccessibilityAction(int action, Bundle arguments) {
//        LogHelper.i(TAG, "performAccessibilityAction");
//        return super.performAccessibilityAction(action, arguments);
//
//    }

    /**
     * Title: onOverScrolled Description:
     *
     * @param scrollX
     * @param scrollY
     * @param clampedX
     * @param clampedY
     * @see android.webkit.WebView#onOverScrolled(int, int, boolean, boolean)
     */
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        LogHelper.i(TAG, "onOverScrolled");
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

    }

    /**
     * Title: onWindowVisibilityChanged Description:
     *
     * @param visibility
     * @see android.webkit.WebView#onWindowVisibilityChanged(int)
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        LogHelper.i(TAG, "onWindowVisibilityChanged");
        super.onWindowVisibilityChanged(visibility);

    }

    /**
     * Title: onDraw Description:
     *
     * @param canvas
     * @see android.webkit.WebView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        LogHelper.i(TAG, "onDraw");
        super.onDraw(canvas);

    }

    /**
     * Title: performLongClick Description:
     *
     * @return
     * @see android.webkit.WebView#performLongClick()
     */
    @Override
    public boolean performLongClick() {
        LogHelper.i(TAG, "performLongClick");
        return super.performLongClick();

    }

    /**
     * Title: onConfigurationChanged Description:
     *
     * @param newConfig
     * @see android.webkit.WebView#onConfigurationChanged(android.content.res.Configuration)
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        LogHelper.i(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);

    }

    /**
     * Title: onCreateInputConnection Description:
     *
     * @param outAttrs
     * @return
     * @see android.webkit.WebView#onCreateInputConnection(android.view.inputmethod.EditorInfo)
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        LogHelper.i(TAG, "onCreateInputConnection");
        return super.onCreateInputConnection(outAttrs);

    }

    /**
     * Title: onVisibilityChanged Description:
     *
     * @param changedView
     * @param visibility
     * @see android.webkit.WebView#onVisibilityChanged(android.view.View, int)
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        LogHelper.i(TAG, "onVisibilityChanged");
        super.onVisibilityChanged(changedView, visibility);

    }

    /**
     * Title: onWindowFocusChanged Description:
     *
     * @param hasWindowFocus
     * @see android.webkit.WebView#onWindowFocusChanged(boolean)
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        LogHelper.i(TAG, "onWindowFocusChanged");
        super.onWindowFocusChanged(hasWindowFocus);

    }

    /**
     * Title: onFocusChanged Description:
     *
     * @param focused
     * @param direction
     * @param previouslyFocusedRect
     * @see android.webkit.WebView#onFocusChanged(boolean, int,
     *      android.graphics.Rect)
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        LogHelper.i(TAG, "onFocusChanged");
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

    }

    /**
     * Title: onSizeChanged Description:
     *
     * @param w
     * @param h
     * @param ow
     * @param oh
     * @see android.webkit.WebView#onSizeChanged(int, int, int, int)
     */
    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        LogHelper.i(TAG, "onSizeChanged");
        super.onSizeChanged(w, h, ow, oh);

    }

    /**
     * Title: onScrollChanged Description:
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     * @see android.webkit.WebView#onScrollChanged(int, int, int, int)
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LogHelper.i(TAG, "onScrollChanged");
        super.onScrollChanged(l, t, oldl, oldt);

    }

    /**
     * Title: dispatchKeyEvent Description:
     *
     * @param event
     * @return
     * @see android.webkit.WebView#dispatchKeyEvent(android.view.KeyEvent)
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogHelper.i(TAG, "dispatchKeyEvent");
        return super.dispatchKeyEvent(event);

    }

    /**
     * Title: requestFocus Description:
     *
     * @param direction
     * @param previouslyFocusedRect
     * @return
     * @see android.webkit.WebView#requestFocus(int, android.graphics.Rect)
     */
    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        LogHelper.i(TAG, "requestFocus");
        return super.requestFocus(direction, previouslyFocusedRect);

    }

    /**
     * Title: onMeasure Description:
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * @deprecated
     * @see android.webkit.WebView#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogHelper.i(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * Title: requestChildRectangleOnScreen Description:
     *
     * @param child
     * @param rect
     * @param immediate
     * @return
     * @see android.webkit.WebView#requestChildRectangleOnScreen(android.view.View,
     *      android.graphics.Rect, boolean)
     */
    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        LogHelper.i(TAG, "requestChildRectangleOnScreen");
        return super.requestChildRectangleOnScreen(child, rect, immediate);

    }

    /**
     * Title: setBackgroundColor Description:
     *
     * @param color
     * @see android.webkit.WebView#setBackgroundColor(int)
     */
    @Override
    public void setBackgroundColor(int color) {
        LogHelper.i(TAG, "setBackgroundColor");
        super.setBackgroundColor(color);

    }

    /**
     * Title: setLayerType Description:
     *
     * @param layerType
     * @param paint
     * @see android.webkit.WebView#setLayerType(int, android.graphics.Paint)
     */
    @Override
    public void setLayerType(int layerType, Paint paint) {
        LogHelper.i(TAG, "setLayerType");
        super.setLayerType(layerType, paint);

    }

}
