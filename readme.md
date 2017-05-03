# 重要更新

## 2017-5-01

- 为项目添加了ehcache作为hibernate的L2缓存，优化了查询。

## 2017-4-30

- 使用Maven重构了项目，目前该项目为纯Maven项目，对项目的迁移和扩展更加友好。
- 优化了数据库和查找方式，加速了某几个接口的查找速度。

## 2017-4-27

- 对API风格进行了大的调整，改为RESTful风格。旧的代码和旧的文档会置于old分支。
- 使用了redis作为缓存，替代了session机制，配合RESTful-api，实现了同时和WEB前端+Android手机端进行交互。

## 2017-4-23

- 为了后续对Android等可能不支持session的前端的开发支持，现实现了纯RESTful设计模式，原web api不变，均可继续使用。文档中areq为更新后，不能使用session的设备需要添加的请求。

# 公共说明

- 测试端口的基地址为http://ecs.jimstar.top:8080/Wear_war/。
- 本文档中地址均为该基地址的相对地址，如，注册API为 http://ecs.jimstar.top:8080/Wear_war/api/user。
- 所有API均使用HTTP状态码进行响应，若功能正常，则状态码为200，无其他提示；若发生异常，则返回对应HTTP状态码和异常原因。
- 对于请求中有Body的API，这些内容会在响应中被返回，便于识别和提取。
- 所有API均遵守RESTful风格，使用了GET,POST,PUT,DELETE四种HTTP方法。
- 每个token在缓存中的生命周期为一小时，超时后，需要重新申请token。此后为适应移动端需求，可能会对生命周期有所调整。
- 对于目前的阶段性版本，开发和本地测试已经完成。

# 文档说明

- 对服务器API文档有任何疑问，请联系服务器代码作者孙博宇 邮箱：cielosun@outlook.com qq:632898354。
- 本文档中，req为必须请求参数，creq为可选请求参数，res为返回参数。
- 本文档中{token}这种格式表示从服务器获取的某个变量。例如：http://ecs.jimstar.top:8080/Wear_war/api/64d2915eb0a54e2b93bcdef71b812961/data，其中64d2915eb0a54e2b93bcdef71b812961对应{token}，该字符串在登录/注册操作时可以从响应中获取。

请求样例：
对于多个属性：
```JSON
{
	"accountNumber":"002",
	"password":"hahaschool",
	"userName":"CieloSun",
	"phone":"132xxxxxxxx",
	"relativeName":"Wendy",
	"relativePhone":"188xxxxxxxx",
	"email":"cielosun@xxxxxx.com"
}
```
对于单个属性：
```JSON
{
	"accountNumber":"002"
}
```

# 用户管理部分

## /api/token

method:POST

通过用户名和密码进行登录。

req:
- String accountNumber
- String password

res:
- String token

## /api/user

method:POST

向数据库添加用户。

req:
- String accountNumber
- String password

creq:
- String userName
- String phone
- String relativeName
- String relativePhone
- String email

res:
- String token

## /api/{token}

method:PUT

更改当前用户数据。

creq:
- String newPassword
- String userName
- String phone
- String relativeName
- String relativePhone
- String email

## /api/{token}

method:GET

显示当前用户的用户数据。

res:
- 一个名为user的对象，其中属性为:
1. String accountNumber 用户账号
2. String userName 用户名
3. String phone 用户电话
4. String relativeName 用户亲属名
5. String relativePhone 用户亲属电话
6. String email 用户email

示例：
```JSON
{
    "user":{"accountNumber":"002",
            "userName":"Boris",
            "phone":"132xxxxxxxx",
            "relativeName":"Wendy",
            "relativePhone":"188xxxxxxxx",
            "email":"cielosun@xxxxxx.com",
            "password":null},
}
```

## /api/{token}

method:DELETE

登出。

## /api/{token}/user

mehod:DELETE

删除当前用户。

# 数据测量部分

以下若说明为measure对象，则其结构为：
```JSON
{
    "accountNumber":"002",
    "commitTime":1492659233000,
    "device":"BE-83-85-13-EB-1D",
    "step":386,
    "distance":10,
    "heart":70,
    "id":1
}
 ```
各属性的类型为：
 - String accountNumber 用户账号
 - Long commitTime 由时间戳转换而成的UNIX毫秒数
 - String device 设备MAC
 - Integer step 步数
 - Integer distance 距离（单位：米）
 - Integer heart 心率
 - int id 自动生成，不需要调用
 
以下若说明为measures列表，则其结构为：
```JSON
{
    "measures":[
                    {
                        "accountNumber":"002",
                        "commitTime":1492659233000,
                        "device":"BE-83-85-13-EB-1D",
                        "step":386,
                        "distance":10,
                        "heart":70,
                        "id":1
                    },
                    {
                        "accountNumber":"002",
                        "commitTime":1492661406000,
                        "device":"BC-83-85-13-EA-1C",
                        "step":429,
                        "distance":11,
                        "heart":65,
                        "id":2
                    }
                ],
}
```

对于所有时间请求，请以UNIX毫秒数（例如：1492661406000）的形式发送。

## /api/{token}/data

method:GET

获取当前用户某段时间的所有数据，按commitTime正序排序。

1. 若无fromTimeMills属性和toTimeMills属性，则直接返回全部数据。
2. 若无fromTimeMills属性，则返回截至toTimeMills的全部数据。
3. 若无toTimeMills属性，则从fromTimeMills到当前时间的所有数据。
4. 若有fromTimeMills属性和toTimeMills属性，则返回从fromTimeMills到toTimeMills之间的所有数据。

creq:
- Long fromTimeMills
- Long toTimeMills

res:
- 名为measures的measure对象列表。

## /api/{token}/date

method:GET

获取当前用户某日的所有数据，按commitTime正序排序。若无timeMills，则返回今日的所有数据。

creq:
- Long timeMills

res:
- 名为measures的measure对象列表。

## /api/{token}/latest

method:GET

获取当前用户某段日期中的每天的最新的数据，按commitTime正序排序。
1. 若无fromTimeMills属性和toTimeMills属性，则直接返回最新的一条数据。
2. 若无fromTimeMills属性，则返回从toTimeMills日期一年前截至toTimeMills日期的每天的最新一条数据。
3. 若无toTimeMills属性，则从fromTimeMills日期到今日的最新一条数据。
4. 若有fromTimeMills属性和toTimeMills属性，则返回从fromTimeMills日期到toTimeMills日期之间的最新一条数据。

creq:
- Long fromTimeMills
- Long toTimeMills

res:
- 名为measures的measure对象列表。

## /api/{token}

method:POST

以当前token身份向服务器存储一组数据。

req:
 - Long commitTime
 - String device
 
creq:
 - Integer step
 - Integer distance
 - Integer heart
