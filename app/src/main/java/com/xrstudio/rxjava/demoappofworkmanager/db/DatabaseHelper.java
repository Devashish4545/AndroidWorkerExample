package com.xrstudio.rxjava.demoappofworkmanager.db;

import io.realm.Realm;

public class DatabaseHelper {
    private static final DatabaseHelper ourInstance = new DatabaseHelper();

    public static DatabaseHelper getInstance() {
        return ourInstance;
    }

    public static final String STANZA_ID ="stanzaID";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_DIRECTION = "messageDirection";

    public void storedataTodb(final String stanzaID , final String message , final int direction) throws Exception {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    MST_MESSAGE_TABLE mstMessageTable = new MST_MESSAGE_TABLE(stanzaID,message,direction,System.currentTimeMillis());
                    realm.insertOrUpdate(mstMessageTable);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e) ;
        } finally {
            if (realm != null)
                realm.close();
        }
    }
}
