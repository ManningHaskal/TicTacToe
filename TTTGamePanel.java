import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import BJGamePanel.MyKeyAdapter;

import java.util.Random;



public class TTTGamePanel extends JPanel implements ActionListener{



	boolean running = false;
	Timer timer;
	Random random;
	boolean XTurn = true;
	String board[] = {"1","2","3","4","5","6","7","8","9"}; 
	String TTT[] = {"T","I","C","T","A","C","T","O","E"}; 
	int xpos[] = {41, 191, 341};
	int ypos[] = {253,403,553};
	int TTTx[] = {146, 236, 297, 146, 221, 297, 146, 219, 297};
	int TTTy[] = {117, 192, 267};
	int flashing = 0;
	int players = 0;
	int delay = 0;
	int win = 0;
	int score[] = {0,0};



	TTTGamePanel()
	{

		this.setPreferredSize(new Dimension(500,650));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());	
		startGame();
	}

	public void startGame()
	{
		timer = new Timer(10/*delay*/, this);
		timer.start();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g)
	{		

		if (running)
		{	
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(8));

			//top of the screen
			g.setFont(new Font ("", Font.BOLD, 60));

			g.setColor(XTurn? Color.cyan : Color.GREEN);
			FontMetrics metrics = getFontMetrics(g.getFont());
			if(players == 2)
				if(win == 0)
					if(XTurn)
						g.drawString("X\'s Turn", 250 - (metrics.stringWidth("X\'s Turn"))/2, g.getFont().getSize()+20);
					else
						g.drawString("O\'s Turn", 250 - (metrics.stringWidth("O\'s Turn"))/2, g.getFont().getSize()+20);
				else
				{
					if(XTurn && win < 9)
						g.drawString("X WINS", 250 - (metrics.stringWidth("X WINS"))/2, g.getFont().getSize()+20);
					else if(!XTurn && win < 9)
						g.drawString("O WINS", 250 - (metrics.stringWidth("O WINS"))/2, g.getFont().getSize()+20);
					else
					{
						g.setColor(Color.white);
						g.drawString("IT'S A DRAW", 250 - (metrics.stringWidth("IT'S A DRAW"))/2, g.getFont().getSize()+20);
					}

				}
			else
			{
				if(win == 0)
					g.drawString("V/S BOT", 250 - (metrics.stringWidth("V/S BOT"))/2, g.getFont().getSize()+20);
				else
				{
					if(XTurn && win < 9)
						g.drawString("X WINS", 250 - (metrics.stringWidth("X WINS"))/2, g.getFont().getSize()+20);
					else if(!XTurn && win < 9)
						g.drawString("O WINS", 250 - (metrics.stringWidth("O WINS"))/2, g.getFont().getSize()+20);
					else
					{
						g.setColor(Color.white);
						g.drawString("IT'S A DRAW", 250 - (metrics.stringWidth("IT'S A DRAW"))/2, g.getFont().getSize()+20);
					}

				}
			}
			g.setFont(new Font ("", Font.BOLD, 15));
			metrics = getFontMetrics(g.getFont());
			g.setColor(Color.LIGHT_GRAY);
			if(win==0)
				g.drawString("Press corresponding number to make move",250-(metrics.stringWidth("Press corresponding number to make move"))/2, g.getFont().getSize()+90);

			else
				g.drawString("Press R to play again or Enter to return to menu",250-(metrics.stringWidth("Press R to play again or Enter to return to menu"))/2, g.getFont().getSize()+90);




			//board
			g.setColor(Color.lightGray);
			g2.drawLine(325, 125, 325, 575);
			g2.drawLine(175, 125, 175, 575);
			g2.drawLine(25, 275, 475, 275);
			g2.drawLine(25, 425, 475, 425);
			g.setFont(new Font ("", Font.BOLD, 30));
			metrics = getFontMetrics(g.getFont());
			g.setColor(Color.white);
			g.drawString("Score: ", 250 - (metrics.stringWidth("Score: " + score[0] + " " + score[1]))/2, 630);
			g.setColor(Color.cyan);
			g.drawString(Integer.toString(score[0]), 275, 630);
			g.setColor(Color.green);
			g.drawString(Integer.toString(score[1]), 305, 630);

			//pieces
			g.setFont(new Font ("", Font.BOLD, 150));
			for (int i = 0; i < board.length; i++)
			{
				if (board[i] == "X")
					g.setColor(Color.cyan);
				else if(board[i] == "O")
					g.setColor(Color.GREEN);
				else 					
					g.setColor(Color.DARK_GRAY);
				g.drawString(board[i], xpos[i%3], ypos[i/3]);
			}

			//winstroke
			g2.setStroke(new BasicStroke(16));
			if (win > 0)
			{
				g2.setColor(XTurn ? Color.cyan : Color.green);
				if(win <= 3)
					g2.drawLine(win*150-50, 125, win*150-50, 575);
				else if(win <=6)
					g2.drawLine(25, (win-3)*150+50, 475, (win-3)*150+50);
				else if (win == 7)
					g2.drawLine(25, 125, 475, 575);
				else if (win == 8)
					g2.drawLine(475, 125, 25, 575);

			}



		}
		else
		{
			//intro screen
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			flashing++;

			//hash
			g.setColor(Color.lightGray);
			g2.drawLine(212, 50, 212, 275);
			g2.drawLine(288, 50, 288, 275);
			g2.drawLine(137, 125, 363, 125);
			g2.drawLine(137, 200, 363, 200);

			//TTT words
			g.setFont(new Font ("", Font.BOLD, 80));
			FontMetrics metrics = getFontMetrics(g.getFont());
			for (int i = 0; i < TTT.length; i++)
			{
				g.setColor(TTT[i] == "O" ? Color.green : Color.white);
				if (!(i == 7 && flashing%100<=40))
					g.drawString(TTT[i], TTTx[i], TTTy[i/3]);
			}

			//select player 1/2
			g.setFont(new Font ("", Font.ITALIC, 40));
			metrics = getFontMetrics(g.getFont());
			g.drawString("How Lonely", 250 - (metrics.stringWidth("How Lonely"))/2, g.getFont().getSize()+345);
			g.drawString("Are You?", 250 - (metrics.stringWidth("Are You?"))/2, 2*g.getFont().getSize()+355);

			g.setFont(new Font ("", Font.PLAIN, 30));
			metrics = getFontMetrics(g.getFont());
			g.setColor(Color.cyan);
			g.drawString("Press (1) for very", 250 - (metrics.stringWidth("Press (1) for very"))/2, g.getFont().getSize()+470);
			g.setColor(Color.green);
			g.drawString("Press (2) for not at all", 250 - (metrics.stringWidth("Press (2) for not at all"))/2, g.getFont().getSize()+535);
		}


	}

	public void aiMove() 
	{
		int bestScore = -999999;
		int score;
		int move = 9;
		for (int i = 0; i < board.length; i++) 
			if (board[i] != "X" && board[i] != "O")	
			{
				board[i] = "O";
				score = miniMax(board, 0, false);
				board[i] = String.valueOf(i+1);
				if (score > bestScore) 
				{
					bestScore = score;
					move = i;
				}
				//can make a 5050 chance to switch if equal or add to a list and chose from there
			}
		board[move] = "O";
		XTurn = true;
	}

	public int miniMax(String[] board, int depth, boolean isMaximizing) 
	{
		String result = testWin(board);
		if (result == "X") 
			return -10;
		else if (result == "O") 
			return 10;
		else if (result == "D") 
			return 0;
		int score;

		int bestScore;
		int moveHere = 9;
		if (isMaximizing) 
		{
			bestScore = -9999999;
			for (int i = 0; i < board.length; i++) 
			{
				if (board[i] != "X" && board[i] != "O")	
				{
					board[i] = "O";
					score = miniMax(board, depth + 1, false);
					board[i] = String.valueOf(i+1);
					bestScore = Integer.max(score, bestScore);
				}
			}
			return bestScore;
		}
		else 
		{
			bestScore = 99999999;
			for (int i = 0; i < board.length; i++) 
			{
				if (board[i] != "X" && board[i] != "O")	
				{
					board[i] = "X";
					score = miniMax(board, depth + 1, true);
					board[i] = String.valueOf(i+1);
					bestScore = Integer.min(score, bestScore);
				}
			}
			return bestScore;
		}
	}


	public static boolean isMovesLeft(String[] board) 
	{
		for (int i = 0; i < board.length; i++) 
			if (board[i] != "X" && board[i] != "O")
				return true;
		return false;
	}



	public void checkWin()
	{
		if (board[4] == board[0] && board[4] == board[8] && win == 0)
		{
			win = 7;
			if(testWin(board) == "X")
				score[0]++;
			else
				score[1]++;
			XTurn = !XTurn;
		}
		else if (board[4] == board[2] && board[4] == board[6] & win == 0)
		{
			win = 8;
			if(testWin(board) == "X")
				score[0]++;
			else
				score[1]++;
			XTurn = !XTurn;
		}
		for(int i = 0; i < 3; i++)
			if (board[i] == board[i+3] && board[i] == board[i+6] && win == 0)
			{
				win = i+1;
				if(testWin(board) == "X")
					score[0]++;
				else
					score[1]++;
				XTurn = !XTurn;
			}
		for(int i = 0; i <7; i+=3)
			if (board[i] == board[i+1] && board[i] == board[i+2] && win == 0)
			{
				win = 4+(i/3);
				if(testWin(board) == "X")
					score[0]++;
				else
					score[1]++;
				XTurn = !XTurn;
			}
		if(!isMovesLeft(board))
			win = 9;
	}

	public static String testWin(String board[])
	{
		if (board[4] == board[0] && board[4] == board[8])
		{
			return board[4];
		}
		else if (board[4] == board[2] && board[4] == board[6])
		{
			return board[4];
		}
		for(int i = 0; i < 3; i++)
			if (board[i] == board[i+3] && board[i] == board[i+6])
			{
				return board[i];

			}
		for(int i = 0; i <7; i+=3)
			if (board[i] == board[i+1] && board[i] == board[i+2])
			{
				return board[i];
			}
		if(!isMovesLeft(board))
			return "D";
		return "no";

		//checkdraw

	}

	public void gameOver(Graphics g)
	{




	}	


	@Override
	public void actionPerformed(ActionEvent e) {

		if (running)
		{
			checkWin();
			delay++;
			if(players==1 && !XTurn && win == 0)
			{
				aiMove();
			}
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(players == 0)
				switch(e.getKeyCode()) 
				{
				case KeyEvent.VK_1:
				{
					delay = 0;
					players = 1;
					running = true;
					int rand = (int)(Math.random()*2);
					XTurn = rand == 1 ? true: false;
					break;
				}
				case KeyEvent.VK_2:
				{
					delay = 0;
					players = 2;
					running = true;
					int rand = (int)(Math.random()*2);
					XTurn = rand == 1 ? true: false;
					break;
				}
				}
			if(players == 1)
				switch(e.getKeyCode()) 
				{
				case KeyEvent.VK_1:
					if (board[0] != "X" && board[0] != "O" && win == 0 && XTurn && delay > 5)
					{
						board[0] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_2:
					if (board[1] != "X" && board[1] != "O" && win == 0 && XTurn)
					{
						board[1] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_3:
					if (board[2] != "X" && board[2] != "O" && win == 0 && XTurn)
					{
						board[2] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_4:
					if (board[3] != "X" && board[3] != "O" && win == 0 && XTurn)
					{
						board[3] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_5:
					if (board[4] != "X" && board[4] != "O" && win == 0 && XTurn)
					{
						board[4] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_6:
					if (board[5] != "X" && board[5] != "O" && win == 0 && XTurn)
					{
						board[5] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_7:
					if (board[6] != "X" && board[6] != "O" && win == 0 && XTurn)
					{
						board[6] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_8:
					if (board[7] != "X" && board[7] != "O" && win == 0 && XTurn)
					{
						board[7] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_9:
					if (board[8] != "X" && board[8] != "O" && win == 0 && XTurn)
					{
						board[8] = "X";
						XTurn = false;
					}
					break;
				case KeyEvent.VK_R:
				{
					if(win != 0)
					{
						String Tboard[] = {"1","2","3","4","5","6","7","8","9"};
						board = Tboard;
						win = 0;
						int rand = (int)(Math.random()*2);
						XTurn = rand == 1 ? true: false;
					}
					break;

				}
				case KeyEvent.VK_ENTER:
				{
					if(win != 0)
					{
						players = 0;
						running = false;
						String Tboard[] = {"1","2","3","4","5","6","7","8","9"};
						board = Tboard;
						win = 0;
						score[0] = score[1] = 0;
					}
					break;

				}
				}
			if(players == 2)
				switch(e.getKeyCode()) 
				{
				case KeyEvent.VK_1:
					if (board[0] == "1" && win == 0)
					{
						board[0] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_2:
					if (board[1] == "2" && win == 0 && delay > 5)
					{
						board[1] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_3:
					if (board[2] == "3" && win == 0)
					{
						board[2] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_4:
					if (board[3] == "4" && win == 0)
					{
						board[3] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_5:
					if (board[4] == "5" && win == 0)
					{
						board[4] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_6:
					if (board[5] == "6" && win == 0)
					{
						board[5] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_7:
					if (board[6] == "7" && win == 0)
					{
						board[6] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_8:
					if (board[7] == "8" && win == 0)
					{
						board[7] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_9:
					if (board[8] == "9" && win == 0)
					{
						board[8] = XTurn ? "X" : "O";
						XTurn = !XTurn;
					}
					break;
				case KeyEvent.VK_R:
				{
					if(win != 0)
					{
						String Tboard[] = {"1","2","3","4","5","6","7","8","9"};
						board = Tboard;
						win = 0;
						int rand = (int)(Math.random()*2);
						XTurn = rand == 1 ? true: false;
					}
					break;

				}
				case KeyEvent.VK_ENTER:
				{
					if(win != 0)
					{
						players = 0;
						running = false;
						String Tboard[] = {"1","2","3","4","5","6","7","8","9"};
						board = Tboard;
						win = 0;
						score[0] = score[1] = 0;
					}
					break;

				}
				case KeyEvent.VK_0:
				{
					System.out.println(testWin(board));
					break;

				}
				}
		}
	}
}

