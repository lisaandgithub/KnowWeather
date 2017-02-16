# KnowWeather
知道天气，知道生活。我的 这个项目基本实现查询天气预报的功能，是一款良心App.
我所做的优化-在WeatherActivity中自己实现了bing每日一图的获取，并没有直接利用郭神封装好的接口！
其实这部分实现也是很简单的，原理如下：
1.我通过百度了解到bing的网页地址为：http://cn.bing.com;
bing每日一图的网页源码地址为：http://cn.bing.com/HPImageArchive.aspx?idx=0&n=1，我们访问这个网址得到的是图片xml文件；
2.打开网址后找到<url>标签，该标签里的内容就是图片的url，我使用pull解析将<url>标签解析出来
3.结合前面两步我们就能得到每日一图的链接地址：http://cn.bing.com+图片的url；知道链接地址，就可以很轻松的将它加载进入我们的控件！
