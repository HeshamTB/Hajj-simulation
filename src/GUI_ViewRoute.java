import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JScrollBar;

public class GUI_ViewRoute {
	JFrame frame;
	private String[] routeColNames = {"Num", "Status", "Length","Capacity", "Best Time", "Used By"};
	private Object[][] routeData;
	private JTable table;
	private Route[] stdRoute;
	private Date currenttimeManager;
	private ArrayList<Campaign> listOfCampaigns;
	private JTextField txtStreets;
	private JLabel lblDate;
	private JLabel lblTime;
	
	public GUI_ViewRoute(Route[] stdRoute, ArrayList<Campaign> listOfCampaigns, Date currenttimeManager) {
		this.stdRoute = stdRoute;
		this.listOfCampaigns = listOfCampaigns;
		this.currenttimeManager = currenttimeManager;
		makeFrame();	
		}

	private void makeFrame() {
		frame = new JFrame("Routes");
		frame.getContentPane().setBackground(new Color(70, 70, 70));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100,100,815,480);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
		routeData = new Object[stdRoute.length][6];
		for (int i = 0; i < stdRoute.length; i++) {
			routeData[i][0] = i;
			routeData[i][1] = String.format("%s : %s",stdRoute[i].getHotelArea(),stdRoute[i].getMashier());
			routeData[i][2] = stdRoute[i].getTotalLength();
			routeData[i][3] = String.format("%.2f",stdRoute[i].capcity());
			routeData[i][4] = stdRoute[i].getFastestTimeOfTravel(new Bus());
			int count = 0;
			for (Campaign campaign : listOfCampaigns)
				if (campaign.getRoute() == stdRoute[i])
					count += campaign.getVehicles().size();
			
			routeData[i][5] = String.format("%d buses", count);
		}
		
		
		table = new JTable(routeData,routeColNames);
		table.setModel(new DefaultTableModel(routeData,routeColNames));
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(190);
		table.getColumnModel().getColumn(2).setPreferredWidth(55);
		table.getColumnModel().getColumn(3).setPreferredWidth(57);
		table.getColumnModel().getColumn(5).setPreferredWidth(75);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(routeColNames);
		ListSelectionModel model2 = table.getSelectionModel();
		model2.addListSelectionListener(e -> {
			int row = model2.getMinSelectionIndex();
			String streets = "";
			for (Street street : stdRoute[row].getStreets())
				streets += street.getName().name() +" >>  ";
			if (stdRoute[row].getMashier() == Mashier.ARAFAT)
				txtStreets.setText(stdRoute[row].getHotelArea().name()+" >>  "+streets+stdRoute[row].getMashier().name());
			else txtStreets.setText(stdRoute[row].getMashier().name()+" >>  "+streets+stdRoute[row].getHotelArea().name());
		});
		table.getTableHeader().setBackground(new Color(17,17,17));
		table.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setBackground(new Color(17,17,17));
		table.setForeground(Color.WHITE);
		table.setGridColor(new Color(102, 102, 102));
		table.setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.setRowHeight(25);
		table.setAutoCreateRowSorter(true);
		table.revalidate();
		
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 44, 755, 300);
		frame.getContentPane().add(scrollPane);
		
		txtStreets = new JTextField("Select Route");
		txtStreets.setBackground(Color.BLACK);
		txtStreets.setForeground(Color.WHITE);
		txtStreets.setFont(new Font("Rockwell", Font.PLAIN, 17));
		txtStreets.setBounds(10, 382, 755, 48);
		frame.getContentPane().add(txtStreets);
		txtStreets.setColumns(10);
		
		lblTime = new JLabel("Time:");
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblTime.setBounds(10, 11, 72, 20);
		frame.getContentPane().add(lblTime);
		
		lblDate = new JLabel(currenttimeManager.toString());
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDate.setBounds(61, 11, 326, 20);
		frame.getContentPane().add(lblDate);
		
		JLabel lblStreets = new JLabel("Streets :");
		lblStreets.setForeground(Color.WHITE);
		lblStreets.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblStreets.setBounds(10, 355, 72, 20);
		frame.getContentPane().add(lblStreets);
		
		frame.setVisible(true);

	}
}
