package de.wlsc.android.ppfreeebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28/01/2017
 */
public class AppActivity extends AppCompatActivity {

  private WebView webview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app);

    loadWebPage();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.refresh:
        loadWebPage();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void loadWebPage() {
    webview = (WebView) findViewById(R.id.webpage);
    webview.loadUrl(Config.PACKT_FREE_EBOOK_URL);
  }
}
