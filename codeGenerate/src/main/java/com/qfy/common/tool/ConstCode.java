package com.qfy.common.tool;

/**
 * 错误代码
 */
public class ConstCode {
        /**
         * 成功
         */
        public static final int OPERAT_SUCCESS = 0;

        /**
         * 失败
         */
        public static final int OPERAT_FAILURE = 1;
        /**
         * 授权失败
         */
        public static final int AUTHORIZATION_FAILURES = 2001;

        /**
         * 参数错误
         */
        public static final int OPERAT_PARAM_ERROR = 2;

        /**
         * 用户状态，已注册，未完善信息
         */
        public static final int USER_STATE_REGISTER = 0;

        /**
         * 用户状态，已注册，完善未审核
         */
        public static final int USER_STATE_NO_CHECK = 1;

        /**
         * 用户状态，已注册，审核通过
         */
        public static final int USER_STATE_CHECKED = 2;

        /**
         * 用户状态，已注册，禁用
         */
        public static final int USER_STATE_DISABLED = 3;

        /**
         * 用户类型，买家
         */
        public static final int USER_TYPE_PRIMARY_BUYER = 1;

        /**
         * 用户状态，卖家
         */
        public static final int USER_TYPE_PRIMARY_SELLER = 2;

        /**
         * 用户副账户状态，主账户操作  已开启
         */
        public static final int SUBUSER_STATE_USEROPERAT_OPEN = 1;

        /**
         * 用户副账户状态，主账户操作 已关闭
         */
        public static final int SUBUSER_STATE_USEROPERAT_CLOSE = 2;

        /**
         * 用户副账户状态，运营管理操作  已开启
         */
        public static final int SUBUSER_STATE_SYSMANAGEOPERAT_OPEN = 3;

        /**
         * 用户副账户状态，运营管理操作 已关闭
         */
        public static final int SUBUSER_STATE_SYSMANAGEOPERAT_CLOSE = 4;

        /**
         * 用户身份标记，主账户
         */
        public static final int USER_TYPE_PRIMARY = 1;

        /**
         * 用户身份标记，副账户
         */
        public static final int USER_TYPE_SUB = 2;

        public static final String[] CAR_TYPE_NAME = new String[] { "", "原厂件", "同质件", "品牌件", "其他" };

        /**
         * 异常邮箱地址的key
         */
        public static final String EXCEPTION_MAILL_ADDRESS_KEY = "email.receiver";
        
        /**
         * token 为空的错误代码
         */
        public static final int TOKEN_NULL_EXCEPTION = 1001;
        /**
         * token 过期的错误代码
         */
        public static final int TOKEN_EXPIRE_EXCEPTION = 1002;
        
        /**
         * shiro 未登录
         */
        public static final int UNAUTHENTICATED = 1003;
        /**
         * shiro 没权限
         */
        public static final int UNAUTHORIZED = 1004;
        
        /**
         * 账户维度：客户
         */
        public static final int DIMENSION_CUSTOMER=1;
        
        /**
         * 账户维度：品牌维度
         */
        public static final int  DIMENSION_BRAND=2;
        
        /**
         * 账户维度：地区
         */
        public static final int DIMENSION_REGION=3;
        
        /**
         * 账户维度：没有任何维度
         */
        public static final int DIMENSION_NO=4;
        
        /**
         * pc登陆
         */
        public static final int LOGIN_PC = 1 ;
        /**
         * 安卓登陆
         */
        public static final int LOGIN_ANDROID = 2 ;
        /**
         * ios登陆
         */
        public static final int LOGIN_IOS = 3 ;
        
        /**
         * 当前登陆用户
         */
        public static final String CURRENT_USER = "user";
        /**
         * 角色：买家主账户
         */
        public static final int USER_ROLE_BUYER = 10000001;
        /**
         * 角色：卖家主账户
         */
        public static final int USER_ROLE_SELLER = 10000002;
        /**
         * 角色：买家子账户
         */
        public static final int USER_ROLE_BUYERCHILD = 10000003;
        /**
         * 角色：卖家子账户
         */
        public static final int USER_ROLE_SELLERCHILD = 10000004;
        /**
         * 角色：总店
         */
        public static final int USER_ROLE_HEADQUARTERS = 10000005;
        /**
         * 角色：分店
         */
        public static final int USER_ROLE_BRANCH = 10000006;
        /**
         * 角色：保险公司
         */
        public static final int USER_ROLE_INSURANCECOMPANY = 10000007;
}
