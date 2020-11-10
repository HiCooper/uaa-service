# oauth2 用户账户授权认证服务

## 设计概要

- tokenStore 使用 `RedisTokenStore`
- 客户端以及用户认证 client 信息 均使用 数据库存储
- 返回 jwt token

## 认证模式支持

- 客户端认证 `client_credentials`
````
curl --location --request POST 'http://localhost:8888/oauth/token' \
--header 'Cookie: JSESSIONID=3JoVsF0eFm6v6AqfP0wj_gnpS8ytaKDVZya7EQDQ' \
--form 'grant_type=client_credentials' \
--form 'scope=web-app' \
--form 'client_id=internal' \
--form 'client_secret=internal'
````

- 密码认证 `password`

````
curl --location --request POST 'http://localhost:8888/oauth/token' \
--header 'Cookie: JSESSIONID=UzzwLzrDGicsZTRCRv2P4v5_pkVR8bw1ePkJWxW6' \
--form 'username=admin' \
--form 'password=123456' \
--form 'grant_type=password' \
--form 'scope=select' \
--form 'client_id=web_app' \
--form 'client_secret=internal'
````

- 刷新token `refresh_token`

````
curl --location --request POST 'http://localhost:8888/oauth/token' \
--header 'Cookie: JSESSIONID=81bu4dm_9itXWWzImxC-NHcTDJ1nIu3vAD07YhWe' \
--form 'refresh_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXIiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJzZWxlY3QiXSwiYXRpIjoiYjQyOWI2ODctMTk5MC00YzZiLTg4MDgtMzE2ZTBlZDE1NWVlIiwiZXhwIjoxNjA0OTkyNjk1LCJpYXQiOjE2MDQ5OTIwOTUsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMDdlMWRhODUtYjdhNC00ZjQ1LWJiZGEtOGZlOTEwNjQ2NGQ1IiwiY2xpZW50X2lkIjoid2ViX2FwcCJ9.fO4fGDkRAzyw9NG5AXJ8ZZkDYLCTQppkHLdYdo6mrxBOMztiNRqn37fG6F7sTAjy_WwDc8-51L5JJkZpTi8YHIpW-a5knIDspnYUL1Pun9kfm1sWUJzLn2qqX73ePKQn5Xf1S5emrH_UT1k0_e_nnI-7n6ZQSWAv3OhhbmLwtJFUf5SRQ6jeLJdX3nIQM7mu11wtLbDBegUeULBmclicGtOqBd5YCr8rzje-1iTbwu5Y3jYWUfYkOyO5LkJswY2gz4SdjtJmmWTeQDcRP6CVJ4aY_fwwKPmrXspQwLn1uWOwk5ZHGxOvdU6Pg23QmeNzCvXL91Pu1upt6-vKWiXSuQ' \
--form 'grant_type=refresh_token' \
--form 'scope=select' \
--form 'client_id=web_app' \
--form 'client_secret=internal'
````

- 授权码认证

1. 获取 code

````
# 浏览器访问
http://localhost:8888/oauth/authorize?response_type=code&client_id=au_code&redirect_uri=http://baidu.com&scope=select
````

2. 授权后，重定向 url 将会 附带一个 参数 code=xxx, 如下示例：code=Qyyjp0
根据 code 获取 access_token

````
curl --location --request POST 'http://localhost:8888/oauth/token' \
--header 'Cookie: JSESSIONID=dBLnj2FO2qhSKwkmty9X-MyDcV9Jp-03CO1hNGGg' \
--form 'grant_type=authorization_code' \
--form 'scope=select' \
--form 'client_id=au_code' \
--form 'client_secret=internal' \
--form 'code=Qyyjp0' \
--form 'redirect_uri=http://baidu.com'
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
curl --location --request GET 'http://localhost:8888/api/resources/detail?username=admin' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXIiXSwic2NvcGUiOlsid2ViLWFwcCJdLCJleHAiOjE2MDQ5OTMwNzYsImlhdCI6MTYwNDk5Mjc3NiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9JTk5FUl9TRVJWSUNFIl0sImp0aSI6ImVkNjUzNzNmLWMwYmYtNGJjYS1iNTBjLWU0ODczNjIzZDY1NyIsImNsaWVudF9pZCI6ImludGVybmFsIn0.kag5iLeMXsf2-p7ffK601qP3VwLMhZfFHmGPy6vBVoh4hIN0IVFMrcTSQSxOcLPzLzusSX2gfIH-rVyV89BypSVDB07bewBED7Cd1Wpp5stbPKbUFj2oprz0ccOLX0Lwn0cG09RS8Xl2BdU6eScWHTQ_KGfkykd0kx9MddV7QGGy9o0d4A6vaFSsuy47p5YzvXZuXPJA5HaFxJ4U3wHAAac7VeL-AYhcN8Fe5icSxai6_5zDb5cDc6qEIodiwFWroLLUwKxWYAeGoJdgsywqMTY3XSabemt9PEzOiyJsF_LWbS0ewscTwtJwUgMR6sY2hhdJ2wyFd5BEsZsGcInqKw'
````

返回用户信息示例：
````json
{
    "username": "admin",
    "nickName": "管理员",
    "email": "admin@email.com"
}
````