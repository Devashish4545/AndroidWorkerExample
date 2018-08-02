
package com.xrstudio.rxjava.demoappofworkmanager.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xrstudio.rxjava.demoappofworkmanager.App;
import com.xrstudio.rxjava.demoappofworkmanager.db.DatabaseHelper;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

import androidx.work.Worker;

/**
 * Cleans up temporary files generated during blurring process
 */
public class SendMessageToDB extends Worker {

    @NonNull
    @Override
    public Result doWork() {
        String[] stanzaID = getInputData().getStringArray(DatabaseHelper.STANZA_ID);
        Message stanza = new Message();
        stanza.setBody(stanzaID[1]);
        stanza.setStanzaId(stanzaID[0]);
        stanza.setTo("918511479937@dev.chat.okdone.io");
        if (App.getInstance().getConnection() != null && App.getInstance().getConnection().isAuthenticated()) {
            try {
                App.getInstance().getConnection().sendStanza(stanza);
                return Result.SUCCESS;
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                return Result.RETRY;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.RETRY;
            }
        } else {
            return Result.RETRY;
        }
    }
}
