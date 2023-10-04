import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {  // 패널에 배경 넣기
    private Image img;

    public ImagePanel(Image img) {
        this.img = img;
        setSize(new Dimension(img.getWidth(null), img.getHeight(null)));
        setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 3, 0, null);
    }
}
