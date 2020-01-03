package SampleGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

public class Main_Dukes extends Application{

	private Image lancerImage;
	private Image onagerImage;
	private Image knightImage;
	private Image castleImage;
	
	private Text messageData = new Text();
	private boolean collision = false;
	private Input input;
	private Pane playfieldLayer;
	private AnimationTimer gameLoop;
	
	double mouse_x, mouse_y; //coordonnees (x,y) de la souris
	
	private Scene scene;
	private Group root;
	
	private int[] troopsReserveOnager = new int[2];
	private int[] troopsReserveKnight = new int[2];
	private int[] troopsReserveLancer = new int[2];
	private int[] troopsProduction = new int[3];
	private ArrayList<Castle> castlesEnnemies = new ArrayList<>();
	private ArrayList<Castle> castlesAllies = new ArrayList<>();
	private ArrayList<Troop> osts = new ArrayList<>(); // on regroupe les 3 unites produits en un objet qu'on ajoute a ce tableau
	private int[] build = {0,0,0};
	private ArrayList<Castle> choiceCastle = new ArrayList<>();	//contiendra le chateau que l'on aura choisi
	
	int turn; //tour de la partie
	boolean choice = false; //vérifie si on a déjà cliqué sur le château
	private ArrayList<Troop> attacksTroop = new ArrayList<Troop>(); //liste de troop en cours d'attack 
	private ArrayList<Castle> attacksCastle = new ArrayList<Castle>();
	private ArrayList<String> dukes = new ArrayList<String>();
	 
	private Button button;
	private Button buttonLancer = new Button();
	private Button buttonKnight = new Button();
	private Button buttonOnager = new Button();
	private Button buttonAttack = new Button();
	
	/*Onager onager = new Onager(playfieldLayer, onagerImage, 1, 1, Settings.ONAGER_COST, Settings.ONAGER_TIME, Settings.ONAGER_SPEED, Settings.ONAGER_HEALTH, Settings.ONAGER_DAMAGE);
	Knight knight = new Knight(Settings.KNIGHT_COST, Settings.KNIGHT_TIME, Settings.KNIGHT_SPEED, Settings.KNIGHT_HEALTH, Settings.KNIGHT_DAMAGE);
	Lancer lancer = new Lancer(Settings.LANCER_COST, Settings.LANCER_TIME, Settings.LANCER_SPEED, Settings.LANCER_HEALTH, Settings.LANCER_DAMAGE);*/
	
	public void start(Stage primaryStage) { //determine le jeu
		root = new Group();
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT + Settings.STATUS_BAR_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		playfieldLayer = new Pane();
		root.getChildren().add(playfieldLayer);
		Polyline polyline = new Polyline();
		
		loadGame();
		
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
		
				processInput(input);
				
				for(Castle castle : castlesAllies) {
					castle.getView().setOnMousePressed(e -> { 
						HBox bar = new HBox();
						messageData.setStyle("-fx-font: 15 arial;");
						messageData.setText("Florins : " + castle.getTreasure() +" Lvl : " + castle.getLevel() + "\n"
								+ "Onager: " + castle.getTroopsReserveOnager()[0] + " Knight: " + castle.getTroopsReserveKnight()[0]
										+ " Lancer: " + castle.getTroopsReserveLancer()[0] + "\n" + "turn : "+turn);
						bar.getChildren().addAll(messageData);
						bar.getStyleClass().add("statusBar");
						bar.relocate(0, Settings.SCENE_HEIGHT);
						bar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
						button = new Button();
						button.setText("build ost");
						button.setLayoutX(Settings.SCENE_WIDTH - 80);
						button.setLayoutY(Settings.SCENE_HEIGHT + 10);
						root.getChildren().addAll(bar, button);
						
						if(choice) {
							choice = false;
							root.getChildren().remove(polyline);
						}
						else {
							choice = true;
							choiceCastle.add(castle);
							polyline.getPoints().addAll(new Double[] { //affiche le rectangle quand on clique sur un chateau
									(castle.getX() + castle.getW()), castle.getY(),
									(castle.getX() + castle.getW()), (castle.getY() + castle.getH()),
									castle.getX(), (castle.getY() + castle.getH()),
									castle.getX(), castle.getY()
							});
							root.getChildren().add(polyline);
						}
					});
				}
				
				for(Castle castle : castlesEnnemies) {
				   castle.getView().setOnMousePressed(e -> {
						HBox bare = new HBox();
						messageData.setStyle("-fx-font: 15 arial;");
						messageData.setText("Florins : " + castle.getTreasure() +" Lvl : " + castle.getLevel() + "\n"
								+ "Onager: " + castle.getTroopsReserveOnager()[0] + " Knight: " + castle.getTroopsReserveKnight()[0]
										+ " Lancer: " + castle.getTroopsReserveLancer()[0] + "\n" + "turn : "+turn);
						bare.getChildren().addAll(messageData);
						bare.getStyleClass().add("statusBar");
						bare.relocate(0, Settings.SCENE_HEIGHT);
						bare.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
						messageData.setText("Florins : " + castle.getTreasure() +" Lvl : " + castle.getLevel() + "\n"
								+ "Onager: " + castle.getTroopsReserveOnager()[0] + " Knight: " + castle.getTroopsReserveKnight()[0]
								+ " Lancer: " + castle.getTroopsReserveLancer()[0] + "\n" + turn);
						root.getChildren().add(bare);
					});
				}
					
				if(choice) {
					button.setOnAction(
							new EventHandler<ActionEvent>(){
								@Override
								public void handle(ActionEvent event) {	//on creer que des lancier pour l'instant

								HBox box = new HBox();
								messageData.setText("\n\n                           " + build[0] + "             " + build[1] + "             " + build[2] + "\n\n"
										+ "RESERVE ->   Lancer : "  + troopsReserveLancer[0] + "  Knight : " + troopsReserveKnight[0] + "  Onager : " + troopsReserveOnager[0]);
								messageData.setStyle("-fx-font: 15 arial;");
								box.getChildren().addAll(messageData);
								box.getStyleClass().add("statusBar");
								box.relocate(0, Settings.SCENE_HEIGHT);
								box.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
								
								buttonLancer = new Button();
								buttonLancer.setText("Lancer");
								buttonLancer.setLayoutX(Settings.SCENE_WIDTH /3);
								buttonLancer.setLayoutY(Settings.SCENE_HEIGHT + 10);
								
								buttonKnight = new Button();
								buttonKnight.setText("Knight");
								buttonKnight.setLayoutX(Settings.SCENE_WIDTH /2);
								buttonKnight.setLayoutY(Settings.SCENE_HEIGHT + 10);
								
								buttonOnager = new Button();
								buttonOnager.setText("Onager");
								buttonOnager.setLayoutX(Settings.SCENE_WIDTH /1.5);
								buttonOnager.setLayoutY(Settings.SCENE_HEIGHT + 10);
								root.getChildren().addAll(box, buttonLancer, buttonKnight, buttonOnager);
								
							}
					});
				}
				
				buttonLancer.setOnAction(
						new EventHandler<ActionEvent>(){
							@Override
							public void handle(ActionEvent event) {	//on creer que des lancier pour l'instant
								if(troopsReserveLancer[0] > 0) {
									build[0] = build[0] + 1;
									troopsReserveLancer[0] = troopsReserveLancer[0] -1;
									choiceCastle.get(0).setTroopsReserveLancer(troopsReserveLancer);
								}
								messageData.setText("\n\n                           " + build[0] + "             " + build[1] + "             " + build[2] + "\n\n"
										+ "RESERVE ->   Lancer : "  + troopsReserveLancer[0] + "  Knight : " + troopsReserveKnight[0] + "  Onager : " + troopsReserveOnager[0]);
								buttonAttack = new Button();
								buttonAttack.setText("Attack");
								buttonAttack.setLayoutX(Settings.SCENE_WIDTH /8);
								buttonAttack.setLayoutY(Settings.SCENE_HEIGHT + 10);
								root.getChildren().add(buttonAttack);
							}
					});
				
				buttonKnight.setOnAction(
						new EventHandler<ActionEvent>(){
							@Override
							public void handle(ActionEvent event) {	//on creer que des lancier pour l'instant
								if(troopsReserveKnight[0] >0) {
									build[1]++;
									troopsReserveKnight[0] = troopsReserveKnight[0] -1;
									choiceCastle.get(0).setTroopsReserveLancer(troopsReserveKnight);
								}
								messageData.setText("\n\n                           " + build[0] + "             " + build[1] + "             " + build[2] + "\n\n"
										+ "RESERVE ->   Lancer : "  + troopsReserveLancer[0] + "  Knight : " + troopsReserveKnight[0] + "  Onager : " + troopsReserveOnager[0]);
								buttonAttack = new Button();
								buttonAttack.setText("Attack");
								buttonAttack.setLayoutX(Settings.SCENE_WIDTH /8);
								buttonAttack.setLayoutY(Settings.SCENE_HEIGHT + 10);
								root.getChildren().add(buttonAttack);
							}
					});
				
				buttonOnager.setOnAction(
						new EventHandler<ActionEvent>(){
							@Override
							public void handle(ActionEvent event) {	//on creer que des lancier pour l'instant
								if(troopsReserveOnager[0] >0 ) {
									build[2]++;
									troopsReserveOnager[0] = troopsReserveOnager[0] -1;
									choiceCastle.get(0).setTroopsReserveLancer(troopsReserveOnager);
								}
								messageData.setText("\n\n                           " + build[0] + "             " + build[1] + "             " + build[2] + "\n\n"
										+ "RESERVE ->   Lancer : "  + troopsReserveLancer[0] + "  Knight : " + troopsReserveKnight[0] + "  Onager : " + troopsReserveOnager[0]);
								buttonAttack = new Button();
								buttonAttack.setText("Attack");
								buttonAttack.setLayoutX(Settings.SCENE_WIDTH /8);
								buttonAttack.setLayoutY(Settings.SCENE_HEIGHT + 10);
								root.getChildren().add(buttonAttack);
							}
					});
				
				buttonAttack.setOnAction(
						new EventHandler<ActionEvent>(){
							@Override
							public void handle(ActionEvent event) {
								Lancer lancer = new Lancer(playfieldLayer, lancerImage, castlesAllies.get(0).getX(), castlesAllies.get(0).getY(), 10,2,1,4,1);
								osts.add(lancer);
								//castlesAllies.get(0).buildOst(osts);
								attacksTroop.add(lancer);
								attacksCastle.add(castlesEnnemies.get(0));
							}
				});
				
				for(int i=0; i< attacksTroop.size(); i++) {
					attacksTroop.get(i).attack(attacksCastle.get(i));	//les troupes vont vers le chateau
				}
		
				attacksTroop.forEach(sprite -> sprite.updateUI());
				checkCollisions();
				removeSprites(osts);
				if(dukes.size() <2) {
					gameOver();
				}
				
				//update();
				
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
		lancerImage = new Image(getClass().getResource("/images/Lancier.png").toExternalForm(), 20, 20, true, true);
		castleImage = new Image(getClass().getResource("/images/castle.png").toExternalForm(), 60, 60, true, true);
		onagerImage = new Image(getClass().getResource("/images/onager.png").toExternalForm(), 60, 60, true, true);
	
		input = new Input(scene);
		input.addListeners();
	
		createStatusBar();
		createKingdom();
		scene.setOnMousePressed(e -> { //recupere les coordonnees de la souris
			mouse_x = e.getX();
			mouse_y = e.getY();
		});
	
	}
	
	public void createKingdom() { //display chateau et troupes
		double x1 = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 4.0; //position chateau 1 (player)
		double y1 = Settings.SCENE_HEIGHT / 2.0;
		
		double x2 = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 1.5; //position chateau 2 (enemy)
		double y2 = Settings.SCENE_HEIGHT / 3.0;
		
		troopsReserveOnager[0] = 2; troopsReserveOnager[1] = Settings.ONAGER_DAMAGE;
		troopsReserveKnight[0] = 5; troopsReserveKnight[1] = Settings.KNIGHT_DAMAGE;
		troopsReserveLancer[0] = 10; troopsReserveLancer[1] = Settings.LANCER_DAMAGE;
		
		// on construit 2 chateaux par defaut pour l'instant
		Castle castle;
		Castle castle2;
		castle = new Castle(playfieldLayer, castleImage, x1, y1, "Alfheim", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		castle2 = new Castle(playfieldLayer, onagerImage, x2, y2, "Midgard", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		castlesAllies.add(castle);
		castlesEnnemies.add(castle2);
		dukes.add(castle.getDuke());
		dukes.add(castle2.getDuke());
	
	}
	
	public void createStatusBar() { //représente l'entête en bas de l'écran qui affichera les données du chateau et des boutons pour selectionner les troops
		HBox statusBar = new HBox();
		messageData.setText("Select castle");
		messageData.setStyle("-fx-font: 15 arial;");
		statusBar.getChildren().addAll(messageData);
		statusBar.getStyleClass().add("statusBar");
		statusBar.relocate(0, Settings.SCENE_HEIGHT);
		statusBar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
		button = new Button();
		button.setText("build ost");
		button.setLayoutX(Settings.SCENE_WIDTH - 80);
		button.setLayoutY(Settings.SCENE_HEIGHT + 10);
		root.getChildren().addAll(statusBar, button);
	}
	
	private void checkCollisions() {
		collision = false;
		for (Castle castleE : castlesEnnemies) {
			for (Troop troop : osts) { // attaque de chaque troupe de l'ost
				if (troop.collidesWith(castleE)) {
					castleE.damagedBy(troop);
					if(!castleE.isAlive()) {
						//join(castlesEnnemies);
						dukes.remove(castleE.getDuke());
					}
					attacksTroop.remove(troop);
					collision = true;
				}
			}
		}
		
	}
	
	private void update() {
		if(turn%20 ==0) {
			castlesAllies.get(0).setTreasure(castlesAllies.get(0).getTreasure() + 50);
		}
		messageData.setText("florins: " + castlesAllies.get(0).getTreasure()+" Life : ");
		turn++;
	}
	
	private void removeSprites(List<? extends Sprite> spriteList) { //supprime ceux qui doit l'etre
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
	
	private void join(List<? extends SpriteCastle> spriteList) {	//le chateau est en notre possession
		Iterator<? extends SpriteCastle> iter = spriteList.iterator();
		while (iter.hasNext()) {
			SpriteCastle sprite = iter.next();
			if (!sprite.isAlive()) {
				sprite.becomeAllies(castleImage);
				castlesEnnemies.remove((Castle) sprite);		//provoque une erreur
				castlesAllies.add((Castle) sprite);
			}
		}
	}
	
	private void gameOver() {
		HBox hbox = new HBox();
		hbox.setPrefSize(Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
		hbox.getStyleClass().add("message");
		Text message = new Text();
		message.getStyleClass().add("message");
		message.setText("Game over");
		hbox.getChildren().add(message);
		root.getChildren().add(hbox);
		gameLoop.stop();
	}
	
	public static void main(String[] args) { //doit seulement contenir launch
		launch(args);
	}
}
