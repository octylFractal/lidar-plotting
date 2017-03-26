package org.rivierarobotics;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

public class LidarClient {

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        JFrame frame = new JFrame("Lidar Plot");
        LidarPanel lidar = new LidarPanel();
        frame.setJMenuBar(createMenuBar(lidar));
        frame.add(lidar);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private static JMenuBar createMenuBar(LidarPanel lidar) {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        AbstractAction action = new AbstractAction("Save") {

            private static final long serialVersionUID = -2300975976226047809L;
            {
                KeyStroke stroke;
                if (System.getProperty("os.name").toLowerCase()
                        .contains("mac")) {
                    stroke = KeyStroke.getKeyStroke("meta S");
                } else {
                    stroke = KeyStroke.getKeyStroke("control S");
                }
                putValue(ACCELERATOR_KEY, stroke);
            }

            @Override
            public void actionPerformed(ActionEvent event) {
                try (Writer writer =
                        Files.newBufferedWriter(Paths.get("dots.csv"))) {
                    CSV csv = new CSV();
                    csv.setHeader("Angle", "Distance");
                    lidar.getDots().forEach(dot -> {
                        csv.addLine(Arrays.asList(dot.angle(), dot.distance()));
                    });
                    csv.writeTo(writer);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
        save.setAction(action);
        file.add(save);
        bar.add(file);
        return bar;
    }

}
