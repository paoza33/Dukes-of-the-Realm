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
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

public class Main_Dukes extends Application{

	private Image lancerImage;
	private Image castleImage;
	private Image castleEImage;
	private Image castleNImage;
	
	private Text messageData = new Text();
	private Input input;
	private Pane playfieldLayer;
	private AnimationTimer gameLoop;
	
	double mouse_x, mouse_y; //coordonnees (x,y) de la souris
	
	private Scene scene;
	private Group root;
	
	private int[] troopsReserveOnager = new int[2];
	private int[] troopsReserveKnight = new int[2];
	private int[] troopsReserveLancer = new int[2];
	private int[] troopsProduction = {0,0,0};	//contient le nombre de troupes a produire
	private ArrayList<Castle> castlesEnnemies = new ArrayList<>();
	private ArrayList<Castle> castlesAllies = new ArrayList<>();
	private ArrayList<Castle> castlesNeutral = new ArrayList<>();

	private ArrayList<Troop> osts = new ArrayList<>(); // contient les objets osts
	private ArrayList<List<Troop>> ost = new ArrayList<>();	//contiendra l'ost par tranche de 3
	private ArrayList<Castle> choiceCastle = new ArrayList<>();	//contiendra le chateau que l'on aura choisi
	
	int turn; //tour de la partie
	boolean choice = false; //verifie si on a deja� clique sur le chateau
	private ArrayList<Troop> attacksTroop = new ArrayList<Troop>(); //liste de troop en cours d'attack 
	private ArrayList<Castle> attacksCastle = new ArrayList<Castle>();
	private ArrayList<String> dukes = new ArrayList<String>();
	
	private double time = 0;
	private double timeOst = 0;
	private boolean beginProduction = false;
	private boolean timeout = false;
	private double speed;
	int out = 30;
	 
	private Button button;
	private Button buttonLancer = new Button();
	private Button buttonKnight = new Button();
	private Button buttonOnager = new Button();
	private Button buttonAttack = new Button();
	int ostReady =0;
	boolean loading =false;
	boolean collisionWithEnnemy;
	boolean collisionWithNeutral;
	
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
							choiceCastle.remove(castle);
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
								public void handle(ActionEvent event) {

								HBox box = new HBox();
								messageData.setText("\n\n                           " + troopsProduction[0] + "             " + troopsProduction[1] + "             " + troopsProduction[2] + "\n\n"
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
				
				buttonLancer.setOnMouseClicked(new EventHandler<MouseEvent>() {

		            @Override
		            public void handle(MouseEvent event) {
						MouseButton button = event.getButton();
		                if(button==MouseButton.PRIMARY){
							if(troopsReserveLancer[0] >0 ) {
								troopsProduction[0]++;
								troopsReserveLancer[0]--;
							}
		                }
		                else if(button==MouseButton.SECONDARY) {
		                	if(troopsReserveLancer[0] <10) {
		                		troopsProduction[0]--;
								troopsReserveLancer[0]++;
		                	}
		                }
								messageData.setText("\n\n                           " + troopsProduction[0] + "             " + troopsProduction[1] + "             " + troopsProduction[2] + "\n\n"
										+ "RESERVE ->   Lancer : "  + troopsReserveLancer[0] + "  Knight : " + troopsReserveKnight[0] + "  Onager : " + troopsReserveOnager[0]);
								buttonAttack = new Button();
								buttonAttack.setText("Attack");
								buttonAttack.setLayoutX(Settings.SCENE_WIDTH /8);
								buttonAttack.setLayoutY(Settings.SCENE_HEIGHT + 10);
								root.getChildren().add(buttonAttack);
							}
					});
				
				buttonKnight.setOnMouseClicked(new EventHandler<MouseEvent>() {

		            @Override
		            public void handle(MouseEvent event) {
						MouseButton button = event.getButton();
		                if(button==MouseButton.PRIMARY){
							if(troopsReserveKnight[0] >0 ) {
								troopsProduction[1]++;
								troopsReserveKnight[0]--;
							}
		                }
		                else if(button==MouseButton.SECONDARY) {
		                	if(troopsReserveKnight[0] <5) {
			                	troopsProduction[1]--;
								troopsReserveKnight[0]++;
		                	}
		                }
								messageData.setText("\n\n                           " + troopsProduction[0] + "             " + troopsProduction[1] + "             " + troopsProduction[2] + "\n\n"
										+ "RESERVE ->   Lancer : "  + troopsReserveLancer[0] + "  Knight : " + troopsReserveKnight[0] + "  Onager : " + troopsReserveOnager[0]);
								buttonAttack = new Button();
								buttonAttack.setText("Attack");
								buttonAttack.setLayoutX(Settings.SCENE_WIDTH /8);
								buttonAttack.setLayoutY(Settings.SCENE_HEIGHT + 10);
								root.getChildren().add(buttonAttack);
							}
					});
				
				buttonOnager.setOnMouseClicked(new EventHandler<MouseEvent>() {

		            @Override
		            public void handle(MouseEvent event) {
						MouseButton button = event.getButton();
		                if(button==MouseButton.PRIMARY){
							if(troopsReserveOnager[0] >0 ) {
								troopsProduction[2]++;
								troopsReserveOnager[0]--;
							}
		                }
		                else if(button==MouseButton.SECONDARY) {
		                	if(troopsReserveOnager[0] <2) {
			                	troopsProduction[2]--;
								troopsReserveOnager[0]++;
		                	}
		                }
						messageData.setText("\n\n                           " + troopsProduction[0] + "             " + troopsProduction[1] + "             " + troopsProduction[2] + "\n\n"
								+ "RESERVE ->   Lancer : "  + troopsReserveLancer[0] + "  Knight : " + troopsReserveKnight[0] + "  Onager : " + troopsReserveOnager[0]);
						buttonAttack = new Button();
						buttonAttack.setText("Attack");
						buttonAttack.setLayoutX(Settings.SCENE_WIDTH /8);
						buttonAttack.setLayoutY(Settings.SCENE_HEIGHT + 10);
						root.getChildren().add(buttonAttack);
					}
				});
				
				buttonAttack.setOnMouseClicked(new EventHandler<MouseEvent>() {

		            @Override
		            public void handle(MouseEvent event) {
		            	if(!timeout) {	//on ne peut lancer une ost quand une ost est en cours d'attaque
		            		choiceCastle.get(0).setTroopsReserveLancer(troopsReserveLancer);
		            		choiceCastle.get(0).setTroopsReserveLancer(troopsReserveKnight);
		            		choiceCastle.get(0).setTroopsReserveOnager(troopsReserveOnager);
			            	for(int i=0; i<troopsProduction[0]; i++) {	//creation de l'ost
			            		Lancer lancer = new Lancer(playfieldLayer, lancerImage, choiceCastle.get(0).getX(), choiceCastle.get(0).getY(), 
			            				Settings.LANCER_HEALTH, Settings.LANCER_COST, Settings.LANCER_TIME, Settings.LANCER_SPEED, Settings.LANCER_DAMAGE);
			            		osts.add(lancer);
			            	}
			            	for(int i=0; i<troopsProduction[1]; i++) {
			            		Knight knight = new Knight(playfieldLayer, lancerImage, choiceCastle.get(0).getX(), choiceCastle.get(0).getY(), 
			            				Settings.KNIGHT_HEALTH, Settings.KNIGHT_COST, Settings.KNIGHT_TIME, Settings.KNIGHT_SPEED, Settings.KNIGHT_DAMAGE);
			            		osts.add(knight);
			            	}
			            	for(int i=0; i<troopsProduction[2]; i++) {
			            		Onager onager = new Onager(playfieldLayer, lancerImage, choiceCastle.get(0).getX(), choiceCastle.get(0).getY(), 
			            				Settings.ONAGER_HEALTH, Settings.ONAGER_COST, Settings.ONAGER_TIME, Settings.ONAGER_SPEED, Settings.ONAGER_DAMAGE);
			            		osts.add(onager);
			            	}
			            	for(int i=0; i<osts.size(); i = i+3) {
			            		int j = osts.size()%3;	//evite de depasser l'indice du tableau
			            		if(i+3 > osts.size()) {
			            			ost.add(osts.subList(i, i+j));
			            		}
			            		else {
			            			ost.add(osts.subList(i, i+3));
			            		}
			            	}
							attacksCastle.add(castlesEnnemies.get(0));
							beginProduction = true;
							for(int i=0; i< ost.size(); i++) {
								for(Troop troop : ost.get(i)) {		//temps de productions de l'ost vaut celui de la plus longue unit�
																	// et la vitesse a l'unite la plus lente
									if(troop instanceof Onager) {
										timeOst = Settings.ONAGER_TIME;
										speed = Settings.ONAGER_SPEED;
									}
									else if(troop instanceof Knight && timeOst < Settings.KNIGHT_TIME){
										timeOst = Settings.KNIGHT_TIME;
										if(speed > Settings.KNIGHT_SPEED || speed == 0) {
											speed = Settings.KNIGHT_SPEED;
										}
									}
									else if(troop instanceof Lancer && (timeOst < Settings.LANCER_TIME || speed > Settings.LANCER_SPEED)){
										if(timeOst < Settings.LANCER_TIME) {
											timeOst = Settings.LANCER_TIME;
										}
										if(speed > Settings.LANCER_SPEED || speed == 0) {
											speed = Settings.LANCER_SPEED;
										}
									}
								}
							}
			            }
					}
				});
				
				if(beginProduction) {
					if(turn %5 == 0) {
						System.out.println(time);
						time++;
					}
					int size = ost.size();
					
					if(time >= timeOst) {
						ostReady++;
						if(ostReady == size) {
							timeout =true;
							beginProduction =false;
							ostReady =0;
						}
						else {
							loading = true;
						}
						time =0;
					}
					if(loading) {
						outTheDoor(ost.get(ostReady));
					}
				}
				
				if(timeout && !collisionWithNeutral) {
					for(Troop troop : ost.get(0)) {
						troop.attack(attacksCastle.get(0), speed);	//les troupes vont vers le chateau
					}
				}
		
				osts.forEach(sprite -> sprite.updateUI());
				checkCollisions();
				removeSprites(osts);
				
				if(dukes.size() <2) {	//si il ne reste qu'un duke alors gameOver
					gameOver();
				}
				turn++;
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
		castleEImage = new Image(getClass().getResource("/images/castleEnnemy.png").toExternalForm(), 60, 60, true, true);
		castleNImage = new Image(getClass().getResource("/images/castleNeutre.png").toExternalForm(), 60, 60, true, true);
	
		input = new Input(scene);
		input.addListeners();
	
		createStatusBar();
		createKingdom();
	}
	
	public void createKingdom() { //display chateau et troupes
		Castle castle;
		Castle castle2;
		Castle castle3;
		// les chateaux contiennent tous les memes donnees au debut
		troopsReserveOnager[0] = 2; troopsReserveOnager[1] = Settings.ONAGER_DAMAGE;
		troopsReserveKnight[0] = 5; troopsReserveKnight[1] = Settings.KNIGHT_DAMAGE;
		troopsReserveLancer[0] = 10; troopsReserveLancer[1] = Settings.LANCER_DAMAGE;
		
		double x = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 4.0; //position chateau 1 (player)
		double y = Settings.SCENE_HEIGHT / 1.5;
		castle = new Castle(playfieldLayer, castleImage, x, y, "Alfheim", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		
		x = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 1.1; //position chateau 2 (enemy)
		y = Settings.SCENE_HEIGHT / 3;		
		castle2 = new Castle(playfieldLayer, castleEImage, x, y, "Midgard", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		
		x = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 2.2; //position chateau 3 (Neutre)
		y = Settings.SCENE_HEIGHT / 2;
		castle3 = new Castle(playfieldLayer, castleNImage, x, y, "Muspellheim", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		
		castlesAllies.add(castle);
		castlesEnnemies.add(castle2);
		castlesNeutral.add(castle3);
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
	
	private void checkCollisions() {		//iterator pour eviter error modif list en parcourant
		collisionWithEnnemy = false;
		collisionWithNeutral = false;
		for (Castle castleE : castlesEnnemies) {
			Iterator<List<Troop>> it = ost.iterator();	//it = ost
			int i=0;
			while(it.hasNext()) {
			//for(int i=0; i< ost.size(); i++) {
				List<Troop> list = it.next();
				Iterator<Troop> iterator = list.iterator();		// iterator = ost.get(i)
				
				//for (Troop troop : ost.get(i)) { // attaque de chaque troupe de l'ost
				while(iterator.hasNext()) {
					Troop troop = iterator.next();
					if (troop.collidesWith(castleE)) {
						timeout = false;
						collisionWithEnnemy =true;
						castleE.damagedBy(troop);
						troop.remove();
						if(!castleE.isAlive()) {
							//join(castlesEnnemies);
							dukes.remove(castleE.getDuke());
						}
						iterator.remove();
						if(ost.get(i).size() == 0) {
							//ost.remove(ost.get(i));
							it.remove();
						}
					}
				}
				i++;
			}
		}
		for (Castle castleN : castlesNeutral) {
			for(int i=0; i< ost.size(); i++) {
				for (Troop troop : ost.get(i)) { 
					if (troop.collidesWith(castleN)) {
						collisionWithNeutral = true;
						troop.bypass(castleN, attacksCastle.get(0), troop.getSpeed());
					}
				}
			}
		}
		
	}
	
	public void outTheDoor(List<Troop> troops) {
		out--;
		if(out >0) {
			Iterator<Troop> it = troops.iterator();	//it = ost
			while(it.hasNext()) {
				Troop troop = it.next();
				troop.setY(troop.getY() - 2);
			}
		}
		else {
			out = 30;
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
				System.out.println("yo");
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
