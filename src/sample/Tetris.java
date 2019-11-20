package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Tetris extends Application {
    //Creating a 10x24 Grid
    public static final int MOVE = 25;
    public static final int SIZE = 25;
    public static int X = SIZE * 10;
    public static int Y = SIZE * 20;
    public static int[][] GRID = new int[X/SIZE][Y/SIZE];
    public static Pane board = new Pane();
    public static Tetromino object;
    public static Scene scene = new Scene(board, X + 200, Y);   //extra place for displaying stats
    private static boolean game = true;
    private static Tetromino nextObject = Controller.MakeRect();
    public static int score = 0;            //Score here
    public static int top = 0;

    public static void main(String[] args){
        String path = "C:/Projects/Tetris_Remake/src/korobeiniki.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        for (int[] ar: GRID){
            Arrays.fill(ar,0); //fill each row with 0
        }

        //Stats display
        Line line = new Line(X, 0, X, Y);   //Draw a line between Score and Board
        Text scoreDisplay = new Text ();
        scoreDisplay.setText("SCORE: ");
        scoreDisplay.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
        scoreDisplay.setFill(Color.DARKBLUE);
        scoreDisplay.setX(X + 5);
        scoreDisplay.setY(50);
        board.getChildren().addAll(scoreDisplay, line);


        //Create the 1st block and starting the game
        Tetromino newBlock = nextObject;
        board.getChildren().addAll(newBlock.a, newBlock.b, newBlock.c, newBlock.d);
        Control(newBlock);
        object = newBlock;
        nextObject = Controller.MakeRect();
        stage.setScene(scene);
        stage.setTitle("Tetris");
        stage.show();

        //Timer
        Timer fall = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {              //This is a hideous hunk of code, a crime against nature (and programming in general)
                    @Override                                   //First, you’ll lose brain cells just looking at this double nesting of Runnables
                    public void run() {                         //Second, it is going to swamp the event queue with little Runnables — a million of them in fact
                        if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0 || object.d.getY() == 0)
                            top++;
                        else
                            top=0;
                        // GAME OVER SCENARIO
                        if (top == 2){
                            Text endGame = new Text("GAME OVER!");
                            endGame.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 12));
                            endGame.setX(250);
                            endGame.setY(10);
                            board.getChildren().add(endGame);
                            game = false;
                        }

                        // Exit
                        if (top == 15) {
                            System.exit(0);
                        }

                        if (game){
                            SoftDrop(object);
                            scoreDisplay.setText("Score:" + Integer.toString(score));
                        }
                    }
                });
            }
        };
        fall.schedule(task, 0, 300);
    }

    //Controller mapping
    private void Control(Tetromino form){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case RIGHT:
                        Controller.RightMove(form);
                        break;
                    case LEFT:
                        Controller.LeftMove(form);
                        break;
                    case DOWN:
                        SoftDrop(form);
                        score++;
                        break;
                    case UP:
                        Controller.Rotate(form);
                        break;
                    case SPACE:
                        //Controller.HardDrop(form);
                        break;
                }
            }
        });
    }

    private void RemoveRows(Pane pane) {
        ArrayList<Node> rects = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newrects = new ArrayList<Node>();
        int full = 0;
        for (int i = 0; i < GRID[0].length; i++) {
            for (int j = 0; j < GRID.length; j++) {
                if (GRID[j][i] == 1)
                    full++;
            }
            if (full == GRID.length)
                lines.add(i + lines.size());
            full = 0;
        }
        if (lines.size() > 0)
            do {
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                score += 50;

                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node : newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
                rects.clear();
            } while (lines.size() > 0);
    }

    private void SoftDrop(@org.jetbrains.annotations.NotNull Tetromino form) {
        if (form.a.getY() == Y - SIZE || form.b.getY() == Y - SIZE || form.c.getY() == Y - SIZE
                || form.d.getY() == Y - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {
            GRID[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
            GRID[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
            GRID[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
            GRID[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
            RemoveRows(board);

            if(game) {
                Tetromino newBlock = nextObject;
                nextObject = Controller.MakeRect();
                object = newBlock;
                board.getChildren().addAll(newBlock.a, newBlock.b, newBlock.c, newBlock.d);
                Control(newBlock);
            }
        }

        if (form.a.getY() + MOVE < Y && form.b.getY() + MOVE < Y && form.c.getY() + MOVE < Y
                && form.d.getY() + MOVE < Y) {
            int movea = GRID[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1];
            int moveb = GRID[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1];
            int movec = GRID[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1];
            int moved = GRID[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1];
            if (movea == 0 && moveb == 0 && movec == 0 && moved == 0) {
                form.a.setY(form.a.getY() + MOVE);
                form.b.setY(form.b.getY() + MOVE);
                form.c.setY(form.c.getY() + MOVE);
                form.d.setY(form.d.getY() + MOVE);
            }
        }
    }

    private boolean moveA(Tetromino form) {
        return (GRID[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1] == 1);
    }

    private boolean moveB(Tetromino form) {
        return (GRID[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1] == 1);
    }

    private boolean moveC(Tetromino form) {
        return (GRID[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1] == 1);
    }

    private boolean moveD(Tetromino form) {
        return (GRID[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1] == 1);
    }
}

