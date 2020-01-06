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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

public class Main_Dukes extends Application{
	
	//the image of ost
	private Image ostImage;
	
	//the image of the allied castle
	private Image castleImage;
	
	//the image of the enemy castle
	private Image castleEImage;
	
	//the image of the neutral castle
	private Image castleNImage;

	// Text of HBox
	private Text messageData = new Text();
	
	// player entry key
	private Input input;
	
	private Pane playfieldLayer;
	private AnimationTimer gameLoop;
		
	private Scene scene;
	private Group root;
	
	// castle's troops reserve
	// index 0 -> number of troops
	// index 1 -> damage matter
	private int[] troopsReserveOnager = new int[2];
	private int[] troopsReserveKnight = new int[2];
	private int[] troopsReserveLancer = new int[2];
	
	// contains the number of troops to be produced {Lancer, Knight,Onager}
	private int[] troopsProduction = {0,0,0};
	
	// enemy castle list
	private ArrayList<Castle> castlesEnnemies = new ArrayList<>();
	
	// list of allied castles
	private ArrayList<Castle> castlesAllies = new ArrayList<>();
	
	// list of neutral castles
	private ArrayList<Castle> castlesNeutral = new ArrayList<>();
	
	// ost list of troops
	private ArrayList<Troop> osts = new ArrayList<>();
	
	//list containing the list of osts by groups of 3
	private ArrayList<List<Troop>> ost = new ArrayList<>();
	
	// contains the allied castle we've chosen for the attack
	private ArrayList<Castle> choiceCastle = new ArrayList<>();
	
	// Number of turns of the AnimationTimer() loop 
	int turn;
	
	// check to see if we've chosen an allied castle.
	boolean choice = false;
	
	// the castle that will be the target of the attack
	private ArrayList<Castle> attacksCastle = new ArrayList<Castle>();
	
	// list of dukes of the realm
	private ArrayList<String> dukes = new ArrayList<String>();
	
	// a timer for the production of the ost
	private double time = 0;
	
	//the production time of the ost
	private double timeOst = 0;
	
	// verifies if the production of an asbestos is in progress.
	private boolean beginProduction = false;
	
	// verifies if the production of an ost is completed
	private boolean timeout = false;
	
	// ost speed
	private double speed;
	
	// if there are 3 troops, the first ready troops take 30 steps to the north. 
	int out = 30;
	
	// Button for build ost
	private Button button;
	
	// Button to add a lancer to the ost
	private Button buttonLancer = new Button();
	
	// Button to add a knight to the ost
	private Button buttonKnight = new Button();
	
	// Button to add an onager to the ost
	private Button buttonOnager = new Button();
	
	// Attack button
	private Button buttonAttack = new Button();
	
	// troop counter ready to go
	int ostReady =0;
	
	// to manage the wait case if the number of ost troops exceeds 3
	boolean loading =false;
	
	// true if there's a collision with a neutral castle, false if not
	boolean collisionWithEnnemy;
	
	//true if there's a collision with an enemy castle, false if not
	boolean collisionWithNeutral;
	
	public void start(Stage primaryStage) {
		
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
						
						// create button "build ost"
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
							
							// create a rectangle around the chosen castle
							polyline.getPoints().addAll(new Double[] {
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
				   //displays the data of the enemy castle
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
				
				// We can only attack if a castle is chosen
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
								
								// create the button lancer
								buttonLancer = new Button();
								buttonLancer.setText("Lancer");
								buttonLancer.setLayoutX(Settings.SCENE_WIDTH /3);
								buttonLancer.setLayoutY(Settings.SCENE_HEIGHT + 10);
								
								// create the button knight
								buttonKnight = new Button();
								buttonKnight.setText("Knight");
								buttonKnight.setLayoutX(Settings.SCENE_WIDTH /2);
								buttonKnight.setLayoutY(Settings.SCENE_HEIGHT + 10);
								
								// create the button onager
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
		            	//You can't launch an ost while an ost is under attack
		            	if(!timeout) {
		            		choiceCastle.get(0).setTroopsReserveLancer(troopsReserveLancer);
		            		choiceCastle.get(0).setTroopsReserveLancer(troopsReserveKnight);
		            		choiceCastle.get(0).setTroopsReserveOnager(troopsReserveOnager);
		            		
		            		//creation of ost
			            	for(int i=0; i<troopsProduction[0]; i++) {	
			            		Lancer lancer = new Lancer(playfieldLayer, ostImage, choiceCastle.get(0).getX(), choiceCastle.get(0).getY(), 
			            				Settings.LANCER_HEALTH, Settings.LANCER_COST, Settings.LANCER_TIME, Settings.LANCER_SPEED, Settings.LANCER_DAMAGE);
			            		osts.add(lancer);
			            	}
			            	for(int i=0; i<troopsProduction[1]; i++) {
			            		Knight knight = new Knight(playfieldLayer, ostImage, choiceCastle.get(0).getX(), choiceCastle.get(0).getY(), 
			            				Settings.KNIGHT_HEALTH, Settings.KNIGHT_COST, Settings.KNIGHT_TIME, Settings.KNIGHT_SPEED, Settings.KNIGHT_DAMAGE);
			            		osts.add(knight);
			            	}
			            	for(int i=0; i<troopsProduction[2]; i++) {
			            		Onager onager = new Onager(playfieldLayer, ostImage, choiceCastle.get(0).getX(), choiceCastle.get(0).getY(), 
			            				Settings.ONAGER_HEALTH, Settings.ONAGER_COST, Settings.ONAGER_TIME, Settings.ONAGER_SPEED, Settings.ONAGER_DAMAGE);
			            		osts.add(onager);
			            	}
			            	
			            	// groups of 3
			            	for(int i=0; i<osts.size(); i = i+3) {
			            		
			            		// avoids exceeding the index in the table
			            		int j = osts.size()%3;
			            		if(i+3 > osts.size()) {
			            			ost.add(osts.subList(i, i+j));
			            		}
			            		
			            		else {
			            			ost.add(osts.subList(i, i+3));
			            		}
			            	}
			            	
			            	//only works when the kingdom contains only one enemy castle
							attacksCastle.add(castlesEnnemies.get(0));
							
							beginProduction = true;
							
							//the production time is equal to that of the longest unit to produce ost
							//and the speed is equal to that of the slowest unit
							for(int i=0; i< ost.size(); i++) {
								for(Troop troop : ost.get(i)) {
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
						time++;
					}
					
					// see line 426
					int size = ost.size();
					
					if(time >= timeOst) {
						ostReady++;
						
						//determines when all the bones are out of the castle to attack
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
				
				// Troops are moving towards the target
				if(timeout && !collisionWithNeutral) {
					for(Troop troop : ost.get(0)) {
						troop.attack(attacksCastle.get(0), speed);
					}
				}
				
				osts.forEach(sprite -> sprite.updateUI());
				
				checkCollisions();
				
				// remove removables from list, layer, etc
				removeSprites(osts);
				
				//if there's only one duke left then gameOver
				if(dukes.size() <2) {
					gameOver();
				}
				update();
				
			}
			
			// when the player presses ESCAPE
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
		ostImage = new Image(getClass().getResource("/images/Lancier.png").toExternalForm(), 20, 20, true, true);
		castleImage = new Image(getClass().getResource("/images/castle.png").toExternalForm(), 60, 60, true, true);
		castleEImage = new Image(getClass().getResource("/images/CastleEnnemy.png").toExternalForm(), 60, 60, true, true);
		castleNImage = new Image(getClass().getResource("/images/CastleNeutre.png").toExternalForm(), 60, 60, true, true);
	
		input = new Input(scene);
		input.addListeners();
	
		createStatusBar();
		createKingdom();
	}
	
	public void createKingdom() {
		Castle castle;
		Castle castle2;
		Castle castle3;
		
		// the castles all contain the same data at the start except the coordinates
		troopsReserveOnager[0] = 2; troopsReserveOnager[1] = Settings.ONAGER_DAMAGE;
		troopsReserveKnight[0] = 5; troopsReserveKnight[1] = Settings.KNIGHT_DAMAGE;
		troopsReserveLancer[0] = 10; troopsReserveLancer[1] = Settings.LANCER_DAMAGE;
		
		//position castle 1 (player)
		double x = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 4.0; 
		double y = Settings.SCENE_HEIGHT / 1.5;
		castle = new Castle(playfieldLayer, castleImage, x, y, "Alfheim", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		
		//position castle 2 (enemy)
		x = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 1.1; 
		y = Settings.SCENE_HEIGHT / 3;		
		castle2 = new Castle(playfieldLayer, castleEImage, x, y, "Midgard", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		
		//position castle 3 (Neutral)
		x = (Settings.SCENE_WIDTH - castleImage.getWidth()) / 2.2; 
		y = Settings.SCENE_HEIGHT / 2;
		castle3 = new Castle(playfieldLayer, castleNImage, x, y, "Muspellheim", 1000, 1, 'N', troopsProduction, troopsReserveOnager, troopsReserveKnight, troopsReserveLancer);
		
		castlesAllies.add(castle);
		castlesEnnemies.add(castle2);
		castlesNeutral.add(castle3);
		
		dukes.add(castle.getDuke());
		dukes.add(castle2.getDuke());
		dukes.add(castle3.getDuke());
	}
	
	public void createStatusBar() {
		HBox statusBar = new HBox();
		messageData.setText("Select castle");
		messageData.setStyle("-fx-font: 15 arial;");
		statusBar.getChildren().addAll(messageData);
		statusBar.getStyleClass().add("statusBar");
		statusBar.relocate(0, Settings.SCENE_HEIGHT);
		statusBar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
		
		// create button for build ost
		button = new Button();
		button.setText("build ost");
		button.setLayoutX(Settings.SCENE_WIDTH - 80);
		button.setLayoutY(Settings.SCENE_HEIGHT + 10);
		root.getChildren().addAll(statusBar, button);
	}
	
	// checks if an ost reaches a castle
	private void checkCollisions() {
		collisionWithEnnemy = false;
		collisionWithNeutral = false;
		
		// verifies if an ost hits its target
		for (Castle castleE : castlesEnnemies) {
			
			// use of an iterator to be able to remove an item from the list while browsing through it
			Iterator<List<Troop>> it = ost.iterator();	//it = ost
			int i=0;
			while(it.hasNext()) {
				List<Troop> list = it.next();
				Iterator<Troop> iterator = list.iterator();
				
				// attack of each troop of the ost
				while(iterator.hasNext()) {
					Troop troop = iterator.next();
					
					if (troop.collidesWith(castleE)) {
						timeout = false;
						collisionWithEnnemy =true;
						
						castleE.damagedBy(troop);
						troop.remove();
						
						if(!castleE.isAlive()) {
							dukes.remove(castleE.getDuke());
						}
						
						iterator.remove();
						if(ost.get(i).size() == 0) {
							it.remove();
						}
					}
				}
				i++;
			}
		}
		
		// checks if an ost meets a castle that is not its target
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
	
	// brings out an ost ready
	public void outTheDoor(List<Troop> troops) {
		out--;
		if(out >0) {
			Iterator<Troop> it = troops.iterator();	//it = ost
			while(it.hasNext()) {
				Troop troop = it.next();
				troop.setY(troop.getY() - 1);
			}
		}
		else {
			out = 30;
		}
	}
	
	// increases the treasure of the castles as time goes by
	private void update() {
		if(turn%20 ==0) {
			castlesAllies.get(0).setTreasure(castlesAllies.get(0).getTreasure() + 50);
		}
		turn++;
	}
	
	private void removeSprites(List<? extends Sprite> spriteList) {
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
	
	// the comment function below causes an error
	
	// the castle is in our possession
	/*private void join(List<? extends SpriteCastle> spriteList) {
		Iterator<? extends SpriteCastle> iter = spriteList.iterator();
		while (iter.hasNext()) {
			SpriteCastle sprite = iter.next();
			if (!sprite.isAlive()) {
				sprite.becomeAllies(castleImage);
				castlesEnnemies.remove((Castle) sprite);
				castlesAllies.add((Castle) sprite);
			}
		}
	}*/
	
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
