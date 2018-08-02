
package com.xrstudio.rxjava.demoappofworkmanager.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xrstudio.rxjava.demoappofworkmanager.db.DatabaseHelper;

import androidx.work.Worker;

/**
 * Cleans up temporary files generated during blurring process
 */
public class InsertToDB extends Worker {
    private static final String TAG = InsertToDB.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        try {
            String[] stanzaID = getInputData().getStringArray(DatabaseHelper.STANZA_ID);
            String stanza = stanzaID[0];
            String message = stanzaID[1];
            int direction = Integer.parseInt(stanzaID[2]);
            DatabaseHelper.getInstance().storedataTodb(stanza,message,direction);
            return Result.SUCCESS;
        } catch (Exception exception) {
            Log.e(TAG, "Error cleaning up", exception);
            return Result.RETRY;
        }
    }
}
