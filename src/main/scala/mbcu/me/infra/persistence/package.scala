package mbcu.me.infra

package object persistence {

  type FakeUserDb = persistence.UserInMemoryDB
  val FakeUserDb = persistence.UserInMemoryDB

}
