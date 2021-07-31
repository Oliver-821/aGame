package structures;

import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Player;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import utils.deckFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import akka.actor.ActorRef;
import structures.basic.EffectAnimation;
import structures.basic.Tile;

/**
 * AI logic to play. 
 */
public class AIPlayer extends Player{


	/**
	 * 
	 * @AIDeck AI deck contains all 20 cards
	 * @cardNum index for AI deck to store the index we are 
	 * @onhandCardNum the number of on hand card
	 * @onhandCard on hand card of AI
	 * @onboardUnit the unit on the board including AI unit and human unit 
	 * @selectedCardNum after calculation, the card id AI will select to summon 
	 * @cardSummonPos the position AI could summon card 
	 * @value value of different card for choosing the summoned card 
	 * @weight mana of card 
	 * @attackModeUnit the unit on the board when AI is risk
	 * @defenseModeUnit the unit on the board when AI isn't risk
	 * @isAIRisk decide if AI is risk or not 
	 */
	private ArrayList<Card> AIDeck;
	private int cardNum = 0;
	private int onhandCardNum = 0;
	public ArrayList<Card> onhandCard;
	private ArrayList<BetterUnit> onboardUnit;
	private ArrayList<Integer> selectedCardNum;
	private ArrayList<int[]> cardAttackSummonPos;
	private ArrayList<int[]> cardDefenseSummonPos;
	private ArrayList<int[]> unitAttackPos;
	private ArrayList<int[]> unitMovePos;
	private  int[] value;
	private  int[] weight;
	private ArrayList<BetterUnit> attackModeUnit;
	private ArrayList<BetterUnit> defenseModeUnit;
	Boolean isAIRisk = false;
	private BetterUnit onPlayUnit;
	private int[] silverguardKnightPos;
	private Boolean isRemainCards = true;

	/**
	 * setter and getter
	 */
	public int getOnhandCardNum() {
		return onhandCardNum;
	}

	public void setOnhandCardNum(int onhandCardNum) {
		this.onhandCardNum = onhandCardNum;
	}

	public ArrayList<Card> getOnhandCard() {
		return onhandCard;
	}

	public void setOnhandCard(ArrayList<Card> onhandCard) {
		this.onhandCard = onhandCard;
	}
	
	public ArrayList<BetterUnit> getOnboardUnit() {
		return onboardUnit;
	}

	public void setOnboardUnit(ArrayList<BetterUnit> onboardUnit) {
		this.onboardUnit = onboardUnit;
	}


	public BetterUnit getOnPlayUnit() {
		return onPlayUnit;
	}

	public void setOnPlayUnit(BetterUnit onPlayUnit) {
		this.onPlayUnit = onPlayUnit;
	}

	public ArrayList<BetterUnit> getAttackModeUnit() {
		return attackModeUnit;
	}

	public void setAttackModeUnit(ArrayList<BetterUnit> attackModeUnit) {
		this.attackModeUnit = attackModeUnit;
	}

	public ArrayList<BetterUnit> getDefenseModeUnit() {
		return defenseModeUnit;
	}

	public void setDefenseModeUnit(ArrayList<BetterUnit> defenseModeUnit) {
		this.defenseModeUnit = defenseModeUnit;
	}

	public Boolean getIsAIRisk() {
		return isAIRisk;
	}

	public void setIsAIRisk(Boolean isAIRisk) {
		this.isAIRisk = isAIRisk;
	}

	public ArrayList<int[]> getUnitAttackPos() {
		return unitAttackPos;
	}

	public void setUnitAttackPos(ArrayList<int[]> unitAttackPos) {
		this.unitAttackPos = unitAttackPos;
	}

	public ArrayList<int[]> getUnitMovePos() {
		return unitMovePos;
	}

	public void setUnitMovePos(ArrayList<int[]> unitMovePos) {
		this.unitMovePos = unitMovePos;
	}
	
	public Boolean getIsRemainCards() {
		return isRemainCards;
	}

	public void setIsRemainCards(Boolean isRemainCards) {
		this.isRemainCards = isRemainCards;
	}
	
	public ArrayList<Integer> getSelectedCardNum() {
		return selectedCardNum;
	}

	public void setSelectedCardNum(ArrayList<Integer> selectedCardNum) {
		this.selectedCardNum = selectedCardNum;
	}


	/**
	 * create deck and shuffle cards set health and mana for human
	 * 
	 * @param out : ActorRef
	 * @param gameState : gameState
	 */

	public AIPlayer(ActorRef out, GameState gameState) {
		super();
		setHealth(20);
		setMana(0);
		// create deck and shuffle deck
		AIDeck = new ArrayList<Card>();
		AIDeck = deckFactory.makeDeck("AI");
		Collections.shuffle(AIDeck);
		onhandCard = new ArrayList<Card>();
		selectedCardNum = new ArrayList<Integer>();
		cardAttackSummonPos = new ArrayList<int[]>();
		cardDefenseSummonPos = new ArrayList<int[]>();
		unitAttackPos = new ArrayList<int[]>();
		unitMovePos = new ArrayList<int[]>();
		onboardUnit = new ArrayList<BetterUnit>();
		attackModeUnit = new ArrayList<BetterUnit>();
		defenseModeUnit = new ArrayList<BetterUnit>();
		onPlayUnit = new BetterUnit();
		silverguardKnightPos = new int[2];
		// set health and mana for AI
		BasicCommands.addPlayer2Notification(out, "set AI", 2);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Health(out, this);
		try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Mana(out, this);
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * at the beginning of the game, AI need to draw three cards 
	 * 
	 * @param out : ActorRef
	 */

	public void firstDraw(ActorRef out) {
		for (int i = 0; i < 3; i++) {
			onhandCard.add(AIDeck.get(i));
			cardNum++;
			onhandCardNum = 2;
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	/**
	 * draw one card by the end of the game 
	 * 
	 * @param out : ActorRef
	 */
	public void darwCard(ActorRef out) {
		onhandCardNum++;
		cardNum++;
		isRemainCards = true;
		if(cardNum<20) {
			if (onhandCardNum > 5) {
				onhandCardNum = 5;
			} else {
				onhandCard.add(AIDeck.get(cardNum));
				try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
				
			}
		} else {
			BasicCommands.addPlayer1Notification(out, "no more cards", 2);
		    isRemainCards = false;
		}


	}


	/**
	 * draw avatar and set up health 
	 * 
	 * @param gameState : gameState
	 * @param out : ActorRef
	 */
	public void drawAvatar(GameState gameState,ActorRef out) {
		BasicCommands.addPlayer2Notification(out, "draw AI Avatar", 2);
		BetterUnit avatar = gameState.getBoard().unit2[16];
		avatar.setPositionByTile(gameState.getBoard().tile[7][2]);
		EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.playEffectAnimation(out, ef, gameState.getBoard().tile[7][2]);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.drawUnit(out, avatar, gameState.getBoard().tile[7][2]);
		gameState.getBoard().tile[7][2].setUnit(avatar);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		avatar.setHealth(getHealth());
		int health = avatar.getHealth();
		BasicCommands.setUnitHealth(out, avatar, health);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, avatar, 2);
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**gain Mana at the beginning of each turn and make it less than 9
	 * 
	 * @param gameState : gameState
	 * @param out : ActorRef
	 */
	public void gainMana(GameState gameState,ActorRef out) {
		BasicCommands.addPlayer2Notification(out, "gain AI mana", 2);
		int mana = gameState.getCurrTurn() + 1;
		if (mana > 9) {
			mana = 9;
		}
		setMana(mana);
		BasicCommands.setPlayer2Mana(out, this);
	}

	/**Drain Mana at the end of each Turn
	 * 
	 * @param out : ActorRef
	 */

	public void drainMana(ActorRef out) {
		BasicCommands.addPlayer2Notification(out, "drain AI mana", 2);
		setMana(0);
		BasicCommands.setPlayer2Mana(out, this);
	}
	
	/**
	 * get on board unit when make decision
	 * check if AI is risk or not and based on result decided AI need to attack or defense
	 * 
	 * @param gameState : GameState
	 */
	public void OnboardUnit(GameState gameState){
		if(onboardUnit != null) onboardUnit.clear();
		if(attackModeUnit != null) attackModeUnit.clear();
		if(defenseModeUnit != null)  defenseModeUnit.clear();
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		isAIRisk = false;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 5; j++) {
				if(tile[i][j].getUnit() != null) {
					if(tile[i][j].getUnit().getId() == 36 || tile[i][j].getUnit().getId() == 44) {
						onboardUnit.add(tile[i][j].getUnit());
						onboardUnit.add(tile[i][j].getUnit());
					}else 
						onboardUnit.add(tile[i][j].getUnit());
					
					if(tile[i][j].getUnit().getId()<30) {
						attackModeUnit.add(tile[i][j].getUnit());
						if(i<4)defenseModeUnit.add(tile[i][j].getUnit());
					}	
				}
			}
		}
		if(defenseModeUnit.size()>3) {
			isAIRisk = true;
		}
	}
	
	/**
	 * use card :
	 * summon unit on the board 
	 * using spell card if had 
	 * 
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public void summonCard( GameState gameState, ActorRef out) {
		findSelectedCardNum(gameState);
		Board board = gameState.getBoard();
		Tile[][] tile =board.getTile();
		Random posNum = new Random();
		int[] pos = null;
		int decreassOfPosition = 0;
		if(selectedCardNum.size() != 0) {
			selectedCardNum.sort(Comparator.naturalOrder());
			for(int i = 0; i<selectedCardNum.size();i++) {
				int handPosition = selectedCardNum.get(i) - decreassOfPosition;
				decreassOfPosition ++;
				Card card = onhandCard.get(handPosition);
				summonHighlight(handPosition, gameState, out);
				if(card.getId() == 47 || card.getId() == 49) {
					BasicCommands.addPlayer2Notification(out, "Staff Of Y'Kir", 1);
					BasicCommands.addPlayer2Notification(out, "Avatar attack +2", 2);
					card.executeAbility(handPosition, gameState, out);
					onhandCard.remove(handPosition);
					onhandCardNum --;
				}else if(card.getId() == 46 || card.getId() == 48) {
					BasicCommands.addPlayer2Notification(out, "Entropic Decay", 1);
					BasicCommands.addPlayer2Notification(out, "destroy unit", 2);
					card.executeAbility(handPosition, gameState, out);
					onhandCard.remove(handPosition);
					onhandCardNum --;
				}else if(card.getId() == 34 || card.getId() == 42) { // draw  range unit beside avatar. It don't need move
					int[] avatarPos = board.getPos(46);
					Boolean flag = false;
					for(int x = avatarPos[0] -1; x <  avatarPos[0] +1; x++) {
						for(int y = avatarPos[1] -1; y <avatarPos[1] + 1; y++ ) {
							if(x>=0 && y>=0 && x<9 && y<9 && tile[x][y].getTileStatus() ==1) {
								summonUnit(x,y,gameState,out);
								flag = true;
								break;
							}
						}
						if(flag) break;
					}
				}else {
					if(isAIRisk) {
						if(cardDefenseSummonPos.size() > 0) {
							int selectedPosNum = posNum.nextInt(cardDefenseSummonPos.size() );
							pos = cardDefenseSummonPos.get(selectedPosNum);		
							summonUnit(pos[0],pos[1],gameState,out);
						}else {
							if(cardAttackSummonPos.size() > 0) {
								int selectedPosNum = posNum.nextInt(cardAttackSummonPos.size() );
								pos = cardAttackSummonPos.get(selectedPosNum);
								summonUnit(pos[0],pos[1],gameState,out);
							}
						}
					}else {
						if(cardAttackSummonPos.size() > 0) {
							int selectedPosNum = posNum.nextInt(cardAttackSummonPos.size() );
							pos = cardAttackSummonPos.get(selectedPosNum);
							summonUnit(pos[0],pos[1],gameState,out);
						} else if(cardDefenseSummonPos.size()>0) {
							int selectedPosNum = posNum.nextInt(cardDefenseSummonPos.size() );
							pos = cardDefenseSummonPos.get(selectedPosNum);
							summonUnit(pos[0],pos[1],gameState,out);
						}
					}

				}	
			}
		}
	}
	

	
	/**
	 * summon unit on the board 
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef	 
	 */
	public void summonUnit(int x, int y,GameState gameState, ActorRef out) {
		Card selectedCard = onhandCard.get(gameState.getHandPosition());
		BasicCommands.addPlayer2Notification(out, "summon card", 2);
		AdvancedCommands.clearHighlight(gameState, out);
		AdvancedCommands.summonUnit(x, y, gameState, out);
		setMana(getMana() - selectedCard.getManacost());
		BasicCommands.setPlayer2Mana(out, this);
		gameState.setGameAction("pending");
		if(selectedCard.getId() == 30|| selectedCard.getId() == 38) {
			selectedCard.executeAbility(gameState.getHandPosition(), gameState, out);
		}
	}
	
	/**
	 * summon Highlight 
	 * unit card highlight 
	 * spell card highlight
	 * 
	 *@param handPosition : on hand card position
	 *@param gameState : GameState
	 *@param out : ActorRef	  
	 */
	public void summonHighlight(int handPosition, GameState gameState, ActorRef out) {
		AdvancedCommands.clearHighlight(gameState, out);
		Card card = onhandCard.get(handPosition);
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		int id = card.getId();
		int[]avatarPos = new int[2];
		avatarPos = board.getPos(46);
		cardAttackSummonPos.clear();
		cardDefenseSummonPos.clear();
		if(id == 46 || id == 48) {
			for(int i = 0 ; i < 9 ; i ++) {
				for(int j = 0; j < 5 ; j++) {
					if(tile[i][j].getUnit() != null && tile[i][j].getUnit().getId()<30) {
						BasicCommands.drawTile(out, tile[i][j], 2);
						try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
					}	
				}	
			}
		} else if (id == 47 || id ==49) {
			Tile avatarTile = board.tile[avatarPos[0]][avatarPos[1]];
			BasicCommands.drawTile(out, avatarTile, 1);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		} else if(id == 33 || id ==41){
			card.executeAbility(handPosition, gameState, out);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}else {
			AdvancedCommands.summonHighlight(handPosition, gameState, out);
			gameState.setGameAction("clickCard");
			gameState.setHandPosition(handPosition);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		for(int i =0; i< 9;i++) {
			for(int j = 0; j<5 ; j++) {
				if(tile[i][j].getTileStatus() == 1) {
					int[] pos = new int[2];
					pos[0] = i;
					pos[1] = j;
					if(pos[0] <avatarPos[0]) {
						cardAttackSummonPos.add(pos);	
					} else 
						cardDefenseSummonPos.add(pos);
				}
			}
		}
	}
	
	/**
	 * create value for cards and select the card we will summon on the board 
	 * 
	 * @param gameState : GameState
	 */
    public void findSelectedCardNum(GameState gameState) {
    	selectedCardNum.clear();
		weight = new int[onhandCard.size()];
		value = new int[onhandCard.size()];
    	int Mana = getMana(); 
		for(int i = 0; i < weight.length ; i++) {
			weight[i] = onhandCard.get(i).getManacost();
		}
		createValue(gameState);
		packCard(Mana,weight.length,weight,value); 
    }
    
    /**
     * When the mana is fixed, we will select the card which value is the highest
     */
	public void packCard(int mana, int kind, int[] weight, int[] value) {
		int[][] dp = new int[kind+1][mana +1];
		for(int i = 1 ; i < kind+1 ; i++) {
			for(int j = 1 ; j < mana + 1; j++) {
				if(weight[i-1] > j)
					dp[i][j] = dp[i-1][j-1];
				else
					dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-weight[i-1]] + value[i-1]);
			}
		}
		int j = mana;
		selectedCardNum.clear();
		for(int i = kind; i>0; i--) {
			if(dp[i][j] >dp[i-1][j]) {
				selectedCardNum.add(i-1);
				j = j -weight[i-1];
			}
			if(j==0)
				break;
			}

	}

    /**
     * create value for on hand card to make best decision 
     * spell card will only be used in the right situation 
     * unit card will be valued by their health and attack, once attack and health is larger, it will get more value 
     * 
     * @param gameState : GameState
     */
    private int[] createValue(GameState gameState) {
    	Board board = gameState.getBoard();
    	for(int i = 0; i < onhandCard.size(); i++) {
    		Card card = onhandCard.get(i);
    		if(card.getId() == 46 || card.getId() == 48) { 
    			for(int index = 0 ; index< onboardUnit.size(); index++) {
    				int health = onboardUnit.get(index).getHealth();
    				if(attackModeUnit.size() > 1) {
        				if(health>=5) {
        					value[i] =30;
        				}else 
        					value[i] =5;
    				}else 
    						value[i] =-10;
    			}
    		}else if (card.getId() == 47 || card.getId() == 49) {
    			boolean check = false ;
    			if(isAIRisk) 
    				check = true;
    			if(check) {
    				value[i] = 30;
    			} else 
    				value[i] = 20;
    		} else {
    			int id = card.getId();
    			int valueNum = (board.unit2[id -30].getAttack() + board.unit2[id -30].getHealth())/2;
    			if(id == 31 || id == 39 || id == 32 || id == 40) {
    				value[i] = valueNum;
    			}else if((id == 35 || id == 43) && isAIRisk) {
    				value[i] = valueNum + 20;
    			}else
    				value[i] = valueNum + 10;			    			
    		}
    	}
    	return value;
    }
    
    /**
     * check silvergurad knight, AI will destroy it firstly 
     * 
     * @param gameState : GameState
     */
    public Boolean checkSilverguardKnight(GameState gameState){
    	Boolean result = false;
    	Board board = gameState.getBoard();
    	Tile[][] tile = board.getTile();
    	for(BetterUnit unit: attackModeUnit) {
    		if(unit.getId() == 7 || unit.getId() == 15) {
    			int[] pos = new int[2];
    			pos = board.getPos(unit.getId());
    			if(tile[pos[0]][pos[1]].getTileStatus() == 2) {
        			result = true;
        			silverguardKnightPos = board.getPos(unit.getId());
    			}
    		}
    	}
    	return result;
    }
    
    
    
    /**
     * select attack unit 
     * give the value to different unit based on unit health and attack 
     * choose the unit has the greatest value 
     * 
     * @param selectedUnit : BetterUnit
     * @param position : list to contain all attack postion 
     * @param gameState : gameState
     * @return int[] :selected attack unit postion in array
     */
    public int[] selectAttackUnit(BetterUnit selectedUnit, ArrayList<int[]> position,GameState gameState) {
    	Board board = gameState.getBoard();
    	Tile[][] tile = board.getTile();
    	int attack = selectedUnit.getAttack();
    	int health = selectedUnit.getHealth();
    	int[] selectedPos = new int[2];
    	int preValue = 0,value = 0;
    	if(position.size() == 1) {
    		selectedPos = position.get(0);
    	}else {
        	for(int[] pos:position) {
        		BetterUnit unit = tile[pos[0]][pos[1]].getUnit();
        		if(unit.getHealth()==attack) {
        			value += 100;
        		}else if(unit.getHealth() <attack) {
        			value += 50 +  unit.getAttack() - health;
        		}else if(unit.getHealth() > attack) {
        			value += 10 + attack - unit.getHealth() + unit.getAttack() - health;
        		}

        		if(value>preValue) {
        			preValue = value;
        			selectedPos = pos; 					
        		}
        		
        	}
    	}
    	return selectedPos;
    }
    
    /**
     * unit action 
     * unit attack and unit move 
     * if unit could attack, it will attack 
     * or it will move 
     * if AI isn't risk, unit will attack Avatar directly or it will attack the units to avoid risk 
     * 
     * @param gameState : GameState
     * @param out : ActorRef
     */
    public void unitAction( GameState gameState, ActorRef out) {
    	Board board = gameState.getBoard();
    	int[] pos = new int[2];
    	Random posNum = new Random();
    	if(onboardUnit.size() > 0) {
        	for(BetterUnit unit: onboardUnit) {
        		if(unit.getHealth()>0 ) {
            		if(gameState.getGameState().equals("run")) {
                		if(unit.getId()>=30 && unit.getId() <46) {
                			pos = board.getPos(unit.getId());
                			highlight(pos[0],pos[1],gameState,out);
                			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                			if(isAIRisk) {
            					if(unitAttackPos.size() >0) {
            						pos = selectAttackUnit(unit,unitAttackPos,gameState);
            						unitAttack(pos[0],pos[1],gameState,out);
            					}else if(unitMovePos.size() >0) {
            						int selectedPosNum = posNum.nextInt(unitMovePos.size() );
            						pos = unitMovePos.get(selectedPosNum);
            						unitMove(pos[0],pos[1],gameState,out);
            						AdvancedCommands.clearHighlight(gameState, out);
            						try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            					}
                			}else {
            					if(unitAttackPos.size() >0) {
        							pos = board.getPos(16);
        							Boolean attackHuman = false;
        							for(int[] position : unitAttackPos) {
        								if(position[0] == pos[0] && position[1] == pos[1])  
        									attackHuman = true;
        							}
        							if(attackHuman) {
        								if(checkSilverguardKnight(gameState)) {
        									unitAttack(silverguardKnightPos[0],silverguardKnightPos[1],gameState,out);
        								}else
        									unitAttack(pos[0],pos[1],gameState,out);
        							} else {
        								pos = selectAttackUnit(unit,unitAttackPos,gameState);
        								unitAttack(pos[0],pos[1],gameState,out);
        							}
            					}else if(unitMovePos.size() >0){
            						pos = selectMovePlace(gameState);
        							unitMove(pos[0],pos[1],gameState,out);
        							AdvancedCommands.clearHighlight(gameState, out);
        							try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            						}
            					}
                				
                			}
                		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
            			}
        			}
        		}
        	}
    	}
    
    

    /**
     * the highlight for attacking, moving 
     * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef 
     */
    public void highlight(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		AdvancedCommands.clearHighlight(gameState, out);
		if(tile[x][y].getUnit()!= null && (tile[x][y].getUnit().getId() == 34 || tile[x][y].getUnit().getId() == 42 )) {
			tile[x][y].getUnit().executeAbility(x, y, gameState, out);
		}else if(tile[x][y].getUnit()!= null && (tile[x][y].getUnit().getId() == 37 || tile[x][y].getUnit().getId() == 45 )){
			tile[x][y].getUnit().executeAbility(x, y, gameState, out);
		}else {
			AdvancedCommands.moveHighlight(x, y, gameState, out);	
			AdvancedCommands.attackHighlight(x, y, gameState, out);
			AdvancedCommands.moveAttackHighlight(x, y, gameState, out);
		}
		gameState.setGameAction("clickUnit");
		unitMovePos.clear();
		unitAttackPos.clear();
		for(int i =0; i< 9;i++) {
			for(int j = 0; j<5 ; j++) {
				if(tile[i][j].getTileStatus() == 1) {
					int[] pos = new int[2];
					pos[0] = i;
					pos[1] = j;
					unitMovePos.add(pos);
				}else if(tile[i][j].getTileStatus() == 2) {
					int[] pos = new int[2];
					pos[0] = i;
					pos[1] = j;
					unitAttackPos.add(pos);
				}
			}
		}
				
    }
    
    /**
     * unit will move and then attack.
     * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
     */
	public void moveAttack(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile selectedTile = board.tile[gameState.getPosX()][gameState.getPosY()];
		Tile tile = board.tile[x][y];
		BetterUnit selectedUnit = selectedTile.getUnit();
		ArrayList<int[]> movePlace = new ArrayList<int[]>();
		Random random;
		for (int i = x - 1; i <= x+1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && j >= 0 && i < 9 && j < 5) {
					Tile tile2 = board.tile[i][j];
					if(tile2.getTileStatus() == 1) {
						int[] place = new int[2];
						place[0] = i;
						place[1] = j;
						movePlace.add(place);
					}
				}
			}	
		}
		if(movePlace.size()>0) {
			random = new Random();
			int index = random.nextInt(movePlace.size());
			int[] updatePlace = new int[2];
			updatePlace = movePlace.get(index);
			unitMove(updatePlace[0], updatePlace[1], gameState, out);
			gameState.setPosX(updatePlace[0]);
			gameState.setPosY(updatePlace[1]);
			gameState.setUnit(selectedUnit);
			gameState.setTempTile(board.tile[updatePlace[0]][updatePlace[1]]);
			tile.setTileStatus(2);
			AdvancedCommands.adjacentAttack(x, y, gameState, out);
			AdvancedCommands.avatarAction(x, y, gameState, out);
		}
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		AdvancedCommands.clearHighlight(gameState, out);
	}
	
	
	/**
	 * unit move
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	public void unitMove(int x, int y, GameState gameState, ActorRef out) {	
		AdvancedCommands.unitMove(x, y, gameState, out);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * unit attack
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef  
	 */
	public void unitAttack(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		BetterUnit selectedUnit = tile[gameState.getPosX()][gameState.getPosY()].getUnit();
		if(selectedUnit!= null && (selectedUnit.getId() == 34 || selectedUnit.getId() == 42 )) {
			AdvancedCommands.rangeAttack(x, y, gameState, out);
			AdvancedCommands.avatarAction(x, y, gameState, out);
		}else if (x < gameState.getPosX() - 1 || y < gameState.getPosY() - 1 || x > gameState.getPosX() + 1 || y> gameState.getPosY() + 1) {
				if(selectedUnit != null) moveAttack(x,y,gameState,out);
				try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
			} else {
				if(selectedUnit != null) {
					AdvancedCommands.adjacentAttack(x, y, gameState, out);
					AdvancedCommands.avatarAction(x, y, gameState, out);
				}
		}
		AdvancedCommands.clearHighlight(gameState, out);
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * avatar action
	 * 
	 *@param gameState : GameState
	 *@param out : ActorRef
	 */
	
	public void avatarAction( GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		int[] avatarPos = board.getPos(46);
		int[] playerAvatarPos = board.getPos(16);
		gameState.setPosX(avatarPos[0]);
		gameState.setPosY(avatarPos[1]);
		gameState.setTempTile(tile[avatarPos[0]][avatarPos[1]]);
		BetterUnit avatar = board.unit2[16];
		gameState.setUnit(avatar);
		int attack = avatar.getAttack();
		int value = 0;
		int prevalue = 0;
		Boolean isAttack = false;
		Boolean attackAvatar = false;
		int[] attackPos = new int[2];
		int[] movePos = new int[2];
		Boolean attackAvatar2 = false;
		highlight(avatarPos[0],avatarPos[1],gameState,out);
		if(attack > 2 && tile[playerAvatarPos[0]][playerAvatarPos[1]].getUnit().getHealth()<= avatar.getHealth() + 2) {
			attackAvatar = true;
		}
		if(unitAttackPos.size()>0) {
			int[] tempPos = new int[2];
			for(int i = 0; i < unitAttackPos.size()  ; i++) {
				tempPos = unitAttackPos.get(i);
				if(tile[tempPos[0]][tempPos[1]].getUnit().getId() <30 ) {
					if(tile[tempPos[0]][tempPos[1]].getUnit().getId() == 16) {
						attackAvatar2 = true;
					}
					BetterUnit unit = tile[tempPos[0]][tempPos[1]].getUnit();
					if(attack == unit.getHealth()) {
						value += 50 + unit.getAttack();
					} else if(attack>unit.getHealth()) {
						value += 30 + unit.getHealth() - attack + unit.getAttack();
					}
					
					if(value > prevalue) {
						prevalue = value;
						isAttack = true;
						attackPos = tempPos;
					}
				}	
			}
		}

		if(isAttack) {
			if(attackPos[0] < avatarPos[0] -1 || attackPos[0] > avatarPos[0] + 1 || 
					attackPos[1] < avatarPos[1] -1 || attackPos[1] > avatarPos[1] -1) {
				moveAttack(attackPos[0], attackPos[1], gameState, out);
			}else 
				AdvancedCommands.adjacentAttack(attackPos[0], attackPos[1], gameState, out);
		} else if(attackAvatar){
			if(playerAvatarPos[0] < avatarPos[0] -1 || playerAvatarPos[0] > avatarPos[0] + 1 || 
					playerAvatarPos[1] < avatarPos[1] -1 || playerAvatarPos[1] > avatarPos[1] -1) {
				if(attackAvatar2) {
					moveAttack(playerAvatarPos[0], playerAvatarPos[1], gameState, out);
				}else {
					movePos = selectMovePlace(gameState);
					unitMove(movePos[0],movePos[1],gameState,out);
				}
			}else if(playerAvatarPos[0] > avatarPos[0] -1 && playerAvatarPos[0] < avatarPos[0] + 1 && 
					playerAvatarPos[1] > avatarPos[1] -1 && playerAvatarPos[1] < avatarPos[1] -1){
				AdvancedCommands.adjacentAttack(playerAvatarPos[0],playerAvatarPos[1], gameState, out);
			}	
		}else if(avatarPos[0] <7 ){
			for(int i = unitMovePos.size() -1; i >= 0; i --) {
				movePos = unitMovePos.get(i);
				if(movePos[0] == avatarPos[0] + 2) {
					unitMove(movePos[0],movePos[1],gameState,out);
					break;
				} else if(movePos[0] == avatarPos[0] + 1) {
					unitMove(movePos[0],movePos[1],gameState,out);
					break;
				}
			}
		}

	}
	
	/**
	 * select perfect move place
	 * 
	 * @param gameState : GameState
	 */
	
	public int[] selectMovePlace(GameState gameState) {
		Board board = gameState.getBoard();
		int[] avatarPos = new int[2];
		avatarPos = board.getPos(16);
		int[] selectedPos = new int[2];
		int value = 0;
		int prevalue = 100;
		for(int[] pos : unitMovePos) {
			value = Math.abs(avatarPos[0] - pos[0]) + Math.abs(avatarPos[1] - pos[1]);
			if(value < prevalue) {
				prevalue = value;
				selectedPos = pos;
			}
		}
		return selectedPos;
	}
    
	/**
	 * AI play 
	 * 
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public void AIPlay(GameState gameState , ActorRef out) {
		// drain AI mana
		gainMana(gameState, out);
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
		// unit action
		if(gameState.getGameState().equals("run")) {
	    	OnboardUnit(gameState);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			unitAction(gameState,out);
			try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
		} 
		// summon card or use spell card
		if(gameState.getGameState().equals("run")) {
			OnboardUnit(gameState);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			summonCard(gameState,out);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		} 
		// Avatar action
		if(gameState.getGameState().equals("run")) {
	    	OnboardUnit(gameState);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			avatarAction(gameState,out);
			try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
		} 
		//AI finish 
		if(gameState.getGameState().equals("run")) {
			BasicCommands.addPlayer2Notification(out, "finish, you turn", 2);
			drainMana(out);
			darwCard(out);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			if(!isRemainCards) {
				BasicCommands.addPlayer2Notification(out, "no more cards", 2);
				gameState.setGameState("player win");
				BasicCommands.addPlayer1Notification(out, "Congratulation, you win", 2);
			}else {
				AdvancedCommands.clearHighlight(gameState, out);
				try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
				// set game state turn ++
				int currTurn = gameState.getCurrTurn() + 1;
				gameState.setCurrTurn(currTurn);
				BasicCommands.addPlayer1Notification(out, "Round " + gameState.getCurrTurn(), 2);
				// AI finish and Human start. Human will gain mana
				gameState.setHandPosition(-1);
				try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
				// set turn as Human
				BasicCommands.addPlayer1Notification(out, "player turn", 2);
				gameState.getHuman().gainMana(gameState, out);
			}
		}
		gameState.setGameAction("pending");
		gameState.setIsHumanTurn(true);
	}
	
	

}
