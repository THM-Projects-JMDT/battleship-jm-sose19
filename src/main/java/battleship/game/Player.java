package battleship.game;

import java.util.Arrays;
import java.util.stream.Stream;
import io.javalin.serversentevent.SseClient;

public class Player {
    private String id;
    private SseClient client;
    private SimpleMap<Integer, Integer>[] field;
    private Game game;
    private int[] shipslength = {2, 2, 3, 3, 4, 5};
    private final int[] shipsize = Arrays.copyOf(shipslength, shipslength.length);
    private boolean ichbindran= true;

    public Player(String id) {
        this.id = id;
        field = new SimpleMap[100];
        for (int i = 0; i < field.length; i++) {
            field[i] = new SimpleMap<>(0, -1);
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
        if (field[feld].getRight() != -1) return false;

        int[] temp = new int[shipsize[counter] - shipslength[counter]];
        if (temp.length == 0) {
            shipslength[counter]--;
            field[feld] = new SimpleMap<>(2, counter);
            if(shipsize[counter]-shipslength[counter]==shipsize[counter])
                client.sendEvent("ShipReady","ShipReady");
            return true;
        }
        int j = 0;
        for (int i = 0; i < field.length; i++) {
            if (field[i].getRight() == counter) temp[j++] = i;
        }
        if ((temp[0] % 10 == 0 || feld % 10 == 0) && (temp[0] % 10 == 9 || feld % 10 == 9)) return false;
        if (temp.length == 1 && (temp[0] - feld == 10 || temp[0] - feld == -10 || temp[0] - feld == 1 || temp[0] - feld == -1)) {
            shipslength[counter]--;
            field[feld] = new SimpleMap<>(2, counter);
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
                field[feld] = new SimpleMap<>(2, counter);
                if(shipsize[counter]-shipslength[counter]==shipsize[counter])
                    client.sendEvent("ShipReady","ShipReady");
                return true;
            }
            if (temp[temp.length - 1] + multiplikator == feld) {
                shipslength[counter]--;
                field[feld] = new SimpleMap<>(2, counter);
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
            for (int j = 0; j < field.length; j++) {
                if (field[j].getRight() == i)
                    temp[x++] = field[j].getLeft();
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

    public String getfield(boolean sichtweiße) {
        String ausgabe = "";
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
        for (int i = 0; i < field.length; i++) {
            if (i % 10 == 0)
                ausgabe += " <tr>\n" + String.format("<td style=\"background-color: #eff;\">") + (i / 10 + 1) + "</td>\n";
            if (field[i].getLeft() == 0) {
                if (sichtweiße == false) {
                    ausgabe += String.format("<td style=\"background-color: #fff;\"></td>\n");
                }else {
                    ausgabe += String.format("<td onclick=\"sendMove(") + i + String.format(")\" style=\"background-color: #fff;\"></td>\n");
                }
            }
            if (field[i].getLeft() == 1) {
                if (sichtweiße == false) {
                    ausgabe += String.format("<td style=\"background-color: #888;\"></td>\n");
                } else {
                    ausgabe += String.format("<td onclick=\"sendMove(") + i + String.format(")\" style=\"background-color: #888;\"></td>\n");
                }
            }
            if (field[i].getLeft() == 2) {
                if (sichtweiße == true) {
                    ausgabe += String.format("<td style=\"background-color: #080;\"></td>\n");
                } else {
                    ausgabe += String.format("<td onclick=\"sendMove(") + i + String.format(")\" style=\"background-color: #fff;\"></td>\n");
                }
            }
            if (field[i].getLeft() == 3) {
                if (sichtweiße == false) {
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

    public boolean canilookatthisfield(int i) {
        if (ichbindran==false &&(field[i].getLeft() == 1 || field[i].getLeft() == 3))
            return false;
        field[i].setLeft(field[i].getLeft()+1);
        return true;
    }

    public boolean checkifend() {
        return Stream.of(field).mapToInt(n -> n.getLeft()).filter(n -> n % 2 == 0).sum() == 0;
    }

    public void changeichbindran(){
        if(ichbindran==false) {
            ichbindran = true;
        }else {
            ichbindran = false;
        }
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