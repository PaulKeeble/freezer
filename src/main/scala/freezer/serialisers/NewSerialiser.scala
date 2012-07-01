package freezer.serialisers

trait NewSerialiser[-T] extends Function1[T,Array[Byte]]

object NewSerialiser {
    implicit def f1ToNewSerialiser[T](f : (T) => Array[Byte]) = new NewSerialiser[T] {
    def apply(v : T) : Array[Byte] = {
      f(v)
    }
  }
}