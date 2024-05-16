
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.core.Enigma;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import enigma.console.TextAttributes;


public class Game {
	private enigma.console.Console console = Enigma.getConsole("Game", 120, 40, 13);
	private KeyListener kListener;
	private  int chrNumber;
	private boolean isPressed = false;
	private char[][] maze;
	public HumanNumber hNumber;
	private ComputerNumber[] compNumbers;
	private Queue inputs;
	public double second = 0;
	private int score = 0;
	private int countOfComputerNumber = 0;
	public boolean gameOverFlag = false;
	public TextAttributes white = new TextAttributes(Color.black, Color.white);
    public TextAttributes blue = new TextAttributes(Color.cyan, Color.black);
    public TextAttributes green = new TextAttributes(Color.green, Color.black);
    public TextAttributes yellow = new TextAttributes(Color.yellow, Color.black);
    public TextAttributes red = new TextAttributes(Color.red, Color.black);
    public TextAttributes pathColor = new TextAttributes(Color.orange, Color.black);
	
	public Game() throws Exception {
		//the game's startup files are loaded and the KeyListener function is called
		 UploadGameLogin();
		 kListener = new KeyListener() {
			public void keyTyped(KeyEvent e) {
				
			}
			public void keyPressed(KeyEvent e) {
				if(!isPressed) {
					 chrNumber = e.getKeyCode();
					 isPressed = true;
				}
		    }
			public void keyReleased(KeyEvent e) {
				
			}
		};
		console.getTextWindow().addKeyListener(kListener);
		MainGameMode();
	}
	private void MainGameMode() throws Exception {
		//the main loop of the game includes work done by second and adds a number to a maze every 5 seconds
		int oldSecond = 0;
		while(!gameOverFlag) {
			if(isPressed) {
				//Every time the key is pressed, old paths are deleted and new ones are drawn.
				deleteOldPaths();
				MoveTheNumber();
				pathFindingNumber();
			}
			Thread.sleep(100);
			second += 0.1;
			if((int)second - oldSecond == 1) {
				//If a second has passed, new random locations for 4, 5, 6 are generated and moved
				setNewLocationForStones();
				oldSecond = (int)second;
				if(oldSecond % 5 == 0) {
					setComputerNumber((int)inputs.peek());
					printInputs();
				}
			}
			printMaze();
			hNumber.printNumber();
			console.getTextWindow().setCursorPosition(69, 22);
			console.getTextWindow().output(String.valueOf((int)second));
			console.getTextWindow().setCursorPosition(70, 21);
			console.getTextWindow().output(String.valueOf(score));
		}
		GameOver();
	}
	public void printMaze()  {
		//we draw the maze in the appropriate color for each cell inside
		int cursorX = 0, cursorY = 0;
		for(int i = 0;i < maze.length; i++) {
			cursorX = 0;
			for(int j = 0;j < maze[0].length; j++) {
				console.getTextWindow().setCursorPosition(cursorX, cursorY);
				if(maze[i][j]!='#' && maze[i][j]!=' ' && (maze[i][j]=='1' || maze[i][j]=='2' || maze[i][j]=='3')) {
					console.getTextWindow().output(maze[i][j],green);
				}
				else if(maze[i][j]!='#' && maze[i][j]!=' '&& (maze[i][j]=='4' || maze[i][j]=='5' || maze[i][j]=='6')){
					console.getTextWindow().output(maze[i][j],yellow);
				}
				else if(maze[i][j]!='#' && maze[i][j]!=' ' && (maze[i][j]=='7' || maze[i][j]=='8' || maze[i][j]=='9')){
					console.getTextWindow().output(maze[i][j],red);
				}
				else if(maze[i][j] == '#') {
					console.getTextWindow().output(maze[i][j], white);
				}
				else if(maze[i][j] == '.') {
					console.getTextWindow().output(maze[i][j], pathColor);
				}
				else {
					console.getTextWindow().output(maze[i][j]);
				}
				cursorX++;
			}
			cursorY++;
		}
		hNumber.printNumber();
	}
	private void MoveTheNumber() {
		//Human number movement is controlled here
		printMaze();
		int oldX = hNumber.cursorX;
		int oldY = hNumber.cursorY;
		hNumber.HumanNumberMovement(chrNumber);
		if(!CanItMove(hNumber.cursorY, hNumber.cursorX)) {
			//if the movement can be made, the number moves, if not,
			//it is hitting the maze wall or the game has been over.
			hNumber.cursorX = oldX;
			hNumber.cursorY = oldY;
		}
		console.getTextWindow().output(oldX, oldY, ' ', blue);
		hNumber.printNumber();
		score += hNumber.CalculationScore();
		hNumber.printBackPack();
		isPressed = false;
	}
	private void addIcon() {
		//necessary icons are added to the screen here
		int cursorX = 62;
		int cursorY = 1;
		console.getTextWindow().setCursorPosition(cursorX, cursorY);
		cursorY++;
		console.getTextWindow().output("Input");
		for(int i = 0;i < 2; i++) {
			for(int j = 0;j < 10; j++) {	
				console.getTextWindow().setCursorPosition(cursorX + j, 2 + cursorY * i);
				console.getTextWindow().output('<', white);
			}
		}
		cursorY = 7;
		console.getTextWindow().setCursorPosition(cursorX, cursorY++);
		console.getTextWindow().output("Backpacks");
		for(int i = 0;i < 8; i++) {
			for(int j = 0;j < 4; j++) {
				if(j < 2) {
					console.getTextWindow().setCursorPosition(cursorX + j * 4, cursorY);
				}
				else {
					console.getTextWindow().setCursorPosition(cursorX - 2 + j * 4, cursorY);
				}
				console.getTextWindow().output('|');
			}
			cursorY++;
		}	 
		console.getTextWindow().setCursorPosition(cursorX, cursorY++);
		console.getTextWindow().output("+---+ +---+");
		console.getTextWindow().setCursorPosition(cursorX, cursorY++);
		console.getTextWindow().output("Left  Right");
		console.getTextWindow().setCursorPosition(cursorX + 2, cursorY++);
		console.getTextWindow().output("Q     W");
		console.getTextWindow().setCursorPosition(cursorX, cursorY += 2);
		console.getTextWindow().output("Score : ");
		console.getTextWindow().setCursorPosition(cursorX, ++cursorY);
		console.getTextWindow().output("Time : ");
	}
	private void fillTheMaze() throws IOException {
		//we fill the maze by reading it from the text file
		maze = new char[23][55];
		File file = new File("maze.txt");
		FileReader fReader = new FileReader(file);
		BufferedReader bReader = new BufferedReader(fReader);
		String line;
		int index = 0;
		while((line = bReader.readLine()) != null) {
			for(int i = 0;i < 55; i++) {
				maze[index][i] = line.charAt(i);
			}
			index++;
		}
		bReader.close();
	}
	private boolean CanItMove(int row , int column) {
		//We check that the human number movement is accessible.
		if(maze[row][column] == '#') {
			return false;
		}
		else if(maze[row][column] == ' ') {
			return true;
		}
		else {
			return ItCanEatNumber(row, column);
		}
	}
	private boolean ItCanEatNumber(int row, int column) {
		//If the number is less than the human number,
		//we add it to the backpacks, otherwise the game is over.
		char value = maze[row][column];
		String sValue = String.valueOf(value);
		int valueINT = Integer.valueOf(sValue);
		if(valueINT <= hNumber.hNumber) {
			maze[row][column] = ' ';
			if(!hNumber.backpack1.isFull()) {
				hNumber.backpack1.push(value);
			}
			else {
				hNumber.backpack1.pop();
				hNumber.backpack1.push(value);
			}
			deleteFromComputerNumbers(row, column);
			return true;
		}
		else {
			if(maze[row][column] != '#') {
				gameOverFlag = true;
			}
			return false;
		}
	}
	private void deleteFromComputerNumbers(int row, int column) {
		for(int i = 0;i < countOfComputerNumber; i++) {
			//When the human number eats a number, we delete it from the computer number sequence.
			if(compNumbers[i] != null) {
				if(compNumbers[i].row == row && compNumbers[i].column == column) {
					compNumbers[i] = null;
				}
			}
		}
	}
	private int getNewNumber() {
		return GenerateRandomNumber.generateNewNumber();
	}
	private void fillInTheInput() {
		while(inputs.size() != 10) {
			inputs.enqueue(getNewNumber());
		}
	}
	private void changeInput() {
		inputs.dequeue();
		inputs.enqueue(getNewNumber());
	}	
	private void printInputs() {
		int cursorX = 62; int cursorY = 3;
		for(int i = 0;i < inputs.size(); i++) {
			console.getTextWindow().setCursorPosition(cursorX, cursorY);
			console.getTextWindow().output(inputs.peek().toString());
			inputs.enqueue(inputs.dequeue());
			cursorX++;
		}
	}
	private int[] AssignRandomLocation(Object input) {
		//we assign a random coordinate to the new number added to the maze
		int[] coordinate = new int[2];
		while(true) {
			int row = GenerateRandomNumber.getRandomNumber(1, 21);
			int column = GenerateRandomNumber.getRandomNumber(1, 53);
			if(maze[row][column] == ' ') {
				maze[row][column] = input.toString().charAt(0);
				coordinate[0] = row;coordinate[1] = column;
				changeInput();
				return coordinate;		
			}
		}
	}
	private void SetPositionStartingStones() {
		//random numbers from 25 are added to the maze at the beginning of the game
		int counter = 0;
		while(true) {
			setComputerNumber(getNewNumber());
			counter++;
			if(counter == 25) {
				break;
			}
		}
	}
	private void setComputerNumber(int number) {
		//When a new number is added to the maze we assign it to the computer number array
		int[] coordinate = AssignRandomLocation(number);
		compNumbers[countOfComputerNumber] = new ComputerNumber(this, maze, console, number);
		compNumbers[countOfComputerNumber].setRow(coordinate[0]);compNumbers[countOfComputerNumber].setColumn(coordinate[1]);
		countOfComputerNumber++;
	}
	private void UploadGameLogin() throws Exception {
		//necessary things loaded at the beginning of the game
	    fillTheMaze(); 
	    inputs = new Queue(5000);
	    compNumbers = new ComputerNumber[1000];
	    hNumber = new HumanNumber(this, maze, console);
	    SetPositionStartingStones();
		addIcon();
		fillInTheInput();
		printInputs();
		printMaze();
		hNumber.printNumber();
	}
	private void setNewLocationForStones()  {
		//We make random movements for 4,5,6 and move 7,8,9s in the direction of the path they draw.
		for(int i = 0;i < countOfComputerNumber; i++) {
			if(compNumbers[i] != null) {
				if(compNumbers[i].getNumber() < 7 && compNumbers[i].getNumber() > 3) {
					compNumbers[i].setNewLocationNumbers();
				}
				else if(compNumbers[i].getNumber() > 6) {
					compNumbers[i].moveNumber();
				}
			}
		}
	}
	private void pathFindingNumber() {
		//human number is called every time it moves
		for(int i = 0;i < countOfComputerNumber; i++) {
			if(compNumbers[i] != null) {
				if(compNumbers[i].getNumber() > 6) {
					compNumbers[i].DFS();
				}
			}
		}
	}
	private void deleteOldPaths() {
		//human number is called every time it moves
		for(int i = 0;i < countOfComputerNumber; i++) {
			if(compNumbers[i] != null) {
				if(compNumbers[i].getNumber() > 6) {
					compNumbers[i].deleteOldPath();
				}
			}
		}
	}
	public void GameOver() throws Exception {
		//Endgame method was read from a game over script bit txt file and an endgame music was added.
		printMaze();
		URL url = this.getClass().getClassLoader().getResource("gameover.wav");
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.start();
		Thread.sleep(200);
		File file = new File("gameover.txt");
		FileReader fReader = new FileReader(file);
		BufferedReader bReader = new BufferedReader(fReader);
		String text;
		int crsX = 5, crsY = 28;
		while((text = bReader.readLine()) != null) {
			console.getTextWindow().setCursorPosition(crsX, crsY);
			console.getTextWindow().output(text, pathColor);
			Thread.sleep(100);
			crsY++;
		}
		bReader.close();
	}
}
