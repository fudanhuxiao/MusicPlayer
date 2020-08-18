package songs;

/*
 * Music Player
 *
 * This instructor-provided file implements the graphical user interface (GUI)
 * for the Music Player program and allows you to test the behavior
 * of your Song class.
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * a class to build the GUI of the music player
 * @author Chen Lin and Xiao Hu
 *
 */
public class MusicPlayer implements ActionListener, StdAudio.AudioEventListener {

	// instance variables
	private Song song;
	private boolean playing; // whether a song is currently playing
	private boolean loading; // whether a song is loaded
	private JFrame frame;
	private JFileChooser fileChooser;
	private JTextField tempoText;
	private JSlider currentTimeSlider;
	private JButton play;
	private JButton stop;
	private JButton pause;
	private JButton load;
	private JButton reverse;
	private JButton up;
	private JButton down;
	private JButton change;
	private JPanel Panel1;
	private JPanel Panel2;
	private JPanel Panel3;
	private JPanel Panel4;
	private JPanel Panel5;
	private JPanel Panel6;

	//these are the two labels that indicate time
	// to the right of the slider
	private JLabel currentTimeLabel;
	private JLabel totalTimeLabel;

	//this the label that says 'welcome to the music player'
	private JLabel statusLabel;

	//this is the label saying "Tempo:"
	private JLabel tempo;

	/*
	 * Creates the music player GUI window and graphical components.
	 */
	/**
	 * constructor
	 */
	public MusicPlayer() {
		song = null;
		loading = false;
		createComponents();
		doLayout();
		StdAudio.addAudioEventListener(this);
		frame.setVisible(true);
	}

	/*
	 * Called when the user interacts with graphical components, such as
	 * clicking on a button.
	 */
	/**
	 * call this method when there is interaction between the user and the graphical components
	 */
	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
		if (cmd.equals("Play")) {
			playSong();
		} else if (cmd.equals("Pause")) {
			StdAudio.setPaused(!StdAudio.isPaused());
		} else if (cmd == "Stop") {
			StdAudio.setMute(true);
			StdAudio.setPaused(false);
		} else if (cmd == "Load") {
			try {
				loadFile();
			} catch (IOException ioe) {
				System.out.println("not able to load from the file");
			}
		} else if (cmd == "Reverse") {
			song.reverse();
		} else if (cmd == "Up") {
			song.octaveUp();
		} else if (cmd == "Down") {
			song.octaveDown();
		} else if (cmd == "Change Tempo") {
			String tempo = tempoText.getText();
			song.changeTempo(Double.parseDouble(tempo));
		}
	}

	/*
	 * Called when audio events occur in the StdAudio library. We use this to
	 * set the displayed current time in the slider.
	 */
	/**
	 * call this method when there are audio events in the StdAudio library
	 */
	public void onAudioEvent(StdAudio.AudioEvent event) {
		// update current time
		if (event.getType() == StdAudio.AudioEvent.Type.PLAY
				|| event.getType() == StdAudio.AudioEvent.Type.STOP) {
			setCurrentTime(getCurrentTime() + event.getDuration());
		}
	}

	/*
	 * Sets up the graphical components in the window and event listeners.
	 */
	/**
	 * create both the graphical components and the event listeners
	 */
	private void createComponents() {
		//TODO - create all your components here.
		// note that you should have already defined your components as instance variables.
		frame = new JFrame("Music Player");
		fileChooser = new JFileChooser();
		tempoText = new JTextField("enter your tempo; default is 1");
		currentTimeSlider = new JSlider(0, 100, 0);
		currentTimeSlider.setPreferredSize(new Dimension(650, 50));
		currentTimeSlider.setMajorTickSpacing(10);
		currentTimeSlider.setMinorTickSpacing(5);
		currentTimeSlider.setPaintTicks(true);
		currentTimeSlider.setPaintLabels(true);
		play = new JButton("Play");
		play.setToolTipText("play the music");
		stop = new JButton("Stop");
		stop.setToolTipText("stop the music");
		pause = new JButton("Pause");
		pause.setToolTipText("press once to pause the music and twice to continue playing the music");
		load = new JButton("Load");
		load.setToolTipText("load the music you want to play");
		reverse = new JButton("Reverse");
		reverse.setToolTipText("reverse the notes' order in the music");
		up = new JButton("Up");
		up.setToolTipText("octave up");
		down = new JButton("Down");
		down.setToolTipText("octave down");
		change = new JButton("Change Tempo");
		change.setToolTipText("change the current tempo");
		Panel1 = new JPanel();
		Panel2 = new JPanel();
		Panel3 = new JPanel();
		Panel4 = new JPanel();
		Panel5 = new JPanel();
		Panel6 = new JPanel();
		statusLabel = new JLabel("Welcome to the Music Player!");
		currentTimeLabel = new JLabel("000000.0 /");
		totalTimeLabel = new JLabel("000000.0 sec");
		tempo = new JLabel("Tempo:");
		play.addActionListener(this);
		stop.addActionListener(this);
		pause.addActionListener(this);
		load.addActionListener(this);
		reverse.addActionListener(this);
		up.addActionListener(this);
		down.addActionListener(this);
		change.addActionListener(this);
		doEnabling();
	}

	/*
	 * Sets whether every button, slider, spinner, etc. should be currently
	 * enabled, based on the current state of whether a song has been loaded and
	 * whether or not it is currently playing. This is done to prevent the user
	 * from doing actions at inappropriate times such as clicking play while the
	 * song is already playing, etc.
	 */
	/**
	 * set whether each graphical component is enabled or not
	 */
	private void doEnabling() {
		//TODO - figure out which buttons need to enabled
		if (loading == false) {
			play.setEnabled(false);
			stop.setEnabled(false);
			pause.setEnabled(false);
			load.setEnabled(true);
			fileChooser.setEnabled(true);
			reverse.setEnabled(false);
			up.setEnabled(false);
			down.setEnabled(false);
			change.setEnabled(false);
			tempoText.setEnabled(false);
			currentTimeSlider.setEnabled(false);
			currentTimeLabel.setEnabled(true);
			totalTimeLabel.setEnabled(true);
			statusLabel.setEnabled(true);
			tempo.setEnabled(true);
		} else {
			if (playing == false) {
				play.setEnabled(true);
				stop.setEnabled(true);
				pause.setEnabled(true);
				load.setEnabled(true);
				fileChooser.setEnabled(true);
				reverse.setEnabled(true);
				up.setEnabled(true);
				down.setEnabled(true);
				change.setEnabled(true);
				tempoText.setEnabled(true);
				currentTimeSlider.setEnabled(false);
				currentTimeLabel.setEnabled(true);
				totalTimeLabel.setEnabled(true);
				statusLabel.setEnabled(true);
				tempo.setEnabled(true);
			} else {
				play.setEnabled(false);
				stop.setEnabled(true);
				pause.setEnabled(true);
				load.setEnabled(false);
				fileChooser.setEnabled(false);
				reverse.setEnabled(false);
				up.setEnabled(false);
				down.setEnabled(false);
				change.setEnabled(false);
				tempoText.setEnabled(false);
				currentTimeSlider.setEnabled(true);
				currentTimeLabel.setEnabled(true);
				totalTimeLabel.setEnabled(true);
				statusLabel.setEnabled(true);
				tempo.setEnabled(true);
			}
		}	
	}

	/*
	 * Performs layout of the components within the graphical window. 
	 * Also make the window a certain size and put it in the center of the screen.
	 */
	/**
	 * set the components' layout in the graphical window
	 */
	private void doLayout() {
		//TODO - figure out how to layout the components
		Panel1.setLayout(new GridLayout(2,1));
		Panel1.add(currentTimeLabel);
		Panel1.add(totalTimeLabel);
		Panel2.setLayout(new FlowLayout());
		Panel2.add(play);
		Panel2.add(stop);
		Panel2.add(pause);
		Panel3.setLayout(new FlowLayout());
		Panel3.add(load);
		Panel3.add(reverse);
		Panel3.add(up);
		Panel3.add(down);
		Panel4.setLayout(new FlowLayout());
		Panel4.add(tempo);
		Panel4.add(tempoText);
		Panel4.add(change);
		Panel5.setLayout(new FlowLayout());
		Panel5.add(currentTimeSlider);
		Panel5.add(Panel1);
		Panel6.setLayout(new GridLayout(1,3));
		Panel6.add(new JPanel());
		Panel6.add(statusLabel);
		Panel6.add(new JPanel());
		frame.setLayout(new GridLayout(5,1));
		frame.add(Panel6);
		frame.add(Panel5);
		frame.add(Panel2);
		frame.add(Panel3);
		frame.add(Panel4);
		frame.pack();
		frame.setSize(780, 300);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	}

	/*
	 * Returns the estimated current time within the overall song, in seconds.
	 */
	/**
	 * get the estimated current time for the song
	 * @return the estimated current time
	 */
	private double getCurrentTime() {
		String timeStr = currentTimeLabel.getText();
		timeStr = timeStr.replace(" /", "");
		try {
			return Double.parseDouble(timeStr);
		} catch (NumberFormatException nfe) {
			return 0.0;
		}
	}

	/*
	 * Pops up a file-choosing window for the user to select a song file to be
	 * loaded. If the user chooses a file, a Song object is created and used
	 * to represent that song.
	 */
	/**
	 * pop up a window for the user to load the file of a song
	 * @throws IOException
	 */
	private void loadFile() throws IOException {
		if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File selected = fileChooser.getSelectedFile();
		if (selected == null) {
			return;
		}
		statusLabel.setText("Current song: " + selected.getName());
		String filename = selected.getAbsolutePath();
		System.out.println("Loading song from " + selected.getName() + " ...");

		//TODO - create a song from the file
		song = new Song(filename);

		tempoText.setText("1.0");
		setCurrentTime(0.0);
		updateTotalTime();
		System.out.println("Loading complete.");
		System.out.println("Song: " + song);
		loading = true;
		doEnabling();
	}

	/*
	 * Initiates the playing of the current song in a separate thread (so
	 * that it does not lock up the GUI). 
	 * You do not need to change this method.
	 * It will not compile until you make your Song class.
	 */
	/**
	 * play the song in a separate thread
	 */
	private void playSong() {
		if (song != null) {
			setCurrentTime(0.0);
			Thread playThread = new Thread(new Runnable() {
				public void run() {
					StdAudio.setMute(false);
					playing = true;
					doEnabling();
					String title = song.getTitle();
					String artist = song.getArtist();
					double duration = song.getTotalDuration();
					System.out.println("Playing \"" + title + "\", by "
							+ artist + " (" + duration + " sec)");
					song.play();
					System.out.println("Playing complete.");
					playing = false;
					doEnabling();
				}
			});
			playThread.start();
		}
	}

	/*
	 * Sets the current time display slider/label to show the given time in
	 * seconds. Bounded to the song's total duration as reported by the song.
	 */
	/**
	 * make the given time shown through the current time display slider and label
	 * @param time, the given time
	 */
	private void setCurrentTime(double time) {
		double total = song.getTotalDuration();
		time = Math.max(0, Math.min(total, time));
		currentTimeLabel.setText(String.format("%08.2f /", time));
		currentTimeSlider.setValue((int) (100 * time / total));
	}

	/*
	 * Updates the total time label on the screen to the current total duration.
	 */
	/**
	 * update the total time label to show the total duration of the current song
	 */
	private void updateTotalTime() {
		//TODO - fill this
		double totalTime = song.getTotalDuration();
		totalTimeLabel.setText(String.format("%08.2f sec", totalTime));
	}
}
