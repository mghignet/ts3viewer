package com.mghignet.android.ts3viewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mghignet.android.ts3viewer.R;
import com.mghignet.android.ts3viewer.domain.Channel;
import com.mghignet.android.ts3viewer.util.CalculatorUtil;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ChannelAdapter extends ArrayAdapter<Channel> {

  private List<Channel> channels;
  private LayoutInflater layoutInflater;
  private ChannelViewHolder holder;
  private Context context;

  public ChannelAdapter(Context context, int textViewResourceId, List<Channel> channels) {
    super(context, textViewResourceId);
    this.context = context;
    this.layoutInflater = LayoutInflater.from(context);
    this.channels = channels;
  }

  public int getCount() {
    return channels.size();
  }

  public Channel getItem(int position) {
    return channels.get(position);
  }

  public long getItemId(int id) {
    return id;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.list_item_channel, parent, false);
      holder = new ChannelViewHolder();
      holder.itemLayout = (View) convertView.findViewById(R.id.item_layout);
      holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
      holder.statusImageView = (ImageView) convertView.findViewById(R.id.status_image);
      holder.flagImageView = (ImageView) convertView.findViewById(R.id.flag_image);
      convertView.setTag(holder);
    } else {
      holder = (ChannelViewHolder) convertView.getTag();
    }

    holder.channel = channels.get(position);

    int paddingLeft = CalculatorUtil.getInstance(context).pxFromDp(20 + 20 * holder.channel.getTreeLevel());
    int paddingOther = CalculatorUtil.getInstance(context).pxFromDp(10);
    holder.itemLayout.setPadding(paddingLeft, paddingOther, paddingOther, paddingOther);

    holder.nameTextView.setText(holder.channel.getName());
    Picasso.with(context).load(holder.channel.getStatusImage()).into(holder.statusImageView);
    Picasso.with(context).load(holder.channel.getFlagImage()).into(holder.flagImageView);

    return convertView;
  }

  public class ChannelViewHolder {
    View itemLayout;
    TextView nameTextView;
    ImageView statusImageView;
    ImageView flagImageView;
    Channel channel;

    public Channel getChannel() {
      return channel;
    }
  }
}
