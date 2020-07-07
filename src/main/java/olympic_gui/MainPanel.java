package olympic_gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import core_classes.Olympic;

public class MainPanel extends JPanel {
	// create ONE TIME the object
	private static final MainPanel mainPanel = new MainPanel();

	/**
	 * Constructor of the main panel. The constructor is private so that the main panel 
	 * will be created only at the beginning and another main panel cannot be created (singleton).
	 * At the beginning the login panel is the first one which is added to the main panel.
	 */
	private MainPanel() {
		
		
		OlympicSearchPanel intro = new OlympicSearchPanel();
		add(intro);
	}

	/**
	 * Method that allows to access to the main panel object.
	 * @return the MainPanel object.
	 */
	public static MainPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * Method that allows the switching between two panels in the program. The panel
	 * passed as parameter is that one which is now added to the main panel and, consequently,
	 * to the frame of the program and will be also shown to the user.
	 * @param panel the JPanel which is now added to the main panel and also visible in the program.
	 */
	public void swapPanel(JPanel panel) {
		JScrollPane to_swap = new JScrollPane(panel);
		to_swap.setPreferredSize(new Dimension(1135, 850));
		to_swap.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.removeAll();
		mainPanel.add(to_swap);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
}
