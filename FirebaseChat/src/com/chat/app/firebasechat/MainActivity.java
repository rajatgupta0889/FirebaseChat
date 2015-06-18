package com.chat.app.firebasechat;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

public class MainActivity extends ListActivity {

	// TODO: change this to your own Firebase URL
	private static final String FIREBASE_URL = "https://marilychat.firebaseio.com";

	private String mUsername;
	private Firebase mFirebaseRef;
	private Context context = this;
	private ValueEventListener mConnectedListener;
	private ChatListAdapter mChatListAdapter;
	String radioSelect = "Incompatibility";
	Intent intent;
	String  group;
	TextView grp_name;
	int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

//		TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		String uuid = tManager.getDeviceId();
		// Make sure we have a mUsername
		setupUsername();
		id = getIntent().getExtras().getInt("id");
		group = getIntent().getExtras().getString("group");
		grp_name=(TextView)findViewById(R.id.textView1);
		grp_name.setText(group);
		

		setTitle("Chatting as " + mUsername);

		// Setup our Firebase mFirebaseRef
		mFirebaseRef = new Firebase(FIREBASE_URL).child(group);

		// Setup our input methods. Enter key on the keyboard or pushing the
		// send button
		EditText inputText = (EditText) findViewById(R.id.messageInput);
		inputText
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView,
							int actionId, KeyEvent keyEvent) {
						if (actionId == EditorInfo.IME_NULL
								&& keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
							sendMessage();
						}
						return true;
					}
				});

		findViewById(R.id.sendButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						sendMessage();
					}
				});
		findViewById(R.id.shortlist).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LayoutInflater li = LayoutInflater.from(context);
						View custom = li.inflate(R.layout.shortlist_dialog,
								null);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						alertDialogBuilder.setView(custom);
						alertDialogBuilder.setCancelable(true);

						// create alert dialog
						final AlertDialog alertDialog = alertDialogBuilder
								.create();
						Button cancel = (Button) custom
								.findViewById(R.id.shortlist_cancel);
						cancel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								alertDialog.cancel();
							}
						});

						Button shortlist = (Button) custom
								.findViewById(R.id.shortlist_shortlist);
						shortlist.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Toast.makeText(MainActivity.this,
										"Neha is Shortlisted!",
										Toast.LENGTH_SHORT).show();
								alertDialog.cancel();
							}
						});

						// show it
						alertDialog.show();
					}

				});
		findViewById(R.id.unmatch).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						LayoutInflater li = LayoutInflater.from(context);
						View custom = li.inflate(R.layout.unmatch_dialog, null);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						alertDialogBuilder.setView(custom);
						alertDialogBuilder.setCancelable(true);

						// create alert dialog
						final AlertDialog alertDialog = alertDialogBuilder
								.create();
						Button cancel = (Button) custom
								.findViewById(R.id.unmatch_cancel);
						cancel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								alertDialog.cancel();
							}
						});

						RadioGroup radio = (RadioGroup) custom
								.findViewById(R.id.unmatch_radioGroup);
						radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(RadioGroup group,
									int checkedId) {
								// TODO Auto-generated method stub

								if (checkedId == R.id.radio1_incompatibility) {
									radioSelect = "Incompatibilty";
								} else if (checkedId == R.id.radio2_language) {
									radioSelect = "Inappropriate Language";
								} else if (checkedId == R.id.radio3_profile) {
									radioSelect = "Fake Profile";
								}
							}
						});

						Button unmatch = (Button) custom
								.findViewById(R.id.unmatch_unmatch);
						unmatch.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Toast.makeText(MainActivity.this, radioSelect,
										Toast.LENGTH_SHORT).show();
								radioSelect = "Incompatibilty";
								alertDialog.cancel();
							}
						});

						// show it
						alertDialog.show();
					}
				});

	};

	@Override
	public void onStart() {
		super.onStart();
		// Setup our view and list adapter. Ensure it scrolls to the bottom as
		// data changes
		final ListView listView = getListView();
		// Tell our list adapter that we only want 50 messages at a time
		mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this,
				R.layout.chat_message, mUsername);
		listView.setAdapter(mChatListAdapter);
		mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				listView.setSelection(mChatListAdapter.getCount() - 1);
			}
		});

		// Finally, a little indication of connection status
		mConnectedListener = mFirebaseRef.getRoot().child(".info/connected")
				.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						boolean connected = (Boolean) dataSnapshot.getValue();
						if (connected) {
							Toast.makeText(MainActivity.this, "Connected",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(MainActivity.this, "Disconnected",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancelled(FirebaseError firebaseError) {
						// No-op
					}
				});
	}

	@Override
	public void onStop() {
		super.onStop();
		mFirebaseRef.getRoot().child(".info/connected")
				.removeEventListener(mConnectedListener);
		mChatListAdapter.cleanup();
	}

	private void setupUsername() {
		SharedPreferences prefs = getApplication().getSharedPreferences(
				"ChatPrefs", 0);
		mUsername = prefs.getString("username", null);
		if (mUsername == null) {
			Random r = new Random();
			// Assign a random user name if we don't have one saved.
			mUsername = "JavaUser" + r.nextInt(100000);
			prefs.edit().putString("username", mUsername).commit();
		}
	}

	private void sendMessage() {
		EditText inputText = (EditText) findViewById(R.id.messageInput);
		String imageurl = "http://placehold.it/120x120&text=image1";
		String input = inputText.getText().toString();
		if (!input.equals("")) {
			// Create our 'model', a Chat object
			Chat chat = new Chat(input, mUsername,  id+""+group);
			// Create a new, auto-generated child of that chat location, and
			// save our chat data there
			mFirebaseRef.push().setValue(chat);
			inputText.setText("");
		}
	}
}
