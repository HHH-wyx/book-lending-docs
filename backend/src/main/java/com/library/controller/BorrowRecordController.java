package com.library.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.common.Result;
import com.library.entity.BorrowRecord;
import com.library.service.BorrowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

/**
 * 借阅记录Controller
 */
@RestController
@RequestMapping("/api/borrow-records")
@Tag(name = "借阅管理", description = "图书借阅和归还相关接口")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 分页查询借阅记录
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询借阅记录", description = "分页查询借阅记录，支持搜索和状态筛选")
    public Result<IPage<BorrowRecord>> getBorrowRecordPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String phoneNumber) {
        
        IPage<BorrowRecord> page = borrowRecordService.getBorrowRecordPage(pageNum, pageSize, keyword, status, phoneNumber);
        return Result.success(page);
    }

    /**
     * 借阅图书
     */
    @PostMapping("/borrow")
    @Operation(summary = "借阅图书", description = "借阅图书，需要登记用户信息")
    public Result<Boolean> borrowBook(@RequestBody BorrowRecord record) {
        // 验证用户信息
        if (!validateBorrowRecord(record)) {
            return Result.fail("请填写完整的借阅信息，手机号格式不正确");
        }

        // 检查图书是否已被借阅
        BorrowRecord currentRecord = borrowRecordService.getCurrentByBookId(record.getBookId());
        if (currentRecord != null) {
            return Result.fail("该图书已被借阅");
        }

        boolean result = borrowRecordService.addBorrowRecord(record);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("借阅失败，请检查图书状态");
    }

    /**
     * 归还图书
     */
    @PostMapping("/return/{recordId}")
    @Operation(summary = "归还图书", description = "归还已借阅的图书")
    public Result<Boolean> returnBook(@PathVariable Long recordId) {
        boolean result = borrowRecordService.returnBook(recordId);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("归还失败，请检查借阅记录状态");
    }

    /**
     * 根据手机号查询用户借阅记录
     */
    @GetMapping("/user/{phoneNumber}")
    @Operation(summary = "查询用户借阅记录", description = "根据手机号查询用户的借阅记录")
    public Result<IPage<BorrowRecord>> getUserBorrowRecords(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        // 验证手机号格式
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return Result.fail("手机号格式不正确");
        }

        IPage<BorrowRecord> page = borrowRecordService.getUserBorrowRecords(pageNum, pageSize, phoneNumber);
        return Result.success(page);
    }

    /**
     * 根据图书ID查询当前借阅记录
     */
    @GetMapping("/current/{bookId}")
    @Operation(summary = "查询图书当前借阅状态", description = "根据图书ID查询当前的借阅记录")
    public Result<BorrowRecord> getCurrentByBookId(@PathVariable Long bookId) {
        BorrowRecord record = borrowRecordService.getCurrentByBookId(bookId);
        return Result.success(record);
    }

    /**
     * 验证借阅记录信息
     */
    private boolean validateBorrowRecord(BorrowRecord record) {
        // 验证必要字段
        if (record == null || record.getBookId() == null) {
            return false;
        }

        // 验证用户姓名
        if (record.getUserName() == null || record.getUserName().trim().isEmpty()) {
            return false;
        }

        // 验证手机号格式
        if (record.getPhoneNumber() == null || !PHONE_PATTERN.matcher(record.getPhoneNumber()).matches()) {
            return false;
        }

        return true;
    }
}