### Sử dụng Redis để lưu trữ TokenRedis lưu dưới dạng cặp giá trị <UserID, AccessToken, RefreshToken>.
Quản lý trạng thái token trong thời gian thực.
- RedisFilter: Kiểm tra Token trên mỗi request
    + Mỗi request sẽ đi qua RedisFilter để kiểm tra:AccessToken có tồn tại trong Redis không.
    + Đảm bảo token còn hợp lệ và chưa bị thu hồi.
- OAuth2 Resource Server: Xác thực và phân quyền
    + Sau khi vượt qua RedisFilter, OAuth2ResourceServer sẽ:Giải mã JWT (decode AccessToken) để lấy thông tin người dùng.
    + Add Authorities để xác thực quyền (Authorization).
- Cơ chế Refresh Token
    + Khi RefreshToken được sử dụng, hệ thống sẽ: Tăng "Experience" cho AccessToken và RefreshToken.
    + Tạo cặp AccessToken và RefreshToken mới, cập nhật Redis.

## JWT (Json Web Token)



