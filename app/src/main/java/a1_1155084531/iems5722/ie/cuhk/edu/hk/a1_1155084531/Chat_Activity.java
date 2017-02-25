package a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531;

//import android.support.v7.app.ActionBar;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.adapter.MsgAdapter;
import a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.data.Message;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chat_Activity extends AppCompatActivity {

    private ListView listView;
    private View loadview;
    private String TAG = "Chat_Activity";
    /**
     * 信息编辑框
     */
    private EditText edt;
    /**
     * 信息发送按钮
     */
    private ImageButton btnEnter;
    private MsgAdapter adp;
    private String room_ID;
    private String room_Name;
    /**
     * ID设置
     */
    private String my_name = "C.Y.Lueng";
    private final String STUDENT_ID = "1155084531";

    private int cur_pos;
    private int total_pos = 0;
    private int total_page;
    private int cur_page = 1;

    /**
     * 线程处理需要用到的参数
     */
    private boolean NEED_DATA_SEND = false;


    private List<Message> msgList = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent it = getIntent();

        room_Name = it.getStringExtra("chatroom_name");
        room_ID = it.getStringExtra("chatroom_id");
        setTitle("NO." + room_ID + " " + room_Name);


        edt = (EditText) findViewById(R.id.editText);
        btnEnter = (ImageButton) findViewById(R.id.button2);
        listView = (ListView) findViewById(R.id.msg_list);
        listView.setDivider(null);
        new ChatAsyncTask().execute();



        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                Date curDate = new Date(System.currentTimeMillis());//获取当前时间

                String str_t = formatter.format(curDate);

                String content = edt.getText().toString().trim();


                if (!content.equals("")) {
                    Message msg = new Message(content, Message.send, str_t, my_name);
                    NEED_DATA_SEND =true;
                    new ChatAsyncTask().execute(content,my_name);
                    Log.d(TAG, my_name);
                    msgList.add(msg);
                    adp.notifyDataSetChanged();
                    listView.setSelection(msgList.size());
                    edt.setText("");

                }

            }
        });

    }
    //改为线程处理
    class ChatAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... msg) {
            JSONObject json = null;
            if (NEED_DATA_SEND == true) {
                json = postRequestWithOkHttp(msg[1],msg[0]);
                NEED_DATA_SEND = false;
                return null;
            } else {
                json = sendRequestWithOkHttp(cur_page);
                return json;
            }
        }

        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("一开始在这个位置： ", String.valueOf(cur_pos));
            if(jsonObject != null){
                parseJSONWithJSONObject(jsonObject);
                adp = new MsgAdapter(Chat_Activity.this,R.layout.message,msgList);
                adp.notifyDataSetChanged();
                listView.setAdapter(adp);
                if (cur_pos != 0) {
                    listView.setSelection(cur_pos);
                    Log.d("cur_pos", String.valueOf(cur_pos));
                }
                //设置滑动到顶部加载下一页，滑动到底部刷新数据
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (total_pos != totalItemCount) {
                            if (cur_pos == 0) {
                                cur_pos = totalItemCount - total_pos;
                            } else {
                                total_pos = totalItemCount;
                            }
                            Log.d(TAG, "onScroll: " + total_pos);
                        }
                        if (firstVisibleItem == 0) {
                            View firstVisibleItemView = listView.getChildAt(0);
                            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                                /**滚动到顶了*/
                                Log.d("listview check", "onScroll:On the top now!");
                                if (cur_page < total_page) {
                                    cur_page++;
                                    Log.d("onScroll: cur_page: ", String.valueOf(cur_page));
                                    new ChatAsyncTask().execute();
                                    Toast.makeText(Chat_Activity.this, "loading...", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Chat_Activity.this, "No chat file found!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    }
                });


            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * This part is using for judging what incident will happen
     * when clicking the refresh button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fresh:
                cur_page = 1;
                msgList.clear();
                new ChatAsyncTask().execute();
                listView.setSelection(listView.getCount()-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private JSONObject postRequestWithOkHttp(final String my_name, final String msgContent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody reqBody = new FormBody.Builder()
                        .add("chatroom_id", room_ID)
                        .add("user_id", STUDENT_ID)
                        .add("name", my_name)
                        .add("message", msgContent)
                        .build();
                Log.d(TAG, msgContent);
                String url = "http://iems5722.albertauyeung.com/api/asgn2/send_message";
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .post(reqBody)
                        .build();
                Log.d(TAG, url);

                try {
                    Response res = client.newCall(request).execute();
                    String resData = res.body().string();
                    Log.d(TAG, resData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return null;
    }

    private JSONObject sendRequestWithOkHttp(final int currentpage) {
        JSONObject json = null;
        Response res = null;
        String appendix = "";
        String basic = "http://iems5722.albertauyeung.com/api/asgn2/get_messages?";
        String page = String.valueOf(currentpage);

        appendix = "chatroom_id=" + room_ID + "&page=" + page;
        String URL_get_messages = basic + appendix;
        OkHttpClient cl = new OkHttpClient();
        Request req = new Request.Builder()
                .url(URL_get_messages).build();
        try {
            res = cl.newCall(req).execute();
            String responseData = res.body().string();
            json = new JSONObject(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private void parseJSONWithJSONObject(JSONObject json) {
        try {
            JSONArray jsonArray = json.getJSONArray("data");
            total_page = (int) json.get("total_pages");
            Log.d(TAG, "parseJSONWithJSONObject: " + total_page);
            if (json.get("status").equals("OK")) {
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    String Mes = jsonArray.getJSONObject(i).getString("message");
                    String Name = jsonArray.getJSONObject(i).getString("name");
                    String timestamp = jsonArray.getJSONObject(i).getString("timestamp");
                    String user_id = jsonArray.getJSONObject(i).getString("user_id");
                    int MES_TYPE;
                    if (!user_id.equals(STUDENT_ID)) {//if user_id 不等于自己的ID才置为received
                        MES_TYPE = Message.Received;
                    } else {
                        MES_TYPE = Message.send;
                    }
                    Message msg = new Message(Mes, MES_TYPE, timestamp, Name);
                    msgList.add(0, msg);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
