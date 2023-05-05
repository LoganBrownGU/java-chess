package userlayers;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CastlingDialogueMenu {

    private final Object lock;
    private final Frame frame;
    private boolean finished = false;
    private boolean confirm = false;

    private void notifyLock() {
        finished = true;
        synchronized (lock) {
            lock.notify();
        }

        frame.dispose();
    }

    public CastlingDialogueMenu(int x, int y, Object lock) {
        this.lock = lock;

        frame = new Frame();
        frame.setSize(300, 100);
        frame.setLocation(x, y);

        Panel panel = new Panel();
        panel.setBounds(0, 0, 300,100);
        frame.add(panel);

        Label label = new Label("would you like to castle?");
        label.setLocation(0, 0);
        panel.add(label);

        Button yes = new Button("yes");
        yes.setBounds(0, 20, 150, 20);
        yes.addActionListener(e -> {
            confirm = true;
            notifyLock();
        });

        Button no = new Button("no");
        no.setBounds(150, 20, 150, 20);
        no.addActionListener(e -> {
            confirm = false;
            notifyLock();
        });

        panel.add(yes);
        panel.add(no);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirm = false;
                notifyLock();
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean getConfirm() {
        return confirm;
    }
}
