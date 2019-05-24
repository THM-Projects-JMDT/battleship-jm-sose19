package battleship.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import io.javalin.serversentevent.SseClient;

public class Player {
    private String id;
    private SseClient client;
    private ArrayList<SimpleMap<Integer, Integer>> field;
    private Game game;
    private int[] shipslength = {2, 2, 3, 3, 4, 5};
    private final int[] shipsize = Arrays.copyOf(shipslength, shipslength.length);
    private boolean hitBoat; 

    private boolean myTurn = false;

    public Player(String id) {
        this.id = id;
        field = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            field.add(new SimpleMap<>(0, -1));
        }
    }

    public String getID() {
        return this.id;
    }

    public SseClient getClient() {
        return this.client;
    }

    public void setClient(SseClient client) {
        this.client = client;
    }

    public Game getGame() {
        return this.game;
    }

    public boolean hitBoat() {
        return this.hitBoat;
    }

    public boolean newGame() {
        if (this.game != null)
            return false;
        this.game = new Game(this);
        return true;
    }

    public boolean newGame(Game game) {
        if (this.game != null)
            return false;
        if (game.joingame(this)) {
            this.game = game;
            return true;
        }
        return false;
    }
    public int getshipslength(){
        return shipslength[0];
    }

    public boolean setships(int feld) {
        if (shipslength[0] == 0)
            // game auf neuen stand setzen, setzen beendet
            return false;
        int counter = shipslength.length - 1;
        while (shipslength[counter] == 0)
            counter--;
        if (field.get(feld).getRight() != -1) return false;

        int[] temp = new int[shipsize[counter] - shipslength[counter]];
        if (temp.length == 0) {
            shipslength[counter]--;
            field.set(feld,new SimpleMap<>(2, counter));
            if(shipsize[counter]-shipslength[counter]==shipsize[counter])
                client.sendEvent("ShipReady","ShipReady");
            return true;
        }
        int j = 0;
        for (int i = 0; i < field.size(); i++) {
            if (field.get(i).getRight() == counter) temp[j++] = i;
        }
        if ((temp[0] % 10 == 0 || feld % 10 == 0) && (temp[0] % 10 == 9 || feld % 10 == 9)) return false;
        if (temp.length == 1 && (temp[0] - feld == 10 || temp[0] - feld == -10 || temp[0] - feld == 1 || temp[0] - feld == -1)) {
            shipslength[counter]--;
            field.set(feld,new SimpleMap<>(2, counter));
            if(shipsize[counter]-shipslength[counter]==shipsize[counter])
                client.sendEvent("ShipReady","ShipReady");
            return true;
        }
        if (temp.length > 1) {
            if ((temp[temp.length - 1] % 10 == 0 || feld % 10 == 0) && (temp[temp.length - 1] % 10 == 9 || feld % 10 == 9))
                return false;
            int multiplikator = 1;
            if (temp[1] - temp[0] == 10) {
                multiplikator = 10;
            }
            if (temp[0] - multiplikator == feld) {
                shipslength[counter]--;
                field.set(feld,new SimpleMap<>(2, counter));
                if(shipsize[counter]-shipslength[counter]==shipsize[counter])
                    client.sendEvent("ShipReady","ShipReady");
                return true;
            }
            if (temp[temp.length - 1] + multiplikator == feld) {
                shipslength[counter]--;
                field.set(feld,new SimpleMap<>(2, counter));
                if(shipsize[counter]-shipslength[counter]==shipsize[counter])
                    client.sendEvent("ShipReady","ShipReady");
                return true;
            }
        }
        return false;
    }

    public String getshipstatus() {
        String ausgabe = "";
        for (int i = 0; i < shipsize.length; i++) {
            ausgabe += "<tr>\n";
            int[] temp = new int[shipsize[i]];
            int x = 0;
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j).getRight() == i)
                    temp[x++] = field.get(j).getLeft();
            }

            for (int y = 0; y < temp.length; y++) {
                if (temp[y] == 0) {
                    ausgabe += String.format(" <td style=\"background-color: #fff;\"></td>\n");
                }
                if (temp[y] == 2) {
                    ausgabe += String.format(" <td style=\"background-color: #080;\"></td>\n");

                }
                if (temp[y] == 3) {
                    ausgabe += String.format(" <td style=\"background-color: #f00;\"></td>\n");

                }


            }
            ausgabe += "</tr>\n";

        }
        return ausgabe;
    }

    public String getfield(boolean hauptfeld, boolean phase2) {
        String ausgabe = "";
        if(hauptfeld==true) {
            ausgabe += String.format("<tr>\n" +
                    "                <td style=\"background-color: #bbb;\"></td>\n" +
                    "                <td style=\"background-color: #eff;\">A</td>\n" +
                    "                <td style=\"background-color: #eff;\">B</td>\n" +
                    "                <td style=\"background-color: #eff;\">C</td>\n" +
                    "                <td style=\"background-color: #eff;\">D</td>\n" +
                    "                <td style=\"background-color: #eff;\">E</td>\n" +
                    "                <td style=\"background-color: #eff;\">F</td>\n" +
                    "                <td style=\"background-color: #eff;\">G</td>\n" +
                    "                <td style=\"background-color: #eff;\">H</td>\n" +
                    "                <td style=\"background-color: #eff;\">I</td>\n" +
                    "                <td style=\"background-color: #eff;\">J</td>\n" +
                    "\n" +
                    "            </tr>");
        }
        for (int i = 0; i < field.size(); i++) {
            if (i % 10 == 0)
                if(hauptfeld==true) {
                    ausgabe += " <tr>\n" + String.format("<td style=\"background-color: #eff;\">") + (i / 10 + 1) + "</td>\n";
                } else{
                    ausgabe += " <tr>\n";
                }
            if (field.get(i).getLeft() == 0) {
                if (hauptfeld == false) {
                    ausgabe += String.format("<td style=\"background-color: #fff;\"></td>\n");
                }else {
                    ausgabe += String.format("<td onclick=\"sendMove(") + i + String.format(")\" style=\"background-color: #fff;\"></td>\n");
                }
            }
            if (field.get(i).getLeft() == 1) {
                    ausgabe += String.format("<td style=\"background-color: #888;\"></td>\n");
            }
            if (field.get(i).getLeft() == 2) {
                if (phase2==true) {
                    ausgabe += String.format("<td style=\"background-color: #080;\"></td>\n");
                } else {
                    ausgabe += String.format("<td onclick=\"sendMove(") + i + String.format(")\" style=\"background-color: #fff;\"></td>\n");
                }
            }
            if (field.get(i).getLeft() == 3) {
                if (hauptfeld == false) {
                    ausgabe += String.format("<td style=\"background-color: #f00;\"></td>\n");
                } else {
                    ausgabe += String.format("<td onclick=\"sendMove(") + i + String.format(")\" style=\"background-color: #f00;\"></td>\n");
                }
            }
            if (i % 10 == 9)
                ausgabe += "</tr>\n";
        }

        return ausgabe;
    }

    public boolean move(int i) {
        if (!myTurn)
            return false;

        hitBoat = field.get(i).getLeft() == 2;

        if (field.get(i).getLeft() == 0 || hitBoat) {
            field.get(i).setLeft(field.get(i).getLeft() + 1);
            return true;
        }
        return false;
    }

    public boolean checkifend() {
        return field.stream().mapToInt(n -> n.getLeft()).filter(n -> n % 2 == 0).sum() == 0;
    }

    public void changeMyTurn(){
        myTurn = !myTurn;
    }

    public void beendegame(){
        myTurn=false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Player)
            return this.id.equals(((Player) o).id);
        return false;
    }

}