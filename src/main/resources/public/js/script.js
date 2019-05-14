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

function joinGame() {
    sentRequestGet('/game/join');
}

function getGameID() {
    sentRequestGet('/game/getid');
}

function setName() {
    sentRequestGet('/player/setname', 'Game=' + document.getElementById('GameID').value + '&Name=' + document.getElementById('Name').value);
}

function aboutGame() {
    sentRequestGet('/game/about');
}

function sendMove(value) {
    //TODo send with value (Post ? )
    //sentRequestGet('/player/move');
    alert(value);
}

function quitGame() {
    var check = confirm('\u26A0 Are you sure you want to leave the game? \n (All progress will be lost)');
    if(check == true) {
        sentRequestGet('/player/remove');
    }
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
                    break;
                case '4':
                    onloadGame();
                    break;
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

function onloadGame() {
    newHtmlContent();
    conectSSE();
}

function invalidGamID() {
    //TODO vtl feld rot aufblinken + text über button
    alert("False Game-ID Plese try agan!")
}
//SSE

async function conectSSE() {
    var eventSource = new EventSource("http://" + location.hostname + ":" + location.port + "/sse");

    //Listener 
    eventSource.addEventListener('Conection', e => test(e.data));
    eventSource.addEventListener('QuitGame', e => playerQuited(e.data));

    //Message handle Functions
    function conectionStatus(data) {
        //TODO vlt. mit id wie? 
        switch(data) {
            case 'Conected':
                //TODO
                break;
            case 'Disconect':
                //TODO close Conection .close() is not working 
                eventSource.close();
                break;
        }
    }
    
    function playerQuited(data) {
        alert(data);
        getPage();
    }
}

//Other Functions

function copyID() {
    var el = document.getElementById('GameID');
    el.select();
    document.execCommand('copy');
}