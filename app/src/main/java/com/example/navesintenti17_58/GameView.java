package com.example.navesintenti17_58;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameView extends View {

    private Bitmap spacecraftBitmap;
    private List<EnemySpacecraft> enemySpacecraftList;
    private float xPosition;
    private Handler handler;
    private Runnable runnable;
    private float enemySpeed; // Enemy spacecraft speed

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        spacecraftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nave);
        enemySpacecraftList = new ArrayList<>();
        handler = new Handler();
        enemySpeed = 30; // Initial speed
        spawnEnemySpacecraft();
    }

    public void setXPosition(float x) {
        xPosition = x;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Load enemy spacecraft bitmap and scale it
        Bitmap SpacecraftBitmapUpload = Bitmap.createScaledBitmap(spacecraftBitmap, 400, 400, false);

        // Draw user's spacecraft
        canvas.drawBitmap(SpacecraftBitmapUpload, xPosition, getHeight() - spacecraftBitmap.getHeight(), null);


        // Draw enemy spacecraft
        Iterator<EnemySpacecraft> iterator = enemySpacecraftList.iterator();
        while (iterator.hasNext()) {
            EnemySpacecraft enemySpacecraft = iterator.next();
            enemySpacecraft.draw(canvas);

            // Update enemy spacecraft position
            enemySpacecraft.yPosition += enemySpeed; // Adjust the speed as needed

            // Check if enemy spacecraft is out of the screen
            if (enemySpacecraft.yPosition > getHeight()) {
                iterator.remove(); // Use Iterator to safely remove the element
            }
        }

        invalidate(); // Redraw the view
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                setXPosition(event.getRawX() - spacecraftBitmap.getWidth() / 2);
                break;
        }
        return true;
    }

    private void spawnEnemySpacecraft() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createEnemySpacecraft();
                spawnEnemySpacecraft(); // Schedule the next enemy spacecraft
            }
        }, 2000); // Initial delay for the first enemy spacecraft
    }

    private void createEnemySpacecraft() {
        Random random = new Random();
        float xPosition = random.nextInt(getWidth());

        // Load enemy spacecraft bitmap and scale it
        Bitmap originalEnemySpacecraftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.naveaux);
        Bitmap enemySpacecraftBitmap = Bitmap.createScaledBitmap(originalEnemySpacecraftBitmap, 200, 200, false);

        EnemySpacecraft enemySpacecraft = new EnemySpacecraft(xPosition, 0, enemySpacecraftBitmap);
        enemySpacecraftList.add(enemySpacecraft);
        invalidate(); // Redraw the view
    }

    private void increaseEnemySpeed() {
        // Increase enemy speed periodically
        enemySpeed += 10; // Adjust the speed increment as needed
    }

    private class EnemySpacecraft {

        private Bitmap enemySpacecraftBitmap;
        private float xPosition;
        private float yPosition;

        public EnemySpacecraft(float x, float y, Bitmap bitmap) {
            enemySpacecraftBitmap = bitmap;
            xPosition = x;
            yPosition = y;
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(enemySpacecraftBitmap, xPosition, yPosition, null);

            // Update enemy spacecraft position
            yPosition += enemySpeed; // Adjust the speed as needed

            // Check if enemy spacecraft is out of the screen
            if (yPosition > getHeight()) {
                enemySpacecraftList.remove(this);
            }
        }
    }
}
