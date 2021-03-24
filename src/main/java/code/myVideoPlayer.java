package code;

import code.videoEncoder;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class myVideoPlayer {
    JFrame frame;
    EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

    public void analyzeFrame(videoEncoder video){
        JButton rewind = new JButton("rewind");
        JButton forward = new JButton("forward");
        JButton pause = new JButton("play/pause");
        JPanel controls = new JPanel();
        controls.add(rewind);
        controls.add(pause);
        controls.add(forward);
        frame = new JFrame("My First Media Player");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
            }
        });
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        frame.add(controls, BorderLayout.SOUTH);
        mediaPlayerComponent.mediaPlayer().media().play(video.source1.toString());
        rewind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.mediaPlayer().controls().skipTime(-10000);
            }
        });
        forward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.mediaPlayer().controls().skipTime(10000);
            }
        });
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.mediaPlayer().controls().pause();
            }
        });
    }
}