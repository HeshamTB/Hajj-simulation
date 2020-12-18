import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class GUI_History {
	
	private List<State> states;
	private ArrayList<Campaign> listOfCampaigns;
	private ArrayList<Vehicle> listOfVehicles;
	private ArrayList<Campaign>[] campPerDistrict;
	private Route[] stdRoutes;
	private Street[] stdStreet;
	private Date currenttimeManager;
	private JFrame frame;
	private JTable streetTable;
	private JTable districtTable;
	private JLabel lblDate;
	private JLabel lblDestination;
	private JLabel lblNumOfBuses;
	private JLabel lblNumOfDonebuses;
	private JLabel lblMaximumTripValue;
	private JLabel lblMinimumTripValue;
    private JLabel lblBusesArrivedInTheLastHourValue;
    private JLabel lblAverageTripForLastHourValue;
	private JLabel lblAverageTimeForTheTrip;
	private Object[][] streetData;
	private String[] streetColNames = {"Street Name", "Street Load %", "Total", "Buses",
										"Local Vehicles","Avg. Time"};
	
	public GUI_History(List<State> states) {
		this.states = states;
		makeFrame();
	}

	private void makeFrame() {
		setData(states.get(0));
		frame = new JFrame("History");
		frame.setBounds(200, 150, 1386, 731);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(50,50,50));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.getContentPane().setLayout(null);
		frame.revalidate();
		frame.setAutoRequestFocus(false);
		frame.setVisible(true);
		
		
		streetData = new Object[stdStreet.length][6];
		for (int i = 0; i < stdStreet.length; i++) {
			streetData[i][0] = stdStreet[i].getName().name();
			streetData[i][1] = stdStreet[i].getPercentRemainingCapacity();
			streetData[i][2] = stdStreet[i].getVehicles().size();
			streetData[i][3] = stdStreet[i].getNumberOfBuses();
			streetData[i][4] = stdStreet[i].getNumberOfLocalCars();
			streetData[i][5] = MakkahCity.avgTimeOnStreet(stdStreet[i]);
		}
		//District data
		Object[][] districtData = new Object[campPerDistrict.length][7];
		String[] districtColNames = {"District", "Campaigns", "Busses", "Arrival %",
				"Avg. Time", "Best time to Arafat", "Best time to District"};
		
		for (int i = 0; i < campPerDistrict.length; i++) {
			districtData[i][0] = campPerDistrict[i].get(0).getHotelDistrict().name();
			districtData[i][1] = campPerDistrict[i].size();
			districtData[i][2] = MakkahCity.busesInDistrict(District.values()[i]);
			districtData[i][3] = MakkahCity.getPercentArrival(District.values()[i]);
			districtData[i][4] = MakkahCity.getAvgTimeOfTrip(District.values()[i]);
			districtData[i][5] = MakkahCity.getShortestRoute(campPerDistrict[i].get(0), Mashier.ARAFAT).getFastestTimeOfTravel(new Bus());
			districtData[i][6] = MakkahCity.getShortestRoute(campPerDistrict[i].get(0), Mashier.MINA).getFastestTimeOfTravel(new Bus());
		}
		
		//tables
		
		//Street table
		streetTable = new JTable(streetData,streetColNames);
		streetTable.setEnabled(false);
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(streetColNames);
		streetTable.getTableHeader().setBackground(new Color(17,17,17));
		streetTable.getTableHeader().setForeground(Color.WHITE);
		streetTable.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		streetTable.setBackground(new Color(17,17,17));
		streetTable.setForeground(Color.white);
		streetTable.setSelectionBackground(Color.RED);
		streetTable.setGridColor(new Color(102, 102, 102));
		streetTable.setSelectionForeground(Color.white);
		streetTable.setFont(new Font("Rockwell", Font.PLAIN, 18));
		streetTable.setRowHeight(25);
		streetTable.setAutoCreateRowSorter(true);
		JScrollPane streetScroll = new JScrollPane(streetTable);
		streetScroll.setBounds(30,75,1140,329);
		frame.getContentPane().add(streetScroll);
		
		//District table
		districtTable = new JTable(districtData,districtColNames);
		districtTable.setEnabled(false);
		districtTable.setForeground(new Color(255, 255, 255));
		districtTable.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
		districtTable.getTableHeader().setBackground(new Color(17,17,17));
		districtTable.getTableHeader().setForeground(Color.WHITE);
		districtTable.setBackground(new Color(17,17,17));
		districtTable.setSelectionBackground(Color.RED);
		model.setColumnIdentifiers(districtColNames);
		districtTable.setSelectionForeground(Color.white);
		districtTable.setFont(new Font("Rockwell", Font.PLAIN, 18));
		districtTable.setGridColor(new Color(102, 102, 102));
		JScrollPane districtScroll = new JScrollPane(districtTable);
		districtScroll.setEnabled(false);
		districtTable.setAutoCreateRowSorter(true);
		districtTable.setRowHeight(25);
		districtTable.revalidate();
		districtScroll.setBounds(30,452,1140,105);
		frame.getContentPane().add(districtScroll);
		
		JLabel lblStreets = new JLabel("Streets");
		lblStreets.setFont(new Font("Rockwell", Font.PLAIN, 24));
		lblStreets.setForeground(new Color(255, 255, 255));
		lblStreets.setBounds(30, 44, 208, 30);
		frame.getContentPane().add(lblStreets);
		
		JLabel lblDistrict = new JLabel("District");
		lblDistrict.setFont(new Font("Rockwell", Font.PLAIN, 24));
		lblDistrict.setForeground(new Color(255, 255, 255));
		lblDistrict.setBounds(30, 425, 166, 29);
		frame.getContentPane().add(lblDistrict);
		
		JLabel lblTime = new JLabel("Time:");
		lblTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblTime.setForeground(new Color(255, 255, 255));
		lblTime.setBounds(50, 11, 72, 14);
		frame.getContentPane().add(lblTime);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setForeground(new Color(255, 255, 255));
		lblStatus.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblStatus.setBounds(423, 9, 72, 18);
		frame.getContentPane().add(lblStatus);
		
		lblDestination = new JLabel();
		lblDestination.setForeground(new Color(255, 255, 255));
		lblDestination.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDestination.setBounds(477, 9, 184, 18);
		frame.getContentPane().add(lblDestination);
		
		lblDate = new JLabel(currenttimeManager.toString());
		lblDate.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblDate.setForeground(Color.WHITE);
		lblDate.setBounds(100, 8, 326, 21);
		frame.getContentPane().add(lblDate);
		
		JLabel lblBuses = new JLabel("Buses: ");
		lblBuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBuses.setForeground(new Color(255, 255, 255));
		lblBuses.setBackground(new Color(192, 192, 192));
		lblBuses.setBounds(30, 578, 56, 14);
		frame.getContentPane().add(lblBuses);
		
		lblNumOfBuses = new JLabel();
		lblNumOfBuses.setText("0");
		lblNumOfBuses.setBackground(new Color(0, 0, 0));
		lblNumOfBuses.setForeground(new Color(255, 255, 255));
		lblNumOfBuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblNumOfBuses.setBounds(79, 579, 90, 12);
		frame.getContentPane().add(lblNumOfBuses);
		
		JLabel lblBusesDone = new JLabel("Buses Done:");
		lblBusesDone.setForeground(new Color(255, 255, 255));
		lblBusesDone.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBusesDone.setBounds(169, 579, 101, 12);
		frame.getContentPane().add(lblBusesDone);
		
		lblNumOfDonebuses = new JLabel();
		lblNumOfDonebuses.setText("0");
		lblNumOfDonebuses.setForeground(new Color(255, 255, 255));
		lblNumOfDonebuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblNumOfDonebuses.setBounds(259, 577, 80, 16);
		frame.getContentPane().add(lblNumOfDonebuses);
		
		JLabel lblMaximumTrip = new JLabel("Maximum Trip:");
		lblMaximumTrip.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMaximumTrip.setForeground(new Color(255, 255, 255));
		lblMaximumTrip.setBounds(30, 619, 112, 22);
		frame.getContentPane().add(lblMaximumTrip);
		
		lblMaximumTripValue = new JLabel();
		lblMaximumTripValue.setText("-:--");
		lblMaximumTripValue.setForeground(new Color(255, 255, 255));
		lblMaximumTripValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMaximumTripValue.setBounds(150, 621, 46, 18);
		frame.getContentPane().add(lblMaximumTripValue);
		
		JLabel lblMinimumTrip = new JLabel("MinimumTrip:");
		lblMinimumTrip.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMinimumTrip.setForeground(Color.WHITE);
		lblMinimumTrip.setBounds(30, 655, 112, 18);
		frame.getContentPane().add(lblMinimumTrip);
		
		lblMinimumTripValue = new JLabel("-:--");
		lblMinimumTripValue.setForeground(Color.WHITE);
		lblMinimumTripValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblMinimumTripValue.setBounds(139, 657, 46, 14);
		frame.getContentPane().add(lblMinimumTripValue);
		
		JLabel lblBusesArrivedInTheLastHour = new JLabel("Buses Arrived In The Last Hour:");
		lblBusesArrivedInTheLastHour.setForeground(Color.WHITE);
		lblBusesArrivedInTheLastHour.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBusesArrivedInTheLastHour.setBounds(360, 578, 237, 14);
		frame.getContentPane().add(lblBusesArrivedInTheLastHour);
		
		lblBusesArrivedInTheLastHourValue = new JLabel();
		lblBusesArrivedInTheLastHourValue.setText("0");
		lblBusesArrivedInTheLastHourValue.setForeground(Color.WHITE);
		lblBusesArrivedInTheLastHourValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblBusesArrivedInTheLastHourValue.setBounds(594, 578, 90, 14);
		frame.getContentPane().add(lblBusesArrivedInTheLastHourValue);
		
		JLabel lblAverageTripForLastHour = new JLabel("Average Trip For Last Hour:");
		lblAverageTripForLastHour.setForeground(Color.WHITE);
		lblAverageTripForLastHour.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAverageTripForLastHour.setBackground(Color.BLACK);
		lblAverageTripForLastHour.setBounds(206, 621, 208, 18);
		frame.getContentPane().add(lblAverageTripForLastHour);
		
		lblAverageTripForLastHourValue = new JLabel("(No Arrivals) In Last Hour");
		lblAverageTripForLastHourValue.setForeground(Color.WHITE);
		lblAverageTripForLastHourValue.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAverageTripForLastHourValue.setBounds(423, 621, 216, 18);
		frame.getContentPane().add(lblAverageTripForLastHourValue);
		
		JLabel lblAvgTime = new JLabel("Average Time For The Trip:");
		lblAvgTime.setForeground(Color.WHITE);
		lblAvgTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAvgTime.setBounds(206, 650, 208, 29);
		frame.getContentPane().add(lblAvgTime);
		
		lblAverageTimeForTheTrip = new JLabel("-:--");
		lblAverageTimeForTheTrip.setForeground(Color.WHITE);
		lblAverageTimeForTheTrip.setFont(new Font("Rockwell", Font.PLAIN, 16));
		lblAverageTimeForTheTrip.setBounds(423, 657, 101, 14);
		frame.getContentPane().add(lblAverageTimeForTheTrip);
		
		JButton btnViewRoutes = new JButton("View Routes");
		btnViewRoutes.setBounds(1195, 75, 166, 30);
		btnViewRoutes.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnViewRoutes.setBackground(new Color(9,9,9));
		btnViewRoutes.setForeground(Color.white);
		btnViewRoutes.addActionListener(e -> {
			GUI_ViewRoute r = new GUI_ViewRoute(stdRoutes ,listOfCampaigns, currenttimeManager);
		});
		frame.getContentPane().add(btnViewRoutes);
		
		JButton btnViewBuses = new JButton("View Buses");
		btnViewBuses.setBounds(1195, 130, 166, 30);
		btnViewBuses.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnViewBuses.setBackground(new Color(9,9,9));
		btnViewBuses.setForeground(Color.white);
		btnViewBuses.addActionListener(e -> {
			GUI_ViewBuses t =  new GUI_ViewBuses(listOfCampaigns , currenttimeManager);
		});
		frame.getContentPane().add(btnViewBuses);
		
		JButton btnViewStreet = new JButton("View Street");
		btnViewStreet.setBounds(1195, 185, 166, 29);
		btnViewStreet.setFont(new Font("Rockwell", Font.PLAIN, 16));
		btnViewStreet.setBackground(new Color(9,9,9));
		btnViewStreet.setForeground(Color.white);
		btnViewStreet.addActionListener(e -> {
			GUI_ViewStreet t =  new GUI_ViewStreet(stdStreet,currenttimeManager);
		});
		frame.getContentPane().add(btnViewStreet);
		
		JComboBox comboBox = new JComboBox<Date>();
		comboBox.setBackground(new Color(150, 150 , 150));
		comboBox.setBounds(1143, 11, 217, 33);
		for (State state : states) {
			comboBox.addItem(state.getStateTime());
		}
		comboBox.addActionListener(e ->{
			setData(states.get(comboBox.getSelectedIndex()));
			updateFrame();
		});
		frame.getContentPane().add(comboBox);
		updateFrame();
	}
	
	public void setData(State state) {
		listOfCampaigns = state.getListOfCampaigns();
		listOfVehicles = state.getListOfVehicles();
		campPerDistrict = state.getCampPerDistrict();
		stdRoutes = state.getStdRoutes();
		stdStreet = state.getStdStreet();
		currenttimeManager = state.getStateTime();
	}
	
	 public void updateFrame() {
			streetData = new Object[stdStreet.length][6];
			for (int i = 0; i < stdStreet.length; i++) {
				streetData[i][0] = stdStreet[i].getName().name();
				streetData[i][1] = stdStreet[i].getPercentRemainingCapacity();
				streetData[i][2] = stdStreet[i].getVehicles().size();
				streetData[i][3] = stdStreet[i].getNumberOfBuses();
				streetData[i][4] = stdStreet[i].getNumberOfLocalCars();
				streetData[i][5] = MakkahCity.avgTimeOnStreet(stdStreet[i]);
			}
			streetTable.setModel(new DefaultTableModel(streetData ,streetColNames));
			
			Object[][] districtData = new Object[campPerDistrict.length][7];
			for (int i = 0; i < campPerDistrict.length; i++) {
				districtData[i][0] = campPerDistrict[i].get(0).getHotelDistrict().name();
				districtData[i][1] = campPerDistrict[i].size();
				districtData[i][2] = MakkahCity.busesInDistrict(District.values()[i]);
				districtData[i][3] = MakkahCity.getPercentArrival(District.values()[i]);
				districtData[i][4] = MakkahCity.getAvgTimeOfTrip(District.values()[i]);
				districtData[i][5] = MakkahCity.getShortestRoute(campPerDistrict[i].get(0), Mashier.ARAFAT).getFastestTimeOfTravel(new Bus());
				districtData[i][6] = MakkahCity.getShortestRoute(campPerDistrict[i].get(0), Mashier.MINA).getFastestTimeOfTravel(new Bus());
			}
			for (int i = 0; i < districtData.length; i++) {
				for (int j = 0; j < districtData[i].length; j++) {
					districtTable.setValueAt(districtData[i][j], i, j);
				}
			}
			lblDate.setText(currenttimeManager.toString());
			
			 String status = "";
			 if (currenttimeManager.getDay() == 9) {
				 status = "Heading to Arafat";
			 }
				else{
					status = "Heading to Hotels";
				}
			 	lblDestination.setText(status);
			 	
			 int numberOfBusses = 0;
			 for (Campaign campaign : listOfCampaigns) {
					numberOfBusses += campaign.getNumberOfBusses();
				} 
			 String bus = String.format("%d", numberOfBusses);
			 lblNumOfBuses.setText(bus);
			
			String numOfdoneBuses = String.format("%d",MakkahCity.getNumberOfArrivedBusses());
			lblNumOfDonebuses.setText(numOfdoneBuses);
			
			if (Vehicle.getMaxArrived() != null && Vehicle.getMinArrived() != null) {
				lblMaximumTripValue.setText(Vehicle.getMaxArrived().getTripTime().toString());
				lblMinimumTripValue.setText(Vehicle.getMinArrived().getTripTime().toString());
			}
			
			String NumberOfBussPerHour = String.format("%d", MakkahCity.getNumberOfArrivedBussesPerHour());
			lblBusesArrivedInTheLastHourValue.setText(NumberOfBussPerHour);
			
			lblAverageTripForLastHourValue.setText(MakkahCity.avgTimeOfTrip());
			lblAverageTimeForTheTrip.setText(MakkahCity.getAvgTripForAllDis());
		 }
	
}
