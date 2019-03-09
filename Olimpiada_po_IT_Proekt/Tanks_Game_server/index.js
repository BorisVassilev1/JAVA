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

class Vector2
{
    constructor(x,y)
    {
        this.x = x;
        this.y = y;
    }
    clone()
    {
        return new Vector2(this.x, this.y);
    }
    add(a)
    {
        this.x += a.x;
        this.y += a.y;
        return this;
    }
    sub(a)
    {
        this.x -= a.x;
        this.y -= a.y;
        return this;
    }
    lengthSquared()
    {
        return this.x * this.x + this.y * this.y;
    }
    scale(a)
    {
        this.x *= a;
        this.y *= a;
        return this;
    }
    set(x,y)
    {
        this.x = x;
        this.y = y;
        return this;
    }
    distSquared(a)
    {
        return this.clone().sub(a).lengthSquared()
    }
}

class Player{
    constructor(posX, posY, team) {
        this.pos = new Vector2(posX, posY);
        this.isMoving = true;
        this.currentDestination = this.pos.clone();
        this.movementSpeed = 200;
        this.lastUpdated = Date.now();
        this.team = team;
        this.maxHealth = 100;
        this.isActive = true;
        this.health = this.maxHealth;
        this.reviveTimer = 0;
    }
    
    setDest(newX, newY)
    {
        this.currentDestination.set(newX, newY);
    }
    
    update()
	{
        var deltaTime = Date.now() - this.lastUpdated;
        if(this.reviveTimer > 0)
        {
            this.reviveTimer -= deltaTime;
        }
        if(deltaTime == 0)
        {
            return;
        }
		if(this.isMoving && this.isActive)
		{
			var a = this.currentDestination.clone();
			a.sub(this.pos);
            if(a.lengthSquared() == 0)
            {
                this.lastUpdated = Date.now();
                return;
            }
			var traveledDistance = this.movementSpeed * deltaTime / 1000;
            if(a.lengthSquared() < traveledDistance * traveledDistance)
            {
                this.pos.set(this.currentDestination.x, this.currentDestination.y);
            }
            else{
                a.scale(traveledDistance / Math.sqrt(a.lengthSquared()));
                this.pos.add(a);
            }
		}
        this.lastUpdated = Date.now();
	}
    
    dealDamage(amt)
    {
        this.health -= amt;
		if(this.health <= 0)
		{
			return true;
		}
    }
}

class Bullet{
    constructor(pos, direction)
    {
        this.pos = new Vector2(pos.x, pos.y);
        this.direction = new Vector2(direction.x, direction.y);
        this.direction.sub(pos);
        this.lastUpdated = Date.now();
        this.lifetime = 5000;
        this.deathTime = Date.now() + this.lifetime;
        this.movementSpeed = 300;
        this.team = 0;
    }
    
    update()
    {
        var deltaTime = Date.now() - this.lastUpdated;
        var a = this.direction.clone();
        var traveledDistance = this.movementSpeed * deltaTime / 1000;
        a.scale(traveledDistance / Math.sqrt(a.lengthSquared()));
        this.pos.add(a);
        this.lastUpdated = Date.now();
    }
}

class Game{
    
    constructor(name)
    {
        this.players = [];
        this.bullets = [];
        this.playersPreviousConnectionIds = [];
        this.name = name;
    }
    
    update()
    {
        for(var i = 0; i < this.players.length; i ++)
        {
            //if(Date.now() - this.players[i].lastUpdated > 20)
            {
                //console.log("player: ");
                //console.log(this.players[i].pos)
                this.players[i].update();
                if(this.players[i].reviveTimer < 0)
                {
                    this.revive(i, games.indexOf(this))
                }
                //console.log(this.players[i].pos)
            }
        }
        for(var i = 0; i < this.bullets.length; i ++)
        {
            //console.log(this.bullets[i])
            //if(this.bullets[i] != null)
            {
                this.bullets[i].update();
                if(this.bullets[i].deathTime < Date.now())
                {
                    this.bullets.splice(i, 1);
                }
            }
        }
        for(var i = 0; i < this.players.length; i ++)
        {
            for(var j = 0; j < this.bullets.length; j ++)
            {
                if(this.players[i].isActive && this.players[i].team != this.bullets[j].team &&
                    this.players[i].pos.distSquared(this.bullets[j].pos) < 400)
                {
                    gameio.to(this.name).emit("damage", i, j,20);
                    //console.log(i + " has taken damage");
                    if(this.players[i].dealDamage(20))
                    {
                        gameio.to(this.name).emit("death", i);
                        this.players[i].isActive = false;
                        this.players[i].reviveTimer = 3000;
                    }
                    this.bullets.splice(j);
                }
            }
        }
    }
    
    revive(id, gameid)
    {
        var pl;
        if(id < numberOfPlayersPerGame / 2){
            pl = new Player(50, 50, 0);
        }
        else{
            pl = new Player(750, 550, 1);
        }
        games[gameid].players[id] = pl;
        gameio.to(this.name).emit("revive", id, pl);
        //console.log("reviving " + id)
    }
}

gameio.sockets.on('connection',(socket) =>{
    console.log("new connection: " + socket.id);
    var myGameID;
    var myGameName;
    var myIDInGame;
    var myGame;
    var myTeam;
    var lastShoot;
    
    var ID;
    socket.on("LauncherID", (id, fn) => {
        for(var i = 0; i < games.length; i ++)
        {
            for(var j = 0; j < numberOfPlayersPerGame; j ++)
            {
                if(games[i].playersPreviousConnectionIds[j] == id)
                {
                    socket.join(games[i].name);
                    myGameID = i;
                    myGameName = games[i].name;
                    myIDInGame = j;
                    myGame = games[i];
                    lastShoot = Date.now();
                    //games[i].players[j].team =  (j > numberOfPlayersPerGame/2) ? 1 : 0;
                    myTeam = games[i].players[j].team;
                    break;
                }
            }
        }
        var players = games[myGameID].players;
        var data = {players, ID: myIDInGame};
        fn(data);
    });
    
    socket.on('move', (data, fn) => {
        //console.log("player: ");
        //console.log(myGame.players[myIDInGame].pos)
        myGame.players[myIDInGame].update();
        //console.log(myGame.players[myIDInGame].pos)
        
        myGame.players[myIDInGame].currentDestination.set(data.x, data.y);
        socket.broadcast.to(myGameName).emit('move', myGame.players[myIDInGame].pos, myGame.players[myIDInGame].currentDestination, myIDInGame);
        fn(myGame.players[myIDInGame].pos, myGame.players[myIDInGame].currentDestination);
    });
    
    socket.on("shoot", (mouseXY, fn) => {
        if(Date.now() > lastShoot + 1000)
        {
            //console.log("player: ");
            //console.log(myGame.players[myIDInGame].pos)
            myGame.players[myIDInGame].update();
            //console.log(myGame.players[myIDInGame].pos)
            var bullet = new Bullet(myGame.players[myIDInGame].pos.clone(), mouseXY);
            bullet.team = myTeam;
            myGame.bullets.push(bullet);
            
//            for(var i = 0; ; i ++)
//            {
//                if(!myGame.bullets[i])
//                {
//                    myGame.bullets[i] = bullet;
//                    break;
//                }
//            }
//            
            lastShoot = Date.now();
            //console.log(bullet);
            socket.broadcast.to(myGameName).emit('shoot',bullet, myIDInGame);
            fn(bullet);
        }
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
        console.log(socket.id + " has joined the queue");
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
            
            //console.log("attempting to start a game");
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
                        //console.log("a player has declined match: " + currentGame.name);
                    }
                    else
                    {
                        havePlayersAcceptedCurrentGame[i] = true;
                        havePlayersResponded[i] = true;
                        //console.log("a player has accepted match: " + currentGame.name);
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
                            if(k < numberOfPlayersPerGame / 2){
                                currentGame.players[k] = new Player(50,50, 0);
                            }
                            else{
                                currentGame.players[k] = new Player(750,550, 1);
                            }
                        }
                        console.log("successfully created a game:")
                        console.log(currentGame);
                        games.push(currentGame);
                        
                        //fn("game is starting");
                        for(var p = 0; p < currentGame.playersPreviousConnectionIds.length; p ++)
                        {
                            var currentClientsSocket = basicio.sockets.connected[currentGame.playersPreviousConnectionIds[p]];
                            currentClientsSocket.emit("Game", true);
                            //MatchQueue.splice(MatchQueue.indexOf(currentGame.players[i], 1));
                        }
                    }
                    else{
                         //console.log("failed to create a game.returning the players to the queue.")
                        for(var p = 0; p < currentGame.playersPreviousConnectionIds.length; p ++)
                        {
                            var currentClientsSocket = basicio.sockets.connected[currentGame.playersPreviousConnectionIds[p]];
                            currentClientsSocket.emit("Game", false);
                            //MatchQueue.push(currentClientsSocket.id);
                            console.log(MatchQueue.length)
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

setTimeout(update, 0);

function update()
{
    for(var i = 0; i < games.length; i ++)
    {
        games[i].update();
    }
    setTimeout(update, 30)
}

