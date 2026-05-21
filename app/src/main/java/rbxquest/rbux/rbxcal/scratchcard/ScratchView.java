package rbxquest.rbux.rbxcal.scratchcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ScratchView extends View {

    private Bitmap   scratchBitmap;
    private Canvas   scratchCanvas;
    private Paint    scratchPaint;   // eraser
    private Paint    drawPaint;      // bitmap draw
    private Path     scratchPath;
    private Runnable revealListener;
    private boolean  revealed = false;
    private float    cornerRadius = 0f;

    public ScratchView(Context ctx)                          { super(ctx);       init(); }
    public ScratchView(Context ctx, AttributeSet a)          { super(ctx, a);    init(); }
    public ScratchView(Context ctx, AttributeSet a, int s)   { super(ctx, a, s); init(); }

    private void init() {
        // Eraser paint — clears pixels as the user scratches
        scratchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scratchPaint.setAlpha(0);
        scratchPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        scratchPaint.setStyle(Paint.Style.STROKE);
        scratchPaint.setStrokeJoin(Paint.Join.ROUND);
        scratchPaint.setStrokeCap(Paint.Cap.ROUND);
        scratchPaint.setStrokeWidth(72);

        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scratchPath = new Path();
    }

    public void setCornerRadius(float radiusPx) { this.cornerRadius = radiusPx; }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        createBitmap(w, h);
    }

    private void createBitmap(int w, int h) {
        if (w <= 0 || h <= 0) return;
        scratchBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        scratchCanvas = new Canvas(scratchBitmap);

        // ── 1. Base gradient: deep gold ──────────────────────────────────
        LinearGradient base = new LinearGradient(0, 0, w, h,
                new int[]{0xFF1C1200, 0xFF2E1E00, 0xFF3A2800, 0xFF2E1E00, 0xFF1C1200},
                new float[]{0f, 0.25f, 0.5f, 0.75f, 1f},
                Shader.TileMode.CLAMP);
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setShader(base);
        if (cornerRadius > 0) {
            scratchCanvas.drawRoundRect(new RectF(0, 0, w, h), cornerRadius, cornerRadius, bgPaint);
        } else {
            scratchCanvas.drawRect(0, 0, w, h, bgPaint);
        }

        // ── 2. Diamond-grid overlay ──────────────────────────────────────
        Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(0x18FFD700);
        gridPaint.setStrokeWidth(1f);
        int sp = 32;
        for (int i = -h; i < w + h; i += sp) {
            scratchCanvas.drawLine(i, 0, i + h, h, gridPaint);
            scratchCanvas.drawLine(i + h, 0, i, h, gridPaint);
        }

        // ── 3. Concentric ring decoration (centre) ───────────────────────
        Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(1.5f);
        float cx = w / 2f, cy = h / 2f;
        float[] radii = {h * 0.30f, h * 0.38f, h * 0.46f};
        for (int i = 0; i < radii.length; i++) {
            ringPaint.setColor(i == 0 ? 0x33FFD700 : 0x18FFD700);
            scratchCanvas.drawCircle(cx, cy, radii[i], ringPaint);
        }

        // ── 4. Star rows ─────────────────────────────────────────────────
        Paint starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint.setTextAlign(Paint.Align.CENTER);
        starPaint.setTextSize(h * 0.10f);
        starPaint.setColor(0x55FFD700);
        scratchCanvas.drawText("✦    ✦    ✦", cx, cy - h * 0.20f, starPaint);
        scratchCanvas.drawText("✦    ✦    ✦", cx, cy + h * 0.30f, starPaint);

        // ── 5. Main "SCRATCH" label ──────────────────────────────────────
        Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        labelPaint.setLetterSpacing(0.18f);

        // Shadow layer
        labelPaint.setColor(0x66000000);
        labelPaint.setTextSize(h * 0.145f);
        scratchCanvas.drawText("SCRATCH HERE", cx + 2, cy + h * 0.055f + 2, labelPaint);

        // Gold label
        LinearGradient labelGrad = new LinearGradient(
                cx - w * 0.35f, cy, cx + w * 0.35f, cy,
                new int[]{0xFFFFD700, 0xFFFFED80, 0xFFFFD700},
                null, Shader.TileMode.CLAMP);
        labelPaint.setShader(labelGrad);
        labelPaint.setTextSize(h * 0.145f);
        scratchCanvas.drawText("SCRATCH HERE", cx, cy + h * 0.055f, labelPaint);

        // ── 6. Finger-swipe hint icon row ────────────────────────────────
        Paint hintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hintPaint.setTextAlign(Paint.Align.CENTER);
        hintPaint.setTextSize(h * 0.085f);
        hintPaint.setColor(0x44FFD700);
        scratchCanvas.drawText("〈  swipe to reveal  〉", cx, cy + h * 0.20f, hintPaint);
    }

    public void setRevealListener(Runnable r) { revealListener = r; }

    public void reset() {
        revealed = false;
        scratchPath.reset();
        if (scratchBitmap != null)
            createBitmap(scratchBitmap.getWidth(), scratchBitmap.getHeight());
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (revealed) return true;
        float x = e.getX(), y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scratchPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                scratchPath.lineTo(x, y);
                if (scratchCanvas != null)
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
        // Sample every 4th pixel for performance
        int total = 0, transparent = 0;
        int[] pixels = new int[w * h];
        scratchBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < pixels.length; i += 4) {
            total++;
            if (Color.alpha(pixels[i]) == 0) transparent++;
        }
        return total > 0 && (transparent * 100 / total) > 55;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (scratchBitmap != null) canvas.drawBitmap(scratchBitmap, 0, 0, drawPaint);
    }
}
