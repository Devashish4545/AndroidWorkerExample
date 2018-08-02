package com.xrstudio.rxjava.demoappofworkmanager;

import android.app.Application;
import android.content.Intent;

import com.xrstudio.rxjava.demoappofworkmanager.adapter.ConversationAdapter;
import com.xrstudio.rxjava.demoappofworkmanager.db.DatabaseHelper;
import com.xrstudio.rxjava.demoappofworkmanager.services.XMPPConncetionService;
import com.xrstudio.rxjava.demoappofworkmanager.worker.Clearjob;
import com.xrstudio.rxjava.demoappofworkmanager.worker.InsertToDB;
import com.xrstudio.rxjava.demoappofworkmanager.worker.SendMessageToDB;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.util.ArrayList;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private AbstractXMPPConnection connection;

    private static App ourInstance=null;
    private WorkManager mWorkManager;


    public static App getInstance() {
        return ourInstance;
    }

    public AbstractXMPPConnection getConnection() {
        return connection;
    }

    public void setConnection(AbstractXMPPConnection connection) {
        this.connection = connection;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        try {
            startService(new Intent(App.this, XMPPConncetionService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initilizeWorker(String stanzaid, String message, int direction) {
        String[] myArray = new String[] {stanzaid ,message,direction+""};
        mWorkManager = WorkManager.getInstance();
        WorkContinuation continuation = mWorkManager
                .beginUniqueWork("",
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest.from(Clearjob.class));

        OneTimeWorkRequest insertToDB =
                new OneTimeWorkRequest.Builder(InsertToDB.class)
                        .setInputData(createInputDataFor(DatabaseHelper.STANZA_ID, myArray))
                        .build();
        continuation = continuation.then(insertToDB);

        if (direction == ConversationAdapter.MSG_TEXT_SEND) {
            Constraints constraint = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            OneTimeWorkRequest save = new OneTimeWorkRequest.Builder(SendMessageToDB.class)
                    .setConstraints(constraint)
                    .addTag(stanzaid)
                    .setInputData(createInputDataFor(DatabaseHelper.STANZA_ID, myArray))
                    .build();

            continuation = continuation.then(save);
        }
        continuation.enqueue();
    }


    private Data createInputDataFor(String key, String[] Value) {
        Data.Builder builder = new Data.Builder();
        builder.putStringArray(key, Value);
        return builder.build();
    }
}
