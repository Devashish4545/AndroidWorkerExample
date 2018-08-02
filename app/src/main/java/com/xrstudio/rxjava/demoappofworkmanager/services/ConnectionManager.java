package com.xrstudio.rxjava.demoappofworkmanager.services;

import com.xrstudio.rxjava.demoappofworkmanager.App;
import com.xrstudio.rxjava.demoappofworkmanager.xmpp.XMPPConnectionBuilder;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.parts.Resourcepart;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by PREMIUM on 21-07-2018.
 */

public class ConnectionManager {

    private static ConnectionManager instance;
    private Timer timer;

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }

        return instance;
    }

    public void login() {
        new XMPPConnectionBuilder();
    }



    public void reConnection() {
        if (App.getInstance().getConnection() == null || !App.getInstance().getConnection().isAuthenticated()) {
            if (timer != null)
                timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    new XMPPConnectionBuilder();
                }
            }, 2000);
        }
    }

    /**
     * This method is used to create xmpp connection.
     *
     * @param connection
     * @return void.
     */


    public void doLogin(final XMPPTCPConnection connection) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loginUser(connection);
            }
        });
        thread.start();
    }

    private void loginUser(XMPPTCPConnection connection) {
        try {
            if (!connection.isAuthenticated()) {
                connection.login("919033420675", "919033420675", Resourcepart.from("Android"));
                App.getInstance().setConnection(connection);
            }

        } catch (XMPPException | SmackException | IOException | InterruptedException e) {
            ConnectionManager.getInstance().reConnection();
        }
    }
}
