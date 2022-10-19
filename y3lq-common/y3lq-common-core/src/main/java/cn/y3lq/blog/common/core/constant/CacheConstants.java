package cn.y3lq.blog.common.core.constant;

/**
 * @author: Y3lq
 * @description: 缓存在 redis 的 key 相关常量
 */
public class CacheConstants {
    /**
     * 用户信息缓存有效期 30分钟
     */
    public final static long EXPIRATION = 30 * 1000 * 60;

    /**
     * 用户信息缓存刷新时间 15分钟
     */
    public final static long REFRESH_TIME = 15 * 1000 * 60;

    /**
     * 用户信息 key 前缀
     */
    public static final String LOGIN_TOKEN_KEY = "login_token:";

    public static final String LOGIN_USER_IDS = "login_user_ids";

    public static final String MENU_LIST = "menu_list";

    /**
     * 验证码的 key 前缀
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_code:";

    /**
     * 验证码有效期
     */
    public static final Long CAPTCHA_EXPIRATION_TIME = 3L;

    /**
     * 文章点赞前缀
     */
    public static final String ARTICLE_LIKE = "article_like";

    /**
     * 日记点赞前缀
     */
    public static final String DIARY_LIKE = "diary_like";

    /**
     * 文章评论点赞前缀
     */
    public static final String ARTICLE_COMMENT_LIKE = "article_comment_like";



    /**
     * 文章点赞
     */
    public static final String ARTICLE_LIKE_FLAG = "1";

    /**
     * 文章取消点赞
     */
    public static final String ARTICLE_UNLIKE_FLAG = "0";

    /**
     * 文章评论点赞
     */
    public static final String ARTICLE_COMMENT_LIKE_FLAG = "1";

    /**
     * 文章评论取消点赞
     */
    public static final String ARTICLE_COMMENT_UNLIKE_FLAG = "0";

    /**
     * 点赞标识
     */
    public static final String LIKE_FLAG = "1";

    /**
     * 取消点赞标识
     */
    public static final String UNLIKE_FLAG = "0";







}
