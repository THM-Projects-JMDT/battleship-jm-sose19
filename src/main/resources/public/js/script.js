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

//Response Functions
function hadleResponse() {
    if(xhr.readyState == 4 && xhr.status == 200) {
        var id = xhr.getResponseHeader("Content-ID");
        switch(id) {
            case '1':
                newHtmlContent();
                break;
            case '2':
                break;
        }
    }
}

function newHtmlContent() {
    document.getElementById('htmlContent').innerHTML =  xhr.responseText;
}