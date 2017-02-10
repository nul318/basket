package teamnova.basket;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by jwh48 on 2017-02-11.
 */

public class ClipboardCatchService extends Service {
    ClipboardManager.OnPrimaryClipChangedListener clipChangedListener;
    final String TAG = "ClipboardService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        clipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Log.d(TAG, "clipboard copy");
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                String result = null;
                ClipDescription description = clipboardManager.getPrimaryClipDescription();
                ClipData clip_text = clipboardManager.getPrimaryClip();
                if(clip_text != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                    result = String.valueOf(clip_text.getItemAt(0).getText());
                activateDialog(result);
            }
        };

        ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(clipChangedListener);
    }

    void activateDialog(String result)
    {
        if(result != null) {
            Intent link_save = new Intent(getApplicationContext(), LinkSaveActivity.class);
            link_save.putExtra("link_data", result);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, link_save, PendingIntent.FLAG_ONE_SHOT);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        }
}
