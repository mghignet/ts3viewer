package com.mghignet.android.ts3viewer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mghignet.android.ts3viewer.R;
import com.mghignet.android.ts3viewer.domain.Client;
import com.mghignet.android.ts3viewer.util.CalculatorUtil;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ClientAdapter extends BaseAdapter {

  private List<Client> clients;
  private LayoutInflater layoutInflater;
  private ClientViewHolder holder;
  private Context context;

  public ClientAdapter(Activity activity, List<Client> clients) {
    this.layoutInflater = LayoutInflater.from(activity);
    this.clients = clients;
    context = activity.getApplicationContext();
  }

  public int getCount() {
    return clients.size();
  }

  public Object getItem(int position) {
    return clients.get(position);
  }

  public long getItemId(int id) {
    return id;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.list_item_client, parent, false);
      holder = new ClientViewHolder();
      holder.itemLayout = (View) convertView.findViewById(R.id.item_layout);
      holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
      holder.statusImageView = (ImageView) convertView.findViewById(R.id.status_image);
      holder.channelAdminImageView = (ImageView) convertView.findViewById(R.id.channel_admin_image);
      holder.serverAdminImageView = (ImageView) convertView.findViewById(R.id.server_admin_image);
      convertView.setTag(holder);
    } else {
      holder = (ClientViewHolder) convertView.getTag();
    }
    holder.client = clients.get(position);

    int paddingLeft = CalculatorUtil.getInstance(context).pxFromDp(20 + 20 * holder.client.getTreeLevel());
    int paddingOther = CalculatorUtil.getInstance(context).pxFromDp(10);
    holder.itemLayout.setPadding(paddingLeft, paddingOther, paddingOther, paddingOther);

    holder.nameTextView.setText(holder.client.getName());
    Picasso.with(context).load(holder.client.getStatusImage()).into(holder.statusImageView);
    Picasso.with(context).load(holder.client.getChannelAdminImage()).into(holder.channelAdminImageView);
    Picasso.with(context).load(holder.client.getServerAdminImage()).into(holder.serverAdminImageView);

    return convertView;
  }

  public class ClientViewHolder {
    View itemLayout;
    TextView nameTextView;
    ImageView statusImageView;
    ImageView channelAdminImageView;
    ImageView serverAdminImageView;
    Client client;

    public Client getClient() {
      return client;
    }
  }
}
