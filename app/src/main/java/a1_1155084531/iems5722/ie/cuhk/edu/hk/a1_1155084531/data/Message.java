package a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.data;

import java.sql.Time;

/**
 * Created by huangkai on 2017/2/6.
 */

public class Message {
    public static final int Received = 0;
    public static final int send=1;
    private String content;
    private int type;
    private String time;
    private String name;

    public Message(String content, int type, String time,String name){
        this.content = content;
        this.type = type;
        this.time = time;
        this.name = name;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }
    public String getTime(){
        return time;
    }
    public String getName() {
        return name;
    }

}
