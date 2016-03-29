package de.victorfx.benchmarkjfx;

import de.victorfx.benchmarkjfx.benchmarks.AreaChartBenchmark;
import de.victorfx.benchmarkjfx.benchmarks.BitmapBall;
import de.victorfx.benchmarkjfx.benchmarks.ListViewBenchmark;
import de.victorfx.benchmarkjfx.benchmarks.VectorBall;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ramon Victor March 2016.
 *         Starts all added Benchmarks. Includes a modified Bubblemark benchmark based on the modified version by
 *         TBEERNOT 2011 (tbeernot.wordpress.com). Original Bubblemark benchmark by Alexey Gavrilov (2007).
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
        benchmarkList.add(new ListViewBenchmark(false));
        benchmarkList.add(new ListViewBenchmark(true));
        benchmarkList.add(new AreaChartBenchmark(false));
        benchmarkList.add(new AreaChartBenchmark(true));

        ProgressBar bar = getProgressBar(primaryStage);
        for (int i = 0; i < benchmarkList.size(); i++) {
            if (benchmarkList.size() != 0) {
                bar.setProgress(Double.valueOf(i) / Double.valueOf(benchmarkList.size()));
            }
            benchmarkList.get(i).showAndWait();
            Thread.sleep(2000);
        }
        primaryStage.close();
    }

    private ProgressBar getProgressBar(Stage primaryStage) {
        ProgressBar bar = new ProgressBar();
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = device.getDisplayMode().getWidth();
        int height = device.getDisplayMode().getHeight();
        bar.setPrefWidth(width * 0.8);
        bar.setPrefHeight(height * 0.025);
        Scene scene = new Scene(bar);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.setY(height - height * 0.025);
        primaryStage.setX(width - primaryStage.getWidth() / 2);
        primaryStage.show();
        return bar;
    }

}
