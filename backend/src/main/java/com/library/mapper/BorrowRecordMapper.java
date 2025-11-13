package com.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.entity.BorrowRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 借阅记录Mapper接口
 */
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

    /**
     * 分页查询借阅记录，包含图书信息
     */
    IPage<BorrowRecord> selectPageWithBook(Page<?> page,
                                              @Param("keyword") String keyword,
                                              @Param("status") Integer status,
                                              @Param("phoneNumber") String phoneNumber);

    /**
     * 查询图书当前借阅记录（未归还）
     */
    BorrowRecord selectCurrentByBookId(Long bookId);

    /**
     * 根据图书ID和状态查询借阅记录
     */
    BorrowRecord selectByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") Integer status);

    /**
     * 根据手机号查询用户当前借阅记录
     */
    IPage<BorrowRecord> selectByPhoneNumber(Page<?> page, String phoneNumber, Integer status);
}