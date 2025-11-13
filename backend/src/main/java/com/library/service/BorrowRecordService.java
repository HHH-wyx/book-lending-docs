package com.library.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.library.entity.BorrowRecord;

/**
 * 借阅记录Service接口
 */
public interface BorrowRecordService extends IService<BorrowRecord> {

    /**
     * 分页查询借阅记录
     */
    IPage<BorrowRecord> getBorrowRecordPage(Integer pageNum, Integer pageSize, String keyword, Integer status, String phoneNumber);

    /**
     * 添加借阅记录
     */
    boolean addBorrowRecord(BorrowRecord record);

    /**
     * 归还图书
     */
    boolean returnBook(Long recordId);

    /**
     * 根据图书ID查询当前借阅记录
     */
    BorrowRecord getCurrentByBookId(Long bookId);

    /**
     * 根据手机号查询用户借阅记录
     */
    IPage<BorrowRecord> getUserBorrowRecords(Integer pageNum, Integer pageSize, String phoneNumber);

    /**
     * 更新借阅状态
     */
    boolean updateRecordStatus(Long recordId, Integer status);
}