Effortlessly identify target audience for advertising

# 1 init adujst sdk
```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Adser.onCreate(this, "adjust app token");
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Adser.onTerminate(this);
    }
}
```

# 2 process result
``` java
Adser.CORE.get().observe(this, attribution -> {
  if (attribution != null) {
    if(Adser.CORE.isAdser()){
      // 
    } else {
      // 
    }
  } else {
    // init ing... 
    // show loading...
  }
});
```
