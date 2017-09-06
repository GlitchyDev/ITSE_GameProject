package GameInfo;

import GameInfo.Environment.World;

import java.util.ArrayList;

/**
 * Created by Robert on 8/27/2017.
 * The Purpose of this ClassL
 * - Represents a Game Instance connected to a Server
 * - Holds reference to the players that are included, and its socket object
 */
public class Client {
    private ArrayList<Player> players;

    public Client(ArrayList<Player> players)
    {
        this.players = players;
    }
    public Client(Player p)
    {
        this.players = new ArrayList<>();
        this.players.add(p);
    }



    public boolean isLocalClient()
    {
        return players.size() > 1;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }
}
