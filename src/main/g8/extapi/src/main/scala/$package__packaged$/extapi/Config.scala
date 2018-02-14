package $package$.extapi

import $package$.shared.Newtype._


final case class DatabaseConfig(
  driver: DbDriver,
  url: DbUrl,
  user: DbUser,
  password: DbPassword,
  maxPoolSize: DbMaxPoolSize = DbMaxPoolSize(8)
)

final case class Config(restHttpPort: PortNumber, db: DatabaseConfig)
object Config {
  val msSqlServer = DbDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver")
  val defaultPort = PortNumber(6789)

  val baseConfig =
    Config(
      defaultPort,
      DatabaseConfig(
        msSqlServer,
        DbUrl("configure-this"),
        DbUser("configure-this"),
        DbPassword("configure-this")
      )
    )
}


