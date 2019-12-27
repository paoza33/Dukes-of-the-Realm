package SampleGame;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;


public class Main_Dukes extends Application{
	
	private Image lancerImage;
	private Image onagerImage;
	private Image knightImage;
	private Image castleImage;
	
	private Castle castle;
	private Castle castle2;
	
	private Text messageData = new Text();
	private boolean collision = false;
	private Input input;
	private Pane playfieldLayer;
	private AnimationTimer gameLoop;
	
	double mouse_x, mouse_y;	//coordonnees (x,y) de la souris
	
	private Scene scene;
	Group root;
	
	private int[] troopsReserveOnager = new int[2];
	private int[] troopsReserveKnight = new int[2];
	private int[] troopsReserveLancer = new int[2];
	private int[] troopsProduction = new int[3];
	private List<Castle> castlesEnnemies = new ArrayList<>();
	private List<Castle> castlesAllies = new ArrayList<>();
	private ArrayList<Troop> osts = new ArrayList<>();	// on regroupe les 3 unites produits en un objet qu'on ajoute a ce tableau
	
	int turn;	//tour de la partie
	boolean choice = false;	//vérifie si on a déjà cliqué sur le château 
	
	/*Onager onager = new Onager(playfieldLayer, onagerImage, 1, 1, Settings.ONAGER_COST, Settings.ONAGER_TIME, Settings.ONAGER_SPEED, Settings.ONAGER_HEALTH, Settings.ONAGER_DAMAGE);
	Knight knight = new Knight(Settings.KNIGHT_COST, Settings.KNIGHT_TIME, Settings.KNIGHT_SPEED, Settings.KNIGHT_HEALTH, Settings.KNIGHT_DAMAGE);
	Lancer lancer = new Lancer(Settings.LANCER_COST, Settings.LANCER_TIME, Settings.LANCER_SPEED, Settings.LANCER_HEALTH, Settings.LANCER_DAMAGE);*/
	
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
				
				processInput(input);
				
				if(mouse_x >= castlesAllies.get(0).getX() && mouse_x <= (castlesAllies.get(0).getX() + castlesAllies.get(0).getW())){	//clique sur un chateau allié
					if(mouse_y >= castlesAllies.get(0).getY() && mouse_y <= (castlesAllies.get(0).getY() + castlesAllies.get(0).getH())) {
						Polyline polyline = new Polyline();
						if(choice) {	// on annule notre choix (mal géré)
							choice = false;
							root.getChildren().remove(polyline);
							
						}
						else {
							choice = true;
							polyline.getPoints().addAll(new Double[] {	//affiche le rectangle quand on clique sur un chateau
									castlesAllies.get(0).getX(), castlesAllies.get(0).getY(),
									(castlesAllies.get(0).getX() + castlesAllies.get(0).getW()), castlesAllies.get(0).getY(),
									(castlesAllies.get(0).getX() + castlesAllies.get(0).getW()), (castlesAllies.get(0).getY() + castlesAllies.get(0).getH()),
									castlesAllies.get(0).getX(), (castlesAllies.get(0).getY() + castlesAllies.get(0).getH()),
									castlesAllies.get(0).getX(), castlesAllies.get(0).getY()
							});
							root.getChildren().add(polyline);
							messageData.setText("Florins : " + castlesAllies.get(0).getTreasure() +"    Lvl : " + castlesAllies.get(0).getLevel() + "\n"
									+ "Onager: " + castlesAllies.get(0).getTroopsReserveOnager()[0] + " Knight: " + castlesAllies.get(0).getTroopsReserveKnight()[0] 
											+ " Lancer: " + castlesAllies.get(0).getTroopsReserveLancer()[0]);
							
						}
						
						
					}
				}				
				
				// movement
				/*player.move();
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
				removeSprites(missiles);*/

				// update score, health, etc
				update();
			}

			private void processInput(Input input) {
				if (input.isExit()) {
					Platform.exit();
					System.exit(0);
				}
			}

		};
		gameLoop.start();
		
	}
	
	private void loadGame() {
		knightImage = new Image(getClass().getResource("/images/knight.png").toExternalForm(), 20, 20, true, true);
		castleImage = new Image(getClass().getResource("/images/castle.png").toExternalForm(), 60, 60, true, true);
		
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
		double x1 = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 4.0;	//position chateau 1 (player)
		double y1 = Settings.SCENE_HEIGHT / 2.0;
		
		double x2 = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 1.5;	//position chateau 2 (enemy)
		double y2 = Settings.SCENE_HEIGHT / 2.0;
		
		troopsReserveOnager[0] = 2; troopsReserveOnager[1] = Settings.ONAGER_DAMAGE;
		troopsReserveKnight[0] = 5; troopsReserveKnight[1] = Settings.KNIGHT_DAMAGE;
		troopsReserveLancer[0] = 10; troopsReserveLancer[1] = Settings.LANCER_DAMAGE;
		
		// on construit 2 chateaux par defaut pour l'instant
		castle = new Castle(playfieldLayer, castleImage, x1, y1, "Alfheim", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		castle2 = new Castle(playfieldLayer, castleImage, x2, y2, "Midgard", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		castlesAllies.add(castle);
		castlesEnnemies.add(castle2);
		
	}
	
	public void createStatusBar() {	//représente l'entête en bas de l'écran qui affichera les données du chateau et des boutons pour selectionner les troops
		HBox statusBar = new HBox();
		messageData.setText("Select castle");
		messageData.setStyle("-fx-font: 15 arial;");
		statusBar.getChildren().addAll(messageData);
		statusBar.getStyleClass().add("statusBar");
		statusBar.relocate(0, Settings.SCENE_HEIGHT);
		statusBar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
		Button button = new Button();
		button.setText("build ost");
		button.setLayoutX(Settings.SCENE_WIDTH - 80);
		button.setLayoutY(Settings.SCENE_HEIGHT + 10);
		root.getChildren().addAll(statusBar, button);
	}
	
	private void checkCollisions() {
		collision = false;
		for (Castle castle : castlesEnnemies) {	//faire deux listes de chateau (ceux possédés et ceux adverses) et ici parcourir chateau adverse
			for (Troop troop : osts) {	// attaque de chaque troupe de l'ost
				if (troop.collidesWith(castle)) {
					castle.damagedBy(troop);
					if(castle.getHealth() < 1) {
						castle.remove();	//chateau ennemi detruit
						castlesAllies.add(castle);	//le chateau devient le notre
					}
					troop.remove();	//la troupe meurt apres son attaque
					collision = true;			
				}
			}
		}
	}
	
	private void update() {
		if (collision) {
			messageData.setText("florins:           Life : " + castle.getHealth());
		}
		//gerer l'affichage des mouvements des ost
		turn++;
		
	}
	
	private void removeSprites(List<? extends Sprite> spriteList) {	//supprime ceux qui doit l'etre
		Iterator<? extends Sprite> iter = spriteList.iterator();
		while (iter.hasNext()) {
			Sprite sprite = iter.next();

			if (sprite.isRemovable()) {
				// remove from layer
				sprite.removeFromLayer();
				// remove from list
				iter.remove();
			}
		}
	}
	
	private void gameOver() {
		gameLoop.stop();
	}

	public static void main(String[] args) { //doit seulement contenir launch
		launch(args);
	}

}
