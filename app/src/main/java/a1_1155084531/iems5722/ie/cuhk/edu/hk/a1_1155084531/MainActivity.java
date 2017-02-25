package a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.adapter.ChatroomAdapter;
import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.data.Chatroom;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView click;
    private ChatroomAdapter Chat_ad = null;
    private String Acticivity_NAME = "MainActivity";
    private List<Chatroom> chatnameList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("IEMS5722");
        setContentView(R.layout.activity_main);
        click = (ListView) findViewById(R.id.main_list);
        new MainAsyncTask().execute();


    }

    class MainAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject json = null;
            json = sendRequestWithOkhttp();
            Log.d("Main", "doInBackground:excuted! ");
            return json;
        }

        protected void onPostExecute(JSONObject jsObject) {
            Log.d("MAIN", "onPostExcute: ");
            if (jsObject != null) {
                parseJSONWithJSONObjecet(jsObject);
                Chat_ad = new ChatroomAdapter(MainActivity.this, R.layout.chatroom, chatnameList);
                Chat_ad.notifyDataSetChanged();
                click.setAdapter(Chat_ad);
            }
        }


    }


    private JSONObject sendRequestWithOkhttp() {
        JSONObject json = null;
        String URL_get_chatrooms = "http://iems5722.albertauyeung.com/api/asgn2/get_chatrooms";
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        Log.d("Main", "sendRequestWithOkhttp:excuted");
        Request req = new Request.Builder().url(URL_get_chatrooms).build();
        try {
            response = client.newCall(req).execute();
            Log.d(Acticivity_NAME, String.valueOf(response.code()));
            String responseData = response.body().string();
            json = new JSONObject(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }

    private void parseJSONWithJSONObjecet(JSONObject jsObject) {
        try {
            JSONArray jsonArray = jsObject.getJSONArray("data");
            Log.d("Main", "parseJSONWithJSONObjecet: "+ "get it ");
            if (jsObject.get("status").equals("OK")) {
                Log.d(Acticivity_NAME, "parseJSONWithJSONObjecet: ");
                for (int i = 0; i < jsonArray.length(); i++) {
                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    chatnameList.add(new Chatroom(name, id));
                }
                Chat_ad = new ChatroomAdapter(MainActivity.this, R.layout.chatroom, chatnameList);
                click.setAdapter(Chat_ad);
                click.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Chatroom chat_info = chatnameList.get(position);
                        Toast.makeText(MainActivity.this, String.valueOf(chat_info.getId()) + " " + chat_info.getContent(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Chat_Activity.class);
                        intent.putExtra("chatroom_name", chat_info.getContent());
                        intent.putExtra("chatroom_id", String.valueOf(chat_info.getId()));
                        startActivity(intent);

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}




