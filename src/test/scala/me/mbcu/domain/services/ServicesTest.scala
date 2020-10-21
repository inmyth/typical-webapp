package me.mbcu.domain.services

import mbcu.me.config.Config.ExecutorsConfig.ComputationScheduler
import mbcu.me.config.Config.{Config, EnvConfig, ExecutorsConfig, Services}
import mbcu.me.domain.models.usermanagement
import mbcu.me.domain.models.usermanagement.User
import mbcu.me.domain.shared.Done
import org.scalatest.flatspec.AsyncFlatSpec

class ServicesTest extends AsyncFlatSpec {

//  val config =
//    Config(
//      EnvConfig(failureProbability = 1.0, fakeS3Config),
//      ExecutorsConfig(ComputationScheduler(1)),
//      Services.UNSTABLE
//    )
//  implicit val ec = config.executorsConfig.computationScheduler.ec
//
//  val services = Services.fromConfig.run(config)
//  val userId   = User.Id(1)
//  val userName = User.UserName("martin")
//  val user     = usermanagement.User(userId, userName)
//
//  behavior of "Default Repository"
//
//  it should "works 100% on fakedb" in {
//    val task = services.certivManagement.insert(user)
//    task.runToFuture.map(p => assert(p === Done))
//  }

}
