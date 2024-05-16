import java.awt.Color;
import java.awt.event.KeyEvent;

import enigma.console.TextAttributes;

public class HumanNumber {
	public Game numberMaze;
	public char[][] maze;
	public enigma.console.TextWindow gameWindow;
	public TextAttributes blue = new TextAttributes(Color.cyan, Color.black);
	public int cursorX = 12, cursorY = 10;
	public int hNumber = 5;
	private double firstSeconds;
	private double lastSeconds;
	public Stack backpack1;
	public Stack backpack2;
	public HumanNumber(Game numberMaze, char[][] maze, enigma.console.Console console) {
		this.gameWindow = console.getTextWindow();
		this.maze = maze;
		this.numberMaze = numberMaze;
		backpack1 = new Stack(8);
		backpack2 = new Stack(8);
	}
	public void printNumber() {
		//The human number is written to the console
		 gameWindow.setCursorPosition(cursorX, cursorY);
		 gameWindow.output((String.valueOf(hNumber)), blue);
	}
	public void HumanNumberMovement(int chrNumber) {
		//Human number and transitions between bags are here
		if(hNumber != 1) {
			if(chrNumber == KeyEvent.VK_RIGHT) {
				cursorX++;
			}
			else if(chrNumber == KeyEvent.VK_LEFT) {
				cursorX--;
			}
			else if(chrNumber == KeyEvent.VK_UP) {
				cursorY--;
			}
			else if(chrNumber == KeyEvent.VK_DOWN) {
				cursorY++;
			}
		}
		else {
			lastSeconds = numberMaze.second;
			if(lastSeconds - firstSeconds >= 4) {
				hNumber = 2;
			}
		}
		if(chrNumber == KeyEvent.VK_Q) {
			if(!backpack1.isFull() && !backpack2.isEmpty()) {
				gameWindow.output(70, 16 - backpack2.size(), ' ');
				backpack1.push(backpack2.pop());
			}
		}
		else if(chrNumber == KeyEvent.VK_W) {
			if(!backpack2.isFull() && !backpack1.isEmpty()) {
				gameWindow.output(64, 16 - backpack1.size(), ' ');
				backpack2.push(backpack1.pop());
			}
		}
	}
	public int CalculationScore() {
		//score calculation section
		int instantScore = 0;
 		if(!backpack1.isEmpty() && !backpack2.isEmpty()) {
 			//the bags are aligned to the smaller one and the score is calculated if any
 			int minLENGTH = Math.min(backpack1.size(), backpack2.size());
 	 		Stack temp1 = new Stack(8);
 	 		Stack temp2 = new Stack(8);
 	 		if(backpack1.size() != minLENGTH) {
 	 			int size1 = backpack1.size();
 	 			for(int i = 0;i < size1 - minLENGTH; i++) {
 	 				temp1.push(backpack1.pop());
 	 			}
 	 		}
 	 		if(backpack2.size() != minLENGTH) {
 	 			int size2 = backpack2.size();
 	 			for(int i = 0;i < size2 - minLENGTH; i++) {
 	 				temp2.push(backpack2.pop());
 	 			}
 	 		}
 	 		if(backpack1.peek() == backpack2.peek()) {
 	 			int controlNumber = Integer.valueOf(backpack1.peek().toString());
 	 			if(controlNumber >= 1 && controlNumber <= 3) {
 	 				instantScore = controlNumber * 1;
 	 			}
 	 			else if(controlNumber >= 4 && controlNumber <= 6) {
 	 				instantScore = controlNumber * 5;
 	 			}
 	 			else {
 	 				instantScore = controlNumber * 25;
 	 			}
 	 			setNewHNumber();
 	 			backpack1.pop();
 	 			backpack2.pop();
 	 		}
 	 		//removed elements added back
 	 		while(!temp1.isEmpty()) {
 	 			backpack1.push(temp1.pop());
	 		}
 	 		while(!temp2.isEmpty()) {
 	 			backpack2.push(temp2.pop());
 			}
 	 		unloadTheBackPack();
 		}
 		return instantScore;
 	}
	private void unloadTheBackPack() {
		//The section of the bags on the console was deleted and printed if the score was calculated
 		int cursorx = 64;
 		int cursory = 8;
 		for(int i = 0;i < 2; i++) {
 			cursory = 8;
 			for(int j = 0;j < 8; j++) {
 				gameWindow.output(cursorx, cursory, ' ');
 				cursory++;
 			}
 			cursorx += 6;
 		}
 	}
	public void printBackPack() {
		//This is the section where backpacks are written on the console.
		Stack tempBackPack1 = new Stack(8);
		Stack tempBackPack2 = new Stack(8);
		int cursorX1 = 64;
		int cursorX2 = 70;
		int cursorY = 15;
		while(!backpack1.isEmpty()) {
			tempBackPack1.push(backpack1.pop());
		}
		while(!backpack2.isEmpty()) {
			tempBackPack2.push(backpack2.pop());
		}
		while(!tempBackPack1.isEmpty()) {
			gameWindow.setCursorPosition(cursorX1, cursorY);
			gameWindow.output(tempBackPack1.peek().toString());
			backpack1.push(tempBackPack1.pop());
			cursorY--;
		}
		cursorY = 15;
		while(!tempBackPack2.isEmpty()) {
			gameWindow.setCursorPosition(cursorX2, cursorY);
			gameWindow.output(tempBackPack2.peek().toString());
			backpack2.push(tempBackPack2.pop());
			cursorY--;
		}
	}
	private void setNewHNumber() {
		//The procedure in which the human number falls from 9 to 1
		if(hNumber < 9) {
			hNumber++;
		}
		else {
			hNumber = 1;
			firstSeconds = numberMaze.second;
		}
	}
}
