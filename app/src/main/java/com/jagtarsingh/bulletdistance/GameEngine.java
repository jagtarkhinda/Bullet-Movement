package com.jagtarsingh.bulletdistance;




import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameEngine extends SurfaceView implements Runnable {
    private final String TAG = "VECTOR-MATH";

    // game thread variables
    private Thread gameThread = null;
    private volatile boolean gameIsRunning;

    // drawing variables
    private Canvas canvas;
    private Paint paintbrush;
    private SurfaceHolder holder;

    // Screen resolution varaibles
    private int screenWidth;
    private int screenHeight;

    //SPRITES
    Square bullet;
    Square enemy;

    int SQUARE_WIDTH = 100;

    public GameEngine(Context context, int screenW, int screenH) {
        super(context);

        // intialize the drawing variables
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        // set screen height and width
        this.screenWidth = screenW;
        this.screenHeight = screenH;

        //initialize sprites
        bullet = new Square(context, 100, 600, SQUARE_WIDTH);
        enemy = new Square(context, 1000, 100, SQUARE_WIDTH);
    }
    @Override
    public void run() {
        // @TODO: Put game loop in here
        while (gameIsRunning == true) {
            updateGame();
            drawGame();
            controlFPS();
        }
    }

    // Game Loop methods
    public void updateGame() {

        // 1. Calculate distance between bullet and enemy

        double a = this.enemy.getxPosition() - this.bullet.getxPosition();
        double b = this.enemy.getyPosition() - this.bullet.getyPosition();

        double d = Math.sqrt((a*a) + (b*b));

        // 2. Calculate xn and xy (how much to move per frame)

        double xn = (a / d);
        double yn = (b / d);

        // 3. Calculate where to move next (new x,y coordinates)

        int newX = this.bullet.getxPosition() + (int) (xn * 15);
        int newY = this.bullet.getyPosition() + (int) (yn * 15);

        this.bullet.setxPosition(newX);
        this.bullet.setyPosition(newY);


    }

    public void drawGame() {
        if (holder.getSurface().isValid()) {

            // initialize the canvas
            canvas = holder.lockCanvas();
            // --------------------------------
            // @TODO: put your drawing code in this section

            // set the game's background color
            canvas.drawColor(Color.argb(255,255,255,255));
            //set stroke
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(8);

            //draw bullet
            paintbrush.setColor(Color.BLACK);
            canvas.drawRect(this.bullet.getxPosition(),
                    this.bullet.getyPosition(),
                    this.bullet.getxPosition()+this.bullet.getWidth(),
                    this.bullet.getyPosition() + this.bullet.getWidth(),paintbrush);

            //draw enemy
            paintbrush.setColor(Color.MAGENTA);
            canvas.drawRect(this.enemy.getxPosition(),
                    this.enemy.getyPosition(),
                    this.enemy.getxPosition()+this.enemy.getWidth(),
                    this.enemy.getyPosition() + this.enemy.getWidth(),paintbrush);


            // --------------------------------
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void controlFPS() {
        try {
            gameThread.sleep(17);
        }
        catch (InterruptedException e) {

        }
    }


    // Deal with user input


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return true;
    }

    // Game status - pause & resume
    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {

        }
    }
    public void  resumeGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}


