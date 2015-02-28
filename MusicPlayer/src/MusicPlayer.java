import java.awt.Desktop;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.worldsworstsoftware.itunes.ItunesLibrary;
import com.worldsworstsoftware.itunes.ItunesPlaylist;
import com.worldsworstsoftware.itunes.ItunesTrack;
import com.worldsworstsoftware.itunes.parser.ItunesLibraryParser;
import com.worldsworstsoftware.itunes.parser.logging.DefaultParserStatusUpdateLogger;
import com.worldsworstsoftware.itunes.parser.logging.ParserStatusUpdateLogger;
import com.worldsworstsoftware.itunes.util.ItunesLibraryFinder;
import com.worldsworstsoftware.logging.DefaultStatusUpdateLogger;
import com.worldsworstsoftware.logging.StatusUpdateLogger;
@SuppressWarnings("unused")
public class MusicPlayer extends Application {
    private Desktop desktop = Desktop.getDesktop();

    public static BorderPane musicBorder = new BorderPane();

    public static ProgressBar progress = new ProgressBar();
    public static ChangeListener<Duration> progressChangeListener;
    public static Text currentTime = new Text("");

    public static ObservableList<Song> dragged = FXCollections.observableArrayList();

    public static GridPane musicButtonGrid = new GridPane();
    public static ListView<Playlist> playlistTable = new ListView<Playlist>();

    public static TableView<Song> musicTable = new TableView<Song>();

    public static File musicDir = new File("src/Dir");

    public static Slider songSlider = new Slider();

    public static Slider[] sliders = new Slider[10];
    public static javafx.scene.text.Text[] sliderText = new Text[10];

    public static VBox topBar = new VBox();

    public static PlayerControl mediaControl;

    //public static final Label timeSliderLabel = new Label("Time: ");
    public static Slider timeSlider = new Slider();

    public static Slider volumeSlider = new Slider(0, 1, 1);

    public static ArrayList<Playlist> playlists = new ArrayList<Playlist>();

    public static ObservableList<Playlist> oPlaylists = FXCollections.observableArrayList(playlists);

    public static ArrayList<Song> songsDir = new ArrayList<Song>();
    public static Button playPauseButton = new Button("Play/ Pause");
    public static HBox hplayPauseButton = new HBox(10);
    public static Text playAction = new Text();
    public static Button nextButton = new Button("Next Song");
    public static HBox hnextButton = new HBox(10);
    public static Button prevButton = new Button("Previous Song");
    public static HBox hprevButton = new HBox(10);
    public static Button addMP3Button = new Button("Add MP3's");
    public static HBox haddMP3Button = new HBox(10);
    public static RadioButton shufflePlayList = new RadioButton("Shuffle?");
    public static HBox hShufflePlayList = new HBox(10);
    public static Button resetEQ = new Button("Reset equalizers");
    public static HBox hresetEQ = new HBox(10);

    public static Label lSearchBox = new Label("Search:");
    public static TextField searchBox = new TextField ();
    public static HBox hSearchBox = new HBox(10);
    public static Button bSearchBox = new Button("Search");

    public static String defaultSkin ;
    public static String lightMetro;
    public static String currentSkin;

    public int index = 0;

    public static StatusUpdateLogger logger = new DefaultStatusUpdateLogger(true, System.out);
    public static String libraryLocation = ItunesLibraryFinder.findLibrary(logger);
    public static DefaultParserStatusUpdateLogger logger2 = new DefaultParserStatusUpdateLogger(true, System.out);
    public static ItunesLibrary itunesLibrary;


    @SuppressWarnings("unchecked")
    final Entry<String, Effect>[] effects = new Entry[] {
        new SimpleEntry<String, Effect>("LightMetro", new SepiaTone()),
        new SimpleEntry<String, Effect>("DarkMetro", new DropShadow())
    };

    public static class Record {
        private final SimpleIntegerProperty id;
        private final SimpleIntegerProperty value_0;
        private final SimpleIntegerProperty value_1;
        private final SimpleIntegerProperty value_2;
        private final SimpleIntegerProperty value_3;
        private final SimpleIntegerProperty value_4;

        Record(int i, int v0, int v1, int v2, int v3,
               int v4) {
            this.id = new SimpleIntegerProperty(i);
            this.value_0 = new SimpleIntegerProperty(v0);
            this.value_1 = new SimpleIntegerProperty(v1);
            this.value_2 = new SimpleIntegerProperty(v2);
            this.value_3 = new SimpleIntegerProperty(v3);
            this.value_4 = new SimpleIntegerProperty(v4);
        }
        public int getId() {
            return id.get();
        }

        public void setId(int v) {
            id.set(v);
        }

        public int getValue_0() {
            return value_0.get();
        }

        public void setValue_0(int v) {
            value_0.set(v);
        }

        public int getValue_1() {
            return value_1.get();
        }

        public void setValue_1(int v) {
            value_1.set(v);
        }

        public int getValue_2() {
            return value_2.get();
        }

        public void setValue_2(int v) {
            value_2.set(v);
        }

        public int getValue_3() {
            return value_3.get();
        }

        public void setValue_3(int v) {
            value_3.set(v);
        }

        public int getValue_4() {
            return value_4.get();
        }

        public void setValue_4(int v) {
            value_4.set(v);
        }

    };
    public static Playlist queue = new Playlist("Queue");
    public static Playlist currentList = new Playlist();
    public static boolean playListReady = false;


    public static FileChooser addMP3Choose = new FileChooser();


    public static void main(String[] args) {
        launch(args);
        libraryLocation = args[0];
    }
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
    @Override
    public void start(Stage primaryStage) throws URISyntaxException {

        logger2.setTrackParseUpdateFrequency(200);

        logger2.setPlaylistParseUpdateFrequency(ParserStatusUpdateLogger.UPDATE_FREQUENCY_ALWAYS); //we could also just do 1..
        itunesLibrary  = ItunesLibraryParser.parseLibrary(libraryLocation, logger2);
        System.out.println("HELLO THIS IS WORKING @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(".. and your library has " + itunesLibrary.getTracks().size() + " tracks and " + itunesLibrary.getPlaylists().size() + " playlists.");
        System.out.println("HELLO THIS IS WORKING @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        Random rand = new Random();

        Set trackIds = itunesLibrary.getTracks().keySet();

        Integer trackId = null;
        Iterator it = trackIds.iterator();
        for (int i = 0; i < (itunesLibrary.getTracks().size()); i++) {
            trackId = (Integer) it.next();
            ItunesTrack track = itunesLibrary.getTrackById(trackId.intValue());
            Song s = new Song(track, index);
            if (s != null) {
                if (s.pointer != null) {
                    if (s.genre != "Podcast") {
                        songsDir.add(s);
                    }

                }

            }
            index++;

        }



        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        try {

        } catch (Exception e) {
            e.printStackTrace();// TODO: handle exception
        }
        defaultSkin = getClass().getResource("/CSS_skins/DefaultSkin.css").toExternalForm();

        lightMetro = getClass().getResource("/CSS_skins/LightMetro.css").toExternalForm();
        currentSkin = defaultSkin;

        getMusicDirectory();
        Callback<TableColumn, TableCell> cellFactory =
        new Callback<TableColumn, TableCell>() {

            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuView = new Menu("View");
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);

        musicTable.setEditable(true);
        TableColumn<Song, String> songCol = new TableColumn("Song Name");
        songCol.setCellValueFactory(new PropertyValueFactory<Song, String>("titleName"));
        songCol.setPrefWidth(300);
        TableColumn<Song, String> artistCol = new TableColumn("Artist Name");
        artistCol.setCellValueFactory(new PropertyValueFactory<Song, String>("artistName"));
        artistCol.setPrefWidth(200);
        TableColumn<Song, String> albumCol = new TableColumn("Album Name");
        albumCol.setPrefWidth(300);

        albumCol.setCellValueFactory(new PropertyValueFactory<Song, String>("albumName"));
        TableColumn<Song, String> durationCol = new TableColumn("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<Song, String>("durationName"));
        TableColumn addToPlaylistCol = new TableColumn<Record, Boolean>("Add to Queue");
        addToPlaylistCol.setSortable(false);
        TableColumn<Song, String> orderCol = new TableColumn("Order Name");
        orderCol.setCellValueFactory(new PropertyValueFactory<Song, String>("orderName"));

        addToPlaylistCol.setCellValueFactory(
            new Callback<TableColumn.CellDataFeatures<Record, Boolean>,
        ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });

        addToPlaylistCol.setCellFactory(
        new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

            @Override
            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
                return new ButtonCell();
            }

        });
        addToPlaylistCol.setEditable(true);

        TableColumn playlistCol = new TableColumn("Name");
        playlistCol.setCellValueFactory(new PropertyValueFactory<Playlist, String>("titleName"));

        Scene musicScene = new Scene(musicBorder, 0, 0);
        for (int i = 0; i < 10; i++) {
            sliderText[i] = new Text();
        }
        for (int i = 0; i < 10; i++) {
            final int fi = i;
            sliderText[i].getStyleClass().add("slidertext");
            sliders[i] = new Slider(EqualizerBand.MIN_GAIN, EqualizerBand.MAX_GAIN, 0);
            sliders[i].setOrientation(Orientation.HORIZONTAL);
            sliders[i].valueProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
                    if (currentList.songPlaying().song != null) {
                        currentList.songPlaying().song.getAudioEqualizer().getBands().get(fi).setGain(newValue.doubleValue());
                    }
                }
            });
        }


        sliderText[0].setText("32 Hz");
        sliderText[1].setText("64 Hz");
        sliderText[2].setText("125 Hz");
        sliderText[3].setText("250 Hz");
        sliderText[4].setText("500 Hz");
        sliderText[5].setText("1 kHz");
        sliderText[6].setText("2 kHz");
        sliderText[7].setText("4 kHz");
        sliderText[8].setText("8 kHz");
        sliderText[9].setText("16 kHz");



        bSearchBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                musicTable.scrollTo(currentList.indexOf(searchBox.getText().toLowerCase()));
                musicTable.getSelectionModel().select(currentList.indexOf(searchBox.getText().toLowerCase()));
            }
        });


        hSearchBox.getChildren().addAll(searchBox, bSearchBox);
        hSearchBox.setSpacing(10);


        Button saveStuff = new Button("Save Stuff");
        HBox hSaveStuff = new HBox(10);

        hSaveStuff.getChildren().add(saveStuff);

        saveStuff.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                saveData();
            }
        });

        primaryStage.setTitle("jTunes");
        primaryStage.setScene(musicScene);
        musicTable.setId("#musicTable");
        musicScene.getStylesheets().add(currentSkin);
        musicButtonGrid.setAlignment(Pos.TOP_CENTER);
        musicButtonGrid.setHgap(10);
        musicButtonGrid.setVgap(10);
        musicButtonGrid.setPadding(new Insets(0, 0, 0, 0));


        hplayPauseButton.setAlignment(Pos.TOP_LEFT);
        hplayPauseButton.getChildren().add(playPauseButton);

        hnextButton.setAlignment(Pos.TOP_LEFT);
        hnextButton.getChildren().add(nextButton);

        hprevButton.setAlignment(Pos.TOP_LEFT);
        hprevButton.getChildren().add(prevButton);


        haddMP3Button.setAlignment(Pos.TOP_LEFT);
        haddMP3Button.getChildren().add(addMP3Button);

        hShufflePlayList.setAlignment(Pos.TOP_LEFT);
        hShufflePlayList.getChildren().add(shufflePlayList);

        progress.setPrefWidth(100);

        //musicButtonGrid.add(playAction, 6, 1);

        musicButtonGrid.add(hplayPauseButton, 2, 1);

        musicButtonGrid.add(hprevButton, 1, 1);

        musicButtonGrid.add(hnextButton, 4, 1);

        musicButtonGrid.add(haddMP3Button, 0, 1);

        musicButtonGrid.add(hSearchBox, 5, 1);

        musicButtonGrid.add(hShufflePlayList, 6, 1);

        Playlist library = new Playlist("Library");
        library.addAll(songsDir);

        currentList.append(library);

        //playlistTable.add(library.hbox, 0, 0);
        //playlistTable.add(queue.hbox, 0, 1);
        playlists.add(library);
        playlists.add(queue);
        oPlaylists.addAll(playlists);

        musicTable.setItems(currentList.playlist);
        musicTable.getColumns().addAll(orderCol, songCol, artistCol, albumCol, addToPlaylistCol);
        musicTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        playlistTable.setItems(oPlaylists);

        topBar.getChildren().add(menuBar);
        topBar.getChildren().add(musicButtonGrid);

        final VBox sidebar = new VBox();
        sidebar.setSpacing(5);
        sidebar.setPadding(new Insets(10, 0, 0, 10));
        sidebar.getChildren().addAll(playlistTable);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
                if (currentList.songPlaying().song != null) currentList.songPlaying().song.setVolume(newValue.doubleValue());
            }
        });

        GridPane equalizers = new GridPane();
        for (int y = 0; y < sliders.length; y++) {
            equalizers.add(sliders[y], 0, (y * 2) + 2);
            equalizers.add(sliderText[y], 0, (y * 2) + 1);
        }
        hresetEQ.setAlignment(Pos.TOP_CENTER);
        hresetEQ.getChildren().add(resetEQ);

        equalizers.add(new Text("Volume"), 0, 21);
        equalizers.add(volumeSlider, 0, 22);

        equalizers.add(hresetEQ, 0, 0);

        final VBox musicVBox = new VBox();
        musicVBox.setSpacing(5);
        musicVBox.setPadding(new Insets(10, 0, 0, 10));
        musicVBox.getChildren().addAll(new Label(""), musicTable);
        musicVBox.setLayoutX(0);
        musicVBox.setLayoutY(70);
        musicVBox.setPrefWidth(bounds.getWidth());
        musicVBox.setPrefHeight(bounds.getHeight());
        musicVBox.setVgrow(musicTable, Priority.ALWAYS);

        musicBorder.setCenter(musicVBox);
        musicBorder.setLeft(sidebar);
        musicBorder.setRight(equalizers);
        musicBorder.setTop(topBar);
        musicBorder.setBottom(mediaControl);
        primaryStage.setScene(musicScene);
        musicScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                musicTable.setPrefWidth((double)newSceneWidth);
                musicTable.setMaxWidth((double)newSceneWidth);

                musicButtonGrid.setPrefWidth((double)newSceneWidth);
                musicButtonGrid.setMaxWidth((double)newSceneWidth);
            }
        });
        musicScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                musicTable.setPrefHeight((double)newSceneHeight);
                musicTable.setMaxHeight((double)newSceneHeight);

                musicTable.setPrefHeight((double)newSceneHeight);
                musicTable.setMaxHeight((double)newSceneHeight);
            }
        });
        musicTable.setRowFactory(cb -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                final ContextMenu rightClickEvents = new ContextMenu();
                MenuItem rightClickPlay = new MenuItem("Play Song");
                rightClickPlay.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {

                        currentList.play(row.getItem());
                        playPauseButton.fire();
                        volumeSlider.setValue(0.9);
                    }
                });
                MenuItem rightClickPause = new MenuItem("Pause Song");
                rightClickPause.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {

                        if (currentList.songPlaying() == row.getItem()) {
                            playPauseButton.fire();
                        } else {

                        }
                    }
                });
                MenuItem remove = new MenuItem("Remove Song");
                remove.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        if (currentList.indexOf(row.getItem()) != -1) {
                            currentList.remove(row.getItem());
                        }
                    }
                });
                Menu rightClickAddToPlaylist = new Menu("Add to a playlist");
                rightClickAddToPlaylist.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        //rightClickAddToPlaylist.getItems().add(queue.pMenuItem);
                        for (Playlist plist : playlists) {
                            System.out.println("GOING THROUGH RIGHT CLICKING:    " + plist.name);
                            rightClickAddToPlaylist.getItems().add(plist.pMenuItem);
                            plist.setMenuOnAction(row.getItem());

                        }

                    }

                });
                rightClickEvents.getItems().add(rightClickPlay);
                rightClickEvents.getItems().add(rightClickPause);
                rightClickEvents.getItems().add(remove);
                rightClickEvents.getItems().add(rightClickAddToPlaylist);
                row.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        if (e.getButton() == MouseButton.SECONDARY)
                            rightClickEvents.show(row, e.getScreenX(), e.getScreenY());
                    }
                });

                if (ev.getClickCount() > 1) {
                    if (currentList.contains(row.getItem())) {
                        stopAll();
                        currentList.play(row.getItem());
                        volumeSlider.setValue(0.9);
                        playASong();
                        playPauseButton.fire();

                    }
                }

                if (musicTable.getSelectionModel().getSelectionMode() == SelectionMode.MULTIPLE) {
                    final ObservableList<Song> selection = musicTable.getSelectionModel().getSelectedItems();

                }

            });
            return row;
        });

        ObservableList<Song> tableContent = FXCollections.observableArrayList();

        musicTable.setOnMouseClicked(new EventHandler<MouseEvent>() { //click
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) { // double click

                    ObservableList<Song> selected = musicTable.getSelectionModel().getSelectedItems();
                    dragged = selected;
                    if (selected != null) {
                        System.out.println("select : " + selected);

                    }
                }
            }
        });

        musicTable.setOnDragDetected(new EventHandler<MouseEvent>() { //drag
            @Override
            public void handle(MouseEvent event) {
                // drag was detected, start drag-and-drop gesture
                ObservableList<Song> selected = musicTable.getSelectionModel().getSelectedItems();

                dragged = selected;
                if (dragged != null) {

                    Dragboard db = musicTable.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString("a Song");
                    db.setContent(content);
                    event.consume();
                }
            }
        });

        musicTable.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // data is dragged over the target
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        playlistTable.setEditable(true);
        playlistTable.setPrefWidth(200);
        playlistTable.setCellFactory(cb -> {
            ListCell<Playlist> cell = new TextFieldListCell<>();
            //musicTable.setItems(cell.getItem().playlist);
            cell.setOnDragOver(ev -> {
                if (ev.getGestureSource() != cell
                && ev.getDragboard().hasString()) {
                    ev.acceptTransferModes(TransferMode.COPY);
                }
                ev.consume();
            });

            cell.setOnMouseClicked(ev -> {
                try{

                    currentList.append(cell.getItem());
                    musicTable.setItems(currentList.playlist);
                    if (ev.getClickCount() > 1) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            cell.setOnDragDropped(ev -> {
                Dragboard db = ev.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    //cell.setText("Got Item: " + db.getString());
                    success = true;
                }
                cell.getItem().addAll(dragged);
                ev.setDropCompleted(success);
                ev.consume();
            });



            return cell;

        });
        playlistTable.setOnEditCommit(new EventHandler<ListView.EditEvent<Playlist>>() {
            @Override
            public void handle(ListView.EditEvent<Playlist> t) {
                playlistTable.getItems().get(t.getIndex()).setTitleName(t.getNewValue().name);
                System.out.println("setOnEditCommit");
            }

        });
        Menu menuEffect = new Menu("Theme");

        MenuItem lightSkin = new MenuItem("Light Skin");
        lightSkin.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                musicScene.getStylesheets().remove(currentSkin);
                musicScene.getStylesheets().add(lightMetro);
                currentSkin = lightMetro;
            }
        });

        //No Effects menu
        MenuItem noEffects = new MenuItem("Default Theme");

        noEffects.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                musicScene.getStylesheets().remove(currentSkin);
                musicScene.getStylesheets().add(defaultSkin);
                currentSkin = defaultSkin;
            }
        });
        menuEffect.getItems().addAll(lightSkin, noEffects);
        MenuItem menuAddFiles = new MenuItem("Import MP3's");
        menuAddFiles.setAccelerator(KeyCombination.keyCombination("Meta+O"));
        menuAddFiles.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                browseFiles(primaryStage, library);

            }
        });
        MenuItem makeNewPlaylist = new MenuItem("Make a new playlist");
        makeNewPlaylist.setAccelerator(KeyCombination.keyCombination("Meta+N"));
        makeNewPlaylist.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                TextFieldPopUp textFieldPopUp = new TextFieldPopUp();
                textFieldPopUp.start(new Stage());
            }
        });
        MenuItem exit = new MenuItem("Exit");
        exit.setAccelerator(KeyCombination.keyCombination("Meta+Q"));
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println("EXITING!");
                System.exit(0);
            }
        });
        menuFile.getItems().addAll(menuAddFiles, makeNewPlaylist, exit);
        menuView.getItems().addAll(menuEffect);

        addMP3Button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                browseFiles(primaryStage, library);

            }
        });
        playPauseButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                if ( currentList.songPlaying().playing == true) {
                    System.out.println("PLAYING");
                    currentList.pause();

                } else {
                    currentList.start();

                }
            }
        });
        shufflePlayList.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (currentList.shuffle == false) {
                    currentList.shuffle = true;
                } else {
                    currentList.shuffle = false;
                }
            }
        });
        playPauseButton.getScene().getAccelerators().put(
            new KeyCodeCombination(KeyCode.SPACE),
        new Runnable() {
            @Override public void run() {
                playPauseButton.fire();
            }
        }
        );
        nextButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                stopAll();
                currentList.next();
                volumeSlider.setValue(0.9);
                playASong();
                playPauseButton.fire();
                // playPauseButton.fire();

            }
        });
        prevButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                stopAll();
                currentList.previous();
                volumeSlider.setValue(0.9);
                playASong();
                playPauseButton.fire();


            }
        });
        resetEQ.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                for (int i = 0; i < 10; i++) {
                    sliders[i].setValue(0.6);
                }


            }
        });
        /*for (Playlist p : playlists) {
            p.setOnAction(musicTable);
        }*/

        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        //primaryStage.setWidth(1000);
        //primaryStage.setHeight(800);
        primaryStage.show();


    }
    public static void playASong() {
        Song addingTo = currentList.songPlaying();
        currentList.addNextListener(addingTo);
        setCurrentlyPlaying(addingTo);
        setMediaControl(addingTo);
        addEQListener(addingTo);

    }
    public static void stopAll() {
        for (Playlist p : oPlaylists) {
            p.stop();
        }
    }
    public static void addEQListener(Song s) {
        if (s.addedEQlistener == false) {
            for (int i = 0; i < 10; i++) {
                final int fi = i;
                sliders[i] = new Slider(EqualizerBand.MIN_GAIN, EqualizerBand.MAX_GAIN, 0);
                sliders[i].setOrientation(Orientation.HORIZONTAL);
                sliders[i].valueProperty().addListener(new ChangeListener<Number>() {
                    @Override public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
                        if (currentList.songPlaying().song != null) {
                            s.song.getAudioEqualizer().getBands().get(fi).setGain(newValue.doubleValue());
                        }
                    }
                });
            }
            s.addedEQlistener = true;
        }

    }
    public static Playlist makePlaylist(String s) {
        Playlist p = new Playlist(s);
        p.setOnAction(musicTable);
        System.out.println("MADE A PLAYLIST");
        playlists.add(p);
        oPlaylists.clear();
        oPlaylists.addAll(playlists);

        //playlistTable.getChildren().add(p.button);
        //playlistTable.add(playlists.get(playlists.size() - 1).hbox, 0, playlists.size() - 1);
        return (p);
    }
    public static void browseFiles(Stage primaryStage, Playlist library) {
        configureFileChooser(addMP3Choose);
        List<File> list =
            addMP3Choose.showOpenMultipleDialog(primaryStage);
        if (list != null) {
            for (File file : list) {
                library.add(new Song(file.toURI(), library.size()));

                addToLibrary(file);
            }
        }
    }
    public static void setCurrentlyPlaying(final Song s) {
        //removeAllListeners();
        MediaPlayer newPlayer = s.song;
        //newPlayer.seek(Duration.ZERO);

        progress.setProgress(0);
        progressChangeListener = new ChangeListener<Duration>() {
            @Override public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                s.setProgressListener(progressChangeListener);
                progress.setProgress(1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis());
                currentTime.setText("0:0 " + makeProperTime(newPlayer.getCurrentTime()) + " " + makeProperTime(newPlayer.getTotalDuration()));
            }
        };
        newPlayer.currentTimeProperty().addListener(progressChangeListener);
    }
    private static void setMediaControl(Song s) {
        System.out.println("Setting Media Viewer");
        mediaControl = new PlayerControl(s);
        musicBorder.setBottom(mediaControl);
    }
    public static String makeProperTime(Duration d) {
        int minTime = (int) d.toMinutes();
        int secTime = (int) d.toSeconds();
        String secTimeString = "";
        if (secTime / 60 >= 1) {
            secTime %= 60;
        }
        if (minTime / 60 >= 1) {
            minTime %= 60;
        }
        if (secTime < 10) {
            secTimeString = "0" + secTime;
            return (minTime + ":" + secTimeString);
        } else {
            return (minTime + ":" + secTime);
        }


    }
    public static void addToLibrary(File f) {

        String fileName = "addToLibrary.txt";

        try {
            FileWriter filewriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(filewriter);
            bufferedWriter.newLine();
            bufferedWriter.write(f.toURI().toString());
            System.out.println("Your file has been written");
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
        } finally {
        }
    }
    public Slider makeSlider(int endTime, int currentTime) {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(endTime);
        slider.setValue(currentTime);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(false);
        slider.setMajorTickUnit(30);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(1);
        return (slider);
    }
    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Add MP3's");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );
    }
    public void getMusicDirectory() {

        try {
            File[] contents = musicDir.listFiles();
            for (int i = 0; i < contents.length; i++) {
                String name = contents[i].getName();
                if (name.indexOf(".mp3") == -1) {
                    continue;
                }
                //System.out.println(contents[i].toURI().toString());
                songsDir.add(new Song( contents[i].toURI(), index));
                index++;
            }
        } catch (Exception e) {
            System.out.println("Error -- " + e.toString());
        }
        try(BufferedReader br = new BufferedReader(new FileReader("src/Dir/addToLibrary.txt"))) {

            for (String line; (line = br.readLine()) != null; ) {

                URI f = new URI(line);

                //System.out.println(f);
                songsDir.add(new Song(f, index));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading add to library-- " + e.toString());
        }
    }
    private class ButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("Add to Queue");
        ButtonCell() {

            cellButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    // do something when button clicked
                    MusicPlayer.queue.add((Song)(getTableRow().getItem()) );

                    //MusicPlayer.currentList.append(playlist);
                    //System.out.println("Button being pressed on: \n" + getTableRow().getItem().getClass());

                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    EventHandler<ActionEvent> btnNewHandler =
    new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent t) {


        }
    };
    class EditingCell extends TableCell<Song, Number> {

        private TextField textField;

        public EditingCell() {}

        @Override
        public void startEdit() {

            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(Number item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(Integer.parseInt(textField.getText()));
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                MusicPlayer.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
    public static void saveData() {
        XStream export = new XStream();
        export.alias("currentList", Playlist.class);
        export.alias("song", Song.class);
        export.processAnnotations(Playlist.class);
        export.processAnnotations(Song.class);   // we use for mappings

        String xml = export.toXML(currentList.get(333));
        System.out.println(xml);
    }


}
