package $package$.extapi.service

import $package$.extapi.Config
import $package$.extapi.model.Model._
import $package$.extapi.store.Store

import scalaz.concurrent.Task

class ReadService[K, V](cfg: Config, repo: Store[K, V]) extends CrudService[K, V] {

  override def create(row: V): Task[V] = {
    Task.fail(ApiNotImplemented("create"))
  }

  override def read(id: K): Task[Seq[V]] = {
    repo.read(id)
  }

  override def readAll(): Task[Seq[V]] = {
    repo.readAll()
  }

  override def delete(id: String): Task[Unit] = {
    Task.fail(ApiNotImplemented("delete"))
  }

  override def update(row: V): Task[Unit] = {
    Task.fail(ApiNotImplemented("update"))
  }

}
