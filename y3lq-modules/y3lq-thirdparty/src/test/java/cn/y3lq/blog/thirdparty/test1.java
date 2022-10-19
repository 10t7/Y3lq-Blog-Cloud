package cn.y3lq.blog.thirdparty;

import com.aliyun.oss.*;
import com.aliyun.oss.model.Bucket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author: Y3lq
 * @description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class test1 {

    @Test
    public void testAliyunOss(){
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";
        String accessKeyId = "xx";
        String accessKeySecret = "xx";
        String bucketName = "y3lq-blog";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


        try {
            // 创建存储空间。
            Bucket bucket = ossClient.createBucket(bucketName);
            ossClient.putObject(bucketName,"123",new ByteArrayInputStream("hi".getBytes()));

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    @Resource
    private OSS ossClient;

    @Test
    public void test2() throws FileNotFoundException {
        InputStream fileInputStream = new FileInputStream("/Users/ccstart/IdeaProjects/qingchengcode/pom.xml");

        ossClient.putObject("y3lq-blog","123",fileInputStream);
        ossClient.shutdown();
    }
}
