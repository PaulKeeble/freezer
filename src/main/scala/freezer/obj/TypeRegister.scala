package freezer.obj
import scala.collection.mutable.LinkedHashSet
import scala.collection.mutable.SynchronizedSet
import freezer.collection.OrderedConcurrentSet

class TypeRegister extends OrderedConcurrentSet[Class[_]]