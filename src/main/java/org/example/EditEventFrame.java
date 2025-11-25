package org.example;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EditEventFrame extends MyFrame  implements ActionListener {

    JFrame frame;
    JPanel panel;
    JButton saveAppointmentButton;
    JButton saveTaskButton;
    JButton editAppointmentButton;
    JButton editTaskButton;
    JFrame frameAppointment;
    JFrame frameTask;
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
    JTextField deadlineText;
    JLabel deadlineLabel;
    JTextField statusText;
    JLabel statusLabel;
    JFrame errorFrame;
    JLabel errorL1;
    JLabel errorL2;
    JLabel errorL3;
    JLabel errorL4;

    EditEventFrame() {
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

        ArrayList<Object> events = new ArrayList<>(); // Λιστα για τα γεγονοτα του ημερολογιου
        for (Object event : calendar.getComponents()) { // Για ολα τα γεγονοτα του ημερολογιου
            if (event instanceof VEvent) { // Αν ειναι appointment
                VEvent vevent = (VEvent) event;
                events.add(vevent); // Προσθηκη στη λιστα
            } else if (event instanceof VToDo) { // Αν ειναι task
                VToDo vtodo = (VToDo) event;
                events.add(vtodo); // Προσθηκη στη λιστα
            }
        }
        events.sort(Comparator.comparing(myCalendar::getDateTime)); // Ταξινομειται η λιστα με τα γεγονοτα με βαση την ώρα εναρξης ή της προθεσμιας τους που παίρνουμε από τη μέθοδο getDateTime

        if (events.size()==0) { // Αν δεν υπαρχουν γεγονοτα στο ημερολογιο
            JLabel l=new JLabel("There are neither appointments nor tasks in this calendar."); // Νεα ετικετα οτι δεν υπαρχουν ουτε appointment ουτε task στο ημερολογιο
            l.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            panel.add(l); // Προσθηκη της ετικετας στο πανελ
        } else { // Αν υπαρχουν γεγονοτα στο ημερολογιο
            for (Object event : events) { // Για ολα τα στοιχεια της λιστας
                if (event instanceof VEvent) { // Αν ειναι appointment
                    JLabel l1 = new JLabel("Appointment: ");
                    JLabel l2 = new JLabel("Title: " + ((VEvent) event).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του appointment
                    l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l3 = new JLabel("Description: " + ((VEvent) event).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του appointment
                    l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l4 = new JLabel("Start DateTime: " + ((VEvent) event).getStartDate().getValue()); // Νεα ετικετα για την ωρα εναρξης του appointment
                    l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l5 = new JLabel("End DateTime: " + ((VEvent) event).getEndDate().getValue()); // Νεα ετικετα για τη διαρκεια του appointment
                    l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l1); // Προσθηκη ετικετας στο πανελ
                    panel.add(l2); // Προσθηκη ετικετας στο πανελ
                    panel.add(l3); // Προσθηκη ετικετας στο πανελ
                    panel.add(l4); // Προσθηκη ετικετας στο πανελ
                    panel.add(l5); // Προσθηκη ετικετας στο πανελ
                    editAppointmentButton = new JButton("Edit"); // Νεο κουμπι για την αλλαγη των στοιχειων του appointment
                    editAppointmentButton.setSize(200, 25); // Μεγεθος κουμπιου
                    editAppointmentButton.addActionListener(new ActionListener() { // Τι κανει το κουμπι αν πατηθει
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frameAppointment = new JFrame(); // Δημιουργια νεου παραθυρου
                            frameAppointment.setTitle("Calendar"); // Ο τιτλος του παραθυρου
                            frameAppointment.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
                            frameAppointment.setSize(300, 550); // Μεγεθος παραθυρου
                            frameAppointment.setLayout(new FlowLayout()); // Ορισμος διαταξης
                            frameAppointment.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
                            frameAppointment.setVisible(true); // Να φαινεται το καινουριο παραθυρο
                            titleText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τον τιτλο του appointment
                            titleText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            titleLabel = new JLabel("Title: "); // Νεα ετικετα
                            descriptionText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την περιγραφη του appointment
                            descriptionText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            descriptionLabel = new JLabel("Description: "); // Νεα ετικετα
                            dateTimeText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την ωρα εναρξης του appointment
                            dateTimeText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            dateTimeLabel = new JLabel("Date and Time: (yyyy-MM-dd HH:mm:ss)"); // Νεα ετικετα
                            durationDaysText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τις ημερες της διαρκειας του appointment
                            durationDaysText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            durationDaysLabel = new JLabel("Days of Duration:"); // Νεα ετικετα
                            durationHoursText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τις ωρες της διαρκειας του appointment
                            durationHoursText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            durationHoursLabel = new JLabel("Hours of Duration:"); // Νεα ετικετα
                            durationMinutesText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τα λεπτα της διαρκειας του appointment
                            durationMinutesText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            durationMinutesLabel = new JLabel("Minutes of Duration:"); // Νεα ετικετα
                            durationSecondsText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τα δευτερολεπτα της διαρκειας του appointment
                            durationSecondsText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            durationSecondsLabel = new JLabel("Seconds of Duration:"); // Νεα ετικετα
                            saveAppointmentButton = new JButton("Save Changes"); // Νεο κουμπι για την αποθηκευσει των αλλαγων του appointment
                            saveAppointmentButton.setLayout(null); // Η διαταξη του κουμπιου
                            saveAppointmentButton.setSize(40, 40); // Μεγεθος κουμπιου
                            saveAppointmentButton.setVisible(true); // Να φαινεται το κουμπι
                            saveAppointmentButton.addActionListener(new ActionListener() { // Τι θα κανει το κουμπι αν πατηθει
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean flag1 = true;
                                    DateTime userTime = null;
                                    String dateTimeString = dateTimeText.getText(); // Η εισοδος του χρηστη για την ωρα εναρξης του appointment
                                    try {
                                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Το format που θελουμε να εχει η ημερομηνια και η ωρα
                                        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, format); // Ελεγχος για το αν η εισοδος του χρηστη ταιριαζει με το format
                                        userTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant())); // Ελεγχος για το αν η εισοδος του χρηστη ειναι μεσα στα ορια πχ 32 μερες και πανω να βγαζει error
                                    } catch (DateTimeParseException d) { // Αμα ειναι λαθος
                                        flag1 = false;
                                    }
                                    boolean flag2 = true;
                                    Dur duration = null;
                                    int durationDays = -1, durationHours = -1, durationMinutes = -1, durationSeconds = -1;
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
                                        flag2 = false;
                                    } else if (durationHours < 0 || durationHours > 23) { // Αν η εισοδος του χρηστη για τις ωρες της διαρκειας του appointment ειναι αρνητικη ή μεγαλυτερη απο 23
                                        flag2 = false;
                                    } else if (durationMinutes < 0 || durationMinutes > 59) { // Αν η εισοδος του χρηστη για τα λεπτα της διαρκειας του appointment ειναι αρνητικη ή μεγαλυτερη απο 59
                                        flag2 = false;
                                    } else if (durationSeconds < 0 || durationSeconds > 59) { // Αν η εισοδος του χρηστη για τα δευτερολεπτα της διαρκειας του appointment ειναι αρνητικη ή μεγαλυτερη απο 59
                                        flag2 = false;
                                    }
                                    if (durationDays == 0 && durationHours == 0 && durationMinutes == 0 && durationSeconds == 0) { // Αν οι εισοδοι του χρήστη για τις ημερες, τις ωρες, τα λεπτα και τα δευτερολεπτα της διαρκειας του appointment ειναι 0
                                        flag2 = false;
                                    }
                                    if (flag2) {
                                        duration = new Dur(durationDays, durationHours, durationMinutes, durationSeconds); // Αποθηκευση της διαρκειας στη μεταβλητη
                                    }
                                    if (flag1 && flag2) {
                                        ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.SUMMARY)); // Αφαιρεση του προηγουμενου τιτλου του appointment
                                        ((VEvent) event).getProperties().add(new Summary(titleText.getText())); // Κωδικοποιηση του νεου τιτλου για να μπορει να αποθηκευτει σε αρχειο .ics
                                        ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.DESCRIPTION)); // Αφαιρεση της προηγουμενης περιγραφης του appointment
                                        ((VEvent) event).getProperties().add(new Description(descriptionText.getText())); // Κωδικοποιηση της νεας περιγραφης για να μπορει να αποθηκευτει αρχειο .ics
                                        ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.DTSTART)); // Αφαιρεση της προηγουμενης ωρας εναρξης του appointment
                                        ((VEvent) event).getProperties().add(new DtStart(userTime)); // Κωδικοποιηση της νεας ωρας εναρξης για να μπορει να αποθηκευτει σε αρχειο .ics
                                        ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.DURATION)); // Αφαιρεση της προηγουμενης διαρκειας του appointment
                                        ((VEvent) event).getProperties().add(new Duration(duration)); // Κωδικοποιηση της νεας διαρκειας για να μπορει να αποθηκευτει σε αρχειο .ics
                                        ((VEvent) event).getProperties().remove(((VEvent) event).getProperty(Property.UID)); // Αφαιρεση του προηγουμενου μοναδικου αναγνωριστικου του appointment
                                        String uid = UUID.randomUUID().toString(); // Δημιουργια νεου μοναδικου αναγνωριστικου του appointment
                                        ((VEvent) event).getProperties().add(new Uid(uid)); // Κωδικοποιηση του νεου μοναδικου αναγνωριστικου για να μπορει να αποθηκευτει σε αρχειο .ics
                                        frameAppointment.dispose(); // Κλεισιμο του παραθυρου
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
                            });
                            frameAppointment.add(titleLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameAppointment.add(titleText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameAppointment.add(descriptionLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameAppointment.add(descriptionText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameAppointment.add(dateTimeLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameAppointment.add(dateTimeText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameAppointment.add(durationDaysLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameAppointment.add(durationDaysText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameAppointment.add(durationHoursLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameAppointment.add(durationHoursText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameAppointment.add(durationMinutesLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameAppointment.add(durationMinutesText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameAppointment.add(durationSecondsLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameAppointment.add(durationSecondsText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameAppointment.add(saveAppointmentButton); // Προσθηκη του κουμπιου στο παραθυρο
                            frameAppointment.setSize(300, 550); // Μεγεθος παραθυρου
                        }
                    });
                    panel.add(editAppointmentButton); // Προσθηκη του κουμπιου στο πανελ
                } else { // Αν ειναι task
                    JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                    JLabel l2 = new JLabel("Title: " + ((VToDo) event).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                    l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l3 = new JLabel("Description: " + ((VToDo) event).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                    l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l4 = new JLabel("Due: " + ((VToDo) event).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                    l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l5 = new JLabel("Status: " + ((VToDo) event).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                    l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l1); // Προσθηκη ετικετας στο πανελ
                    panel.add(l2); // Προσθηκη ετικετας στο πανελ
                    panel.add(l3); // Προσθηκη ετικετας στο πανελ
                    panel.add(l4); // Προσθηκη ετικετας στο πανελ
                    panel.add(l5); // Προσθηκη ετικετας στο πανελ
                    editTaskButton = new JButton("Edit"); // Νεο κουμπι για την αλλαγη της καταστασης ολοκληρωση των tasks
                    editTaskButton.setSize(200, 25); // Μεγεθος κουμπιου
                    editTaskButton.addActionListener(new ActionListener() { // Τι κανει το κουμπι αν πατηθει
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frameTask = new JFrame(); // Δημιουργια νεου παραθυρου
                            frameTask.setTitle("Calendar"); // Ο τιτλος του παραθυρου
                            frameTask.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
                            frameTask.setSize(600, 350); // Μεγεθος παραθυρου
                            frameTask.setLayout(new FlowLayout()); // Ορισμος διαταξης
                            frameTask.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
                            frameTask.setVisible(true); // Να φαινεται το καινουριο παραθυρο
                            titleText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης τον τιτλο του task
                            titleText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            titleLabel = new JLabel("Title: "); // Νεα ετικετα
                            descriptionText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την περιγραφη του task
                            descriptionText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            descriptionLabel = new JLabel("Description: "); // Νεα ετικετα
                            deadlineText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την προθεσμια του task
                            deadlineText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            deadlineLabel = new JLabel("Deadline: (yyyy-MM-dd HH:mm:ss)"); // Νεα ετικετα
                            statusText = new JTextField(); // Δημιουργια κενου πλαισιου για να γραψει ο χρηστης την κατασταση ολοκληρωσης του task
                            statusText.setPreferredSize(new Dimension(250, 35)); // Μεγεθος πλαισιου
                            statusLabel = new JLabel("Status: (complete/in-process)"); // Νεα ετικετα
                            saveTaskButton = new JButton("Save Changes"); // Νεο κουμπι για την αποθηκευση τον αλλαγων
                            saveTaskButton.setLayout(null); // Η διαταξη του κουμπιου
                            saveTaskButton.setSize(40, 40); // Μεγεθος κουμπιου
                            saveTaskButton.setVisible(true); // Να φαινεται το καινουριο κουμπι
                            saveTaskButton.addActionListener(new ActionListener() {// Τι κανει το κουμπι αν πατηθει
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean flag1 = true;
                                    DateTime userTime = null;
                                    String deadline = deadlineText.getText(); // Η εισοδος του χρηστη για την προθεσμια του task
                                    try {
                                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Το format που θελουμε να εχει η ημερομηνια και η ωρα
                                        LocalDateTime localDateTime = LocalDateTime.parse(deadline, format); // Ελεγχος για το αν η εισοδος του χρηστη ταιριαζει με το format
                                        userTime = new DateTime(java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant())); // Ελεγχος για το αν η εισοδος του χρηστη ειναι μεσα στα ορια πχ 32 μερες και πανω να βγαζει error
                                    } catch (DateTimeParseException d) { // Αμα ειναι λαθος
                                        flag1 = false;
                                    }
                                    boolean flag2 = false;
                                    if ((statusText.getText().equalsIgnoreCase("complete")) || (statusText.getText().equalsIgnoreCase("in-process"))) { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι complete ή in-process
                                        flag2 = true;
                                    }
                                    if (flag1 && flag2) {
                                        ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.SUMMARY)); // Αφαιρεση του προηγουμενου τιτλου του task
                                        ((VToDo) event).getProperties().add(new Summary(titleText.getText())); // Κωδικοποιηση του νεου τιτλου για να μπορει να αποθηκευτει σε αρχειο .ics
                                        ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.DESCRIPTION)); // Αφαιρεση της προηγουμενης περιγραφης του task
                                        ((VToDo) event).getProperties().add(new Description(descriptionText.getText())); // Κωδικοποιηση της νεας περιγραφης για να μπορει να αποθηκευτει σε αρχειο .ics
                                        ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.STATUS)); // Αφαιρεση της προηγουμενης καταστασης ολοκληρωσης του task
                                        if (statusText.getText().equalsIgnoreCase("complete")) { // Αν η εισοδος του χρηστη για τη νεα κατασταση ολοκληρωσης του task ειναι complete
                                            ((VToDo) event).getProperties().add(new Status("COMPLETED")); // Το νεο status του task αποθηκεύεται ως COMPLETED στο αρχείο .ics
                                        } else { // Αν η εισοδος του χρηστη για την κατασταση ολοκληρωσης του task ειναι in-process
                                            ((VToDo) event).getProperties().add(new Status("IN-PROCESS")); // Το νεο status του task αποθηκεύεται ως IN-PROCESS στο αρχείο .ics
                                        }
                                        ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.DUE)); // Αφαιρεση της προηγουμενης προθεσμιας του task
                                        ((VToDo) event).getProperties().add(new Due(userTime)); // Κωδικοποιηση της νεας προθεσμιας για να μπορει να αποθηκευτει σε αρχειο .ics
                                        ((VToDo) event).getProperties().remove(((VToDo) event).getProperty(Property.UID)); // Αφαιρεση του προηγουμενου μοναδικου αναγνωριστικου του task
                                        String uid = UUID.randomUUID().toString(); // Δημιουργια νεου μοναδικου αναγνωριστικου του task
                                        ((VToDo) event).getProperties().add(new Uid(uid)); // Κωδικοποιηση του νεου μοναδικου αναγνωριστικου για να μπορει να αποθηκευτει σε αρχειο .ics
                                        frameTask.dispose(); // Κλεισιμο του παραθυρου
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
                            });
                            frameTask.add(titleLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameTask.add(titleText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameTask.add(descriptionLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameTask.add(descriptionText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameTask.add(deadlineLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameTask.add(deadlineText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameTask.add(statusLabel); // Προσθηκη της ετικετας στο παραθυρο
                            frameTask.add(statusText); // Προσθηκη του πλαισιου στο παραθυρο
                            frameTask.add(saveTaskButton); // Προσθηκη του κουμπιου στο παραθυρο
                            frameTask.setSize(300, 350); // Μεγεθος παραθυρου
                        }
                    });
                    panel.add(editTaskButton); // Προσθηκη του κουμπιου στο πανελ
                }
            }
        }
    }
}