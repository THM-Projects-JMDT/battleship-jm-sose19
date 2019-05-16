# Links 
  - /game
    - /new
    - /join
    - /about
    - /getid
  - /player
    - /startgame
  - /getside
  - /sse

# formParms
  - Spier Name -> "Name"
  - Game ID -> "Game-ID"

# sessionAttribute
  - Spieler ID -> "Player-ID"  
  - Spieler Name -> "Name"
  - Feld Cordinate -> "Cordinate"

# Content-ID
  - Neue Seite laden -> 0
  - Mit Game-id -> 1
  - Falche Game-id > 2 
  - Neue Seite Laden + request GameID -> 3
  - Neue Sete Landen + starte SSe -> 4
  - Seite im Fester Öffnen -> 5
  - Antwort auf Boat setzen -> 6

  - Gleiche Codes mit 404 fehler für den Code

# Server Send events
  - Verbindungs Details -> "Conection"
  - Spieler ist einem Spiel beigetreten -> "PlayerConect"
  - Anderer Spieler hat Spiel verlassen -> "QuitGame"
  - Game Status meldungen -> "GameStatus"

# Game States 
  - erstellt -> 0 
  - 1 Spieler -> 1
  - 2 Spieler -> 2
  - 1 Spieler Ready -> 3
  - Beide Spieler Ready -> 4
  - Spieler 1 an der Reihe -> 5
  - Spieler 2 an der Reihe -> 6
  - Spieler 1 win -> 7
  - Spieler 2 win -> 8 