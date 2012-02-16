package freezer.obj
import scala.collection.mutable.LinkedHashSet
import scala.collection.mutable.SynchronizedSet

class TypeRegister extends LinkedHashSet[Class[_]] with SynchronizedSet[Class[_]]{

}