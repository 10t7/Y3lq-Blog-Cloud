package cn.y3lq.blog.system;

import cn.y3lq.blog.common.core.constant.CacheConstants;
import cn.y3lq.blog.common.core.domain.User;
import cn.y3lq.blog.system.entity.ArticleEntity;
import cn.y3lq.blog.system.entity.ArticleLikeEntity;
import cn.y3lq.blog.system.entity.UserEntity;
import cn.y3lq.blog.system.mapper.ArticleMapper;
import cn.y3lq.blog.system.mapper.RoleMapper;
import cn.y3lq.blog.system.mapper.UserMapper;
import cn.y3lq.blog.system.mapper.UserRoleMapper;
import cn.y3lq.blog.system.service.MenuService;
import cn.y3lq.blog.system.service.RoleService;
import cn.y3lq.blog.system.service.UserService;
import cn.y3lq.blog.system.vo.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.*;

/**
 * @author: Y3lq
 * @description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test1 {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private MenuService menuService;
//
//
//
//
//    @Test
//    public void test2() {
//        Set<String> roleKeys = roleService.getRoleKeys(1l);
//        System.out.println(roleKeys);
//    }
//
//
//
//    @Test
//    public void test3() {
//        Set<String> menuPermissions = menuService.getMenuPermissions(2L);
//        System.out.println(menuPermissions);
//    }
//
//    @Test
//    public void test4() {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setNickname("lq");
//        List<UserListVO> userEntities = userService.selectUserList(userEntity);
//        System.out.println(userEntities);
//    }
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Test
//    public void test5() {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setPassword("122223");
//        userEntity.setUsername("12223");
//        userEntity.setAvatar("122223");
//        userEntity.setNickname("312321");
//        userEntity.setDelFlag("0");
//        userEntity.setCreateTime(new Date());
//        userEntity.setCreateBy("1");
//        userEntity.setStatus("0");
//        userEntity.setRemark("3123");
//        userMapper.insertUser(userEntity);
//    }
//
//    @Autowired
//    private UserRoleMapper userRoleMapper;
//
//    @Test
//    public void test6() {
//        List<Long> roleIds = userRoleMapper.getRoleIds(1L);
//        System.out.println(roleIds);
//    }
//
//    @Test
//    public void test7() {
//        UserInfoVO userByUserId = userMapper.getUserByUserId(1L);
//        List<Long> roleIds = userRoleMapper.getRoleIds(1L);
//        Long[] longs = roleIds.toArray(new Long[roleIds.size()]);
//        userByUserId.setRoleIds(longs);
//        System.out.println(userByUserId);
//    }
//
//    @Autowired
//    private RoleMapper roleMapper;
//
//    @Test
//    public void test8() {
//        UserVO userVO = new UserVO();
//        Long[] roleIds = userVO.getRoleIds();
//        System.out.println(roleIds.length);
//    }
//
//    @Autowired
//    private ArticleMapper articleMapper;
//
//    @Test
//    public void test9() {
//        ArticleVO articleVO = new ArticleVO();
//        List<ArticleVO> articleVOS = articleMapper.selectArticleList(articleVO);
//        System.out.println(articleVOS);
//    }
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//
//    @Test
//    public void test10() {
//        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_LIKE,1000+"::"+1,"1");
//        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_LIKE,1000+"::"+2,"1");
//        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_LIKE,1000+"::"+3,"0");
//        redisTemplate.opsForHash().put(CacheConstants.ARTICLE_LIKE,1000+"::"+4,"1");
//        Map articleLikeMap = redisTemplate.opsForHash().entries(CacheConstants.ARTICLE_LIKE);
//        List<ArticleLikeEntity> addArticleLikeEntities = new ArrayList<>();
//        List<ArticleLikeEntity> removeArticleLikeEntities = new ArrayList<>();
//        Iterator iterator = articleLikeMap.keySet().iterator();
//        while (iterator.hasNext()) {
//            String key = (String) iterator.next();
//            String[] split = key.split("::");
//            if (articleLikeMap.get(key).equals("0")) {
//                // 为 0 则删除点赞
//                ArticleLikeEntity articleLikeEntity = new ArticleLikeEntity();
//                articleLikeEntity.setArticleId(Long.parseLong(split[0]));
//                articleLikeEntity.setUserId(Long.parseLong(split[1]));
//                removeArticleLikeEntities.add(articleLikeEntity);
//            }else{
//                // 为点赞
//                ArticleLikeEntity articleLikeEntity = new ArticleLikeEntity();
//                articleLikeEntity.setArticleId(Long.parseLong(split[0]));
//                articleLikeEntity.setUserId(Long.parseLong(split[1]));
//                addArticleLikeEntities.add(articleLikeEntity);
//            }
//
//        }
//        System.out.println(addArticleLikeEntities);
//        System.out.println(removeArticleLikeEntities);
//    }
//
//
//    @Test
//    public void test11() {
//        HashMap<String, ArrayList<String>> stringArrayListHashMap = new HashMap<>();
//        stringArrayListHashMap.put("1",new ArrayList<String>());
//        ArrayList<String> strings = stringArrayListHashMap.get("1");
//        strings.add("3123123121");
//        System.out.println(stringArrayListHashMap);
//    }
//
//    @Autowired
//    private MessageService messageService;
//
//    @Test
//    public void test12() {
//        List<MessageVO> messageVOList = messageService.selectBlogList(1L);
//        System.out.println(messageVOList);
//    }
//
//    @Test
//    public void test13() {
//        UserEntity userEntity = new UserEntity();
//        Long userId = userEntity.getUserId();
//    }

}
