package cn.y3lq.blog.system.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: Y3lq
 * @description: 修改密码封装类
 */
@Data
public class ResetPwdVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "需要修改的密码不能为空")
    @Length(min = 2, max = 18, message = "用户密码长度应在[2, 18]")
    private String password;
}
