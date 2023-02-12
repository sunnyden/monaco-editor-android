package com.sunnydeng.monacoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

import org.apache.commons.lang3.StringEscapeUtils;

public class MonacoEditor extends WebView {
    private OnCodeEditorReadyCallback mOnCodeEditorReadyCallback;
    private String language = "javascript";
    private boolean isLightMode = true;

    public MonacoEditor(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MonacoEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonacoEditor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MonacoEditor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public MonacoEditor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context){
        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(context))
                .build();
        this.setWebViewClient(new WebViewClientCompat() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.addJavascriptInterface(this, "callback");
        this.loadUrl("https://appassets.androidplatform.net/assets/monaco-editor/index.html");
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = new BaseInputConnection(this, true);
        outAttrs.imeOptions = outAttrs.imeOptions & ~EditorInfo.IME_FLAG_NAVIGATE_NEXT &
                ~EditorInfo.IME_FLAG_NAVIGATE_PREVIOUS;
        return inputConnection;
    }

    public void setLightMode(boolean isLightMode){
        this.post(() -> evaluateJavascript("window.setLightMode("+(isLightMode?"true":"false")+")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        }));
    }

    public void setOnReadyCallback(OnCodeEditorReadyCallback onCodeEditorReadyCallback){
        this.mOnCodeEditorReadyCallback = onCodeEditorReadyCallback;
    }

    public void setCode(String code){
        this.post(() -> evaluateJavascript("window.setCode(\""+ StringEscapeUtils.unescapeEcmaScript(code)+"\")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        }));
    }

    public String getCode() {
        return code;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isLightMode() {
        return isLightMode;
    }

    public void onLanguageChanged(String language){
        this.language = language;
    }

    @JavascriptInterface
    public void onReady(){
        if(this.mOnCodeEditorReadyCallback != null){
            this.mOnCodeEditorReadyCallback.onCodeEditorReady();
        }
    }

    private String code;
    @JavascriptInterface
    public void onCodeChanged(String code){
        this.code = code;
    }


    @JavascriptInterface
    public void onThemeChanged(boolean isLightMode){
        this.isLightMode = isLightMode;
    }
}
