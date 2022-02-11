import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game in a
 * very simple GUI window.
 * 
 */

public class TicTacToeFrame extends TicTacToe implements ActionListener {
	// public static final String PLAYER_X = "X"; // player using "X"
	// public static final String PLAYER_O = "O"; // player using "O"
	// public static final String EMPTY = " "; // empty cell
	// public static final String TIE = "T"; // game ended in a tie

	private JLabel turn; // shows whos playing
	private JButton[][] board; // creates a board of buttons
	private JMenuItem Quit; // quits the game
	private JMenuItem newGame; // starts a new game

	/**
	 * Constructs a new Tic-Tac-Toe board with a frame, panel, buttons a menu, with
	 * options to close the game and start a new game
	 */
	public TicTacToeFrame() {

		JFrame frame = new JFrame("Tic Tac Toe"); // creates a window
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setFont(new Font("Monospaced", Font.BOLD, 18));

		// creates frame, panel, and menu
		JPanel panel = new JPanel();
		turn = new JLabel("It is X turn");
		board = new JButton[3][3];
		JMenuBar MenuBar = new JMenuBar();
		frame.setJMenuBar(MenuBar);
		JMenu menu = new JMenu("Options");
		MenuBar.add(menu);
		Quit = new JMenuItem("Quit Game");
		newGame = new JMenuItem("New Game");
		menu.add(Quit);
		Quit.addActionListener(this);
		menu.add(newGame);
		newGame.addActionListener(this);

		// shortcuts to close and reset the game
		final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
		Quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));

		frame.setLayout(new BorderLayout());
		panel.setLayout(new GridLayout(3, 3));

		// adds button to board
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = new JButton();
				board[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
				board[i][j].setPreferredSize(new Dimension(2, 2));
				panel.add(board[i][j]);
				board[i][j].addActionListener(this);
			}
		}
		frame.add(turn, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);

		winner = EMPTY;

		// default starting player is always X
		player = PLAYER_X;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TicTacToeFrame Board = new TicTacToeFrame();
	}

	/**
	 * Everytime a button is clicked it checkss
	 *
	 * @param ActionEvent e
	 */
	public void actionPerformed(ActionEvent e) {
		// checks if button is clicked
		if (e.getSource() instanceof JButton) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (e.getSource() == board[i][j]) {
						board[i][j].setEnabled(true);
						board[i][j].setText(player);
						// plays the game
						playGame(i, j);
					}
				}
			}
		}
		// checks if menu is clicked
		else if (e.getSource() instanceof JMenuItem) {
			if (e.getSource().equals(Quit)) {
				System.exit(0);
			}
			if (e.getSource().equals(newGame)) {
				clearBoard();
			}
		}
	}

	/**
	 * resets the game
	 *
	 */
	private void clearBoard() {
		// resets all the buttons
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j].setText(EMPTY);
				board[i][j].setEnabled(true);
			}
		}
		winner = EMPTY;
		numFreeSquares = 9;
		player = PLAYER_X; // Player X always has the first turn.
		turn.setText("It is X turn");
	}

	/**
	 * Returns true if filling the given square gives us a winner, and false
	 * otherwise.
	 *
	 * @param int row of square just set
	 * @param int col of square just set
	 * 
	 * @return true if we have a winner, false otherwise
	 */
	public boolean haveWinner(int row, int col) {
		// unless at least 5 squares have been filled, we don't need to go any further
		// (the earliest we can have a winner is after player X's 3rd move).

		if (numFreeSquares > 4)
			return false;

		// Note: We don't need to check all rows, columns, and diagonals, only those
		// that contain the latest filled square. We know that we have a winner
		// if all 3 squares are the same, as they can't all be blank (as the latest
		// filled square is one of them).

		// check row "row"
		if (board[row][0].getText().equals(board[row][1].getText())
				&& board[row][0].getText().equals(board[row][2].getText()))
			return true;

		// check column "col"
		if (board[0][col].getText().equals(board[1][col].getText())
				&& board[0][col].getText().equals(board[2][col].getText()))
			return true;

		// if row=col check one diagonal
		if (row == col)
			if (board[0][0].getText().equals(board[1][1].getText())
					&& board[0][0].getText().equals(board[2][2].getText()))
				return true;

		// if row=2-col check other diagonal
		if (row == 2 - col)
			if (board[0][2].getText().equals(board[1][1].getText())
					&& board[0][2].getText().equals(board[2][0].getText()))
				return true;

		// no winner yet
		return false;
	}

	/**
	 * Plays the game updates the board everytime a button is clicked
	 *
	 * @param int row of square
	 * @param int col of square
	 */
	public void playGame(int i, int j) {

		int row, col;
		// get input (row and column)

		row = i;
		col = j;

		numFreeSquares--; // decrement number of free squares

		// see if the game is over
		if (haveWinner(row, col)) {
			winner = player; // must be the player who just went
		} else if (numFreeSquares == 0) {
			winner = TIE; // board is full so it's a tie
		}
		// change to other player (this won't do anything if game has ended)
		if (player == PLAYER_X) {
			player = PLAYER_O;
		} else {
			player = PLAYER_X;
		}
		// changes the players turn text and disable a key from being clicked again
		if (winner.equals(EMPTY)) {
			turn.setText("it is " + player + " turn");
		} else {
			for (int t = 0; t < 3; t++) {
				for (int h = 0; h < 3; h++) {
					board[t][h].setEnabled(false);
				}
			}
			turn.setText("player " + winner + " won");
		}
	}
}