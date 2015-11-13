package com.sj.customlib.circletag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sj on 2015/11/13 0013.
 */
public class CircleNumberTag extends View {
    private String TAG = "CanvasView";
    private int textSize = 15;

    private int defNum = 1;
    private int num = 0;

    private boolean hasUnRead = false;
    private TextPaint textPaint;
    private Paint paint;
    private float density;
    private int numColor=0xffffffff;

    public CircleNumberTag(Context context) {
        this(context, null);
    }

    public CircleNumberTag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleNumberTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        density = getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleNumberTag);
        textSize = typedArray.getInteger(R.styleable.CircleNumberTag_pin_textSize, textSize);
        num = typedArray.getInteger(R.styleable.CircleNumberTag_pin_text, defNum);
        numColor = typedArray.getInteger(R.styleable.CircleNumberTag_pin_textColor, numColor);
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(0xffff0000);

        //用来绘制文字 黑色 居中 100
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(numColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(density * textSize);
        textPaint.setFakeBoldText(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(getHeight());
        //在width和height中取较大值作为宽高，保证得到的是一个正方形
        if (width <= height)
            setMeasuredDimension(height, height);
        else
            setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果有未读消息，绘制圆圈和文字
        if (hasUnRead) {
            drawCircle(canvas);
            drawText(canvas);
        }
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);
    }

    private void drawText(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int halfCanvasWidth = canvasWidth / 2;
        int canvasHeight = canvas.getHeight();
        int halfCanvasHeight = canvasHeight / 2;
//        float textHeight = Math.abs(textPaint.descent()) + Math.abs(textPaint.ascent());
        Rect rect=new Rect();
        textPaint.getTextBounds(String.valueOf(num),0,String.valueOf(num).toCharArray().length, rect);
        float textHeight = rect.height();
        Paint p = textPaint;
        //平移坐标系

        canvas.translate(0, halfCanvasHeight);
        canvas.drawText(String.valueOf(num), halfCanvasWidth, textHeight / 2, textPaint);
        canvas.restore();
    }

    /**
     * 设置未读消息数目
     * @param num
     */
    public void setText(int num) {
        setHasUnRead(true);
        this.num = num;
        invalidate();
    }

    public void clearUnRead(){
        setHasUnRead(false);
        invalidate();
    }

    public void setHasUnRead(boolean hasUnRead) {
        this.hasUnRead = hasUnRead;
    }

}
