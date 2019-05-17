//Request variable
var xhr = new XMLHttpRequest;

//variable
var myWindow = document.getElementById('window');
var falseGameID = false;

//Listener
//TODO Listener für nachgeladene Seitenteile wie? doch mit onclick? 
window.onload = () => getPage();
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
xhr.onreadystatechange = async function() {
    if (xhr.readyState == 4) {
        var id = this.getResponseHeader("Content-ID");

        var response = this.responseText;

        //if request correct do
        if (xhr.status == 200) {
            switch(id) {
                case '0':
                    newHtmlContent(response);
                    break;
                case '1':
                    displayGameID(response);
                    break;
                case '3':
                    onloadLogin(response);
                    break;
                case '4':
                    onloadGame(response);
                    break;
                case '5':
                    openWindow(response);
                    break;
                case '6':
                    realoadField(response);
                    break;
                case '7':
                    reloadmyShips(response);
                    break;
                case '8':
                    reloadenemyShips(response);
                    break;
                case '9':
                    reloadenemyField(response);
                    break;
            }
        }

        //if request incorrect do 
        if (xhr.status == 400) {
            switch(id) {
                case '1':
                    requestGameID(response);
                    break;
                case '2':
                    invalidGamID(response);
                case '6':
                    invalidePlacement();
            }
        }
    }
}

function newHtmlContent(resonse='') {
    document.getElementById('htmlContent').innerHTML = resonse;
}

function displayGameID(resonse='') {
    var gameid = document.getElementById('GameID');
    gameid.value = resonse;
    gameid.readOnly = true;
    document.getElementById('copyID').style.display = "inline-block";
}

function requestGameID() {
    document.getElementById('GameID').readOnly = false;
    document.getElementById('copyID').style.display = "none";
}

function onloadLogin(resonse='') {
    newHtmlContent(resonse)
    getGameID();
}

function onloadGame(resonse='') {
    newHtmlContent(resonse);
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

function openWindow(resonse='') {
    myWindow.style.display = "block";
    document.getElementById('lodedWindowContent').innerHTML = resonse;
}

function closeWindow() {
    myWindow.style.display = "none";
}
function closeWindowbyFokus(e) {
    if(e.target == myWindow) {
        closeWindow();
    }
}

function realoadField(resonse='') {
    document.getElementById('mainboard').innerHTML = resonse;
}
function reloadenemyField(resonse='') {
    document.getElementById('enemyboard').innerHTML = resonse;
}
function reloadmyShips(resonse='') {
    document.getElementById('schiffe').innerHTML = resonse;
}
function reloadenemyShips(resonse='') {
    document.getElementById('enemyschiffe').innerHTML = resonse;
}

function invalidePlacement(resonse='') {
    document.getElementById('mainboardError').style.display = "block";
    resetInvalidePlacement();
}

async function resetInvalidePlacement() {
    setTimeout(function() {
        document.getElementById('mainboardError').style.display = "none";
    }, 200)
}

//SSE
async function conectSSE() {
    var eventSource = new EventSource("http://" + location.hostname + ":" + location.port + "/sse");
    //Listener 
    eventSource.addEventListener('Conection', e => conectionStatus(e.data));
    eventSource.addEventListener('QuitGame', e => playerQuited(e.data));
    eventSource.addEventListener('UpdateMyships', e => getMyShips());
    eventSource.addEventListener('UpdateEnemyships', e => getEnemyField());
    eventSource.addEventListener('UpdateEnemyboard', e => getEnemyShips());
    eventSource.addEventListener('ShipReady', e => shipReady());

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
        }
    }
    
    function playerQuited(data) {
        alert(data);
        getPage();
    }
    async function getMyShips() {
        setTimeout(function() {
            sentRequestGet('/player/getmyships');
        }, 10)
    }
    function getEnemyShips() {
        sentRequestGet('/player/getenemyships');
    }
    function getEnemyField() {
        sentRequestGet('/player/getenemyfield');
    }
    function shipReady() {
        document.getElementById('mainboardError').style.display = "block";
        document.getElementById('mainboardError').style.backgroundColor = "rgba(0,0,100,0.6)";
        resetshipReady();
    }

    async function resetshipReady() {
        setTimeout(function() {
            document.getElementById('mainboardError').style.display = "none";
            document.getElementById('mainboardError').style.backgroundColor = "rgba(100,0,0,0.6)";
        }, 200)
    }


}


//Other Functions

function copyID() {
    var el = document.getElementById('GameID');
    el.select();
    document.execCommand('copy');
}