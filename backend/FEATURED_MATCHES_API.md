# API Quản lý Trận Đấu Nổi Bật

## Tổng Quan
Mô-đun này cung cấp các endpoint để quản lý trận đấu nổi bật (featured matches) trong hệ thống quản lý bóng đá.

## Cấu trúc Files
- **`api/admin/featuredMatchController.js`** - Controller chính cho quản lý trận đấu nổi bật
- **`football_management.sql`** - Database schema (đã thêm trường `is_featured` vào bảng `matches`)

## Database Changes
Trường `is_featured` đã được thêm vào bảng `matches`:
```sql
ALTER TABLE matches ADD COLUMN `is_featured` tinyint(1) NOT NULL DEFAULT '0';
```

## API Endpoints

### 1. Cập nhật Trạng thái Trận Đấu Nổi Bật
**Route:** `PATCH /api/admin/matches/:id/featured`

**Mô tả:** Admin thiết lập hoặc hủy trận đấu nổi bật

**Yêu cầu:**
- **Headers:** Authorization token (nếu có middleware authentication)
- **Params:** 
  - `id` (number) - ID của trận đấu
- **Body:**
  ```json
  {
    "is_featured": true
  }
  ```

**Phản hồi thành công (200):**
```json
{
  "success": true,
  "message": "Đã thêm trận đấu vào danh sách nổi bật.",
  "data": {
    "id": 1,
    "phase_id": 1,
    "group_id": 3,
    "home_team_id": 1,
    "home_team_name": "Team A",
    "home_team_logo": "logo_url",
    "away_team_id": 2,
    "away_team_name": "Team B",
    "away_team_logo": "logo_url",
    "scheduled_at": "2024-03-01T00:00:00.000Z",
    "played_at": "2024-03-01T00:00:00.000Z",
    "home_score": 3,
    "away_score": 2,
    "status": "finished",
    "is_featured": 1,
    "created_at": "2026-06-20T06:17:11.499Z",
    "updated_at": "2026-06-22T10:30:00.000Z"
  }
}
```

**Phản hồi lỗi:**
- `400` - Dữ liệu không hợp lệ
```json
{
  "success": false,
  "message": "Trường is_featured phải là kiểu boolean (true/false)."
}
```
- `404` - Trận đấu không tìm thấy
```json
{
  "success": false,
  "message": "Không tìm thấy trận đấu."
}
```
- `500` - Lỗi hệ thống

---

### 2. Lấy Danh sách Trận Đấu Nổi Bật
**Route:** `GET /api/admin/matches/featured`

**Mô tả:** Lấy danh sách các trận đấu nổi bật (có phân trang)

**Query Parameters:**
- `limit` (number, optional) - Số lượng trận đấu mỗi trang (mặc định: 10)
- `offset` (number, optional) - Vị trí bắt đầu (mặc định: 0)

**Ví dụ Request:**
```
GET /api/admin/matches/featured?limit=10&offset=0
```

**Phản hồi thành công (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "phase_id": 1,
      "group_id": 3,
      "home_team_id": 1,
      "home_team_name": "Team A",
      "home_team_logo": "logo_url",
      "away_team_id": 2,
      "away_team_name": "Team B",
      "away_team_logo": "logo_url",
      "scheduled_at": "2024-03-01T00:00:00.000Z",
      "status": "finished",
      "is_featured": 1
    }
  ],
  "pagination": {
    "total": 5,
    "limit": 10,
    "offset": 0,
    "hasMore": false
  }
}
```

---

## Cách Sử Dụng

### Bước 1: Import Controller
Trong file app.js hoặc file router chính của bạn:
```javascript
const featuredMatchController = require('./api/admin/featuredMatchController');
app.use('/api', featuredMatchController);
```

### Bước 2: Sử dụng API

**Ví dụ 1 - Thêm trận đấu vào danh sách nổi bật:**
```bash
curl -X PATCH http://localhost:3000/api/admin/matches/1/featured \
  -H "Content-Type: application/json" \
  -d '{"is_featured": true}'
```

**Ví dụ 2 - Xóa trận đấu khỏi danh sách nổi bật:**
```bash
curl -X PATCH http://localhost:3000/api/admin/matches/1/featured \
  -H "Content-Type: application/json" \
  -d '{"is_featured": false}'
```

**Ví dụ 3 - Lấy danh sách trận đấu nổi bật:**
```bash
curl http://localhost:3000/api/admin/matches/featured?limit=10&offset=0
```

---

## Tích hợp Middleware Authentication

Để bảo vệ endpoint PATCH (chỉ admin được phép), hãy thêm middleware authentication:

```javascript
// Middleware kiểm tra quyền Admin
const isAdmin = async (req, res, next) => {
  try {
    // Kiểm tra token JWT hoặc session
    const user = req.user; // Từ middleware JWT
    if (!user || user.role !== 'admin') {
      return res.status(403).json({ success: false, message: 'Chỉ admin được phép.' });
    }
    next();
  } catch (error) {
    res.status(401).json({ success: false, message: 'Không được phép.' });
  }
};

// Áp dụng middleware
app.patch('/api/admin/matches/:id/featured', isAdmin, featuredMatchController);
```

---

## Ghi Chú
- Trường `is_featured` mặc định là `0` (false) cho tất cả trận đấu mới
- Khi cập nhật trạng thái, trường `updated_at` sẽ tự động cập nhật
- API trả về thông tin đội nhà/đội khách (tên và logo) để hiển thị giao diện
- Endpoint GET `/api/admin/matches/featured` không yêu cầu authentication (public)
