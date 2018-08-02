package com.xrstudio.rxjava.demoappofworkmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.xrstudio.rxjava.demoappofworkmanager.adapter.ConversationAdapter;
import com.xrstudio.rxjava.demoappofworkmanager.db.MST_MESSAGE_TABLE;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

import javax.annotation.Nullable;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private RecyclerView stickylist;
    private EditText editText;
    private ImageView imageView;
    Realm realm = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stickylist = findViewById(R.id.stickylist);
        editText = findViewById(R.id.typin_area);
        imageView = findViewById(R.id.btn_send);
        realm = Realm.getDefaultInstance();


        RealmResults<MST_MESSAGE_TABLE> single_chatModel_ArrayList = realm.where(MST_MESSAGE_TABLE.class).sort("messageDate").findAll();
        ConversationAdapter mAdapter = new ConversationAdapter(single_chatModel_ArrayList, true, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        single_chatModel_ArrayList.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<MST_MESSAGE_TABLE>>() {
            @Override
            public void onChange(RealmResults<MST_MESSAGE_TABLE> mst_message_tables, @Nullable OrderedCollectionChangeSet changeSet) {
                stickylist.scrollToPosition(mst_message_tables.size() - 1);
            }
        });

        stickylist.setLayoutManager(linearLayoutManager);
        stickylist.setAdapter(mAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText() + "";
                if (!TextUtils.isEmpty(message)) {
                    sendmessage(message);
                    editText.setText("");
                }
            }
        });

    }

    private void sendmessage(String message) {
        Message stanza = new Message();
        App.getInstance().initilizeWorker(stanza.getStanzaId(), message, ConversationAdapter.MSG_TEXT_SEND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
