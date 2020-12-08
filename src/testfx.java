import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class testfx extends Application {
 
			private TableView table = new TableView();
    		Stage window;
    		private Button btVeiwBuses = new Button("View Buses");
    		private Button btViewStreets = new Button("View Streets");
    		private Button btViewCampaigns = new Button("View Campaigns");
    		private Button btViewRoutes= new Button("View Routes");
			private Button btPrintReport = new Button("Print Report");
			private Button btExit = new Button("Exit");
		
    @Override
    public void start(Stage primarystage) {
    	window = primarystage;
        
        //table title
        Label Text = new Label("Streets Report");
        Text.setFont(new Font("Arial", 16));
        Text.setTextFill(Color.WHITE);
       
        //table
        table.setEditable(true);
        TableColumn streetName = new TableColumn("Street Name");
        streetName.setPrefWidth(150);
        streetName.setStyle("-fx-base: SILVER");
        TableColumn StreetLoad = new TableColumn("Street Load");
        StreetLoad.setPrefWidth(150);
        StreetLoad.setStyle("-fx-base: SILVER");
        TableColumn Total = new TableColumn("Total");
        Total.setPrefWidth(100);
        Total.setStyle("-fx-base: SILVER");
        TableColumn Buses = new TableColumn("Buses");
        Buses.setStyle("-fx-base: SILVER");
        TableColumn localVehicles = new TableColumn("Local Vehicles");
        localVehicles.setStyle("-fx-base: SILVER");
        localVehicles.setPrefWidth(150);
        TableColumn avgTime = new TableColumn("Avg. Time");
        avgTime.setStyle("-fx-base: SILVER");
        avgTime.setPrefWidth(150);
        table.getColumns().addAll(streetName, StreetLoad, Total,Buses,localVehicles,avgTime);
        
        //table root
        VBox root1 = new VBox();
        root1.setSpacing(5);
        root1.setPadding(new Insets(13, 14, 15, 17));
        root1.getChildren().addAll(Text, table);
        root1.setAlignment(Pos.CENTER);
        root1.setStyle("-fx-base: SILVER");
        
        //buttons
        HBox root2 = new HBox();
        root2.getChildren().addAll(btVeiwBuses, btViewStreets, btViewCampaigns,
        		btViewRoutes, btPrintReport, btExit);
        root2.setSpacing(5);
        root2.setPadding(new Insets(13, 14, 15, 17));
        root2.setAlignment(Pos.CENTER);
        root2.setStyle("-fx-base: SILVER");
 
        //All Elements
        VBox root3 = new VBox();
        root3.setSpacing(5);
        root3.setPadding(new Insets(13, 14, 15, 17));
        root3.getChildren().addAll(root1, root2);
        root3.setAlignment(Pos.CENTER);
        //widnow
        StackPane pane = new StackPane(root3);
        
        pane.setStyle("-fx-background-color: BLACK; "
    			+ "-fx-padding: 5; -fx-font-size: 14;");
        
        Scene scene = new Scene(pane,850,550);
        window.setTitle("Hajj Simulation");
        window.setScene(scene);
        window.show();
        
    }
 
    public static void main(String[] args) {
        launch(args);
    }
} 