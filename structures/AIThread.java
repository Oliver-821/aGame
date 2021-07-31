package structures;

import akka.actor.ActorRef;

/**
 * use thread to run AI play 
 * thread to avoid missing message 
 */
public class AIThread implements Runnable{
	/**
	 * @AI : AIPlayer
	 * @gameState : GameState
	 * @out : ActorRef
	 */
	AIPlayer AI;
	GameState gameState;
	ActorRef out;
	public volatile boolean exit = false;
	
	/**
	 *@param AI : AIPlayer
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public AIThread(AIPlayer AI,GameState gameState , ActorRef out){
		this.AI = AI;
		this.gameState = gameState;
		this.out = out;
	}
	@Override
	public synchronized void run() {  
		while(!exit) {
			AI.AIPlay(gameState, out);
		}
	}
}
