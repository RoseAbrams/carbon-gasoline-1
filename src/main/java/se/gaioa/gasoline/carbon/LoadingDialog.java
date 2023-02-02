package se.gaioa.gasoline.carbon;

public class LoadingDialog extends javax.swing.JDialog implements Runnable {

    private int progress = 0;
    private int total = 0;
    private long progressTimeLast = System.currentTimeMillis();
    private final long[] progressTime = new long[100];
    private final Thread updater = new Thread(this, "App: LoadingDialog Updater");

    public LoadingDialog(java.awt.Frame parent) {
        super(parent);
        initComponents();
        updater.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barLoading = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Loading...");
        setModal(true);
        setResizable(false);

        barLoading.setString("unset");
        barLoading.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barLoading, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barLoading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barLoading;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (true) {
            barLoading.setString(progress + " / " + total + " (" + (int) (((float) progress / total) * 100) + "%)");
            int sum = 0;
            for (long l : progressTime) {
                sum += l;
            }
            int remaininS = (int) ((total - progress) * ((float) sum / progressTime.length) * .001);
            setTitle("About " + (remaininS / 60 + 1) + " minutes left");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
//                throw new Error(e);
            }
        }
    }

    public void reportProgress(int i) {
        progress = i;
        barLoading.setValue(progress);
        progressTime[progress % 100] = System.currentTimeMillis() - progressTimeLast;
        progressTimeLast = System.currentTimeMillis();
    }

    public void reportTotal(int i) {
        total = i;
        barLoading.setMaximum(total);
    }

    public void reportDone() {
        dispose();
        updater.interrupt();
    }
}
