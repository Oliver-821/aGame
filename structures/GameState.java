package structures;


import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;


/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
	/**
	 * @currTurn game turn. Turn 1 starts after initialize. After human play and AI play. Change to Turn 2
	 * @tempTile the tile clicked on the board 
	 * @card the card clicked on the board
	 * @unit the unit clicked on the board 
	 * @human humanPlayer
	 * @AI AIPlayer
	 * @isHumanTurn check if it is human turn or not. 
	 * @board store board
	 * @posX the posX of tile clicked on the board 
	 * @posY the posY of tile clicked on the board 
	 * @gameState store gameState
	 * @handPosition the position of card clicked 
	 * @gameAction record game action : clickTile clickCard
	 */
	private int currTurn = 0;
	private Tile tempTile ;
	private Card card;
	private BetterUnit unit;
	private HumanPlayer human;
	private AIPlayer AI;
	private Boolean isHumanTurn = false;
	private Board board;
	private int posX;
	private int posY;
	private String gameState = "";
	private int handPosition = -1;
	private String gameAction = "tile click";




	public int getCurrTurn() {
		return currTurn;
	}
	public void setCurrTurn(int currTurn) {
		this.currTurn = currTurn;
	}
	public Tile getTempTile() {
		return tempTile;
	}
	public void setTempTile(Tile tempTile) {
		this.tempTile = tempTile;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public BetterUnit getUnit() {
		return unit;
	}
	public void setUnit(BetterUnit unit) {
		this.unit = unit;
	}
	public HumanPlayer getHuman() {
		return human;
	}
	public void setHuman(HumanPlayer human) {
		this.human = human;
	}
	public AIPlayer getAI() {
		return AI;
	}
	public void setAI(AIPlayer aI) {
		AI = aI;
	}

	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public String getGameState() {
		return gameState;
	}
	public void setGameState(String gameState) {
		this.gameState = gameState;
	}
	public int getHandPosition() {
		return handPosition;
	}
	public void setHandPosition(int handPosition) {
		this.handPosition = handPosition;
	}
	public String getGameAction() {
		return gameAction;
	}
	public void setGameAction(String gameAction) {
		this.gameAction = gameAction;
	}
	
	public Boolean getIsHumanTurn() {
		return isHumanTurn;
	}
	public void setIsHumanTurn(Boolean isHumanTurn) {
		this.isHumanTurn = isHumanTurn;
	}
	
	
	/**
	 * check current player 
	 */
	
	public Player getCurrPlayer() {	
		return isHumanTurn ? human : AI;
	}
	
	
}
