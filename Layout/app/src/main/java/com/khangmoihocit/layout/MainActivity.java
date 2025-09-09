package com.khangmoihocit.layout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText; // MỚI: Thêm thư viện EditText
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Integer> cardNumbers;
    private ArrayList<Button> cardButtons;
    private Button firstCard = null;
    private Button secondCard = null;
    private int firstCardValue;
    private int secondCardValue;
    private int score = 100;
    private int consecutiveMisses = 0;
    private int matchedPairs = 0;

    private TextView tvScore;
    private TextView tvPlayerName; // MỚI: Thêm biến cho TextView tên người chơi
    private GridLayout cardsGrid;
    private String playerName; // MỚI: Thêm biến để lưu tên người chơi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // THAY ĐỔI: Đảm bảo bạn dùng đúng tên tệp layout của mình
        setContentView(R.layout.layout_matching_game);

        tvScore = findViewById(R.id.tvScore);
        tvPlayerName = findViewById(R.id.tvPlayerName); // MỚI: Ánh xạ TextView tên người chơi
        cardsGrid = findViewById(R.id.cards_grid);

        // THAY ĐỔI: Không gọi setupGame() trực tiếp, gọi hộp thoại trước
        showEnterNameDialog();
    }

    // MỚI: Toàn bộ phương thức để hiển thị hộp thoại nhập tên
    private void showEnterNameDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_enter_name, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText etPlayerName = dialogView.findViewById(R.id.et_player_name);
        Button btnStartGame = dialogView.findViewById(R.id.btn_start_game);

        final AlertDialog dialog = builder.create();

        btnStartGame.setOnClickListener(v -> {
            String name = etPlayerName.getText().toString().trim();
            if (name.isEmpty()) {
                playerName = "Người chơi"; // Tên mặc định
            } else {
                playerName = name;
            }
            tvPlayerName.setText("Người chơi: " + playerName);
            dialog.dismiss();
            setupGame(); // Bắt đầu game sau khi có tên
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }


    private void setupGame() {
        score = 100;
        consecutiveMisses = 0;
        matchedPairs = 0;
        firstCard = null;
        secondCard = null;
        updateScoreDisplay();
        cardsGrid.removeAllViews();

        cardNumbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            cardNumbers.add(i);
            cardNumbers.add(i);
        }
        Collections.shuffle(cardNumbers);

        cardButtons = new ArrayList<>();
        int totalCards = 20;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        int spacing = (int) (8 * getResources().getDisplayMetrics().density);
        int cardSize = (screenWidth - 2 * padding - 3 * spacing) / 4;

        for (int i = 0; i < totalCards; i++) {
            final Button card = new Button(this);
            card.setBackground(ContextCompat.getDrawable(this, R.drawable.card_bg));
            card.setTextColor(Color.BLACK);
            card.setTextSize(28);
            card.setTag(i);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = cardSize;
            params.height = cardSize;
            params.setMargins(spacing/2, spacing/2, spacing/2, spacing/2);
            card.setLayoutParams(params);

            card.setOnClickListener(v -> handleCardClick((Button) v));
            cardButtons.add(card);
            cardsGrid.addView(card);
        }
    }

    private void handleCardClick(Button clickedCard) {
        if (secondCard != null || clickedCard.getText().length() > 0) {
            return;
        }

        int index = (int) clickedCard.getTag();
        int value = cardNumbers.get(index);
        flipCard(clickedCard, value);

        if (firstCard == null) {
            firstCard = clickedCard;
            firstCardValue = value;
        } else {
            secondCard = clickedCard;
            secondCardValue = value;
            setAllCardsEnabled(false);
            checkMatch();
        }
    }

    private void flipCard(Button card, int value) {
        card.setText(String.valueOf(value));
        card.setBackground(ContextCompat.getDrawable(this, R.drawable.card_bg_flipped));
    }

    private void checkMatch() {
        new Handler().postDelayed(() -> {
            if (firstCardValue == secondCardValue) {
                score += 10;
                consecutiveMisses = 0;
                matchedPairs++;
                firstCard.setEnabled(false);
                secondCard.setEnabled(false);
                firstCard.setAlpha(0.5f);
                secondCard.setAlpha(0.5f);

                if (matchedPairs == 10) {
                    // THAY ĐỔI: Thêm tên người chơi vào thông báo thắng
                    showGameResult("Bạn đã thắng!", "Chúc mừng " + playerName + " đã tìm thấy tất cả các cặp!");
                }
            } else {
                consecutiveMisses++;
                if (consecutiveMisses >= 3) {
                    score -= 5;
                    consecutiveMisses = 0;
                }
                firstCard.setText("");
                secondCard.setText("");
                firstCard.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.card_bg));
                secondCard.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.card_bg));
            }

            firstCard = null;
            secondCard = null;
            updateScoreDisplay();

            if (score <= 0) {
                showGameResult("Game Over!", "Bạn đã hết điểm. Hãy thử lại nhé!");
            } else {
                setAllCardsEnabled(true);
            }
        }, 800);
    }

    private void updateScoreDisplay() {
        tvScore.setText("Điểm: " + score);
    }

    private void setAllCardsEnabled(boolean isEnabled) {
        for (Button card : cardButtons) {
            if (card.isEnabled()) {
                card.setClickable(isEnabled);
            }
        }
    }

    private void showGameResult(String title, String message) {
        LayoutInflater inflater = LayoutInflater.from(this);
        // THAY ĐỔI: Đảm bảo bạn có tệp layout dialog_game_result.xml
        View dialogView = inflater.inflate(R.layout.dialog_game_result, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        TextView tvResultTitle = dialogView.findViewById(R.id.tv_result_title);
        TextView tvResultMessage = dialogView.findViewById(R.id.tv_result_message);
        TextView tvResultScore = dialogView.findViewById(R.id.tv_result_score);
        Button btnPlayAgain = dialogView.findViewById(R.id.btn_play_again);
        Button btnExit = dialogView.findViewById(R.id.btn_exit);

        tvResultTitle.setText(title);
        tvResultMessage.setText(message);
        tvResultScore.setText("Điểm cuối cùng: " + score);

        final AlertDialog dialog = builder.create();

        btnPlayAgain.setOnClickListener(v -> {
            dialog.dismiss();
            // THAY ĐỔI: Gọi lại hộp thoại nhập tên khi chơi lại
            showEnterNameDialog();
        });

        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }
}