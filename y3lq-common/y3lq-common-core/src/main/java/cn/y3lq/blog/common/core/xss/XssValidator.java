package cn.y3lq.blog.common.core.xss;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Y3lq
 * @description: 自定义xss校验注解实现
 */
public class XssValidator implements ConstraintValidator<Xss, String> {

    private static final String HTML_PATTERN = "<(\\S*?)[^>]*>.*?|<.*? />";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        return !containHtml(s);
    }

    public static boolean containHtml(String s) {
        Pattern compile = Pattern.compile(HTML_PATTERN);
        Matcher matcher = compile.matcher(s);
        return matcher.matches();
    }
}
