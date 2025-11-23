# Service B 借閱記錄管理系統 - 測試報告

## 系統概況
- **服務名稱**: Service B - Borrow Service
- **埠號**: 8080
- **框架**: Spring Boot 3.1.4 + Java 17
- **持久化**: CSV (borrow_records.csv)
- **API 文件**: RESTful JSON API

---

## CRUD 操作測試結果 ✅

### 1. CREATE - 新增借閱記錄 (POST /api/borrows)
```bash
curl -X POST http://localhost:8080/api/borrows \
  -H "Content-Type: application/json" \
  -d '{"bookId": 1, "borrower": "Alice", "borrowDate": "2025-11-23", "dueDate": "2025-12-07"}'
```
**結果**: ✅ 成功 - 返回 ID=1 的新記錄

### 2. READ - 查詢所有記錄 (GET /api/borrows)
```bash
curl -X GET http://localhost:8080/api/borrows
```
**結果**: ✅ 成功 - 返回所有借閱記錄

### 3. READ - 查詢單筆記錄 (GET /api/borrows/{id})
```bash
curl -X GET http://localhost:8080/api/borrows/1
```
**結果**: ✅ 成功 - 返回 ID=1 的記錄詳情

### 4. READ - 查詢特定書籍的記錄 (GET /api/borrows/book/{bookId})
```bash
curl -X GET http://localhost:8080/api/borrows/book/1
```
**結果**: ✅ 成功 - 返回所有借書 ID=1 的記錄

### 5. READ - 查詢逾期未還 (GET /api/borrows/status/overdue)
```bash
curl -X GET http://localhost:8080/api/borrows/status/overdue
```
**結果**: ✅ 成功 - 返回所有逾期未還的記錄

### 6. UPDATE - 更新借閱記錄 (PUT /api/borrows/{id})
```bash
curl -X PUT http://localhost:8080/api/borrows/1 \
  -H "Content-Type: application/json" \
  -d '{"bookId": 1, "borrower": "Alice Chen", "borrowDate": "2025-11-23", "dueDate": "2025-12-10"}'
```
**結果**: ✅ 成功 - 更新借閱人名稱和應還日期

### 7. UPDATE - 標記為已還 (PUT /api/borrows/{id}/return)
```bash
curl -X PUT "http://localhost:8080/api/borrows/2/return?returnDate=2025-11-25"
```
**結果**: ✅ 成功 - 標記為已還書，並記錄還書日期

### 8. DELETE - 刪除記錄 (DELETE /api/borrows/{id})
```bash
curl -X DELETE http://localhost:8080/api/borrows/2
```
**結果**: ✅ 成功 - 刪除指定記錄

---

## 數據持久化測試 ✅

### CSV 文件驗證
**文件位置**: `/Users/linyuting/Documents/GitHub/1141_JAVA_final/borrow_records.csv`

**文件內容** (示例):
```csv
1,1,Alice Chen,2025-11-23,2025-12-10,null
```

**結果**: ✅ 成功 - 數據正確保存到 CSV 文件

---

## 前端功能測試 ✅

### 靜態文件訪問
```bash
curl -s http://localhost:8080/borrow.html | head -20
```
**結果**: ✅ 成功 - 前端 HTML 正常加載

### 前端特性
- ✅ 響應式設計 (桌面端和移動端適配)
- ✅ 無印良品風格設計
- ✅ 新增借閱記錄表單
- ✅ 借閱記錄列表表格
- ✅ 還書操作按鈕
- ✅ 刪除記錄功能
- ✅ 統計卡片 (總數、未還、已還、逾期)
- ✅ 實時刷新和消息提示

---

## 技術實現清單

### 後端組件
- ✅ `BorrowRecord.java` - 數據模型，包含逾期邏輯
- ✅ `BorrowRecordDTO.java` - API 請求/響應 DTO
- ✅ `BorrowRecordNotFoundException.java` - 自定義異常
- ✅ `BorrowRecordsRepository.java` - CSV 持久化層
- ✅ `BorrowController.java` - REST API 控制器 (8 個端點)
- ✅ `BorrowServiceApplication.java` - Spring Boot 應用主類
- ✅ `application.properties` - 應用配置 (port=8080)

### 前端組件
- ✅ `borrow.html` - 完整的無印良品風格前端

---

## 編譯和部署

### 構建
```bash
cd ServiceB_BorrowService
mvn -DskipTests clean package
```
**結果**: ✅ BUILD SUCCESS

### 運行
```bash
java -jar target/borrow-service-0.0.1-SNAPSHOT.jar
```
**結果**: ✅ 服務成功啟動在 port 8080

---

## Git 提交

**提交信息**:
```
Implement Service B borrow record management with complete CRUD operations and MUJI-style frontend
```

**包含的更改**:
- 7 個 Java 源文件
- 1 個 HTML 前端文件
- 1 個 properties 配置文件
- 1 個 CSV 數據文件

---

## 狀態總結

| 項目 | 狀態 |
|------|------|
| 後端 CRUD 實現 | ✅ 完成 |
| API 測試 | ✅ 全部通過 |
| CSV 持久化 | ✅ 正常工作 |
| 前端界面 | ✅ 完成並可訪問 |
| 構建部署 | ✅ 成功 |
| Git 提交 | ✅ 完成 |

---

**測試日期**: 2025-11-23
**測試人員**: Lin Yuting
**系統狀態**: 🟢 準備好用
