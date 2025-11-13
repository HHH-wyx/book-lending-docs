---
layout: default
title: 图书API参考
---

# 图书API参考

本文档详细介绍了图书借阅系统的图书相关API接口，可用于与前端系统集成。

## 基础URL

所有API请求的基础URL为：`http://localhost:8080/api`

## 图书相关接口

### 1. 获取图书列表

**请求信息**
- **方法**：GET
- **路径**：`/books`
- **参数**：
  - `page`：页码（可选，默认1）
  - `size`：每页数量（可选，默认10）
  - `category`：图书分类（可选）
  - `status`：图书状态（可选，0=可借阅，1=已借出）
  - `search`：搜索关键词（可选）

**响应示例**
```json
{
  "total": 100,
  "books": [
    {
      "id": 1,
      "title": "红楼梦",
      "author": "曹雪芹",
      "isbn": "9787020002207",
      "category": "文学",
      "publishDate": "1791-01-01",
      "status": 0,
      "description": "中国古典四大名著之一"
    },
    // 更多图书数据...
  ]
}
```

### 2. 获取图书详情

**请求信息**
- **方法**：GET
- **路径**：`/books/:id`
- **参数**：无

**响应示例**
```json
{
  "id": 1,
  "title": "红楼梦",
  "author": "曹雪芹",
  "isbn": "9787020002207",
  "category": "文学",
  "publishDate": "1791-01-01",
  "status": 0,
  "description": "中国古典四大名著之一",
  "coverImage": "/images/book1.jpg",
  "totalCopies": 5,
  "availableCopies": 3
}
```

### 3. 借阅图书

**请求信息**
- **方法**：POST
- **路径**：`/borrow`
- **请求体**：
```json
{
  "bookId": 1,
  "userId": 1001,
  "expectedReturnDate": "2024-12-31"
}
```

**响应示例**
```json
{
  "success": true,
  "borrowId": 101,
  "message": "借阅成功",
  "dueDate": "2024-12-31"
}
```

### 4. 归还图书

**请求信息**
- **方法**：POST
- **路径**：`/return`
- **请求体**：
```json
{
  "borrowId": 101,
  "userId": 1001
}
```

**响应示例**
```json
{
  "success": true,
  "message": "归还成功",
  "actualReturnDate": "2024-12-15",
  "isOverdue": false
}
```

## 错误处理

所有API返回的错误响应格式如下：

```json
{
  "success": false,
  "errorCode": "ERROR_CODE",
  "message": "错误描述"
}

常见错误代码：
- `BOOK_NOT_FOUND`：图书不存在
- `BOOK_UNAVAILABLE`：图书不可借阅
- `BORROW_NOT_FOUND`：借阅记录不存在
- `USER_NOT_FOUND`：用户不存在
- `PERMISSION_DENIED`：权限不足
```