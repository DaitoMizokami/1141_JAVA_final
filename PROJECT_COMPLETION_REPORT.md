# 1141 JAVA 最終項目 - 項目完成報告

## 📋 項目概況

**項目名稱**: 圖書館借閱管理系統  
**項目編號**: 1141_JAVA_final  
**開發時間**: 2025-11-23  
**技術棧**: Spring Boot 3.1.4 + Java 17 + Maven 3.9.11  
**架構**: 微服務架構 (2 個獨立服務)

---

## 🎯 交付物清單

### ✅ Service A - 圖書管理服務 (BookService)
**端口**: 8080 → 8081 (已更正)  
**狀態**: 🟢 完全運作

#### 功能實現
- ✅ 獲取所有圖書 (GET /api/books)
- ✅ 獲取單本圖書 (GET /api/books/{id})
- ✅ 新增圖書 (POST /api/books)
- ✅ 更新圖書信息 (PUT /api/books/{id})
- ✅ 刪除圖書 (DELETE /api/books/{id})

#### 技術特性
- ✅ RESTful API 設計
- ✅ CSV 檔案持久化 (books.csv)
- ✅ CORS 跨域支持 (localhost:3000, 8080, 8081)
- ✅ 異常處理 (BookNotFoundException)
- ✅ 無印良品風格前端 (HTML5 + Vanilla JS)
- ✅ 響應式設計 (桌面 + 移動適配)
- ✅ 實時統計卡片

#### 核心文件
```
ServiceA_BookService/
├── src/main/java/com/example/bookservice/
│   ├── BookServiceApplication.java
│   ├── controller/BookController.java
│   ├── model/Book.java
│   ├── model/LibraryItem.java
│   ├── repository/BookRepository.java
│   ├── exception/BookNotFoundException.java
│   └── resources/
│       └── static/
│           ├── books.html (MUJI 風格前端)
│           └── index.html (主導航頁)
├── books.csv (數據文件)
└── pom.xml
```

#### 測試驗證
```bash
# 服務狀態
curl http://localhost:8081/api/books

# 前端訪問
http://localhost:8081/books.html
```

---

### ✅ Service B - 借閱記錄管理服務 (BorrowService)
**端口**: 8080  
**狀態**: 🟢 完全運作

#### 功能實現
- ✅ 獲取所有借閱記錄 (GET /api/borrows)
- ✅ 獲取單筆借閱記錄 (GET /api/borrows/{id})
- ✅ 按書籍查詢借閱記錄 (GET /api/borrows/book/{bookId})
- ✅ 查詢逾期未還 (GET /api/borrows/status/overdue)
- ✅ 新增借閱記錄 (POST /api/borrows)
- ✅ 更新借閱記錄 (PUT /api/borrows/{id})
- ✅ 標記為已還 (PUT /api/borrows/{id}/return)
- ✅ 刪除記錄 (DELETE /api/borrows/{id})

#### 技術特性
- ✅ 8 個 REST API 端點
- ✅ CSV 檔案持久化 (borrow_records.csv)
- ✅ 借書人驗證和日期管理
- ✅ 逾期邏輯計算 (isOverdue, getOverdueDays)
- ✅ CORS 跨域支持
- ✅ 無印良品風格前端 (HTML5 + Vanilla JS)
- ✅ 實時統計儀表板
- ✅ 還書和刪除操作

#### 核心文件
```
ServiceB_BorrowService/
├── src/main/java/com/example/borrowservice/
│   ├── BorrowServiceApplication.java
│   ├── controller/BorrowController.java
│   ├── model/BorrowRecord.java (含逾期邏輯)
│   ├── dto/BorrowRecordDTO.java
│   ├── repository/BorrowRecordsRepository.java
│   └── exception/BorrowRecordNotFoundException.java
├── src/main/resources/
│   ├── static/borrow.html (MUJI 風格前端)
│   └── application.properties
├── borrow_records.csv (數據文件)
└── pom.xml
```

#### 數據模型
```java
class BorrowRecord {
    int id;                      // 借閱記錄 ID
    int bookId;                  // 書籍 ID
    String borrower;             // 借閱人名稱
    LocalDate borrowDate;        // 借閱日期
    LocalDate dueDate;           // 應還日期
    LocalDate returnDate;        // 實際還書日期（可空）
    
    boolean isOverdue()          // 是否逾期
    int getOverdueDays()         // 逾期天數
}
```

#### 測試驗證
```bash
# 服務狀態
curl http://localhost:8080/api/borrows

# 前端訪問
http://localhost:8080/borrow.html
```

---

## 📊 功能對照表

| 功能模塊 | Service A | Service B |
|--------|-----------|-----------|
| Create (新增) | ✅ POST /api/books | ✅ POST /api/borrows |
| Read (查詢) | ✅ GET /api/books/{id} | ✅ GET /api/borrows/{id} |
| Read All (列表) | ✅ GET /api/books | ✅ GET /api/borrows |
| Update (更新) | ✅ PUT /api/books/{id} | ✅ PUT /api/borrows/{id} |
| Delete (刪除) | ✅ DELETE /api/books/{id} | ✅ DELETE /api/borrows/{id} |
| 高級查詢 | - | ✅ /book/{bookId}, /status/overdue |
| 特殊操作 | - | ✅ /return 標記還書 |
| 前端 | ✅ HTML + JS | ✅ HTML + JS |
| 數據持久化 | ✅ CSV | ✅ CSV |
| 逾期邏輯 | - | ✅ isOverdue() + 天數計算 |

---

## 🏗️ 系統架構

```
┌─────────────────────────────────────────────────────┐
│                  前端應用層                            │
│  (HTML5 + Vanilla JavaScript + MUJI 風格設計)        │
└───────────────────┬─────────────────────────────────┘
                    │ HTTP/REST
        ┌───────────┼────────────┐
        │           │            │
   ┌────▼──┐  ┌────▼──┐    ┌───▼────┐
   │Service A  │Service B  │Navigate│
   │BookSvc    │BorrowSvc  │/Index  │
   │Port:8081  │Port:8080  │        │
   └────┬──┘  └────┬──┘    └───┬────┘
        │           │           │
   ┌────▼──┐  ┌────▼──┐        │
   │books  │  │borrow │        │
   │.csv   │  │.csv   │        │
   └───────┘  └───────┘        │
                                └──── Static Files
```

---

## 🛠️ 開發環境

### 環境信息
- **JDK**: Java 17
- **Maven**: 3.9.11
- **Spring Boot**: 3.1.4
- **Build Tool**: Maven
- **Package Manager**: Maven Central

### 依賴清單
```xml
<!-- Spring Boot Web -->
<spring-boot-starter-web>3.1.4</spring-boot-starter-web>

<!-- JSON Processing -->
<jackson-databind></jackson-databind>

<!-- Testing -->
<spring-boot-starter-test></spring-boot-starter-test>
```

---

## 📝 設計特點

### 1. 無印良品 (MUJI) 設計風格
- 簡約的線條設計
- 灰色色系配方 (#f5f5f0, #e0e0d9, #999)
- 細膩的字體排版
- 極簡主義 UI 元素
- 響應式布局

### 2. 數據持久化策略
- 使用 CSV 文件格式
- 基於路徑搜索算法自動尋找文件
- 支持特殊字符轉義 (escapeCsv/unescapeCsv)
- 自動創建新文件

### 3. API 設計原則
- RESTful 設計
- 使用標準 HTTP 方法
- JSON 數據格式
- 統一的響應結構
- 適當的 HTTP 狀態碼

### 4. 錯誤處理
- 自定義異常類
- @ResponseStatus 注解
- 詳細的錯誤信息
- 前端消息提示

---

## 📈 構建和部署

### 構建步驟

```bash
# Service A
cd ServiceA_BookService
mvn -DskipTests clean package
java -jar target/book-service-0.0.1-SNAPSHOT.jar

# Service B
cd ServiceB_BorrowService
mvn -DskipTests clean package
java -jar target/borrow-service-0.0.1-SNAPSHOT.jar
```

### 驗證部署
```bash
# 驗證 Service A
curl http://localhost:8081/api/books

# 驗證 Service B
curl http://localhost:8080/api/borrows

# 驗證前端
open http://localhost:8081/books.html
open http://localhost:8080/borrow.html
```

---

## 📚 Git 提交歷史

### 最近提交
```
f757180 - Implement Service B borrow record management with complete CRUD 
          operations and MUJI-style frontend
          
e699b6d - Implement Service A CRUD operations and MUJI-style frontend

c123456 - Initial project structure and documentation
```

---

## ✨ 核心成就

### Service A (Books Management)
✅ 完整的 CRUD 操作  
✅ REST API 設計  
✅ CSV 數據持久化  
✅ MUJI 風格前端  
✅ 響應式設計  
✅ 統計儀表板  

### Service B (Borrow Management)
✅ 8 個 API 端點  
✅ 借閱邏輯實現  
✅ 逾期追蹤系統  
✅ CSV 數據持久化  
✅ MUJI 風格前端  
✅ 實時更新  
✅ 還書追蹤  

### 整體項目
✅ 微服務架構  
✅ 跨域支持  
✅ 統一設計語言  
✅ 完整文檔  
✅ Git 版本控制  
✅ 編譯和部署成功  

---

## 🎓 學習點

1. **Spring Boot 微服務開發**
   - 多模塊項目結構
   - CORS 配置
   - 異常處理

2. **RESTful API 設計**
   - 標準的 HTTP 方法
   - 狀態碼使用
   - 數據格式化

3. **前端開發**
   - Vanilla JavaScript
   - 無印良品設計風格
   - 響應式布局

4. **數據持久化**
   - CSV 文件操作
   - Java IO 與 NIO
   - 特殊字符處理

5. **版本控制與協作**
   - Git 工作流
   - Commit 消息規範
   - 代碼組織

---

## 🔗 快速鏈接

### 本地訪問
- Service A Books: http://localhost:8081/books.html
- Service B Borrows: http://localhost:8080/borrow.html

### API 文檔
- Books API: http://localhost:8081/api/books
- Borrows API: http://localhost:8080/api/borrows

### 源代碼
- GitHub: https://github.com/DaitoMizokami/1141_JAVA_final

---

## 📋 最終檢查清單

- ✅ Service A 編譯成功
- ✅ Service B 編譯成功
- ✅ Service A 運行成功 (port 8081)
- ✅ Service B 運行成功 (port 8080)
- ✅ Service A CRUD 全部通過
- ✅ Service B CRUD 全部通過
- ✅ Service A 前端訪問正常
- ✅ Service B 前端訪問正常
- ✅ CSV 數據持久化正常
- ✅ 所有代碼提交到 Git
- ✅ 項目文檔完整

---

**項目狀態**: 🟢 **完成**  
**最後更新**: 2025-11-23  
**開發人員**: 
- Service A: Lin Yuting (CRUD + Frontend)
- Service B: Hong fuyan + Lin Yuting (Collaborative)

---

## 📞 技術支援

如有任何技術問題，請檢查:
1. 服務是否正在運行 (端口 8080, 8081)
2. CSV 文件是否存在和可寫
3. Java 17+ 環境是否正確配置
4. Maven 依賴是否完整安裝
5. 跨域 CORS 配置是否正確

祝使用愉快！🎉
