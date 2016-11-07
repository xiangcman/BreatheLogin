最近在看Drakeet的博客的时候，看到一篇不错的文章，介绍的是一种呼吸灯的生成。看了代码后，顿时觉得这种思路挺不错的，虽然实现方式很普通，但是能实现这种样式，能体现了图像算法的魅力所在。先看下实现的样本图吧:
![simple.gif](https://github.com/1002326270xc/BreatheLogin/blob/master/photo/simple.gif)

大家看到样本图后，首先觉得是文本框的背景色一直发生变化的呢。对的，这里的颜色值是一直不断发生变化来实现的，其实也不是颜色值发生变化的，只是改变颜色值的apha值而已，下面来看看这个apha是遵循什么图像变化的吧:

![图像.png](http://upload-images.jianshu.io/upload_images/2528336-6eb4570ebf3297bc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![函数.png](http://upload-images.jianshu.io/upload_images/2528336-0754e7ea1a150610.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

其实这里的apha值就是按照上面那个抛物线的轨迹来发生改变的，
大家可以去代码中看到有该函数的实现:
<pre><code>
private static double getBreathingY(long time, int n, int t) {   
    float k = 1.0f / 3;   
    float pi = 3.1415f;  
    float x = time / 1000.0f;   
    t = (int) (t / 1000.0f);  
    if (x >= ((n - 1) * t) && x < ((n - (1 - k)) * t)) {        
       double i = pi / (k * t) * ((x - (0.5f * k * t)) - (n - 1) * t);     
       return 0.5f * Math.sin(i) + 0.5f; 
    } else if (x >= ((n - (1 - k)) * t) && x < n * t) {  
       double j = pi / ((1 - k) * t) * ((x - (0.5f * (3 - k) * t)) - (n - 1) * t);        
       double one = 0.5f * Math.sin(j) + 0.5f;     
       return one * one;   
    }  
    return 0;
}
</code></pre>

理解了这个函数的实现，基本上就get所有的点了，其实也没什么好说的了，其余的动画实现也是android基本动画实现。大家可以直接去看看代码是如何实现的吧。

###关于我
   - email: a1002326270@163.com
   - 简书: http://www.jianshu.com/users/7b186b7247c1/latest_articles
