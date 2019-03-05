var gameexpress = require('express');
var gameapp = gameexpress();
var gameserver = gameapp.listen(3000);
var gamesocket = require('socket.io');
var gameio = gamesocket(gameserver);


var basicexpress = require('express');
var basicapp = basicexpress();
var basicserver = basicapp.listen(3001);
var basicsocket = require('socket.io');
var basicio = basicsocket(basicserver);

//app.use(express.static('client'));

console.log("Server listening at port 3000");
console.log("Server listening at port 3001");
var players = [];

class Player{
    constructor(posX, posY, id) {
        this.X = posX;
        this.Y = posY;
        this.ID = id;
        this.isMoving = true;
        this.destX = posX;
        this.destY = posY;
    }
    setPos(newX, newY) {
        this.X = newX;
        this.Y = newY;
    }
    setDir(newX, newY) {
        this.dirX = newX;
        this.dirY = newY;
    }
    
    setDest(newX, newY)
    {
        setDir(newX- this.X, newY - this.Y);
        this.destX = newX;
        this.destY = newY;
    }
}

class Game{
    
    constructor(name)
    {
        this.players = [];
        this.playersPreviousConnectionIds = [];
        this.name = name;
    }
    
}

gameio.sockets.on('connection',(socket) =>{
    console.log("new connection: " + socket.id);
    
    var ID;
    socket.on("getinit", (fn) => {
        console.log(fn);
        ID = 0;
        while(true)
        {
            if(players[ID] == undefined || players[ID] == null)
            {
                break;   
            }
            ID ++;
        }
        //var ID = socket.id;
        var newPlayer = new Player(0,0,ID);
        //newPlayer.destY = 0;
        players[ID] = newPlayer;
        var data = {players, ID};
//        socket.emit('init', data);
        socket.broadcast.emit('new player',{ID, newPlayer});
        console.log(newPlayer);
        fn(data);
    });
    
    socket.on('move', (data) => {
        //console.log(data);
        //players[data.ID].setPos(data.X, data.Y);
        players[data.ID].X = data.X;
        players[data.ID].Y = data.Y;
        //players[data.ID].dirX = data.dirX;
        //players[data.ID].dirY = data.dirY;
        players[data.ID].isMoving = data.isMoving;
        players[data.ID].destX = data.destX;
        players[data.ID].destY = data.destY;
        socket.broadcast.emit('move', data);
    });
    
    socket.on('disconnect',() =>{
        players[ID] = null;
        socket.broadcast.emit('player disconnected', ID);
        console.log(socket.id + ' has disconnected');
    });
});

var MatchQueue = [];
var games = [];
var numberOfGamesPlaying = 0;
var numberOfPlayersPerGame = 2;

basicio.sockets.on('connection', (socket) => {
    console.log("Connected to a launcher!");
    socket.on("Find Match", () => {
        MatchQueue.push(socket.id);
        console.log("Players In Queue: " + MatchQueue.length);
        while(MatchQueue.length >= numberOfPlayersPerGame)
        {
            var havePlayersAcceptedCurrentGame = [numberOfPlayersPerGame];
            var havePlayersResponded = [numberOfPlayersPerGame];
            var currentGame = new Game("game_" + numberOfGamesPlaying);
            havePlayersAcceptedCurrentGame.fill(false);
            havePlayersResponded.fill(false);
            console.log("attempting to start a game");
            for(var i = 0; i < numberOfPlayersPerGame; i ++)
            {
                var some_socket = basicio.sockets.connected[MatchQueue[i]];
                currentGame.playersPreviousConnectionIds.push(MatchQueue[i]);
                //some_socket.join("game_" + numberOfGamesPlaying);
                some_socket.emit("Match Found", () => {
                    console.log("a player has accepted match: " + currentGame.name);
                    //console.log(fn);
                    havePlayersAcceptedCurrentGame[i] = true;
                    havePlayersResponded[i] = true;
                    console.log(i);
                    var isGameReady = true;
                    for(var j = 0; j < numberOfPlayersPerGame; j ++)
                    {
                        if(havePlayersAcceptedCurrentGame[j] == false && havePlayersResponded[j] == true)
                        {
                            isGameReady = false;
                        }
                        if(havePlayersResponded[j] == false)
                        {
                            console.log(havePlayersResponded);
                            console.log(havePlayersAcceptedCurrentGame);
                            return;
                        }
                    }
                    if(isGameReady)
                    {
                        games.push(currentGame);
                        console.log("starting the game...");
                        //fn("game is starting");
                        for(var p = 0; p < currentGame.players.length; p ++)
                        {
                            var currentClientsSocket = basicio.sockets.connected[currentGame.players[p]];
                            currentClientsSocket.emit("Game", "start");
                        }
                    }
                    else{
                        console.log("some players declined");
                        //fn("fail");
                        for(var p = 0; p < currentGame.players.length; p ++)
                        {
                            var currentClientsSocket = basicio.sockets.connected[currentGame.players[p]];
                            currentClientsSocket.emit("Game", "fail");
                        }
                    }
                });
                
                numberOfGamesPlaying ++;
            }
            MatchQueue.splice(0, numberOfPlayersPerGame);
        }
    });
    socket.on('dissconnect', () => {
        MatchQueue.splice(MatchQueue.indexOf(socket.id),1);
        console.log(socket.id + "'s launcher has disconnected!")
    });
});
