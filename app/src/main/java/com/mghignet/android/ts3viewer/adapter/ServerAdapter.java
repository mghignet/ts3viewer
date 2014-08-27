package com.mghignet.android.ts3viewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mghignet.android.ts3viewer.R;
import com.mghignet.android.ts3viewer.domain.Server;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ServerAdapter extends ArrayAdapter<Server> {

  private List<Server> servers;
  private LayoutInflater layoutInflater;
  private ServerViewHolder holder;
  private Context context;

  public ServerAdapter(Context context, int textViewResourceId, List<Server> servers) {
    super(context, textViewResourceId);
    this.layoutInflater = LayoutInflater.from(context);
    this.context = context;
    this.servers = servers;
  }

  public int getCount() {
    return servers.size();
  }

  public Server getItem(int position) {
    return servers.get(position);
  }

  public long getItemId(int id) {
    return id;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.list_item_server, parent, false);
      holder = new ServerViewHolder();
      holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
      holder.statusImageView = (ImageView) convertView.findViewById(R.id.status_image);
      convertView.setTag(holder);
    } else {
      holder = (ServerViewHolder) convertView.getTag();
    }

    holder.server = servers.get(position);

    holder.nameTextView.setText(holder.server.getName());
    Picasso.with(context).load(holder.server.getStatusImage()).into(holder.statusImageView);

    return convertView;
  }

  public class ServerViewHolder {
    TextView nameTextView;
    ImageView statusImageView;
    Server server;

    public Server getServer() {
      return server;
    }
  }
}
