# Projekt: Battleship (Fr/2, Kr)

> Unser Projekt ist "Schiffe versenken". //TODO: Beschreibung

![Screenshot](Screenshot-Battleship.png)

Keywords: Bootstrap, Server-Sent Events (SSE), Accessmanager, Path, ctx.render()

Projektbeteiligte:

* Jannik Lapp
* Max Stephan


<Inhaltsverzeichnis>
//TODO

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
//Client mit sse verbinden
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

# ctx.render()

# Path