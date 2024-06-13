import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {

    private Grid grid;

    public GridPanel(Grid grid) {
        this.grid = grid;
        grid.setPanel(this);
        grid.getApple().setPanel(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();
        int width = panelWidth / gridWidth;
        int height = panelHeight / gridHeight;

        canvas.setStroke(new BasicStroke(3));

        for (Snake snake : grid.getSnakes()) {
            // paint head
            int x = snake.getHead().x;
            int y = snake.getHead().y;

            canvas.setColor(snake.getColor());
            canvas.fillRect(x * width, y * height, width, height);
            canvas.setColor(Color.BLACK);
            canvas.drawRect(x * width, y * height, width, height);
            canvas.setColor(Color.WHITE);
            canvas.fillOval(x * width + (width / 4), y * height + (height / 4), width / 2, height / 2);

            for (Point point : snake.getBody()) {
                x = point.x;
                y = point.y;
                canvas.setColor(snake.getColor());
                canvas.fillRect(x * width, y * height, width, height);
                canvas.setColor(Color.BLACK);
                canvas.drawRect(x * width, y * height, width, height);
            }
        }

        // paint apple
        if (grid.getApple().getX() != -1 && grid.getApple().getY() != -1) {
            canvas.setStroke(new BasicStroke(1));
            canvas.setColor(Color.RED);
            canvas.fillOval(grid.getApple().getX() * width, grid.getApple().getY() * height, width, height);
            canvas.setColor(Color.GREEN);
            canvas.drawOval(grid.getApple().getX() * width, grid.getApple().getY() * height, width, height);
        }
    }
}
