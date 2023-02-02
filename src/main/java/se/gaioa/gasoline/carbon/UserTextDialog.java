package se.gaioa.gasoline.carbon;

public class UserTextDialog extends javax.swing.JDialog {

    public UserTextDialog(java.awt.Frame parent, TinderUser user) {
        super(parent);
        initComponents();
        textProfile.setText(user.toProfileText());
        textMetadata.setText(user.toMetadataText());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollTextProfile = new javax.swing.JScrollPane();
        textProfile = new javax.swing.JTextArea();
        scrollTextMetadata = new javax.swing.JScrollPane();
        textMetadata = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        scrollTextProfile.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        textProfile.setEditable(false);
        textProfile.setColumns(20);
        textProfile.setLineWrap(true);
        textProfile.setRows(5);
        textProfile.setWrapStyleWord(true);
        scrollTextProfile.setViewportView(textProfile);

        scrollTextMetadata.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        textMetadata.setEditable(false);
        textMetadata.setColumns(20);
        textMetadata.setLineWrap(true);
        textMetadata.setRows(5);
        textMetadata.setWrapStyleWord(true);
        scrollTextMetadata.setViewportView(textMetadata);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTextProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTextMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollTextMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(scrollTextProfile))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollTextMetadata;
    private javax.swing.JScrollPane scrollTextProfile;
    private javax.swing.JTextArea textMetadata;
    private javax.swing.JTextArea textProfile;
    // End of variables declaration//GEN-END:variables
}
