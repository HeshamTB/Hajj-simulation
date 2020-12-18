import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GUI_Report {
	JFrame frame;
	private static ArrayList<Campaign> listOfCampaigns;
	private static Route[] stdRoutes;
	private JTable streetTable;
	private JTable districtTable;
	private Street[] stdStreet;
	private Date currenttimeManager;
	private static ArrayList<Campaign>[] campPerDistrict;
	private  String[] streetColNames = {"Street Name", "Street Load %", "Total","Buses","Local Vehicles","Avg. Time"};
	private  String[] districtColNames = {"District", "Campaigns", "Busses", "Arrival %","Avg. Time", "Best time to Arafat", "Best time to District"};
	private  Object[][] streetData;
	private  Object[][] districtData;
	private JLabel lblDate;
	
	public GUI_Report(ArrayList<Campaign> listOfCampaigns, Route[] stdRoutes, Street[] stdStreet, ArrayList<Campaign>[] campPerDistrict, Date currenttimeManager) {
		this.listOfCampaigns = listOfCampaigns;
		this.stdRoutes = stdRoutes;
		this.stdStreet = stdStreet;
		this.campPerDistrict = campPerDistrict;
		this.currenttimeManager = currenttimeManager;
		makeFrame();
	}
	

	private void makeFrame() {
		frame = new JFrame("Report");
		
		//Street data
		streetData = new Object[stdStreet.length][6];
		
		for (int i = 0; i < stdStreet.length; i++) {
			streetData[i][0] = stdStreet[i].getName().name();
			streetData[i][1] = stdStreet[i].getPercentRemainingCapacity();
			streetData[i][2] = stdStreet[i].getVehicles().size();
			streetData[i][3] = stdStreet[i].getNumberOfBuses();
			streetData[i][4] = stdStreet[i].getNumberOfLocalCars();
			streetData[i][5] = avgTimeOnStreet(stdStreet[i]);
		}
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(streetColNames);
		//district
		districtData = new Object[campPerDistrict.length][7];
		for (int i = 0; i < campPerDistrict.length; i++) {
			districtData[i][0] = campPerDistrict[i].get(0).getHotelDistrict().name();
			districtData[i][1] = campPerDistrict[i].size();
			districtData[i][2] = busesInDistrict(District.values()[i]);
			districtData[i][3] = getPercentArrival(District.values()[i]);
			districtData[i][4] = getAvgTimeOfTrip(District.values()[i]);
			districtData[i][5] = getShortestRoute(campPerDistrict[i].get(0), Mashier.ARAFAT).getFastestTimeOfTravel(new Bus());
			districtData[i][6] = getShortestRoute(campPerDistrict[i].get(0), Mashier.MINA).getFastestTimeOfTravel(new Bus());
		}
			//tables
			
        	//Street table
			streetTable = new JTable(streetData,streetColNames);
			streetTable.setEnabled(false);
			streetTable.getTableHeader().setBackground(new Color(17,17,17));
			streetTable.getTableHeader().setForeground(Color.WHITE);
			streetTable.getTableHeader().setFont(new Font("Rockwell", Font.PLAIN, 18));
			streetTable.setBackground(new Color(17,17,17));
			streetTable.setForeground(Color.white);
			streetTable.setGridColor(new Color(102, 102, 102));
			streetTable.setSelectionForeground(Color.white);
			streetTable.setFont(new Font("Rockwell", Font.PLAIN, 18));
			streetTable.setRowHeight(20);
			streetTable.setAutoCreateRowSorter(true);
			JScrollPane streetScroll = new JScrollPane(streetTable);
			streetTable.getColumnModel().getColumn(0).setPreferredWidth(111);
			streetTable.getColumnModel().getColumn(1).setPreferredWidth(46);
			streetTable.getColumnModel().getColumn(2).setPreferredWidth(1);
			streetTable.getColumnModel().getColumn(3).setPreferredWidth(0);
			streetTable.getColumnModel().getColumn(4).setPreferredWidth(60);
			streetTable.getColumnModel().getColumn(5).setPreferredWidth(0);
			streetScroll.setBounds(10,75,809,269);
			
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
			districtTable.getColumnModel().getColumn(0).setPreferredWidth(43);
			districtTable.getColumnModel().getColumn(1).setPreferredWidth(30);
			districtTable.getColumnModel().getColumn(2).setPreferredWidth(0);
			districtTable.getColumnModel().getColumn(3).setPreferredWidth(0);
			districtTable.getColumnModel().getColumn(4).setPreferredWidth(0);
			districtTable.getColumnModel().getColumn(5).setPreferredWidth(85);
			districtTable.getColumnModel().getColumn(6).setPreferredWidth(93);
			districtTable.setRowHeight(20);
			districtTable.revalidate();
			districtScroll.setBounds(10,395,809,89);
			
			//lbl
			
			JLabel lblTime = new JLabel("Time:");
			lblTime.setFont(new Font("Rockwell", Font.PLAIN, 16));
			lblTime.setForeground(new Color(255, 255, 255));
			lblTime.setBounds(50, 11, 72, 14);
			
			//Add Elements
			frame.getContentPane().add(streetScroll);
			frame.getContentPane().add(districtScroll);
			frame.getContentPane().setBackground(new Color(50,50,50));
			frame.getContentPane().setForeground(new Color(0, 0, 0));
			frame.setBounds(100,100,856,540);
			frame.getContentPane().setLayout(null);
			
			JLabel lblStreets = new JLabel("Streets");
			lblStreets.setForeground(Color.WHITE);
			lblStreets.setFont(new Font("Rockwell", Font.PLAIN, 24));
			lblStreets.setBounds(10, 34, 208, 30);
			frame.getContentPane().add(lblStreets);
			
			JLabel lblDistrict = new JLabel("District");
			lblDistrict.setForeground(Color.WHITE);
			lblDistrict.setFont(new Font("Rockwell", Font.PLAIN, 24));
			lblDistrict.setBounds(10, 355, 166, 29);
			frame.getContentPane().add(lblDistrict);
			
			JLabel lblTime_1 = new JLabel("Time:");
			lblTime_1.setForeground(Color.WHITE);
			lblTime_1.setFont(new Font("Rockwell", Font.PLAIN, 16));
			lblTime_1.setBounds(190, 21, 72, 14);
			frame.getContentPane().add(lblTime_1);
			
			lblDate = new JLabel(currenttimeManager.toString());
			lblDate.setForeground(Color.WHITE);
			lblDate.setFont(new Font("Rockwell", Font.PLAIN, 16));
			lblDate.setBounds(238, 18, 326, 21);
			frame.getContentPane().add(lblDate);
			frame.setLocationRelativeTo(null);
			frame.revalidate();
			frame.setLocation(100,150);
			frame.setAutoRequestFocus(false);
			frame.setVisible(true);
		
	}
	
	//Methods
	public static String avgTimeOnStreet(Street street) {
		int sum = 0;
		int counter = 1;
		for (Campaign campaign : listOfCampaigns)
			for (Vehicle vehicle : campaign.getArrivedVehicles())
				if (vehicle.hasCrossedStreet(street)){
					sum += vehicle.getTimeOnStreet(street);
					counter++;
				}
		sum /= counter;
		int hours = sum / 60;
		int minutes = sum % 60;
		if (hours == 0 && minutes == 0) return street.getFastestTimeOfTravel(new Bus());
		return String.format("%02d:%02d", hours, minutes);
	}
	private static int busesInDistrict(District district){
		int buses = 0;
		for (Campaign campaign : campPerDistrict[district.ordinal()]){
			buses += campaign.getNumberOfBusses();
		}
		return buses;
	}
	
	private static int getPercentArrival(District district) {
		int sum = 0;
		for (Campaign campaign : campPerDistrict[district.ordinal()]) {
			sum += campaign.getPercentArrived();
		}
		return sum/campPerDistrict[district.ordinal()].size();
	}
	
	private static String getAvgTimeOfTrip(District district){
		int sum = 0;
		int counter = 1;
		for (Campaign campaign : campPerDistrict[district.ordinal()]) {
			for (Vehicle vehicle : campaign.getVehicles()) {
				if (vehicle.isArrivedToDest()) {
					long minutes = (vehicle.getTimeOfArrival().getTime() - vehicle.getTimeStartedMoving().getTime())/60000;
					sum+= minutes;
					counter++;
				}
			}
		}
		sum = sum /counter;
		int hours = sum / 60;
		int minutes = sum % 60;
		if (hours == 0 && minutes == 0) return " n/a";
		return String.format("%2d:%02d", hours,minutes);
	}
	
	private static Route getShortestRoute(Campaign campaign, Mashier mashier) {
		Route[] routes = getRoutesToDistrict(campaign.getHotelDistrict());
		Route route = null;
		double min = Double.MAX_VALUE;
		for (Route r : routes) {
			if (r.getMashier() == mashier){
				if (r.getTotalLength() < min) {
					min = r.getTotalLength();
					route = r;
				}
			}
		}
		return route;
	}
	
	private static Route[] getRoutesToDistrict(District district) {
		ArrayList<Route> routes = new ArrayList<>();
		for (Route route : stdRoutes) {
			if (route.getHotelArea() == district) {
				routes.add(route);
			}
		}
		Route[] routesArray = new Route[routes.size()];
		return routes.toArray(routesArray);
	}
}