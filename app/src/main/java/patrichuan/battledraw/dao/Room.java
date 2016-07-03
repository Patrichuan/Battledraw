package patrichuan.battledraw.dao;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    final String TAG = "Room";

    private String state = "NOT_STARTED";
    private Map<String, String> players;

    public Room() {
        players = new HashMap<>();
    }

    public String getState () {
        return state;
    }

    public void setState (String state) {
        this.state = state;
    }

    public  Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("state", state);
        result.put("players", players);
        return result;
    }

    public void toLog() {
        Log.d(TAG, "state: " + state);
        if (players!=null) {
            for (int i=0; i<=players.size(); i++) {
                Log.d(TAG, "player" + i + ": " + players.get(i));
            }
        }
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }
}