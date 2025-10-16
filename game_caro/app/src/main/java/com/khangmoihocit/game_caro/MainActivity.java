package com.khangmoihocit.game_caro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    // IP 10.0.2.2 là địa chỉ đặc biệt để máy ảo Android kết nối đến localhost của máy tính
    private static final String SERVER_IP = "10.0.2.2";
    private static final int SERVER_PORT = 9876;
    private ExecutorService executorService;
    private Handler handler;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private CaroBoardView caroBoardView;
    private TextView statusTextView, myScoreTextView,
            opponentScoreTextView;
    private EditText roomIdEditText, messageEditText;
    private Button createButton, joinButton, sendButton, rematchButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<String> chatMessages;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTextView = findViewById(R.id.statusTextView);
        myScoreTextView = findViewById(R.id.myScoreTextView);
        opponentScoreTextView = findViewById(R.id.opponentScoreTextView);
        roomIdEditText = findViewById(R.id.roomIdEditText);
        messageEditText = findViewById(R.id.messageEditText);
        createButton = findViewById(R.id.createButton);
        joinButton = findViewById(R.id.joinButton);
        sendButton = findViewById(R.id.sendButton);
        rematchButton = findViewById(R.id.rematchButton);
        caroBoardView = findViewById(R.id.caroBoardView);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        chatRecyclerView.setAdapter (chatAdapter);


        // dùng một pool luồng để có thể gửi và nhận đồng thời.
        executorService = Executors.newCachedThreadPool();

        handler = new Handler(Looper.getMainLooper());
        connectToServer();

        createButton.setOnClickListener(v -> {
            String roomId = roomIdEditText.getText().toString().trim();
            if (!roomId.isEmpty()){
                // Đã sửa, không có khoảng trắng sau dấu phẩy
                sendMessageToServer("CREATE_ROOM," + roomId);
            } else {
                Toast.makeText(this, "Vui lòng nhập tên phòng.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        joinButton.setOnClickListener (v -> {
            String roomId = roomIdEditText.getText().toString().trim();
            if (!roomId.isEmpty()) {
                // Đã sửa, không có khoảng trắng sau dấu phẩy
                sendMessageToServer ("JOIN_ROOM," + roomId);
            } else {
                Toast.makeText(this, "Vui lòng nhập tên phòng.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        sendButton.setOnClickListener(v -> {
            String message =
                    messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessageToServer("CHAT_MESSAGE," + message);
                addChatMessage("Bạn: " + message);
                messageEditText.setText("");
            }
        });

        rematchButton.setOnClickListener(v -> {
            sendMessageToServer ("REMATCH_REQUEST");
            rematchButton.setVisibility(View.GONE);
        });

        caroBoardView.setMoveListener((row, col) -> {
            sendMessageToServer("MOVE," + row + ","
                    + col);
            caroBoardView.setMyTurn (false);
            updateStatus ("Đã gửi nước đi, chờ đối thủ...");
        });
    }

    private void addChatMessage (String message) {
        handler.post(() -> {
            chatMessages.add(message);
            chatAdapter.notifyItemInserted (chatMessages.size()-1);
            chatRecyclerView.scrollToPosition (chatMessages.size()-1);
        });
    }

    private void connectToServer(){
        executorService.execute(() -> {
            try {
                clientSocket = new Socket (SERVER_IP, SERVER_PORT);
                out = new PrintWriter(clientSocket.getOutputStream(),
                        true);
                in = new BufferedReader (new
                        InputStreamReader (clientSocket.getInputStream()));
                updateStatus ("Đã kết nối đến Server.");
                String serverMessage;
                // Luồng này sẽ bị chặn ở đây để lắng nghe,
                // đó là lý do chúng ta cần một luồng khác để gửi đi.
                while ((serverMessage = in.readLine())
                        != null) {
                    processServerMessage (serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Hiển thị lỗi rõ ràng hơn
                handler.post(() -> updateStatus ("Không thể kết nối đến Server: " + e.getMessage()));
            }
        });
    }

    private void sendMessageToServer (String message) {
        // Tác vụ này sẽ chạy trên một luồng khác (nhờ newCachedThreadPool)
        // và sẽ không bị chặn bởi vòng lặp readLine() trong connectToServer()
        executorService.execute(() -> {
            if (out != null) {
                out.println(message);
            }
        });
    }

    private void processServerMessage (String message) {
        handler.post(() -> {
            String[] parts = message.split(",");
            if (parts.length == 0) return; // Tránh lỗi nếu tin nhắn rỗng
            String command = parts[0];

            switch (command) {
                case "ROOM_CREATED":
                    updateStatus ("Phòng " + parts [1] + " đã được tạo. Chờ người chơi khác...");
                    setRoomControlsEnabled(false);
                    break;
                case "ROOM_JOINED":
                    updateStatus ("Đã tham gia phòng " + parts [1] + ". Chờ trò chơi bắt đầu...");
                    setRoomControlsEnabled(false);
                    break;

                case "START_GAME":
                    String mySymbol = parts [1];
                    String opponentSymbol = parts [2];
                    caroBoardView.setSymbols (mySymbol, opponentSymbol);
                    caroBoardView.resetBoard();
                    rematchButton.setVisibility (View.GONE);


                    if (mySymbol.equals("X")) {
                        updateStatus("Trò chơi bắt đầu! Lượt của bạn (X)");
                        caroBoardView.setMyTurn(true);
                    } else {
                        updateStatus("Trò chơi bắt đầu! Chờ đối thủ (X) đi...");
                        caroBoardView.setMyTurn(false);
                    }
                    break;

                case "MOVE":
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt (parts [2]);
                    String symbol = parts [3];
                    caroBoardView.updateBoard (row, col, symbol);


                    if (symbol.equals(caroBoardView.getMySymbol())) {
                        updateStatus ("Chờ đối thủ đánh...");
                    } else {
                        // Nếu nước đi là của đối thủ, thì giờ là lượt mình
                        updateStatus ("Đến lượt bạn!");
                        caroBoardView.setMyTurn(true);
                    }
                    break;

                case "GAME_OVER":
                    if (parts [1].equals("WINNER")) {
                        String winner = parts [2];
                        int score1 = Integer.parseInt(parts[3]); //// Điểm của Client 1 (X)
                        int score2 = Integer.parseInt(parts[4]); //// Điểm của Client 2 (O)


                        int myScore = (caroBoardView.getMySymbol().equals("X")) ? score1 : score2;
                        int opponentScore = (caroBoardView.getMySymbol().equals("X")) ? score2 : score1;

                        updateScore (myScore, opponentScore);
                        updateStatus ("Kết thúc! Người chiến thắng là "
                                + winner);
                        Toast.makeText(this, "Người chiến thắng là " +
                                winner, Toast.LENGTH_LONG).show();

                    } else if (parts[1].equals("DRAW")) {
                        int score1 = Integer.parseInt(parts [2]); //// Điểm Client 1 (X)
                        int score2 = Integer.parseInt(parts[3]); //// Điểm Client 2 (O)


                        int myScore = (caroBoardView.getMySymbol().equals("X")) ? score1 : score2;
                        int opponentScore = (caroBoardView.getMySymbol().equals("X")) ? score2 : score1;

                        updateScore (myScore, opponentScore);
                        updateStatus ("Kết thúc! Hỏa.");
                        Toast.makeText(this, "Trận đấu hòa.",
                                Toast.LENGTH_LONG).show();
                    }
                    caroBoardView.setMyTurn (false);
                    rematchButton.setVisibility (View.VISIBLE);
                    break;

                case "REMATCH_START":
                    caroBoardView.resetBoard();
                    rematchButton.setVisibility(View.GONE);


                    if (caroBoardView.getMySymbol().equals("X")) {
                        updateStatus("Trận mới bắt đầu! Lượt của bạn (X)");
                        caroBoardView.setMyTurn(true);
                    } else {
                        updateStatus("Trận mới bắt đầu! Chờ đối thủ (X) đi...");
                        caroBoardView.setMyTurn(false);
                    }
                    break;

                case "CHAT_MESSAGE":
                    // Tách tin nhắn từ sau dấu phẩy đầu tiên
                    String chatMsg = message.substring(command.length() + 1);
                    addChatMessage("Đối thủ: " + chatMsg);
                    break;

                case "ERROR":
                    // Tách tin nhắn lỗi từ sau dấu phẩy đầu tiên
                    String errorMsg = message.substring(command.length() + 1);
                    updateStatus("Lỗi: " + errorMsg);
                    Toast.makeText(this, "Lỗi: " + errorMsg, Toast.LENGTH_LONG).show();
                    setRoomControlsEnabled(true);
                    break;

                case "MESSAGE":
                    // Tách tin nhắn thông báo từ sau dấu phẩy đầu tiên
                    String infoMsg = message.substring(command.length() + 1);
                    Toast.makeText(this, infoMsg,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void updateStatus (String status) {
        handler.post(() -> statusTextView.setText("Trạng thái: " +
                status));
    }

    private void updateScore (int myScore, int opponentScore) {
        handler.post(() -> {
            myScoreTextView.setText("Bạn: " + myScore);
            opponentScoreTextView.setText("Đối thủ: " + opponentScore);
        });
    }

    private void setRoomControlsEnabled (boolean enabled) {
        handler.post(() -> {
            roomIdEditText.setEnabled (enabled);
            createButton.setEnabled (enabled);
            joinButton.setEnabled(enabled);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng socket và dừng luồng khi Activity bị hủy
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}