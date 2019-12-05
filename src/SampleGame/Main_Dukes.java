package SampleGame;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class Main_Dukes extends Application{
	
	private Image lancerImage;
	private Image onagerImage;
	private Image knightImage;
	private Image castleImage;
	
	private Scene scene;
	Group root;
	
	List<Troop> troop = new ArrayList<>();
	
	public void start(Stage primaryStage) {	//determine le jeu
		root = new Group();
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT + Settings.STATUS_BAR_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}
	
	private void loadGame() {
		lancerImage = new Image(getClass().getResource("/images/lancer.png").toExternalForm(), 100, 100, true, true);
		knightImage = new Image(getClass().getResource("/images/knight.png").toExternalForm(), 100, 100, true, true);
		onagerImage = new Image(getClass().getResource("/images/onager.png").toExternalForm(), 100, 100, true, true);
		
	}
	
	public void createStatusBar() {	//représente l'entête en bas de l'écran qui affichera les données du chateau et des boutons pour selectionner les troops
		HBox statusBar = new HBox();
	}
	
	private void gameOver() {
		
	}

	public static void main(String[] args) { //doit seulement contenir launch
		launch(args);
	}

}
