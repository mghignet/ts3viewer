package com.mghignet.android.ts3viewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.mghignet.android.ts3viewer.Fragment.SettingsFragment;
import com.mghignet.android.ts3viewer.R;
import com.mghignet.android.ts3viewer.adapter.ClientAdapter;
import com.mghignet.android.ts3viewer.adapter.SeparatedChannelListAdapter;
import com.mghignet.android.ts3viewer.adapter.SeparatedServerListAdapter;
import com.mghignet.android.ts3viewer.domain.Channel;
import com.mghignet.android.ts3viewer.domain.Client;
import com.mghignet.android.ts3viewer.domain.Server;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

  public static final String TS3VIEWER_ADDRESS = "http://www.tsviewer.com/ts3viewer.php?ID=";
  public static final String TS3VIEWER_SERVER_PREFIX_PATTERN = "^ts3v_[0-9]+_";
  public static final String TS3VIEWER_SERVER_ID_PATTERN = TS3VIEWER_SERVER_PREFIX_PATTERN + "ts3_h_s[0-9]+$";
  public static final String TS3VIEWER_CHANNEL_ID_ENDPATTERN = "_ch[0-9]+$"; //This pattern is appended to the server pattern
  public static final String TS3VIEWER_CLIENT_ID_ENDPATTERN = "_cl[0-9]+$"; //This pattern is appended to the server pattern

  public static String ts3ViewerId;

  private List<Server> servers;
  private Map<String, Server> serverMap;

  private ListView listView;
  private View listViewProgressBar;
  private View listViewLoadingError;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    listView = (ListView) findViewById(R.id.list);
    listViewProgressBar = (View) findViewById(R.id.listview_progressbar);
    listViewLoadingError = (View) findViewById(R.id.listview_loading_error);

    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
  }

  public void onResume() {
    super.onResume();

    refresh();
  }

  private void getTS3Info() {
    servers = new ArrayList<Server>();
    serverMap = new HashMap<String, Server>();

    new getTS3InfoTask().execute();

//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//
//				String url = "ts3server://88.190.17.165?port=9987";
//				//String url = "ts3server://ts3.hoster.com?port=9987";
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(url));
//				startActivity(i);
//
//			}
//			
//		});
  }

  private class getTS3InfoTask extends AsyncTask<Void, Void, Void> {
    protected Void doInBackground(Void... progress) {
      String htmlData = getHttpData();
      htmlData = splitHttpData(htmlData);
      Elements divs = getHtmlDivs(htmlData);
      extractServers(divs);
      return null;
    }

    protected void onPostExecute(Void result) {
      //Create our list and custom adapter
      if (servers.size() > 0) {
        SeparatedServerListAdapter serverAdapter = new SeparatedServerListAdapter(MainActivity.this, servers);
        for (Server server : servers) {
          SeparatedChannelListAdapter channelAdapter = new SeparatedChannelListAdapter(MainActivity.this, server.getChannels());
          serverAdapter.addSection(server, channelAdapter);
          for (Channel channel : server.getChannels()) {
            channelAdapter.addSection(channel, new ClientAdapter(MainActivity.this, channel.getClients()));
          }
        }
        listView.setAdapter(serverAdapter);
      } else {
        listViewLoadingError.setVisibility(View.VISIBLE);
      }

      listViewProgressBar.setVisibility(View.GONE);
    }
  }


  private String getHttpData() {
    BufferedReader in = null;
    String httpData = "";
    try {
      HttpClient client = new DefaultHttpClient();
      HttpGet request = new HttpGet();
      request.setHeader("Cache-Control", "no-cache");
      request.setURI(new URI(TS3VIEWER_ADDRESS + ts3ViewerId));
      Log.d("ts", request.getURI().toString());
      HttpResponse response = client.execute(request);
      in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuffer sb = new StringBuffer("");
      String line = "";
      String NL = System.getProperty("line.separator");
      while ((line = in.readLine()) != null) {
        sb.append(line + NL);
      }
      in.close();
      httpData = sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return httpData;
  }

  private String splitHttpData(String httpData) {
    String parsed = "";
    httpData = httpData.replace("\\\"", "\"");

    try {
      String[] parsedArray = httpData.split("<div class=\"ts3v_" + ts3ViewerId + "\">");
      parsed = parsedArray[1];
      parsedArray = parsed.split("</div><style type=\"text/css\">");
      parsed = parsedArray[0];
    } catch (Exception e) {
      e.printStackTrace();
    }

    return parsed;
  }

  private Elements getHtmlDivs(String htmlData) {
    Document doc;
    Elements selectedDivs = new Elements();
    try {
      doc = Jsoup.parse(htmlData);

      Elements allDivs = doc.getElementsByTag("div");
      for (Element div : allDivs) {
        if (div.attr("id") != null && !"".equals(div.attr("id"))) {
          selectedDivs.add(div);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return selectedDivs;
  }

  synchronized private void extractServers(Elements divs) {

    //Create the server list
    for (Element div : divs) {
      String id = div.attr("id");
      if (id.matches(TS3VIEWER_SERVER_ID_PATTERN)) {
        Server server = getServerInfoFromDiv(div);
        servers.add(server);
        serverMap.put(id, server);
      }
    }

    //Create channel lists and set them to the servers created
    for (Server server : servers) {
      for (Element div : divs) {
        String id = div.attr("id");
        if (id.matches(server.getId() + TS3VIEWER_CHANNEL_ID_ENDPATTERN)) {
          //it's a channel!
          Channel channel = getChannelInfoFromDiv(div);
          server.getChannels().add(channel);
        } else if (id.matches(server.getId() + TS3VIEWER_CLIENT_ID_ENDPATTERN)) {
          //it's a client!
          Client client = getClientInfoFromDiv(div);
          server.getChannels().get(server.getChannels().size() - 1).getClients().add(client); //Add the client to the last added channel to this server
        }
      }
    }
  }

  private Server getServerInfoFromDiv(Element div) {
    Server server = new Server();

    Element serverElement = div.getElementsByAttributeValueContaining("href", "ts3server://").first();
    Element serverStatusImage = div.getElementsByAttributeValueContaining("src", "http://static.tsviewer.com/images/ts3/viewer/server").first();

    server.setId(div.attr("id"));
    server.setAddress(serverElement.attr("href"));
    server.setName(serverElement.text());
    if (serverStatusImage != null) {
      server.setStatusImage(serverStatusImage.attr("src"));
    }

    return server;
  }

  private Channel getChannelInfoFromDiv(Element div) {
    Channel channel = new Channel();

    Element channelElement = div.getElementsByAttributeValueContaining("class", "ts3v_" + ts3ViewerId + "_channel").first();
    int treeLevel = 0;
    try {
      treeLevel = div.getElementsByAttributeValueContaining("src", "http://static.tsviewer.com/images/ts3/viewer/tree").size() +
      new Integer(div.attr("style").split("margin-left: ")[1].split("px")[0]) / 20;
    } catch (ArrayIndexOutOfBoundsException e) {
    }
    Element channelStatusImage = div.getElementsByAttributeValueMatching("src", "http://static.tsviewer.com/images/ts3/viewer/channel(?!_flag)").first();
    Element channelFlagImage = div.getElementsByAttributeValueContaining("src", "http://static.tsviewer.com/images/ts3/viewer/channel_flag").first();

    channel.setId(div.attr("id"));
    channel.setName(channelElement.text());
    channel.setTreeLevel(treeLevel);
    if (channelStatusImage != null) {
      channel.setStatusImage(channelStatusImage.attr("src"));
    }
    if (channelFlagImage != null) {
      channel.setFlagImage(channelFlagImage.attr("src"));
    }

    return channel;
  }

  private Client getClientInfoFromDiv(Element div) {
    Client client = new Client();

    Element clientElement = div.getElementsByAttributeValueContaining("class", "ts3v_" + ts3ViewerId + "_user").first();
    int treeLevel = 0;
    try {
      treeLevel = new Integer(div.attr("style").split("margin-left: ")[1].split("px")[0]) / 20;
    } catch (ArrayIndexOutOfBoundsException e) {
    }
    Element clientStatusImage = div.getElementsByAttributeValueContaining("src", "http://static.tsviewer.com/images/ts3/viewer/client").first();
    Element clientPrivilegeServerAdminImage = div.getElementsByAttributeValueContaining("title", "Server Admin").first();
    Element clientPrivilegeChannelAdminImage = div.getElementsByAttributeValueContaining("title", "Channel Admin").first();

    client.setId(div.attr("id"));
    client.setName(clientElement.text());
    client.setTreeLevel(treeLevel);
    client.setStatusImage(clientStatusImage.attr("src"));
    if (clientPrivilegeChannelAdminImage != null) {
      client.setChannelAdminImage(clientPrivilegeChannelAdminImage.attr("src"));
    }
    if (clientPrivilegeServerAdminImage != null) {
      client.setServerAdminImage(clientPrivilegeServerAdminImage.attr("src"));
    }

    return client;
  }

  public void refresh() {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    ts3ViewerId = sharedPref.getString(SettingsFragment.PREF_SERVER_ID, "");

    listViewProgressBar.setVisibility(View.VISIBLE);
    listViewLoadingError.setVisibility(View.GONE);
    getTS3Info();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.refresh:
        refresh();
        return true;
      case R.id.settings:
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

}