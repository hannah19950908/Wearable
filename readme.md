# 公共说明

- 所有API均会返回status属性，值为0表示成功，通常值为1表示失败（若有多种失败情况会特殊说明）。以下文档中不再说明。
- 所有API的request内容都会在response中。
- 所有请求参数和可选请求参数都必须是JSON格式，即使仅仅只有一条内容，不接受但个属性的POST或GET方法，只接受JSON。
- 除不需要参数的方法（可为POST或GET）外，所有方法均为POST方法。
- req为必须请求参数，creq为可选请求参数，res为返回参数。
- 若请求中没有accountNumber参数，即说明需要session中存储了accountNumber参数，请成功注册/成功登录后调用。
- 请求时，允许JSON结构中有冗余信息，服务器会直接无视，但返回的内容中也会有该冗余信息。
- 对服务器API文档有任何疑问，请联系服务器代码作者孙博宇 邮箱：cielosun@outlook.com qq:632898354。
- 目前User部分的开发和测试已经完成，Measure部分开发完成，还在测试中。

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

## /User/login

通过用户名和密码进行登录。登录成功为0。

req:
- String accountNumber
- String password

res:

## /User/check

通过用户名检查数据库中是否有该用户，没有该用户为0。

req:
- String accountNumber

res:

## /User/join

向数据库添加用户，添加成功为0,用户已存在为1，其他原因添加失败为2。

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

## /User/edit

更改当前用户数据，更新成功为0。

req:
- String oldPassword

creq:
- String newPassword
- String userName
- String phone
- String relativeName
- String relativePhone
- String email

res:

## /User/display

显示当前用户的用户数据。成功为0。

req:

res:
- 一个名为user的对象，其中属性为:
String accountNumber,
String userName,
String phone,
String relativeName,
String relativePhone,
String email

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
    "status":0
}
```

## /User/logout

登出，由于清除session的方法有延时，请前端用跳转并重置到需要登录的状态帮忙掩护一下,登出成功返回0。

req:

res:

## /User/delete

删除当前用户。删除成功为0。

req:

res:

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
以下若说明为measures列表，则其结构为：
```JSON
{
    "measures":[
                    {
                        "accountNumber":"002",
                        "commitTime":1492659233000,
                        "device":"BE-83-85-13-EB-1D",
                        "step":386,"distance":10,
                        "heart":70,
                        "id":1
                    },
                    {
                        "accountNumber":"002",
                        "commitTime":1492661406000,
                        "device":"BC-83-85-13-EA-1C",
                        "step":429,"distance":11,
                        "heart":65,
                        "id":2
                    }
                ],
    "status":0
}
```
## /Measure/getAll

获取当前用户的所有数据，按commitTime正序排序，若获取成功为0。

req:

res:
- 名为measures的measure对象列表。

## /Measure/getAllByTimeRange

获取当前用户某段时间的所有数据，按commitTime正序排序，若获取成功为0。

req:
- Timestamp fromTime
- Timestamp toTime

res:
- 名为measures的measure对象列表。

## /Measure/getAllFromTime

获取当前用户从某个时间至今的所有数据，按commitTime正序排序，若获取成功为0。

req:
- Timestamp fromTime

res:
- 名为measures的measure对象列表。

## /Measure/getLatest

获取当前用户的最新一条信息,获取成功为0。

req:

res:
- 名为measure的measure对象。

## /Measure/getToday

获取当前用户今日的所有数据，按commitTime正序排序，若获取成功为0。

req:

res:
- 名为measures的measure对象列表。

## /Measure/getByDate

获取当前用户某日的所有数据，按commitTime正序排序，若获取成功为0。

req:
- Timestamp timestamp

res:
- 名为measures的measure对象列表。

## /Measure/getEachDateLatestByDateRange

获取当前用户某段日期中的每天的最后的数据，按commitTime正序排序，若获取成功为0。

req:
- Timestamp fromTime
- Timestamp toTime

res:
- 名为measures的measure对象列表。