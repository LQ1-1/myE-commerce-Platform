以我这台惠普电脑为后端服务器,后端服务器监听8081端口

惠普电脑在GalaxyS下的ip地址是192.168.66.94

API

# **登录:api/Login_Manual or ** /api/Login_RequestBody

返回字符串，

密码错误返回"Wrong Password";

账号不存在返回"No such Account"

账号处于封禁状态返回"Banned"

账号处于注销状态返回"Deleted"

账号密码正确，账号类型是普通用户返回"RegularUser",进入购物界面

账号密码正确，账号类型是商家用户返回"Merchant"，进入商家界面

账号密码正确，账号类型是管理员用户返回"Administrator",进入管理员界面



http://192.168.66.94:8081/api/Login_Manual手动请求参数:格式形如

{         http://localhost:8081/api/Login_Manual?uID=1522788291&uPassword=qh20050908)     }

账户变量名必须是uID,密码变量名必须是uPassword,密码必须先经过SHA256哈希之后再提交给后端服务器



http://192.168.66.94:8081/api/Login_RequestBody参数是json形式,通过请求体传递json格式

json文件中账号键值对名必须是uID，密码键值对名必须是uPassword密码必须先经过SHA256哈希之后再提交给后端服务器



# 注册:/api/Registration

返回字符串

插入成功返回"RegistrationSuccess"

插入失败则返回报错信息，账户已经存在返回"AccountAlreadyExist"

插入失败则返回报错信息，某一个属性太长则返回已经存在返回"ValuetooLargeForColumn"

参数也是json形式,通过请求体传递json格式.

json文件中

账号键值名uID

昵称键值名uNickName

密码键值名uPassword

电话键值名uPhone

邮箱键值名uEmail

性别键值名uGender

注册日期键值名uRegisterDate

账号类型键值名uAccountType

账号状态键值名uAccountStatus