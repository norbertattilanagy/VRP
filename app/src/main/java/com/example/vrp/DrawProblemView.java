package com.example.vrp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class DrawProblemView extends View {

    private Paint paint = new Paint();
    private View view;
    private Canvas canvas;
    int xMultiplayer, yMultiplayer;
    int[] colors = {Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW};

    public DrawProblemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        view = findViewById(R.id.problemView);
    }

    @Override
    public void onDraw(Canvas canv) {

        super.onDraw(canvas);

        canvas = canv;
        canvas.drawColor(-1);

        System.out.println(ViewProblemActivity.routes.size());
        if (ViewProblemActivity.routes.size() == 0)
            drawDepot();
        else {
            calculateMultiplayer();
            drawProblem();
        }
    }

    public void calculateMultiplayer() {
        double minX = ViewProblemActivity.depotX;
        double maxX = ViewProblemActivity.depotX;
        double minY = ViewProblemActivity.depotY;
        double maxY = ViewProblemActivity.depotY;

        for (int i = 0; i < ViewProblemActivity.locationList.size(); i++) {
            if (minX > ViewProblemActivity.locationList.get(i).x)
                minX = ViewProblemActivity.locationList.get(i).x;

            if (maxX < ViewProblemActivity.locationList.get(i).x)
                maxX = ViewProblemActivity.locationList.get(i).x;

            if (minY > ViewProblemActivity.locationList.get(i).y)
                minY = ViewProblemActivity.locationList.get(i).y;

            if (maxY < ViewProblemActivity.locationList.get(i).y)
                maxY = ViewProblemActivity.locationList.get(i).y;
        }

        int xSize = (int) (maxX - minX);
        int ySize = (int) (maxY - minY);

        xMultiplayer = view.getWidth() / ((int) maxX + 2);
        yMultiplayer = view.getHeight() / ((int) maxY + 2);
    }

    public void drawProblem() {
        Drawable depot = getResources().getDrawable(R.drawable.warehouse_icon);
        Drawable market = getResources().getDrawable(R.drawable.market_icon);

        Random rand = new Random();

        //color
        int warehouseColor = Color.rgb(0, 255, 255);
        int marketColor = Color.rgb(0, 255, 0);

        double preX = ViewProblemActivity.depotX;
        double preY = ViewProblemActivity.depotY;

        for (int i = 0; i < ViewProblemActivity.routes.size(); i++) {
            int lineColor = colors[rand.nextInt(colors.length)];

            //draw line
            for (int j = 0; j < ViewProblemActivity.routes.get(i).locations.size(); j++) {
                VRPLocation location = ViewProblemActivity.routes.get(i).locations.get(j);
                paint.setColor(lineColor);
                paint.setStrokeWidth(10);
                canvas.drawLine((int) (preX + 1) * xMultiplayer, (int) (preY + 1) * yMultiplayer, (int) (location.x + 1) * xMultiplayer, (int) (location.y + 1) * yMultiplayer, paint);
                preX = location.x;
                preY = location.y;
            }

            for (int j = 0; j < ViewProblemActivity.routes.get(i).locations.size(); j++) {
                VRPLocation location = ViewProblemActivity.routes.get(i).locations.get(j);
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(location.name,
                        (int) (location.x + 1) * xMultiplayer,
                        (int) (location.y + 1) * yMultiplayer - 100, paint);

                //set color
                if (location.getWarehouse())
                    paint.setColor(warehouseColor);
                else
                    paint.setColor(marketColor);

                //System.out.println("====" + location.x + "*" + xMultiplayer+"=" + (location.x * xMultiplayer - 50));
                canvas.drawCircle((int) (location.x + 1) * xMultiplayer, (int) (location.y + 1) * yMultiplayer, 80, paint);

                //draw image
                if (location.getWarehouse()) {//depot
                    depot.setBounds((int) (location.x + 1) * xMultiplayer - 50, (int) (location.y + 1) * yMultiplayer - 50, (int) (location.x + 1) * xMultiplayer + 50, (int) (location.y + 1) * yMultiplayer + 50);
                    depot.draw(canvas);
                } else {
                    market.setBounds((int) (location.x + 1) * xMultiplayer - 50, (int) (location.y + 1) * yMultiplayer - 50, (int) (location.x + 1) * xMultiplayer + 50, (int) (location.y + 1) * yMultiplayer + 50);
                    market.draw(canvas);
                }
            }
        }
    }

    public void drawDepot() {
        Drawable depot = getResources().getDrawable(R.drawable.warehouse_icon);

        int warehouseColor = Color.rgb(0, 255, 255);
        paint.setColor(warehouseColor);

        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 80, paint);

        depot.setBounds(canvas.getWidth() / 2 - 50, canvas.getHeight() / 2 - 50, canvas.getWidth() / 2 + 50, canvas.getHeight() / 2 + 50);
        depot.draw(canvas);
    }
}
