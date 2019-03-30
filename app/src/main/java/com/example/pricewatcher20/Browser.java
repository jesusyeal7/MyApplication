package com.example.pricewatcher20;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.content.Intent.EXTRA_TEXT;

public class Browser extends AppCompatActivity {
    WebView internet;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        internet = (WebView) findViewById(R.id.internet);
        Intent getPage = getIntent();
        url = getPage.getStringExtra("link");
        internet.setWebViewClient(new WebViewClient());
        internet.loadUrl(url);
        WebSettings webPageConf = internet.getSettings();
        webPageConf.setJavaScriptEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.Share){
            share();
            return true;
        }
        if(id == R.id.exit){
            exit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(internet.canGoBack()){
            internet.goBack();
        }else {
            super.onBackPressed();
        }
    }
    public void share(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        url = internet.getUrl();
        shareIntent.putExtra(Intent.EXTRA_TEXT,url);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Copied url");

        shareIntent.setData(Uri.parse(url));
        setResult(0,shareIntent);
        finish();
    }
    public void exit(){
        Intent i = new Intent();
        i.setData(Uri.parse(""));
        setResult(Activity.RESULT_OK,i);
        finish();
    }
}
