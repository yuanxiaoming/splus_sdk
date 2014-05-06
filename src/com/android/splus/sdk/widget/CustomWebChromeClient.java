
package com.android.splus.sdk.widget;

import com.android.splus.sdk.utils.log.LogHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * @ClassName: CustomWebChromeClient
 * @author xiaoming.yuan
 * @date 2013-11-5 下午1:03:18
 */
@SuppressWarnings("unused")
public class CustomWebChromeClient extends WebChromeClient {
    private Context mContext;

    private WebChromeClient mWrappedClient;

    private OnWebViewRequestListener mOnWebViewRequestListener;

    public CustomWebChromeClient(Context context, WebChromeClient webChromeClient) {
        this.mContext = context;
        this.mWrappedClient = webChromeClient;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        LogHelper.i("getDefaultVideoPoster", "getDefaultVideoPoster");
        return mWrappedClient.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        LogHelper.i("getVideoLoadingProgressView", "getVideoLoadingProgressView");
        return mWrappedClient.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        LogHelper.i("getVisitedHistory", "callback");
        mWrappedClient.getVisitedHistory(callback);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        LogHelper.i("onConsoleMessage", consoleMessage.message() + "");
        return mWrappedClient.onConsoleMessage(consoleMessage);
    }

    /**
     * 设置Request时的回调方法
     * 
     * @param onWebViewRequestListener WebView中对象GET方式请求网络时执行的回调监听对象
     */
    public void setOnWebViewRequestListener(OnWebViewRequestListener onWebViewRequestListener) {
        LogHelper.i("setOnWebViewRequestListener", "setOnWebViewRequestListener");
        this.mOnWebViewRequestListener = onWebViewRequestListener;
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        LogHelper.i("onConsoleMessage", message);
        mWrappedClient.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        LogHelper.i("onExceededDatabaseQuota", url);
        mWrappedClient.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        LogHelper.i("onGeolocationPermissionsHidePrompt", "onGeolocationPermissionsHidePrompt");
        mWrappedClient.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        LogHelper.i("onGeolocationPermissionsShowPrompt", origin);
        mWrappedClient.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onHideCustomView() {
        LogHelper.i("onHideCustomView", "onHideCustomView");
        mWrappedClient.onHideCustomView();
    }

    @Override
    public boolean onJsTimeout() {
        LogHelper.i("onJsTimeout", "onJsTimeout");
        return mWrappedClient.onJsTimeout();
    }

    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        LogHelper.i("onReachedMaxAppCacheSize", "onReachedMaxAppCacheSize");
        mWrappedClient.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        LogHelper.i("onReceivedTouchIconUrl", url);
        mWrappedClient.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        LogHelper.i("onShowCustomView", "onShowCustomView");
        mWrappedClient.onShowCustomView(view, callback);
    }

    @Override
    public void onCloseWindow(WebView window) {
        LogHelper.i("onCloseWindow", "close");

        mWrappedClient.onCloseWindow(window);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
        // WebView newView = new WebView(context);
        // WebView.WebViewTransport transport = (WebView.WebViewTransport)
        // resultMsg.obj;
        // transport.setWebView(newView);
        // newView.loadUrl(transport.toString());
        // AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // builder.setView(newView);
        // Dialog webDialog = builder.create();
        // webDialog.setCanceledOnTouchOutside(true);
        // webDialog.show();
        LogHelper.i("onCreateWindow", "onCreateWindow");
        return super.onCreateWindow(view, dialog, userGesture, resultMsg);
    }

    /**
     * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        LogHelper.i("onJsAlert", "url==" + url + "message=" + message);

        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("提示").setMessage(message).setPositiveButton("确定", null);
        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
        builder.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                LogHelper.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
                return true;
            }
        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
        return true;
        // return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        LogHelper.i("onJsAlert", "url==" + url + "message=" + message);
        return super.onJsBeforeUnload(view, url, message, result);
    }

    /**
     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        LogHelper.i("onJsAlert", "url==" + url + "message=" + message);
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("提示").setMessage(message).setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        }).setNeutralButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                LogHelper.i("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                return true;
            }
        });
        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
        // return super.onJsConfirm(view, url, message, result);
    }

    /**
     * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        LogHelper.i("onJsPrompt", "url==" + url + "message=" + message);
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("提示").setMessage(message);
        final EditText et = new EditText(view.getContext());
        et.setSingleLine();
        et.setText(defaultValue);
        builder.setView(et).setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm(et.getText().toString());
            }

        }).setNeutralButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                LogHelper.v("onJsPrompt", "keyCode==" + keyCode + "event=" + event);
                return true;
            }
        });

        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
        // return super.onJsPrompt(view, url, message, defaultValue,
        // result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        LogHelper.i("onProgressChanged", "onProgressChanged");
        mWrappedClient.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        LogHelper.i("onReceivedIcon", "onReceivedIcon");
        mWrappedClient.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogHelper.i("onReceivedTitle", "onReceivedTitle");
        mWrappedClient.onReceivedTitle(view, title);
    }

    @Override
    public void onRequestFocus(WebView view) {
        LogHelper.i("onRequestFocus", "onRequestFocus");
        mWrappedClient.onRequestFocus(view);
    }

    // For Android > 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        LogHelper.i("openFileChooser>3.0", "openFileChooser>3.0");
        ((CustomWebChromeClient) mWrappedClient).openFileChooser(uploadMsg, acceptType);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        LogHelper.i("openFileChooser<3.0", "openFileChooser<3.0");
        ((CustomWebChromeClient) mWrappedClient).openFileChooser(uploadMsg);
    }

    /**
     * WebView中对象GET方式请求网络时执行的回调方法
     * 
     * @ClassName: OnWebViewRequestListener
     * @author xiaoming.yuan
     * @date 2013-11-5 下午2:55:30
     */
    public interface OnWebViewRequestListener {
        public void request(String data);
    }
}
