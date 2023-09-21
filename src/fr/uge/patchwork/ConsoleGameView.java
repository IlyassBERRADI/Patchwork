package fr.uge.patchwork;
import java.awt.Graphics2D;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.ApplicationContext;

/**
 * A record representing the command line version of the game
 * 
 * @author Antoine BENOIT
 * @author Ilyass BERRADI
 * @since 07/05/2023
 *
 */

public record ConsoleGameView() implements GameView {
  
  
  
  
  /**
   * Moves the player's time token if he buys a patch
   * @param chosenPiece  The bought patch
   * @param data
   */
  private void moveAfterBuying(Patch chosenPiece, SimpleGameData data) {
    int j2;
    int posCurrentPlayer = data.getTurnPlayer().getPosition();
    for (j2 = posCurrentPlayer + 1; j2 <= chosenPiece.timeStep() + posCurrentPlayer && j2 < 54; j2++) {
      if (data.getTimeBoard()[j2] == 1) {
        data.updateButtonScore();
      }
      if (data.getTimeBoard()[j2] == 2) {
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + data.getTurnPlayer().getBoard()+"\n\n");
        procedureToPutPatch(new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), data);
      }
    }
    data.getTurnPlayer().setPosition(j2 - 1);
  }
  
  

  
  
  /**
   * Gets row position typed in by the player
   * @param data
   * @return Row position
   */
  private int getInputX(SimpleGameData data) {
    Scanner scanner = new Scanner(System.in);
    int x = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.println("Board :\n"+data.getTurnPlayer().getBoard().toString()+"\n\n");
      System.out.println("In which position of the quilt do you want to put the patch");
      System.out.println("Write the line number :");
      try {
        x = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }
    return x;
  }

  
  /**
   * Gets column position typed in by the player
   * @return    Column position
   */
  private int getInputY() {
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

  
  
  
  /**
   * Moves the player's time token if he skips his turn
   * @param data
   */
  private void moveAfterSkipping(SimpleGameData data) {
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
        procedureToPutPatch(new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), data);
        data.getTimeBoard()[i] = 0;
      }
      buttonsAdded++;
    }
    currentPlayer.setPosition(i - 1);
    currentPlayer.setButtons(buttonsAdded + currentPlayer.getButtons());
    data.switchTurn();
    
  }

  /**
   * Allows the player to finish his turn after buying a patch
   * 
   * @param chosenPiece
   * @param posOtherPlayer Position of the opponent
   * @param data The game's data
   */
  private void playAfterBuy(Patch chosenPiece, int posOtherPlayer, SimpleGameData data) {
    Objects.requireNonNull(chosenPiece);
    procedureToPutPatch(chosenPiece, data);
    moveAfterBuying(chosenPiece, data);
    if (data.getTurnPlayer().getBoard().verifyBonus() && data.getBonusSquare7Owner() == null && data.getMode() == 2) {
      data.setBonusSquare7Owner(data.getTurnPlayer());
      System.out.println("You won the Square 7 * 7 bonus!!!\n");
    }
    if (data.getTurnPlayer().getPosition() > posOtherPlayer) {
      data.switchTurn();
    }
  }
  
  
  /**
   * Announces the winner by counting the score of each player
   * @param data The game's data
   */
  public static void winner(SimpleGameData data) {
    int score1 = data.getPlayer1().getBoard().getNbButtons() - data.getPlayer1().getBoard().countScoreBlank();
    int score2 = data.getPlayer2().getBoard().getNbButtons() - data.getPlayer2().getBoard().countScoreBlank();
    if (data.getPlayer1().equals(data.getBonusSquare7Owner())) {
      score1 += 7;
    }
    if (data.getPlayer2().equals(data.getBonusSquare7Owner())) {
      score2 += 7;
    }
    if (score1 > score2) {
      System.out.println("The winner is player1!");
    } else if (score1 < score2) {
      System.out.println("The winner is player2!");
    } else {
      if (data.getFirstFinisher().equals(data.getPlayer1())) {
        System.out.println("The winner is player1!");
      } else {
        System.out.println("The winner is player2!");
      }
    }
  }
  
  @Override
  public void play_game(ApplicationContext context, SimpleGameData data) {
      Objects.requireNonNull(context);
      Objects.requireNonNull(data);
      System.out.println(data);
      announcePlayerTurn(data);
      Scanner scanner = new Scanner(System.in);
      boolean madeChoice = false;
      int posOtherPlayer = data.getTurnPlayer().equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
      Patch chosenPiece = null;
      while (!madeChoice) {
        int choice = getPlayerChoice(scanner, data);
        if (choice == 1 || choice == 2 || choice == 3) {
          chosenPiece = data.buyPatch(choice);
          if (chosenPiece != null) {
            madeChoice = true;
          }
        } else {
          moveAfterSkipping(data);
          return;
        }
      }
      playAfterBuy(chosenPiece, posOtherPlayer, data);
      data.tieWinner();
      
  }
  
  
  
  
  
  /**
   * Allows the player to type in the terminal the rotation he likes to choose 0 :
   * No rotation 1 : Rotation to the right 2 : Rotation upside down 3 : Rotation
   * to the left
   * 
   * @param p       Patch to rotate
   * @param data    Game's data
   * @return Patch after rotation
   */
  private Patch chooseRotation(Patch p, SimpleGameData data) {
    Scanner scanner = new Scanner(System.in);
    boolean validInput = false;
    int i = 0;
    while (!validInput) {
      System.out.println("Choose a rotation for this patch\n0: none\n"+p.printShape()+"\n\n1: right\n"+data.getTurnPlayer().getBoard().switchRotation(p, 1).printShape()+
          "\n\n2: down\n"+data.getTurnPlayer().getBoard().switchRotation(p, 2).printShape()+"\n\n3: left\n"+data.getTurnPlayer().getBoard().switchRotation(p, 3).printShape());
      try {
        i = scanner.nextInt();
        if (i!=0 && i!=1 && i!=2 && i!=3) {
          System.out.println("Invalid input !!!\n");
          continue;
        }
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter an number.");
        scanner.next();
      }
    }

    return data.getTurnPlayer().getBoard().switchRotation(p, i);
  }
  
  
  /**
   * Puts the patch in a certain position chosen by the player
   * 
   * @param chosenPiece The piece the player wants to put
   * @param data        The game's data
   */
  private void procedureToPutPatch(Patch chosenPiece, SimpleGameData data) {
    int x = 0, y = 0;
    Patch pieceToPut;
    do {
      x = getInputX(data);
      y = getInputY();
      if (data.getMode() == 2) {
        pieceToPut = chooseRotation(chosenPiece, data);
      } else {
        pieceToPut = chosenPiece;
      }
    } while (!data.getTurnPlayer().getBoard().ableToPut(x, y, pieceToPut));
    data.getTurnPlayer().getBoard().putPatch(x, y, pieceToPut);
    data.getTurnPlayer().getBoard().addButtons(pieceToPut);
  }
  
  
  /**
   * Gets the player's choice out of the three patches in front of the neutral
   * time token
   * 
   * @param scanner Reads input of the current player
   * @param data
   * @return The choice made by the player
   */
  private int getPlayerChoice(Scanner scanner, SimpleGameData data) {
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
  }
  
  
  
  /**
   * Announces the player who has the turn to play
   * @param data
   */
  
  private void announcePlayerTurn(SimpleGameData data) {
    System.out.println("===========\n");
    if (data.getTurnPlayer().equals(data.getPlayer1())) {
      System.out.println("Its player1 turn\n");
    } else {
      System.out.println("Its player2 turn\n");
    }
    System.out.println("===========");
  }
}
