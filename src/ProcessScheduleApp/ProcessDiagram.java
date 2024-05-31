package ProcessScheduleApp;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class ProcessDiagram extends JPanel {
    private final int CANVAS_WIDTH = 1250;
    private final int CANVAS_HEIGHT = 700;
    private final int Process_Size = 50;

    private final List<long[]> processes = new ArrayList<>();
    private final Map<long[], String> map = new HashMap<>();
    private long over;
    public ProcessDiagram() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    public void addProcess(long start, long end, String name, long over) {
        long[] interval = new long[]{start, end};
        processes.add(interval);
        map.put(interval, name);
        this.over = over;
        repaint();
    }

    public void addProcess(long over) {
        this.over = over;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        for (int i = 1; i < CANVAS_WIDTH; i++) {
            g2d.draw(new Line2D.Double(i, 0, i, CANVAS_HEIGHT));
        }
        long cur = 0;
        for(int i = 0; i < processes.size(); i++){
            if(processes.get(i)[0] > cur){
                g2d.setColor(Color.black);
                g2d.draw(new Line2D.Double(cur, (double) CANVAS_HEIGHT /2, processes.get(i)[0]-1, (double) CANVAS_HEIGHT /2));
            }
            Random random = new Random(map.get(processes.get(i)).hashCode());
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            g2d.setColor(new Color(red,green,blue));
            Rectangle2D rect = new Rectangle2D.Double(processes.get(i)[0], (double)(CANVAS_HEIGHT/2 - Process_Size/2), (processes.get(i)[1] - processes.get(i)[0]), Process_Size);
            g2d.fill(rect);
            g2d.setColor(Color.black);
            g2d.draw(rect);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(map.get(processes.get(i)), (float)(processes.get(i)[1]), (int)(CANVAS_HEIGHT/2 - Process_Size/2) - 10);
            cur = processes.get(i)[1];
        }
        if(over > cur){
            g2d.setColor(Color.black);
            g2d.draw(new Line2D.Double(cur, (double) CANVAS_HEIGHT /2, over, (double) CANVAS_HEIGHT /2));
        }
    }
}