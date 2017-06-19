package de.wlsc.android.ppfreeebook;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import org.jsoup.nodes.Document;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28/01/2017
 */
public class BookDocument {

  private Context context;
  private Document document;
  private AppWidgetManager appWidgetManager;
  private int[] appWidgetIds;

  public Context getContext() {
    return this.context;
  }

  public Document getDocument() {
    return this.document;
  }

  public AppWidgetManager getAppWidgetManager() {
    return this.appWidgetManager;
  }

  public int[] getAppWidgetIds() {
    return this.appWidgetIds;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public void setAppWidgetManager(AppWidgetManager appWidgetManager) {
    this.appWidgetManager = appWidgetManager;
  }

  public void setAppWidgetIds(int[] appWidgetIds) {
    this.appWidgetIds = appWidgetIds;
  }
}
