package org.santech.webviewauto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.santech.webviewauto.fragments.BrowserFragment;
import org.santech.webviewauto.keyboard.KeyboardHandler;
import org.santech.webviewauto.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class WebViewAutoActivity extends AppCompatActivity {

    private enum BrowserInputMode {
        URL_INPUT_MODE, CONTENT_INPUT_MODE
    }

    private static final String CURRENT_FRAGMENT_KEY = "app_current_fragment";
    private String mCurrentFragmentTag;

    public String homeURL = "https://duckduckgo.com";
    public String currentURL = homeURL;
    public BrowserInputMode inputMode = BrowserInputMode.URL_INPUT_MODE;
    public String originalAgentString = "";
    public String currentBrowserMode = "MOBILE";

    public List<String> urlHistory = new ArrayList<>();

    public KeyboardHandler handler = new KeyboardHandler();

    @Override
    protected void onCreate(Bundle bundle) {
        ActivityAccessHelper.getInstance().activity = this;

        super.onCreate(bundle);
        setContentView(R.layout.activity_car_main);

        if (bundle != null && bundle.containsKey(CURRENT_FRAGMENT_KEY)) {
            mCurrentFragmentTag = bundle.getString(CURRENT_FRAGMENT_KEY);
        }

        setupBrowserFragment();
        setupUI();
    }

    private void setupBrowserFragment() {
        BrowserFragment browserFragment = new BrowserFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, browserFragment, BrowserFragment.TAG)
                .commit();
        mCurrentFragmentTag = BrowserFragment.TAG;
    }

    private void setupUI() {
        WebView webView = findViewById(R.id.webview_component);
        setupWebView(webView);

        final EditText urlInput = findViewById(R.id.browser_url_input);
        urlInput.setText(currentURL);

        setupButtons();
        setupKeyboard();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webView.loadUrl(currentURL);
        urlHistory.add(currentURL);
    }

    private void setupButtons() {
        Button menuButton = findViewById(R.id.browser_url_menu);
        menuButton.setOnClickListener(v -> openMenu());

        Button keyboardToggle = findViewById(R.id.browser_url_keyboard_toggle);
        keyboardToggle.setOnClickListener(v -> toggleKeyboard());

        Button backspace = findViewById(R.id.browser_url_backspace);
        backspace.setOnClickListener(v -> handleBackspace());

        Button okButton = findViewById(R.id.browser_url_ok);
        okButton.setOnClickListener(v -> handleOkButton());

        Button submitButton = findViewById(R.id.browser_url_submit);
        submitButton.setOnClickListener(v -> handleSubmit());
    }

    private void setupKeyboard() {
        final LinearLayout keyboard = findViewById(R.id.browser_keyboard);
        keyboard.removeAllViews();
        keyboard.addView(handler.createKeyboardView(this, this));
    }

    private void openMenu() {
        // Implement menu opening logic
    }

    private void toggleKeyboard() {
        EditText urlInput = findViewById(R.id.browser_url_input);
        LinearLayout keyboard = findViewById(R.id.browser_keyboard);
        WebView webView = findViewById(R.id.webview_component);

        if (urlInput.hasFocus() && keyboard.getVisibility() == View.GONE) {
            inputMode = BrowserInputMode.URL_INPUT_MODE;
            showKeyboard();
        } else if (!urlInput.hasFocus() && keyboard.getVisibility() == View.GONE) {
            inputMode = BrowserInputMode.CONTENT_INPUT_MODE;
            urlInput.setText("");
            showKeyboard();
        } else if (keyboard.getVisibility() == View.VISIBLE) {
            hideKeyboard();
        }
    }

    private void handleBackspace() {
        EditText urlInput = findViewById(R.id.browser_url_input);
        String oldContent = urlInput.getText().toString();
        int selStart = urlInput.getSelectionStart();
        if (oldContent.length() != 0 && selStart > 0) {
            urlInput.getText().delete(selStart - 1, selStart);
        }
    }

    private void handleOkButton() {
        EditText urlInput = findViewById(R.id.browser_url_input);
        WebView webView = findViewById(R.id.webview_component);

        if (inputMode == BrowserInputMode.URL_INPUT_MODE) {
            String newURL = urlInput.getText().toString();
            changeURL(newURL);
        } else if (inputMode == BrowserInputMode.CONTENT_INPUT_MODE) {
            webView.evaluateJavascript(
                    "document.activeElement.value = '" + urlInput.getText().toString() + "';",
                    null
            );
            urlInput.setText(currentURL);
        }
        hideKeyboard();
    }

    private void handleSubmit() {
        WebView webView = findViewById(R.id.webview_component);
        webView.evaluateJavascript("document.activeElement.form.submit();", null);
    }

    public void showKeyboard() {
        WebView webView = findViewById(R.id.webview_component);
        LinearLayout keyboard = findViewById(R.id.browser_keyboard);
        webView.setVisibility(View.GONE);
        keyboard.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard() {
        WebView webView = findViewById(R.id.webview_component);
        LinearLayout keyboard = findViewById(R.id.browser_keyboard);
        webView.setVisibility(View.VISIBLE);
        keyboard.setVisibility(View.GONE);
    }

    public void keyInputCallback(String enteredKey) {
        EditText urlInput = findViewById(R.id.browser_url_input);
        urlInput.getText().insert(urlInput.getSelectionStart(), enteredKey);
    }

    public void changeURL(String url) {
        EditText urlInput = findViewById(R.id.browser_url_input);
        WebView webView = findViewById(R.id.webview_component);
        urlInput.setText(url);

        List<String> desktopModeURLs = new ArrayList<>();
        if (desktopModeURLs.contains(url) && !currentBrowserMode.equals("DESKTOP")) {
            setDesktopMode(webView);
            currentBrowserMode = "DESKTOP";
        } else {
            setMobileMode(webView);
            currentBrowserMode = "MOBILE";
        }

        webView.loadUrl(url);
        currentURL = url;
        if (!urlHistory.isEmpty() && !urlHistory.get(urlHistory.size() - 1).equals(url)) {
            urlHistory.add(url);
        }
    }

    private void setDesktopMode(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        originalAgentString = webSettings.getUserAgentString();
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    private void setMobileMode(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(originalAgentString);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(false);
    }
}
