/**
2048 clone
--------------------------
- finish tasks marked throughout code
- check for win and lose conditions
- give option to reset on game over or win or continue on win
*/

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.util.Random;
import java.util.ArrayList;

public class TFEPlus extends JFrame implements KeyListener {
	Container pane = getContentPane();
	Board game;
	ArrayList<PendingAction> actions = new ArrayList<PendingAction>();
	
	public TFEPlus() {
		//set up the board
		game = new Board();
		add(game);
		//the game should always start with 2 random tiles
		game.generateTile();
		game.generateTile();
		//set frame properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(606,629);
		setTitle("2048");
		addKeyListener(this);
		setVisible(true);
	}
	
	//handle key presses
	@Override
	public void keyPressed(KeyEvent e) {
		/*predict tile movements needed to fill empty gaps (use two variables to track how many to move, each move in same sequence should be additive _X_X <- the first should move 1 left, the second should move 1 + 1 left) and add 1 if it is mergable with previous tile (need variable array size 4 to detect if a tiles in set were previously merged)*/
		boolean tileMoved = false; //used to track if a tile needs generated this move
		boolean[][] hasMerged = new boolean[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				hasMerged[i][j] = false;
			}
		}
		/**SUBSEQUENT BUTTON PRESSES SHOULD BE IGNORED UNTIL ANIMATION IS COMPLETE*/
		//predict the movement of each tile
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			//iterate through each block
			for(int i = 0; i < 4; i++) {
				//set up tracking for each row
				int moveCount = 0;
				for(int j = 1; j < 4; j++) {
					//skip this block if empty
					if(game.getTileNumber(i,j) > 0) {
						//for this row, count number of empty blocks that are between this and the end or previous non-zero block
						boolean sumThis = false;
						for(int k = j - 1; k > -1; k--) {
							if(game.getTileNumber(i,k) == 0) { //move through 0s
								moveCount++;
							} else if((game.getTileNumber(i,k) == game.getTileNumber(i,j)) && !hasMerged[i][k]) { //move into tile of same number if the other tile has not been merged already
								hasMerged[i][j] = true;
								moveCount++;
								sumThis = true;
								break;
							} else {
								break;
							}
						}
						//add animation to arraylist
						if(moveCount != 0) {
							actions.add(new PendingAction(i,j,i,j - moveCount,sumThis));
							tileMoved = true;
						}
					}
				}
			}
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//iterate through each block
			for(int i = 0; i < 4; i++) {
				//set up tracking for each row
				int moveCount = 0;
				for(int j = 2; j > -1; j--) {
					//skip this block if empty
					if(game.getTileNumber(i,j) > 0) {
						//for this row, count number of empty blocks that are between this and the end or previous non-zero block
						boolean sumThis = false;
						for(int k = j + 1; k < 4; k++) {
							if(game.getTileNumber(i,k) == 0) { //move through 0s
								moveCount++;
							} else if((game.getTileNumber(i,k) == game.getTileNumber(i,j)) && !hasMerged[i][k]) { //move into tile of same number if the other tile has not been merged already
								hasMerged[i][j] = true;
								moveCount++;
								sumThis = true;
								break;
							} else {
								break;
							}
						}
						//add animation to arraylist
						if(moveCount != 0) {
							actions.add(new PendingAction(i,j,i,j + moveCount,sumThis));
							tileMoved = true;
						}
					}
				}
			}
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			//iterate through each block
			for(int j = 0; j < 4; j++) {
				//set up tracking for each column
				int moveCount = 0;
				for(int i = 1; i < 4; i++) {
					//skip this block if empty
					if(game.getTileNumber(i,j) > 0) {
						//for this column, count number of empty blocks that are between this and the end or previous non-zero block
						boolean sumThis = false;
						for(int k = i - 1; k > -1; k--) {
							if(game.getTileNumber(k,j) == 0) { //move through 0s
								moveCount++;
							} else if((game.getTileNumber(k,j) == game.getTileNumber(i,j)) && !hasMerged[k][j]) { //move into tile of same number if the other tile has not been merged already
								hasMerged[i][j] = true;
								moveCount++;
								sumThis = true;
								break;
							} else {
								break;
							}
						}
						//add animation to arraylist
						if(moveCount != 0) {
							actions.add(new PendingAction(i,j,i - moveCount,j,sumThis));
							tileMoved = true;
						}
					}
				}
			}
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			//iterate through each block
			for(int j = 0; j < 4; j++) {
				//set up tracking for each column
				int moveCount = 0;
				for(int i = 2; i > -1; i--) {
					//skip this block if empty
					if(game.getTileNumber(i,j) > 0) {
						//for this column, count number of empty blocks that are between this and the end or previous non-zero block
						boolean sumThis = false;
						for(int k = i + 1; k < 4; k++) {
							if(game.getTileNumber(k,j) == 0) { //move through 0s
								moveCount++;
							} else if((game.getTileNumber(k,j) == game.getTileNumber(i,j)) && !hasMerged[k][j]) { //move into tile of same number if the other tile has not been merged already
								hasMerged[i][j] = true;
								moveCount++;
								sumThis = true;
								break;
							} else {
								break;
							}
						}
						//add animation to arraylist
						if(moveCount != 0) {
							actions.add(new PendingAction(i,j,i + moveCount,j,sumThis));
							tileMoved = true;
						}
					}
				}
			}
		} else {
			return;
		}
		//clear actions
		if(actions.size() != 0) {
			game.moveTiles(actions);
			actions.clear();
		}
	}
	
	//start the game
	public static void main(String args[]) {
		TFEPlus gui = new TFEPlus();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}
}

class Board extends JPanel implements ActionListener {
	Tile[][] tiles = new Tile[4][4];
	Timer timer = new Timer(15,this); //speed and actionlistener
	boolean victory = false;
	//used for queueing animations
	ArrayList<PendingAction> actions = new ArrayList<PendingAction>();
	
	public Board() {
		//allow for explicit placement of labels
		setLayout(null);
		//create tiles
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				tiles[i][j] = new Tile(i,j);
			}
		}
		repaint();
	}
	
	public void generateTile() {
		Random random = new Random();
		int startingTileX = random.nextInt(4);
		int startingTileY = random.nextInt(4);
		while(true) {
			if(getTileNumber(startingTileX, startingTileY) > 0) {
				startingTileX = random.nextInt(4);
				startingTileY = random.nextInt(4);
			} else {
				break;
			}
		}
		tiles[startingTileX][startingTileY].setNumber(Tile.generate());
	}
	
	public void moveTiles(ArrayList<PendingAction> actions) {
		for(int i = 0; i < actions.size(); i++) {
			this.actions.add(actions.get(i));
		}
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		//flag tiles that are moving to draw last to prevent overlap (moving tiles should be drawn over stationary ones)
		for(PendingAction p : actions) {
			tiles[p.getX1()][p.getY1()].setMovement(true);
		}
		//move all tiles in actions 1 interval
		/**CONSIDER FINDING WAY TO USE DOUBLES FOR THIS: 18.5*/
		int interval = 37; //steps in each movement (must be a number that divides evenly into the distance between tiles including a tile size: 148)
		for(int i = 0; i < this.actions.size(); i++) {
			//compare x positions; move if needed
			if(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getXPos() < tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].getXPos()) {
				tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].setPos(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getXPos() + interval, tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getYPos());
			} else if(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getXPos() > tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].getXPos()) {
				tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].setPos(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getXPos() - interval, tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getYPos());
			}
			//compare y positions; move if needed
			if(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getYPos() < tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].getYPos()) {
				tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].setPos(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getXPos(), tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getYPos() + interval);
			} else if(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getYPos() > tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].getYPos()) {
				tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].setPos(tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getXPos(), tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getYPos() - interval);
			}
		}
		//check for completed actions
		for(int i = 0; i < this.actions.size(); i++) {
			if(this.tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getXPos() == this.tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].getXPos() && this.tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getYPos() == this.tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].getYPos()) {
				//apply math to completed actions
				if(actions.get(i).getSumThis()) {
					this.tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].setNumber(this.tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].getNumber()*2); //set tile 2 value to double its current value
					this.tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].setNumber(0); //set tile 1 value to 0
				} else {
					this.tiles[this.actions.get(i).getX2()][this.actions.get(i).getY2()].setNumber(this.tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].getNumber()); //set tile 2 value to tile 1 value
					this.tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].setNumber(0); //set tile 1 value to 0
				}
				//apply pulse animation to added tiles
				/**PULSE ADDED TILES*/
				//reset tile positions using bubble out animation
				this.tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].reset(this.actions.get(i).getX1(), this.actions.get(i).getY1());
				/**APPLY BUBBLE OUT ANIMATION*/
				//remove completed action from arraylist
				tiles[this.actions.get(i).getX1()][this.actions.get(i).getY1()].setMovement(false);
				this.actions.remove(i);
				i--;
			}
		}
		repaint();
		//loop the timer if there are incomplete actions
		if(this.actions.size() == 0) {
			//check to see if the game is over
			checkBoard();
			//generate 1 random tile in an empty space
			generateTile();
			timer.stop();
		} else {
			timer.restart();
		}
	}
	
	public void checkBoard() {
		//check tiles for 2048
		if(!victory) {
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					if(tiles[i][j].getNumber() == 2048) {
						victory = true;
						if(JOptionPane.showConfirmDialog(null, "Would you like to play again?", "You won!", JOptionPane.YES_NO_OPTION) == 1) {
							System.exit(0);
						} else {
							return;
						}
					}
				}
			}
		}
		//check tiles for at least 1 empty space
		boolean moveAvailable = false;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(tiles[i][j].getNumber() == 0) {
					moveAvailable = true;
				}
			}
		}
		//check tiles for at least 1 possible move
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 3; j++) {
				if(tiles[i][j].getNumber() == tiles[i][j + 1].getNumber()) { //check rows for consecutive numbers
					moveAvailable = true;
				}
				if(tiles[j][i].getNumber() == tiles[j + 1][i].getNumber()) { //check columns for consecutive numbers
					moveAvailable = true;
				}
			}
		}
		//game over
		if(!moveAvailable) {
			JOptionPane.showMessageDialog(null, "Board Over", "You Lost.", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw background tiles
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				g.setColor(new Color(210,210,210));
				g.fillRect((i + 1) * 8 + i * 140, (j + 1) * 8 + j * 140, 140, 140);
			}
		}
		//draw only nonmoving tiles
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(!tiles[i][j].getMovement()) {
					//set color of tile
					tiles[i][j].update();
					//draw square
					g.setColor(tiles[i][j].getColor());
					g.fillRect(tiles[i][j].getXPos(),tiles[i][j].getYPos(),Tile.getSize(),Tile.getSize());
					//draw label
					g.setFont(Tile.getFont());
					if(tiles[i][j].getNumber() <= 2048) {
						g.setColor(Color.BLACK);
					} else {
						g.setColor(Color.GRAY);
					}
					//draw a centered string in each box
					FontMetrics metrics = g.getFontMetrics(Tile.getFont());
					int x = ((Tile.getSize() - metrics.stringWidth(tiles[i][j].getText())) / 2) + tiles[i][j].getXPos();
					int y = (((Tile.getSize() - metrics.getHeight()) / 2) + metrics.getAscent()) + tiles[i][j].getYPos();
					g.drawString(tiles[i][j].getText(), x, y);
				}
			}
		}
		//draw moving tiles
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(tiles[i][j].getMovement()) {
					//set color of tile
					tiles[i][j].update();
					//draw square
					g.setColor(tiles[i][j].getColor());
					g.fillRect(tiles[i][j].getXPos(),tiles[i][j].getYPos(),Tile.getSize(),Tile.getSize());
					//draw label
					g.setFont(Tile.getFont());
					if(tiles[i][j].getNumber() <= 2048) {
						g.setColor(Color.BLACK);
					} else {
						g.setColor(Color.GRAY);
					}
					//draw a centered string in each box
					FontMetrics metrics = g.getFontMetrics(Tile.getFont());
					int x = ((Tile.getSize() - metrics.stringWidth(tiles[i][j].getText())) / 2) + tiles[i][j].getXPos();
					int y = (((Tile.getSize() - metrics.getHeight()) / 2) + metrics.getAscent()) + tiles[i][j].getYPos();
					g.drawString(tiles[i][j].getText(), x, y);
				}
			}
		}
	}
	
	public int getTileNumber(int x, int y) {
		return tiles[x][y].getNumber();
	}
}

class PendingAction {
	int x1, x2, y1, y2;
	boolean sumThis;
	
	public PendingAction(int x1, int y1, int x2, int y2, boolean sumThis) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.sumThis = sumThis;
	}
	
	public int getX1() {
		return this.x1;
	}
	
	public int getY1() {
		return this.y1;
	}
	
	public int getX2() {
		return this.x2;
	}
	
	public int getY2() {
		return this.y2;
	}
	
	public boolean getSumThis() {
		return this.sumThis;
	}
}

class Tile {
	final static private int size = 140;
	private int xPos;
	private int yPos;
	private int xVel = 0;
	private int yVel = 0;
	private int number = 0;
	private boolean movement = false;
	
	Color zero = new Color(210,210,210);
	Color two = new Color(255,255,255);
	Color four = new Color(255,255,225);
	Color eight = new Color(255,178,102);
	Color sixteen = new Color(255,128,0);
	Color thirtyTwo = new Color(255,102,102);
	Color sixtyFour = new Color(255,51,51);
	Color oneTwentyEight = new Color(255,255,204);
	Color twoFiftySix = new Color(255,255,153);
	Color fiveTwelve = new Color(255,255,102);
	Color oneThousandTwentyFour = new Color(255,255,51);
	Color twoThousandFortyEight = new Color(255,255,0);
	Color highColor = new Color(0,0,0);
	private Color color = zero;
	
	static Font font = new Font("Serif", Font.BOLD, 48);
	
	public Tile(int xPos, int yPos) {
		this.xPos = (yPos + 1) * 8 + yPos * 140;
		this.yPos = (xPos + 1) * 8 + xPos * 140;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public void setPos(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public static int getSize() {
		return size;
	}
	
	public static Font getFont() {
		return font;
	}
	
	public static int generate() {
		Random random = new Random();
		int randomNumber = random.nextInt(10) + 1;
		if(randomNumber == 4) {
			return 4;
		} else {
			return 2;
		}
	}
	
	public boolean getMovement() {
		return this.movement;
	}
	
	public void setMovement(boolean movement) {
		this.movement = movement;
	}
	
	public void reset(int xPos, int yPos) {
		this.xPos = (yPos + 1) * 8 + yPos * 140;
		this.yPos = (xPos + 1) * 8 + xPos * 140;
	}
	
	public void update() {
		//set tile colors
		switch(this.number) {
			case 0:
				this.color = this.zero;
			break;
			case 2:
				this.color = this.two;
			break;
			case 4:
				this.color = this.four;
			break;
			case 8:
				this.color = this.eight;
			break;
			case 16:
				this.color = this.sixteen;
			break;
			case 32:
				this.color = this.thirtyTwo;
			break;
			case 64:
				this.color = this.sixtyFour;
			break;
			case 128:
				this.color = this.oneTwentyEight;
			break;
			case 256:
				this.color = this.twoFiftySix;
			break;
			case 512:
				this.color = this.fiveTwelve;
			break;
			case 1024:
				this.color = this.oneThousandTwentyFour;
			break;
			case 2048:
				this.color = this.twoThousandFortyEight;
			break;
			default:
				this.color = this.highColor;
		}
	}
	
	public String getText() {
		if(this.number != 0) {
			return String.valueOf(this.number);
		} else {
			return "";
		}
	}
}