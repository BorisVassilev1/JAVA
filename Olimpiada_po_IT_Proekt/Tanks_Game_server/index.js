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
//var players = [];

class Player{
    constructor(posX, posY) {
        this.X = posX;
        this.Y = posY;
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
    var myGameID;
    var myGameName;
    var myIDInGame;
    
    var ID;
    socket.on("LauncherID", (id, fn) => {
        for(var i = 0; i < games.length; i ++)
        {
            for(var j = 0; j < numberOfPlayersPerGame; j ++)
            {
                if(games[i].playersPreviousConnectionIds[j] == id)
                {
                    console.log("this player is matched to game: " + games[i].name);
                    socket.join(games[i].name);
                    myGameID = i;
                    myGameName = games[i].name;
                    myIDInGame = j;
                    return;
                }
            }
        }
        
        //var ID = socket.id;
        //var newPlayer = new Player(0,0);
        //newPlayer.destY = 0;
        var players = games[myGameID].players;
        var data = {players, myIDInGame};
//        socket.emit('init', data);
        //socket.broadcast.emit('new player',data);
        console.log("SENDING THE INIT PACKET")
        fn(data);
    });
    
    socket.on('move', (data) => {
        //console.log(data);
        //players[data.ID].setPos(data.X, data.Y);
        games[myGameID].players[data.ID].X = data.X;
        games[myGameID].players[data.ID].Y = data.Y;
        //players[data.ID].dirX = data.dirX;
        //players[data.ID].dirY = data.dirY;
        games[myGameID].players[data.ID].isMoving = data.isMoving;
        games[myGameID].players[data.ID].destX = data.destX;
        games[myGameID].players[data.ID].destY = data.destY;
        socket.broadcast.to(myGameName).emit('move', data);
    });
    
    socket.on('disconnect',() =>{
        //players[ID] = null;
        socket.broadcast.to(myGameName).emit('player disconnected', myIDInGame);
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
//            havePlayersAcceptedCurrentGame.fill(false, 0, numberOfPlayersPerGame);
//            havePlayersResponded.fill(false, 0, numberOfPlayersPerGame);
            for(var i = 0; i < numberOfPlayersPerGame; i ++)
            {
                havePlayersResponded[i] = false;
            }
            for(var i = 0; i < numberOfPlayersPerGame; i ++)
            {
                havePlayersAcceptedCurrentGame[i] = false;
            }
            //console.log(havePlayersResponded);
            //console.log(havePlayersAcceptedCurrentGame);
            
            console.log("attempting to start a game");
            for(var i = 0; i < numberOfPlayersPerGame; i ++)
            {
                var some_socket = basicio.sockets.connected[MatchQueue[i]];
                currentGame.playersPreviousConnectionIds.push(MatchQueue[i]);
                //some_socket.join("game_" + numberOfGamesPlaying);
                //console.log("MatchQueue[" + i + "] = " + MatchQueue[i])
                some_socket.emit("Match Found", i, (i, reply) => {
                    if(reply == false)
                    {
                        havePlayersAcceptedCurrentGame[i] = false;
                        havePlayersResponded[i] = true;
                        console.log("a player has declined match: " + currentGame.name);
                    }
                    else
                    {
                        havePlayersAcceptedCurrentGame[i] = true;
                        havePlayersResponded[i] = true;
                        console.log("a player has accepted match: " + currentGame.name);
                    }
                    //console.log(fn);
                    
                    //console.log(i);
                    var isGameReady = true;
                    for(var j = 0; j < numberOfPlayersPerGame; j ++)
                    {
                        if(havePlayersAcceptedCurrentGame[j] == false && havePlayersResponded[j] == true)
                        {
                            isGameReady = false;
                        }
                        if(havePlayersResponded[j] == false)
                        {
                            return;
                        }
                    }
                    if(isGameReady)
                    {
                        for(var k = 0; k < currentGame.playersPreviousConnectionIds.length; k ++)
                        {
                            currentGame.players[k] = new Player(0,0);
                        }
                        games.push(currentGame);
                        console.log("starting the game...");
                        //fn("game is starting");
                        for(var p = 0; p < currentGame.playersPreviousConnectionIds.length; p ++)
                        {
                            var currentClientsSocket = basicio.sockets.connected[currentGame.playersPreviousConnectionIds[p]];
                            currentClientsSocket.emit("Game", true);
                            //MatchQueue.splice(MatchQueue.indexOf(currentGame.players[i], 1));
                        }
                    }
                    else{
                        console.log("some players declined");
                        //fn("fail");
                         console.log("failed to create a game.returning the players to the queue.")
                        for(var p = 0; p < currentGame.playersPreviousConnectionIds.length; p ++)
                        {
                            var currentClientsSocket = basicio.sockets.connected[currentGame.playersPreviousConnectionIds[p]];
                            currentClientsSocket.emit("Game", false);
                            MatchQueue.push(currentClientsSocket.id);
                        }
                    }
                });
                
                numberOfGamesPlaying ++;
            }
            MatchQueue.splice(0, numberOfPlayersPerGame);
        }
    });
    
    socket.on("Leave Queue", () => {
        MatchQueue.splice(MatchQueue.indexOf(socket.id),1);
        console.log(socket.id + " has left the queue")
    });
     
    socket.on('dissconnect', () => {
        MatchQueue.splice(MatchQueue.indexOf(socket.id),1);
        console.log(socket.id + "'s launcher has disconnected!")
    });
});
