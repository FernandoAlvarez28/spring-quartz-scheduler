# spring-quartz-scheduler
Small Spring Boot project to learn and test how to schedule tasks with [Quartz](http://quartz-scheduler.org/) and
[H2 Database Engine](https://www.h2database.com).

## Introduction
With Spring we can create automated tasks using [@Scheduled](https://spring.io/guides/gs/scheduling-tasks/), passing an
interval or cron to schedule and/or repeat these tasks however we want. For example, one task could send e-mails about
expiring invoices everyday at 2am. This task would need to access data from somewere (like an database) and could even
save more information like logs.

This works pretty well, but what will happen if you launch another instance of the same application? The scheduled task
would run twice at the same time and probably lead to data inconsistency, among other things.

This is where Quartz comes in, allowing to launch multiple instances and "split" the tasks executions between applications
without concurrency and redundant executions.

## Context
To learn, test and implement Quartz, I needed to create some tasks, and for that I invented a "fake store" to allow a user
(we developers) to "buy" (create) "fake orders". These orders have some (fake) steps to follow:

Purchase -> Payment confirmation -> Dispatch -> Delivery

These steps could be implemented with scheduled tasks: to fetch information from X service every Y minutes to check if the
payment was made or if the logistics department shipped the order, for example.

As this store is fake, these steps are also fakes and don't actually happen, but are here to illustrate the usage of tasks.
When a user order something from this fake store, there are 3 [tasks (Jobs)](#The tasks/jobs) that will run (within Quartz) to
simply update the order's status.

## Project configuration
This project is divided in 3 Maven modules:
- [quartz-scheduler-core](./quartz-scheduler-core/pom.xml): Core module with the common entities, services, repositories
and dependencies;
- [quartz-scheduler-web](./quartz-scheduler-web/pom.xml): Application with Spring Web and Thymeleaf to provide a front-end
for the user to make these fake orders. Listen the 8080 port and use the core module;
- [quartz-scheduler-executor](./quartz-scheduler-executor/pom.xml): Application with Spring and Quartz to run these scheduled
tasks. Can run in multiples instances and also use the core module.

Both applications are in this same repository and uses the [H2 Database Engine](https://www.h2database.com) as database,
so there is no database setup needed as it runs in memory and the project creates all the necessary tables. Although any
changes or data saved to this database will be lost when the Web application is stopped or restarted.

## Quartz configuration
To achieve this goal, Quartz need to use a JDBC JobStore, which saves the jobs and its triggers in database. The
first application instance that acquires the lock will execute the job, while the other instances may executer other jobs.

The following configuration is present on [executor/application.properties](./quartz-scheduler-executor/src/main/resources/application.properties):
```properties
spring.quartz.job-store-type=jdbc
# Setting to "always" will drop and recreate Quartz tables, but will not follow the
# "spring.quartz.properties.org.quartz.jobStore.tablePrefix" property.
spring.quartz.jdbc.initialize-schema=never

# Can be any name you want
spring.quartz.properties.org.quartz.scheduler.instanceName=QuartzSchedulerExecutor
spring.quartz.properties.org.quartz.scheduler.instanceId=AUTO

spring.quartz.properties.org.quartz.threadPool.class=org.quartz.simpl.SimpleThreadPool
# Spring's @Scheduled runs one task at a time by default, so I leaved one thread here too
spring.quartz.properties.org.quartz.threadPool.threadCount=1

spring.quartz.properties.org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
spring.quartz.properties.org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
# This "quartzDataSource" datasource is defined in the next block;
# Can be any name, but it must be defined in "spring.quartz.properties.org.quartz.dataSource.[yourDataSourceName]"
# or you can omit this property for Quartz to use the same "spring.datasource.*" properties
spring.quartz.properties.org.quartz.jobStore.dataSource=quartzDataSource
# Prefix for the Quartz's tables
spring.quartz.properties.org.quartz.jobStore.tablePrefix=QUARTZ_
# I honestly don't know what this property does
spring.quartz.properties.org.quartz.jobStore.useProperties=false
# For clustering to work properly
spring.quartz.properties.org.quartz.jobStore.isClustered=true

# DataSource config for Quartz. It's the same as defined in "spring.datasource.*" but you can specify something for Quartz
# Or just don't configure DataSource for Quartz and it will use the application's datasource
spring.quartz.properties.org.quartz.dataSource.quartzDataSource.driver=org.h2.Driver
spring.quartz.properties.org.quartz.dataSource.quartzDataSource.URL=jdbc:h2:tcp://localhost:9090/mem:quartzschedulerapplication
spring.quartz.properties.org.quartz.dataSource.quartzDataSource.user=sa
spring.quartz.properties.org.quartz.dataSource.quartzDataSource.password=password
spring.quartz.properties.org.quartz.dataSource.quartzDataSource.maxConnections=5
# Connection Pool for Quartz; default is c3p0, which may not be present in the classpath (at least for me):
# https://github.com/quartz-scheduler/quartz/wiki/How-to-Use-DB-Connection-Pool
spring.quartz.properties.org.quartz.dataSource.quartzDataSource.provider=hikaricp

# Prints when Jobs start and finish
spring.quartz.properties.org.quartz.plugin.triggerHistory.class=org.quartz.plugins.history.LoggingTriggerHistoryPlugin
```

The Quartz's tables must be created. Here they are defined in [init.sql](./quartz-scheduler-core/src/main/resources/scripts/init.sql)
and with the same prefix as defined in `spring.quartz.properties.org.quartz.jobStore.tablePrefix` property. The H2 is
configured to run this script on start up:
```properties
spring.datasource.url=jdbc:h2:mem:quartzschedulerapplication;INIT=RUNSCRIPT FROM 'classpath:scripts/init.sql'
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
```

The tables must exists when you run Quartz with JDBCJobStore. While this worked for this project with H2, real life applications
and/or other Databases may need different approaches, like manually running this script once or using something automated like
[Flyway](https://flywaydb.org) or [Liquibase](https://www.liquibase.org).

## How to run
1. Clone this repository and open it on your favorite Java IDE (like [IntelliJ](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/downloads/));
2. Run Maven to import all dependencies;
3. Start the [Web application (QuartzSchedulerWebApplication)](./quartz-scheduler-web/src/main/java/alvarez/fernando/quartzscheduler/web/QuartzSchedulerWebApplication.java)
first, because it initializes, runs and exposes the H2 Database to the Executor application. After started, you can access
the fake store on [localhost:8080](http://localhost:8080) and the database on [localhost:8080/h2-console](http://localhost:8080/h2-console).
4. Start many [Executor applications (QuartzSchedulerExecutorApplication)](./quartz-scheduler-executor/src/main/java/alvarez/fernando/quartzscheduler/executor/QuartzSchedulerExecutorApplication.java)
you'd like. They will access the H2 Database exposed by the Web application to schedule and execute the [Jobs](#The tasks/jobs).
5. Access the fake store on [localhost:8080](http://localhost:8080) and "purchase" any number you want;
6. See the Executor application's log to see the [tasks](#The tasks/jobs)'s executions.

The Web application will create and save these [FakeOrders](./quartz-scheduler-core/src/main/java/alvarez/fernando/quartzscheduler/core/fakeorder/FakeOrder.java),
and then the Executor application will move these orders through the steps mentioned above.

## The tasks/jobs
The Executor application schedules the following tasks:
- [FakeOrderPaymentConfirmationJob](./quartz-scheduler-executor/src/main/java/alvarez/fernando/quartzscheduler/executor/job/FakeOrderPaymentConfirmationJob.java) -
Runs every 30 seconds, fetching all `NEW` `FakeOrder`s and have a 65% chance to update their status to `PAID` instead of `EXPIRED`;
- [FakeOrderDispatchJob](./quartz-scheduler-executor/src/main/java/alvarez/fernando/quartzscheduler/executor/job/FakeOrderDispatchJob.java) -
Runs every 2 minutes, fetching all `PAID` `FakeOrder`s and updating their status to `SENT`;
- [FakeOrderDeliveryTrackingJob](./quartz-scheduler-executor/src/main/java/alvarez/fernando/quartzscheduler/executor/job/FakeOrderDeliveryTrackingJob.java) -
Runs every 3 minutes, fetching all `SENT` `FakeOrder`s and have a 55% chance to update their status to `DELIVERED`;

### "Notification"
Every status changes creates a "Notification". This may represent an e-mail, SMS, push notification, or anything that needs
to be executed after a status change and that can take some time. Every saved Notification sleeps the current thread by
250 milliseconds, making some tasks to take a long time to execute. This is fully intentional in this project to test concurrency.
Think of the following situation:
1. The `FakeOrderPaymentConfirmationJob` runs every 30 second and load 400 FakeOrders once;
2. It will update the 400 FakeOrders's status to `PAID` and then will create a Notification for each FakeOrder;
3. Each Notification takes 250 miliseconds to be sent (it's just a `Thread.sleep()` here but could be an API integration),
so to complete this Job execution will take 100 seconds;
4. The Job runs at every 30 seconds. So while one application instance is busy executing this Job, the other instances could
get the same Job to execute? Depending on how it's implemented, this can lead to some problems and may not be desired.

With this situation I managed to test the concurrency, as described in the [next section](#concurrency).

## Other notes
### Concurrency
To prevent the same Job to execute twice at the same time, you need to configure Quartz with JDBC as [described before](#quartz-configuration).
With this, the Jobs will not be triggered by two or more applications, but they can still be executed more than once as [exemplified
before](#notification). To fix this, you need to anotate the jobs with `@DisallowConcurrentExecution`:
```java
import org.quartz.DisallowConcurrentExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class MyJob extends QuartzJobBean {
    //...
}
```

You also should set the `spring.quartz.properties.org.quartz.jobStore.isClustered` property to `true` and provide
`instanceName` and `instanceId` to better control the Job's concurrency:
```properties
spring.quartz.properties.org.quartz.scheduler.instanceName=SomeName
spring.quartz.properties.org.quartz.scheduler.instanceId=AUTO

spring.quartz.properties.org.quartz.jobStore.isClustered=true
```

### Dependency Injection
Anotating the Jobs with `@Component` or making then Beans is unnecessary. Quartz will create a new instance for
each execution, but with Spring Boot Quartz Starter it still can inject any dependency you specify in the constructor
or with the `@Autowired` annotation:
```java
public class MyJob extends QuartzJobBean {
	
	private final MyService myService;
	
	public MyJob(MyService myService) {
		this.myService = myService;
	}
	
	//...
}
```
In this project I used [Lombok](https://projectlombok.org) to generate the constructors but the final code is something
like this example above.

### Unscheduling Jobs
By using Quartz with JDBC, any Job you schedule will be saved in the database. If you don't drop or truncate the tables,
you only need to schedule the first time; then you could even remove the scheduling logic and Quartz will still execute your Jobs,
because it's information and triggers are already saved in the database.

With this in mind, you can't simply remove your Job (and its scheduler) from the source code to completely remove it from
your application. Your logic may not be there, but Quartz may still try to instantiate the Job's class,
leading to unecessary errors and/or maybe other problems.

One way to solve this is dropping or truncating the tables at every start, but I don't know how this would work with
multiples instances. Maybe this is the way that Quartz is intended to be used, but I didn't thought and researched much about this.

You will need to think about how to deal with this. Eventually I'll need too.

### Structure
To schedule Jobs you need to use the `org.quartz.Scheduler` and its methods `scheduleJob` or `scheduleJobs`. This class
can be injected by Spring, and right after using its methods to schedule your jobs they can be triggered. So in my opinion
it's better to schedule after your application is started. But how you organize these schedules it's up to you.

I created the class [JobConfig](./quartz-scheduler-executor/src/main/java/alvarez/fernando/quartzscheduler/executor/job/configuration/JobConfig.java)
to receive a List of [JobScheduler](./quartz-scheduler-executor/src/main/java/alvarez/fernando/quartzscheduler/executor/job/configuration/JobScheduler.java)s.
These Schedulers creates a pair of `JobDetail` and `Trigger`, which `JobConfig` will use when the application is fully started.
To schedule more Jobs, I only need to implement a new `JobScheduler` implementation.

The `JobScheduler` in its turn must provide its [JobIdentification](./quartz-scheduler-executor/src/main/java/alvarez/fernando/quartzscheduler/executor/job/configuration/JobIdentification.java),
which can be an Enum with the Job's class, group and description. Could have the Cron too, if the application will only
use Cron for Jobs.

I don't know if this is the best way to organize the Jobs and it's schedulers, but it worked well for me in this project.
