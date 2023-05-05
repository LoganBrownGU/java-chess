package userlayers;

import pieces.PieceType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PromotionDialogueMenu {

    private final Object lock;
    private String piece = null;
    private final Choice choice;
    private final Frame frame;

    ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            piece = choice.getItem(choice.getSelectedIndex());
            synchronized (lock) {
                piece = null;
                lock.notify();
            }
            frame.dispose();

            System.out.println(piece);
        }
    };

    public PromotionDialogueMenu(int x, int y, Object lock) {
        frame = new Frame("promotion");
        this.lock = lock;
        frame.setSize(300, 100);
        frame.setLocation(x, y);

        Panel panel = new Panel();
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(panel);

        choice = new Choice();
        for (PieceType type : PieceType.values())
            choice.add(type.toString().toLowerCase());
        choice.setBounds(0, 40, 100, 20);
        panel.add(choice);

        Button confirm = new Button("confirm");
        confirm.addActionListener(buttonListener);
        confirm.setBounds(0, 60, 100, 20);
        panel.add(confirm);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (lock) {
                    piece = null;
                    lock.notify();
                }
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }

    public String getPiece() {
        return piece;
    }
}
