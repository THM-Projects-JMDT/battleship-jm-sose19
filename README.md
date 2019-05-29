# Projekt: Battleship (Fr/2, Kr)

> Unser Projekt ist "Schiffe versenken". //TODO: Beschreibung

![Screenshot](Screenshot-Battleship.png)

Keywords: Bootstrap, Server-Sent Events (SSE), Accessmanager, Path, ctx.render()

Projektbeteiligte:

* Jannik Lapp
* Max Stephan


<Inhaltsverzeichnis>
//TODO Inhaltsverzeichnis

# Server-Sent Events (SSE)

```Java
app.sse("/sse", client -> {
    //Daten Senden
    client.sendEvent("data");

    client.onClose( () -> { 
        //Handle Conection Closed 
    });
});
```
```js
//Client mit SSE verbinden
var eventSource = new EventSource("//" + location.hostname + ":" + location.port + "/sse"); 

//Antwort Listener
eventSource.addEventListener('message', e => {
    //Antwort verarbeiten
    console.log(e.data);
});
```

```Java
//Daten mit bestimmtem event Senden
client.sendEvent("event", "data");
```

```js
//Antwort Listener mit eigenem Event
eventSource.addEventListener('event', e => {
    //Antwort verarbeiten
    console.log(e.data);
});
```

```Java
//Daten mit bestimmtem event + id Senden
client.sendEvent("event", "data", "1");
```

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
    }, roles(ANYONE));
    //als roles(ANYONE) muss ein Set<Role> mit allen erlaubent Rollen übergeben werden
```

# ctx.render()

# Path