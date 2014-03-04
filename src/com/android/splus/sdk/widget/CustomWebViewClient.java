
package com.android.splus.sdk.widget;

import com.android.splus.sdk.utils.log.LogHelper;
import com.android.splus.sdk.utils.progressDialog.ProgressDialogUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @ClassName: CustomWebViewClient
 * @author xiaoming.yuan
 * @date 2013-11-5 下午1:02:45
 */
@SuppressLint("NewApi")
public class CustomWebViewClient extends WebViewClient {
    @SuppressWarnings("unused")
    private Context mContext;

    private ProgressDialog mProgressDialog;

    private final static String TAG = "CustomWebViewClient";

    public CustomWebViewClient(Context context) {
        this.mContext = context;
        mProgressDialog = ProgressDialogUtil.showProgress(context, "", "", true, false);
        mProgressDialog.dismiss();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogHelper.i(TAG + ":shouldOverrideUrlLoading:", url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        LogHelper.i(TAG + ":doUpdateVisitedHistory:", url + ":" + isReload);
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        LogHelper.i(TAG + ":onFormResubmission:", "dontResend" + dontResend.getData().toString()
                + "resend" + resend.getData().toString());
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        LogHelper.i(TAG + ":onLoadResource:", url);
        super.onLoadResource(view, url);
        // 加载时 如果url被拦截 一般为ip地址
        if (url.startsWith("http://")) {
            int endIndex = url.indexOf("/", 7);
            if (endIndex >= 14) {
                String ip = url.substring(7, endIndex);
                boolean isMatch = ip.matches("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                        + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                        + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                        + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
                if (isMatch) {
                    LogHelper.i(TAG, "网络需要登陆");
                }
            }
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        LogHelper.i(TAG + ":onReceivedError:", description);
        super.onReceivedError(view, errorCode, description, failingUrl);
        Toast.makeText(mContext, "请重新连接您的网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host,
            String realm) {
        LogHelper.i(TAG + ":onReceivedHttpAuthRequest:", host);
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        LogHelper.i(TAG + ":onReceivedLoginRequest:", "");
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        LogHelper.i(TAG + ":onReceivedHttpAuthRequest:", "");
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        LogHelper.i(TAG + ":onScaleChanged:", oldScale + "");
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        LogHelper.i(TAG + ":onTooManyRedirects:", cancelMsg.getData().toString());
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        LogHelper.i(TAG + ":onUnhandledKeyEvent:", "KeyCode" + event.getKeyCode() + "Action"
                + event.getAction());
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        LogHelper.i(TAG + ":shouldInterceptRequest:", url);
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        LogHelper.i(TAG + ":shouldOverrideKeyEvent:", "KeyCode" + event.getKeyCode() + "Action"
                + event.getAction());
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
        // 清理内存
        System.gc();
        LogHelper.i(TAG + ":onPageFinished:", url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
        // 清理内存
        System.gc();
        LogHelper.i(TAG + ":onPageStarted:", url);
        super.onPageStarted(view, url, favicon);
    }
}
