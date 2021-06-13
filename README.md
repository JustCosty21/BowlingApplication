# BowlingApplication

## Short description of the application
The application consists of a parent project and two modules: **bowling** and **bowling-score-keeper**. The first one handles the back-end logic while the other
handles the actual logic of the bowling game. <br>
Here is the link of the application: https://github.com/dgestep/bowling-score-keeper<br> It has a **GNU General Public License v2.0** so as long as I include the source code of it, it is safe to use.
This helped a lot since I've never gone bowling (I know, this is pretty sad). Currently, I am playing around with Docker so the application comes with a script and a
Dockerfile. The script creates a jar file of the application (bowling) and runs docker. The jar file is moved to the container and is ran on port 8080. 

## How to play? 
1. Create a number of players: <br>
![Create a player](/images/create-player.PNG) <br>
Player no can be any number > 0 the player desires.<br>
Frames represents the first and second hit / a strike/ first and second hit + split -> [8 2 true]. Separate each frame with a comma <br>
2. Create a game: <br>
![Create a game](/images/create-game.PNG)<br>
Decide on a name for the game and add the player no of all the players that want to play.
3. Play a game: <br>
![Play a game](/images/play-game.PNG)<br>
Make a post on /api/game/{id of the game that was created}<br>
4. Postman output: <br>
![Postman output](/images/output-postman.PNG)<br>
5. Text file output
![Textfile output](/images/output-file.PNG)<br>
A .txt file is created inside the application. Open it to see the output!

## Other requests <br>
* Get all players: <br>
![Create a game](/images/get-player.PNG)<br>
* Get all games: <br>
![Create a game](/images/get-game.PNG)<br>
