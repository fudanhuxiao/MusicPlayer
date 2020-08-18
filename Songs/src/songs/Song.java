package songs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Song class: read the text into music.
 * @author Xiao Hu and Chen Lin
 *
 */
public class Song {

	private Note[] noteArray;//record the notes of this song into this array. noteArray = new Note[numOfNotes].
	private String title;//record the title of this song. (first line of the txt.)
	private String artist;//record the artist of this song. (second line of the txt.)
	private int numOfNotes;//record the number of notes in this song. (third line of the txt.)

	/**
	 * read the file and store the first line into title, second line into artist, third line into numOfNotes.
	 * from fourth line until the end, store the info into Note, all Notes are recorded in noteArray.
	 * @param filename
	 */
	public Song(String filename) {
		File file = new File(filename);
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			title = reader.readLine();
			artist = reader.readLine();
			numOfNotes = Integer.parseInt(reader.readLine());
			noteArray = new Note[numOfNotes];
			for (int i=0; i<numOfNotes; i++) {
				String line = reader.readLine();
				if (line == null)
					break;
				line = line.trim();
				if (line.equals(""))
					continue; // ignore possible blank lines
				String[] noteInfo = line.split(" ");
				if (noteInfo[1].equals("R")) {
					noteArray[i] = new Note(Double.valueOf(noteInfo[0]),noteInfo[2].equals("true"));
				} else {
					Pitch pit = Pitch.valueOf(noteInfo[1]) ;
					Accidental acc = Accidental.valueOf(noteInfo[3]);
					noteArray[i] = new Note(Double.valueOf(noteInfo[0]),pit,Integer.parseInt(noteInfo[2]),acc,noteInfo[4].equals("true")); 
				}
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Returns the title of the song.
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the artist.
	 * @return
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * return the total duration (length) of the song in seconds.
	 * first calculated the durations of the song's notes, then calculated the repeated duration,
	 * and added then together.
	 * @return
	 */
	public double getTotalDuration() {
		double totalduration=0;
		for (int i=0; i<numOfNotes; i++) {
			totalduration += noteArray[i].getDuration();
		}
		for (int i=0; i<numOfNotes; i++) {
			if (noteArray[i].isRepeat()) {
				do {
					totalduration += noteArray[i].getDuration();
					i++;
				} while (!noteArray[i].isRepeat());
				totalduration += noteArray[i].getDuration();
			}
		}
		return totalduration;
	}

	/**
	 * 1. store the correct sequence of notes to play into an ArrayList<Note> notePlaylist;
	 * 2. play every note in notePlaylist;
	 */
	public void play() {
		ArrayList<Note> notePlaylist = new ArrayList<Note>();
		for (int i=0; i<numOfNotes; i++) {
			if (noteArray[i].isRepeat()) {
				int startnum = i;
				do {
					notePlaylist.add(noteArray[i]);
					i++;
				} while(!noteArray[i].isRepeat());
				int endnum = i;
				notePlaylist.add(noteArray[i]);
				for (int j=startnum; j<=endnum; j++) {
					notePlaylist.add(noteArray[j]);
				}
			} else {
				notePlaylist.add(noteArray[i]);
			}
		}
		for (int k=0; k<notePlaylist.size();k++) {
			notePlaylist.get(k).play();
		}
	}

	/**
	 * modify the noteArray so that every note have 1 octave lower than their current state
	 * (except a note with octave 1, or a rest.)
	 * @return whether an octave was made lowered.
	 */
	public boolean octaveDown() {
		boolean down = false;
		for (int i=0; i<numOfNotes; i++) {
			if ((noteArray[i].getOctave()>1) && (!noteArray[i].isRest())) {
				noteArray[i].setOctave(noteArray[i].getOctave()-1);
				down = true;
			}
		}
		if (down) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * raises the octave by 1.(except a rest or a note with octave 10)
	 * @return whether an octave was raised.
	 */
	public boolean octaveUp() {
		boolean up =false;
		for (int i=0; i<numOfNotes; i++) {
			if ((noteArray[i].getOctave()<10) && (!noteArray[i].isRest())) {
				noteArray[i].setOctave(noteArray[i].getOctave()+1);
				up = true;
			}
		}
		if (up) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * change the duration of each note in the song by the given ratio. 
	 * print a message if the ratio<=0.
	 * @param ratio
	 */
	public void changeTempo(double ratio) {
		if (ratio<=0) {
			System.out.println("Ratio<=0 is not accepted! Tempo remains unchanged!");
		} else {
			for (int i=0; i<numOfNotes; i++) {
				noteArray[i].setDuration(noteArray[i].getDuration()*ratio);
			}
		}
	}

	/**
	 * reverse the order of the notes in the song.
	 */
	public void reverse() {
		for (int i=0; i<(numOfNotes/2); i++) {
			Note tempnote1 = noteArray[i];
			Note tempnote2 = noteArray[numOfNotes-1-i];
			noteArray[i] = tempnote2;
			noteArray[numOfNotes-1-i] = tempnote1;
		}
	}

	/**
	 * the toString returns all elements in the noteArray.
	 * so that every note of the song would be returned.
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Arrays.toString(noteArray);
	}
}
