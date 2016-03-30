package de.victorfx.benchmarkjfx.benchmarks;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ramon Victor March 2016.
 */
public class ListViewBenchmark extends Stage {

    private static final int LIST_MAX = 10;
    private static final int WARMUP_COUNT = 3;
    private static final boolean WARMUP_ENABLED = true;
    private static final int BENCHMARK_COUNT = 20;
    private static final boolean BENCHMARK_REPORT_ENABLED = true;
    private static final String BENCHMARK_REPORT_NAME = "ListView.txt";
    private static final String BENCHMARK_REPORT_NAME_FULLSCREEN = "ListViewFullscreen.txt";
    private static final String TESTDATA_NAME = "Test";
    private static final String TESTDATA_SUFFIX = "List";

    private final boolean fullscreen;
    private int frames = 0;
    private long lastFrame = 0;
    private int warmupCount = 0;
    private int benchmarkCount = 0;
    private int testdataCount = 1;

    private final List<ListView<String>> listViews = new ArrayList<>();
    private final List<Integer> benchmarkResults = new ArrayList<>();
    private final List<String> benchmarkEndResults = new ArrayList<>();

    public ListViewBenchmark(Boolean fullscreen) {
        this.setTitle("ListView Benchmark");
        this.fullscreen = fullscreen;
        System.out.println("<- Prepare ListView Benchmark with Fullscreen = " + fullscreen + " ->");
    }

    @Override
    public void showAndWait() {

        final HBox root = new HBox();
        if (fullscreen) {
            this.setFullScreen(true);
        }
        final ListView<String> listView = new ListView<>();
        listViews.add(listView);
        root.getChildren().add(listView);
        Scene scene = new Scene(root, 640, 480);

        System.out.println("<- Start ListView Benchmark with Fullscreen = " + fullscreen + " ->");
        this.setScene(scene);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < listViews.size(); i++) {
                    listViews.get(i).getItems().add(0, TESTDATA_NAME + testdataCount + TESTDATA_SUFFIX + (i + 1));
                }
                ++testdataCount;
                ++frames;
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastFrame + 1_000_000_000) {
                    // Warmup
                    if (WARMUP_ENABLED && warmupCount < WARMUP_COUNT) {
                        System.out.println("<- Warm-Up ->");
                        ++warmupCount;
                    } else {
                        warmupCount = WARMUP_COUNT;
                    }
                    if (warmupCount == WARMUP_COUNT) {
                        // Benchmark
                        if (benchmarkCount < BENCHMARK_COUNT) {
                            System.out.println("DATA_COUNT_PER_LIST: " + listView.getItems().size() + "; FPS: " +
                                    frames + "; LIST_COUNT: " + listViews.size() + "; MAX_DATA: " + (listView
                                    .getItems().size() * listViews.size()) + ";");
                            benchmarkResults.add(frames);
                            ++benchmarkCount;
                        } else {
                            benchmarkEndResults.add(listView.getItems().size() + "; " + getMiddleValue
                                    (benchmarkResults) + "; " + listViews.size() + "; " + (listView.getItems().size()
                                    * listViews.size()) + "\n");
                            benchmarkResults.clear();
                            if (listViews.size() >= LIST_MAX) {
                                String name = fullscreen ? BENCHMARK_REPORT_NAME_FULLSCREEN : BENCHMARK_REPORT_NAME;
                                if (BENCHMARK_REPORT_ENABLED) {
                                    try {
                                        FileWriter writer = new FileWriter(new File(name));
                                        writer.append("Benchmark; ListView with Fullscreen = ").append(String.valueOf
                                                (fullscreen)).append(";\n");
                                        writer.append("DATA_COUNT_PER_LIST; FPS; LIST_COUNT; MAX_DATA;\n");
                                        for (String s : benchmarkEndResults) {
                                            writer.append(s);
                                        }
                                        writer.flush();
                                        writer.close();
                                        listViews.clear();
                                        this.stop();
                                        close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                for (ListView<String> listView1 : listViews) {
                                    listView1.getItems().clear();
                                }
                                testdataCount = 1;
                                ListView<String> view = new ListView<>();
                                root.getChildren().add(view);
                                listViews.add(view);
                            }
                            benchmarkCount = 0;
                            warmupCount = 0;
                        }
                    }
                    frames = 0;
                    lastFrame = currentNanoTime;
                }
            }
        }.start();

        super.showAndWait();
    }

    private double getMiddleValue(List<Integer> listOfValues) {
        int summary = 0;
        for (Integer listOfValue : listOfValues) {
            summary += listOfValue;
        }
        return summary / listOfValues.size();
    }
}
