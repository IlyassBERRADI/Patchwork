import java.util.InputMismatchException;
import java.util.Objects;

import fr.umlv.zen5.KeyboardKey;
import java.util.Scanner;

/**
 * A class representing a quilt board where a player can place his patches
 * 
 * @author Antoine BENOIT
 * @author Ilyass BERRADI
 * @since 07/05/2023
 *
 */
public class QuiltBoard {

  /**
   * The array that represents the quilt board content
   */
  private final int[][] patchsCollected = new int[9][9];

  /**
   * The score of buttons accumulated in the quilt board
   */
  private int nbButtons = 0;

  /**
   * Constructs a quilt board by initializing the patchsCollected attribute's
   * elements to zero
   */
  public QuiltBoard() {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        patchsCollected[i][j] = 0;
      }
    }
  }

  /**
   * Returns a string representation of a row of the quilt board
   * 
   * @param builder
   * @return StringBuilder
   */
  private StringBuilder buildLine(StringBuilder builder) {
    for (int i = 0; i < 9; i++) {
      var separator = "";
      builder.append(i);
      builder.append(" ");
      for (int j = 0; j < 9; j++) {
        if (patchsCollected[i][j] == 0) {
          builder.append(separator).append("_");
        } else {
          builder.append(separator).append("X");
        }

        separator = " ";
      }
      builder.append("\n");
    }
    return builder;
  }
  
  
  /**
   * Returns the patches in the quiltboard
   * @return array of int values
   */
  public int[][] getPatchesCollected(){
    return patchsCollected;
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append("  ");
    for (int i = 0; i < 9; i++) {
      builder.append(i);
      builder.append(" ");
    }
    builder.append("\n");
    builder = buildLine(builder);
    builder.append("  ");
    builder.append(nbButtons);
    builder.append(" buttons on the board\n");
    builder.append("Total score : "+ (nbButtons-countScoreBlank()));
    return builder.toString();
  }

  /**
   * Puts a certain patch on the quilt board
   * 
   * @param x line position
   * @param y column position
   * @param p patch to put
   */
  public void putPatch(int x, int y, Patch p) {
    Objects.requireNonNull(p);
    for (int i = x; i < x + p.height(); i++) {
      for (int j = y; j < y + p.width(); j++) {
        patchsCollected[i][j] = patchsCollected[i][j] + p.shape()[i - x][j - y];
      }
    }
  }

  /**
   * Verifies if it's possible to put the patch in a certain position on the quilt
   * board
   * 
   * @param x Line position
   * @param y Column position
   * @param p Patch to put
   * @return boolean
   */
  public boolean ableToPut(int x, int y, Patch p) {
    Objects.requireNonNull(p);
    for (int i = x; i < x + p.height(); i++) {
      for (int j = y; j < y + p.width(); j++) {
        if ((8 < i || 8 < j) || (patchsCollected[i][j] == 1 && p.shape()[i - x][j - y] == 1)) {
          System.out.println("Illegal position!!!\n");
          System.out.println("Try again\n");
          return false;
        }
      }
    }
    return true;
  }

  
  /**
   * Returns the number of buttons collected in the quiltboard
   * 
   * @return number of buttons
   */
  public int getNbButtons() {
    return nbButtons;
  }
  
  
  /**
   * Switches to the rotation chosen by the player
   * 
   * @param p patch to rotate
   * @param r number of rotation
   * @return rotated patch
   */
  public Patch switchRotation(Patch p, int r) {
    Objects.requireNonNull(p);
    switch (r) {
    case 1:
      return rotateRight(p);
    case 2:
      return rotateDown(p);
    case 3:
      return rotateLeft(p);
    }
    ;
    return p;
  }

  /**
   * Rotates patch to the right
   * 
   * @param p Patch to rotate
   * @return Patch after rotation
   */
  public Patch rotateRight(Patch p) {
    Objects.requireNonNull(p);
    int[][] shape = new int[p.width()][p.height()];
    for (int i = 0; i < p.width(); i++) {
      for (int j = 0; j < p.height(); j++) {
        shape[i][j] = p.shape()[p.height() - j - 1][i];
      }
    }
    return new Patch(p.buttons(), p.price(), p.timeStep(), p.width(), p.height(), shape);
  }

  /**
   * Rotates patch to the left
   * 
   * @param p Patch to rotate
   * @return Patch after rotation
   */
  public Patch rotateLeft(Patch p) {
    Objects.requireNonNull(p);
    return rotateRight(rotateRight(rotateRight(p)));
  }

  /**
   * Rotates patch upside down
   * 
   * @param p Patch to rotate
   * @return Patch after rotation
   */
  public Patch rotateDown(Patch p) {
    Objects.requireNonNull(p);
    return rotateRight(rotateRight(p));
  }

  /**
   * Adds the patch buttons to the total score of the quilt board
   * 
   * @param patch Patch to add its buttons
   */
  public void addButtons(Patch patch) {
    Objects.requireNonNull(patch);
    nbButtons += patch.buttons();
  }

  /**
   * Counts the empty spaces in the quilt board
   * 
   * @return The total result to subtract from the final score
   */
  public int countScoreBlank() {
    int count = 0;
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (patchsCollected[i][j] == 0) {
          count += 2;
        }
      }
    }
    return count;
  }

  /**
   * Verifies if a player completed a 7x7 square on his quilt board
   * 
   * @return boolean
   */
  public boolean verifyBonus() {
    for (int i = 0; i <= 2; i++) {
      for (int j = 0; j <= 2; j++) {
        boolean hasSquare = true;
        for (int x = i; x <= i + 2; x++) {
          for (int y = j; y <= j + 2; y++) {
            if (x < 7 && y < 7 && patchsCollected[x][y] != 1) {
              hasSquare = false;
              break;
            }
          }
          if (!hasSquare) {
            return false;
          }
        }
        if (hasSquare) {
          return true;
        }
      }
    }
    return false;
  }
}
