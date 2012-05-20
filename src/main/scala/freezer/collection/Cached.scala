package freezer.collection

class Cached[T](f: => T) extends Function0[T] {
  private var cached :Option[T] = None
  
  def apply() : T = {
    synchronized {
      if(cached == None) cached = Some(f)
      cached.get
    }
  }
  
  def reset() {
    synchronized {
      cached = None
    }
  }
}