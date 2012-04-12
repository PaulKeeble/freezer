package freezer.obj
import scala.collection.mutable.LinkedHashSet
import scala.collection.mutable.SynchronizedSet

class TypeRegister extends OrderedConcurrentSet[Class[_]]