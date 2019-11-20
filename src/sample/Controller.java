package sample;
import javafx.css.Size;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Controller {
    //Get size and variables of the grid from class Tetris
    public static final int MOVE = Tetris.MOVE;
    public static final int SIZE = Tetris.SIZE;
    public static int X = Tetris.X;
    public static int Y = Tetris.Y;
    public static int[][] GRID = Tetris.GRID;


    //Moving the Tetromino RIGHT
    public static void RightMove(Tetromino form){
        if(form.a.getX() + MOVE <= X - SIZE &&
                form.b.getX() + MOVE <= X - SIZE &&
                form.c.getX() + MOVE <= X - SIZE &&
                form.d.getX() + MOVE <= X - SIZE)
        {
            int movea = GRID [(int) (form.a.getX() / SIZE + 1)] [(int) (form.a.getX() / SIZE)];
            int moveb = GRID [(int) (form.b.getX() / SIZE + 1)] [(int) (form.b.getX() / SIZE)];
            int movec = GRID [(int) (form.b.getX() / SIZE + 1)] [(int) (form.c.getX() / SIZE)];
            int moved = GRID [(int) (form.b.getX() / SIZE + 1)] [(int) (form.d.getX() / SIZE)];

            if (movea == 0 && moveb == 0 && movec == 0 && moved == 0){
                form.a.setX(form.a.getX() + MOVE);
                form.b.setX(form.b.getX() + MOVE);
                form.c.setX(form.c.getX() + MOVE);
                form.d.setX(form.d.getX() + MOVE);
            }
        }
    }
    //Moving the Tetromino LEFT
    public static void LeftMove(Tetromino form){
        if(form.a.getX() - MOVE >= 0 &&
                form.b.getX() - MOVE >= 0 &&
                form.c.getX() - MOVE >= 0 &&
                form.d.getX() - MOVE >= 0)
        {
            int movea = GRID [(int) (form.a.getX() / SIZE - 1)] [(int) (form.a.getX() / SIZE)];
            int moveb = GRID [(int) (form.b.getX() / SIZE - 1)] [(int) (form.b.getX() / SIZE)];
            int movec = GRID [(int) (form.b.getX() / SIZE - 1)] [(int) (form.c.getX() / SIZE)];
            int moved = GRID [(int) (form.b.getX() / SIZE - 1)] [(int) (form.d.getX() / SIZE)];

            if (movea == 0 && moveb == 0 && movec == 0 && moved == 0){
                form.a.setX(form.a.getX() - MOVE);
                form.b.setX(form.b.getX() - MOVE);
                form.c.setX(form.c.getX() - MOVE);
                form.d.setX(form.d.getX() - MOVE);
            }
        }
    }

    //To rotate a tetromino, block c will be use as a center
    public static void Rotate(Tetromino form) {
        if(!form.getName().equals("o")) {
                int org_a_x = (int) form.a.getX();
                int org_a_y = (int) form.a.getY();
                int org_b_x = (int) form.b.getX();
                int org_b_y = (int) form.b.getY();
                int org_c_x = (int) form.c.getX();
                int org_c_y = (int) form.c.getY();
                int org_d_x = (int) form.d.getX();
                int org_d_y = (int) form.d.getY();

                form.a.setX((form.c.getY() + form.c.getX() - org_a_y));
                form.a.setY((org_a_x - form.c.getX() + form.c.getY()));

                form.b.setX((form.c.getY() + form.c.getX() - org_b_y));
                form.b.setY((org_b_x - form.c.getX() + form.c.getY()));

                form.d.setX((form.c.getY() + form.c.getX() - org_d_y));
                form.d.setY((org_d_x - form.c.getX() + form.c.getY()));
        }
    }

    static Tetromino MakeRect(){
        // generate a random number from 0-6, each number equal one tetromino being spawned
        Random random = new Random();
        int x = random.nextInt(7);
        String name = null;
        // the size of each rectangle is slightly smaller than the size of a tile (24x24 instead of 25x25) so it looks separated from each other
        Rectangle   a = new Rectangle(SIZE-1, SIZE-1),
                b = new Rectangle(SIZE-1, SIZE-1),
                c = new Rectangle(SIZE-1, SIZE-1),
                d = new Rectangle(SIZE-1, SIZE-1);

        if (x == 0) {                               //if X or Y is not set, it will automatically be 0
            name = "j";             //[a]
            a.setX(X/2 - SIZE);     //[b][c][d]
            b.setX(X/2 -SIZE);
            b.setY(SIZE);
            c.setX(X/2);
            c.setY(SIZE);
            d.setX(X/2 + SIZE);
            d.setY(SIZE);
        }else if(x==1){
            name = "l";             //      [a]
            a.setX(X/2 + SIZE);     //[b][c][d]
            b.setX(X/2 -SIZE);
            b.setY(SIZE);
            c.setX(X/2);
            c.setY(SIZE);
            d.setX(X/2 + SIZE);
            d.setY(SIZE);
        }else if(x==2){
            name = "o";
            a.setX(X/2 - SIZE);     //[a][b]
            b.setX(X/2);            //[c][d]
            c.setX(X/2 - SIZE);
            c.setY(SIZE);
            d.setX(X/2);
            d.setY(SIZE);
        }else if(x==3){             //
            name = "s";
            a.setX(X/2 + SIZE);
            b.setX(X/2);
            c.setX(X/2);
            c.setY(SIZE);
            d.setX(X/2 - SIZE);
            d.setY(SIZE);
        }else if(x==4){
            name = "t";
            a.setX(X/2 - SIZE);
            b.setX(X/2);
            c.setX(X/2);
            c.setY(SIZE);
            d.setX(X/2 + SIZE);
        }else if(x==5){
            name = "z";
            a.setX(X/2 + SIZE);
            b.setX(X/2);
            c.setX(X/2 + SIZE);
            c.setY(SIZE);
            d.setX(X/2 + SIZE + SIZE);
            d.setY(SIZE);
        }else if(x==6){
            name = "i";
            a.setX(X/2 - SIZE - SIZE);
            b.setX(X/2 - SIZE);
            c.setX(X/2);
            d.setX(X/2 + SIZE);
        }
        return new Tetromino(a, b, c, d, name);
    }
}

