package de.wlsc.android.ppfreeebook;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28/01/2017
 */
public class WidgetProvider extends AppWidgetProvider {

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    BookDocument bookDocument = new BookDocument();

    bookDocument.setContext(context);
    bookDocument.setAppWidgetManager(appWidgetManager);
    bookDocument.setAppWidgetIds(appWidgetIds);

    new RetrieveWebpageTask().execute(bookDocument);

    Intent intent = new Intent(context, AppActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_app);

    views.setOnClickPendingIntent(R.id.widget, pendingIntent);

    ComponentName componentName = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
    appWidgetManager.updateAppWidget(componentName, views);
  }

  @Override
  public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

    BookDocument bookDocument = new BookDocument();

    bookDocument.setContext(context);
    bookDocument.setAppWidgetManager(appWidgetManager);
    bookDocument.setAppWidgetIds(new int[]{appWidgetId});

    new RetrieveWebpageTask().execute(bookDocument);
  }

  private class RetrieveWebpageTask extends AsyncTask<BookDocument, Void, BookDocument> {

    @Override
    protected BookDocument doInBackground(BookDocument... bookDocuments) {

      BookDocument bookDocument = bookDocuments[0];

      try {
        bookDocument.setDocument(Jsoup.connect(Config.PACKT_FREE_EBOOK_URL).get());
        return bookDocument;
      } catch (IOException e) {
        Log.e("RetrieveWebpageTask", "Cannot load url");
      }

      return null;
    }

    @Override
    protected void onPostExecute(BookDocument bookDocument) {

      if (bookDocument == null) {
        return;
      }

      String bookTitle = bookDocument.getDocument()
              .select(".dotd-main-book-summary .dotd-title")
              .text();
      String bookExpireTime = bookDocument.getDocument()
              .select(".dotd-main-book-summary [^data-countdown-to]")
              .attr("data-countdown-to");

      long timeDiff = Math.abs(System.currentTimeMillis() / 1_000 - Integer.valueOf(bookExpireTime));
      long timeLeftInHours = TimeUnit.SECONDS.toHours(timeDiff);
      String formattedExpireTime = String.valueOf(timeLeftInHours == 0 ? 1 : timeLeftInHours);

      ComponentName thisWidget = new ComponentName(bookDocument.getContext(), WidgetProvider.class);
      int[] allWidgetIds = bookDocument.getAppWidgetManager().getAppWidgetIds(thisWidget);

      for (int widgetId : allWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(bookDocument.getContext().getPackageName(), R.layout.widget);
        remoteViews.setTextViewText(R.id.bookTitle, bookTitle);
        remoteViews.setTextViewText(R.id.bookExpirationTime, formattedExpireTime + " hours left");

        Intent intent = new Intent(bookDocument.getContext(), WidgetProvider.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, bookDocument.getAppWidgetIds());

        bookDocument.getAppWidgetManager().updateAppWidget(widgetId, remoteViews);
      }
    }
  }
}
