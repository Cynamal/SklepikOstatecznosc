package federacje.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import objects.Kasa;

import java.io.PrintStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static common.FederateAbstract.logS;
import static federacje.GUI.GUIFederate.dzialanieLog;

/**
 * Created by Marcin on 22.06.2017.
 */
public class GUIapp extends Application {

    GUIFederate federate;
    Button StartSym;
    Button StopSym;


    public ListView<String> IPLIst= new ListView<>();
    TextArea Konsola= new TextArea("Brak wpisow");
    public PrintStream ps;

    public void StopSetHendler(Stage primaryStage)
    {
        StopSym.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


               federate.CheckStartedLock.lock();
                federate.fedamb.Started=false;
                federate.CheckStartedLock.unlock();
                StopSym.setVisible(false);
            }
        });
    }
    public void StartSetHendler(Stage primaryStage)
    {
        StartSym.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                federate.CheckStartedLock.lock();
                federate.fedamb.Started=true;
                federate.CheckStartedLock.unlock();

                StopSym.setVisible(true);
                StartSym.setVisible(false);
            }
        });
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        federate=new GUIFederate(this);
        setLeyout(primaryStage);
        StartSetHendler(primaryStage);
        StopSetHendler(primaryStage);
        //  GUIFederate.SrunFederate(this);
        Thread thread = new Thread() {
            public void run() {
                federate.runFederate();

            }

        };
        thread.start();


    }

    public void setLeyout(Stage primaryStage)
    {
        primaryStage.setTitle("GUI Federate");
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        StartSym = new Button();
        StopSym = new Button();
        StopSym.setText("Zakoncz Symulacje");
        StartSym.setText("Rozpocznij Symulacje");
        StopSetHendler(primaryStage);
        StartSetHendler(primaryStage);
        StackPane root = new StackPane();
        GridPane grid = new GridPane();

        grid.add(StartSym,1,5);
        grid.add(StopSym,0,5);
        IPLIst.setMinSize(20,120);
        grid.add(IPLIst, 0, 6, 2, 6);
        grid.add(Konsola,0,30,2,30);
        primaryStage.setScene(new Scene(root, 500, 650));
        root.getChildren().add(grid);
        StopSym.setVisible(false);
        StartSym.setVisible(true);
        primaryStage.show();
        rewriteControls();
    }
    ObservableList<String> oldItems= FXCollections.observableArrayList(Kasa.ToStringList(federate.kasy));
    String oldLog=dzialanieLog;
    public void rewriteControls()
    {
        Thread tr= new Thread() {
            public void run() {

                while (true) {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            if(federate.isRunning)
                            {
                                if(!oldLog.equals(dzialanieLog))
                                {
                                    oldLog=dzialanieLog;
                                    Konsola.setText(dzialanieLog);

                                }

                                oldItems= FXCollections.observableArrayList(Kasa.ToStringList(federate.kasy));
                                IPLIst.setItems(oldItems);
                            }





                            //ObservableList<String> items = FXCollections.observableArrayList(Kasa.ToStringList(federate.kasy));


                            //Konsola.setText(logS);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tr.start();

    }
    @Override
    public void stop() {
        System.out.println("Closing app");
        System.exit(0);
    }
}
