package quiz;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ServerGUI extends Application{
	
	private Label status = new Label("Server is not working.");
	private Button startServer = new Button("Start server");
	private Button stopServer = new Button("Stop server");
	
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("Server");
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.TOP_LEFT);
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(10,10,10,10));
		
		ServerTCP serv = new ServerTCP();
		startServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				serv.start();
				status.setText("Server is working !");
				status.setTextFill(Color.GREEN);
			}
		});
		
		stopServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
				status.setText("Server is not working !");
				status.setTextFill(Color.RED);
			}
		});
		
		status.setTextFill(Color.RED);
		gridPane.add(startServer, 1, 1);
		gridPane.add(stopServer, 2, 1);
		gridPane.add(status, 1, 4);
		
		Scene scene = new Scene(gridPane, 256,128);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
			launch(args);	
		}
}
