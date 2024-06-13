import javax.swing.*;
import java.util.Random;

public class Apple implements Runnable {

    private int x;
    private int y;
    private Grid grid;
    private double dropRate;
    private Random random;
    private JPanel panel;

    public Apple(Grid grid, double dropRate){
        this.grid = grid;
        this.dropRate = dropRate;
        random = new Random();
        x = -1;
        y = -1;
    }

    @Override
    public void run() {
        while(true) {
            if (x == -1 && y == -1) {
                int randomX = random.nextInt(grid.getHeight());
                int randomY = random.nextInt(grid.getWidth());

                if (grid.getGrid()[randomY][randomX] == 0){
                    x = randomX;
                    y = randomY;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            panel.repaint();
        }
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public double getDropRate() {
        return dropRate;
    }

    public void setDropRate(double dropRate) {
        this.dropRate = dropRate;
    }
}
