package front;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

public class TimecheckWindow extends JFrame {
    private static LocalDateTime exitTime;

    public TimecheckWindow(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitTime = LocalDateTime.now();
                System.out.println("채팅방이 닫힌 시간: " + exitTime);
                dispose(); // 창을 닫을 때만 종료하도록 수정
            }
        });
    }

    public static LocalDateTime getExitTime() {
        return exitTime;
    }
}
