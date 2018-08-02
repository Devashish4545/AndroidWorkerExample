package com.xrstudio.rxjava.demoappofworkmanager.xmpp;

import com.xrstudio.rxjava.demoappofworkmanager.App;
import com.xrstudio.rxjava.demoappofworkmanager.adapter.ConversationAdapter;
import com.xrstudio.rxjava.demoappofworkmanager.services.ConnectionManager;
import com.xrstudio.rxjava.demoappofworkmanager.services.NetworkManager;
import com.xrstudio.rxjava.demoappofworkmanager.services.XmppConnectionListener;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;

import java.io.IOException;

public class XMPPConnectionBuilder {
    private static boolean isConnecting = false;

    public XMPPConnectionBuilder() {
        if (!isConnecting) {
            isConnecting = true;
            createXmppThread();
        }
    }


    public void createXmppThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetworkManager.isNetworkAvailable()) {
                    processConnectToXmpp();
                }
                isConnecting = false;
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setDaemon(true);
        thread.start();
    }


    protected XMPPTCPConnection processConnectToXmpp() {

        XMPPTCPConnectionConfiguration connectionConfiguration;
        XMPPTCPConnection connection = null;
        try {

            XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
            builder.setConnectTimeout(30000);
            builder.setXmppDomain("dev.chat.okdone.io");
            builder.setHost("dev.chat.okdone.io");
            builder.setPort(5222);
            builder.setSendPresence(false);
            builder.enableDefaultDebugger();
            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            connectionConfiguration = builder.build();

            XMPPTCPConnection.setUseStreamManagementDefault(true);
            XMPPTCPConnection.setUseStreamManagementResumptionDefault(true);


            connection = (XMPPTCPConnection) App.getInstance().getConnection();
            if (connection != null) {
                connection.disconnect();
            }
            connection = new XMPPTCPConnection(connectionConfiguration);

            connection.addConnectionListener(XmppConnectionListener.getInstance());
            connection.setReplyTimeout(30000);

            if (!connection.isConnected()) {
                connection.connect();
            }

            DeliveryReceiptManager.getInstanceFor(connection).setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.disabled);
            ServerPingWithAlarmManager.getInstanceFor(connection).setEnabled(true);


            StanzaFilter chatMessageFilter = MessageTypeFilter.CHAT;
            connection.addSyncStanzaListener(new StanzaListener() {
                @Override
                public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {

                    Message message = (Message) packet;
                    String msg = message.getBody();
                    if (msg != null) {
                        App.getInstance().initilizeWorker(message.getStanzaId(),msg, ConversationAdapter.MSG_TEXT_RECV);
                    }
                }
            }, chatMessageFilter);

//            ReconnectionManager.getInstanceFor((AbstractXMPPConnection) connection).enableAutomaticReconnection();
//            ReconnectionManager.getInstanceFor((AbstractXMPPConnection) connection).addReconnectionListener(XmppReConnectionListener.getInstance());


        } catch (XMPPException e) {
            e.printStackTrace();
            ConnectionManager.getInstance().reConnection();
        } catch (InterruptedException | IOException | SmackException e) {
            ConnectionManager.getInstance().reConnection();
            e.printStackTrace();
        }
        return connection;
    }
}
