//Request variable
var xhr = new XMLHttpRequest;

//Listener
//TODO Listener für nachgeladene Seitenteile wie? doch mit onclick? 
window.onload = () => getPage();
xhr.onreadystatechange = () => hadleResponse();

//Request Functions
function sentRequestGet(path = '', query = '') {
    xhr.open('Get', path + '?' + query);
    xhr.send();
}

function getPage() {
    sentRequestGet('/getside')
}

function newGame() {
    sentRequestGet('/game/new');
}
function getGameID() {
    sentRequestGet('/game/getid');
}
function setName() {
    sentRequestGet('/player/setname', 'Game=' + document.getElementById('GameID').value + '&Name=' + document.getElementById('Name').value);
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
                case '3':
                    onloadLogin();
            }
        }

        //if request incorrect do 
        if (xhr.status == 400) {
            switch(id) {
                case '1':
                    requestGameID();
                    break;
                case '2':
                    invalidGamID();
            }
        }
    }
}

function newHtmlContent() {
    document.getElementById('htmlContent').innerHTML = xhr.responseText;
}

function displayGameID() {
    var gameid = document.getElementById('GameID');
    gameid.value = xhr.responseText;
    gameid.readOnly = true;
    document.getElementById('copyID').style.display = "inline-block";
}

function requestGameID() {
    document.getElementById('GameID').readOnly = false;
    document.getElementById('copyID').style.display = "none";
}

function onloadLogin() {
    newHtmlContent()
    getGameID();
}

function invalidGamID() {
    //TODO vtl feld rot aufblinken + text über button
    alert("False Game-ID Plese try agan!")
}

//Other Functions

function copyID() {
    var el = document.getElementById('GameID');
    el.select();
    document.execCommand('copy');
}