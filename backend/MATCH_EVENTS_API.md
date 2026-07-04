# API Quản Lý Sự Kiện Trận Đấu

## 📋 Danh Sách API

### 1️⃣ CẬP NHẬT TỈ SỐ
**Endpoint:** `PUT /api/match-events/:matchId/score`

**Mô tả:** Cập nhật tỉ số của trận đấu

**Parameters:**
- `matchId` (URL): ID của trận đấu

**Request Body:**
```json
{
  "home_score": 2,
  "away_score": 1,
  "status": "ongoing"  // tùy chọn: scheduled, ongoing, finished, etc.
}
```

**Response:**
```json
{
  "success": true,
  "message": "Cập nhật tỉ số thành công.",
  "data": {
    "matchId": 1,
    "home_score": 2,
    "away_score": 1,
    "status": "ongoing"
  }
}
```

---

### 2️⃣ THAY CẦU THỦ
**Endpoint:** `POST /api/match-events/:matchId/substitution`

**Mô tả:** Ghi nhận sự kiện thay cầu thủ

**Parameters:**
- `matchId` (URL): ID của trận đấu

**Request Body:**
```json
{
  "team_id": 1,
  "player_in_id": 5,      // ID cầu thủ vào sân
  "player_out_id": 3,     // ID cầu thủ rời sân
  "minute": 45,
  "period": "first_half"  // first_half, second_half, extra_time_first, extra_time_second, penalty_shootout
}
```

**Response:**
```json
{
  "success": true,
  "message": "Ghi nhận thay cầu thủ thành công.",
  "data": {
    "matchId": 1,
    "team_id": 1,
    "player_out_id": 3,
    "player_in_id": 5,
    "minute": 45,
    "period": "first_half",
    "eventIds": [123, 124]
  }
}
```

---

### 3️⃣ GHI NHẬN THẺ VÀNG
**Endpoint:** `POST /api/match-events/:matchId/yellow-card`

**Mô tả:** Ghi nhận thẻ vàng cho cầu thủ

**Parameters:**
- `matchId` (URL): ID của trận đấu

**Request Body:**
```json
{
  "team_id": 1,
  "player_id": 7,
  "minute": 32,
  "period": "first_half",
  "note": "Phạm lỗi nguy hiểm"  // tùy chọn
}
```

**Response:**
```json
{
  "success": true,
  "message": "Ghi nhận thẻ vàng thành công.",
  "data": {
    "eventId": 125,
    "matchId": 1,
    "team_id": 1,
    "player_id": 7,
    "card_color": "yellow",
    "minute": 32,
    "period": "first_half",
    "note": "Phạm lỗi nguy hiểm"
  }
}
```

---

### 4️⃣ GHI NHẬN THẺ ĐỎ
**Endpoint:** `POST /api/match-events/:matchId/red-card`

**Mô tả:** Ghi nhận thẻ đỏ cho cầu thủ

**Parameters:**
- `matchId` (URL): ID của trận đấu

**Request Body:**
```json
{
  "team_id": 2,
  "player_id": 12,
  "minute": 55,
  "period": "second_half",
  "note": "Hành động bạo lực"  // tùy chọn
}
```

**Response:**
```json
{
  "success": true,
  "message": "Ghi nhận thẻ đỏ thành công.",
  "data": {
    "eventId": 126,
    "matchId": 1,
    "team_id": 2,
    "player_id": 12,
    "card_color": "red",
    "minute": 55,
    "period": "second_half",
    "note": "Hành động bạo lực"
  }
}
```

---

### 5️⃣ LẤY DANH SÁCH SỰ KIỆN TRONG TRẬN
**Endpoint:** `GET /api/match-events/:matchId`

**Mô tả:** Lấy tất cả sự kiện của trận đấu (bàn thắng, thay cầu thủ, thẻ vàng, thẻ đỏ)

**Parameters:**
- `matchId` (URL): ID của trận đấu

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 54,
      "match_id": 1,
      "player_id": 2,
      "player_name": "Nguyễn Văn A",
      "team_id": 1,
      "team_name": "Đội A",
      "type": "goal",
      "minute": 20,
      "period": "first_half",
      "note": null,
      "card_color": null,
      "sub_out_player_id": null,
      "sub_out_player_name": null,
      "created_at": "2026-06-20T06:17:11.506Z"
    },
    {
      "id": 59,
      "match_id": 1,
      "player_id": 7,
      "player_name": "Trần Văn B",
      "team_id": 1,
      "team_name": "Đội A",
      "type": "yellow_card",
      "minute": 37,
      "period": "first_half",
      "note": null,
      "card_color": "yellow",
      "sub_out_player_id": null,
      "created_at": "2026-06-20T06:17:11.571Z"
    }
  ]
}
```

---

### 6️⃣ XÓA SỰ KIỆN
**Endpoint:** `DELETE /api/match-events/:eventId`

**Mô tả:** Xóa một sự kiện

**Parameters:**
- `eventId` (URL): ID của sự kiện

**Response:**
```json
{
  "success": true,
  "message": "Xóa sự kiện thành công.",
  "eventId": 125
}
```

---

## 📝 Ví Dụ Sử Dụng (cURL)

### Cập nhật tỉ số
```bash
curl -X PUT http://localhost:3000/api/match-events/1/score \
  -H "Content-Type: application/json" \
  -d '{"home_score": 2, "away_score": 1, "status": "ongoing"}'
```

### Ghi nhận thay cầu thủ
```bash
curl -X POST http://localhost:3000/api/match-events/1/substitution \
  -H "Content-Type: application/json" \
  -d '{
    "team_id": 1,
    "player_in_id": 5,
    "player_out_id": 3,
    "minute": 45,
    "period": "first_half"
  }'
```

### Ghi nhận thẻ vàng
```bash
curl -X POST http://localhost:3000/api/match-events/1/yellow-card \
  -H "Content-Type: application/json" \
  -d '{
    "team_id": 1,
    "player_id": 7,
    "minute": 32,
    "period": "first_half",
    "note": "Phạm lỗi"
  }'
```

### Ghi nhận thẻ đỏ
```bash
curl -X POST http://localhost:3000/api/match-events/1/red-card \
  -H "Content-Type: application/json" \
  -d '{
    "team_id": 2,
    "player_id": 12,
    "minute": 55,
    "period": "second_half"
  }'
```

### Lấy danh sách sự kiện
```bash
curl http://localhost:3000/api/match-events/1
```

### Xóa sự kiện
```bash
curl -X DELETE http://localhost:3000/api/match-events/125
```

---

## 🔄 Các Loại Period
- `first_half` - Hiệp 1
- `second_half` - Hiệp 2
- `extra_time_first` - Hiệp phụ 1
- `extra_time_second` - Hiệp phụ 2
- `penalty_shootout` - Loạt đá luân lưu

## 🔄 Các Loại Sự Kiện
- `goal` - Bàn thắng
- `own_goal` - Phản lưới
- `yellow_card` - Thẻ vàng
- `red_card` - Thẻ đỏ
- `second_yellow` - Thẻ vàng thứ 2
- `substitution_in` - Cầu thủ vào sân
- `substitution_out` - Cầu thủ rời sân
- `penalty_scored` - Ghi bàn từ 11m
- `penalty_missed` - Sút hỏng 11m

---

## ✅ Tính Năng
✓ Cập nhật tỉ số thời gian thực
✓ Quản lý thay cầu thủ
✓ Ghi nhận thẻ vàng và thẻ đỏ
✓ Lưu lịch sử tất cả sự kiện
✓ Xem chi tiết từng sự kiện (cầu thủ, thời gian, kỳ, ghi chú)
✓ Xóa sự kiện khi cần sửa
