# oauth2 用户账户授权认证服务

## 目标功能

### 1. 独立的用户管理，权限管理，角色管理模块（用户权限以角色区分）

### 2. 提供三种三方授权：客户端授权，密码授权，授权码授权

## 设计概要

- tokenStore 使用 `RedisTokenStore`
- 客户端以及用户认证 client 信息 均使用 数据库存储
- 返回 jwt token

## 认证模式支持

- 客户端认证 `client_credentials`
````
http://localhost:8888/oauth/token?grant_type=client_credentials&scope=web-app&client_id=internal&client_secret=internal
````

- 密码认证 `password`

````
http://localhost:8888/oauth/token?username=admin&password=123456&grant_type=password&scope=select&client_id=web_app&client_secret=internal
````

- 刷新token `refresh_token`

````
http://localhost:8888/oauth/token?refresh_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXIiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJzZWxlY3QiXSwiYXRpIjoiZmQ0ZmQ0NzctMjIwNi00MDA1LWE3NmYtZDhiMjkxNDRlYmI2IiwiZXhwIjoxNTc0MDQ0OTI2LCJpYXQiOjE1NzM0NDAxMjYsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiYzFjYzlmMmQtMDBlNS00ZjFjLThhOWYtMDc4MDE3ZjA5YWQxIiwiY2xpZW50X2lkIjoid2ViX2FwcCJ9.VT0fsHMc3EJ7nSgkJrubG3jskrxb1sBNYHXOhEVhL1Iry_HKGp8r1yBMQbd2hOUnwGDU9ml5FttgGL3YN9o0COAxmJB2VqES3k7Inx81pC9d_4tyiYUS7COMdgKfDIWEfeYbaJJkozdI-ncWcHoEDI4tehNW_rKWYht_YoQvM5rGDFnQxbl4zjL-DDGQElpBor2gn7axazGA0-kwELY4IJsVxf_sZqrRLkF9AbP1qB4gxd2ggM3CoBlcdvc10XUnIivNae0YRopc5T1ai4SucQXQjDa9Kt8g_7HHnUqoDNZYParKzcz5Y5O8H4gsubHnuDTcilp5LdbsaIgnmYXPgg&grant_type=refresh_token&scope=select&client_id=web_app&client_secret=internal
````