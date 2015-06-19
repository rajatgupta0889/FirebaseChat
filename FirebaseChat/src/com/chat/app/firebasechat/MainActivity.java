package com.chat.app.firebasechat;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.Window;
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
import android.widget.LinearLayout;
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

public class MainActivity extends ActionBarActivity implements OnClickListener {

	// TODO: change this to your own Firebase URL
	private static final String FIREBASE_URL = "https://marilychat.firebaseio.com";

	private String mUsername;
	private Firebase mFirebaseRef;
	private Context context = this;
	private ValueEventListener mConnectedListener;
	private ChatListAdapter mChatListAdapter;
	String radioSelect = "Incompatibility";
	Intent intent;
	String group;
	TextView grp_name, days;
	String remainingdays;
	Toolbar toolbar;
	TextView title, shortlist, unmatch;
	LinearLayout menuitem;
	EditText inputText;

	private Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();

		// Make sure we have a mUsername
		setupUsername();
		remainingdays = getIntent().getExtras().getString("days");
		group = getIntent().getExtras().getString("group");
		// days.setText(remainingdays);
		title.setText("");

		toolbar.setNavigationIcon(R.drawable.back);
		toolbar.setTitle(group);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.inflateMenu(R.menu.mymenu);
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Handle the menu item

				toggleVissibility();

				return true;
			}
		});
		toolbar.setNavigationOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SelectUser.class);
				startActivity(intent);

			}
		});

		// Setup our Firebase mFirebaseRef
		mFirebaseRef = new Firebase(FIREBASE_URL).child(group);

		// Setup our input methods. Enter key on the keyboard or pushing the
		// send button
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

		findViewById(R.id.sendButton).setOnClickListener(this);
		shortlist.setOnClickListener(this);
		unmatch.setOnClickListener(this);

	};

	private void toggleVissibility() {
		// TODO Auto-generated method stub
		if (menuitem.getVisibility() == View.VISIBLE) {
			menuitem.setVisibility(View.INVISIBLE);
		} else {
			menuitem.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mymenu, menu);
		this.menu = menu;
		MenuItem days = menu.findItem(R.id.IconMenu2);
		MenuItem clock = menu.findItem(R.id.IconMenu1);
		clock.setEnabled(false);
		days.setEnabled(false);
		String[] day = remainingdays.split(" ");
		days.setTitle(day[0] + "  " + day[1]);

		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		// Setup our view and list adapter. Ensure it scrolls to the bottom as
		// data changes
		final ListView listView = (ListView) findViewById(R.id.list);
		// Tell our list adapter that we only want 50 messages at a time
		mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(100), this,
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
							Toast.makeText(MainActivity.this, "Please wait..",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(MainActivity.this,
									"Check Your Connection", Toast.LENGTH_SHORT)
									.show();
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

	void init() {
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		title = (TextView) toolbar.findViewById(R.id.toolbar_title);
		menuitem = (LinearLayout) findViewById(R.id.menuitem);
		shortlist = (TextView) findViewById(R.id.shortlist);
		unmatch = (TextView) findViewById(R.id.unmatch);
		inputText = (EditText) findViewById(R.id.messageInput);
		// days = (TextView)findViewById(R.id.days);

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
			Chat chat = new Chat(input, mUsername, group);
			// Create a new, auto-generated child of that chat location, and
			// save our chat data there
			mFirebaseRef.push().setValue(chat);
			inputText.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shortlist:
			menuitem.setVisibility(View.INVISIBLE);
			LayoutInflater li = LayoutInflater.from(context);
			View custom = li.inflate(R.layout.shortlist_dialog, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			alertDialogBuilder.setView(custom);
			alertDialogBuilder.setCancelable(true);

			// create alert dialog
			final AlertDialog alertDialog = alertDialogBuilder.create();
			Button cancel = (Button) custom.findViewById(R.id.shortlist_cancel);
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
					Toast.makeText(MainActivity.this, "" + group,
							Toast.LENGTH_SHORT).show();
					alertDialog.cancel();
				}
			});
			alertDialog.show();

			break;
		case R.id.unmatch:
			menuitem.setVisibility(View.INVISIBLE);
			LayoutInflater li1 = LayoutInflater.from(context);
			View custom1 = li1.inflate(R.layout.unmatch_dialog, null);
			AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
					context);

			alertDialogBuilder1.setView(custom1);
			alertDialogBuilder1.setCancelable(true);

			// create alert dialog
			final AlertDialog alertDialog1 = alertDialogBuilder1.create();
			Button cancel1 = (Button) custom1.findViewById(R.id.unmatch_cancel);
			cancel1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog1.cancel();
				}
			});

			RadioGroup radio = (RadioGroup) custom1
					.findViewById(R.id.unmatch_radioGroup);
			radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
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

			Button unmatch = (Button) custom1
					.findViewById(R.id.unmatch_unmatch);
			unmatch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, radioSelect,
							Toast.LENGTH_SHORT).show();
					radioSelect = "Incompatibilty";
					alertDialog1.cancel();
				}
			});
			alertDialog1.show();

			break;
		case R.id.sendButton:
			sendMessage();
			break;

		default:
			break;
		}

	}
}
