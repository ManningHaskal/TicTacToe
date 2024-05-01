import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class TTTGameFrame extends JFrame{
	

	
	TTTGameFrame()
	{
		TTTGamePanel Panel = new TTTGamePanel();
		this.add(Panel);
		this.setBackground(Color.black);
		this.setTitle("Tic Tac Toe");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();		
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
	
	