# Saper

<b>Simplified minesweeper game</b> basic functionality:
- reset game, chosing, counting and flaging mines,
- four difficuclty levels: easy, medium, hard, extreme,
- timer, 

<i>My 4th project, one of favorites. Short, little project which gives me a lot o fun. However only small part of 
code is covered with tests, rest looks fine in my opinion, so I am pretty satisfied with this project.

Well recived on SO Code Review:
http://codereview.stackexchange.com/questions/88636/beginner-minesweeper-game</i>

#Minesweeper: rules and basics

<b>The object</b>
Find the empty squares while avoiding the mines. The faster you clear the board, the better your score.

<b>The board</b>
Minesweeper has four standard boards to choose from, each progressively more difficult.
<br><i>EASY</i>: 64 tiles, 10 mines
<br><i>MEDIUM</i>: 256 tiles, 40 mines
<br><i>HARD</i>: 576 tiles, 100 mines
<br><i>EXTREME</i>: 1024 tiles, 250 mines

<b>The rules in Minesweeper are simple:</b>
<br>1) Uncover a mine, and the game ends.
<br>2) Uncover an empty square, and you keep playing.
<br>3) Uncover a number, and it tells you how many mines lay hidden in the eight surrounding squares—information you use to deduce which nearby squares are safe to click.

<b>Hints and tips</b>
- <i>Mark the mines.</i> If you suspect a square conceals a mine, right-click it. This puts a flag on the square. (If you're not sure, right-click again to make it a question mark.)
- <i>Study the patterns.</i> If three squares in a row display 2-3-2, then you know three mines are probably lined up beside that row. If a square says 8, every surrounding square is mined.
- <i>Explore the unexplored.</i> Not sure where to click next? Try clearing some unexplored territory. You're better off clicking in the middle of unmarked squares than in an area you suspect is mined.





