package quiz;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ServerGUI extends Application{
	
	private Label status = new Label("Server is not working.");
	private Button startServer = new Button("Start server");
	private Button stopServer = new Button("Stop server");
	private TextField port = new TextField("");
	private CheckBox portChoose = new CheckBox("Wpisz port");
	private CheckBox portDefault = new CheckBox("Port domyœlny (3506)");
	private Label warning = new Label("");
	
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("Server");
		portDefault.setSelected(true);
		portChoose.setDisable(true);
		port.setDisable(true);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.TOP_LEFT);
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(10,10,10,10));
		
		portChoose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				if(portDefault.isDisable() == false)
				{
					portDefault.setDisable(true);
				}
				if(portChoose.isSelected())
				{
					port.setDisable(false);
				}
				if(!portChoose.isSelected())
				{
					port.setDisable(true);
					portDefault.setDisable(false);
				}
			}
		});
		
		portDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(portDefault.isSelected())
				{
					portChoose.setDisable(true);
				}
				else
				{
					portChoose.setDisable(false);
				}
			}
		});
		
		
		startServer.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(ActionEvent event) {	
				warning.setText("");
				if(portDefault.isSelected())
				{
					ServerTCP serv = new ServerTCP(3506);
					serv.start();
					try {
						serv.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(serv.getPortFlag() == 1)
					{
						warning.setText("Port w u¿yciu !");
						warning.setTextFill(Color.RED);
					}
					else if(serv.getPortFlag() == 0)
					{
						warning.setText("");
						//serv.start();
						status.setText("Server is working !");
						status.setTextFill(Color.GREEN);
					}
					
				}
				else if(portChoose.isSelected())
				{
					if(port.getText() == null || port.getText().trim().isEmpty())
					{
						warning.setText("Wybierz port !");
						warning.setTextFill(Color.RED);
					}
					else
					{
						ServerTCP serv = new ServerTCP(Integer.parseInt(port.getText()));
						if(serv.getPortFlag() == 1)
						{
							warning.setText("Port w u¿yciu !");
							warning.setTextFill(Color.RED);
						}
						else if(serv.getPortFlag() == 0)
						{
							warning.setText("");
							serv.start();
							status.setText("Server is working !");
							status.setTextFill(Color.GREEN);
						}	
					}
				}
				
				else
				{
					warning.setText("Wybierz port !");
					warning.setTextFill(Color.RED);
				}
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
		gridPane.add(portChoose, 1, 1);
		gridPane.add(port, 2, 1);
		gridPane.add(portDefault, 1, 2);
		gridPane.add(startServer, 1, 4);
		gridPane.add(stopServer, 2, 4);
		gridPane.add(status, 1, 6);
		gridPane.add(warning, 1, 5);
		Scene scene = new Scene(gridPane, 328,178);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
			launch(args);	
		}
}
