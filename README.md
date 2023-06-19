# sHiTop - 山海Top - 项目合集

所有生产环境包后续都从这里统一出。

------

## 项目列表

| **项目代码** | **项目名**  | **生产地址** | **预发布地址** |
|----------|----------|---|---|
| B1       | betfiery |||
| B2       | aajogo   |||
| B3       | Rico100  |||
| M1       | mexlucky |||
| M2       | mexswin  |||
| V1       | vang     |||
| P1       | phlwin   |||
| P2       | philucky |||
| P3       | topphl   |||
| P4       | xtaya    |||

------
## 新建项目流程

1. 创建apanalo项目副本
2. 修改工程名、包名等
3. 修改配置文件:./xxxapp/settings.gradle
     ```groovy
    ext.base = [
            appId  : "应用包名",
            appName: "应用名",
            apkName: "应用apk名",
    settings:'',
        develop: [ // 开发环境配置
                    verCode : 1,
                    verName : "0.0.1",
                    localConfig: "基础配置"
            ],
            preview: [
                    verCode : 1,
                    verName : "0.0.1",
                    localConfig: "基础配置"
            ],
            online : [
                    verCode : 1,
                    verName : "0.0.1",
                    localConfig: "eyJjdXJyZW5jeSI6IlBIUCIsImV2ZW50VGtuTWFwIjp7IkZCX0xPR0lOIjoianMxcGZoIiwiRkJfUkVHSVNURVIiOiJqaG90ZmEiLCJGSVJTVF9SRUNIQVJHRSI6InI1OWl3dyIsIkdPT0dMRV9MT0dJTiI6ImtjZ3oxOCIsIkdPT0dMRV9SRUdJU1RFUiI6InlpOWYybiIsIkxPR0lOIjoia2VxOWdqIiwiUEFHRV9WSUVXIjoiNHlmZWx5IiwiUEFZX1JFQ0hBUkdFIjoia2F2azExIiwiUkVHSVNURVIiOiJnc3ZxcDkiLCJTRUNPTkRfUkVDSEFSR0UiOiJnbWgyaTAifSwiZmlyZWJhc2VUb3BpYyI6ImFjdGl2aXR5QXAiLCJsaW5rIjoiaHR0cHM6Ly9hcGFuYWxvLmNvbT9yZWZlcnJhbGNvZGU9NjQzMTg0MDhlODkzY2Y2OTJjMTUxOTQ2JmZyb21BcGs9ZnJvbUFwayIsInZlcnNpb25VcmwiOiJodHRwczovL2Fzc2V0cy5hcGFuYWxvLmNvbS9hcGsvdmVyc2lvbi5qc29uIn0"
            ]
    
    ]
    ext.jks = [
            dir  : "签名文件路径",
            pwd  : "签名文件密码",
            alias: "签名文件别名",
    ]
    ext.facebook = [
            appId      : "脸书应用id",
            clientToken: "脸书clientToken",
    ]
    ext.onesignal = [
            appId: ""
    ]
    ext.adjust = [
            appToken: ""
    ]
    ```
   
4. 基础配置生成步骤
    1. 修改json配置
   
```json
  {
    "link": "主页地址",
    "currency": "货币代码",
    "eventTknMap": { // 事件配置
        "FB_LOGIN": "js1pfh",
        "FB_REGISTER": "jhotfa",
        "FIRST_RECHARGE": "r59iww",
        "GOOGLE_LOGIN": "kcgz18",
        "GOOGLE_REGISTER": "yi9f2n",
        "LOGIN": "keq9gj",
        "PAGE_VIEW": "4yfely",
        "PAY_RECHARGE": "kavk11",
        "REGISTER": "gsvqp9",
        "SECOND_RECHARGE": "gmh2i0"
    }
  }
```
 2. 加密串通过shiTop工程的测试代码ExampleUnitTest生成

14:54:55.159 Making request to url : https://app.adjust.com/sdk_click
14:54:55.877 Response string: {"app_token":"bn2v0eblh3wg","adid":"9249dc9782eef0d25c4472c8528b470d","timestamp":"2023-06-16T06:54:52.789Z+0000","message":"Install tracked","ask_in":2000}
14:54:55.880 Response message: Install tracked

14:54:55.898 Session Success msg:Install tracked time:2023-06-16T06:54:52.789Z+0000 adid:9249dc9782eef0d25c4472c8528b470d 
                         json:{"app_token":"bn2v0eblh3wg","adid":"9249dc9782eef0d25c4472c8528b470d","timestamp":"2023-06-16T06:54:52.789Z+0000","message":"Install tracked","ask_in":2000}
14:54:55.921 Response string: {"app_token":"bn2v0eblh3wg","adid":"9249dc9782eef0d25c4472c8528b470d","timestamp":"2023-06-16T06:54:52.817Z+0000","message":"Click tracked"}
14:54:55.921 Response message: Click tracked

14:54:57.954 Making request to url: https://app.adjust.com/attribution
14:54:58.150 Response string: {"app_token":"bn2v0eblh3wg","adid":"9249dc9782eef0d25c4472c8528b470d","timestamp":"2023-06-16T06:54:55.063Z+0000","message":"Attribution found",
                              "attribution":{"tracker_token":"11vhf9j5","tracker_name":"installer","network":"installer"}}
14:54:58.152 Response message: Attribution found