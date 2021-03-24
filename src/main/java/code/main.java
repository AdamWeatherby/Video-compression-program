package code;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class main extends Application{
    Button begin;
    Button convert;
    videoEncoder video;

    Boolean filled;

    @Override
    public void start(Stage primaryStage) {
        filled = false;
        Label label = new Label("Drag a file to me.");
        Label dropped = new Label("");
        HBox dragTarget = new HBox();
        dragTarget.setSpacing(10);
        dragTarget.setPadding(new Insets(15,12,15,12));
        dragTarget.getChildren().addAll(label,dropped);
        dragTarget.setAlignment(Pos.CENTER);
        dragTarget.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != dragTarget
                        && event.getDragboard().hasFiles()
                            &&filled==false) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        dragTarget.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    String fileName[] = db.getFiles().toString().split("\\\\");
                    dropped.setText(fileName[fileName.length-1].replaceAll("]",""));
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                filled = true;
                video = new videoEncoder(db.getFiles().get(0));
                begin = new Button();
                begin.setText("Play video");
                begin.setOnAction(e->new myVideoPlayer().analyzeFrame(video));
                convert = new Button();
                convert.setText("convert");
                dragTarget.getChildren().addAll(begin,convert);
                dragTarget.setAlignment(Pos.BOTTOM_CENTER);
                Changer(dragTarget,primaryStage);
            }
        });


        StackPane root = new StackPane();
        root.getChildren().add(dragTarget);

        Scene scene = new Scene(root, 200, 200);
        primaryStage.setTitle("Manic Compression");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    VBox textBoxes = new VBox();
    TextField maxiSize = new TextField("8");
    Label maxSizeLab = new Label("max file size");
    TextField startingPoint = new TextField("0");
    Label startingPointLab = new Label("Starting point");
    TextField Duration = new TextField("0");
    Label DurationLab = new Label("Duration");
    public void Changer(HBox dragTarget, Stage primaryStage){
        maxiSize.setMaxWidth(80);
        startingPoint.setMaxWidth(80);
        Duration.setMaxWidth(80);

        textBoxes.getChildren().addAll(maxSizeLab,maxiSize,startingPointLab,startingPoint,DurationLab,Duration);
        textBoxes.setAlignment(Pos.CENTER);
        StackPane root = new StackPane();
        dragTarget.setPickOnBounds(false);
        textBoxes.setPickOnBounds(false);
        root.getChildren().addAll(dragTarget,textBoxes);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Manic Compression");
        primaryStage.setScene(scene);
        primaryStage.show();
        convert.setOnAction(e->buttonPressed());
    }
    public void buttonPressed(){
        double finalSize = Double.valueOf(maxiSize.getText())*1000000;
        float finalStart = Float.valueOf(startingPoint.getText());
        float finalDuration = Float.valueOf(Duration.getText());
        videoEncoder finalVid = new videoEncoder(video.source1);
        finalVid.encodeToSize(finalSize,finalStart,finalDuration);
    }
}
