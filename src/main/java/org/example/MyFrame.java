package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import java.util.Comparator;
import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;
import gr.hua.dit.oop2.calendar.TimeListener;
import gr.hua.dit.oop2.calendar.TimeEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyFrame extends JFrame implements ActionListener {

        static Calendar calendar=myCalendar.newCalendar(); // Δημιουργια νεου ημερολογιου μεσω της μεθοδου newCalendar
        JFrame myFrame;
        JLabel l1;
        JLabel l2;
        JLabel l3;
        JLabel l4;
        JLabel l5;
        JButton newAppointment;
        JButton newTask;
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenu editMenu;
        JMenu showMenu;
        JMenuItem loadItem;
        JMenuItem saveItem;
        JMenuItem exitItem;
        JMenuItem eventItem;
        JMenuItem statusItem;
        JMenuItem dayItem;
        JMenuItem weekItem;
        JMenuItem monthItem;
        JMenuItem pastdayItem;
        JMenuItem pastweekItem;
        JMenuItem pastmonthItem;
        JMenuItem todoItem;
        JMenuItem dueItem;
        VEvent[] sortedAppointments;
        VToDo[] sortedTasks;

        MyFrame() {
            myFrame=new JFrame(); // Καινουριο παραθυρο
            myFrame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Για το κλεισιμο του παραθυρου
            myFrame.setSize(400,400); // Μεγεθος παραθυρου
            myFrame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
            myFrame.setLayout(new FlowLayout()); // Ορισμος διαταξης Πηραμε βοηθεια απο αυτο το link: https://www.c-sharpcorner.com/article/layouts-in-java/
            myFrame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους

            l1=new JLabel("Welcome to calendar!"); // Νεα ετικετα
            l1.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
            l1.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            myFrame.add(l1); // Προσθηκη ετικετας στο παραθυρο

            l2=new JLabel("Click on file on top left to save or load a calendar."); // Νεα ετικετα
            l2.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
            l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            myFrame.add(l2); // Προσθηκη ετικετας στο παραθυρο

            l3=new JLabel("Click on edit on top left to edit an event or the statue of a task."); // Νεα ετικετα
            l3.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
            l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            myFrame.add(l3); // Προσθηκη ετικετας στο παραθυρο

            l4=new JLabel("Click on show on top left to see the events of the calendar."); // Νεα ετικετα
            l4.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
            l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            myFrame.add(l4); // Προσθηκη ετικετας στο παραθυρο

            newAppointment = new JButton("New Appointment"); // Νεο κουμπι για την προσθεση appointment
            newAppointment.setBounds(300,100,100,50); // Συντεταγμενες και μεγεθος κουμπιου
            newAppointment.setVisible(true); // Να φαινεται το κουμπι
            newAppointment.addActionListener(this); // Τι θα κανει το κουμπι αν πατηθει
            myFrame.add(newAppointment); // Προσθηκη κουμπιου στο παραθυρο
            newTask = new JButton("New Task"); // Νεο κουμπι για την προσθεση task
            newTask.setBounds(400,100,100,50); // Συντεταγμενες και μεγεθος κουμπιου
            newTask.setVisible(true); // Να φαινεται το κουμπι
            newTask.addActionListener(this); // Τι θα κανει το κουμπι αν πατηθει
            myFrame.add(newTask); // Προσθηκη κουμπιου στο παραθυρο

            l5=new JLabel(); // Νεα ετικετα
            TimeTeller teller=TimeService.getTeller();
            teller.addTimeListener(new TimeListener() { // Καθε φορα που αλλαζει η ωρα εκετελειται αυτη η μεθοδο
                @Override
                public void timeChanged(TimeEvent e) {
                    l5.setText("Time: " + e.getDateTime()); // Εμφανιση της ωρας
                }
            });
            TimeService.waitUntilTimeEnds();
            myFrame.add(l5); // Προσθηκη ετικετας στο παραθυρο

            LocalDateTime Now = teller.now(); // Η ωρα του συστηματος αυτη τη στιγμη
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Το format που θελουμε να εχει η ωρα
            String time = Now.format(formatter); // Μετατροπη της ωρας στη μορφη του format
            String[] parts = time.split(":"); // Χωρισμος της ωρα με βαση το συμβολο :
            int minutes = Integer.parseInt(parts[1]); // Τα λεπτα της ωρας
            if (minutes == 0 || minutes == 15 || minutes == 30 || minutes == 45) { // Αν τα λεπτα της ωρας ειναι 0, 15, 30 ή 45
                ArrayList<Object> events = new ArrayList<>();
                for (Object event : calendar.getComponents()) { // Για κεθε γεγονος που υπαρχει μεσα στο ημερολογιο
                    if (event instanceof VEvent) { // Αν το γεγονος ειναι appointment
                        VEvent vevent = (VEvent) event;
                        events.add(vevent); // Προσθηκη στη λιστα
                    } else if (event instanceof VToDo) { // Αν το γεγονος ειναι task
                        VToDo vtodo = (VToDo) event;
                        events.add(vtodo); // Προσθηκη στη λιστα
                    }
                }
                events.sort(Comparator.comparing(myCalendar::getDateTime)); // Ταξινομειται η λιστα με βαση την ωρα εναρξης ή την προθεσμια που παιρνουμε απο τη μεθοδο getDateTime
                myCalendar.notifications(events); // Καλειται η μεθοδος για τις ειδοποιησεις
            }

            menuBar=new JMenuBar(); // Δημιουργια γραμμης μενου

            fileMenu= new JMenu("File"); // Δημιουργια μενου File
            editMenu=new JMenu("Edit"); // Δημιουργια μενου Edit
            showMenu=new JMenu("Show"); // Δημιουργια μενου Show

            loadItem=new JMenuItem("Load"); // Δημιουργια της επιλογης μενου Load
            saveItem=new JMenuItem("Save"); // Δημιουργια της επιλογης μενου Save
            exitItem=new JMenuItem("Exit"); // Δημιουργια της επιλογης μενου Exit

            loadItem.addActionListener(this); // Τι θα κανει αν πατηθει
            saveItem.addActionListener(this); // Τι θα κανει αν πατηθει
            exitItem.addActionListener(this); // Τι θα κανει αν πατηθει

            eventItem=new JMenuItem("Events"); // Δημιουργια της επιλογης μενου Events
            statusItem=new JMenuItem("Status"); // Δημιουργια της επιλογης μενου Status

            eventItem.addActionListener(this); // Τι θα κανει αν πατηθει
            statusItem.addActionListener(this); // Τι θα κανει αν πατηθει

            dayItem=new JMenuItem("Day"); // Δημιουργια της επιλογης μενου Day
            weekItem=new JMenuItem("Week"); // Δημιουργια της επιλογης μενου Week
            monthItem=new JMenuItem("Month"); // Δημιουργια της επιλογης μενου Month
            pastdayItem=new JMenuItem("Past Day"); // Δημιουργια της επιλογης μενου Past Day
            pastweekItem=new JMenuItem("Past Week"); // Δημιουργια της επιλογης μενου Past Week
            pastmonthItem=new JMenuItem("Past Month"); // Δημιουργια της επιλογης μενου Past Month
            todoItem=new JMenuItem("To Do"); // Δημιουργια της επιλογης μενου To Do
            dueItem=new JMenuItem("Due"); // Δημιουργια της επιλογης μενου Due

            dayItem.addActionListener(this); // Τι θα κανει αν πατηθει
            weekItem.addActionListener(this); // Τι θα κανει αν πατηθει
            monthItem.addActionListener(this); // Τι θα κανει αν πατηθει
            pastdayItem.addActionListener(this); // Τι θα κανει αν πατηθει
            pastweekItem.addActionListener(this); // Τι θα κανει αν πατηθει
            pastmonthItem.addActionListener(this); // Τι θα κανει αν πατηθει
            todoItem.addActionListener(this); // Τι θα κανει αν πατηθει
            dueItem.addActionListener(this); // Τι θα κανει αν πατηθει

            fileMenu.add(loadItem); // Προσθηκη της επιλογης μενου Load στο μενου File
            fileMenu.add(saveItem); // Προσθηκη της επιλογης μενου Save στο μενου File
            fileMenu.add(exitItem); // Προσθηκη της επιλογης μενου Exit στο μενου File

            editMenu.add(eventItem); // Προσθηκη της επιλογης Events Load στο μενου Edit
            editMenu.add(statusItem); // Προσθηκη της επιλογης Status Load στο μενου Edit

            showMenu.add(dayItem); // Προσθηκη της επιλογης μενου Day στο μενου Show
            showMenu.add(weekItem); // Προσθηκη της επιλογης μενου Week στο μενου Show
            showMenu.add(monthItem); // Προσθηκη της επιλογης μενου Month στο μενου Show
            showMenu.add(pastdayItem); // Προσθηκη της επιλογης μενου Past Day στο μενου Show
            showMenu.add(pastweekItem); // Προσθηκη της επιλογης μενου Past Week στο μενου Show
            showMenu.add(pastmonthItem); // Προσθηκη της επιλογης μενου Past Month στο μενου Show
            showMenu.add(todoItem); // Προσθηκη της επιλογης μενου To Do στο μενου Show
            showMenu.add(dueItem); // Προσθηκη της επιλογης μενου Due στο μενου Show

            menuBar.add(fileMenu); // Προσθηκη του μενου File στη γραμμη μενου
            menuBar.add(editMenu); // Προσθηκη του μενου Edit στη γραμμη μενου
            menuBar.add(showMenu); // Προσθηκη του μενου Show στη γραμμη μενου

            myFrame.setJMenuBar(menuBar); // Προσθηκη της γραμμης μενου στο παραθυρο

            myFrame.setVisible(true); // Να φαινεται το παραθυρο
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource()==loadItem) { // Αν πατηθει το κουμπι για load
                JFileChooser fileChooser=new JFileChooser(); // Δημιουργια ενος filechooser για να επιλεξει ο χρηστης ενα αρχειο
                int response=fileChooser.showOpenDialog(null); // Εμφανιση του παραθυρου με τα αρχεια που εχει ο χρηστης στον υπολογιστη του
                if (response==JFileChooser.APPROVE_OPTION) { // Αν ο χρηστης πατησει το κουμπι open
                    String calendarName = fileChooser.getSelectedFile().getAbsolutePath(); // Το αρχειο που επιλεξε ο χρηστης
                    calendar = myCalendar.load(calendarName); // Φορτωνεται το ημερολογιο μεσω της μεθοδου load
                    if (calendar == null) { // Αν δεν ειναι ημερολογιο το αρχειο που επιλεξε ο χρηστης
                        JFrame errorFrame = new JFrame(); // Νεο παραθυρο για μηνυμα λαθους
                        errorFrame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
                        errorFrame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
                        errorFrame.setSize(400, 100); // Μεγεθος παραθυρου
                        errorFrame.setLayout(new FlowLayout()); // Ορισμος διαταξης
                        errorFrame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
                        JLabel errorL1 = new JLabel("This file cannot be loaded."); // Νεα ετικετα οτι δεν μπορει να φορτωθει το αρχειο που επιλεξε ο χρηστης
                        errorL1.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                        errorL1.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        errorFrame.add(errorL1); // Προσθηκη της ετικετας στο παραθυρο
                        JLabel errorL2 = new JLabel("Try again with another file."); // Νεα ετικετα για να ξαναπροσπαθησει ο χρηστης με αλλο αρχειο
                        errorL2.setHorizontalAlignment(JLabel.CENTER); // Θεση ετικετας
                        errorL2.setVerticalAlignment(JLabel.BOTTOM); // Θεση ετικετας
                        errorFrame.add(errorL2); // Προσθηκη της ετικετας στο παραθυρο
                        errorFrame.setVisible(true); // Να φαινεται το παραθυρο
                        calendar=myCalendar.newCalendar(); // Δημιουργια νεου ημερολογιου μεσω της μεθοδου newCalendar
                    }
                }
            } else if (e.getSource()==saveItem) { // Αν πατηθει το κουμπι για save
                JFileChooser fileChooser=new JFileChooser(); // Δημιουργια ενος filechooser για να επιλεξει ο χρηστης ενα αρχειο
                int response=fileChooser.showSaveDialog(null); // Εμφανιση του παραθυρου με τα αρχεια που εχει ο χρηστης στον υπολογιστη του
                if (response==JFileChooser.APPROVE_OPTION) { // Αν ο χρηστης πατησει το κουμπι save
                    String calendarName = fileChooser.getSelectedFile().getAbsolutePath(); // Το αρχειο που επιλεξε ο χρηστης ή το αρχειο που δημιουργεισαι
                    try (FileOutputStream fout = new FileOutputStream(calendarName)) { // Αυτό το κομμάτι κώδικα το πήραμε από αυτό το link:https://www.ical4j.org/examples/output/
                        CalendarOutputter out = new CalendarOutputter();
                        out.output(calendar, fout);
                    } catch (IOException | net.fortuna.ical4j.model.ValidationException f) { // Αν υπαρξει προβλημα
                        System.out.println("Error saving calendar: " + f.getMessage());
                    }
                }
            } else if (e.getSource()==newAppointment) { // Αν πατηθει το κουμπι για νεο appointment
                new NewAppointmentFrame(); // Κληση της κλασης NewAppointmentFrame για προσθεση νεου appointment
            } else if (e.getSource()==newTask) { // Αν πατηθει το κουμπι για νεο task
                new NewTaskFrame(); // Κληση της κλασης NewTaskFrame για προσθεση νεου task
            } else if(e.getSource()==eventItem) { // Αν πατηθει το κουμπι για αλλαγη στοιχειων σε καποιο γεγονος
                new EditEventFrame(); // Κληση της κλασης EditEventFrame για αλλαγη στοιχειων σε καποιο γεγονος
            } else if(e.getSource()==statusItem) { // Αν πατηθει το κουμπι για αλλαγη της καταστασης ολοκληρωσης ενος task
                new EditStatusFrame(); // Κληση της κλασης EditStatusFrame για αλλαγη της καταστασης ολοκληρωσης ενος task
            } else if (e.getSource()==dayItem) { // Αν πατηθει το κουμπι για εμφανιση των γεγονοτων μεχρι το τελος της ημερας
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedAppointments=appointmentArray(); // Ο πινακας με τα appointment του ημερολογιου που δημιουργειται με τη μεθοδο appointmentArray
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("day",sortedAppointments,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των γεγονοτων του ημερολογιου
            } else if (e.getSource()==weekItem) { // Αν πατηθει το κουμπι για εμφανιση των γεγονοτων μεχρι το τελος της εβδομαδας
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedAppointments=appointmentArray(); // Ο πινακας με τα appointment του ημερολογιου που δημιουργειται με τη μεθοδο appointmentArray
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("week",sortedAppointments,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των γεγονοτων του ημερολογιου
            } else if (e.getSource()==monthItem) { // Αν πατηθει το κουμπι για εμφανιση των γεγονοτων μεχρι το τελος του μηνα
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedAppointments=appointmentArray(); // Ο πινακας με τα appointment του ημερολογιου που δημιουργειται με τη μεθοδο appointmentArray
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("month",sortedAppointments,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των γεγονοτων του ημερολογιου
            } else if (e.getSource()==pastdayItem) { // Αν πατηθει το κουμπι για εμφανιση των γεγονοτων απο την αρχη της ημερας
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedAppointments=appointmentArray(); // Ο πινακας με τα appointment του ημερολογιου που δημιουργειται με τη μεθοδο appointmentArray
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("pastday",sortedAppointments,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των γεγονοτων του ημερολογιου
            } else if (e.getSource()==pastweekItem) { // Αν πατηθει το κουμπι για εμφανιση των γεγονοτων απο την αρχη της εβδομαδας
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedAppointments=appointmentArray(); // Ο πινακας με τα appointment του ημερολογιου που δημιουργειται με τη μεθοδο appointmentArray
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("pastweek",sortedAppointments,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των γεγονοτων του ημερολογιου
            } else if (e.getSource()==pastmonthItem) { // Αν πατηθει το κουμπι για εμφανιση των γεγονοτων απο την αρχη του μηνα
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedAppointments=appointmentArray(); // Ο πινακας με τα appointment του ημερολογιου που δημιουργειται με τη μεθοδο appointmentArray
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("pastmonth",sortedAppointments,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των γεγονοτων του ημερολογιου
            } else if (e.getSource()==todoItem) { // Αν πατηθει το κουμπι για εμφανιση των tasks που δεν εχουν ολοκληρωθει και δεν εχει περασει η προθεσμια τους
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("todo",null,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των task του ημερολογιου
            } else if (e.getSource()==dueItem) { // Αν πατηθει το κουμπι για εμφανιση των tasks που δεν εχουν ολοκληρωθει και εχει περασει η προθεσμια τους
                JPanel panel=showFrame(); // Δημιουργια πανελ μεσω της μεθοδου showFrame
                sortedTasks=taskArray(); // Ο πινακας με τα task του ημερολογιου που δημιουργειται με τη μεθοδο taskArray
                myCalendar.printEvents("due",null,sortedTasks,panel); // Κληση της μεθοδου printEvents για την εμφανιση των task του ημερολογιου
            } else if (e.getSource()==exitItem) { // Αν πατηθει το κουμπι για εξοδο
                System.exit(0); // Τερματισμος του προγραμματος
            }
        }

        public JPanel showFrame() { // Μεθοδος που δημιουργει ενα νεο παραθυρο και ενα πανελ στο παραθυρο αυτο
            myFrame.setVisible(false); // Να μη φαινεται το αρχικο παραθυρο
            JFrame frame=new JFrame(); // Καινουριο παραθυρο
            frame.setTitle("Calendar"); // Ο τιτλος του παραθυρου
            frame.setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE); // Για το κλεισιμο του παραθυρου
            frame.setLayout(new FlowLayout()); // Ορισμος διαταξης
            frame.setSize(800,500); // Μεγεθος παραθυρου
            frame.setLocationRelativeTo(null); // Να βρισκεται στο κεντρο της οθονης
            frame.setResizable(false); // Να μην επιτρεπεται η αλλαγη μεγεθους
            JPanel panel = new JPanel(); // Δημιουργια πανελ
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Ορισμος διαταξης του πανελ
            JScrollPane scrollPane = new JScrollPane(panel); // Νεα μπαρα κυλισης
            panel.setAutoscrolls(true); // Να μπορει να κυλαει παρακατω το παραθυρο αναλογα του μεγεθους του περιεχομενου του
            scrollPane.setPreferredSize(new Dimension(785, 500)); // Μεγεθος της μπαρας κυλισης
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Εμφανιση της μπαρας κυλισης
            scrollPane.setVisible(true); // Να φαινεται η μπαρα κυλισης
            frame.setVisible(true); // Να φαινεται το καινουριο παραθυρο
            frame.add(scrollPane); // Προσθηκη της μπαρας κυλισης στο παραθυρο
            myFrame.setVisible(true); // Να φαινεται το αρχικο παραθυρο
            return panel; // Επιστροφη του πανελ
        }

        public VEvent[] appointmentArray() { // Μεθοδος που τοποθετει τα appointments σε μια λιστα και τη μετατρεπει σε πινακα
            ArrayList<VEvent> Appointments = new ArrayList<>(); // Μια arraylist για τα appointments
            for (Object component : calendar.getComponents(Component.VEVENT)) { // Για καθε γεγονος μεσα στο ημερολογιο
                if (component instanceof VEvent) { // Αν το γεγονος εοναι τυπου VEvent δηλαδη appointment
                    VEvent vevent = (VEvent) component;
                    Appointments.add(vevent); // Προσθετω το appointment στη arraylist
                }
            }
            Appointments.sort(Comparator.comparing(myCalendar::getDateTime)); // Ταξινομειται η arraylist με τα appointments με βαση την ημερομηνια και την ωρα τους που παιρνουμε από τη μεθοδο getDateTime
            return Appointments.toArray(new VEvent[0]); // Επιστροφη της λιστας αφου μετατραπει σε πινακα
        }

        public VToDo[] taskArray() { // Μεθοδος που τοποθετει τα tasks σε μια λιστα και τη μετατρεπει σε πινακα
            ArrayList<VToDo> Tasks = new ArrayList<>(); // Μια arraylist για τα tasks
            for (Object component : calendar.getComponents(Component.VTODO)) {
                if (component instanceof VToDo) { // Αν το γεγονος ειναι τυπου VToDo δηλαδη task
                    VToDo vtodo = (VToDo) component;
                    Tasks.add(vtodo); // Προσθετω το task στη arraylist
                }
            }
            Tasks.sort(Comparator.comparing(myCalendar::getDateTime)); // Ταξινομειται η arraylist με τα tasks με βαση την ημερομηνια και την προθεσμια τους που παιρνουμε απο τη μεθοδο getDateTime
            return Tasks.toArray(new VToDo[0]); // Επιστροφη της λιστας αφου μετατραπει σε πινακα
        }
    }