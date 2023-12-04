# 数据库初始化

-- 创建库
create database if not exists now_api;

-- 切换库
use now_api;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 用户密钥表
create table if not exists user_key
(
    id         bigint auto_increment comment 'id' primary key,
    userId     bigint                             not null comment '用户id',
    accessKey  varchar(256)                       not null comment 'accessKey',
    secretKey  varchar(256)                       not null comment 'secretKey',
    leftNum    bigint   default 100               not null comment '剩余调用次数',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    unique index idx_userId (userId)
) comment '用户密钥' collate = utf8mb4_unicode_ci;

-- 接口信息
create table if not exists interface_info
(
    id             bigint auto_increment comment 'id' primary key,
    name           varchar(256)                           not null comment '接口名称',
    url            varchar(256)                           not null comment '接口地址',
    userId         bigint                                 null comment '发布人',
    method         varchar(256)                           not null comment '请求方法',
    requestParams  text                                   null comment '接口请求参数',
    responseParams text                                   null comment '接口响应参数',
    reduceScore    bigint       default 0                 null comment '扣除积分数',
    requestExample text                                   null comment '请求示例',
    requestHeader  text                                   null comment '请求头',
    responseHeader text                                   null comment '响应头',
    returnFormat   varchar(512) default 'JSON'            null comment '返回格式(JSON等等)',
    description    varchar(256)                           null comment '描述信息',
    status         tinyint      default 0                 not null comment '接口状态（0- 默认下线 1- 上线）',
    totalInvokes   bigint       default 0                 not null comment '接口总调用次数',
    avatarUrl      varchar(1024)                          null comment '接口头像',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint      default 0                 not null comment '是否删除'
)
    comment '接口信息';

INSERT INTO interface_info (name, url, userId, method, requestParams, responseParams, reduceScore, requestExample,
                            requestHeader, responseHeader, returnFormat, description, status, totalInvokes, avatarUrl,
                            createTime, updateTime, isDelete)
VALUES ('用户登录接口', 'https://example.com/api/login', 123456, 'POST', '{"username": "user", "password": "pass"}',
        '{"token": "abcdefg"}', 0, '请求示例1', 'Content-Type: application/json', 'Content-Type: application/json',
        'JSON', '用户登录接口描述信息', 1, 1000, 'http://example.com/avatar1.jpg', NOW(), NOW(), 0),
       ('商品列表接口', 'https://example.com/api/products', 789012, 'GET', '',
        '[{"id": 1, "name": "商品1", "price": 10.99}, {"id": 2, "name": "商品2", "price": 19.99}]', 0, '请求示例2',
        'Authorization: Bearer token123', 'Content-Type: application/json', 'JSON', '商品列表接口描述信息', 1, 5000,
        'http://example.com/avatar2.jpg', NOW(), NOW(), 0),
       ('订单创建接口', 'https://example.com/api/orders', 345678, 'POST',
        '{"userId": 123456, "items": [{"id": 1, "quantity": 2}]}', '{"orderId": "ord123"}', 5, '请求示例3',
        'Content-Type: application/json', 'Content-Type: application/json', 'JSON', '订单创建接口描述信息', 1, 3000,
        'http://example.com/avatar3.jpg', NOW(), NOW(), 0),
       ('用户信息更新接口', 'https://example.com/api/users/update', 987654, 'PUT',
        '{"name": "张三", "email": "zhangsan@example.com"}', '{"result": "success"}', 0, '请求示例4',
        'Content-Type: application/json', 'Content-Type: application/json', 'JSON', '用户信息更新接口描述信息', 1, 2000,
        'http://example.com/avatar4.jpg', NOW(), NOW(), 0);

INSERT INTO interface_info (name, url, userId, method, requestParams, responseParams, reduceScore, requestExample,
                            requestHeader, responseHeader, returnFormat, description, status, totalInvokes, avatarUrl,
                            createTime, updateTime, isDelete)
VALUES ('商品详情接口', 'https://example.com/api/products/1', 123456, 'GET', '',
        '{"id": 1, "name": "商品1", "price": 10.99}', 0, '请求示例5', 'Content-Type: application/json',
        'Content-Type: application/json', 'JSON', '商品详情接口描述信息', 1, 8000, 'http://example.com/avatar5.jpg',
        NOW(), NOW(), 0),
       ('用户收藏夹接口', 'https://example.com/api/users/favorites', 789012, 'POST', '{"productId": 1}',
        '{"result": "success"}', 5, '请求示例6', 'Content-Type: application/json', 'Content-Type: application/json',
        'JSON', '用户收藏夹接口描述信息', 1, 6000, 'http://example.com/avatar6.jpg', NOW(), NOW(), 0),
       ('订单支付接口', 'https://example.com/api/orders/pay', 345678, 'POST',
        '{"orderId": "ord123", "paymentMethod": "credit_card"}', '{"result": "success"}', 10, '请求示例7',
        'Content-Type: application/json', 'Content-Type: application/json', 'JSON', '订单支付接口描述信息', 1, 4000,
        'http://example.com/avatar7.jpg', NOW(), NOW(), 0),
       ('用户地址管理接口', 'https://example.com/api/users/addresses', 987654, 'GET', '',
        '[{"id": 1, "user_id": 987654, "address": "北京市朝阳区"}]', 0, '请求示例8', 'Content-Type: application/json',
        'Content-Type: application/json', 'JSON', '用户地址管理接口描述信息', 1, 7000, 'http://example.com/avatar8.jpg',
        NOW(), NOW(), 0),
       ('订单取消接口', 'https://example.com/api/orders/cancel', 123456, 'DELETE', '{"orderId": "ord123"}',
        '{"result": "success"}', 5, '请求示例9', 'Content-Type: application/json', 'Content-Type: application/json',
        'JSON', '订单取消接口描述信息', 1, 5000, 'http://example.com/avatar9.jpg', NOW(), NOW(), 0);

-- 用户调用接口关系表
create table if not exists user_interface_invoke
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint                             not null comment '调用人id',
    interfaceId  bigint                             not null comment '接口id',
    totalInvokes bigint   default 0                 not null comment '总调用次数',
    status       tinyint  default 0                 not null comment '调用状态（0- 正常 1- 封号）',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
    comment '用户接口调用表';

-- 商品信息表
create table if not exists product_info
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(256)                       not null comment '产品名称',
    description varchar(256)                       null comment '产品描述',
    userId      bigint                             null comment '创建人',
    price       bigint                             null comment '金额(分)',
    addPoints   bigint   default 0                 not null comment '增加积分个数',
    status      tinyint  default 0                 not null comment '商品状态（0- 默认下线 1- 上线）',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '商品信息';

-- 商品订单
create table if not exists product_order
(
    id             bigint auto_increment comment '订单号' primary key,
    userId         bigint                                 not null comment '创建人',
    productId      bigint                                 not null comment '商品id',
    orderName      varchar(256)                           not null comment '商品名称',
    total          bigint                                 not null comment '金额(分)',
    codeUrl        varchar(256)                           null comment '二维码地址',
    status         varchar(256) default 'NOTPAY'          not null comment '交易状态(SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付 CLOSED：已关闭 REVOKED：已撤销（仅付款码支付会返回）
                                                                              USERPAYING：用户支付中（仅付款码支付会返回）PAYERROR：支付失败（仅付款码支付会返回）)',
    formData       text                                   null comment '支付宝formData',
    expirationTime datetime                               null comment '过期时间',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '商品订单';

#付款信息
create table if not exists payment_info
(
    id          bigint auto_increment comment 'id' primary key,
    orderNo     varchar(256)                       null comment '商户订单号',
    tradeState  varchar(256)                       null comment '交易状态(SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付 CLOSED：已关闭 REVOKED：已撤销（仅付款码支付会返回）
                                                                              USERPAYING：用户支付中（仅付款码支付会返回）PAYERROR：支付失败（仅付款码支付会返回）)',
    successTime varchar(256)                       null comment '支付完成时间',
    userId      varchar(256)                       null comment '用户标识',
    payerTotal  bigint                             null comment '用户支付金额',
    content     text                               null comment '接口返回内容',
    total       bigint                             null comment '总金额(分)',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '付款信息';

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    cover      varchar(1024)                      null comment '封面图',
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';

INSERT INTO post (title, content, tags, thumbNum, favourNum, userId, isDelete)
VALUES ('如何学习摄影技巧', '学习摄影的基本知识和技巧分享', '["摄影", "技巧", "学习"]', 25, 10, 1, 0),
       ('美食推荐：披萨', '披萨的制作方法和美味食谱', '["美食", "披萨", "烹饪"]', 95, 65, 2, 0),
       ('健身计划：HIIT训练', 'HIIT训练的方法和效果介绍', '["健身", "HIIT", "运动"]', 130, 80, 3, 0),
       ('旅游攻略：巴厘岛之旅', '巴厘岛旅游景点、美食和文化介绍', '["旅行", "巴厘岛", "印度尼西亚"]', 140, 90, 1, 0),
       ('健康生活：瑜伽呼吸法', '瑜伽呼吸法的基本动作和好处介绍', '["健康", "瑜伽", "放松"]', 75, 50, 2, 0),
       ('摄影技巧：夜景摄影', '夜景摄影的构图和光线处理方法', '["摄影", "夜景摄影", "艺术"]', 60, 30, 3, 0),
       ('电影推荐：《星球大战》系列', '《星球大战》系列的剧情简介和评价', '["电影", "星球大战", "科幻"]', 150, 100, 1, 0),
       ('旅游攻略：澳大利亚之旅', '澳大利亚旅游景点、美食和文化介绍', '["旅行", "澳大利亚", "澳大利亚文化"]', 160, 110, 2,
        0),
       ('健身计划：核心训练', '核心训练的方法和效果介绍', '["健身", "核心训练", "运动"]', 170, 120, 3, 0),
       ('摄影技巧：人像拍摄技巧', '人像拍摄的技巧和注意事项', '["摄影", "人像拍摄", "艺术"]', 105, 65, 1, 0),
       ('美食推荐：烤鸡腿', '烤鸡腿的制作方法和美味食谱', '["美食", "烤鸡腿", "烹饪"]', 125, 85, 2, 0),
       ('健身计划：有氧运动+力量训练', '有氧运动和力量训练结合的训练方法介绍', '["健身", "有氧运动", "力量训练"]', 180,
        130, 3, 0),
       ('旅游攻略：新西兰之旅', '新西兰旅游景点、美食和文化介绍', '["旅行", "新西兰", "新西兰文化"]', 190, 140, 1, 0),
       ('健康生活：冥想与放松音乐', '冥想和放松音乐结合的方法和效果介绍', '["健康", "冥想", "音乐"]', 115, 75, 2, 0),
       ('摄影技巧：微距摄影', '微距摄影的构图和光线处理方法', '["摄影", "微距摄影", "艺术"]', 90, 55, 3, 0),
       ('电影推荐：《阿凡达》', '《阿凡达》的剧情简介和评价', '["电影", "阿凡达", "科幻"]', 200, 150, 1, 0),
       ('旅游攻略：意大利之旅', '意大利旅游景点、美食和文化介绍', '["旅行", "意大利", "意大利文化"]', 210, 160, 2, 0),
       ('健身计划：全身训练', '全身训练的方法和效果介绍', '["健身", "全身训练", "运动"]', 220, 170, 3, 0),
       ('摄影技巧：黑白摄影', '黑白摄影的构图和后期处理方法', '["摄影", "黑白摄影", "艺术"]', 135, 95, 1, 0),
       ('美食推荐：烤鸡翅', '烤鸡翅的制作方法和美味食谱', '["美食", "烤鸡翅", "烹饪"]', 145, 105, 2, 0),
       ('健身计划：拉伸训练', '拉伸训练的方法和效果介绍', '["健身", "拉伸训练", "运动"]', 230, 180, 3, 0),
       ('旅游攻略：日本之旅', '日本旅游景点、美食和文化介绍', '["旅行", "日本", "日本文化"]', 240, 190, 1, 0),
       ('健康生活：冥想与放松音乐', '冥想和放松音乐结合的方法和效果介绍', '["健康", "冥想", "音乐"]', 155, 115, 2, 0),
       ('摄影技巧：风光摄影', '风光摄影的构图和光线处理方法', '["摄影", "风光摄影", "艺术"]', 120, 80, 3, 0),
       ('电影推荐：《星际穿越》', '《星际穿越》的剧情简介和评价', '["电影", "星际穿越", "科幻"]', 250, 200, 1, 0),
       ('旅游攻略：法国之旅', '法国旅游景点、美食和文化介绍', '["旅行", "法国", "法国文化"]', 260, 210, 2, 0);
