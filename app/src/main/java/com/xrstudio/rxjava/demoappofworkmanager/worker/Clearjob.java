
package com.xrstudio.rxjava.demoappofworkmanager.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xrstudio.rxjava.demoappofworkmanager.db.DatabaseHelper;

import androidx.work.Worker;

/**
 * Cleans up temporary files generated during blurring process
 */
public class Clearjob extends Worker {
    private static final String TAG = Clearjob.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        return Result.SUCCESS;
    }
}
