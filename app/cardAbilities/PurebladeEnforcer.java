package cardAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.AIPlayer;
import structures.BetterUnit;
import structures.Board;
import structures.GameState;
import structures.HumanPlayer;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class PurebladeEnforcer implements cardAbility{
	//spell
		@Override
		public void execute(int handPosition, GameState gameState, ActorRef out) {
			// get the board
			Board board;
			// the the human
			HumanPlayer human;
			AIPlayer ai;
			board = gameState.getBoard();
			human = gameState.getHuman();
			ai = gameState.getAI();
			Tile[][] tiles = board.getTile();
			
			// determine whether Mana is enough
			int mana;
			mana = human.getMana();
			if(mana<2) {
				BasicCommands.addPlayer1Notification(out, "you mana is not enough", 2);
				return;
			}else {
				BasicCommands.addPlayer1Notification(out, "summon PurebladeEnforcer", 2);
				System.out.println("human mana before: "+human.getMana());
				mana -= 2;
				human.setMana(mana);
				System.out.println("human mana after: "+human.getMana());
				BasicCommands.setPlayer1Mana(out,human);
				
				
			}		
			int[]pos = new int[2];
			// get position
			pos = board.getPos(6);
			for(int i = 0; i < 9 ; i++) {
				for(int j = 0; j < 5; j++) {
					if(tiles[i][j].getUnit() == null) {
						BasicCommands.drawTile(out, tiles[i][j], 1);
						tiles[i][j].setTileStatus(1);
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			}
			
			gameState.setGameAction("clickCard");
			gameState.setHandPosition(handPosition);
			
			// get tile according to pos
			Tile tile = board.tile[pos[0]][pos[1]];
			
			
			AdvancedCommands.clearHighlight(gameState, out);
					
			
			
			// delete card
			BasicCommands.deleteCard(out, handPosition);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}

		
		
		
		
		
		
		}
