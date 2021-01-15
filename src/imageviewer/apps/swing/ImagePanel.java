package imageviewer.apps.swing;

import imageviewer.view.ImageDisplay;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel implements ImageDisplay{
    
    private String current;
    private String future;
    private BufferedImage image;
    private BufferedImage futureImage;
    private int offset = 0;
    private Shift shift = new Shift.Null();
    
    public ImagePanel() {
        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(image, offset, 0, null);
        if (offset == 0) return;
        int x = offset > 0 ? -(futureImage.getWidth()-offset) : offset+image.getWidth();
        g.drawImage(futureImage, x, 0, null);
    }

    private void reset() {
        offset=0;
        repaint();
    }
    
    private void toggle() {
        current = future;
        image = futureImage;
        offset = 0;
        repaint();
    }
    
    private void setOffset (int offset){
        this.offset = offset;
        if (offset < 0) setFuture(shift.right());
        if (offset > 0) setFuture(shift.left());
        repaint();
    }

    @Override
    public void display(String name) {
        this.current = name;
        this.image = load(name);
        repaint();
    }
    
    public void setFuture(String name) {
        if (name.equals(future)) return;
        this.future = name;
        this.futureImage = load(name);
    }
    
    private BufferedImage load(String name) {
        try {
            return ImageIO.read(new File(name));
        } catch (Exception ex) {
            System.out.println("Can't load " + name);
            return null;
        }
    }

    @Override
    public void on(Shift shift) {
        this.shift = shift;
    }
    
    @Override
    public String current() {
        return this.current;
    }
    
    private class MouseHandler implements MouseListener, MouseMotionListener{

        private int initial;
        
        @Override
        public void mouseClicked(MouseEvent event) {
        }

        @Override
        public void mousePressed(MouseEvent event) {
            this.initial = event.getX();
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if (Math.abs(offset) > getWidth() / 2)
                toggle();
            else
                reset();
        }

        @Override
        public void mouseEntered(MouseEvent event) {
        }

        @Override
        public void mouseExited(MouseEvent event) {
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            setOffset(event.getX() - initial);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
        }

    }
}