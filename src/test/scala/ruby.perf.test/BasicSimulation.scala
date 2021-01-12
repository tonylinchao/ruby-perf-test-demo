package ruby.perf.test

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080/fusedemo") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // MuleSoft VPC test
  val scn_vpc = scenario("Test MuleSoft VPC").repeat(10) {  // millisecond  .repeat(100)  during(1000 * 60)
    exec(http("MuleSoft VPC")
      .get("/vpc/test").check(status is 200)
    )
  } 


  // setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))

  setUp(
    scn_vpc.inject(
      //nothingFor(4.seconds), // 设置一段停止的时间
      atOnceUsers(10), // 立即注入一定数量的虚拟用户
      rampUsers(10).during(1.seconds), // 在指定时间内，设置一定数量逐步注入的虚拟用户
      //constantUsersPerSec(20).during(15.seconds), // 定义一个在每秒钟恒定的并发用户数，持续指定的时间
      //constantUsersPerSec(20).during(15.seconds).randomized, // 定义一个在每秒钟围绕指定并发数随机增减的并发，持续指定时间
      //rampUsersPerSec(10).to(20).during(10.minutes), // 定义一个并发数区间，运行指定时间，并发增长的周期是一个规律的值
      //rampUsersPerSec(10).to(20).during(10.minutes).randomized, // 定义一个并发数区间，运行指定时间，并发增长的周期是一个随机的值
      //heavisideUsers(20).during(20.seconds) // 定义一个持续的并发，围绕和海维赛德函数平滑逼近的增长量，持续指定时间
    ).protocols(httpProtocol)
  )

}
