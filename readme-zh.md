[English](./readme.md) | [简体中文](./readme-zh.md)

# 关于本项目

这是一个自动爬取LeetCode周赛题目的小工具，可以方便地在本地运行，无需处理输入输出，同时可以定制每日一题和自定义分类题目，便于复习。

# 安装

```xml
<dependency>
    <groupId>io.github.wuxin0011</groupId>
    <artifactId>java-lc-run</artifactId>
    <version>0.0.4-beta</version>
</dependency>
```

[最新版本](https://central.sonatype.com/artifact/io.github.wuxin0011/java-lc-run)

# 使用示例

## 示例仓库

首次使用前请配置您的LeetCode网站cookies！

您可以从[这里🚀](https://github.com/wuxin0011/java-lc-run-example)下载使用示例

也可以通过Maven进行配置

# 使用说明

## 自定义配置

如需自定义配置，请在项目resources目录下创建`config.properties`文件：



```properties
# 项目启动根目录
root_dir=[src,main,java]
# cookie配置目录，"."表示工作目录
request_config=.
# 若获取用户名失败，将使用此默认用户名
username=wuxin0011
# 是否创建周赛README文件
create_contest_readme=true
```

## 周赛功能

### 自定义周赛 [LCContest.WEEK_CONTEST](./src/main/java/code_generation/crwal/leetcode/LCContest.java)



``` java
public class CustomWeekContest {
    public static void main(String[] args) {
        LCContest.WEEK_CONTEST.createNo(CustomWeekContest.class);
    }
}
```

### 自定义双周赛 [LCContest.BI_WEEK_CONTEST](./src/main/java/code_generation/crwal/leetcode/LCContest.java)



```java
public class CustomBIWeekContest {
    public static void main(String[] args) {
        LCContest.BI_WEEK_CONTEST.createNo(CustomBIWeekContest.class);
    }
}
```

### 自动创建下一场比赛 [Next](./src/main/java/code_generation/crwal/leetcode/LCContest.java)



``` java
public class Next {
    public static void main(String[] args) {
        LCContest.autoCreateNext(Next.class);
    }
}
```

### 自定义比赛 [Problem](./src/main/java/code_generation/contest/Problem.java)



``` java
public class OtherContest {
    public static void main(String[] args) {
        Problem.customContest(OtherContest.class);
    }
}
```

## 每日一题

### 自定义每日一题 [LCEveryDay](./src/main/java/code_generation/crwal/leetcode/LCEveryDay.java)



``` java
public class LCDailyCustom {
    public static void main(String[] args) {
        LCEveryDay.everyDay.custom(LCDailyCustom.class);
    }
}
```

### 今日题目 [LCEveryDay](./src/main/java/code_generation/crwal/leetcode/LCEveryDay.java)



``` java
public class LCDailyToday {
    public static void main(String[] args) {
        LCEveryDay.everyDay.today(LCDailyToday.class);
    }
}
```

## 自定义题目目录

### 自定义题目目录 [LCSolutionTemplate](./src/main/java/code_generation/crwal/leetcode/LCSolutionTemplate.java)



```java

public class SolutionTest {
    public static void main(String[] args) {
        new LCSolutionTemplate(SolutionTest.class, "Solution").run();
    }
}
```

# 致谢

感谢[JetBrains](https://www.jetbrains.com/?from=py-lc-run)提供的开源许可证支持



