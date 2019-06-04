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

import java.util.ArrayList;

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
    int score = 0;

    //making ArrayList for square class
    ArrayList<Square> bullets = new ArrayList<Square>();

    public GameEngine(Context context, int screenW, int screenH) {
        super(context);

        // intialize the drawing variables
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        // set screen height and width
        this.screenWidth = screenW;
        this.screenHeight = screenH;

        //making multiple bullets
        this.bullets.add(new Square(context, 100, 600, SQUARE_WIDTH));
        this.bullets.add(new Square(context, 100, 450, SQUARE_WIDTH));
        this.bullets.add(new Square(context, 100, 300, SQUARE_WIDTH));
        this.bullets.add(new Square(context, 100, 150, SQUARE_WIDTH));

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

    boolean enemyMovingDown = true;

    // Game Loop methods
    public void updateGame() {

        //moving enemy up and down


        if(enemyMovingDown == true)
        {
            this.enemy.setyPosition(this.enemy.getyPosition() + 30);
        }
        else{
            this.enemy.setyPosition(this.enemy.getyPosition() - 30);
        }

        // update the enemy hitbox
        this.enemy.updateHitBox();

        //enemy collision detection with wall
        if(this.enemy.getyPosition() >= this.screenHeight - 400)
        {
            enemyMovingDown = false;
        }
        else if(this.enemy.getyPosition() <= 0) {
            enemyMovingDown = true;
        }
        //---------------------------------------------



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

        //setting new bullet position
        this.bullet.setxPosition(newX);
        this.bullet.setyPosition(newY);
        //---------------------------------------------------------


        //moving bullet hitbox
        this.bullet.updateHitBox();

        //Collision detection for bullet

        if(bullet.getHitBox().intersect(enemy.getHitBox()))
        {

            //UPDATING SCORE
            this.score += 1;
            // RESETTING BULLET POSITION
            this.bullet.setxPosition(100);
            this.bullet.setyPosition(600);

            //RESET BULLET HITBOX
            this.bullet.updateHitBox();
        }
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

//            //draw bullet
//            paintbrush.setColor(Color.BLACK);
//            canvas.drawRect(this.bullet.getxPosition(),
//                    this.bullet.getyPosition(),
//                    this.bullet.getxPosition()+this.bullet.getWidth(),
//                    this.bullet.getyPosition() + this.bullet.getWidth(),paintbrush);
//
//            //draw bullet hitbox
//            paintbrush.setColor(Color.RED);
//            paintbrush.setStyle(Paint.Style.STROKE);
//            canvas.drawRect(this.bullet.getHitBox(),paintbrush);

            for(int i = 0; i<this.bullets.size(); i++)
            {
                //getting x,y of each bullet
                Square bullet_i = this.bullets.get(i);

                int x = bullet_i.getxPosition();
                int y = bullet_i.getyPosition();

                //drawing the each bullet
                paintbrush.setColor(Color.BLACK);
                paintbrush.setStyle(Paint.Style.FILL);

                canvas.drawRect(x,
                        y,
                        x + bullet_i.getWidth(),
                        y + bullet_i.getWidth(),paintbrush);

                //drawing the hitbox for each bullet
                paintbrush.setColor(Color.GREEN);
                paintbrush.setStyle(Paint.Style.FILL);
                canvas.drawRect(bullet_i.getHitBox(),paintbrush);

            }

            //// adding some comments to master branch.

            //draw enemy
            paintbrush.setColor(Color.MAGENTA);
            canvas.drawRect(this.enemy.getxPosition(),
                    this.enemy.getyPosition(),
                    this.enemy.getxPosition()+this.enemy.getWidth(),
                    this.enemy.getyPosition() + this.enemy.getWidth(),paintbrush);

            //draw enemy hitbox
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            canvas.drawRect(this.enemy.getHitBox(),paintbrush);

            // draw the score
            paintbrush.setTextSize(100);
            paintbrush.setStrokeWidth(5);
            canvas.drawText("Score: " + this.score, 10, 100, paintbrush);


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


