package com.khangmoihocit.game_caro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.Arrays;
public class CaroBoardView extends View {
    private static final int BOARD_SIZE = 8;
    private final int[][] board = new int [BOARD_SIZE] [BOARD_SIZE];
    private Paint linePaint, xPaint, oPaint;
    private float cellWidth;
    private MoveListener moveListener;
    private boolean isMyTurn =
            false;
    private String mySymbol = "X";
    private String opponentSymbol = "0";
    public interface MoveListener {
        void onMove (int row, int col);
    }
    public CaroBoardView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        resetBoard();
    }
    private void init()
    {
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
        xPaint = new Paint();
        xPaint.setColor(Color.RED);
        xPaint.setStrokeWidth(10);
        xPaint.setStyle (Paint.Style.STROKE);
        oPaint = new Paint();
        oPaint.setColor(Color.BLUE);
        oPaint.setStrokeWidth (10);
        oPaint.setStyle (Paint.Style.STROKE);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int
            heightMeasureSpec) {
        int width = MeasureSpec.getSize (widthMeasureSpec);
        setMeasuredDimension (width, width);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged (w, h, oldw, oldh);
        cellWidth = (float) w/ BOARD_SIZE;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard (canvas);
        drawPieces (canvas);
    }
    private void drawBoard (Canvas canvas) {
        for (int i=0; i <= BOARD_SIZE; i++) {
            canvas.drawLine(0, i *
                    cellWidth, getWidth(), i *
                    cellWidth, linePaint);
            canvas.drawLine(i *
                            cellWidth, 0, i * cellWidth,
                    getHeight(), linePaint);
        }
    }
    private void drawPieces (Canvas canvas) {
        for (int i=0; i < BOARD_SIZE; i++) {
            for (int j=0; j < BOARD_SIZE; j++) {
                float centerX = j * cellWidth + cellWidth / 2;
                float centerY = i * cellWidth + cellWidth / 2;
                if (board[i][j] == 1) { // Player X
                    canvas.drawLine (centerX - cellWidth / 3, centerY -
                                    cellWidth / 3, centerX + cellWidth / 3, centerY + cellWidth / 3,
                            xPaint);
                    canvas.drawLine (centerX + cellWidth / 3, centerY -
                                    cellWidth/3, centerX - cellWidth / 3, centerY + cellWidth / 3,
                            xPaint);
                } else if (board[i][j] == -1) { // Player O
                    canvas.drawCircle (centerX, centerY, cellWidth / 3,
                            oPaint);
                }
            }
        }
    }
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isMyTurn) {
            int col =
                    (int) (event.getX() / cellWidth);
            int row= (int) (event.getY() / cellWidth);
            if (row >= 0 && row <
                    BOARD_SIZE && col >= 0 && col <
                    BOARD_SIZE && board [row][col] == 0) {
                if (moveListener != null) {
                    moveListener.onMove (row, col);
                }
            } else {
                Toast.makeText(getContext(), "Không phải lượt của bạn hoặc ô đã có quân cờ!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent (event);
    }
    public void setMoveListener (MoveListener listener) {
        this.moveListener = listener;
    }
    public void updateBoard (int row, int col, String symbol) {
        if (symbol.equals("X")) {
            board[row][col] = 1;  // 1 LUÔN LUÔN là X
        } else if (symbol.equals("0")) {
            board[row][col] = -1; // -1 LUÔN LUÔN là O
        }
        // Yêu cầu vẽ lại bàn cờ với dữ liệu mới
        invalidate();
    }
    public void setMyTurn (boolean turn) {
        this.isMyTurn = turn;
    }
    public void setSymbols (String mySymbol, String opponentSymbol) {
        this.mySymbol = mySymbol;
        this.opponentSymbol =
                opponentSymbol;
    }
    public void resetBoard() {
        for (int[] row : board) {
            Arrays.fill(row, 0);
        }
        invalidate();
    }

    public String getMySymbol() {
        return mySymbol;
    }
}