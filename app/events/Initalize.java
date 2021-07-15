package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;
import structures.AIPlayer;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.InitialThread;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
	
		//CommandDemo.executeDemo(out); 
		
		Board board= new Board(out);
		gameState.setBoard(board);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		//game state should record as first 
		gameState.setCurrTurn(1);	
		// set basic variable
		gameState.setIsHumanTurn(false);
		gameState.setGameState("game not start");
		gameState.setGameAction("pending");
		// use thread to run initial 
		InitialThread initialThread = new InitialThread(gameState,out);
		Thread initial = new Thread(initialThread);
		initial.start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		initialThread.exit = true;
		
	
		
	
	}

}


