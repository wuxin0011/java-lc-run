


# About
This is a small tool that automatically crawls the weekly competition, which can be easily run locally, without processing input and output, and at the same time can customise the daily question and custom classified questions for easy review.

# install

```xml
<dependency>
    <groupId>io.github.wuxin0011</groupId>
    <artifactId>java-lc-run</artifactId>
    <version>0.0.1-beta</version>
</dependency>
```
[last version](https://central.sonatype.com/artifact/io.github.wuxin0011/java-lc-run)

# Example


## contest


### custom week contest [ LCContest.WEEK_CONTEST](./src/main/java/code_generation/crwal/leetcode/LCContest.java)
 
```java
public class CustomWeekContest {
    public static void main(String[] args) {
        LCContest.WEEK_CONTEST.createNo(CustomWeekContest.class);
    }
}
```



### custom bi week contest [ LCContest.BI_WEEK_CONTEST](./src/main/java/code_generation/crwal/leetcode/LCContest.java)

```java
public class CustomBIWeekContest {

    public static void main(String[] args) {
        LCContest.BI_WEEK_CONTEST.createNo(CustomBIWeekContest.class);
    }
}
```


### [Next](./src/main/java/code_generation/crwal/leetcode/LCContest.java)



```java
public class Next {

    public static void main(String[] args) {
        LCContest.autoCreateNext(Next.class);
    }


}
```

### [custom contest](./src/main/java/code_generation/contest/Problem.java)

```java
public class OtherContest {

    public static void main(String[] args) {
        Problem.customContest(OtherContest.class);
    }
}
```


## Every day

### [custom day](./src/main/java/code_generation/crwal/leetcode/LCEveryDay.java)

```java
public class LCDailyCustom {


    public static void main(String[] args) {
        LCEveryDay.everyDay.custom(LCDailyCustom.class);
    }
}
```

### [today](./src/main/java/code_generation/crwal/leetcode/LCEveryDay.java)



```java
public class LCDailyCustom {


    public static void main(String[] args) {
        LCEveryDay.everyDay.custom(LCDailyCustom.class);
    }
}
```

## custom problem dir

### [custom dir](./src/main/java/code_generation/crwal/leetcode/LCSolutionTemplate.java)




```java
public class SolutionTest {

    public static void main(String[] args) {

        new LCSolutionTemplate(SolutionTest.class, "Solution").run();
    }
}
```


# Thanks

Thanks [JetBrains](https://www.jetbrains.com/?from=py-lc-run) for the open source license provided
