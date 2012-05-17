package freezer

object IO {
  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
  
  def printToFile(name: String) : Function[(java.io.PrintWriter=>Unit),Unit] = printToFile(new java.io.File(name))
}