package org.example;

import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.component.VToDo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class NewTaskFrame extends MyFrame implements ActionListener {
    JFrame frame;
    JTextField titleText;
    JLabel titleLabel;
    JTextField descriptionText;
    JLabel descriptionLabel;
    JTextField deadlineText;
    JLabel deadlineLabel;
    JTextField statusText;
    JLabel statusLabel;
    JButton addTaskButton;
    JFrame errorFrame;
    JLabel errorL1;
    JLabel errorL2;
    JLabel errorL3;
    NewTaskFrame() {
        myFrame.setVisible(false); // Να μη φαινεται το αρχικο παραθυρο
        frame = new JFrame(); // Καινουριο παραθυρο
        frame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
        frame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
        frame.setSize(600, 350); // Μεγεθος παραθυρου
        frame.setLayout(new FlowLayout()); // Ορισμος διαταξης
        frame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
        frame.setVisible(true); // Να φαινεται το καινουριο παραθυρο
        titleText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τον τιτλο του task
        titleText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
        titleLabel = new JLabel("Title: "); // Νεα ετικετα
        descriptionText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την περιγραφη του task
        descriptionText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        descriptionLabel = new JLabel("Description: "); // Νεα ετικετα
        deadlineText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την προθεσμια του task
        deadlineText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        deadlineLabel = new JLabel("Deadline: (yyyy-MM-dd HH:mm:ss)"); // Νεα ετικετα
        statusText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την κατασταση ολοκληρωσης του task
        statusText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        statusLabel = new JLabel("Status: (complete/in-process)"); // Νεα ετικετα
        addTaskButton = new JButton("Add Task"); // Νεο κουμπι για την προσθηκη του task
        addTaskButton.setLayout(null); // Η διαταξη του κουμπιου
        addTaskButton.setSize(40, 40); // Μεγεθος κουμπιου
        addTaskButton.setVisible(true); // Να φαινεται το κουμπι
        addTaskButton.addActionListener(this); // Τι θα κανει το κουμπι αν πατηθει
        frame.add(titleLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(titleText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(descriptionLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(descriptionText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(deadlineLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(deadlineText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(statusLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(statusText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(addTaskButton); // Προσθηκη κουμπιου στο παραθυρο
        frame.pack(); // Ρυθμιση του μεγεθους του παραθυρου για να χωρεσουν οι ετικετες, τα πλαισια και το κουμπι
        frame.setSize(300, 350); // Μεγεθος παραθυρου
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==addTaskButton) { // Αν πατηθει το κουμπι
            boolean flag1 = true;
            DateTime userTime = null;
            String deadline = deadlineText.getText(); // Η εισοδος του χρηστη για την προθεσμια του task
            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Το format που θελουμε να εχει η ημερομηνια και η ωρα
                LocalDateTime localDateTime = LocalDateTime.parse(deadline, format); // Ελεγχος για το αν η εισοδος του χρηστη ταιριαζει με το format
                userTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant())); // Ελεγχος για το αν η εισοδος του χρηστη ειναι μεσα στα ορια πχ 32 μερες και πανω να βγαζει error
            } catch (DateTimeParseException d) { // Αμα ειναι λαθος
                flag1=false;
            }
            boolean flag2 = false;
            if ((statusText.getText().equalsIgnoreCase("complete")) || (statusText.getText().equalsIgnoreCase("in-process"))) { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι complete ή in-process
                flag2 = true;
            }
            if (flag1 && flag2) {
                VToDo task = new VToDo(); // Μεταβλητη τυπου VToDo για την προσθεση
                task.getProperties().add(new Summary(titleText.getText())); // Κωδικοποιηση του τιτλου για να μπορει να αποθηκευτει σε αρχειο .ics
                task.getProperties().add(new Description(descriptionText.getText())); // Κωδικοποιηση της περιγραφης για να μπορει να αποθηκευτει σε αρχειο .ics
                if (statusText.getText().equalsIgnoreCase("complete")) { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι complete
                    task.getProperties().add(new Status("COMPLETED")); // Το status του task αποθηκεύεται ως COMPLETED στο αρχείο .ics
                } else { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι in-process
                    task.getProperties().add(new Status("IN-PROCESS")); // Το status του task αποθηκεύεται ως IN-PROCESS στο αρχείο .ics
                }
                task.getProperties().add(new Due(userTime)); // Κωδικοποιηση της προθεσμιας για να μπορει να αποθηκευτει σε αρχειο .ics
                String uid = UUID.randomUUID().toString(); // Δημιουργια μοναδικου αναγνωριστικου
                task.getProperties().add(new Uid(uid)); // Κωδικοποιηση του μοναδικου αναγνωριστικου για να μπορει να αποθηκευτει σε αρχειο .ics
                calendar.getComponents().add(task); // Προσθεση του task στο ημερολογιο
                frame.dispose(); // Κλεισιμο του παραθυρου
            } else {
                errorFrame = new JFrame(); // Νεο παραθυρο για μηνυμα λαθους
                errorFrame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
                errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Για το κλεισιμο του παραθυρου
                errorFrame.setLayout(new FlowLayout()); // Ορισμος διαταξης
                errorFrame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
                errorFrame.setSize(400,100); // Μεγεθος παραθυρου
                errorFrame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
                errorL1 = new JLabel("Correct Format:"); // Νεα ετικετα για το ποια θα πρεπει να ειναι η εισοδος του χρηστη
                errorL1.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                errorL1.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                errorFrame.add(errorL1); // Προσθηκη της ετικετας στο παραθυρο
                errorL2 = new JLabel("Deadline: (yyyy-MM-dd HH:mm:ss)"); // Νεα ετικετα
                errorL2.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                errorL2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                errorFrame.add(errorL2); // Προσθηκη της ετικετας στο παραθυρο
                errorL3 = new JLabel("Status: (complete/in-process)"); // Νεα ετικετα
                errorL3.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                errorL3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                errorFrame.add(errorL3); // Προσθηκη της ετικετας στο παραθυρο
                errorFrame.setVisible(true); // Να φαινεται το καινουριο παραθυρο
            }
        }
    }
}