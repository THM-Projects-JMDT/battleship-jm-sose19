//Request variable
var xhr = new XMLHttpRequest;

//Listener
//TODO Listener für nachgeladene Seitenteile wie? doch mit onclick? 
window.onload = () => getPage();
xhr.onreadystatechange = () => hadleResponse();

//Request Functions
function sentRequestGet(path = '') {
    xhr.open('Get', path);
    xhr.send();
}

function getPage() {
    sentRequestGet('/getside')
}

function newGame() {
    sentRequestGet('/game/new');
}
function joinGame() {
    sentRequestGet('/game/join');
}
function aboutGame() {
    sentRequestGet('/game/about');
}

//Response Functions
function hadleResponse() {
    if (xhr.readyState == 4) {
        var id = xhr.getResponseHeader("Content-ID");

        //if request correct do
        if (xhr.status == 200) {
            switch(id) {
                case '0':
                    newHtmlContent();
                    break;
                case '1':
                    displayGameID();
                    break;
            }
        }

        //if request incorrect do 
        if (xhr.status == 404) {
            switch(id) {
                case '1':
                    requestGameID();
                    break;
            }
        }
    }
}

function newHtmlContent() {
    document.getElementById('htmlContent').innerHTML = xhr.responseText;
}

function displayGameID() {
    //TODO übergebene gameID in feld eintragen 
}

function requestGameID() {
    //TODO Game-ID feld freigeben zur eingaben
}