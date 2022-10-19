package cn.y3lq.blog.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Y3lq
 * @description: 用户跟角色关联实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;
}
