# 🚀 快速開始指南

## 環境要求
- Java 17+
- Maven 3.9+
- macOS/Linux/Windows

---

## 🏃 立即運行

### 方式 1: 使用已編譯的 JAR 檔案

```bash
# 啟動 Service A (Books - 端口 8081)
cd /Users/linyuting/Documents/GitHub/1141_JAVA_final/ServiceA_BookService
java -jar target/book-service-0.0.1-SNAPSHOT.jar

# 在新終端啟動 Service B (Borrows - 端口 8080)
cd /Users/linyuting/Documents/GitHub/1141_JAVA_final/ServiceB_BorrowService
java -jar target/borrow-service-0.0.1-SNAPSHOT.jar
```

### 方式 2: 從源代碼編譯

```bash
# 編譯 Service A
cd ServiceA_BookService
mvn -DskipTests clean package
java -jar target/book-service-0.0.1-SNAPSHOT.jar

# 編譯 Service B (新終端)
cd ServiceB_BorrowService
mvn -DskipTests clean package
java -jar target/borrow-service-0.0.1-SNAPSHOT.jar
```

---

## 📱 訪問應用

### Web 前端
- **圖書管理** (Service A): http://localhost:8081/books.html
- **借閱記錄** (Service B): http://localhost:8080/borrow.html
- **主導航頁**: http://localhost:8081/index.html

### REST API

#### Service A - 圖書管理
```bash
# 獲取所有圖書
curl http://localhost:8081/api/books

# 新增圖書
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"新書","author":"作者","status":"AVAILABLE"}'

# 獲取單本圖書
curl http://localhost:8081/api/books/1

# 更新圖書
curl -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"更新標題","author":"作者","status":"AVAILABLE"}'

# 刪除圖書
curl -X DELETE http://localhost:8081/api/books/1
```

#### Service B - 借閱記錄
```bash
# 獲取所有借閱記錄
curl http://localhost:8080/api/borrows

# 新增借閱記錄
curl -X POST http://localhost:8080/api/borrows \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"borrower":"借閱人","borrowDate":"2025-11-23","dueDate":"2025-12-07"}'

# 獲取單筆記錄
curl http://localhost:8080/api/borrows/1

# 按書籍查詢
curl http://localhost:8080/api/borrows/book/1

# 查詢逾期未還
curl http://localhost:8080/api/borrows/status/overdue

# 更新記錄
curl -X PUT http://localhost:8080/api/borrows/1 \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"borrower":"新名字","borrowDate":"2025-11-23","dueDate":"2025-12-10"}'

# 標記為已還
curl -X PUT "http://localhost:8080/api/borrows/1/return?returnDate=2025-11-25"

# 刪除記錄
curl -X DELETE http://localhost:8080/api/borrows/1
```

---

## 📊 常用命令

### 檢查服務狀態
```bash
# 檢查 Service A
curl -i http://localhost:8081/api/books

# 檢查 Service B
curl -i http://localhost:8080/api/borrows
```

### 查看數據文件
```bash
# 圖書數據
cat ServiceA_BookService/books.csv

# 借閱記錄數據
cat borrow_records.csv
```

### 開發模式編譯
```bash
# 快速編譯（跳過測試）
mvn -DskipTests clean compile

# 完整編譯和打包
mvn clean package

# 運行測試
mvn test
```

---

## 🎨 設計風格

所有前端採用 **無印良品 (MUJI)** 設計風格：
- 簡約線條
- 灰色配方 (#f5f5f0, #e0e0d9)
- 細膩排版
- 響應式布局

---

## 📝 數據格式

### 圖書格式 (CSV)
```csv
id,title,author,status
1,Java Programming,John Doe,AVAILABLE
```

### 借閱記錄格式 (CSV)
```csv
id,bookId,borrower,borrowDate,dueDate,returnDate
1,1,Alice Chen,2025-11-23,2025-12-10,null
```

---

## 🐛 故障排除

### Service 無法啟動
```bash
# 檢查端口是否被占用
lsof -i :8080
lsof -i :8081

# 殺掉占用端口的進程
kill -9 <PID>
```

### CSV 文件錯誤
```bash
# 檢查文件權限
ls -la books.csv
ls -la borrow_records.csv

# 重新創建文件（Service 會自動創建）
rm books.csv borrow_records.csv
```

### 編譯失敗
```bash
# 清理並重新編譯
mvn clean install

# 確認 Java 版本
java -version
# 應顯示 Java 17+
```

---

## 📚 更多文檔

- 詳細的項目報告: `PROJECT_COMPLETION_REPORT.md`
- Service B 測試報告: `SERVICE_B_TEST_REPORT.md`
- Service A README: `ServiceA_BookService/README.md`

---

## 💡 提示

1. **首次使用**: CSV 文件會自動創建，無需手動創建
2. **數據持久化**: 所有數據自動保存到 CSV 檔案
3. **跨域請求**: 已配置 CORS，支持 localhost:3000, 8080, 8081
4. **日期格式**: 統一使用 `YYYY-MM-DD` 格式
5. **實時更新**: 前端自動刷新數據，無需手動刷新

---

## 🎯 快速測試

複製以下命令立即體驗：

```bash
# 1. 新增圖書
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"測試書籍","author":"測試作者","status":"AVAILABLE"}'

# 2. 新增借閱記錄
curl -X POST http://localhost:8080/api/borrows \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"borrower":"測試用戶","borrowDate":"2025-11-23","dueDate":"2025-12-07"}'

# 3. 查看所有記錄
curl http://localhost:8080/api/borrows | python3 -m json.tool

# 4. 在瀏覽器中訪問
# 打開 http://localhost:8081/books.html
# 打開 http://localhost:8080/borrow.html
```

---

祝使用愉快！如有任何問題，請參考項目文檔或提出 Issue。

🚀 **系統已準備就緒！** 🚀
