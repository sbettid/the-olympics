package olympic_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
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
import core_classes.TorchVisit;
import core_classes.Winner;
import core_classes.WinnerCountry;
import queryManager.QueryMaker;

class JComponentTableCellRenderer implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		return (JComponent) value;
	}
}

public class OlympicGamePanel extends JPanel {
	private JPanel oneOlympicPanel, titlePanel, scrollPanel;
	private JLabel title;
	private JScrollPane scrollPane;

	public OlympicGamePanel(int year, String type, String city) {
		// Create panel which contains all the components with a Box Layout
		oneOlympicPanel = new JPanel();
		oneOlympicPanel.setLayout(new BoxLayout(oneOlympicPanel, BoxLayout.Y_AXIS));

		// title panel
		titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		title = new JLabel(city + " - " + year);
		title.setFont((new Font("Helvetica", Font.BOLD, 45)));
		
		titlePanel.add(title);
		
		oneOlympicPanel.add(titlePanel);
		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 35)));

		// general data query
		String general = "SELECT ?n_events ?n_athletes ?n_countries ?n_disciplines ?startingDate ?endingDate ?website ?country ?noc\n"
				+ "{\n"
				+ "  ?game a :OlympicGame; :olympicYear ?year; :olympicType ?type; :olympicNumberEvents ?n_events; :olympicNumberAthletes ?n_athletes; :olympicNumberCountries ?n_countries; :olympicNumberDisciplines ?n_disciplines; :websiteURL ?website; :hostedIn [:cityIn [ :countryName ?country; :NOC ?noc]].\n"
				+ "  FILTER(?year = " + year + " && ?type = '" + type + "').\n" + "}";

		JSONArray resultsGeneral = QueryMaker.query(general);

		String costQuery = "SELECT ?cost\n" + "{\n"
				+ "  ?game a :OlympicGame; :olympicYear ?year; :olympicType ?type; :olympicCost ?cost. \n"
				+ "  FILTER(?year = " + year + " && ?type = '" + type + "').\n" + "}";
		JSONArray costQ = QueryMaker.query(costQuery);

		JPanel descriptionP = new JPanel();
		descriptionP.setLayout(new BorderLayout());

		JLabel left = new JLabel("General Information");
		left.setFont((new Font("Helvetica", Font.BOLD, 17)));
		JButton viewTorch = new JButton("View torch route");
		viewTorch.addActionListener(new ShowTorchRoute(type, year));

		descriptionP.add(left, BorderLayout.WEST);
		descriptionP.add(viewTorch, BorderLayout.EAST);

		oneOlympicPanel.add(descriptionP);
		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));

		JPanel firstColumn = new JPanel();
		firstColumn.setLayout(new BoxLayout(firstColumn, BoxLayout.Y_AXIS));

		// path
		JPanel sub0 = new JPanel();
		sub0.setLayout(new BoxLayout(sub0, BoxLayout.X_AXIS));
		sub0.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon0 = new JLabel();
		if (type.contains("winter"))
			icon0.setIcon(new ImageIcon("icons/snow.png"));
		else
			icon0.setIcon(new ImageIcon("icons/sun.png"));
		JLabel typeL = new JLabel("Type: " + type);
		sub0.add(icon0);
		sub0.add(Box.createRigidArea(new Dimension(10, 0)));
		sub0.add(typeL);

		// path
		JPanel subC = new JPanel();
		subC.setLayout(new BoxLayout(subC, BoxLayout.X_AXIS));
		subC.setAlignmentX(LEFT_ALIGNMENT);
		JLabel iconC = new JLabel();
		iconC.setIcon(new ImageIcon(
				"icons/country/" + resultsGeneral.getJSONObject(0).getJSONObject("noc").getString("value") + ".png"));
		JLabel countryLabel = new JLabel(
				"Country: " + resultsGeneral.getJSONObject(0).getJSONObject("country").getString("value"));
		subC.add(iconC);
		subC.add(Box.createRigidArea(new Dimension(10, 0)));
		subC.add(countryLabel);

		// length
		JPanel sub1 = new JPanel();
		sub1.setLayout(new BoxLayout(sub1, BoxLayout.X_AXIS));
		sub1.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon1 = new JLabel();
		icon1.setIcon(new ImageIcon("icons/search.png"));
		JLabel web = new JLabel("<html>Website: <a href=\""
				+ resultsGeneral.getJSONObject(0).getJSONObject("website").getString("value")
				+ "\">Click here</a></html>");
		web.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Desktop.getDesktop().browse(
								new URI(resultsGeneral.getJSONObject(0).getJSONObject("website").getString("value")));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		sub1.add(icon1);
		sub1.add(Box.createRigidArea(new Dimension(10, 0)));
		sub1.add(web);

		// deleted
		JPanel sub6 = new JPanel();
		sub6.setLayout(new BoxLayout(sub6, BoxLayout.X_AXIS));
		sub6.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon6 = new JLabel();
		icon6.setIcon(new ImageIcon("icons/money.png"));
		String cost = "NA";
		if (!costQ.isEmpty()) {

			cost = costQ.getJSONObject(0).getJSONObject("cost").getString("value");

		}

		JLabel costL = null;
		costL = new JLabel("Cost (in billions $): " + cost);
		sub6.add(icon6);
		sub6.add(Box.createRigidArea(new Dimension(10, 0)));
		sub6.add(costL);

		firstColumn.add(sub0);
		firstColumn.add(Box.createRigidArea(new Dimension(0, 10)));
		firstColumn.add(subC);
		firstColumn.add(Box.createRigidArea(new Dimension(0, 10)));
		firstColumn.add(sub1);
		firstColumn.add(Box.createRigidArea(new Dimension(0, 10)));
		firstColumn.add(sub6);

		JPanel secondColumn = new JPanel();
		secondColumn.setLayout(new BoxLayout(secondColumn, BoxLayout.Y_AXIS));

		// add
		JPanel sub2 = new JPanel();
		sub2.setLayout(new BoxLayout(sub2, BoxLayout.X_AXIS));
		sub2.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon2 = new JLabel();
		icon2.setIcon(new ImageIcon("icons/medal.png"));
		JLabel events = new JLabel(
				"Number of events: " + resultsGeneral.getJSONObject(0).getJSONObject("n_events").getInt("value"));
		sub2.add(icon2);
		sub2.add(Box.createRigidArea(new Dimension(10, 0)));
		sub2.add(events);

		// deleted
		JPanel sub3 = new JPanel();
		sub3.setLayout(new BoxLayout(sub3, BoxLayout.X_AXIS));
		sub3.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon3 = new JLabel();
		icon3.setIcon(new ImageIcon("icons/athlete.png"));
		JLabel athletes = new JLabel(
				"Number of athletes: " + resultsGeneral.getJSONObject(0).getJSONObject("n_athletes").getInt("value"));
		sub3.add(icon3);
		sub3.add(Box.createRigidArea(new Dimension(10, 0)));
		sub3.add(athletes);

		// deleted
		JPanel sub4 = new JPanel();
		sub4.setLayout(new BoxLayout(sub4, BoxLayout.X_AXIS));
		sub4.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon4 = new JLabel();
		icon4.setIcon(new ImageIcon("icons/cup.png"));
		JLabel disciplines = new JLabel("Number of disciplines: "
				+ resultsGeneral.getJSONObject(0).getJSONObject("n_disciplines").getInt("value"));
		sub4.add(icon4);
		sub4.add(Box.createRigidArea(new Dimension(10, 0)));
		sub4.add(disciplines);

		// deleted
		JPanel sub5 = new JPanel();
		sub5.setLayout(new BoxLayout(sub5, BoxLayout.X_AXIS));
		sub5.setAlignmentX(LEFT_ALIGNMENT);
		JLabel icon5 = new JLabel();
		icon5.setIcon(new ImageIcon("icons/flag.png"));
		JLabel countries = new JLabel(
				"Number of countries: " + resultsGeneral.getJSONObject(0).getJSONObject("n_countries").getInt("value"));
		sub5.add(icon5);
		sub5.add(Box.createRigidArea(new Dimension(10, 0)));
		sub5.add(countries);

		secondColumn.add(sub2);
		secondColumn.add(Box.createRigidArea(new Dimension(0, 10)));
		secondColumn.add(sub3);
		secondColumn.add(Box.createRigidArea(new Dimension(0, 10)));
		secondColumn.add(sub4);
		secondColumn.add(Box.createRigidArea(new Dimension(0, 10)));
		secondColumn.add(sub5);

		dataPanel.add(firstColumn);
		dataPanel.add(Box.createRigidArea(new Dimension(80, 0)));
		dataPanel.add(secondColumn);

		JPanel container1 = new JPanel();
		JPanel container2 = new JPanel();
		container2.add(dataPanel);
		container2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		container1.add(container2);
		oneOlympicPanel.add(container2);
		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		// QUERY AND POPULATE
		Set<String> country_medals = new HashSet<String>();
		HashMap<String, Integer> golds = new HashMap<String, Integer>();
		HashMap<String, Integer> silvers = new HashMap<String, Integer>();
		HashMap<String, Integer> bronzes = new HashMap<String, Integer>();

		// query
		String queryG = "SELECT ?noc ?country (count(?medal) as ?medalCount)\n" + "{\n"
				+ "  ?podium a :OnPodium; :onPodiumOlympicGame [ :olympicYear ?year; :olympicType ?type]; :onPodiumNR [ :representingNation [ :NOC ?noc; :countryName ?country]]; :medal ?medal.\n"
				+ "  FILTER(?year = " + year + " && ?type = '" + type + "' && ?medal = 'gold').\n" + "}\n"
				+ "GROUP BY ?noc ?country";

		JSONArray resultsG = QueryMaker.query(queryG);
		for (int i = 0; i < resultsG.length(); i++) {
			String noc = resultsG.getJSONObject(i).getJSONObject("noc").getString("value");
			noc += "-" + resultsG.getJSONObject(i).getJSONObject("country").getString("value");
			int gold = resultsG.getJSONObject(i).getJSONObject("medalCount").getInt("value");
			golds.put(noc, gold);
			country_medals.add(noc);
		}

		String queryS = "SELECT ?noc ?country (count(?medal) as ?medalCount)\n" + "{\n"
				+ "  ?podium a :OnPodium; :onPodiumOlympicGame [ :olympicYear ?year; :olympicType ?type]; :onPodiumNR [ :representingNation [ :NOC ?noc; :countryName ?country]]; :medal ?medal.\n"
				+ "  FILTER(?year = " + year + " && ?type = '" + type + "' && ?medal = 'silver').\n" + "}\n"
				+ "GROUP BY ?noc ?country";

		JSONArray resultsS = QueryMaker.query(queryS);
		for (int i = 0; i < resultsS.length(); i++) {
			String noc = resultsS.getJSONObject(i).getJSONObject("noc").getString("value");
			noc += "-" + resultsS.getJSONObject(i).getJSONObject("country").getString("value");
			int silver = resultsS.getJSONObject(i).getJSONObject("medalCount").getInt("value");
			silvers.put(noc, silver);
			country_medals.add(noc);
		}

		String queryB = "SELECT ?noc ?country (count(?medal) as ?medalCount)\n" + "{\n"
				+ "  ?podium a :OnPodium; :onPodiumOlympicGame [ :olympicYear ?year; :olympicType ?type]; :onPodiumNR [ :representingNation [ :NOC ?noc; :countryName ?country]]; :medal ?medal.\n"
				+ "  FILTER(?year = " + year + " && ?type = '" + type + "' && ?medal = 'bronze').\n" + "}\n"
				+ "GROUP BY ?noc ?country";

		JSONArray resultsB = QueryMaker.query(queryB);
		for (int i = 0; i < resultsB.length(); i++) {
			String noc = resultsB.getJSONObject(i).getJSONObject("noc").getString("value");
			noc += "-" + resultsB.getJSONObject(i).getJSONObject("country").getString("value");
			int bronze = resultsB.getJSONObject(i).getJSONObject("medalCount").getInt("value");
			bronzes.put(noc, bronze);
			country_medals.add(noc);
		}

		// examine result set
		ArrayList<CountryMedal> medal_collection = new ArrayList<CountryMedal>();
		for (String s : country_medals) {

			CountryMedal curr = new CountryMedal(s, golds.getOrDefault(s, 0), silvers.getOrDefault(s, 0),
					bronzes.getOrDefault(s, 0));
			medal_collection.add(curr);
		}

		// sort
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

		String[] column_names = { "", "Country", "gold", "silver", "bronze", "total" };
		ArrayList<Object[]> data = new ArrayList<>();
		for (int i = 0; i < medal_collection.size(); i++) {
			String image = "icons/country/" + medal_collection.get(i).getNoc() + ".png";
			data.add(new Object[] { new ImageIcon(image), medal_collection.get(i).getCountry(),
					medal_collection.get(i).getGold(), medal_collection.get(i).getSilver(),
					medal_collection.get(i).getBronze(), medal_collection.get(i).getTotal() });
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

		JLabel goldL = new JLabel(new ImageIcon("icons/gold.png"), JLabel.CENTER);
		goldL.setBorder(headerBorder);
		columnGold.setHeaderRenderer(renderer);
		columnGold.setHeaderValue(goldL);

		JLabel silverL = new JLabel(new ImageIcon("icons/silver.png"), JLabel.CENTER);
		silverL.setBorder(headerBorder);
		columnSilver.setHeaderRenderer(renderer);
		columnSilver.setHeaderValue(silverL);

		JLabel bronzeL = new JLabel(new ImageIcon("icons/bronze.png"), JLabel.CENTER);
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
		
		JPanel support = new JPanel();
		JLabel supportInfo = new JLabel("Medal Collection");
		supportInfo.setFont((new Font("Helvetica", Font.BOLD, 20)));
		support.add(supportInfo);
	
		if (!medal_collection.isEmpty()) {
			oneOlympicPanel.add(support);
			oneOlympicPanel.add(scrollPanel);
		} else {
			oneOlympicPanel.add(support);
			JLabel no_information = new JLabel("No available information about this game");
			no_information.setAlignmentX(Component.CENTER_ALIGNMENT);
			oneOlympicPanel.add(no_information);
		}

		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		
		
		// search discipline
		// combobox panel
		
		String disciplineQuery = "SELECT ?discipline_name\n" + 
				"{\n" + 
				"  ?include a :Include; :includeOlympicGame [ :olympicType ?type; :olympicYear ?year]; :includeDiscipline [:disciplineName ?discipline_name].\n" + 
				"  FILTER(?year = "+year+" && ?type = '"+type+"').\n" + 
				"}";
		
		JSONArray disciplineR = QueryMaker.query(disciplineQuery);
		String[] list_disc = new String[disciplineR.length()+1];
		list_disc[0] = "-";
		for (int i = 0; i < disciplineR.length(); i++) {
			list_disc[i+1] = disciplineR.getJSONObject(i).getJSONObject("discipline_name").getString("value");
		}
		
		JPanel comboPanel = new JPanel();
		JComboBox eventCB = new JComboBox <>(new String[] {"-"});
		JButton search = new JButton("Search winners");
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selected_event = (String)eventCB.getSelectedItem();
				String winnerQ = "SELECT distinct ?name ?surname  ?gender ?medal\n" + 
						"{\n" + 
						"  ?on_podium a :OnPodium; :medal ?medal; :onPodiumOlympicGame [:olympicType ?type; :olympicYear ?year]; :onPodiumEvent [ :eventName ?event_name]; :onPodiumNR [ :athleteFirstName ?name; :athleteLastName ?surname; :nationRepresenterGender ?gender;];\n" + 
						"  FILTER(?year = "+year+" && ?type = '"+type+"' && ?event_name = '"+ selected_event +"').\n" + 
						"}";
				JSONArray winnerR = QueryMaker.query(winnerQ);
				
				
				
				JPanel winnerPanel = new JPanel();
				winnerPanel.setLayout(new BoxLayout(winnerPanel, BoxLayout.X_AXIS));
				
				boolean team_event = false;
				if(winnerR.length() <= 6) {
					ArrayList<Winner> males = new ArrayList<>();
					ArrayList<Winner> female = new ArrayList<>();
				
					for(int i = 0; i < winnerR.length(); i++) {
						
						String name = winnerR.getJSONObject(i).getJSONObject("name").getString("value");
						String surname = winnerR.getJSONObject(i).getJSONObject("surname").getString("value");
						String gender = winnerR.getJSONObject(i).getJSONObject("gender").getString("value");
						String medal = winnerR.getJSONObject(i).getJSONObject("medal").getString("value");
						
						if(gender.equals("Men")) {
							males.add(new Winner(name,surname,gender,medal));
						} else 
							female.add(new Winner(name,surname,gender,medal));
					}
					
					
					//Sort males
					Collections.sort(males, new Comparator<Winner>(){

						@Override
						public int compare(Winner arg0, Winner arg1) {
							if(arg0.getMedal().equals("gold"))
								return -1;
							else if(arg1.getMedal().equals("gold"))
								return 1;
							else {
								if(arg0.getMedal().equals("silver"))
									return -1;
								else
									return 1;
							
							}
							
						}
						
					});
					
					Collections.sort(female, new Comparator<Winner>(){

						@Override
						public int compare(Winner arg0, Winner arg1) {
							if(arg0.getMedal().equals("gold"))
								return -1;
							else if(arg1.getMedal().equals("gold"))
								return 1;
							else {
								if(arg0.getMedal().equals("silver"))
									return -1;
								else
									return 1;
							
							}
							
						}
						
					});
					
					
					String[] column_names = { "Name", "Surname", "Medal"};
					ArrayList<Object[]> data = new ArrayList<>();
					for (int i = 0; i < males.size(); i++) {
						data.add(new Object[] { males.get(i).getName(), males.get(i).getSurname(), new ImageIcon("icons/"+ males.get(i).getMedal() + ".png")});
					}
					
					JTable malesTable = new JTable(data.toArray(new Object[0][0]), column_names){
						public Class getColumnClass(int column) {
							return (column == 2) ? Icon.class : Object.class;
						}
					};
					malesTable.setEnabled(false);
					malesTable.setRowHeight(table.getRowHeight() + 20);					
					
					ArrayList<Object[]> data2 = new ArrayList<>();
					for (int i = 0; i < female.size(); i++) {
						data2.add(new Object[] { female.get(i).getName(), female.get(i).getSurname(), new ImageIcon("icons/"+ female.get(i).getMedal() + ".png")});
					}
					
					JTable femaleTable = new JTable(data2.toArray(new Object[0][0]), column_names) {
						public Class getColumnClass(int column) {
							return (column == 2) ? Icon.class : Object.class;
						}
					};
					
					femaleTable.setEnabled(false);
					femaleTable.setRowHeight(table.getRowHeight() + 20);					
					// divide male and female
					JPanel malePanel = new JPanel();
					malePanel.setLayout(new BoxLayout(malePanel, BoxLayout.Y_AXIS));
					JPanel s1 = new JPanel();
					s1.add( new JLabel("Male"));
					JScrollPane malePane = new JScrollPane(malesTable);
					malePane.setPreferredSize(new Dimension(300, 130));
					malePanel.add(s1);
					malePanel.add(malePane);
					
					JPanel femalePanel = new JPanel();
					femalePanel.setLayout(new BoxLayout(femalePanel, BoxLayout.Y_AXIS));
					JPanel s2 = new JPanel();
					s2.add(new JLabel("Female"));
					JScrollPane femalePane = new JScrollPane(femaleTable);
					femalePane.setPreferredSize(new Dimension(300, 130));
					femalePanel.add(s2);
					femalePanel.add(femalePane);
					
					boolean male_present = false;
					if(males.size() != 0) {
						male_present = true;
						winnerPanel.add(malePanel);
					}
					if(female.size() != 0) {
						if (male_present)
							winnerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
						winnerPanel.add(femalePanel);
					}
					
				} else {
					team_event = true;
					ArrayList<WinnerCountry> males = new ArrayList<>();
					ArrayList<WinnerCountry> female = new ArrayList<>();
					
					String winnerC = "SELECT distinct ?country ?gender ?medal\n" + 
							"{\n" + 
							"  ?on_podium a :OnPodium; :medal ?medal; :onPodiumOlympicGame [:olympicType ?type; :olympicYear ?year]; :onPodiumEvent [ :eventName ?event_name]; :onPodiumNR [ :nationRepresenterGender ?gender; :representingNation [:countryName ?country]];\n" + 
							"  FILTER(?year = "+year+" && ?type = '"+type+"' && ?event_name = '"+ selected_event +"').\n" + 
							"}";
					JSONArray winnerCountries = QueryMaker.query(winnerC);
					
					for(int i = 0; i < winnerCountries.length(); i++) {
						
						String country = winnerCountries.getJSONObject(i).getJSONObject("country").getString("value");
						String gender = winnerCountries.getJSONObject(i).getJSONObject("gender").getString("value");
						String medal = winnerCountries.getJSONObject(i).getJSONObject("medal").getString("value");
						
						if(gender.equals("Men")) {
							males.add(new WinnerCountry(country,gender,medal));
						} else 
							female.add(new WinnerCountry(country,gender,medal));
					}
					
					
					//Sort males
					Collections.sort(males, new Comparator<WinnerCountry>(){

						@Override
						public int compare(WinnerCountry arg0, WinnerCountry arg1) {
							if(arg0.getMedal().equals("gold"))
								return -1;
							else if(arg1.getMedal().equals("gold"))
								return 1;
							else {
								if(arg0.getMedal().equals("silver"))
									return -1;
								else
									return 1;
							
							}
							
						}
						
					});
					
					Collections.sort(female, new Comparator<WinnerCountry>(){

						@Override
						public int compare(WinnerCountry arg0, WinnerCountry arg1) {
							if(arg0.getMedal().equals("gold"))
								return -1;
							else if(arg1.getMedal().equals("gold"))
								return 1;
							else {
								if(arg0.getMedal().equals("silver"))
									return -1;
								else
									return 1;
							
							}
							
						}
						
					});
					
					
					String[] column_names = { "Country", "Medal"};
					ArrayList<Object[]> data = new ArrayList<>();
					for (int i = 0; i < males.size(); i++) {
						data.add(new Object[] { males.get(i).getCountry(),  new ImageIcon("icons/"+ males.get(i).getMedal() + ".png")});
					}
					
					JTable malesTable = new JTable(data.toArray(new Object[0][0]), column_names){
						public Class getColumnClass(int column) {
							return (column == 1) ? Icon.class : Object.class;
						}
					};
					malesTable.setEnabled(false);
					malesTable.setRowHeight(table.getRowHeight() + 20);
					
					ArrayList<Object[]> data2 = new ArrayList<>();
					for (int i = 0; i < female.size(); i++) {
						data2.add(new Object[] { female.get(i).getCountry(), new ImageIcon("icons/"+ female.get(i).getMedal() + ".png")});
					}
					
					JTable femaleTable = new JTable(data2.toArray(new Object[0][0]), column_names) {
						public Class getColumnClass(int column) {
							return (column == 1) ? Icon.class : Object.class;
						}
					};
					femaleTable.setEnabled(false);
					femaleTable.setRowHeight(table.getRowHeight() + 20);
					
					// divide male and female
					JPanel malePanel = new JPanel();
					malePanel.setLayout(new BoxLayout(malePanel, BoxLayout.Y_AXIS));
					JPanel s1 = new JPanel();
					s1.add( new JLabel("Male"));
					JScrollPane malePane = new JScrollPane(malesTable);
					malePane.setPreferredSize(new Dimension(300, 130));
					malePanel.add(s1);
					malePanel.add(malePane);
					
					JPanel femalePanel = new JPanel();
					femalePanel.setLayout(new BoxLayout(femalePanel, BoxLayout.Y_AXIS));
					JPanel s2 = new JPanel();
					s2.add(new JLabel("Female"));
					JScrollPane femalePane = new JScrollPane(femaleTable);
					femalePane.setPreferredSize(new Dimension(300, 130));
					femalePanel.add(s2);
					femalePanel.add(femalePane);
					
					boolean male_present = false;
					if(males.size() != 0) {
						male_present = true;
						winnerPanel.add(malePanel);
					}
					if(female.size() != 0) {
						if (male_present)
							winnerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
						winnerPanel.add(femalePanel);
					}

					
				}
				
				if (team_event)
					JOptionPane.showMessageDialog(oneOlympicPanel, winnerPanel, selected_event + " (Team) - Winners", JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(oneOlympicPanel, winnerPanel, selected_event + " - Winners", JOptionPane.PLAIN_MESSAGE);
				
								
			}
			
		});
		search.setEnabled(false);
		eventCB.setEnabled(false);
		eventCB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					if (((String)eventCB.getSelectedItem()).equals("-"))
						search.setEnabled(false);
					else
						search.setEnabled(true);
				}
			}
			
		});
		JLabel eventL = new JLabel ("Event");
		comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
		JPanel s1 = new JPanel();
		JPanel s2 = new JPanel();
		JComboBox disciplineCB = new JComboBox <>(list_disc);
		disciplineCB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					String eventQuery = "SELECT distinct ?event_name\n" + 
							"{\n" + 
							"  ?on_podium a :OnPodium; :onPodiumOlympicGame [:olympicType ?type; :olympicYear ?year]; :onPodiumEvent [ :ofDiscipline [ :disciplineName ?discipline]; :eventName ?event_name].\n" + 
							"  FILTER(?year = "+year+" && ?type = '"+type+"' && ?discipline = '"+ (String)arg0.getItem()+"').\n" + 
							"}";
					JSONArray eventR = QueryMaker.query(eventQuery);	
					String [] list_event = new String[eventR.length()+1];
					list_event[0] = "-";
					for (int i = 0; i < eventR.length(); i++) {
						list_event[i+1] = eventR.getJSONObject(i).getJSONObject("event_name").getString("value");
					}
					DefaultComboBoxModel model = new DefaultComboBoxModel(list_event);
					eventCB.setModel(model);
					if (((String)disciplineCB.getSelectedItem()).equals("-")) {
						eventCB.setEnabled(false);
						search.setEnabled(false);
					}
					else
						eventCB.setEnabled(true);
				}
				
				
			}
			
		});
		JLabel disciplineL = new JLabel ("Discipline");
		s1.add(disciplineL);
		s1.add(disciplineCB);
		comboPanel.add(s1);
		comboPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		s2.add(eventL);
		s2.add(eventCB);
		comboPanel.add(s2);
		comboPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		
		comboPanel.add(search);
		

		oneOlympicPanel.add(comboPanel);
		oneOlympicPanel.add(Box.createRigidArea(new Dimension(0, 8)));
				

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

	private class ShowTorchRoute implements ActionListener {

		String type;
		int year;

		public ShowTorchRoute(String type, int year) {
			this.type = type;
			this.year = year;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String torchQuery = "SELECT ?city ?noc ?countryName ?order \r\n" + "{\r\n"
					+ "  ?pass a :PassTrough; :passthroughTorch [:hasTorch [ :olympicYear ?year; :olympicType ?type]]; :passthroughCity [ :cityName ?city; :cityIn [ :NOC ?noc; :countryName ?countryName] ]; :passOrder ?order.\r\n"
					+ "  FILTER(?year = " + year + " && ?type = '" + type + "').\r\n" + "}";

			JSONArray torchResults = QueryMaker.query(torchQuery);

			ArrayList<TorchVisit> torchRoute = new ArrayList<>();
			HashSet<String> countries = new HashSet<>();
			
			
			if (!torchResults.isEmpty()) {

				for (int i = 0; i < torchResults.length(); i++) {

					String noc = torchResults.getJSONObject(i).getJSONObject("noc").getString("value");
					int order = torchResults.getJSONObject(i).getJSONObject("order").getInt("value");
					String city = torchResults.getJSONObject(i).getJSONObject("city").getString("value");
					String countryName = torchResults.getJSONObject(i).getJSONObject("countryName").getString("value");
					countries.add(countryName);

					torchRoute.add(new TorchVisit(noc, city, order));
				}

				Collections.sort(torchRoute, new Comparator<TorchVisit>() {

					@Override
					public int compare(TorchVisit arg0, TorchVisit arg1) {
						if (arg0.getOrder() > arg1.getOrder())
							return 1;
						else if (arg0.getOrder() < arg1.getOrder())
							return -1;
						else
							return 0;
					}

				});

				JPanel torchPanel = new JPanel();
				torchPanel.setLayout(new BoxLayout(torchPanel, BoxLayout.Y_AXIS));

				JPanel torchTitlePanel = new JPanel();
				torchTitlePanel.setLayout(new BoxLayout(torchTitlePanel, BoxLayout.X_AXIS));

				JLabel imageTorch1 = new JLabel(new ImageIcon("icons/torch-2.png"));
				JLabel imageTorch2 = new JLabel(new ImageIcon("icons/torch-2.png"));
				JLabel torchTitle = new JLabel("Torch Route in short");
				torchTitle.setFont(new Font("Helvetica", Font.BOLD, 14));
				torchTitlePanel.add(imageTorch1);
				torchTitlePanel.add(Box.createRigidArea(new Dimension(5, 0)));
				torchTitlePanel.add(torchTitle);
				torchTitlePanel.add(Box.createRigidArea(new Dimension(5, 0)));
				torchTitlePanel.add(imageTorch2);


				torchPanel.add(torchTitlePanel);
				torchPanel.add(Box.createRigidArea(new Dimension(0, 15)));
			
				
				JPanel row = new JPanel();
				row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
				
				JPanel column1 = new JPanel();
				column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
				
				// add
				JPanel sub0 = new JPanel();
				sub0.setLayout(new BoxLayout(sub0, BoxLayout.X_AXIS));
				sub0.setAlignmentX(LEFT_ALIGNMENT);
				JLabel visitedC = new JLabel("Visited countries: " + countries.size());
				sub0.add(visitedC);
				
				
				
				column1.add(sub0);
				
				
				JPanel column2 = new JPanel();
				column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));
				
				
				// add
				JPanel sub3 = new JPanel();
				sub3.setLayout(new BoxLayout(sub3, BoxLayout.X_AXIS));
				sub3.setAlignmentX(LEFT_ALIGNMENT);
				JLabel numBearers = new JLabel("Visited cities: "+ torchRoute.size());
				sub3.add(numBearers);
				
				column2.add(sub3);
				
				row.add(column1);
				row.add(Box.createHorizontalStrut(20));
				row.add(column2);
				
				torchPanel.add(row);
				
				String[] column_names = { "", "City"};
				ArrayList<Object[]> data = new ArrayList<>();
				for (int i = 0; i < torchRoute.size(); i++) {
					String image = "icons/country/" + torchRoute.get(i).getNoc() + ".png";
					data.add(new Object[] { new ImageIcon(image), torchRoute.get(i).getCityName()});
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
				table.setEnabled(false);

				
				JPanel scrollTorchPanel = new JPanel();
				JScrollPane scrollTorchPane = new JScrollPane(table);
				scrollTorchPane.setPreferredSize(new Dimension(285, 346));
				scrollTorchPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
				scrollTorchPane.setAlignmentX(Component.CENTER_ALIGNMENT);
				scrollTorchPanel.add(scrollTorchPane);

				torchPanel.add(scrollTorchPanel);
				JOptionPane.showMessageDialog(oneOlympicPanel, torchPanel, "Torch Route", JOptionPane.PLAIN_MESSAGE);

			} else {
				JLabel message = new JLabel("Torch route information not available");
				JOptionPane.showMessageDialog(oneOlympicPanel, message, "Torch Route", JOptionPane.PLAIN_MESSAGE);
			}

		}

	}

}
