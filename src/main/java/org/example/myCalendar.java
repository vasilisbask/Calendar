package org.example;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import java.io.IOException;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;
import net.fortuna.ical4j.util.CompatibilityHints;

import javax.swing.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import static javax.swing.JOptionPane.showMessageDialog;

public class myCalendar {
    public static Calendar newCalendar() { // Μέθοδος για τη δημιουργία νέου ημερολογίου αν δεν υπάρχει ήδη αυτό που έδωσε ο χρήστης
        Calendar calendar = new Calendar(); // Αυτό το κομμάτι κώδικα το πήραμε από αυτό το link:https://www.ical4j.org/examples/model/ όπου βρήκαμε τη βιβλιοθήκη ical4j που χρησιμοποιήσαμε
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        return calendar;
    }

    public static Calendar load(String calendarName) { // Μέθοδος για load του ημερολογίου που έδωσε ο χρήστης
        try { // Αυτό το κομμάτι κώδικα το πήραμε από αυτό το link:https://www.ical4j.org/examples/parsing/
            FileInputStream fin = new FileInputStream(calendarName);
            CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fin);
            return calendar;
        } catch (IOException | ParserException e ){ // Αν υπάρξει πρόβλημα είσοδου
            return null;
        }
        // Αν υπάρχει πρόβλημα κατά τον έλεγχο πχ αν το αρχείο δεν είναι έγκυρο
    }

    public static void printEvents(String choice, VEvent[] sortedAppointments, VToDo[] sortedTasks, JPanel panel) { // Μεθοδος για ταξινομημένη εμφάνιση των γεγονότων του ημερολογίου
        TimeTeller teller = TimeService.getTeller();
        LocalDateTime Now = teller.now(); // Η ώρα του συστήματος αυτή τη στιγμή
        DayOfWeek Day = Now.getDayOfWeek(); // Η σημερινή ημέρα
        YearMonth Month = YearMonth.from(Now); // Ο μήνας αυτή τη στιγμή
        switch (choice) { // Ανάλογα την παράμετρο του χρήστη
            case "day": // Αν είναι day
                LocalDateTime dayEnd = Now.toLocalDate().atTime(23, 59, 59); // Η σημερινή ημέρα με ώρα 23:59:59
                printFuture(dayEnd,sortedAppointments,sortedTasks,panel); // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις και παίρνει ως παραμέτρους τους 2 πίνακες και το τέλος της ημέρας
                break;
            case "week": // Αν είναι week
                int daysUntilEndOfWeek = DayOfWeek.SUNDAY.getValue() - Day.getValue(); // Ο αριθμός των ημερών μέχρι το τέλος της εβδομάδας
                LocalDateTime endOfWeek = Now.plusDays(daysUntilEndOfWeek).withHour(23).withMinute(59).withSecond(59); // Η τελευταία ημέρα της εβδομάδας με ώρα 23:59:59
                printFuture(endOfWeek,sortedAppointments,sortedTasks,panel); // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις και παίρνει ως παραμέτρους τους 2 πίνακες και το τέλος της εβδομάδας
                break;
            case "month": // Αν είναι month
                LocalDate endingMonth = Month.atEndOfMonth(); // Η τελευταία ημέρα του μήνα
                LocalDateTime endOfMonth=endingMonth.atTime(23, 59, 59); // Η τελευταία ημέρα του μήνα με ώρα 23:59:59
                printFuture(endOfMonth,sortedAppointments,sortedTasks,panel); // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις και παίρνει ως παραμέτρους τους 2 πίνακες και το τέλος του μήνα
                break;
            case "pastday": // Αν είναι pastday
                LocalDateTime startOfDay = Now.toLocalDate().atTime(0, 0, 0); // Η σημερινή ημέρα με ώρα 00:00:00
                printPast(startOfDay,sortedAppointments,sortedTasks,panel); // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις και παίρνει ως παραμέτρους τους 2 πίνακες και την αρχή της ημέρας
                break;
            case "pastweek": // Αν είναι pastweek
                int daysUntilStartOfWeek = Day.getValue() - DayOfWeek.MONDAY.getValue(); // Ο αριθμός των ημερών μέχρι την αρχή της εβδομάδας
                LocalDateTime startOfWeek = Now.minusDays(daysUntilStartOfWeek).withHour(0).withMinute(0).withSecond(0); // Η πρώτη ημέρα της εβδομάδας με ώρα 00:00:00
                printPast(startOfWeek,sortedAppointments,sortedTasks,panel); // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις και παίρνει ως παραμέτρους τους 2 πίνακες και την αρχή της εβδομάδας
                break;
            case "pastmonth": // Αν είναι pastmonth
                LocalDate startingMonth = Month.atDay(01); // Η πρώτη ημέρα του μήνα
                LocalDateTime startOfMonth=startingMonth.atTime(0, 0, 0); // Η πρώτη ημέρα του μήνα με ώρα 00:00:00
                printPast(startOfMonth,sortedAppointments,sortedTasks,panel); // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις και παίρνει ως παραμέτρους τους 2 πίνακες και την αρχή του μήνα
                break;
            case "todo": // Αν είναι todo
                if (sortedTasks.length != 0) { // Αν υπάρχουν tasks στο ημερολόγιο
                    for (VToDo task : sortedTasks) { // Για κάθε task που υπάρχει
                        if (task.getStatus().getValue().equals("IN-PROCESS")) { // Αν το task δεν έχει ολοκληρωθεί
                            Date endDate = task.getDue().getDate(); // Η προθεσμία του task
                            LocalDateTime eventEndDate = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()); // Μετατρέπω την προθεσμία του task σε localdatetime
                            if (Now.isBefore(eventEndDate)) { // Αν η προθεσμία του task είναι μετά την τωρινή ώρα
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
                            }
                        }
                    }
                    break;
                } else { // Αν δεν υπάρχουν
                    JLabel l=new JLabel("There are not tasks in this calendar."); // Νεα ετικετα για μηνυμα οτι δεν υπαρχουν tasks
                    l.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l); // Προσθηκη ετικετας στο πανελ
                    break;
                }
            case "due": // Αν είναι due
                if (sortedTasks.length != 0) { // Αν υπάρχουν tasks στο ημερολόγιο
                    for (VToDo task : sortedTasks) { // Για κάθε task που υπάρχει
                        if (task.getStatus().getValue().equals("IN-PROCESS")) { // Αν το task δεν έχει ολοκληρωθεί
                            Date endDate = task.getDue().getDate(); // Η προθεσμία του task
                            LocalDateTime eventEndDate = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()); // Μετατρέπω την προθεσμία του task σε localdatetime
                            if (Now.isAfter(eventEndDate)) { // Αν η προθεσμία του task είναι πριν την τωρινή ώρα
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
                            }
                        }
                    }
                    break;
                } else { // Αν δεν υπάρχουν
                    JLabel l=new JLabel("There are not tasks in this calendar."); // Νεα ετικετα για μηνυμα οτι δεν υπαρχουν tasks
                    l.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l); // Προσθηκη ετικετας στο πανελ
                    break;
                }
        }
        TimeService.stop();
    }

    public static void printFuture(LocalDateTime time,VEvent[] sortedAppointments,VToDo[] sortedTasks,JPanel panel) { // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις των day ή week ή month
        int iAppointment=0,iTask=0; // Δύο μετρητές για τους δύο πίνακες, ένας για τον πίνακα των appointments και ένας για τον πίνακα των tasks
        TimeTeller teller = TimeService.getTeller();
        LocalDateTime Now = teller.now(); // Η ώρα του συστήματος αυτή τη στιγμή
        LocalDateTime dtStart; // Μεταβλητή για την ώρα του appointment
        LocalDateTime dtEnd; // Μεταβλητή για την προθεσμία του task
        if (sortedAppointments.length == 0 && sortedTasks.length == 0) { // Αν το ημερολογιο ειναι αδειο
            JLabel l=new JLabel("There are neither appointments nor tasks in this calendar."); // Νεα ετικετα για μηνυμα οτι δεν υπαρχουν ουτε appointments ουτε tasks στο ημερολογιο
            l.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            panel.add(l); // Προσθηκη ετικετας στο πανελ
        } else if (sortedAppointments.length != 0 && sortedTasks.length != 0) { // Αν υπάρχουν και appointments και tasks στο ημερολόγιο
            while ((iAppointment < sortedAppointments.length && iTask < sortedTasks.length)) { // Όσο οι μετρητές είναι μικρότεροι από τα μεγέθη των πινάκων τους αντίστοιχα
                dtStart = LocalDateTime.ofInstant(sortedAppointments[iAppointment].getStartDate().getDate().toInstant(), ZoneId.systemDefault()); // Η ώρα του appointment
                dtEnd = LocalDateTime.ofInstant(sortedTasks[iTask].getDue().getDate().toInstant(), ZoneId.systemDefault()); // Η προθεσμία του task
                if (dtStart.isBefore(dtEnd)) { // Αν η ώρα του appointment είναι πριν της ώρας του task
                    if (dtStart.isAfter(Now) && dtStart.isBefore(time)) { // Αν η ώρα του appointment είναι μετά της τωρινής ώρας και πριν της μεταβλητής time
                        JLabel l1 = new JLabel("Appointment: "); // Νεα ετικετα
                        JLabel l2 = new JLabel("Title: " + ((VEvent) sortedAppointments[iAppointment]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του appointment
                        l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l3= new JLabel("Description: " + ((VEvent) sortedAppointments[iAppointment]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του appointment
                        l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l4 = new JLabel("Start DateTime: " + ((VEvent) sortedAppointments[iAppointment]).getStartDate().getValue()); // Νεα ετικετα για την ωρα εναρξης του appointment
                        l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l5 = new JLabel("End DateTime: " + ((VEvent) sortedAppointments[iAppointment]).getEndDate().getValue()); // Νεα ετικετα για την ωρα τελους του appointment
                        l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        panel.add(l1); // Προσθηκη ετικετας στο πανελ
                        panel.add(l2); // Προσθηκη ετικετας στο πανελ
                        panel.add(l3); // Προσθηκη ετικετας στο πανελ
                        panel.add(l4); // Προσθηκη ετικετας στο πανελ
                        panel.add(l5); // Προσθηκη ετικετας στο πανελ
                    }
                    if (iAppointment + 1 < sortedAppointments.length) { // Αν μπορεί να αυξηθεί ο μετρητής του πίνακα των appointment
                        iAppointment++; // Αυξάνεται
                    } else { // Αν δεν μπορεί να αυξηθεί ο μετρητής του πίνακα των appointment
                        for (int i = iTask; i < sortedTasks.length; i++) { // Για τα tasks από τη θέση iTask μέχρι το τέλος του πίνακα
                            dtEnd = LocalDateTime.ofInstant(sortedTasks[i].getDue().getDate().toInstant(), ZoneId.systemDefault()); // Η προθεσμία του task
                            if (dtEnd.isAfter(Now) && dtEnd.isBefore(time)) { // Αν η προθεσμία του task είναι μετά της τωρινής ώρας και πριν της μεταβλητής time
                                JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                                JLabel l2 = new JLabel("Title: " + ((VToDo) sortedTasks[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                                l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l3 = new JLabel("Description: " + ((VToDo) sortedTasks[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                                l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l4 = new JLabel("Due: " + ((VToDo) sortedTasks[i]).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                                l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l5 = new JLabel("Status: " + ((VToDo) sortedTasks[i]).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                                l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                panel.add(l1); // Προσθηκη ετικετας στο πανελ
                                panel.add(l2); // Προσθηκη ετικετας στο πανελ
                                panel.add(l3); // Προσθηκη ετικετας στο πανελ
                                panel.add(l4); // Προσθηκη ετικετας στο πανελ
                                panel.add(l5); // Προσθηκη ετικετας στο πανελ
                            }
                        }
                        iAppointment = sortedAppointments.length; // Ο μετρητής του πίνακα των appointments γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                        iTask = sortedTasks.length; // Ο μετρητής του πίνακα των tasks γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                    }
                } else { // Αν η ώρα του task είναι πριν της ώρας του appointment
                    if (dtEnd.isAfter(Now) && dtEnd.isBefore(time)) { // Αν η προθεσμία του task είναι μετά της τωρινής ώρας και πριν της μεταβλητής time
                        JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                        JLabel l2 = new JLabel("Title: " + ((VToDo) sortedTasks[iTask]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                        l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l3 = new JLabel("Description: " + ((VToDo) sortedTasks[iTask]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                        l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l4 = new JLabel("Due: " + ((VToDo) sortedTasks[iTask]).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                        l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l5 = new JLabel("Status: " + ((VToDo) sortedTasks[iTask]).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                        l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        panel.add(l1); // Προσθηκη ετικετας στο πανελ
                        panel.add(l2); // Προσθηκη ετικετας στο πανελ
                        panel.add(l3); // Προσθηκη ετικετας στο πανελ
                        panel.add(l4); // Προσθηκη ετικετας στο πανελ
                        panel.add(l5); // Προσθηκη ετικετας στο πανελ
                    }
                    if (iTask + 1 < sortedTasks.length) { // Αν μπορεί να αυξηθεί ο μετρητής του πίνακα των tasks
                        iTask++; // Αυξάνεται
                    } else { // Αν δεν μπορεί να αυξηθεί ο μετρητής του πίνακα των tasks
                        for (int i = iAppointment; i < sortedAppointments.length; i++) { // Για τα appointments από τη θέση iAppointment μέχρι το τέλος του πίνακα
                            dtStart = LocalDateTime.ofInstant(sortedAppointments[i].getStartDate().getDate().toInstant(), ZoneId.systemDefault()); // Η ώρα του appointment
                            if (dtStart.isAfter(Now) && dtStart.isBefore(time)) { // Αν η ώρα του appointment είναι μετά της τωρινής ώρας και πριν της μεταβλητής time
                                JLabel l1 = new JLabel("Appointment: "); // Νεα ετικετα
                                JLabel l2 = new JLabel("Title: " + ((VEvent) sortedAppointments[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του appointment
                                l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l3= new JLabel("Description: " + ((VEvent) sortedAppointments[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του appointment
                                l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l4 = new JLabel("Start DateTime: " + ((VEvent) sortedAppointments[i]).getStartDate().getValue()); // Νεα ετικετα για την ωρα εναρξης του appointment
                                l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l5 = new JLabel("End DateTime: " + ((VEvent) sortedAppointments[i]).getEndDate().getValue()); // Νεα ετικετα για την ωρα τελους του appointment
                                l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                panel.add(l1); // Προσθηκη ετικετας στο πανελ
                                panel.add(l2); // Προσθηκη ετικετας στο πανελ
                                panel.add(l3); // Προσθηκη ετικετας στο πανελ
                                panel.add(l4); // Προσθηκη ετικετας στο πανελ
                                panel.add(l5); // Προσθηκη ετικετας στο πανελ
                            }
                        }
                        iAppointment = sortedAppointments.length; // Ο μετρητής του πίνακα των appointments γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                        iTask = sortedTasks.length; // Ο μετρητής του πίνακα των tasks γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                    }
                }
            }
        } else if (sortedAppointments.length == 0) { // Αν υπάρχουν μόνο tasks στο ημερολόγιο
            for (int i = iTask; i < sortedTasks.length; i++) { // Για κάθε task μέχρι το τέλος του πίνακα
                dtEnd = LocalDateTime.ofInstant(sortedTasks[i].getDue().getDate().toInstant(), ZoneId.systemDefault()); // Η προθεσμία του task
                if (dtEnd.isAfter(Now) && dtEnd.isBefore(time)) { // Αν η προθεσμία του task είναι μετά της τωρινής ώρας και πριν της μεταβλητής time
                    JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                    JLabel l2 = new JLabel("Title: " + ((VToDo) sortedTasks[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                    l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l3 = new JLabel("Description: " + ((VToDo) sortedTasks[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                    l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l4 = new JLabel("Due: " + ((VToDo) sortedTasks[i]).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                    l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l5 = new JLabel("Status: " + ((VToDo) sortedTasks[i]).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                    l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l1); // Προσθηκη ετικετας στο πανελ
                    panel.add(l2); // Προσθηκη ετικετας στο πανελ
                    panel.add(l3); // Προσθηκη ετικετας στο πανελ
                    panel.add(l4); // Προσθηκη ετικετας στο πανελ
                    panel.add(l5); // Προσθηκη ετικετας στο πανελ
                }
            }
        } else { // Αν υπάρχουν μόνο appointments στο ημερολόγιο
            for (int i = iAppointment; i < sortedAppointments.length; i++) { // Για κάθε appointments μέχρι το τέλος του πίνακα
                dtStart = LocalDateTime.ofInstant(sortedAppointments[i].getStartDate().getDate().toInstant(), ZoneId.systemDefault()); // Η ώρα του appointment
                if (dtStart.isAfter(Now) && dtStart.isBefore(time)) { // Αν η ώρα του appointment είναι μετά της τωρινής ώρας και πριν της μεταβλητής time
                    JLabel l1 = new JLabel("Appointment: "); // Νεα ετικετα
                    JLabel l2 = new JLabel("Title: " + ((VEvent) sortedAppointments[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του appointment
                    l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l3= new JLabel("Description: " + ((VEvent) sortedAppointments[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του appointment
                    l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l4 = new JLabel("Start DateTime: " + ((VEvent) sortedAppointments[i]).getStartDate().getValue()); // Νεα ετικετα για την ωρα εναρξης του appointment
                    l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l5 = new JLabel("End DateTime: " + ((VEvent) sortedAppointments[i]).getEndDate().getValue()); // Νεα ετικετα για την ωρα τελους του appointment
                    l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l1); // Προσθηκη ετικετας στο πανελ
                    panel.add(l2); // Προσθηκη ετικετας στο πανελ
                    panel.add(l3); // Προσθηκη ετικετας στο πανελ
                    panel.add(l4); // Προσθηκη ετικετας στο πανελ
                    panel.add(l5); // Προσθηκη ετικετας στο πανελ
                }
            }
        }
    }

    public static void printPast(LocalDateTime time,VEvent[] sortedAppointments,VToDo[] sortedTasks,JPanel panel) { // Μέθοδος όπου ελέγχει τις ώρες των γεγονότων και τα εμφανίζει αν ισχύουν οι προϋποθέσεις των pastday ή pastweek ή pastmonth
        int iAppointment = 0, iTask = 0; // Δύο μετρητές για τους δύο πίνακες, ένας για τον πίνακα των appointments και ένας για τον πίνακα των tasks
        TimeTeller teller = TimeService.getTeller();
        LocalDateTime Now = teller.now(); // Η ώρα του συστήματος αυτή τη στιγμή
        LocalDateTime dtStart; // Μεταβλητή για την ώρα του appointment
        LocalDateTime dtEnd; // Μεταβλητή για την προθεσμία του task
        if (sortedAppointments.length == 0 && sortedTasks.length == 0) { // Αν το ημερολογιο ειναι αδειο
            JLabel l=new JLabel("There are neither appointments nor tasks in this calendar."); // Νεα ετικετα για μηνυμα οτι δεν υπαρχουν ουτε appointments ουτε tasks στο ημερολογιο
            l.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
            panel.add(l); // Προσθηκη ετικετας στο πανελ
        } else if (sortedAppointments.length != 0 && sortedTasks.length != 0) { // Αν υπάρχουν και appointments και tasks στο ημερολόγιο
            while ((iAppointment < sortedAppointments.length && iTask < sortedTasks.length)) { // Όσο οι μετρητές είναι μικρότεροι από τα μεγέθη των πινάκων τους αντίστοιχα
                dtStart = LocalDateTime.ofInstant(sortedAppointments[iAppointment].getStartDate().getDate().toInstant(), ZoneId.systemDefault()); // Η ώρα του appointment
                dtEnd = LocalDateTime.ofInstant(sortedTasks[iTask].getDue().getDate().toInstant(), ZoneId.systemDefault()); // Η προθεσμία του task
                if (dtStart.isBefore(dtEnd)) { // Αν η ώρα του appointment είναι πριν της ώρας του task
                    if (dtStart.isBefore(Now) && dtStart.isAfter(time)) { // Αν η ώρα του appointment είναι πριν της τωρινής ώρας και μετά της μεταβλητής time
                        JLabel l1 = new JLabel("Appointment: "); // Νεα ετικετα
                        JLabel l2 = new JLabel("Title: " + ((VEvent) sortedAppointments[iAppointment]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του appointment
                        l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l3= new JLabel("Description: " + ((VEvent) sortedAppointments[iAppointment]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του appointment
                        l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l4 = new JLabel("Start DateTime: " + ((VEvent) sortedAppointments[iAppointment]).getStartDate().getValue()); // Νεα ετικετα για την ωρα εναρξης του appointment
                        l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l5 = new JLabel("End DateTime: " + ((VEvent) sortedAppointments[iAppointment]).getEndDate().getValue()); // Νεα ετικετα για την ωρα τελους του appointment
                        l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        panel.add(l1); // Προσθηκη ετικετας στο πανελ
                        panel.add(l2); // Προσθηκη ετικετας στο πανελ
                        panel.add(l3); // Προσθηκη ετικετας στο πανελ
                        panel.add(l4); // Προσθηκη ετικετας στο πανελ
                        panel.add(l5); // Προσθηκη ετικετας στο πανελ
                    }
                    if (iAppointment + 1 < sortedAppointments.length) { // Αν μπορεί να αυξηθεί ο μετρητής του πίνακα των appointments
                        iAppointment++; // Αυξάνεται
                    } else { // Αν δεν μπορεί να αυξηθεί ο μετρητής του πίνακα των appointment
                        for (int i = iTask; i < sortedTasks.length; i++) { // Για τα tasks από τη θέση iTask μέχρι το τέλος του πίνακα
                            dtEnd = LocalDateTime.ofInstant(sortedTasks[i].getDue().getDate().toInstant(), ZoneId.systemDefault()); // Η προθεσμία του task
                            if (dtEnd.isBefore(Now) && dtEnd.isAfter(time)) { // Αν η προθεσμία του task είναι πριν της τωρινής ώρας και μετά της μεταβλητής time
                                JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                                JLabel l2 = new JLabel("Title: " + ((VToDo) sortedTasks[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                                l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l3 = new JLabel("Description: " + ((VToDo) sortedTasks[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                                l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l4 = new JLabel("Due: " + ((VToDo) sortedTasks[i]).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                                l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l5 = new JLabel("Status: " + ((VToDo) sortedTasks[i]).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                                l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                panel.add(l1); // Προσθηκη ετικετας στο πανελ
                                panel.add(l2); // Προσθηκη ετικετας στο πανελ
                                panel.add(l3); // Προσθηκη ετικετας στο πανελ
                                panel.add(l4); // Προσθηκη ετικετας στο πανελ
                                panel.add(l5); // Προσθηκη ετικετας στο πανελ
                            }
                        }
                        iAppointment = sortedAppointments.length; // Ο μετρητής του πίνακα των appointments γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                        iTask = sortedTasks.length; // Ο μετρητής του πίνακα των tasks γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                    }
                } else { // Αν η ώρα του task είναι πριν της ώρας του appointment
                    if (dtEnd.isBefore(Now) && dtEnd.isAfter(time)) { // Αν η προθεσμία του task είναι πριν της τωρινής ώρας και μετά της μεταβλητής time
                        JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                        JLabel l2 = new JLabel("Title: " + ((VToDo) sortedTasks[iTask]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                        l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l3 = new JLabel("Description: " + ((VToDo) sortedTasks[iTask]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                        l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l4 = new JLabel("Due: " + ((VToDo) sortedTasks[iTask]).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                        l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        JLabel l5 = new JLabel("Status: " + ((VToDo) sortedTasks[iTask]).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                        l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                        panel.add(l1); // Προσθηκη ετικετας στο πανελ
                        panel.add(l2); // Προσθηκη ετικετας στο πανελ
                        panel.add(l3); // Προσθηκη ετικετας στο πανελ
                        panel.add(l4); // Προσθηκη ετικετας στο πανελ
                        panel.add(l5); // Προσθηκη ετικετας στο πανελ
                    }
                    if (iTask + 1 < sortedTasks.length) { // Αν μπορεί να αυξηθεί ο μετρητής του πίνακα των tasks
                        iTask++; // Αυξάνεται
                    } else { // Αν δεν μπορεί να αυξηθεί ο μετρητής του πίνακα των tasks
                        for (int i = iAppointment; i < sortedAppointments.length; i++) { // Για τα appointments από τη θέση iAppointment μέχρι το τέλος του πίνακα
                            dtStart = LocalDateTime.ofInstant(sortedAppointments[i].getStartDate().getDate().toInstant(), ZoneId.systemDefault()); // Η ώρα του appointment
                            if (dtStart.isBefore(Now) && dtStart.isAfter(time)) { // Αν η ώρα του appointment είναι πριν της τωρινής ώρας και μετά της μεταβλητής time
                                JLabel l1 = new JLabel("Appointment: "); // Νεα ετικετα
                                JLabel l2 = new JLabel("Title: " + ((VEvent) sortedAppointments[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του appointment
                                l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l3= new JLabel("Description: " + ((VEvent) sortedAppointments[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του appointment
                                l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l4 = new JLabel("Start DateTime: " + ((VEvent) sortedAppointments[i]).getStartDate().getValue()); // Νεα ετικετα για την ωρα εναρξης του appointment
                                l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                JLabel l5 = new JLabel("End DateTime: " + ((VEvent) sortedAppointments[i]).getEndDate().getValue()); // Νεα ετικετα για την ωρα τελους του appointment
                                l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                                panel.add(l1); // Προσθηκη ετικετας στο πανελ
                                panel.add(l2); // Προσθηκη ετικετας στο πανελ
                                panel.add(l3); // Προσθηκη ετικετας στο πανελ
                                panel.add(l4); // Προσθηκη ετικετας στο πανελ
                                panel.add(l5); // Προσθηκη ετικετας στο πανελ
                            }
                        }
                        iAppointment = sortedAppointments.length; // Ο μετρητής του πίνακα των appointments γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                        iTask = sortedTasks.length; // Ο μετρητής του πίνακα των tasks γίνεται ίσος με το μέγεθος του πίνακα για να σταματήσει το loop
                    }
                }
            }

        } else if (sortedAppointments.length == 0) { // Αν υπάρχουν μόνο tasks στο ημερολόγιο
            for (int i = iTask; i < sortedTasks.length; i++) { // Για κάθε task μέχρι το τέλος του πίνακα
                dtEnd = LocalDateTime.ofInstant(sortedTasks[i].getDue().getDate().toInstant(), ZoneId.systemDefault()); // Η προθεσμία του task
                if (dtEnd.isBefore(Now) && dtEnd.isAfter(time)) { // Αν η προθεσμία του task είναι πριν της τωρινής ώρας και μετά της μεταβλητής time
                    JLabel l1 = new JLabel("Task: "); // Νεα ετικετα
                    JLabel l2 = new JLabel("Title: " + ((VToDo) sortedTasks[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του task
                    l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l3 = new JLabel("Description: " + ((VToDo) sortedTasks[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του task
                    l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l4 = new JLabel("Due: " + ((VToDo) sortedTasks[i]).getDue().getValue()); // Νεα ετικετα για την προθεσμια του task
                    l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l5 = new JLabel("Status: " + ((VToDo) sortedTasks[i]).getStatus().getValue()); // Νεα ετικετα για την κατασταση ολοκληρωσης του task
                    l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l1); // Προσθηκη ετικετας στο πανελ
                    panel.add(l2); // Προσθηκη ετικετας στο πανελ
                    panel.add(l3); // Προσθηκη ετικετας στο πανελ
                    panel.add(l4); // Προσθηκη ετικετας στο πανελ
                    panel.add(l5); // Προσθηκη ετικετας στο πανελ
                }
            }

        } else { // Αν υπάρχουν μόνο appointments στο ημερολόγιο
            for (int i = iAppointment; i < sortedAppointments.length; i++) { // Για κάθε appointment μέχρι το τέλος του πίνακα
                dtStart = LocalDateTime.ofInstant(sortedAppointments[i].getStartDate().getDate().toInstant(), ZoneId.systemDefault()); // Η ώρα του appointment
                if (dtStart.isBefore(Now) && dtStart.isAfter(time)) { // Αν η ώρα του appointment είναι πριν της τωρινής ώρας και μετά της μεταβλητής time
                    JLabel l1 = new JLabel("Appointment: "); // Νεα ετικετα
                    JLabel l2 = new JLabel("Title: " + ((VEvent) sortedAppointments[i]).getSummary().getValue()); // Νεα ετικετα για τον τιτλο του appointment
                    l2.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l3= new JLabel("Description: " + ((VEvent) sortedAppointments[i]).getDescription().getValue()); // Νεα ετικετα για την περιγραφη του appointment
                    l3.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l4 = new JLabel("Start DateTime: " + ((VEvent) sortedAppointments[i]).getStartDate().getValue()); // Νεα ετικετα για την ωρα εναρξης του appointment
                    l4.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    JLabel l5 = new JLabel("End DateTime: " + ((VEvent) sortedAppointments[i]).getEndDate().getValue()); // Νεα ετικετα για την ωρα τελους του appointment
                    l5.setVerticalAlignment(JLabel.TOP); // Θεση ετικετας
                    panel.add(l1); // Προσθηκη ετικετας στο πανελ
                    panel.add(l2); // Προσθηκη ετικετας στο πανελ
                    panel.add(l3); // Προσθηκη ετικετας στο πανελ
                    panel.add(l4); // Προσθηκη ετικετας στο πανελ
                    panel.add(l5); // Προσθηκη ετικετας στο πανελ
                }
            }

        }
    }

    public static DateTime getDateTime (Object event) { // Μεθοδος που επιστρεφει την ωρα εναρξης ενος appointment ή την προθεσμια ενος task
        if (event instanceof VEvent) { // Αν το γεγονος ειναι appointment
            if (((VEvent) event).getStartDate() != null) { // Αν η ωρα εναρξης του appointment δεν ειναι null δηλαδη υπαρχει
                Date startDate = ((VEvent) event).getStartDate().getDate(); // Η ωρα εναρξης του appointment
                DateTime datetime=new DateTime(startDate.getTime()); // Μετατροπη της ωρας εναρξης του appointment σε τυπο DateTime
                return datetime; // Επιστροφη της ωρας εναρξης του appointment
            }
        } else if (event instanceof VToDo) { // Αν το γεγονος ειναι task
            if (((VToDo) event).getDue() != null) { // Αν η προθεσμια του task δεν ειναι null δηλαδη υπαρχει
                Date dueDate = ((VToDo) event).getDue().getDate(); // Η προθεσμια του task
                DateTime datetime=new DateTime(dueDate.getTime()); // Μετατροπη της προθεσμιας του task σε τυπο DateTime
                return datetime; // Επιστροφη της προθεσμιας του task
            }
        }
        return null; // Επιστροφη null αν δεν υπαρχει η ωρα εναρξης του appointment ή η προθεσμια του task
    }

    public static void notifications(ArrayList<Object> events) { // Μεθοδος για τις ειδοποιησεις
        TimeTeller teller=TimeService.getTeller();
        LocalDateTime currentTime = teller.now(); // Η ωρα του συστηματος αυτη τη στιγμη
        LocalDateTime endTime = currentTime.plusMinutes(30); // Η ωρα τελους προσθετοντας 30 λεπτα στην τωρινη ωρα του συστηματος
        for (Object component : events) { // Για καθε γεγονος της λιστας
            if (component instanceof VEvent) { // Αν το γεγονος ειναι appointment
                VEvent appointment = (VEvent) component;
                LocalDateTime dtStart = LocalDateTime.ofInstant(appointment.getStartDate().getDate().toInstant(), ZoneId.systemDefault()); // Η ωρα εναρξης του appointment
                if (dtStart.isAfter(currentTime) && dtStart.isBefore(endTime)) { // Αν η ωρα εναρξης του appointment ειναι μετα την τωρινη ωρα του συστηματος και πριν την ωρα τελους
                    showMessageDialog(null, "Appointment in less than 30 minutes\n" + "Title: " + appointment.getSummary().getValue() + "\nDescription: " + appointment.getDescription().getValue() + "\nStart DateTime: " + appointment.getStartDate().getValue() + "\nEnd DateTime: " + appointment.getEndDate().getValue()); // Ειδοποιηση οτι υπαρχει appointment σε λιγοτερο απο 30 λεπτα εμφανιζοντας και τα στοιχεια του
                }
            } else if (component instanceof VToDo) { // Αν το γεγονος ειναι task
                VToDo task = (VToDo) component;
                LocalDateTime due = LocalDateTime.ofInstant(task.getDue().getDate().toInstant(), ZoneId.systemDefault()); // Η προθεσμια του task
                if (due.isAfter(currentTime) && due.isBefore(endTime)) { // Αν η προθεσμια του task ειναι μετα την τωρινη ωρα του συστηματος και πριν την ωρα τελους
                    showMessageDialog(null, "Task in less than 30 minutes\n" + "Title: " + task.getSummary().getValue() + "\nDescription: " + task.getDescription().getValue() + "\nDue: " + task.getDue().getValue() + "\nStatus: " + task.getStatus().getValue()); // Ειδοποιηση οτι υπαρχει task σε λιγοτερο απο 30 λεπτα εμφανιζοντας και τα στοιχεια του
                }
            }
        }
    }
}