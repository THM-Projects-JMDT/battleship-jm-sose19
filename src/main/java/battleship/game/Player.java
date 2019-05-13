package battleship.game;

import java.util.Arrays;
import io.javalin.serversentevent.SseClient;

public class Player {
    private String id;
    private SseClient client;
    private SimpleMap<Integer,Integer> [] field;
    private int log=0;
    private Game game;
    private int[] shipslength ={2,2,2,2,3,3,3,4,4,5};
    private final int[] shipsize =shipslength;
    private boolean[] firstfieldship={true,true,true,true,true,true,true,true,true,true};

    public Player(String id){
        this.id = id;
        field =  new SimpleMap[100];
    }


    public String getID(){ return this.id; }
    public SseClient getClient() { return this.client; }
    public void setClient(SseClient client) { this.client = client; }
    public Game getGame() { return this.game; }


    public boolean setships(int feld){
        if(shipslength[0]==0)
            // game auf neuen stand setzen setzen beendet
            return false;
        int counter= shipslength.length-1;
        while(shipslength[counter]==0)
            counter--;

        if(firstfieldship[counter]==true) {
            firstfieldship[counter] = false;
            shipslength[counter]--;
            log = feld;
            field[feld]=new SimpleMap<>(2,counter);
            return true;
        }else{
            int temp=log-feld;
            if(temp==10||temp==-10||temp==1||temp==-1) {
                shipslength[counter]--;
                log = feld;
                field[feld]=new SimpleMap<>(2,counter);
                return true;
            }

        }
        return false;

    }

    public String getshipstatus(){
        String ausgabe="";
        for(int i=0;i<shipsize.length-1;i++){
            int[] temp=new int[shipsize[i]-1];
            int x = 0;
                for(int j=0;j<field.length;j++) {
                    if (field[j].getRight() == i)
                        temp[x++] = field[j].getRight();
                }

                 for(int y=0;y<temp.length;y++){
                     if(temp[y]==0)
                         if(temp[y]==2)
                             if(temp[y]==3)

            }

        }
        return ausgabe;
    }

    public int checkifthersaship(int i){
        return field[i++].getLeft();
    }
    public boolean canilookatthisfield(int i){
        if (field[i].getLeft()==1 ||field[i].getLeft()==3)
            return false;
        return true;
    }
    public boolean checkifend(){
        return Arrays.stream(field).filter(n -> (int) n.getLeft() % 2 == 0).sum()==0;
    }

}
