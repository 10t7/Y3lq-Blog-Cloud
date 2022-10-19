package cn.y3lq.blog.system.vo;


import java.io.Serializable;

/**
 * @author: Y3lq
 * @description: 路由显示信息
 */

public class MetaVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 路由标题
     */
    private String title;

    /**
     * 路由图标
     */
    private String icon;

    /**
     * 是否缓存
     */
    private Boolean noCache;

    /**
     * 内链地址
     */
    private String link;

    public MetaVO(String title, String icon, String link) {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaVO(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public MetaVO() {
    }

    public MetaVO(String title, String icon, Boolean noCache, String link) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.link = link;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getNoCache() {
        return noCache;
    }

    public void setNoCache(Boolean noCache) {
        this.noCache = noCache;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
