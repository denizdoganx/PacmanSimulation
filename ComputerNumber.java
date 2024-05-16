
public class ComputerNumber {
	public Game numberMaze;
	public char[][] maze;
	public enigma.console.TextWindow gameWindow;
	Stack pathToDeleteDFS;
	Stack pathToDeleteBFS;
	Stack backupPath;
	Stack instantPath;
	public int compNumber;
	public int row, column;
	public ComputerNumber(Game numberMaze, char[][] maze, enigma.console.Console console, int compNumber) {
		//Necessary things are prepared for each computer number
		this.gameWindow = console.getTextWindow();
		this.maze = maze;
		this.numberMaze = numberMaze;
		this.compNumber = compNumber;
		pathToDeleteDFS = new Stack(1000);
		pathToDeleteBFS = new Stack(1000);
		instantPath = new Stack(1000);
		backupPath = new Stack(1000);
	}
	public void setNewLocationNumbers() {
		//4,5,6 Necessary algorithms have been written to make a random move
		char tempNumber = maze[row][column];
		boolean rightControl = false,leftControl = false, upControl = false, downControl = false;
		while(true) {
			int randomLocation = GenerateRandomNumber.getRandomNumber(1, 4);
			if(randomLocation == 1) {
				if(isThereAStoneForNumberOfTheMaze(row - 1, column) && !controlHumanNumberCoordinate(row, column)) {
					maze[row - 1][column] = tempNumber;
					maze[row][column] = ' ';
					row--;
					break;
				}
				upControl= true;
			}
			else if(randomLocation == 2) {
				if(isThereAStoneForNumberOfTheMaze(row, column + 1) && !controlHumanNumberCoordinate(row, column)) {
					maze[row][column + 1] = tempNumber;
					maze[row][column] = ' ';
					column++;
					break;
				}
				rightControl = true;
			}
			else if(randomLocation  == 3) {
				if(isThereAStoneForNumberOfTheMaze(row + 1, column) && !controlHumanNumberCoordinate(row, column)) {
					maze[row + 1][column] = tempNumber;
					maze[row][column] = ' ';
					row++;
					break;
				}
				downControl = true;
			}
			else {
				if(isThereAStoneForNumberOfTheMaze(row, column - 1) && !controlHumanNumberCoordinate(row, column)) {
					maze[row][column - 1] = tempNumber;
					maze[row][column] = ' ';
					column--;
					break;
				}
				leftControl = true;
				
			}
			//if every side of the number is full we exit the loop without any movement
			if(ComputerNumberEatHumanNumber(row, column)) {
				numberMaze.gameOverFlag = true;
			}
			//if every side of the number is full we exit the loop without any movement
			if(upControl && rightControl && downControl && leftControl) {
				break;
			}
		}
	}
	private boolean controlHumanNumberCoordinate(int row, int column) {
		return row == numberMaze.hNumber.cursorY && column == numberMaze.hNumber.cursorX;
	}
	private boolean isThereAStoneForNumberOfTheMaze(int row, int column) {
		//The direction of 4,5,6 is being controlled
		if(maze[row][column] == ' ') {
			return true;
		}
		else {
			return false;
		}
	}
	public int getNumber() {
		return compNumber;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public void moveNumber() {
		//Path finding numbers were moved in the direction of the path they drew.
		//for depth first search
		if(!pathToDeleteDFS.isEmpty()) {
			Object way;
			way = pathToDeleteDFS.pop();
			backupPath.pop();
			maze[row][column] = ' ';
			if((int)way == 1) {
				row--;
				maze[row][column] = String.valueOf(compNumber).charAt(0);
			}
			else if((int)way == 2) {
				column++;
				maze[row][column] = String.valueOf(compNumber).charAt(0);
			}
			else if((int)way == 3) {
				row++;
				maze[row][column] = String.valueOf(compNumber).charAt(0);
			}
			else {
				column--;
				maze[row][column] = String.valueOf(compNumber).charAt(0);
			}
		}
		//for breadth first search
		else if(!pathToDeleteBFS.isEmpty()) {
			Object way;
			way = pathToDeleteBFS.peek();
			if((int)way == 1) {
				row--;
				if(maze[row][column] == ' ' || maze[row][column] == '.') {
					maze[row + 1][column] = ' ';
					maze[row][column] = String.valueOf(compNumber).charAt(0);
					pathToDeleteBFS.pop();
				}
				else {
					row++;
				}
			}
			else if((int)way == 2) {
				column++;
				if(maze[row][column] == ' ' || maze[row][column] == '.') {
					maze[row][column - 1] = ' ';
					maze[row][column] = String.valueOf(compNumber).charAt(0);
					pathToDeleteBFS.pop();
				}
				else {
					column--;
				}
			}
			else if((int)way == 3) {
				row++;
				if(maze[row][column] == ' ' || maze[row][column] == '.') {
					maze[row - 1][column] = ' ';
					maze[row][column] = String.valueOf(compNumber).charAt(0);
					pathToDeleteBFS.pop();
				}
				else {
					row--;
				}
			}
			else {
				column--;
				if(maze[row][column] == ' ' || maze[row][column] == '.') {
					maze[row][column + 1] = ' ';
					maze[row][column] = String.valueOf(compNumber).charAt(0);
					pathToDeleteBFS.pop();
				}
				else {
					column++;
				}
			}
			if(ComputerNumberEatHumanNumber(row, column)) {
				numberMaze.gameOverFlag = true;
			}
		}
	}
	public void deleteOldPath() {
		//old paths are deleted
		Object way;
		int row2 = row;int column2 = column;
		////for depth first search
		while(!pathToDeleteDFS.isEmpty()) {
			way = pathToDeleteDFS.pop();
			if((int)way == 1) {
				row2--;
				maze[row2][column2] = ' ';
			}
			else if((int)way == 2) {
				column2++;
				maze[row2][column2] = ' ';
			}
			else if((int)way == 3) {
				row2++;
				maze[row2][column2] = ' ';
			}
			else {
				column2--;
				maze[row2][column2] = ' ';
			}
		}
		////for breadth first search
		while(!pathToDeleteBFS.isEmpty()) {
			way = pathToDeleteBFS.pop();
			if((int)way == 1) {
				row2--;
				maze[row2][column2] = ' ';
			}
			else if((int)way == 2) {
				column2++;
				maze[row2][column2] = ' ';
			}
			else if((int)way == 3) {
				row2++;
				maze[row2][column2] = ' ';
			}
			else {
				column2--;
				maze[row2][column2] = ' ';
			}
		}
	}
	public void DFS() {
		//here additional operations for DFS are available
		while(!pathToDeleteDFS.isEmpty()) {
			pathToDeleteDFS.pop();
		}
		if(!backupPath.isEmpty()) {
			int pathLength = backupPath.size();
			Stack tempPath = new Stack(1000);
			Object deletedElement = 0;
			int beginRow = row;int beginColumn = column;
			int stepCounter = 0;
			boolean isSamePath = false;
			boolean isItSmallerPath = false;
			while(!backupPath.isEmpty()) {
				//if there are circles in the previous path, we delete them.
				deletedElement = backupPath.pop();
				tempPath.push(deletedElement);
				if((int)deletedElement == 1) {
					beginRow--;
					if(maze[beginRow][beginColumn] == ' ') {
						stepCounter++;
					}
					else {
						break;
					}
					if(upControl(beginRow, beginColumn)) {
						isItSmallerPath = true;
						break;
					}
				}
				else if((int)deletedElement == 2) {
					beginColumn++;
					if(maze[beginRow][beginColumn] == ' ') {
						stepCounter++;
					}
					else {
						break;
					}
					if(rightControl(beginRow, beginColumn)) {
						isItSmallerPath = true;
						break;
					}
				}
				else if((int)deletedElement == 3) {
					beginRow++;
					if(maze[beginRow][beginColumn] == ' ') {
						stepCounter++;
					}
					else {
						break;
					}
					if(downControl(beginRow, beginColumn)) {
						isItSmallerPath = true;
						break;
					}
				}
				else {
					beginColumn--;
					if(maze[beginRow][beginColumn] == ' ') {
						stepCounter++;
					}
					else {
						break;
					}
					if(leftControl(beginRow, beginColumn)) {
						isItSmallerPath = true;
						break;
					}
				}
			}
			//And at the same time, if the action made by the human number does not require a new path,
			//we did not try to find a new path in the blank, we used the old pathi.
			if(isItSmallerPath) {
				while(!backupPath.isEmpty()) {
					backupPath.pop();
				}
				Object element;
				while(!tempPath.isEmpty()) {
					element = tempPath.peek();
					tempPath.pop();
					backupPath.push(element);
					pathToDeleteDFS.push(element);
					if((int)element == 1) {
						maze[beginRow][beginColumn] = '.';
						beginRow++;
					}
					else if((int)element == 2) {
						maze[beginRow][beginColumn] = '.';
						beginColumn--;
					}
					else if((int)element == 3) {
						maze[beginRow][beginColumn] = '.';
						beginRow--;
					}
					else {
						maze[beginRow][beginColumn] = '.';
						beginColumn++;
					}
				}
			}
			else if(pathLength == stepCounter) {
				if((int)deletedElement == 1) {
					beginRow--;
					if(maze[beginRow][beginColumn] == ' ') {
						tempPath.push(1);
						if(upControl(beginRow, beginColumn)) {
							isSamePath = true;
						}
					}
				}
				else if((int)deletedElement == 2) {
					beginColumn++;
					if(maze[beginRow][beginColumn] == ' ') {
						tempPath.push(2);
						if(rightControl(beginRow, beginColumn)) {
							isSamePath = true;
						}
					}
				}
				else if((int)deletedElement == 3) {
					beginRow++;
					if(maze[beginRow][beginColumn] == ' ') {
						tempPath.push(3);
						if(downControl(beginRow, beginColumn)) {
							isSamePath = true;
						}
					}
				}
				else {
					beginColumn--;
					if(maze[beginRow][beginColumn] == ' ') {
						tempPath.push(4);
						if(leftControl(beginRow, beginColumn)) {
							isSamePath = true;
						}
					}
				}
				if(isSamePath) {
					int turningPoint;
					while(!tempPath.isEmpty()) {
						deletedElement = tempPath.pop();
						backupPath.push(deletedElement);
						pathToDeleteDFS.push(deletedElement);
						turningPoint = (int)deletedElement;
						if(turningPoint == 1) {
							maze[beginRow][beginColumn] = '.';
							beginRow++;
						}
						else if(turningPoint == 2) {
							maze[beginRow][beginColumn] = '.';
							beginColumn--;
						}
						else if(turningPoint == 3) {
							maze[beginRow][beginColumn] = '.';
							beginRow--;
						}
						else {
							maze[beginRow][beginColumn] = '.';
							beginColumn++;
						}
					}
				}
				else {
					while(!backupPath.isEmpty()) {
						backupPath.pop();
					}
				}
			}
			else {
				while(!backupPath.isEmpty()) {
					backupPath.pop();
				}
			}
		}
		//if a new path needs to be drawn, the following function is called
		if(backupPath.isEmpty()) {
			findNewPath();
		}
	}
	private void findNewPath() {
		char[][] bMaze = backupMaze();
		int direction;
		boolean isFindIt = false;
		int i = row;int j = column;
		int counterTime = 0;
		while(!isFindIt) {
			//We pick a random direction and go as far as we can go
			direction = chooseDirection();
			while(direction == 1 && i > 0) {
				i--;
				if(bMaze[i][j] == ' ') {
					instantPath.push(1);
					//We put zero on the crossed points so that the same paths are not tried over and over again.
					bMaze[i][j] = '0';
				}
				else {
					i++;
					break;
				}
				if(upControl(i, j)) {
					isFindIt = true;
					break;
				}
			}
			while(direction == 2 && j < 54) {
				j++;
				if(bMaze[i][j] == ' ') {
					instantPath.push(2);
					bMaze[i][j] = '0';
				}
				else {
					j--;
					break;
				}
				if(rightControl(i, j)) {
					isFindIt = true;
					break;
				}
			}
			while(direction == 3 && i < 22) {
				i++;
				if(bMaze[i][j] == ' ') {
					instantPath.push(3);
					bMaze[i][j] = '0';
				}
				else {
					i--;
					break;
				}
				if(downControl(i, j)) {
					isFindIt = true;
					break;
				}
			}
			while(direction == 4 && j > 0) {
				j--;
				if(bMaze[i][j] == ' ') {
					instantPath.push(4);
					bMaze[i][j] = '0';
				}
				else {
					j++;
					break;
				}
				if(leftControl(i, j)) {
					isFindIt = true;
					break;
				}
			}
			if(i > 0 && i < 22 && j > 0 && j < 54 && !isFindIt) {
				//For DFS, if there is no navigable direction,
				//we delete an element from the path until we go to another point with a navigable point.
				if(bMaze[i - 1][j] != ' ' && bMaze[i][j + 1] != ' ' && bMaze[i + 1][j] != ' ' && bMaze[i][j - 1] != ' ') {
					Object peekElement;
					int directionNumber;
					while(true) {
						peekElement = instantPath.pop();
						if(peekElement == null) {
							isFindIt = true;
							break; 
						}
						directionNumber = Integer.valueOf(peekElement.toString());
						if(directionNumber == 1) {
							i++;
						}
						else if(directionNumber == 2) {
							j--;
						}
						else if(directionNumber == 3) {
							i--;
						}
						else {
							j++;
						}
						if(bMaze[i - 1][j] == ' ' || bMaze[i][j + 1] == ' ' || bMaze[i + 1][j] == ' ' || bMaze[i][j - 1] == ' ' || bMaze[i][j] == ' ') {
							break;
						}
					}
				}
			}
			if(counterTime == 1000000) {
				break;
			}
			counterTime++;
		}
		//path opens in reverse and the path is drawn
		Object way;
		while(!instantPath.isEmpty()) {
			way = instantPath.pop();
			pathToDeleteDFS.push(way);
			backupPath.push(way);
			if((int)way == 1) {
				maze[i][j] = '.';
				i++;
			}
			else if((int)way == 2) {
				maze[i][j] = '.';
				j--;
			}
			else if((int)way == 3) {
				maze[i][j] = '.';
				i--;
			}
			else {
				maze[i][j] = '.';
				j++;
			}
		}
	}
	private boolean upControl(int row, int column) {
		//Checking human number availability
		int controlRow = numberMaze.hNumber.cursorY;int controlColumn = numberMaze.hNumber.cursorX;
		if(controlRow == row && controlColumn == column - 1) {
			return true;
		}
		else if(controlRow == row && controlColumn == column + 1) {
			return true;
		}
		else if(controlRow == row - 1 && controlColumn == column) {
			return true;
		}
		else {
			return false;
		}
	}
	private boolean rightControl(int row, int column) {
		//Checking human number availability
		int controlRow = numberMaze.hNumber.cursorY;int controlColumn = numberMaze.hNumber.cursorX;
		if(controlRow == row && controlColumn == column + 1) {
			return true;
		}
		else if(controlRow == row - 1 && controlColumn == column) {
			return true;
		}
		else if(controlRow == row + 1 && controlColumn == column) {
			return true;
		}
		else {
			return false;
		}
	}
	private boolean downControl(int row, int column) {
		//Checking human number availability
		int controlRow = numberMaze.hNumber.cursorY;int controlColumn = numberMaze.hNumber.cursorX;
		if(controlRow == row && controlColumn == column - 1) {
			return true;
		}
		else if(controlRow == row && controlColumn == column + 1) {
			return true;
		}
		else if(controlRow == row + 1 && controlColumn == column) {
			return true;
		}
		else {
			return false;
		}
	}
	private boolean leftControl(int row, int column) {
		//Checking human number availability
		int controlRow = numberMaze.hNumber.cursorY;int controlColumn = numberMaze.hNumber.cursorX;
		if(controlRow == row && controlColumn == column - 1) {
			return true;
		}
		else if(controlRow == row - 1 && controlColumn == column) {
			return true;
		}
		else if(controlRow == row + 1 && controlColumn == column) {
			return true;
		}
		else {
			return false;
		}
	}
	private int chooseDirection() {
		return GenerateRandomNumber.getRandomNumber(1, 4);
	}
	private char[][] backupMaze() {
		//Creating a backup maze for path finding
		char[][] backup = new char[23][55];
		for(int i = 0;i < 23; i++) {
			for(int j = 0;j < 55; j++) {
				if(maze[i][j] == '.' || maze[i][j] == '0') {
					backup[i][j] = ' ';
				}
				else {
					backup[i][j] = maze[i][j];
				}
			}
		}
		return backup;
	}
	private boolean ComputerNumberEatHumanNumber(int row, int column) {
		//Can you eat your computer number and human number?
		int controlRow = numberMaze.hNumber.cursorY;int controlColumn = numberMaze.hNumber.cursorX;
		if(compNumber > numberMaze.hNumber.hNumber) {
			if(controlRow == row - 1 && controlColumn == column) {
				return true;
			}
			else if(controlRow == row + 1 && controlColumn == column) {
				return true;
			}
			else if(controlRow == row && controlColumn == column - 1) {
				return true;
			}
			else if(controlRow == row && controlColumn == column + 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	public void BFS() {
		//Required structures for breadth first search created
		int[][] backup = new int[23][55];
		for(int i = 0;i < backup.length; i++) {
			for(int j = 0;j < backup[0].length; j++) {
				if(maze[i][j] == ' ' || maze[i][j] == '.') {
					backup[i][j] = 0;
				}
				else {
					backup[i][j] = - 1;
				}
			}
		}
		backup[row][column] = - 1;
		Queue bfsSearch = new Queue(1000);
		String coordinate = String.valueOf(row);
		coordinate += ";";
		coordinate += String.valueOf(column);
		String[] instantCoordinate;
		int instantRow;
		int instantColumn;
		int lastRow = 0,lastColumn = 0;
		int pathLength = 0;
		bfsSearch.enqueue(coordinate);
		while(!bfsSearch.isEmpty()) {
			//The cells to which each cell is attached were found and a map was created in the maze.
			//Coordinates of the element at the beginning of the queue are taken and a map
			//of navigable directions is created.
			instantCoordinate = bfsSearch.peek().toString().split(";");
			instantRow = Integer.valueOf(instantCoordinate[0]);
			instantColumn = Integer.valueOf(instantCoordinate[1]);
			if(backup[instantRow - 1][instantColumn] == 0) {
				coordinate = String.valueOf(instantRow - 1); 
				coordinate += ";";
				coordinate += String.valueOf(instantColumn);
				bfsSearch.enqueue(coordinate);
				backup[instantRow - 1][instantColumn] = 1;
				if(upControl(instantRow - 1, instantColumn)) {
					lastRow = instantRow - 1;
					lastColumn = instantColumn;
					break;
				}
			}
			if(backup[instantRow][instantColumn + 1] == 0) {
				coordinate = String.valueOf(instantRow); 
				coordinate += ";";
				coordinate += String.valueOf(instantColumn + 1);
				bfsSearch.enqueue(coordinate);
				backup[instantRow][instantColumn + 1] = 2;
				if(rightControl(instantRow, instantColumn + 1)) {
					lastRow = instantRow;
					lastColumn = instantColumn + 1;
					break;
				}
			}
			if(backup[instantRow + 1][instantColumn] == 0) {
				coordinate = String.valueOf(instantRow + 1); 
				coordinate += ";";
				coordinate += String.valueOf(instantColumn);
				bfsSearch.enqueue(coordinate);
				backup[instantRow + 1][instantColumn] = 3;
				if(downControl(instantRow + 1, instantColumn)) {
					lastRow = instantRow + 1;
					lastColumn = instantColumn;
					break;
				}
			}
			if(backup[instantRow][instantColumn - 1] == 0) {
				coordinate = String.valueOf(instantRow); 
				coordinate += ";";
				coordinate += String.valueOf(instantColumn - 1);
				bfsSearch.enqueue(coordinate);
				backup[instantRow][instantColumn - 1] = 4;
				if(leftControl(instantRow, instantColumn - 1)) {
					lastRow = instantRow;
					lastColumn = instantColumn - 1;
					break;
				}
			}
			bfsSearch.dequeue();
			pathLength++;
		}
		//The shortest path was processed as a path to the maze with reverse operations.
		for(int i = 0;i < pathLength + 1; i++) {
			if(backup[lastRow][lastColumn] == 1) {
				pathToDeleteBFS.push(1);
				if(maze[lastRow][lastColumn] == ' ') {
					maze[lastRow][lastColumn] = '.';	
				}
				lastRow++;
			}
			if(backup[lastRow][lastColumn] == 2) {
				pathToDeleteBFS.push(2);
				if(maze[lastRow][lastColumn] == ' ') {
					maze[lastRow][lastColumn] = '.';	
				}
				lastColumn--;
			}
			if(backup[lastRow][lastColumn] == 3) {
				pathToDeleteBFS.push(3);
				if(maze[lastRow][lastColumn] == ' ') {
					maze[lastRow][lastColumn] = '.';	
				}
				lastRow--;
			}
			else if(backup[lastRow][lastColumn] == 4) {
				pathToDeleteBFS.push(4);
				if(maze[lastRow][lastColumn] == ' ') {
					maze[lastRow][lastColumn] = '.';	
				}
				lastColumn++;
			}
		}
	}
}
