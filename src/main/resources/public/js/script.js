//Request variable
var xhr = new XMLHttpRequest;

//Listener
xhr.onreadystatechange = () => hadleResponse();
document.getElementById("newGame").addEventListener('click', () => newGame());

//Request Function
function sentRequestGet(path = '') {
    xhr.open('Get', path);
    xhr.send();
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

//Listener Functions
function newGame() {
    sentRequestGet('/game/new');
}