package structures;

import akka.actor.ActorRef;
import commands.BasicCommands;

/**
 * Initial thread to avoid missing message 
 */
public class InitialThread implements Runnable{
	
	private GameState gameState;
	private ActorRef out;
	public volatile boolean exit = false;
	
	/**
	 * 
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public InitialThread(GameState gameState,  ActorRef out) {
		this.gameState = gameState;
		this.out = out;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!exit) {
			// create humanPlayer 
			HumanPlayer human = new HumanPlayer(out,gameState);
			gameState.setHuman(human);

			
			//create AI player
			AIPlayer AI = new AIPlayer(out,gameState);
			gameState.setAI(AI);
			
			human.firstDraw(out);
			human.drawAvatar(gameState,out);
			AI.drawAvatar(gameState,out);
			AI.firstDraw(out);
			BasicCommands.addPlayer1Notification(out, "game Start", 2);
			gameState.setGameState("run");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.addPlayer1Notification(out, "round " + gameState.getCurrTurn(), 2);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.addPlayer1Notification(out, "human turn ", 2);
			human.setMana(2);
			BasicCommands.setPlayer1Mana(out, human);
			gameState.setIsHumanTurn(true);
		}
	}

}
