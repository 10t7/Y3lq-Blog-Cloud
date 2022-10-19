package cn.y3lq.blog.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 路由
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 当路由下面children 的路由大于1 自动变为嵌套模式
     */
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    private MetaVO meta;

    /**
     * 子路由
     */
    private List<RouterVO> children;
}
