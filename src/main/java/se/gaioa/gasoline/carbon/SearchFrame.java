package se.gaioa.gasoline.carbon;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import se.gaioa.gasoline.carbon.location.LocationSetter;

public class SearchFrame extends javax.swing.JFrame {

    public static final File BASE = new File("F:\\TinderDB");
    public static final File SER = new File("C:\\Users\\Rose\\Downloads\\tinder dataset.ser");
    private TinderUserCollection dataset;

    public SearchFrame() {
        initComponents();
        setVisible(true);
        LocationSetter.initCache();
        if (SER.exists() && JOptionPane.showConfirmDialog(this, "Load from serialized file?",
                "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            dataset = (TinderUserCollection) loadSer(SER);
            JOptionPane.showMessageDialog(this, "Dataset loaded!", "Done", JOptionPane.INFORMATION_MESSAGE);
        } else {
            LoadingDialog ld = new LoadingDialog(this);
            new Thread(() -> {
                try {
                    dataset = new TinderUserCollection(BASE, ld);
                    saveSer(dataset, SER);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }, "App: Dataset Builder").start();
            ld.setVisible(true);
        }
    }

    public static Object loadSer(File from) throws IllegalArgumentException {
        System.out.println("loading serialized file...");
        try ( FileInputStream fis = new FileInputStream(from);  ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object outputO = ois.readObject();
            System.out.println("done");
            return outputO;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Serialized object was of an unknown class", e);
        } catch (InvalidClassException e) {
            throw new IllegalArgumentException("Serialized file was from an older version", e);
        } catch (StreamCorruptedException e) {
            throw new IllegalArgumentException("Serialized file corrupted", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Generic error", e);
        }
    }

    public static void saveSer(Object from, File to) {
        System.out.println("saving serialized file...");
        if (to.exists()) {
            to.delete();
        }
        try ( FileOutputStream fos = new FileOutputStream(to);  ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(from);
            oos.flush();
            fos.flush();
        } catch (InvalidClassException | NotSerializableException e) {
            throw new Error("A non-serializable or erroneous object was encountered while saving", e);
        } catch (IOException e) {
            throw new Error("Generic error", e);
        }
        System.out.println("done");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionVerified = new javax.swing.ButtonGroup();
        optionMatched = new javax.swing.ButtonGroup();
        optionGender = new javax.swing.ButtonGroup();
        optionSwipe = new javax.swing.ButtonGroup();
        optionReason = new javax.swing.ButtonGroup();
        scrollResults = new javax.swing.JScrollPane();
        listResults = new javax.swing.JList<>();
        inputID = new javax.swing.JTextField();
        inputName = new javax.swing.JTextField();
        inputAge = new javax.swing.JTextField();
        inputCity = new javax.swing.JTextField();
        buttonSearch = new javax.swing.JButton();
        buttonView = new javax.swing.JButton();
        buttonExplore = new javax.swing.JButton();
        inputBio = new javax.swing.JTextField();
        groupVerified = new javax.swing.JPanel();
        optionVerifiedAny = new javax.swing.JRadioButton();
        optionVerifiedFalse = new javax.swing.JRadioButton();
        optionVerifiedTrue = new javax.swing.JRadioButton();
        groupGender = new javax.swing.JPanel();
        optionGenderAny = new javax.swing.JRadioButton();
        optionGenderUndef = new javax.swing.JRadioButton();
        optionGenderUnspc = new javax.swing.JRadioButton();
        optionGenderMale = new javax.swing.JRadioButton();
        optionGenderFemale = new javax.swing.JRadioButton();
        optionGenderEnby = new javax.swing.JRadioButton();
        groupMatched = new javax.swing.JPanel();
        optionMatchedAny = new javax.swing.JRadioButton();
        optionMatchedFalse = new javax.swing.JRadioButton();
        optionMatchedTrue = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        groupSwipe = new javax.swing.JPanel();
        optionSwipeAny = new javax.swing.JRadioButton();
        optionSwipeLeft = new javax.swing.JRadioButton();
        optionSwipeRight = new javax.swing.JRadioButton();
        optionSwipeSuper = new javax.swing.JRadioButton();
        optionSwipeUndef = new javax.swing.JRadioButton();
        groupReason = new javax.swing.JPanel();
        optionReasonAny = new javax.swing.JRadioButton();
        optionReasonUndef = new javax.swing.JRadioButton();
        optionReasonUnspc = new javax.swing.JRadioButton();
        optionReasonAppearance = new javax.swing.JRadioButton();
        optionReasonPersonality = new javax.swing.JRadioButton();
        optionReasonBoth = new javax.swing.JRadioButton();
        optionReasonHorny = new javax.swing.JRadioButton();
        optionReasonCurious = new javax.swing.JRadioButton();
        buttonStats = new javax.swing.JButton();
        inputEducation = new javax.swing.JTextField();
        inputWork = new javax.swing.JTextField();
        inputLabel = new javax.swing.JTextField();
        selectExplore = new javax.swing.JComboBox<>();
        buttonCSV = new javax.swing.JButton();
        labelNResults = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CARBON: Tinder Dataset Browser");
        setResizable(false);

        listResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollResults.setViewportView(listResults);

        inputID.setToolTipText("ID");
        inputID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        inputName.setToolTipText("Name");
        inputName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        inputAge.setToolTipText("Age");
        inputAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        inputCity.setToolTipText("City");
        inputCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        buttonSearch.setText("Search");
        buttonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        buttonView.setText("View");
        buttonView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionView(evt);
            }
        });

        buttonExplore.setText("Open:");
        buttonExplore.setToolTipText("");
        buttonExplore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionExplore(evt);
            }
        });

        inputBio.setToolTipText("Bio");
        inputBio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        groupVerified.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupVerified.setToolTipText("Verified");

        optionVerified.add(optionVerifiedAny);
        optionVerifiedAny.setSelected(true);
        optionVerifiedAny.setText("any");

        optionVerified.add(optionVerifiedFalse);
        optionVerifiedFalse.setText("false");

        optionVerified.add(optionVerifiedTrue);
        optionVerifiedTrue.setText("true");

        javax.swing.GroupLayout groupVerifiedLayout = new javax.swing.GroupLayout(groupVerified);
        groupVerified.setLayout(groupVerifiedLayout);
        groupVerifiedLayout.setHorizontalGroup(
            groupVerifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupVerifiedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupVerifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionVerifiedAny)
                    .addComponent(optionVerifiedFalse)
                    .addComponent(optionVerifiedTrue))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupVerifiedLayout.setVerticalGroup(
            groupVerifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupVerifiedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionVerifiedAny)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionVerifiedFalse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionVerifiedTrue)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        groupGender.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupGender.setToolTipText("Gender");

        optionGender.add(optionGenderAny);
        optionGenderAny.setSelected(true);
        optionGenderAny.setText("any");

        optionGender.add(optionGenderUndef);
        optionGenderUndef.setText("undefined");

        optionGender.add(optionGenderUnspc);
        optionGenderUnspc.setText("unspecified");

        optionGender.add(optionGenderMale);
        optionGenderMale.setText("male");

        optionGender.add(optionGenderFemale);
        optionGenderFemale.setText("female");

        optionGender.add(optionGenderEnby);
        optionGenderEnby.setText("enby");
        optionGenderEnby.setToolTipText("");

        javax.swing.GroupLayout groupGenderLayout = new javax.swing.GroupLayout(groupGender);
        groupGender.setLayout(groupGenderLayout);
        groupGenderLayout.setHorizontalGroup(
            groupGenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupGenderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupGenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionGenderAny)
                    .addComponent(optionGenderUndef)
                    .addComponent(optionGenderUnspc)
                    .addComponent(optionGenderMale)
                    .addComponent(optionGenderFemale)
                    .addComponent(optionGenderEnby))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupGenderLayout.setVerticalGroup(
            groupGenderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupGenderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionGenderAny)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionGenderUndef)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionGenderUnspc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionGenderMale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionGenderFemale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionGenderEnby)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        groupMatched.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupMatched.setToolTipText("Matched");

        optionMatched.add(optionMatchedAny);
        optionMatchedAny.setSelected(true);
        optionMatchedAny.setText("any");

        optionMatched.add(optionMatchedFalse);
        optionMatchedFalse.setText("false");

        optionMatched.add(optionMatchedTrue);
        optionMatchedTrue.setText("true");

        javax.swing.GroupLayout groupMatchedLayout = new javax.swing.GroupLayout(groupMatched);
        groupMatched.setLayout(groupMatchedLayout);
        groupMatchedLayout.setHorizontalGroup(
            groupMatchedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupMatchedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupMatchedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionMatchedAny)
                    .addComponent(optionMatchedFalse)
                    .addComponent(optionMatchedTrue))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupMatchedLayout.setVerticalGroup(
            groupMatchedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupMatchedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionMatchedAny)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionMatchedFalse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionMatchedTrue)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        optionMatchedTrue.getAccessibleContext().setAccessibleDescription("");

        jButton1.setText("Display");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionDisplay(evt);
            }
        });

        groupSwipe.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupSwipe.setToolTipText("Swipe");

        optionSwipe.add(optionSwipeAny);
        optionSwipeAny.setSelected(true);
        optionSwipeAny.setText("any");

        optionSwipe.add(optionSwipeLeft);
        optionSwipeLeft.setText("left");

        optionSwipe.add(optionSwipeRight);
        optionSwipeRight.setText("right");

        optionSwipe.add(optionSwipeSuper);
        optionSwipeSuper.setText("super");

        optionSwipe.add(optionSwipeUndef);
        optionSwipeUndef.setText("undefined");

        javax.swing.GroupLayout groupSwipeLayout = new javax.swing.GroupLayout(groupSwipe);
        groupSwipe.setLayout(groupSwipeLayout);
        groupSwipeLayout.setHorizontalGroup(
            groupSwipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupSwipeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupSwipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionSwipeAny)
                    .addComponent(optionSwipeLeft)
                    .addComponent(optionSwipeRight)
                    .addComponent(optionSwipeSuper)
                    .addComponent(optionSwipeUndef))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupSwipeLayout.setVerticalGroup(
            groupSwipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupSwipeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionSwipeAny)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionSwipeUndef)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionSwipeLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionSwipeRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionSwipeSuper)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        groupReason.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupReason.setToolTipText("Reason");

        optionReason.add(optionReasonAny);
        optionReasonAny.setSelected(true);
        optionReasonAny.setText("any");

        optionReason.add(optionReasonUndef);
        optionReasonUndef.setText("undefined");

        optionReason.add(optionReasonUnspc);
        optionReasonUnspc.setText("unspecified");

        optionReason.add(optionReasonAppearance);
        optionReasonAppearance.setText("appearance");

        optionReason.add(optionReasonPersonality);
        optionReasonPersonality.setText("personality");

        optionReason.add(optionReasonBoth);
        optionReasonBoth.setText("both");

        optionReason.add(optionReasonHorny);
        optionReasonHorny.setText("horny");

        optionReason.add(optionReasonCurious);
        optionReasonCurious.setText("curious");

        javax.swing.GroupLayout groupReasonLayout = new javax.swing.GroupLayout(groupReason);
        groupReason.setLayout(groupReasonLayout);
        groupReasonLayout.setHorizontalGroup(
            groupReasonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupReasonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupReasonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionReasonAny)
                    .addComponent(optionReasonUndef)
                    .addComponent(optionReasonUnspc)
                    .addComponent(optionReasonAppearance)
                    .addComponent(optionReasonPersonality)
                    .addComponent(optionReasonBoth)
                    .addComponent(optionReasonHorny)
                    .addComponent(optionReasonCurious))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        groupReasonLayout.setVerticalGroup(
            groupReasonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(groupReasonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(optionReasonAny)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionReasonUndef)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionReasonUnspc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionReasonAppearance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionReasonPersonality)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionReasonBoth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionReasonHorny)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionReasonCurious)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonStats.setText("Print stats");
        buttonStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionStats(evt);
            }
        });

        inputEducation.setToolTipText("Education");
        inputEducation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        inputWork.setToolTipText("Work");
        inputWork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        inputLabel.setToolTipText("Label");
        inputLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionSearch(evt);
            }
        });

        selectExplore.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "folder", "profile", "metadata" }));

        buttonCSV.setText("Print data to file");
        buttonCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionCSV(evt);
            }
        });

        labelNResults.setText("0");
        labelNResults.setToolTipText("number of results");
        labelNResults.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollResults, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonExplore)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectExplore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelNResults))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(inputID, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addComponent(inputName)
                        .addComponent(inputCity)
                        .addComponent(inputAge, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputEducation)
                        .addComponent(inputWork)
                        .addComponent(inputLabel))
                    .addComponent(groupVerified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputBio, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSearch))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonStats)
                        .addGap(18, 18, 18)
                        .addComponent(buttonCSV))
                    .addComponent(groupGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(groupSwipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(groupReason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(groupMatched, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(329, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inputID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(groupVerified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputWork, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputEducation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputBio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(groupGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(groupMatched, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(groupSwipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(groupReason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(scrollResults, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonSearch)
                        .addComponent(buttonStats)
                        .addComponent(buttonCSV))
                    .addComponent(labelNResults, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonView)
                    .addComponent(buttonExplore)
                    .addComponent(jButton1)
                    .addComponent(selectExplore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private static final DefaultListModel<TinderUser> EMPTY_RESULTS = new DefaultListModel<>();

    private void actionSearch(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionSearch
        try {
            TinderUserSearch search = new TinderUserSearch();
            //<editor-fold defaultstate="collapsed" desc="long code">
            if (!inputID.getText().isBlank()) {
                if (isLong(inputID.getText())) {
                    search.requireID(Long.parseLong(inputID.getText()));
                } else {
                    JOptionPane.showMessageDialog(this, "Non-number specified as ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (!inputName.getText().isBlank()) {
                search.requireName(inputName.getText());
            }
            if (!inputAge.getText().isBlank()) {
                if (isInt(inputAge.getText())) {
                    search.requireAge(Byte.parseByte(inputAge.getText()));
                } else {
                    JOptionPane.showMessageDialog(this, "Non-number specified as age.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (!optionVerifiedAny.isSelected()) {
                search.requireVerified(optionVerifiedTrue.isSelected());
            }
            if (!inputWork.getText().isBlank()) {
                search.requireWork(inputWork.getText());
            }
            if (!inputEducation.getText().isBlank()) {
                search.requireEducation(inputEducation.getText());
            }
            if (!inputCity.getText().isBlank()) {
                search.requireCity(inputCity.getText());
            }
            if (!inputLabel.getText().isBlank()) {
                search.requireLabel(inputLabel.getText());
            }
            if (!inputBio.getText().isBlank()) {
                search.requireBio(inputBio.getText());
            }
            if (!optionGenderAny.isSelected()) {
                switch (getSelectedIndex(optionGender)) {
                    case 1 ->
                        search.requireGender(TinderUser.Gender.UNDEFINED);
                    case 2 ->
                        search.requireGender(TinderUser.Gender.UNSPECIFIED);
                    case 3 ->
                        search.requireGender(TinderUser.Gender.MALE);
                    case 4 ->
                        search.requireGender(TinderUser.Gender.FEMALE);
                    case 5 ->
                        search.requireGender(TinderUser.Gender.ENBY);
                    default ->
                        throw new AssertionError();
                }
            }
            if (!optionSwipeAny.isSelected()) {
                switch (getSelectedIndex(optionSwipe)) {
                    case 4 ->
                        search.requireSwipe(TinderUser.SwipeResult.UNDEFINED);
                    case 1 ->
                        search.requireSwipe(TinderUser.SwipeResult.LEFT);
                    case 2 ->
                        search.requireSwipe(TinderUser.SwipeResult.RIGHT);
                    case 3 ->
                        search.requireSwipe(TinderUser.SwipeResult.SUPER);
                    default ->
                        throw new AssertionError();
                }
            }
            if (!optionReasonAny.isSelected()) {
                switch (getSelectedIndex(optionReason)) {
                    case 1 ->
                        search.requireReason(TinderUser.SwipeReason.UNDEFINED);
                    case 2 ->
                        search.requireReason(TinderUser.SwipeReason.UNSPECIFIED);
                    case 3 ->
                        search.requireReason(TinderUser.SwipeReason.APPEARANCE);
                    case 4 ->
                        search.requireReason(TinderUser.SwipeReason.PERSONALITY);
                    case 5 ->
                        search.requireReason(TinderUser.SwipeReason.BOTH);
                    case 6 ->
                        search.requireReason(TinderUser.SwipeReason.HORNY);
                    case 7 ->
                        search.requireReason(TinderUser.SwipeReason.CURIOUS);
                    default ->
                        throw new AssertionError();
                }
            }
            if (!optionMatchedAny.isSelected()) {
                search.requireMatched(optionMatchedTrue.isSelected());
            }
//</editor-fold>
            if (!search.isSet()) {
                JOptionPane.showMessageDialog(this, "No search arguments.", "Error", JOptionPane.ERROR_MESSAGE);
                listResults.setModel(EMPTY_RESULTS);
                labelNResults.setText("0");
                return;
            }
            dataset.search(search);
            if (search.getResults().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "No results.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            if (search.getResults().size() > 100) {
                JOptionPane.showMessageDialog(this, "Over 100 results. Search ignored for memory safety.", "Warning", JOptionPane.WARNING_MESSAGE);
                listResults.setModel(EMPTY_RESULTS);
                labelNResults.setText("0");
                return;
            }
            DefaultListModel<TinderUser> results = new DefaultListModel<>();
            results.addAll(search.getResults());
            listResults.setModel(results);
            labelNResults.setText(String.valueOf(search.getResults().size()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }//GEN-LAST:event_actionSearch

    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static int getSelectedIndex(ButtonGroup bg) {
        Enumeration<AbstractButton> buttonEnm = bg.getElements();
        for (int i = 0; buttonEnm.hasMoreElements(); i++) {
            if (buttonEnm.nextElement().isSelected()) {
                return i;
            }
        }
        throw new UnsupportedOperationException("No selection found.");
    }

    private boolean ensureSelection() {
        if (listResults.getSelectedValue() == null) {
            if (listResults.getModel().getSize() == 1) {
                listResults.setSelectedIndex(0);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Nothing selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    private void actionView(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionView
        if (!ensureSelection()) {
            return;
        }
        new UserTextDialog(this, listResults.getSelectedValue());
    }//GEN-LAST:event_actionView

    private void actionExplore(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionExplore
        if (!ensureSelection()) {
            return;
        }
        File target;
        switch (selectExplore.getSelectedIndex()) {
            case 0 ->
                target = listResults.getSelectedValue().getFileDirectory();
            case 1 ->
                target = listResults.getSelectedValue().getFileProfile();
            case 2 ->
                target = listResults.getSelectedValue().getFileMetadata();
            default ->
                throw new AssertionError();
        }
        try {
            Desktop.getDesktop().open(target);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "An error occured while opening the path.\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_actionExplore

    private void actionDisplay(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionDisplay
        if (!ensureSelection()) {
            return;
        }
        new ImageDialog(this, listResults.getSelectedValue());
    }//GEN-LAST:event_actionDisplay

    private void actionStats(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionStats
        TinderDatasetStats.analyze(dataset);
    }//GEN-LAST:event_actionStats

    private void actionCSV(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionCSV
//        TinderDatasetStats.bioLength(dataset);
        TinderDatasetStats.eduUnique(dataset);
        TinderDatasetStats.cityUnique(dataset);
    }//GEN-LAST:event_actionCSV

    public static void main(String[] args) {
        new SearchFrame();
//        SwingUtilities.invokeLater(SearchFrame::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCSV;
    private javax.swing.JButton buttonExplore;
    private javax.swing.JButton buttonSearch;
    private javax.swing.JButton buttonStats;
    private javax.swing.JButton buttonView;
    private javax.swing.JPanel groupGender;
    private javax.swing.JPanel groupMatched;
    private javax.swing.JPanel groupReason;
    private javax.swing.JPanel groupSwipe;
    private javax.swing.JPanel groupVerified;
    private javax.swing.JTextField inputAge;
    private javax.swing.JTextField inputBio;
    private javax.swing.JTextField inputCity;
    private javax.swing.JTextField inputEducation;
    private javax.swing.JTextField inputID;
    private javax.swing.JTextField inputLabel;
    private javax.swing.JTextField inputName;
    private javax.swing.JTextField inputWork;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel labelNResults;
    private javax.swing.JList<TinderUser> listResults;
    private javax.swing.ButtonGroup optionGender;
    private javax.swing.JRadioButton optionGenderAny;
    private javax.swing.JRadioButton optionGenderEnby;
    private javax.swing.JRadioButton optionGenderFemale;
    private javax.swing.JRadioButton optionGenderMale;
    private javax.swing.JRadioButton optionGenderUndef;
    private javax.swing.JRadioButton optionGenderUnspc;
    private javax.swing.ButtonGroup optionMatched;
    private javax.swing.JRadioButton optionMatchedAny;
    private javax.swing.JRadioButton optionMatchedFalse;
    private javax.swing.JRadioButton optionMatchedTrue;
    private javax.swing.ButtonGroup optionReason;
    private javax.swing.JRadioButton optionReasonAny;
    private javax.swing.JRadioButton optionReasonAppearance;
    private javax.swing.JRadioButton optionReasonBoth;
    private javax.swing.JRadioButton optionReasonCurious;
    private javax.swing.JRadioButton optionReasonHorny;
    private javax.swing.JRadioButton optionReasonPersonality;
    private javax.swing.JRadioButton optionReasonUndef;
    private javax.swing.JRadioButton optionReasonUnspc;
    private javax.swing.ButtonGroup optionSwipe;
    private javax.swing.JRadioButton optionSwipeAny;
    private javax.swing.JRadioButton optionSwipeLeft;
    private javax.swing.JRadioButton optionSwipeRight;
    private javax.swing.JRadioButton optionSwipeSuper;
    private javax.swing.JRadioButton optionSwipeUndef;
    private javax.swing.ButtonGroup optionVerified;
    private javax.swing.JRadioButton optionVerifiedAny;
    private javax.swing.JRadioButton optionVerifiedFalse;
    private javax.swing.JRadioButton optionVerifiedTrue;
    private javax.swing.JScrollPane scrollResults;
    private javax.swing.JComboBox<String> selectExplore;
    // End of variables declaration//GEN-END:variables
}
