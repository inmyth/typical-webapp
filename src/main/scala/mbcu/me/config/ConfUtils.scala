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
      cur.asConfigValue.map(s => Bucket(s.unwrapped().asInstanceOf[String]))
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

}
