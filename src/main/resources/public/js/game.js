var eventSource = new EventSource("http://" + location.hostname + ":" + location.port + "/test");

//Listener 
eventSource.addEventListener('Conection', e => conectionStatus(e.data));

function conectionStatus(data) {
    //TODO
}

function sendMove(value) {
    //TODo send with value (Post ? )
    //sentRequestGet('/player/move');
    alert("Test");
}