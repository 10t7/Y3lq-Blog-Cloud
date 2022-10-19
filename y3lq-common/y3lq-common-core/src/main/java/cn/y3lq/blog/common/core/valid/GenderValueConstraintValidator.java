package cn.y3lq.blog.common.core.valid;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Y3lq
 * @description: 校验性别的校验器
 */
public class GenderValueConstraintValidator implements ConstraintValidator<GenderValue,String> {

    private Set<String> set = new HashSet<>();

    @Override
    public void initialize(GenderValue constraintAnnotation) {
        String[] value = constraintAnnotation.value();
        for (String s : value) {
            if(!StringUtils.isBlank(s)){
                set.add(s);
            }
        }
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isEmpty(s)){
            // 为空则不校验
            return true;
        }
        return set.contains(s);
    }
}
