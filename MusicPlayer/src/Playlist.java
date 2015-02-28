import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.*;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.media.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuItem;

@XStreamAlias("playlist")
public class Playlist{
	@XStreamAlias("OPlaylist")
	public ObservableList<Song> playlist = FXCollections.observableArrayList();
	@XStreamAlias("name")
    public String name;
	@XStreamAlias("button")
    public Button button;
	@XStreamAlias("titleName")
    public final SimpleStringProperty titleName = new SimpleStringProperty(name);
	@XStreamAlias("hbox")
    public HBox hbox = new HBox(10);
	@XStreamAlias("menuItem")
    public MenuItem pMenuItem;
	@XStreamAlias("indexOfPlaying")
    public int indexOfPlaying = 0;
	public boolean shuffle =false;

    public String toString() {
        return (name);
    }
    public Playlist(String l) {
        name = l;
        setTitleName(name);
        button = new Button(l);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.getChildren().add(button);
        setMenuItem(name);
    }
    public Playlist() {
        name = "Blank Playlist";
        button = new Button(name);
        setTitleName(name);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.getChildren().add(button);
        setMenuItem(name);
    }
    public void add(Song s) {
        this.playlist.add(s);
        //addNextListener(s);
    }
    public void remove(int i){
    	this.playlist.remove(i);
    }
    public void remove(Song s){
    	playlist.remove(indexOf(s));
    }
    public void addAll(ArrayList<Song> aS) {
        this.playlist.addAll(aS);
        for (int i = 0; i < playlist.size() - 1; i++) {
            //addNextListener(playlist.get(i));
        }
    }
    public void setMenuItem(String n) {
        pMenuItem = new MenuItem(n);
    }
    public void setMenuOnAction(Song s) {
        pMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                thisPlaylist().add(s);

            }
        });
    }
    public String getTitleName() {
        return titleName.get();
    }
    public void addNextListener(Song s) {
    	s.setSong();
        MediaPlayer player = s.song;
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
            		s.song.stop();
                indexOfPlaying = getIndexOfPlaying();
                
                MusicPlayer.nextButton.fire();
            }
        });
    }
    public void setTitleName(String fname) {
        titleName.set(fname);
    }
    public void addAll(ObservableList<Song> oS) {
        this.playlist.addAll(oS);
    }
    public void clear() {
        this.playlist.clear();
    }
    public int size() {
        return (this.playlist.size());
    }
    public void start() {
        //System.out.println(indexOfPlaying+"<<<< INDEX 1 START");
        //indexOfPlaying=getIndexOfPlaying();
        //System.out.println(indexOfPlaying+"<<<< INDEX 2 START");
        playlist.get(indexOfPlaying).play();
        
        setActionText();
    }
    public void append(Playlist pL) {
        clear();
        addAll(pL.playlist);
    }
    public void setIndexOfPlaying(final int i) {
        indexOfPlaying = i;
    }
    public void setActionText() {
        MusicPlayer.playAction.setText("Playing song: \n" + songPlaying());
    }
    public void play(Song s) {
    		s.setSong();
        removeListeners();
        //indexOfPlaying=getIndexOfPlaying();
        //System.out.println(indexOfPlaying + "<<< INDEX 1 SONG PLAY");
        try {
            stop();
            indexOfPlaying = this.indexOf(s);
            //playlist.get(indexOfPlaying).play();
            //System.out.println(indexOfPlaying + "<<< INDEX 2 SONG PLAY");
            setActionText();
        } catch (Exception e) {
            System.out.println("Error playing! " + e.toString());
        }
    }
    public void play(int i) {
		get(i).setSong();
        removeListeners();
        
        //indexOfPlaying=getIndexOfPlaying();

            //System.out.println(indexOfPlaying + "<<< INDEX 1 INT PLAY");

        try {
            stop();
            indexOfPlaying = i;

            System.out.println(indexOfPlaying + "<<< INDEX 2 INT PLAY");
           // get(indexOfPlaying).play();
            playlist.get(indexOfPlaying).song.currentTimeProperty().addListener(playlist.get(indexOfPlaying).progresschangelistener);
            setActionText();
        } catch (Exception e) {
            System.out.println("Error playing! " + e.toString());
        }
    }
    public Song get(int i) {
        return (playlist.get(i));
    }
    public Song songPlaying() {
        //indexOfPlaying = getIndexOfPlaying();
        System.out.println(indexOfPlaying+"<<<<<< INDEX OF PLAYING 1");
        return (get(indexOfPlaying));
    }
    public void stop() {
        //playlist.get(indexOfPlaying).stop();
        for (Song s : playlist) {
        		if(s.song!=null){
        			s.song.stop();
        		}
            
        }
    }
    public void pause() {
        //indexOfPlaying = getIndexOfPlaying();
        //System.out.println(indexOfPlaying + "<<< INDEX");
       songPlaying().pause();

        //System.out.println(indexOfPlaying + "<<< INDEX2");
    }
    public void next() {
        removeListeners();
        stop();
        if (indexOfPlaying < playlist.size() - 1 && shuffle ==false) {
            indexOfPlaying ++;
            System.out.println(indexOfPlaying+"<<<<<< INDEX OF PLAYING 1");

        } else {
            indexOfPlaying = 0;
        }
        if(shuffle==true){
        		indexOfPlaying=(int)(Math.random()*playlist.size()-1);
        }
        play(indexOfPlaying);
        setActionText();
    }
    public void previous() {
        removeListeners();
        stop();
        if (indexOfPlaying - 1 == -1 && shuffle ==false) {
            indexOfPlaying = this.playlist.size() - 1;
        } else {
            indexOfPlaying--;
        }
        if(shuffle==true){
    		indexOfPlaying=(int)(Math.random()*playlist.size()-1);
    }
        play(indexOfPlaying);
        setActionText();
    }
    public int indexOf(Song s) {
        return (playlist.indexOf(s));
    }
    public boolean contains(Song s) {
        boolean returned = false;
        if (indexOf(s) != -1) {
            returned = true;
        }
        return (returned);
    }
    public int getIndexOfPlaying() {
        int returned = indexOfPlaying;
        for (Song s : playlist) {
            if (s.playing == true) {
                returned = (indexOf(s));
            }
        }
        return (returned);
    }
    public Playlist thisPlaylist() {
        return (this);
    }
    public void setOnAction(TableView<Song> t) {
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                t.setItems(thisPlaylist().playlist);
            }
        });
    }
    public void removeListeners() {

        for (Song s : playlist) {
            try {
                s.song.currentTimeProperty().removeListener(s.progresschangelistener);
            } catch (Exception d) {
                //System.out.println("ERROR REMOVING: " + d.toString());
            }

        }
    }
	public Song indexOf(String text) {
		Song returned= get(1);
		for(Song s: playlist){
			if(s.getTitleName().toLowerCase().indexOf(text)!=-1){
				returned=s;
			}
			
		}
		return returned;
	}
}
