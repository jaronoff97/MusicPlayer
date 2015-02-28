import java.io.File;
import java.net.URI;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.worldsworstsoftware.itunes.ItunesTrack;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
@XStreamAlias("song")
public class Song {
	@XStreamAlias("mediaSong")
    public MediaPlayer song;
	@XStreamAlias("media")
    public Media media;
    public int x, y;
	@XStreamAlias("uriPointer")
    public URI pointer;
    public int width, height;
    public String duration;
	@XStreamAlias("duration")
    public Duration dur;
    public int totalSeconds;
    public int originalOrder;
    public boolean addedEQlistener=false;
    public String title, artist, album;
	@XStreamAlias("orderName")
    public final SimpleIntegerProperty orderName = new SimpleIntegerProperty(originalOrder);
	@XStreamAlias("TitleName")
    public final SimpleStringProperty titleName = new SimpleStringProperty(title);
	@XStreamAlias("artistName")
    public final SimpleStringProperty artistName = new SimpleStringProperty(artist);
	@XStreamAlias("albumName")
    public final SimpleStringProperty albumName = new SimpleStringProperty(album);
	@XStreamAlias("durationName")
    public final SimpleStringProperty durationName = new SimpleStringProperty(duration);
	@XStreamAlias("albumCover")
    public ImageView albumCover;
	@XStreamAlias("file")
    public File file;
    private static final String DEFAULT_IMG_URL = Song.class.getResource("CSS_skins/resources/defaultAlbum.png").toString();
	@XStreamAlias("albumDefault")
    private static final javafx.scene.image.Image DEFAULT_ALBUM_COVER = new javafx.scene.image.Image(DEFAULT_IMG_URL.toString());

	@XStreamAlias("readonlyMediaplayer")
	private final ReadOnlyObjectWrapper<MediaPlayer> mediaPlayer = new ReadOnlyObjectWrapper<MediaPlayer>(this, "mediaPlayer");

	@XStreamAlias("button")
    public Button bSong;
	@XStreamAlias("label")
    public Label lSong;
	@XStreamAlias("songAction")
    public Text songAction = new Text();
	@XStreamAlias("hBOX")
    public HBox hSong;
    public boolean playing = false;
    public int currentTime = 0;
    public boolean songAtEnd = false;

    public int trackID = -1;
    public String genre = null;
    public String kind = null;
    public int size = -1;
    public int totalTime = -1;
    public int trackNumber = -1;
    public int trackCount = -1;
    public int year = -1;
    public String dateModified = null;
    public String dateAdded = null;
    public int bitRate = -1;
    public int sampleRate = -1;
    public String comments = null;
    public int playCount = -1;
    public long playDate = -1;
    public String playDateUTC = null;
    public String persistentID = null;
    public String trackType = null;
    public String location = null;
    public int fileFolderCount = -1;
    public int libraryFolderCount = -1;
    public boolean disabled = false;        	
	public int skipCount = -1;
	public String skipDate = null;
	public String composer = null;
	public String albumArtist = null;
	public int artworkCount = -1;
	public String grouping = null;
	public int discNumber = -1;
	public int discCount = -1;
	public int BPM = -1;
	public int playlistTrackNumber = -1;

    public String path;

	@XStreamAlias("progressListener")
    public ChangeListener<Duration> progresschangelistener;


    public String toString() {
        return ("Name: " + title + "\n Artist: " + artist + "\n Album: " + album +"\n Number: "+originalOrder);
        //return ("--");
    }
    protected String copyString(String value)
    {
    	if (value == null)
    	{
    		return null;
    	}
    	else
    	{
    		return new String(value);
    	}
    }
    public Song(){
        
    }
    public Song(ItunesTrack obj, int oO){
    		trackID = obj.getTrackID();
        title = copyString(obj.getName());
        artist = copyString(obj.getArtist());
        album = copyString(obj.getAlbum());
        genre = copyString(obj.getGenre());
        kind = copyString(obj.getKind());
        size = obj.getSize();
        totalTime = obj.getTotalTime();
        trackNumber = obj.getTrackNumber();
        trackCount = obj.getTrackCount();
        year = obj.getYear();
        dateModified = copyString(obj.getDateModified());
        dateAdded = copyString(obj.getDateAdded());
        bitRate = obj.getBitRate();
        sampleRate = obj.getSampleRate();
        comments = copyString(obj.getComments());
        playCount = obj.getPlayCount();
        playDate = obj.getPlayDate();
        playDateUTC = copyString(obj.getPlayDateUTC());
        persistentID = copyString(obj.getPersistentID());
        trackType = copyString(obj.getTrackType());
        location = copyString(obj.getLocation());
        fileFolderCount = obj.getFileFolderCount();
        libraryFolderCount = obj.getLibraryFolderCount();
        playlistTrackNumber = obj.getPlaylistTrackNumber();
        originalOrder=oO;
        try {
			pointer= new URI(location);
			path=pointer.toASCIIString();
			System.out.println(toString());
		} catch (Exception e) {
			
		}
        
        setTitleName(title);
        setArtistName(artist);
        setAlbumName(album);
        setOrderName(originalOrder);
    }
    public Song( URI f, int oO) {
        resetProperties();
        //song = makeMediaPlayerFromURI(f);
        //media = song.getMedia();
        originalOrder = oO;
        path = f.toASCIIString();
        //System.out.println("TRYING TO DO THE FOLLOWING: "+path);
        pointer = f;
        System.out.println("Made Song");
        setURL(path);
        //setOnReady();
        setOnEndOfMedia();


        //System.out.println(this);
    }
    public void setOnReady() {
        song.setOnReady(new Runnable() {

            @Override
            public void run() {
                currentTime = (int)song.getCurrentTime().toSeconds();
                Duration t = song.getTotalDuration();
                int minTime = (int) t.toMinutes();
                int secTime = (int) t.toSeconds();
                totalSeconds = secTime;
                if (secTime / 60 >= 1) {
                    secTime %= 60;
                }
                if (minTime / 60 >= 1) {
                    minTime %= 60;
                }
                duration = minTime + ":" + secTime;
            }
        });
    }
    public void setOnEndOfMedia() {
        song.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                songAtEnd = true;
            }
        });
    }
    public MediaPlayer makeMediaPlayerFromURI(URI f){
    	return(new MediaPlayer(new Media(f.toString())));
    }
    public MediaPlayer getSong(){
    	return(makeMediaPlayerFromURI(pointer));
    }
    public void setSong(){
    	song=makeMediaPlayerFromURI(pointer);
    }
    public void handleMetadata(String key, Object value) {
        if (key.equals("album")) {
            album = value.toString();
            //System.out.println(album);
        }
        if (key.equals("artist")) {
            artist = value.toString();
            //System.out.println(artist);
        }
        if (key.equals("title")) {
            title = value.toString();
            //System.out.println(title);
            //System.out.println("-------------------");
        }
        if (key.equals("image")) {
            //albumCover.setImage((javafx.scene.image.Image) value);
        }
        hSong = new HBox(10);
        setDurationName("");
        setTitleName(title);
        setArtistName(artist);
        setAlbumName(album);
        setOrderName(originalOrder);
        //System.out.println(getOrderName());
    }
    public String getTitleName() {
        return titleName.get();
    }
    public String getArtistName() {
        return artistName.get();
    }
    public String getAlbumName() {
        return albumName.get();
    }
    public int getOrderName() {
        return orderName.get();
    }
    public String getDurationName() {
        return durationName.get();
    }
    public void setURL(String url) {
        if (mediaPlayer.get() != null) {
            mediaPlayer.get().stop();
        }

        initializeMedia(url);
    }
    public void setTitleName(String fname) {
        titleName.set(fname);
    }
    public void setArtistName(String fname) {
        artistName.set(fname);
    }
    public void setAlbumName(String fname) {
        albumName.set(fname);
    }
    public void setDurationName(String fname) {
        int minTime = (int) song.getTotalDuration().toMinutes();
        int secTime = (int) song.getTotalDuration().toSeconds();
        if (secTime / 60 >= 1) { // this are to display later something like a clock 19:02:20
            secTime %= 60; //if you want just the time in minutes use only the toMinutes()
        }
        if (minTime / 60 >= 1) {
            minTime %= 60;
        }
        String label = minTime + ":" + secTime;
        //System.out.println(label);
        durationName.set(label);
    }
    public void setOrderName(int oO) {
        orderName.set(oO);
    }

    public void play() {
        song.play();
        playing = true;
    }
    public void stop() {
        song.stop();
        playing = false;
    }
    public void pause() {
        song.pause();
        playing = false;
    }
    public void mute() {
        if (this.song.isMute() == false) {
            this.song.setMute(true);
        }
        if (this.song.isMute() == true) {
            this.song.setMute(false);
        }
    }
    private void initializeMedia(String url) {
        resetProperties();
        song=getSong();
        try {
            final Media media = new Media(url);
            media.getMetadata().addListener(new MapChangeListener<String, Object>() {
                @Override
                public void onChanged(Change<? extends String, ? extends Object> ch) {
                    if (ch.wasAdded()) {

                        handleMetadata(ch.getKey(), ch.getValueAdded());

                    }
                }
            });

            mediaPlayer.setValue(new MediaPlayer(media));
            mediaPlayer.get().setOnError(new Runnable() {
            	
                @Override
                public void run() {
                    String errorMessage = mediaPlayer.get().getError().getMessage();
                    // Handle errors during playback
                    System.out.println("MediaPlayer Error: " + errorMessage);
                }
            });
        } catch (RuntimeException re) {
            // Handle construction errors
            System.out.println("Caught Exception: " + re.getMessage());
        }
    }
    private void resetProperties() {
        setArtistName("");
        setAlbumName("");
        setTitleName("");
        setAlbumCover(DEFAULT_ALBUM_COVER);
    }
    public String toURIString() {
        return (file.toURI().toString());
    }
    public void setAlbumCover(javafx.scene.image.Image value) {
        albumCover = new ImageView(value);
    }
    public int getCurrentMinuteTime() {
        return ((int)this.song.getCurrentTime().toMinutes());
    }
    public Duration getCurrentTime() {
        return (this.song.getCurrentTime());
    }
    public void seek(Duration d) {
        song.seek(d);
    }
    public Duration getDuration() {
        return (song.getMedia().getDuration());
    }
    public void setVolume(double d) {
        song.setVolume(d);
    }
    public double getVolume() {
        return (song.getVolume());
    }
    public MediaPlayer getMediaPlayer() {
        return (song);
    }
    public void setProgressListener(ChangeListener<Duration> pCL){
        progresschangelistener=pCL;
    }
}
