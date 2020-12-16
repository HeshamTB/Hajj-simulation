import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class GUI_ViewStreet {
	private JFrame frame;
	private Street street;
	private ArrayList<Vehicle> vehicles = new ArrayList<>();
	private Street[] stdStreet = new Street[StreetNames.values().length];
	private PDate currenttimeManager;
	private Object[][] vehicleData;
	private String[] vehicleColNames = {"ID", "District", "location","Progress", "trip time"};
	private JTable table;
	private JLabel lblCapcityValue; 
	private JLabel lblDate;
	private JLabel lblLanesValue;
	private JLabel lblNumVehicles;
	private JLabel lblLengthValue;
	private JTextField txtLow;
	private JTextField[] textFieldArray = new JTextField[20];
	private JTextField txtHigh;
	
	public GUI_ViewStreet(Street[] streets, PDate currenttimeManager) {
		stdStreet = streets.clone();
		this.currenttimeManager = (PDate) currenttimeManager.clone();
		makeFrame();
	}
	
	private void makeFrame() {
		//street = stdStreet[0];
		frame = new JFrame("Streets");
		frame.getContentPane().setBackground(new Color(70, 70, 70));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100,100,815,480);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
		
		
		vehicleData = new Object[vehicles.size()][6];
		for (int i = 0; i < vehicles.size(); i++) {
			vehicleData[i][0] = vehicles.get(i).getUID();
			if (vehicles.get(i) instanceof Bus)
				vehicleData[i][1] = ((Bus)vehicles.get(i)).getCampaign().getHotelDistrict().name();
			else vehicleData[i][1] = "Local Vehicle";
			vehicleData[i][2] = vehicles.get(i).getCurrentLocation();
			vehicleData[i][3] = vehicles.get(i).getProgress();
			vehicleData[i][4] = vehicles.get(i).getTripTime();
			
		}
		
		table = new JTable(vehicleData,vehicleColNames);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(vehicleColNames);
		table.getTableHeader().setBackground(new Color(17,17,17));
		table.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setModel(new DefaultTableModel(vehicleData ,vehicleColNames));
		table.setBackground(new Color(17,17,17));
		table.setForeground(Color.WHITE);
		table.setGridColor(new Color(102, 102, 102));
		table.setFont(new Font("Rockwell", Font.PLAIN, 18));
		table.setRowHeight(25);
		table.setAutoCreateRowSorter(true);
		table.revalidate();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 194, 755, 236);
		frame.getContentPane().add(scrollPane);
		
		JList list = new JList();
		list.setFont(new Font("Tahoma", Font.PLAIN, 14));
		list.setBackground(Color.GRAY);
		list.setModel(new AbstractListModel() {
			Street[] streets = stdStreet;
			public int getSize() {
				return stdStreet.length;
			}
			public Object getElementAt(int index) {
				return streets[index].getName().name();
			}
		});
		list.addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						street = stdStreet[list.getSelectedIndex()];
						vehicles = street.getVehicles();
						updateTable();
						for (int i = 0; i < textFieldArray.length; i++) {
							int colorFactor = (int)((1-capcityPoint(i))*255); 
							textFieldArray[i].setBackground(new Color(255,colorFactor,0));
						}
						lblCapcityValue.setText(String.format("%%%d", street.getPercentRemainingCapacity()));
						lblDate.setText(currenttimeManager.getCurrentTime().toString());
						lblNumVehicles.setText(String.format("%d", street.getVehicles().size()));
						lblLengthValue.setText(String.format("%.2f", street.getLength()));
						lblLanesValue.setText(String.format("%d", street.getNumberOfLanes()));
					}
				});
		list.setBounds(10, 11, 118, 146);
		JScrollPane scrollList = new JScrollPane(list);
		scrollList.setBounds(10, 11, 173, 172);
		frame.getContentPane().add(scrollList);
		
		txtLow = new JTextField("Low");
		txtLow.setFont(new Font("Rockwell", Font.BOLD, 13));
		txtLow.setDisabledTextColor(Color.BLACK);
		txtLow.setEnabled(false);
		txtLow.setEditable(false);
		txtLow.setBackground(Color.YELLOW);
		txtLow.setBounds(710, 105, 40, 20);
		frame.getContentPane().add(txtLow);
		
		txtHigh = new JTextField("High");
		txtHigh.setFont(new Font("Rockwell", Font.BOLD, 13));
		txtHigh.setDisabledTextColor(Color.BLACK);
		txtHigh.setEnabled(false);
		txtHigh.setEditable(false);
		txtHigh.setColumns(0);
		txtHigh.setBackground(Color.RED);
		txtHigh.setBounds(710, 80, 40, 20);
		frame.getContentPane().add(txtHigh);
		
		JLabel lblTime = new JLabel("Time:");
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblTime.setBounds(193, 11, 72, 20);
		frame.getContentPane().add(lblTime);
		
		lblDate = new JLabel(currenttimeManager.getCurrentTime().toString());
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDate.setBounds(243, 11, 326, 20);
		frame.getContentPane().add(lblDate);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setForeground(Color.WHITE);
		lblStatus.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblStatus.setBounds(511, 11, 72, 20);
		frame.getContentPane().add(lblStatus);
		
		JLabel lblDestination = new JLabel();
		lblDestination.setForeground(Color.WHITE);
		lblDestination.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDestination.setBounds(568, 11, 184, 20);
		if (currenttimeManager.getCurrentCalendar().get(Calendar.DAY_OF_MONTH) == 9) 
			 lblDestination.setText("Heading to Arafat");
		else lblDestination.setText("Heading to Hotels");
		frame.getContentPane().add(lblDestination);
		
		JLabel lblCapcity = new JLabel("Capcity:");
		lblCapcity.setForeground(Color.WHITE);
		lblCapcity.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblCapcity.setBounds(193, 45, 72, 20);
		frame.getContentPane().add(lblCapcity);
		
		lblCapcityValue = new JLabel();
		lblCapcityValue.setForeground(Color.WHITE);
		lblCapcityValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblCapcityValue.setBounds(258, 45, 48, 20);
		frame.getContentPane().add(lblCapcityValue);
		
		JLabel lblVehicles = new JLabel("Vehicles: ");
		lblVehicles.setForeground(Color.WHITE);
		lblVehicles.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblVehicles.setBounds(370, 45, 72, 20);
		frame.getContentPane().add(lblVehicles);
		
		lblNumVehicles = new JLabel();
		lblNumVehicles.setForeground(Color.WHITE);
		lblNumVehicles.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblNumVehicles.setBounds(441, 45, 61, 20);
		frame.getContentPane().add(lblNumVehicles);
		
		JLabel lblLength = new JLabel("Length:");
		lblLength.setForeground(Color.WHITE);
		lblLength.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblLength.setBounds(193, 85, 61, 20);
		frame.getContentPane().add(lblLength);
		
		lblLengthValue = new JLabel();
		lblLengthValue.setForeground(Color.WHITE);
		lblLengthValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblLengthValue.setBounds(253, 85, 93, 20);
		frame.getContentPane().add(lblLengthValue);
		
		JLabel lblLanes = new JLabel("Lanes");
		lblLanes.setForeground(Color.WHITE);
		lblLanes.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblLanes.setBounds(370, 85, 61, 20);
		frame.getContentPane().add(lblLanes);
		
		lblLanesValue = new JLabel();
		lblLanesValue.setForeground(Color.WHITE);
		lblLanesValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblLanesValue.setBounds(431, 85, 26, 20);
		frame.getContentPane().add(lblLanesValue);
		
		JLabel lbl100 = new JLabel();
		lbl100.setText("%100");
		lbl100.setForeground(Color.WHITE);
		lbl100.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lbl100.setBounds(715, 170, 45, 15);
		frame.getContentPane().add(lbl100);
		
		JLabel lbl0 = new JLabel();
		lbl0.setText("%0");
		lbl0.setForeground(Color.WHITE);
		lbl0.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lbl0.setBounds(193, 170, 25, 15);
		frame.getContentPane().add(lbl0);
		
		JLabel lblCapcityPoint = new JLabel("CapcityPoint:");
		lblCapcityPoint.setForeground(Color.WHITE);
		lblCapcityPoint.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblCapcityPoint.setBounds(193, 120, 101, 20);
		frame.getContentPane().add(lblCapcityPoint);
		
		
		for (int i = 0; i < textFieldArray.length; i++) {
			textFieldArray[i] = new JTextField();
			textFieldArray[i].setEnabled(false);
			textFieldArray[i].setEditable(false);
			textFieldArray[i].setBackground(new Color(70, 70, 70));
			textFieldArray[i].setBounds(190+i*28, 142, 28, 20);
			frame.getContentPane().add(textFieldArray[i]);
			textFieldArray[i].setColumns(0);
		}
		
		frame.setVisible(true);
		
	}
	
	public void updateTable() {
		if (vehicles.isEmpty()) return;
		vehicleData = new Object[vehicles.size()][6];
		for (int i = 0; i < vehicles.size(); i++) {
			vehicleData[i][0] = vehicles.get(i).getUID();// TODO: There is an Exception error here;
			if (vehicles.get(i) instanceof Bus)
				vehicleData[i][1] = ((Bus)vehicles.get(i)).getCampaign().getHotelDistrict().name();
			else vehicleData[i][1] = "Local Vehicle";
			vehicleData[i][2] = vehicles.get(i).getCurrentLocation();
			vehicleData[i][3] = vehicles.get(i).getProgress();
			vehicleData[i][4] = vehicles.get(i).getTripTime();
			
		}
		table.setModel(new DefaultTableModel(vehicleData ,vehicleColNames));
	}
	
	public double capcityPoint(int x) {
		double part = street.getLength()/20;
		double capcity = street.capcityPoint(x*part, (x+1)*part);
		
		return capcity;
	}
}
