package org.example;

import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.UUID;

public class NewAppointmentFrame extends MyFrame implements ActionListener {
    JFrame frame;
    JTextField titleText;
    JLabel titleLabel;
    JTextField descriptionText;
    JLabel descriptionLabel;
    JTextField dateTimeText;
    JLabel dateTimeLabel;
    JTextField durationDaysText;
    JLabel durationDaysLabel;
    JTextField durationHoursText;
    JLabel durationHoursLabel;
    JTextField durationMinutesText;
    JLabel durationMinutesLabel;
    JTextField durationSecondsText;
    JLabel durationSecondsLabel;
    JButton addAppointmentButton;
    JFrame errorFrame;
    JLabel errorL1;
    JLabel errorL2;
    JLabel errorL3;
    JLabel errorL4;

    NewAppointmentFrame() {
        myFrame.setVisible(false); // Να μη φαινεται το αρχικο παραθυρο
        frame = new JFrame(); // Καινουριο παραθυρο
        frame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
        frame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
        frame.setSize(300, 550); // Μεγεθος παραθυρου
        frame.setLayout(new FlowLayout()); // Ορισμος διαταξης
        frame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
        frame.setVisible(true); // Να φαινεται το καινουριο παραθυρο
        titleText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τον τιτλο του appointment
        titleText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
        titleLabel = new JLabel("Title: "); // Νεα ετικετα
        descriptionText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την περιγραφη του appointment
        descriptionText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        descriptionLabel = new JLabel("Description: "); // Νεα ετικετα
        dateTimeText = new JTextField();  // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την ωρα εναρξης του appointment
        dateTimeText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        dateTimeLabel = new JLabel("Date and Time: (yyyy-MM-dd HH:mm:ss)"); // Νεα ετικετα
        durationDaysText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τις ημερες της διαρκειας του appointment
        durationDaysText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        durationDaysLabel = new JLabel("Days of Duration:"); // Νεα ετικετα
        durationHoursText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τις ωρες της διαρκειας του appointment
        durationHoursText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        durationHoursLabel = new JLabel("Hours of Duration:"); // Νεα ετικετα
        durationMinutesText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τα λεπτα της διαρκειας του appointment
        durationMinutesText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        durationMinutesLabel = new JLabel("Minutes of Duration:"); // Νεα ετικετα
        durationSecondsText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τα δευτερολεπτα της διαρκειας του appointment
        durationSecondsText.setPreferredSize(new Dimension(250,35)); // Μεγεθος πλαισιου
        durationSecondsLabel = new JLabel("Seconds of Duration:"); //Νεα ετικετα
        addAppointmentButton = new JButton("Add Appointment"); // Νεο κουμπι για την προσθηκη του appointment
        addAppointmentButton.setLayout(null); // Η διαταξη του κουμπιου
        addAppointmentButton.setSize(40, 40); // Μεγεθος κουμπιου
        addAppointmentButton.setVisible(true); // Να φαινεται το κουμπι
        addAppointmentButton.addActionListener(this); // Τι θα κανει το κουμπι αν πατηθει
        frame.add(titleLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(titleText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(descriptionLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(descriptionText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(dateTimeLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(dateTimeText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(durationDaysLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(durationDaysText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(durationHoursLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(durationHoursText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(durationMinutesLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(durationMinutesText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(durationSecondsLabel); // Προσθηκη ετικετας στο παραθυρο
        frame.add(durationSecondsText); // Προσθηκη πλαισιου στο παραθυρο
        frame.add(addAppointmentButton); // Προσθηκη κουμπιου στο παραθυρο
        frame.pack(); // Ρυθμιση του μεγεθους του παραθυρου για να χωρεσουν οι ετικετες, τα πλαισια και το κουμπι
        frame.setSize(300, 550); // Μεγεθος παραθυρου
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==addAppointmentButton) { // Αν πατηθει το κουμπι
            boolean flag1 = true;
            DateTime userTime = null;
            String dateTimeString =dateTimeText.getText(); // Η εισοδος του χρηστη για την ωρα εναρξης του appointment
            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  // Το format που θελουμε να εχει η ημερομηνια και η ωρα
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, format); // Ελεγχος για το αν η εισοδος του χρηστη ταιριαζει με το format
                userTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant())); // Ελεγχος για το αν η εισοδος του χρηστη ειναι μεσα στα ορια πχ 32 μερες και πανω να βγαζει error
            } catch (DateTimeParseException d) { // Αμα ειναι λαθος
                flag1=false;
            }
            boolean flag2 = true;
            Dur duration = null;
            int durationDays=-1,durationHours=-1,durationMinutes=-1,durationSeconds=-1;
            if (!Objects.equals(durationDaysText.getText(), "")) { // Αν ο χρηστης εδωσε εισοδο για τις ημερες της διαρκειας του appointment
                durationDays = Integer.parseInt(durationDaysText.getText()); // Αποθηκευση της εισοδου του χρηστη στη μεταβλητη
            }
            if (!Objects.equals(durationHoursText.getText(), "")) { // Αν ο χρηστης εδωσε εισοδο για τις ωρες της διαρκειας του appointment
                durationHours = Integer.parseInt(durationHoursText.getText()); // Αποθηκευση της εισοδου του χρηστη στη μεταβλητη
            }
            if (!Objects.equals(durationMinutesText.getText(), "")) { // Αν ο χρηστης εδωσε εισοδο για τα λεπτα της διαρκειας του appointment
                durationMinutes = Integer.parseInt(durationMinutesText.getText()); // Αποθηκευση της εισοδου του χρηστη στη μεταβλητη
            }
            if (!Objects.equals(durationSecondsText.getText(), "")) { // Αν ο χρηστης εδωσε εισοδο για τα δευτερολεπτα της διαρκειας του appointment
                durationSeconds = Integer.parseInt(durationSecondsText.getText()); // Αποθηκευση της εισοδου του χρηστη στη μεταβλητη
            }
            if (durationDays < 0) { // Αν η εισοδος του χρηστη για τις ημερες της διαρκειας του appointment ειναι αρνητικη
                flag2=false;
            } else if (durationHours < 0 || durationHours > 23) { // Αν η εισοδος του χρηστη για τις ωρες της διαρκειας του appointment ειναι αρνητικη ή μεγαλυτερη απο 23
                flag2=false;
            } else if (durationMinutes < 0 || durationMinutes > 59) { // Αν η εισοδος του χρηστη για τα λεπτα της διαρκειας του appointment ειναι αρνητικη ή μεγαλυτερη απο 59
                flag2=false;
            } else if (durationSeconds < 0 || durationSeconds > 59) { // Αν η εισοδος του χρηστη για τα δευτερολεπτα της διαρκειας του appointment ειναι αρνητικη ή μεγαλυτερη απο 59
                flag2=false;
            }
            if (durationDays==0 && durationHours==0 && durationMinutes==0 && durationSeconds==0) { // Αν οι εισοδοι του χρήστη για τις ημερες, τις ωρες, τα λεπτα και τα δευτερολεπτα της διαρκειας του appointment ειναι 0
                flag2=false;
            }
            if (flag2) {
                duration = new Dur(durationDays,durationHours,durationMinutes, durationSeconds); // Αποθηκευση της διαρκειας στη μεταβλητη
            }
            if (flag1 && flag2) {
                VEvent appointment = new VEvent(); // Μεταβλητη τυπου VEvent για την προσθεση
                appointment.getProperties().add(new Summary(titleText.getText())); // Κωδικοποιηση του τιτλου για να μπορει να αποθηκευτει σε αρχειο .ics
                appointment.getProperties().add(new Description(descriptionText.getText())); // Κωδικοποιηση της περιγραφης για να μπορει να αποθηκευτει αρχειο .ics
                appointment.getProperties().add(new DtStart(userTime)); // Κωδικοποιηση της ωρας εναρξης για να μπορει να αποθηκευτει σε αρχειο .ics
                appointment.getProperties().add(new Duration(duration)); // Κωδικοποιηση της διαρκειας για να μπορει να αποθηκευτει σε αρχειο .ics
                String uid = UUID.randomUUID().toString(); // Δημιουργια μοναδικου αναγνωριστικου
                appointment.getProperties().add(new Uid(uid)); // Κωδικοποιηση του μοναδικου αναγνωριστικου για να μπορει να αποθηκευτει σε αρχειο .ics
                calendar.getComponents().add(appointment); // Προθεση του appointment στο ημερολογιο
                frame.dispose(); // Κλεισιμο του παραθυρου
            } else {
                errorFrame = new JFrame(); // Νεο παραθυρο για μηνυμα λαθους
                errorFrame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
                errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Για το κλεισιμο του παραθυρου
                errorFrame.setLayout(new FlowLayout()); // Ορισμος διαταξης
                errorFrame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
                errorFrame.setSize(400,150); // Μεγεθος παραθυρου
                errorFrame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
                errorL1 = new JLabel("Correct Format:"); // Νεα ετικετα για το ποια θα πρεπει να ειναι η εισοδος του χρηστη
                errorL1.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                errorL1.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                errorFrame.add(errorL1); // Προσθηκη της ετικετας στο παραθυρο
                errorL2 = new JLabel("Date and Time: (yyyy-MM-dd HH:mm:ss)"); // Νεα ετικετα
                errorL2.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                errorL2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                errorFrame.add(errorL2); // Προσθηκη της ετικετας στο παραθυρο
                errorL3 = new JLabel("Duration: D>0 0<<23 0<m<59 0<s<59"); // Νεα ετικετα
                errorL3.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                errorL3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                errorFrame.add(errorL3); // Προσθηκη της ετικετας στο παραθυρο
                errorL4 = new JLabel("Duration must be greater than 0."); // Νεα ετικετα
                errorL4.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                errorL4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                errorFrame.add(errorL4); // Προσθηκη της ετικετας στο παραθυρο
                errorFrame.setVisible(true); // Να φαινεται το καινουριο παραθυρο
            }
        }
    }
}