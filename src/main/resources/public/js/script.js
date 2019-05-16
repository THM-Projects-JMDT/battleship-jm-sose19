//Request variable
var xhr = new XMLHttpRequest;

//variable
var myWindow = document.getElementById('window');
var falseGameID = false;

//Listener
//TODO Listener für nachgeladene Seitenteile wie? doch mit onclick? 
window.onload = () => getPage();
xhr.onreadystatechange = () => hadleResponse();
myWindow.onclick = e => closeWindowbyFokus(e);

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

function startGame() {
    sentRequestGet('/player/startgame', 'Game=' + document.getElementById('GameID').value + '&Name=' + document.getElementById('Name').value);
}

function aboutGame() {
    sentRequestGet('/game/about');
}

function sendMove(value) {
    sentRequestGet('/player/move', "Cordinate=" + value);
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
                case '5':
                    openWindow();
                    break;
                case '6':
                    realoadField();
                    break;
                case '7':
                    reloadmyShips();
                    break;
                case '8':
                    reloadenemyShips();
                    break;
                case '9':
                    reloadenemyField();
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
                case '6':
                    invalidePlacement();
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
    falseGameID = true;
    document.getElementById('GameID').style.borderColor = "red";
    document.getElementById('GameIDError').style.display = "inline-block"
}

function resetFalseGameID() {
    if(falseGameID == true) {
        document.getElementById('GameID').style.borderColor = "lightgrey";
        document.getElementById('GameIDError').style.display = "none";
    }
}

function openWindow() {
    myWindow.style.display = "block";
    document.getElementById('lodedWindowContent').innerHTML = xhr.responseText;
}

function closeWindow() {
    myWindow.style.display = "none";
}
function closeWindowbyFokus(e) {
    if(e.target == myWindow) {
        closeWindow();
    }
}

function realoadField() {
    document.getElementById('mainboard').innerHTML = xhr.responseText;
}
function reloadenemyField() {
    document.getElementById('enemyboard').innerHTML = xhr.responseText;
}
function reloadmyShips() {
    document.getElementById('schiffe').innerHTML = xhr.responseText;
}
function reloadenemyShips() {
    document.getElementById('enemyschiffe').innerHTML = xhr.responseText;
}

function invalidePlacement() {
    alert('Invalid Placement! Please Try again!');
}

//SSE
async function conectSSE() {
    var eventSource = new EventSource("http://" + location.hostname + ":" + location.port + "/sse");
    //Listener 
    eventSource.addEventListener('Conection', e => conectionStatus(e.data));
    eventSource.addEventListener('QuitGame', e => playerQuited(e.data));

    //Message handle Functions
    function conectionStatus(data) {
        //TODO vlt. mit id wie? 
        switch(data) {
            case 'Conected':
                //TODO
                break;
            case 'Disconnected':
                eventSource.close();
                break;
            case '7':
                reloadmyShips()
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