package cn.y3lq.blog.system.mapper;

import cn.y3lq.blog.system.entity.UserSettingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Y3lq
 * @description: 用户个人设置数据层
 */
@Mapper
public interface UserSettingMapper {
    /**
     * 获取指定用户的个人设置
     */
    UserSettingEntity getUserSettingByUserId(@Param("userId") Long userId);

    /**
     * 新增用户设置
     */
    void insertUserSetting(UserSettingEntity userSetting);

    int isHasInit(@Param("userId") Long userId);

    /**
     * 更新用户设置
     */
    void updateUserSetting(UserSettingEntity userSettingEntity);

}
