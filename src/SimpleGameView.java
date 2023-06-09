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


public record SimpleGameView(int xOrigin, int yOrigin, int height, int width, int squareSize, ImageLoader loader, int widthWindow,
    int heightWindow) implements GameView {
  public static SimpleGameView initGameGraphics(int xOrigin, int yOrigin, int length, SimpleGameData data, ImageLoader loader, int widthWindow, int heightWindow) {
    Objects.requireNonNull(data);
    Objects.requireNonNull(loader);
    var squareSize = length / 7;
    return new SimpleGameView(xOrigin, yOrigin, length, 7 * squareSize, squareSize, loader, widthWindow, heightWindow);
  }
  
  private static void checkRange(double min, double value, double max) {
    if (value < min || value > max) {
        throw new IllegalArgumentException("Invalid coordinate: " + value);
    }
  }
  
  private void drawImage(Graphics2D graphics, BufferedImage image, float x, float y, float dimX, float dimY) {
    var width = image.getWidth();
    var height = image.getHeight();
    var scale = Math.min(dimX / width, dimY / height);
    var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
            y + (dimY - scale * height) / 2);
    graphics.drawImage(image, transform, null);
  }
  
  /*private int getXPlayer(int pos) {
    return 
  }*/
  
  private void changeDirection(Player player) {
    if ((player.getPosition()-5)%7==0) {
      player.switchDirection();
    }
  }
  
  
  public int getPlayerChoice(ApplicationContext context, SimpleGameData data) {//ADD CONDITION ON X AND Y
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
        System.out.println("res===="+res);
        System.out.println("x==="+location.x+" y===="+location.y);
        System.out.println("xmin===="+(xOrigin+2*squareSize)+" xmax==="+(xOrigin+5*squareSize)+" ymin==="+(yOrigin+7*squareSize+120)+
            "ymax===="+(yOrigin+7.5*squareSize+120));
        if (res!=-1) {
          System.out.println("res===="+res);
          return res;
        }
        
      }
    }
    
  }
  
  
  public void drawQuiltBoard(Graphics2D graphics, SimpleGameData data) {
    for (int i = 0; i < 10; i++) {
      graphics.drawLine(xOrigin+8*squareSize, yOrigin+squareSize*i, xOrigin+17*squareSize, yOrigin+squareSize*i);
      graphics.drawLine(xOrigin+squareSize*(8+i), yOrigin, xOrigin+(8+i)*squareSize, yOrigin+squareSize*9);
    }
    if (data.getTurnPlayer().equals(data.getPlayer1())) {
      graphics.setColor(Color.BLUE);
      System.out.println(("aaaa"));
    }
    else if (data.getTurnPlayer().equals(data.getPlayer2())) {
      graphics.setColor(Color.ORANGE);
      System.out.println("bbbb");
    }
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (data.getTurnPlayer().getBoard().getPatchesCollected()[i][j]==1) {
          graphics.fillRect(xOrigin+8*squareSize+j*squareSize, yOrigin+i*squareSize, squareSize, squareSize);
        }
      }
    }
  }
  
  
  private void announcePlayerTurn(SimpleGameData data, Graphics2D graphics) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(xOrigin+2*squareSize, yOrigin-squareSize, 5*squareSize, squareSize);
    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.BOLD, 24));
    if (data.getTurnPlayer().equals(data.getPlayer1())) {
      graphics.drawString("It's player1's turn !", xOrigin+2*squareSize, yOrigin-squareSize);
    }
    else {
      graphics.drawString("It's player2's turn !", xOrigin+2*squareSize, yOrigin-squareSize);
    }
    
  }
  
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
  
  private void drawPlayer(Graphics2D graphics ,SimpleGameData data) {
    graphics.setColor(Color.BLUE);
    if (data.getPlayer1().getPosition()<5) {
      
      graphics.fill(new Rectangle(xOrigin+2*squareSize+(data.getPlayer1().getPosition())*squareSize+
          (squareSize-20)/2+19, yOrigin+7*squareSize+squareSize/2-20/2, 20, 20));
    }
    else if(data.getPlayer1().getPosition()>=5 && ((data.getPlayer1().getPosition()-5)%7)%2==0) {
      graphics.fill(new Rectangle2D.Double(xOrigin+6*squareSize+0.5*squareSize-20-3-((data.getPlayer1().getPosition()-5)%7)*squareSize,
         yOrigin+6*squareSize+0.5*squareSize-20/2-((data.getPlayer1().getPosition()-5)/7)*squareSize , 20, 20));
    }
    else if (data.getPlayer1().getPosition()>=5 && ((data.getPlayer1().getPosition()-5)%7)%2==1) {
      graphics.fill(new Rectangle2D.Double(xOrigin+0.5*squareSize-20-3+((data.getPlayer1().getPosition()-5)%7)*squareSize,
          yOrigin+6*squareSize+0.5*squareSize-20/2-((data.getPlayer1().getPosition()-5)/7)*squareSize, 20, 20));
    }
    graphics.setColor(Color.ORANGE);
    if (data.getPlayer2().getPosition()<5) {
      graphics.fill(new Rectangle(xOrigin+2*squareSize+(data.getPlayer2().getPosition())*squareSize+
          (squareSize-20)/2-18, yOrigin+7*squareSize+squareSize/2-20/2, 20, 20));
    }
    else if(data.getPlayer2().getPosition()>=5 && ((data.getPlayer2().getPosition()-5)%7)%2==0) {
      graphics.fill(new Rectangle2D.Double(xOrigin+6*squareSize+0.5*squareSize+20+3-((data.getPlayer2().getPosition()-5)%7)*squareSize,
          yOrigin+6*squareSize+0.5*squareSize-20/2-((data.getPlayer2().getPosition()-5)/7)*squareSize, 20, 20));
      System.out.println("xbrr==="+(((xOrigin+6*squareSize+0.5*squareSize+20+3-((data.getPlayer2().getPosition()-5)%7)*squareSize)-xOrigin)/squareSize));
    }
    else if (data.getPlayer2().getPosition()>=5 && ((data.getPlayer2().getPosition()-5)%7)%2==1) {
      graphics.fill(new Rectangle2D.Double(xOrigin+0.5*squareSize+20+3+((data.getPlayer2().getPosition()-5)%7)*squareSize,
          yOrigin+6*squareSize+0.5*squareSize-20/2-((data.getPlayer2().getPosition()-5)/7)*squareSize, 20, 20));
      System.out.println("xbrr==="+(((xOrigin+6*squareSize+0.5*squareSize+20+3-((data.getPlayer2().getPosition()-5)%7)*squareSize)-xOrigin)/squareSize));
    }
    
  }
  
  /*public int getPatchWidth(Patch p) {
    
  }
  
  public int getPatchHeight(Patch p) {
    
  }*/
  
  private void drawPatch(Patch patch, Graphics2D graphics, int x, int y) {
    for (int i = 0; i < patch.height(); i++) {
      for (int j = 0; j < patch.width(); j++) {
        if (patch.shape()[i][j]==1) {
          graphics.fillRect(x+j*30, y+i*30, 30, 30);
        }
      }
      
    }
    /*graphics.setColor(Color.BLACK);
    graphics.drawLine(x, y, x+patch.width()*30, y);
    graphics.drawLine(x, y+patch.height()*30, x+patch.width()*30, y+patch.height()*30);
    graphics.drawLine(x, y, x, y+patch.height()*30);
    graphics.drawLine(x+patch.width()*30, y, x+patch.width()*30, y+patch.height()*30);*/
  }
  
  /*private void drawPlayer(Player player) {
    
  }*/
  
  /**
   * Allows the player to play his turn
   */
  
  /*public void play(ApplicationContext context, SimpleGameData data) {
    boolean madeChoice = false;
     
    int posOtherPlayer = data.getTurnPlayer().equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
    Patch chosenPiece = null;
    while (!madeChoice) {
      int choice = getPlayerChoice(context, data);
      if (choice == 1 || choice == 2 || choice == 3) {
        chosenPiece = buyPatch(choice, data);
        if (chosenPiece != null) {
          madeChoice = true;
        }
      } else {
        moveAfterSkipping(context, data);
        return;
      }
    }
    playAfterBuy(context, chosenPiece, posOtherPlayer, data);
  }*/
  
  public void playAfterBuy(ApplicationContext context, Patch chosenPiece, int posOtherPlayer, SimpleGameData data) {
    Objects.requireNonNull(chosenPiece);
    procedureToPutPatch(context, chosenPiece, data, this);
    moveAfterBuying(context, chosenPiece, data, this);
    if (data.getTurnPlayer().getBoard().verifyBonus() && data.getBonusSquare7Owner() == null && data.getMode() == 2) {
      data.setBonusSquare7Owner(data.getTurnPlayer());
      System.out.println("You won the Square 7 * 7 bonus!!!\n");
    }
    if (data.getTurnPlayer().getPosition() > posOtherPlayer) {
      data.switchTurn();
      //SimpleGameView.draw(context, data, view);
    }
  }
  
  /*public int getPlayerChoice(ApplicationContext context, SimpleGameData data) {
    Scanner scanner = new Scanner(System.in);
    int choice = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.println("Which patch do you want to buy?\n\n" + "1- " + data.getPatches().get(data.getNTPos()).toString() + "\n2- "
          + data.getPatches().get(data.getNTPos() + 1).toString());
      System.out.println();
      System.out.println("3- " + data.getPatches().get(data.getNTPos() + 2).toString());
      System.out.println("\n\n");
      System.out.println("Type the number of the patch you want or type another " + "number to skip");
      try {
        choice = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }
    return choice;
  }*/
  
  /**
   * Allows the player to buy a patch
   * 
   * @param choice Number of the chosen piece out of the three
   * @return The chosen piece
   */
  public Patch buyPatch(int choice, SimpleGameData data, ApplicationContext context, SimpleGameView view) {
    Patch chosenPiece;
    if (choice == 1 && data.getPatches().get(data.getNTPos()).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(0);
    } else if (choice == 2 && data.getPatches().get(data.getNTPos() + 1).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(1);
    } else if (choice == 3 && data.getPatches().get(data.getNTPos() + 2).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(2);
    } else {
      //System.out.println("\nYou don't have enough buttons!!!\n");
      data.setNotEnoughBut(true);
      SimpleGameView.draw(context, data, view);
      try {
        Thread.sleep(3000); // 3000 milliseconds = 3 seconds
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      data.setNotEnoughBut(false);
      SimpleGameView.draw(context, data, view);
      //wait 3 seconds then make text dissapear
      //System.out.println("Try again :\n");
      return null;
    }
    data.getTurnPlayer().addPatchOwned(chosenPiece);
    data.getTurnPlayer().setButtons(data.getTurnPlayer().getButtons() - chosenPiece.price());
    return chosenPiece;
  }

  public void notEnoughButtons(Graphics2D graphics, SimpleGameData data) {
    if (data.getNotEnoughBut()) {
      graphics.setColor(Color.RED);
      graphics.setFont(new Font("Arial", Font.BOLD, 24));
      graphics.drawString("You don't have enough buttons!!!", xOrigin-3*squareSize-75, yOrigin+10*squareSize);
      
      
      //SimpleGameView.draw(null, data, null);
    }
    else {
      graphics.setColor(Color.WHITE);
      System.out.println("i will drop you");
      graphics.fillRect(xOrigin-3*squareSize-75, yOrigin+9*squareSize, 3*squareSize, 3*squareSize);
      
    }
  }
  
  /**
   * Moves the player's time token if he skips his turn
   * 
   * @param scanner Read the player's input
   */
  public void moveAfterSkipping(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
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
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + data.getTurnPlayer().getBoard());
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
   * 
   * @param chosenPiece The bought patch
   * @param scanner     Read the player's input
   */
  public void moveAfterBuying(ApplicationContext context, Patch chosenPiece, SimpleGameData data, SimpleGameView view) {
    int j2;
    int posCurrentPlayer = data.getTurnPlayer().getPosition();
    for (j2 = posCurrentPlayer + 1; j2 <= chosenPiece.timeStep() + posCurrentPlayer && j2 < 54; j2++) {
      if (data.getTimeBoard()[j2] == 1) {
        data.updateButtonScore();
      }
      if (data.getTimeBoard()[j2] == 2) {
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + data.getTurnPlayer().getBoard()+"\n\n");
        procedureToPutPatch(context, new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), data, view);
      }
    }
    data.getTurnPlayer().setPosition(j2 - 1);
  }

  
  public void procedureToPutPatch(ApplicationContext context, Patch chosenPiece, SimpleGameData data, SimpleGameView view) {
    int x = 9, y = 9;
    Patch pieceToPut=chosenPiece;
    data.setChosenPatch(pieceToPut);
    do {
      
      
      SimpleGameView.draw(context, data, view);
      
      pieceToPut = chooseRotation(chosenPiece, data, context);
      while (true) {
        //System.out.println("aaaaa");
        var event = context.pollEvent();
        if (event==null) {
          continue;
        }
        var action = event.getAction();
        
        //System.out.println(action);
        if (action!=Action.POINTER_DOWN) {
          continue;
        }
        else {
          var location = event.getLocation();
          if (location.x<xOrigin+8*squareSize  || location.x>xOrigin+17*squareSize  ||  location.y<yOrigin  ||  location.y>yOrigin+squareSize*9 ) {
            continue;
          }
          x = ((int)location.x-xOrigin)/squareSize-8;
          y = ((int)location.y-yOrigin)/squareSize;
          //System.out.println("x="+x+" y="+y+"\n");
          break;
        }
        //System.out.println("xxx="+x+" yyy="+y+"\n");
      }
      //System.out.println("bbbbbb");
      //System.out.println("xxxxx="+x+" yyyyy="+y+"\n");
    } while (!data.getTurnPlayer().getBoard().ableToPut(x, y, pieceToPut));
    //System.out.println("aaaaa\n");
    data.setChosenPatch(null);
    data.getTurnPlayer().getBoard().putPatch(x, y, pieceToPut);
    System.out.println("x="+x+" y="+y+"\n");
    SimpleGameView.draw(context, data, view);
    try {
      Thread.sleep(2000); // 3000 milliseconds = 3 seconds
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    data.getTurnPlayer().getBoard().addButtons(pieceToPut);
  }

  public Patch chooseRotation(Patch p, SimpleGameData data, ApplicationContext context) {
    Patch p2;
    
    while (true) {
      p2=checkRotationChoiceRange(context, data.getTurnPlayer().getBoard(), p);
      if (p2!=null) {
        
        return p2;
      }
      
    }
    
    
  }
  
  public void drawRotationChoice(Graphics2D graphics, SimpleGameData data) {
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
  
  public Patch checkRotationChoiceRange(ApplicationContext context, QuiltBoard board, Patch p) {
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
   * Gets row position typed in by the player
   * 
   * @param scanner
   * @return Row position
   */
  /*public int getInputX(ApplicationContext context) {//get where the player clicks AND CHANGE METHOD NAME
    var event = context.pollEvent();
    var action = event.getAction();
    if (action==Action.POINTER_DOWN) {
      var location = event.getLocation();
      return patchClicked(location.x, location.y, data);
    }*/
    //int x = 0;
    /*boolean validInput = false;
    while (!validInput) {
      System.out.println("Board :\n"+toString()+"\n\n");
      System.out.println("In which position of the quilt do you want to put the patch");
      System.out.println("Write the line number :");
      try {
        x = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }*/
    
    //return x;
  //}

  /**
   * Gets column position typed in by the player
   * 
   * @param scanner
   * @return Column position
   */
  public int getInputY() {
    Scanner scanner = new Scanner(System.in);
    int y = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.println("Write the column number :");
      try {
        y = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }
    return y;
  }

  
  private void draw(Graphics2D graphics, SimpleGameData data) {
    // example
    graphics.setColor(Color.WHITE);
    graphics.fill(new Rectangle2D.Float(0, 0, widthWindow, heightWindow));
    System.out.println(widthWindow);
    graphics.setColor(Color.BLACK);
    drawQuiltBoard(graphics, data);
    drawTimeBoard(graphics);
    /*graphics.setColor(Color.BLUE);
    drawPlayer(graphics, data.getPlayer1());
    graphics.setColor(Color.ORANGE);
    drawPlayer(graphics, data.getPlayer2());*/
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
    /*graphics.setColor(Color.BLACK);
    graphics.fillRect(xOrigin+2*squareSize, yOrigin+7*squareSize+120, 3*squareSize, squareSize/2);*/
    //play(null, data, graphics);
    /*initialise a function that when you call it there is a if(is patch selected)
     * if it's true then we execute drawquiltboard, ispatchselected is true after POINTERDOWN*/
    
    /*graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.BOLD, 24));
    graphics.drawString("Patchwork", width/2, height/4);
    graphics.setFont(new Font("Gill Sans", Font.PLAIN, 12));
    graphics.drawString("1- Play", width/2, height/4+30);
    graphics.drawString("2- Quit", width/2, height/4+40);*/
    
    /*double radius = 350;
    double centerX = xOrigin+(squareSize*8)/2-50; 
    double centerY = yOrigin+(squareSize*8)/2-50; 
    double angleIncrement = Math.PI / 2; 
    System.out.println("xOrigin= "+ xOrigin+" xOrigin+width="+(xOrigin+8*squareSize)+" centerX= "+centerX);
    int numPatches = (int) (2 * Math.PI / angleIncrement); 

    for (int i = 0; i < numPatches; i++) {
        double angle = i * angleIncrement;
        double x = centerX + radius * Math.cos(angle);
        double y = centerY + radius * Math.sin(angle);
        
        drawImage(graphics, loader.image(i), (float)x, (float)y, 100, 100);
        
    }*/
    
    
    
    
  }
  
  private void drawTimeBoard(Graphics2D graphics) {
    graphics.setColor(Color.BLACK);
    for (int i = 0; i < 8; i++) {
      graphics.drawLine(xOrigin, yOrigin+squareSize*i, xOrigin+7*squareSize, yOrigin+squareSize*i);
      graphics.drawLine(xOrigin+squareSize*i, yOrigin, xOrigin+i*squareSize, yOrigin+squareSize*7);
    }
    graphics.drawLine(xOrigin+2*squareSize, yOrigin+squareSize*8, xOrigin+7*squareSize, yOrigin+squareSize*8);
    //graphics.drawLine(xOrigin+2*squareSize, yOrigin+squareSize*7, xOrigin+12*squareSize, yOrigin+squareSize*7);
    for (int i = 0; i < 6; i++) {
      
      graphics.drawLine(xOrigin+squareSize*(2+i), yOrigin+squareSize*7, xOrigin+(i+2)*squareSize, yOrigin+squareSize*8);
    }
  }
  
  
  public static void draw(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
    context.renderFrame(graphics -> view.draw(graphics, data)); // do not modify
  }
}
