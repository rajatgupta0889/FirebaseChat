package com.chat.app.firebasechat;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater layoutinflate;
	List<MessageModel> list;
	

	public UserListAdapter(Context context, List<MessageModel> list) {
		super();
		this.context = context;
		layoutinflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = layoutinflate.inflate(R.layout.user_list_item, null);
		}
		MessageModel item = list.get(position);
		ImageView userimage = (ImageView) convertView
				.findViewById(R.id.userimage);
		TextView username = (TextView) convertView.findViewById(R.id.username);
		TextView remainingdays = (TextView) convertView
				.findViewById(R.id.remainingdays);
		TextView newmessages = (TextView) convertView
				.findViewById(R.id.newmessages);
		username.setText(item.getUserName());
		remainingdays.setText(item.getRemainingDay());
		newmessages.setText(item.getNewMessage());
		

		return convertView;
	}

}
