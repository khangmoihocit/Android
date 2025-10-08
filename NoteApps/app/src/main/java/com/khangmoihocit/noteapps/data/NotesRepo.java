package com.khangmoihocit.noteapps.data;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class NotesRepo {
    // ArrayList tĩnh để lưu trữ dữ liệu trong bộ nhớ
    private static final List<Note> DATA = new ArrayList<>();
    // Cờ để đảm bảo dữ liệu mẫu chỉ được thêm một lần
    private static boolean seeded = false;

    /**
     * Đảm bảo rằng dữ liệu mẫu được thêm vào danh sách nếu đây là lần đầu tiên truy cập.
     * Phương thức này được đồng bộ hóa để tránh xung đột luồng.
     */
    private static synchronized void ensureSeed() {
        if (!seeded) {
            // Dữ liệu mẫu cho lần chạy đầu
            Note a = new Note();
            a.id = System.currentTimeMillis();
            a.title = "Mua đồ tạp hóa";
            a.content = "- Sữa tươi không đường\n- Bánh mì sandwich\n- 2 quả bơ";
            a.createdAt = a.updatedAt = System.currentTimeMillis();

            Note b = new Note();
            b.id = a.id + 1; // Đảm bảo ID không trùng
            b.title = "Ý tưởng cho dự án mới";
            b.content = "Viết một bài blog hướng dẫn về Jetpack Compose cho người mới bắt đầu.";
            b.createdAt = b.updatedAt = b.id;

            DATA.add(0, b);
            DATA.add(0, a);
            seeded = true;
        }
    }

    /**
     * Trả về một bản sao của danh sách tất cả các ghi chú.
     */
    public static synchronized List<Note> getAll() {
        ensureSeed();
        return new ArrayList<>(DATA); // Trả về bản sao để tránh sửa đổi trực tiếp
    }

    /**
     * Tìm một ghi chú theo ID.
     * @return một bản sao của ghi chú nếu tìm thấy, ngược lại là null.
     */
    public static synchronized Note getById(long id) {
        ensureSeed();
        for (Note n : DATA) {
            if (n.id == id) {
                return cloneNote(n);
            }
        }
        return null;
    }

    /**
     * Thêm một ghi chú mới vào đầu danh sách.
     * @return ID của ghi chú mới được tạo.
     */
    public static synchronized long insert(Note n) {
        ensureSeed();
        n.id = System.currentTimeMillis(); // Gán ID mới dựa trên thời gian hiện tại
        DATA.add(0, cloneNote(n));
        return n.id;
    }

    /**
     * Cập nhật một ghi chú đã tồn tại.
     */
    public static synchronized void update(Note n) {
        ensureSeed();
        for (int i = 0; i < DATA.size(); i++) {
            if (DATA.get(i).id == n.id) {
                DATA.set(i, cloneNote(n));
                break;
            }
        }
    }

    /**
     * Xóa một ghi chú dựa trên ID.
     */
    public static synchronized void delete(long id) {
        ensureSeed();
        DATA.removeIf(x -> x.id == id);
    }

    /**
     * Tìm kiếm ghi chú có tiêu đề hoặc nội dung chứa chuỗi truy vấn.
     * @param q Chuỗi tìm kiếm (không phân biệt chữ hoa/thường).
     * @return một danh sách các ghi chú phù hợp.
     */
    public static synchronized List<Note> search(String q) {
        ensureSeed();
        if (q == null || q.trim().isEmpty()) {
            return getAll();
        }
        String s = q.toLowerCase();
        List<Note> out = new ArrayList<>();
        for (Note n : DATA) {
            String t = (n.title == null ? "" : n.title).toLowerCase();
            String c = (n.content == null ? "" : n.content).toLowerCase();
            if (t.contains(s) || c.contains(s)) {
                out.add(cloneNote(n));
            }
        }
        return out;
    }

    /**
     * Tìm các ghi chú có lịch nhắc nhở trong tương lai.
     * @param now Thời gian hiện tại (millis) để so sánh.
     * @return một danh sách ghi chú được sắp xếp theo thời gian nhắc nhở tăng dần.
     */
    public static synchronized List<Note> findUpcoming(long now) {
        ensureSeed();
        List<Note> out = new ArrayList<>();
        for (Note n : DATA) {
            if (n.remindAt != null && n.remindAt > now) {
                out.add(cloneNote(n));
            }
        }
        // Sắp xếp các ghi chú theo thời gian nhắc nhở sớm nhất
        out.sort(Comparator.comparingLong(o -> o.remindAt));
        return out;
    }

    /**
     * Tạo một bản sao của đối tượng Note để đảm bảo tính bất biến của dữ liệu trong repo.
     */
    private static Note cloneNote(Note n) {
        Note x = new Note();
        x.id = n.id;
        x.title = n.title;
        x.content = n.content;
        x.createdAt = n.createdAt;
        x.updatedAt = n.updatedAt;
        x.remindAt = n.remindAt;
        return x;
    }
}