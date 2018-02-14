package $package$.extapi.store

import scalaz.concurrent.Task

trait Store[K, V] {
  type KeyType = K
  type ValueType = V

  def read(id: K): Task[Seq[V]]
  def readAll(): Task[Seq[V]]

}
