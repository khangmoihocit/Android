package com.khangmoihocit.th_shopping;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.khangmoihocit.th_shopping.model.CartItem;

import java.util.ArrayList;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "shop.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_DETAILS = "order_details";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng cũ
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_CART + "(id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER, quantity INTEGER)");

        // Bảng mới cho lịch sử
        // 1. Bảng lưu thông tin đơn hàng
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_date LONG, " +
                "total_amount INTEGER)");

        // 2. Bảng lưu chi tiết từng sản phẩm trong đơn hàng đó
        db.execSQL("CREATE TABLE " + TABLE_ORDER_DETAILS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +
                "product_name TEXT, " +
                "product_price INTEGER, " +
                "quantity INTEGER, " +
                "FOREIGN KEY(order_id) REFERENCES " + TABLE_ORDERS + "(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        onCreate(db);
    }

    // --- Các hàm cho Products (Không đổi) ---
    public boolean insertProduct(String name, int price) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        return db.insert(TABLE_PRODUCTS, null, cv) > 0;
    }

    public Cursor getAllProducts() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // --- Các hàm cho Cart (Không đổi, chỉ sửa tên bảng) ---
    public boolean insertToCart(int productId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, quantity FROM " + TABLE_CART + " WHERE product_id = ?", new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int qty = cursor.getInt(1) + 1;
            ContentValues cv = new ContentValues();
            cv.put("quantity", qty);
            db.update(TABLE_CART, cv, "id=?", new String[]{String.valueOf(id)});
        } else {
            ContentValues cv = new ContentValues();
            cv.put("product_id", productId);
            cv.put("quantity", 1);
            db.insert(TABLE_CART, null, cv);
        }
        cursor.close();
        return true;
    }

    public Cursor getCartItems() {
        String sql = "SELECT cart.id, products.name, products.price, cart.quantity FROM " + TABLE_CART + " JOIN " + TABLE_PRODUCTS + " ON cart.product_id = products.id";
        return getReadableDatabase().rawQuery(sql, null);
    }

    public void updateCartQuantity(int cartId, int qty) {
        ContentValues cv = new ContentValues();
        cv.put("quantity", qty);
        getWritableDatabase().update(TABLE_CART, cv, "id=?", new String[]{String.valueOf(cartId)});
    }

    public void deleteCartItem(int cartId) {
        getWritableDatabase().delete(TABLE_CART, "id=?", new String[]{String.valueOf(cartId)});
    }

    public void clearCart() {
        getWritableDatabase().delete(TABLE_CART, null, null);
    }

    // --- HÀM MỚI: Xử lý Thanh toán và Lịch sử ---

    /**
     * Chuyển giỏ hàng thành đơn hàng (thanh toán)
     */
    public long finalizeOrder(ArrayList<CartItem> cartItems, int totalAmount) {
        SQLiteDatabase db = getWritableDatabase();

        // 1. Thêm vào bảng 'orders'
        ContentValues orderValues = new ContentValues();
        orderValues.put("order_date", System.currentTimeMillis()); // Lưu ngày tháng
        orderValues.put("total_amount", totalAmount);

        // Chèn và lấy ID của đơn hàng mới
        long orderId = db.insert(TABLE_ORDERS, null, orderValues);

        if (orderId == -1) {
            return -1; // Lỗi
        }

        // 2. Thêm từng sản phẩm vào 'order_details'
        for (CartItem item : cartItems) {
            ContentValues detailValues = new ContentValues();
            detailValues.put("order_id", orderId);
            detailValues.put("product_name", item.name);
            detailValues.put("product_price", item.price);
            detailValues.put("quantity", item.quantity);
            db.insert(TABLE_ORDER_DETAILS, null, detailValues);
        }

        // 3. Xóa giỏ hàng
        clearCart();

        return orderId;
    }

    /**
     * Lấy danh sách các đơn hàng đã mua
     */
    public Cursor getOrderHistory() {
        // Lấy các đơn hàng, sắp xếp mới nhất lên trước
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_ORDERS + " ORDER BY order_date DESC", null);
    }

    /**
     * Lấy chi tiết các sản phẩm của một đơn hàng
     */
    public Cursor getOrderDetails(int orderId) {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_ORDER_DETAILS + " WHERE order_id = ?", new String[]{String.valueOf(orderId)});
    }
}

