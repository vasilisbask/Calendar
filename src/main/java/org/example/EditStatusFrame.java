package org.example;

import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

public class EditStatusFrame extends MyFrame  implements ActionListener {

    JFrame frame;
    JPanel panel;
    JButton editButton;
    JFrame frameTask;
    JTextField statusText;
    JLabel statusLabel;
    JButton editTaskButton;
    JFrame errorFrame;
    JLabel errorL1;
    JLabel errorL2;

    EditStatusFrame() {
        myFrame.setVisible(false); // Να μη φαινεται το αρχικο παραθυρο
        frame = new JFrame(); // Καινουριο παραθυρο
        frame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Για το κλεισιμο του παραθυρου
        frame.setLayout(new BorderLayout()); // Ορισμος διαταξης
        frame.setSize(700, 400); // Μεγεθος παραθυρου
        frame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
        frame.setResizable(true); // Να επιτρεπεται η αλλαγη μεγεθους
        frame.setVisible(true); // Να φαινεται το καινουριο παραθυρο
        panel = new JPanel(); // Δημιουργια πανελ
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Διαταξη πανελ
        panel.setBackground(Color.white); // Χρωμα του πανελ
        JScrollPane scrollPane = new JScrollPane(panel); // Νεα μπαρα κυλισης
        scrollPane.setPreferredSize(new Dimension(800, 400)); // Μεγεθος της μπαρας κυλισης
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Εμφανιση της μπαρας κυλισης
        scrollPane.setPreferredSize(new Dimension(panel.getPreferredSize().width + 10, 400)); // Μέγεθος της μπαρας κυλισης σε σχεση με το μεγεθος του πανελ
        frame.add(scrollPane, BorderLayout.CENTER); // Προσθηκη της μπαρας κυλισης στο παραθυρο

        ArrayList<VToDo> tasks = new ArrayList<>(); // Λιστα για τα task του ημερολογιου
        for (Object component : calendar.getComponents()) { // Για ολα τα γεγονοτα του ημερολογιου
            if (component instanceof VToDo) { // Αν ειναι task

                VToDo vtodo = (VToDo) component;
                tasks.add(vtodo); // Προσθηκη στη λιστα
            }
        }
        tasks.sort(Comparator.comparing(myCalendar::getDateTime)); // Ταξινομείται η λιστα με βαση την προθεσμια των tasks

        if (tasks.size()==0) { // Αν δεν υπαρχουν tasks
            JLabel l=new JLabel("There are not tasks in this calendar."); // Νεα ετικετα
            l.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            panel.add(l); // Προσθηκη ετικετας στο πανελ
        } else { // Αν υπαρχουν tasks
            for (VToDo task : tasks) { // Για καθε task
                JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                JLabel l2 = new JLabel("Title: " + ((VToDo) task).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                JLabel l3 = new JLabel("Description: " + ((VToDo) task).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                JLabel l4 = new JLabel("Due: " + ((VToDo) task).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                JLabel l5 = new JLabel("Status: " + ((VToDo) task).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                panel.add(l1); // Προσθηκη ετικετας στο πανελ
                panel.add(l2); // Προσθηκη ετικετας στο πανελ
                panel.add(l3); // Προσθηκη ετικετας στο πανελ
                panel.add(l4); // Προσθηκη ετικετας στο πανελ
                panel.add(l5); // Προσθηκη ετικετας στο πανελ
                editButton = new JButton("Edit"); // Νεο κουμπι για την αλλαγη της καταστασης ολοκληρωση των tasks
                editButton.setSize(200, 25); // Μεγεθος κουμπιου
                editButton.addActionListener(new ActionListener() { // Τι κανει το κουμπι αν πατηθει
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frameTask = new JFrame(); // Δημιουργια νεου παραθυρου
                        frameTask.setTitle("Calendar"); // Ο τιτλος του παραθυρου
                        frameTask.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
                        frameTask.setSize(300, 150); // Μεγεθος παραθυρου
                        frameTask.setLayout(new FlowLayout()); // Ορισμος διαταξης
                        frameTask.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
                        frameTask.setVisible(true); // Να φαινεται το καινουριο παραθυρο
                        statusText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης
                        statusText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                        statusLabel = new JLabel("Status: (complete/in-process)"); // Νεα ετικετα
                        editTaskButton = new JButton("Save Changes"); // Νεο κουμπι για την αποθηκευση τον αλλαγων
                        editTaskButton.setLayout(null); // Η διαταξη του κουμπιου
                        editTaskButton.setSize(40, 40); // Μεγεθος κουμπιου
                        editTaskButton.setVisible(true); // Να φαινεται το καινουριο κουμπι
                        editTaskButton.addActionListener(new ActionListener() { // Τι κανει το κουμπι αν πατηθει
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                boolean flag = false;
                                if ((statusText.getText().equalsIgnoreCase("complete")) || (statusText.getText().equalsIgnoreCase("in-process"))) { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι complete ή in-process
                                    flag = true;
                                }
                                if (flag) {
                                    ((VToDo) task).getProperties().remove(((VToDo) task).getProperty(Property.STATUS)); // Αφαιρεση της προηγουμενης καταστασης ολοκληρωσης του task
                                    if (statusText.getText().equalsIgnoreCase("complete")) { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι complete
                                        ((VToDo) task).getProperties().add(new Status("COMPLETED")); // Προσθηκη της νεας καταστασης ολοκληρωσης του task
                                    } else { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι in-process
                                        ((VToDo) task).getProperties().add(new Status("IN-PROCESS")); // Προσθηκη της νεας καταστασης ολοκληρωσης του task
                                    }
                                    frameTask.dispose(); // Κλεισιμο του παραθυρου
                                } else {
                                    errorFrame = new JFrame(); // Νεο παραθυρο για μηνυμα λαθους
                                    errorFrame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
                                    errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Για το κλεισιμο του παραθυρου
                                    errorFrame.setLayout(new FlowLayout()); // Ορισμος διαταξης
                                    errorFrame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
                                    errorFrame.setSize(400, 100); // Μεγεθος παραθυρου
                                    errorFrame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
                                    errorL1 = new JLabel("Correct Format:"); // Νεα ετικετα για το ποια θα πρεπει να ειναι η εισοδος του χρηστη
                                    errorL1.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                                    errorL1.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                    errorFrame.add(errorL1); // Προσθηκη της ετικετας στο παραθυρο
                                    errorL2 = new JLabel("Status: (complete/in-process)"); // Νεα ετικετα
                                    errorL2.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                                    errorL2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                    errorFrame.add(errorL2); // Προσθηκη της ετικετας στο παραθυρο
                                    errorFrame.setVisible(true); // Να φαινεται το καινουριο παραθυρο
                                }
                            }
                        });
                        frameTask.add(statusLabel); // Προσθηκη της ετικετας στο παραθυρο
                        frameTask.add(statusText); // Προσθηκη του πλαισιου στο παραθυρο
                        frameTask.add(editTaskButton); // Προσθηκη του κουμπιου στο παραθυρο
                        frameTask.setSize(300, 150); // Μεγεθος παραθυρου
                    }
                });
                panel.add(editButton); // Προσθηκη του κουμπιου στο πανελ
            }
        }
    }
}