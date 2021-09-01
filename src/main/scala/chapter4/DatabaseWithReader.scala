package chapter4

import cats.data.Reader

final case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
)

object DatabaseWithReader { 
    type DbReader[A] = Reader[Db, A]

    def findUsername(userId: Int): DbReader[Option[String]] = 
        Reader( db => db.usernames.get(userId) )

    def checkPassword(username: String, password: String): DbReader[Boolean] = 
        Reader( db => db.passwords.get(username).map( pass => pass == password).get ) 

    def checkLogin(userId: Int, password: String): DbReader[Boolean] = 
        for { 
            user <- findUsername(userId)
            logStatus <- checkPassword(user.get, password)
        } yield logStatus

}