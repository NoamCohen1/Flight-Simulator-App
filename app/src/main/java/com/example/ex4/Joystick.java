package com.example.ex4;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class Joystick {
    public static final int STICK_NONE = 0;
    public static final int STICK_UP = 1;
    public static final int STICK_UPRIGHT = 2;
    public static final int STICK_RIGHT = 3;
    public static final int STICK_DOWNRIGHT = 4;
    public static final int STICK_DOWN = 5;
    public static final int STICK_DOWNLEFT = 6;
    public static final int STICK_LEFT = 7;
    public static final int STICK_UPLEFT = 8;

    private int STICK_ALPHA = 200;
    private int LAYOUT_ALPHA = 200;
    private int OFFSET = 0;

    private Context mContext;
    private ViewGroup mLayout;
    private LayoutParams params;
    private int stick_width, stick_height;

    private float position_x = 0, position_y = 0;
    int min_distance = 0;
    private float distance = 0, angle = 0;

    private DrawCanvas draw;
    private Paint paint;
    private Bitmap stick;

    private boolean touch_state = true;

    public Joystick(Context context, ViewGroup layout, int stick_res_id) {
        mContext = context;

        stick = BitmapFactory.decodeResource(mContext.getResources(), stick_res_id);

        stick_width = stick.getWidth();
        stick_height = stick.getHeight();

        draw = new DrawCanvas(mContext);
        paint = new Paint();
        mLayout = layout;
        params = mLayout.getLayoutParams();
    }

    public void startJoistick(){
        draw.position(params.width / 2, params.height / 2);
        draw();
    }

    public void drawStick(MotionEvent arg1) {
        position_x = (int) (arg1.getX() - (params.width / 2));
        position_y = (int) (arg1.getY() - (params.height / 2));
        distance = (float) Math.sqrt(Math.pow(position_x, 2) + Math.pow(position_y, 2));
        angle = (float) cal_angle(position_x, position_y);
        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            if (distance <= (params.width / 2) - OFFSET) {
                draw.position(arg1.getX(), arg1.getY());
                draw();
                touch_state = true;
            }
        } else if (arg1.getAction() == MotionEvent.ACTION_MOVE && touch_state) {
            if (distance <= (params.width / 2) - OFFSET) {
                draw.position(arg1.getX(), arg1.getY());
                draw();
            } else if (distance > (params.width / 2) - OFFSET) {
                float x = (float) (Math.cos(Math.toRadians(cal_angle(position_x, position_y)))
                        * ((params.width / 2) - OFFSET));
                float y = (float) (Math.sin(Math.toRadians(cal_angle(position_x, position_y)))
                        * ((params.height / 2) - OFFSET));
                x += (params.width / 2);
                y += (params.height / 2);
                draw.position(x, y);
                draw();
            } else {
                mLayout.removeView(draw);
            }
        } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
            draw.position(params.width / 2, params.height / 2);
            draw();
        }
    }

    public float normal(float x){
        float i = x + 200;
        float normal = i/400;
        normal= (normal*2) -1;
        return normal;
    }
    public float getX() {
        if (distance > min_distance && touch_state) {
            if (position_x>200 )
                return 1;
            if (position_x<-200)
                return -1;
            return normal(position_x);
        }
        return 0;
    }

    public float getY() {
        if (distance > min_distance && touch_state) {
            if (position_y>200)
                return -1;
            if (position_y<-200)
                return +1;
            return -(normal(position_y));
        }
        return 0;
    }

    public float getAngle() {
        if (!(distance > min_distance && touch_state)) {
            return 0;
        }
        return angle;
    }

    public float getDistance() {
        if (!(distance > min_distance && touch_state)) {
            return 0;
        }
        return distance;
    }

    public void setMinimumDistance(int minDistance) {
        min_distance = minDistance;
    }

    public int get8Direction() {
        if (distance > min_distance && touch_state) {
            if (angle >= 247.5 && angle < 292.5) {
                return STICK_UP;
            } else if (angle >= 292.5 && angle < 337.5) {
                return STICK_UPRIGHT;
            } else if (angle >= 337.5 || angle < 22.5) {
                return STICK_RIGHT;
            } else if (angle >= 22.5 && angle < 67.5) {
                return STICK_DOWNRIGHT;
            } else if (angle >= 67.5 && angle < 112.5) {
                return STICK_DOWN;
            } else if (angle >= 112.5 && angle < 157.5) {
                return STICK_DOWNLEFT;
            } else if (angle >= 157.5 && angle < 202.5) {
                return STICK_LEFT;
            } else if (angle >= 202.5 && angle < 247.5) {
                return STICK_UPLEFT;
            }
        } else if (distance <= min_distance && touch_state) {
            return STICK_NONE;
        }
        return 0;
    }

    public void setOffset(int offset) {
        OFFSET = offset;
    }

    public void setStickAlpha(int alpha) {
        STICK_ALPHA = alpha;
        paint.setAlpha(alpha);
    }

    public void setLayoutAlpha(int alpha) {
        LAYOUT_ALPHA = alpha;
        mLayout.getBackground().setAlpha(alpha);
    }

    public void setStickSize(int width, int height) {
        stick = Bitmap.createScaledBitmap(stick, width, height, false);
        stick_height = stick.getHeight();
        stick_width = stick.getWidth();
    }

    public void setLayoutSize(int width, int height) {
        params.height = height;
        params.width = width;
    }

    private double cal_angle(float x, float y) {
        if (y >= 0 && x >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if (x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if (x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if (y < 0 && x >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
    }

    private void draw() {
        try {
            mLayout.removeView(draw);
        } catch (Exception e) {
        }
        mLayout.addView(draw);
    }

    private class DrawCanvas extends View {
        float x, y;

        private DrawCanvas(Context mContext) {
            super(mContext);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(stick, x, y, paint);
        }

        private void position(float pos_x, float pos_y) {
            x = pos_x;
            x -= (stick_width / 2);
            y = pos_y;
            y -= (stick_height / 2);
        }
    }
}
