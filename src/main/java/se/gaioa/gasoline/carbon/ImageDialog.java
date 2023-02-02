package se.gaioa.gasoline.carbon;

import java.awt.Graphics;
import java.awt.Image;

public class ImageDialog extends javax.swing.JDialog {

    private final Image IMG;

    private ImageDialog(java.awt.Frame parent, Image image) {
        super(parent);
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        IMG = image;
    }

    public ImageDialog(java.awt.Frame parent, TinderUser user) {
        this(parent, user.toGUI());
    }

    public ImageDialog(java.awt.Frame parent, TinderUser user, int nPic) {
        this(parent, user.getPicture(nPic));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(IMG, 0, 0, null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
