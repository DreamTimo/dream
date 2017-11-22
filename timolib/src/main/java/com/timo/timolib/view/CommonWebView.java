package com.timo.timolib.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.timo.timolib.R;
import com.timo.timolib.utils.NetUtil;

/**
 * 2015年3月31日
 *
 * @author taozui taozui@58peilian.com
 * @Description: 通用WebView，带有loading、网络检查
 */
public class CommonWebView extends FrameLayout {
    private Context mContext;
    private WebView mWebView;
    private ProgressBar mLoadingView;
    private View mNetUnavailableView;
    private boolean mIsLocal;
    private ChromeClient mChromeClient;
    private CommonWebViewClient mCommonWebViewClient;
    private OnRetryConnectionClickListener mOnRetryConnectionClickListener;
    private OnPageLoadListener mOnPageLoadListener;
    private String mCurrentUrl;
    private boolean mIsShowLoading;


    /**
     * 2015年3月31日
     *
     * @author taozui taozui@58peilian.com
     * @Description: 无网络时的listener
     */
    public interface OnRetryConnectionClickListener {
        /**
         * 点击重试时调用
         */
        void reconnect();
    }

    /**
     * 使WebView不可滚动
     */
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(0, 0);
    }

    /**
     * 2015年3月31日
     *
     * @author taozui taozui@58peilian.com
     * @Description: Webview加载过程中的listener
     */
    public interface OnPageLoadListener {
        /**
         * 页面加载完成
         *
         * @param view webview
         * @param url  url
         */
        void onPageFinished(WebView view, String url);

        /**
         * 页面开始加载
         *
         * @param view
         * @param url
         * @param favicon
         */
        void onPageStarted(WebView view, String url, Bitmap favicon);

        /**
         * 页面重定向
         *
         * @param view
         * @param url
         * @return 是否继续执行WebViewClient中的逻辑
         */
        boolean shouldOverrideUrlLoading(WebView view, String url);
    }

    public CommonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context, attrs);
    }

    public CommonWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView(context, attrs);
    }

    public CommonWebView(Context context) {
        super(context);
        mContext = context;
        initView(context, null);
    }

    /**
     * 初始化view
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        // 默认显示loading
        setIsShowLoading(true);

        mWebView = new WebView(context);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(lp);

        mLoadingView = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progressbar_webview, null);
        LayoutParams lpLoading = new LayoutParams(
                LayoutParams.MATCH_PARENT, 5);
        lpLoading.gravity = Gravity.TOP | Gravity.LEFT;
        mLoadingView.setLayoutParams(lpLoading);

        mNetUnavailableView = LayoutInflater.from(mContext).inflate(R.layout.layout_net_error, null);//new TextView(mContext);
        LayoutParams lpImage = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpImage.gravity = Gravity.CENTER;
        mNetUnavailableView.setLayoutParams(lpImage);

        mNetUnavailableView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mOnRetryConnectionClickListener != null) {
                    mOnRetryConnectionClickListener.reconnect();
                }
                load(mCurrentUrl);
            }
        });
        this.addView(mWebView);
        this.addView(mNetUnavailableView);
        this.addView(mLoadingView);
        mLoadingView.setVisibility(View.GONE);
        mNetUnavailableView.setVisibility(View.GONE);
        initWebview();
    }

    /**
     * 页面重新加载
     */
    public void reload() {
        mWebView.reload();
    }

    /**
     * 页面向前
     */
    public void goForward() {
        mWebView.goForward();
    }

    /**
     * 是否能页面向前
     *
     * @return
     */
    public boolean canGoForward() {
        return mWebView.canGoForward();
    }

    /**
     * 是否能页面向后
     *
     * @return
     */
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    /**
     * 页面向后
     */
    public void goBack() {
        mWebView.goBack();
    }

    /**
     * 页面加载，此处会检测网络状态
     *
     * @param url
     */
    public void load(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mCurrentUrl = url;
        if (mIsLocal || checkNet()) {
            mWebView.loadUrl(url);
        }
    }

    public void callJsFunction(String func) {
        mWebView.loadUrl(func);
    }

    public void postUrl(String url, byte[] postData) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mCurrentUrl = url;
        if (mIsLocal || checkNet()) {
            mWebView.postUrl(url, postData);
        }
    }

    /**
     * 页面向后
     *
     * @return
     */
    public boolean back() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    /**
     * 初始化webview
     */
    @SuppressLint("NewApi")
    private void initWebview() {
        mChromeClient = new ChromeClient();
        mCommonWebViewClient = new CommonWebViewClient();
        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mCommonWebViewClient);

        CookieSyncManager.createInstance(mContext);
        CookieSyncManager.getInstance().startSync();
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= 21) {//Added in API level 21
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        mWebView.getSettings().setLoadWithOverviewMode(true);
        /** 58 **/
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setGeolocationDatabasePath(
                mContext.getFilesDir().getPath());
        /** 58 **/
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.setHorizontalScrollBarEnabled(true);
        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.getSettings().setAllowContentAccess(true);
        }
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {

            }
        });
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       Callback callback) {
            callback.invoke(origin, true, false);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            mLoadingView.setProgress(progress);
            if (IsShowLoading() && progress < 100) {
                mLoadingView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onShowCustomView(View paramView, CustomViewCallback callback) {
            super.onShowCustomView(paramView, callback);
            if ((paramView instanceof FrameLayout)) {
                FrameLayout localFrameLayout = (FrameLayout) paramView;
                if ((localFrameLayout.getFocusedChild() instanceof VideoView)) {
                    VideoView localVideoView = (VideoView) localFrameLayout
                            .getFocusedChild();
                    localFrameLayout.removeView(localVideoView);
                    localVideoView.start();
                }
            }
        }
    }

    private class CommonWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (mOnPageLoadListener != null) {
                if (mOnPageLoadListener.shouldOverrideUrlLoading(view, url)) {
                    return true;
                }
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //非本地html加载才需要做网络检查
            if (!isLocal()) {
                if (mIsShowLoading) {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
                checkNet();
            }
            if (mOnPageLoadListener != null) {
                mOnPageLoadListener.onPageStarted(view, url, favicon);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mLoadingView.setVisibility(View.GONE);
            if (mOnPageLoadListener != null) {
                mOnPageLoadListener.onPageFinished(view, url);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }

    //页面pause时调用
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onPause() {
        if (!NetUtil.getInstance().checkNet(mContext) && !mIsLocal) {
            mWebView.onPause();
        }
    }

    //页面resume时调用
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onResume() {
        if (!NetUtil.getInstance().checkNet(mContext) && !mIsLocal) {
            mWebView.onResume();
        }
    }

    //网络检查
    private boolean checkNet() {
        if (!NetUtil.getInstance().checkNet(mContext)) {
            mNetUnavailableView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.GONE);
            return false;
        } else {
            mNetUnavailableView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            return true;
        }
    }

    /**
     * 控制Html布局方式
     *
     * @param l NORMAL：正常显示，没有渲染变化。
     *          SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中。
     *          NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。
     */
    public void setLayoutAlgorithm(WebSettings.LayoutAlgorithm l) {
        if (mWebView == null) {
            return;
        }
        mWebView.getSettings().setLayoutAlgorithm(l);
    }

    public void loadDataWithBaseURL(String baseUrl, String data,
                                    String mimeType, String encoding, String failUrl) {
        if (mWebView == null) {
            return;
        }
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
    }

    public void loadData(String data, String mimeType, String encoding) {
        if (mWebView == null) {
            return;
        }
        mWebView.loadData(data, mimeType, encoding);
    }

    /**
     * 是否加载本地html
     *
     * @return
     */
    public boolean isLocal() {
        return mIsLocal;
    }

    /**
     * 设置是否加载本地html
     *
     * @param mIsLocal
     */
    public void setIsLocal(boolean mIsLocal) {
        this.mIsLocal = mIsLocal;
    }

    /**
     * 设置重新加载的listener
     *
     * @param listener
     */
    public void setOnRetryConnectionClickListener(
            OnRetryConnectionClickListener listener) {
        mOnRetryConnectionClickListener = listener;
    }

    /**
     * 设置加载listener
     *
     * @param listener
     */
    public void setOnPageLoadListener(OnPageLoadListener listener) {
        mOnPageLoadListener = listener;
    }

    /**
     * 获取webview
     *
     * @return
     */
    public WebView getWebView() {
        return mWebView;
    }

    public void setWebViewClient(WebViewClient client) {
        mWebView.setWebViewClient(client);
    }

    /**
     * 是否显示loading
     *
     * @return
     */
    public boolean IsShowLoading() {
        return mIsShowLoading;
    }

    /**
     * 设置是否显示loading
     *
     * @param isShowLoading
     */
    public void setIsShowLoading(boolean isShowLoading) {
        this.mIsShowLoading = isShowLoading;
    }

    public void finish() {
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }
}
