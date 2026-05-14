package rbx.rbxcalculator.calctool.app.robox.spin;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class WheelView extends View {

    // Prizes drawn on each segment (clockwise from top)
    public static final String[] LABELS = {
            "100", "2000", "300", "5000", "7000", "800", "400", "1000"
    };

    // Segment fill colours — match the screenshot palette
    private static final int[] SEGMENT_COLORS = {
            0xFF4CAF50,  // Green
            0xFFFF9800,  // Orange
            0xFFF44336,  // Red
            0xFF009688,  // Teal
            0xFFECEFF1,  // Near-white (light)
            0xFFFF9800,  // Orange
            0xFFE91E63,  // Pink / Magenta
            0xFF1565C0,  // Deep Blue
    };

    private final Paint segPaint      = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dotPaint      = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint triPaint      = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint centerPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint strokePaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint ringPaint     = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final RectF oval          = new RectF();
    private final Path  triPath       = new Path();

    public WheelView(Context ctx)                              { super(ctx);            init(); }
    public WheelView(Context ctx, AttributeSet a)             { super(ctx, a);         init(); }
    public WheelView(Context ctx, AttributeSet a, int def)    { super(ctx, a, def);    init(); }

    private void init() {
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setColor(Color.WHITE);

        borderPaint.setColor(0xFF1A237E);          // dark indigo ring
        borderPaint.setStyle(Paint.Style.FILL);

        dotPaint.setColor(0xFFFFD700);             // gold dots
        dotPaint.setStyle(Paint.Style.FILL);

        triPaint.setColor(Color.WHITE);
        triPaint.setStyle(Paint.Style.FILL);

        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(0xFFB8860B);          // dark-gold stroke

        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setColor(0x55FFFFFF);            // semi-white ring near center
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float cx = getWidth()  / 2f;
        final float cy = getHeight() / 2f;
        final float r  = Math.min(cx, cy);

        final float outerR  = r * 0.97f;   // dark-blue outer border
        final float dotsR   = r * 0.88f;   // ring on which dots sit
        final float wheelR  = r * 0.80f;   // coloured segment radius
        final float hubR    = r * 0.16f;   // gold center hub
        final float hubRing = r * 0.22f;   // white ring around hub

        // ── 1. Outer dark-blue border ring ──────────────────────────────
        canvas.drawCircle(cx, cy, outerR, borderPaint);

        // ── 2. Coloured segments ─────────────────────────────────────────
        final int   n     = LABELS.length;
        final float sweep = 360f / n;
        oval.set(cx - wheelR, cy - wheelR, cx + wheelR, cy + wheelR);

        for (int i = 0; i < n; i++) {
            segPaint.setColor(SEGMENT_COLORS[i]);
            canvas.drawArc(oval, -90f + i * sweep, sweep, true, segPaint);
        }

        // ── 3. Segment divider lines ─────────────────────────────────────
        Paint divPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        divPaint.setColor(0x44000000);
        divPaint.setStyle(Paint.Style.STROKE);
        divPaint.setStrokeWidth(r * 0.006f);
        for (int i = 0; i < n; i++) {
            float rad = (float) Math.toRadians(-90 + i * sweep);
            canvas.drawLine(cx, cy,
                    cx + (float) Math.cos(rad) * wheelR,
                    cy + (float) Math.sin(rad) * wheelR,
                    divPaint);
        }

        // ── 4. Prize text on each segment ────────────────────────────────
        float textSize = r * 0.13f;
        textPaint.setTextSize(textSize);
        float textR = wheelR * 0.62f;

        for (int i = 0; i < n; i++) {
            float midAngle = -90f + i * sweep + sweep / 2f;
            float tx = cx + (float) Math.cos(Math.toRadians(midAngle)) * textR;
            float ty = cy + (float) Math.sin(Math.toRadians(midAngle)) * textR;

            // Light segment → orange text; dark segments → white text
            boolean isLight = SEGMENT_COLORS[i] == 0xFFECEFF1;
            textPaint.setColor(isLight ? 0xFFE65100 : Color.WHITE);

            canvas.save();
            canvas.rotate(midAngle + 90f, tx, ty);
            canvas.drawText(LABELS[i], tx, ty + textSize * 0.38f, textPaint);
            canvas.restore();
        }

        // ── 5. Gold dots evenly around the dots-ring ─────────────────────
        int dotCount = 24;
        float dotRadius = r * 0.038f;
        for (int i = 0; i < dotCount; i++) {
            double ang = Math.toRadians(i * 360.0 / dotCount);
            canvas.drawCircle(
                    cx + (float) Math.cos(ang) * dotsR,
                    cy + (float) Math.sin(ang) * dotsR,
                    dotRadius, dotPaint);
        }

        // ── 6. White triangle pointers at segment boundaries ──────────────
        for (int i = 0; i < n; i++) {
            drawTriangle(canvas, cx, cy, wheelR, outerR,
                    -90f + i * sweep, r);
        }

        // ── 7. Semi-white ring just outside the hub ───────────────────────
        ringPaint.setStrokeWidth(r * 0.03f);
        canvas.drawCircle(cx, cy, hubRing, ringPaint);

        // ── 8. Gold hub with radial gradient ─────────────────────────────
        centerPaint.setShader(new RadialGradient(
                cx - hubR * 0.3f, cy - hubR * 0.3f, hubR * 1.2f,
                new int[]{0xFFFFFFCC, 0xFFFFD700, 0xFFB8860B, 0xFF7A5800},
                new float[]{0f, 0.35f, 0.7f, 1f},
                Shader.TileMode.CLAMP));
        canvas.drawCircle(cx, cy, hubR, centerPaint);

        strokePaint.setStrokeWidth(r * 0.012f);
        canvas.drawCircle(cx, cy, hubR, strokePaint);
    }

    private void drawTriangle(Canvas canvas,
                               float cx, float cy,
                               float wheelR, float outerR,
                               float angleDeg, float r) {
        // Tip points inward onto the wheel edge
        double tipRad  = Math.toRadians(angleDeg);
        float  tipX    = cx + (float) Math.cos(tipRad) * (wheelR - r * 0.02f);
        float  tipY    = cy + (float) Math.sin(tipRad) * (wheelR - r * 0.02f);

        // Base sits between wheelR and outerR
        float  baseR   = (wheelR + outerR) * 0.52f;
        float  halfW   = (outerR - wheelR) * 0.38f;
        double perpRad = Math.toRadians(angleDeg + 90);

        float b1x = cx + (float) Math.cos(tipRad) * baseR + (float) Math.cos(perpRad) * halfW;
        float b1y = cy + (float) Math.sin(tipRad) * baseR + (float) Math.sin(perpRad) * halfW;
        float b2x = cx + (float) Math.cos(tipRad) * baseR - (float) Math.cos(perpRad) * halfW;
        float b2y = cy + (float) Math.sin(tipRad) * baseR - (float) Math.sin(perpRad) * halfW;

        triPath.reset();
        triPath.moveTo(tipX, tipY);
        triPath.lineTo(b1x, b1y);
        triPath.lineTo(b2x, b2y);
        triPath.close();
        canvas.drawPath(triPath, triPaint);
    }
}
