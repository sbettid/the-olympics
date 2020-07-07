package olympic_gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class OlympicsManager {

	public static void main(String[] args) {
		// create the frame
		JFrame frame = new JFrame("The Olympics");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1135, 850));
		frame.getContentPane().add(MainPanel.getMainPanel());
		
		// create menu bar and menu item
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem about = new JMenuItem("About");

		// create and add the action listener for exit and about
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		String message = "Olympic Dataset";
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainPanel.getMainPanel(), message, "Welcome!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon ("icons/appIcon.png"));
			}
		});

		// add menuItem to menu and the menu to the menuBar
		mb.add(menu);
		menu.add(exit);
		menu.add(about);

		frame.setJMenuBar(mb);
		
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

}
