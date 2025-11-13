package com.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 借阅记录实体类
 */
@Data
@TableName("borrow_record")
public class BorrowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 图书标题（关联查询）
     */
    @TableField(exist = false)
    private String bookTitle;

    /**
     * 图书编码（关联查询）
     */
    @TableField(exist = false)
    private String bookCode;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 借阅时间
     */
    private LocalDateTime borrowTime;

    /**
     * 应还时间
     */
    private LocalDateTime dueTime;

    /**
     * 实际归还时间
     */
    private LocalDateTime returnTime;

    /**
     * 借阅状态（0:借阅中, 1:已归还, 2:逾期）
     */
    private Integer status;

    /**
     * 获取状态名称
     */
    @TableField(exist = false)
    public String getStatusName() {
        switch (status) {
            case 0:
                return "借阅中";
            case 1:
                return "已归还";
            case 2:
                return "逾期";
            default:
                return "未知";
        }
    }
}