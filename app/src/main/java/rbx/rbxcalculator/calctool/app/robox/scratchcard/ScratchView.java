package rbx.rbxcalculator.calctool.app.robox.scratchcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ScratchView extends View {
    private Bitmap scratchBitmap;
    private Canvas scratchCanvas;
    private Paint scratchPaint;
    private Path scratchPath;
    private Runnable revealListener;
    private boolean revealed = false;

    public ScratchView(Context ctx) { super(ctx); init(); }
    public ScratchView(Context ctx, AttributeSet a) { super(ctx, a); init(); }
    public ScratchView(Context ctx, AttributeSet a, int s) { super(ctx, a, s); init(); }

    private void init() {
        scratchPaint = new Paint();
        scratchPaint.setAlpha(0);
        scratchPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        scratchPaint.setStyle(Paint.Style.STROKE);
        scratchPaint.setStrokeJoin(Paint.Join.ROUND);
        scratchPaint.setStrokeCap(Paint.Cap.ROUND);
        scratchPaint.setStrokeWidth(60);
        scratchPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        createBitmap(w, h);
    }

    private void createBitmap(int w, int h) {
        scratchBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        scratchCanvas = new Canvas(scratchBitmap);
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#AAAAAA"));
        scratchCanvas.drawRect(0, 0, w, h, bgPaint);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        scratchCanvas.drawText("Scratch Here!", w / 2f, h / 2f, textPaint);
    }

    public void setRevealListener(Runnable r) { this.revealListener = r; }

    public void reset() {
        revealed = false;
        scratchPath.reset();
        if (scratchBitmap != null)
            createBitmap(scratchBitmap.getWidth(), scratchBitmap.getHeight());
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX(), y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: scratchPath.moveTo(x, y); break;
            case MotionEvent.ACTION_MOVE:
                scratchPath.lineTo(x, y);
                scratchCanvas.drawPath(scratchPath, scratchPaint);
                invalidate();
                if (!revealed && isRevealed()) {
                    revealed = true;
                    if (revealListener != null) revealListener.run();
                }
                break;
        }
        return true;
    }

    private boolean isRevealed() {
        if (scratchBitmap == null) return false;
        int w = scratchBitmap.getWidth(), h = scratchBitmap.getHeight();
        int[] pixels = new int[w * h];
        scratchBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        int transparent = 0;
        for (int p : pixels) if (Color.alpha(p) == 0) transparent++;
        return (transparent * 100 / pixels.length) > 50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (scratchBitmap != null) canvas.drawBitmap(scratchBitmap, 0, 0, null);
    }
}
