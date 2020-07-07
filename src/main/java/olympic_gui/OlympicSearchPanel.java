package olympic_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;

import core_classes.Olympic;
import queryManager.QueryMaker;

public class OlympicSearchPanel extends JPanel{
	private JPanel olympicPanel, titlePanel, scrollPanel;
	private JLabel title;
	private JScrollPane scrollPane;
	private JTable olympicJTable;
	
	
	public OlympicSearchPanel() {
		// fill the olympic
		ArrayList<Olympic> oly_list = new ArrayList<Olympic>();
		
		String query = "SELECT ?city ?year ?type \n" + 
				"{\n" + 
				"  ?game a :OlympicGame; :olympicYear ?year; :olympicType ?type; :hostedIn [:cityName ?city].\n" + 
				"}";
		
		JSONArray results = QueryMaker.query(query);
		
		for(int i =0; i<results.length();i++) {
			Olympic curr = new Olympic();
			curr.setCity(results.getJSONObject(i).getJSONObject("city").getString("value"));
			curr.setYear(results.getJSONObject(i).getJSONObject("year").getInt("value"));
			curr.setType(results.getJSONObject(i).getJSONObject("type").getString("value"));
			
			oly_list.add(curr);
		}
		Collections.sort(oly_list, new Comparator<Olympic>() {
		    @Override
		    public int compare(Olympic o1, Olympic o2) {
		        if (o1.getYear() < o2.getYear())
		        	return -1;
		        else if (o1.getYear() > o2.getYear())
		        	return 1;
		        else 
		        	return 0;
		    }
		});
		
		
		
		// fill the cities for combobox
		String queryCity = "SELECT DISTINCT ?city\n" + 
				"{\n" + 
				"  ?game a :OlympicGame; :hostedIn [:cityName ?city].\n" + 
				"} ";
		
		JSONArray resultsCity = QueryMaker.query(queryCity);
		String[] cities = new String[resultsCity.length()+1];
		cities[0] = "All";
		for(int i =0; i<resultsCity.length();i++) {
			cities[i+1] = resultsCity.getJSONObject(i).getJSONObject("city").getString("value");
		}
		
		// fill the cities for combobox
		String queryYear = "SELECT DISTINCT ?year\n" + 
				"{\n" + 
				"  ?game a :OlympicGame; :olympicYear ?year.\n" + 
				"} ";
		
		JSONArray resultsYear = QueryMaker.query(queryYear);
		String[] years = new String[resultsYear.length()+1];
		years[0] = "All";
		for(int i =0; i<resultsYear.length();i++) {
			years[i+1] = resultsYear.getJSONObject(i).getJSONObject("year").getString("value");
		}
	
		// Create panel which contains all the components with a Box Layout
		olympicPanel = new JPanel();
		olympicPanel.setLayout(new BoxLayout(olympicPanel, BoxLayout.Y_AXIS));
		olympicPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		// title panel
		titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		title = new JLabel("The Olympics");
		title.setFont((new Font("Helvetica", Font.BOLD, 45)));
		JLabel image1 = new JLabel();
		image1.setIcon(new ImageIcon("icons/olympic-games.png"));
		titlePanel.add(image1);
		titlePanel.add(Box.createRigidArea(new Dimension(50, 0)));
		titlePanel.add(title);
		titlePanel.add(Box.createRigidArea(new Dimension(50, 0)));
		JLabel image2 = new JLabel();
		image2.setIcon(new ImageIcon("icons/olympic-games.png"));
		titlePanel.add(image2);

		olympicPanel.add(titlePanel);
		olympicPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		
		
		// ScrolledPane
		String[] columns = {"City", "Year", "Type"};
		
		ArrayList<Object[]> data = new ArrayList<>();
		for (int i = 0; i < oly_list.size(); i++)
			data.add(new Object[] {oly_list.get(i).getCity(),oly_list.get(i).getYear(),oly_list.get(i).getType() });
		
		olympicJTable = new JTable(data.toArray(new Object[0][0]), columns) {
			public boolean isCellEditable(int row, int column) {                
            	return false;               
		}};
		olympicJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		olympicJTable.setAutoCreateRowSorter(true);
		olympicJTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if (arg0.getClickCount() == 2) {
					int row = olympicJTable.rowAtPoint(arg0.getPoint());
					int year = (int)olympicJTable.getValueAt(row, 1);
					String type = (String)olympicJTable.getValueAt(row, 2);
					String city = (String)olympicJTable.getValueAt(row, 0);
					System.out.print(year + type);
					
					OlympicGamePanel op = new OlympicGamePanel(year, type, city);
					MainPanel.getMainPanel().swapPanel(op);
				}
				
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
		});;
		
		scrollPanel = new JPanel();
		scrollPane = new JScrollPane(olympicJTable);
		scrollPane.setPreferredSize(new Dimension(600, 346));
		scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
		scrollPane.setAlignmentX(CENTER_ALIGNMENT);
		scrollPanel.add(scrollPane);
		olympicPanel.add(scrollPanel);

		olympicPanel.add(Box.createRigidArea(new Dimension(0, 35)));
		
		
		
		// search
		JPanel search = new JPanel();
		JLabel searchLabel = new JLabel ("Search for olympic games by");
		search.add(searchLabel);
		olympicPanel.add(search);
		olympicPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		
		// combobox panel
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
		JPanel s1 = new JPanel();
		JPanel s2 = new JPanel();
		JPanel s3 = new JPanel();
		String [] types = {"All", "summergames", "wintergames"};
		JComboBox citiesCB = new JComboBox <>(cities);
		JComboBox yearCB = new JComboBox <>(years);
		JComboBox typeCB = new JComboBox <>(types);
		JLabel cityL = new JLabel ("City");
		JLabel yearL = new JLabel ("Year");
		JLabel typeL = new JLabel ("Type");
		s1.add(cityL);
		s1.add(citiesCB);
		comboPanel.add(s1);
		comboPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		s2.add(yearL);
		s2.add(yearCB);
		comboPanel.add(s2);
		comboPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		s3.add(typeL);
		s3.add(typeCB);
		comboPanel.add(s3);
		comboPanel.setBorder(BorderFactory.createEtchedBorder());

		olympicPanel.add(comboPanel);
		olympicPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		
		JPanel filterPanel = new JPanel();
		JButton filter = new JButton("Filter");
		filter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String combo1 = (String)citiesCB.getSelectedItem();
				String combo2 = (String)yearCB.getSelectedItem();
				String combo3 = (String)typeCB.getSelectedItem();
				((TableRowSorter) olympicJTable.getRowSorter()).setRowFilter(null);
				
				List<RowFilter<Object,Object>> rfs = new ArrayList<>();
				//check for All
				if (!combo1.equals("All")) {
					rfs.add(RowFilter.regexFilter(combo1, 0));
				}
				if (!combo2.equals("All")) {
					rfs.add(RowFilter.regexFilter(combo2, 1));
				}
				if (!combo3.equals("All")) {
					rfs.add(RowFilter.regexFilter(combo3, 2));
				}
				
				RowFilter<Object, Object> ref = RowFilter.andFilter(rfs);
				
				((TableRowSorter) olympicJTable.getRowSorter()).setRowFilter(ref);;
				
			}
			
		});
		filterPanel.add(filter);
		olympicPanel.add(filterPanel);
		add(olympicPanel);
	}

}
