# oauth2 用户账户授权认证服务

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

- 授权码认证

1. 获取 code

````
# GET
http://localhost:8888/oauth/authorize?response_type=code&client_id=au_code&redirect_uri=http://baidu.com&scope=select
````

2. 授权后，重定向 url 将会 附带一个 参数 code=xxx, 如下示例：code=dmwJI4
根据 code 获取 access_token

````
# POST
http://localhost:8888/oauth/token?grant_type=authorization_code&scope=select&client_id=au_code&client_secret=internal&code=dmwJI4&redirect_uri=http://baidu.com
````
返回示例：
````json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidXNlci1yZWNvdXJjZSIsIm9yZGVyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2VsZWN0Il0sImV4cCI6MTU3MzcxODI5OCwiaWF0IjoxNTczNzE3OTk4LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImp0aSI6ImM1M2UzNTVhLWRkOGEtNDllNy04N2I5LTQwMzdiYTk2M2VlMyIsImNsaWVudF9pZCI6ImF1X2NvZGUifQ.UWRaxkRaw6EuM8oFGZzb0i6yz_BlzsfO48Dsexsz9nDv-brrIcGAo6xBQFcdsshlR0QHrByViDMHiq092eoYq2CtOMLpVEbwt67OMnDns0-BHAp0U1ytIBMogoMYaZCRMzTl8-IyHyTmdBfIp0O70W3cZhLaFR_Zo2-U0Q9g14Hra3BdK2Rxh_KU51swk-Fd9mLc8NbJVwhv_PPDsXaX3gWp64_YF8PxA1_S5uC_Cu6tIhnADJh9Ko37oIr5oHk18Qahcyn7q1xwF8afXBDDx7e17egAbv1IFNi5kpFpsknUMzwyGVwkIalHKev2inUUC7I7jJUlc0fQUkXs5i7U7g",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidXNlci1yZWNvdXJjZSIsIm9yZGVyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2VsZWN0Il0sImF0aSI6ImM1M2UzNTVhLWRkOGEtNDllNy04N2I5LTQwMzdiYTk2M2VlMyIsImV4cCI6MTU3NDMyMjc5OCwiaWF0IjoxNTczNzE3OTk4LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImp0aSI6IjA1ZmMzNjEyLTg3MWMtNDhjZi1iNzRlLTY5OWZiMGZlYzI4ZCIsImNsaWVudF9pZCI6ImF1X2NvZGUifQ.EvEVy3aBfm2agcxVK-TGSFJyvg_hds8l9P7UnbYgJ9oPtAtRaovL1vYXzFAeHx56n4_c9snP1of2Dkfj6JfqyhGRc7pisPSgCBxYlWQjLJnVbKETuldsAQW97KZFFCs9BokSMuOCVSesBRZTkcxNJQAufmYHH_xvsUledrTMoEPwdnz0WTzyuPi7b23jt-mTXbw_gcb-Yr5npI5PpxVPxGZwGAX4QK9whQ2hITtm6H0GwtYiwZ1dxhfRLgFYSbzSc6rmOpH6FOxLlyYW6IQTghT7Yk-Gs959ihMaBb2Ux2sH9GH9IAwCV00gHCK3Z2BA5RaWk8k-0clAe58jhYag4Q",
    "expires_in": 299,
    "scope": "select",
    "iat": 1573717998,
    "jti": "c53e355a-dd8a-49e7-87b9-4037ba963ee3"
}
````

- 使用获取的 access_token 获取受保护的  用户资源 '/api/resources/detail'

````bash
# GET
# head [{"key":"access_token","value":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJ3ZWItYXBwIl0sImV4cCI6MTU3MzE3OTgxMywiaWF0IjoxNTczMTc4MDEzLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6ImMzZDU5ZDY4LThlNDEtNGEyYi05MTRhLTgzOGUzYzNjZGJkMiIsImNsaWVudF9pZCI6ImludGVybmFsIn0.Eg5v9kicy0CPwOGCqqNcpiITVScvhWv10SPusU9ntXirdTIMiXZAnXoQMPT0ja4JBtTYGJJzaAEBYZcoedXyg7yVxp1jVnWg4gSe3Kvl2IsXQdk0mSMkyLfk_DxIloahWlaP1_bhrDdcNYrQwAlk-qWaM4RsEDCpN-g3V1wPylwVcOjshH64c7BKocvTYzM3hNjs7J_Uk9BrZcPIVQyX5gyDNUM199FfW0ipKDFGHfncs4fGdpSj1G19MZ_zHVxQ81LQgeJ9iQITPVZs3F0hpESH0X4mtylbesxprS1BMTA-G01M1YqYAMwU6GKHsLNQzzwQBZCcZNHqFeklVIcU7Q","description":"","type":"text","enabled":true}]
http://localhost:8888/api/resources/detail?username=admin
````

返回用户信息示例：
````json
{
    "username": "admin",
    "nickName": "管理员",
    "email": "admin@email.com"
}
````