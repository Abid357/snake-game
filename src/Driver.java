import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Driver {

    public static final double APPLE_DROP_RATE = 0.25;
    public static final int GRID_ROWS = 30;
    public static final int GRID_COLUMNS = 30;

    public static void main(String[] args){
        Grid grid = new Grid(GRID_ROWS, GRID_COLUMNS, APPLE_DROP_RATE);

        JPanel mainPanel = new JPanel();
        JPanel gridPanel = new GridPanel(grid);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(new Dimension((int) width / 2, (int) height / 2));
            frame.setContentPane(mainPanel);

            JButton createSnakeButton = new JButton("Add Snake");
            createSnakeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    grid.createSnake();
                }
            });

            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(gridPanel, BorderLayout.CENTER);
            mainPanel.add(createSnakeButton, BorderLayout.NORTH);

            frame.setVisible(true);
        });
    }
}
