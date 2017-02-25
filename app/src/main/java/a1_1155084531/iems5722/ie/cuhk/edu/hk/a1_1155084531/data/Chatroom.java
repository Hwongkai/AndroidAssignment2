package a1_1155084531.iems5722.ie.cuhk.edu.hk.a1_1155084531.data;

/**
 * Created by huangkai on 2017/2/18.
 */

public class Chatroom {
    private String content;
    private int id;
    public Chatroom(String content,int id){
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
