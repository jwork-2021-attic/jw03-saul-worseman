# 第三次JAVA作业

## 1.代码工作原理

### 关于自定义的类加载器 

代码工作的核心原理在于自定义的类加载器。

注意到我们`SteganographyClassLoader`是继承了`ClassLoader`，并且只`override`了一个`findClass`方法,现在暂时没有用到，先看`loadClass`做了什么.

```java
protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);

                  ......
            return c;
        }
    }
```

这是我截取的一部分`ClassLoader`的源码(JDK 11,略去以及记录状态和决议部分)，可以看到，我们会先检查是不是这个类已经被加载到内存中了，显然本例是没有的，然后从`parent ClassLoader[ExtClassLoader]`中加载,如果没有加载到，则从`Bootstrap ClassLoader`中尝试加载(`findBootstrapClassOrNull`方法)，显然在本例中这两条路都是走不通的，所以我们的自定义类最后走到了`findClass`方法。可是F12点进去一看



```java
protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
```

`ClassLoader`的`findClass`只是简单地抛出一个异常，这时我们想到我们唯一`Override`的那个方法，`findclass`



```java
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            BufferedImage img = ImageIO.read(url);

            SteganographyEncoder encoder = new SteganographyEncoder(img);
            byte[] bytes = encoder.decodeByteArray();
            return this.defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }

    }
```

可以看到，该方法从encoder解析出字节码，然后和名字等其他必要参数一起传给`defineClass`



```java
static native Class<?> defineClass1(ClassLoader loader, String name, byte[] b, int off, int len,
                                        ProtectionDomain pd, String source);

```

沿着调用链往下走，最后会发现走到defineClass1，但是在这个文件怎么也找不到其定义，因为native关键字说明其修饰的方法是一个原生态方法，方法对应的实现不是在当前文件，而是在用其他语言（如C和C++）实现的文件中。所以我们的溯源到此为止，然后类就Load成功了。



最后调用`c.newInstance `可以根据参数进行初始化并返回实例.

## 2.两个图片的URL

![](https://i.loli.net/2021/10/04/O4FeRcsWohuXUty.png)

快速排序：

`/home/saul/jw03-saul-worseman/example/resources/example.QuickSorter.png`

URL:https://i.loli.net/2021/10/04/O4FeRcsWohuXUty.png

选择排序：

`/home/saul/jw03-saul-worseman/example/resources/example.SelectSorter.png`

URL:https://i.loli.net/2021/10/04/2IusdZr6Hv7Azp4.png

二者均在本机验证成功。但因为网络原因，不保证URL一定可用，起码我是挂在外面。

### 3.排序结果录屏

选择排序

[![asciicast](https://asciinema.org/a/NbNSpS9rsGlMncI6aHshPZg10.svg)](https://asciinema.org/a/NbNSpS9rsGlMncI6aHshPZg10)

快速排序

[![asciicast](https://asciinema.org/a/wyRA2MJkDB6FZUzPpea7SlIev.svg)](https://asciinema.org/a/wyRA2MJkDB6FZUzPpea7SlIev)

### 4.使用他人图片

本机使用刘睿哲的图片（感谢其在QQ群的分享），验证结果正确。

[![asciicast](https://asciinema.org/a/IoB6AESZh9ySlDUoLKjLwFi79.svg)](https://asciinema.org/a/IoB6AESZh9ySlDUoLKjLwFi79)