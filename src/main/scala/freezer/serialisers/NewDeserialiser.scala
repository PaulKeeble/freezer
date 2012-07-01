package freezer.serialisers
import freezer.collection.ArrayView

trait NewDeserialiser[+T] extends Function1[ArrayView[Byte],(T,ArrayView[Byte])]

object NewDeserialiser {
  implicit def f1ToNewDeserialiser[T](f: (ArrayView[Byte]) => (T,ArrayView[Byte])) = new NewDeserialiser[T] {
    def apply(a : ArrayView[Byte]) : (T,ArrayView[Byte]) = {
      f(a)
    }
  }
}