# 公共说明

- 所有API均会返回status属性，值为0表示成功，通常值为1表示失败（若有多种失败情况会特殊说明）。以下文档中不再说明。
- 所有API的request内容都会在response中。
- 除不需要参数的方法（可为POST或GET）外，所有方法均为POST方法。
- req为必须请求参数，creq为可选请求参数，res为返回参数。

# 用户管理部分

## /User/login

通过用户名和密码进行登录。登录成功为0

req:
- String accountNumber
- String password

res:

## /User/check

通过用户名检查数据库中是否有该用户，没有该用户为0

req:
- String accountNumber

res:

## /User/join

向数据库添加用户，添加成功为0,用户已存在为1，其他原因添加失败为2

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

更改当前用户数据，更新成功为0

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

显示当前用户的用户数据。成功为0

req:

res:
- 一个名为user的列表，其中表项为:
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

登出，由于清除session的方法有延时，请前端用跳转并重置到需要登录的状态帮忙掩护一下。

req:

res:

## /User/delete

删除当前用户。删除成功为0

req:

res: