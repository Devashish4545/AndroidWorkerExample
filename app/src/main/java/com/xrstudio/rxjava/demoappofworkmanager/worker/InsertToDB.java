/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
