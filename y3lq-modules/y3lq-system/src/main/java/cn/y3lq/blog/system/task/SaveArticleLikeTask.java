package cn.y3lq.blog.system.task;

import cn.y3lq.blog.system.service.ArticleService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * @author: Y3lq
 * @description: 保存文章点赞的定时任务
 */
public class SaveArticleLikeTask extends QuartzJobBean {

    @Autowired
    private ArticleService articleService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 存储在redis的点赞持久化到mysql
        articleService.saveArticleLikedFromRedis();
    }
}
