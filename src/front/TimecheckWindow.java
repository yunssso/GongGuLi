package front;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

public class TimecheckWindow extends JFrame {
    public TimecheckWindow() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("채팅방이 닫힌 시간: " + LocalDateTime.now());
                System.exit(0);
            }
        });
    }
}
