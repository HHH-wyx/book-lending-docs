# 图书借阅系统设计文档

## 1. 系统概述

本系统是一个图书借阅网站，实现图书的管理、搜索、借阅和归还功能。采用前后端分离架构，前端使用Vue框架，后端使用Spring Boot框架，数据库使用MySQL。

## 2. 数据库设计

### 2.1 数据库表结构

#### 2.1.1 图书分类表 (book_category)

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `INT` | `PRIMARY KEY AUTO_INCREMENT` | 分类ID |
| `name` | `VARCHAR(50)` | `NOT NULL UNIQUE` | 分类名称 |
| `description` | `VARCHAR(255)` | `NULL` | 分类描述 |
| `created_at` | `DATETIME` | `DEFAULT CURRENT_TIMESTAMP` | 创建时间 |

#### 2.1.2 图书表 (book)

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `INT` | `PRIMARY KEY AUTO_INCREMENT` | 图书ID |
| `code` | `VARCHAR(50)` | `NOT NULL UNIQUE` | 图书编码 |
| `title` | `VARCHAR(255)` | `NOT NULL` | 书名 |
| `author` | `VARCHAR(100)` | `NOT NULL` | 作者 |
| `category_id` | `INT` | `NOT NULL, FOREIGN KEY REFERENCES book_category(id)` | 分类ID |
| `status` | `VARCHAR(20)` | `NOT NULL DEFAULT 'available'` | 状态(available/borrowed) |
| `created_at` | `DATETIME` | `DEFAULT CURRENT_TIMESTAMP` | 创建时间 |
| `updated_at` | `DATETIME` | `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | 更新时间 |

#### 2.1.3 借阅记录表 (borrow_record)

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `INT` | `PRIMARY KEY AUTO_INCREMENT` | 记录ID |
| `book_id` | `INT` | `NOT NULL, FOREIGN KEY REFERENCES book(id)` | 图书ID |
| `user_phone` | `VARCHAR(20)` | `NOT NULL` | 用户手机号 |
| `borrow_date` | `DATETIME` | `DEFAULT CURRENT_TIMESTAMP` | 借阅时间 |
| `return_date` | `DATETIME` | `NULL` | 归还时间 |
| `status` | `VARCHAR(20)` | `NOT NULL DEFAULT 'borrowed'` | 状态(borrowed/returned) |

### 2.2 数据库关系图

```
+----------------+       +----------------+       +----------------+
| book_category  |       |      book      |       | borrow_record  |
+----------------+       +----------------+       +----------------+
| id (PK)        |<----->| id (PK)        |<----->| id (PK)        |
| name           |       | code           |       | book_id (FK)   |
| description    |       | title          |       | user_phone     |
| created_at     |       | author         |       | borrow_date    |
+----------------+       | category_id (FK)|       | return_date    |
                         | status         |       | status         |
                         | created_at     |       +----------------+
                         | updated_at     |
                         +----------------+
```

### 2.3 数据初始化SQL

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS books CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE books;

-- 创建图书分类表
CREATE TABLE IF NOT EXISTS book_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 创建图书表
CREATE TABLE IF NOT EXISTS book (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'available',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES book_category(id)
);

-- 创建借阅记录表
CREATE TABLE IF NOT EXISTS borrow_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    user_phone VARCHAR(20) NOT NULL,
    borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    return_date DATETIME NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'borrowed',
    FOREIGN KEY (book_id) REFERENCES book(id)
);

-- 插入测试数据
INSERT INTO book_category (name, description) VALUES
('计算机科学', '计算机相关书籍'),
('文学', '各类文学作品'),
('历史', '历史相关书籍'),
('科学', '自然科学类书籍'),
('艺术', '艺术相关书籍');

INSERT INTO book (code, title, author, category_id) VALUES
('CS001', '深入理解计算机系统', 'Randal E. Bryant', 1),
('CS002', '算法导论', 'Thomas H. Cormen', 1),
('LI001', '百年孤独', '加西亚·马尔克斯', 2),
('LI002', '三体', '刘慈欣', 2),
('HI001', '明朝那些事儿', '当年明月', 3),
('SC001', '时间简史', '史蒂芬·霍金', 4),
('AR001', '艺术的故事', '贡布里希', 5);
```

## 3. API设计

### 3.1 API概览

| API路径 | 方法 | 模块/文件 | 类型 | 功能描述 |
| :--- | :--- | :--- | :--- | :--- |
| `/api/categories` | `GET` | `CategoryController` | `Router` | 获取图书分类列表 |
| `/api/books` | `GET` | `BookController` | `Router` | 获取图书列表（支持分类筛选和搜索） |
| `/api/books/{id}` | `GET` | `BookController` | `Router` | 获取图书详情 |
| `/api/books` | `POST` | `BookController` | `Router` | 添加新图书 |
| `/api/books/{id}` | `PUT` | `BookController` | `Router` | 更新图书信息 |
| `/api/books/{id}` | `DELETE` | `BookController` | `Router` | 删除图书 |
| `/api/borrow` | `POST` | `BorrowController` | `Router` | 借阅图书 |
| `/api/return/{recordId}` | `PUT` | `BorrowController` | `Router` | 归还图书 |
| `/api/borrow/records` | `GET` | `BorrowController` | `Router` | 获取借阅记录 |
| `/api/books/search` | `GET` | `BookController` | `Router` | 搜索图书 |

### 3.2 API详细设计

#### 3.2.1 获取图书分类列表

- **路径**: `/api/categories`
- **方法**: `GET`
- **响应格式**: 
  ```json
  [
    {
      "id": 1,
      "name": "计算机科学",
      "description": "计算机相关书籍",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
  ```

#### 3.2.2 获取图书列表

- **路径**: `/api/books`
- **方法**: `GET`
- **查询参数**: 
  - `categoryId` (可选): 分类ID
  - `search` (可选): 搜索关键词
  - `page` (可选，默认1): 页码
  - `size` (可选，默认10): 每页数量
- **响应格式**: 
  ```json
  {
    "content": [
      {
        "id": 1,
        "code": "CS001",
        "title": "深入理解计算机系统",
        "author": "Randal E. Bryant",
        "category": {
          "id": 1,
          "name": "计算机科学"
        },
        "status": "available"
      }
    ],
    "totalElements": 100,
    "totalPages": 10,
    "size": 10,
    "number": 0
  }
  ```

#### 3.2.3 借阅图书

- **路径**: `/api/borrow`
- **方法**: `POST`
- **请求体**: 
  ```json
  {
    "bookId": 1,
    "userPhone": "13800138000"
  }
  ```
- **响应格式**: 
  ```json
  {
    "id": 1,
    "book": {
      "id": 1,
      "title": "深入理解计算机系统",
      "code": "CS001"
    },
    "userPhone": "13800138000",
    "borrowDate": "2024-01-01T10:00:00",
    "status": "borrowed"
  }
  ```

#### 3.2.4 归还图书

- **路径**: `/api/return/{recordId}`
- **方法**: `PUT`
- **响应格式**: 
  ```json
  {
    "id": 1,
    "book": {
      "id": 1,
      "title": "深入理解计算机系统",
      "code": "CS001"
    },
    "userPhone": "13800138000",
    "borrowDate": "2024-01-01T10:00:00",
    "returnDate": "2024-01-10T10:00:00",
    "status": "returned"
  }
  ```

## 4. 系统架构

### 4.1 前端架构

- **框架**: Vue 3 + Vite
- **状态管理**: Pinia
- **路由**: Vue Router
- **UI组件**: 自定义组件（苹果风格）
- **HTTP客户端**: Axios

### 4.2 后端架构

- **框架**: Spring Boot 3.x
- **ORM**: MyBatis-Plus
- **数据库**: MySQL 8.0
- **API文档**: SpringDoc OpenAPI
- **跨域处理**: Spring Web CORS

### 4.3 部署架构

- **开发环境**: 本地开发
- **生产环境**: 可部署到Docker容器或云服务器

## 5. 功能模块

### 5.1 图书管理模块
- 图书信息的增删改查
- 图书分类管理

### 5.2 借阅管理模块
- 用户借阅登记
- 图书归还处理
- 借阅记录查询

### 5.3 搜索模块
- 按书名、作者搜索
- 按分类筛选

## 6. 技术栈

### 6.1 前端
- **Vue 3**: 渐进式JavaScript框架
- **Vite**: 现代前端构建工具
- **Vue Router**: 路由管理
- **Pinia**: 状态管理
- **Axios**: HTTP客户端
- **CSS3**: 响应式设计

### 6.2 后端
- **Spring Boot 3.x**: Java后端框架
- **Spring MVC**: Web控制器
- **MyBatis-Plus**: ORM框架
- **MySQL 8.0**: 关系型数据库
- **SpringDoc OpenAPI**: API文档

### 6.3 开发工具
- **IDE**: IntelliJ IDEA, VS Code
- **构建工具**: Maven, npm
- **版本控制**: Git

## 7. 安全考虑

- 输入验证和参数检查
- SQL注入防护（使用ORM参数化查询）
- 跨域资源共享（CORS）配置
- 错误处理和日志记录

## 8. 性能优化

- 数据库索引优化
- 分页查询
- 前端缓存策略
- 延迟加载