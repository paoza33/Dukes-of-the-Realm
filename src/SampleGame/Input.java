package SampleGame;

import java.util.BitSet;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Input {
	
	private Scene scene = null;
	
	private BitSet keyboardBitSet = new BitSet();

	public Input(Scene scene) {
		this.scene = scene;
	}
	
	private boolean is(KeyCode key) {	//retourne un booleen vérifiant si la touche préssée est le paramètre
		return keyboardBitSet.get(key.ordinal());
	}
	
	public boolean isExit() {	//ESCAPE quittera le jeu
		return is(ESCAPE);
	}
}
