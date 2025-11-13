-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS books DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE books;

-- 删除已存在的表
DROP TABLE IF EXISTS borrow_record;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS book_category;

-- 创建图书分类表
CREATE TABLE book_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    sort INT DEFAULT 0 COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建图书表
CREATE TABLE book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图书ID',
    code VARCHAR(20) NOT NULL UNIQUE COMMENT '图书编码',
    title VARCHAR(100) NOT NULL COMMENT '书名',
    author VARCHAR(50) NOT NULL COMMENT '作者',
    category_id BIGINT COMMENT '分类ID',
    status TINYINT DEFAULT 0 COMMENT '状态（0:可借阅, 1:已借出, 2:下架）',
    description TEXT COMMENT '简介',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES book_category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建借阅记录表
CREATE TABLE borrow_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    book_id BIGINT NOT NULL COMMENT '图书ID',
    user_name VARCHAR(50) NOT NULL COMMENT '用户姓名',
    phone_number VARCHAR(20) NOT NULL COMMENT '用户手机号',
    borrow_time DATETIME NOT NULL COMMENT '借阅时间',
    due_time DATETIME NOT NULL COMMENT '应还时间',
    return_time DATETIME COMMENT '实际归还时间',
    status TINYINT DEFAULT 0 COMMENT '状态（0:借阅中, 1:已归还, 2:逾期）',
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
CREATE INDEX idx_book_category ON book(category_id);
CREATE INDEX idx_book_status ON book(status);
CREATE INDEX idx_book_code ON book(code);
CREATE INDEX idx_borrow_record_book_id ON borrow_record(book_id);
CREATE INDEX idx_borrow_record_phone_number ON borrow_record(phone_number);
CREATE INDEX idx_borrow_record_status ON borrow_record(status);

-- 插入初始分类数据
INSERT INTO book_category (name, description, sort) VALUES
('文学', '中外文学经典', 1),
('历史', '历史类书籍', 2),
('哲学', '哲学思想类书籍', 3),
('科技', '科学技术类书籍', 4),
('艺术', '艺术鉴赏类书籍', 5),
('教育', '教育理论与方法', 6),
('经济', '经济管理类书籍', 7),
('计算机', '计算机技术类书籍', 8);

-- 插入初始图书数据
INSERT INTO book (code, title, author, category_id, status, description) VALUES
('BK001', '活着', '余华', 1, 0, '讲述了农村人福贵悲惨的人生遭遇'),
('BK002', '百年孤独', '加西亚·马尔克斯', 1, 0, '魔幻现实主义文学代表作'),
('BK003', '人类简史', '尤瓦尔·赫拉利', 2, 0, '从智人到智神的演化史'),
('BK004', '中国通史', '吕思勉', 2, 0, '中国历史的详细介绍'),
('BK005', '理想国', '柏拉图', 3, 0, '古希腊哲学经典著作'),
('BK006', '论语', '孔子', 3, 0, '儒家思想的核心著作'),
('BK007', '时间简史', '史蒂芬·霍金', 4, 0, '关于宇宙起源和发展的科普著作'),
('BK008', '物种起源', '查尔斯·达尔文', 4, 0, '进化论的奠基之作'),
('BK009', '设计心理学', '唐纳德·诺曼', 5, 0, '关于设计与人类心理的研究'),
('BK010', '色彩理论', '约瑟夫·阿尔伯斯', 5, 0, '色彩艺术的理论研究'),
('BK011', '教育学', '夸美纽斯', 6, 0, '教育学经典著作'),
('BK012', '教育心理学', '布鲁纳', 6, 0, '教育心理学理论与实践'),
('BK013', '经济学原理', '曼昆', 7, 0, '经济学入门经典教材'),
('BK014', '国富论', '亚当·斯密', 7, 0, '古典经济学奠基之作'),
('BK015', '深入理解计算机系统', 'Randal E. Bryant', 8, 0, '计算机系统原理经典教材'),
('BK016', '算法导论', 'Thomas H. Cormen', 8, 0, '算法领域的权威著作');

-- 插入初始借阅记录（示例数据）
INSERT INTO borrow_record (book_id, user_name, phone_number, borrow_time, due_time, return_time, status) VALUES
(1, '张三', '13800138001', '2024-01-15 10:30:00', '2024-02-14 10:30:00', NULL, 0),
(2, '李四', '13900139002', '2024-01-10 14:20:00', '2024-02-09 14:20:00', '2024-01-30 16:45:00', 1);