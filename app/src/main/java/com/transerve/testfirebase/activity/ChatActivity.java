package com.transerve.testfirebase.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.transerve.testfirebase.R;
import com.transerve.testfirebase.adapter.ChatAdapter;
import com.transerve.testfirebase.model.Chat;
import com.transerve.testfirebase.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String TAG = ChatActivity.class.getSimpleName();
    private Context context = ChatActivity.this;

    /*** Current user id*/
    public static final int MY_ID = 1;
    /*** Other user id*/
    public static final int OTHER_ID = 2;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private RecyclerView recyclerView;
    private EditText tvMessage;
    private ImageButton bSend;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private ChildEventListener childEventListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        String chatRoom = Utils.generate(MY_ID, OTHER_ID);
        myRef = database.getReference("chat/" + chatRoom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvMessage = (EditText) findViewById(R.id.tvMessage);
        bSend = (ImageButton) findViewById(R.id.bSend);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setStackFromEnd(true);
        manager.setSmoothScrollbarEnabled(true);
//        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        setAdapter();

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = tvMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    String from_name = "Current user";
                    String from_email = "currentuser@gmail.com";
                    String to_device_id = "DEVICE_ID_OF_OTHER_USER";
                    Chat chat = new Chat(MY_ID, from_name, from_email, to_device_id, message);
                    myRef.push().setValue(chat);
                }
                tvMessage.setText("");
            }
        });
    }

    public void scrollToBottom() {
        recyclerView.smoothScrollToPosition(chatList.size() - 1);
    }

    private void setAdapter() {
        chatAdapter = new ChatAdapter(context, chatList);
        recyclerView.setAdapter(chatAdapter);
    }

    private void setData() {
        // Read from the database
        chatList = new ArrayList<>();
        childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, "onChildAdded: " + dataSnapshot.getValue() + ", s : " + s);

                Chat chat = dataSnapshot.getValue(Chat.class);
                chat.setId(s);
                chatList.add(chat);
                chatAdapter.refresh(chatList);
                scrollToBottom();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, "onChildChanged: " + dataSnapshot.getValue() + ", s : " + s);

                Chat chat = dataSnapshot.getValue(Chat.class);
                chat.setId(s);

                for (int i = chatList.size() - 1; i >= 0; i--) {
                    if (chatList.get(i).getId() != null) {
                        if (chatList.get(i).getId().equals(chat.getId())) {
                            Log.e(TAG, "chat.getId() : " + chat.getId());
                            Log.e(TAG, "list : " + chatList.size() + ", time : " + chatList.get(i).getTime());
                            chatList.remove(i);
                            Log.e(TAG, "list : " + chatList.size());
//                            setAdapter();
                            chatAdapter.refresh(chatList);
                            chatList.add(chat);
                            Log.e(TAG, "list : " + chatList.size() + ", time : " + chatList.get(i).getTime());
                            chatAdapter.refresh(chatList);
                            scrollToBottom();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onChildRemoved: " + dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, "onChildMoved: " + dataSnapshot.getValue() + ", s : " + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (childEventListener != null) {
            myRef.removeEventListener(childEventListener);
        }
    }
}
