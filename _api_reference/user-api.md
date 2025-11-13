---
layout: default
title: 用户API参考
---

# 用户API参考

本文档详细介绍了图书借阅系统的用户相关API接口，可用于与前端系统集成。

## 基础URL

所有API请求的基础URL为：`http://localhost:8080/api`

## 用户相关接口

### 1. 用户登录

**请求信息**
- **方法**：POST
- **路径**：`/auth/login`
- **请求体**：
```json
{
  "username": "user123",
  "password": "password123"
}
```

**响应示例**
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1001,
    "username": "user123",
    "name": "张三",
    "role": "reader",
    "email": "zhangsan@example.com"
  }
}
```

### 2. 获取用户借阅记录

**请求信息**
- **方法**：GET
- **路径**：`/users/:userId/borrow-records`
- **参数**：
  - `status`：借阅状态（可选，0=未归还，1=已归还）
  - `page`：页码（可选，默认1）
  - `size`：每页数量（可选，默认10）
- **请求头**：
  - `Authorization: Bearer {token}`

**响应示例**
```json
{
  "total": 5,
  "records": [
    {
      "borrowId": 101,
      "bookId": 1,
      "bookTitle": "红楼梦",
      "borrowDate": "2024-11-15",
      "dueDate": "2024-12-15",
      "returnDate": null,
      "status": 0,
      "isOverdue": false
    },
    // 更多借阅记录...
  ]
}
```

### 3. 获取用户信息

**请求信息**
- **方法**：GET
- **路径**：`/users/:userId`
- **请求头**：
  - `Authorization: Bearer {token}`

**响应示例**
```json
{
  "id": 1001,
  "username": "user123",
  "name": "张三",
  "email": "zhangsan@example.com",
  "phone": "13800138000",
  "registerDate": "2024-01-01",
  "role": "reader",
  "active": true,
  "overdueCount": 0,
  "currentBorrowCount": 2
}
```

### 4. 更新用户信息

**请求信息**
- **方法**：PUT
- **路径**：`/users/:userId`
- **请求头**：
  - `Authorization: Bearer {token}`
- **请求体**：
```json
{
  "name": "张三(更新)",
  "email": "zhangsan_new@example.com",
  "phone": "13900139000"
}
```

**响应示例**
```json
{
  "success": true,
  "message": "用户信息更新成功",
  "user": {
    "id": 1001,
    "username": "user123",
    "name": "张三(更新)",
    "email": "zhangsan_new@example.com",
    "phone": "13900139000",
    "role": "reader"
  }
}
```

## 认证说明

- 除登录接口外，其他所有用户相关接口都需要在请求头中包含有效的JWT令牌
- 令牌有效期通常为24小时，过期后需要重新登录获取新令牌
- 不同角色（管理员、读者等）拥有不同的权限，某些接口可能会根据角色返回不同的数据或拒绝访问

## 错误处理

所有API返回的错误响应格式如下：

```json
{
  "success": false,
  "errorCode": "ERROR_CODE",
  "message": "错误描述"
}

常见错误代码：
- `INVALID_CREDENTIALS`：用户名或密码错误
- `TOKEN_EXPIRED`：令牌已过期
- `INVALID_TOKEN`：无效的令牌
- `USER_NOT_FOUND`：用户不存在
- `PERMISSION_DENIED`：权限不足
```