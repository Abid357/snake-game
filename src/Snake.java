import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Snake implements Runnable {

    public static final char DIRECTION_UP = 'U';
    public static final char DIRECTION_DOWN = 'D';
    public static final char DIRECTION_RIGHT = 'R';
    public static final char DIRECTION_LEFT = 'L';
    public static int CURRENT_SNAKE_ID = 1;
    private int id;
    private int length;
    private Color color;
    private char direction;
    private Point head;
    private List<Point> body;
    private Grid grid;
    private Random random;
    private static List<Color> colors;

    public Snake(Grid grid) {
        this.grid = grid;
        random = new Random();

        id = CURRENT_SNAKE_ID++;
        length = random.nextInt(6) + 3;
        head = new Point();
        body = new LinkedList<>();

        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.ORANGE);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.DARK_GRAY);
        colors.add(Color.PINK);
        colors.add(Color.MAGENTA);

        color = colors.get(random.nextInt(10));
    }

    public void addBody(Point point) {
        body.add(point);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getHead() {
        return head;
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public char getDirection() {
        return direction;
    }

    public static char getDirection(int value) {
        switch (value) {
            case 0:
                return DIRECTION_UP;
            case 1:
                return DIRECTION_DOWN;
            case 2:
                return DIRECTION_RIGHT;
            case 3:
                return DIRECTION_LEFT;
            default:
                return ' ';
        }
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        String data = "Snake{" +
                "id=" + id +
                ", length=" + length +
                ", color=" + color +
                ", direction=" + direction +
                '}';
        data += "\n";
        data += "[" + head.x + "," + head.y + "]-";
        for (int i = 0; i < body.size(); i++) {
            data += "(" + body.get(i).x + "," + body.get(i).y + ")";
            if (i != body.size() - 1)
                data += "-";
        }
        return data;
    }

    @Override
    public void run() {
        while(true) {
            try {
                char direction = getDirection(random.nextInt(4));
                boolean permission = grid.moveSnake(this, direction);
//                System.out.println("direction=" + direction + " this.direction=" + this.direction);
                if (permission) {
                    int oldHeadX = head.x;
                    int oldHeadY = head.y;

                    // update head
                    switch (direction) {
                        case DIRECTION_RIGHT:
                            head.x++;
                            break;
                        case DIRECTION_LEFT:
                            head.x--;
                            break;
                        case DIRECTION_UP:
                            head.y--;
                            break;
                        case DIRECTION_DOWN:
                            head.y++;
                            break;
                    }

                    // update body
                    int oldBodyX = 0;
                    int oldBodyY = 0;
                    if (!body.isEmpty()) {
                        oldBodyX = body.get(0).x;
                        oldBodyY = body.get(0).y;
                        body.get(0).x = oldHeadX;
                        body.get(0).y = oldHeadY;
                    }

                    for (int i = 1; i < body.size(); i++) {
                        int currentBodyX = body.get(i).x;
                        int currentBodyY = body.get(i).y;
                        body.get(i).x = oldBodyX;
                        body.get(i).y = oldBodyY;
                        oldBodyX = currentBodyX;
                        oldBodyY = currentBodyY;
                    }

                    this.direction = direction;

                    grid.updateGrid(this);
                    grid.setGridCell(oldBodyX, oldBodyY, 0);

//                    System.out.println(grid);

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void decreaseLength(){
        grid.setGridCell(body.remove(body.size() - 1), 0);
        length--;
    }

    public void increaseLength(){
        if (!body.isEmpty()) {
            int x = body.get(body.size() - 1).x;
            int y = body.get(body.size() - 1).y;

            body.add(new Point(x, y));
            grid.setGridCell(body.get(body.size() - 1), id);
            length++;
        }
    }

    public List<Point> getBody() {
        return body;
    }
}
