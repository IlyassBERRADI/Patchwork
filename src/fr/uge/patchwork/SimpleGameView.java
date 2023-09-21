package fr.uge.patchwork;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

/**
 * A record representing the graphic display version of the game where xOrigin and yOrigin are the coordinates of the time board,
 * height and width represent the dimensions of the time board, squareSize is the size of a square in all boards and widthWindow and 
 * heightWindow are the dimensions of the window
 * 
 * @author Antoine BENOIT
 * @author Ilyass BERRADI
 * @since 07/05/2023
 *
 */
public record SimpleGameView(int xOrigin, int yOrigin, int height, int width, int squareSize, int widthWindow,
    int heightWindow) implements GameView {
  
  
  /**
   * Initializes the game graphics
   * @param xOrigin
   * @param yOrigin
   * @param length height of time board
   * @param data
   * @param widthWindow
   * @param heightWindow
   * @return instance of SimpleGameView
   */
  public static SimpleGameView initGameGraphics(int xOrigin, int yOrigin, int length, SimpleGameData data, int widthWindow, int heightWindow) {
    Objects.requireNonNull(data);
    var squareSize = length / 7;
    return new SimpleGameView(xOrigin, yOrigin, length, 7 * squareSize, squareSize, widthWindow, heightWindow);
  }
  
  
  
  
  /**
   * Gets the player's choice out of the three patches in front of the neutral
   * time token
   * 
   * @param context 
   * @param data
   * @return The choice made by the player
   */
  private int getPlayerChoice(ApplicationContext context, SimpleGameData data) {
    int res;
    while (true) {
      var event = context.pollEvent();
      if (event==null) {
        continue;
      }
      var action = event.getAction();
      if (action==Action.POINTER_DOWN) {
        var location = event.getLocation();
        res=patchClicked(location.x, location.y, data);
        
        if (res!=-1) {
          return res;
        }
        
      }
    }
    
  }
  
  
  /**
   * Draws the quiltboard on the window with its patches
   * @param graphics
   * @param data
   */
  private void drawQuiltBoard(Graphics2D graphics, SimpleGameData data) {
    for (int i = 0; i < 10; i++) {
      graphics.drawLine(xOrigin+8*squareSize, yOrigin+squareSize*i, xOrigin+17*squareSize, yOrigin+squareSize*i);
      graphics.drawLine(xOrigin+squareSize*(8+i), yOrigin, xOrigin+(8+i)*squareSize, yOrigin+squareSize*9);
    }
    if (data.getTurnPlayer().equals(data.getPlayer1())) {
      graphics.setColor(Color.BLUE);
    }
    else if (data.getTurnPlayer().equals(data.getPlayer2())) {
      graphics.setColor(Color.ORANGE);
    }
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (data.getTurnPlayer().getBoard().getPatchesCollected()[i][j]==1) {
          graphics.fillRect(xOrigin+8*squareSize+j*squareSize, yOrigin+i*squareSize, squareSize, squareSize);
        }
      }
    }
  }
  
  
  /**
   * Draws a text announcing the player who has the turn to play
   * @param data
   * @param graphics
   */
  private void announcePlayerTurn(SimpleGameData data, Graphics2D graphics) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(xOrigin+2*squareSize, yOrigin-squareSize, 5*squareSize, squareSize);
    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.BOLD, 24));
    if (data.getTurnPlayer().equals(data.getPlayer1())) {
      graphics.drawString("It's player1's turn !", xOrigin+2*squareSize, yOrigin-squareSize);
      graphics.drawString("Score: "+(data.getPlayer1().getBoard().getNbButtons() - data.getPlayer1().getBoard().countScoreBlank()), xOrigin+8*squareSize, yOrigin-squareSize);
      graphics.drawString("Buttons: "+ data.getPlayer1().getButtons(), xOrigin+14*squareSize, yOrigin-squareSize);
      
    }
    else {
      graphics.drawString("It's player2's turn !", xOrigin+2*squareSize, yOrigin-squareSize);
      graphics.drawString("Score: "+(data.getPlayer2().getBoard().getNbButtons() - data.getPlayer2().getBoard().countScoreBlank()), xOrigin+8*squareSize, yOrigin-squareSize);
      graphics.drawString("Buttons: "+ data.getPlayer2().getButtons(), xOrigin+14*squareSize, yOrigin-squareSize);
    }
  }
  
  
  /**
   * Returns 1, 2 or 3 if one of the three patches is clicked, -2 if the player presses "skip my turn" and -1 if he presses anywhere
   * else 
   * @param x x coordinate
   * @param y y coordinate
   * @param data
   * @return 
   */
  private int patchClicked(float x, float y, SimpleGameData data) {
    if (x>=xOrigin-3*squareSize-75 && x<=xOrigin-3*squareSize-75+data.getPatches().get(data.getNTPos()).width()*30
        && y>=yOrigin-15 && y<=yOrigin-15+data.getPatches().get(data.getNTPos()).height()*30) {
      return 1;
    }
    else if (x>=xOrigin-3*squareSize-75 && x<=xOrigin-3*squareSize-75+data.getPatches().get(data.getNTPos()+1).width()*30
        && y>=yOrigin-15+(7/2)*squareSize && y<=yOrigin-15+data.getPatches().get(data.getNTPos()+1).height()*30+(7/2)*squareSize) {
      return 2;
    }
    else if (x>=xOrigin-3*squareSize-75 && x<=xOrigin-3*squareSize-75+data.getPatches().get(data.getNTPos()+2).width()*30
        && y>=yOrigin-15+7*squareSize && y<=yOrigin-15+7*squareSize+data.getPatches().get(data.getNTPos()+2).height()*30) {
      return 3;
    }
    else if(x>=xOrigin+2*squareSize && y>=yOrigin+7*squareSize+120 && y<=yOrigin+7.5*squareSize+120 && x<=xOrigin+5*squareSize) {
      return -2;
    }
    else {
      return -1;
    }
  }
  
  
  /**
   * Draws the player on the time board using his position
   * @param graphics
   * @param data
   */
  private void drawPlayer(Graphics2D graphics ,SimpleGameData data) {
    graphics.setColor(Color.BLUE);
    int playerPosition1=data.getPlayer1().getPosition();
    int playerPosition2=data.getPlayer2().getPosition();
    int player1PositionAfterFirstSteps=playerPosition1-5;
    int player2PositionAfterFirstSteps=playerPosition2-5;
    int player1LinePositionStartingSecondLine=(player1PositionAfterFirstSteps)/7;
    int player2LinePositionStartingSecondLine=(player2PositionAfterFirstSteps)/7;
    int player1PositionInLine=player1PositionAfterFirstSteps%7;
    int player2PositionInLine=player2PositionAfterFirstSteps%7;
    int playerSize=20;
    int gap=3;
    if (playerPosition1<5) {
      
      graphics.fill(new Rectangle(xOrigin+2*squareSize+(data.getPlayer1().getPosition())*squareSize+
          (squareSize-20)/2+19, yOrigin+7*squareSize+squareSize/2-playerSize/2, playerSize, playerSize));
    }
    else if(playerPosition1>=5 && player1LinePositionStartingSecondLine%2==0) {
      graphics.fill(new Rectangle2D.Double(xOrigin+6*squareSize+gap-player1PositionInLine*squareSize, 
          yOrigin+6.5*squareSize-playerSize/2-player1LinePositionStartingSecondLine*squareSize, playerSize, playerSize));
    }
    else if (playerPosition1>=5 && player1LinePositionStartingSecondLine%2==1) {
      graphics.fill(new Rectangle2D.Double(xOrigin+gap+player1PositionInLine*squareSize, 
          yOrigin+6.5*squareSize-playerSize/2-player1LinePositionStartingSecondLine*squareSize, playerSize, playerSize));
    }
    graphics.setColor(Color.ORANGE);
    if (playerPosition2<5) {
      graphics.fill(new Rectangle(xOrigin+2*squareSize+(data.getPlayer2().getPosition())*squareSize+
          (squareSize-20)/2-18, yOrigin+7*squareSize+squareSize/2-playerSize/2, playerSize, playerSize));
    }
    else if(playerPosition2>=5 && player2LinePositionStartingSecondLine%2==0) {
      graphics.fill(new Rectangle2D.Double(xOrigin+7*squareSize-gap-playerSize-player2PositionInLine*squareSize, 
          yOrigin+6.5*squareSize-playerSize/2-player2LinePositionStartingSecondLine*squareSize, playerSize, playerSize));
    }
    else if (playerPosition2>=5 && player2LinePositionStartingSecondLine%2==1) {
      graphics.fill(new Rectangle2D.Double(xOrigin+squareSize-gap-playerSize+player2PositionInLine*squareSize, 
          yOrigin+6.5*squareSize-playerSize/2-player2LinePositionStartingSecondLine*squareSize, playerSize, playerSize));
    }
    
  }
  
  
  /**
   * Draws a patch 
   * @param patch
   * @param graphics
   * @param x x coordinate
   * @param y y coordinate
   */
  private void drawPatch(Patch patch, Graphics2D graphics, int x, int y) {
    for (int i = 0; i < patch.height(); i++) {
      for (int j = 0; j < patch.width(); j++) {
        if (patch.shape()[i][j]==1) {
          graphics.fillRect(x+j*30, y+i*30, 30, 30);
        }
      }
      
    }
    
  }
  
  
  /**
   * Allows the player to finish his turn after buying a patch
   * 
   * @param context
   * @param chosenPiece
   * @param posOtherPlayer Position of the opponent
   * @param data
   */
  private void playAfterBuy(ApplicationContext context, Patch chosenPiece, int posOtherPlayer, SimpleGameData data) {
    Objects.requireNonNull(chosenPiece);
    procedureToPutPatch(context, chosenPiece, data, this);
    moveAfterBuying(context, chosenPiece, data, this);
    if (data.getTurnPlayer().getBoard().verifyBonus() && data.getBonusSquare7Owner() == null && data.getMode() == 2) {
      data.setBonusSquare7Owner(data.getTurnPlayer());
    }
    if (data.getTurnPlayer().getPosition() > posOtherPlayer) {
      data.switchTurn();
    }
  }
  
  
  
  /**
   * Allows the player to buy a patch
   * @param choice Number of the chosen piece out of the three
   * @param data
   * @param context
   * @param view
   * @return The chosen piece
   */
  private Patch buyPatch(int choice, SimpleGameData data, ApplicationContext context, SimpleGameView view) {
    Patch chosenPiece;
    if (choice == 1 && data.getPatches().get(data.getNTPos()).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(0);
    } else if (choice == 2 && data.getPatches().get(data.getNTPos() + 1).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(1);
    } else if (choice == 3 && data.getPatches().get(data.getNTPos() + 2).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(2);
    } else {
      data.setNotEnoughBut(true);
      SimpleGameView.draw(context, data, view);
      try {
        Thread.sleep(1000); 
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      data.setNotEnoughBut(false);
      SimpleGameView.draw(context, data, view);
      
      return null;
    }
    data.getTurnPlayer().addPatchOwned(chosenPiece);
    data.getTurnPlayer().setButtons(data.getTurnPlayer().getButtons() - chosenPiece.price());
    return chosenPiece;
  }

  
  /**
   * Draw a text announcing that a player doesn't have enough buttons to buy the selected patch
   * @param graphics
   * @param data
   */
  private void notEnoughButtons(Graphics2D graphics, SimpleGameData data) {
    if (data.getNotEnoughBut()) {
      graphics.setColor(Color.RED);
      graphics.setFont(new Font("Arial", Font.BOLD, 24));
      graphics.drawString("You don't have enough buttons!!!", xOrigin-3*squareSize-75, yOrigin+10*squareSize);
      
      
    }
    else {
      graphics.setColor(Color.WHITE);
      graphics.fillRect(xOrigin-3*squareSize-75, yOrigin+9*squareSize, 3*squareSize, 3*squareSize);
      
    }
  }
  
  
  /**
   * Moves the player's time token if he skips his turn
   * @param context
   * @param data
   * @param view
   */
  private void moveAfterSkipping(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
    int posCurrentPlayer = data.getTurnPlayer().getPosition();
    Player currentPlayer = data.getTurnPlayer();
    int buttonsAdded = 0;
    int posOtherPlayer = currentPlayer.equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
    int i;
    for (i = posCurrentPlayer + 1; i <= posOtherPlayer + 1 && i < 54; i++) {
      if (data.getTimeBoard()[i] == 1) {
        data.updateButtonScore();
      }
      if (data.getTimeBoard()[i] == 2) {
        
        procedureToPutPatch(context, new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), data, view);
        data.getTimeBoard()[i] = 0;
      }
      buttonsAdded++;
    }
    currentPlayer.setPosition(i - 1);
    currentPlayer.setButtons(buttonsAdded + currentPlayer.getButtons());
    data.switchTurn();
  }

 
  
  
  /**
   * Moves the player's time token if he buys a patch
   * @param context
   * @param chosenPiece The bought patch
   * @param data
   * @param view
   */
  private void moveAfterBuying(ApplicationContext context, Patch chosenPiece, SimpleGameData data, SimpleGameView view) {
    int j2;
    int posCurrentPlayer = data.getTurnPlayer().getPosition();
    for (j2 = posCurrentPlayer + 1; j2 <= chosenPiece.timeStep() + posCurrentPlayer && j2 < 54; j2++) {
      if (data.getTimeBoard()[j2] == 1) {
        data.updateButtonScore();
      }
      if (data.getTimeBoard()[j2] == 2) {
        
        procedureToPutPatch(context, new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), data, view);
      }
    }
    data.getTurnPlayer().setPosition(j2 - 1);
  }

  
  
  
  /**
   * Puts the patch in a certain position chosen by the player
   * @param context
   * @param chosenPiece The piece the player wants to put
   * @param data
   * @param view
   */
  private void procedureToPutPatch(ApplicationContext context, Patch chosenPiece, SimpleGameData data, SimpleGameView view) {
    int x = 9, y = 9;
    Patch pieceToPut=chosenPiece;
    data.setChosenPatch(pieceToPut);
    SimpleGameView.draw(context, data, view);
    
    pieceToPut = chooseRotation(chosenPiece, data, context);
    do {
      
      
      
      while (true) {
        var event = context.pollEvent();
        if (event==null) {
          continue;
        }
        var action = event.getAction();
        
        if (action!=Action.POINTER_DOWN) {
          continue;
        }
        else {
          var location = event.getLocation();
          if (location.x<xOrigin+8*squareSize  || location.x>xOrigin+17*squareSize  ||  location.y<yOrigin  ||  location.y>yOrigin+squareSize*9 ) {
            continue;
          }
          y = ((int)location.x-xOrigin)/squareSize-8;
          x = ((int)location.y-yOrigin)/squareSize;
          if (data.getTurnPlayer().getBoard().getPatchesCollected()[y][x]==1) {
            continue;
          }
          break;
        }
      }
      
    } while (!data.getTurnPlayer().getBoard().ableToPut(x, y, pieceToPut));
    data.setChosenPatch(null);
    data.getTurnPlayer().getBoard().putPatch(x, y, pieceToPut);
    SimpleGameView.draw(context, data, view);
    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    data.getTurnPlayer().getBoard().addButtons(pieceToPut);
  }

  /**
   * Allows the player to type in the terminal the rotation he likes to choose 0 :
   * No rotation 1 : Rotation to the right 2 : Rotation upside down 3 : Rotation
   * to the left
   * 
   * @param p       Patch to rotate
   * @param data    Game's data
   * @param context
   * @return Patch after rotation
   */
  private Patch chooseRotation(Patch p, SimpleGameData data, ApplicationContext context) {
    Patch p2;
    
    while (true) {
      p2=checkRotationChoiceRange(context, data.getTurnPlayer().getBoard(), p);
      if (p2!=null) {
        
        return p2;
      }
      
    }
    
    
  }
  
  
  /**
   * Draws rotation choices
   * @param graphics
   * @param data
   */
  private void drawRotationChoice(Graphics2D graphics, SimpleGameData data) {
    if (data.getChosenPatch()!=null) {
      graphics.setColor(Color.WHITE);
      graphics.fillRect(xOrigin-4*squareSize-75, 0, 4*squareSize+60, heightWindow);
      graphics.setColor(Color.BLACK);
      graphics.setFont(new Font("Arial", Font.BOLD, 24));
      graphics.drawString("Choose a rotation :", xOrigin-3*squareSize-75, yOrigin/2);
      drawPatch(data.getChosenPatch(), graphics, xOrigin-3*squareSize-75, yOrigin-15);
      drawPatch(data.getTurnPlayer().getBoard().rotateLeft(data.getChosenPatch()), graphics, xOrigin-3*squareSize-75, yOrigin+(7/2)*squareSize-15);
      drawPatch(data.getTurnPlayer().getBoard().rotateDown(data.getChosenPatch()), graphics, xOrigin-3*squareSize-75, yOrigin+7*squareSize-15);
      drawPatch(data.getTurnPlayer().getBoard().rotateRight(data.getChosenPatch()), graphics, xOrigin-3*squareSize-75, yOrigin+(21/2)*squareSize-15);
    }
    
  }
  
  
  /**
   * Returns the patch with the rotation clicked on the screen, returns null if no rotation is selected
   * @param context
   * @param board   The player's board
   * @param p   the patch to rotate
   * @return
   */
  private Patch checkRotationChoiceRange(ApplicationContext context, QuiltBoard board, Patch p) {
    while (true) {
      var event = context.pollEvent();
      
      if (event==null) {
        continue;
      }
      var action = event.getAction();
      if (action.equals(Action.POINTER_DOWN)) {
        var location = event.getLocation();
        if (location.x>=xOrigin-3*squareSize-75 && location.x<=xOrigin-3*squareSize-75+p.width()*30
            && location.y>=yOrigin-15 && location.y<=yOrigin-15+p.height()*30) {
          return p;
        }
        else if (location.x>=xOrigin-3*squareSize-75 && location.x<=xOrigin-3*squareSize-75+board.switchRotation(p, 3).width()*30
            && location.y>=yOrigin-15+(7/2)*squareSize && location.y<=yOrigin-15+board.switchRotation(p, 3).height()*30+(7/2)*squareSize) {
          return board.switchRotation(p, 3);
        }
        else if (location.x>=xOrigin-3*squareSize-75 && location.x<=xOrigin-3*squareSize-75+board.switchRotation(p, 2).width()*30
            && location.y>=yOrigin-15+7*squareSize && location.y<=yOrigin-15+7*squareSize+board.switchRotation(p, 2).height()*30) {
          return board.switchRotation(p, 2);
        }
        else if (location.x>=xOrigin-3*squareSize-75 && location.x<=xOrigin-3*squareSize-75+board.switchRotation(p, 1).width()*30
            && location.y>=yOrigin-15+(21/2)*squareSize && location.y<=yOrigin-15+(21/2)*squareSize+board.switchRotation(p, 1).height()*30) {
          return board.switchRotation(p, 1);
        }
        else {
          return null;
        }
      }
    
    }
    
    
  }
  
  /**
   * Announces the winner by counting the score of each player
   * @param data 
   */
  
  /**
   * Draws a text that announces the winner by counting the score of each player 
   * @param data    The game's data
   * @param graphics
   */
  private void winner(SimpleGameData data, Graphics2D graphics) {
    int score1 = data.getPlayer1().getBoard().getNbButtons() - data.getPlayer1().getBoard().countScoreBlank();
    int score2 = data.getPlayer2().getBoard().getNbButtons() - data.getPlayer2().getBoard().countScoreBlank();
    if (data.getPlayer1().equals(data.getBonusSquare7Owner())) {
      score1 += 7;
    }
    if (data.getPlayer2().equals(data.getBonusSquare7Owner())) {
      score2 += 7;
    }
    graphics.setColor(Color.GREEN);
    graphics.setFont(new Font("Arial", Font.BOLD, 24));
    if (score1 > score2) {
      graphics.drawString("The winner is player1!", xOrigin+12*squareSize, yOrigin+10*squareSize);
    } else if (score1 < score2) {
      graphics.drawString("The winner is player2!", xOrigin+12*squareSize, yOrigin+10*squareSize);
    } else {
      if (data.getFirstFinisher().equals(data.getPlayer1())) {
        graphics.drawString("The winner is player1!", xOrigin+12*squareSize, yOrigin+10*squareSize);
      } else {
        graphics.drawString("The winner is player2!", xOrigin+12*squareSize, yOrigin+10*squareSize);
      }
    }
  }

  @Override
  public void play_game(ApplicationContext context, SimpleGameData data) {
    SimpleGameView.draw(context, data, this);
    boolean madeChoice = false;
    
    int posOtherPlayer = data.getTurnPlayer().equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
    Patch chosenPiece = null;
    while (!madeChoice) {
      int choice = this.getPlayerChoice(context, data);
      if (choice == 1 || choice == 2 || choice == 3) {
        chosenPiece = this.buyPatch(choice, data, context, this);
        SimpleGameView.draw(context, data, this);
        if (chosenPiece != null) {
          madeChoice = true;
        }
      } else {
        moveAfterSkipping(context, data, this);
        SimpleGameView.draw(context, data, this);
        return;
      }
    }
    this.playAfterBuy(context, chosenPiece, posOtherPlayer, data);
    SimpleGameView.draw(context, data, this);
    data.tieWinner();
  }
  
  
  /**
   * Draws all the graphic elements on the window depending on the game's progress
   * @param graphics
   * @param data
   */
  private void draw(Graphics2D graphics, SimpleGameData data) {
    graphics.setColor(Color.WHITE);
    graphics.fill(new Rectangle2D.Float(0, 0, widthWindow, heightWindow));
    graphics.setColor(Color.BLACK);
    drawQuiltBoard(graphics, data);
    drawTimeBoard(graphics);
    
    announcePlayerTurn(data, graphics);
    
    drawPlayer(graphics, data);
    
    
    graphics.setColor(Color.GREEN);
    drawPatch(data.getPatches().get(data.getNTPos()), graphics, xOrigin-3*squareSize-75, yOrigin-15);
    drawPatch(data.getPatches().get(data.getNTPos()+1), graphics, xOrigin-3*squareSize-75, yOrigin+(7/2)*squareSize-15);
    drawPatch(data.getPatches().get(data.getNTPos()+2), graphics, xOrigin-3*squareSize-75, yOrigin+7*squareSize-15);
    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.BOLD, 24));
    graphics.drawString("Select a piece :", xOrigin-4*squareSize-75, yOrigin/2);
    graphics.drawString("Skip your turn", xOrigin+2*squareSize, yOrigin+8*squareSize+80);
    notEnoughButtons(graphics, data);
    drawRotationChoice(graphics, data);
    if (data.endGame()) {
      winner(data, graphics);
    }
  }
  
  /**
   * Draws the time board
   * @param graphics
   */
  private void drawTimeBoard(Graphics2D graphics) {
    graphics.setColor(Color.BLACK);
    for (int i = 0; i < 8; i++) {
      graphics.drawLine(xOrigin, yOrigin+squareSize*i, xOrigin+7*squareSize, yOrigin+squareSize*i);
      graphics.drawLine(xOrigin+squareSize*i, yOrigin, xOrigin+i*squareSize, yOrigin+squareSize*7);
    }
    graphics.drawLine(xOrigin+2*squareSize, yOrigin+squareSize*8, xOrigin+7*squareSize, yOrigin+squareSize*8);
    for (int i = 0; i < 6; i++) {
      
      graphics.drawLine(xOrigin+squareSize*(2+i), yOrigin+squareSize*7, xOrigin+(i+2)*squareSize, yOrigin+squareSize*8);
    }
  }
  
  /**
   * Draws the game board from its data, using an existing
   * {@code ApplicationContext}.
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data.
   * @param view    GameView on which to draw.
   */
  public static void draw(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
    context.renderFrame(graphics -> view.draw(graphics, data)); // do not modify
  }
}
