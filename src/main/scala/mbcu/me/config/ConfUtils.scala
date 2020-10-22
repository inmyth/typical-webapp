package mbcu.me.config

import awscala.Region
import awscala.s3.Bucket
import com.typesafe.config.ConfigValueType
import pureconfig.error.{FailureReason, WrongType}
import pureconfig.{ConfigCursor, ConfigReader}

import scala.util.{Failure, Success, Try}

object ConfUtils {

  implicit val awsS3BucketConferter = new ConfigReader[Bucket] {
    def from(cur: ConfigCursor) = {
      cur.asObjectCursor.map(s => {
        val x = s.objValue.get("name").unwrapped().asInstanceOf[String]
        Bucket(x)
      })
    }
  }

  case class AWSRegionUnrecognized(value: String) extends FailureReason {
    override def description: String = s"""Unrecognized AWS Region: $value"""
  }

  implicit val awsRegionConferter = ConfigReader.fromCursor[Region] { cur =>
    cur.asConfigValue.flatMap { s =>
      {
        val region = s.unwrapped().asInstanceOf[String]
        Try(Region(region)) match {
          case Success(n) if n != null => Right(n)
          case Success(_)              => cur.failed(AWSRegionUnrecognized(region))
          case Failure(_)              => cur.failed(WrongType(ConfigValueType.STRING, Set(ConfigValueType.STRING)))
        }
      }

    }
  }

//  implicit val awsRegionConferter = new ConfigReader[Region] {
//    def from(cur: ConfigCursor) = {
//      cur.asConfigValue.map(s => {
//        val x = s.unwrapped().asInstanceOf[String]
//        Try(Option(Region(x)).get) match {
//          case Success(value) => value
//          case Failure(_)     => throw new IllegalArgumentException("AWS Region not recognized")
//        }
//      })
//    }
//  }

}
