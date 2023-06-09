
/**
 * A record representing a patch with the number of buttons it contains, its
 * price, the number of steps to take on the time board, its height, its width
 * and its shape
 * 
 * @author Antoine BENOIT
 * @author Ilyass BERRADI
 * @since 07/05/2023
 *
 */
public record Patch(int buttons, int price, int timeStep, int height, int width, int[][] shape) {

  /**
   * Returns a string representation of the shape of the patch
   * 
   * @return String representation
   */
  public String printShape() {
    var builder = new StringBuilder();
    for (int i = 0; i < height; i++) {
      builder.append("   ");
      for (int j = 0; j < width; j++) {
        if (shape[i][j] == 1) {
          builder.append("X");
        } else {
          builder.append("_");
        }
        builder.append(" ");
      }
      if (i < height - 1) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return "Buttons : " + buttons + " / Price : " + price + " / TimeSteps : " + timeStep + "\n" + printShape();
  }
}
