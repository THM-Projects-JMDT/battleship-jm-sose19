# Projekt: Battleship (Fr/2, Kr)

> Unser Projekt ist das Spiel "Schiffe versenken". Man Spielt es für gewöhnlich zu zweit,
sie benötigen also 2 Rechner im selben Netzwerk oder zum Testen einfacher zwei unterschiedliche Browser. Zum Starten des Spiels muss ein Spieler ein neues Spiel erstellen, dies kann mit dem Button "New Game" gemacht werden, anschließend kann man noch seinen Namen angeben und die Game-ID abspeichern. Mit der Game-Id kann der andere Spieler dann über „Join Game“ dem Spiel beitreten. Es ist nicht möglich, dass 3 Spieler in einem Spiel sind, aber es können natürlich zur gleichen Zeit mehrere Spiele existieren. Hat das Spiel nun angefangen beginnt man, damit die Schiffe zu setzten. Dies erfolgt durch anklicken auf dem großem Spielfeld. Oben links bekommt man angezeigt, wie viele Schiffe man noch setzten muss. Dabei begint man mit dem Größten schiff. Haben beide Spieler ihre Schiffe gesetzt, beginnt das eigentliche Spiel. In der Mitte befindet sich jetzt das gegnerische Spielfeld, links oben und unten das eigene Feld sowie die eigenen Schiffe. Oben rechts sieht man die gegnerischen Schiffe (rot bedeutet Schiff wurde schon "getroffen"). Abwechselnd können nun die Spieler das gegnerische Spielfeld "aufdecken". Hat ein Spieler gewonnen wird dies angezeigt und das Spiel ist beendet.

![Screenshot](Screenshot-Battleship.png)

Keywords: Bootstrap, Server-Sent Events (SSE), Access Manager, ctx.render(), Routes, Javalin Exeptions, Session Attribute, Path

Projektbeteiligte:

* Jannik Lapp
* Max Stephan

# Inhalt

* [Server-Sent Events (SSE)](#Server-Sent-Events-(SSE)) 
* [Access Manager](#Access-Manager)
* [ctx.render()](#ctx.render())
* [Routes](#Routes)
* [Javalin Exeption](#Javalin-Exeption)
* [Streams](#Streams)
* [Javalin Exeptions](#Javalin-Exeptions)
* [Session Atribute](#Session-Atribute)
* [Path](#Path)

# Server-Sent Events (SSE)

Um vom **Server** Daten an den **Client** zu **senden** sind Server-Sent Events eine einfache und gute Möglichkeit. In Javalin muss man diese wie folgt deklariren:

```Java
app.sse("/sse", client -> {
    //Daten Senden
    client.sendEvent("data");
    
    //SomeCode

    client.onClose( () -> { 
        //some Code
    });
});
```

In dieser Methode definiert man was beim **Verbinden** passiert und mit **client.onClose()** kann man, dann noch definieren was nach dem **Verbindungs abbau** passiert. Man bekommt einen **SSE client** übergeben der alle **Verbindungsdetails** beinhaltet. Mit `client.sendEvent("data")` kann man Daten an den Client **sende**. Und mit `client.ctx` bekommt man den **Context** von Javalin. Nach dem Verbindungsaufbau sollte man sich den **client speichern** um weiterhin Daten Senden zu Können. In **Java Script** baut man wie folgt die Verbindung auf:

```js
//Client mit SSE verbinden
var eventSource = new EventSource("http://" + location.hostname + ":" + location.port + "/sse"); 
//"/sse" -> muss dem in app.sse festgelegtem pfad entsprechen
```

Um die vom Server gesendeten daten beim Client zu verarbeiten, muss man einen **Event Listener** definieren:

```js
//Antwort Listener
eventSource.addEventListener('message', e => {
    //Antwort verarbeiten
    console.log(e.data);
});
```

Man bekommt dann das **event e** übergeben und kann mit `e.data` auf die gesendeten **Daten zugreifen**. 

Wenn man verschiedene Daten Senden will, die **unterschiedlich** vom Client verarbeitet werden sollen, kann man beim Senden auch einen **Event Namen** festlegen:

```Java
//Daten mit bestimmtem Event Senden
client.sendEvent("event", "data");
```

Um in **Java Script** die daten zu verarbeiten, muss man für jedes Event einen **eigenen Listener** anlegen:

```js
//Antwort Listener mit eigenem Event
eventSource.addEventListener('event', e => {
    //Antwort verarbeiten
    console.log(e.data);
});
```

# Access Manager

Um in Javalin **sicherstellen** zu können, wer eine **Anfrage stellen darf** kann man den Access Manager Verwenden. So kann man den Access Manager konfigurieren:

```Java
//AccesManager konfigurieren
app.accessManager((handler, ctx, permittedRoles) -> {
    //Rolle des Benutzers bekommen
    MyRole userRole = getUserRole(ctx);
    //Wenn erlaubt dann Code ausführen
    if (permittedRoles.contains(userRole)) {
        handler.handle(ctx);
    } else {
        //Sonst mit einem 401 antworten
        ctx.status(401).result("Unauthorized");
    }
});
```

> **Achtung**:  app.accesManager muss **vor** app.start() aufgerufen werden

Der Acces Manager bekommt einen **Handler übergeben**, den **Context** und ein **Set** mit den **erlaubten Rollen**. Er testet dann ob der Client die **benötigte berechtungung** besitzt, ist dies der fall wird der **Handler aufgeführt**. Wenn nicht wurde ein **"Unauthoried"** zurückgegeben. 

Um die **Rollen** zu **definieren**, muss man eine **enum** erstellen das `Role` **implementiert**. Dann benötigt man nur noch eine Methode, mit der man die Rolle des Benutzers bekommt:

```Java
//Mögliche Rollen festlegen 
enum MyRole implements Role {
    ANYONE, ROLE_ONE, ROLE_TWO, ROLE_THREE;
}

Role getUserRole(Context ctx) {
    //Benutzer Rolle herausfinden und zurückgeben
}
```

Nachdem man den Acces Manager konfiguriert hat, muss man nur noch **festlegen** welche **Berechtigungen** für die **anfragen** benötigt werden:   

```Java
app.get("/test", ctx -> { 
        //some Code
    }, Set<Role>);
    //das Set<Role> legt die erlaubten Rollen fest 
```

Dies funktioniert **nicht nur** mit `app.get()`, sondern auch mit `app.post()` oder auch bei der **SSE Definition**.

> **Tip**: Javalin lässt einem bei der Definition des Acces Managers viel **Spielraum** und somit benötigt man die Methode `getUserRole()` **nicht** um die Berechtigung zu überprüfen, man kann dies auch **anders lösen**. Wie wir das bei unserm Programm auch gemacht haben:

```Java
Javalin app = Javalin.create()
.enableStaticFiles("/public")
.accessManager((handler, ctx, permittedRoles) -> {
    //Wenn AccessRole.ANYONE übergeben wird ->  immer ausführen
    if (permittedRoles.contains(AccessRole.ANYONE))
        handler.handle(ctx);
    //Wenn der Client ein Spieler ist
    else if(Players.isPlayer(ctx))
        //Und AccessRole.REAGISTERED übergeben wurde -> ausführen
        if(permittedRoles.contains(AccessRole.REGISTERED))
            handler.handle(ctx);
        //oder AccessRole.INGAME übergeben wurde und der Spieler ein spiel hat -> ausführen
        else if(permittedRoles.contains(AccessRole.INGAME) && Players.hasGame(ctx))
            handler.handle(ctx);
    //sonst mit einem Unauthorized antworten
    else
        ctx.status(401).result("Unauthorized");
};)
.start(7000); 
```

In unserem Programm **stellen** wir mit dem Acess Manager **sicher**, das es den **Spieler gibt** bzw. das er auch ein **Spiel** hat, um `NullPointerExceptions` zu **vermeiden**.

# ctx.render()

Mit Javalin kann man einige **Dateitypen** Rendern lassen (Aktuell sind es 6 Template Engins), um somit einfach HTML Dokumente als Antwort zu senden: 

```Java
app.get("/page", ctx -> {
    //Some code
    ctx.render("path");
})
```

> **Achtung**: Das Rendern von verschiedenen Datei Typen benötigt meist andere **Abhänigkeiten**, um herauszufinden welche kann man einfach den **Cod**e einmal ausführen und in der Konsole wird einem dann eine **Fehler** Meldung mit der **benötigte Abhänigkeit** angezeigt und man kann diese einfach zu **build.gradle** hinzufügen.

Bei der Pfad Angabe ist das Startverzeichnis der **"resources"** Ordner. Javalin verwendet immer die zur Dateiendung **passende** Rendering **Engin**e. Falls diese unterstützt wird.

>**Achtung**: Beim Rendern von **Markdown** Dateien muss der **Datei Pfad** mit einem **"/"** beginnen da Javalin sonst die Dateien nicht findet. 

Wenn man auch **nicht unterstützte** Dateien rendern will, kann man dies **selber definieren**, das wird [hier](https://javalin.io/documentation#faq) gut beschrieben.

Man kann auch noch ein bei `ctx.render()` auch noch ein **Modell** übergeben, damit kann man **Werte Paare** übergeben, um **variablen** in Dateien zu **ersetzen**. Dies haben wir allerdings **nicht verwendet** und somit können wir hier **keine genauere Erklärung** dazu Liefern.

Um `ctx.render()` auch z.B. in **Server-Send Event verwenden** zu können, kann man die Methode `ctx.resultString()` verwenden:

```Java
//Datei bekommen 
ctx.render(path).resultString();
//ctx.render mit SSE
client.sendEvent("Key", client.ctx.render(path).resultString());
```

# Routes

Um einem etwas **schreibarbeit** zu **ersparen**, kann man in Javalin `app.routes()` verwenden:

```Java
import static io.javalin.apibuilder.ApiBuilder.*;

//Klassen definition und anderer Code  

app.routes(() -> {
    get("/get", ctx -> { /*Some Code*/ });
    post("/post", ctx -> { /*Some Code*/});
});
```

>**Achtung**: Damit man das funktioniert muss man den **APiBuilder importieren**.

Man kann auch noch `path()` verwenden um die **Pfade zu setzen** und diese auch zu **schachteln**: 

```Java
app.routes(() -> {
    path("users", () -> {
        get(/*Some Code*/);
        post(/*Some Code*/);
        path(":id", () -> {
            get(/*Some Code*/);
        });
    });
});
```

> **Tip**: Man kann die **Hadler** in Javalin auch in andere **Klassen auslagern**. Dies sorgt für eine Bessere **Strukturierung** und erhöht die **Übersichtlichkeit** des Codes:

```Java
//Main Klasse
app.get("/getpage", PageController.getPage);

//Andere Klasse
public static Handler getPage = ctx -> {
    //Some Code
};
```

# Javalin Exeptions

Es gibt in Javalin einige **vordefiniterte HttpResponse Exeptions** die man verwenden kann, um auf fehlerhaft Request zu reagieren wie z.B. `throw new BadRequestResponse("nachicht")`: Diese Exeption "beantwortet" den Request mit dem **HTTP Status Code** `400`. Alle HttpResponse Exeptions sind [hier](https://javalin.io/documentation#default-responses) gut erklärt.

# Session Atribute

Um **Daten** einem Bestimmten **Client zuordnen** zu können, kann man diese als Session Attribute speichern: 

```Java
//Daten Speichern
ctx.sessionAttribute("key", "value");
```

Und so wieder Lessen: 

```Java
//Daten lesen
String data = ctx.sessionAttribute("key");
```

Wenn man **keine** weiteren **Einstellungen** vornimmt werden die Dateien nur im **Arbeitsspeicher** des Servers zwischengespeichert und sind nach dem **Neustart nicht** mehr **vorhanden**. Wenn man will, das die Daten auch nach einem Neustart noch vorhanden sind oder man sie **nicht** im Arbeitsspeicher haben will, kann man die **Konfiguration** des Session Handlers **ändern**:

```Java
//Main Methode
app.sessionHandler(/*file Session Handler Configuriren*/)
```

> **Achtung:** `app.sessionHandler()` muss **vor** `app.start()` aufgerufen werden. 

Dies haben wir in Unserm Programm zwar **nicht** verwendet, wir hatten das allerdings erst vor und somit konnte ich auch ein **Beispiel** erstellen:

```Java
//Main Methode
app.sessionHandler(() -> fileSessionHandler());

//Auserhalb der main Methode
static SessionHandler fileSessionHandler() {
    //Eigenen SessionHandeler erstellen
    SessionHandler sessionHandler = new SessionHandler();
    //Neuen session cach erstellen
    SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
    //speicherort festlegen
    sessionCache.setSessionDataStore(fileSessionDataStore());
    sessionHandler.setSessionCache(sessionCache);
    sessionHandler.setHttpOnly(true);
    return sessionHandler;
}

//Speicher -ort und -art festlegen
static FileSessionDataStore fileSessionDataStore() {
    FileSessionDataStore fileSessionDataStore = new FileSessionDataStore();
    File baseDir = new File(System.getProperty("java.io.tmpdir"));
    File storeDir = new File(baseDir, "javalin-session-store");
    storeDir.mkdir();
    fileSessionDataStore.setStoreDir(storeDir);
    return fileSessionDataStore;
}
```

Dies ist nur ein **Anwendungsbeispiel,** man kann die Daten auch in einer **Datenbank speichern**. Dafür gibt es [hier](https://javalin.io/tutorials/jetty-session-handling-java) eine gutes Beispiel.

# Path

> **Tip**: Wir haben bei unserem Programm um die **Pfadverwaltung** zu **vereinfachen** eine Klasse mit **statischen Variablen** erstellt, dies vereinfacht die Änderung eines Pfades, da man diesen dann nicht an **mehreren orten** ändern muss.
# Streams

In unserem **Programm** haben wir auch ein Paar **Streams** verwendent die wir hier noch einmal zur **Hilfe zeigen** wollten: 

```Java
return field.stream()
            .mapToInt(n -> n.getLeft())
            // Diese methode wandelt den Stream<SimpleMaps> in einen IntSream um
            .filter(n -> n % 2 == 0)
            // Der Filter sorgt dafür, dass nur alle 2en aufsummiert werden, da die 2 in unserem Fall "nicht zerstörtes Schiff bedeutet"
            .sum() == 0;
```

```Java
public static Player getPlayer(Context ctx)  throws NoSuchElementException {
return players.stream()
             // in player sind alle spieler + spielerIDs gespeichert
            .filter(p -> p.getID().equals(ctx.sessionAttribute("Player-ID")))
            // es werden die Spieler die, die selbe ID haben wie im ctx attribute Player ID mitangegegeben herausgefilter
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