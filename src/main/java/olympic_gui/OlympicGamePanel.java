package olympic_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core_classes.CountryMedal;
import core_classes.Olympic;
import queryManager.QueryMaker;

class JComponentTableCellRenderer implements TableCellRenderer {
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	      boolean hasFocus, int row, int column) {
	    return (JComponent) value;
	  }
	}

public class OlympicGamePanel extends JPanel{
	private JPanel oneOlympicPanel, titlePanel, scrollPanel;
	private JLabel title;
	private JScrollPane scrollPane;
	
	public OlympicGamePanel (int year, String type, String city) {
		// Create panel which contains all the components with a Box Layout
		oneOlympicPanel = new JPanel();
		oneOlympicPanel.setLayout(new BoxLayout(oneOlympicPanel, BoxLayout.Y_AXIS));
		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 50)));

		// title panel
		titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		title = new JLabel(city + " - " + year);
		title.setFont((new Font("Helvetica", Font.BOLD, 45)));
		//image1 = new JLabel();
		//image1.setIcon(new ImageIcon("icons/reversemovie.png"));
		//titlePanel.add(image1);
		//titlePanel.add(Box.createRigidArea(new Dimension(50, 0)));
		titlePanel.add(title);
		//titlePanel.add(Box.createRigidArea(new Dimension(50, 0)));
		//image2 = new JLabel();
		//image2.setIcon(new ImageIcon("icons/clapperboard.png"));
		//titlePanel.add(image2);
		oneOlympicPanel.add(titlePanel);
		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		
		
		
		
		// general data query
		String general = "SELECT ?n_events ?n_athletes ?n_countries ?n_disciplines ?startingDate ?endingDate ?website ?country ?noc\n" + 
				"{\n" + 
				"  ?game a :OlympicGame; :olympicYear ?year; :olympicType ?type; :olympicNumberEvents ?n_events; :olympicNumberAthletes ?n_athletes; :olympicNumberCountries ?n_countries; :olympicNumberDisciplines ?n_disciplines; :websiteURL ?website; :hostedIn [:cityIn [ :countryName ?country; :NOC ?noc]].\n" + 
				"  FILTER(?year = "+ year+ " && ?type = '" + type + "').\n" + 
				"}";
		
		JSONArray resultsGeneral = QueryMaker.query(general);
		
		String costQuery = "SELECT ?cost\n" + 
				"{\n" + 
				"  ?game a :OlympicGame; :olympicYear ?year; :olympicType ?type; :olympicCost ?cost. \n" + 
				"  FILTER(?year = "+year+" && ?type = '"+type+"').\n" + 
				"}";
		JSONArray costQ = QueryMaker.query(costQuery);
		
		
		
		JPanel descriptionP = new JPanel();
		descriptionP.setLayout(new BorderLayout());
		
		JLabel left = new JLabel("Prova");
		JLabel right = new JLabel("Prova2");
		
		descriptionP.add(left, BorderLayout.WEST);
		descriptionP.add(right, BorderLayout.EAST);
		
		oneOlympicPanel.add(descriptionP);
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));
		
		JPanel firstColumn = new JPanel();
		firstColumn.setLayout(new BoxLayout(firstColumn, BoxLayout.Y_AXIS));
		
		// path
		JPanel sub0 = new JPanel();
		sub0.setLayout(new BoxLayout(sub0, BoxLayout.X_AXIS));
		sub0.setAlignmentX(LEFT_ALIGNMENT);
		JLabel typeL = new JLabel("Type: " + type);
		sub0.add(typeL);
		
		// path
		JPanel subC = new JPanel();
		subC.setLayout(new BoxLayout(subC, BoxLayout.X_AXIS));
		subC.setAlignmentX(LEFT_ALIGNMENT);
		JLabel countryLabel = new JLabel("Country: " + resultsGeneral.getJSONObject(0).getJSONObject("country").getString("value"));
		subC.add(countryLabel);
		
		// length
		JPanel sub1 = new JPanel();
		sub1.setLayout(new BoxLayout(sub1, BoxLayout.X_AXIS));
		sub1.setAlignmentX(LEFT_ALIGNMENT);
		JLabel web = new JLabel("<html>Website: <a href=\"" + 
		resultsGeneral.getJSONObject(0).getJSONObject("website").getString("value") + "\">Click here</a></html>");
		web.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Desktop.getDesktop().browse(new URI(resultsGeneral.getJSONObject(0).getJSONObject("website").getString("value")));
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
				}
				
			}
		});
		sub1.add(web);
		
		// deleted
		JPanel sub6 = new JPanel();
		sub6.setLayout(new BoxLayout(sub6, BoxLayout.X_AXIS));
		sub6.setAlignmentX(LEFT_ALIGNMENT);
		double cost = 0.0;
		if(!costQ.isEmpty()) {
			
				cost = costQ.getJSONObject(0).getJSONObject("cost").getDouble("value");
			
		}
		
		JLabel costL = null;
		if (cost == 0.0)
			costL = new JLabel("Cost (in billions $): NA");
		else
			costL = new JLabel("Cost (in billions $): " + cost);

		sub6.add(costL);
		
		firstColumn.add(sub0);
		firstColumn.add(Box.createRigidArea(new Dimension(0,10)));
		firstColumn.add(subC);
		firstColumn.add(Box.createRigidArea(new Dimension(0,10)));
		firstColumn.add(sub1);
		firstColumn.add(Box.createRigidArea(new Dimension(0,10)));
		firstColumn.add(sub6);
		
		JPanel secondColumn = new JPanel();
		secondColumn.setLayout(new BoxLayout(secondColumn, BoxLayout.Y_AXIS));
		
		// add
		JPanel sub2 = new JPanel();
		sub2.setLayout(new BoxLayout(sub2, BoxLayout.X_AXIS));
		sub2.setAlignmentX(LEFT_ALIGNMENT);
		JLabel events = new JLabel("Number of events: " + resultsGeneral.getJSONObject(0).getJSONObject("n_events").getInt("value"));
		sub2.add(events);
		
		// deleted
		JPanel sub3 = new JPanel();
		sub3.setLayout(new BoxLayout(sub3, BoxLayout.X_AXIS));
		sub3.setAlignmentX(LEFT_ALIGNMENT);
		JLabel athletes = new JLabel("Number of athletes: " + resultsGeneral.getJSONObject(0).getJSONObject("n_athletes").getInt("value"));
		sub3.add(athletes);
		
		// deleted
		JPanel sub4 = new JPanel();
		sub4.setLayout(new BoxLayout(sub4, BoxLayout.X_AXIS));
		sub4.setAlignmentX(LEFT_ALIGNMENT);
		JLabel disciplines = new JLabel("Number of disciplines: " + resultsGeneral.getJSONObject(0).getJSONObject("n_disciplines").getInt("value"));
		sub4.add(disciplines);
		
		// deleted
		JPanel sub5 = new JPanel();
		sub5.setLayout(new BoxLayout(sub5, BoxLayout.X_AXIS));
		sub5.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon5 = new JLabel();
		icon5.setIcon(new ImageIcon("icons/gold.png"));
		JLabel countries = new JLabel("Number of countries: " + resultsGeneral.getJSONObject(0).getJSONObject("n_countries").getInt("value"));
		sub5.add(icon5);
		sub5.add(Box.createRigidArea(new Dimension(10, 0)));
		sub5.add(countries);
		
		secondColumn.add(sub2);
		secondColumn.add(Box.createRigidArea(new Dimension(0,10)));
		secondColumn.add(sub3);
		secondColumn.add(Box.createRigidArea(new Dimension(0,10)));
		secondColumn.add(sub4);
		secondColumn.add(Box.createRigidArea(new Dimension(0,10)));
		secondColumn.add(sub5);
		
		dataPanel.add(firstColumn);
		dataPanel.add(Box.createRigidArea(new Dimension(80,0)));
		dataPanel.add(secondColumn);
		
		JPanel container1 = new JPanel();
		JPanel container2 = new JPanel();
		container2.add(dataPanel);
		container2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		container1.add(container2);
		oneOlympicPanel.add(container2);
		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		
		
		// QUERY AND POPULATE
		Set<String> country_medals = new HashSet<String>();
		HashMap <String, Integer> golds = new HashMap<String, Integer>();
		HashMap <String, Integer> silvers = new HashMap<String, Integer>();
		HashMap <String, Integer> bronzes = new HashMap<String, Integer>();
		
		// query
		String queryG = "SELECT ?noc ?country (count(?medal) as ?medalCount)\n" + 
				"{\n" + 
				"  ?podium a :OnPodium; :onPodiumOlympicGame [ :olympicYear ?year; :olympicType ?type]; :onPodiumNR [ :representingNation [ :NOC ?noc; :countryName ?country]]; :medal ?medal.\n" + 
				"  FILTER(?year = " + year + " && ?type = '" +type + "' && ?medal = 'gold').\n" + 
				"}\n" + 
				"GROUP BY ?noc ?country";
		
		JSONArray resultsG = QueryMaker.query(queryG);
		for(int i =0; i<resultsG.length();i++) {
			String noc = resultsG.getJSONObject(i).getJSONObject("noc").getString("value");
			noc += "-" + resultsG.getJSONObject(i).getJSONObject("country").getString("value");
			int gold = resultsG.getJSONObject(i).getJSONObject("medalCount").getInt("value");
			golds.put(noc, gold);
			country_medals.add(noc);
		}
		
		
		String queryS = "SELECT ?noc ?country (count(?medal) as ?medalCount)\n" + 
				"{\n" + 
				"  ?podium a :OnPodium; :onPodiumOlympicGame [ :olympicYear ?year; :olympicType ?type]; :onPodiumNR [ :representingNation [ :NOC ?noc; :countryName ?country]]; :medal ?medal.\n" + 
				"  FILTER(?year = " + year + " && ?type = '" +type + "' && ?medal = 'silver').\n" + 
				"}\n" + 
				"GROUP BY ?noc ?country";
		
		JSONArray resultsS = QueryMaker.query(queryS);
		for(int i =0; i<resultsS.length();i++) {
			String noc = resultsS.getJSONObject(i).getJSONObject("noc").getString("value");
			noc += "-" + resultsS.getJSONObject(i).getJSONObject("country").getString("value");
			int silver = resultsS.getJSONObject(i).getJSONObject("medalCount").getInt("value");
			silvers.put(noc, silver);
			country_medals.add(noc);
		}
		
		String queryB = "SELECT ?noc ?country (count(?medal) as ?medalCount)\n" + 
				"{\n" + 
				"  ?podium a :OnPodium; :onPodiumOlympicGame [ :olympicYear ?year; :olympicType ?type]; :onPodiumNR [ :representingNation [ :NOC ?noc; :countryName ?country]]; :medal ?medal.\n" + 
				"  FILTER(?year = " + year + " && ?type = '" +type + "' && ?medal = 'bronze').\n" + 
				"}\n" + 
				"GROUP BY ?noc ?country";
		
		JSONArray resultsB = QueryMaker.query(queryB);
		for(int i =0; i<resultsB.length();i++) {
			String noc = resultsB.getJSONObject(i).getJSONObject("noc").getString("value");
			noc += "-" + resultsB.getJSONObject(i).getJSONObject("country").getString("value");
			int bronze = resultsB.getJSONObject(i).getJSONObject("medalCount").getInt("value");
			bronzes.put(noc, bronze);
			country_medals.add(noc);
		}
		
		// examine result set
		ArrayList<CountryMedal> medal_collection = new ArrayList<CountryMedal>();
		for (String s : country_medals) {
			
			CountryMedal curr = new CountryMedal(s, golds.getOrDefault(s, 0), silvers.getOrDefault(s, 0), bronzes.getOrDefault(s, 0));
			medal_collection.add(curr);
		}
		
		//sort
		Collections.sort(medal_collection, new Comparator<CountryMedal>() {
		    @Override
		    public int compare(CountryMedal o1, CountryMedal o2) {
		        if (o1.getGold() > o2.getGold())
		        	return -1;
		        else if (o1.getGold() < o2.getGold())
		        	return 1;
		        else {
		        	if (o1.getSilver() > o2.getSilver())
			        	return -1;
			        else if (o1.getSilver() < o2.getSilver())
			        	return 1;
			        else {
			        	if (o1.getBronze() > o2.getBronze())
				        	return -1;
				        else if (o1.getBronze() < o2.getBronze())
				        	return 1;
				        else
				        	return 0;
			        }
		        }
		    }
		});
		
		
		
		// JList
		
		String [] column_names = {"", "Country", "gold", "silver", "bronze", "total"};
		ArrayList<Object[]> data = new ArrayList<>();
		for (int i = 0; i < medal_collection.size(); i++) {
			String image = "icons/country/"+ medal_collection.get(i).getNoc() +".png";
			data.add(new Object[] {new ImageIcon(image), medal_collection.get(i).getCountry(),medal_collection.get(i).getGold(), medal_collection.get(i).getSilver(), medal_collection.get(i).getBronze(), medal_collection.get(i).getTotal()});
		}
		
		
		// JTable
		JTable table = new JTable(data.toArray(new Object[0][0]), column_names) {
			public Class getColumnClass(int column) {
				return (column == 0) ? Icon.class : Object.class;
			}
		};
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(60);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(60);
		table.getColumnModel().getColumn(5).setPreferredWidth(60);
		table.setEnabled(false);
		
		TableCellRenderer renderer = new JComponentTableCellRenderer();
	    TableColumnModel columnModel = table.getColumnModel();

	    TableColumn columnGold = columnModel.getColumn(2);
	    TableColumn columnSilver = columnModel.getColumn(3);
	    TableColumn columnBronze = columnModel.getColumn(4);
	    Border headerBorder = UIManager.getBorder("TableHeader.cellBorder");
	    
	    JLabel goldL = new JLabel(new ImageIcon("icons/gold.png"),JLabel.CENTER);
	    goldL.setBorder(headerBorder);
	    columnGold.setHeaderRenderer(renderer);
	    columnGold.setHeaderValue(goldL);
	    
	    JLabel silverL = new JLabel(new ImageIcon("icons/silver.png"),JLabel.CENTER);
	    silverL.setBorder(headerBorder);
	    columnSilver.setHeaderRenderer(renderer);
	    columnSilver.setHeaderValue(silverL);
	    
	    JLabel bronzeL = new JLabel(new ImageIcon("icons/bronze.png"),JLabel.CENTER);
	    bronzeL.setBorder(headerBorder);
	    columnBronze.setHeaderRenderer(renderer);
	    columnBronze.setHeaderValue(bronzeL);

		
		// ScrolledPane
		scrollPanel = new JPanel();
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(525, 346));
		scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollPanel.add(scrollPane);
		if (!medal_collection.isEmpty()) {
			oneOlympicPanel.add(scrollPanel);
		}

		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		
		JPanel backPanel = new JPanel();
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				OlympicSearchPanel olympicP = new OlympicSearchPanel();
				MainPanel.getMainPanel().swapPanel(olympicP);
				
			}
			
		});
		backPanel.add(back);	
		oneOlympicPanel.add(backPanel);
		
		
		
		
		
		add(oneOlympicPanel);
	}

}
