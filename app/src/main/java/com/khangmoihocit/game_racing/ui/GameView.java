package com.khangmoihocit.game_racing.ui;


import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.Nullable;

import com.khangmoihocit.game_racing.R;
import com.khangmoihocit.game_racing.audio.Audio;

import java.util.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    // Loop [cite: 161, 162]
    private Thread loop;
    private volatile boolean running = false, paused = false;
    private final long STEP = 16_666_667L; // 60 FPS
    private long lastNs;
    private SurfaceHolder holder; // [cite: 163]

    // World [cite: 164, 165]
    private int W, H;
    private float density = 1f;
    private final Random rand = new Random();

    // Entities [cite: 166-169]
    private Car player;
    private final List<Obstacle> obs = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final List<Particle> parts = new ArrayList<>();

    // Input [cite: 170, 171]
    private float tiltX = 0, tiltY = 0;
    private float touchTilt = 0;

    // Game state [cite: 172-175]
    private int level = 1;
    private float worldSpeed = 300;
    private float timeLeft = 60f;
    private int score = 0, lives = 3;
    private boolean gameOver = false;
    private float spawnT = 0f, coinT = 0f, nitroT = 0f, nitroLeft = 0f;

    // Road [cite: 176, 177]
    private float laneX1, laneX2, laneW;
    private float dashOffset = 0f;
    private float roadOff = 0f;

    // Paints [cite: 178, 179]
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);

    // HUD listener [cite: 180-182]
    public interface HudListener {
        void onHud(int level, int score, float timeLeft);
        void onGameOver(int finalScore); // <-- THÊM DÒNG NÀY
    }
    private HudListener hud;
    public void setHudListener(HudListener h) { this.hud = h; }


    // Audio & bitmaps [cite: 183-185]
    private final Audio audio = new Audio();
    private Bitmap roadTile, bmpPlayer, bmpCar, bmpTruck, bmpCoin, bmpNitro;

    public GameView(Context c, @Nullable AttributeSet a) {
        super(c, a);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        text.setColor(Color.WHITE);
        text.setTextSize(42f);
    } // [cite: 186]

    // Thêm hàm này để xử lý chạm màn hình
    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        int action = event.getAction();

        if (action == android.view.MotionEvent.ACTION_DOWN || action == android.view.MotionEvent.ACTION_MOVE) {
            // Nếu chạm vào nửa TRÁI màn hình
            if (event.getX() < W / 2) {
                touchTilt = 20.0f; //
            }
            // Nếu chạm vào nửa PHẢI màn hình
            else {
                touchTilt = -20.0f; //
            }
            return true; // Báo là đã xử lý chạm
        }

        if (action == android.view.MotionEvent.ACTION_UP) {
            touchTilt = 0; //
            return true;
        }

        return super.onTouchEvent(event);
    }
    // Surface [cite: 187]
    @Override public void surfaceCreated(SurfaceHolder h) {} // [cite: 188]
    @Override public void surfaceChanged(SurfaceHolder h, int f, int w, int hgt) {
        this.W = w;
        this.H = hgt;
        density = getResources().getDisplayMetrics().density;
        initWorld();
        initBitmaps();
        audio.init(getContext());
        startLoop();
    } // [cite: 189]
    @Override public void surfaceDestroyed(SurfaceHolder h) {
        stopLoop();
        freeBitmaps();
        audio.release();
    } // [cite: 190]

    // Public API [cite: 192]
    public void setLevel(int lv) {
        this.level = Math.max(1, Math.min(5, lv));
        applyLevel();
    } // [cite: 193]
    public boolean togglePause() {
        paused = !paused;
        return paused;
    } // [cite: 194]
    public void startLoop() {
        if (loop == null || !loop.isAlive()) {
            running = true;
            loop = new Thread(this);
            loop.start();
        }
    } // [cite: 195]
    public void stopLoop() {
        running = false;
        if (loop != null) {
            try { loop.join(); } catch (InterruptedException ignore) {}
        }
    } // [cite: 196]
    public void onTiltInput(float tx, float ty) {
        this.tiltX = tx;
        this.tiltY = ty;
    } // [cite: 197]

    // Init [cite: 198]
    private void initWorld() {
        laneW = W * 0.75f;
        laneX1 = (W - laneW) / 2f;
        laneX2 = laneX1 + laneW;
        float carW = 52 * density, carH = 90 * density;
        player = new Car(W / 2f - carW / 2f, H * 0.78f, carW, carH);
        obs.clear();
        coins.clear();
        parts.clear();
        score = 0;
        lives = 3;
        gameOver = false;
        timeLeft = 60f;
        spawnT = coinT = nitroT = 0f;
        nitroLeft = 0f;
        dashOffset = 0f;
        roadOff = 0f;
        applyLevel();
    } // [cite: 199, 200]

    // [cite: 204]
    float[] speed = {300, 360, 420, 500, 580};
    private void applyLevel() {
        worldSpeed = speed[level - 1]; // [cite: 202]
    } // [cite: 201]

    // Loop [cite: 203, 205]
    @Override public void run() {
        long acc = 0;
        lastNs = System.nanoTime();
        while (running) {
            long now = System.nanoTime();
            acc += (now - lastNs);
            lastNs = now;
            while (acc >= STEP) {
                if (!paused) update((float) (STEP / 1_000_000_000.0));
                acc -= STEP;
            }
            drawFrame();
        }
    } // [cite: 206]

    // Update [cite: 207]
    private void update(float dt) {
        if (gameOver) return;
        if (timeLeft <= 0) {
            gameOver = true;
            paused = true;
            if (hud != null) hud.onGameOver(score); // <-- THÊM DÒNG NÀY
            return; // <-- Thêm return để dừng update
        }
        timeLeft -= dt;
        if (timeLeft <= 0) {
            gameOver = true;
            paused = true;
        }
        if (hud != null) hud.onHud(level, score, Math.max(0f, timeLeft));
        // [cite: 208]

        float boost = 1f;
        if (tiltY > 2.0f) boost = 1.25f; // [cite: 209]
        if (nitroLeft > 0f) {
            boost = 1.5f;
            nitroLeft -= dt;
        } // [cite: 209]
        float vWorld = worldSpeed * boost;
        roadOff += vWorld * dt;
        dashOffset += vWorld * dt;
        // [cite: 209]

        // player [cite: 210]
        float sens = 70f;

//
        float totalTiltX = tiltX + touchTilt;
        player.vx += (-totalTiltX) * sens * dt; //

        player.vx *= 0.92f;
        player.x += player.vx * dt;
        if (player.x < laneX1 + 8) {
            player.x = laneX1 + 8;
            player.vx = 0;
        }
        if (player.x + player.w > laneX2 - 8) {
            player.x = laneX2 - 8 - player.w;
            player.vx = 0;
        }
        // [cite: 211]

        // spawn [cite: 212]
        float spawnInterval = new float[]{1.0f, 0.85f, 0.7f, 0.6f, 0.5f}[level - 1];
        float coinInterval = new float[]{1.2f, 1.1f, 1.0f, 0.9f, 0.8f}[level - 1];
        spawnT += dt;
        coinT += dt;
        nitroT += dt;
        // [cite: 213]

        if (spawnT >= spawnInterval) {
            spawnT = 0f;
            spawnObstacle();
        } // [cite: 214]
        if (coinT >= coinInterval) {
            coinT = 0f;
            spawnCoinRow();
        } // [cite: 214]
        if (nitroT >= 8f + rand.nextFloat() * 4f) {
            nitroT = 0f;
            spawnNitro();
        } // [cite: 214]

        // obstacles [cite: 215]
        for (Iterator<Obstacle> it = obs.iterator(); it.hasNext(); ) {
            Obstacle o = it.next();
            o.y += vWorld * dt * o.speedMul; // [cite: 217]
            if (o.y > H + 40) {
                it.remove();
                continue;
            } // [cite: 217]

            if (o.type == Obstacle.NITRO) { // [cite: 218]
                if (RectF.intersects(o.rect(), player.rect())) {
                    nitroLeft = 2.0f;
                    it.remove();
                    audio.nitro();
                    particleBurst(o.cx(), o.cy(), Color.CYAN);
                } // [cite: 219]
            } else if (RectF.intersects(o.rect(), player.rect())) { // [cite: 221]
                lives--;
                audio.hit();
                particleBurst(player.cx(), player.cy(), Color.YELLOW);
                if (lives <= 0) {
                    gameOver = true;
                    paused = true;
                    if (hud != null) hud.onGameOver(score);
                    return;
                } else {
                    player.vx = (player.cx() < o.cx() ? -120 : 120);
                }
            } // [cite: 221]
        }

        // coins [cite: 223]
        for (Iterator<Coin> it = coins.iterator(); it.hasNext(); ) {
            Coin c = it.next();
            c.y += vWorld * dt; // [cite: 224]
            if (c.y > H + 40) {
                it.remove();
                continue;
            } // [cite: 224]
            if (distance(c.x, c.y, player.cx(), player.y + player.h * 0.6f) < c.r + 16) {
                score += 10;
                audio.coin();
                it.remove();
                particleBurst(c.x, c.y, Color.argb(255, 255, 215, 0));
            } // [cite: 224]
        }

        // particles [cite: 225-227]
        for (Iterator<Particle> it = parts.iterator(); it.hasNext(); )
            if (it.next().update(dt)) it.remove();

        // score theo quãng đường [cite: 229]
        score += (int) (vWorld * 0.02f * dt); // [cite: 230]
    }

    // --- Các hàm nạp Bitmap (từ Phần 4) ---
    // [cite: 135]
    private Bitmap loadScaled(int resId, float w, float h) {
        Bitmap src = BitmapFactory.decodeResource(getResources(), resId); // [cite: 136]
        return Bitmap.createScaledBitmap(src, (int) w, (int) h, true); // [cite: 136]
    }
    private void initBitmaps() {
        float carw = 52 * density, carH = 90 * density; // [cite: 136]
        bmpPlayer = loadScaled(R.drawable.car_player, carw, carH); // [cite: 137]
        bmpCar = loadScaled(R.drawable.car_enemy, carw, carH); // [cite: 138]
        bmpTruck = loadScaled(R.drawable.truck_enemy, carw * 1.3f, carH * 1.3f); // [cite: 139]
        bmpCoin = loadScaled(R.drawable.coin, 24 * density, 24 * density); // [cite: 139]
        bmpNitro = loadScaled(R.drawable.nitro, 30 * density, 30 * density); // [cite: 140]
        roadTile = BitmapFactory.decodeResource(getResources(), R.drawable.road_tile); // [cite: 141]
    }
    private void freeBitmaps() {
        if (bmpPlayer != null) bmpPlayer.recycle(); // [cite: 142]
        if (bmpCar != null) bmpCar.recycle(); // [cite: 143]
        if (bmpTruck != null) bmpTruck.recycle(); // [cite: 143]
        if (bmpCoin != null) bmpCoin.recycle(); // [cite: 144]
        if (bmpNitro != null) bmpNitro.recycle(); // [cite: 144]
        if (roadTile != null) roadTile.recycle(); // [cite: 144]
        bmpPlayer = bmpCar = bmpTruck = bmpCoin = bmpNitro = roadTile = null; // [cite: 145]
    }

    // --- Các hàm Spawning ---
    private void spawnObstacle() {
        float w = 52 * density, h = 90 * density; // [cite: 231]
        float x = laneX1 + 20 + rand.nextFloat() * (laneW - 40 - w); // [cite: 231]
        int t = (rand.nextFloat() < new float[]{0.85f, 0.8f, 0.75f, 0.7f, 0.65f}[level - 1]) ? Obstacle.CAR : Obstacle.TRUCK; // [cite: 231]
        if (t == Obstacle.TRUCK) {
            w *= 1.3f;
            h *= 1.3f;
        } // [cite: 232]
        obs.add(new Obstacle(x, -h, w, h, t)); // [cite: 232]
    }
    private void spawnCoinRow() {
        float y = -30;
        float gap = 60;
        int n = 4 + rand.nextInt(3);
        float x0 = laneX1 + 30 + rand.nextFloat() * (laneW - 60);
        for (int i = 0; i < n; i++) {
            coins.add(new Coin(x0 + (i - n / 2f) * gap, y - i * 20));
        }
    } // [cite: 233, 234]
    private void spawnNitro() {
        float x = laneX1 + 30 + rand.nextFloat() * (laneW - 60);
        obs.add(Obstacle.nitro(x, -40, 36 * density));
    } // [cite: 235]
    private void particleBurst(float x, float y, int col) {
        for (int i = 0; i < 18; i++) {
            double ang = i * (Math.PI * 2) / 18 + rand.nextDouble() * 0.2 - 0.1;
            float sp = 120 + rand.nextFloat() * 160;
            parts.add(new Particle(x, y,
                    (float) Math.cos(ang) * sp, (float) Math.sin(ang) * sp, col));
        }
    } // [cite: 268-270]

    // Draw [cite: 236]
    private void drawFrame() {
        if (!holder.getSurface().isValid()) return; // [cite: 237]
        Canvas c = holder.lockCanvas();
        if (c == null) return;
        try {
            c.drawColor(Color.rgb(20, 20, 20)); // [cite: 239]
            drawRoadBitmap(c); // [cite: 239]

            // coins [cite: 240]
            if (bmpCoin != null) {
                for (Coin coin : coins)
                    c.drawBitmap(bmpCoin, coin.x - bmpCoin.getWidth() / 2f, coin.y - bmpCoin.getHeight() / 2f, null); // [cite: 241, 242]
            } else {
                p.setColor(Color.YELLOW);
                for (Coin coin : coins)
                    c.drawCircle(coin.x, coin.y, coin.r, p); // [cite: 242, 243]
            }

            // obstacles [cite: 244]
            for (Obstacle o : obs) {
                if (o.type == Obstacle.NITRO) { // [cite: 245]
                    if (bmpNitro != null)
                        c.drawBitmap(bmpNitro, o.x, o.y, null); // [cite: 246]
                    else {
                        p.setColor(Color.CYAN);
                        c.drawCircle(o.cx(), o.cy(), o.w * 0.6f, p);
                    } // [cite: 246]
                } else {
                    if (bmpCar != null && bmpTruck != null) { // [cite: 247]
                        Bitmap b = (o.type == Obstacle.TRUCK) ? bmpTruck : bmpCar; // [cite: 248]
                        c.drawBitmap(b, o.x, o.y, null); // [cite: 248]
                    } else {
                        p.setColor(o.type == Obstacle.CAR ? Color.RED : Color.DKGRAY);
                        c.drawRoundRect(o.rect(), 10, 10, p);
                    } // [cite: 248]
                }
            }

            // player [cite: 249]
            if (bmpPlayer != null)
                c.drawBitmap(bmpPlayer, player.x, player.y, null); // [cite: 250]
            else {
                p.setColor(Color.GREEN);
                c.drawRoundRect(player.rect(), 12, 12, p);
            } // [cite: 250]

            // particles [cite: 251, 252]
            for (Particle pt : parts) pt.draw(c, p);

            // lives [cite: 253]
            p.setColor(Color.WHITE);
            for (int i = 0; i < lives; i++)
                c.drawCircle(24 + i * 18, H - 28, 6, p); // [cite: 254]

        } finally {
            holder.unlockCanvasAndPost(c); // [cite: 255]
        }
    }

    // --- Các hàm vẽ (từ Phần 4 và 5) ---
    private void drawRoadBitmap(Canvas c) {
        if (roadTile == null) {
            drawRoad(c); // [cite: 147]
            return;
        }
        int tw = roadTile.getWidth(), th = roadTile.getHeight(); // [cite: 148]
        float left = laneX1, right = laneX2;
        int nx = (int) Math.ceil((right - left) / tw) + 1; // [cite: 148]
        int ny = (int) Math.ceil(H / (float) th) + 2; // [cite: 149]
        float oy = -(roadOff % th); // [cite: 149]
        for (int iy = 0; iy < ny; iy++)
            for (int ix = 0; ix < nx; ix++) {
                float x = left + ix * tw;
                float y = oy + iy * th;
                c.drawBitmap(roadTile, x, y, null);
            } // [cite: 150]
    }

    private void drawRoad(Canvas c) { // [cite: 256]
        p.setColor(Color.GRAY);
        c.drawRect(laneX1 - 16, 0, laneX2 + 16, H, p);
        p.setColor(Color.rgb(40, 40, 40));
        c.drawRect(laneX1, 0, laneX2, H, p);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(8);
        float dashH = 50, gap = 40;
        float off = dashOffset % (dashH + gap); // [cite: 257]
        for (float y = -off; y < H; y += dashH + gap)
            c.drawLine(W / 2f, y, W / 2f, y + dashH, p); // [cite: 257]
    }

    // Utils & entities [cite: 258]
    private static float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1, dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    } // [cite: 259]
    private static class Car {
        float x, y, w, h, vx = 0;
        Car(float x, float y, float w, float h) { this.x = x; this.y = y; this.w = w; this.h = h; }
        RectF rect() { return new RectF(x, y, x + w, y + h); }
        float cx() { return x + w / 2f; }
        float cy() { return y + h / 2f; }
    }
    private static class Obstacle {
        static final int CAR = 0, TRUCK = 1, NITRO = 2; // [cite: 262]
        float x, y, w, h;
        int type;
        float speedMul = 1f;
        Obstacle(float x, float y, float w, float h, int type) {
            this.x = x; this.y = y; this.w = w; this.h = h; this.type = type;
            if (type == TRUCK) speedMul = 0.9f;
        } // [cite: 263]
        static Obstacle nitro(float x, float y, float size) {
            Obstacle o = new Obstacle(x, y, size, size, NITRO);
            o.speedMul = 1f;
            return o;
        } // [cite: 264, 265]
        RectF rect() { return new RectF(x, y, x + w, y + h); } // [cite: 265]
        float cx() { return x + w / 2f; } // [cite: 266]
        float cy() { return y + h / 2f; } // [cite: 266]
    }
    private static class Coin {
        float x, y, r = 10;
        Coin(float x, float y) { this.x = x; this.y = y; }
    } // [cite: 267]
    private static class Particle {
        float x, y, vx, vy, life = 0.6f;
        int col;
        Particle(float x, float y, float vx, float vy, int col) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.col = col;
        } // [cite: 271, 272]
        boolean update(float dt) {
            life -= dt;
            x += vx * dt;
            y += vy * dt;
            vy += 220 * dt; // gravity
            return life <= 0;
        } // [cite: 273]
        void draw(Canvas c, Paint p) {
            int a = (int) (Math.max(0, life / 0.6f) * 220);
            p.setColor(col);
            p.setAlpha(a);
            c.drawRect(x - 3, y - 3, x + 3, y + 3, p);
            p.setAlpha(255);
        } // [cite: 274]
    }
}