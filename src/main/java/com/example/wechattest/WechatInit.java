package com.example.wechattest;

import com.example.wechattest.DataModel.WechatToken;
import com.example.wechattest.api.service.WechatService;
import com.example.wechattest.config.LogConfig;
import com.example.wechattest.util.MyProps;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class WechatInit {

    private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);
    public WechatInit() {
    }

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MyProps myProps;
    @Autowired
    WechatService wechatService;
    public void init(){
        //初始化获取accesstoken
        List<WechatToken> result = wechatService.getAccessToken();
        String accesstoken = result.get(0).getAccess_token();
        LOG.info(accesstoken);
        //更新公众号菜单
        this.createMenu(accesstoken);
        LOG.info("微信初始化完成");
    }

    //生成菜单
    public void createMenu(String accesstoken){
        String url =myProps.getCreateMenuUrl()+accesstoken;
        JSONObject postData = null;
        try{
            postData = new JSONObject(myProps.getMenu());
        }catch (Exception e){

        }
        ResponseEntity<WechatToken> responseEntity = restTemplate.postForEntity(url,postData ,WechatToken.class);
        LOG.info(responseEntity.getBody().toString());
    }

    //获取菜单
    public JSONObject getMenu(String accesstoken){
        String url =myProps.getGetMenuUrl()+accesstoken;
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url,JSONObject.class);
        LOG.info(responseEntity.getBody().toString());
        return responseEntity.getBody();
    }
}