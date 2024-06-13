import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private int[][] grid;
    private int rows;
    private int cols;
    private List<Snake> snakes;
    private Random random;
    private JPanel panel;
    private Apple apple;

    public Grid(int rows, int cols, double appleDropRate) {
        this.rows = rows;
        this.cols = cols;
        grid = new int[rows][cols];
        snakes = new ArrayList<>();
        random = new Random();

        apple = new Apple(this, appleDropRate);
        Thread thread = new Thread(apple);
        thread.start();
    }

    public int[][] getGrid(){
        return grid;
    }

    public Apple getApple(){
        return apple;
    }

    /**
     * Note:
     * Position X is determined between range 0 and total columns
     * Position Y is determined between range 0 and total rows
     */
    public synchronized Snake createSnake() {
        Snake snake = new Snake(this);

        snakes.add(snake);
        snake.setDirection(Snake.getDirection(random.nextInt(4)));

        // create snake head
        int snakeHeadX;
        int snakeHeadY;
        do {
            snakeHeadX = random.nextInt(cols);
            snakeHeadY = random.nextInt(rows);
        } while (grid[snakeHeadY][snakeHeadX] != 0);
        grid[snakeHeadY][snakeHeadX] = snake.getId();
        snake.getHead().setLocation(snakeHeadX, snakeHeadY);

        // create snake body
        int bodyLength = snake.getLength() - 1;
        int nextX = snakeHeadX, nextY = snakeHeadY;

        char direction = Snake.DIRECTION_RIGHT;
        while (bodyLength != 0) {
            if (direction == Snake.DIRECTION_RIGHT) {
                if (nextX == cols - 1 || grid[nextY][nextX + 1] != 0) {
                    direction = Snake.DIRECTION_DOWN;
                } else {
                    nextX++;
                    grid[nextY][nextX] = snake.getId();
                    snake.addBody(new Point(nextX, nextY));
                    bodyLength--;
                }
            } else if (direction == Snake.DIRECTION_LEFT) {
                if (nextX == 0 || grid[nextY][nextX - 1] != 0) {
                    direction = Snake.DIRECTION_UP;
                } else {
                    nextX--;
                    grid[nextY][nextX] = snake.getId();
                    snake.addBody(new Point(nextX, nextY));
                    bodyLength--;
                }
            } else if (direction == Snake.DIRECTION_DOWN) {
                if (nextY == rows - 1 || grid[nextY + 1][nextX] != 0) {
                    direction = Snake.DIRECTION_LEFT;
                } else {
                    nextY++;
                    grid[nextY][nextX] = snake.getId();
                    snake.addBody(new Point(nextX, nextY));
                    bodyLength--;
                }
            } else {
                if (nextY == 0 || grid[nextY - 1][nextX] != 0) {
                    direction = Snake.DIRECTION_RIGHT;
                } else {
                    nextY--;
                    grid[nextY][nextX] = snake.getId();
                    snake.addBody(new Point(nextX, nextY));
                    bodyLength--;
                }
            }
        }

        Thread thread = new Thread(snake);
        thread.start();

        panel.repaint();

        return snake;
    }

    public void setPanel(JPanel panel){
        this.panel = panel;
    }

    public synchronized void setGridCell(Point point, int value) {
        setGridCell(point.x, point.y, value);
    }

    public synchronized void setGridCell(int x, int y, int value) {
        grid[y][x] = value;
        panel.repaint();
    }

    public synchronized void updateGrid(Snake snake) {
        setGridCell(snake.getHead(), snake.getId());
        for (int i = 0; i < snake.getBody().size(); i++) {
            setGridCell(snake.getBody().get(i), snake.getId());
        }
    }

    public List<Snake> getSnakes(){
        return snakes;
    }

    public int getWidth(){
        return cols;
    }

    public int getHeight(){
        return rows;
    }

    public synchronized boolean moveSnake(Snake snake, char direction) {
        // snake's current head position
        int nextX = snake.getHead().x;
        int nextY = snake.getHead().y;

        // check boundary conditions
        if (direction == Snake.DIRECTION_RIGHT && nextX == cols - 1) return false;
        if (direction == Snake.DIRECTION_LEFT && nextX == 0) return false;
        if (direction == Snake.DIRECTION_DOWN && nextY == rows - 1) return false;
        if (direction == Snake.DIRECTION_UP && nextY == 0) return false;

        int nextCellValue;
        int x, y;
        if (direction == Snake.DIRECTION_RIGHT) {
            x = nextX + 1;
            y = nextY;
        } else if (direction == Snake.DIRECTION_LEFT) {
            x = nextX - 1;
            y = nextY;
        } else if (direction == Snake.DIRECTION_DOWN) {
            x = nextX;
            y = nextY + 1;
        } else {
            x = nextX;
            y = nextY - 1;
        }

        nextCellValue = grid[y][x];

        if (nextCellValue == 0) {
            if (x == apple.getX() && y == apple.getY()) {
                snake.increaseLength();
                apple.setX(-1);
                apple.setY(-1);
            }
            return true;
        }
        else {
            if (nextCellValue != snake.getId()) {
                Snake victimSnake = getSnakeAtCell(x, y);
                if (victimSnake == null) return false;
                victimSnake.decreaseLength();

                if (victimSnake.getLength() == 1){
                    setGridCell(victimSnake.getHead(), 0);
                    snakes.remove(victimSnake);
                }
            }
            return false;
        }
    }

    public Snake getSnakeAtCell(int x, int y) {
        for (int i = 0; i < snakes.size(); i++)
            if (snakes.get(i).getId() == grid[y][x])
                return snakes.get(i);
        return null;
    }

    @Override
    public String toString() {
        String view = "";
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++)
                view += grid[r][c] + "\t";
            view += "\n";
        }
        return view;
    }
}
