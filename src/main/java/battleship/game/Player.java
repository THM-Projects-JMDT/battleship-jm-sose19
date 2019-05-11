package battleship.game;

import java.util.ArrayList;
import java.util.Arrays;

import io.javalin.serversentevent.SseClient;

public class Player {
    private String id;
    private SseClient client;
    private int [] field;
    private ArrayList<SimpleMap<Integer,Integer,Integer>> log;
    private Game game;

    public Player(String id){
        this.id = id;
        field =  new int[100];
        log = new ArrayList<>();
    }


    public String getID(){ return this.id; }
    public SseClient getClient() { return this.client; }
    public void setClient(SseClient client) { this.client = client; }
    public Game getGame() { return this.game; }

    public void setShipfield(int i){
        field[i]=2;
    }

    public boolean checkfornewshipfield(int field,int shipsize, int shipnumber){
        if(log.get(log.size()-shipsize).getRight()==shipnumber)
            return false;
        if(log.get(log.size()).getLeft()%10 == 0 && field % 10 == 9)
            return false;
        if(log.get(log.size()).getLeft()%10 == 9 && field % 10 == 0)
            return false;
        int [] n = {1,-1,10,-10};
        for (int i : n) {
            if(log.get(log.size()).getLeft()==field+i) {
                log.add(new SimpleMap<>(field, shipsize, shipnumber));
                setShipfield(field);
                return true;
            }

        }
        return false;

    }
    public int checkifthersaship(int i){
        return field[i++];
    }
    public boolean canilookatthisfield(int i){
        if (field[i]==1 ||field[i]==3)
            return false;
        return true;
    }
    public boolean checkifend(){
        return Arrays.stream(field).filter(n -> n % 2 == 0).sum()==0;
    }

}
