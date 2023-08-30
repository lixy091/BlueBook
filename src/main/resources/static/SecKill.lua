local stockKey = 'kill:stock:' .. ARGV[1]
local orderKey = 'order:user:' .. ARGV[1]

--判断库存是否足够
if tonumber(redis.call('get' , stockKey)) <= 0 then
    return 1
end
--判断用户是否下过单
if redis.call('sismember' , orderKey , ARGV[2]) == 1 then
    return 2
end
--库存减1
redis.call("incrby" , stockKey ,-1)
--将用户加入订单列表中
redis.call("sadd" , orderKey , ARGV[2])
--添加信息到消息队列
--redis.call("xadd" , "stream.orders" , "*", "voucherId" , ARGV[1] , "userId" , ARGV[2] , "id" , ARGV[3])
return 0