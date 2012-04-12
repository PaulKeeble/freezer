package freezer.serialisers

class NoDefaultConstructorException(msg:String) extends Exception(msg) {
  def this() = this(null)
}