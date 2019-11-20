package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tetromino {
    Rectangle a;
    Rectangle b;
    Rectangle c;
    Rectangle d;
    private String name;
    Color color;


    public Tetromino(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Tetromino(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;
        //Set color for each form of Tetromino
        switch (name){
            case "j":
                color = Color.BLUE;
                break;
            case "l":
                color = Color.ORANGE;
                break;
            case "o":
                color = Color.YELLOWGREEN;
                break;
            case "s":
                color = Color.GREEN;
                break;
            case "t":
                color = Color.PINK;
                break;
            case "z":
                color = Color.RED;
                break;
            case "i":
                color = Color.LIGHTBLUE;
                break;
        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }

    public String getName() {
        return name;
    }
}
