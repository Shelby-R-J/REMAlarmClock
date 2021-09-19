import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI extends JFrame implements ActionListener {
    private JPanel panel, panel2, panel3;
    private GridBagLayout gbLayout;
    private GridBagConstraints gbc;
    private JFrame frame, frame2, frame3;
    private JButton sleep, stopAlarm, enterAnswer;
    private JLabel time, timeToSleepDescrip, maxTimeWakeDescrip, currentSleep, currentAwake, wakeUpTime, timeNote, sleepNote, question;
    private JTextField timeToSleep, maxTimeWake, questionAnswer;
    private String fallSleepTime, limitSleep, currentTime, alarmTimes = "", randomTextResponse, randomString;
    private boolean alarmExist = false;
    private boolean sleepPressed = false;
    private Clip alarmSound;
    private AudioInputStream audioInput;
    private File alarmSound1;
    private boolean clockExist = false;
    private Clock clock;
    private static String currentLimitTime = "00:00";
    private String timeCurrent = clock.grabRealCurrentTime();
    private boolean rightAnswer = false;

    public GUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        frame = new JFrame();
        frame2 = new JFrame();
        frame3 = new JFrame();
        panel = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        gbLayout = new GridBagLayout();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        alarmSound1 = new File("alarmSound.wav");

        defaultSetup();

        while(!clockExist) {
            updateClock();
            if (!currentTime.equals("") && !alarmTimes.equals(""))
            {
                if (currentTime.equals(alarmTimes) && !alarmExist)
                {
                    try {
                        playAlarmSound(alarmSound1);
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        ex.printStackTrace();
                    }

                    clearSetup2to3();
                    page3();

                    alarmExist = true;
                }

            }

        }
    }

    public void frameSetup(JFrame j, JPanel p) {
        j.setPreferredSize(new Dimension(300, 330));
        j.add(p, BorderLayout.PAGE_END);
        j.setTitle("Alarm Clock");
        j.pack();
        j.setVisible(true);
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.setLayout(gbLayout);
        timeCurrent = timeCurrent.substring(0, 5);
        time = new JLabel(timeCurrent);
    }

    public void actionPerformed(ActionEvent e)
    {
        String text = e.getActionCommand();

        fallSleepTime = timeToSleep.getText();
        limitSleep = maxTimeWake.getText();

        maxTimeWake.setText("");
        timeToSleep.setText("");

        if(!fallSleepTime.equals("") && clock.setFallAsleep(fallSleepTime))
        {
            clock.setFallAsleep(fallSleepTime);
            currentAwake.setText(fallSleepTime);
        }
        if(!limitSleep.equals("") && Clock.setLimit(limitSleep))
        {
            Clock.setLimit(limitSleep);
            currentSleep.setText(limitSleep);
            currentLimitTime = limitSleep;
        }

        if (text.equals("Sleep")) {
            sleepPressed = true;

            clock.elapseTime();
            alarmTimes = clock.addThem();

            clearSetup1to2();
            page2();
            time.setText(timeCurrent);



//            sleepPressed = false;
//            try {
//                stopAlarmSound(alarmSound1);
//            } catch (LineUnavailableException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            } catch (UnsupportedAudioFileException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
        } else if (text.equals("Reset Alarm")) {
            clearSetup2to1();
            defaultSetup();
            time.setText(timeCurrent);
            currentAwake.setText("0");
            currentSleep.setText("00:00");
        } else if (text.equals("Submit")) {
            randomTextResponse = questionAnswer.getText();
            questionAnswer.setText("");
            rightAnswer = clock.checkString(randomTextResponse, randomString);
            if (rightAnswer) {
                try {
                    stopAlarmSound(alarmSound1);
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                clearSetup3to1();
                defaultSetup();
                alarmExist = false;

            }
        }

        panel.revalidate();

    }

    public static String getCurrentLimitSleep() {

        if (currentLimitTime.equals("")) {
            return ("00:00");
        } else {
            return currentLimitTime;
        }
    }

    public void defaultSetup() {
        panel.removeAll();
        frameSetup(frame, panel);

        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setVerticalAlignment(SwingConstants.CENTER);
        time.setFont(new Font("Serif", Font.PLAIN, 50));
        time.setPreferredSize(new Dimension(100, 100));
        panel.add(time);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbLayout.setConstraints(time, gbc);

        timeToSleepDescrip = new JLabel("Approximate minutes to fall asleep:");
        timeToSleepDescrip.setFont(new Font("Serif", Font.PLAIN, 12));
        timeToSleepDescrip.setHorizontalAlignment(SwingConstants.CENTER);
        timeToSleepDescrip.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(timeToSleepDescrip);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbLayout.setConstraints(timeToSleepDescrip, gbc);

        currentAwake = new JLabel("0");
        currentAwake.setFont(new Font("Serif", Font.PLAIN, 12));
        currentAwake.setHorizontalAlignment(SwingConstants.CENTER);
        currentAwake.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(currentAwake);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbLayout.setConstraints(currentAwake, gbc);

        timeToSleep = new JTextField(10);
        panel.add(timeToSleep);
        timeToSleep.setEditable(true);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbLayout.setConstraints(timeToSleep, gbc);

        maxTimeWakeDescrip = new JLabel("Latest Time to Wake (HR:MINUTE):");
        maxTimeWakeDescrip.setFont(new Font("Serif", Font.PLAIN, 12));
        panel.add(maxTimeWakeDescrip);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbLayout.setConstraints(maxTimeWakeDescrip, gbc);

        currentSleep = new JLabel("00:00");
        currentSleep.setFont(new Font("Serif", Font.PLAIN, 12));
        currentSleep.setHorizontalAlignment(SwingConstants.CENTER);
        currentSleep.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(currentSleep);
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbLayout.setConstraints(currentSleep, gbc);

        maxTimeWake = new JTextField(10);
        panel.add(maxTimeWake);
        maxTimeWake.setEditable(true);
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbLayout.setConstraints(maxTimeWake, gbc);

        sleep = new JButton("Sleep");
        sleep.addActionListener(this);
        //sleep.setPreferredSize(new Dimension(40, 40));
        sleep.setHorizontalAlignment(SwingConstants.CENTER);
        sleep.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(sleep);
        gbc.gridy = 12;
        gbc.gridx = 0;
        gbLayout.setConstraints(sleep, gbc);

        timeNote = new JLabel("(This system runs on 24-hour time;");
        timeNote.setFont(new Font("Serif", Font.PLAIN, 10));
        timeNote.setHorizontalAlignment(SwingConstants.CENTER);
        timeNote.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(timeNote);
        gbc.gridy = 13;
        gbc.gridx = 0;
        gbLayout.setConstraints(timeNote, gbc);

        sleepNote = new JLabel("timer starts when you press 'Sleep')");
        sleepNote.setFont(new Font("Serif", Font.PLAIN, 10));
        sleepNote.setHorizontalAlignment(SwingConstants.CENTER);
        sleepNote.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(sleepNote);
        gbc.gridy = 14;
        gbc.gridx = 0;
        gbLayout.setConstraints(sleepNote, gbc);

        panel.revalidate();

    }

    public void page2() {
        panel2.removeAll();
        frameSetup(frame2, panel2);

        wakeUpTime = new JLabel("Alarm set for: " + alarmTimes);
        wakeUpTime.setHorizontalAlignment(SwingConstants.CENTER);
        wakeUpTime.setVerticalAlignment(SwingConstants.CENTER);
        wakeUpTime.setFont(new Font("Serif", Font.PLAIN, 20));
        panel2.add(wakeUpTime);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbLayout.setConstraints(wakeUpTime, gbc);

        stopAlarm = new JButton("Reset Alarm");
        panel2.add(stopAlarm);
        stopAlarm.addActionListener(this);
        //sleep.setPreferredSize(new Dimension(40, 40));
        stopAlarm.setHorizontalAlignment(SwingConstants.CENTER);
        stopAlarm.setVerticalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbLayout.setConstraints(stopAlarm, gbc);

        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setVerticalAlignment(SwingConstants.CENTER);
        time.setFont(new Font("Serif", Font.PLAIN, 50));
        time.setPreferredSize(new Dimension(100, 100));
        panel2.add(time);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbLayout.setConstraints(time, gbc);

        panel2.revalidate();
    }

    public void page3() {
        panel3.removeAll();
        frameSetup(frame3, panel3);
        randomString = clock.randomString();
        question = new JLabel(randomString);
        question.setFont(new Font("Serif", Font.PLAIN, 50));
        question.setHorizontalAlignment(SwingConstants.CENTER);
        question.setVerticalAlignment(SwingConstants.CENTER);
        panel3.add(question);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbLayout.setConstraints(question, gbc);

        questionAnswer = new JTextField(10);
        panel3.add(questionAnswer);
        questionAnswer.setEditable(true);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbLayout.setConstraints(questionAnswer, gbc);

        enterAnswer = new JButton("Submit");
        enterAnswer.addActionListener(this);
        enterAnswer.setHorizontalAlignment(SwingConstants.CENTER);
        enterAnswer.setVerticalAlignment(SwingConstants.CENTER);
        panel3.add(enterAnswer);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbLayout.setConstraints(enterAnswer, gbc);

        panel3.revalidate();
    }

    public void clearSetup1to2() {
        frame.setVisible(false);
        frame2.setVisible(true);
    }

    public void clearSetup2to1() {
        //frame2.dispose();
        frame2.setVisible(false);
        frame.setVisible(true);
    }

    public void clearSetup2to3() {
        frame2.dispose();
        frame2.setVisible(false);
        frame3.setVisible(true);
    }

    public void clearSetup3to1() {
        //frame3.dispose();
        frame3.setVisible(false);
        frame.setVisible(true);
    }

    //play and stop methods
    public void playAlarmSound(File audioFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInput = AudioSystem.getAudioInputStream(audioFile);
        alarmSound = AudioSystem.getClip();
        alarmSound.open(audioInput);
        alarmSound.start();
        alarmSound.loop(10);
    }

    public void stopAlarmSound(File audioFile) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        alarmSound.stop();

    }

    public void updateClock()
    {
        currentTime = clock.grabRealCurrentTime();
        String timeCurrent = currentTime.substring(0, 5);
        time.setText(timeCurrent);
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        new GUI();

    }


}
