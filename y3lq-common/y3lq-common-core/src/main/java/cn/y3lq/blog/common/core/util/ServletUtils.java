package cn.y3lq.blog.common.core.util;

import cn.hutool.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Y3lq
 * @description: 客户端工具类
 */
public class ServletUtils {

    /**
     * 将字符串渲染到客户端
     *
     * @param response
     * @param string
     */
    public static void renderString(HttpServletResponse response, String string) {
        try{
            response.setStatus(HttpStatus.HTTP_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
