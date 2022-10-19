package cn.y3lq.blog.system.config;

import cn.y3lq.blog.system.task.SaveArticleCommentLikeTask;
import cn.y3lq.blog.system.task.SaveArticleLikeTask;
import cn.y3lq.blog.system.task.SaveDiaryLikeTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Y3lq
 * @description: 定时任务调度配置
 */
@Configuration
public class QuartzConfig {

    private static final String ARTICLE_LIKE_TASK_IDENTITY = "SaveArticleLikeTaskQuartz";

    private static final String ARTICLE_COMMENT_LIKE_TASK_IDENTITY = "SaveArticleCommentLikeTaskQuartz";

    private static final String DIARY_LIKE_TASK_IDENTITY = "SaveDiaryLikeTaskQuartz";





    @Bean
    public JobDetail quartzDetail1() {
        return JobBuilder.newJob(SaveArticleLikeTask.class).withIdentity(ARTICLE_LIKE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public JobDetail quartzDetail2() {
        return JobBuilder.newJob(SaveArticleCommentLikeTask.class).withIdentity(ARTICLE_COMMENT_LIKE_TASK_IDENTITY).storeDurably().build();
    }
    @Bean
    public JobDetail quartzDetail3() {
        return JobBuilder.newJob(SaveDiaryLikeTask.class).withIdentity(DIARY_LIKE_TASK_IDENTITY).storeDurably().build();
    }



    @Bean
    public Trigger quartzTrigger1() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(60)  //设置时间周期单位秒
//                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail1())
                .withIdentity(ARTICLE_LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
    @Bean
    public Trigger quartzTrigger2() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(60)  //设置时间周期单位秒
//                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail2())
                .withIdentity(ARTICLE_COMMENT_LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public Trigger quartzTrigger3() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(60)  //设置时间周期单位秒
//                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail3())
                .withIdentity(DIARY_LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }


}
