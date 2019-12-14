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
	
	private Castle castle;
	private Castle castle2;
	
	private Text florinsMessage = new Text();
	private boolean collision = false;
	private Input input;
	private Pane playfieldLayer;
	private AnimationTimer gameLoop;
	
	double mouse_x, mouse_y;	//coordonnées (x,y) de la souris
	
	private Scene scene;
	Group root;
	
	private int[] troopsReserve = new int[3];
	private Troop[] troopsProduction = new Troop[3];
	private List<Castle> castles = new ArrayList<>();
	
	public void start(Stage primaryStage) {	//determine le jeu
		root = new Group();
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT + Settings.STATUS_BAR_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		playfieldLayer = new Pane();
		root.getChildren().add(playfieldLayer);
		
		loadGame();
		
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				processInput(input, now);


				// movement
				player.move();
				enemies.forEach(sprite -> sprite.move());
				missiles.forEach(sprite -> sprite.move());

				// check collisions
				checkCollisions();

				// update sprites in scene
				player.updateUI();
				enemies.forEach(sprite -> sprite.updateUI());
				missiles.forEach(sprite -> sprite.updateUI());

				// check if sprite can be removed
				enemies.forEach(sprite -> sprite.checkRemovability());
				missiles.forEach(sprite -> sprite.checkRemovability());

				// remove removables from list, layer, etc
				removeSprites(enemies);
				removeSprites(missiles);

				// update score, health, etc
				update();
			}

			private void processInput(Input input, long now) {
				if (input.isExit()) {
					Platform.exit();
					System.exit(0);
				}
			}

		};
		gameLoop.start();
		
	}
	
	private void loadGame() {
		lancerImage = new Image(getClass().getResource("/images/lancer.png").toExternalForm(), 20, 20, true, true);
		knightImage = new Image(getClass().getResource("/images/knight.png").toExternalForm(), 20, 20, true, true);
		onagerImage = new Image(getClass().getResource("/images/onager.png").toExternalForm(), 20, 20, true, true);
		castleImage = new Image(getClass().getResource("/images/castle.png").toExternalForm(), 20, 20, true, true);
		
		input = new Input(scene);
		input.addListeners();
		
		createStatusBar();
		createKingdom();
		
		scene.setOnMousePressed(e -> {	//recupere les coordonnees de la souris
			mouse_x = e.getX();
			mouse_y = e.getY();
		});
		
	}
	
	public void createKingdom() {	//display chateau et troupes
		double x1 = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 4.0;	//position chateau 1
		double y1 = Settings.SCENE_HEIGHT / 2.0;
		
		double x2 = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 1.5;	//position chateau 2
		double y2 = Settings.SCENE_HEIGHT / 2.0;
		Onager onager = new Onager(Settings.ONAGER_COST, Settings.ONAGER_TIME, Settings.ONAGER_SPEED, Settings.ONAGER_HEALTH, Settings.ONAGER_DAMAGE);
		Knight knight = new Knight(Settings.KNIGHT_COST, Settings.KNIGHT_TIME, Settings.KNIGHT_SPEED, Settings.KNIGHT_HEALTH, Settings.KNIGHT_DAMAGE);
		Lancer lancer = new Lancer(Settings.LANCER_COST, Settings.LANCER_TIME, Settings.LANCER_SPEED, Settings.LANCER_HEALTH, Settings.LANCER_DAMAGE);
		
		troopsProduction[0] = lancer;
		troopsProduction[1] = knight;
		troopsProduction[2]= onager;
		
		troopsReserve[0] = 10;
		troopsReserve[1] = 5;
		troopsReserve[2] = 2;
		
		castle = new Castle(playfieldLayer, castleImage, x1, y1, Settings.CASTLE_HEALTH, "Alfheim", 1000, 1, 'N', troopsProduction, troopsReserve);
		castle2 = new Castle(playfieldLayer, castleImage, x2, y2, Settings.CASTLE_HEALTH, "Midgard", 1000, 1, 'N', troopsProduction, troopsReserve);
		
	}
	
	public void createStatusBar() {	//représente l'entête en bas de l'écran qui affichera les données du chateau et des boutons pour selectionner les troops
		HBox statusBar = new HBox();
		florinsMessage.setText("Florins : " + Settings.CASTLE_FLORINS +"           Lvl : " + castle.getLevel());
		statusBar.getChildren().addAll(florinsMessage);
		statusBar.getStyleClass().add("statusBar");
		statusBar.relocate(0, Settings.SCENE_HEIGHT);
		statusBar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
		root.getChildren().add(statusBar);
	}
	
	private void checkCollisions() {
		collision = false;
		for(Castle castle : castles) {
			
		}
	}
	
	private void update() {
		if (collision) {
			florinsMessage.setText("florins: : " + castle.getTreasure() + "          Life : " + castle.getHealth());
		}
		//gerer l'affichage des mouvements des ost
		
	}
	
	private void gameOver() {
		
	}

	public static void main(String[] args) { //doit seulement contenir launch
		launch(args);
	}

}
