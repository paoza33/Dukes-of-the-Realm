package SampleGame;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class Castle extends SpriteCastle{
	private String duke;
	private double x, y;
	private int treasure, level;
	private char door;
	private int[] troopsReserveOnager; //indice 0 -> nombres soldat indice 1 -> degats (vie d'une troupe)
	private int[] troopsReserveKnight;
	private int[] troopsReserveLancer;
	private int[] troopsProduction; // liste de troops en cours de productions
	private ArrayList<Troop> ost; // ost de 3 membres max
	private ArrayList<Troop> osts; // liste de tout les osts
	
	
	public Castle(Pane layer, Image image, double x, double y, String duke,int treasure, int level, char door, int[] troopsProduction, int[] troopsReserveOnager, int[] troopsReserveKnight, int[] troopsReserveLancer) {
		super(layer,image,x,y);
		this.x = x;
		this.y = y;
		this.duke = duke;
		this.treasure = treasure;
		this.level = level;
		this.door = door;
		this.troopsProduction = troopsProduction;
		this.troopsReserveOnager = troopsReserveOnager;
		this.troopsReserveKnight = troopsReserveKnight;
		this.troopsReserveLancer = troopsReserveLancer;
	}
		
	/*public void buildOst(ArrayList<Troop> troopsAttack) { // limite l'ost à 3 membres pour sortir du chateau
		buildOsts(troopsAttack);
		if(!(osts.size() < 4)) {
			ArrayList<Troop> subList = new ArrayList<>();
			for(int i = 0; i< osts.size(); i+= 3) {
				subList = (ArrayList<Troop>) osts.subList(i, i+3);
				ost.addAll(subList);
			}
		}
	}*/
	
	public void damagedBy( Troop troop) {
		ArrayList<String> string = new ArrayList<String>(3); //type de troupe de la reserve
		int count =0;
		if(troopsReserveOnager.length > 0) { //verifie si il reste ce type de troupe dans la reserve
			string.add("Onager");
			count++;
		}
		if(troopsReserveKnight.length > 0) {
			string.add("Knight");
			count++;
		}
		if(troopsReserveLancer.length > 0) {
			string.add("Lancer");
			count ++;
		}
		Random rand = new Random();
		int i = rand.nextInt(count);
		
		if(string.get(i) == "Onager") { //verification du type de troupe a cibler
			troopsReserveOnager[1] -= troop.getDamage();
			if(troopsReserveOnager[1] < 0) { // si la troupe est morte, on decroit le nombre de soldat de ce type de 1
				troopsReserveOnager[0] -= 1;
				troopsReserveOnager[1] = Settings.ONAGER_HEALTH;	// on reactualise la vie
			}
		}
		else if(string.get(i) == "Knight") {
			troopsReserveKnight[1] -= troop.getDamage();
			if(troopsReserveKnight[1] < 0) {
				troopsReserveKnight[0] -= 1;
				troopsReserveKnight[1] = Settings.KNIGHT_HEALTH;
			}
		}
		else if(string.get(i) == "Lancer") {
			troopsReserveLancer[1] -= troop.getDamage();
			if(troopsReserveLancer[1] < 0) {
				troopsReserveLancer[0] -= 1;
				troopsReserveLancer[1] = Settings.LANCER_HEALTH;
			}
		}
		troop.remove();
	}
	
	public char getDoor() {
		return door;
	}
	
	public void setDoor(char door) {
		this.door = door;
	}
	
	public int[] getTroopsReserveOnager() {
		return troopsReserveOnager;
	}
	
	public double getX() {
		return x;
	}
	
	public int[] getTroopsReserveKnight() {
		return troopsReserveKnight;
	}
	
	public void setTroopsReserveKnight(int[] troopsReserveKnight) {
		this.troopsReserveKnight = troopsReserveKnight;
	}
	
	public int[] getTroopsReserveLancer() {
		return troopsReserveLancer;
	}
	
	public void setTroopsReserveLancer(int[] troopsReserveLancer) {
		this.troopsReserveLancer = troopsReserveLancer;
	}
	
	public ArrayList<Troop> getOst() {
		return ost;
	}
	
	public void setOst(ArrayList<Troop> ost) {
		this.ost = ost;
	}
	
	public ArrayList<Troop> getOsts() {
		return osts;
	}
	
	public void setOsts(ArrayList<Troop> osts) {
		this.osts = osts;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int[] getTroopsProduction() {
		return troopsProduction;
	}
	
	public void setTroopsProduction(int[] troopsProduction) {
		this.troopsProduction = troopsProduction;
	}
	
	public void setTroopsReserveOnager(int[] troopsReserve) {
		this.troopsReserveOnager = troopsReserve;
	}
	
	public int getTreasure() {
		return treasure;
	}
	
	public void setTreasure(int treasure) {
		this.treasure = treasure;
	}
	
	public String getDuke() {
		return duke;
	}
	
	public void setDuke(String duke) {
		this.duke = duke;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void treasureByLevel() {
		this.treasure = this.treasure + this.level*10; // le trésor gagne (niveau * 10 florins) par tour
	}
	
	public boolean isAlive() {
		if(troopsReserveOnager[0] <1 && troopsReserveKnight[0] < 1 && troopsReserveLancer[0] < 1) {
			return false;
		}
			return true;
	}
	
	public void becomeAllies(Image image) {
		ImageView imageView;
		imageView = new ImageView(image);
        imageView.relocate(x, y);
		setImageView(imageView);
	}
	
	public void becomeEnnemies(Image image) {
		ImageView imageView;
		imageView = new ImageView(image);
        imageView.relocate(x, y);
		setImageView(imageView);
	}
	
	public boolean EndProd(int[] troopsProduction, int time) { //teste toutes les troupes en cours de production
		if(troopsProduction[2] > 0 && (Settings.ONAGER_TIME - time) <=0) { //les troupes les plus lentes representent le temps de depart de l'ost
			return true;
		}
		else if(troopsProduction[1] > 0 && !(troopsProduction[2] > 0) &&(Settings.KNIGHT_TIME - time) <=0) {
			return true;
		}
		else if(troopsProduction[0] > 0 && !(troopsProduction[2] > 0)&& !(troopsProduction[1] > 0) &&(Settings.LANCER_TIME - time) <=0) {
			return true;
		}
		return false;
		}

}