package com.chat.app.firebasechat;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SelectUser extends ActionBarActivity implements OnClickListener {

	UserListAdapter adapter;
	List<MessageModel> list;
	ListView list1;
	LayoutInflater inflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listuser);
		list = new ArrayList<MessageModel>();
		adapter = new UserListAdapter(SelectUser.this, list);
		list1 = (ListView) findViewById(R.id.list1);
		list.add(new MessageModel("", "02 days remaining", "3 new messages",
				"Tanya", "", ""));
		list.add(new MessageModel("", "16 days remaining", "2 new messages",
				"Priya", "", ""));
		list.add(new MessageModel("", "30 days remaining", "", "Pooja", "", ""));
		list.add(new MessageModel("", "21 days remaining", "", "Komal", "", ""));
		list.add(new MessageModel("", "14 days remaining", "110 new messages",
				"Jyoti", "", ""));
		list.add(new MessageModel("", "19 days remaining", "23 new messages",
				"Sandy", "", ""));
		list.add(new MessageModel("", "12 days remaining", "1 new messages",
				"Rachel", "", ""));
		list.add(new MessageModel("", "01 days remaining", "3 new messages",
				"Riya", "", ""));
		list.add(new MessageModel("", "Today is last day", "", "Aarti", "", ""));
		list.add(new MessageModel("", "09 days remaining", "", "Sophia", "", ""));
		adapter.notifyDataSetChanged();
		list1.setAdapter(adapter);
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SelectUser.this, MainActivity.class);
				intent.putExtra("group", list.get(position).getUserName());
				intent.putExtra("days", list.get(position).getRemainingDay());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
