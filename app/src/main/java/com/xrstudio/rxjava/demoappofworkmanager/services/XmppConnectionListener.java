package com.xrstudio.rxjava.demoappofworkmanager.services;

import com.xrstudio.rxjava.demoappofworkmanager.App;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.ping.PingManager;

/**
 * Created by PREMIUM on 24-07-2018.
 */

public class XmppConnectionListener implements ConnectionListener {

    private static XmppConnectionListener instance;

    public static XmppConnectionListener getInstance() {
        if (instance == null) {
            instance = new XmppConnectionListener();
        }

        return instance;
    }


    @Override
    public void connected(XMPPConnection connection) {
            ConnectionManager.getInstance().doLogin((XMPPTCPConnection) connection);
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        XMPPConncetionService.getInstance().onBecameForeground();
        App.getInstance().setConnection((AbstractXMPPConnection) connection);
        PingManager.getInstanceFor(connection).setPingInterval(15);
    }

    @Override
    public void connectionClosed() {
    }

    @Override
    public void connectionClosedOnError(Exception e) {

        if (e instanceof XMPPException.StreamErrorException) {
            XMPPException.StreamErrorException xmppEx = (XMPPException.StreamErrorException) e;
            StreamError error = xmppEx.getStreamError();

            if (StreamError.Condition.conflict == error.getCondition()) {
                return;
            }
        }
        if (NetworkManager.isNetworkAvailable()) {
            reconnection();
        }

    }

    public static final void reconnection() {
        // We need to reconnect after 1 seconds.
        if (NetworkManager.isNetworkAvailable()) {
            ConnectionManager.getInstance().login();
        }
    }
}
