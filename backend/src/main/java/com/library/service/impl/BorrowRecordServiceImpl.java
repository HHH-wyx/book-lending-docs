package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.mapper.BorrowRecordMapper;
import com.library.service.BookService;
import com.library.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 借阅记录Service实现类
 */
@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {

    @Autowired
    private BookService bookService;

    @Override
    public IPage<BorrowRecord> getBorrowRecordPage(Integer pageNum, Integer pageSize, String keyword, Integer status, String phoneNumber) {
        Page<BorrowRecord> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectPageWithBook(page, keyword, status, phoneNumber);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBorrowRecord(BorrowRecord record) {
        // 检查图书是否存在且可借阅
        Book book = bookService.getBookById(record.getBookId());
        if (book == null || book.getStatus() != 0) {
            return false;
        }

        // 设置借阅时间和应还时间（默认30天）
        LocalDateTime borrowTime = LocalDateTime.now();
        record.setBorrowTime(borrowTime);
        record.setDueTime(borrowTime.plus(30, ChronoUnit.DAYS));
        record.setStatus(0); // 借阅中

        // 保存借阅记录
        if (!save(record)) {
            return false;
        }

        // 更新图书状态为已借出
        return bookService.updateBookStatus(record.getBookId(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean returnBook(Long recordId) {
        // 查询借阅记录
        BorrowRecord record = getById(recordId);
        if (record == null || record.getStatus() != 0) {
            return false;
        }

        // 设置归还时间和状态
        record.setReturnTime(LocalDateTime.now());
        record.setStatus(1); // 已归还

        // 更新借阅记录
        if (!updateById(record)) {
            return false;
        }

        // 更新图书状态为可借阅
        return bookService.updateBookStatus(record.getBookId(), 0);
    }

    @Override
    public BorrowRecord getCurrentByBookId(Long bookId) {
        return baseMapper.selectCurrentByBookId(bookId);
    }

    @Override
    public IPage<BorrowRecord> getUserBorrowRecords(Integer pageNum, Integer pageSize, String phoneNumber) {
        Page<BorrowRecord> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectByPhoneNumber(page, phoneNumber, 0); // 只查询借阅中的记录
    }

    @Override
    public boolean updateRecordStatus(Long recordId, Integer status) {
        BorrowRecord record = new BorrowRecord();
        record.setId(recordId);
        record.setStatus(status);
        return updateById(record);
    }

    /**
     * 定时更新逾期状态
     */
    public void updateOverdueStatus() {
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0) // 借阅中
                   .lt("due_time", LocalDateTime.now()); // 已过应还时间
        
        BorrowRecord updateRecord = new BorrowRecord();
        updateRecord.setStatus(2); // 逾期
        
        update(updateRecord, queryWrapper);
    }
}