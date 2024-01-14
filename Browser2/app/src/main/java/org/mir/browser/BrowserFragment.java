package org.mir.browser;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.mir.browser.Bookmarks.BookmarkActivity;
import org.mir.browser.Bookmarks.BookmarkModel;
import org.mir.browser.Downloads.DownloadActivity;
import org.mir.browser.Downloads.DownloadModel;
import org.mir.browser.History.HistoryActivity;
import org.mir.browser.History.HistoryModel;
import org.mir.browser.Sync.SyncActivity;
import org.mir.browser.Tab.TabAdapter;
import org.mir.browser.Tab.TabDataModel;
import org.mir.browser.Tab.TabSpaceDecoration;
import org.mir.browser.WebViewPager.WebViewAdapter;
import org.mir.browser.WebViewPager.WebViewFragment;

import java.util.ArrayList;

public class BrowserFragment extends Fragment implements View.OnClickListener,SharedPreferences.OnSharedPreferenceChangeListener
{
    private int tabPosition = 0;
    private WebView webView;
    private WebSettings webSettings;
    private boolean showToast = true;
    private boolean isJavaScript = true;
    private boolean isDarkMode = false;
    private String downloadFileName;
    private String downloadFileUrl;
    private String userId = "Safa";

    // UI Controller
    private TextInputEditText urlEditText;
    private ImageButton bookmarkButton, addTabButton, backButton, forwardButton, homeButton, refreshButton;
    private Toolbar toolbar;
    private RecyclerView tabRecyclerView;
    private ViewPager2 webViewPager;

    // Adapter
    private WebViewAdapter webViewAdapter;
    private TabAdapter tabAdapter;

    // LayoutManager
    private LinearLayoutManager linearLayoutManager;

    // Decorator
    private TabSpaceDecoration tabSpaceDecoration;

    // Arraylist
    private ArrayList<TabDataModel> tabList;
    private ArrayList<WebViewFragment> webViewList;
    private final ArrayList<String>[] urlList = new ArrayList[10];
    private ArrayList<String>[] tabTitleList = new ArrayList[10];

    // Listener
    private TabAdapter.TabAdapterListener tabAdapterListener;
    private WebViewFragment.WebViewListener webViewListener;

    private String search_query = "http://www.google.com/search?q=";
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private HistoryModel historyModel;
    private DownloadModel downloadModel;
    private BookmarkModel bookmarkModel;


    public BrowserFragment()
    {

    }

    public static BrowserFragment newInstance()
    {
        BrowserFragment fragment = new BrowserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);

        // Initialize
        initialize(view);

        // Arraylists
        tabList.add(new TabDataModel("Home"));
        webViewList.add(WebViewFragment.newInstance(webViewListener));

        // Tab RecyclerView
        tabRecyclerView.setLayoutManager(linearLayoutManager);
        tabRecyclerView.addItemDecoration(tabSpaceDecoration);
        tabRecyclerView.setAdapter(tabAdapter);

        // WebView Viewpager
        webViewPager.setAdapter(webViewAdapter);
        webViewPager.setOffscreenPageLimit(5);

        // Listener
        listener();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return view;
    }

    private void initialize(View view)
    {
        for (int i = 0; i < 10; i++)
        {
            urlList[i] = new ArrayList<String>();
            urlList[i].add("");
        }

        for (int i = 0; i < 10; i++)
        {
            tabTitleList[i] = new ArrayList<String>();
            tabTitleList[i].add("Home");
        }

        urlEditText = view.findViewById(R.id.url_edit_text);
        bookmarkButton = view.findViewById(R.id.bookmark_btn);
        addTabButton = view.findViewById(R.id.add_tab_btn);
        backButton = view.findViewById(R.id.back_btn);
        forwardButton = view.findViewById(R.id.forward_btn);
        homeButton = view.findViewById(R.id.home_btn);
        refreshButton = view.findViewById(R.id.refresh_btn);
        tabRecyclerView = view.findViewById(R.id.tab_recycler_view);
        webViewPager = view.findViewById(R.id.webview_pager);
        toolbar = view.findViewById(R.id.toolbar);

        tabList = new ArrayList<>();
        webViewList = new ArrayList<>();


        webViewListener = new WebViewFragment.WebViewListener()
        {
            @Override
            public void onLoadingFinished(WebView view1, String url) {
                onPageLoad(view1, url);
            }
        };

        webViewAdapter = new WebViewAdapter(getActivity().getSupportFragmentManager(), getLifecycle(), webViewList);

        tabAdapterListener = new TabAdapter.TabAdapterListener()
        {
            @Override
            public void onTitleClicked(int position) {
                onTabTitleClicked(position);
            }

            @Override
            public void onCloseClicked(int position)
            {
                tabAdapter.removeTab(position);
                webViewAdapter.removeFragment(position);
                String temp = urlList[0].get(0);
                if(temp.equals("file:///android_asset/links.html"))
                {
                    urlEditText.setText("");
                }
                else
                {
                    urlEditText.setText(urlList[0].get(0));
                }
            }
        };

        tabAdapter = new TabAdapter(tabList, tabAdapterListener);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tabSpaceDecoration = new TabSpaceDecoration(20);

        toolbar.inflateMenu(R.menu.preference_menu);
        Drawable preference_icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_table_rows_24);
        toolbar.setOverflowIcon(preference_icon);

        rootNode = FirebaseDatabase.getInstance("https://browser-c0dd5-default-rtdb.asia-southeast1.firebasedatabase.app");
    }

    private void listener()
    {
        addTabButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        forwardButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        urlEditText.setOnFocusChangeListener((view1, b) ->
        {
            if(b)
            {
                onInput();
            }
        });
        toolbar.setOnMenuItemClickListener(item ->
        {
            onOptionClick(item);
            return true;
        });
        bookmarkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

        int id = view.getId();

        if (id == R.id.bookmark_btn)
        {
            onBookmark();
        }

        if(id == R.id.add_tab_btn)
        {
            addTab();
        }

        else if(id == R.id.back_btn)
        {
            if (webView.canGoBack())
            {
                webView.goBack();
            }
        }

        else if(id == R.id.forward_btn)
        {
            if (webView.canGoForward())
            {
                webView.goForward();
            }
        }

        else if(id == R.id.home_btn)
        {
            webView.loadUrl("file:///android_asset/links.html");
        }
        else if(id == R.id.refresh_btn)
        {
            webView.reload();
        }
    }

    private void onOptionClick(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.new_tab)
        {
            addTab();
        }
        else if (itemId == R.id.new_private_tab)
        {
            if(!isJavaScript)
            {
                Toast.makeText(getContext(), "Javascript", Toast.LENGTH_SHORT).show();
            }
        }
        else if(itemId == R.id.sync)
        {
            Intent syncActivity = new Intent(getContext(), SyncActivity.class);
            startActivity(syncActivity);
        }
        else if (itemId == R.id.history_item)
        {
            Intent historyIntent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(historyIntent);
        }
        else if (itemId == R.id.bookmark_item)
        {
            Intent bookmarkIntent = new Intent(getActivity(), BookmarkActivity.class);
            startActivity(bookmarkIntent);
        }
        else if (itemId == R.id.downloads_item)
        {
            Intent downloadIntent = new Intent(getActivity(), DownloadActivity.class);
            startActivity(downloadIntent);
        }
        else if (itemId == R.id.desktop_site)
        {
            if (!item.isChecked())
            {
                item.setChecked(true);
                setDesktopMode(true);
            } else {
                item.setChecked(false);
                setDesktopMode(false);
            }
        }

        else if (itemId == R.id.settings)
        {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
        }

        else if(itemId == R.id.about)
        {
            Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
            startActivity(aboutIntent);
        }

        else if (itemId == R.id.exit)
        {
            getActivity().finish();
        }
    }

    private void onInput()
    {
        webView = webViewAdapter.focusedWebView(tabPosition).getWebView();
        urlEditText.setOnEditorActionListener((textView, i, keyEvent) ->
        {
            boolean handled = false;
            if(i == EditorInfo.IME_ACTION_DONE)
            {
                InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                String url = urlEditText.getText().toString();
                boolean isUrl = Patterns.WEB_URL.matcher(url).matches();

                if(isUrl)
                {
                    webView.loadUrl("http://www." + url);
                }
                else
                {
                    webView.loadUrl(search_query + url);
                }

                handled = true;
            }
            return handled;
        });
    }

    private void onPageLoad(WebView view, String url)
    {
        webView = view;
        // Changing Tab name
        // Full website title
        String fullTitle = view.getTitle();
        // Only first word from title
        String title = getFirstWord(fullTitle);
        // replacing first value
        tabTitleList[tabPosition].remove(0);
        tabTitleList[tabPosition].add(title);
        // Updating tab title with first value of tabTitleList
        tabList.set(tabPosition, new TabDataModel(tabTitleList[tabPosition].get(0)));
        // Notifying adapter
        tabAdapter.notifyItemChanged(tabPosition);

        // Replacing first value
        urlList[tabPosition].remove(0);
        urlList[tabPosition].add(url);
        // Setting url
        if(webViewPager.getCurrentItem() == tabPosition)
        {
            if(url.equals("file:///android_asset/links.html"))
            {
                urlEditText.setText("");
            }
            else
            {
                urlEditText.setText(url);
            }
        }
        // Clearing focus from urlEdit
        urlEditText.clearFocus();

        historyModel = new HistoryModel(view.getTitle(), view.getUrl());

        reference = rootNode.getReference("Users/" + userId + "/History");
        String key = reference.push().getKey();
        reference.child(key).setValue(historyModel);;

        //setAll();

        onDownload(view);
    }
    private void setAll()
    {
        UserDataModel userDataModel = new UserDataModel(historyModel, bookmarkModel, downloadModel);
        DatabaseReference reference = rootNode.getReference("Users");
        reference.child(userId).setValue(userDataModel);
    }

    private void onTabTitleClicked(int position)
    {
        tabPosition = position;
        webViewPager.setCurrentItem(position, true);
        String t = urlList[position].get(0);
        if(t.equals("file:///android_asset/links.html"))
        {
            urlEditText.setText("");
        }
        else
        {
            urlEditText.setText(t);
        }
        webView = webViewAdapter.focusedWebView(position).getWebView();
    }

    private void addTab()
    {
        tabAdapter.addTab(new TabDataModel());
        webViewAdapter.addFragment(WebViewFragment.newInstance(webViewListener));
        tabPosition ++;
    }

    public void onBookmark()
    {
        Toast.makeText(getContext(), "Bookmark", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.bookmark_popup, null);
        TextInputEditText nameEditText = popupView.findViewById(R.id.name_edit);
        TextInputEditText descEditText = popupView.findViewById(R.id.description_edit);
        Button addButton = popupView.findViewById(R.id.add_btn);
        Button cancelButton = popupView.findViewById(R.id.cancel_btn);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        nameEditText.setText(webView.getTitle());

        addButton.setOnClickListener(view ->
        {
            bookmarkModel = new BookmarkModel(nameEditText.getText().toString(), webView.getUrl(), descEditText.getText().toString());

            reference = rootNode.getReference("Users/" + userId + "/Bookmarks");
            String key = reference.push().getKey();
            reference.child(key).setValue(bookmarkModel);

            popupWindow.dismiss();
            Toast.makeText(getContext(), "Bookmark Added", Toast.LENGTH_SHORT).show();
        });

        cancelButton.setOnClickListener(view -> popupWindow.dismiss());
    }

    private void setDesktopMode(boolean b)
    {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (b)
        {
            try
            {
                String ua = webView.getSettings().getUserAgentString();
                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(b);
        webView.getSettings().setLoadWithOverviewMode(b);
        webView.reload();
    }

    private void onDownload(WebView webView)
    {
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) ->
        {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            downloadFileName = URLUtil.guessFileName(url,contentDisposition,mimetype);
            downloadFileUrl = url;
            request.allowScanningByMediaScanner();
            request.setTitle(downloadFileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadFileName);
            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
            Toast.makeText(getContext(), "Downloading File", Toast.LENGTH_LONG).show();
            downloadModel = new DownloadModel(downloadFileName, downloadFileUrl);

            reference = rootNode.getReference("Users/" + userId + "/Downloads");
            String key = reference.push().getKey();
            reference.child(key).setValue(downloadModel);

        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        if(s.equals("display_text"))
        {
            showToast = sharedPreferences.getBoolean("display_text",true);
        }
        if(s.equals("search_preference"))
        {
            String search_engine = sharedPreferences.getString(s, "");
            if(search_engine.equals("google"))
            {
                search_query = "http://www.google.com/search?q=";
            }
            else if(search_engine.equals("duckduckgo"))
            {
                search_query = "https://duckduckgo.com/?q=";
            }
            else if(search_engine.equals("bing"))
            {
                search_query = "https://www.bing.com/search?q=";
            }
        }
        if(s.equals("javascript"))
        {
            isJavaScript = sharedPreferences.getBoolean("javascript", true);
            javaScriptSettings();
        }
        if(s.equals("dark_mode"))
        {
            isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
            darkModeSettings();
        }
    }

    private void javaScriptSettings()
    {
        webSettings = webView.getSettings();
        if(isJavaScript)
        {
            webSettings.setJavaScriptEnabled(true);
        }
        else {
            webSettings.setJavaScriptEnabled(false);
        }

        webView.reload();
    }

    private void darkModeSettings()
    {
        webSettings = webView.getSettings();
        if(isDarkMode)
        {
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK))
            {
                WebSettingsCompat.setForceDark(webSettings, WebSettingsCompat.FORCE_DARK_ON);
            }
        }
        else
        {
            WebSettingsCompat.setForceDark(webSettings, WebSettingsCompat.FORCE_DARK_OFF);
        }
    }

    private String getFirstWord(String text)
    {
        int index = text.indexOf(' ');
        if(text.contains("."))
        {
            index = text.indexOf('.');
        }
        if(text.contains(":"))
        {
            index = text.indexOf(':');
        }
        if (index > -1)
            return text.substring(0, index).trim();
        else
            return text;
    }
}