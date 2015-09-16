package pilot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;


public class MasterView extends Canvas {

    private static final long serialVersionUID = 1L;

    private JFrame frame;
    private JPanel panel;
    private BufferStrategy bufferStrat;

    private HexMapModel map;
    private HexMapView view;
    private MKLevelController level;

    public MasterView() {
        this.level = new MKLevelController();
        this.map = level.getMapForLevel(1);
        this.frame = new JFrame("Pilot");
        this.panel = (JPanel)this.frame.getContentPane();
    }

    public void activate() {
        Dimension windowSize = new Dimension(1200, 675);
        this.view = new MKHexMapView(map, windowSize, 60, this.level);

        this.setBounds(0, 0, windowSize.width, windowSize.height);

        this.panel.setPreferredSize(windowSize);
        this.panel.setLayout(null);
        this.panel.add(this);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.pack();
        this.frame.setVisible(true);

        this.setIgnoreRepaint(true);
        this.createBufferStrategy(2);
        this.bufferStrat = this.getBufferStrategy();
    }

    public void render() {
        Graphics graphics = this.bufferStrat.getDrawGraphics();
        graphics.drawImage(this.view.getRenderedFrame(), 0, 0, null);
        graphics.dispose();
        this.bufferStrat.show();
    }
}
