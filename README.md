1 easy 

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
