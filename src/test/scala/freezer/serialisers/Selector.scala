package freezer.serialisers

trait Node

trait Selector[String, Node,T] extends Function1[String, NewDeserialiser[T]] {
  def apply(s: String) : NewDeserialiser[T] = {
    
  }
}

trait LeafSelector[T] extends Selector[T, NewDeserialiser]

object LeafSelector {
  implicit def function12LeafSelector[T](f: (String)=> NewDeserialiser[T]) = new LeafSelector[T] {
    def apply(t: String) = f(t)
  }
}

object composeTest {
  import LeafSelector.function12LeafSelector
  
  def select: LeafSelector[AnyVal] = {
    val f = (t: String) => {
      t match {
        case "byte" => Primitives.deserialiseByte
        case "int" => Primitives.deserialiseInt
      }
    }
    function12LeafSelector(f)
  }
}