package patrichuan.battledraw;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pat on 02/03/2016.
 */
public class Player {

    private String name;
    private String score;
    private String picture;
    private String inRoom;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Player() {

    }

    public Player(String name) {
        this.name = name;
        score = "0";
        picture = "no_picture";
        inRoom = "no_room";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInRoom() {
        return inRoom;
    }

    public void setInRoom(String inRoom) {
        this.inRoom = inRoom;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("score", score);
        result.put("picture", picture);
        result.put("inRoom", inRoom);
        return result;
    }
}