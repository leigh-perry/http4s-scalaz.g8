package $package$.extapi.service

import scalaz.concurrent.Task

trait CrudService[K, V] {
  type KeyType = K
  type ValueType = V

  def create(row: V): Task[V]
  def readAll(): Task[Seq[V]]
  def read(id: K): Task[Seq[V]]
  def delete(id: String): Task[Unit]
  def update(row: V): Task[Unit]

}
