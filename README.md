# Projekt: Battleship (Fr/2, Kr)

> Unser Projekt ist "Schiffe versenken". Unser Spiel spielt man für gewöhnlich zu zweit, 
  sie benötigen also 2 Rechner im selben Netzwerk oder zum Testen einfacher zwei unterschiedliche Browser.
  Zum starten des Spiels muss ein Spieler (oder einer der Browser) ein neues Spiel erstellen,
  dies kann mit dem Button "New Game" gemacht werden, anschließend kann man dem Spiel noch einem Namen geben und die Game-ID abspeichern.
  Mit der Game-Id kann der andere Spieler dann über „Join Game“ dem Spiel joinen. Es ist nicht möglich, dass 3 Spieler in einem Spiel sind,
  aber es können natürlich zur gleichen Zeit mehrere Spiele existieren.
  Hat das Spiel nun angefangen ist die erste Phase, die Schiffe zu setzten. Dies erfolgt durch anklicken auf dem großem Spielfeld.
  Oben links bekommt man angezeigt wie viele Schiffe man noch setzten muss, man beginnt beim Setzten mit dem größtem Schiff.
  Haben beide Spieler ihre Schiffe gesetzt, beginnt das eigentliche Spiel. 
  In der Mitte befindet sich jetzt das gegnerische Spielfeld, links oben und unten das eigene Feld sowie die eigenen Schiffe.
  Oben rechts sieht man die gegnerischen Schiffe (rot bedeutet Schiff wurde schon "versenkt").
  Abwechselnd können nun die Spieler das gegnerische Spielfeld "aufdecken". Hat ein Spieler gewonnen wird dies angezeigt und das Spiel ist beendet.

![Screenshot](Screenshot-Battleship.png)

Keywords: Bootstrap, Server-Sent Events (SSE), Accessmanager, ctx.render(), Routes, Path

Projektbeteiligte:

* Jannik Lapp
* Max Stephan


<Inhaltsverzeichnis>
//TODO Inhaltsverzeichnis

# Server-Sent Events (SSE)

Um vom Server Daten an den Klient zu senden sind Server-Sent Events eine einfache und gute Möglichkeit. In Javalin muss man die Verbindungs Adresse wie folgt deklariren: 

```Java
app.sse("/sse", client -> {
    //Daten Senden
    client.sendEvent("data");

    client.onClose( () -> { 
        //Handle Conection Closed 
    });
});
```

In dieser Methode definiert man was beim verbinden Passiert und mit client.onClose() kann man dann noch definieren was beim Beenden Passieren soll. Man bekommt einen SSE client übergeben der sozusagen die Verbindung darstellt. Mit `client.sendEvent("data")` kann man dann daten senden. Und mit client.ctx kommt man an den Context von Javalin. Nach dem verbindungs aufbau sollte man sich den client speichern um weiterhin daten Senden zu Können. In Java Script baut man wie folgt die Verbindung auf: 

```js
//Client mit SSE verbinden "/sse" -> muss in app.sse festgelegtem pfad entsprechen
var eventSource = new EventSource("//" + location.hostname + ":" + location.port + "/sse"); 
```

Um die vom Server gesendeten daten beim Client zu verarbeiten muss man einen Event Listener definieren:  //TODO erklären mit e

```js
//Antwort Listener
eventSource.addEventListener('message', e => {
    //Antwort verarbeiten
    console.log(e.data);
});
```

Wenn man mehre verschidenen daten Senden will, die unterschiedlich vom Client verarbeitet werden sollen konn man beim Senden auch einen event namen festlegen: 

```Java
//Daten mit bestimmtem event Senden
client.sendEvent("event", "data");
```

Um dann in Java Script die daten zu verarbeiten muss man dann für jedes event einen eigenen Listener anlegen: 

```js
//Antwort Listener mit eigenem Event
eventSource.addEventListener('event', e => {
    //Antwort verarbeiten
    console.log(e.data);
});
```

Wenn man in den Unterschiedlichen events auch noch eine unterscheidung zwichen den daten machen will kan man auch noch IDs angeben:  

```Java
//Daten mit bestimmtem event + id Senden
client.sendEvent("event", "data", "1");
```

In Java Script kann man dann wie folgt auf die IDs zugreifen: 

```js
//TODO with id 
```

# Accessmanager

```Java
//AccesManager konfigurieren
app.accessManager((handler, ctx, permittedRoles) -> {
    MyRole userRole = getUserRole(ctx);
    if (permittedRoles.contains(userRole)) {
        handler.handle(ctx);
    } else {
        ctx.status(401).result("Unauthorized");
    }
});

Role getUserRole(Context ctx) {
    //Benutzer Role herausfinden und zurückgeben 
}

//Mögliche Rollen festlegen 
enum MyRole implements Role {
    ANYONE, ROLE_ONE, ROLE_TWO, ROLE_THREE;
}
```

> **Achtung**:  app.accesManager muss vor app.start() aufgerufen werden

```Java
app.get("/test", ctx -> { 
        //Normaler get code 
    }, Set<Role>);
    //das Set<Role> legt die erlaubten Rollen fest 
```

# ctx.render()

```Java
app.get("/page", ctx -> {
    //Some code
    ctx.render("path");
})
```

> **Achtung**: Das Rendern von Verchiedenen Datei typen benötigt meist andere dependencies, um herauszufinden welche kann man einfach den Code einmal aufrufen und in der Konsole wird einem dann die benötigte dependencie angezeigt und man kann sie zu build.gradle hinzufügen.

//TODO pfad erklären ab resources 

>**Achtung**: Beim Rendern von **Markdown** dateien muss der **Datei Pfad** mit einem **'/'** beginnen da dies intern mit //TODO genaue bezeichnung

```Java
//Datei bekommen 
ctx.render(path).resultString();
//ctx.render mit SSE
client.sendEvent("Key", client.ctx.render(path).resultString());
```

# Routes

```Java
app.routes(() -> {
    get("/get", ctx -> { /*Some Code*/ });
    post("/post", ctx -> { /*Some Code*/});
});
```
//TOD mit path usw. 

>**Achtung**: Damit man get usw. aufrufen kann muss man den APiBuilder importieren: 

`import static io.javalin.apibuilder.ApiBuilder.*;`

# Path

//TODO 

//vtl. noch ein ctx.Header, ctx.sessionAttribute("Name", ctx.queryParam("Name")), Javalin Exeptions, vtl. noch igenwas von js;

# Streams
```Java
return field.stream()
            .mapToInt(n -> n.getLeft())
            // Da field eine Arraylist aus SimpleMaps ist (welche Generisch ist) müssen die Werte erst mit MapToInt zu einem Int Array geparst werden
            .filter(n -> n % 2 == 0)
            // Der Filter sorgt dafür, dass wir nur alle 2en aufsummieren, da die 2 in unserem Fall "nicht zerstörtes Schiff bedeutet"
            .sum() == 0;
```

```Java
public static Player getPlayer(Context ctx)  throws NoSuchElementException {
return players.stream()
             // in player sind alle spieler+spielerIDs gespeichert, der stream durchläuft alle
            .filter(p -> p.getID().equals(ctx.sessionAttribute("Player-ID")))
            // es werden die Spieler die, die selbe ID haben wie im ctx attribute Player ID mitangegegeben herausgefilter (Logischerweiße gibt es immer nur maximal 1 Treffer)
            .findFirst()
            // der Erste wird zurückgegeben
            .orElseThrow();
            // oder eine NoSuchElementException falls kein Spieler gefunden wurde
}

```

```Java
public static Game getGame(Context ctx) throws NoSuchElementException {
        return players.stream()
                // durchläuft wieder alle Spieler
                .filter(p -> p.getGame() != null)
                // filtert alle Spieler raus die ein Game haben
                .map(p -> p.getGame())
                // da Game benötigt wird, wird sich von jedem Spieler das Game geholt
                .filter(g -> g.getId().equals(ctx.queryParam("Game")))
                // filtert alle games die, die selbe game-id haben wie im ctx attribute game
                .findFirst()
                // das Erste zurückgeben
                .orElseThrow();
                // oder falls nichts gefunden wieder eine NoSuchElementException
    }
```