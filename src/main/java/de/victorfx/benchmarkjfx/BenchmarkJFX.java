package de.victorfx.benchmarkjfx;

import de.victorfx.benchmarkjfx.benchmarks.BitmapBall;
import de.victorfx.benchmarkjfx.benchmarks.VectorBall;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ramon Victor March 2016.
 * Starts all added Benchmarks. Includes a modified Bubblemark benchmark based on the modified version by
 * TBEERNOT 2011 (tbeernot.wordpress.com). Original Bubblemark benchmark by Alexey Gavrilov (2007).
 */
public class BenchmarkJFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<Stage> benchmarkList = new ArrayList<>();
        benchmarkList.add(new VectorBall(false));
        benchmarkList.add(new VectorBall(true));
        benchmarkList.add(new BitmapBall(false));
        benchmarkList.add(new BitmapBall(true));

        for (Stage stage : benchmarkList) {
            stage.showAndWait();
            Thread.sleep(2000);
        }

    }

}
